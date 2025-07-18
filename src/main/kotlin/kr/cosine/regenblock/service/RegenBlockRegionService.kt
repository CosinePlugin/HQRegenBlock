package kr.cosine.regenblock.service

import kr.cosine.regenblock.data.RegenBlockRegion
import kr.cosine.regenblock.enumeration.PositionType
import kr.cosine.regenblock.json.RegenBlockRegionJson
import kr.cosine.regenblock.registry.RegenBlockRegionRegistry
import kr.cosine.regenblock.view.impl.RegenBlockRegionListView
import kr.hqservice.framework.bukkit.core.extension.editMeta
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

@Service
class RegenBlockRegionService(
    private val regenBlockRegionJson: RegenBlockRegionJson,
    private val regenBlockRegionRegistry: RegenBlockRegionRegistry
) {
    private val regenBlockRegionTool = ItemStack(Material.STICK).editMeta {
        setDisplayName("§d리젠블럭 구역 지정 도구")
    }

    private val regenBlockPositionMap = mutableMapOf<UUID, MutableMap<PositionType, Location>>()

    fun setPosition(player: Player, positionType: PositionType, location: Location): Boolean {
        if (!regenBlockRegionTool.isSimilar(player.inventory.itemInMainHand)) return false
        val playerPositions = regenBlockPositionMap.computeIfAbsent(player.uniqueId) { mutableMapOf() }
        playerPositions[positionType] = location
        player.sendMessage("§d${positionType.displayName} 위치가 설정되었습니다. (${location.blockX}, ${location.blockY}, ${location.blockZ})")
        return true
    }

    fun removePosition(player: Player) {
        regenBlockPositionMap.remove(player.uniqueId)
    }

    fun getRegenBlockRegionTool(): ItemStack {
        return regenBlockRegionTool.clone()
    }

    fun createRegenBlockRegion(player: Player, key: String) {
        val regenBlockPositionMap = regenBlockPositionMap[player.uniqueId] ?: run {
            player.sendMessage("§c리젠블럭 구역 지정 도구를 이용하여 구역을 지정해주세요.")
            return
        }
        val firstPosition = regenBlockPositionMap[PositionType.FIRST] ?: run {
            player.sendMessage("§c${PositionType.FIRST.displayName} 위치가 설정되지 않았습니다.")
            return
        }
        val secondPosition = regenBlockPositionMap[PositionType.SECOND] ?: run {
            player.sendMessage("§c${PositionType.SECOND.displayName} 위치가 설정되지 않았습니다.")
            return
        }
        if (firstPosition.world != secondPosition.world) {
            player.sendMessage("§c두 위치가 같은 월드에 있어야 합니다.")
            return
        }
        if (regenBlockRegionRegistry.isRegenBlockRegion(key)) {
            player.sendMessage("§c이미 존재하는 리젠블럭 구역입니다.")
            return
        }
        val regenBlockRegion = RegenBlockRegion.of(firstPosition, secondPosition)
        if (isRegenBlockRegionColliding(regenBlockRegion)) {
            player.sendMessage("§c이미 존재하는 리젠블럭 구역과 겹칩니다.")
            return
        }
        removePosition(player)
        regenBlockRegionRegistry.setRegenBlockRegion(key, regenBlockRegion)
        regenBlockRegionJson.save()
        player.sendMessage("§a$key 리젠블럭 구역이 생성되었습니다.")
    }

    private fun isRegenBlockRegionColliding(regenBlockRegion: RegenBlockRegion): Boolean {
        return regenBlockRegionRegistry.getRegenBlockRegions().any { it.isColliding(regenBlockRegion) }
    }

    fun removeRegenBlockRegion(key: String) {
        regenBlockRegionRegistry.removeRegenBlockRegion(key)
        regenBlockRegionJson.save()
    }

    fun openRegenBlockRegionList(player: Player) {
        RegenBlockRegionListView(regenBlockRegionRegistry).open(player)
    }

    fun setOreGroupKey(regenBlockRegion: RegenBlockRegion, oreGroupKey: String) {
        regenBlockRegion.setOreHolderKey(oreGroupKey)
        regenBlockRegionRegistry.isChanged = true
        regenBlockRegionJson.save()
    }
}