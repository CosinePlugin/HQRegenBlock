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
import kr.hqservice.framework.inventory.util.hasSpace
import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

@Service
class RegenBlockService(
    private val regenBlockConfiguration: RegenBlockConfiguration,
    private val regenBlockRegionRegistry: RegenBlockRegionRegistry,
    private val oreGroupRegistry: OreGroupRegistry,
    private val regenBlockQueueRegistry: RegenBlockQueueRegistry
) {
    sealed interface RegenBlockResult {
        object InvalidRegenBlock : RegenBlockResult
        object Fail : RegenBlockResult
        object Success : RegenBlockResult
    }

    fun regen(player: Player, block: Block): RegenBlockResult {
        val location = block.location
        val regenBlockRegion = findRegenBlockRegion(location) ?: return RegenBlockResult.InvalidRegenBlock
        val oreHolderKey = regenBlockRegion.findOreGroupKey() ?: return RegenBlockResult.InvalidRegenBlock
        val oreHolder = oreGroupRegistry.findOreGroup(oreHolderKey) ?: return RegenBlockResult.InvalidRegenBlock
        val ore = oreHolder[block.type] ?: return RegenBlockResult.Fail
        val result = ore.findDrop(DropType.DEFAULT)
        if (result == null) {
            player.sendMessage("§c광물이 설정되어 있지 않습니다. 관리자에게 문의해주세요.")
            return RegenBlockResult.Fail
        }
        val nextOreMaterial = oreHolder.randomOrNull() ?: run {
            player.sendMessage("§c광석이 설정되어 있지 않습니다. 관리자에게 문의해주세요.")
            return RegenBlockResult.Fail
        }
        // 올바른 도구로 캤을 때만
        val itemStack = player.inventory.itemInMainHand
        if (!player.isOp && block.getDrops(itemStack, player).isEmpty()) {
            NotificationType.IMPOSSIBLE_TOOL.notice(player)
            return RegenBlockResult.Fail
        }
        val experienceRange = ore.findExperienceRange()
        if (experienceRange != null && ore.isChance(ChanceType.EXPERIENCE_DROP)) {
            val experience = experienceRange.random()
            location.spawnExperience(experience)
        }
        val defaultResult = result.clone().apply {
            if (ore.isFortuneEnabled()) {
                amount += itemStack.getBonusAmountByFortune()
            }
        }
        if (!player.giveOrDrop(location, defaultResult)) return RegenBlockResult.Fail

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
        return RegenBlockResult.Success
    }

    private fun Location.spawnExperience(experience: Int) {
        if (experience > 0) {
            world?.spawn(this, ExperienceOrb::class.java) {
                it.experience = experience
            }
        }
    }

    private fun ItemStack.getBonusAmountByFortune(): Int {
        val fortune = getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)
        if (fortune == 0) return 0
        val roll = Random.nextInt(fortune + 2) - 1
        val bonus = if (roll < 0) 0 else roll
        return bonus
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