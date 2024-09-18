package me.hwiggy.shoelace.api.messages.transformer.component

import me.hwiggy.shoelace.api.messages.transformer.MessageTransformers
import me.hwiggy.shoelace.api.messages.transformer.PatternTransformer
import me.hwiggy.shoelace.api.messages.transformer.component.impl.ComponentBlankTransformer
import me.hwiggy.shoelace.api.messages.transformer.component.impl.ComponentCenterTransformer
import me.hwiggy.shoelace.api.messages.transformer.component.impl.ComponentHeaderTransformer
import me.hwiggy.shoelace.api.messages.transformer.component.impl.ComponentLineTransformer
import net.md_5.bungee.api.chat.BaseComponent

class ComponentTransformers @JvmOverloads constructor(
    init: MessageTransformers<Array<out BaseComponent>>.() -> Unit = { }
) : MessageTransformers<Array<out BaseComponent>>(init) {
    companion object {
        private val globals = ComponentTransformers {
            register(ComponentBlankTransformer)
            register(ComponentHeaderTransformer)
            register(ComponentCenterTransformer)
            register(ComponentLineTransformer)
        }

        @JvmStatic fun registerGlobal(transformer: PatternTransformer<Array<out BaseComponent>>) {
            globals.register(transformer)
        }
    }

    override fun preprocess(input: Array<out BaseComponent>) = input
    override fun globals() = globals
}