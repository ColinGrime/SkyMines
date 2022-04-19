package com.github.colingrime.updater;

import com.github.colingrime.SkyMines;
import com.github.colingrime.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public record SpigotUpdater(SkyMines plugin) {

    private static final String UPDATE_URL = "https://api.spigotmc.org/legacy/update.php?resource=101373";
    private static final String RESOURCE_URL = "https://www.spigotmc.org/resources/skymines.101373/";

    public void checkForUpdate() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(UPDATE_URL).openStream()))) {
            String latestVersion = reader.readLine();
            if (!plugin.getDescription().getVersion().equals(latestVersion)) {
                Logger.warn("A new update has been found (v" + latestVersion + ")");
                Logger.warn("Download it here: " + RESOURCE_URL);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}