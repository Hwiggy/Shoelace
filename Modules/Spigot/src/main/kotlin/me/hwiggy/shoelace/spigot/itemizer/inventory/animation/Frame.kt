package me.hwiggy.shoelace.spigot.itemizer.inventory.animation

import me.hwiggy.shoelace.spigot.itemizer.inventory.InventoryManipulator
import org.bukkit.inventory.Inventory

abstract class Frame {
    open val configurator: InventoryManipulator.() -> Unit = { }

    abstract val displayTicks: Long

    protected fun applyManipulator(inventory: Inventory) {
        InventoryManipulator().also(configurator).also { it.applyTo(inventory) }
    }

    open fun renderInto(inventory: Inventory) = applyManipulator(inventory)

    open fun shouldRepeat() = false
}

class OneshotFrame @JvmOverloads constructor(
    override val displayTicks: Long = 1L,
    override val configurator: InventoryManipulator.() -> Unit = { }
) : Frame() {
    override fun shouldRepeat() = false
}

class RepeatingFrame @JvmOverloads constructor(
    private var repeatAmount: Int,
    override val displayTicks: Long = 1L,
    override val configurator: InventoryManipulator.() -> Unit = { }
) : Frame() {
    override fun shouldRepeat() = repeatAmount-- > 0
}