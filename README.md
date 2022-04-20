# SkyMines

Fully customizable, personal skymines

### Features:

* Skymines are personal. Only the owner of a mine can access its panel.
* Skymines are upgradable, there are currently two paths:
  * BlockVariety - alter the composition of a mine.
  * ResetCooldown - alter the cooldown until a mine can be reset.
* Additionally, you can set an upgrade's cost and require permission for access.
* Fully customizable main menu / upgrade menu.
* Panels are accessible via right-click on mine wall or panel command.
* Skymines have their own homes that can be set (players must be close to the mine).
* Skymines can be personalized with the various options available in the config.yml.
* All messages and panel slots are configurable in the messages.yml and panel.yml files.
* Multiple storage options: MySQL, SQLite, YAML.

### Dependencies / Hooks:

* Vault is **required**. Used for skymine upgrades.
* FastAsyncWorldEdit hook is available for efficient skymine creation.

---

### Player Commands:

Aliases available: /skymines, /skymine, /sm

* `/skymines`
  * View help information on skymine commands.
* `/skymines list`
  * Lists the IDs of your skymines, along with an easy way to teleport to them.
* `/skymines panel [id]`
  * Opens up the main panel for the specified skymine.
* `/skymines home [id]` **(skymines.home)**
  * Teleports to the specified skymine.
* `/skymines sethome [id]` **(skymines.sethome)**
  * Sets the home of the specified skymine.
* `/skymines reset [id]`
  * Resets the specified skymine.
* `/skymines upgrades [id]`
  * Opens up the upgrade menu of the specified skymine.
* `/skymines pickup [id]`
  * Picks up the specified skymine.

### Admin Commands:

* `/skymines give [player] {LxHxW} {amount}` **(skymines.give)**
  * Gives the player the specified amount of skymine tokens.
  * Example: /skymines give Notch 10x10x10 5
* `/skymines reload` (skymines.reload)
  * Reloads the config.yml file.

---

README.md was last updated for v1.0.0
