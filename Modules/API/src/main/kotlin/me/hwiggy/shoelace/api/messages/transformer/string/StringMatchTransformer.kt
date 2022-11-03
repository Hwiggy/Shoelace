package me.hwiggy.shoelace.api.messages.transformer.string

import me.hwiggy.shoelace.api.messages.transformer.MatchTransformer

abstract class StringMatchTransformer(name: String) : MatchTransformer<String>(name) {
    final override fun stringValue(input: String) = input
    final override fun combine(parts: Collection<String>) = parts.joinToString("")
    final override fun transformPart(part: String) = part
}