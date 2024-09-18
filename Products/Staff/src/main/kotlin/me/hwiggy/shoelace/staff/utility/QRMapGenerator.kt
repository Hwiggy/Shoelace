package me.hwiggy.shoelace.staff.utility

import dev.samstevens.totp.qr.QrData
import dev.samstevens.totp.qr.ZxingPngQrGenerator
import me.hwiggy.shoelace.spigot.utility.MapCreator
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapView
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

object QRMapGenerator {
    private val imageGen = ZxingPngQrGenerator().also { it.imageSize = 128 }

    fun createMap(
        player: Player,
        data: QrData,
        viewModifier: (MapView) -> Unit = {},
        metaModifier: (MapMeta) -> Unit = {}
    ): ItemStack {
        val image = imageGen.generate(data).let(::ByteArrayInputStream).let(ImageIO::read)
        return MapCreator.createMap(player.world, image, viewModifier, metaModifier)
    }
}