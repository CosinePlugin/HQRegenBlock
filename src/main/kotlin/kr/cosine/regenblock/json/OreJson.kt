package kr.cosine.regenblock.json

import kr.cosine.regenblock.registry.OreGroupRegistry
import kr.cosine.regenblock.storable.json.StorableJson
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.plugin.Plugin

@Bean
class OreJson(
    plugin: Plugin,
    oreGroupRegistry: OreGroupRegistry
) : StorableJson<OreGroupRegistry>(
    "${plugin.dataFolder}/ore.json",
    oreGroupRegistry
)