package me.hwiggy.shoelace.sleeping

import me.hwiggy.regroup.spigot.yml.YamlResource
import me.hwiggy.shoelace.spigot.bootstrap.PluginBootstrap
import me.hwiggy.shoelace.spigot.events.Events
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerBedLeaveEvent
import org.bukkit.scheduler.BukkitTask
import java.nio.file.Path
import java.util.*
import kotlin.math.ceil


class Sleeping : PluginBootstrap() {
    override fun onEnable() {
        val config = resources.loadFromJarThrowing(YamlResource, Path.of("config.yml"))
        val threshold = config.getDouble("THRESHOLD")
        val timeToAdvance = config.getInt("TIME_TO_ADVANCE")

        val bedCounts = mutableMapOf<String, Double>()
        val skipTasks = mutableMapOf<String, BukkitTask?>()

        Events.listen<PlayerBedEnterEvent>(this) { event ->
            if (event.bedEnterResult != PlayerBedEnterEvent.BedEnterResult.OK) return@listen
            val world = event.bed.world
            val worldName = world.name

            val inBed = bedCounts.compute(worldName) { _, v ->
                (v ?: 0.0) + 1.0
            }!!

            val playersInWorld = world.players.size
            val needed = (threshold / 100) * playersInWorld
            val sleeping = (inBed / playersInWorld) * 100

            if (skipTasks[worldName] == null) {
                world.players.forEach {
                    val messages = messageCache[Locale(it.locale)]
                    messages.sendMessage(it, "SLEEPING.ENTERED_BED") {
                        it.replace("<player>", event.player.name)
                            .replace("<sleeping>", inBed.toInt().toString())
                            .replace("<threshold>", ceil(needed).toInt().toString())
                    }
                }
            }

            if (sleeping < threshold) return@listen
            if (skipTasks[worldName] != null) return@listen

            world.players.forEach {
                val messages = messageCache[Locale(it.locale)]
                messages.sendMessage(it, "SLEEPING.SKIP_TO_DAY")
            }

            val diff = 24000 - world.time
            val delta = diff / (timeToAdvance * 20.0)

            val task = Bukkit.getScheduler().runTaskTimer(this, Runnable {
                world.time += delta.toInt()
            }, 0L, 1L)

            skipTasks[worldName] = task

            Bukkit.getScheduler().runTaskLater(this, Runnable {
                task.cancel()
                skipTasks[worldName] = null
            }, timeToAdvance * 20L)
        }

        Events.listen<PlayerBedLeaveEvent>(this) { event ->
            val world = event.bed.world
            val worldName = world.name

            val inBed = bedCounts.compute(worldName) { _, v ->
                ((v ?: 1.0) - 1.0).coerceAtLeast(0.0)
            }!!

            val playersInWorld = world.players.size
            val needed = (threshold / 100) * playersInWorld

            if (skipTasks[worldName] == null) {
                world.players.forEach { player ->
                    val messages = messageCache[Locale(player.locale)]
                    messages.sendMessage(player, "SLEEPING.LEFT_BED") {
                        it.replace("<player>", event.player.name)
                            .replace("<sleeping>", inBed.toInt().toString())
                            .replace("<threshold>", ceil(needed).toInt().toString())
                    }
                }
            }
        }
    }
}