package me.hwiggy.shoelace.spigot.itemizer.item

import org.bukkit.Material
import org.bukkit.inventory.meta.ItemMeta

class ItemBuilder @JvmOverloads constructor(
    override val material: Material,
    override val amount: Int = 1,
    configurator: ItemBuilder.() -> Unit = { }
) : AbstractItemBuilder<ItemMeta, ItemBuilder>(configurator)