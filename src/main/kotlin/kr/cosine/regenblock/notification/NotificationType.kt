package kr.cosine.regenblock.notification

import org.bukkit.entity.Player

enum class NotificationType(
    private var notifications: MutableList<Notification> = mutableListOf()
) {
    IMPOSSIBLE_TOOL,
    EXTRA_DROP,
    BONUS_DROP;

    fun addNotifications(vararg notifications: Notification?) {
        notifications.forEach { notification ->
            if (notification == null) return@forEach
            this.notifications.add(notification)
        }
    }

    fun clear() {
        notifications.clear()
    }

    fun notice(player: Player, replace: (String) -> String = { it }) {
        notifications.forEach {
            it.notice(player, replace)
        }
    }

    companion object {
        private val values = values()

        fun of(text: String): NotificationType? {
            return runCatching { valueOf(text.uppercase().replace("-", "_")) }.getOrNull()
        }

        fun clear() {
            values.forEach(NotificationType::clear)
        }

        const val ITEM_REPLACER = "%item%"
        const val AMOUNT_REPLACER = "%amount%"
    }
}