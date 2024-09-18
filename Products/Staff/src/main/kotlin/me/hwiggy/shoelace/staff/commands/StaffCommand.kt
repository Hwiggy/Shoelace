package me.hwiggy.shoelace.staff.commands

import me.hwiggy.kommander.arguments.Arguments
import me.hwiggy.kommander.spigot.PlayerCommand
import me.hwiggy.konverse.Konverse
import me.hwiggy.shoelace.api.messages.formatWithUnit
import me.hwiggy.shoelace.spigot.events.Events
import me.hwiggy.shoelace.spigot.messages.SpigotMessageHandler
import me.hwiggy.shoelace.spigot.utility.InventorySerializer
import me.hwiggy.shoelace.staff.database.StorageProvider
import me.hwiggy.shoelace.staff.utility.QRMapGenerator
import me.hwiggy.shoelace.staff.utility.TOTPAuthentication
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.Prompt
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.map.MapView
import org.bukkit.plugin.Plugin

class StaffCommand(
    plugin: Plugin,
    private val config: YamlConfiguration,
    private val storage: StorageProvider,
    private val totp: TOTPAuthentication,
    private val messages: SpigotMessageHandler,
    private val attempts: Int
) : PlayerCommand() {
    override val name = "staff"
    override val description = "Staff-related utilities."
    override val permission = "staff.command.staff"
    private val loggedIn = HashSet<Player>()
    init {
        Events.listen<PlayerQuitEvent>(plugin) {
            handlePlayerLogout(it.player)
        }
    }

    private fun handlePlayerLogin(who: Player) {
        loggedIn.add(who)
        bulkExecuteCommands(who, "login-commands")
    }

    private fun handlePlayerLogout(who: Player) {
        if (loggedIn.remove(who)) {
            bulkExecuteCommands(who, "logout-commands")
            restorePlayerInventory(who)
        }
    }

    private fun bulkExecuteCommands(who: Player, path: String) {
        config.getStringList(path).map { it.replace("%player%", who.name) }.forEach {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), it)
        }
    }
    private fun restorePlayerInventory(who: Player) {
        val inventory = storage.inventory(who.uniqueId) ?: return
        InventorySerializer.base64ToInv(inventory, who.inventory)
    }
    override fun execute(sender: Player, arguments: Arguments.Processed) {
        if (sender in loggedIn) {
            handlePlayerLogout(sender)
            messages.sendMessage(sender, "staff.logout")
            return
        }
        val (credential, generated) = totp.getCredential(sender.uniqueId)
        storage.storeInventory(sender.uniqueId, InventorySerializer.invToBase64(sender.inventory))
        sender.inventory.clear()
        if (generated) { // This staff member just registered for TOTP
            val data = totp.getQRData(credential)
            val map = QRMapGenerator.createMap(sender, data,
                { it.scale = MapView.Scale.CLOSEST }
            )
            sender.inventory.setItemInMainHand(map)
            create.addConversationAbandonedListener {
                if (it.context.allSessionData["success"] == true) {
                    handlePlayerLogin(sender)
                    sender.inventory.setItemInMainHand(null)
                }
                else restorePlayerInventory(sender)
            }.buildConversation(sender).begin()
        }
        login.addConversationAbandonedListener {
            if (it.context.allSessionData["success"] == true) handlePlayerLogin(sender)
            else restorePlayerInventory(sender)
        }.buildConversation(sender).begin()
    }

    private val createPrompt = Konverse.prompt {
        initial { messages.getFormattedLegacy("staff.create.prompt") }
        accepted { _, _ -> loginPrompt }
    }

    private val loginPrompt = Konverse.prompt {
        initial { messages.getFormattedLegacy("staff.login.prompt") }
        validator { ctx, input -> totp.testCode((ctx.forWhom as Player).uniqueId, input) }
        accepted { _, _ -> successPrompt }
        rejected { ctx, _ ->
            val nextPrompt = this.build()
            val tries = ctx.allSessionData.compute("tries") { _, b ->
                (b as Int?)?.plus(1) ?: 1
            } as Int
            if (tries > attempts) failedPrompt else {
                Konverse.prompt {
                    initial {
                        messages.getFormattedLegacy("staff.login.invalid") {
                            it.replace("%remaining%", (attempts - tries).formatWithUnit("attempt"))
                        }
                    }
                    accepted { _, _ -> nextPrompt }
                }
            }
        }
        blocking()
    }

    private val successPrompt = Konverse.prompt {
        initial { messages.getFormattedLegacy("staff.login.success") }
        accepted { ctx, _ ->
            ctx.setSessionData("success", true)
            Prompt.END_OF_CONVERSATION
        }
    }

    private val failedPrompt = Konverse.prompt {
        initial { messages.getFormattedLegacy("staff.login.failed") }
        accepted { _, _ -> Prompt.END_OF_CONVERSATION }
    }

    private val create = ConversationFactory(plugin).withFirstPrompt(createPrompt)
    private val login = ConversationFactory(plugin).withFirstPrompt(loginPrompt)
}

