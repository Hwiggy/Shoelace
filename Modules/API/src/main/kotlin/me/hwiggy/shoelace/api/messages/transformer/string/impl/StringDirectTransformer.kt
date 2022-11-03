package me.hwiggy.shoelace.api.messages.transformer.string.impl

import me.hwiggy.shoelace.api.messages.transformer.string.StringMatchTransformer

/**
 * A PatternTransformer that replaces a pattern with a static value
 *
 * @author Hunter Wignall
 */
class StringDirectTransformer(
    name: String,
    private val value: String
) : StringMatchTransformer(name) {
    override fun matchReplacement(vararg args: String) = value
}