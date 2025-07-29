package kr.cosine.regenblock.scheduler.module

import kr.cosine.regenblock.configuration.RegenBlockConfiguration
import kr.cosine.regenblock.scheduler.RegenBlockQueueSaveScheduler
import kr.cosine.regenblock.scheduler.RegenBlockQueueScheduler
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup

@Module
class SchedulerModule(
    private val plugin: HQBukkitPlugin,
    private val regenBlockConfiguration: RegenBlockConfiguration,
    private val regenBlockQueueScheduler: RegenBlockQueueScheduler,
    private val regenBlockQueueSaveScheduler: RegenBlockQueueSaveScheduler
) {
    @Setup
    fun setup() {
        regenBlockQueueScheduler.runTaskTimerAsynchronously(plugin, 0, 20)

        val queueAutoSavePeriod = regenBlockConfiguration.queueAutoSavePeriod
        regenBlockQueueSaveScheduler.runTaskTimerAsynchronously(plugin, queueAutoSavePeriod, queueAutoSavePeriod)
    }
}