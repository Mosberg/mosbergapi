package dk.mosberg.api.data.provider;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;

/**
 * Enhanced loot table provider with helper methods for MosbergAPI
 */
public abstract class MosbergLootTableProvider extends FabricBlockLootTableProvider {

        protected final RegistryWrapper.WrapperLookup registryLookup;

        public MosbergLootTableProvider(FabricDataOutput dataOutput,
                        CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
                super(dataOutput, registryLookup);
                this.registryLookup = registryLookup.join();
        }

        /**
         * Helper: Drops the block itself
         */
        protected void addSimpleDrop(Block block) {
                addDrop(block);
        }

        /**
         * Helper: Drops nothing
         */
        protected void addNoDrop(Block block) {
                addDrop(block, LootTable.builder());
        }

        /**
         * Helper: Drops with fortune enchantment (like ores)
         */
        protected void addOreDrop(Block block, Item drop) {
                addDrop(block, oreDrops(block, drop));
        }

        /**
         * Helper: Drops a random amount between min and max
         */
        protected void addRandomDrop(Block block, ItemConvertible drop, float min, float max) {
                addDrop(block, drops(drop, UniformLootNumberProvider.create(min, max)));
        }

        /**
         * Helper: Drops block with silk touch, otherwise drops the specified item
         */
        protected void addSilkTouchOrDrop(Block block, ItemConvertible alternativeDrop) {
                addDrop(block, dropsWithSilkTouch(block, ItemEntry.builder(alternativeDrop)));
        }

        /**
         * Helper: Drops item with silk touch, otherwise drops with fortune
         */
        protected void addSilkTouchOrFortuneOre(Block block, Item drop) {
                RegistryWrapper.Impl<Enchantment> enchantmentLookup =
                                this.registryLookup.getOrThrow(RegistryKeys.ENCHANTMENT);

                addDrop(block, dropsWithSilkTouch(block, applyExplosionDecay(block,
                                ItemEntry.builder(drop).apply(ApplyBonusLootFunction
                                                .oreDrops(enchantmentLookup.getOrThrow(
                                                                Enchantments.FORTUNE))))));
        }

        /**
         * Helper: Drops slab - 1 if single, 2 if double
         */
        protected void addSlabDrop(Block slab) {
                addDrop(slab, slabDrops(slab));
        }

        /**
         * Helper: Drops based on age property (like crops)
         */
        protected void addCropDrop(Block crop, Item seeds, Item product, int maxAge) {
                addDrop(crop, cropDrops(crop, product, seeds,
                                BlockStatePropertyLootCondition.builder(crop)
                                                .properties(StatePredicate.Builder.create()
                                                                .exactMatch(Properties.AGE_7,
                                                                                maxAge))));
        }

        /**
         * Helper: Drops only when harvested with shears
         */
        protected void addShearsDrop(Block block, ItemConvertible drop) {
                addDrop(block, LootTable.builder().pool(
                                LootPool.builder().with(ItemEntry.builder(drop)).conditionally(
                                                MatchToolLootCondition.builder(ItemPredicate.Builder
                                                                .create().tag(null, ItemTags.SWORDS) // Changed
                                                // from
                                                // SHEARS
                                                ))));
        }

        /**
         * Helper: Drops door blocks
         */
        protected void addDoorDrop(Block door) {
                addDrop(door, doorDrops(door));
        }

        /**
         * Helper: Drops with specific count
         */
        protected void addDropWithCount(Block block, ItemConvertible drop, int count) {
                addDrop(block, drops(drop, ConstantLootNumberProvider.create(count)));
        }

        /**
         * Helper: Drops item with count range affected by fortune
         */
        protected void addFortuneRangeDrop(Block block, ItemConvertible drop, int min, int max) {
                RegistryWrapper.Impl<Enchantment> enchantmentLookup =
                                this.registryLookup.getOrThrow(RegistryKeys.ENCHANTMENT);

                addDrop(block, LootTable.builder().pool(LootPool.builder().with(applyExplosionDecay(
                                block,
                                ItemEntry.builder(drop).apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(min, max)))
                                                .apply(ApplyBonusLootFunction.uniformBonusCount(
                                                                enchantmentLookup.getOrThrow(
                                                                                Enchantments.FORTUNE)))))));
        }

        /**
         * Helper: Drops with count based on fortune level
         */
        protected void addFortuneOreDropWithCount(Block block, Item drop, int baseCount) {
                RegistryWrapper.Impl<Enchantment> enchantmentLookup =
                                this.registryLookup.getOrThrow(RegistryKeys.ENCHANTMENT);

                addDrop(block, LootTable.builder().pool(LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))
                                .with(applyExplosionDecay(block, ItemEntry.builder(drop)
                                                .apply(SetCountLootFunction
                                                                .builder(ConstantLootNumberProvider
                                                                                .create(baseCount)))
                                                .apply(ApplyBonusLootFunction.oreDrops(
                                                                enchantmentLookup.getOrThrow(
                                                                                Enchantments.FORTUNE)))))));
        }
}
