package me.hwiggy.shoelace.api.messages

import me.hwiggy.shoelace.api.messages.transformer.component.ComponentTransformers
import me.hwiggy.shoelace.api.messages.transformer.component.impl.ComponentDirectTransformer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

abstract class AbstractMessageHandler<Recipient> protected constructor() {
    private val componentTransformers by lazy {
        ComponentTransformers {
            register(ComponentDirectTransformer("prefix", getContent("PREFIX") ?: return@ComponentTransformers))
        }
    }
    abstract fun getContent(path: String): String?

    fun getFormattedContent(
        path: String,
        transform: (String) -> String
    ) = (getContent(path) ?: "").let(transform).let(TextComponent::fromLegacyText).let(componentTransformers::apply)

    fun getFormattedLegacy(path: String, transform: (String) -> String): String {
        return getFormattedContent(path, transform).toLegacyText()
    }
    fun getFormattedContent(path: String) = getFormattedContent(path) { it }

    fun getFormattedLegacy(path: String) = getFormattedLegacy(path) { it }

    abstract fun sendMessage(
        recipient: Recipient,
        path: String,
        transform: (String) -> String = { it }
    )

    fun Array<out BaseComponent>.toLegacyText(): String = TextComponent.toLegacyText(*this)
}