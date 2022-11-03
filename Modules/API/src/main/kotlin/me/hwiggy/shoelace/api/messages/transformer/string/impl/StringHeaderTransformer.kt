package me.hwiggy.shoelace.api.messages.transformer.string.impl

import me.hwiggy.shoelace.api.messages.Messages
import me.hwiggy.shoelace.api.messages.transformer.string.StringInputTransformer
import net.md_5.bungee.api.ChatColor

/**
 * A PatternTransformer that centers the content and applies header bars on either side
 *
 * Parameters:
 *  code - The color code for the lines (Default none)
 *
 *  @author Hunter Wignall
 */
object StringHeaderTransformer : StringInputTransformer("header") {
    override fun inputReplacement(input: String, vararg args: String): String {
        val code = args.firstOrNull()?.first()?.let(ChatColor::getByChar)?.toString() ?: ""
        val padding = Messages.getCenterSpacing(input)
        val line = "$code${StringLineTransformer.matchReplacement((padding.length).toString())}"
        return Messages.centerMessage("$line $input $line")
    }
}