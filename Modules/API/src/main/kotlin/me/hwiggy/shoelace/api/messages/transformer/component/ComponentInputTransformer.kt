package me.hwiggy.shoelace.api.messages.transformer.component

import me.hwiggy.shoelace.api.messages.transformer.InputTransformer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

abstract class ComponentInputTransformer(name: String): InputTransformer<Array<out BaseComponent>>(name) {
    // Component to text conversion often introduces preceding color codes
    override val startOnly = false
    final override fun stringValue(input: Array<out BaseComponent>): String = TextComponent.toLegacyText(*input)
    final override fun removeMatch(input: Array<out BaseComponent>, match: MatchResult): Array<out BaseComponent> {
        return input.map {
            val content = it.toPlainText()
            val withoutMatch = content.replace(match.value, "")
            TextComponent(withoutMatch).also { new ->
                new.copyFormatting(it, true)
            }
        }.toTypedArray()
    }
}