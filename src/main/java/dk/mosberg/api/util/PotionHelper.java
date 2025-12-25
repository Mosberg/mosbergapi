package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * Helper class for working with potions and status effects in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for creating potion items, applying status effects, and
 * managing potion data on item stacks.
 *
 * <h2>Component-Based Potion System</h2>
 * <p>
 * Minecraft 1.21+ uses a component-based system for potions:
 * <ul>
 * <li><strong>DataComponentTypes.POTION_CONTENTS:</strong> Stores potion data on items</li>
 * <li><strong>PotionContentsComponent:</strong> Contains potion type and custom effects</li>
 * <li><strong>Registry-Based:</strong> Potions are registered in the dynamic registry</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Create a potion item
 * ItemStack potion = PotionHelper.createPotion(Potions.HEALING);
 *
 * // Apply effect to entity
 * PotionHelper.applyEffect(entity, StatusEffects.SPEED, 200, 1);
 *
 * // Check if entity has effect
 * if (PotionHelper.hasEffect(entity, StatusEffects.REGENERATION)) {
 *     // Entity has regeneration
 *          }
 *
 *          // Get potion from item
 *          RegistryEntry<Potion> potion = PotionHelper.getPotion(stack);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see Potion
 * @see StatusEffect
 * @see StatusEffectInstance
 * @see PotionContentsComponent
 */
public class PotionHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PotionHelper.class);

    private PotionHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Creates a potion bottle item stack.
     *
     * @param potion The potion registry entry
     * @return An item stack of a potion bottle
     * @throws NullPointerException if potion is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack healingPotion = PotionHelper.createPotion(Potions.HEALING);
     * }</pre>
     */
    @NotNull
    public static ItemStack createPotion(@NotNull RegistryEntry<Potion> potion) {
        if (potion == null)
            throw new NullPointerException("Potion cannot be null");

        ItemStack stack = new ItemStack(Items.POTION);
        stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));
        LOGGER.debug("Created potion: {}", getPotionId(potion));
        return stack;
    }

    /**
     * Creates a splash potion item stack.
     *
     * @param potion The potion registry entry
     * @return An item stack of a splash potion
     * @throws NullPointerException if potion is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack splashPoison = PotionHelper.createSplashPotion(Potions.POISON);
     * }</pre>
     */
    @NotNull
    public static ItemStack createSplashPotion(@NotNull RegistryEntry<Potion> potion) {
        if (potion == null)
            throw new NullPointerException("Potion cannot be null");

        ItemStack stack = new ItemStack(Items.SPLASH_POTION);
        stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));
        LOGGER.debug("Created splash potion: {}", getPotionId(potion));
        return stack;
    }

    /**
     * Creates a lingering potion item stack.
     *
     * @param potion The potion registry entry
     * @return An item stack of a lingering potion
     * @throws NullPointerException if potion is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack lingeringRegen = PotionHelper.createLingeringPotion(Potions.REGENERATION);
     * }</pre>
     */
    @NotNull
    public static ItemStack createLingeringPotion(@NotNull RegistryEntry<Potion> potion) {
        if (potion == null)
            throw new NullPointerException("Potion cannot be null");

        ItemStack stack = new ItemStack(Items.LINGERING_POTION);
        stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));
        LOGGER.debug("Created lingering potion: {}", getPotionId(potion));
        return stack;
    }

    /**
     * Creates tipped arrows with a potion effect.
     *
     * @param potion The potion registry entry
     * @param count The number of arrows
     * @return An item stack of tipped arrows
     * @throws NullPointerException if potion is null
     * @throws IllegalArgumentException if count is not positive
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack poisonArrows = PotionHelper.createTippedArrow(Potions.POISON, 64);
     * }</pre>
     */
    @NotNull
    public static ItemStack createTippedArrow(@NotNull RegistryEntry<Potion> potion, int count) {
        if (potion == null)
            throw new NullPointerException("Potion cannot be null");
        if (count <= 0)
            throw new IllegalArgumentException("Count must be positive");

        ItemStack stack = new ItemStack(Items.TIPPED_ARROW, count);
        stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));
        LOGGER.debug("Created {} tipped arrows: {}", count, getPotionId(potion));
        return stack;
    }

    /**
     * Gets the potion from an item stack.
     *
     * @param stack The item stack to check
     * @return The potion registry entry, or null if no potion
     * @throws NullPointerException if stack is null
     *
     * @example
     *
     *          <pre>{@code
     * RegistryEntry<Potion> potion = PotionHelper.getPotion(stack);
     * if (potion != null) {
     *          LOGGER.info("Potion: {}", PotionHelper.getPotionId(potion));
     *          }
     * }</pre>
     */
    @Nullable
    public static RegistryEntry<Potion> getPotion(@NotNull ItemStack stack) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");

        PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
        return contents != null ? contents.potion().orElse(null) : null;
    }

    /**
     * Sets the potion on an item stack.
     *
     * @param stack The item stack to modify
     * @param potion The potion registry entry
     * @throws NullPointerException if stack or potion is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack bottle = new ItemStack(Items.POTION);
     * PotionHelper.setPotion(bottle, Potions.STRENGTH);
     * }</pre>
     */
    public static void setPotion(@NotNull ItemStack stack, @NotNull RegistryEntry<Potion> potion) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");
        if (potion == null)
            throw new NullPointerException("Potion cannot be null");

        stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potion));
        LOGGER.debug("Set potion on stack: {}", getPotionId(potion));
    }

    /**
     * Checks if an item stack has a potion.
     *
     * @param stack The item stack to check
     * @return true if the item has potion data
     *
     * @example
     *
     *          <pre>{@code
     * if (PotionHelper.hasPotion(stack)) {
     *     // Stack has a potion
     *          }
     * }</pre>
     */
    public static boolean hasPotion(@NotNull ItemStack stack) {
        return getPotion(stack) != null;
    }

    /**
     * Applies a status effect to an entity.
     *
     * @param entity The entity to apply the effect to
     * @param effect The status effect
     * @param duration The duration in ticks
     * @param amplifier The amplifier (level - 1)
     * @throws NullPointerException if entity or effect is null
     * @throws IllegalArgumentException if duration is negative
     *
     * @example
     *
     *          <pre>{@code
     * // Apply Speed II for 30 seconds
     * PotionHelper.applyEffect(player, StatusEffects.SPEED, 600, 1);
     * }</pre>
     */
    public static void applyEffect(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect, int duration, int amplifier) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");
        if (duration < 0)
            throw new IllegalArgumentException("Duration cannot be negative");

        entity.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier));
        LOGGER.debug("Applied effect {} (level {}) for {} ticks to {}", getStatusEffectId(effect),
                amplifier + 1, duration, entity.getName().getString());
    }

    /**
     * Applies a status effect to an entity with default amplifier (0).
     *
     * @param entity The entity to apply the effect to
     * @param effect The status effect
     * @param duration The duration in ticks
     * @throws NullPointerException if entity or effect is null
     *
     * @example
     *
     *          <pre>{@code
     * // Apply Regeneration I for 10 seconds
     * PotionHelper.applyEffect(player, StatusEffects.REGENERATION, 200);
     * }</pre>
     */
    public static void applyEffect(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect, int duration) {
        applyEffect(entity, effect, duration, 0);
    }

    /**
     * Applies a status effect instance to an entity.
     *
     * @param entity The entity to apply the effect to
     * @param effect The status effect instance
     * @throws NullPointerException if entity or effect is null
     *
     * @example
     *
     *          <pre>{@code
     * StatusEffectInstance effect = new StatusEffectInstance(
     *     StatusEffects.STRENGTH, 1200, 1, false, true);
     * PotionHelper.applyEffect(player, effect);
     * }</pre>
     */
    public static void applyEffect(@NotNull LivingEntity entity,
            @NotNull StatusEffectInstance effect) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");
        if (effect == null)
            throw new NullPointerException("StatusEffectInstance cannot be null");

        entity.addStatusEffect(effect);
        LOGGER.debug("Applied effect instance {} to {}", getStatusEffectId(effect.getEffectType()),
                entity.getName().getString());
    }

    /**
     * Removes a status effect from an entity.
     *
     * @param entity The entity to remove the effect from
     * @param effect The status effect to remove
     * @throws NullPointerException if entity or effect is null
     *
     * @example
     *
     *          <pre>{@code
     * PotionHelper.removeEffect(player, StatusEffects.POISON);
     * }</pre>
     */
    public static void removeEffect(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");

        entity.removeStatusEffect(effect);
        LOGGER.debug("Removed effect {} from {}", getStatusEffectId(effect),
                entity.getName().getString());
    }

    /**
     * Checks if an entity has a specific status effect.
     *
     * @param entity The entity to check
     * @param effect The status effect to check for
     * @return true if the entity has the effect
     * @throws NullPointerException if entity or effect is null
     *
     * @example
     *
     *          <pre>{@code
     * if (PotionHelper.hasEffect(entity, StatusEffects.INVISIBILITY)) {
     *     // Entity is invisible
     *          }
     * }</pre>
     */
    public static boolean hasEffect(@NotNull LivingEntity entity,
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
     * @throws NullPointerException if entity or effect is null
     *
     * @example
     *
     *          <pre>{@code
     * StatusEffectInstance speed = PotionHelper.getEffect(entity, StatusEffects.SPEED);
     * if (speed != null) {
     *          LOGGER.info("Speed level: {}", speed.getAmplifier() + 1);
     *          }
     * }</pre>
     */
    @Nullable
    public static StatusEffectInstance getEffect(@NotNull LivingEntity entity,
            @NotNull RegistryEntry<StatusEffect> effect) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");

        return entity.getStatusEffect(effect);
    }

    /**
     * Clears all status effects from an entity.
     *
     * @param entity The entity to clear effects from
     * @throws NullPointerException if entity is null
     *
     * @example
     *
     *          <pre>{@code
     * PotionHelper.clearAllEffects(entity);
     * // Entity now has no status effects
     * }</pre>
     */
    public static void clearAllEffects(@NotNull LivingEntity entity) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");

        entity.clearStatusEffects();
        LOGGER.debug("Cleared all effects from {}", entity.getName().getString());
    }

    /**
     * Gets a potion by its identifier.
     *
     * @param id The identifier of the potion
     * @return The potion registry entry, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * RegistryEntry<Potion> potion = PotionHelper.getPotionEntry("minecraft:healing");
     * }</pre>
     */
    @Nullable
    public static RegistryEntry<Potion> getPotionEntry(@NotNull String id) {
        return getPotionEntry(Identifier.tryParse(id));
    }

    /**
     * Gets a potion by its identifier.
     *
     * @param id The identifier of the potion
     * @return The potion registry entry, or null if not found
     */
    @Nullable
    public static RegistryEntry<Potion> getPotionEntry(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.POTION.getEntry(Registries.POTION.getRawId(Registries.POTION.get(id)))
                .orElse(null);
    }

    /**
     * Checks if a potion exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the potion is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (PotionHelper.hasPotion("mosbergapi:custom_potion")) {
     *     LOGGER.info("Custom potion is registered!");
     *          }
     * }</pre>
     */
    public static boolean hasPotion(@NotNull String id) {
        return hasPotion(Identifier.tryParse(id));
    }

    /**
     * Checks if a potion exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the potion is registered
     */
    public static boolean hasPotion(@Nullable Identifier id) {
        return id != null && Registries.POTION.containsId(id);
    }

    /**
     * Gets the identifier of a potion.
     *
     * @param potion The potion registry entry
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = PotionHelper.getPotionId(Potions.HEALING);
     * // Returns: minecraft:healing
     * }</pre>
     */
    @Nullable
    public static Identifier getPotionId(@NotNull RegistryEntry<Potion> potion) {
        if (potion == null)
            throw new NullPointerException("Potion cannot be null");
        return potion.getKey().map(key -> key.getValue()).orElse(null);
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
     * Identifier id = PotionHelper.getStatusEffectId(StatusEffects.SPEED);
     * // Returns: minecraft:speed
     * }</pre>
     */
    @Nullable
    public static Identifier getStatusEffectId(@NotNull RegistryEntry<StatusEffect> effect) {
        if (effect == null)
            throw new NullPointerException("StatusEffect cannot be null");
        return effect.getKey().map(key -> key.getValue()).orElse(null);
    }

    /**
     * Gets all registered potions.
     *
     * @return An iterable of all potion registry entries
     *
     * @example
     *
     *          <pre>{@code
     * for (RegistryEntry<Potion> potion : PotionHelper.getAllPotions()) {
     *          LOGGER.info("Potion: {}", PotionHelper.getPotionId(potion));
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<RegistryEntry<Potion>> getAllPotions() {
        // Convert streamEntries to a list to match the return type
        return Registries.POTION.streamEntries().map(entry -> (RegistryEntry<Potion>) entry)
                .toList();
    }

    /**
     * Gets the number of registered potions.
     *
     * @return The count of potions
     *
     * @example
     *
     *          <pre>{@code
     * int count = PotionHelper.getPotionCount();
     *          LOGGER.info("Total potions registered: {}", count);
     * }</pre>
     */
    public static int getPotionCount() {
        return Registries.POTION.size();
    }
}
