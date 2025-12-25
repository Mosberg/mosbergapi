package dk.mosberg.api.client.registry;

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
 * Registry class for all custom screen handlers (container GUIs) in MosbergAPI. Register your
 * screen handlers here using the provided helper methods.
 *
 * @example
 *
 *          <pre>{@code
 * public static final ScreenHandlerType<CustomScreenHandler> CUSTOM_SCREEN = register(
 *     "custom_screen",
 *     CustomScreenHandler::new);
 *
 * public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK = registerExtended(
 *     "backpack",
 *     BackpackScreenHandler::new,
 *     BackpackData.PACKET_CODEC);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergScreenHandlers {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergScreenHandlers.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers a simple screen handler without extra data.
     *
     * @param name The screen handler name (will be prefixed with mod ID)
     * @param factory The screen handler factory
     * @param <T> The screen handler class type
     * @return The registered ScreenHandlerType
     *
     * @example
     *
     *          <pre>{@code
     * ScreenHandlerType<ChestScreenHandler> chest = MosbergScreenHandlers.register(
     *     "custom_chest",
     *     ChestScreenHandler::new);
     * }</pre>
     */
    public static <T extends ScreenHandler> ScreenHandlerType<T> register(String name,
            ScreenHandlerType.Factory<T> factory) {
        Identifier id = Identifier.of(MOD_ID, name);
        ScreenHandlerType<T> type = new ScreenHandlerType<>(factory, null);
        ScreenHandlerType<T> registered = Registry.register(Registries.SCREEN_HANDLER, id, type);
        LOGGER.debug("Registered screen handler: {}", name);
        return registered;
    }

    /**
     * Registers an extended screen handler with custom data synchronization.
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
     * ExtendedScreenHandlerType<BackpackScreenHandler, BackpackData> backpack =
     *     MosbergScreenHandlers.registerExtended("backpack",
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
        LOGGER.debug("Registered extended screen handler: {}", name);
        return registered;
    }

    /**
     * Initialize and register all screen handlers. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI screen handlers");
        // Screen handler registration happens when register() is called
    }
}
