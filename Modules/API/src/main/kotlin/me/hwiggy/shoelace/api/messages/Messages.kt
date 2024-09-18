package me.hwiggy.shoelace.api.messages

import net.md_5.bungee.api.ChatColor

object Messages {
    /**
     * A constant for half of a standard chat window
     */
    const val MAX_WIDTH = 152

    /**
     * Translates '&' color codes in an input String
     *
     * @param input The String to translate '&' codes in
     * @return The translated String
     */
    @JvmStatic
    fun colorText(input: String): String = ChatColor.translateAlternateColorCodes('&', input)

    @JvmStatic
    fun stripColor(input: String): String = ChatColor.stripColor(input)

    /**
     * Returns a centered message, as calculated with [Messages.MAX_WIDTH]
     *
     * @param input The input string to center
     * @return The centered string
     */
    @JvmStatic
    @JvmOverloads
    fun getCenterSpacing(input: String, maxWidth: Int = MAX_WIDTH): String {
        val halvedMessageSize = messageWidth(input) / 2
        val toCompensate = maxWidth - halvedMessageSize
        val spaceLength = FontInfo.SPACE.width() + 1
        var compensated = 0
        val sb = StringBuilder()
        while (compensated < toCompensate) {
            sb.append(" ")
            compensated += spaceLength
        }
        return sb.toString()
    }

    @JvmStatic fun messageWidth(input: String): Int {
        val message = colorText(input)
        var messagePxSize = 0
        var previousCode = false
        var isBold = false
        for (c in message) {
            when {
                c == '§' -> previousCode = true
                previousCode -> {
                    previousCode = false
                    isBold = c == 'l'
                }
                else -> {
                    val fontInfo = FontInfo.infoFor(c)
                    messagePxSize += fontInfo.width(isBold)
                    messagePxSize++
                }
            }
        }
        return messagePxSize
    }

    @JvmStatic
    @JvmOverloads
    fun centerMessage(input: String, maxWidth: Int = MAX_WIDTH): String {
        return input.split("\n").map {
            val centerSpacing = getCenterSpacing(it, maxWidth)
            return@map "$centerSpacing$it$centerSpacing"
        }.joinToString("\n")
    }
}

fun String.insert(position: Int, other: String) = substring(0, position) + other + substring(position, length)

fun Int.formatWithUnit(unit: String, suffix: String = "s"): String = if (this == 1) "$this $unit" else "$this $unit$suffix"
