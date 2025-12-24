package dk.mosberg.api.client.util;

import java.util.function.Supplier;
import dk.mosberg.api.client.registry.MosbergRenderStates;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.Entity;

/**
 * Simplified helper for common render state operations.
 * <p>
 * Provides quick access to render state registration and common render state patterns.
 * </p>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class RenderStatesHelper {

    private RenderStatesHelper() {
        throw new UnsupportedOperationException("RenderStatesHelper is a utility class");
    }

    /**
     * Quick registration for entity render states.
     * <p>
     * Simplified version that doesn't require entity class parameter.
     * </p>
     *
     * @param <S> The render state type
     * @param id State identifier
     * @param stateClass State class
     * @param factory State factory
     *
     * @example
     *
     *          <pre>{@code
     * RenderStatesHelper.entity("custom_mob",
     *     CustomMobRenderState.class,
     *     CustomMobRenderState::new);
     * }</pre>
     */
    public static <S extends EntityRenderState> void entity(String id, Class<S> stateClass,
            Supplier<S> factory) {
        MosbergRenderStates.registerEntityRenderState(id, stateClass, factory);
    }

    /**
     * Quick registration for living entity render states.
     *
     * @param <S> The render state type extending LivingEntityRenderState
     * @param id State identifier
     * @param stateClass State class
     * @param factory State factory
     */
    public static <S extends LivingEntityRenderState> void livingEntity(String id,
            Class<S> stateClass, Supplier<S> factory) {
        MosbergRenderStates.registerEntityRenderState(id, stateClass, factory);
    }

    /**
     * Quick registration for block entity render states.
     *
     * @param <S> The render state type
     * @param id State identifier
     * @param stateClass State class
     * @param factory State factory
     *
     * @example
     *
     *          <pre>{@code
     * RenderStatesHelper.blockEntity("custom_chest",
     *     CustomChestRenderState.class,
     *     CustomChestRenderState::new);
     * }</pre>
     */
    public static <S extends BlockEntityRenderState> void blockEntity(String id,
            Class<S> stateClass, Supplier<S> factory) {
        MosbergRenderStates.registerBlockEntityRenderState(id, stateClass, factory);
    }

    /**
     * Quick registration for item render states.
     *
     * @param <S> The render state type
     * @param id State identifier
     * @param factory State factory
     *
     * @example
     *
     *          <pre>{@code
     * RenderStatesHelper.item("custom_item", CustomItemRenderState::new);
     * }</pre>
     */
    public static <S extends ItemRenderState> void item(String id, Supplier<S> factory) {
        MosbergRenderStates.registerItemRenderState(id, factory);
    }

    /**
     * Checks if an entity render state exists.
     *
     * @param id State identifier
     * @return true if registered
     */
    public static boolean hasEntity(String id) {
        return MosbergRenderStates.hasEntityRenderState(id);
    }

    /**
     * Checks if a block entity render state exists.
     *
     * @param id State identifier
     * @return true if registered
     */
    public static boolean hasBlockEntity(String id) {
        return MosbergRenderStates.hasBlockEntityRenderState(id);
    }

    /**
     * Checks if an item render state exists.
     *
     * @param id State identifier
     * @return true if registered
     */
    public static boolean hasItem(String id) {
        return MosbergRenderStates.hasItemRenderState(id);
    }

    /**
     * Gets an entity render state by ID.
     *
     * @param <E> Entity type
     * @param <S> State type
     * @param id State identifier
     * @return RenderStateInfo or null
     */
    public static <E extends Entity, S extends EntityRenderState> MosbergRenderStates.RenderStateInfo<E, S> getEntity(
            String id) {
        return MosbergRenderStates.getEntityRenderState(id);
    }

    /**
     * Gets a block entity render state by ID.
     *
     * @param <T> Block entity type
     * @param <S> State type
     * @param id State identifier
     * @return RenderStateInfo or null
     */
    public static <T extends BlockEntity, S extends BlockEntityRenderState> MosbergRenderStates.RenderStateInfo<T, S> getBlockEntity(
            String id) {
        return MosbergRenderStates.getBlockEntityRenderState(id);
    }

    /**
     * Gets an item render state factory by ID.
     *
     * @param <S> State type
     * @param id State identifier
     * @return State factory or null
     */
    public static <S extends ItemRenderState> Supplier<S> getItem(String id) {
        return MosbergRenderStates.getItemRenderState(id);
    }
}
