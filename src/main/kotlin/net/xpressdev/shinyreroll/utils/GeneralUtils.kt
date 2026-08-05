package net.xpressdev.shinyreroll.utils

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.party
import com.cobblemon.mod.common.util.pc
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.config.ConfigStore
import net.xpressdev.shinyreroll.managers.SelectionManager

object GeneralUtils {

    fun calculateCurrentShinyChance(player: ServerPlayerEntity): Double {
        return SelectionManager.resolve(player).size * ConfigStore.config.reroll.chanceFraction
    }

    fun formatShinyChance(player: ServerPlayerEntity): String {
        return String.format("%.2f", calculateCurrentShinyChance(player) * 100)
    }

    private fun maxShinyChance(): Double {
        return ConfigStore.config.reroll.maxPokemonSelected * ConfigStore.config.reroll.chanceFraction
    }

    fun formatMaxShinyChance(): String {
        return String.format("%.2f", maxShinyChance() * 100)
    }

    fun isOverLimit(player: ServerPlayerEntity): Boolean {
        return SelectionManager.resolve(player).size > ConfigStore.config.reroll.maxPokemonSelected
    }

    fun getRandomPokemon(): Pokemon {
        val paradox = setOf(
            "greattusk", "screamtail", "brutebonnet", "fluttermane", "slitherwing", "sandyshocks", "roaringmoon",
            "irontreads", "ironbundle", "ironhands", "ironjugulis", "ironmoth", "ironthorns", "ironvaliant",
            "walkingwake", "ironleaves", "gougingfire", "ragingbolt", "ironboulder", "ironcrown"
        )

        val unclassified = setOf( // I was too lazy to check if the species contained "-", so I added both
            "tapukoko", "tapulele", "tapubulu", "tapufini", "ho-oh", "hooh", "wo-chien", "wochien",
            "chienpao", "chien-pao", "ting-lu", "tinglu", "chi-yu", "chiyu"
        )

        val allowed = ConfigStore.config.allowed

        var pokemon: Pokemon
        do {
            pokemon = PokemonSpecies.random().create(1)
        } while (
            (pokemon.isLegendary() && !allowed.legendary) ||
            (pokemon.isMythical() && !allowed.mythical) ||
            (pokemon.isUltraBeast() && !allowed.ultraBeast) ||
            (paradox.contains(pokemon.species.name.lowercase()) && !allowed.paradox) ||
            (unclassified.contains(pokemon.species.name.lowercase()) && !allowed.legendary) ||
            (!pokemon.species.implemented && !allowed.unimplemented)
        )

        pokemon.shiny = true

        return pokemon
    }

    fun checkSelectedPokemon(player: ServerPlayerEntity): Boolean {
        return SelectionManager.resolve(player).all { it.shiny }
    }

    fun removeSelectedPokemonFromPlayer(player: ServerPlayerEntity) {
        val party = player.party()
        val pc = player.pc()

        SelectionManager.resolve(player).forEach { pokemon ->
            party.remove(pokemon) || pc.remove(pokemon)
        }
    }

    private fun giveRolledPokemon(player: ServerPlayerEntity): Pokemon {
        SelectionManager.clear(player)

        val pokemon = getRandomPokemon()
        player.party().add(pokemon)
        return pokemon
    }

    fun rollNewShiny(player: ServerPlayerEntity): Pokemon? {
        if (!checkSelectedPokemon(player))
            return null

        removeSelectedPokemonFromPlayer(player)
        return giveRolledPokemon(player)
    }

}