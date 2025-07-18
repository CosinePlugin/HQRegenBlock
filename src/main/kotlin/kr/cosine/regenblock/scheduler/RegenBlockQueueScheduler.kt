package kr.cosine.regenblock.scheduler

import kr.cosine.regenblock.extension.runTask
import kr.cosine.regenblock.registry.RegenBlockQueueRegistry
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.scheduler.BukkitRunnable

@Bean
class RegenBlockQueueScheduler(
    private val plugin: HQBukkitPlugin,
    private val regenBlockQueueRegistry: RegenBlockQueueRegistry
) : BukkitRunnable() {
    override fun run() {
        regenBlockQueueRegistry.removeIf { regenBlockQueue ->
            val isGenerated = regenBlockQueue.isChunkLoaded() && regenBlockQueue.isNextGenerateTime()
            if (isGenerated) {
                plugin.runTask {
                    regenBlockQueue.getLocation().block.type = regenBlockQueue.oreMaterial
                }
            }
            isGenerated
        }
    }
}