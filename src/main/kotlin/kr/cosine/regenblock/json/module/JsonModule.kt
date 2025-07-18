package kr.cosine.regenblock.json.module

import kr.cosine.regenblock.json.OreJson
import kr.cosine.regenblock.json.RegenBlockRegionJson
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup
import kr.hqservice.framework.bukkit.core.component.module.Teardown

@Module
class JsonModule(
    private val oreJson: OreJson,
    private val regenBlockRegionJson: RegenBlockRegionJson
) {
    @Setup
    fun setup() {
        oreJson.load()
        regenBlockRegionJson.load()
    }

    @Teardown
    fun teardown() {
        oreJson.save()
        regenBlockRegionJson.save()
    }
}