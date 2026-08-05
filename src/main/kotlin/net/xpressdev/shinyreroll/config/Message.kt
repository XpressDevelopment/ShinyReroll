package net.xpressdev.shinyreroll.config

import net.xpressdev.xpresslib.lang.XpressMessage

enum class Message(override val key: String, override val default: String) : XpressMessage {

    COMMAND_CONFIG_RELOADED("command.config_reloaded", "<green>Config reloaded!"),
    COMMAND_PLAYER_NOT_FOUND("command.player_not_found", "<red>Player not found!"),
    COMMAND_ROLL_IN_PROGRESS("command.roll_in_progress", "<red>You already have a reroll in progress!"),

    GUI_PARTY_PC_SELECT_TITLE("gui.party_pc_select.title", "Shiny Reroll"),
    GUI_PARTY_PC_SELECT_FROM_PARTY("gui.party_pc_select.from_party", "Select from Party"),
    GUI_PARTY_PC_SELECT_FROM_PC("gui.party_pc_select.from_pc", "Select from PC"),
    GUI_PARTY_PC_SELECT_REROLL("gui.party_pc_select.reroll", "<gold>Reroll Shinies"),

    GUI_PARTY_SELECT_TITLE("gui.party_select.title", "Select Pokemon"),
    GUI_PARTY_SELECT_EMPTY_SLOT("gui.party_select.empty_slot", "Empty Slot"),

    GUI_PC_SELECT_TITLE("gui.pc_select.title", "Select Pokemon - Box {box}"),

    GUI_SELECTED_TITLE("gui.selected.title", "Selected Pokemon - {page}"),
    GUI_SELECTED_TOO_MANY_NAME("gui.selected.too_many_name", "<red>Too many Pokemon selected"),
    GUI_SELECTED_TOO_MANY_LORE("gui.selected.too_many_lore", "<gray>You can only select up to {max} Pokemon"),
    GUI_SELECTED_MUST_SELECT_ONE("gui.selected.must_select_one", "<red>You must select at least one Pokemon"),

    GUI_CONFIRMATION_TITLE("gui.confirmation.title", "Reroll Shiny - {chance}%"),
    GUI_CONFIRMATION_PROMPT("gui.confirmation.prompt", "<white><bold>Are you sure you want to reroll your shinies?"),

    GUI_ROLLING_TITLE("gui.rolling.title", "Shiny Reroll"),

    GUI_EMPTY("gui.empty", "Empty"),
    GUI_PREVIOUS("gui.previous", "Previous"),
    GUI_NEXT("gui.next", "Next"),
    GUI_CONFIRM("gui.confirm", "<green>Confirm"),
    GUI_CANCEL("gui.cancel", "<dark_red>Cancel"),
    GUI_INFO_NAME("gui.info.name", "<gold>Info"),
    GUI_INFO_CURRENT_CHANCE("gui.info.current_chance", "<gray>Current Shiny Chance: <gold>{chance}%"),
    GUI_INFO_MAX_CHANCE("gui.info.max_chance", "<gray>Max Shiny Chance: <gold>{max}%"),
    GUI_NOT_SHINY("gui.not_shiny", "<red>Not a Shiny Pokemon"),

    ROLL_VALIDATION_FAILED(
        "roll.validation_failed",
        "<red>Your selection changed since you confirmed — nothing was rerolled.",
    ),
    ROLL_NO_ELIGIBLE_SPECIES(
        "roll.no_eligible_species",
        "<red>No eligible Pokémon are available with the current settings.",
    ),
    ROLL_NO_SPACE("roll.no_space", "<red>Your party and PC are both full — nothing was consumed."),
    ROLL_WIN("roll.win", "<gold>You've received a <yellow>Shiny {species}<gold>!"),
    ROLL_LOSS("roll.loss", "<red>Womp Womp! Better luck next time!"),

    POKEMON_LORE_LEVEL("pokemon.lore.level", "<aqua>Level: <white>{level}"),
    POKEMON_LORE_NATURE("pokemon.lore.nature", "<yellow>Nature: <white>{nature}"),
    POKEMON_LORE_ABILITY("pokemon.lore.ability", "<gold>Ability: <white>{ability}"),
    POKEMON_LORE_IVS_HEADER("pokemon.lore.ivs_header", "<light_purple>IVs:"),
    POKEMON_LORE_IVS_LINE_1(
        "pokemon.lore.ivs_line_1",
        " <red>HP: <white>{hp}  <blue>Atk: <white>{atk}  <gray>Def: <white>{def}",
    ),
    POKEMON_LORE_IVS_LINE_2(
        "pokemon.lore.ivs_line_2",
        " <aqua>SpAtk: <white>{spatk}  <yellow>SpDef: <white>{spdef}  <green>Spd: <white>{spd}",
    ),
    POKEMON_LORE_EVS_HEADER("pokemon.lore.evs_header", "<dark_aqua>EVs:"),
    POKEMON_LORE_EVS_LINE_1(
        "pokemon.lore.evs_line_1",
        " <red>HP: <white>{hp}  <blue>Atk: <white>{atk}  <gray>Def: <white>{def}",
    ),
    POKEMON_LORE_EVS_LINE_2(
        "pokemon.lore.evs_line_2",
        " <aqua>SpAtk: <white>{spatk}  <yellow>SpDef: <white>{spdef}  <green>Spd: <white>{spd}",
    ),
    ;
}
