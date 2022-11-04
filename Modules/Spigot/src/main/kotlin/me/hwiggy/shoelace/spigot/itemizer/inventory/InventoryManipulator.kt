package me.hwiggy.shoelace.spigot.itemizer.inventory

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

@Suppress("unused")
class InventoryManipulator {
    private val tasks = ArrayList<(Inventory) -> Unit>()
    private fun task(block: (Inventory) -> Unit) {
        tasks.add(block)
    }

    fun applyTo(inventory: Inventory) = tasks.forEach { it(inventory) }

    fun fill(item: ItemStack) = fill { item }
    fun fill(item: () -> ItemStack) = task {
        fill(it, 0, it.size - 1, item)
    }

    fun fill(
        c1: Int, r1: Int,
        c2: Int, r2: Int,
        item: ItemStack
    ) = fill(c1, r1, c2, r2) { item }

    fun fill(
        c1: Int, r1: Int,
        c2: Int, r2: Int,
        item: () -> ItemStack
    ) = task {
        fill(it, c1 + (r1 * 9), c2 + (r2 * 9), item)
    }

    fun fill(start: Int, end: Int, item: ItemStack) = fill(start, end) { item }
    fun fill(start: Int, end: Int, item: () -> ItemStack) = task {
        fill(it, start, end, item)
    }

    private fun fill(inventory: Inventory, start: Int, end: Int, item: () -> ItemStack) {
        (start..end).forEach { set(inventory, it, item) }
    }

    fun column(column: Int, item: ItemStack) = column(column) { item }
    fun column(column: Int, item: () -> ItemStack) = task {
        require(column in 0..8) { "Column must be between 0 and 8!" }
        var current = column
        while (current < it.size) {
            set(it, current, item)
            current += 9
        }
    }

    fun row(row: Int, item: ItemStack) = row(row) { item }
    fun row(row: Int, item: () -> ItemStack) = task {
        val maxRow = (it.size / 9) - 1
        require(row in 0..maxRow) { "Row must be between 0 and $maxRow!" }
        var current = row * 9
        while (current < row + 9) {
            set(it, current, item)
            current++
        }
    }

    fun slot(slot: Int, item: ItemStack) = slot(slot) { item }
    fun slot(slot: Int, item: () -> ItemStack) = task { set(it, slot, item) }

    private fun set(inventory: Inventory, slot: Int, item: () -> ItemStack) {
        inventory.setItem(slot, item())
    }
}