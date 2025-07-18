package kr.cosine.regenblock.notification

import org.bukkit.entity.Player

data class Sound(
    private val name: String,
    private val volume: Float,
    private val pitch: Float
) : Notification {
    override fun notice(player: Player, replace: (String) -> String) {
        player.playSound(player.location, name, volume, pitch)
    }
}
