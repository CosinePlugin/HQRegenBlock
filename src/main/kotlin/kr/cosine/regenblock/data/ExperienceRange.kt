package kr.cosine.regenblock.data

data class ExperienceRange(
    val min: Int,
    val max: Int
) {
    fun random(): Int {
        return (min..max).random()
    }
}