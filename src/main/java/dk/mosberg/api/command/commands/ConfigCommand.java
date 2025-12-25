package dk.mosberg.api.command.commands;

import org.jetbrains.annotations.NotNull;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dk.mosberg.api.command.MosbergCommand;
import dk.mosberg.api.config.ConfigManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

/**
 * Command for managing MosbergAPI configurations.
 *
 * <h2>Usage</h2>
 * <ul>
 * <li>{@code /mosbergapi config reload [config]} - Reload configuration(s)</li>
 * <li>{@code /mosbergapi config save [config]} - Save configuration(s)</li>
 * <li>{@code /mosbergapi config reset <config>} - Reset configuration to defaults</li>
 * <li>{@code /mosbergapi config get <key>} - Get configuration value</li>
 * <li>{@code /mosbergapi config set <key> <value>} - Set configuration value</li>
 * <li>{@code /mosbergapi config list} - List all configurations</li>
 * </ul>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigCommand extends MosbergCommand {

    @Override
    @NotNull
    public String getName() {
        return "config";
    }

    @Override
    public void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
            @NotNull CommandRegistryAccess registryAccess,
            @NotNull CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("mosbergapi").requires(source -> hasPermission(source))
                        .then(CommandManager.literal("config")
                                .then(CommandManager.literal("reload").executes(this::reloadAll)
                                        .then(CommandManager
                                                .argument("config", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    builder.suggest("main");
                                                    builder.suggest("registry");
                                                    builder.suggest("world");
                                                    builder.suggest("entities");
                                                    builder.suggest("items");
                                                    builder.suggest("blocks");
                                                    builder.suggest("commands");
                                                    return builder.buildFuture();
                                                }).executes(this::reload)))
                                .then(CommandManager.literal("save").executes(this::saveAll)
                                        .then(CommandManager
                                                .argument("config", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    builder.suggest("main");
                                                    builder.suggest("registry");
                                                    builder.suggest("world");
                                                    builder.suggest("entities");
                                                    builder.suggest("items");
                                                    builder.suggest("blocks");
                                                    builder.suggest("commands");
                                                    return builder.buildFuture();
                                                }).executes(this::save)))
                                .then(CommandManager.literal("reset")
                                        .then(CommandManager
                                                .argument("config", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    builder.suggest("main");
                                                    builder.suggest("registry");
                                                    builder.suggest("world");
                                                    builder.suggest("entities");
                                                    builder.suggest("items");
                                                    builder.suggest("blocks");
                                                    builder.suggest("commands");
                                                    return builder.buildFuture();
                                                }).executes(this::reset)))
                                .then(CommandManager
                                        .literal(
                                                "get")
                                        .then(CommandManager
                                                .argument("key", StringArgumentType.greedyString())
                                                .executes(this::get)))
                                .then(CommandManager
                                        .literal("set").then(
                                                CommandManager
                                                        .argument("key", StringArgumentType.word())
                                                        .then(CommandManager
                                                                .argument("value",
                                                                        StringArgumentType
                                                                                .greedyString())
                                                                .executes(this::set))))
                                .then(CommandManager.literal("list").executes(this::list))));
    }

    private int reloadAll(CommandContext<ServerCommandSource> context) {
        sendInfo(context, "Reloading all configurations...");
        boolean success = ConfigManager.reloadAll("mosbergapi");

        if (success) {
            sendSuccess(context, "Successfully reloaded all configurations");
        } else {
            sendError(context, "Failed to reload some configurations");
        }
        return success ? 1 : 0;
    }

    private int reload(CommandContext<ServerCommandSource> context) {
        String configName = StringArgumentType.getString(context, "config");
        sendInfo(context, "Reloading configuration: " + configName);

        boolean success = ConfigManager.reload("mosbergapi", configName);

        if (success) {
            sendSuccess(context, "Successfully reloaded " + configName);
        } else {
            sendError(context, "Failed to reload " + configName);
        }
        return success ? 1 : 0;
    }

    private int saveAll(CommandContext<ServerCommandSource> context) {
        sendInfo(context, "Saving all configurations...");
        boolean success = ConfigManager.saveAll("mosbergapi");

        if (success) {
            sendSuccess(context, "Successfully saved all configurations");
        } else {
            sendError(context, "Failed to save some configurations");
        }
        return success ? 1 : 0;
    }

    private int save(CommandContext<ServerCommandSource> context) {
        String configName = StringArgumentType.getString(context, "config");
        sendInfo(context, "Saving configuration: " + configName);

        boolean success = ConfigManager.save("mosbergapi", configName);

        if (success) {
            sendSuccess(context, "Successfully saved " + configName);
        } else {
            sendError(context, "Failed to save " + configName);
        }
        return success ? 1 : 0;
    }

    private int reset(CommandContext<ServerCommandSource> context) {
        String configName = StringArgumentType.getString(context, "config");
        sendInfo(context, "Resetting configuration: " + configName);

        ConfigManager.reset("mosbergapi", configName);
        sendSuccess(context, "Successfully reset " + configName + " to defaults");
        return 1;
    }

    private int get(CommandContext<ServerCommandSource> context) {
        String key = StringArgumentType.getString(context, "key");
        String value = ConfigManager.getString("mosbergapi", key);

        if (value.isEmpty()) {
            sendError(context, "Configuration key not found: " + key);
            return 0;
        }

        sendInfo(context, key + " = " + value);
        return 1;
    }

    private int set(CommandContext<ServerCommandSource> context) {
        String key = StringArgumentType.getString(context, "key");
        String value = StringArgumentType.getString(context, "value");

        // Try to parse as boolean, number, or string
        Object parsedValue = parseValue(value);
        ConfigManager.set("mosbergapi", key, parsedValue);

        sendSuccess(context, "Set " + key + " = " + value);
        return 1;
    }

    private int list(CommandContext<ServerCommandSource> context) {
        sendInfo(context, "Available configurations:");
        sendInfo(context, "  - main");
        sendInfo(context, "  - registry");
        sendInfo(context, "  - world");
        sendInfo(context, "  - entities");
        sendInfo(context, "  - items");
        sendInfo(context, "  - blocks");
        sendInfo(context, "  - commands");
        return 1;
    }

    private Object parseValue(String value) {
        // Try boolean
        if (value.equalsIgnoreCase("true"))
            return true;
        if (value.equalsIgnoreCase("false"))
            return false;

        // Try integer
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
        }

        // Try double
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
        }

        // Default to string
        return value;
    }
}
