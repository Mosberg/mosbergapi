package dk.mosberg.api.registry;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.block.Block;
import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.entry.LootPoolEntryType;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.nbt.LootNbtProviderType;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.loot.provider.score.LootScoreProviderType;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.predicate.component.ComponentPredicate;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.scoreboard.number.NumberFormatType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.StatType;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.structure.rule.PosRuleTestType;
import net.minecraft.structure.rule.RuleTestType;
import net.minecraft.structure.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.FloatProviderType;
import net.minecraft.util.math.intprovider.IntProviderType;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSourceType;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.chunk.placement.StructurePlacementType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.size.FeatureSizeType;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.heightprovider.HeightProviderType;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;
import net.minecraft.world.gen.root.RootPlacerType;
import net.minecraft.world.gen.stateprovider.BlockStateProviderType;
import net.minecraft.world.gen.structure.StructureType;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import net.minecraft.world.poi.PointOfInterestType;

/**
 * Centralized registry helper for all Minecraft registries. Provides type-safe registration methods
 * for mod content.
 */
public class MosbergRegistries {

        // Entity & AI
        public static <T extends Activity> T registerActivity(String name, T activity) {
                return Registry.register(Registries.ACTIVITY, Identifier.of(getModId(), name),
                                activity);
        }

        public static <T extends EntityAttribute> T registerAttribute(String name, T attribute) {
                return Registry.register(Registries.ATTRIBUTE, Identifier.of(getModId(), name),
                                attribute);
        }

        public static <T extends MemoryModuleType<?>> T registerMemoryModuleType(String name,
                        T memoryModule) {
                return Registry.register(Registries.MEMORY_MODULE_TYPE,
                                Identifier.of(getModId(), name), memoryModule);
        }

        public static <T extends Schedule> T registerSchedule(String name, T schedule) {
                return Registry.register(Registries.SCHEDULE, Identifier.of(getModId(), name),
                                schedule);
        }

        public static <T extends SensorType<?>> T registerSensorType(String name, T sensorType) {
                return Registry.register(Registries.SENSOR_TYPE, Identifier.of(getModId(), name),
                                sensorType);
        }

        // Blocks & Block Entities
        public static <T extends Block> T registerBlock(String name, T block) {
                return Registry.register(Registries.BLOCK, Identifier.of(getModId(), name), block);
        }

        public static <T extends BlockEntityType<?>> T registerBlockEntityType(String name,
                        T blockEntityType) {
                return Registry.register(Registries.BLOCK_ENTITY_TYPE,
                                Identifier.of(getModId(), name), blockEntityType);
        }

        public static <T extends BlockPredicateType<?>> T registerBlockPredicateType(String name,
                        T predicateType) {
                return Registry.register(Registries.BLOCK_PREDICATE_TYPE,
                                Identifier.of(getModId(), name), predicateType);
        }

        public static <T extends BlockStateProviderType<?>> T registerBlockStateProviderType(
                        String name, T providerType) {
                return Registry.register(Registries.BLOCK_STATE_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        // Items & Components
        public static <T extends Item> T registerItem(String name, T item) {
                return Registry.register(Registries.ITEM, Identifier.of(getModId(), name), item);
        }

        public static <T extends ItemGroup> T registerItemGroup(String name, T itemGroup) {
                return Registry.register(Registries.ITEM_GROUP, Identifier.of(getModId(), name),
                                itemGroup);
        }

        public static <T extends ComponentType<?>> T registerDataComponentType(String name,
                        T componentType) {
                return Registry.register(Registries.DATA_COMPONENT_TYPE,
                                Identifier.of(getModId(), name), componentType);
        }

        // FIXED: ComponentPredicate.Type<?> is final, use concrete type
        public static ComponentPredicate.Type<?> registerDataComponentPredicateType(String name,
                        ComponentPredicate.Type<?> predicateType) {
                return Registry.register(Registries.DATA_COMPONENT_PREDICATE_TYPE,
                                Identifier.of(getModId(), name), predicateType);
        }

        // FIXED: ConsumeEffect.Type<?> is final, use concrete type
        public static ConsumeEffect.Type<?> registerConsumeEffectType(String name,
                        ConsumeEffect.Type<?> effectType) {
                return Registry.register(Registries.CONSUME_EFFECT_TYPE,
                                Identifier.of(getModId(), name), effectType);
        }

        // Entities
        public static <T extends EntityType<?>> T registerEntityType(String name, T entityType) {
                return Registry.register(Registries.ENTITY_TYPE, Identifier.of(getModId(), name),
                                entityType);
        }

        // Fluids
        public static <T extends Fluid> T registerFluid(String name, T fluid) {
                return Registry.register(Registries.FLUID, Identifier.of(getModId(), name), fluid);
        }

        // FIXED: GameEvent is final, use concrete type
        public static GameEvent registerGameEvent(String name, GameEvent gameEvent) {
                return Registry.register(Registries.GAME_EVENT, Identifier.of(getModId(), name),
                                gameEvent);
        }

        // FIXED: SoundEvent is final, use concrete type
        public static SoundEvent registerSoundEvent(String name, SoundEvent soundEvent) {
                return Registry.register(Registries.SOUND_EVENT, Identifier.of(getModId(), name),
                                soundEvent);
        }

        // Particle Types
        public static <T extends ParticleType<?>> T registerParticleType(String name,
                        T particleType) {
                return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(getModId(), name),
                                particleType);
        }

        // Status Effects & Potions
        public static <T extends StatusEffect> T registerStatusEffect(String name, T statusEffect) {
                return Registry.register(Registries.STATUS_EFFECT, Identifier.of(getModId(), name),
                                statusEffect);
        }

        public static <T extends Potion> T registerPotion(String name, T potion) {
                return Registry.register(Registries.POTION, Identifier.of(getModId(), name),
                                potion);
        }

        // Enchantments
        public static <T extends ComponentType<?>> T registerEnchantmentEffectComponentType(
                        String name, T componentType) {
                return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
                                Identifier.of(getModId(), name), componentType);
        }

        // Recipes
        public static <T extends RecipeSerializer<?>> T registerRecipeSerializer(String name,
                        T serializer) {
                return Registry.register(Registries.RECIPE_SERIALIZER,
                                Identifier.of(getModId(), name), serializer);
        }

        public static <T extends RecipeType<?>> T registerRecipeType(String name, T recipeType) {
                return Registry.register(Registries.RECIPE_TYPE, Identifier.of(getModId(), name),
                                recipeType);
        }

        public static <T extends RecipeBookCategory> T registerRecipeBookCategory(String name,
                        T category) {
                return Registry.register(Registries.RECIPE_BOOK_CATEGORY,
                                Identifier.of(getModId(), name), category);
        }

        // FIXED: RecipeDisplay.Serializer<?> is final, use concrete type
        public static RecipeDisplay.Serializer<?> registerRecipeDisplay(String name,
                        RecipeDisplay.Serializer<?> displaySerializer) {
                return Registry.register(Registries.RECIPE_DISPLAY, Identifier.of(getModId(), name),
                                displaySerializer);
        }

        // FIXED: SlotDisplay.Serializer<?> is final, use concrete type
        public static SlotDisplay.Serializer<?> registerSlotDisplay(String name,
                        SlotDisplay.Serializer<?> slotDisplay) {
                return Registry.register(Registries.SLOT_DISPLAY, Identifier.of(getModId(), name),
                                slotDisplay);
        }

        // Screen Handlers
        public static <T extends ScreenHandlerType<?>> T registerScreenHandler(String name,
                        T screenHandler) {
                return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(getModId(), name),
                                screenHandler);
        }

        // Stats
        public static <T extends StatType<?>> T registerStatType(String name, T statType) {
                return Registry.register(Registries.STAT_TYPE, Identifier.of(getModId(), name),
                                statType);
        }

        public static Identifier registerCustomStat(String name, Identifier stat) {
                return Registry.register(Registries.CUSTOM_STAT, Identifier.of(getModId(), name),
                                stat);
        }

        // Loot System - FIXED: All these types are final
        public static LootConditionType registerLootConditionType(String name,
                        LootConditionType conditionType) {
                return Registry.register(Registries.LOOT_CONDITION_TYPE,
                                Identifier.of(getModId(), name), conditionType);
        }

        public static LootFunctionType<?> registerLootFunctionType(String name,
                        LootFunctionType<?> functionType) {
                return Registry.register(Registries.LOOT_FUNCTION_TYPE,
                                Identifier.of(getModId(), name), functionType);
        }

        public static LootPoolEntryType registerLootPoolEntryType(String name,
                        LootPoolEntryType entryType) {
                return Registry.register(Registries.LOOT_POOL_ENTRY_TYPE,
                                Identifier.of(getModId(), name), entryType);
        }

        public static LootNumberProviderType registerLootNumberProviderType(String name,
                        LootNumberProviderType providerType) {
                return Registry.register(Registries.LOOT_NUMBER_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        public static LootNbtProviderType registerLootNbtProviderType(String name,
                        LootNbtProviderType providerType) {
                return Registry.register(Registries.LOOT_NBT_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        public static LootScoreProviderType registerLootScoreProviderType(String name,
                        LootScoreProviderType providerType) {
                return Registry.register(Registries.LOOT_SCORE_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        // World Generation - Features
        public static <T extends Feature<?>> T registerFeature(String name, T feature) {
                return Registry.register(Registries.FEATURE, Identifier.of(getModId(), name),
                                feature);
        }

        public static <T extends FeatureSizeType<?>> T registerFeatureSizeType(String name,
                        T sizeType) {
                return Registry.register(Registries.FEATURE_SIZE_TYPE,
                                Identifier.of(getModId(), name), sizeType);
        }

        public static <T extends PlacementModifierType<?>> T registerPlacementModifierType(
                        String name, T modifierType) {
                return Registry.register(Registries.PLACEMENT_MODIFIER_TYPE,
                                Identifier.of(getModId(), name), modifierType);
        }

        // World Generation - Carvers
        public static <T extends Carver<?>> T registerCarver(String name, T carver) {
                return Registry.register(Registries.CARVER, Identifier.of(getModId(), name),
                                carver);
        }

        // World Generation - Trees
        public static <T extends FoliagePlacerType<?>> T registerFoliagePlacerType(String name,
                        T placerType) {
                return Registry.register(Registries.FOLIAGE_PLACER_TYPE,
                                Identifier.of(getModId(), name), placerType);
        }

        public static <T extends TrunkPlacerType<?>> T registerTrunkPlacerType(String name,
                        T placerType) {
                return Registry.register(Registries.TRUNK_PLACER_TYPE,
                                Identifier.of(getModId(), name), placerType);
        }

        public static <T extends RootPlacerType<?>> T registerRootPlacerType(String name,
                        T placerType) {
                return Registry.register(Registries.ROOT_PLACER_TYPE,
                                Identifier.of(getModId(), name), placerType);
        }

        public static <T extends TreeDecoratorType<?>> T registerTreeDecoratorType(String name,
                        T decoratorType) {
                return Registry.register(Registries.TREE_DECORATOR_TYPE,
                                Identifier.of(getModId(), name), decoratorType);
        }

        // World Generation - Structures
        public static <T extends StructureType<?>> T registerStructureType(String name,
                        T structureType) {
                return Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(getModId(), name),
                                structureType);
        }

        public static <T extends StructurePieceType> T registerStructurePiece(String name,
                        T pieceType) {
                return Registry.register(Registries.STRUCTURE_PIECE,
                                Identifier.of(getModId(), name), pieceType);
        }

        public static <T extends StructurePlacementType<?>> T registerStructurePlacement(
                        String name, T placementType) {
                return Registry.register(Registries.STRUCTURE_PLACEMENT,
                                Identifier.of(getModId(), name), placementType);
        }

        public static <T extends StructurePoolElementType<?>> T registerStructurePoolElement(
                        String name, T elementType) {
                return Registry.register(Registries.STRUCTURE_POOL_ELEMENT,
                                Identifier.of(getModId(), name), elementType);
        }

        public static <T extends StructureProcessorType<?>> T registerStructureProcessor(
                        String name, T processorType) {
                return Registry.register(Registries.STRUCTURE_PROCESSOR,
                                Identifier.of(getModId(), name), processorType);
        }

        public static <T extends RuleTestType<?>> T registerRuleTest(String name, T ruleTest) {
                return Registry.register(Registries.RULE_TEST, Identifier.of(getModId(), name),
                                ruleTest);
        }

        public static <T extends PosRuleTestType<?>> T registerPosRuleTest(String name,
                        T posRuleTest) {
                return Registry.register(Registries.POS_RULE_TEST, Identifier.of(getModId(), name),
                                posRuleTest);
        }

        public static <T extends RuleBlockEntityModifierType<?>> T registerRuleBlockEntityModifier(
                        String name, T modifierType) {
                return Registry.register(Registries.RULE_BLOCK_ENTITY_MODIFIER,
                                Identifier.of(getModId(), name), modifierType);
        }

        // World Generation - Misc
        public static <T extends ChunkStatus> T registerChunkStatus(String name, T chunkStatus) {
                return Registry.register(Registries.CHUNK_STATUS, Identifier.of(getModId(), name),
                                chunkStatus);
        }

        public static <T extends HeightProviderType<?>> T registerHeightProviderType(String name,
                        T providerType) {
                return Registry.register(Registries.HEIGHT_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        // Villagers - FIXED: These types are final
        public static VillagerProfession registerVillagerProfession(String name,
                        VillagerProfession profession) {
                return Registry.register(Registries.VILLAGER_PROFESSION,
                                Identifier.of(getModId(), name), profession);
        }

        public static VillagerType registerVillagerType(String name, VillagerType villagerType) {
                return Registry.register(Registries.VILLAGER_TYPE, Identifier.of(getModId(), name),
                                villagerType);
        }

        public static PointOfInterestType registerPointOfInterestType(String name,
                        PointOfInterestType poiType) {
                return Registry.register(Registries.POINT_OF_INTEREST_TYPE,
                                Identifier.of(getModId(), name), poiType);
        }

        // Commands & Arguments
        public static <T extends ArgumentSerializer<?, ?>> T registerCommandArgumentType(
                        String name, T argumentSerializer) {
                return Registry.register(Registries.COMMAND_ARGUMENT_TYPE,
                                Identifier.of(getModId(), name), argumentSerializer);
        }

        // Criteria
        public static <T extends Criterion<?>> T registerCriterion(String name, T criterion) {
                return Registry.register(Registries.CRITERION, Identifier.of(getModId(), name),
                                criterion);
        }

        // Number Providers
        public static <T extends FloatProviderType<?>> T registerFloatProviderType(String name,
                        T providerType) {
                return Registry.register(Registries.FLOAT_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        public static <T extends IntProviderType<?>> T registerIntProviderType(String name,
                        T providerType) {
                return Registry.register(Registries.INT_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        // Misc - FIXED: These types are final
        public static DecoratedPotPattern registerDecoratedPotPattern(String name,
                        DecoratedPotPattern pattern) {
                return Registry.register(Registries.DECORATED_POT_PATTERN,
                                Identifier.of(getModId(), name), pattern);
        }

        public static MapDecorationType registerMapDecorationType(String name,
                        MapDecorationType decorationType) {
                return Registry.register(Registries.MAP_DECORATION_TYPE,
                                Identifier.of(getModId(), name), decorationType);
        }

        public static <T extends NumberFormatType<?>> T registerNumberFormatType(String name,
                        T formatType) {
                return Registry.register(Registries.NUMBER_FORMAT_TYPE,
                                Identifier.of(getModId(), name), formatType);
        }

        public static <T extends PositionSourceType<?>> T registerPositionSourceType(String name,
                        T sourceType) {
                return Registry.register(Registries.POSITION_SOURCE_TYPE,
                                Identifier.of(getModId(), name), sourceType);
        }

        public static ChunkTicketType registerTicketType(String name, ChunkTicketType ticketType) {
                return Registry.register(Registries.TICKET_TYPE, Identifier.of(getModId(), name),
                                ticketType);
        }


        // Helper method to get mod ID
        private static String getModId() {
                return "mosbergapi";
        }

        /**
         * Initialize all registries. Call this from your mod initializer.
         */
        public static void initialize() {
                // Registry initialization happens automatically when referenced
        }
}
