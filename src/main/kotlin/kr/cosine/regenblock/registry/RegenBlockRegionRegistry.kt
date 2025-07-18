package kr.cosine.regenblock.registry

import kr.cosine.regenblock.data.RegenBlockRegion
import kr.cosine.regenblock.storable.json.Restorable
import kr.hqservice.framework.global.core.component.Bean

@Bean
class RegenBlockRegionRegistry : Restorable<RegenBlockRegionRegistry> {
    private val regenBlockRegionMap = mutableMapOf<String, RegenBlockRegion>()

    @Transient
    override var isChanged = false

    override fun restore(restorable: RegenBlockRegionRegistry) {
        this.regenBlockRegionMap.clear()
        this.regenBlockRegionMap.putAll(restorable.regenBlockRegionMap)
    }

    fun isRegenBlockRegion(key: String): Boolean {
        return regenBlockRegionMap.containsKey(key)
    }

    fun findRegenBlockRegion(key: String): RegenBlockRegion? {
        return regenBlockRegionMap[key]
    }

    fun setRegenBlockRegion(key: String, region: RegenBlockRegion) {
        regenBlockRegionMap[key] = region
        isChanged = true
    }

    fun removeRegenBlockRegion(key: String) {
        regenBlockRegionMap.remove(key)
        isChanged = true
    }

    fun getRegenBlockRegionMap(): Map<String, RegenBlockRegion> {
        return regenBlockRegionMap
    }

    fun getRegenBlockRegionKeys(): List<String> {
        return regenBlockRegionMap.keys.toList()
    }

    fun getRegenBlockRegions(): Collection<RegenBlockRegion> {
        return regenBlockRegionMap.values
    }
}