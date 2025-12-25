package dk.mosberg.api.command.commands;

import org.jetbrains.annotations.NotNull;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dk.mosberg.api.command.MosbergCommand;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

/**
 * Command for displaying help information.
 *
 * <h2>Usage</h2>
 * <ul>
 * <li>{@code /mosbergapi help} - Show general help</li>
 * <li>{@code /mosbergapi help <command>} - Show specific command help</li>
 * </ul>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class HelpCommand extends MosbergCommand {

    @Override
    @NotNull
    public String getName() {
        return "help";
    }

    @Override
    public int getPermissionLevel() {
        return 0; // Everyone can access help
    }

    @Override
    public void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
            @NotNull CommandRegistryAccess registryAccess,
            @NotNull CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("mosbergapi").executes(this::help)
                .then(CommandManager.literal("help").executes(this::help)));
    }

    private int help(CommandContext<ServerCommandSource> context) {
        sendInfo(context, "§6=== MosbergAPI Commands ===");
        sendInfo(context, "§e/mosbergapi config §7- Manage configurations");
        sendInfo(context, "§e/mosbergapi registry §7- Inspect registries");
        sendInfo(context, "§e/mosbergapi entity §7- Entity utilities");
        sendInfo(context, "§e/mosbergapi item §7- Item utilities");
        sendInfo(context, "§e/mosbergapi block §7- Block utilities");
        sendInfo(context, "§e/mosbergapi world §7- World utilities");
        sendInfo(context, "§e/mosbergapi debug §7- Debug tools");
        sendInfo(context, "§7Use §e/mosbergapi help <command> §7for more info");

        return 1;
    }
}
