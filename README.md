<div align="center">
    <img src="images/SkyMines.png" alt="NightBank" width="300" height="300" />
</div>

<div align="center">
    <h3>SkyMines</h3>
    <p>Fully customizable, personal skymines.</p>
</div>

## Dependencies:
* [`Midnight`](https://github.com/ColinGrime/Midnight) is **required**. It's my library! :)
* [`Vault`](https://www.spigotmc.org/resources/vault.34315/) is **required**. Used for skymine upgrades.
* Requires **Java 21**.

## Features:
* Mines are **personal**. Only the owner of a mine can access its panel.
* Mines are **upgradable**, there are currently two paths:
  * Block Variety - upgrade the composition of a mine.
  * Reset Cooldown - decrease the cooldown time to reset a mine.
* Additionally, you can set an upgrade's cost and require permission for access.
* **Fully customizable** main menu / upgrade menu.
* Panels are accessible via right-click on mine wall or panel command.
* Mines have their own homes that can be set (players must be close to the mine).
* Mines can be personalized with the various options available in the config.yml.
* All messages and panel slots are **configurable** in the messages.yml and panel.yml files.
* **Multiple storage options**: PostgreSQL, MySQL, and SQLite.

## Configurations:
* [`config.yml`](https://github.com/ColinGrime/SkyMines/blob/master/src/main/resources/config.yml) - Allows you to change the various settings associated with this plugin.
* [`messages.yml`](https://github.com/ColinGrime/SkyMines/blob/master/src/main/resources/messages.yml) - Allows you to change practically all messages used in this plugin.
* [`panels.yml`](https://github.com/ColinGrime/SkyMines/blob/master/src/main/resources/panels.yml) - Allows you to change the GUI layouts used in this plugin.

## Commands:
Aliases: /skymines, /skymine, /sm

* `/skymines`
  * View help information on skymine commands.
* `/skymines list`
  * Lists the IDs of your skymines, along with an easy way to teleport to them.
* `/skymines panel [id]`
  * Opens up the main panel for the specified skymine.
* `/skymines upgrades [id]`
  * Opens up the upgrade menu of the specified skymine.
* `/skymines home [id]` **(skymines.home)**
  * Teleports to the specified skymine.
* `/skymines sethome [id]` **(skymines.sethome)**
  * Sets the home of the specified skymine.
* `/skymines reset [id]`
  * Resets the specified skymine.
* `/skymines pickup [id]`
  * Picks up the specified skymine.

## Admin Commands:
Aliases: /skyminesadmin, /skymineadmin, /sma

* `/sma give [player] {LxHxW} {amount} {material}` **(skymines.admin.give)**
  * Gives the player the specified amount of skymine tokens.
  * Example: /skymines give Notch 10x10x10 5 BEDROCK
* `/sma lookup [player]` **(skymines.admin.lookup)**
  * Lists the IDs of the player's skymines.
* `/sma pickup [player] [id]` **(skymines.admin.pickup)**
  * Pick up the specified player's skymine.
* `/sma remove [player] [id]` **(skymines.admin.remove)**
  * Remove the specified player's skymine.
* `/sma reload` **(skymines.admin.reload)**
  * Reloads all configuration files.

## Permissions:
* `skymines.admin` - Access to the `/sma` command.
* `skymines.admin.give` - Access to the `/sma give` command.
* `skymines.admin.lookup` - Access to the `/sma lookup` command.
* `skymines.admin.panel` - All skymine panels are accessible via right-click on mine wall.
* `skymines.admin.pickup` - Access to the `/sma pickup` command.
* `skymines.admin.remove` - Access to the `/sma remove` command.
* `skymines.admin.reload` - Access to the `/sma reload` command.
* `skymines.home` - Teleport to your mine's home.
* `skymines.sethome` - Set your mine's home.

---

README.md was last updated on v1.3.0.
