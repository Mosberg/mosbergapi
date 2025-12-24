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
}
