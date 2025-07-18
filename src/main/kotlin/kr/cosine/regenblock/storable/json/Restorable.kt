package kr.cosine.regenblock.storable.json

interface Restorable<T : Any> {
    var isChanged: Boolean

    fun restore(restorable: T)
}