package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;

/**
 * Registry class for all custom particle types in MosbergAPI. Register your particles here using
 * the provided helper methods.
 */
public class MosbergParticles {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergParticles.class);

    // Example particle registration (uncomment and customize as needed):
    // public static final SimpleParticleType EXAMPLE_PARTICLE = registerSimple("example_particle",
    // false);
    // public static final SimpleParticleType EXAMPLE_PARTICLE_ALWAYS_SHOW =
    // registerSimple("example_particle_always", true);

    /**
     * Registers a simple particle type using Fabric API.
     *
     * @param name The particle name (will be prefixed with mod ID)
     * @param alwaysShow If true, the particle will always show regardless of particle settings
     * @return The registered SimpleParticleType
     */
    public static SimpleParticleType registerSimple(String name, boolean alwaysShow) {
        LOGGER.debug("Registering simple particle: {} (alwaysShow: {})", name, alwaysShow);

        // Use Fabric API to create the particle type
        SimpleParticleType particle = FabricParticleTypes.simple(alwaysShow);

        return MosbergRegistries.registerParticleType(name, particle);
    }

    /**
     * Registers a simple particle type that respects particle settings. This is equivalent to
     * registerSimple(name, false).
     *
     * @param name The particle name (will be prefixed with mod ID)
     * @return The registered SimpleParticleType
     */
    public static SimpleParticleType registerSimple(String name) {
        return registerSimple(name, false);
    }

    /**
     * Registers a particle type with a custom ParticleType.
     *
     * @param name The particle name (will be prefixed with mod ID)
     * @param particleType The ParticleType to register
     * @param <T> The particle effect type
     * @return The registered ParticleType
     */
    public static <T extends ParticleEffect> ParticleType<T> register(String name,
            ParticleType<T> particleType) {
        LOGGER.debug("Registering particle type: {}", name);
        return MosbergRegistries.registerParticleType(name, particleType);
    }

    /**
     * Initialize and register all particles. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI particles");
        // Particle registration happens when the class is loaded
    }
}
