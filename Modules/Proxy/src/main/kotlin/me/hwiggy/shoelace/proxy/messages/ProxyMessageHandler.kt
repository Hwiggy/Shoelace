package me.hwiggy.shoelace.proxy.messages

import me.hwiggy.shoelace.api.messages.AbstractMessageHandler
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.config.Configuration

class ProxyMessageHandler(
    config: Configuration
) : AbstractMessageHandler<CommandSender>(
    config::getString, config::getStringList
) {
    override fun sendMessage(recipient: CommandSender, path: String, transform: (String) -> String) {
        recipient.sendMessage(*getFormattedMessage(path, transform))
    }

    override fun sendListMessage(recipient: CommandSender, path: String, transform: (String) -> String) {
        recipient.sendMessage(*getFormattedListMessage(path, transform))
    }
}