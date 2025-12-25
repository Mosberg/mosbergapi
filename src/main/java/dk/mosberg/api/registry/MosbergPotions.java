package dk.mosberg.api.registry;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom potions in MosbergAPI. Register your potions here using the
 * provided helper methods.
 *
 * <p>In Minecraft 1.21.10+, potions are created with a name string and status effect instances.
 * The name is used for translation keys (e.g., "item.minecraft.potion.effect.custom_potion").
 *
 * @example
 * <pre>{@code
 * public static final RegistryEntry<Potion> CUSTOM_POTION = register("custom",
 *     new StatusEffectInstance(StatusEffects.STRENGTH, 3600, 0));
 *
 * public static final RegistryEntry<Potion> LONG_CUSTOM = register("long_custom",
 *     new StatusEffectInstance(StatusEffects.STRENGTH, 9600, 0));
 *
 * public static final RegistryEntry<Potion> STRONG_CUSTOM = register("strong_custom",
 *     new StatusEffectInstance(StatusEffects.STRENGTH, 1800, 1));
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergPotions {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergPotions.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers a potion with a single status effect.
     * The potion name will be used for the translation key.
     *
     * @param name The potion name (will be prefixed with mod ID for registry, used as-is for translation)
     * @param effect The status effect instance for this potion
     * @return The registered Potion entry
     *
     * @example
     * <pre>{@code
     * RegistryEntry<Potion> fireResistance = MosbergPotions.register("custom_fire_resistance",
     *     new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 3600));
     * // Translation key: "item.minecraft.potion.effect.custom_fire_resistance"
     * }</pre>
     */
    public static RegistryEntry<Potion> register(String name, StatusEffectInstance effect) {
        return register(name, name, effect);
    }

    /**
     * Registers a potion with multiple status effects.
     *
     * @param name The potion name (will be prefixed with mod ID for registry)
     * @param effects The status effect instances for this potion
     * @return The registered Potion entry
     *
     * @example
     * <pre>{@code
     * RegistryEntry<Potion> multiEffect = MosbergPotions.register("multi_buff",
     *     new StatusEffectInstance(StatusEffects.STRENGTH, 1200),
     *     new StatusEffectInstance(StatusEffects.SPEED, 1200));
     * }</pre>
     */
    public static RegistryEntry<Potion> register(String name, StatusEffectInstance... effects) {
        return register(name, name, effects);
    }

    /**
     * Registers a potion with a custom translation key name and status effects.
     *
     * @param registryName The registry name (will be prefixed with mod ID)
     * @param translationName The name used for translation keys
     * @param effects The status effect instances for this potion
     * @return The registered Potion entry
     *
     * @example
     * <pre>{@code
     * // Registry: mosbergapi:healing_potion
     * // Translation: "item.minecraft.potion.effect.healing"
     * RegistryEntry<Potion> healing = MosbergPotions.register(
     *     "healing_potion",
     *     "healing",
     *     new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 0));
     * }</pre>
     */
    public static RegistryEntry<Potion> register(String registryName, String translationName,
            StatusEffectInstance... effects) {
        Identifier id = Identifier.of(MOD_ID, registryName);
        Potion potion = new Potion(translationName, effects);
        Potion registered = Registry.register(Registries.POTION, id, potion);
        LOGGER.debug("Registered potion: {} with {} effect(s)", registryName, effects.length);
        return Registries.POTION.getEntry(registered);
    }

    /**
     * Creates a standard potion set (normal, long, strong) with automatic naming.
     * Uses standard naming convention: base, "long_" + base, "strong_" + base.
     *
     * @param baseName The base name (e.g., "healing")
     * @param normalEffect The effect for normal variant
     * @param longEffect The effect for long variant (typically longer duration)
     * @param strongEffect The effect for strong variant (typically higher amplifier)
     * @return A PotionSet containing all three variants
     *
     * @example
     * <pre>{@code
     * PotionSet strength = MosbergPotions.registerSet("strength",
     *     new StatusEffectInstance(StatusEffects.STRENGTH, 1800, 0),  // 1:30 @ Level I
     *     new StatusEffectInstance(StatusEffects.STRENGTH, 4800, 0),  // 4:00 @ Level I
     *     new StatusEffectInstance(StatusEffects.STRENGTH, 900, 1));  // 0:45 @ Level II
     * }</pre>
     */
    public static PotionSet registerSet(String baseName, StatusEffectInstance normalEffect,
            StatusEffectInstance longEffect, StatusEffectInstance strongEffect) {
        RegistryEntry<Potion> normal = register(baseName, normalEffect);
        RegistryEntry<Potion> longVariant = register("long_" + baseName, "long_" + baseName, longEffect);
        RegistryEntry<Potion> strong = register("strong_" + baseName, "strong_" + baseName, strongEffect);

        LOGGER.debug("Registered potion set: {} (normal, long, strong)", baseName);
        return new PotionSet(normal, longVariant, strong);
    }

    /**
     * Creates a basic potion set with only normal and long variants (no strong variant).
     * Useful for utility potions like Night Vision, Invisibility, etc.
     *
     * @param baseName The base name
     * @param normalEffect The effect for normal variant
     * @param longEffect The effect for long variant
     * @return A PotionSet with only normal and long variants (strong will be null)
     *
     * @example
     * <pre>{@code
     * PotionSet nightVision = MosbergPotions.registerBasicSet("night_vision",
     *     new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3600, 0),  // 3:00
     *     new StatusEffectInstance(StatusEffects.NIGHT_VISION, 9600, 0)); // 8:00
     * }</pre>
     */
    public static PotionSet registerBasicSet(String baseName, StatusEffectInstance normalEffect,
            StatusEffectInstance longEffect) {
        RegistryEntry<Potion> normal = register(baseName, normalEffect);
        RegistryEntry<Potion> longVariant = register("long_" + baseName, "long_" + baseName, longEffect);

        LOGGER.debug("Registered basic potion set: {} (normal, long)", baseName);
        return new PotionSet(normal, longVariant, null);
    }

    /**
     * Initialize and register all potions. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI potions");
        // Potion registration happens when register() is called
    }

    /**
     * Record to hold a standard set of normal, long, and strong potion variants.
     *
     * @param normal The base potion variant
     * @param longVariant The extended duration variant
     * @param strong The increased power variant (can be null for basic sets)
     */
    public record PotionSet(
        RegistryEntry<Potion> normal,
        RegistryEntry<Potion> longVariant,
        @Nullable RegistryEntry<Potion> strong
    ) {
        /**
         * Checks if this set has a strong variant.
         *
         * @return true if the strong variant exists
         */
        public boolean hasStrongVariant() {
            return strong != null;
        }
    }
}
