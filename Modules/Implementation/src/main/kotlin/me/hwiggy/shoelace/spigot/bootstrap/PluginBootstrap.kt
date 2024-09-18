package me.hwiggy.shoelace.spigot.bootstrap

import me.hwiggy.kommander.spigot.ReflectiveCommandHandler
import me.hwiggy.regroup.spigot.yml.YamlResource
import me.hwiggy.shoelace.spigot.messages.SpigotMessageHandler
import me.hwiggy.shoelace.spigot.resource.PluginResourceManager
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Path

open class PluginBootstrap : JavaPlugin() {
    protected val commands = ReflectiveCommandHandler(this)
    protected val resources = PluginResourceManager(this)
    private val messageGroup = resources.group(YamlResource, Path.of("messages"))
    protected val messageCache = SpigotMessageHandler.Cache(messageGroup::get)
}