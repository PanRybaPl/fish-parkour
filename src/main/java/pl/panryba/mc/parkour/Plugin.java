package pl.panryba.mc.parkour;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig();
        PluginApi api = new PluginApi(config);

        getServer().getPluginManager().registerEvents(new PlayerListener(api), this);
    }
}
