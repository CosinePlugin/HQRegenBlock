package kr.cosine.regenblock.extension

import kr.cosine.regenblock.data.Position
import org.bukkit.Location

fun Location.toPosition(): Position {
    return Position(blockX, blockY, blockZ)
}