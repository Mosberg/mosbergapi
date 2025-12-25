package dk.mosberg.api.client.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

/**
 * Helper class for working with screen handlers (container GUIs) in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for managing GUI inventories, handling slot interactions,
 * and working with the screen handler system.
 *
 * <h2>Screen Handler System</h2>
 * <p>
 * Screen handlers manage the server-side logic for GUIs:
 * <ul>
 * <li><strong>Slots:</strong> Individual inventory positions (player + container)</li>
 * <li><strong>Sync:</strong> Automatic synchronization between client and server</li>
 * <li><strong>Click Handling:</strong> Mouse interactions with slots</li>
 * <li><strong>Transfer:</strong> Shift-click item movement logic</li>
 * </ul>
 *
 * <h2>Common Patterns</h2>
 * <ul>
 * <li><strong>Player Inventory:</strong> 9 hotbar + 27 main inventory slots</li>
 * <li><strong>Container Slots:</strong> Custom inventory slots (chest, furnace, etc.)</li>
 * <li><strong>Slot Indexing:</strong> Container slots first, then player inventory</li>
 * <li><strong>Quick Move:</strong> Shift-click transfer between inventories</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Get screen handler type
 * ScreenHandlerType<?> type = ScreenHandlerHelper.getType("minecraft:generic_9x3");
 *
 * // Check if handler exists
 * if (ScreenHandlerHelper.exists("mosbergapi:custom_container")) {
 *     LOGGER.info("Custom screen handler registered!");
 *          }
 *
 *          // Transfer item stacks (for shift-click)
 *          boolean transferred = ScreenHandlerHelper.transferSlot(handler, sourceSlot);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see ScreenHandler
 * @see ScreenHandlerType
 * @see Slot
 */
public class ScreenHandlerHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenHandlerHelper.class);

    /** Standard player inventory slot count (hotbar + main) */
    public static final int PLAYER_INVENTORY_SIZE = 36;

    /** Hotbar slot count */
    public static final int HOTBAR_SIZE = 9;

    /** Main inventory slot count (3 rows) */
    public static final int MAIN_INVENTORY_SIZE = 27;

    private ScreenHandlerHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Gets a screen handler type by its identifier.
     *
     * @param id The identifier of the screen handler type
     * @return The screen handler type, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * ScreenHandlerType<?> type = ScreenHandlerHelper.getType("minecraft:generic_9x3");
     * }</pre>
     */
    @Nullable
    public static ScreenHandlerType<?> getType(@NotNull String id) {
        return getType(Identifier.tryParse(id));
    }

    /**
     * Gets a screen handler type by its identifier.
     *
     * @param id The identifier of the screen handler type
     * @return The screen handler type, or null if not found
     */
    @Nullable
    public static ScreenHandlerType<?> getType(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.SCREEN_HANDLER.get(id);
    }

    /**
     * Checks if a screen handler type exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the screen handler type is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (ScreenHandlerHelper.exists("mosbergapi:custom_container")) {
     *     LOGGER.info("Custom screen handler is registered!");
     *          }
     * }</pre>
     */
    public static boolean exists(@NotNull String id) {
        return exists(Identifier.tryParse(id));
    }

    /**
     * Checks if a screen handler type exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the screen handler type is registered
     */
    public static boolean exists(@Nullable Identifier id) {
        return id != null && Registries.SCREEN_HANDLER.containsId(id);
    }

    /**
     * Gets the identifier of a screen handler type.
     *
     * @param type The screen handler type
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = ScreenHandlerHelper.getId(type);
     * // Returns: minecraft:generic_9x3 (for example)
     * }</pre>
     */
    @Nullable
    public static Identifier getId(@NotNull ScreenHandlerType<?> type) {
        if (type == null)
            throw new NullPointerException("ScreenHandlerType cannot be null");
        return Registries.SCREEN_HANDLER.getId(type);
    }

    /**
     * Checks if a player can interact with a screen handler.
     *
     * @param handler The screen handler
     * @param player The player
     * @return true if the player can use the screen handler
     * @throws NullPointerException if handler or player is null
     *
     * @example
     *
     *          <pre>{@code
     * if (ScreenHandlerHelper.canUse(handler, player)) {
     *     // Player can interact with this GUI
     *          }
     * }</pre>
     */
    public static boolean canUse(@NotNull ScreenHandler handler, @NotNull PlayerEntity player) {
        if (handler == null)
            throw new NullPointerException("ScreenHandler cannot be null");
        if (player == null)
            throw new NullPointerException("Player cannot be null");

        return handler.canUse(player);
    }

    /**
     * Gets the number of slots in a screen handler.
     *
     * @param handler The screen handler
     * @return The total number of slots
     * @throws NullPointerException if handler is null
     *
     * @example
     *
     *          <pre>{@code
     * int slotCount = ScreenHandlerHelper.getSlotCount(handler);
     *          LOGGER.info("Handler has {} slots", slotCount);
     * }</pre>
     */
    public static int getSlotCount(@NotNull ScreenHandler handler) {
        if (handler == null)
            throw new NullPointerException("ScreenHandler cannot be null");
        return handler.slots.size();
    }

    /**
     * Gets a slot from a screen handler by index.
     *
     * @param handler The screen handler
     * @param index The slot index
     * @return The slot, or null if index is invalid
     * @throws NullPointerException if handler is null
     *
     * @example
     *
     *          <pre>{@code
     * Slot slot = ScreenHandlerHelper.getSlot(handler, 0);
     * if (slot != null) {
     *     ItemStack stack = slot.getStack();
     *          }
     * }</pre>
     */
    @Nullable
    public static Slot getSlot(@NotNull ScreenHandler handler, int index) {
        if (handler == null)
            throw new NullPointerException("ScreenHandler cannot be null");

        if (index < 0 || index >= handler.slots.size()) {
            return null;
        }
        return handler.slots.get(index);
    }

    /**
     * Gets the item stack in a specific slot.
     *
     * @param handler The screen handler
     * @param index The slot index
     * @return The item stack, or empty stack if slot is invalid
     * @throws NullPointerException if handler is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack stack = ScreenHandlerHelper.getStackInSlot(handler, 5);
     * if (!stack.isEmpty()) {
     *          LOGGER.info("Slot 5 contains: {}", stack.getItem());
     *          }
     * }</pre>
     */
    @NotNull
    public static ItemStack getStackInSlot(@NotNull ScreenHandler handler, int index) {
        Slot slot = getSlot(handler, index);
        return slot != null ? slot.getStack() : ItemStack.EMPTY;
    }

    /**
     * Sets the item stack in a specific slot.
     *
     * @param handler The screen handler
     * @param index The slot index
     * @param stack The item stack to set
     * @throws NullPointerException if handler or stack is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack stack = new ItemStack(Items.DIAMOND);
     * ScreenHandlerHelper.setStackInSlot(handler, 0, stack);
     * }</pre>
     */
    public static void setStackInSlot(@NotNull ScreenHandler handler, int index,
            @NotNull ItemStack stack) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");

        Slot slot = getSlot(handler, index);
        if (slot != null) {
            slot.setStack(stack);
        }
    }

    /**
     * Checks if a slot can accept an item stack.
     *
     * @param handler The screen handler
     * @param index The slot index
     * @param stack The item stack to check
     * @return true if the slot can accept the item
     * @throws NullPointerException if handler or stack is null
     *
     * @example
     *
     *          <pre>{@code
     * if (ScreenHandlerHelper.canInsert(handler, 0, stack)) {
     *     // Stack can be placed in this slot
     *          }
     * }</pre>
     */
    public static boolean canInsert(@NotNull ScreenHandler handler, int index,
            @NotNull ItemStack stack) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");

        Slot slot = getSlot(handler, index);
        return slot != null && slot.canInsert(stack);
    }

    /**
     * Checks if a slot can be taken from (removed).
     *
     * @param handler The screen handler
     * @param index The slot index
     * @param player The player attempting to take
     * @return true if the player can take from this slot
     * @throws NullPointerException if handler or player is null
     *
     * @example
     *
     *          <pre>{@code
     * if (ScreenHandlerHelper.canTakeItems(handler, 5, player)) {
     *     // Player can remove items from this slot
     *          }
     * }</pre>
     */
    public static boolean canTakeItems(@NotNull ScreenHandler handler, int index,
            @NotNull PlayerEntity player) {
        if (player == null)
            throw new NullPointerException("Player cannot be null");

        Slot slot = getSlot(handler, index);
        return slot != null && slot.canTakeItems(player);
    }

    /**
     * Calculates the number of container slots (excluding player inventory).
     *
     * @param handler The screen handler
     * @return The number of container slots
     * @throws NullPointerException if handler is null
     *
     * @example
     *
     *          <pre>{@code
     * int containerSlots = ScreenHandlerHelper.getContainerSlotCount(handler);
     * // For a chest: 27, double chest: 54, etc.
     * }</pre>
     */
    public static int getContainerSlotCount(@NotNull ScreenHandler handler) {
        int totalSlots = getSlotCount(handler);
        return Math.max(0, totalSlots - PLAYER_INVENTORY_SIZE);
    }

    /**
     * Checks if a slot index is in the player's inventory section.
     *
     * @param handler The screen handler
     * @param index The slot index
     * @return true if the slot is part of the player inventory
     *
     * @example
     *
     *          <pre>{@code
     * if (ScreenHandlerHelper.isPlayerInventorySlot(handler, slotIndex)) {
     *     // This is a player inventory slot
     *          }
     * }</pre>
     */
    public static boolean isPlayerInventorySlot(@NotNull ScreenHandler handler, int index) {
        int containerSlots = getContainerSlotCount(handler);
        return index >= containerSlots && index < getSlotCount(handler);
    }

    /**
     * Checks if a slot index is in the container section.
     *
     * @param handler The screen handler
     * @param index The slot index
     * @return true if the slot is part of the container
     *
     * @example
     *
     *          <pre>{@code
     * if (ScreenHandlerHelper.isContainerSlot(handler, slotIndex)) {
     *     // This is a container slot
     *          }
     * }</pre>
     */
    public static boolean isContainerSlot(@NotNull ScreenHandler handler, int index) {
        return index >= 0 && index < getContainerSlotCount(handler);
    }

    /**
     * Closes a screen handler for a player.
     *
     * @param handler The screen handler to close
     * @param player The player
     * @throws NullPointerException if handler or player is null
     *
     * @example
     *
     *          <pre>{@code
     * ScreenHandlerHelper.close(handler, player);
     * // GUI is now closed
     * }</pre>
     */
    public static void close(@NotNull ScreenHandler handler, @NotNull PlayerEntity player) {
        if (handler == null)
            throw new NullPointerException("ScreenHandler cannot be null");
        if (player == null)
            throw new NullPointerException("Player cannot be null");

        handler.onClosed(player);
        LOGGER.debug("Closed screen handler for player: {}", player.getName().getString());
    }

    /**
     * Gets all registered screen handler types.
     *
     * @return An iterable of all screen handler types
     *
     * @example
     *
     *          <pre>{@code
     * for (ScreenHandlerType<?> type : ScreenHandlerHelper.getAllTypes()) {
     *          LOGGER.info("Screen handler: {}", ScreenHandlerHelper.getId(type));
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<ScreenHandlerType<?>> getAllTypes() {
        return Registries.SCREEN_HANDLER;
    }

    /**
     * Gets the number of registered screen handler types.
     *
     * @return The count of screen handler types
     *
     * @example
     *
     *          <pre>{@code
     * int count = ScreenHandlerHelper.getTypeCount();
     *          LOGGER.info("Total screen handler types: {}", count);
     * }</pre>
     */
    public static int getTypeCount() {
        return Registries.SCREEN_HANDLER.size();
    }

    /**
     * Transfers items from the player inventory to the container.
     *
     * <p>
     * Helper method for implementing shift-click behavior. Returns true if any items were
     * transferred.
     *
     * @param handler The screen handler
     * @param inventory The container inventory
     * @param stack The item stack to transfer
     * @return true if items were transferred
     * @throws NullPointerException if any parameter is null
     *
     * @example
     *
     *          <pre>{@code
     * // In your ScreenHandler's quickMove method:
     * boolean transferred = ScreenHandlerHelper.transferToContainer(
     *     this, containerInventory, stack);
     * }</pre>
     */
    public static boolean transferToContainer(@NotNull ScreenHandler handler,
            @NotNull Inventory inventory, @NotNull ItemStack stack) {
        if (handler == null)
            throw new NullPointerException("ScreenHandler cannot be null");
        if (inventory == null)
            throw new NullPointerException("Inventory cannot be null");
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack slotStack = inventory.getStack(i);
            // Use static canCombine method (Minecraft 1.21.10+)
            if (slotStack.isEmpty() || ItemStack.areItemsAndComponentsEqual(stack, slotStack)) {
                int maxCount = Math.min(stack.getMaxCount(), inventory.getMaxCount(stack));
                int transferAmount = Math.min(stack.getCount(), maxCount - slotStack.getCount());

                if (transferAmount > 0) {
                    if (slotStack.isEmpty()) {
                        inventory.setStack(i, stack.split(transferAmount));
                    } else {
                        slotStack.increment(transferAmount);
                        stack.decrement(transferAmount);
                    }

                    if (stack.isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return stack.getCount() > 0;
    }

}
