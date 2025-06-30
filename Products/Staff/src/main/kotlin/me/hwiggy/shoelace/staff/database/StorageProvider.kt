package me.hwiggy.shoelace.staff.database

import me.hwiggy.shoelace.spigot.utility.SerializedInventory
import java.util.*

interface StorageProvider {
    fun credential(id: UUID): String?
    fun storeCredential(id: UUID, secret: String): String
    fun inventory(id: UUID): SerializedInventory?
    fun storeInventory(id: UUID, inventory: SerializedInventory)
    fun clearStoredInventory(id: UUID)
}