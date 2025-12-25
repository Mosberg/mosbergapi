package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom fluids in MosbergAPI. Register your fluids here using the provided
 * helper methods.
 *
 * @example
 *
 *          <pre>{@code
 * public static final FlowableFluid CUSTOM_FLUID_STILL = register("custom_fluid_still",
 *     new CustomFluid.Still());
 * public static final FlowableFluid CUSTOM_FLUID_FLOWING = register("custom_fluid_flowing",
 *     new CustomFluid.Flowing());
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergFluids {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergFluids.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers a fluid with a simple name.
     *
     * @param name The fluid name (will be prefixed with mod ID)
     * @param fluid The Fluid to register
     * @param <T> The fluid class type
     * @return The registered Fluid
     *
     * @example
     *
     *          <pre>{@code
     * FlowableFluid stillFluid = MosbergFluids.register("lava_still", new MyLava.Still());
     * FlowableFluid flowingFluid = MosbergFluids.register("lava_flowing", new MyLava.Flowing());
     * }</pre>
     */
    public static <T extends Fluid> T register(String name, T fluid) {
        Identifier id = Identifier.of(MOD_ID, name);
        T registeredFluid = Registry.register(Registries.FLUID, id, fluid);
        LOGGER.debug("Registered fluid: {}", name);
        return registeredFluid;
    }

    /**
     * Registers a pair of still and flowing fluids with automatic naming.
     *
     * @param baseName The base name for the fluids (e.g., "custom_liquid")
     * @param stillFluid The still fluid variant
     * @param flowingFluid The flowing fluid variant
     * @return A FluidPair containing both registered fluids
     *
     * @example
     *
     *          <pre>{@code
     * FluidPair pair = MosbergFluids.registerPair("acid",
     *     new AcidFluid.Still(),
     *     new AcidFluid.Flowing());
     * }</pre>
     */
    public static <T extends FlowableFluid> FluidPair<T> registerPair(String baseName, T stillFluid,
            T flowingFluid) {
        T still = register(baseName + "_still", stillFluid);
        T flowing = register(baseName + "_flowing", flowingFluid);
        LOGGER.debug("Registered fluid pair: {} (still & flowing)", baseName);
        return new FluidPair<>(still, flowing);
    }

    /**
     * Initialize and register all fluids. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI fluids");
        // Fluid registration happens when register() is called
    }

    /**
     * Record to hold a pair of still and flowing fluid variants.
     *
     * @param <T> The fluid type
     */
    public record FluidPair<T extends FlowableFluid>(T still, T flowing) {
        /**
         * Gets the still variant of this fluid.
         *
         * @return The still fluid
         */
        public T getStill() {
            return still;
        }

        /**
         * Gets the flowing variant of this fluid.
         *
         * @return The flowing fluid
         */
        public T getFlowing() {
            return flowing;
        }
    }
}
