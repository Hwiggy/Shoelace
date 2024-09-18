package me.hwiggy.shoelace.spigot.resource

import me.hwiggy.regroup.api.Resource
import org.bukkit.plugin.java.JavaPlugin

class PluginResourceManager(plugin: JavaPlugin) : Resource.Manager(
    plugin::class.java, plugin.dataFolder.toPath()
)