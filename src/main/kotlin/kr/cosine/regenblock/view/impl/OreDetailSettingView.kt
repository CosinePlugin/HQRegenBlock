package kr.cosine.regenblock.view.impl

import kr.cosine.regenblock.data.Ore
import kr.cosine.regenblock.enumeration.ChanceType
import kr.cosine.regenblock.enumeration.DropType
import kr.cosine.regenblock.extension.format
import kr.cosine.regenblock.extension.getDisplayName
import kr.cosine.regenblock.extension.playButtonClickSound
import kr.cosine.regenblock.registry.OreGroupRegistry
import kr.cosine.regenblock.view.Container
import kr.hqservice.framework.inventory.button.HQButton
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import kr.hqservice.framework.inventory.extension.setLeftClickFunction
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryCloseEvent

class OreDetailSettingView(
    private val oreGroupRegistry: OreGroupRegistry,
    private val material: Material,
    private val ore: Ore,
    private val onClose: (Player) -> Unit
) : Container(9, "${material.getDisplayName()} 설정") {
    override fun onCreate() {
        listOf(
            getChanceSettingButton(ChanceType.REGEN),
            getDropSettingButton(DropType.DEFAULT),
            getChanceSettingButton(ChanceType.EXTRA_DROP),
            getExtraDropCountSettingButton(),
            getChanceSettingButton(ChanceType.BONUS_DROP),
            getDropSettingButton(DropType.BONUS)
        ).forEachIndexed { index, button ->
            button.setSlot(this, index)
        }
    }

    private fun getDropSettingButton(dropType: DropType): HQButton {
        val drop = ore.findDrop(dropType)
        return HQButtonBuilder(Material.CHEST).apply {
            setDisplayName("§6${dropType.displayName} 드랍 아이템")
            setLore(ore.getDropLore(dropType))
            setLeftClickFunction { event ->
                val player = event.getWhoClicked()
                val dropSettingView = DropSettingView(material, dropType, drop?.clone(), ::delayReopen) { newDrop ->
                    ore.setDrop(dropType, newDrop)
                    oreGroupRegistry.isChanged = true
                    player.sendMessage("§a${dropType.displayName} 드랍 아이템이 설정되었습니다.")
                }
                openContainer(dropSettingView, player)
            }
        }.build()
    }

    private fun getChanceSettingButton(chanceType: ChanceType): HQButton {
        val chanceDisplayName = chanceType.displayName
        return HQButtonBuilder(Material.REDSTONE).apply {
            setDisplayName("§6$chanceDisplayName 확률")
            setLore(listOf(ore.getChanceLore(chanceType)))
            setLeftClickFunction { event ->
                val player = event.getWhoClicked()
                player.playButtonClickSound()
                player.sendMessage("§a$chanceDisplayName 확률을 입력해주세요. §c(취소: -)")
                startChatObserver(player) {
                    val chance = it.toDoubleOrNull() ?: run {
                        player.sendMessage("§c숫자만 입력할 수 있습니다.")
                        return@startChatObserver false
                    }
                    if (chance < 0.0) {
                        player.sendMessage("§c양수만 입력할 수 있습니다.")
                        return@startChatObserver false
                    }
                    ore.setChance(chanceType, chance)
                    oreGroupRegistry.isChanged = true
                    player.sendMessage("§a$chanceDisplayName 확률을 ${chance}%로 설정하였습니다.")
                    return@startChatObserver true
                }
            }
        }.build()
    }

    private fun getExtraDropCountSettingButton(): HQButton {
        return HQButtonBuilder(Material.WRITABLE_BOOK).apply {
            setDisplayName("§6추가 드랍 개수별 확률")
            setLore(
                ore.getExtraDropCountLore() + listOf(
                    "",
                    "§a좌클릭 §7▸ §f추가 드랍 개수를 추가합니다.",
                    "§c쉬프트+우클릭 §7▸ §f추가 드랍 개수를 제거합니다."
                )
            )
            setClickFunction { event ->
                val player = event.getWhoClicked()
                when (event.getClickType()) {
                    ClickType.LEFT -> {
                        player.playButtonClickSound()
                        player.sendMessage("§a추가 드랍 개수와 확률을 입력해주세요. §c(취소: -)")
                        player.sendMessage("§7└ ex: 3/23.5")
                        startChatObserver(player) {
                            val split = it.split("/")
                            val extraDropCount = split.getOrNull(0)?.toIntOrNull() ?: run {
                                player.sendMessage("§c추가 드랍 개수에는 숫자만 입력할 수 있습니다.")
                                return@startChatObserver false
                            }
                            val chance = split.getOrNull(1)?.toDoubleOrNull() ?: run {
                                player.sendMessage("§c확률에는 숫자만 입력할 수 있습니다.")
                                return@startChatObserver false
                            }
                            if (extraDropCount < 1) {
                                player.sendMessage("§c추가 드랍 개수에는 1 이상의 양수만 입력할 수 있습니다.")
                                return@startChatObserver false
                            }
                            if (chance < 0.0) {
                                player.sendMessage("§c확률에는 0 이상의 양수만 입력할 수 있습니다.")
                                return@startChatObserver false
                            }
                            ore.setExtraDropCount(extraDropCount, chance)
                            oreGroupRegistry.isChanged = true
                            player.sendMessage("§a추가 드랍 개수 ${extraDropCount}회, 확률 ${chance.format()}%로 설정되었습니다.")
                            return@startChatObserver true
                        }
                    }

                    ClickType.SHIFT_RIGHT -> {
                        player.playButtonClickSound()
                        val extraDropCountMap = ore.getExtraDropCountMap()
                        if (extraDropCountMap.isEmpty()) {
                            player.sendMessage("§c추가 드랍 개수가 설정되어 있지 않습니다.")
                            return@setClickFunction
                        }
                        player.sendMessage("§a제거할 추가 드랍 개수를 입력해주세요. §c(취소: -)")
                        extraDropCountMap.forEach { (count, chance) ->
                            player.sendMessage("§f- ${count}개 §7/ §a${chance.format()}%")
                        }
                        startChatObserver(player) {
                            val extraDropCount = it.toIntOrNull() ?: run {
                                player.sendMessage("§c숫자만 입력할 수 있습니다.")
                                return@startChatObserver false
                            }
                            if (!extraDropCountMap.containsKey(extraDropCount)) {
                                player.sendMessage("§c해당 추가 드랍 개수가 설정되어 있지 않습니다.")
                                return@startChatObserver false
                            }
                            ore.removeExtraDropCount(extraDropCount)
                            oreGroupRegistry.isChanged = true
                            player.sendMessage("§a추가 드랍 개수 ${extraDropCount}개가 제거되었습니다.")
                            return@startChatObserver true
                        }
                    }

                    else -> {}
                }
            }
        }.build()
    }

    override fun onDestroy(event: InventoryCloseEvent) {
        onClose(event.player as Player)
    }
}