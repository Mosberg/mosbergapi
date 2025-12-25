package dk.mosberg.api.command;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/**
 * Base class for MosbergAPI commands.
 *
 * <p>
 * Provides common functionality for all commands including permission checking, error handling, and
 * message formatting.
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class MosbergCommand {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Gets the command name.
     *
     * @return The command name
     */
    @NotNull
    public abstract String getName();

    /**
     * Gets the minimum permission level required to execute this command.
     *
     * @return The permission level (0-4)
     */
    public int getPermissionLevel() {
        return 2;
    }

    /**
     * Registers the command with the dispatcher.
     *
     * @param dispatcher The command dispatcher
     * @param registryAccess The command registry access
     * @param environment The registration environment
     */
    public abstract void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
            @NotNull CommandRegistryAccess registryAccess,
            @NotNull CommandManager.RegistrationEnvironment environment);

    /**
     * Checks if the source has permission to execute this command.
     *
     * @param source The command source
     * @return true if the source has permission
     */
    protected boolean hasPermission(@NotNull ServerCommandSource source) {
        return source.hasPermissionLevel(getPermissionLevel());
    }

    /**
     * Sends a success message to the command source.
     *
     * @param context The command context
     * @param message The message to send
     */
    protected void sendSuccess(@NotNull CommandContext<ServerCommandSource> context,
            @NotNull String message) {
        context.getSource().sendFeedback(() -> Text.literal("§a[MosbergAPI] " + message), false);
    }

    /**
     * Sends an error message to the command source.
     *
     * @param context The command context
     * @param message The error message to send
     */
    protected void sendError(@NotNull CommandContext<ServerCommandSource> context,
            @NotNull String message) {
        context.getSource().sendError(Text.literal("§c[MosbergAPI] " + message));
    }

    /**
     * Sends an info message to the command source.
     *
     * @param context The command context
     * @param message The info message to send
     */
    protected void sendInfo(@NotNull CommandContext<ServerCommandSource> context,
            @NotNull String message) {
        context.getSource().sendFeedback(() -> Text.literal("§e[MosbergAPI] " + message), false);
    }
}
