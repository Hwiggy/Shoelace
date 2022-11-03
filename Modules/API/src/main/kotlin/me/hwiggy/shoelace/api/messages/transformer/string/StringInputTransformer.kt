package me.hwiggy.shoelace.api.messages.transformer.string

import me.hwiggy.shoelace.api.messages.transformer.InputTransformer

abstract class StringInputTransformer(name: String) : InputTransformer<String>(name) {
    final override fun stringValue(input: String) = input
    final override fun removeMatch(input: String, match: MatchResult) =
        input.removeRange(match.range)
}