package kr.cosine.regenblock.data

interface Key {
    val parent: String

    val child: String

    val key get() = "$parent.$child"
}