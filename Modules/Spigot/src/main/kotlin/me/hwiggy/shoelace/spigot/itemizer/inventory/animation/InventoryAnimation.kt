package me.hwiggy.shoelace.spigot.itemizer.inventory.animation

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

abstract class InventoryAnimation protected constructor() : Iterator<Frame>, Listener {
    protected val frames = ArrayList<Frame>()
    protected var index = -1

    fun start(owner: Plugin, holder: InventoryHolder) {
        holder.inventory.also {
            animations[it] = Task(owner, it).apply(Task::start)
        }
        Bukkit.getPluginManager().registerEvents(this, owner)
    }

    fun frames(vararg frame: Frame) = frames.addAll(frame)

    @EventHandler
    private fun onClose(event: InventoryCloseEvent) {
        val inventory = event.inventory
        if (inventory.viewers.isNotEmpty()) return
        animations.remove(inventory)?.cancel()
    }

    companion object {
        private val animations = IdentityHashMap<Inventory, Task>()

        @JvmStatic
        fun <T : InventoryAnimation> create(
            constructor: () -> T, configurator: T.() -> Unit
        ) = constructor().also(configurator)
    }

    private inner class Task(
        private val owner: Plugin,
        private val inventory: Inventory
    ) : BukkitRunnable() {
        private lateinit var lastFrame: Frame
        override fun run() {
            if (!this::lastFrame.isInitialized || !lastFrame.shouldRepeat()) {
                if (!hasNext()) return cancel()
                lastFrame = next()
            }
            lastFrame.renderInto(inventory)
            runTaskLater(owner, lastFrame.displayTicks)
        }

        fun start() = runTask(owner)
    }
}

class OneshotAnimation : InventoryAnimation() {
    override fun hasNext() = index < frames.size
    override fun next() = frames[++index]
}

class LoopingAnimation : InventoryAnimation() {
    override fun hasNext() = true
    override fun next(): Frame {
        if (++index >= frames.size) index = 0
        return frames[index]
    }
}