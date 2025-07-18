package kr.cosine.regenblock.extension

import com.google.gson.GsonBuilder
import kr.cosine.regenblock.json.adapter.ItemStackWrapperTypeAdapter
import kr.cosine.regenblock.json.adapter.RegenBlockTypeAdapter

fun RegenBlockGsonBuilder(vararg regenBlockTypeAdapters: RegenBlockTypeAdapter<*>): GsonBuilder {
    return GsonBuilder()
        .setPrettyPrinting()
        .enableComplexMapKeySerialization()
        .disableHtmlEscaping()
        .registerTypeAdapters(
            ItemStackWrapperTypeAdapter,
            *regenBlockTypeAdapters
        )
}

fun GsonBuilder.registerTypeAdapters(vararg regenBlockTypeAdapters: RegenBlockTypeAdapter<*>): GsonBuilder {
    regenBlockTypeAdapters.forEach { regenBlockTypeAdapter ->
        registerTypeAdapter(regenBlockTypeAdapter.clazz, regenBlockTypeAdapter)
    }
    return this
}