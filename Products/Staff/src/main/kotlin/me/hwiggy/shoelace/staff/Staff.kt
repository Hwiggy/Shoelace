package me.hwiggy.shoelace.staff

import me.hwiggy.regroup.spigot.yml.YamlResource
import me.hwiggy.shoelace.spigot.bootstrap.PluginBootstrap
import me.hwiggy.shoelace.staff.commands.StaffCommand
import me.hwiggy.shoelace.staff.utility.TOTPAuthentication
import me.hwiggy.shoelace.staff.database.LocalStorageProvider
import me.hwiggy.shoelace.staff.database.MySQLStorageProvider
import org.bukkit.configuration.ConfigurationSection
import java.nio.file.Path
import java.util.*

class Staff : PluginBootstrap() {
    private lateinit var totp: TOTPAuthentication
    override fun onEnable() {
        val config = resources.loadFromJarThrowing(YamlResource, Path.of("config.yml"))
        val storage = when (config["storage.type"]) {
            "mysql" -> MySQLStorageProvider(config["storage.config"] as ConfigurationSection)
            else -> LocalStorageProvider(
                resources, config["storage.config"] as ConfigurationSection
            )
        }
        this.totp = TOTPAuthentication(storage, config["authentication"] as ConfigurationSection)

        commands.register(
            StaffCommand(this, config, storage, totp, messageCache[Locale("EN_US")], 5)
        )
    }
}