package net.xpressdev.shinyreroll.managers

import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.party
import com.cobblemon.mod.common.util.pc
import java.util.UUID
import net.minecraft.server.network.ServerPlayerEntity

object SelectionManager {

    private val selections = mutableMapOf<UUID, MutableSet<UUID>>()

    fun clear(player: ServerPlayerEntity) {
        selections.remove(player.uuid)
    }

    fun toggle(player: ServerPlayerEntity, pokemon: Pokemon) {
        val selected = selections.getOrPut(player.uuid) { mutableSetOf() }
        if (!selected.add(pokemon.uuid)) selected.remove(pokemon.uuid)
    }

    fun isSelected(player: ServerPlayerEntity, pokemon: Pokemon): Boolean {
        return selections[player.uuid]?.contains(pokemon.uuid) ?: false
    }

    fun resolve(player: ServerPlayerEntity): List<Pokemon> {
        val ids = selections[player.uuid] ?: return emptyList()

        val resolved = mutableListOf<Pokemon>()
        val resolvedIds = mutableSetOf<UUID>()

        player.party().forEach {
            if (ids.contains(it.uuid) && resolvedIds.add(it.uuid)) resolved.add(it)
        }
        player.pc().forEach {
            if (ids.contains(it.uuid) && resolvedIds.add(it.uuid)) resolved.add(it)
        }

        if (resolvedIds.size != ids.size) ids.retainAll(resolvedIds)

        return resolved
    }
}
