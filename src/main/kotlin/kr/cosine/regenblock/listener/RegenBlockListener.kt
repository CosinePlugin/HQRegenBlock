package kr.cosine.regenblock.listener

import kr.cosine.regenblock.enumeration.PositionType
import kr.cosine.regenblock.extension.isRightClick
import kr.cosine.regenblock.service.RegenBlockRegionService
import kr.cosine.regenblock.service.RegenBlockService
import kr.hqservice.framework.bukkit.core.listener.HandleOrder
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot

@Listener
class RegenBlockListener(
    private val regenBlockService: RegenBlockService,
    private val regenBlockRegionService: RegenBlockRegionService
) {
    @Subscribe(ignoreCancelled = true)
    fun onBlockBreakInRegion(event: BlockBreakEvent) {
        val player = event.player
        val isRegen = regenBlockService.regen(player, event.block) ?: return
        if (!player.isOp && !isRegen) {
            event.isCancelled = true
        }
    }

    @Subscribe(HandleOrder.LAST)
    fun onBlockPlaceInRegion(event: BlockPlaceEvent) {
        if (!event.player.isOp && regenBlockService.isRegenBlockRegion(event.block.location)) {
            event.isCancelled = true
        }
    }

    @Subscribe(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        if (player.isOp && regenBlockRegionService.setPosition(player, PositionType.FIRST, event.block.location)) {
            event.isCancelled = true
        }
    }

    @Subscribe(ignoreCancelled = true)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND || !event.action.isRightClick) return
        val block = event.clickedBlock ?: return
        val player = event.player
        if (player.isOp && regenBlockRegionService.setPosition(player, PositionType.SECOND, block.location)) {
            event.isCancelled = true
        }
    }

    @Subscribe
    fun onPlayerQuit(event: PlayerQuitEvent) {
        regenBlockRegionService.removePosition(event.player)
    }
}