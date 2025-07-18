package kr.cosine.regenblock.json.adapter

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kr.cosine.regenblock.data.ItemStackWrapper
import kr.cosine.regenblock.util.ItemStackSerializer

object ItemStackWrapperTypeAdapter : RegenBlockTypeAdapter<ItemStackWrapper>(ItemStackWrapper::class) {
    override fun read(jsonReader: JsonReader): ItemStackWrapper {
        return jsonReader.nextString()
            .run(ItemStackSerializer::deserialize)
            .run(::ItemStackWrapper)
    }

    override fun write(jsonWriter: JsonWriter, itemStackWrapper: ItemStackWrapper) {
        val compressed = itemStackWrapper.toString()
        jsonWriter.value(compressed)
    }
}