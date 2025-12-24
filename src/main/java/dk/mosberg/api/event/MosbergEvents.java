package dk.mosberg.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Custom event system for MosbergAPI
 */
public class MosbergEvents {

    public static final Event<OnBlockMined> BLOCK_MINED = EventFactory
            .createArrayBacked(OnBlockMined.class, callbacks -> (player, world, pos, state) -> {
                for (OnBlockMined callback : callbacks) {
                    callback.onBlockMined(player, world, pos, state);
                }
            });

    @FunctionalInterface
    public interface OnBlockMined {
        void onBlockMined(PlayerEntity player, World world, BlockPos pos, BlockState state);
    }
}
