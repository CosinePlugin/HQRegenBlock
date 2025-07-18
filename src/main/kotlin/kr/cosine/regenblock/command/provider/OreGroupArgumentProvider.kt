package kr.cosine.regenblock.command.provider

import kr.cosine.regenblock.command.argument.OreGroupArgument
import kr.cosine.regenblock.registry.OreGroupRegistry
import kr.hqservice.framework.command.CommandArgumentProvider
import kr.hqservice.framework.command.CommandContext
import kr.hqservice.framework.command.argument.exception.ArgumentFeedback
import kr.hqservice.framework.global.core.component.Component
import org.bukkit.Location

@Component
class OreGroupArgumentProvider(
    private val oreGroupRegistry: OreGroupRegistry
) : CommandArgumentProvider<OreGroupArgument> {
    override suspend fun cast(context: CommandContext, argument: String?): OreGroupArgument {
        if (argument == null) {
            throw ArgumentFeedback.Message("그룹을 입력해주세요.")
        }
        val oreGroup = oreGroupRegistry.findOreGroup(argument)
            ?: throw ArgumentFeedback.Message("존재하지 않는 광석 그룹입니다.")
        return OreGroupArgument(argument, oreGroup)
    }

    override suspend fun getTabComplete(context: CommandContext, location: Location?): List<String> {
        return oreGroupRegistry.getOreGroupKeys()
    }
}