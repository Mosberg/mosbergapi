package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Helper class for working with particles in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for spawning particles, managing particle effects, and
 * creating visual feedback in both client and server contexts.
 *
 * <h2>Particle System</h2>
 * <p>
 * Minecraft's particle system allows for various visual effects:
 * <ul>
 * <li><strong>Simple Particles:</strong> Basic effects without extra data (smoke, flame)</li>
 * <li><strong>Parameterized Particles:</strong> Effects with color, size, or item data</li>
 * <li><strong>Client-Only:</strong> Particles are rendered client-side for performance</li>
 * <li><strong>Server Spawning:</strong> Server can request clients to spawn particles</li>
 * </ul>
 *
 * <h2>Common Particle Types</h2>
 * <ul>
 * <li>{@link ParticleTypes#FLAME} - Fire particles</li>
 * <li>{@link ParticleTypes#SMOKE} - Smoke particles</li>
 * <li>{@link ParticleTypes#HEART} - Heart particles (breeding, taming)</li>
 * <li>{@link ParticleTypes#EXPLOSION} - Explosion effects</li>
 * <li>{@link ParticleTypes#ENCHANT} - Enchantment table particles</li>
 * <li>{@link ParticleTypes#PORTAL} - Nether portal particles</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Spawn single particle
 * ParticleHelper.spawn(world, ParticleTypes.FLAME, pos);
 *
 * // Spawn particle cloud
 * ParticleHelper.spawnCloud(world, ParticleTypes.SMOKE, pos, 10, 0.5);
 *
 * // Spawn particles in circle
 * ParticleHelper.spawnCircle(world, ParticleTypes.ENCHANT, pos, 2.0, 20);
 *
 * // Spawn particles along line
 * ParticleHelper.spawnLine(world, ParticleTypes.END_ROD, startPos, endPos, 0.5);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see ParticleEffect
 * @see ParticleTypes
 */
public class ParticleHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParticleHelper.class);

    private ParticleHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Spawns a single particle at a specific position.
     *
     * @param world The world to spawn in (must be ServerWorld)
     * @param particle The particle effect to spawn
     * @param pos The position to spawn at
     * @throws NullPointerException if world, particle, or pos is null
     * @throws IllegalArgumentException if world is not a ServerWorld
     *
     * @example
     *
     *          <pre>{@code
     * ParticleHelper.spawn((ServerWorld) world, ParticleTypes.FLAME,
     *     new Vec3d(x, y, z));
     * }</pre>
     */
    public static void spawn(@NotNull World world, @NotNull ParticleEffect particle,
            @NotNull Vec3d pos) {
        spawn(world, particle, pos, 0, 0, 0, 0);
    }

    /**
     * Spawns a single particle at a block position.
     *
     * @param world The world to spawn in
     * @param particle The particle effect to spawn
     * @param pos The block position to spawn at
     *
     * @example
     *
     *          <pre>{@code
     * ParticleHelper.spawn((ServerWorld) world, ParticleTypes.SMOKE, blockPos);
     * }</pre>
     */
    public static void spawn(@NotNull World world, @NotNull ParticleEffect particle,
            @NotNull BlockPos pos) {
        spawn(world, particle, Vec3d.ofCenter(pos));
    }

    /**
     * Spawns particles with velocity.
     *
     * @param world The world to spawn in
     * @param particle The particle effect to spawn
     * @param pos The position to spawn at
     * @param velocityX X velocity
     * @param velocityY Y velocity
     * @param velocityZ Z velocity
     * @param speed Speed multiplier
     * @throws NullPointerException if world, particle, or pos is null
     *
     * @example
     *
     *          <pre>{@code
     * // Spawn particle moving upward
     * ParticleHelper.spawn(world, ParticleTypes.FLAME, pos, 0, 0.1, 0, 0.1);
     * }</pre>
     */
    public static void spawn(@NotNull World world, @NotNull ParticleEffect particle,
            @NotNull Vec3d pos, double velocityX, double velocityY, double velocityZ,
            double speed) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (particle == null)
            throw new NullPointerException("ParticleEffect cannot be null");
        if (pos == null)
            throw new NullPointerException("Position cannot be null");

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(particle, pos.x, pos.y, pos.z, 1, velocityX, velocityY,
                    velocityZ, speed);
            LOGGER.debug("Spawned particle {} at {}", particle.getType(), pos);
        } else {
            LOGGER.warn("Cannot spawn particles in non-ServerWorld");
        }
    }

    /**
     * Spawns multiple particles in a cloud pattern.
     *
     * @param world The world to spawn in
     * @param particle The particle effect to spawn
     * @param pos The center position
     * @param count Number of particles to spawn
     * @param spread Spread radius
     * @throws NullPointerException if world, particle, or pos is null
     *
     * @example
     *
     *          <pre>{@code
     * // Spawn 20 smoke particles in a 1 block radius
     * ParticleHelper.spawnCloud(world, ParticleTypes.SMOKE, pos, 20, 1.0);
     * }</pre>
     */
    public static void spawnCloud(@NotNull World world, @NotNull ParticleEffect particle,
            @NotNull Vec3d pos, int count, double spread) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (particle == null)
            throw new NullPointerException("ParticleEffect cannot be null");
        if (pos == null)
            throw new NullPointerException("Position cannot be null");

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(particle, pos.x, pos.y, pos.z, count, spread, spread, spread,
                    0.0);
            LOGGER.debug("Spawned {} {} particles at {} (spread: {})", count, particle.getType(),
                    pos, spread);
        }
    }

    /**
     * Spawns multiple particles in a cloud at a block position.
     *
     * @param world The world to spawn in
     * @param particle The particle effect to spawn
     * @param pos The center block position
     * @param count Number of particles
     * @param spread Spread radius
     *
     * @example
     *
     *          <pre>{@code
     * ParticleHelper.spawnCloud(world, ParticleTypes.EXPLOSION, blockPos, 10, 0.5);
     * }</pre>
     */
    public static void spawnCloud(@NotNull World world, @NotNull ParticleEffect particle,
            @NotNull BlockPos pos, int count, double spread) {
        spawnCloud(world, particle, Vec3d.ofCenter(pos), count, spread);
    }

    /**
     * Spawns particles in a circle pattern.
     *
     * @param world The world to spawn in
     * @param particle The particle effect to spawn
     * @param center The center position
     * @param radius The circle radius
     * @param particleCount Number of particles in the circle
     * @throws NullPointerException if world, particle, or center is null
     *
     * @example
     *
     *          <pre>{@code
     * // Spawn 30 enchantment particles in a 3 block radius circle
     * ParticleHelper.spawnCircle(world, ParticleTypes.ENCHANT, pos, 3.0, 30);
     * }</pre>
     */
    public static void spawnCircle(@NotNull World world, @NotNull ParticleEffect particle,
            @NotNull Vec3d center, double radius, int particleCount) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (particle == null)
            throw new NullPointerException("ParticleEffect cannot be null");
        if (center == null)
            throw new NullPointerException("Center position cannot be null");

        if (!(world instanceof ServerWorld))
            return;

        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI * i) / particleCount;
            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);
            spawn(world, particle, new Vec3d(x, center.y, z));
        }

        LOGGER.debug("Spawned {} particles in circle at {} (radius: {})", particleCount, center,
                radius);
    }

    /**
     * Spawns particles in a sphere pattern.
     *
     * @param world The world to spawn in
     * @param particle The particle effect to spawn
     * @param center The center position
     * @param radius The sphere radius
     * @param particleCount Number of particles
     *
     * @example
     *
     *          <pre>{@code
     * // Spawn portal particles in a sphere
     * ParticleHelper.spawnSphere(world, ParticleTypes.PORTAL, pos, 2.0, 50);
     * }</pre>
     */
    public static void spawnSphere(@NotNull World world, @NotNull ParticleEffect particle,
            @NotNull Vec3d center, double radius, int particleCount) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (particle == null)
            throw new NullPointerException("ParticleEffect cannot be null");
        if (center == null)
            throw new NullPointerException("Center position cannot be null");

        if (!(world instanceof ServerWorld))
            return;

        for (int i = 0; i < particleCount; i++) {
            double phi = Math.acos(1 - 2.0 * (i + 0.5) / particleCount);
            double theta = Math.PI * (1 + Math.sqrt(5)) * i;

            double x = center.x + radius * Math.sin(phi) * Math.cos(theta);
            double y = center.y + radius * Math.sin(phi) * Math.sin(theta);
            double z = center.z + radius * Math.cos(phi);

            spawn(world, particle, new Vec3d(x, y, z));
        }

        LOGGER.debug("Spawned {} particles in sphere at {} (radius: {})", particleCount, center,
                radius);
    }

    /**
     * Spawns particles along a line between two points.
     *
     * @param world The world to spawn in
     * @param particle The particle effect to spawn
     * @param start The start position
     * @param end The end position
     * @param spacing Distance between particles
     * @throws NullPointerException if world, particle, start, or end is null
     *
     * @example
     *
     *          <pre>{@code
     * // Draw a line of particles from player to target
     * ParticleHelper.spawnLine(world, ParticleTypes.END_ROD,
     *     player.getPos(), target.getPos(), 0.5);
     * }</pre>
     */
    public static void spawnLine(@NotNull World world, @NotNull ParticleEffect particle,
            @NotNull Vec3d start, @NotNull Vec3d end, double spacing) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (particle == null)
            throw new NullPointerException("ParticleEffect cannot be null");
        if (start == null)
            throw new NullPointerException("Start position cannot be null");
        if (end == null)
            throw new NullPointerException("End position cannot be null");

        if (!(world instanceof ServerWorld))
            return;

        Vec3d direction = end.subtract(start);
        double distance = direction.length();
        Vec3d step = direction.normalize().multiply(spacing);

        int particleCount = (int) (distance / spacing);
        Vec3d current = start;

        for (int i = 0; i <= particleCount; i++) {
            spawn(world, particle, current);
            current = current.add(step);
        }

        LOGGER.debug("Spawned {} particles along line from {} to {}", particleCount, start, end);
    }

    /**
     * Spawns particles in a helix pattern.
     *
     * @param world The world to spawn in
     * @param particle The particle effect to spawn
     * @param center The center position
     * @param height The helix height
     * @param radius The helix radius
     * @param rotations Number of full rotations
     * @param particleCount Total number of particles
     *
     * @example
     *
     *          <pre>{@code
     * // Spawn a magical helix effect
     * ParticleHelper.spawnHelix(world, ParticleTypes.ENCHANT, pos, 5.0, 1.5, 3, 60);
     * }</pre>
     */
    public static void spawnHelix(@NotNull World world, @NotNull ParticleEffect particle,
            @NotNull Vec3d center, double height, double radius, double rotations,
            int particleCount) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (particle == null)
            throw new NullPointerException("ParticleEffect cannot be null");
        if (center == null)
            throw new NullPointerException("Center position cannot be null");

        if (!(world instanceof ServerWorld))
            return;

        for (int i = 0; i < particleCount; i++) {
            double progress = (double) i / particleCount;
            double angle = 2 * Math.PI * rotations * progress;

            double x = center.x + radius * Math.cos(angle);
            double y = center.y + height * progress;
            double z = center.z + radius * Math.sin(angle);

            spawn(world, particle, new Vec3d(x, y, z));
        }

        LOGGER.debug("Spawned {} particles in helix at {} (height: {}, radius: {})", particleCount,
                center, height, radius);
    }

    /**
     * Gets a particle type by its identifier.
     *
     * @param id The identifier of the particle type
     * @return The particle type, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * ParticleType<?> type = ParticleHelper.getParticleType("minecraft:flame");
     * }</pre>
     */
    @Nullable
    public static ParticleType<?> getParticleType(@NotNull String id) {
        return getParticleType(Identifier.tryParse(id));
    }

    /**
     * Gets a particle type by its identifier.
     *
     * @param id The identifier of the particle type
     * @return The particle type, or null if not found
     */
    @Nullable
    public static ParticleType<?> getParticleType(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.PARTICLE_TYPE.get(id);
    }

    /**
     * Checks if a particle type exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the particle type is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (ParticleHelper.hasParticleType("mosbergapi:custom_particle")) {
     *     LOGGER.info("Custom particle is registered!");
     *          }
     * }</pre>
     */
    public static boolean hasParticleType(@NotNull String id) {
        return hasParticleType(Identifier.tryParse(id));
    }

    /**
     * Checks if a particle type exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the particle type is registered
     */
    public static boolean hasParticleType(@Nullable Identifier id) {
        return id != null && Registries.PARTICLE_TYPE.containsId(id);
    }

    /**
     * Gets the identifier of a particle type.
     *
     * @param type The particle type
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = ParticleHelper.getId(ParticleTypes.FLAME);
     * // Returns: minecraft:flame
     * }</pre>
     */
    @Nullable
    public static Identifier getId(@NotNull ParticleType<?> type) {
        if (type == null)
            throw new NullPointerException("ParticleType cannot be null");
        return Registries.PARTICLE_TYPE.getId(type);
    }

    /**
     * Gets all registered particle types.
     *
     * @return An iterable of all particle types
     *
     * @example
     *
     *          <pre>{@code
     * for (ParticleType<?> type : ParticleHelper.getAllParticleTypes()) {
     *          LOGGER.info("Particle: {}", ParticleHelper.getId(type));
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<ParticleType<?>> getAllParticleTypes() {
        return Registries.PARTICLE_TYPE;
    }

    /**
     * Gets the number of registered particle types.
     *
     * @return The count of particle types
     *
     * @example
     *
     *          <pre>{@code
     * int count = ParticleHelper.getParticleTypeCount();
     *          LOGGER.info("Total particles registered: {}", count);
     * }</pre>
     */
    public static int getParticleTypeCount() {
        return Registries.PARTICLE_TYPE.size();
    }
}
