package me.hwiggy.shoelace.spigot.itemizer.menu

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.event.inventory.InventoryClickEvent

abstract class ActionableMenu : Menu {
    final override fun onClick(event: InventoryClickEvent) {
        val stack = event.currentItem ?: return
        val item = NBTItem(stack)
        event.isCancelled = if (item.hasKey(KEY)) actionClick(item.getString(KEY), event) else genericClick(event)
    }

    abstract fun actionClick(action: String, event: InventoryClickEvent): Boolean
    abstract fun genericClick(event: InventoryClickEvent): Boolean

    companion object {
        const val KEY = "menu:action"
    }
}