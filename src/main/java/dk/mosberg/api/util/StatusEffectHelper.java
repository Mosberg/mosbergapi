package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * Helper class for working with status effects in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for managing status effects, checking effect properties,
 * and manipulating effect instances on entities.
 *
 * <h2>Status Effect System</h2>
 * <p>
 * Status effects modify entity behavior and attributes:
 * <ul>
 * <li><strong>Beneficial:</strong> Positive effects (Speed, Regeneration, etc.)</li>
 * <li><strong>Harmful:</strong> Negative effects (Poison, Slowness, etc.)</li>
 * <li><strong>Neutral:</strong> Neither good nor bad (Night Vision, Glowing)</li>
 * </ul>
 *
 * <h2>Effect Properties</h2>
 * <ul>
 * <li><strong>Duration:</strong> How long the effect lasts (in ticks)</li>
 * <li><strong>Amplifier:</strong> Effect strength (0 = level I, 1 = level II, etc.)</li>
 * <li><strong>Ambient:</strong> From beacon, less intrusive particles</li>
 * <li><strong>Particles:</strong> Whether to show particle effects</li>
 * <li><strong>Icon:</strong> Whether to show effect icon in inventory</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Apply effect
 * StatusEffectHelper.apply(entity, StatusEffects.SPEED, 600, 1);
 *
 * // Check effect category
 * if (StatusEffectHelper.isBeneficial(StatusEffects.REGENERATION)) {
 *     // This is a positive effect
 *          }
 *
 *          // Get effect duration remaining
 *          int remaining = StatusEffectHelper.getRemainingDuration(entity, StatusEffects.POISON);
 *
 *          // Extend effect duration
 *          StatusEffectHelper.extendDuration(entity, StatusEffects.STRENGTH, 200);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see StatusEffect
 * @see StatusEffectInstance
 * @see StatusEffectCategory
 */
public class StatusEffectHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusEffectHelper.class);

    /** One second in ticks */
    public static final int SECOND = 20;

    /** One minute in ticks */
    public static final int MINUTE = 60 * SECOND;

    private StatusEffectHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Applies a status effect to an entity.
     *
     * @param entity The entity to apply the effect to
     * @param effect The status effect
     * @param duration Duration in ticks
     * @param amplifier Effect amplifier (0 = level I)
     * @param ambient Whether this is an ambient effect (from beacon)
     * @param showParticles Whether to show particles
     * @param showIcon Whether to show the effect icon
     * @throws NullPointerException if entity or effect is null
     *
     * @example
     *
     *          <pre>{@code
     * // Apply Speed II for 30 seconds with particles
     * StatusEffectHelper.apply(player, StatusEffects.SPEED, 600, 1, false, true, true);
     * }</pre>
     */
    public static void apply(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect, int duration, int amplifier,
            boolean ambient, boolean showParticles, boolean showIcon) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");

        StatusEffectInstance instance = new StatusEffectInstance(effect, duration, amplifier,
                ambient, showParticles, showIcon);
        entity.addStatusEffect(instance);

        LOGGER.debug("Applied effect {} to {} (duration: {}, amplifier: {})", getId(effect),
                entity.getName().getString(), duration, amplifier);
    }

    /**
     * Applies a status effect with default settings (particles and icon visible).
     *
     * @param entity The entity to apply the effect to
     * @param effect The status effect
     * @param duration Duration in ticks
     * @param amplifier Effect amplifier
     *
     * @example
     *
     *          <pre>{@code
     * StatusEffectHelper.apply(entity, StatusEffects.REGENERATION, 400, 0);
     * }</pre>
     */
    public static void apply(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect, int duration, int amplifier) {
        apply(entity, effect, duration, amplifier, false, true, true);
    }

    /**
     * Applies a status effect with amplifier 0 (level I).
     *
     * @param entity The entity to apply the effect to
     * @param effect The status effect
     * @param duration Duration in ticks
     *
     * @example
     *
     *          <pre>{@code
     * // Apply Strength I for 1 minute
     * StatusEffectHelper.apply(player, StatusEffects.STRENGTH, StatusEffectHelper.MINUTE);
     * }</pre>
     */
    public static void apply(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect, int duration) {
        apply(entity, effect, duration, 0);
    }

    /**
     * Removes a status effect from an entity.
     *
     * @param entity The entity to remove the effect from
     * @param effect The status effect to remove
     * @return true if the effect was removed
     * @throws NullPointerException if entity or effect is null
     *
     * @example
     *
     *          <pre>{@code
     * if (StatusEffectHelper.remove(entity, StatusEffects.POISON)) {
     *     LOGGER.info("Cured poison!");
     *          }
     * }</pre>
     */
    public static boolean remove(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");

        boolean removed = entity.removeStatusEffect(effect);
        if (removed) {
            LOGGER.debug("Removed effect {} from {}", getId(effect), entity.getName().getString());
        }
        return removed;
    }

    /**
     * Checks if an entity has a specific status effect.
     *
     * @param entity The entity to check
     * @param effect The status effect to check for
     * @return true if the entity has the effect
     *
     * @example
     *
     *          <pre>{@code
     * if (StatusEffectHelper.has(entity, StatusEffects.INVISIBILITY)) {
     *     // Entity is invisible
     *          }
     * }</pre>
     */
    public static boolean has(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");

        return entity.hasStatusEffect(effect);
    }

    /**
     * Gets a status effect instance from an entity.
     *
     * @param entity The entity to check
     * @param effect The status effect to get
     * @return The status effect instance, or null if not present
     *
     * @example
     *
     *          <pre>{@code
     * StatusEffectInstance speed = StatusEffectHelper.get(entity, StatusEffects.SPEED);
     * if (speed != null) {
     *     int level = speed.getAmplifier() + 1;
     *     int remaining = speed.getDuration();
     *          }
     * }</pre>
     */
    @Nullable
    public static StatusEffectInstance get(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");

        return entity.getStatusEffect(effect);
    }

    /**
     * Gets the remaining duration of a status effect on an entity.
     *
     * @param entity The entity to check
     * @param effect The status effect to check
     * @return The remaining duration in ticks, or 0 if not present
     *
     * @example
     *
     *          <pre>{@code
     * int remaining = StatusEffectHelper.getRemainingDuration(entity, StatusEffects.POISON);
     *          LOGGER.info("Poison will last {} more seconds", remaining / 20);
     * }</pre>
     */
    public static int getRemainingDuration(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect) {
        StatusEffectInstance instance = get(entity, effect);
        return instance != null ? instance.getDuration() : 0;
    }

    /**
     * Gets the amplifier of a status effect on an entity.
     *
     * @param entity The entity to check
     * @param effect The status effect to check
     * @return The amplifier (0 = level I), or -1 if not present
     *
     * @example
     *
     *          <pre>{@code
     * int amplifier = StatusEffectHelper.getAmplifier(entity, StatusEffects.STRENGTH);
     * int level = amplifier + 1; // Convert to display level (I, II, III)
     * }</pre>
     */
    public static int getAmplifier(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect) {
        StatusEffectInstance instance = get(entity, effect);
        return instance != null ? instance.getAmplifier() : -1;
    }

    /**
     * Extends the duration of an existing status effect.
     *
     * @param entity The entity with the effect
     * @param effect The status effect to extend
     * @param additionalDuration Additional duration in ticks
     * @return true if the effect was extended
     *
     * @example
     *
     *          <pre>{@code
     * // Add 10 more seconds to existing Speed effect
     * StatusEffectHelper.extendDuration(entity, StatusEffects.SPEED, 200);
     * }</pre>
     */
    public static boolean extendDuration(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect, int additionalDuration) {
        StatusEffectInstance existing = get(entity, effect);
        if (existing == null)
            return false;

        int newDuration = existing.getDuration() + additionalDuration;
        apply(entity, effect, newDuration, existing.getAmplifier(), existing.isAmbient(),
                existing.shouldShowParticles(), existing.shouldShowIcon());

        LOGGER.debug("Extended effect {} on {} by {} ticks", getId(effect),
                entity.getName().getString(), additionalDuration);
        return true;
    }

    /**
     * Increases the amplifier of an existing status effect.
     *
     * @param entity The entity with the effect
     * @param effect The status effect to upgrade
     * @param amplifierIncrease Amount to increase amplifier by
     * @return true if the effect was upgraded
     *
     * @example
     *
     *          <pre>{@code
     * // Upgrade Speed I to Speed II
     * StatusEffectHelper.increaseAmplifier(entity, StatusEffects.SPEED, 1);
     * }</pre>
     */
    public static boolean increaseAmplifier(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect, int amplifierIncrease) {
        StatusEffectInstance existing = get(entity, effect);
        if (existing == null)
            return false;

        int newAmplifier = existing.getAmplifier() + amplifierIncrease;
        apply(entity, effect, existing.getDuration(), newAmplifier, existing.isAmbient(),
                existing.shouldShowParticles(), existing.shouldShowIcon());

        LOGGER.debug("Increased effect {} amplifier on {} by {}", getId(effect),
                entity.getName().getString(), amplifierIncrease);
        return true;
    }

    /**
     * Clears all status effects from an entity.
     *
     * @param entity The entity to clear effects from
     * @return The number of effects removed
     *
     * @example
     *
     *          <pre>{@code
     * int cleared = StatusEffectHelper.clearAll(entity);
     *          LOGGER.info("Cleared {} status effects", cleared);
     * }</pre>
     */
    public static int clearAll(@NotNull LivingEntity entity) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");

        int count = entity.getActiveStatusEffects().size();
        entity.clearStatusEffects();

        LOGGER.debug("Cleared {} effects from {}", count, entity.getName().getString());
        return count;
    }

    /**
     * Clears all harmful effects from an entity.
     *
     * @param entity The entity to clear effects from
     * @return The number of effects removed
     *
     * @example
     *
     *          <pre>{@code
     * // Remove all negative effects
     * int cleared = StatusEffectHelper.clearHarmful(entity);
     * }</pre>
     */
    public static int clearHarmful(@NotNull LivingEntity entity) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");

        int count = 0;
        for (StatusEffectInstance instance : entity.getActiveStatusEffects().values()) {
            if (instance.getEffectType().value().getCategory() == StatusEffectCategory.HARMFUL) {
                entity.removeStatusEffect(instance.getEffectType());
                count++;
            }
        }

        LOGGER.debug("Cleared {} harmful effects from {}", count, entity.getName().getString());
        return count;
    }

    /**
     * Clears all beneficial effects from an entity.
     *
     * @param entity The entity to clear effects from
     * @return The number of effects removed
     *
     * @example
     *
     *          <pre>{@code
     * // Remove all positive effects (for boss immunity mechanic)
     * StatusEffectHelper.clearBeneficial(boss);
     * }</pre>
     */
    public static int clearBeneficial(@NotNull LivingEntity entity) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");

        int count = 0;
        for (StatusEffectInstance instance : entity.getActiveStatusEffects().values()) {
            if (instance.getEffectType().value().getCategory() == StatusEffectCategory.BENEFICIAL) {
                entity.removeStatusEffect(instance.getEffectType());
                count++;
            }
        }

        LOGGER.debug("Cleared {} beneficial effects from {}", count, entity.getName().getString());
        return count;
    }

    /**
     * Checks if a status effect is beneficial.
     *
     * @param effect The status effect to check
     * @return true if the effect is beneficial
     *
     * @example
     *
     *          <pre>{@code
     * if (StatusEffectHelper.isBeneficial(effect)) {
     *     // This is a positive effect
     *          }
     * }</pre>
     */
    public static boolean isBeneficial(@NotNull RegistryEntry<StatusEffect> effect) {
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");
        return effect.value().getCategory() == StatusEffectCategory.BENEFICIAL;
    }

    /**
     * Checks if a status effect is harmful.
     *
     * @param effect The status effect to check
     * @return true if the effect is harmful
     *
     * @example
     *
     *          <pre>{@code
     * if (StatusEffectHelper.isHarmful(effect)) {
     *     // This is a negative effect
     *          }
     * }</pre>
     */
    public static boolean isHarmful(@NotNull RegistryEntry<StatusEffect> effect) {
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");
        return effect.value().getCategory() == StatusEffectCategory.HARMFUL;
    }

    /**
     * Checks if a status effect is neutral.
     *
     * @param effect The status effect to check
     * @return true if the effect is neutral
     *
     * @example
     *
     *          <pre>{@code
     * if (StatusEffectHelper.isNeutral(effect)) {
     *     // This effect is neither good nor bad
     *          }
     * }</pre>
     */
    public static boolean isNeutral(@NotNull RegistryEntry<StatusEffect> effect) {
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");
        return effect.value().getCategory() == StatusEffectCategory.NEUTRAL;
    }

    /**
     * Gets a status effect by its identifier.
     *
     * @param id The identifier of the status effect
     * @return The status effect registry entry, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * RegistryEntry<StatusEffect> effect =
     *     StatusEffectHelper.getEffect("minecraft:speed");
     * }</pre>
     */
    @Nullable
    public static RegistryEntry<StatusEffect> getEffect(@NotNull String id) {
        return getEffect(Identifier.tryParse(id));
    }

    /**
     * Gets a status effect by its identifier.
     *
     * @param id The identifier of the status effect
     * @return The status effect registry entry, or null if not found
     */
    @Nullable
    public static RegistryEntry<StatusEffect> getEffect(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.STATUS_EFFECT.getEntry(id).orElse(null);
    }

    /**
     * Checks if a status effect exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the status effect is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (StatusEffectHelper.hasEffect("mosbergapi:custom_effect")) {
     *     LOGGER.info("Custom effect is registered!");
     *          }
     * }</pre>
     */
    public static boolean hasEffect(@NotNull String id) {
        return hasEffect(Identifier.tryParse(id));
    }

    /**
     * Checks if a status effect exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the status effect is registered
     */
    public static boolean hasEffect(@Nullable Identifier id) {
        return id != null && Registries.STATUS_EFFECT.containsId(id);
    }

    /**
     * Gets the identifier of a status effect.
     *
     * @param effect The status effect registry entry
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = StatusEffectHelper.getId(StatusEffects.SPEED);
     * // Returns: minecraft:speed
     * }</pre>
     */
    @Nullable
    public static Identifier getId(@NotNull RegistryEntry<StatusEffect> effect) {
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");
        return effect.getKey().map(key -> key.getValue()).orElse(null);
    }

    /**
     * Gets the color of a status effect.
     *
     * @param effect The status effect
     * @return The color as an integer (RGB)
     *
     * @example
     *
     *          <pre>{@code
     * int color = StatusEffectHelper.getColor(StatusEffects.SPEED);
     * int red = (color >> 16) & 0xFF;
     * int green = (color >> 8) & 0xFF;
     * int blue = color & 0xFF;
     * }</pre>
     */
    public static int getColor(@NotNull RegistryEntry<StatusEffect> effect) {
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");
        return effect.value().getColor();
    }

    /**
     * Gets all registered status effects.
     *
     * @return An iterable of all status effect registry entries
     *
     * @example
     *
     *          <pre>{@code
     * for (RegistryEntry<StatusEffect> effect : StatusEffectHelper.getAllEffects()) {
     *          LOGGER.info("Effect: {}", StatusEffectHelper.getId(effect));
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<? extends RegistryEntry<StatusEffect>> getAllEffects() {
        return Registries.STATUS_EFFECT.streamEntries().toList();
    }

    /**
     * Gets the number of registered status effects.
     *
     * @return The count of status effects
     *
     * @example
     *
     *          <pre>{@code
     * int count = StatusEffectHelper.getEffectCount();
     *          LOGGER.info("Total status effects: {}", count);
     * }</pre>
     */
    public static int getEffectCount() {
        return Registries.STATUS_EFFECT.size();
    }
}
