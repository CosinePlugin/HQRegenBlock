package kr.cosine.regenblock.configuration.module

import kr.cosine.regenblock.configuration.RegenBlockConfiguration
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup

@Module
class ConfigurationModule(
    private val regenBlockConfiguration: RegenBlockConfiguration
) {
    @Setup
    fun setup() {
        regenBlockConfiguration.load()
    }
}