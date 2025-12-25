package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * Helper class for working with damage types in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for creating damage sources, checking damage properties,
 * and managing the damage type system.
 *
 * <h2>Damage Type System (1.21+)</h2>
 * <p>
 * Damage types are now data-driven through JSON files:
 * <ul>
 * <li><strong>Location:</strong> {@code data/<namespace>/damage_type/<name>.json}</li>
 * <li><strong>Registry Keys:</strong> Used to reference damage types in code</li>
 * <li><strong>Dynamic Registry:</strong> Loaded at runtime from data packs</li>
 * <li><strong>Properties:</strong> Scaling, exhaustion, effects, death messages</li>
 * </ul>
 *
 * <h2>Damage Properties</h2>
 * <ul>
 * <li><strong>Scaling:</strong> How damage scales with difficulty (never,
 * when_caused_by_living_non_player, always)</li>
 * <li><strong>Exhaustion:</strong> Amount of hunger exhaustion when taking damage</li>
 * <li><strong>Effects:</strong> Sound effects, fire damage behavior, etc.</li>
 * <li><strong>Death Messages:</strong> Customizable death message keys</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Create damage source
 * DamageSource source = DamageTypeHelper.create(world, DamageTypes.MAGIC);
 *
 * // Create damage with attacker
 * DamageSource attackerDamage = DamageTypeHelper.create(
 *     world, DamageTypes.MOB_ATTACK, attacker);
 *
 * // Deal damage to entity
 * entity.damage(serverWorld, source, 5.0f);
 *
 * // Check damage properties
 * if (DamageTypeHelper.bypassesArmor(world, DamageTypes.MAGIC)) {
 *     // This bypasses armor
 *          }
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see DamageType
 * @see DamageSource
 * @see RegistryKey
 */
public class DamageTypeHelper {
    private DamageTypeHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Checks if a damage type exists in the registry.
     *
     * @param world The world (for registry access)
     * @param damageType The damage type registry key
     * @return true if the damage type is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (DamageTypeHelper.exists(world, ModDamageTypes.CUSTOM)) {
     *     LOGGER.info("Custom damage type is registered!");
     *          }
     * }</pre>
     */
    public static boolean exists(@NotNull World world,
            @NotNull RegistryKey<DamageType> damageType) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (damageType == null)
            throw new NullPointerException("Damage type cannot be null");

        return world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE).contains(damageType);
    }

    /**
     * Creates a registry key for a damage type.
     *
     * @param id The damage type identifier
     * @return The registry key for the damage type
     * @throws NullPointerException if id is null
     *
     * @example
     *
     *          <pre>{@code
     * RegistryKey<DamageType> key = DamageTypeHelper.createKey(
     *     Identifier.of("mosbergapi", "acid_damage"));
     * }</pre>
     */
    @NotNull
    public static RegistryKey<DamageType> createKey(@NotNull Identifier id) {
        if (id == null)
            throw new NullPointerException("Identifier cannot be null");
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id);
    }

    /**
     * Creates a registry key for a damage type.
     *
     * @param id The damage type identifier string
     * @return The registry key for the damage type, or null if id is invalid
     *
     * @example
     *
     *          <pre>{@code
     * RegistryKey<DamageType> key = DamageTypeHelper.createKey("mosbergapi:acid_damage");
     * }</pre>
     */
    @Nullable
    public static RegistryKey<DamageType> createKey(@NotNull String id) {
        Identifier identifier = Identifier.tryParse(id);
        return identifier != null ? createKey(identifier) : null;
    }

    /**
     * Checks if a damage type is an explosion.
     *
     * @param source The damage source
     * @return true if the damage is from an explosion
     *
     * @example
     *
     *          <pre>{@code
     * if (DamageTypeHelper.isExplosion(source)) {
     *     // Apply blast protection
     *          }
     * }</pre>
     */
    public static boolean isExplosion(@NotNull DamageSource source) {
        if (source == null)
            throw new NullPointerException("DamageSource cannot be null");
        return source.isIn(DamageTypeTags.IS_EXPLOSION);
    }

    /**
     * Checks if a damage type is a projectile.
     *
     * @param source The damage source
     * @return true if the damage is from a projectile
     *
     * @example
     *
     *          <pre>{@code
     * if (DamageTypeHelper.isProjectile(source)) {
     *     // Apply projectile protection
     *          }
     * }</pre>
     */
    public static boolean isProjectile(@NotNull DamageSource source) {
        if (source == null)
            throw new NullPointerException("DamageSource cannot be null");
        return source.isIn(DamageTypeTags.IS_PROJECTILE);
    }

    /**
     * Gets the attacker entity from a damage source.
     *
     * @param source The damage source
     * @return The attacking entity, or null if none
     *
     * @example
     *
     *          <pre>{@code
     * Entity attacker = DamageTypeHelper.getAttacker(source);
     * if (attacker instanceof PlayerEntity player) {
     *     // Attacked by a player
     *          }
     * }</pre>
     */
    @Nullable
    public static Entity getAttacker(@NotNull DamageSource source) {
        if (source == null)
            throw new NullPointerException("DamageSource cannot be null");
        return source.getAttacker();
    }

    /**
     * Gets the source entity from a damage source (e.g., projectile).
     *
     * @param source The damage source
     * @return The source entity, or null if none
     *
     * @example
     *
     *          <pre>{@code
     * Entity sourceEntity = DamageTypeHelper.getSource(source);
     * if (sourceEntity instanceof ArrowEntity arrow) {
     *     // Damage from an arrow
     *          }
     * }</pre>
     */
    @Nullable
    public static Entity getSource(@NotNull DamageSource source) {
        if (source == null)
            throw new NullPointerException("DamageSource cannot be null");
        return source.getSource();
    }

    /**
     * Checks if damage source has an attacker.
     *
     * @param source The damage source
     * @return true if there is an attacker
     *
     * @example
     *
     *          <pre>{@code
     * if (DamageTypeHelper.hasAttacker(source)) {
     *     // This damage came from an entity
     *          }
     * }</pre>
     */
    public static boolean hasAttacker(@NotNull DamageSource source) {
        return getAttacker(source) != null;
    }

    /**
     * Gets all registered damage type keys.
     *
     * @param world The world (for registry access)
     * @return An iterable of all damage type keys
     *
     * @example
     *
     *          <pre>{@code
     * for (RegistryKey<DamageType> key : DamageTypeHelper.getAllKeys(world)) {
     *          LOGGER.info("Damage type: {}", key.getValue());
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<RegistryKey<DamageType>> getAllKeys(@NotNull World world) {
        if (world == null)
            throw new NullPointerException("World cannot be null");

        return world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE).streamEntries()
                .map(RegistryEntry.Reference::registryKey).toList();
    }

    /**
     * Gets the number of registered damage types.
     *
     * @param world The world (for registry access)
     * @return The count of damage types
     *
     * @example
     *
     *          <pre>{@code
     * int count = DamageTypeHelper.getDamageTypeCount(world);
     *          LOGGER.info("Total damage types: {}", count);
     * }</pre>
     */
    public static int getDamageTypeCount(@NotNull World world) {
        if (world == null)
            throw new NullPointerException("World cannot be null");

        return world.getRegistryManager().getOrThrow(RegistryKeys.DAMAGE_TYPE).size();
    }
}
