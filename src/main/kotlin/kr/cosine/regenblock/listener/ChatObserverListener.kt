package kr.cosine.regenblock.listener

import kr.cosine.regenblock.registry.ChatObserverRegistry
import kr.hqservice.framework.bukkit.core.listener.HandleOrder
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent

@Listener
class ChatObserverListener {
    @Subscribe(HandleOrder.FIRST, true)
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        ChatObserverRegistry.observe(event.player.uniqueId, event)
    }

    @Subscribe(HandleOrder.LAST, true)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        ChatObserverRegistry.removeChatObserver(event.player.uniqueId)
    }
}