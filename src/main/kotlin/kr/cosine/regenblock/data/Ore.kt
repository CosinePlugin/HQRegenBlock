package kr.cosine.regenblock.data

import kr.cosine.regenblock.enumeration.ChanceType
import kr.cosine.regenblock.enumeration.DropType
import kr.cosine.regenblock.extension.chance
import kr.cosine.regenblock.extension.format
import kr.cosine.regenblock.extension.randomOrNull
import kr.cosine.regenblock.extension.toItemStackWrapper
import org.bukkit.inventory.ItemStack

data class Ore(
    private val dropMap: MutableMap<DropType, ItemStackWrapper>,
    private val chanceMap: MutableMap<ChanceType, Double>,
    private val extraDropCountMap: MutableMap<Int, Double>
) {
    fun findDrop(dropType: DropType): ItemStackWrapper? {
        return dropMap[dropType]
    }

    fun setDrop(dropType: DropType, itemStack: ItemStack?) {
        if (itemStack == null) {
            dropMap.remove(dropType)
            return
        }
        dropMap[dropType] = itemStack.toItemStackWrapper()
    }

    fun isChance(chanceType: ChanceType): Boolean {
        return getChance(chanceType).chance()
    }

    fun getChance(chanceType: ChanceType): Double {
        return chanceMap.computeIfAbsent(chanceType) { 0.0 }
    }

    fun setChance(chanceType: ChanceType, chance: Double) {
        chanceMap[chanceType] = chance
    }

    fun getExtraDropCountMap(): Map<Int, Double> {
        return extraDropCountMap
    }

    fun setExtraDropCount(extraDropCount: Int, chance: Double) {
        extraDropCountMap[extraDropCount] = chance
    }

    fun removeExtraDropCount(extraDropCount: Int) {
        extraDropCountMap.remove(extraDropCount)
    }

    fun getExtraDropCount(): Int {
        return extraDropCountMap.randomOrNull() ?: 0
    }

    fun getChanceLore(chanceType: ChanceType): String {
        return "§f${getChance(chanceType).format()}%"
    }

    fun getDropLore(dropType: DropType): List<String> {
        val drop = findDrop(dropType)
        return if (drop == null) {
            listOf("§8설정되지 않음")
        } else {
            listOf("§f${drop.getDisplayName()} §7(x${drop.amount})")
        }
    }

    fun getExtraDropCountLore(): List<String> {
        val extraDropCountMap = getExtraDropCountMap()
        return if (extraDropCountMap.isEmpty()) {
            listOf("§8설정되지 않음")
        } else {
            extraDropCountMap.toSortedMap(compareBy { it }).map { (count, chance) ->
                "§f${count}개 §7/ §a${chance.format()}%"
            }
        }
    }

    companion object {
        fun init(): Ore {
            return Ore(mutableMapOf(), mutableMapOf(), mutableMapOf())
        }
    }
}