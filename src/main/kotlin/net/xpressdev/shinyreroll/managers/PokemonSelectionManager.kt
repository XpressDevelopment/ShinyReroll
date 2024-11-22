package net.xpressdev.shinyreroll.managers

import com.cobblemon.mod.common.api.storage.pc.PCPosition
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.party
import com.cobblemon.mod.common.util.pc
import net.minecraft.server.network.ServerPlayerEntity
import net.xpressdev.shinyreroll.utils.ServerUtils
import java.util.UUID

object PokemonSelectionManager {

    private val selectedPartyPokemon = mutableMapOf<UUID, MutableList<Int>>()
    private val selectedPcPokemon = mutableMapOf<UUID, MutableList<PCPosition>>()

    fun init(player: ServerPlayerEntity) {
        selectedPartyPokemon[player.uuid] = mutableListOf()
        selectedPcPokemon[player.uuid] = mutableListOf()
    }

    fun clear(player: ServerPlayerEntity) {
        selectedPartyPokemon.remove(player.uuid)
        selectedPcPokemon.remove(player.uuid)
    }

    fun selectPartyPokemon(player: ServerPlayerEntity, slot: Int) {
        selectedPartyPokemon[player.uuid]?.add(slot)
    }

    fun deselectPartyPokemon(player: ServerPlayerEntity, slot: Int) {
        selectedPartyPokemon[player.uuid]?.remove(slot)
    }

    fun selectPcPokemon(player: ServerPlayerEntity, position: PCPosition) {
        selectedPcPokemon[player.uuid]?.add(position)
    }

    fun deselectPcPokemon(player: ServerPlayerEntity, position: PCPosition) {
        selectedPcPokemon[player.uuid]?.remove(position)
    }

    fun getSelectedPartyPokemon(player: ServerPlayerEntity): List<Int> {
        return selectedPartyPokemon[player.uuid] ?: emptyList()
    }

    fun getSelectedPcPokemon(player: ServerPlayerEntity): List<PCPosition> {
        return selectedPcPokemon[player.uuid] ?: emptyList()
    }

    fun isPartyPokemonSelected(player: ServerPlayerEntity, slot: Int): Boolean {
        return selectedPartyPokemon[player.uuid]?.contains(slot) ?: false
    }

    fun isPcPokemonSelected(player: ServerPlayerEntity, position: PCPosition): Boolean {
        return selectedPcPokemon[player.uuid]?.contains(position) ?: false
    }

    fun getSelectedPokemon(player: ServerPlayerEntity): MutableList<Pokemon> {
        val selectedPokemon = mutableListOf<Pokemon>()
        val party = player.party()
        val pc = player.pc()

        selectedPartyPokemon[player.uuid]?.forEach { slot ->
            party.get(slot)?.let {
                selectedPokemon.add(it)
            }
        }

        selectedPcPokemon[player.uuid]?.forEach { position ->
            selectedPokemon.add(pc[position]!!)
        }

        return selectedPokemon
    }

    fun isPokemonSelected(player: ServerPlayerEntity, pokemon: Pokemon): Boolean {
        return getSelectedPokemon(player).contains(pokemon)
    }

    fun removeSelectedPokemon(player: ServerPlayerEntity, pokemon: Pokemon) {
        val pokemonUuid = pokemon.uuid

        val party = player.party()
        val pc = player.pc()

        party.find {
            it.uuid == pokemonUuid
        }?.let {
            selectedPartyPokemon[player.uuid]?.remove(party.indexOf(it))
            return
        }

        selectedPcPokemon[player.uuid]?.forEach { position ->
            if (pc[position]?.uuid == pokemonUuid) {
                selectedPcPokemon[player.uuid]?.remove(position)
                return
            }
        }
    }

}