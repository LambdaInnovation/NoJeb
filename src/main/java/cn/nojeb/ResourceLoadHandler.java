package cn.nojeb;

import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import scala.sys.process.ProcessBuilderImpl.Dummy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ResourceLoadHandler {

    public static void onSoundManagerReload(IResourceManager manager, SoundHandler handler) {
    }

    public static void onModelManagerReload(IResourceManager manager, ModelManager mm) {
        try {
            ModelLoader loader = new ModelLoader(new DummyResourceManager(), mm.getTextureMap(), mm.getBlockModelShapes());
            ModelResourceLocation missingModelLocation; {
                Field field = ModelBakery.class.getDeclaredField("MODEL_MISSING");
                field.setAccessible(true);
                missingModelLocation = (ModelResourceLocation) field.get(null);
            }

            IModel defaultModel = ModelLoaderRegistry.getMissingModel();

            IBakedModel bakedModel = defaultModel.bake(defaultModel.getDefaultState(), DefaultVertexFormats.ITEM,
                (resourceLocation -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(resourceLocation.toString())));

            {
                Field field = ModelManager.class.getDeclaredField("modelRegistry");
                field.setAccessible(true);

                RegistrySimple<ModelResourceLocation, IBakedModel> bakedRegistry = new RegistrySimple<>();
                bakedRegistry.putObject(missingModelLocation, bakedModel);
                field.set(mm, bakedRegistry);
            }

            {
                Field field = ModelManager.class.getDeclaredField("defaultModel");
                field.setAccessible(true);
                field.set(mm, bakedModel);
            }
        } catch (Exception ex) {
            throw new RuntimeException((ex));
        }
    }

    static class DummyResourceManager implements IResourceManager {

        @Override
        public Set<String> getResourceDomains() {
            return Collections.emptySet();
        }

        @Override
        public IResource getResource(ResourceLocation location) throws IOException {
            throw new FileNotFoundException(location.toString());
        }

        @Override
        public List<IResource> getAllResources(ResourceLocation location) throws IOException {
            return Collections.emptyList();
        }
    }

}
