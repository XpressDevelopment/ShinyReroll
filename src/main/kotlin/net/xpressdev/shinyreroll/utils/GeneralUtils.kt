package net.xpressdev.shinyreroll.utils

import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.party
import com.cobblemon.mod.common.util.pc
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.LOGGER
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

    fun getRandomPokemon(): Pokemon? {
        val species = ConfigStore.eligibleSpecies
        if (species.isEmpty()) {
            LOGGER.error("No Pokemon species are eligible for a reroll reward with the current config.")
            return null
        }

        val pokemon = species.random().create(1)
        pokemon.shiny = true
        return pokemon
    }

    fun validate(player: ServerPlayerEntity): Boolean {
        val expected = SelectionManager.selectionSize(player)
        val selected = SelectionManager.resolve(player)

        return expected in 1..ConfigStore.config.reroll.maxPokemonSelected &&
            selected.size == expected &&
            selected.all { it.shiny }
    }

    fun removeSelectedPokemonFromPlayer(player: ServerPlayerEntity) {
        val party = player.party()
        val pc = player.pc()

        SelectionManager.resolve(player).forEach { pokemon ->
            party.remove(pokemon) || pc.remove(pokemon)
        }
    }

    private fun giveReward(player: ServerPlayerEntity, pokemon: Pokemon): Boolean {
        val party = player.party()
        val pc = player.pc()

        return when {
            party.getFirstAvailablePosition() != null -> party.add(pokemon)
            pc.getFirstAvailablePosition() != null -> pc.add(pokemon)
            else -> false
        }
    }

    fun rollNewShiny(player: ServerPlayerEntity): Pokemon? {
        val pokemon = getRandomPokemon() ?: return null
        if (!giveReward(player, pokemon)) return null

        removeSelectedPokemonFromPlayer(player)
        SelectionManager.clear(player)

        return pokemon
    }

}
