package me.hwiggy.shoelace.api.messages.transformer.string.impl

import me.hwiggy.shoelace.api.messages.Messages
import me.hwiggy.shoelace.api.messages.transformer.string.StringInputTransformer

/**
 * A PatternTransformer that centers the content
 * Parameters:
 *  maxWidth - The maximum width of the text window, used to calculate center
 *      Default: 152px
 *
 * @author Hunter Wignall
 */
object StringCenterTransformer : StringInputTransformer("center") {
    override fun inputReplacement(input: String, vararg args: String): String {
        val maxWidth = args.firstOrNull()?.toIntOrNull() ?: Messages.MAX_WIDTH
        return Messages.centerMessage(input, maxWidth)
    }
}