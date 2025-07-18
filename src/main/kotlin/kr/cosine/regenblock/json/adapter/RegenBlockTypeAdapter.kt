package kr.cosine.regenblock.json.adapter

import com.google.gson.TypeAdapter
import kotlin.reflect.KClass

abstract class RegenBlockTypeAdapter<T : Any>(
    val clazz: Class<T>
) : TypeAdapter<T>() {
    constructor(clazz: KClass<T>) : this(clazz.java)
}