package me.hwiggy.shoelace.spigot.utility

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

class SerializedInventory(
    val armor: String,
    val items: String
) {
    operator fun component1() = armor
    operator fun component2() = items

    companion object {
        fun from(configuration: ConfigurationSection) =
            SerializedInventory(configuration["armor"] as String, configuration["items"] as String)
    }
}
object InventorySerializer {
    private val encoder = Base64.getEncoder()
    private val decoder = Base64.getDecoder()

    fun invToBase64(inventory: PlayerInventory) : SerializedInventory {
        val armor = inventory.armorContents.let(::itemsToBase64)
        val items = inventory.contents.let(::itemsToBase64)
        return SerializedInventory(armor, items)
    }

    fun base64ToInv(
        inventory: SerializedInventory,
        destination: PlayerInventory
    ) {
        val (armorData, itemData) = inventory
        val armor = armorData.let(::base64ToItems)
        val items = itemData.let(::base64ToItems)
        destination.setArmorContents(armor)
        destination.contents = items
    }

    private fun itemsToBase64(items: Array<ItemStack>) = ByteArrayOutputStream().use { byteOutput ->
        BukkitObjectOutputStream(byteOutput).use { objOutput ->
            objOutput.writeInt(items.size)
            items.forEach(objOutput::writeObject)
            encoder.encodeToString(byteOutput.toByteArray())
        }
    }

    private fun base64ToItems(data: String): Array<ItemStack?> {
        val inputData = decoder.decode(data)
        val inputStream = ByteArrayInputStream(inputData)
        val objInput = BukkitObjectInputStream(inputStream)
        val array = Array<ItemStack?>(objInput.readInt()) { null }
        (array.indices).forEach {
            array[it] = objInput.readObject() as ItemStack?
        }
        objInput.close()
        return array
    }
}