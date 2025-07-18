package kr.cosine.regenblock.registry

import kr.cosine.regenblock.data.RegenBlockQueue
import kr.cosine.regenblock.storable.json.Restorable
import kr.hqservice.framework.global.core.component.Bean

@Bean
class RegenBlockQueueRegistry : Restorable<RegenBlockQueueRegistry> {
    private val regenBlockQueues = mutableListOf<RegenBlockQueue>()

    @Transient
    override var isChanged = false

    override fun restore(restorable: RegenBlockQueueRegistry) {
        this.regenBlockQueues.clear()
        this.regenBlockQueues.addAll(restorable.regenBlockQueues)
    }

    fun addRegenBlockQueue(regenBlockQueue: RegenBlockQueue) {
        regenBlockQueues.add(regenBlockQueue)
    }

    fun removeIf(predicate: (RegenBlockQueue) -> Boolean) {
        regenBlockQueues.removeIf(predicate)
    }
}