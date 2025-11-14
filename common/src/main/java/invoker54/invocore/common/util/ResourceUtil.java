package invoker54.invocore.common.util;

import invoker54.invocore.client.util.ClientUtil;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceUtil {
    public static final String ATLAS = "atlases";
    public static final String BLOCKSTATE = "blockstates";
    public static final String FONT = "font";
    public static final String LANG = "lang";
    public static final String PARTICLE = "particles";
    public static final String SHADER = "shaders";
    public static final String TEXT = "shaders";
    public static final String TEXTURE = "textures";

    public static final String MINECRAFT = "minecraft";

    public static final String PNG = ".png";
    public static final String JPG = ".jpg";
    public static final String MCMETA = ".mcmeta";
    public static final String JSON = ".json";

    public static ResourceLocation create(String path){
        return new ResourceLocation(path);
    }

    public static ResourceLocation create(String modId, String path){
        return new ResourceLocation(modId, path);
    }

    public static ResourceLocation get(String basePath, String... keyWords){
        return getAll(ClientUtil.getMinecraft().getResourceManager(), basePath, keyWords).get(0);
    }

    public static ResourceLocation get(ResourceManager manager, String basePath, String... keyWords){
        return getAll(manager, basePath, keyWords).get(0);
    }

    public static List<ResourceLocation> getAll(ResourceManager manager, String basePath, String... keyWords){
        List<ResourceLocation> list = new ArrayList<>(manager.listResources(basePath,
                (location -> {
                    String stringLocation = location.toString();
                    return Arrays.stream(keyWords).allMatch(stringLocation::contains);
                })).keySet().stream().toList());
        if (list.isEmpty()) list.add(MissingTextureAtlasSprite.getLocation());
        return list;
    }
}
