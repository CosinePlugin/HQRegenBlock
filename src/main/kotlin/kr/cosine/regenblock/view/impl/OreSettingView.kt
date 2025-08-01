package kr.cosine.regenblock.view.impl

import kr.cosine.regenblock.data.Ore
import kr.cosine.regenblock.data.OreGroup
import kr.cosine.regenblock.enumeration.ChanceType
import kr.cosine.regenblock.enumeration.DropType
import kr.cosine.regenblock.extension.listsOf
import kr.cosine.regenblock.extension.playButtonClickSound
import kr.cosine.regenblock.json.OreJson
import kr.cosine.regenblock.registry.OreGroupRegistry
import kr.cosine.regenblock.view.PageView
import kr.hqservice.framework.inventory.button.HQButton
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class OreSettingView(
    private val oreJson: OreJson,
    private val oreGroupRegistry: OreGroupRegistry,
    private val oreGroup: OreGroup,
) : PageView<Map.Entry<Material, Ore>>("광석 그룹 설정") {
    override val elements = oreGroup.entries

    override fun getButton(element: Map.Entry<Material, Ore>): HQButton {
        val material = element.key
        val ore = element.value
        return HQButtonBuilder(material).apply {
            setRemovable(true)
            setLore(
                listsOf(
                    getChanceLore(ore, ChanceType.REGEN),
                    getDropLore(ore, DropType.DEFAULT),
                    getChanceLore(ore, ChanceType.EXTRA_DROP),
                    getExtraDropCountLore(ore),
                    getChanceLore(ore, ChanceType.BONUS_DROP),
                    getDropLore(ore, DropType.BONUS),
                    getChanceLore(ore, ChanceType.EXPERIENCE_DROP),
                    getExperienceRangeLore(ore),
                    getFortuneEnabledLore(ore),
                    listOf(
                        "§a좌클릭 §7▸ §f세부 설정으로 이동합니다.",
                        "§c쉬프트+우클릭 §7▸ §f목록에서 제거합니다.",
                    )
                )
            )
            setClickFunction { event ->
                val player = event.getWhoClicked()
                when (event.getClickType()) {
                    ClickType.LEFT -> {
                        player.playButtonClickSound()
                        val oreDetailSettingView = OreDetailSettingView(oreGroupRegistry, material, element.value, ::delayReopen)
                        openContainer(oreDetailSettingView, player)
                    }

                    ClickType.SHIFT_RIGHT -> {
                        player.playButtonClickSound()
                        oreGroup.remove(material)
                        oreGroupRegistry.isChanged = true
                        refresh()
                    }

                    else -> {}
                }
            }
        }.build()
    }

    private fun getChanceLore(ore: Ore, chanceType: ChanceType): List<String> {
        return listOf("§6${chanceType.displayName} 확률", ore.getChanceLore(chanceType)) + emptyLore
    }

    private fun getDropLore(ore: Ore, dropType: DropType): List<String> {
        return listOf("§6${dropType.displayName} 드랍 아이템") + ore.getDropLore(dropType) + emptyLore
    }

    private fun getExtraDropCountLore(ore: Ore): List<String> {
        return listOf("§6추가 드랍 개수별 확률") + ore.getExtraDropCountLore() + emptyLore
    }

    private fun getExperienceRangeLore(ore: Ore): List<String> {
        return listOf("§6경험치 범위", ore.getExperienceRangeLore()) + emptyLore
    }

    private fun getFortuneEnabledLore(ore: Ore): List<String> {
        return listOf("§6행운 적용", ore.getFortuneEnabledLore()) + emptyLore
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == null || event.rawSlot < size) return
        val player = event.whoClicked as Player
        val itemStack = event.currentItem?.clone() ?: return
        player.playButtonClickSound()
        val material = itemStack.type
        if (!material.isBlock) {
            player.sendMessage("§c블록만 추가할 수 있습니다.")
            return
        }
        oreGroup.add(material)
        oreGroupRegistry.isChanged = true
        refresh()
    }

    override fun onDestroy(event: InventoryCloseEvent) {
        if (oreGroupRegistry.isChanged) {
            oreJson.save()
        }
    }

    private companion object {
        val emptyLore = listOf("")
    }
}