package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

/**
 * Registry class for screen handler TYPES (server-side). Registers the screen handler types that
 * can be opened on both client and server.
 *
 * <p>
 * For client-side screen registration, see {@link dk.mosberg.api.client.registry.MosbergScreens}.
 *
 * @example
 *
 *          <pre>{@code
 * // In main source (server-side)
 * public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK_TYPE =
 *     MosbergScreenHandlerTypes.register("backpack", BackpackScreenHandler::new);
 *
 * // In client source (client-side)
 * MosbergScreens.register(MosbergScreenHandlerTypes.BACKPACK_TYPE, BackpackScreen::new);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergScreenHandlerTypes {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergScreenHandlerTypes.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers a simple screen handler type without extra data. This registers the TYPE that both
     * client and server understand.
     *
     * @param name The screen handler name (will be prefixed with mod ID)
     * @param factory The screen handler factory
     * @param <T> The screen handler class type
     * @return The registered ScreenHandlerType
     *
     * @example
     *
     *          <pre>{@code
     * ScreenHandlerType<ChestScreenHandler> CHEST = MosbergScreenHandlerTypes.register(
     *     "custom_chest",
     *     ChestScreenHandler::new);
     * }</pre>
     */
    public static <T extends ScreenHandler> ScreenHandlerType<T> register(String name,
            ScreenHandlerType.Factory<T> factory) {
        Identifier id = Identifier.of(MOD_ID, name);
        ScreenHandlerType<T> type = new ScreenHandlerType<>(factory, null);
        ScreenHandlerType<T> registered = Registry.register(Registries.SCREEN_HANDLER, id, type);
        LOGGER.debug("Registered screen handler type: {}", name);
        return registered;
    }

    /**
     * Registers an extended screen handler type with custom data synchronization.
     *
     * @param name The screen handler name
     * @param factory The extended screen handler factory
     * @param codec The packet codec for serializing/deserializing data
     * @param <T> The screen handler class type
     * @param <D> The data class type
     * @return The registered ExtendedScreenHandlerType
     *
     * @example
     *
     *          <pre>{@code
     * ExtendedScreenHandlerType<BackpackScreenHandler, BackpackData> BACKPACK =
     *     MosbergScreenHandlerTypes.registerExtended("backpack",
     *         BackpackScreenHandler::new,
     *         BackpackData.PACKET_CODEC);
     * }</pre>
     */
    public static <T extends ScreenHandler, D> ExtendedScreenHandlerType<T, D> registerExtended(
            String name, ExtendedScreenHandlerType.ExtendedFactory<T, D> factory,
            PacketCodec<? super RegistryByteBuf, D> codec) {
        Identifier id = Identifier.of(MOD_ID, name);
        ExtendedScreenHandlerType<T, D> type = new ExtendedScreenHandlerType<>(factory, codec);
        ExtendedScreenHandlerType<T, D> registered =
                Registry.register(Registries.SCREEN_HANDLER, id, type);
        LOGGER.debug("Registered extended screen handler type: {}", name);
        return registered;
    }

    /**
     * Initialize and register all screen handler types. Call this method from your mod initializer
     * (server-side).
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI screen handler types");
        // Screen handler type registration happens when register() is called
    }
}
