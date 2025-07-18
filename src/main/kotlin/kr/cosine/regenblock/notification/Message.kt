package kr.cosine.regenblock.notification

import org.bukkit.entity.Player

data class Message(
    private val messages: List<String>
) : Notification {
    override fun notice(player: Player, replace: (String) -> String) {
        messages.forEach { message ->
            player.sendMessage(replace(message))
        }
    }
}
