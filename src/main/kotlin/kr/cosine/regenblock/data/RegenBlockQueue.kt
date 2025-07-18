package kr.cosine.regenblock.data

import kr.cosine.regenblock.extension.toPosition
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

class RegenBlockQueue(
    private val worldName: String,
    private val position: Position,
    val oreMaterial: Material,
    private val nextGenerateTime: Long
) {
    constructor(
        location: Location,
        oreMaterial: Material,
        regenDuration: Long
    ) : this(
        location.world!!.name,
        location.toPosition(),
        oreMaterial,
        System.currentTimeMillis() + regenDuration
    )

    private val world get() = Bukkit.getWorld(worldName)

    fun isNextGenerateTime(): Boolean {
        return System.currentTimeMillis() >= nextGenerateTime
    }

    fun isChunkLoaded(): Boolean {
        return position.isChunkLoaded(world)
    }

    fun getLocation(): Location {
        return position.getLocation(world)
    }
}