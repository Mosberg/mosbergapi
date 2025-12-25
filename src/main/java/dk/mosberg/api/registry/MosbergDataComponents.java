package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry class for custom data components in MosbergAPI.
 *
 * <p>
 * Data components replace NBT in Minecraft 1.21+ for storing item/block data. They provide
 * type-safe, codec-based serialization for custom data.
 *
 * @example
 *
 *          <pre>{@code
 * // Integer component
 * public static final ComponentType<Integer> CHARGE = register(
 *     "charge",
 *     builder -> builder.codec(Codec.INT)
 *         .packetCodec(PacketCodecs.VAR_INT)
 * );
 *
 * // Custom object component
 * public record CustomData(String name, int level) {
 *     public static final Codec<CustomData> CODEC = RecordCodecBuilder.create(instance ->
 *         instance.group(
 *             Codec.STRING.fieldOf("name").forGetter(CustomData::name),
 *             Codec.INT.fieldOf("level").forGetter(CustomData::level)
 *         ).apply(instance, CustomData::new)
 *     );
 *
 *     public static final PacketCodec<RegistryByteBuf, CustomData> PACKET_CODEC =
 *         PacketCodec.tuple(
 *             PacketCodecs.STRING, CustomData::name,
 *             PacketCodecs.VAR_INT, CustomData::level,
 *             CustomData::new
 *         );
 *          }
 *
 *          public static final ComponentType<CustomData> CUSTOM = register("custom_data",
 *          builder -> builder.codec(CustomData.CODEC).packetCodec(CustomData.PACKET_CODEC));
 *
 *          // Usage in items
 *          ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
 *          stack.set(ModComponents.CHARGE, 100);
 *          int charge = stack.getOrDefault(ModComponents.CHARGE, 0);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergDataComponents {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergDataComponents.class);

    /**
     * Registers a data component type.
     *
     * @param name The component name (will be prefixed with mod ID)
     * @param builderConsumer Consumer that configures the component builder
     * @param <T> The component data type
     * @return The registered ComponentType
     *
     * @example
     *
     *          <pre>{@code
     * public static final ComponentType<Integer> MANA = register(
     *     "mana",
     *     builder -> builder.codec(Codec.INT)
     *         .packetCodec(PacketCodecs.VAR_INT)
     * );
     * }</pre>
     */
    public static <T> ComponentType<T> register(String name,
            java.util.function.UnaryOperator<ComponentType.Builder<T>> builderConsumer) {
        Identifier id = Identifier.of("mosbergapi", name);
        LOGGER.debug("Registering data component: {}", id);

        ComponentType<T> componentType = builderConsumer.apply(ComponentType.builder()).build();

        return Registry.register(Registries.DATA_COMPONENT_TYPE, id, componentType);
    }

    /**
     * Registers a simple codec-only component (no packet codec). Use for server-only data that
     * doesn't need to sync to clients.
     *
     * @param name The component name
     * @param codec The codec for serialization
     * @param <T> The component data type
     * @return The registered ComponentType
     *
     * @example
     *
     *          <pre>{@code
     * public static final ComponentType<String> SERVER_DATA = registerSimple(
     *     "server_data",
     *     Codec.STRING
     * );
     * }</pre>
     */
    public static <T> ComponentType<T> registerSimple(String name, Codec<T> codec) {
        return register(name, builder -> builder.codec(codec));
    }

    /**
     * Registers a component with both codec and packet codec. Use for data that needs to sync
     * between server and client.
     *
     * <p>
     * <b>Important:</b> The packet codec must be typed as
     * {@code PacketCodec<? super RegistryByteBuf, T>} or {@code PacketCodec<RegistryByteBuf, T>}.
     *
     * @param name The component name
     * @param codec The codec for disk serialization
     * @param packetCodec The packet codec for network serialization
     * @param <T> The component data type
     * @return The registered ComponentType
     *
     * @example
     *
     *          <pre>{@code
     * public static final ComponentType<Integer> ENERGY = registerNetworked(
     *     "energy",
     *     Codec.INT,
     *     PacketCodecs.VAR_INT
     * );
     * }</pre>
     */
    public static <T> ComponentType<T> registerNetworked(String name, Codec<T> codec,
            PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return register(name, builder -> builder.codec(codec).packetCodec(packetCodec));
    }

    /**
     * Registers a cached component (uses reference equality for change detection). Use for
     * immutable data that doesn't change often.
     *
     * <p>
     * Cached components are more efficient when the value rarely changes, as Minecraft can skip
     * unnecessary checks.
     *
     * @param name The component name
     * @param codec The codec for serialization
     * @param <T> The component data type
     * @return The registered ComponentType
     *
     * @example
     *
     *          <pre>{@code
     * public static final ComponentType<UUID> OWNER_UUID = registerCached(
     *     "owner_uuid",
     *     Uuids.CODEC
     * );
     * }</pre>
     */
    public static <T> ComponentType<T> registerCached(String name, Codec<T> codec) {
        return register(name, builder -> builder.codec(codec).cache());
    }

    /**
     * Registers a networked cached component. Combines the benefits of caching and network
     * synchronization.
     *
     * @param name The component name
     * @param codec The codec for disk serialization
     * @param packetCodec The packet codec for network serialization
     * @param <T> The component data type
     * @return The registered ComponentType
     *
     * @example
     *
     *          <pre>{@code
     * public static final ComponentType<Boolean> LOCKED = registerNetworkedCached(
     *     "locked",
     *     Codec.BOOL,
     *     PacketCodecs.BOOL
     * );
     * }</pre>
     */
    public static <T> ComponentType<T> registerNetworkedCached(String name, Codec<T> codec,
            PacketCodec<? super RegistryByteBuf, T> packetCodec) {
        return register(name, builder -> builder.codec(codec).packetCodec(packetCodec).cache());
    }

    /**
     * Initialize and register all data components. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI data components");
        // Component registration happens when register() is called
    }
}
