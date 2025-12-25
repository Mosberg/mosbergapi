package dk.mosberg.api.command.commands;

import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dk.mosberg.api.command.MosbergCommand;
import dk.mosberg.api.util.InventoryHelper;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Command for item utilities and management.
 *
 * <h2>Usage</h2>
 * <ul>
 * <li>{@code /mosbergapi item give <player> <item> [count]} - Give item to player</li>
 * <li>{@code /mosbergapi item clear <player> [item]} - Clear items from player</li>
 * <li>{@code /mosbergapi item repair <player>} - Repair held item</li>
 * <li>{@code /mosbergapi item count <player> [item]} - Count items in inventory</li>
 * <li>{@code /mosbergapi item list <player>} - List player's inventory</li>
 * </ul>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class ItemCommand extends MosbergCommand {

    @Override
    @NotNull
    public String getName() {
        return "item";
    }

    @Override
    public void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
            @NotNull CommandRegistryAccess registryAccess,
            @NotNull CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("mosbergapi").requires(source -> hasPermission(source))
                        .then(CommandManager.literal("item")
                                .then(CommandManager.literal("give").then(CommandManager
                                        .argument("player", EntityArgumentType.players())
                                        .then(CommandManager
                                                .argument("item",
                                                        ItemStackArgumentType
                                                                .itemStack(registryAccess))
                                                .executes(context -> give(context, 1))
                                                .then(CommandManager
                                                        .argument("count",
                                                                IntegerArgumentType.integer(1, 64))
                                                        .executes(context -> give(context,
                                                                IntegerArgumentType.getInteger(
                                                                        context, "count")))))))
                                .then(CommandManager.literal("clear").then(CommandManager
                                        .argument("player", EntityArgumentType.players())
                                        .executes(this::clearAll)
                                        .then(CommandManager
                                                .argument("item",
                                                        ItemStackArgumentType
                                                                .itemStack(registryAccess))
                                                .executes(this::clearItem))))
                                .then(CommandManager.literal("repair")
                                        .then(CommandManager
                                                .argument("player", EntityArgumentType.player())
                                                .executes(this::repair)))
                                .then(CommandManager.literal("count").then(CommandManager
                                        .argument("player", EntityArgumentType.player())
                                        .executes(this::countAll)
                                        .then(CommandManager
                                                .argument("item",
                                                        ItemStackArgumentType
                                                                .itemStack(registryAccess))
                                                .executes(this::countItem))))
                                .then(CommandManager.literal("list")
                                        .then(CommandManager
                                                .argument("player", EntityArgumentType.player())
                                                .executes(this::list)))));
    }

    private int give(CommandContext<ServerCommandSource> context, int count) {
        try {
            Collection<ServerPlayerEntity> players =
                    EntityArgumentType.getPlayers(context, "player");
            ItemStack stack = ItemStackArgumentType.getItemStackArgument(context, "item")
                    .createStack(count, false);

            int successCount = 0;
            for (ServerPlayerEntity player : players) {
                InventoryHelper.addItemToInventory(player.getInventory(), stack.copy());
                successCount++;
            }

            sendSuccess(context, "Gave " + count + " " + stack.getName().getString() + " to "
                    + successCount + " players");
            return successCount;
        } catch (Exception e) {
            sendError(context, "Failed to give items: " + e.getMessage());
            return 0;
        }
    }

    private int clearAll(CommandContext<ServerCommandSource> context) {
        try {
            Collection<ServerPlayerEntity> players =
                    EntityArgumentType.getPlayers(context, "player");

            int totalCleared = 0;
            for (ServerPlayerEntity player : players) {
                player.getInventory().clear();
                totalCleared += 36; // Approximate count
            }

            sendSuccess(context, "Cleared items from " + players.size() + " players");
            return totalCleared;
        } catch (Exception e) {
            sendError(context, "Failed to clear items: " + e.getMessage());
            return 0;
        }
    }

    private int clearItem(CommandContext<ServerCommandSource> context) {
        try {
            Collection<ServerPlayerEntity> players =
                    EntityArgumentType.getPlayers(context, "player");
            ItemStack stack = ItemStackArgumentType.getItemStackArgument(context, "item")
                    .createStack(1, false);

            int totalCleared = 0;
            for (ServerPlayerEntity player : players) {
                int cleared = 0;
                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack invStack = player.getInventory().getStack(i);
                    if (ItemStack.areItemsEqual(invStack, stack)) {
                        cleared += invStack.getCount();
                        player.getInventory().setStack(i, ItemStack.EMPTY);
                    }
                }
                totalCleared += cleared;
            }

            sendSuccess(context, "Cleared " + totalCleared + " " + stack.getName().getString()
                    + " from " + players.size() + " players");
            return totalCleared;
        } catch (Exception e) {
            sendError(context, "Failed to clear items: " + e.getMessage());
            return 0;
        }
    }

    private int repair(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            ItemStack stack = player.getMainHandStack();

            if (stack.isEmpty()) {
                sendError(context, "Player is not holding an item");
                return 0;
            }

            if (!stack.isDamageable()) {
                sendError(context, "Item cannot be repaired");
                return 0;
            }

            stack.setDamage(0);
            sendSuccess(context, "Repaired " + stack.getName().getString());
            return 1;
        } catch (Exception e) {
            sendError(context, "Failed to repair item: " + e.getMessage());
            return 0;
        }
    }

    private int countAll(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            int count = 0;
            for (int i = 0; i < player.getInventory().size(); i++) {
                if (!player.getInventory().getStack(i).isEmpty()) {
                    count++;
                }
            }
            sendInfo(context,
                    player.getName().getString() + " has " + count + " item slots filled");
            return count;
        } catch (Exception e) {
            sendError(context, "Failed to count items: " + e.getMessage());
            return 0;
        }
    }

    private int countItem(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            ItemStack stack = ItemStackArgumentType.getItemStackArgument(context, "item")
                    .createStack(1, false);

            int count = 0;
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack invStack = player.getInventory().getStack(i);
                if (ItemStack.areItemsEqual(invStack, stack)) {
                    count += invStack.getCount();
                }
            }

            sendInfo(context, player.getName().getString() + " has " + count + " "
                    + stack.getName().getString());
            return count;
        } catch (Exception e) {
            sendError(context, "Failed to count items: " + e.getMessage());
            return 0;
        }
    }

    private int list(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
            sendInfo(context, "Inventory for " + player.getName().getString() + ":");

            for (int slot = 0; slot < player.getInventory().size(); slot++) {
                ItemStack stack = player.getInventory().getStack(slot);
                if (!stack.isEmpty()) {
                    sendInfo(context, String.format("  Slot %d: %dx %s", slot, stack.getCount(),
                            stack.getName().getString()));
                }
            }

            return 1;
        } catch (Exception e) {
            sendError(context, "Failed to list items: " + e.getMessage());
            return 0;
        }
    }
}
