package me.hwiggy.shoelace.api.messages.transformer.string.impl

import me.hwiggy.shoelace.api.messages.transformer.string.StringMatchTransformer

/**
 * A PatternTransformer that inserts a string of spaces
 * Parameters:
 *  length: How many spaces the line should contain (default 80)
 *
 *  @author Hunter Wignall
 */
object StringBlankTransformer : StringMatchTransformer("blank") {
    override fun matchReplacement(vararg args: String): String {
        val length = args.firstOrNull()?.toIntOrNull() ?: 80
        return " ".repeat(length)
    }
}