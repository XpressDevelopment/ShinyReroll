package net.xpressdev.shinyreroll.config

data class RerollSettings(
    val maxPokemonSelected: Int = 10,
    val chancePerPokemon: Double = 10.0,
) {
    val chanceFraction: Double get() = chancePerPokemon / 100.0
}

data class AllowedSettings(
    val legendary: Boolean = false,
    val mythical: Boolean = false,
    val ultraBeast: Boolean = false,
    val paradox: Boolean = false,
    val unimplemented: Boolean = false,
)

data class ShinyRerollConfig(
    val reroll: RerollSettings = RerollSettings(),
    val allowed: AllowedSettings = AllowedSettings(),
)
