package dk.mosberg.api.command.commands;

import org.jetbrains.annotations.NotNull;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dk.mosberg.api.command.MosbergCommand;
import dk.mosberg.api.util.MosbergHelper;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Command for entity utilities and management.
 *
 * <h2>Usage</h2>
 * <ul>
 * <li>{@code /mosbergapi entity spawn <type> [x] [y] [z]} - Spawn an entity</li>
 * <li>{@code /mosbergapi entity kill <selector>} - Kill entities</li>
 * <li>{@code /mosbergapi entity teleport <entity> <x> <y> <z>} - Teleport entity</li>
 * <li>{@code /mosbergapi entity heal <entity> [amount]} - Heal an entity</li>
 * <li>{@code /mosbergapi entity count [type]} - Count entities</li>
 * <li>{@code /mosbergapi entity list} - List nearby entities</li>
 * </ul>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class EntityCommand extends MosbergCommand {

    @Override
    @NotNull
    public String getName() {
        return "entity";
    }

    @Override
    public void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
            @NotNull CommandRegistryAccess registryAccess,
            @NotNull CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("mosbergapi")
                .requires(source -> hasPermission(source))
                .then(CommandManager.literal("entity")
                        .then(CommandManager.literal("spawn").then(CommandManager
                                .argument("type",
                                        RegistryEntryReferenceArgumentType.registryEntry(
                                                registryAccess, RegistryKeys.ENTITY_TYPE))
                                .executes(this::spawnAtExecutor)
                                .then(CommandManager.argument("x", IntegerArgumentType.integer())
                                        .then(CommandManager
                                                .argument("y", IntegerArgumentType.integer()).then(
                                                        CommandManager
                                                                .argument("z",
                                                                        IntegerArgumentType
                                                                                .integer())
                                                                .executes(
                                                                        this::spawnAtPosition))))))
                        .then(CommandManager.literal("kill")
                                .then(CommandManager
                                        .argument("entities", EntityArgumentType.entities())
                                        .executes(this::kill)))
                        .then(CommandManager.literal("teleport").then(CommandManager
                                .argument("entity", EntityArgumentType.entity())
                                .then(CommandManager.argument("x", IntegerArgumentType.integer())
                                        .then(CommandManager
                                                .argument("y", IntegerArgumentType.integer()).then(
                                                        CommandManager
                                                                .argument("z",
                                                                        IntegerArgumentType
                                                                                .integer())
                                                                .executes(this::teleport))))))
                        .then(CommandManager.literal("heal")
                                .then(CommandManager.argument("entity", EntityArgumentType.entity())
                                        .executes(context -> heal(context, -1))
                                        .then(CommandManager
                                                .argument("amount", IntegerArgumentType.integer(1))
                                                .executes(context -> heal(context,
                                                        IntegerArgumentType.getInteger(context,
                                                                "amount"))))))
                        .then(CommandManager.literal("count").executes(this::countAll)
                                .then(CommandManager
                                        .argument("type",
                                                RegistryEntryReferenceArgumentType.registryEntry(
                                                        registryAccess, RegistryKeys.ENTITY_TYPE))
                                        .executes(this::countType)))
                        .then(CommandManager.literal("list").executes(this::list))));
    }

    private int spawnAtExecutor(CommandContext<ServerCommandSource> context) {
        try {
            ServerCommandSource source = context.getSource();
            Vec3d pos = source.getPosition();
            return spawnEntity(context, (int) pos.x, (int) pos.y, (int) pos.z);
        } catch (Exception e) {
            sendError(context, "Failed to spawn entity: " + e.getMessage());
            return 0;
        }
    }

    private int spawnAtPosition(CommandContext<ServerCommandSource> context) {
        int x = IntegerArgumentType.getInteger(context, "x");
        int y = IntegerArgumentType.getInteger(context, "y");
        int z = IntegerArgumentType.getInteger(context, "z");
        return spawnEntity(context, x, y, z);
    }

    private int spawnEntity(CommandContext<ServerCommandSource> context, int x, int y, int z) {
        try {
            RegistryEntry.Reference<EntityType<?>> entityTypeEntry =
                    RegistryEntryReferenceArgumentType.getRegistryEntry(context, "type",
                            RegistryKeys.ENTITY_TYPE);
            EntityType<?> entityType = entityTypeEntry.value();
            ServerWorld world = context.getSource().getWorld();
            BlockPos pos = new BlockPos(x, y, z);

            // Use the correct create method signature for 1.21.10
            Entity entity = entityType.create(world, null, pos, SpawnReason.COMMAND, true, false);
            if (entity == null) {
                sendError(context, "Failed to create entity");
                return 0;
            }

            world.spawnEntity(entity);

            sendSuccess(context, "Spawned " + entityTypeEntry.registryKey().getValue() + " at " + x
                    + ", " + y + ", " + z);
            return 1;
        } catch (Exception e) {
            sendError(context, "Failed to spawn entity: " + e.getMessage());
            return 0;
        }
    }

    private int kill(CommandContext<ServerCommandSource> context) {
        try {
            var entities = EntityArgumentType.getEntities(context, "entities");
            int count = 0;

            for (Entity entity : entities) {
                entity.kill((ServerWorld) entity.getEntityWorld());
                count++;
            }

            sendSuccess(context, "Killed " + count + " entities");
            return count;
        } catch (Exception e) {
            sendError(context, "Failed to kill entities: " + e.getMessage());
            return 0;
        }
    }

    private int teleport(CommandContext<ServerCommandSource> context) {
        try {
            Entity entity = EntityArgumentType.getEntity(context, "entity");
            int x = IntegerArgumentType.getInteger(context, "x");
            int y = IntegerArgumentType.getInteger(context, "y");
            int z = IntegerArgumentType.getInteger(context, "z");

            BlockPos destination = new BlockPos(x, y, z);
            MosbergHelper.ENTITY.teleport(entity, destination);
            sendSuccess(context, "Teleported entity to " + x + ", " + y + ", " + z);
            return 1;
        } catch (Exception e) {
            sendError(context, "Failed to teleport entity: " + e.getMessage());
            return 0;
        }
    }

    private int heal(CommandContext<ServerCommandSource> context, int amount) {
        try {
            Entity entity = EntityArgumentType.getEntity(context, "entity");

            if (!(entity instanceof LivingEntity living)) {
                sendError(context, "Entity is not a living entity");
                return 0;
            }

            float healAmount = amount > 0 ? amount : living.getMaxHealth();
            MosbergHelper.ENTITY.heal(living, healAmount);

            sendSuccess(context, "Healed entity by " + healAmount + " HP");
            return 1;
        } catch (Exception e) {
            sendError(context, "Failed to heal entity: " + e.getMessage());
            return 0;
        }
    }

    private int countAll(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();
        int count = 0;
        for (@SuppressWarnings("unused")
        Entity entity : world.iterateEntities()) {
            count++;
        }
        sendInfo(context, "Total entities in world: " + count);
        return count;
    }

    private int countType(CommandContext<ServerCommandSource> context) {
        try {
            RegistryEntry.Reference<EntityType<?>> entityTypeEntry =
                    RegistryEntryReferenceArgumentType.getRegistryEntry(context, "type",
                            RegistryKeys.ENTITY_TYPE);
            EntityType<?> entityType = entityTypeEntry.value();
            ServerWorld world = context.getSource().getWorld();

            int count = 0;
            for (Entity entity : world.iterateEntities()) {
                if (entity.getType() == entityType) {
                    count++;
                }
            }

            sendInfo(context, "Found " + count + " entities of type "
                    + entityTypeEntry.registryKey().getValue());
            return count;
        } catch (Exception e) {
            sendError(context, "Failed to count entities: " + e.getMessage());
            return 0;
        }
    }

    private int list(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getWorld();
        Vec3d pos = context.getSource().getPosition();

        sendInfo(context, "Nearby entities (within 32 blocks):");
        int count = 0;
        for (Entity entity : world.iterateEntities()) {
            double distance = entity.squaredDistanceTo(pos);
            if (distance <= 32 * 32) {
                String info = String.format("  - %s at (%.1f, %.1f, %.1f)",
                        entity.getType().getName().getString(), entity.getX(), entity.getY(),
                        entity.getZ());
                sendInfo(context, info);
                count++;
                if (count >= 20) {
                    sendInfo(context, "  ... (showing first 20)");
                    break;
                }
            }
        }

        if (count == 0) {
            sendInfo(context, "  No entities found nearby");
        }
        return count;
    }
}
