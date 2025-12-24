package dk.mosberg.api.util;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

/**
 * Utility class providing helper methods for Item and ItemStack operations
 */
public class ItemHelper {

    /**
     * Creates an ItemStack with the specified count
     */
    public ItemStack createStack(Item item, int count) {
        return new ItemStack(item, count);
    }

    /**
     * Creates a single ItemStack
     */
    public ItemStack createStack(Item item) {
        return new ItemStack(item);
    }

    /**
     * Checks if an ItemStack is empty
     */
    public boolean isEmpty(ItemStack stack) {
        return stack.isEmpty();
    }

    /**
     * Gets the count of items in a stack
     */
    public int getCount(ItemStack stack) {
        return stack.getCount();
    }

    /**
     * Sets the count of items in a stack
     */
    public void setCount(ItemStack stack, int count) {
        stack.setCount(count);
    }

    /**
     * Increments the stack count
     */
    public void increment(ItemStack stack, int amount) {
        stack.increment(amount);
    }

    /**
     * Decrements the stack count
     */
    public void decrement(ItemStack stack, int amount) {
        stack.decrement(amount);
    }

    /**
     * Checks if a stack is at max count
     */
    public boolean isMaxCount(ItemStack stack) {
        return stack.getCount() >= stack.getMaxCount();
    }

    /**
     * Gets the max stack size for an item
     */
    public int getMaxCount(ItemStack stack) {
        return stack.getMaxCount();
    }

    /**
     * Damages an item stack (server-side)
     */
    public void damage(ItemStack stack, int amount, ServerWorld world,
            @Nullable ServerPlayerEntity player) {
        stack.damage(amount, world, player, item -> {
            // Item broke callback
        });
    }

    /**
     * Damages an item stack with equipment slot tracking (server-side)
     */
    public void damage(ItemStack stack, int amount, LivingEntity entity, EquipmentSlot slot) {
        stack.damage(amount, entity, slot);
    }

    /**
     * Gets the current damage (durability used) of an item
     */
    public int getDamage(ItemStack stack) {
        return stack.getDamage();
    }

    /**
     * Sets the damage of an item
     */
    public void setDamage(ItemStack stack, int damage) {
        stack.setDamage(damage);
    }

    /**
     * Gets the max damage (durability) of an item
     */
    public int getMaxDamage(ItemStack stack) {
        return stack.getMaxDamage();
    }

    /**
     * Checks if an item is damaged
     */
    public boolean isDamaged(ItemStack stack) {
        return stack.isDamaged();
    }

    /**
     * Checks if an item is damageable
     */
    public boolean isDamageable(ItemStack stack) {
        return stack.isDamageable();
    }

    /**
     * Gets the remaining durability of an item
     */
    public int getRemainingDurability(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamage();
    }

    /**
     * Checks if an item is about to break
     */
    public boolean isAboutToBreak(ItemStack stack, int threshold) {
        return getRemainingDurability(stack) <= threshold;
    }

    /**
     * Copies an ItemStack
     */
    public ItemStack copy(ItemStack stack) {
        return stack.copy();
    }

    /**
     * Checks if two ItemStacks are equal (item and components)
     */
    public boolean areEqual(ItemStack stack1, ItemStack stack2) {
        return ItemStack.areItemsAndComponentsEqual(stack1, stack2);
    }

    /**
     * Checks if two ItemStacks are of the same item type
     */
    public boolean isSameItem(ItemStack stack1, ItemStack stack2) {
        return ItemStack.areItemsEqual(stack1, stack2);
    }

    /**
     * Sets a custom name for an ItemStack
     */
    public void setCustomName(ItemStack stack, Text name) {
        stack.set(DataComponentTypes.CUSTOM_NAME, name);
    }

    /**
     * Gets the custom name of an ItemStack
     */
    @Nullable
    public Text getCustomName(ItemStack stack) {
        return stack.get(DataComponentTypes.CUSTOM_NAME);
    }

    /**
     * Checks if an ItemStack has a custom name
     */
    public boolean hasCustomName(ItemStack stack) {
        return stack.contains(DataComponentTypes.CUSTOM_NAME);
    }

    /**
     * Sets lore (description) for an ItemStack
     */
    public void setLore(ItemStack stack, List<Text> lore) {
        stack.set(DataComponentTypes.LORE, new net.minecraft.component.type.LoreComponent(lore));
    }

    /**
     * Makes an ItemStack unbreakable
     */
    public void makeUnbreakable(ItemStack stack) {
        stack.set(DataComponentTypes.UNBREAKABLE, net.minecraft.util.Unit.INSTANCE);
    }

    /**
     * Makes an ItemStack unbreakable with custom tooltip visibility Note: In 1.21.10, UNBREAKABLE
     * uses Unit type and always shows in tooltip
     */
    public void makeUnbreakable(ItemStack stack, boolean showInTooltip) {
        if (showInTooltip) {
            stack.set(DataComponentTypes.UNBREAKABLE, net.minecraft.util.Unit.INSTANCE);
        } else {
            // Remove the component to hide it
            stack.remove(DataComponentTypes.UNBREAKABLE);
        }
    }

    /**
     * Checks if an ItemStack is unbreakable
     */
    public boolean isUnbreakable(ItemStack stack) {
        return stack.contains(DataComponentTypes.UNBREAKABLE);
    }

    /**
     * Removes the unbreakable property from an ItemStack
     */
    public void removeUnbreakable(ItemStack stack) {
        stack.remove(DataComponentTypes.UNBREAKABLE);
    }


    /**
     * Gets the enchantment level of an item
     */
    public int getEnchantmentLevel(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        return EnchantmentHelper.getLevel(enchantment, stack);
    }

    /**
     * Checks if an item has any enchantments
     */
    public boolean hasEnchantments(ItemStack stack) {
        return stack.hasEnchantments();
    }

    /**
     * Gives an item to a player, dropping it if inventory is full
     */
    public void giveOrDrop(PlayerEntity player, ItemStack stack) {
        if (!player.giveItemStack(stack)) {
            player.dropItem(stack, false);
        }
    }

    /**
     * Checks if a player can hold an item
     */
    public boolean canPlayerHold(PlayerEntity player, ItemStack stack) {
        return player.getInventory().getEmptySlot() != -1;
    }

    /**
     * Gets the translation key for an item
     */
    public String getTranslationKey(ItemStack stack) {
        return stack.getItem().getTranslationKey();
    }

    /**
     * Gets the display name of an item
     */
    public Text getName(ItemStack stack) {
        return stack.getName();
    }

    /**
     * Gets the rarity of an item
     */
    public net.minecraft.util.Rarity getRarity(ItemStack stack) {
        return stack.getRarity();
    }

    /**
     * Checks if an item has glint (enchantment glow)
     */
    public boolean hasGlint(ItemStack stack) {
        return stack.hasGlint();
    }

    /**
     * Checks if an item is food
     */
    public boolean isFood(ItemStack stack) {
        return stack.contains(DataComponentTypes.FOOD);
    }

    /**
     * Gets the food component from an item stack
     */
    @Nullable
    public net.minecraft.component.type.FoodComponent getFoodComponent(ItemStack stack) {
        return stack.get(DataComponentTypes.FOOD);
    }

    /**
     * Checks if two items can stack together
     */
    public boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return ItemStack.areItemsAndComponentsEqual(stack1, stack2)
                && stack1.getCount() + stack2.getCount() <= stack1.getMaxCount();
    }

    /**
     * Splits a stack in half
     */
    public ItemStack split(ItemStack stack, int amount) {
        return stack.split(amount);
    }
}
