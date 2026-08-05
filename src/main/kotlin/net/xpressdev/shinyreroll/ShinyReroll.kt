package net.xpressdev.shinyreroll

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
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
    }

    override fun onInitialize() {
        ConfigStore.load()

        ServerLifecycleEvents.SERVER_STARTED.register { server: MinecraftServer ->
            _serverInstance = server
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            RerollManager.clear(handler.player)
            SelectionManager.clear(handler.player)
        }

        ServerTickEvents.START_SERVER_TICK.register {
            RerollManager.updateRolls()
        }

        ShinyRerollCommand.register()
    }

}
