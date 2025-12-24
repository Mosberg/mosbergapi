package dk.mosberg.api.util;

import java.util.List;
import java.util.Random;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.WorldProperties.SpawnPoint;

/**
 * Utility class providing helper methods for World operations
 */
public class WorldHelper {

    /**
     * Checks if the world is on the client side
     */
    public boolean isClient(World world) {
        return world.isClient();
    }

    /**
     * Checks if the world is on the server side
     */
    public boolean isServer(World world) {
        return !world.isClient();
    }

    /**
     * Gets the world time
     */
    public long getTime(World world) {
        return world.getTime();
    }

    /**
     * Gets the time of day (0-24000)
     */
    public long getTimeOfDay(World world) {
        return world.getTimeOfDay();
    }

    /**
     * Sets the time of day
     */
    public void setTimeOfDay(ServerWorld world, long time) {
        world.setTimeOfDay(time);
    }

    /**
     * Checks if it's daytime
     */
    public boolean isDaytime(World world) {
        return world.isDay();
    }

    /**
     * Checks if it's nighttime
     */
    public boolean isNighttime(World world) {
        return world.isNight();
    }

    /**
     * Checks if it's raining
     */
    public boolean isRaining(World world) {
        return world.isRaining();
    }

    /**
     * Checks if it's thundering
     */
    public boolean isThundering(World world) {
        return world.isThundering();
    }

    /**
     * Sets the weather to clear
     */
    public void clearWeather(ServerWorld world) {
        world.setWeather(6000, 0, false, false);
    }

    /**
     * Sets the weather to rain
     */
    public void setRaining(ServerWorld world, int duration) {
        world.setWeather(0, duration, true, false);
    }

    /**
     * Sets the weather to thunder
     */
    public void setThundering(ServerWorld world, int duration) {
        world.setWeather(0, duration, true, true);
    }

    /**
     * Creates an explosion at the specified position
     */
    public void createExplosion(World world, @Nullable Entity entity, double x, double y, double z,
            float power, boolean createFire) {
        world.createExplosion(entity, x, y, z, power, createFire, World.ExplosionSourceType.BLOCK);
    }

    /**
     * Creates an explosion at a BlockPos
     */
    public void createExplosion(World world, @Nullable Entity entity, BlockPos pos, float power,
            boolean createFire) {
        createExplosion(world, entity, pos.getX(), pos.getY(), pos.getZ(), power, createFire);
    }

    /**
     * Gets all players in the world
     */
    public List<? extends PlayerEntity> getPlayers(World world) {
        return world.getPlayers();
    }

    /**
     * Gets all entities in a box area
     */
    public List<Entity> getEntitiesInBox(World world, Box box) {
        return world.getOtherEntities(null, box);
    }

    /**
     * Gets the spawn position from the server (GlobalPos with dimension)
     */
    public SpawnPoint getServerSpawnPos(ServerWorld world) {
        return world.getServer().getSpawnPoint();
    }

    /**
     * Gets the spawn position as BlockPos (from the server's global spawn)
     */
    public BlockPos getSpawnPos(ServerWorld world) {
        return world.getServer().getSpawnPoint().getPos();
    }

    /**
     * Gets the spawn dimension from the server
     */
    public RegistryKey<World> getSpawnDimension(ServerWorld world) {
        return world.getServer().getSpawnPoint().getDimension();
    }

    /**
     * Creates a spawn point
     */
    public WorldProperties.SpawnPoint createSpawnPoint(RegistryKey<World> dimension, BlockPos pos,
            float yaw, float pitch) {
        return WorldProperties.SpawnPoint.create(dimension, pos, yaw, pitch);
    }

    /**
     * Checks if a chunk is loaded at the given position Uses chunk coordinates instead of
     * deprecated block position check
     */
    public boolean isChunkLoaded(World world, BlockPos pos) {
        return world.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4);
    }

    /**
     * Checks if a chunk is loaded at the given chunk coordinates
     */
    public boolean isChunkLoaded(World world, int chunkX, int chunkZ) {
        return world.isChunkLoaded(chunkX, chunkZ);
    }

    /**
     * Gets a random position within a radius
     */
    public BlockPos getRandomPos(Random random, BlockPos center, int radius) {
        int x = center.getX() + random.nextInt(radius * 2 + 1) - radius;
        int y = center.getY() + random.nextInt(radius * 2 + 1) - radius;
        int z = center.getZ() + random.nextInt(radius * 2 + 1) - radius;
        return new BlockPos(x, y, z);
    }

    /**
     * Finds the highest non-air block at X/Z coordinates
     */
    public BlockPos getTopPosition(World world, BlockPos pos) {
        return world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos);
    }

    /**
     * Gets the Y coordinate of the topmost block at X/Z coordinates with specific heightmap
     */
    public int getTopY(World world, Heightmap.Type heightmap, BlockPos pos) {
        return world.getTopY(heightmap, pos.getX(), pos.getZ());
    }

    /**
     * Gets the Y coordinate of the topmost block at X/Z coordinates
     */
    public int getTopY(World world, BlockPos pos) {
        return world.getTopY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ());
    }

    /**
     * Gets the dimension key
     */
    public String getDimensionName(World world) {
        return world.getRegistryKey().getValue().toString();
    }

    /**
     * Checks if a position has skylight access
     */
    public boolean canSeeSky(World world, BlockPos pos) {
        return world.isSkyVisible(pos);
    }

    /**
     * Gets the biome at a position
     */
    public String getBiomeName(World world, BlockPos pos) {
        return world.getBiome(pos).getKey().map(key -> key.getValue().toString()).orElse("unknown");
    }

    /**
     * Plays a sound at a position
     */
    public void playSound(World world, BlockPos pos, net.minecraft.sound.SoundEvent sound,
            float volume, float pitch) {
        world.playSound(null, pos, sound, net.minecraft.sound.SoundCategory.BLOCKS, volume, pitch);
    }

    /**
     * Spawns particles at a position
     */
    public void spawnParticles(ServerWorld world, net.minecraft.particle.ParticleEffect particle,
            Vec3d pos, int count, double deltaX, double deltaY, double deltaZ, double speed) {
        world.spawnParticles(particle, pos.x, pos.y, pos.z, count, deltaX, deltaY, deltaZ, speed);
    }

    /**
     * Gets the sea level of the world
     */
    public int getSeaLevel(World world) {
        return world.getSeaLevel();
    }

    /**
     * Gets the bottom Y level of the world
     */
    public int getBottomY(World world) {
        return world.getBottomY();
    }

    /**
     * Gets the height of the world
     */
    public int getHeight(World world) {
        return world.getHeight();
    }

    /**
     * Gets the maximum build height of the world
     */
    public int getMaxBuildHeight(World world) {
        return world.getTopY(null, 0, 0);
    }
}
