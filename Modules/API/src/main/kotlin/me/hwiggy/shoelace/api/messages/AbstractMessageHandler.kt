package me.hwiggy.shoelace.api.messages

import me.hwiggy.shoelace.api.messages.transformer.component.ComponentTransformers
import me.hwiggy.shoelace.api.messages.transformer.component.impl.ComponentDirectTransformer

abstract class AbstractMessageHandler<R> protected constructor(
    private val messageRetriever: (String) -> String?,
    private val listMessageRetriever: (String) -> List<String>
) {
    private val transformers = ComponentTransformers {
        register(ComponentDirectTransformer("prefix", getMessage("PREFIX")))
    }

    private fun getMessage(path: String): String = requireNotNull(messageRetriever(path)) {
        "Could not load message at path: $path !"
    }

    private fun getListMessage(path: String): List<String> = listMessageRetriever(path)

    @JvmOverloads
    fun getFormattedMessage(
        path: String, transform: (String) -> String = { it }
    ) = formatMessage(getMessage(path).let(transform))

    @JvmOverloads
    fun getFormattedListMessage(
        path: String, transform: (String) -> String = { it }
    ) = formatMessage(getListMessage(path).joinToString("\n").let(transform))

    fun formatMessage(toFormat: String) = transformers.apply(toFormat)

    abstract fun sendMessage(
        recipient: R,
        path: String,
        transform: (String) -> String
    )
    fun sendMessage(recipient: R, path: String) = sendMessage(recipient, path) { it }

    abstract fun sendListMessage(
        recipient: R,
        path: String,
        transform: (String) -> String
    )
    fun sendListMessage(recipient: R, path: String) = sendListMessage(recipient, path) { it }
}