package dk.mosberg.api.command.commands;

import org.jetbrains.annotations.NotNull;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dk.mosberg.api.command.MosbergCommand;
import dk.mosberg.api.util.MosbergHelper;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

/**
 * Command for block utilities and manipulation.
 *
 * <h2>Usage</h2>
 * <ul>
 * <li>{@code /mosbergapi block set <x> <y> <z> <block>} - Set a block</li>
 * <li>{@code /mosbergapi block get <x> <y> <z>} - Get block information</li>
 * <li>{@code /mosbergapi block fill <x1> <y1> <z1> <x2> <y2> <z2> <block>} - Fill area with
 * blocks</li>
 * <li>{@code /mosbergapi block break <x> <y> <z>} - Break a block</li>
 * </ul>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class BlockCommand extends MosbergCommand {

        @Override
        @NotNull
        public String getName() {
                return "block";
        }

        @Override
        public void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
                        @NotNull CommandRegistryAccess registryAccess,
                        @NotNull CommandManager.RegistrationEnvironment environment) {
                dispatcher.register(CommandManager.literal("mosbergapi")
                                .requires(source -> hasPermission(source))
                                .then(CommandManager.literal("block").then(CommandManager
                                                .literal("set")
                                                .then(CommandManager
                                                                .argument("x", IntegerArgumentType
                                                                                .integer())
                                                                .then(CommandManager.argument("y",
                                                                                IntegerArgumentType
                                                                                                .integer())
                                                                                .then(CommandManager
                                                                                                .argument("z", IntegerArgumentType
                                                                                                                .integer())
                                                                                                .then(CommandManager
                                                                                                                .argument("block",
                                                                                                                                BlockStateArgumentType
                                                                                                                                                .blockState(registryAccess))
                                                                                                                .executes(this::setBlock))))))
                                                .then(CommandManager.literal("get")
                                                                .then(CommandManager.argument("x",
                                                                                IntegerArgumentType
                                                                                                .integer())
                                                                                .then(CommandManager
                                                                                                .argument("y", IntegerArgumentType
                                                                                                                .integer())
                                                                                                .then(CommandManager
                                                                                                                .argument("z", IntegerArgumentType
                                                                                                                                .integer())
                                                                                                                .executes(this::getBlock)))))
                                                .then(CommandManager.literal("fill")
                                                                .then(CommandManager.argument("x1",
                                                                                IntegerArgumentType
                                                                                                .integer())
                                                                                .then(CommandManager
                                                                                                .argument("y1", IntegerArgumentType
                                                                                                                .integer())
                                                                                                .then(CommandManager
                                                                                                                .argument("z1", IntegerArgumentType
                                                                                                                                .integer())
                                                                                                                .then(CommandManager
                                                                                                                                .argument("x2", IntegerArgumentType
                                                                                                                                                .integer())
                                                                                                                                .then(CommandManager
                                                                                                                                                .argument("y2", IntegerArgumentType
                                                                                                                                                                .integer())
                                                                                                                                                .then(CommandManager
                                                                                                                                                                .argument("z2", IntegerArgumentType
                                                                                                                                                                                .integer())
                                                                                                                                                                .then(CommandManager
                                                                                                                                                                                .argument("block",
                                                                                                                                                                                                BlockStateArgumentType
                                                                                                                                                                                                                .blockState(registryAccess))
                                                                                                                                                                                .executes(this::fill)))))))))
                                                .then(CommandManager.literal("break")
                                                                .then(CommandManager.argument("x",
                                                                                IntegerArgumentType
                                                                                                .integer())
                                                                                .then(CommandManager
                                                                                                .argument("y", IntegerArgumentType
                                                                                                                .integer())
                                                                                                .then(CommandManager
                                                                                                                .argument("z", IntegerArgumentType
                                                                                                                                .integer())
                                                                                                                .executes(this::breakBlock)))))));
        }

        private int setBlock(CommandContext<ServerCommandSource> context) {
                try {
                        ServerWorld world = context.getSource().getWorld();
                        int x = IntegerArgumentType.getInteger(context, "x");
                        int y = IntegerArgumentType.getInteger(context, "y");
                        int z = IntegerArgumentType.getInteger(context, "z");
                        BlockState state = BlockStateArgumentType.getBlockState(context, "block")
                                        .getBlockState();

                        BlockPos pos = new BlockPos(x, y, z);
                        MosbergHelper.BLOCK.setBlockState(world, pos, state);

                        sendSuccess(context, "Set block at " + x + ", " + y + ", " + z + " to "
                                        + state.getBlock().getName().getString());
                        return 1;
                } catch (Exception e) {
                        sendError(context, "Failed to set block: " + e.getMessage());
                        return 0;
                }
        }

        private int getBlock(CommandContext<ServerCommandSource> context) {
                try {
                        ServerWorld world = context.getSource().getWorld();
                        int x = IntegerArgumentType.getInteger(context, "x");
                        int y = IntegerArgumentType.getInteger(context, "y");
                        int z = IntegerArgumentType.getInteger(context, "z");

                        BlockPos pos = new BlockPos(x, y, z);
                        BlockState state = MosbergHelper.BLOCK.getBlockState(world, pos);

                        sendInfo(context, "Block at " + x + ", " + y + ", " + z + ":");
                        sendInfo(context, "  Type: " + state.getBlock().getName().getString());
                        sendInfo(context, "  Hardness: " + state.getHardness(world, pos));
                        sendInfo(context, "  Light Level: " + state.getLuminance());

                        return 1;
                } catch (Exception e) {
                        sendError(context, "Failed to get block: " + e.getMessage());
                        return 0;
                }
        }

        private int fill(CommandContext<ServerCommandSource> context) {
                try {
                        ServerWorld world = context.getSource().getWorld();
                        int x1 = IntegerArgumentType.getInteger(context, "x1");
                        int y1 = IntegerArgumentType.getInteger(context, "y1");
                        int z1 = IntegerArgumentType.getInteger(context, "z1");
                        int x2 = IntegerArgumentType.getInteger(context, "x2");
                        int y2 = IntegerArgumentType.getInteger(context, "y2");
                        int z2 = IntegerArgumentType.getInteger(context, "z2");
                        BlockState state = BlockStateArgumentType.getBlockState(context, "block")
                                        .getBlockState();

                        BlockPos pos1 = new BlockPos(Math.min(x1, x2), Math.min(y1, y2),
                                        Math.min(z1, z2));
                        BlockPos pos2 = new BlockPos(Math.max(x1, x2), Math.max(y1, y2),
                                        Math.max(z1, z2));

                        int count = 0;
                        for (BlockPos pos : BlockPos.iterate(pos1, pos2)) {
                                MosbergHelper.BLOCK.setBlockState(world, pos, state);
                                count++;
                        }

                        sendSuccess(context, "Filled " + count + " blocks with "
                                        + state.getBlock().getName().getString());
                        return count;
                } catch (Exception e) {
                        sendError(context, "Failed to fill blocks: " + e.getMessage());
                        return 0;
                }
        }

        private int breakBlock(CommandContext<ServerCommandSource> context) {
                try {
                        ServerWorld world = context.getSource().getWorld();
                        int x = IntegerArgumentType.getInteger(context, "x");
                        int y = IntegerArgumentType.getInteger(context, "y");
                        int z = IntegerArgumentType.getInteger(context, "z");

                        BlockPos pos = new BlockPos(x, y, z);
                        MosbergHelper.BLOCK.breakBlock(world, pos, true);

                        sendSuccess(context, "Broke block at " + x + ", " + y + ", " + z);
                        return 1;
                } catch (Exception e) {
                        sendError(context, "Failed to break block: " + e.getMessage());
                        return 0;
                }
        }
}
