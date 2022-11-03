package me.hwiggy.shoelace.api.messages.transformer.component

import me.hwiggy.shoelace.api.messages.Messages
import me.hwiggy.shoelace.api.messages.transformer.MatchTransformer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

abstract class ComponentMatchTransformer(name: String): MatchTransformer<Array<out BaseComponent>>(name) {
    final override fun stringValue(input: Array<out BaseComponent>): String = TextComponent.toLegacyText(*input)
    final override fun transformPart(part: String): Array<out BaseComponent> = TextComponent.fromLegacyText(Messages.colorText(part))
    final override fun combine(parts: Collection<Array<out BaseComponent>>) =
        parts.flatMap(Array<out BaseComponent>::asSequence).toTypedArray()
}