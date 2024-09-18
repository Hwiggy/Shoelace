package me.hwiggy.shoelace.spigot.utility

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapCanvas
import org.bukkit.map.MapRenderer
import org.bukkit.map.MapView
import java.awt.Image

object MapCreator {
    fun createMap(
        world: World, image: Image,
        viewModifier: (MapView) -> Unit,
        metaModifier: (MapMeta) -> Unit
    ): ItemStack {
        val map = ItemStack(Material.FILLED_MAP)
        val meta = map.itemMeta as MapMeta
        val view = Bukkit.createMap(world).also(viewModifier)
        view.renderers.clear()
        view.addRenderer(object : MapRenderer() {
            override fun render(map: MapView, canvas: MapCanvas, player: Player) {
                canvas.drawImage(0,0, image)
            }
        })
        meta.mapView = view
        metaModifier(meta)
        map.itemMeta = meta
        return map
    }
}