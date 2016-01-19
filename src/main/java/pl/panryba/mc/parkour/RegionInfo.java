package pl.panryba.mc.parkour;

import com.sk89q.worldedit.BlockVector;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author PanRyba.pl
 */
public class RegionInfo {
    private Location teleport;
    private BlockVector from;
    private BlockVector to;
    private World world;
    private String message;

    public BlockVector getFrom() {
        return from;
    }

    public BlockVector getTo() {
        return to;
    }

    public Location getTeleport() {
        return teleport;
    }

    public void setFrom(BlockVector from) {
        this.from = from;
    }

    public void setTo(BlockVector to) {
        this.to = to;
    }

    public void setTeleport(Location teleport) {
        this.teleport = teleport;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
