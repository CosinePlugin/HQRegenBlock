package kr.cosine.regenblock.service

import kr.cosine.regenblock.configuration.RegenBlockConfiguration
import kr.cosine.regenblock.data.Ore
import kr.cosine.regenblock.data.RegenBlockQueue
import kr.cosine.regenblock.data.RegenBlockRegion
import kr.cosine.regenblock.enumeration.ChanceType
import kr.cosine.regenblock.enumeration.DropType
import kr.cosine.regenblock.extension.format
import kr.cosine.regenblock.notification.NotificationType
import kr.cosine.regenblock.registry.OreGroupRegistry
import kr.cosine.regenblock.registry.RegenBlockQueueRegistry
import kr.cosine.regenblock.registry.RegenBlockRegionRegistry
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.inventory.util.hasSpace
import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Service
class RegenBlockService(
    private val regenBlockConfiguration: RegenBlockConfiguration,
    private val regenBlockRegionRegistry: RegenBlockRegionRegistry,
    private val oreGroupRegistry: OreGroupRegistry,
    private val regenBlockQueueRegistry: RegenBlockQueueRegistry
) {
    fun regen(player: Player, block: Block): Boolean? {
        val location = block.location
        val regenBlockRegion = findRegenBlockRegion(location) ?: return null
        val oreHolderKey = regenBlockRegion.findOreGroupKey() ?: return null
        val oreHolder = oreGroupRegistry.findOreGroup(oreHolderKey) ?: return null
        val ore = oreHolder[block.type] ?: return false
        val result = ore.findDrop(DropType.DEFAULT)
        if (result == null) {
            player.sendMessage("§c광물이 설정되어 있지 않습니다. 관리자에게 문의해주세요.")
            return false
        }
        val nextOreMaterial = oreHolder.randomOrNull() ?: run {
            player.sendMessage("§c광석이 설정되어 있지 않습니다. 관리자에게 문의해주세요.")
            return false
        }
        // 올바른 도구로 캤을 때만
        val itemStack = player.inventory.itemInMainHand
        if (!player.isOp && block.getDrops(itemStack, player).isEmpty()) {
            NotificationType.IMPOSSIBLE_TOOL.notice(player)
            return false
        }
        if (!player.giveOrDrop(location, result.clone())) return false

        fun drop(chanceType: ChanceType, itemStack: ItemStack) {
            if (ore.isChance(chanceType)) {
                player.giveOrDrop(location, itemStack)
                chanceType.notificationType?.notice(player) {
                    it.replace(NotificationType.ITEM_REPLACER, itemStack.getDisplayName())
                        .replace(NotificationType.AMOUNT_REPLACER, itemStack.amount.format())
                }
            }
        }

        if (ore.hasExtraDropCount()) {
            val extraDrop = result.clone().apply {
                amount = ore.getExtraDropCount()
            }
            drop(ChanceType.EXTRA_DROP, extraDrop)
        }
        val bonusDrop = ore.findDrop(DropType.BONUS)
        if (bonusDrop != null) {
            drop(ChanceType.BONUS_DROP, bonusDrop.clone())
        }

        val regenBlockQueue = RegenBlockQueue(location, nextOreMaterial, regenBlockConfiguration.regenDuration)
        regenBlockQueueRegistry.addRegenBlockQueue(regenBlockQueue)
        return true
    }


    private fun Player.giveOrDrop(location: Location, itemStack: ItemStack): Boolean {
        if (regenBlockConfiguration.inventoryGive) {
            if (!inventory.hasSpace(itemStack)) {
                NotificationType.LACK_INVENTORY_SPACE.notice(this)
                return false
            }
            inventory.addItem(itemStack)
        } else {
            world.dropItemNaturally(location, itemStack)
        }
        return true
    }

    fun isRegenBlockRegion(location: Location): Boolean {
        return findRegenBlockRegion(location) != null
    }

    private fun findRegenBlockRegion(location: Location): RegenBlockRegion? {
        return regenBlockRegionRegistry.getRegenBlockRegions().find {
            it.contains(location)
        }
    }
}