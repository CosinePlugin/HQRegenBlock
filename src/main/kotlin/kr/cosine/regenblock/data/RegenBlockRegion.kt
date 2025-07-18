package kr.cosine.regenblock.data

import kr.cosine.regenblock.extension.center
import kr.cosine.regenblock.extension.isIntersect
import org.bukkit.Bukkit
import org.bukkit.Location

class RegenBlockRegion(
    val worldName: String,
    private val minPosition: Position,
    private val maxPosition: Position,
    private var oreGroupKey: String? = null
) {
    val world get() = Bukkit.getWorld(worldName)

    private val xRange get() = minPosition.x..maxPosition.x
    private val yRange get() = minPosition.y..maxPosition.y
    private val zRange get() = minPosition.z..maxPosition.z

    fun isColliding(regenBlockRegion: RegenBlockRegion): Boolean {
        return xRange.isIntersect(regenBlockRegion.xRange) &&
                yRange.isIntersect(regenBlockRegion.yRange) &&
                zRange.isIntersect(regenBlockRegion.zRange)
    }

    fun contains(location: Location): Boolean {
        val x = location.blockX
        val y = location.blockY
        val z = location.blockZ
        return location.world?.name == worldName &&
                minPosition.x <= x && x <= maxPosition.x &&
                minPosition.y <= y && y <= maxPosition.y &&
                minPosition.z <= z && z <= maxPosition.z
    }

    fun findOreGroupKey(): String? {
        return oreGroupKey
    }

    fun setOreHolderKey(key: String) {
        this.oreGroupKey = key
    }

    fun getCenterLocation(): Location {
        return Position(xRange.center, yRange.center, zRange.center).getLocation(world)
    }

    fun getFormattedPositions(): String {
        return "(${minPosition.x}, ${minPosition.y}, ${minPosition.z}) ~ (${maxPosition.x}, ${maxPosition.y}, ${maxPosition.z})"
    }

    companion object {
        private fun minAndMaxOf(coordinate: Int, coordinate2: Int): Pair<Int, Int> {
            return minOf(coordinate, coordinate2) to maxOf(coordinate, coordinate2)
        }

        fun of(location: Location, location2: Location): RegenBlockRegion {
            val (minX, maxX) = minAndMaxOf(location.blockX, location2.blockX)
            val (minY, maxY) = minAndMaxOf(location.blockY, location2.blockY)
            val (minZ, maxZ) = minAndMaxOf(location.blockZ, location2.blockZ)
            val minPosition = Position(minX, minY, minZ)
            val maxPosition = Position(maxX, maxY, maxZ)
            return RegenBlockRegion(location.world!!.name, minPosition, maxPosition)
        }
    }
}