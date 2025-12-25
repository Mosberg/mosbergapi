package dk.mosberg.api.registry;

import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

/**
 * Registry class for all custom villager professions and points of interest in MosbergAPI.
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergVillagers {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergVillagers.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers a point of interest type for a workstation block.
     *
     * @param name The POI name (will be prefixed with mod ID)
     * @param workstation The workstation block
     * @return The RegistryKey for the POI type
     */
    public static RegistryKey<PointOfInterestType> registerPOI(String name, Block workstation) {
        Identifier id = Identifier.of(MOD_ID, name);
        RegistryKey<PointOfInterestType> key =
                RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, id);

        PointOfInterestHelper.register(id, 1, 1, workstation);

        LOGGER.debug("Registered POI: {} for block: {}", name, Registries.BLOCK.getId(workstation));
        return key;
    }

    /**
     * Registers a point of interest type with custom ticket count and search distance.
     */
    public static RegistryKey<PointOfInterestType> registerPOI(String name, int ticketCount,
            int searchDistance, Block workstation) {
        Identifier id = Identifier.of(MOD_ID, name);
        RegistryKey<PointOfInterestType> key =
                RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, id);

        PointOfInterestHelper.register(id, ticketCount, searchDistance, workstation);

        LOGGER.debug("Registered POI: {} (tickets: {}, distance: {})", name, ticketCount,
                searchDistance);
        return key;
    }

    /**
     * Registers a villager profession with a POI and work sound.
     *
     * <p>
     * <strong>Important:</strong> VillagerProfession constructor takes Text as first parameter in
     * 1.21.10!
     * </p>
     *
     * @param name The profession name (will be prefixed with mod ID)
     * @param poiKey The point of interest registry key
     * @param workSound The sound played when the villager works (nullable)
     * @return The registered VillagerProfession
     */
    public static VillagerProfession registerProfession(String name,
            RegistryKey<PointOfInterestType> poiKey, @Nullable SoundEvent workSound) {
        Identifier id = Identifier.of(MOD_ID, name);

        // Create Text from the identifier
        Text professionText = Text.translatable("entity.minecraft.villager." + name);

        // Create predicates that match the POI key
        Predicate<RegistryEntry<PointOfInterestType>> heldWorkstation =
                entry -> entry.matchesKey(poiKey);
        Predicate<RegistryEntry<PointOfInterestType>> acquirableWorkstation =
                entry -> entry.matchesKey(poiKey);

        // VillagerProfession constructor parameters (in order):
        // 1. Text id (not String!)
        // 2. Predicate<RegistryEntry<PointOfInterestType>> heldWorkstation
        // 3. Predicate<RegistryEntry<PointOfInterestType>> acquirableWorkstation
        // 4. ImmutableSet<Item> gatherableItems
        // 5. ImmutableSet<Block> secondaryJobSites
        // 6. @Nullable SoundEvent workSound
        VillagerProfession profession = new VillagerProfession(professionText, // Text (NOT String!)
                heldWorkstation, acquirableWorkstation, ImmutableSet.of(), ImmutableSet.of(),
                workSound);

        VillagerProfession registered =
                Registry.register(Registries.VILLAGER_PROFESSION, id, profession);
        LOGGER.debug("Registered villager profession: {}", name);
        return registered;
    }

    /**
     * Registers a villager profession with gatherable items and secondary job sites.
     */
    public static VillagerProfession registerProfession(String name,
            RegistryKey<PointOfInterestType> poiKey, ImmutableSet<Item> gatherableItems,
            ImmutableSet<Block> secondaryJobSites, @Nullable SoundEvent workSound) {
        Identifier id = Identifier.of(MOD_ID, name);

        Text professionText = Text.translatable("entity.minecraft.villager." + name);

        Predicate<RegistryEntry<PointOfInterestType>> heldWorkstation =
                entry -> entry.matchesKey(poiKey);
        Predicate<RegistryEntry<PointOfInterestType>> acquirableWorkstation =
                entry -> entry.matchesKey(poiKey);

        VillagerProfession profession = new VillagerProfession(professionText, heldWorkstation,
                acquirableWorkstation, gatherableItems, secondaryJobSites, workSound);

        VillagerProfession registered =
                Registry.register(Registries.VILLAGER_PROFESSION, id, profession);
        LOGGER.debug(
                "Registered villager profession: {} with {} gatherable items and {} secondary job sites",
                name, gatherableItems.size(), secondaryJobSites.size());
        return registered;
    }

    /**
     * Registers a villager profession without a work sound.
     */
    public static VillagerProfession registerProfession(String name,
            RegistryKey<PointOfInterestType> poiKey) {
        return registerProfession(name, poiKey, null);
    }

    /**
     * Registers both a POI and profession in one call.
     */
    public static ProfessionData registerProfessionWithPOI(String name, Block workstation,
            @Nullable SoundEvent workSound) {
        RegistryKey<PointOfInterestType> poiKey = registerPOI(name + "_poi", workstation);
        VillagerProfession profession = registerProfession(name, poiKey, workSound);

        LOGGER.debug("Registered profession with POI: {}", name);
        return new ProfessionData(poiKey, profession);
    }

    /**
     * Registers both a POI and profession without a work sound.
     */
    public static ProfessionData registerProfessionWithPOI(String name, Block workstation) {
        return registerProfessionWithPOI(name, workstation, null);
    }

    /**
     * Initialize all villager professions. Call this from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI villager professions");
    }

    /**
     * Record to hold a villager profession and its associated POI.
     */
    public record ProfessionData(RegistryKey<PointOfInterestType> poiKey,
            VillagerProfession profession) {
    }
}
