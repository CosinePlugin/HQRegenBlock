package kr.cosine.regenblock.data

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World

data class Position(
    val x: Int,
    val y: Int,
    val z: Int
) {
    val chunkX get() = x shr 4
    val chunkZ get() = z shr 4

    fun getLocation(world: World?): Location {
        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }

    fun isChunkLoaded(world: World?): Boolean {
        return world?.isChunkLoaded(chunkX, chunkZ) == true
    }

    fun findChunk(world: World?): Chunk? {
        return world?.getChunkAt(chunkX, chunkZ)
    }
}