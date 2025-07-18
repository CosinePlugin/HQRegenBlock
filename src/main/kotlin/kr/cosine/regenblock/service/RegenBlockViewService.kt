package kr.cosine.regenblock.service

import kr.cosine.regenblock.data.OreGroup
import kr.cosine.regenblock.json.OreJson
import kr.cosine.regenblock.registry.OreGroupRegistry
import kr.cosine.regenblock.view.impl.OreSettingView
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.entity.Player

@Service
class RegenBlockViewService(
    private val oreJson: OreJson,
    private val oreGroupRegistry: OreGroupRegistry
) {
    fun openOreSettingView(player: Player, oreGroup: OreGroup) {
        OreSettingView(oreJson, oreGroupRegistry, oreGroup).open(player)
    }
}