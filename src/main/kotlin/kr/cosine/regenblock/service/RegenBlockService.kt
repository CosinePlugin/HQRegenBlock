package kr.cosine.regenblock.service

import kr.cosine.regenblock.configuration.RegenBlockConfiguration
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
import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player

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
        val world = block.world
        world.dropItemNaturally(location, result.clone())

        if (ore.isChance(ChanceType.EXTRA_DROP)) {
            val extraDropCount = ore.getExtraDropCount()
            val extraDrop = result.clone().apply {
                amount = extraDropCount
            }
            world.dropItemNaturally(location, extraDrop)
            NotificationType.EXTRA_DROP.notice(player) {
                it.replace(NotificationType.ITEM_REPLACER, result.getDisplayName())
                    .replace(NotificationType.AMOUNT_REPLACER, extraDropCount.format())
            }
        }
        if (ore.isChance(ChanceType.BONUS_DROP)) {
            val bonusDrop = ore.findDrop(DropType.BONUS)?.clone()
            if (bonusDrop != null) {
                world.dropItemNaturally(location, bonusDrop)
                NotificationType.BONUS_DROP.notice(player) {
                    it.replace(NotificationType.ITEM_REPLACER, bonusDrop.getDisplayName())
                        .replace(NotificationType.AMOUNT_REPLACER, bonusDrop.amount.format())
                }
            }
        }

        val regenBlockQueue = RegenBlockQueue(location, nextOreMaterial, regenBlockConfiguration.regenDuration)
        regenBlockQueueRegistry.addRegenBlockQueue(regenBlockQueue)
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