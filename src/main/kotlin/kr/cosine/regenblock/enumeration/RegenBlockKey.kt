package kr.cosine.regenblock.enumeration

import kr.cosine.regenblock.data.Key

enum class RegenBlockKey : Key {
    ORE_ITEM_META;

    override val parent = "regen_block"

    override val child = name.lowercase()
}