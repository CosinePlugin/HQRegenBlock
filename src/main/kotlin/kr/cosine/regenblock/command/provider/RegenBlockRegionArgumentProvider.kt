package kr.cosine.regenblock.command.provider

import kr.cosine.regenblock.command.argument.RegenBlockRegionArgument
import kr.cosine.regenblock.registry.RegenBlockRegionRegistry
import kr.hqservice.framework.command.CommandArgumentProvider
import kr.hqservice.framework.command.CommandContext
import kr.hqservice.framework.command.argument.exception.ArgumentFeedback
import kr.hqservice.framework.global.core.component.Component
import org.bukkit.Location

@Component
class RegenBlockRegionArgumentProvider(
    private val regenBlockRegionRegistry: RegenBlockRegionRegistry
) : CommandArgumentProvider<RegenBlockRegionArgument> {
    override suspend fun cast(context: CommandContext, argument: String?): RegenBlockRegionArgument {
        if (argument == null) {
            throw ArgumentFeedback.Message("구역을 입력해주세요.")
        }
        val regenBlockRegion = regenBlockRegionRegistry.findRegenBlockRegion(argument)
            ?: throw ArgumentFeedback.Message("존재하지 않는 구역입니다.")
        return RegenBlockRegionArgument(argument, regenBlockRegion)
    }

    override suspend fun getTabComplete(context: CommandContext, location: Location?): List<String> {
        return regenBlockRegionRegistry.getRegenBlockRegionKeys()
    }
}