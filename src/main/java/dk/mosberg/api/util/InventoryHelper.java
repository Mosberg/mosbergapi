package dk.mosberg.api.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Utility class providing helper methods for Inventory operations
 */
public class InventoryHelper {

    /**
     * Checks if an inventory is empty
     */
    public boolean isEmpty(Inventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.getStack(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears all items from an inventory
     */
    public void clear(Inventory inventory) {
        inventory.clear();
    }

    /**
     * Gets the first empty slot index in an inventory
     */
    public int getFirstEmptySlot(Inventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if an inventory has space for an item stack
     */
    public boolean hasSpace(Inventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack slotStack = inventory.getStack(i);
            if (slotStack.isEmpty()) {
                return true;
            }
            if (ItemStack.areItemsAndComponentsEqual(slotStack, stack)
                    && slotStack.getCount() + stack.getCount() <= slotStack.getMaxCount()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Counts how many of a specific item are in the inventory
     */
    public int countItem(Inventory inventory, Item item) {
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isOf(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    /**
     * Checks if an inventory contains at least the specified amount of an item
     */
    public boolean contains(Inventory inventory, Item item, int amount) {
        return countItem(inventory, item) >= amount;
    }

    /**
     * Finds the first slot containing a specific item
     */
    public int findSlot(Inventory inventory, Item item) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isOf(item)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes a specific amount of an item from the inventory
     */
    public boolean removeItem(Inventory inventory, Item item, int amount) {
        int remaining = amount;
        for (int i = 0; i < inventory.size() && remaining > 0; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isOf(item)) {
                int toRemove = Math.min(stack.getCount(), remaining);
                stack.decrement(toRemove);
                remaining -= toRemove;
            }
        }
        return remaining == 0;
    }

    /**
     * Tries to insert an item stack into the inventory
     */
    public boolean insertStack(Inventory inventory, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        // Try to merge with existing stacks first
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack slotStack = inventory.getStack(i);
            if (ItemStack.areItemsAndComponentsEqual(slotStack, stack)) {
                int maxCount = Math.min(inventory.getMaxCountPerStack(), stack.getMaxCount());
                int transferAmount = Math.min(stack.getCount(), maxCount - slotStack.getCount());
                if (transferAmount > 0) {
                    slotStack.increment(transferAmount);
                    stack.decrement(transferAmount);
                    if (stack.isEmpty()) {
                        return true;
                    }
                }
            }
        }

        // Try to place in empty slots
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isEmpty()) {
                inventory.setStack(i, stack.copy());
                stack.setCount(0);
                return true;
            }
        }

        return stack.isEmpty();
    }

    /**
     * Gets all non-empty item stacks from an inventory
     */
    public List<ItemStack> getAllStacks(Inventory inventory) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    /**
     * Copies all items from one inventory to another
     */
    public void copyInventory(Inventory source, Inventory destination) {
        int size = Math.min(source.size(), destination.size());
        for (int i = 0; i < size; i++) {
            destination.setStack(i, source.getStack(i).copy());
        }
    }

    /**
     * Swaps the contents of two inventories
     */
    public void swapInventories(Inventory inv1, Inventory inv2) {
        int size = Math.min(inv1.size(), inv2.size());
        for (int i = 0; i < size; i++) {
            ItemStack temp = inv1.getStack(i).copy();
            inv1.setStack(i, inv2.getStack(i).copy());
            inv2.setStack(i, temp);
        }
    }

    /**
     * Gets the total number of occupied slots
     */
    public int getOccupiedSlots(Inventory inventory) {
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.getStack(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if a player's inventory contains a specific item
     */
    public boolean playerHasItem(PlayerEntity player, Item item) {
        return player.getInventory().contains(new ItemStack(item));
    }

    /**
     * Removes an item from a player's inventory
     */
    public boolean removeFromPlayer(PlayerEntity player, Item item, int amount) {
        return removeItem(player.getInventory(), item, amount);
    }

    /**
     * Transfers items between two inventories up to the specified maximum amount.
     * <p>
     * Attempts to move items from the source inventory to the destination inventory, respecting
     * stack sizes and attempting to merge with existing stacks when possible.
     *
     * @param source The inventory to transfer items from
     * @param destination The inventory to transfer items to
     * @param maxAmount The maximum number of items to transfer
     * @return The total number of items successfully transferred
     *
     * @throws IllegalArgumentException if source or destination is null
     * @throws IllegalArgumentException if maxAmount is negative
     *
     * @example
     *
     *          <pre>{@code
     * // Transfer up to 64 items from chest to player inventory
     * int transferred = inventoryHelper.transferItems(chestInventory, playerInventory, 64);
     *
     * // Transfer all items (large number)
     * int transferred = inventoryHelper.transferItems(source, dest, Integer.MAX_VALUE);
     * }</pre>
     *
     * @since 1.0.0
     * @author Mosberg
     */
    public int transferItems(Inventory source, Inventory destination, int maxAmount) {
        if (source == null) {
            throw new IllegalArgumentException("Source inventory cannot be null");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Destination inventory cannot be null");
        }
        if (maxAmount < 0) {
            throw new IllegalArgumentException("Max amount cannot be negative: " + maxAmount);
        }

        int transferred = 0;

        // Iterate through all slots in the source inventory
        for (int i = 0; i < source.size() && transferred < maxAmount; i++) {
            ItemStack sourceStack = source.getStack(i);

            // Skip empty slots
            if (sourceStack.isEmpty()) {
                continue;
            }

            // Calculate how many items we can still transfer
            int remainingToTransfer = maxAmount - transferred;

            // Calculate how many to transfer from this slot (limited by stack size and remaining
            // capacity)
            int toTransfer = Math.min(sourceStack.getCount(), remainingToTransfer);

            // Create a copy of the stack to transfer
            ItemStack transferStack = sourceStack.copyWithCount(toTransfer);

            // Try to insert into destination
            ItemStack remaining = addItemToInventory(destination, transferStack);

            // Calculate how many were actually transferred
            int actuallyTransferred = toTransfer - remaining.getCount();

            // Remove the transferred items from the source
            sourceStack.decrement(actuallyTransferred);

            // Update the total transferred count
            transferred += actuallyTransferred;

            // If we couldn't transfer any items to the destination, it's probably full
            if (actuallyTransferred == 0) {
                break;
            }
        }

        return transferred;
    }

    /**
     * Adds an item stack to an inventory, merging with existing stacks when possible.
     * <p>
     * This method attempts to:
     * <ol>
     * <li>Merge with existing stacks of the same item type</li>
     * <li>Place in empty slots if merging isn't possible</li>
     * <li>Return any remaining items that couldn't be inserted</li>
     * </ol>
     *
     * @param destination The inventory to add items to
     * @param stack The item stack to add (will not be modified)
     * @return A new ItemStack containing any items that couldn't be inserted (empty if all were
     *         inserted)
     *
     * @throws IllegalArgumentException if destination or stack is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack diamonds = new ItemStack(Items.DIAMOND, 100);
     * ItemStack remaining = InventoryHelper.addItemToInventory(inventory, diamonds);
     *
     * if (remaining.isEmpty()) {
     *     System.out.println("All items inserted successfully");
     *          } else {
     *          System.out.println(remaining.getCount() + " items couldn't fit");
     *          }
     * }</pre>
     *
     * @since 1.0.0
     * @author Mosberg
     */
    public static ItemStack addItemToInventory(Inventory destination, ItemStack stack) {
        if (destination == null) {
            throw new IllegalArgumentException("Destination inventory cannot be null");
        }
        if (stack == null) {
            throw new IllegalArgumentException("ItemStack cannot be null");
        }

        // Return early if the stack is empty
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Create a working copy so we don't modify the original
        ItemStack remaining = stack.copy();

        // Phase 1: Try to merge with existing stacks
        for (int i = 0; i < destination.size() && !remaining.isEmpty(); i++) {
            ItemStack slotStack = destination.getStack(i);

            // Skip empty slots (we'll handle those in phase 2)
            if (slotStack.isEmpty()) {
                continue;
            }

            // Check if stacks can be merged (same item and components)
            if (ItemStack.areItemsAndComponentsEqual(slotStack, remaining)) {
                // Calculate how many items we can add to this stack
                int maxCount = Math.min(destination.getMaxCountPerStack(), slotStack.getMaxCount());
                int availableSpace = maxCount - slotStack.getCount();

                if (availableSpace > 0) {
                    int toTransfer = Math.min(remaining.getCount(), availableSpace);
                    slotStack.increment(toTransfer);
                    remaining.decrement(toTransfer);
                }
            }
        }

        // Phase 2: Try to place remaining items in empty slots
        for (int i = 0; i < destination.size() && !remaining.isEmpty(); i++) {
            ItemStack slotStack = destination.getStack(i);

            // Only use empty slots
            if (!slotStack.isEmpty()) {
                continue;
            }

            // Calculate how many items we can place in this empty slot
            int maxCount = Math.min(destination.getMaxCountPerStack(), remaining.getMaxCount());
            int toPlace = Math.min(remaining.getCount(), maxCount);

            // Place the items
            destination.setStack(i, remaining.copyWithCount(toPlace));
            remaining.decrement(toPlace);
        }

        // Return any items that couldn't be inserted
        return remaining.isEmpty() ? ItemStack.EMPTY : remaining;
    }

}
