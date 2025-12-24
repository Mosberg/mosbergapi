package dk.mosberg.api.data.provider;

import java.util.concurrent.CompletableFuture;
import net.minecraft.data.DataOutput;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryWrapper;

/**
 * Base class for MosbergAPI recipe providers Extend this and override getRecipeGenerator() to add
 * your recipes
 */
public abstract class MosbergRecipeProvider extends RecipeGenerator.RecipeProvider {

    protected final RegistryWrapper.WrapperLookup registryLookup;

    public MosbergRecipeProvider(DataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
        this.registryLookup = registriesFuture.join();
    }

    /**
     * Override this method to create your recipe generator Return a new instance of your custom
     * RecipeGenerator
     */
    @Override
    protected abstract RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries,
            RecipeExporter exporter);
}
