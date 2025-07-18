package kr.cosine.regenblock.extension

import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun Material.getDisplayName(): String {
    return ItemStack(this).getDisplayName()
}