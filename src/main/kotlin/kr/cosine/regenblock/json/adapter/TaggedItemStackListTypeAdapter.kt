package kr.cosine.regenblock.json.adapter

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kr.cosine.regenblock.data.TaggedItemStack
import kr.cosine.regenblock.util.ItemStackSerializer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class TaggedItemStackListTypeAdapter<L : List<E>, E : TaggedItemStack>(
    itemStacksClazz: KClass<L>,
    private val itemStackClazz: KClass<E>
) : RegenBlockTypeAdapter<L>(itemStacksClazz) {
    final override fun read(jsonReader: JsonReader): L {
        return jsonReader.nextString()
            .run(ItemStackSerializer::deserializeToArray)
            .map { itemStackClazz.primaryConstructor!!.call(it) }
            .toMutableList()
            .run { clazz.kotlin.primaryConstructor!!.call(this) }
    }

    final override fun write(jsonWriter: JsonWriter, taggedItemStacks: L) {
        val itemStacks = taggedItemStacks.map { it.toItemStack() }
        val compressed = ItemStackSerializer.serialize(itemStacks)
        jsonWriter.value(compressed)
    }
}