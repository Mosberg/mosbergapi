package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom status effects in MosbergAPI. Register your status effects here
 * using the provided helper methods.
 *
 * @example
 * <pre>{@code
 * public static final RegistryEntry<StatusEffect> CUSTOM_EFFECT = register("custom_effect",
 *     new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x98D982));
 *
 * public static final RegistryEntry<StatusEffect> CURSE_EFFECT = register("curse",
 *     new CustomStatusEffect(StatusEffectCategory.HARMFUL, 0xFF0000)
 *         .addAttributeModifier(EntityAttributes.MOVEMENT_SPEED,
 *             Identifier.of("mosbergapi", "curse_slowness"),
 *             -0.15, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergStatusEffects {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergStatusEffects.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers a status effect with a simple name.
     *
     * @param name The effect name (will be prefixed with mod ID)
     * @param effect The StatusEffect to register
     * @return The registered StatusEffect entry
     *
     * @example
     * <pre>{@code
     * RegistryEntry<StatusEffect> poison = MosbergStatusEffects.register("deadly_poison",
     *     new PoisonStatusEffect(StatusEffectCategory.HARMFUL, 0x4E9331));
     * }</pre>
     */
    public static RegistryEntry<StatusEffect> register(String name, StatusEffect effect) {
        Identifier id = Identifier.of(MOD_ID, name);
        StatusEffect registered = Registry.register(Registries.STATUS_EFFECT, id, effect);
        LOGGER.debug("Registered status effect: {}", name);
        return Registries.STATUS_EFFECT.getEntry(registered);
    }

    /**
     * Registers a beneficial status effect with default settings.
     *
     * @param name The effect name
     * @param color The particle color (RGB hex)
     * @param effectClass The StatusEffect implementation class
     * @return The registered StatusEffect entry
     *
     * @example
     * <pre>{@code
     * RegistryEntry<StatusEffect> blessing = MosbergStatusEffects.registerBeneficial(
     *     "blessing", 0xFFD700, BlessingStatusEffect.class);
     * }</pre>
     */
    public static <T extends StatusEffect> RegistryEntry<StatusEffect> registerBeneficial(
            String name, int color, Class<T> effectClass) {
        try {
            StatusEffect effect = effectClass.getConstructor(StatusEffectCategory.class, int.class)
                .newInstance(StatusEffectCategory.BENEFICIAL, color);
            return register(name, effect);
        } catch (Exception e) {
            LOGGER.error("Failed to create beneficial effect: {}", name, e);
            throw new RuntimeException("Failed to register beneficial effect", e);
        }
    }

    /**
     * Registers a harmful status effect with default settings.
     *
     * @param name The effect name
     * @param color The particle color (RGB hex)
     * @param effectClass The StatusEffect implementation class
     * @return The registered StatusEffect entry
     *
     * @example
     * <pre>{@code
     * RegistryEntry<StatusEffect> curse = MosbergStatusEffects.registerHarmful(
     *     "curse", 0x8B0000, CurseStatusEffect.class);
     * }</pre>
     */
    public static <T extends StatusEffect> RegistryEntry<StatusEffect> registerHarmful(
            String name, int color, Class<T> effectClass) {
        try {
            StatusEffect effect = effectClass.getConstructor(StatusEffectCategory.class, int.class)
                .newInstance(StatusEffectCategory.HARMFUL, color);
            return register(name, effect);
        } catch (Exception e) {
            LOGGER.error("Failed to create harmful effect: {}", name, e);
            throw new RuntimeException("Failed to register harmful effect", e);
        }
    }

    /**
     * Registers a neutral status effect with default settings.
     *
     * @param name The effect name
     * @param color The particle color (RGB hex)
     * @param effectClass The StatusEffect implementation class
     * @return The registered StatusEffect entry
     *
     * @example
     * <pre>{@code
     * RegistryEntry<StatusEffect> glow = MosbergStatusEffects.registerNeutral(
     *     "glow", 0xFFFFFF, GlowStatusEffect.class);
     * }</pre>
     */
    public static <T extends StatusEffect> RegistryEntry<StatusEffect> registerNeutral(
            String name, int color, Class<T> effectClass) {
        try {
            StatusEffect effect = effectClass.getConstructor(StatusEffectCategory.class, int.class)
                .newInstance(StatusEffectCategory.NEUTRAL, color);
            return register(name, effect);
        } catch (Exception e) {
            LOGGER.error("Failed to create neutral effect: {}", name, e);
            throw new RuntimeException("Failed to register neutral effect", e);
        }
    }

    /**
     * Initialize and register all status effects. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI status effects");
        // Status effect registration happens when register() is called
    }
}
