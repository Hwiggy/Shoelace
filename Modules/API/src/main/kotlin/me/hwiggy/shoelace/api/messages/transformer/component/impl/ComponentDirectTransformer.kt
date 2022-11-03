package me.hwiggy.shoelace.api.messages.transformer.component.impl

import me.hwiggy.shoelace.api.messages.transformer.component.ComponentMatchTransformer
import net.md_5.bungee.api.chat.TextComponent

/**
 * A PatternTransformer that replaces a pattern with a static value
 *
 * @author Hunter Wignall
 */
class ComponentDirectTransformer(
    name: String,
    private val value: String
) : ComponentMatchTransformer(name) {
    override fun matchReplacement(vararg args: String) = TextComponent.fromLegacyText(value)
}