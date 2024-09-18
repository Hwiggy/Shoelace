package me.hwiggy.shoelace.farming

import com.google.common.cache.CacheBuilder
import me.hwiggy.shoelace.spigot.bootstrap.PluginBootstrap
import me.hwiggy.shoelace.spigot.events.Events
import org.bukkit.Effect
import org.bukkit.Material.*
import org.bukkit.block.data.Ageable
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*
import java.util.concurrent.TimeUnit

class Farming : PluginBootstrap() {
    private val infantCropCooldown = CacheBuilder.newBuilder()
        .weakKeys().expireAfterWrite(10, TimeUnit.SECONDS)
        .build<Player, Unit?>()

    override fun onEnable() {
        Events.listen<PlayerInteractEvent>(this) {
            if (it.action != Action.PHYSICAL) return@listen
            if (it.clickedBlock?.type != FARMLAND) return@listen
            it.isCancelled = true
        }
        val types = mapOf(
            WHEAT to WHEAT_SEEDS,
            POTATOES to POTATO,
            CARROTS to CARROT,
            BEETROOTS to BEETROOT_SEEDS,
            NETHER_WART to NETHER_WART
        )
        Events.listen<BlockBreakEvent>(this) {
            val player = it.player
            val messages = messageCache[Locale(player.locale)]
            val block = it.block
            if (block.type !in types.keys) return@listen
            val data = block.blockData as? Ageable ?: return@listen
            if (data.age == data.maximumAge) {
                val items = it.block.getDrops(player.inventory.itemInMainHand).map { item ->
                    item.apply { if (type in types.values) amount -= 1 }
                }
                player.inventory.addItem(*items.toTypedArray()).forEach { (_, u) ->
                    player.world.dropItemNaturally(player.location, u)
                }
                player.world.playEffect(block.location, Effect.VILLAGER_PLANT_GROW, 10, 5)
                block.blockData = data.apply { age = 0 }
                it.isDropItems = false
                it.isCancelled = true
                return@listen
            }
            if (data.age != data.maximumAge && !player.isSneaking) {
                infantCropCooldown.get(player) {
                    messages.sendMessage(it.player, "BABY-CROP.DESTROY")
                }
                it.isCancelled = true
            }
        }
    }
}