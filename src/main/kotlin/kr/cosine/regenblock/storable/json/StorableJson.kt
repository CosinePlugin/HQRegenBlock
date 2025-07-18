package kr.cosine.regenblock.storable.json

import com.google.gson.Gson
import kr.cosine.regenblock.extension.RegenBlockGsonBuilder
import kr.cosine.regenblock.json.adapter.RegenBlockTypeAdapter
import kr.cosine.regenblock.storable.Storable
import java.io.File

abstract class StorableJson<T : Restorable<T>>(
    private val path: String,
    private val restorable: T,
    vararg mczoneTypeAdapters: RegenBlockTypeAdapter<*>
) : Storable(path) {
    protected open var gson: Gson = RegenBlockGsonBuilder(*mczoneTypeAdapters).create()

    override fun load() {
        if (!file.exists()) return
        val registry = gson.fromJson(file.bufferedReader(), restorable::class.java)
        this.restorable.restore(registry)
    }

    override fun forceLoad() {
        restorable.isChanged = false
        file = File(path)
        load()
    }

    override fun save(): Boolean {
        if (restorable.isChanged) {
            restorable.isChanged = false
            val json = gson.toJson(restorable)
            file.bufferedWriter().use {
                it.appendLine(json)
                it.flush()
            }
            return true
        }
        return false
    }
}