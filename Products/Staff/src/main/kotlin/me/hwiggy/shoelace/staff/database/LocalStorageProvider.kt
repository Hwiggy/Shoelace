package me.hwiggy.shoelace.staff.database

import me.hwiggy.regroup.spigot.yml.YamlResource
import me.hwiggy.shoelace.spigot.resource.PluginResourceManager
import me.hwiggy.shoelace.spigot.utility.SerializedInventory
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.MemorySection
import java.io.ObjectInputFilter.Config
import java.nio.file.Path
import java.util.*

class LocalStorageProvider(
    private val resources: PluginResourceManager,
    conf: ConfigurationSection
) : StorageProvider {

    private val fileName = (conf["filename"] as String).let(Path::of)
    private val file = resources.loadFromDiskThrowing(YamlResource, fileName)
    override fun credential(id: UUID) = file["${id}.secret"] as String?

    override fun storeCredential(id: UUID, secret: String): String {
        val data = getDataSection(id)
        data["secret"] = secret
        resources.save(YamlResource, file, fileName)
        return secret
    }

    override fun inventory(id: UUID): SerializedInventory? {
        val data = file.getConfigurationSection(id.toString()) ?: return null
        val armor = data.getString("armor") ?: ""
        val items = data.getString("items") ?: ""
        return SerializedInventory(armor, items)
    }
    override fun storeInventory(id: UUID, inventory: SerializedInventory) {
        val data = getDataSection(id)
        data["armor"] = inventory.armor
        data["items"] = inventory.items
        resources.save(YamlResource, file, fileName)
    }

    override fun clearStoredInventory(id: UUID) {
        val data = getDataSection(id)
        data["armor"] = null
        data["items"] = null
        resources.save(YamlResource, file, fileName)
    }

    private fun getDataSection(id: UUID): ConfigurationSection {
        return file.getConfigurationSection(id.toString()) ?: file.createSection(id.toString())
    }
}