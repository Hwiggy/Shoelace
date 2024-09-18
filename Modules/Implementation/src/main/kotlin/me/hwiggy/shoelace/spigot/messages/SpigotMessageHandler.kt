package me.hwiggy.shoelace.spigot.messages

import me.hwiggy.shoelace.api.messages.AbstractMessageHandler
import me.hwiggy.shoelace.api.messages.Messages
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import java.util.*

class SpigotMessageHandler(
    private val config: ConfigurationSection
) : AbstractMessageHandler<CommandSender>() {

    class Cache(
        private val factory: (Locale) -> ConfigurationSection
    ) {
        private val cache = IdentityHashMap<Locale, SpigotMessageHandler>()
        operator fun get(locale: Locale = Locale.ENGLISH): SpigotMessageHandler =
            cache.computeIfAbsent(locale) {
                val config = factory(it)
                SpigotMessageHandler(config)
            }
    }

    override fun getContent(path: String): String? {
        return when (val content = config.get(path)) {
            null -> null
            is String -> content.let(Messages::colorText)
            is List<*> -> content.joinToString("\n").let(Messages::colorText)
            else -> content.toString().let(Messages::colorText)
        }
    }

    override fun sendMessage(
        recipient: CommandSender,
        path: String,
        transform: (String) -> String
    ) {
        val content = getFormattedContent(path, transform) ?: return
        recipient.spigot().sendMessage(*content)
    }
}