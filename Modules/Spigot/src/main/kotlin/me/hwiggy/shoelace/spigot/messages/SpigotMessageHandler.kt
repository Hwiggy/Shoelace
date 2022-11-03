package me.hwiggy.shoelace.spigot.messages

import me.hwiggy.shoelace.api.messages.AbstractMessageHandler
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection

class SpigotMessageHandler(
    config: ConfigurationSection
) : AbstractMessageHandler<CommandSender>(
    config::getString, config::getStringList
) {
    override fun sendMessage(recipient: CommandSender, path: String, transform: (String) -> String) {
        recipient.spigot().sendMessage(*getFormattedMessage(path, transform))
    }

    override fun sendListMessage(recipient: CommandSender, path: String, transform: (String) -> String) {
        recipient.spigot().sendMessage(*getFormattedListMessage(path, transform))
    }

}