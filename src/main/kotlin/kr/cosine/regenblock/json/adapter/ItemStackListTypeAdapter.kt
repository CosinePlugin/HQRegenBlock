package kr.cosine.regenblock.json.adapter

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kr.cosine.regenblock.util.ItemStackSerializer
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class ItemStackListTypeAdapter<T : List<ItemStack>>(
    clazz: KClass<T>
) : RegenBlockTypeAdapter<T>(clazz) {
    final override fun read(jsonReader: JsonReader): T {
        return jsonReader.nextString()
            .run(ItemStackSerializer::deserializeToList)
            .run { clazz.kotlin.primaryConstructor!!.call(this) }
    }

    final override fun write(jsonWriter: JsonWriter, itemStacks: T) {
        val compressed = ItemStackSerializer.serialize(itemStacks)
        jsonWriter.value(compressed)
    }
}