package kr.cosine.regenblock.extension

import org.bukkit.plugin.Plugin

fun Plugin.runTask(runnable: Runnable) {
    server.scheduler.runTask(this, runnable)
}