package me.hwiggy.shoelace.api.messages.transformer.component.impl

import me.hwiggy.shoelace.api.messages.Messages
import me.hwiggy.shoelace.api.messages.transformer.component.ComponentInputTransformer
import me.hwiggy.shoelace.api.messages.transformer.string.impl.StringLineTransformer
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

object ComponentHeaderTransformer : ComponentInputTransformer("header") {
    override fun inputReplacement(
        input: Array<out BaseComponent>,
        vararg args: String
    ): Array<out BaseComponent> {
        val content = stringValue(input)
        val code = args.firstOrNull()?.first()?.let(ChatColor::getByChar)?.toString() ?: ""
        val padding = Messages.getCenterSpacing(content)
        val line = "$code${StringLineTransformer.matchReplacement((padding.length).toString())}".let(
            Messages::colorText
        )
        val lineComponent = TextComponent.fromLegacyText(line)
        return lineComponent + input + lineComponent
    }
}