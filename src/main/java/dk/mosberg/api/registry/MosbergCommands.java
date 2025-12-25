package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/**
 * Registry class for all custom commands in MosbergAPI. Register your commands here using the
 * provided helper methods.
 *
 * @example
 *
 *          <pre>{@code
 * public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
 *                             CommandRegistryAccess registryAccess,
 *                             CommandManager.RegistrationEnvironment environment) {
 *     // Simple command
 *     MosbergCommands.register(dispatcher, "test", 2, ctx -> {
 *         ctx.getSource().sendFeedback(() -> Text.literal("Test command executed!"), false);
 *         return 1;
 *          });
 *
 *          // Command with arguments
 *          dispatcher.register(CommandManager.literal("give_custom")
 *          .requires(source -> source.hasPermissionLevel(2)).then(CommandManager
 *          .argument("amount", IntegerArgumentType.integer(1, 64)).executes(ctx -> {
 *          int amount = IntegerArgumentType.getInteger(ctx, "amount");
 *          // Give item logic
 *          return 1;
 *          })));
 *          }
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergCommands {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergCommands.class);

    /**
     * Registers a simple command with a permission level.
     *
     * @param dispatcher The command dispatcher
     * @param name The command name
     * @param permissionLevel The required permission level (0-4)
     * @param executor The command executor
     *
     * @example
     *
     *          <pre>{@code
     * MosbergCommands.register(dispatcher, "reload_config", 3, ctx -> {
     *     ConfigManager.loadConfig("config.json", Config.class, new Config());
     *     ctx.getSource().sendFeedback(() -> Text.literal("Config reloaded!"), true);
     *     return 1;
     *          });
     * }</pre>
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, String name,
            int permissionLevel, com.mojang.brigadier.Command<ServerCommandSource> executor) {
        LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal(name)
                .requires(source -> source.hasPermissionLevel(permissionLevel)).executes(executor);

        dispatcher.register(command);
        LOGGER.debug("Registered command: {} (permission level: {})", name, permissionLevel);
    }

    /**
     * Registers a command that requires operator permissions (level 2).
     *
     * @param dispatcher The command dispatcher
     * @param name The command name
     * @param executor The command executor
     *
     * @example
     *
     *          <pre>{@code
     * MosbergCommands.registerOp(dispatcher, "debug", ctx -> {
     *     ctx.getSource().sendFeedback(() -> Text.literal("Debug info..."), false);
     *     return 1;
     *          });
     * }</pre>
     */
    public static void registerOp(CommandDispatcher<ServerCommandSource> dispatcher, String name,
            com.mojang.brigadier.Command<ServerCommandSource> executor) {
        register(dispatcher, name, 2, executor);
    }

    /**
     * Registers a command available to all players.
     *
     * @param dispatcher The command dispatcher
     * @param name The command name
     * @param executor The command executor
     *
     * @example
     *
     *          <pre>{@code
     * MosbergCommands.registerPublic(dispatcher, "info", ctx -> {
     *     ctx.getSource().sendFeedback(() -> Text.literal("MosbergAPI v1.0.0"), false);
     *     return 1;
     *          });
     * }</pre>
     */
    public static void registerPublic(CommandDispatcher<ServerCommandSource> dispatcher,
            String name, com.mojang.brigadier.Command<ServerCommandSource> executor) {
        register(dispatcher, name, 0, executor);
    }

    /**
     * Creates a success feedback text with the MosbergAPI prefix.
     *
     * @param message The success message
     * @return The formatted text
     */
    public static Text successText(String message) {
        return Text.literal("§a[Success]§r " + message);
    }

    /**
     * Creates an error feedback text with the MosbergAPI prefix.
     *
     * @param message The error message
     * @return The formatted text
     */
    public static Text errorText(String message) {
        return Text.literal("§c[Error]§r " + message);
    }

    /**
     * Creates an info feedback text with the MosbergAPI prefix.
     *
     * @param message The info message
     * @return The formatted text
     */
    public static Text infoText(String message) {
        return Text.literal("§6[Info]§r " + message);
    }

    /**
     * Initialize and register all commands. This should be called from CommandRegistrationCallback.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI commands");
        // Commands are registered via CommandRegistrationCallback
    }
}
