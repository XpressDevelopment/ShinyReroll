package net.xpressdev.shinyreroll.managers

import com.cobblemon.mod.common.CobblemonItems
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.xpressdev.shinyreroll.guis.RollingScreenGui
import net.xpressdev.shinyreroll.utils.GeneralUtils
import net.xpressdev.shinyreroll.utils.MMUtils
import net.xpressdev.shinyreroll.utils.ServerUtils
import java.util.UUID
import kotlin.random.Random

object RerollManager {

    data class PlayerRollState(
        var remainingTime: Int,
        var currentDelay: Float,
        var elapsedTicks: Int = 0
    )

    val shownItemMap = mutableMapOf<UUID, MutableList<ItemStack>>()
    val playerRollStates = mutableMapOf<ServerPlayerEntity, PlayerRollState>()
    val playerOutcomes = mutableMapOf<ServerPlayerEntity, Boolean>()
    val playersToRemove = mutableListOf<ServerPlayerEntity>()

    val shownItemList = listOf(
        CobblemonItems.POKE_BALL.defaultStack, CobblemonItems.POKE_BALL.defaultStack, CobblemonItems.POKE_BALL.defaultStack,
        CobblemonItems.MASTER_BALL.defaultStack,
        CobblemonItems.POKE_BALL.defaultStack, CobblemonItems.POKE_BALL.defaultStack, CobblemonItems.POKE_BALL.defaultStack
    )

    fun init(player: ServerPlayerEntity) {
        shownItemMap[player.uuid] = shownItemList.toMutableList()
        calculateOutcome(player)
        startRolling(player, 200, 1.0f)
        playersToRemove.remove(player)
    }

    private fun startRolling(player: ServerPlayerEntity, totalTime: Int, initialDelay: Float) {
        playerRollStates[player] = PlayerRollState(
            remainingTime = totalTime,
            currentDelay = initialDelay
        )
    }

    private fun doRoll(player: ServerPlayerEntity) {
        val list = shownItemMap[player.uuid] ?: return

        player.playSoundToPlayer(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 0.5f, 1.0f)

        val item = list.removeFirst()
        list.add(item)

        shownItemMap[player.uuid] = list
        RollingScreenGui.openGui(player)
    }

    fun updateRolls() {
        playerRollStates.entries.forEach { (player, state) ->
            state.remainingTime--

            if (state.elapsedTicks >= state.currentDelay && state.remainingTime > 0) {
                doRoll(player)
                state.elapsedTicks = 0
                state.currentDelay += Random.nextFloat() * (1f - 0.7f) + 0.7f
            } else {
                state.elapsedTicks++
            }

            if (state.remainingTime <= 0) {
                handleOutcome(player, state)
            }
        }

        playersToRemove.forEach { player ->
            playerOutcomes.remove(player)
            playerRollStates.remove(player)
        }
    }

    private fun handleOutcome(player: ServerPlayerEntity, state: PlayerRollState) {
        if (calculateOutcome(player))
            handleWinOutcome(player, state)
        else
            handleLossOutcome(player, state)
    }

    private fun handleWinOutcome(player: ServerPlayerEntity, state: PlayerRollState) {
        val middleItem = shownItemMap[player.uuid]!![3].item
        if (middleItem != CobblemonItems.MASTER_BALL.defaultStack.item) {
            val index = shownItemMap[player.uuid]!!.indexOf(CobblemonItems.MASTER_BALL.defaultStack)
            val distance = if (index < 3) 3 - index else 3 + index
            state.remainingTime = distance * 20
        } else {
            shownItemMap.remove(player.uuid)
            playersToRemove.add(player)
            player.playSoundToPlayer(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 1.0f, 1.0f)

            val pokemon = GeneralUtils.rollNewShiny(player)
            if (pokemon == null) {
                player.sendMessage(MMUtils.parseText("<red>One or more of your pokemon was not shiny."))
            } else {
                player.sendMessage(MMUtils.parseText("<gold>You've received a <yellow>Shiny ${pokemon.species.name}<gold>!"))
            }
        }
    }

    private fun handleLossOutcome(player: ServerPlayerEntity, state: PlayerRollState) {
        if (shownItemMap[player.uuid]!![3].item == CobblemonItems.MASTER_BALL.defaultStack.item) {
            // doRoll(player)
            state.remainingTime = 30
            return
        }

        GeneralUtils.removeSelectedPokemonFromPlayer(player)
        player.sendMessage(MMUtils.parseText("<red>Womp Womp! Better luck next time!"))
        player.playSoundToPlayer(SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.NEUTRAL, 1.0f, 0.1f)

        shownItemMap.remove(player.uuid)
        playersToRemove.add(player)
    }

    private fun calculateOutcome(player: ServerPlayerEntity): Boolean {
        if (!playerOutcomes.containsKey(player))
            playerOutcomes[player] = Math.random() <= GeneralUtils.calculateCurrentShinyChance(player)
        return playerOutcomes[player]!!
    }


}