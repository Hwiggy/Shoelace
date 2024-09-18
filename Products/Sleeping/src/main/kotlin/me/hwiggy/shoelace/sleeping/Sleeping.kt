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

        var skipTask: BukkitTask? = null
        val bedCounts = HashMap<String, Double>()
        Events.listen<PlayerBedEnterEvent>(this) { event ->
            if (event.bedEnterResult != PlayerBedEnterEvent.BedEnterResult.OK) return@listen
            val inBed = bedCounts.compute(event.bed.world.name) { _, v  ->
                if (v == null) 1.0 else v + 1.0
            }!!
            val playersInWorld = event.bed.world.players.size
            val needed = (threshold / 100) * playersInWorld
            val sleeping = (inBed / playersInWorld) * 100
            event.bed.world.players.forEach {
                val messages = messageCache[Locale(it.locale)]
                messages.sendMessage(it, "SLEEPING.ENTERED_BED") {
                    it.replace("<player>", event.player.name)
                        .replace("<sleeping>", inBed.toInt().toString())
                        .replace("<threshold>", ceil(needed).toInt().toString())
                }
            }
            if (sleeping < threshold) return@listen
            event.bed.world.players.forEach {
                val messages = messageCache[Locale(it.locale)]
                messages.sendMessage(it, "SLEEPING.SKIP_TO_DAY")
                val diff = 24000 - event.bed.world.time
                val delta = diff / (timeToAdvance * 20.0)
                skipTask = Bukkit.getScheduler().runTaskTimer(this, Runnable{
                    event.bed.world.time += delta.toInt()
                }, 0L, 1L)
                Bukkit.getScheduler().runTaskLater(this, Runnable {
                    skipTask!!.cancel()
                    skipTask = null
                }, timeToAdvance * 20L)
            }
        }
        Events.listen<PlayerBedLeaveEvent>(this) { event ->
            val inBed = bedCounts.compute(event.bed.world.name) { _, v ->
                if (v == null) throw IllegalStateException()
                v - 1.0
            }!!
            val playersInWorld = event.bed.world.players.size
            val needed = (threshold / 100) * playersInWorld
            if (skipTask == null) {
                event.bed.world.players.forEach { player ->
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