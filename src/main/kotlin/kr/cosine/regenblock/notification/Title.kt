package kr.cosine.regenblock.notification

import org.bukkit.entity.Player

data class Title(
    private val main: String,
    private val sub: String,
    private val fadeIn: Int,
    private val duration: Int,
    private val fadeOut: Int
) : Notification {
    override fun notice(player: Player, replace: (String) -> String) {
        player.sendTitle(replace(main), replace(sub), fadeIn, duration, fadeOut)
    }
}