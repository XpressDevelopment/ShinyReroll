package net.xpressdev.shinyreroll

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.kyori.adventure.platform.fabric.FabricServerAudiences
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.minecraft.server.MinecraftServer
import net.xpressdev.shinyreroll.commands.ShinyRerollCommand
import net.xpressdev.shinyreroll.managers.RerollManager
import net.xpressdev.shinyreroll.utils.Config
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ShinyReroll : ModInitializer {

    companion object {
        private lateinit var _serverInstance: MinecraftServer
        val serverInstance: MinecraftServer
            get() = _serverInstance

        var luckPerms: LuckPerms? = null

        var adventure: FabricServerAudiences? = null

        var LOGGER: Logger = LoggerFactory.getLogger("ShinyReroll")

        var hasEconomy = false

        private const val COOLDOWN: Int = 1
        private var _counter: Int = 0
        val counter: Int
            get() = _counter
        var onCooldown: Boolean = false

        var maxPokemonSelected: Int = 10
        var chancePerPokemon: Double = 0.1
        var allowLegendary: Boolean = false
        var allowMythical: Boolean = false
        var allowUltraBeast: Boolean = false
        var allowParadox: Boolean = false
        var allowUnimplemented: Boolean = false

        private val _config = Config()
        val config: Config
            get() = _config
    }

    override fun onInitialize() {
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