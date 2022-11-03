package me.hwiggy.shoelace.api.messages.transformer.string.impl

import me.hwiggy.shoelace.api.messages.transformer.string.StringMatchTransformer

/**
 * A PatternTransformer that inserts a line of a specific width
 *
 * Parameters:
 *  See [StringBlankTransformer]
 *
 *  @author Hunter Wignall
 */
object StringLineTransformer : StringMatchTransformer("line") {
    override fun matchReplacement(vararg args: String) =
        "&m${StringBlankTransformer.matchReplacement(*args)}&r"
}