package kr.cosine.regenblock.extension

val IntRange.radius get() = (last - first) / 2

val IntRange.center get() = first + radius

fun IntRange.isIntersect(range: IntRange): Boolean {
    return intersect(range).isNotEmpty()
}