package dk.mosberg.api.command.commands;

import org.jetbrains.annotations.NotNull;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dk.mosberg.api.command.MosbergCommand;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

/**
 * Command for inspecting Minecraft registries.
 *
 * <h2>Usage</h2>
 * <ul>
 * <li>{@code /mosbergapi registry list <type>} - List all entries in a registry</li>
 * <li>{@code /mosbergapi registry count <type>} - Count entries in a registry</li>
 * <li>{@code /mosbergapi registry search <type> <query>} - Search registry entries</li>
 * <li>{@code /mosbergapi registry info <type> <id>} - Get info about a registry entry</li>
 * </ul>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class RegistryCommand extends MosbergCommand {

    @Override
    @NotNull
    public String getName() {
        return "registry";
    }

    @Override
    public void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
            @NotNull CommandRegistryAccess registryAccess,
            @NotNull CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("mosbergapi").requires(source -> hasPermission(source))
                        .then(CommandManager.literal("registry")
                                .then(CommandManager.literal("list")
                                        .then(CommandManager
                                                .argument("type", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    builder.suggest("blocks");
                                                    builder.suggest("items");
                                                    builder.suggest("entities");
                                                    builder.suggest("sounds");
                                                    builder.suggest("particles");
                                                    builder.suggest("potions");
                                                    builder.suggest("effects");
                                                    return builder.buildFuture();
                                                }).executes(this::list)))
                                .then(CommandManager.literal("count")
                                        .then(CommandManager
                                                .argument("type", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    builder.suggest("blocks");
                                                    builder.suggest("items");
                                                    builder.suggest("entities");
                                                    builder.suggest("sounds");
                                                    builder.suggest("particles");
                                                    builder.suggest("potions");
                                                    builder.suggest("effects");
                                                    return builder.buildFuture();
                                                }).executes(this::count)))
                                .then(CommandManager.literal("search")
                                        .then(CommandManager
                                                .argument("type", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    builder.suggest("blocks");
                                                    builder.suggest("items");
                                                    builder.suggest("entities");
                                                    return builder.buildFuture();
                                                }).then(
                                                        CommandManager
                                                                .argument("query",
                                                                        StringArgumentType
                                                                                .greedyString())
                                                                .executes(this::search))))
                                .then(CommandManager.literal("info")
                                        .then(CommandManager
                                                .argument("type", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    builder.suggest("blocks");
                                                    builder.suggest("items");
                                                    builder.suggest("entities");
                                                    return builder.buildFuture();
                                                }).then(
                                                        CommandManager
                                                                .argument("id",
                                                                        StringArgumentType
                                                                                .greedyString())
                                                                .executes(this::info))))));
    }

    private int list(CommandContext<ServerCommandSource> context) {
        String type = StringArgumentType.getString(context, "type");
        Registry<?> registry = getRegistry(type);

        if (registry == null) {
            sendError(context, "Unknown registry type: " + type);
            return 0;
        }

        sendInfo(context, "Listing " + type + " registry (showing first 20):");
        int count = 0;
        for (Identifier id : registry.getIds()) {
            if (count++ >= 20) {
                sendInfo(context, "  ... and " + (registry.size() - 20) + " more");
                break;
            }
            sendInfo(context, "  - " + id);
        }

        return 1;
    }

    private int count(CommandContext<ServerCommandSource> context) {
        String type = StringArgumentType.getString(context, "type");
        Registry<?> registry = getRegistry(type);

        if (registry == null) {
            sendError(context, "Unknown registry type: " + type);
            return 0;
        }

        sendSuccess(context, type + " registry contains " + registry.size() + " entries");
        return 1;
    }

    private int search(CommandContext<ServerCommandSource> context) {
        String type = StringArgumentType.getString(context, "type");
        String query = StringArgumentType.getString(context, "query").toLowerCase();
        Registry<?> registry = getRegistry(type);

        if (registry == null) {
            sendError(context, "Unknown registry type: " + type);
            return 0;
        }

        sendInfo(context, "Searching " + type + " for '" + query + "':");
        int found = 0;
        for (Identifier id : registry.getIds()) {
            if (id.toString().toLowerCase().contains(query)) {
                sendInfo(context, "  - " + id);
                found++;
                if (found >= 20) {
                    sendInfo(context, "  ... (showing first 20 results)");
                    break;
                }
            }
        }

        if (found == 0) {
            sendError(context, "No results found");
            return 0;
        }

        sendSuccess(context, "Found " + found + " results");
        return 1;
    }

    private int info(CommandContext<ServerCommandSource> context) {
        String type = StringArgumentType.getString(context, "type");
        String idStr = StringArgumentType.getString(context, "id");
        Registry<?> registry = getRegistry(type);

        if (registry == null) {
            sendError(context, "Unknown registry type: " + type);
            return 0;
        }

        Identifier id = Identifier.tryParse(idStr);
        if (id == null) {
            sendError(context, "Invalid identifier: " + idStr);
            return 0;
        }

        if (!registry.containsId(id)) {
            sendError(context, "Entry not found: " + id);
            return 0;
        }

        Object entry = registry.get(id);
        sendInfo(context, "Information for " + id + ":");
        sendInfo(context, "  Type: " + entry.getClass().getSimpleName());
        sendInfo(context, "  Registry: " + type);
        sendInfo(context, "  Namespace: " + id.getNamespace());
        sendInfo(context, "  Path: " + id.getPath());

        return 1;
    }

    private Registry<?> getRegistry(String type) {
        return switch (type.toLowerCase()) {
            case "blocks", "block" -> Registries.BLOCK;
            case "items", "item" -> Registries.ITEM;
            case "entities", "entity" -> Registries.ENTITY_TYPE;
            case "sounds", "sound" -> Registries.SOUND_EVENT;
            case "particles", "particle" -> Registries.PARTICLE_TYPE;
            case "potions", "potion" -> Registries.POTION;
            case "effects", "effect", "statuseffects" -> Registries.STATUS_EFFECT;
            default -> null;
        };
    }
}
