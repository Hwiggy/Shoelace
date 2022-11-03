package me.hwiggy.shoelace.api.messages.transformer.component.impl

import me.hwiggy.shoelace.api.messages.Messages
import me.hwiggy.shoelace.api.messages.transformer.component.ComponentInputTransformer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

object ComponentCenterTransformer : ComponentInputTransformer("center") {
    override fun inputReplacement(
        input: Array<out BaseComponent>,
        vararg args: String
    ): Array<out BaseComponent> {
        val asString = stringValue(input)
        val maxWidth = args.firstOrNull()?.toIntOrNull() ?: Messages.MAX_WIDTH
        val padding = Messages.getCenterSpacing(asString, maxWidth)
        val padComponent = TextComponent.fromLegacyText(padding)
        return padComponent + input
    }

}