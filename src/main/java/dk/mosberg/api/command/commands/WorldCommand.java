package dk.mosberg.api.command.commands;

import org.jetbrains.annotations.NotNull;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dk.mosberg.api.command.MosbergCommand;
import dk.mosberg.api.util.WorldHelper;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

/**
 * Command for world utilities and information.
 *
 * <h2>Usage</h2>
 * <ul>
 * <li>{@code /mosbergapi world info} - Get world information</li>
 * <li>{@code /mosbergapi world time <set|add> <value>} - Manage world time</li>
 * <li>{@code /mosbergapi world weather <clear|rain|thunder>} - Set weather</li>
 * <li>{@code /mosbergapi world difficulty <peaceful|easy|normal|hard>} - Set difficulty</li>
 * <li>{@code /mosbergapi world seed} - Get world seed</li>
 * </ul>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class WorldCommand extends MosbergCommand {

    @Override
    @NotNull
    public String getName() {
        return "world";
    }

    @Override
    public void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
            @NotNull CommandRegistryAccess registryAccess,
            @NotNull CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("mosbergapi")
                .requires(source -> hasPermission(source))
                .then(CommandManager.literal("world")
                        .then(CommandManager.literal("info").executes(this::info))
                        .then(CommandManager.literal("seed").executes(this::seed))
                        .then(CommandManager.literal("dimension").executes(this::dimension))));
    }

    private int info(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();

        sendInfo(context, "World Information:");
        sendInfo(context, "  Dimension: " + world.getRegistryKey().getValue());
        sendInfo(context, "  Time: " + world.getTimeOfDay());
        sendInfo(context, "  Day Time: " + WorldHelper.getTimeOfDay(world));
        sendInfo(context, "  Weather: "
                + (world.isRaining() ? (world.isThundering() ? "Thunder" : "Rain") : "Clear"));
        sendInfo(context, "  Difficulty: " + world.getDifficulty().getName());
        sendInfo(context, "  Spawn: " + world.getSpawnPoint());
        sendInfo(context, "  Loaded Chunks: " + world.getChunkManager().getLoadedChunkCount());

        return 1;
    }

    private int seed(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();
        long seed = world.getSeed();

        sendInfo(context, "World Seed: " + seed);
        return 1;
    }

    private int dimension(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();
        Vec3d pos = context.getSource().getPosition();

        sendInfo(context, "Current Dimension: " + world.getRegistryKey().getValue());
        sendInfo(context, "Position: " + String.format("%.2f, %.2f, %.2f", pos.x, pos.y, pos.z));
        sendInfo(context, "Biome: "
                + world.getBiome(context.getSource().getPlayer().getBlockPos()).getIdAsString());

        return 1;
    }
}
