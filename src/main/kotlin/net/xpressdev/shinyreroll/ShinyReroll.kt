package net.xpressdev.shinyreroll

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.loader.api.FabricLoader
import net.kyori.adventure.platform.fabric.FabricServerAudiences
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.minecraft.server.MinecraftServer
import net.xpressdev.shinyreroll.commands.ShinyRerollCommand
import net.xpressdev.shinyreroll.config.ConfigStore
import net.xpressdev.shinyreroll.managers.RerollManager
import net.xpressdev.shinyreroll.managers.SelectionManager

class ShinyReroll : ModInitializer {

    companion object {
        private lateinit var _serverInstance: MinecraftServer
        val serverInstance: MinecraftServer
            get() = _serverInstance

        var luckPerms: LuckPerms? = null

        var adventure: FabricServerAudiences? = null

        var hasEconomy = false

        private const val COOLDOWN: Int = 1
        private var _counter: Int = 0
        val counter: Int
            get() = _counter
        var onCooldown: Boolean = false
    }

    override fun onInitialize() {
        ConfigStore.load()

        ServerLifecycleEvents.SERVER_STARTED.register { server: MinecraftServer ->
            _serverInstance = server

            try {
                luckPerms = LuckPermsProvider.get()
            } catch (e: Exception) {
                LOGGER.warn("LuckPerms not found")
            }

            adventure = FabricServerAudiences.of(server)

            if (FabricLoader.getInstance().isModLoaded("impactor"))
                hasEconomy = true
            else
                LOGGER.warn("Economy service not found.")
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            RerollManager.clear(handler.player)
            SelectionManager.clear(handler.player)
        }

        ServerTickEvents.START_SERVER_TICK.register {
            if (onCooldown) {
                if (counter > COOLDOWN)  {
                    onCooldown = false
                    _counter = 0
                } else {
                    _counter++
                }
            }

            RerollManager.updateRolls()
        }

        ShinyRerollCommand()
    }

}