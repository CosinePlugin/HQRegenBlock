package kr.cosine.regenblock.command

import kr.cosine.regenblock.configuration.RegenBlockConfiguration
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.command.CommandSender

@Command(label = "리젠블럭관리", isOp = true)
class RegenBlockAdminCommand(
    private val regenBlockConfiguration: RegenBlockConfiguration,
) {
    @CommandExecutor("리로드", "config.yml을 리로드합니다.", priority = 4)
    fun reload(sender: CommandSender) {
        regenBlockConfiguration.reload()
        sender.sendMessage("§aconfig.yml을 리로드하였습니다.")
    }
}