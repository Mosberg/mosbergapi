package dk.mosberg.api.command;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mojang.brigadier.CommandDispatcher;
import dk.mosberg.api.command.commands.BlockCommand;
import dk.mosberg.api.command.commands.ConfigCommand;
import dk.mosberg.api.command.commands.DebugCommand;
import dk.mosberg.api.command.commands.EntityCommand;
import dk.mosberg.api.command.commands.HelpCommand;
import dk.mosberg.api.command.commands.ItemCommand;
import dk.mosberg.api.command.commands.RegistryCommand;
import dk.mosberg.api.command.commands.WorldCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

/**
 * Central registry for all MosbergAPI commands.
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergCommands {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergCommands.class);
    private static final List<MosbergCommand> COMMANDS = new ArrayList<>();
    private static boolean initialized = false;

    /**
     * Registers all MosbergAPI commands.
     */
    public static void register() {
        if (initialized) {
            LOGGER.warn("Commands already registered, skipping duplicate registration");
            return;
        }

        // Register all command implementations
        COMMANDS.add(new ConfigCommand());
        COMMANDS.add(new RegistryCommand());
        COMMANDS.add(new EntityCommand());
        COMMANDS.add(new ItemCommand());
        COMMANDS.add(new BlockCommand());
        COMMANDS.add(new WorldCommand());
        COMMANDS.add(new DebugCommand());
        COMMANDS.add(new HelpCommand());

        // Register with Fabric API
        CommandRegistrationCallback.EVENT.register(MosbergCommands::registerAll);

        initialized = true;
        LOGGER.info("Registered {} MosbergAPI commands", COMMANDS.size());
    }

    /**
     * Registers all commands with the command dispatcher.
     */
    private static void registerAll(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
            @NotNull CommandRegistryAccess registryAccess,
            @NotNull CommandManager.RegistrationEnvironment environment) {
        for (MosbergCommand command : COMMANDS) {
            try {
                command.register(dispatcher, registryAccess, environment);
            } catch (Exception e) {
                LOGGER.error("Failed to register command: {}", command.getName(), e);
            }
        }
    }

    /**
     * Gets all registered commands.
     */
    @NotNull
    public static List<MosbergCommand> getCommands() {
        return new ArrayList<>(COMMANDS);
    }

    /**
     * Gets a command by name.
     */
    public static MosbergCommand getCommand(@NotNull String name) {
        return COMMANDS.stream().filter(cmd -> cmd.getName().equalsIgnoreCase(name)).findFirst()
                .orElse(null);
    }
}
