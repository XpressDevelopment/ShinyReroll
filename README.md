# ShinyReroll

Lets players turn in a set of shiny Pokémon for a chance to roll a new random shiny, for
[Cobblemon](https://cobblemon.com/) on Fabric.

| | |
|---|---|
| Minecraft | 1.21.1 |
| Loader | Fabric |
| Requires | Fabric API, Fabric Language Kotlin, Cobblemon |
| Optional | Any Fabric permissions plugin (LuckPerms, etc.) |

---

## How it works

A player opens `/shinyreroll open`, selects one or more shiny Pokémon from their party and PC,
and confirms. Each selected shiny adds `reroll.chancePerPokemon` percent to the roll's win
chance, linearly and uncapped except by `reroll.maxPokemonSelected`. Winning grants one new
random shiny Pokémon at level 1 — species picked from whatever `allowed.*` permits — and
either outcome consumes every selected Pokémon.

The reward is delivered to the party if there is room, otherwise the PC. If both are full, the
reroll is refused and **nothing is consumed**. The selection is re-validated immediately
before consumption — if a selected Pokémon stopped resolving, stopped being shiny, or the
count is no longer within the configured limit, the roll aborts and nothing is touched.

Only shiny Pokémon can be selected; non-shiny entries in the selection GUIs render as an
unclickable "Not a Shiny Pokemon" placeholder.

---

## Config

Files live in `config/ShinyReroll/`. `/shinyreroll reload` re-reads both.

```
config/ShinyReroll/
├── config.json
└── lang.json
```

Upgrading from 1.x? See [`docs/MIGRATION_1.x_to_2.0.md`](docs/MIGRATION_1.x_to_2.0.md).

### `config.json`

```json
{
  "reroll": {
    "maxPokemonSelected": 10,
    "chancePerPokemon": 10.0
  },
  "allowed": {
    "legendary": false,
    "mythical": false,
    "ultraBeast": false,
    "paradox": false,
    "unimplemented": false
  }
}
```

| Key | Default | Notes |
|---|---|---|
| `reroll.maxPokemonSelected` | `10` | Most shinies a player may put into one reroll. |
| `reroll.chancePerPokemon` | `10.0` | **Percent** chance contributed by each selected shiny, e.g. `10.0` = 10%. Overall chance is `selectedCount × chancePerPokemon`, capped only by `maxPokemonSelected`. |
| `allowed.legendary` | `false` | Legendaries may be rolled as the reward. Also covers `RESTRICTED` species (the Tapus, the Treasures of Ruin, Ho-Oh). |
| `allowed.mythical` | `false` | Mythicals may be rolled as the reward. |
| `allowed.ultraBeast` | `false` | Ultra Beasts may be rolled as the reward. |
| `allowed.paradox` | `false` | Paradox Pokémon may be rolled as the reward. |
| `allowed.unimplemented` | `false` | Species Cobblemon has not implemented may be rolled as the reward. |

### `lang.json`

Every player-facing string, with `{placeholder}` substitution and MiniMessage formatting.
Delete a key to fall back to the default.

| Key | Placeholders |
|---|---|
| `command.config_reloaded` | |
| `command.roll_in_progress` | |
| `gui.party_pc_select.title` | |
| `gui.party_pc_select.from_party` | |
| `gui.party_pc_select.from_pc` | |
| `gui.party_pc_select.reroll` | |
| `gui.party_select.title` | |
| `gui.party_select.empty_slot` | |
| `gui.pc_select.title` | `{box}` |
| `gui.selected.title` | `{page}` |
| `gui.selected.too_many_name` | |
| `gui.selected.too_many_lore` | `{max}` |
| `gui.selected.must_select_one` | |
| `gui.confirmation.title` | `{chance}` |
| `gui.confirmation.prompt` | |
| `gui.rolling.title` | |
| `gui.empty` | |
| `gui.previous` | |
| `gui.next` | |
| `gui.confirm` | |
| `gui.cancel` | |
| `gui.info.name` | |
| `gui.info.current_chance` | `{chance}` |
| `gui.info.max_chance` | `{max}` |
| `gui.not_shiny` | |
| `roll.validation_failed` | |
| `roll.no_eligible_species` | |
| `roll.no_space` | |
| `roll.win` | `{species}` |
| `roll.loss` | |
| `pokemon.lore.level` | `{level}` |
| `pokemon.lore.nature` | `{nature}` |
| `pokemon.lore.ability` | `{ability}` |
| `pokemon.lore.ivs_header` | |
| `pokemon.lore.ivs_line_1` | `{hp}` `{atk}` `{def}` |
| `pokemon.lore.ivs_line_2` | `{spatk}` `{spdef}` `{spd}` |
| `pokemon.lore.evs_header` | |
| `pokemon.lore.evs_line_1` | `{hp}` `{atk}` `{def}` |
| `pokemon.lore.evs_line_2` | `{spatk}` `{spdef}` `{spd}` |

---

## Commands

`/shinyreroll`

| Command | What it does |
|---|---|
| `/shinyreroll reload` | Re-reads config and lang. |
| `/shinyreroll open` | Opens the reroll GUI for yourself. Refuses if you already have a roll in progress. |
| `/shinyreroll open <player>` | Opens the reroll GUI for another player. Supports tab completion and selectors. |

## Permissions

Operator level 2 grants everything. With a permissions plugin installed:

| Node | Grants |
|---|---|
| `shinyreroll.reload` | `/shinyreroll reload` |
| `shinyreroll.self` | `/shinyreroll open` |
| `shinyreroll.other` | `/shinyreroll open <player>` |

---

## Building

```
./gradlew build
```

The jar lands in `build/libs/`.

## Licence

MIT.
