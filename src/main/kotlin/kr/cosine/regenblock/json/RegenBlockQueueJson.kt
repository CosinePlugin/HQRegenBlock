package kr.cosine.regenblock.json

import kr.cosine.regenblock.registry.RegenBlockQueueRegistry
import kr.cosine.regenblock.storable.json.StorableJson
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.plugin.Plugin

@Bean
class RegenBlockQueueJson(
    plugin: Plugin,
    regenBlockQueueRegistry: RegenBlockQueueRegistry
) : StorableJson<RegenBlockQueueRegistry>(
    "${plugin.dataFolder}/queue.json",
    regenBlockQueueRegistry
)