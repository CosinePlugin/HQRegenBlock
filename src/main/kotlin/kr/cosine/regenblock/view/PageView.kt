package kr.cosine.regenblock.view

import kr.cosine.regenblock.extension.playButtonClickSound
import kr.hqservice.framework.inventory.button.HQButton
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import kr.hqservice.framework.inventory.extension.setLeftClickFunction
import org.bukkit.Material

abstract class PageView<T>(
    title: String,
    isCancelled: Boolean = true
) : Container(54, title, isCancelled) {
    private var page = 0

    abstract val elements: Collection<T>
    private var currentElement = emptyList<T>()

    override fun onCreate() {
        drawPageButton()
        drawElements()
    }

    private fun drawPageButton() {
        HQButtonBuilder(Material.WHITE_STAINED_GLASS_PANE)
            .setDisplayName("§f")
            .build()
            .setSlot(this, 46..52)

        HQButtonBuilder(Material.RED_STAINED_GLASS_PANE).apply {
            setDisplayName("§c이전 페이지로 이동")
            setLeftClickFunction { event ->
                val player = event.getWhoClicked()
                player.playButtonClickSound()
                if (page == 0) return@setLeftClickFunction
                page--
                refresh()
            }
        }.build().setSlot(this, 45)

        HQButtonBuilder(Material.LIME_STAINED_GLASS_PANE).apply {
            setDisplayName("§a다음 페이지로 이동")
            setLeftClickFunction { event ->
                val player = event.getWhoClicked()
                player.playButtonClickSound()
                if (elements.size <= (page + 1) * ELEMENT_SIZE) return@setLeftClickFunction
                page++
                refresh()
            }
        }.build().setSlot(this, 53)
    }

    private fun drawElements() {
        currentElement = elements.drop(page * ELEMENT_SIZE).take(ELEMENT_SIZE)
        currentElement.forEachIndexed { slot, element ->
            getButton(element).setSlot(this, slot)
        }
    }

    abstract fun getButton(element: T): HQButton

    private companion object {
        const val ELEMENT_SIZE = 45
    }
}