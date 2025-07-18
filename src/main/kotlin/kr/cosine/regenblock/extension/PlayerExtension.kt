package kr.cosine.regenblock.extension

import org.bukkit.Sound
import org.bukkit.entity.Player

fun Player.playButtonClickSound(volume: Float = 1f, pitch: Float = 1f) {
    playSound(location, Sound.UI_BUTTON_CLICK, volume, pitch)
}