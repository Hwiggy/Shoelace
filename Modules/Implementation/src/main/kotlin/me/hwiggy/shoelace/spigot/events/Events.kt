@file:JvmName("Events")
package me.hwiggy.shoelace.spigot.events

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin
import java.util.function.Consumer

interface Events : Listener, EventExecutor {
    fun unregister() = HandlerList.unregisterAll(this)

    companion object {
        @JvmStatic
        fun <T : Event> fireEvent(event: T) = Bukkit.getPluginManager().callEvent(event)

        @JvmOverloads
        @JvmStatic
        fun <T : Event> listen(
            plugin: Plugin,
            type: Class<T>,
            priority: EventPriority = EventPriority.NORMAL,
            listener: Consumer<T>
        ): Events {
            val events = object : Events {
                override fun execute(ignored: Listener, event: Event) {
                    if (!type.isInstance(event)) return
                    listener.accept(event as T)
                }
            }
            Bukkit.getPluginManager().registerEvent(type, events, priority, events, plugin)
            return events
        }

        inline fun <reified T : Event> listen(
            plugin: Plugin,
            priority: EventPriority,
            noinline listener: (T) -> Unit
        ) = listen(plugin, T::class.java, priority) { listener.invoke(it) }

        inline fun <reified T : Event> listen(
            plugin: Plugin,
            noinline listener: (T) -> Unit
        ) = listen(plugin, EventPriority.NORMAL, listener)
    }
}