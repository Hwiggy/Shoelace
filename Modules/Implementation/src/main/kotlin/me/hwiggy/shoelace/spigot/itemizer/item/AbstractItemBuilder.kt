package me.hwiggy.shoelace.spigot.itemizer.item

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

@Suppress("UNCHECKED_CAST")
abstract class AbstractItemBuilder<Meta : ItemMeta, Self : AbstractItemBuilder<Meta, Self>>
@JvmOverloads constructor(configurator: Self.() -> Unit = { }) {

    init {
        self().configurator()
    }

    abstract val material: Material
    abstract val amount: Int

    private var displayName: String? = null
    private var localizedName: String? = null
    private var lore = emptyList<String>()
    private var customModelData: Int? = null
    private var enchantments: Array<out ItemEnchantment> = emptyArray()
    private var itemFlags: Array<out ItemFlag> = emptyArray()
    private var unbreakable = false

    private var applicator: ((NBTItem) -> Unit)? = null

    open fun updateMeta(meta: Meta) = Unit
    private fun transformMeta(input: Meta): Meta {
        input.setDisplayName(displayName)
        input.lore = lore
        enchantments.forEach { (type, level) ->
            input.addEnchant(type, level, true)
        }
        input.addItemFlags(*itemFlags)
        updateMeta(input)
        return input
    }

    fun displayName(name: String) = self().apply { this.displayName = name }
    fun localizedName(name: String) = self().apply { this.localizedName = name }
    fun lore(vararg lore: String) = self().apply { this.lore = lore.toList() }
    fun customModelData(data: Int) = self().apply { this.customModelData = data }
    fun enchantments(vararg enchantments: ItemEnchantment) = self().apply { this.enchantments = enchantments }
    fun flags(vararg flags: ItemFlag) = self().apply { this.itemFlags = flags }
    fun unbreakable() = self().apply { this.unbreakable = true }
    fun nbt(applicator: (NBTItem) -> Unit) = self().apply { this.applicator = applicator }

    protected fun self() = this as Self

    fun build(): ItemStack {
        val item = ItemStack(material, amount)
        val meta = item.itemMeta ?: return item
        item.itemMeta = transformMeta(meta as Meta)
        val nbtItem = NBTItem(item)
        applicator?.invoke(nbtItem)
        return nbtItem.item
    }
}

class ItemEnchantment(private val enchantment: Enchantment, private val level: Int) {
    operator fun component1() = enchantment
    operator fun component2() = level
}

fun Enchantment.withLevel(level: Int) = ItemEnchantment(this, level)