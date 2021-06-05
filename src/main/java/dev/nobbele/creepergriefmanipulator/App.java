package dev.nobbele.creepergriefmanipulator;

import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EntityExplodeEventHandler(), this);
    }

    @Override
    public void onDisable() {

    }
}