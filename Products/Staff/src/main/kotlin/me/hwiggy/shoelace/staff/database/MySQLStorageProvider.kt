package me.hwiggy.shoelace.staff.database

import me.hwiggy.shoelace.api.database.MySQL
import me.hwiggy.shoelace.spigot.utility.SerializedInventory
import org.bukkit.configuration.ConfigurationSection
import java.util.*

class MySQLStorageProvider(
    dbConf: ConfigurationSection
) : StorageProvider {
    private val pool: MySQL = MySQL(
        dbConf["host"] as String,
        dbConf["port"] as Int,
        dbConf["database"] as String,
        dbConf["username"] as String,
        dbConf["password"] as String
    )
    private val prefix = dbConf["prefix"] as String?

    private val credentialTable = (prefix ?: "") + "_credential"
    private val inventoryTable = (prefix ?: "") + "_inventory"

    init {
        pool.connect {
            it.prepareStatement(
                "CREATE TABLE IF NOT EXISTS $credentialTable(`id` VARCHAR(36) NOT NULL UNIQUE, `secret` VARCHAR(64) NOT NULL);"
            ).executeUpdate()
            it.prepareStatement(
                "CREATE TABLE IF NOT EXISTS $inventoryTable(`id` VARCHAR(36) NOT NULL UNIQUE, `armor` VARCHAR DEFAULT NULL, `items` VARCHAR DEFAULT NULL)"
            ).executeUpdate()
        }
    }

    override fun credential(id: UUID): String? {
        return pool.connect {
            val results = it.prepareStatement(
                "SELECT `secret` FROM `$credentialTable` WHERE `id`=?;"
            ).also {
                it.setString(1, credentialTable)
                it.setString(2, id.toString())
            }.executeQuery()
            if (results.isBeforeFirst) return@connect null
            results.next()
            results.getString("secret")
        }
    }

    override fun storeCredential(id: UUID, secret: String): String {
        return pool.connect {
            it.prepareStatement(
                "INSERT INTO $credentialTable(`id`, `secret`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `secret`=VALUES(`secret`);"
            ).also {
                it.setString(1, id.toString())
                it.setString(2, secret)
            }.executeUpdate()
            secret
        }!!
    }

    override fun inventory(id: UUID): SerializedInventory? {
        return pool.connect {
            val results = it.prepareStatement(
                "SELECT `armor`, `items` FROM $inventoryTable WHERE `id`=?;"
            ).also {
                it.setString(1, id.toString())
            }.executeQuery()
            if (results.isBeforeFirst) return@connect null
            SerializedInventory(
                results.getString("armor"),
                results.getString("items")
            )
        }
    }

    override fun storeInventory(id: UUID, inventory: SerializedInventory) {
        pool.connect {
            it.prepareStatement(
                "INSERT INTO $inventoryTable(`id`, `armor`, `items`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `items`=VALUES(`items`), `armor`=VALUES(`armor`);"
            ).executeUpdate()
        }
    }

    override fun clearStoredInventory(id: UUID) {
        pool.connect {
            it.prepareStatement(
                "UPDATE $inventoryTable SET `armor` = null AND `items` = NULL WHERE `id` = ?;"
            ).also {
                it.setString(1, id.toString())
            }.executeUpdate()
        }
    }
}