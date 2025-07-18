package kr.cosine.regenblock.data

import org.bukkit.inventory.ItemStack

data class Drop(
    private val itemStacks: List<ItemStack> = emptyList()
) : List<ItemStack> by itemStacks