package kr.cosine.regenblock.view.impl

import kr.cosine.regenblock.data.RegenBlockRegion
import kr.cosine.regenblock.extension.playButtonClickSound
import kr.cosine.regenblock.registry.RegenBlockRegionRegistry
import kr.cosine.regenblock.view.PageView
import kr.hqservice.framework.inventory.button.HQButton
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import kr.hqservice.framework.inventory.extension.setLeftClickFunction
import org.bukkit.Material

class RegenBlockRegionListView(
    regenBlockRegionRegistry: RegenBlockRegionRegistry
) : PageView<Map.Entry<String, RegenBlockRegion>>("리젠블럭 구역 목록") {
    override val elements = regenBlockRegionRegistry.getRegenBlockRegionMap().entries

    override fun getButton(element: Map.Entry<String, RegenBlockRegion>): HQButton {
        val regenBlockRegion = element.value
        return HQButtonBuilder(icons.random()).apply {
            setDisplayName("§f${element.key} 구역")
            setLore(
                listOf(
                    "§6광석 그룹",
                    "§f${regenBlockRegion.findOreGroupKey() ?: "§8설정되지 않음"}",
                    "",
                    "§6구역",
                    "§f- 월드: ${regenBlockRegion.worldName}",
                    "§f- 범위: ${regenBlockRegion.getFormattedPositions()}",
                    "",
                    "§a좌클릭 §7▸ §f해당 위치로 이동합니다.",
                )
            )
            setLeftClickFunction { event ->
                val player = event.getWhoClicked()
                player.playButtonClickSound()
                player.closeInventory()
                val centerLocation = regenBlockRegion.getCenterLocation()
                player.teleport(centerLocation)
            }
        }.build()
    }

    private companion object {
        val icons = listOf(
            Material.STONE,
            Material.IRON_ORE,
            Material.GOLD_ORE,
            Material.DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.COAL_ORE,
            Material.LAPIS_ORE,
            Material.REDSTONE_ORE,
            Material.COPPER_ORE
        )
    }
}