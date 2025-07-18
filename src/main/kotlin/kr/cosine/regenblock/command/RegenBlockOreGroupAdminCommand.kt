package kr.cosine.regenblock.command

import kr.cosine.regenblock.command.argument.OreGroupArgument
import kr.cosine.regenblock.service.RegenBlockOreService
import kr.cosine.regenblock.service.RegenBlockViewService
import kr.hqservice.framework.command.ArgumentLabel
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(label = "광석그룹", parent = RegenBlockAdminCommand::class)
class RegenBlockOreGroupAdminCommand(
    private val regenBlockOreService: RegenBlockOreService,
    private val regenBlockViewService: RegenBlockViewService
) {
    @CommandExecutor("생성", "광석 그룹을 생성합니다.", priority = 1)
    fun createOreGroup(
        sender: CommandSender,
        @ArgumentLabel("이름") key: String
    ) {
        if (regenBlockOreService.createOreGroup(key)) {
            sender.sendMessage("§a$key 광석 그룹을 생성하였습니다.")
        } else {
            sender.sendMessage("§c이미 존재하는 광석 그룹입니다.")
        }
    }

    @CommandExecutor("제거", "광석 그룹을 제거합니다.", priority = 2)
    fun deleteOreGroup(
        sender: CommandSender,
        @ArgumentLabel("광석그룹") oreGroupArgument: OreGroupArgument
    ) {
        val key = oreGroupArgument.key
        regenBlockOreService.deleteOreGroup(key)
        sender.sendMessage("§a$key 광석 그룹을 제거하였습니다.")
    }

    @CommandExecutor("설정", "광석 그룹 설정 화면을 오픈합니다.", priority = 3)
    fun openOreSettingView(
        player: Player,
        @ArgumentLabel("광석그룹") oreGroupArgument: OreGroupArgument
    ) {
        regenBlockViewService.openOreSettingView(player, oreGroupArgument.oreGroup)
    }
}