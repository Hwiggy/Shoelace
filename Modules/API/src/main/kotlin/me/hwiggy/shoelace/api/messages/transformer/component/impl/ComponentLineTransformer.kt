package me.hwiggy.shoelace.api.messages.transformer.component.impl

import me.hwiggy.shoelace.api.messages.Messages
import me.hwiggy.shoelace.api.messages.transformer.component.ComponentMatchTransformer
import me.hwiggy.shoelace.api.messages.transformer.string.impl.StringLineTransformer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

object ComponentLineTransformer : ComponentMatchTransformer("line") {
    override fun matchReplacement(vararg args: String): Array<out BaseComponent>?
        = StringLineTransformer.matchReplacement(*args)
            .let(Messages::colorText)
            .let(TextComponent::fromLegacyText)
}