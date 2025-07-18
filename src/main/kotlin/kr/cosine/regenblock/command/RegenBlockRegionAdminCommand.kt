package kr.cosine.regenblock.command

import kr.cosine.regenblock.command.argument.OreGroupArgument
import kr.cosine.regenblock.command.argument.RegenBlockRegionArgument
import kr.cosine.regenblock.service.RegenBlockOreService
import kr.cosine.regenblock.service.RegenBlockRegionService
import kr.hqservice.framework.command.ArgumentLabel
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(label = "구역", parent = RegenBlockAdminCommand::class)
class RegenBlockRegionAdminCommand(
    private val regenBlockOreService: RegenBlockOreService,
    private val regenBlockRegionService: RegenBlockRegionService
) {
    @CommandExecutor("도구", "리젠블럭 구역 지정 도구를 지급합니다.", priority = 1)
    fun giveRegenBlockRegionTool(player: Player) {
        val itemStack = regenBlockRegionService.getRegenBlockRegionTool()
        player.inventory.addItem(itemStack)
        player.sendMessage("§a리젠블럭 구역 지정 도구를 지급하였습니다.")
    }

    @CommandExecutor("생성", "리젠블럭 구역을 생성합니다.", priority = 2)
    fun createRegenBlockRegion(
        player: Player,
        @ArgumentLabel("이름") key: String
    ) {
        regenBlockRegionService.createRegenBlockRegion(player, key)
    }

    @CommandExecutor("제거", "리젠블럭 구역을 제거합니다.", priority = 3)
    fun deleteRegenBlockRegion(
        sender: CommandSender,
        @ArgumentLabel("구역") regenBlockRegionArgument: RegenBlockRegionArgument
    ) {
        val key = regenBlockRegionArgument.key
        regenBlockRegionService.removeRegenBlockRegion(key)
        sender.sendMessage("§a$key 리젠블럭 구역을 삭제하였습니다.")
    }

    @CommandExecutor("광석그룹설정", "리젠블럭 구역의 광석 그룹을 설정합니다.", priority = 4)
    fun setOreGroupKey(
        sender: CommandSender,
        @ArgumentLabel("구역") regenBlockRegionArgument: RegenBlockRegionArgument,
        @ArgumentLabel("광석그룹") oreGroupArgument: OreGroupArgument
    ) {
        val oreGroupKey = oreGroupArgument.key
        regenBlockRegionService.setOreGroupKey(regenBlockRegionArgument.regenBlockRegion, oreGroupKey)
        sender.sendMessage("§a${regenBlockRegionArgument.key} 리젠블럭 구역의 광석 그룹을 $oreGroupKey(으)로 설정하였습니다.")
    }

    @CommandExecutor("광석그룹확인", "리젠블럭 구역의 광석 그룹을 확인합니다.", priority = 5)
    fun showOreGroupKey(
        sender: CommandSender,
        @ArgumentLabel("구역") regenBlockRegionArgument: RegenBlockRegionArgument
    ) {
        val regenBlockRegionKey = regenBlockRegionArgument.key
        val oreGroupKey = regenBlockRegionArgument.regenBlockRegion.findOreGroupKey() ?: run {
            sender.sendMessage("§c${regenBlockRegionArgument.key} 리젠블럭 구역에 설정된 광석 그룹이 없습니다.")
            return
        }
        sender.sendMessage("§a${regenBlockRegionKey} 리젠블럭 구역의 광석 그룹: $oreGroupKey")
    }

    @CommandExecutor("목록", "리젠블럭 구역 목록을 확인합니다.", priority = 6)
    fun openRegenBlockRegionList(player: Player) {
        regenBlockRegionService.openRegenBlockRegionList(player)
    }
}