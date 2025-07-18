package kr.cosine.regenblock.scheduler.module

import kr.cosine.regenblock.scheduler.RegenBlockQueueScheduler
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup

@Module
class SchedulerModule(
    private val plugin: HQBukkitPlugin,
    private val regenBlockQueueScheduler: RegenBlockQueueScheduler
) {
    @Setup
    fun setup() {
        regenBlockQueueScheduler.runTaskTimerAsynchronously(plugin, 0, 20)
    }
}