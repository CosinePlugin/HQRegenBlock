package kr.cosine.regenblock.util

import kr.hqservice.framework.bukkit.core.extension.toByteArray
import kr.hqservice.framework.bukkit.core.extension.toItemArray
import kr.hqservice.framework.bukkit.core.extension.toItemStack
import org.bukkit.inventory.ItemStack
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

object ItemStackSerializer {
    fun serialize(itemStack: ItemStack): String {
        return itemStack.toByteArray().run(Base64Coder::encodeLines)
    }

    fun serialize(itemStacks: List<ItemStack>): String {
        return itemStacks
            .toTypedArray<ItemStack?>()
            .toByteArray()
            .run(Base64Coder::encodeLines)
    }

    fun deserialize(compressed: String): ItemStack {
        return Base64Coder.decodeLines(compressed).toItemStack()
    }

    fun deserializeToArray(compressed: String): Array<ItemStack> {
        return compressed
            .run(Base64Coder::decodeLines)
            .toItemArray()
    }

    fun deserializeToList(compressed: String): List<ItemStack> {
        return deserializeToArray(compressed).toList()
    }
}