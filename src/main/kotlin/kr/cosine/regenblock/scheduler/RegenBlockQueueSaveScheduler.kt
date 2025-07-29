package kr.cosine.regenblock.scheduler

import kr.cosine.regenblock.json.RegenBlockQueueJson
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.scheduler.BukkitRunnable

@Bean
class RegenBlockQueueSaveScheduler(
    private val regenBlockQueueJson: RegenBlockQueueJson
) : BukkitRunnable() {
    override fun run() {
        regenBlockQueueJson.save()
    }
}