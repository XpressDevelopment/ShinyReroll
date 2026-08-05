package net.xpressdev.shinyreroll.config

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.labels.CobblemonPokemonLabels
import com.cobblemon.mod.common.pokemon.Species
import java.io.File
import net.fabricmc.loader.api.FabricLoader
import net.xpressdev.shinyreroll.LOGGER
import net.xpressdev.xpresslib.config.ConfigManager
import net.xpressdev.xpresslib.json.JsonFiles
import net.xpressdev.xpresslib.lang.Lang

object ConfigStore {

    private const val DIRECTORY = "ShinyReroll"
    private const val CONFIG_FILE = "config.json"

    private val directory: File = FabricLoader.getInstance().configDir.resolve(DIRECTORY).toFile()

    private val files = JsonFiles(LOGGER)

    private val manager = ConfigManager(directory.toPath(), LOGGER, files)

    private val configHandle = manager.config(CONFIG_FILE, ConfigCodec, ::ShinyRerollConfig)
    private val langHandle = manager.lang(Message.entries)

    val config: ShinyRerollConfig get() = configHandle.value

    val lang: Lang get() = langHandle.value

    val eligibleSpecies: List<Species>
        get() {
            val allowed = config.allowed

            return PokemonSpecies.species.filter { species ->
                (allowed.unimplemented || species.implemented) &&
                    (allowed.legendary || !species.labels.contains(CobblemonPokemonLabels.LEGENDARY)) &&
                    (allowed.legendary || !species.labels.contains(CobblemonPokemonLabels.RESTRICTED)) &&
                    (allowed.mythical || !species.labels.contains(CobblemonPokemonLabels.MYTHICAL)) &&
                    (allowed.ultraBeast || !species.labels.contains(CobblemonPokemonLabels.ULTRA_BEAST)) &&
                    (allowed.paradox || !species.labels.contains(CobblemonPokemonLabels.PARADOX))
            }
        }

    fun load() {
        if (!directory.isDirectory && !directory.mkdirs()) {
            LOGGER.error("Could not create the config directory ${directory.absolutePath}")
        }

        manager.load()
    }
}
