package net.xpressdev.shinyreroll.config

import com.google.gson.JsonObject
import net.xpressdev.shinyreroll.LOGGER
import net.xpressdev.xpresslib.json.JsonCodec
import net.xpressdev.xpresslib.json.bool
import net.xpressdev.xpresslib.json.boolIn
import net.xpressdev.xpresslib.json.double
import net.xpressdev.xpresslib.json.doubleIn
import net.xpressdev.xpresslib.json.int
import net.xpressdev.xpresslib.json.intIn
import net.xpressdev.xpresslib.json.obj

object ConfigCodec : JsonCodec<ShinyRerollConfig> {

    private const val REROLL = "reroll"
    private const val ALLOWED = "allowed"

    override fun decode(json: JsonObject): ShinyRerollConfig {
        val defaults = ShinyRerollConfig()

        val reroll = json.obj(REROLL)
        val allowed = json.obj(ALLOWED)

        return ShinyRerollConfig(
            reroll = RerollSettings(
                maxPokemonSelected = intOf(
                    reroll, json, "maxPokemonSelected", "maxPokemonSelected", defaults.reroll.maxPokemonSelected,
                ),
                chancePerPokemon = doubleOf(
                    reroll, json, "chancePerPokemon", "chancePerPokemon", defaults.reroll.chancePerPokemon,
                ),
            ),
            allowed = AllowedSettings(
                legendary = boolOf(allowed, json, "legendary", "allowLegendary", defaults.allowed.legendary),
                mythical = boolOf(allowed, json, "mythical", "allowMythical", defaults.allowed.mythical),
                ultraBeast = boolOf(allowed, json, "ultraBeast", "allowUltraBeast", defaults.allowed.ultraBeast),
                paradox = boolOf(allowed, json, "paradox", "allowParadox", defaults.allowed.paradox),
                unimplemented = boolOf(
                    allowed, json, "unimplemented", "allowUnimplemented", defaults.allowed.unimplemented,
                ),
            ),
        )
    }

    override fun encode(value: ShinyRerollConfig): JsonObject = JsonObject().apply {
        add(
            REROLL,
            JsonObject().apply {
                addProperty("maxPokemonSelected", value.reroll.maxPokemonSelected)
                addProperty("chancePerPokemon", value.reroll.chancePerPokemon)
            },
        )
        add(
            ALLOWED,
            JsonObject().apply {
                addProperty("legendary", value.allowed.legendary)
                addProperty("mythical", value.allowed.mythical)
                addProperty("ultraBeast", value.allowed.ultraBeast)
                addProperty("paradox", value.allowed.paradox)
                addProperty("unimplemented", value.allowed.unimplemented)
            },
        )
    }

    private fun boolOf(nested: JsonObject?, root: JsonObject, key: String, legacy: String?, default: Boolean): Boolean =
        root.boolIn(nested, key, legacy?.let { root.bool(it, LOGGER) } ?: default, LOGGER)

    private fun intOf(nested: JsonObject?, root: JsonObject, key: String, legacy: String?, default: Int): Int =
        root.intIn(nested, key, legacy?.let { root.int(it, LOGGER) } ?: default, LOGGER)

    private fun doubleOf(nested: JsonObject?, root: JsonObject, key: String, legacy: String?, default: Double): Double =
        root.doubleIn(nested, key, legacy?.let { root.double(it, LOGGER) } ?: default, LOGGER)
}
