package dk.mosberg.api.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * Helper for entity attributes
 */
public class AttributeHelper {

    public static double getValue(LivingEntity entity, RegistryEntry<EntityAttribute> attribute) {
        EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
        return instance != null ? instance.getValue() : 0;
    }

    public static void setValue(LivingEntity entity, RegistryEntry<EntityAttribute> attribute,
            double value) {
        EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }
}
