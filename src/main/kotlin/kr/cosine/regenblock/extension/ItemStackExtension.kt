package kr.cosine.regenblock.extension

import com.google.common.collect.ImmutableMultiset
import com.google.gson.Gson
import kr.cosine.regenblock.data.ItemStackWrapper
import kr.cosine.regenblock.data.Tag
import org.bukkit.inventory.ItemStack

fun ItemStack.getTag(): Tag {
    return Tag(itemMeta?.persistentDataContainer)
}

fun ItemStack.getCustomTag(gson: Gson): Tag {
    return Tag(itemMeta?.persistentDataContainer, gson)
}

fun ItemStack.tag(block: Tag.() -> Unit): ItemStack {
    itemMeta = itemMeta?.apply {
        Tag(persistentDataContainer).block()
    }
    return this
}

fun ItemStack.customTag(gson: Gson, block: Tag.() -> Unit): ItemStack {
    itemMeta = itemMeta?.apply {
        Tag(persistentDataContainer, gson).block()
    }
    return this
}

fun ItemStack.toItemStackWrapper(): ItemStackWrapper {
    return ItemStackWrapper(this)
}

fun List<ItemStack>.toMap(): Map<ItemStack, Int> {
    val materialMap = mutableMapOf<ItemStack, Int>()
    forEach {
        val material = it.clone().apply { this.amount = 1 }
        materialMap[material] = materialMap.getOrDefault(material, 0) + it.amount
    }
    return materialMap
}

fun Map<ItemStack, Int>.toSplitedList(): List<ItemStack> {
    val newItemStacks = mutableListOf<ItemStack>()
    for ((itemStack, amount) in this) {
        val maxStackSize = itemStack.maxStackSize
        var remaining = amount
        while (remaining > 0) {
            val stackAmount = remaining.coerceAtMost(maxStackSize)
            val newItemStack = itemStack.clone().apply { this.amount = stackAmount }
            newItemStacks.add(newItemStack)
            remaining -= stackAmount
        }
    }
    return newItemStacks
}

fun ItemStack.toSplitedList(): List<ItemStack> {
    return listOf(this).toMap().toSplitedList()
}

fun List<ItemStack>.isEqual(itemStacks: List<ItemStack>): Boolean {
    return ImmutableMultiset.copyOf(this) == ImmutableMultiset.copyOf(itemStacks)
}