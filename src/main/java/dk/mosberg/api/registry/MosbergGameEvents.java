package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.event.GameEvent;

/**
 * Registry class for all custom game events (sculk sensor triggers) in MosbergAPI. Register your
 * game events here using the provided helper methods.
 *
 * @example
 *
 *          <pre>{@code
 * public static final GameEvent SPELL_CAST = register("spell_cast", 16);
 * public static final GameEvent PORTAL_ACTIVATED = register("portal_activated", 32);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergGameEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergGameEvents.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers a game event with a simple name and notification radius.
     *
     * @param name The game event name (will be prefixed with mod ID)
     * @param notificationRadius The radius in blocks that this event can be detected
     * @return The registered GameEvent
     *
     * @example
     *
     *          <pre>{@code
     * GameEvent teleport = MosbergGameEvents.register("teleport", 16);
     * }</pre>
     */
    public static GameEvent register(String name, int notificationRadius) {
        Identifier id = Identifier.of(MOD_ID, name);
        GameEvent event = new GameEvent(notificationRadius);
        GameEvent registered = Registry.register(Registries.GAME_EVENT, id, event);
        LOGGER.debug("Registered game event: {} (radius: {})", name, notificationRadius);
        return registered;
    }

    /**
     * Registers a game event with default notification radius of 16 blocks.
     *
     * @param name The game event name
     * @return The registered GameEvent
     *
     * @example
     *
     *          <pre>{@code
     * GameEvent customAction = MosbergGameEvents.register("custom_action");
     * }</pre>
     */
    public static GameEvent register(String name) {
        return register(name, 16);
    }

    /**
     * Initialize and register all game events. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI game events");
        // Game event registration happens when register() is called
    }
}
