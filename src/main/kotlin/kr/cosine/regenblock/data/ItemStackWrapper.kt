package kr.cosine.regenblock.data

import kr.cosine.regenblock.util.ItemStackSerializer
import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.inventory.ItemStack

data class ItemStackWrapper(
    val itemStack: ItemStack
) {
    val itemMeta get() = itemStack.itemMeta

    val material get() = itemStack.type

    val amount get() = itemStack.amount

    fun getDisplayName(): String {
        return itemStack.getDisplayName()
    }

    fun clone(): ItemStack {
        return itemStack.clone()
    }

    fun isSimilar(itemStack: ItemStack): Boolean {
        return this.itemStack.isSimilar(itemStack)
    }

    override fun toString(): String {
        return ItemStackSerializer.serialize(itemStack)
    }
}