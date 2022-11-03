package me.hwiggy.shoelace.api.messages.transformer.string

import me.hwiggy.shoelace.api.messages.transformer.string.impl.StringCenterTransformer
import me.hwiggy.shoelace.api.messages.transformer.string.impl.StringHeaderTransformer
import me.hwiggy.shoelace.api.messages.transformer.string.impl.StringLineTransformer
import me.hwiggy.shoelace.api.messages.Messages
import me.hwiggy.shoelace.api.messages.transformer.MessageTransformers
import me.hwiggy.shoelace.api.messages.transformer.string.impl.StringBlankTransformer

class StringTransformers @JvmOverloads constructor(
    init: MessageTransformers<String>.() -> Unit = { }
) : MessageTransformers<String>(init) {
    companion object {
        private val globals = StringTransformers {
            register(StringLineTransformer)
            register(StringCenterTransformer)
            register(StringHeaderTransformer)
            register(StringBlankTransformer)
        }
    }

    override fun globals() = globals
    override fun preprocess(input: String) = Messages.colorText(input)
}