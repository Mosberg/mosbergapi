package dk.mosberg.api.util;

import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Utility class providing helper methods for Entity operations
 */
public class EntityHelper {

    /**
     * Spawns an entity at the specified position
     */
    @Nullable
    public <T extends Entity> T spawn(ServerWorld world, EntityType<T> entityType, BlockPos pos) {
        return entityType.spawn(world, pos, SpawnReason.COMMAND);
    }

    /**
     * Spawns an entity at the specified position with a custom spawn reason
     */
    @Nullable
    public <T extends Entity> T spawn(ServerWorld world, EntityType<T> entityType, BlockPos pos,
            SpawnReason reason) {
        return entityType.spawn(world, pos, reason);
    }

    /**
     * Spawns an entity at the specified Vec3d position
     */
    @Nullable
    public <T extends Entity> T spawn(ServerWorld world, EntityType<T> entityType, Vec3d pos) {
        return entityType.spawn(world, BlockPos.ofFloored(pos), SpawnReason.COMMAND);
    }

    /**
     * Teleports an entity to the specified position
     */
    public void teleport(Entity entity, BlockPos pos) {
        entity.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                entity.getYaw(), entity.getPitch());
    }

    /**
     * Teleports an entity to the specified Vec3d position
     */
    public void teleport(Entity entity, Vec3d pos) {
        entity.refreshPositionAndAngles(pos.x, pos.y, pos.z, entity.getYaw(), entity.getPitch());
    }

    /**
     * Teleports an entity to another entity's position
     */
    public void teleportToEntity(Entity entity, Entity target) {
        entity.refreshPositionAndAngles(target.getX(), target.getY(), target.getZ(),
                entity.getYaw(), entity.getPitch());
    }

    /**
     * Damages a living entity (server-side only)
     */
    public boolean damage(ServerWorld world, LivingEntity entity, DamageSource source,
            float amount) {
        return entity.damage(world, source, amount);
    }

    /**
     * Heals a living entity
     */
    public void heal(LivingEntity entity, float amount) {
        entity.heal(amount);
    }

    /**
     * Gets the health of a living entity
     */
    public float getHealth(LivingEntity entity) {
        return entity.getHealth();
    }

    /**
     * Gets the max health of a living entity
     */
    public float getMaxHealth(LivingEntity entity) {
        return entity.getMaxHealth();
    }

    /**
     * Checks if an entity is alive
     */
    public boolean isAlive(LivingEntity entity) {
        return entity.isAlive();
    }

    /**
     * Gets entities within a radius of a position
     */
    public List<Entity> getEntitiesInRadius(World world, Vec3d center, double radius) {
        return world.getOtherEntities(null,
                net.minecraft.util.math.Box.of(center, radius * 2, radius * 2, radius * 2));
    }

    /**
     * Gets entities within a radius with a predicate filter
     */
    public List<Entity> getEntitiesInRadius(World world, Vec3d center, double radius,
            Predicate<Entity> predicate) {
        return world.getOtherEntities(null,
                net.minecraft.util.math.Box.of(center, radius * 2, radius * 2, radius * 2),
                predicate);
    }

    /**
     * Gets the nearest player to an entity
     */
    @Nullable
    public PlayerEntity getNearestPlayer(World world, Entity entity, double maxDistance) {
        return world.getClosestPlayer(entity, maxDistance);
    }

    /**
     * Gets the distance between two entities
     */
    public double getDistance(Entity entity1, Entity entity2) {
        return entity1.distanceTo(entity2);
    }

    /**
     * Checks if an entity is in water
     */
    public boolean isInWater(Entity entity) {
        return entity.isSubmergedInWater();
    }

    /**
     * Checks if an entity is on ground
     */
    public boolean isOnGround(Entity entity) {
        return entity.isOnGround();
    }

    /**
     * Sets an entity on fire for the specified seconds
     */
    public void setOnFire(Entity entity, int seconds) {
        entity.setOnFireFor(seconds);
    }

    /**
     * Extinguishes fire from an entity
     */
    public void extinguish(Entity entity) {
        entity.extinguish();
    }

    /**
     * Gets the entity's position as Vec3d
     */
    public Vec3d getPosition(Entity entity) {
        return new Vec3d(entity.getX(), entity.getY(), entity.getZ());
    }

    /**
     * Gets the entity's X coordinate
     */
    public double getX(Entity entity) {
        return entity.getX();
    }

    /**
     * Gets the entity's Y coordinate
     */
    public double getY(Entity entity) {
        return entity.getY();
    }

    /**
     * Gets the entity's Z coordinate
     */
    public double getZ(Entity entity) {
        return entity.getZ();
    }

    /**
     * Gets the entity's block position
     */
    public BlockPos getBlockPos(Entity entity) {
        return entity.getBlockPos();
    }

    /**
     * Removes an entity from the world
     */
    public void remove(Entity entity) {
        entity.discard();
    }

    /**
     * Gets the entity's yaw rotation
     */
    public float getYaw(Entity entity) {
        return entity.getYaw();
    }

    /**
     * Gets the entity's pitch rotation
     */
    public float getPitch(Entity entity) {
        return entity.getPitch();
    }

    /**
     * Sets the entity's position and rotation
     */
    public void setPositionAndRotation(Entity entity, double x, double y, double z, float yaw,
            float pitch) {
        entity.refreshPositionAndAngles(x, y, z, yaw, pitch);
    }
}
