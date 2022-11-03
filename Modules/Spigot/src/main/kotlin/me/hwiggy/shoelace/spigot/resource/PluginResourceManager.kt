package me.hwiggy.shoelace.spigot.resource

import me.hwiggy.shoelace.api.resources.AbstractResourceManager
import me.hwiggy.shoelace.api.resources.ResourceLoader
import me.hwiggy.shoelace.api.resources.ResourceSaver
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin

class PluginResourceManager(plugin: JavaPlugin) : AbstractResourceManager(
    plugin::class.java, plugin.dataFolder.toPath()
) {
    companion object {
        @JvmStatic val YAML_LOADER = ResourceLoader(YamlConfiguration::loadConfiguration)
        @JvmStatic val YAML_SAVER = ResourceSaver(YamlConfiguration::save)
    }
}