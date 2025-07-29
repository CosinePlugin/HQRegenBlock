package kr.cosine.regenblock.enumeration

import kr.cosine.regenblock.notification.NotificationType

enum class ChanceType(
    val displayName: String
) {
    REGEN("리젠"),
    EXTRA_DROP("추가 드랍"),
    BONUS_DROP("보너스 드랍");

    val notificationType = NotificationType.of(name)
}