package net.xpressdev.shinyreroll.utils

import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.xpressdev.shinyreroll.config.ConfigStore
import net.xpressdev.shinyreroll.config.Message
import net.xpressdev.xpresslib.text.Messenger

object Messages {

    private val messenger = Messenger(ConfigStore::lang, ColorUtil::toText)

    fun text(message: Message, vararg placeholders: Pair<String, String>): Text =
        messenger.render(message, *placeholders)

    fun raw(text: String): Text = messenger.renderRaw(text)

    fun send(player: ServerPlayerEntity, message: Message, vararg placeholders: Pair<String, String>) {
        player.sendMessage(text(message, *placeholders), false)
    }

    fun feedback(source: ServerCommandSource, message: Message, vararg placeholders: Pair<String, String>) {
        val rendered = text(message, *placeholders)
        source.sendFeedback({ rendered }, true)
    }

    fun error(source: ServerCommandSource, message: Message, vararg placeholders: Pair<String, String>) {
        source.sendError(text(message, *placeholders))
    }
}
