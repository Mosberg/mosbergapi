package dk.mosberg.api.command.commands;

import org.jetbrains.annotations.NotNull;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dk.mosberg.api.command.MosbergCommand;
import dk.mosberg.api.config.ConfigManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

/**
 * Command for debugging and development utilities.
 *
 * <h2>Usage</h2>
 * <ul>
 * <li>{@code /mosbergapi debug toggle} - Toggle debug mode</li>
 * <li>{@code /mosbergapi debug memory} - Show memory usage</li>
 * <li>{@code /mosbergapi debug gc} - Run garbage collection</li>
 * <li>{@code /mosbergapi debug info} - Show system information</li>
 * </ul>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class DebugCommand extends MosbergCommand {

    @Override
    @NotNull
    public String getName() {
        return "debug";
    }

    @Override
    public int getPermissionLevel() {
        return 3; // Higher permission for debug commands
    }

    @Override
    public void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
            @NotNull CommandRegistryAccess registryAccess,
            @NotNull CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("mosbergapi").requires(source -> hasPermission(source))
                        .then(CommandManager.literal("debug")
                                .then(CommandManager.literal("toggle").executes(this::toggle))
                                .then(CommandManager.literal("memory").executes(this::memory))
                                .then(CommandManager.literal("gc").executes(this::gc))
                                .then(CommandManager.literal("info").executes(this::info))));
    }

    private int toggle(CommandContext<ServerCommandSource> context) {
        boolean current = ConfigManager.getBoolean("mosbergapi", "debug_mode");
        ConfigManager.set("mosbergapi", "debug_mode", !current);
        ConfigManager.save("mosbergapi", "main");

        sendSuccess(context, "Debug mode: " + (!current ? "ON" : "OFF"));
        return 1;
    }

    private int memory(CommandContext<ServerCommandSource> context) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = totalMemory - freeMemory;

        sendInfo(context, "Memory Usage:");
        sendInfo(context, "  Used: " + usedMemory + " MB");
        sendInfo(context, "  Free: " + freeMemory + " MB");
        sendInfo(context, "  Total: " + totalMemory + " MB");
        sendInfo(context, "  Max: " + maxMemory + " MB");
        sendInfo(context, "  Usage: " + (usedMemory * 100 / maxMemory) + "%");

        return 1;
    }

    private int gc(CommandContext<ServerCommandSource> context) {
        sendInfo(context, "Running garbage collection...");
        long before = Runtime.getRuntime().freeMemory();
        System.gc();
        long after = Runtime.getRuntime().freeMemory();
        long freed = (after - before) / 1024 / 1024;

        sendSuccess(context, "Freed " + freed + " MB of memory");
        return 1;
    }

    private int info(CommandContext<ServerCommandSource> context) {
        sendInfo(context, "System Information:");
        sendInfo(context, "  Java Version: " + System.getProperty("java.version"));
        sendInfo(context, "  OS: " + System.getProperty("os.name"));
        sendInfo(context, "  Processors: " + Runtime.getRuntime().availableProcessors());
        sendInfo(context, "  Debug Mode: " + ConfigManager.getBoolean("mosbergapi", "debug_mode"));

        return 1;
    }
}
