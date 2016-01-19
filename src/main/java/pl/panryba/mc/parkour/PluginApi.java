package pl.panryba.mc.parkour;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PanRyba.pl
 */
public class PluginApi {
    private RegionsManager regionsManager;
    private Map<Player, Location> players;

    public PluginApi(FileConfiguration config) {
        players = new HashMap<>();
        List<RegionInfo> regions = new ArrayList<>();
        List<Map<String, Object>> regionsSection = (List<Map<String, Object>>)config.getList("regions");

        for(Map<String, Object> map : regionsSection) {
            RegionInfo info = new RegionInfo();
            String worldName = (String)map.get("world");
            String regionName = (String)map.get("region");

            World world = Bukkit.getWorld(worldName);
            RegionManager manager = WGBukkit.getRegionManager(world);
            ProtectedRegion region = manager.getRegion(regionName);

            BlockVector min = region.getMinimumPoint();
            BlockVector max = region.getMaximumPoint();

            info.setFrom(min);
            info.setTo(max);
            info.setWorld(world);

            String message = (String)map.getOrDefault("msg", null);
            if(message != null) {
                message = ColorUtils.replaceColors(message);
            }
            info.setMessage(message);
            info.setTeleport(readLocation((Map<String, Object>) (map.get("tp"))));

            regions.add(info);
        }

        regionsManager = new RegionsManager(Bukkit.getWorlds());
        regionsManager.refresh(regions);
    }

    private Location readLocation(Map<String, Object> map) {
        World world = Bukkit.getWorld((String) map.get("world"));
        int x = (int)map.get("x");
        int y = (int)map.get("y");
        int z = (int)map.get("z");
        double yaw = (double)map.get("yaw");
        double pitch = (double)map.get("pitch");

        return new Location(world, x, y, z, (float)yaw, (float)pitch);
    }

    public void playerMoved(Player player, Location to) {
        Location previous = players.get(player);
        Location target = to.getBlock().getLocation();

        if(previous != null && previous.equals(target)) {
            return;
        }

        players.put(player, target);

        RegionInfo info = this.regionsManager.getRegionAt(target);
        if(info == null) {
            return;
        }

        player.teleport(info.getTeleport(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        String message = info.getMessage();
        if(message != null) {
            player.sendMessage(message);
        }
    }

    public void playerQuit(Player player) {
        this.players.remove(player);
    }
}
