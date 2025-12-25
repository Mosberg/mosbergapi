package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Helper class for working with sounds in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for playing sounds, managing sound events, and
 * controlling audio playback in both client and server contexts.
 *
 * <h2>Sound Categories</h2>
 * <p>
 * Minecraft organizes sounds into categories that players can control independently:
 * <ul>
 * <li>{@link SoundCategory#MASTER} - Master volume control</li>
 * <li>{@link SoundCategory#MUSIC} - Background music</li>
 * <li>{@link SoundCategory#RECORDS} - Jukebox music</li>
 * <li>{@link SoundCategory#WEATHER} - Weather sounds (rain, thunder)</li>
 * <li>{@link SoundCategory#BLOCKS} - Block sounds (breaking, placing)</li>
 * <li>{@link SoundCategory#HOSTILE} - Hostile mob sounds</li>
 * <li>{@link SoundCategory#NEUTRAL} - Neutral mob sounds</li>
 * <li>{@link SoundCategory#PLAYERS} - Player sounds</li>
 * <li>{@link SoundCategory#AMBIENT} - Ambient sounds (caves, etc.)</li>
 * <li>{@link SoundCategory#VOICE} - Voice chat</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Play sound at position
 * SoundHelper.playSound(world, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS);
 *
 * // Play sound to specific player
 * SoundHelper.playSound(player, SoundEvents.ENTITY_PLAYER_LEVELUP);
 *
 * // Play sound with custom volume and pitch
 * SoundHelper.playSound(world, entity.getPos(), ModSounds.SPELL_CAST,
 *     SoundCategory.PLAYERS, 1.0f, 1.5f);
 *
 * // Check if sound exists
 * if (SoundHelper.hasSound("mosbergapi:custom_sound")) {
 *     // Sound is registered
 *          }
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see SoundEvent
 * @see SoundCategory
 * @see SoundEvents
 */
public class SoundHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoundHelper.class);

    /** Default volume for sound playback (1.0 = 100%) */
    public static final float DEFAULT_VOLUME = 1.0f;

    /** Default pitch for sound playback (1.0 = normal speed) */
    public static final float DEFAULT_PITCH = 1.0f;

    private SoundHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Plays a sound at a specific position in the world.
     *
     * @param world The world to play the sound in
     * @param pos The position to play from
     * @param sound The sound event to play
     * @param category The sound category
     * @param volume The volume (0.0 to 1.0+)
     * @param pitch The pitch (0.5 to 2.0, default 1.0)
     * @throws NullPointerException if world, pos, sound, or category is null
     *
     * @example
     *
     *          <pre>{@code
     * SoundHelper.playSound(world, blockPos, SoundEvents.BLOCK_ANVIL_USE,
     *     SoundCategory.BLOCKS, 1.0f, 1.0f);
     * }</pre>
     */
    public static void playSound(@NotNull World world, @NotNull BlockPos pos,
            @NotNull SoundEvent sound, @NotNull SoundCategory category, float volume, float pitch) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (pos == null)
            throw new NullPointerException("Position cannot be null");
        if (sound == null)
            throw new NullPointerException("SoundEvent cannot be null");
        if (category == null)
            throw new NullPointerException("SoundCategory cannot be null");

        world.playSound(null, pos, sound, category, volume, pitch);
        LOGGER.debug("Played sound {} at {} (volume: {}, pitch: {})", getId(sound), pos, volume,
                pitch);
    }

    /**
     * Plays a sound at a specific position with default volume and pitch.
     *
     * @param world The world to play the sound in
     * @param pos The position to play from
     * @param sound The sound event to play
     * @param category The sound category
     *
     * @example
     *
     *          <pre>{@code
     * SoundHelper.playSound(world, blockPos, SoundEvents.BLOCK_NOTE_BLOCK_PLING,
     *     SoundCategory.BLOCKS);
     * }</pre>
     */
    public static void playSound(@NotNull World world, @NotNull BlockPos pos,
            @NotNull SoundEvent sound, @NotNull SoundCategory category) {
        playSound(world, pos, sound, category, DEFAULT_VOLUME, DEFAULT_PITCH);
    }

    /**
     * Plays a sound at a vector position in the world.
     *
     * @param world The world to play the sound in
     * @param pos The vector position to play from
     * @param sound The sound event to play
     * @param category The sound category
     * @param volume The volume (0.0 to 1.0+)
     * @param pitch The pitch (0.5 to 2.0)
     *
     * @example
     *
     *          <pre>{@code
     * Vec3d pos = entity.getPos();
     * SoundHelper.playSound(world, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
     *     SoundCategory.PLAYERS, 1.0f, 1.2f);
     * }</pre>
     */
    public static void playSound(@NotNull World world, @NotNull Vec3d pos,
            @NotNull SoundEvent sound, @NotNull SoundCategory category, float volume, float pitch) {
        playSound(world, BlockPos.ofFloored(pos), sound, category, volume, pitch);
    }

    /**
     * Plays a sound at an entity's position.
     *
     * @param entity The entity to play from
     * @param sound The sound event to play
     * @param category The sound category
     * @param volume The volume
     * @param pitch The pitch
     *
     * @example
     *
     *          <pre>{@code
     * SoundHelper.playSound(player, SoundEvents.ENTITY_PLAYER_LEVELUP,
     *     SoundCategory.PLAYERS, 1.0f, 1.0f);
     * }</pre>
     */
    public static void playSound(@NotNull Entity entity, @NotNull SoundEvent sound,
            @NotNull SoundCategory category, float volume, float pitch) {
        if (entity == null)
            throw new NullPointerException("Entity cannot be null");
        playSound(entity.getEntityWorld(), entity.getBlockPos(), sound, category, volume, pitch);
    }

    /**
     * Plays a sound at an entity's position with default volume and pitch.
     *
     * @param entity The entity to play from
     * @param sound The sound event to play
     * @param category The sound category
     *
     * @example
     *
     *          <pre>{@code
     * SoundHelper.playSound(entity, SoundEvents.ENTITY_GENERIC_EXPLODE,
     *     SoundCategory.HOSTILE);
     * }</pre>
     */
    public static void playSound(@NotNull Entity entity, @NotNull SoundEvent sound,
            @NotNull SoundCategory category) {
        playSound(entity, sound, category, DEFAULT_VOLUME, DEFAULT_PITCH);
    }

    /**
     * Plays a sound to a specific player only.
     *
     * <p>
     * The sound is sent to the player's client and only they can hear it.
     *
     * @param player The player to play the sound to
     * @param sound The sound event to play
     * @param category The sound category
     * @param volume The volume
     * @param pitch The pitch
     *
     * @example
     *
     *          <pre>{@code
     * SoundHelper.playSound(player, ModSounds.SPELL_COMPLETE,
     *     SoundCategory.PLAYERS, 1.0f, 1.0f);
     * }</pre>
     */
    public static void playSound(@NotNull ServerPlayerEntity player, @NotNull SoundEvent sound,
            @NotNull SoundCategory category, float volume, float pitch) {
        if (player == null)
            throw new NullPointerException("Player cannot be null");
        if (sound == null)
            throw new NullPointerException("SoundEvent cannot be null");
        if (category == null)
            throw new NullPointerException("SoundCategory cannot be null");

        player.playSoundToPlayer(sound, category, volume, pitch);
        LOGGER.debug("Played sound {} to player {} (volume: {}, pitch: {})", getId(sound),
                player.getName().getString(), volume, pitch);
    }

    /**
     * Plays a sound to a player with default volume and pitch.
     *
     * @param player The player to play the sound to
     * @param sound The sound event to play
     * @param category The sound category
     *
     * @example
     *
     *          <pre>{@code
     * SoundHelper.playSound(player, SoundEvents.ENTITY_PLAYER_LEVELUP,
     *     SoundCategory.PLAYERS);
     * }</pre>
     */
    public static void playSound(@NotNull ServerPlayerEntity player, @NotNull SoundEvent sound,
            @NotNull SoundCategory category) {
        playSound(player, sound, category, DEFAULT_VOLUME, DEFAULT_PITCH);
    }

    /**
     * Plays a sound to all players in a world.
     *
     * @param world The server world
     * @param sound The sound event to play
     * @param category The sound category
     * @param volume The volume
     * @param pitch The pitch
     *
     * @example
     *
     *          <pre>{@code
     * SoundHelper.playSoundToAll((ServerWorld) world, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
     *     SoundCategory.MASTER, 1.0f, 1.0f);
     * }</pre>
     */
    public static void playSoundToAll(@NotNull ServerWorld world, @NotNull SoundEvent sound,
            @NotNull SoundCategory category, float volume, float pitch) {
        if (world == null)
            throw new NullPointerException("ServerWorld cannot be null");

        for (ServerPlayerEntity player : world.getPlayers()) {
            playSound(player, sound, category, volume, pitch);
        }
    }

    /**
     * Gets a sound event by its identifier.
     *
     * @param id The identifier of the sound event
     * @return The sound event, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * SoundEvent sound = SoundHelper.getSound("minecraft:block_anvil_use");
     * if (sound != null) {
     *     // Use the sound
     *          }
     * }</pre>
     */
    @Nullable
    public static SoundEvent getSound(@NotNull String id) {
        return getSound(Identifier.tryParse(id));
    }

    /**
     * Gets a sound event by its identifier.
     *
     * @param id The identifier of the sound event
     * @return The sound event, or null if not found
     */
    @Nullable
    public static SoundEvent getSound(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.SOUND_EVENT.get(id);
    }

    /**
     * Checks if a sound event exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the sound event is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (SoundHelper.hasSound("mosbergapi:custom_sound")) {
     *     LOGGER.info("Custom sound is registered!");
     *          }
     * }</pre>
     */
    public static boolean hasSound(@NotNull String id) {
        return hasSound(Identifier.tryParse(id));
    }

    /**
     * Checks if a sound event exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the sound event is registered
     */
    public static boolean hasSound(@Nullable Identifier id) {
        return id != null && Registries.SOUND_EVENT.containsId(id);
    }

    /**
     * Gets the identifier of a sound event.
     *
     * @param sound The sound event
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = SoundHelper.getId(SoundEvents.BLOCK_ANVIL_USE);
     * // Returns: minecraft:block_anvil_use
     * }</pre>
     */
    @Nullable
    public static Identifier getId(@NotNull SoundEvent sound) {
        if (sound == null)
            throw new NullPointerException("SoundEvent cannot be null");
        return Registries.SOUND_EVENT.getId(sound);
    }

    /**
     * Gets the range of a sound event.
     *
     * @param sound The sound event
     * @return The range in blocks, or 16.0 if not variable
     *
     * @example
     *
     *          <pre>{@code
     * float range = SoundHelper.getRange(ModSounds.DISTANT_THUNDER);
     *          LOGGER.info("Sound can be heard {} blocks away", range);
     * }</pre>
     */
    public static float getRange(@NotNull SoundEvent sound) {
        if (sound == null)
            throw new NullPointerException("SoundEvent cannot be null");
        return sound.getDistanceToTravel(DEFAULT_VOLUME);
    }

    /**
     * Calculates random pitch variation.
     *
     * <p>
     * Useful for making repeated sounds less monotonous.
     *
     * @param basePitch The base pitch (typically 1.0)
     * @param variation The amount of variation (0.0 to 1.0)
     * @return A randomized pitch value
     *
     * @example
     *
     *          <pre>{@code
     * float pitch = SoundHelper.randomPitch(1.0f, 0.2f);
     * // Returns pitch between 0.8 and 1.2
     * SoundHelper.playSound(world, pos, sound, category, 1.0f, pitch);
     * }</pre>
     */
    public static float randomPitch(float basePitch, float variation) {
        return basePitch + ((float) Math.random() - 0.5f) * variation * 2;
    }

    /**
     * Gets all registered sound events.
     *
     * @return An iterable of all sound events
     *
     * @example
     *
     *          <pre>{@code
     * for (SoundEvent sound : SoundHelper.getAllSounds()) {
     *          LOGGER.info("Sound: {}", SoundHelper.getId(sound));
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<SoundEvent> getAllSounds() {
        return Registries.SOUND_EVENT;
    }

    /**
     * Gets the number of registered sound events.
     *
     * @return The count of sound events
     *
     * @example
     *
     *          <pre>{@code
     * int count = SoundHelper.getSoundCount();
     *          LOGGER.info("Total sounds registered: {}", count);
     * }</pre>
     */
    public static int getSoundCount() {
        return Registries.SOUND_EVENT.size();
    }
}
