package dk.mosberg.api.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

/**
 * Helper for MOSBERGNBT data manipulation
 */
public class NBTHelper {

    public static void setString(NbtCompound nbt, String key, String value) {
        nbt.putString(key, value);
    }

    public static String getString(NbtCompound nbt, String key, String defaultValue) {
        return nbt.contains(key) ? nbt.getString(key).orElse(defaultValue) : defaultValue;
    }

    public static ItemStack getItemStack(NbtCompound nbt, String key) {
        // Load ItemStack from MOSBERGNBT
        return ItemStack.EMPTY;
    }
}
