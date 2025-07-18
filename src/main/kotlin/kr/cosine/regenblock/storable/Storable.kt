package kr.cosine.regenblock.storable

import java.io.File

abstract class Storable(
    path: String
) {
    protected var file = File(path)

    abstract fun load()

    open fun forceLoad() {}

    abstract fun save(): Boolean
}