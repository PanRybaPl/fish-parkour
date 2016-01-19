package pl.panryba.mc.parkour;

import org.bukkit.Location;
import org.bukkit.World;
import org.khelekore.prtree.MBRConverter;
import org.khelekore.prtree.PRTree;
import org.khelekore.prtree.SimpleMBR;

import java.util.*;

public class RegionsManager {
    private class LocationMBRConverter implements MBRConverter<Location> {

        private final Map<World, Double> worldIds;

        public LocationMBRConverter(List<World> worlds) {
            worldIds = new HashMap<>();

            int n = 0;
            for(World world : worlds) {
                worldIds.put(world, (double) n);
                n++;
            }
        }

        @Override
        public int getDimensions() {
            return 4;
        }

        @Override
        public double getMin(int i, Location t) {
            switch (i) {
                case 0:
                    return t.getX();
                case 1:
                    return t.getY();
                case 2:
                    return t.getZ();
                case 3:
                    return this.worldIds.get(t.getWorld());
            }

            return 0;
        }

        @Override
        public double getMax(int i, Location t) {
            switch (i) {
                case 0:
                    return t.getX();
                case 1:
                    return t.getY();
                case 2:
                    return t.getZ();
                case 3:
                    return this.worldIds.get(t.getWorld());
            }

            return 0;
        }
    }

    private class RegionInfoMBRConverter implements MBRConverter<RegionInfo> {

        private final Map<World, Double> worldIds;

        public RegionInfoMBRConverter(List<World> worlds) {
            worldIds = new HashMap<>();

            int n = 0;
            for(World world : worlds) {
                worldIds.put(world, (double)n);
                n++;
            }
        }

        @Override
        public int getDimensions() {
            return 4;
        }

        @Override
        public double getMin(int i, RegionInfo regionInfo) {
            switch (i) {
                case 0:
                    return regionInfo.getFrom().getX();
                case 1:
                    return regionInfo.getFrom().getY();
                case 2:
                    return regionInfo.getFrom().getZ();
                case 3:
                    return this.worldIds.get(regionInfo.getWorld());
            }

            return 0;
        }

        @Override
        public double getMax(int i, RegionInfo regionInfo) {
            switch (i) {
                case 0:
                    return regionInfo.getTo().getX();
                case 1:
                    return regionInfo.getTo().getY();
                case 2:
                    return regionInfo.getTo().getZ();
                case 3:
                    return this.worldIds.get(regionInfo.getWorld());
            }

            return 0;
        }
    }
    private final RegionInfoMBRConverter converter;
    private final LocationMBRConverter locConverter;

    private PRTree<RegionInfo> prRegions;
    private static final int BRANCH_FACTOR = 30;

    public RegionsManager(List<World> worlds) {
        this.converter = new RegionInfoMBRConverter(worlds);
        this.locConverter = new LocationMBRConverter(worlds);
        this.prRegions = new PRTree<>(converter, BRANCH_FACTOR);
    }

    public boolean hasRegionAt(Location location) {
        return getRegionAt(location) != null;
    }

    public void refresh(Collection<RegionInfo> regions) {
        prRegions = new PRTree<>(converter, BRANCH_FACTOR);
        prRegions.load(regions);
    }

    public RegionInfo getRegionAt(Location location) {
        if(this.prRegions.isEmpty())
            return null;

        SimpleMBR locationMbr = new SimpleMBR(location, locConverter);
        Iterator<RegionInfo> i = prRegions.find(locationMbr).iterator();

        if(!i.hasNext()) {
            return null;
        }

        return i.next();
    }
}
