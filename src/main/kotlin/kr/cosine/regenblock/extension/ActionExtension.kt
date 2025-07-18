package kr.cosine.regenblock.extension

import org.bukkit.event.block.Action

val Action.isRightClick: Boolean
    get() = this == Action.RIGHT_CLICK_AIR || this == Action.RIGHT_CLICK_BLOCK