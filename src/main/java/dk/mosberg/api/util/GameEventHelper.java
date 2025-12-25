package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * Helper class for working with game events in Minecraft 1.21.10.
 *
 * <p>
 * Game events are used to trigger sculk sensors and other vibration-sensitive blocks. This helper
 * provides convenient methods for emitting game events and working with vibrations.
 *
 * <h2>Game Event System</h2>
 * <p>
 * Game events propagate through the world and can be detected by:
 * <ul>
 * <li>Sculk sensors - detect vibrations within range</li>
 * <li>Sculk shriekers - triggered by certain events</li>
 * <li>Wardens - detect player movements and actions</li>
 * <li>Custom listeners - your own event handlers</li>
 * </ul>
 *
 * <h2>Event Ranges</h2>
 * <p>
 * Each game event has a notification radius that determines how far it can be detected:
 * <ul>
 * <li>Small events (footsteps): 1-8 blocks</li>
 * <li>Medium events (block interactions): 8-16 blocks</li>
 * <li>Large events (explosions): 16-32 blocks</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Emit a custom game event
 * GameEventHelper.emitEvent(world, ModGameEvents.SPELL_CAST, pos, player);
 *
 * // Emit at entity position
 * GameEventHelper.emitEvent(world, GameEvent.TELEPORT, entity);
 *
 * // Check if event exists
 * if (GameEventHelper.hasEvent("mosbergapi:custom_event")) {
 *     // Event is registered
 *          }
 *
 *          // Get event by ID
 *          GameEvent event = GameEventHelper.getEvent("minecraft:block_place");
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see GameEvent
 * @see net.minecraft.world.event.listener.GameEventListener
 */
public class GameEventHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameEventHelper.class);

    private GameEventHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Emits a game event at a specific position.
     *
     * @param world The world to emit the event in
     * @param event The game event to emit
     * @param pos The position to emit from
     * @param entity The entity that caused the event (can be null)
     * @throws NullPointerException if world, event, or pos is null
     *
     * @example
     *
     *          <pre>{@code
     * GameEventHelper.emitEvent(world, GameEvent.BLOCK_PLACE, blockPos, player);
     * }</pre>
     */
    public static void emitEvent(@NotNull World world, @NotNull GameEvent event,
            @NotNull BlockPos pos, @Nullable Entity entity) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (event == null)
            throw new NullPointerException("GameEvent cannot be null");
        if (pos == null)
            throw new NullPointerException("Position cannot be null");

        world.emitGameEvent(Registries.GAME_EVENT.getEntry(event), pos,
                GameEvent.Emitter.of(entity));
        LOGGER.debug("Emitted game event {} at {} (entity: {})", Registries.GAME_EVENT.getId(event),
                pos, entity);
    }

    /**
     * Emits a game event at a specific vector position.
     *
     * @param world The world to emit the event in
     * @param event The game event to emit
     * @param pos The vector position to emit from
     * @param entity The entity that caused the event (can be null)
     *
     * @example
     *
     *          <pre>{@code
     * Vec3d pos = entity.getPos();
     * GameEventHelper.emitEvent(world, GameEvent.TELEPORT, pos, entity);
     * }</pre>
     */
    public static void emitEvent(@NotNull World world, @NotNull GameEvent event, @NotNull Vec3d pos,
            @Nullable Entity entity) {
        emitEvent(world, event, BlockPos.ofFloored(pos), entity);
    }

    /**
     * Emits a game event at an entity's position.
     *
     * @param world The world to emit the event in
     * @param event The game event to emit
     * @param entity The entity to emit from
     *
     * @example
     *
     *          <pre>{@code
     * GameEventHelper.emitEvent(world, GameEvent.ENTITY_DIE, deadEntity);
     * }</pre>
     */
    public static void emitEvent(@NotNull World world, @NotNull GameEvent event,
            @NotNull Entity entity) {
        emitEvent(world, event, entity.getBlockPos(), entity);
    }

    /**
     * Emits a game event with a registry entry.
     *
     * @param world The world to emit the event in
     * @param eventEntry The registry entry for the game event
     * @param pos The position to emit from
     * @param entity The entity that caused the event (can be null)
     *
     * @example
     *
     *          <pre>{@code
     * RegistryEntry<GameEvent> entry = world.getRegistryManager()
     *     .getOrThrow(RegistryKeys.GAME_EVENT)
     *     .getEntry(ModGameEvents.SPELL_CAST);
     * GameEventHelper.emitEvent(world, entry, pos, player);
     * }</pre>
     */
    public static void emitEvent(@NotNull World world, @NotNull RegistryEntry<GameEvent> eventEntry,
            @NotNull BlockPos pos, @Nullable Entity entity) {
        if (eventEntry.value() != null) {
            emitEvent(world, eventEntry.value(), pos, entity);
        }
    }

    /**
     * Gets a game event by its identifier.
     *
     * @param id The identifier of the game event
     * @return The game event, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * GameEvent event = GameEventHelper.getEvent("minecraft:block_place");
     * if (event != null) {
     *     // Use the event
     *          }
     * }</pre>
     */
    @Nullable
    public static GameEvent getEvent(@NotNull String id) {
        return getEvent(Identifier.tryParse(id));
    }

    /**
     * Gets a game event by its identifier.
     *
     * @param id The identifier of the game event
     * @return The game event, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * GameEvent event = GameEventHelper.getEvent(Identifier.of("mosbergapi", "spell_cast"));
     * }</pre>
     */
    @Nullable
    public static GameEvent getEvent(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.GAME_EVENT.get(id);
    }

    /**
     * Checks if a game event exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the game event is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (GameEventHelper.hasEvent("mosbergapi:custom_event")) {
     *     LOGGER.info("Custom event is registered!");
     *          }
     * }</pre>
     */
    public static boolean hasEvent(@NotNull String id) {
        return hasEvent(Identifier.tryParse(id));
    }

    /**
     * Checks if a game event exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the game event is registered
     */
    public static boolean hasEvent(@Nullable Identifier id) {
        return id != null && Registries.GAME_EVENT.containsId(id);
    }

    /**
     * Gets the notification radius of a game event.
     *
     * @param event The game event
     * @return The notification radius in blocks
     *
     * @example
     *
     *          <pre>{@code
     * int radius = GameEventHelper.getRange(GameEvent.EXPLODE);
     *          LOGGER.info("Explosion can be heard {} blocks away", radius);
     * }</pre>
     */
    public static int getRange(@NotNull GameEvent event) {
        if (event == null)
            throw new NullPointerException("GameEvent cannot be null");
        return event.notificationRadius();
    }

    /**
     * Gets the identifier of a game event.
     *
     * @param event The game event
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = GameEventHelper.getId(GameEvent.BLOCK_PLACE);
     * // Returns: minecraft:block_place
     * }</pre>
     */
    @Nullable
    public static Identifier getId(@NotNull GameEvent event) {
        if (event == null)
            throw new NullPointerException("GameEvent cannot be null");
        return Registries.GAME_EVENT.getId(event);
    }

    /**
     * Checks if a position is within range of a game event source.
     *
     * @param event The game event
     * @param source The source position
     * @param listener The listener position
     * @return true if the listener is within range
     *
     * @example
     *
     *          <pre>{@code
     * boolean canHear = GameEventHelper.isInRange(
     *     GameEvent.BLOCK_PLACE,
     *     blockPos,
     *     sensorPos
     * );
     * }</pre>
     */
    public static boolean isInRange(@NotNull GameEvent event, @NotNull BlockPos source,
            @NotNull BlockPos listener) {
        if (event == null)
            throw new NullPointerException("GameEvent cannot be null");
        if (source == null)
            throw new NullPointerException("Source position cannot be null");
        if (listener == null)
            throw new NullPointerException("Listener position cannot be null");

        double distance = Math.sqrt(source.getSquaredDistance(listener));
        return distance <= event.notificationRadius();
    }

    /**
     * Emits a vibration event at a position.
     *
     * <p>
     * Vibrations are a special type of game event that propagate through blocks and can be detected
     * by sculk sensors. This is a convenience method for common vibration patterns.
     *
     * @param world The world to emit in (must be ServerWorld)
     * @param event The game event causing the vibration
     * @param pos The source position
     * @param entity The entity that caused the vibration
     *
     * @example
     *
     *          <pre>{@code
     * GameEventHelper.emitVibration(
     *     (ServerWorld) world,
     *     GameEvent.BLOCK_PLACE,
     *     blockPos,
     *     player
     * );
     * }</pre>
     */
    public static void emitVibration(@NotNull ServerWorld world, @NotNull GameEvent event,
            @NotNull BlockPos pos, @Nullable Entity entity) {
        if (world == null)
            throw new NullPointerException("ServerWorld cannot be null");
        emitEvent(world, event, pos, entity);
    }

    /**
     * Gets all registered game events.
     *
     * @return An iterable of all game events
     *
     * @example
     *
     *          <pre>{@code
     * for (GameEvent event : GameEventHelper.getAllEvents()) {
     *          LOGGER.info("Event: {} (range: {})", GameEventHelper.getId(event), event.range());
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<GameEvent> getAllEvents() {
        return Registries.GAME_EVENT;
    }

    /**
     * Gets the number of registered game events.
     *
     * @return The count of game events
     *
     * @example
     *
     *          <pre>{@code
     * int count = GameEventHelper.getEventCount();
     *          LOGGER.info("Total game events registered: {}", count);
     * }</pre>
     */
    public static int getEventCount() {
        return Registries.GAME_EVENT.size();
    }
}
