package kr.cosine.regenblock.registry

import kr.cosine.regenblock.observer.ChatObserver
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.*

object ChatObserverRegistry {
    private val chatObserverMap = mutableMapOf<UUID, ChatObserver>()

    fun addChatObserver(subscriber: UUID, chatObserver: ChatObserver) {
        chatObserverMap[subscriber] = chatObserver
    }

    fun removeChatObserver(subscriber: UUID) {
        chatObserverMap.remove(subscriber)
    }

    fun observe(subscriber: UUID, event: AsyncPlayerChatEvent) {
        val chatObserver = chatObserverMap[subscriber]
        if (chatObserver != null) {
            event.isCancelled = true
            chatObserver.onChat(event.message)
        }
    }
}