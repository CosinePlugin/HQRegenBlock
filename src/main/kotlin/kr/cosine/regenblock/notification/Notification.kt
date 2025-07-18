package kr.cosine.regenblock.notification

import org.bukkit.entity.Player

interface Notification {
    fun notice(player: Player, replace: (String) -> String = { it })
}