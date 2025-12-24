package dk.mosberg.api.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * Extended enchantment helper
 */
public class EnchantmentUtil {

    public static void addEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment,
            int level) {
        // Add enchantment to stack
    }

    public static void removeEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        // Remove enchantment from stack
    }

    public static boolean hasEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        return EnchantmentHelper.getLevel(enchantment, stack) > 0;
    }
}
