package kr.cosine.regenblock.extension

fun listsOf(vararg list: List<String>): List<String> {
    return list.flatMap { it }
}