package net.xpressdev.shinyreroll.utils

import com.google.gson.*
import net.fabricmc.loader.api.FabricLoader
import net.xpressdev.shinyreroll.ShinyReroll
import java.io.*

class Config {

    data class ConfigData (
        val maxPokemonSelected: Int,
        val chancePerPokemon: Double,
        val allowLegendary: Boolean,
        val allowMythical: Boolean,
        val allowUltraBeast: Boolean,
        val allowParadox: Boolean,
        val allowUnimplemented: Boolean
    )

    init {
        createFolders()

        loadStartingConfig()
        loadConfig()
    }

    private fun createFolders() {
        val folderPath = FabricLoader.getInstance().configDir.resolve("ShinyReroll")
        val folder = folderPath.toFile()
        if (!folder.exists())
            folder.mkdir()
    }

    private fun getConfigFile(): File {
        val savePath = FabricLoader.getInstance().configDir.resolve("ShinyReroll/config.json")
        val saveFile = savePath.toFile()
        if (!saveFile.exists()) {
            if (saveFile.createNewFile()) {
                loadStartingConfig().let { list ->
                    FileWriter(saveFile).use {
                        GsonBuilder().setPrettyPrinting().create().toJson(list, it)
                    }
                }
            }
        }
        return saveFile
    }

    private fun loadStartingConfig(): JsonObject? {
        val jsonStream: InputStream? = ShinyReroll::class.java.getResourceAsStream("/config/config.json")
        return jsonStream?.use {
            InputStreamReader(it).use { reader ->
                JsonParser.parseReader(reader).asJsonObject
            }
        }
    }

    fun loadConfig() {
        val file = getConfigFile()

        val json = FileReader(file).use {
            JsonParser.parseReader(it).asJsonObject
        }

        val defaults = mapOf(
            "maxPokemonSelected" to 10,
            "chancePerPokemon" to 10.0,
            "allowLegendary" to false,
            "allowMythical" to false,
            "allowUltraBeast" to false,
            "allowParadox" to false,
            "allowUnimplemented" to false
        )

        val configUpdated = ensureConfigCompleteness(json, defaults)
        if (configUpdated) {
            FileWriter(file).use {
                GsonBuilder().setPrettyPrinting().create().toJson(json, it)
            }
        }

        loadConfigFromJson()
    }

    private fun loadConfigFromJson() {
        val gson = Gson()
        val file = getConfigFile()

        try {
            val reader = FileReader(file)
            val configData: ConfigData = gson.fromJson(reader, ConfigData::class.java)

            ShinyReroll.maxPokemonSelected = configData.maxPokemonSelected
            ShinyReroll.chancePerPokemon = configData.chancePerPokemon / 100.0
            ShinyReroll.allowLegendary = configData.allowLegendary
            ShinyReroll.allowMythical = configData.allowMythical
            ShinyReroll.allowUltraBeast = configData.allowUltraBeast
            ShinyReroll.allowParadox = configData.allowParadox
            ShinyReroll.allowUnimplemented = configData.allowUnimplemented

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ensureConfigCompleteness(json: JsonObject, defaults: Map<String, Any>): Boolean {
        var configUpdated = false
        for ((key, value) in defaults) {
            if (!json.has(key)) {
                when (value) {
                    is Boolean -> json.addProperty(key, value)
                    is Number -> json.addProperty(key, value)
                    is String -> json.addProperty(key, value)
                    is List<*> -> {
                        val array = JsonArray()
                        value.forEach { array.add(it.toString()) }
                        json.add(key, array)
                    }
                    else -> throw IllegalArgumentException("Unsupported type for config value")
                }
                configUpdated = true
            }
        }
        return configUpdated
    }

}