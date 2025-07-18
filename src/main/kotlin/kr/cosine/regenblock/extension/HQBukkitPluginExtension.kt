package kr.cosine.regenblock.extension

import kotlinx.coroutines.*
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.coroutine.bukkitDelay
import kr.hqservice.framework.bukkit.core.coroutine.extension.BukkitMain

fun HQBukkitPlugin.delayLaunch(
    tick: Long = 1,
    dispatcher: CoroutineDispatcher = Dispatchers.BukkitMain,
    block: suspend CoroutineScope.() -> Unit
) : Job {
    return launch(dispatcher) {
        bukkitDelay(tick)
        block()
    }
}