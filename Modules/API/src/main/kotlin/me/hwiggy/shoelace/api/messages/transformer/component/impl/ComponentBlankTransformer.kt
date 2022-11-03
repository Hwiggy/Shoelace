package me.hwiggy.shoelace.api.messages.transformer.component.impl

import me.hwiggy.shoelace.api.messages.transformer.component.ComponentMatchTransformer
import me.hwiggy.shoelace.api.messages.transformer.string.impl.StringBlankTransformer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

object ComponentBlankTransformer : ComponentMatchTransformer("blank") {
    override fun matchReplacement(vararg args: String): Array<out BaseComponent> =
        StringBlankTransformer.matchReplacement(*args).let {
            TextComponent.fromLegacyText(it)
        }

}