package me.hwiggy.shoelace.spigot.itemizer.inventory

import org.bukkit.Bukkit
import org.bukkit.inventory.InventoryHolder

class InventoryBuilder(
    private val size: Int,
    private val title: String? = null,
    private val configurator: InventoryManipulator.() -> Unit = { }
) {
    @JvmOverloads
    fun build(holder: InventoryHolder? = null) = when {
        title != null -> Bukkit.createInventory(holder, size, title)
        else -> Bukkit.createInventory(holder, size)
    }.also { InventoryManipulator().also(configurator).applyTo(it) }
}