package net.xpressdev.shinyreroll.managers

import com.cobblemon.mod.common.CobblemonItems
import java.util.UUID
import kotlin.random.Random
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.xpressdev.shinyreroll.ShinyReroll
import net.xpressdev.shinyreroll.config.Message
import net.xpressdev.shinyreroll.guis.RollingScreenGui
import net.xpressdev.shinyreroll.utils.GeneralUtils
import net.xpressdev.shinyreroll.utils.Messages

object RerollManager {

    data class RollState(
        val items: MutableList<ItemStack>,
        val win: Boolean,
        var rollsLeft: Int,
        var elapsedTicks: Int = 0,
        var delay: Float,
    )

    val shownItemList = listOf(
        CobblemonItems.POKE_BALL.defaultStack, CobblemonItems.POKE_BALL.defaultStack, CobblemonItems.POKE_BALL.defaultStack,
        CobblemonItems.MASTER_BALL.defaultStack,
        CobblemonItems.POKE_BALL.defaultStack, CobblemonItems.POKE_BALL.defaultStack, CobblemonItems.POKE_BALL.defaultStack
    )

    private val rolls = mutableMapOf<UUID, RollState>()

    fun init(player: ServerPlayerEntity) {
        val win = Math.random() <= GeneralUtils.calculateCurrentShinyChance(player)

        rolls[player.uuid] = RollState(
            items = shownItemList.toMutableList(),
            win = win,
            rollsLeft = rollCountFor(win),
            delay = Random.nextDouble(0.6, 1.0).toFloat(),
        )
    }

    fun clear(player: ServerPlayerEntity) {
        rolls.remove(player.uuid)
    }

    fun isRolling(player: ServerPlayerEntity): Boolean = rolls.containsKey(player.uuid)

    fun itemsFor(player: ServerPlayerEntity): List<ItemStack> = rolls[player.uuid]?.items ?: shownItemList

    private fun rollCountFor(win: Boolean): Int {
        val period = shownItemList.size
        return if (win) {
            period * Random.nextInt(3, 7)
        } else {
            var count = Random.nextInt(15, 36)
            if (count % period == 0) count++
            count
        }
    }

    fun updateRolls() {
        val server = ShinyReroll.serverInstance

        rolls.entries.toList().forEach { (uuid, state) ->
            val player = server.playerManager.getPlayer(uuid)
            if (player == null) {
                rolls.remove(uuid)
                return@forEach
            }

            if (state.elapsedTicks >= state.delay && state.rollsLeft > 0) {
                doRoll(player, state)
                state.rollsLeft--
                state.elapsedTicks = 0
                state.delay += Random.nextDouble(0.6, 1.0).toFloat()
            } else {
                state.elapsedTicks++
            }

            if (state.rollsLeft <= 0) {
                finish(player, uuid, state)
            }
        }
    }

    private fun doRoll(player: ServerPlayerEntity, state: RollState) {
        player.playSoundToPlayer(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 0.5f, 1.0f)

        val item = state.items.removeFirst()
        state.items.add(item)

        RollingScreenGui.openGui(player)
    }

    private fun finish(player: ServerPlayerEntity, uuid: UUID, state: RollState) {
        rolls.remove(uuid)

        if (state.win) {
            player.playSoundToPlayer(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 1.0f, 1.0f)

            val pokemon = GeneralUtils.rollNewShiny(player)
            if (pokemon == null) {
                Messages.send(player, Message.ROLL_NOT_SHINY)
            } else {
                RollingScreenGui.openGui(player, pokemon)
                Messages.send(player, Message.ROLL_WIN, "species" to pokemon.species.name)
            }
        } else {
            GeneralUtils.removeSelectedPokemonFromPlayer(player)
            Messages.send(player, Message.ROLL_LOSS)
            player.playSoundToPlayer(SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.NEUTRAL, 1.0f, 0.1f)
        }
    }
}
