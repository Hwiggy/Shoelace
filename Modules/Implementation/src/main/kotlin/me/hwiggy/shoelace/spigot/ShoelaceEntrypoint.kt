package me.hwiggy.shoelace.spigot

import me.hwiggy.shoelace.spigot.utility.SerializedInventory
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin

class ShoelaceEntrypoint : JavaPlugin() {
    override fun onEnable() {
        ConfigurationSerialization.registerClass(SerializedInventory::class.java)
    }
}