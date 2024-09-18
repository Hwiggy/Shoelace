package me.hwiggy.shoelace.spigot.itemizer.menu

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.inventory.InventoryHolder
import org.bukkit.plugin.Plugin

interface Menu : InventoryHolder {
    fun onOpen(event: InventoryOpenEvent) = Unit
    fun onClose(event: InventoryCloseEvent) = Unit
    fun onClick(event: InventoryClickEvent) = Unit
    fun onDrag(event: InventoryDragEvent) = Unit

    object EventListener : Listener {
        private lateinit var owner: Plugin
        private fun <T : InventoryEvent> handle(
            event: T, handler: (Menu, T) -> Unit
        ) {
            handler(event.inventory.holder as? Menu ?: return, event)
        }

        @EventHandler
        private fun onOpen(event: InventoryOpenEvent) = handle(event, Menu::onOpen)

        @EventHandler
        private fun onClose(event: InventoryCloseEvent) = handle(event, Menu::onClose)

        @EventHandler
        private fun onClick(event: InventoryClickEvent) = handle(event, Menu::onClick)

        @EventHandler
        private fun onDrag(event: InventoryDragEvent) = handle(event, Menu::onDrag)

        fun register(forWho: Plugin) {
            if (this::owner.isInitialized) throw IllegalStateException(
                "Already registered by plugin ${forWho.name}!"
            ) else forWho.also {
                Bukkit.getPluginManager().registerEvents(this, it)
            }.also(this::owner::set)
        }
    }
}