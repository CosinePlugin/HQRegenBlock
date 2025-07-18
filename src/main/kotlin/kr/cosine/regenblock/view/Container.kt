package kr.cosine.regenblock.view

import kr.cosine.regenblock.extension.delayLaunch
import kr.cosine.regenblock.observer.ChatObserver
import kr.cosine.regenblock.registry.ChatObserverRegistry
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.inventory.container.HQContainer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

abstract class Container(
    protected val size: Int,
    title: String,
    isCancelled: Boolean = true
) : HQContainer(size, title, isCancelled) {
    protected var isPrevented = false

    abstract fun onCreate()

    final override fun initialize(inventory: Inventory) {
        onCreate()
    }

    final override fun onOpen(vararg players: Player) {
        onOpen()
        players.forEach(::onOpen)
    }

    open fun onOpen() {}

    open fun onOpen(player: Player) {}

    protected fun reopen(player: Player) {
        refresh()
        open(player)
    }

    protected fun delayReopen(player: Player) {
        delayLaunch {
            reopen(player)
        }
    }

    protected fun delayOpen(player: Player) {
        delayLaunch {
            open(player)
        }
    }

    protected fun delayLaunch(
        tick: Long = 1,
        block: suspend () -> Unit
    ) {
        plugin.delayLaunch(tick) {
            block()
        }
    }

    protected fun openContainer(container: HQContainer, player: Player) {
        isPrevented = true
        player.closeInventory()
        delayLaunch {
            container.open(player)
        }
    }

    protected fun startChatObserver(player: Player, onChat: (String) -> Boolean) {
        isPrevented = true
        player.closeInventory()
        val playerUniqueId = player.uniqueId
        val chatObserver = object : ChatObserver {
            override fun onChat(message: String) {
                if (message == "-") {
                    ChatObserverRegistry.removeChatObserver(playerUniqueId)
                    player.sendMessage("§a설정이 취소되었습니다.")
                    delayReopen(player)
                    return
                }
                if (onChat(message)) {
                    ChatObserverRegistry.removeChatObserver(playerUniqueId)
                    delayReopen(player)
                }
            }
        }
        ChatObserverRegistry.addChatObserver(playerUniqueId, chatObserver)
    }

    override fun onClose(event: InventoryCloseEvent) {
        if (isPrevented) {
            isPrevented = false
            return
        }
        onDestroy(event)
    }

    open fun onDestroy(event: InventoryCloseEvent) {}

    protected companion object {
        val plugin by lazy { Bukkit.getPluginManager().getPlugin("HQRegenBlock") as HQBukkitPlugin }
    }
}