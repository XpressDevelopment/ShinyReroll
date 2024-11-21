package net.xpressdev.shinyreroll.utils

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.party
import com.cobblemon.mod.common.util.pc
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.ShinyReroll
import net.xpressdev.shinyreroll.managers.PokemonSelectionManager

object GeneralUtils {

    fun calculateCurrentShinyChance(player: ServerPlayerEntity): Double {
        return PokemonSelectionManager.getSelectedPokemon(player).size * ShinyReroll.chancePerPokemon
    }

    fun formatShinyChance(player: ServerPlayerEntity): String {
        return String.format("%.2f", calculateCurrentShinyChance(player) * 100)
    }

    private fun maxShinyChance(): Double {
        return ShinyReroll.maxPokemonSelected * ShinyReroll.chancePerPokemon
    }

    fun formatMaxShinyChance(): String {
        return String.format("%.2f", maxShinyChance() * 100)
    }

    fun isOverLimit(player: ServerPlayerEntity): Boolean {
        return PokemonSelectionManager.getSelectedPokemon(player).size > ShinyReroll.maxPokemonSelected
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

        var pokemon: Pokemon
        do {
            pokemon = PokemonSpecies.random().create(1)
        } while (
            (pokemon.isLegendary() && !ShinyReroll.allowLegendary) ||
            (pokemon.isMythical() && !ShinyReroll.allowMythical) ||
            (pokemon.isUltraBeast() && !ShinyReroll.allowUltraBeast) ||
            (paradox.contains(pokemon.species.name.lowercase()) && !ShinyReroll.allowParadox) ||
            (unclassified.contains(pokemon.species.name.lowercase()) && !ShinyReroll.allowLegendary) ||
            (!pokemon.species.implemented && !ShinyReroll.allowUnimplemented)
        )

        pokemon.shiny = true

        return pokemon
    }

    fun checkSelectedPokemon(player: ServerPlayerEntity): Boolean {
        return PokemonSelectionManager.getSelectedPokemon(player).all { it.shiny }
    }

    fun removeSelectedPokemonFromPlayer(player: ServerPlayerEntity) {
        val pm = PokemonSelectionManager
        val selectedPartyPokemon = pm.getSelectedPartyPokemon(player)
        val selectedPcPokemon = pm.getSelectedPcPokemon(player)

        val party = player.party()
        val pc = player.pc()

        selectedPartyPokemon.forEach {
            party.remove(party.get(it)!!)
        }

        selectedPcPokemon.forEach {
            pc.remove(pc[it]!!)
        }
    }

    private fun giveRolledPokemon(player: ServerPlayerEntity): Pokemon {
        PokemonSelectionManager.clear(player)

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