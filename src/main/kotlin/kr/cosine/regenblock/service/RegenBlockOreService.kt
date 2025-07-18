package kr.cosine.regenblock.service

import kr.cosine.regenblock.data.OreGroup
import kr.cosine.regenblock.json.OreJson
import kr.cosine.regenblock.registry.OreGroupRegistry
import kr.hqservice.framework.global.core.component.Service

@Service
class RegenBlockOreService(
    private val oreJson: OreJson,
    private val oreGroupRegistry: OreGroupRegistry
) {
    fun createOreGroup(key: String): Boolean {
        if (oreGroupRegistry.isOreGroup(key)) return false
        val oreGroup = OreGroup()
        oreGroupRegistry.setOreGroup(key, oreGroup)
        oreJson.save()
        return true
    }

    fun deleteOreGroup(key: String): Boolean {
        oreGroupRegistry.removeOreGroup(key)
        oreJson.save()
        return true
    }
}