package kr.cosine.regenblock.view.impl

import kr.cosine.regenblock.enumeration.DropType
import kr.cosine.regenblock.extension.getDisplayName
import kr.cosine.regenblock.view.Container
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class DropSettingView(
    material: Material,
    dropType: DropType,
    private val drop: ItemStack?,
    private val onClose: (Player) -> Unit,
    private val onChange: (ItemStack?) -> Unit
) : Container(9, "${material.getDisplayName()} ${dropType.displayName} 드랍 설정", false) {
    override fun onCreate() {
        drawDrop()
        drawBackground()
    }

    private fun drawDrop() {
        inventory.setItem(ITEM_SLOT, drop)
    }

    private fun drawBackground() {
        val background = HQButtonBuilder(Material.BARRIER).setDisplayName("§f").build()
        for (slot in 0 until size) {
            if (slot != ITEM_SLOT) {
                background.setSlot(this, slot)
            }
        }
    }

    override fun onDestroy(event: InventoryCloseEvent) {
        val player = event.player as Player
        val drop = event.inventory.getItem(ITEM_SLOT)
        if (this.drop != drop) {
            onChange(drop)
        }
        onClose(player)
    }

    private companion object {
        const val ITEM_SLOT = 4
    }
}