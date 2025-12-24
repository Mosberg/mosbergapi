package dk.mosberg.api.util;

import org.jetbrains.annotations.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

/**
 * Utility class providing helper methods for Block operations
 */
public class BlockHelper {

    /**
     * Safely gets a BlockState at the specified position
     */
    public BlockState getBlockState(World world, BlockPos pos) {
        return world.getBlockState(pos);
    }

    /**
     * Sets a BlockState at the specified position with default update flags
     */
    public void setBlockState(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
    }

    /**
     * Sets a BlockState with custom update flags
     */
    public void setBlockState(World world, BlockPos pos, BlockState state, int flags) {
        world.setBlockState(pos, state, flags);
    }

    /**
     * Checks if a block at the position is air
     */
    public boolean isAir(World world, BlockPos pos) {
        return world.getBlockState(pos).isAir();
    }

    /**
     * Gets the Block at the specified position
     */
    public Block getBlock(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock();
    }

    /**
     * Checks if two positions have the same block type
     */
    public boolean isSameBlock(World world, BlockPos pos1, BlockPos pos2) {
        return world.getBlockState(pos1).getBlock() == world.getBlockState(pos2).getBlock();
    }

    /**
     * Gets the BlockEntity at the specified position
     */
    @Nullable
    public BlockEntity getBlockEntity(World world, BlockPos pos) {
        return world.getBlockEntity(pos);
    }

    /**
     * Gets the BlockEntity at the specified position with type checking
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> T getBlockEntity(World world, BlockPos pos, Class<T> type) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (type.isInstance(blockEntity)) {
            return (T) blockEntity;
        }
        return null;
    }

    /**
     * Breaks a block at the specified position with drops
     */
    public void breakBlock(World world, BlockPos pos, boolean drops) {
        world.breakBlock(pos, drops);
    }

    /**
     * Gets a neighboring BlockState in the specified direction
     */
    public BlockState getNeighborBlockState(World world, BlockPos pos, Direction direction) {
        return world.getBlockState(pos.offset(direction));
    }

    /**
     * Checks if a block can be replaced at the position
     */
    public boolean isReplaceable(World world, BlockPos pos) {
        return world.getBlockState(pos).isReplaceable();
    }

    /**
     * Gets the light level at the block position
     */
    public int getLightLevel(World world, BlockPos pos) {
        return world.getLightLevel(pos);
    }

    /**
     * Schedules a block tick
     */
    public void scheduleBlockTick(WorldAccess world, BlockPos pos, Block block, int delay) {
        world.scheduleBlockTick(pos, block, delay);
    }

    /**
     * Updates neighboring blocks
     */
    public void updateNeighbors(World world, BlockPos pos, Block block) {
        world.updateNeighbors(pos, block);
    }

    /**
     * Checks if a position is within world bounds
     */
    public boolean isInBounds(World world, BlockPos pos) {
        return world.isInBuildLimit(pos);
    }

    /**
     * Gets the hardness of a block at the position
     */
    public float getHardness(World world, BlockPos pos) {
        return world.getBlockState(pos).getHardness(world, pos);
    }
}
