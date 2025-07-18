package kr.cosine.regenblock.extension

import java.util.concurrent.ThreadLocalRandom
import kotlin.math.ln

fun Double.chance(): Boolean {
    return when {
        this >= 100.0 -> true
        this <= 0.0 -> false
        else -> {
            val successful = this
            val fail = 100.0 - successful
            mapOf(true to successful, false to fail).random()
        }
    }
}

fun Int.chance(): Boolean {
    return toDouble().chance()
}

fun <T> Map<T, Double>.random(): T {
    return randomOrNull() ?: throw IllegalArgumentException("Map이 비어있습니다.")
}

fun <T> Map<T, Double>.randomOrNull(): T? {
    return entries.minByOrNull {
        -ln(ThreadLocalRandom.current().nextDouble()) / it.value
    }?.key
}