package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom item groups in MosbergAPI. Item groups appear as tabs in the
 * creative inventory.
 */
public class MosbergItemGroups {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergItemGroups.class);

    // Example item group (uncomment and customize as needed):
    // public static final RegistryKey<ItemGroup> MOSBERG_GROUP = register("mosberg_group");
    //
    // public static final ItemGroup MOSBERG_GROUP_INSTANCE = ItemGroup.create(
    // ItemGroup.Row.TOP,
    // 0
    // )
    // .displayName(Text.translatable("itemGroup.mosbergapi.mosberg_group"))
    // .icon(() -> new ItemStack(Items.DIAMOND))
    // .entries((context, entries) -> {
    // // Add items to the creative tab
    // entries.add(Items.DIAMOND);
    // })
    // .build();

    /**
     * Creates a RegistryKey for an item group.
     *
     * @param name The item group name
     * @return The RegistryKey for the item group
     */
    public static RegistryKey<ItemGroup> of(String name) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("mosbergapi", name));
    }

    /**
     * Registers an item group with a simple name.
     *
     * @param name The item group name (will be prefixed with mod ID)
     * @param itemGroup The ItemGroup to register
     * @return The registered ItemGroup
     */
    public static ItemGroup register(String name, ItemGroup itemGroup) {
        LOGGER.debug("Registering item group: {}", name);
        return MosbergRegistries.registerItemGroup(name, itemGroup);
    }

    /**
     * Creates and registers a simple item group.
     *
     * @param name The item group name
     * @param displayName The display name text
     * @param icon The icon item stack supplier
     * @return The registered ItemGroup
     */
    public static ItemGroup registerSimple(String name, Text displayName, ItemStack icon) {
        ItemGroup group = ItemGroup.create(ItemGroup.Row.TOP, 0).displayName(displayName)
                .icon(() -> icon).build();
        return register(name, group);
    }

    /**
     * Initialize and register all item groups. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI item groups");
        // Item group registration happens when the class is loaded
    }
}
