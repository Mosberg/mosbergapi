package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom entity attributes in MosbergAPI. Register your attributes here
 * using the provided helper methods.
 *
 * @example
 *
 *          <pre>{@code
 * public static final RegistryEntry<EntityAttribute> MANA = register("mana",
 *     new ClampedEntityAttribute("attribute.name.mosbergapi.mana", 100.0, 0.0, 1000.0)
 *         .setTracked(true));
 *
 * public static final RegistryEntry<EntityAttribute> MAGIC_RESISTANCE = registerClamped(
 *     "magic_resistance", 0.0, 0.0, 1.0);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergAttributes {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergAttributes.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers an entity attribute with a simple name.
     *
     * @param name The attribute name (will be prefixed with mod ID)
     * @param attribute The EntityAttribute to register
     * @return The registered EntityAttribute entry
     *
     * @example
     *
     *          <pre>{@code
     * RegistryEntry<EntityAttribute> stamina = MosbergAttributes.register("stamina",
     *     new ClampedEntityAttribute("attribute.name.mosbergapi.stamina", 20.0, 0.0, 100.0));
     * }</pre>
     */
    public static RegistryEntry<EntityAttribute> register(String name, EntityAttribute attribute) {
        Identifier id = Identifier.of(MOD_ID, name);
        EntityAttribute registered = Registry.register(Registries.ATTRIBUTE, id, attribute);
        LOGGER.debug("Registered entity attribute: {}", name);
        return Registries.ATTRIBUTE.getEntry(registered);
    }

    /**
     * Registers a clamped entity attribute with default values.
     *
     * @param name The attribute name
     * @param defaultValue The default value
     * @param min The minimum value
     * @param max The maximum value
     * @return The registered EntityAttribute entry
     *
     * @example
     *
     *          <pre>{@code
     * RegistryEntry<EntityAttribute> luck = MosbergAttributes.registerClamped(
     *     "luck", 0.0, -10.0, 10.0);
     * }</pre>
     */
    public static RegistryEntry<EntityAttribute> registerClamped(String name, double defaultValue,
            double min, double max) {
        String translationKey = "attribute.name." + MOD_ID + "." + name;
        EntityAttribute attribute =
                new ClampedEntityAttribute(translationKey, defaultValue, min, max).setTracked(true);
        return register(name, attribute);
    }

    /**
     * Registers a tracked clamped entity attribute.
     *
     * @param name The attribute name
     * @param defaultValue The default value
     * @param min The minimum value
     * @param max The maximum value
     * @param tracked Whether to track this attribute
     * @return The registered EntityAttribute entry
     *
     * @example
     *
     *          <pre>{@code
     * RegistryEntry<EntityAttribute> rage = MosbergAttributes.registerClamped(
     *     "rage", 0.0, 0.0, 100.0, true);
     * }</pre>
     */
    public static RegistryEntry<EntityAttribute> registerClamped(String name, double defaultValue,
            double min, double max, boolean tracked) {
        String translationKey = "attribute.name." + MOD_ID + "." + name;
        EntityAttribute attribute =
                new ClampedEntityAttribute(translationKey, defaultValue, min, max)
                        .setTracked(tracked);
        return register(name, attribute);
    }

    /**
     * Initialize and register all entity attributes. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI entity attributes");
        // Attribute registration happens when register() is called
    }
}
