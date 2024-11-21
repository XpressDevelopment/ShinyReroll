package net.xpressdev.shinyreroll.utils

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.item.PokemonItem
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text

object PokemonUtils {

    fun getPokemonItem(pokemon: Pokemon): ItemStack {
        val ivs = pokemon.ivs
        val evs = pokemon.evs
        return ItemBuilder(PokemonItem.from(pokemon, 1))
            .hideAdditional()
            .addLore(
                listOf<Text>(
                    Text.literal("§bLevel: §f${pokemon.level}"),
                    Text.literal(
                        "§eNature: §f${
                            pokemon.nature.displayName.replace(
                                "cobblemon.nature.",
                                ""
                            ).replaceFirstChar { it.uppercase() }
                        }"),
                    Text.literal(
                        "§6Ability: §f${
                            pokemon.ability.displayName.replace(
                                "cobblemon.ability.",
                                ""
                            ).replaceFirstChar { it.uppercase() }
                        }"
                    ),
                    Text.literal("§dIVs:"),
                    Text.literal(
                        " §cHP: §f${ivs.getOrDefault(Stats.HP)}  " +
                                "§9Atk: §f${ivs.getOrDefault(Stats.ATTACK)}  " +
                                "§7Def: §f${ivs.getOrDefault(Stats.DEFENCE)}"
                    ),
                    Text.literal(
                        " §bSpAtk: §f${ivs.getOrDefault(Stats.SPECIAL_ATTACK)}  " +
                                "§eSpDef: §f${ivs.getOrDefault(Stats.SPECIAL_DEFENCE)}  " +
                                "§aSpd: §f${ivs.getOrDefault(Stats.SPEED)}"
                    ),
                    Text.literal("§3EVs:"),
                    Text.literal(
                        " §cHP: §f${evs.getOrDefault(Stats.HP)}  " +
                                "§9Atk: §f${evs.getOrDefault(Stats.ATTACK)}  " +
                                "§7Def: §f${evs.getOrDefault(Stats.DEFENCE)}"
                    ),
                    Text.literal(
                        " §bSpAtk: §f${evs.getOrDefault(Stats.SPECIAL_ATTACK)}  " +
                                "§eSpDef: §f${evs.getOrDefault(Stats.SPECIAL_DEFENCE)}  " +
                                "§aSpd: §f${evs.getOrDefault(Stats.SPEED)}"
                    ),
                )
            )
            .setCustomName(pokemon.nickname ?: Text.literal(pokemon.species.name))
            .build()
    }

    fun getSelectedPokemonItem(pokemon: Pokemon): ItemStack {
        val ivs = pokemon.ivs
        val evs = pokemon.evs
        return ItemBuilder(CobblemonItems.VERDANT_BALL.defaultStack)
            .hideAdditional()
            .addLore(
                listOf<Text>(
                    Text.literal("§bLevel: §f${pokemon.level}"),
                    Text.literal(
                        "§eNature: §f${
                            pokemon.nature.displayName.replace(
                                "cobblemon.nature.",
                                ""
                            ).replaceFirstChar { it.uppercase() }
                        }"),
                    Text.literal(
                        "§6Ability: §f${
                            pokemon.ability.displayName.replace(
                                "cobblemon.ability.",
                                ""
                            ).replaceFirstChar { it.uppercase() }
                        }"
                    ),
                    Text.literal("§dIVs:"),
                    Text.literal(
                        " §cHP: §f${ivs.getOrDefault(Stats.HP)}  " +
                                "§9Atk: §f${ivs.getOrDefault(Stats.ATTACK)}  " +
                                "§7Def: §f${ivs.getOrDefault(Stats.DEFENCE)}"
                    ),
                    Text.literal(
                        " §bSpAtk: §f${ivs.getOrDefault(Stats.SPECIAL_ATTACK)}  " +
                                "§eSpDef: §f${ivs.getOrDefault(Stats.SPECIAL_DEFENCE)}  " +
                                "§aSpd: §f${ivs.getOrDefault(Stats.SPEED)}"
                    ),
                    Text.literal("§3EVs:"),
                    Text.literal(
                        " §cHP: §f${evs.getOrDefault(Stats.HP)}  " +
                                "§9Atk: §f${evs.getOrDefault(Stats.ATTACK)}  " +
                                "§7Def: §f${evs.getOrDefault(Stats.DEFENCE)}"
                    ),
                    Text.literal(
                        " §bSpAtk: §f${evs.getOrDefault(Stats.SPECIAL_ATTACK)}  " +
                                "§eSpDef: §f${evs.getOrDefault(Stats.SPECIAL_DEFENCE)}  " +
                                "§aSpd: §f${evs.getOrDefault(Stats.SPEED)}"
                    ),
                )
            )
            .setCustomName(pokemon.nickname ?: Text.literal(pokemon.species.name))
            .build()
    }

}