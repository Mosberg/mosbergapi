package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom sound events in MosbergAPI. Register your sounds here using the
 * provided helper methods.
 */
public class MosbergSounds {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergSounds.class);

    // Example sound registration (uncomment and customize as needed):
    // public static final SoundEvent EXAMPLE_SOUND = register("example_sound");
    // public static final SoundEvent AMBIENT_SOUND = register("ambient.example");
    // public static final SoundEvent ITEM_USE = register("item.example.use");

    /**
     * Registers a sound event with a simple name. Creates a SoundEvent with a fixed range (the
     * default behavior).
     *
     * @param name The sound name (will be prefixed with mod ID)
     * @return The registered SoundEvent
     */
    public static SoundEvent register(String name) {
        Identifier id = Identifier.of("mosbergapi", name);
        LOGGER.debug("Registering sound event: {}", id);
        SoundEvent soundEvent = SoundEvent.of(id);
        return MosbergRegistries.registerSoundEvent(name, soundEvent);
    }

    /**
     * Registers a sound event with a variable range. Useful for sounds that need to be heard at
     * different distances.
     *
     * @param name The sound name (will be prefixed with mod ID)
     * @param range The maximum distance in blocks the sound can be heard
     * @return The registered SoundEvent
     */
    public static SoundEvent registerWithRange(String name, float range) {
        Identifier id = Identifier.of("mosbergapi", name);
        LOGGER.debug("Registering sound event with range: {} ({})", id, range);
        SoundEvent soundEvent = SoundEvent.of(id, range);
        return MosbergRegistries.registerSoundEvent(name, soundEvent);
    }

    /**
     * Registers a pre-created SoundEvent.
     *
     * @param name The sound name (will be prefixed with mod ID)
     * @param soundEvent The SoundEvent to register
     * @return The registered SoundEvent
     */
    public static SoundEvent register(String name, SoundEvent soundEvent) {
        LOGGER.debug("Registering custom sound event: {}", name);
        return MosbergRegistries.registerSoundEvent(name, soundEvent);
    }

    /**
     * Initialize and register all sounds. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI sounds");
        // Sound registration happens when the class is loaded
    }
}
