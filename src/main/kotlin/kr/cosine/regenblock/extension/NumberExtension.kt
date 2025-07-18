package kr.cosine.regenblock.extension

import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#,###")

fun Double.format(): String {
    return decimalFormat.format(this)
}

fun Int.format(): String {
    return decimalFormat.format(this)
}

fun Long.format(): String {
    return decimalFormat.format(this)
}