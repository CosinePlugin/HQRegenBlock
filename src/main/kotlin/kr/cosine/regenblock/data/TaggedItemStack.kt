package kr.cosine.regenblock.data

import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.inventory.ItemStack

interface TaggedItemStack {
    fun toItemStack(): ItemStack

    fun getDisplayName(): String {
        return toItemStack().getDisplayName()
    }
}