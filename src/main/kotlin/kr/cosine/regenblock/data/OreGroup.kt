package kr.cosine.regenblock.data

import kr.cosine.regenblock.enumeration.ChanceType
import org.bukkit.Material
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.ln

data class OreGroup(
    private val oreMap: MutableMap<Material, Ore> = mutableMapOf()
) : MutableMap<Material, Ore> by oreMap {
    fun randomOrNull(): Material? {
        return oreMap.minByOrNull {
            -ln(ThreadLocalRandom.current().nextDouble()) / it.value.getChance(ChanceType.REGEN)
        }?.key
    }

    fun add(material: Material) {
        oreMap[material] = Ore.init()
    }
}