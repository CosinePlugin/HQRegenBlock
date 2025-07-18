package kr.cosine.regenblock.registry

import kr.cosine.regenblock.data.OreGroup
import kr.cosine.regenblock.storable.json.Restorable
import kr.hqservice.framework.global.core.component.Bean

@Bean
class OreGroupRegistry : Restorable<OreGroupRegistry> {
    private val oreGroupMap = mutableMapOf<String, OreGroup>()

    @Transient
    override var isChanged = false

    override fun restore(restorable: OreGroupRegistry) {
        this.oreGroupMap.clear()
        this.oreGroupMap.putAll(restorable.oreGroupMap)
    }

    fun isOreGroup(key: String): Boolean {
        return oreGroupMap.containsKey(key)
    }

    fun findOreGroup(key: String): OreGroup? {
        return oreGroupMap[key]
    }

    fun setOreGroup(key: String, oreGroup: OreGroup) {
        oreGroupMap[key] = oreGroup
        isChanged = true
    }

    fun removeOreGroup(key: String) {
        oreGroupMap.remove(key)
        isChanged = true
    }

    fun getOreGroupMap(): Map<String, OreGroup> {
        return oreGroupMap
    }

    fun getOreGroupKeys(): List<String> {
        return oreGroupMap.keys.toList()
    }
}