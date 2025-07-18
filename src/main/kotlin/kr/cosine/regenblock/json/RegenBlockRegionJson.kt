package kr.cosine.regenblock.json

import kr.cosine.regenblock.registry.RegenBlockRegionRegistry
import kr.cosine.regenblock.storable.json.StorableJson
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.plugin.Plugin

@Bean
class RegenBlockRegionJson(
    plugin: Plugin,
    regenBlockRegionRegistry: RegenBlockRegionRegistry
) : StorableJson<RegenBlockRegionRegistry>(
    "${plugin.dataFolder}/region.json",
    regenBlockRegionRegistry
)