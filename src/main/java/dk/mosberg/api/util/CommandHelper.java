package dk.mosberg.api.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

/**
 * Helper for registering commands
 */
public class CommandHelper {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, String name,
            int permissionLevel, Command<ServerCommandSource> command) {
        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal(name)
                .requires(source -> source.hasPermissionLevel(permissionLevel)).executes(command));
    }
}
