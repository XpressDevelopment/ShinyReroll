package net.xpressdev.shinyreroll.utils

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.item.PokemonItem
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.xpressdev.shinyreroll.config.Message

object PokemonUtils {

    fun getPokemonItem(pokemon: Pokemon): ItemStack = buildItem(PokemonItem.from(pokemon, 1), pokemon)

    fun getSelectedPokemonItem(pokemon: Pokemon): ItemStack =
        buildItem(CobblemonItems.VERDANT_BALL.defaultStack, pokemon)

    private fun buildItem(base: ItemStack, pokemon: Pokemon): ItemStack {
        val ivs = pokemon.ivs
        val evs = pokemon.evs

        return ItemBuilder(base)
            .hideAdditional()
            .addLore(
                listOf(
                    Messages.text(Message.POKEMON_LORE_LEVEL, "level" to "${pokemon.level}"),
                    Messages.text(
                        Message.POKEMON_LORE_NATURE,
                        "nature" to displayName(pokemon.nature.displayName, "cobblemon.nature."),
                    ),
                    Messages.text(
                        Message.POKEMON_LORE_ABILITY,
                        "ability" to displayName(pokemon.ability.displayName, "cobblemon.ability."),
                    ),
                    Messages.text(Message.POKEMON_LORE_IVS_HEADER),
                    Messages.text(
                        Message.POKEMON_LORE_IVS_LINE_1,
                        "hp" to "${ivs.getOrDefault(Stats.HP)}",
                        "atk" to "${ivs.getOrDefault(Stats.ATTACK)}",
                        "def" to "${ivs.getOrDefault(Stats.DEFENCE)}",
                    ),
                    Messages.text(
                        Message.POKEMON_LORE_IVS_LINE_2,
                        "spatk" to "${ivs.getOrDefault(Stats.SPECIAL_ATTACK)}",
                        "spdef" to "${ivs.getOrDefault(Stats.SPECIAL_DEFENCE)}",
                        "spd" to "${ivs.getOrDefault(Stats.SPEED)}",
                    ),
                    Messages.text(Message.POKEMON_LORE_EVS_HEADER),
                    Messages.text(
                        Message.POKEMON_LORE_EVS_LINE_1,
                        "hp" to "${evs.getOrDefault(Stats.HP)}",
                        "atk" to "${evs.getOrDefault(Stats.ATTACK)}",
                        "def" to "${evs.getOrDefault(Stats.DEFENCE)}",
                    ),
                    Messages.text(
                        Message.POKEMON_LORE_EVS_LINE_2,
                        "spatk" to "${evs.getOrDefault(Stats.SPECIAL_ATTACK)}",
                        "spdef" to "${evs.getOrDefault(Stats.SPECIAL_DEFENCE)}",
                        "spd" to "${evs.getOrDefault(Stats.SPEED)}",
                    ),
                )
            )
            .setCustomName(pokemon.nickname ?: Text.literal(pokemon.species.name))
            .build()
    }

    private fun displayName(raw: String, prefix: String): String =
        raw.replace(prefix, "").replaceFirstChar { it.uppercase() }

}
