# SkyMines
[WIP] Many features are NOT implemented yet.
More features/commands may be added in the future.

### Features:
* Fully customizable, personal sky mines.
* Supports mine resets with configurable chances for each block.
* MySQL integration to save mine info.

### Supports:
* Vault is required in order for skymines to be upgradeable.
* Hooks into IridumSkyblock to allow teammates to access skymines.

---

### Player Commands:

Tip: ID can be omitted if player owns only 1 mine~
* `/skymines` or `/skymines panel` (skymines.use)
  * Opens up the main panel for accessing your skymines.
* `/skymines [id]` or `/skymines panel [id]` (skymines.use)
  * Opens up the panel of the specified skymine.
* `/skymines list` (skymines.use)
  * Lists the IDs of your skymines, and gives you a fast way to teleport to them.
* `/skymines home [id]` (skymines.home)
  * Teleports to the specified skymine.
* `/skymines sethome [id]` (skymines.sethome)
  * Sets the home of the specified skymine.
* `/skymines reset [id]` (skymines.reset)
  * Resets the specified skymine.
  * If a player is inside an owned skymine, id can be omitted.

### Admin Commands:
* `/skymines give [player] {amount}` (skymines.give)
  * Gives the player the specified amount of skymine tokens.
* `/skymines reload` (skymines.reload)
  * Reloads the config.yml file.
