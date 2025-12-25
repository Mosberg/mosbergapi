package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 * Helper class for working with fluids in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for fluid manipulation, flow checking, and fluid state
 * management in the world.
 *
 * <h2>Fluid System</h2>
 * <p>
 * Minecraft's fluid system consists of:
 * <ul>
 * <li><strong>Still Fluids:</strong> Non-flowing source blocks</li>
 * <li><strong>Flowing Fluids:</strong> Fluids spreading from source blocks</li>
 * <li><strong>Fluid Levels:</strong> 0-8, where 8 is a full source block</li>
 * <li><strong>Fluid Tags:</strong> {@link FluidTags#WATER}, {@link FluidTags#LAVA}</li>
 * </ul>
 *
 * <h2>Custom Fluids</h2>
 * <p>
 * When creating custom fluids, you need both a still and flowing variant:
 * <ul>
 * <li>Still variant - Source blocks that generate fluid</li>
 * <li>Flowing variant - Fluid spreading from sources</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Check if position contains water
 * if (FluidHelper.isWater(world, pos)) {
 *     // Position has water
 *          }
 *
 *          // Get fluid state at position
 *          FluidState state = FluidHelper.getFluidState(world, pos);
 *
 *          // Check if fluid is flowing
 *          if (FluidHelper.isFlowing(state)) {
 *          // Fluid is spreading
 *          }
 *
 *          // Get fluid level
 *          int level = FluidHelper.getLevel(state);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see Fluid
 * @see FluidState
 * @see FlowableFluid
 */
public class FluidHelper {

    /** Maximum fluid level (source block) */
    public static final int MAX_LEVEL = 8;

    /** Minimum fluid level (empty) */
    public static final int MIN_LEVEL = 0;

    private FluidHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Gets the fluid state at a position.
     *
     * @param world The world to check
     * @param pos The position to check
     * @return The fluid state at the position
     * @throws NullPointerException if world or pos is null
     *
     * @example
     *
     *          <pre>{@code
     * FluidState state = FluidHelper.getFluidState(world, blockPos);
     * if (!state.isEmpty()) {
     *          LOGGER.info("Found fluid: {}", state.getFluid());
     *          }
     * }</pre>
     */
    @NotNull
    public static FluidState getFluidState(@NotNull World world, @NotNull BlockPos pos) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (pos == null)
            throw new NullPointerException("Position cannot be null");

        return world.getFluidState(pos);
    }

    /**
     * Checks if a position contains any fluid.
     *
     * @param world The world to check
     * @param pos The position to check
     * @return true if the position contains fluid
     *
     * @example
     *
     *          <pre>{@code
     * if (FluidHelper.hasFluid(world, pos)) {
     *     // Position contains some fluid
     *          }
     * }</pre>
     */
    public static boolean hasFluid(@NotNull World world, @NotNull BlockPos pos) {
        return !getFluidState(world, pos).isEmpty();
    }

    /**
     * Checks if a position contains a specific fluid.
     *
     * @param world The world to check
     * @param pos The position to check
     * @param fluid The fluid to check for
     * @return true if the position contains the specified fluid
     * @throws NullPointerException if world, pos, or fluid is null
     *
     * @example
     *
     *          <pre>{@code
     * if (FluidHelper.hasFluid(world, pos, Fluids.WATER)) {
     *     // Position contains water
     *          }
     * }</pre>
     */
    public static boolean hasFluid(@NotNull World world, @NotNull BlockPos pos,
            @NotNull Fluid fluid) {
        if (fluid == null)
            throw new NullPointerException("Fluid cannot be null");
        return getFluidState(world, pos).isOf(fluid);
    }

    /**
     * Checks if a position contains water.
     *
     * @param world The world to check
     * @param pos The position to check
     * @return true if the position contains water
     *
     * @example
     *
     *          <pre>{@code
     * if (FluidHelper.isWater(world, pos)) {
     *     entity.extinguish();
     *          }
     * }</pre>
     */
    public static boolean isWater(@NotNull World world, @NotNull BlockPos pos) {
        return getFluidState(world, pos).isIn(FluidTags.WATER);
    }

    /**
     * Checks if a position contains lava.
     *
     * @param world The world to check
     * @param pos The position to check
     * @return true if the position contains lava
     *
     * @example
     *
     *          <pre>{@code
     * if (FluidHelper.isLava(world, pos)) {
     *     entity.setOnFireFor(5);
     *          }
     * }</pre>
     */
    public static boolean isLava(@NotNull World world, @NotNull BlockPos pos) {
        return getFluidState(world, pos).isIn(FluidTags.LAVA);
    }

    /**
     * Gets the fluid level at a position.
     *
     * <p>
     * Fluid levels range from 0 (empty) to 8 (full source block).
     *
     * @param world The world to check
     * @param pos The position to check
     * @return The fluid level (0-8)
     *
     * @example
     *
     *          <pre>{@code
     * int level = FluidHelper.getLevel(world, pos);
     * if (level == 8) {
     *     LOGGER.info("Found a source block");
     *          }
     * }</pre>
     */
    public static int getLevel(@NotNull World world, @NotNull BlockPos pos) {
        return getFluidState(world, pos).getLevel();
    }

    /**
     * Gets the fluid level from a fluid state.
     *
     * @param state The fluid state
     * @return The fluid level (0-8)
     * @throws NullPointerException if state is null
     *
     * @example
     *
     *          <pre>{@code
     * FluidState state = world.getFluidState(pos);
     * int level = FluidHelper.getLevel(state);
     * }</pre>
     */
    public static int getLevel(@NotNull FluidState state) {
        if (state == null)
            throw new NullPointerException("FluidState cannot be null");
        return state.getLevel();
    }

    /**
     * Checks if a fluid state is a source block (full level).
     *
     * @param state The fluid state to check
     * @return true if the fluid is a source block
     * @throws NullPointerException if state is null
     *
     * @example
     *
     *          <pre>{@code
     * FluidState state = world.getFluidState(pos);
     * if (FluidHelper.isSource(state)) {
     *     // This is a source block
     *          }
     * }</pre>
     */
    public static boolean isSource(@NotNull FluidState state) {
        if (state == null)
            throw new NullPointerException("FluidState cannot be null");
        return state.isStill();
    }

    /**
     * Checks if a position contains a fluid source block.
     *
     * @param world The world to check
     * @param pos The position to check
     * @return true if the position contains a fluid source
     *
     * @example
     *
     *          <pre>{@code
     * if (FluidHelper.isSource(world, pos)) {
     *     // Position has a source block
     *          }
     * }</pre>
     */
    public static boolean isSource(@NotNull World world, @NotNull BlockPos pos) {
        return isSource(getFluidState(world, pos));
    }

    /**
     * Checks if a fluid state is flowing (not a source).
     *
     * @param state The fluid state to check
     * @return true if the fluid is flowing
     * @throws NullPointerException if state is null
     *
     * @example
     *
     *          <pre>{@code
     * if (FluidHelper.isFlowing(state)) {
     *     // Fluid is spreading from a source
     *          }
     * }</pre>
     */
    public static boolean isFlowing(@NotNull FluidState state) {
        if (state == null)
            throw new NullPointerException("FluidState cannot be null");
        return !state.isEmpty() && !state.isStill();
    }

    /**
     * Gets the height of fluid at a position.
     *
     * <p>
     * Returns a value between 0.0 and 1.0 representing the visual height of the fluid.
     *
     * @param world The world to check
     * @param pos The position to check
     * @return The fluid height (0.0 to 1.0)
     *
     * @example
     *
     *          <pre>{@code
     * float height = FluidHelper.getHeight(world, pos);
     *          LOGGER.info("Fluid fills {}% of the block", height * 100);
     * }</pre>
     */
    public static float getHeight(@NotNull World world, @NotNull BlockPos pos) {
        FluidState state = getFluidState(world, pos);
        return state.getHeight(world, pos);
    }

    /**
     * Checks if fluid can flow in a specific direction from a position.
     *
     * @param world The world to check
     * @param pos The source position
     * @param direction The direction to check
     * @return true if fluid can flow in that direction
     * @throws NullPointerException if world, pos, or direction is null
     *
     * @example
     *
     *          <pre>{@code
     * if (FluidHelper.canFlowTo(world, pos, Direction.DOWN)) {
     *     // Fluid can flow downward from this position
     *          }
     * }</pre>
     */
    public static boolean canFlowTo(@NotNull World world, @NotNull BlockPos pos,
            @NotNull Direction direction) {
        if (world == null)
            throw new NullPointerException("World cannot be null");
        if (pos == null)
            throw new NullPointerException("Position cannot be null");
        if (direction == null)
            throw new NullPointerException("Direction cannot be null");

        FluidState state = getFluidState(world, pos);
        if (state.isEmpty())
            return false;

        BlockPos targetPos = pos.offset(direction);
        BlockState targetState = world.getBlockState(targetPos);
        FluidState targetFluidState = world.getFluidState(targetPos);

        // Check if target position can accept the fluid
        // Either it's empty/air, or it already contains the same fluid at a lower level
        if (targetState.isAir() || targetFluidState.isEmpty()) {
            return true;
        }

        // Check if target has same fluid at lower level
        if (targetFluidState.isOf(state.getFluid())) {
            return targetFluidState.getLevel() < state.getLevel();
        }

        return false;
    }

    /**
     * Gets a fluid by its identifier.
     *
     * @param id The identifier of the fluid
     * @return The fluid, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * Fluid fluid = FluidHelper.getFluid("minecraft:water");
     * if (fluid != null) {
     *     // Use the fluid
     *          }
     * }</pre>
     */
    @Nullable
    public static Fluid getFluid(@NotNull String id) {
        return getFluid(Identifier.tryParse(id));
    }

    /**
     * Gets a fluid by its identifier.
     *
     * @param id The identifier of the fluid
     * @return The fluid, or null if not found
     */
    @Nullable
    public static Fluid getFluid(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.FLUID.get(id);
    }

    /**
     * Checks if a fluid exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the fluid is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (FluidHelper.hasFluidType("mosbergapi:acid")) {
     *     LOGGER.info("Acid fluid is registered!");
     *          }
     * }</pre>
     */
    public static boolean hasFluidType(@NotNull String id) {
        return hasFluidType(Identifier.tryParse(id));
    }

    /**
     * Checks if a fluid exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the fluid is registered
     */
    public static boolean hasFluidType(@Nullable Identifier id) {
        return id != null && Registries.FLUID.containsId(id);
    }

    /**
     * Gets the identifier of a fluid.
     *
     * @param fluid The fluid
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = FluidHelper.getId(Fluids.WATER);
     * // Returns: minecraft:water
     * }</pre>
     */
    @Nullable
    public static Identifier getId(@NotNull Fluid fluid) {
        if (fluid == null)
            throw new NullPointerException("Fluid cannot be null");
        return Registries.FLUID.getId(fluid);
    }

    /**
     * Checks if a fluid is flowable (can spread).
     *
     * @param fluid The fluid to check
     * @return true if the fluid can flow
     *
     * @example
     *
     *          <pre>{@code
     * if (FluidHelper.isFlowable(fluid)) {
     *     // This fluid can spread
     *          }
     * }</pre>
     */
    public static boolean isFlowable(@NotNull Fluid fluid) {
        if (fluid == null)
            throw new NullPointerException("Fluid cannot be null");
        return fluid instanceof FlowableFluid;
    }

    /**
     * Gets the still (source) variant of a flowable fluid.
     *
     * @param fluid The fluid to get the still variant of
     * @return The still fluid, or the input fluid if not flowable
     *
     * @example
     *
     *          <pre>{@code
     * Fluid still = FluidHelper.getStill(Fluids.FLOWING_WATER);
     * // Returns: Fluids.WATER
     * }</pre>
     */
    @NotNull
    public static Fluid getStill(@NotNull Fluid fluid) {
        if (fluid == null)
            throw new NullPointerException("Fluid cannot be null");

        if (fluid instanceof FlowableFluid flowable) {
            return flowable.getStill();
        }
        return fluid;
    }

    /**
     * Gets the flowing variant of a flowable fluid.
     *
     * @param fluid The fluid to get the flowing variant of
     * @return The flowing fluid, or the input fluid if not flowable
     *
     * @example
     *
     *          <pre>{@code
     * Fluid flowing = FluidHelper.getFlowing(Fluids.WATER);
     * // Returns: Fluids.FLOWING_WATER
     * }</pre>
     */
    @NotNull
    public static Fluid getFlowing(@NotNull Fluid fluid) {
        if (fluid == null)
            throw new NullPointerException("Fluid cannot be null");

        if (fluid instanceof FlowableFluid flowable) {
            return flowable.getFlowing();
        }
        return fluid;
    }

    /**
     * Gets all registered fluids.
     *
     * @return An iterable of all fluids
     *
     * @example
     *
     *          <pre>{@code
     * for (Fluid fluid : FluidHelper.getAllFluids()) {
     *          LOGGER.info("Fluid: {}", FluidHelper.getId(fluid));
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<Fluid> getAllFluids() {
        return Registries.FLUID;
    }

    /**
     * Gets the number of registered fluids.
     *
     * @return The count of fluids
     *
     * @example
     *
     *          <pre>{@code
     * int count = FluidHelper.getFluidCount();
     *          LOGGER.info("Total fluids registered: {}", count);
     * }</pre>
     */
    public static int getFluidCount() {
        return Registries.FLUID.size();
    }

    /**
     * Checks if a fluid is empty (no fluid present).
     *
     * @param state The fluid state to check
     * @return true if there is no fluid
     * @throws NullPointerException if state is null
     *
     * @example
     *
     *          <pre>{@code
     * if (FluidHelper.isEmpty(state)) {
     *     // No fluid at this position
     *          }
     * }</pre>
     */
    public static boolean isEmpty(@NotNull FluidState state) {
        if (state == null)
            throw new NullPointerException("FluidState cannot be null");
        return state.isEmpty();
    }

    /**
     * Gets the fluid from a fluid state.
     *
     * @param state The fluid state
     * @return The fluid
     * @throws NullPointerException if state is null
     *
     * @example
     *
     *          <pre>{@code
     * FluidState state = world.getFluidState(pos);
     * Fluid fluid = FluidHelper.getFluid(state);
     *          LOGGER.info("Fluid type: {}", FluidHelper.getId(fluid));
     * }</pre>
     */
    @NotNull
    public static Fluid getFluid(@NotNull FluidState state) {
        if (state == null)
            throw new NullPointerException("FluidState cannot be null");
        return state.getFluid();
    }
}
