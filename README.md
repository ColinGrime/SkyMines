<div align="center">
    <h3>SkyMines</h3>
    <p>Fully customizable, personal skymines.</p>
</div>

## Dependencies:
* [`Midnight`](https://github.com/ColinGrime/Midnight) is **required**. It's my library! :)
* [`Vault`](https://www.spigotmc.org/resources/vault.34315/) is **required**. Used for skymine upgrades.
* Requires **Java 21**.

## Overview:
SkyMines allows you to give players physical tokens that place private, upgradeable mines. 
Each token represents a specific mine type with customizable size, layout, and upgrades. 
Once placed, the mine is only accessible to the owner and can be upgraded through a built-in menu. 
Server admins can define unique mine types and upgrade paths, including block composition and reset cooldowns. 
All menus, messages, and behaviors are configurable through the YAML files listed below.

## Features:
* Mines are **personal** – only the owner of a mine can access and manage them.
* Mines are **placed** using physical token items.
* Mines are **accessed** via right-click on the wall or the `/sm panel` command.
* Mines have **holograms** that can show its name, owner, and reset timer.
* Mines have **homes** that can be set (players must be close to the mine).
* Define **custom mine types** in `mines.yml`:
  * Customize token appearance.
  * Set default size and border type.
  * Link to unique upgrade paths.
* Define **custom upgrade types** in `upgrades.yml`:
  * Composition – define block distributions per level.
  * Reset Cooldown – reduce time between resets.
* Additionally, you can set an upgrade's cost and optional upgrade permission.
* Upgrade menus are **dynamic**—no manual updates needed when editing upgrades.
* Mines can be customized with the various options available in the `config.yml`.
* All messages and panel slots are **configurable** in the `messages.yml` and `menus.yml` files.
* **Multiple storage options**: PostgreSQL, MySQL, SQLite

## Player Workflow
* Players can receive **SkyMine Tokens** that hold the type and size of their mine.
* Players can right-click a **SkyMine Token** over air to place it down.
* Players can right-click the border of a **SkyMine** to open its Main Menu.
* Players can then choose to Go Home, Reset Mine, Upgrade the Mine, or Pickup Mine.
* Players can set the mine's home via the `/sm sethome [id]` command.
* Players can set the mine's name via the `/sm name [id] [name]` command.
* Players can list and teleport to their mines via the `/sm list` command.
* Players can edit their mine preferences via the `/sm settings` command.
  * `Notifications`: Toggle cooldown and other mine notifications.
  * `Automatic Reset`: Toggle automatic resetting of mines (player must be online).
* Players can shift-left-click the border of a **SkyMine** to instantly teleport to its home.

## Configurations:
* [`mines.yml`](https://github.com/ColinGrime/SkyMines/blob/master/src/main/resources/mines.yml) - Allows you to define custom mine types with unique upgrade paths.
* [`upgrades.yml`](https://github.com/ColinGrime/SkyMines/blob/master/src/main/resources/upgrades.yml) - Allows you to define custom upgrade types to associate with mines.
* [`config.yml`](https://github.com/ColinGrime/SkyMines/blob/master/src/main/resources/config.yml) - Allows you to change the various settings associated with this plugin.
* [`messages.yml`](https://github.com/ColinGrime/SkyMines/blob/master/src/main/resources/messages.yml) - Allows you to change practically all messages used in this plugin.
* [`menus.yml`](https://github.com/ColinGrime/SkyMines/blob/master/src/main/resources/menus.yml) - Allows you to change the GUI layouts used in this plugin.

## Commands:
Aliases: /skymines, /skymine, /sm

* `/skymines`
  * View help information on skymine commands.
* `/skymines list`
  * Lists the IDs of your skymines, along with an easy way to teleport to them.
* `/skymines name [id] [name]`
  * Attaches a name to the specified mine.
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
* `/skymines settings`
  * Opens up the preferences menu.

## Admin Commands:
Aliases: /skyminesadmin, /skymineadmin, /sma

* `/sma give [player] {mine} {LxHxW} {amount}` **(skymines.admin.give)**
  * Gives the player mine tokens with the specified type, size, and amount.
  * Example: /skymines give Notch default 10x10x10 5
* `/sma lookup [player]` **(skymines.admin.lookup)**
  * Lookup the IDs of the player's skymines.
* `/sma pickup [player] [id]` **(skymines.admin.pickup)**
  * Pickup the specified player's skymine.
* `/sma remove [player] [id]` **(skymines.admin.remove)**
  * Remove the specified player's skymine.
* `/sma list` **(skymines.admin.list)**
  * Lists all enabled/disabled mines.
* `/sma debug` **(skymines.admin.debug)**
  * Displays information for the selected mine.
* `/sma reload` **(skymines.admin.reload)**
  * Reloads all configuration files.

## Permissions:
Player:
* `skymines.name` - Set your mine's name.
* `skymines.home` - Teleport to your mine's home.
* `skymines.sethome` - Set your mine's home.
* `skymines.settings` - Open the preference menu.
* `skymines.settings.notify` - Toggle the `Notifications` setting.
* `skymines.settings.autoreset` - Toggle the `Auto Reset` setting.

Admin:
* `skymines.admin` - Access to the `/sma` command.
* `skymines.admin.debug` - Access to the `/sma debug` command.
* `skymines.admin.give` - Access to the `/sma give` command.
* `skymines.admin.list` - Access to the `/sma list` command.
* `skymines.admin.lookup` - Access to the `/sma lookup` command.
* `skymines.admin.panel` - All skymine panels are accessible via right-click on mine wall.
* `skymines.admin.pickup` - Access to the `/sma pickup` command.
* `skymines.admin.remove` - Access to the `/sma remove` command.
* `skymines.admin.reload` - Access to the `/sma reload` command.

---

README.md was last updated on v2.1.0.
