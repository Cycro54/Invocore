package invoker54.invocore.client.invoimage;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import invoker54.invocore.Invocore;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.util.InvoZone;
import invoker54.invocore.common.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public abstract class InvoImage {
    public static ModLogger LOGGER = ModLogger.getLogger(InvoImage.class, Invocore.debugMode);
    public static final String IMAGE_TYPE = "IMAGE_TYPE";
    public static final String MAIN_ZONE = "MAIN_ZONE";
    public static final String ROTATION_FLOAT = "ROTATION_FLOAT";
    public static final String PIVOT_VECTOR = "PIVOT_VECTOR";

    protected final InvoZone mainZone;
    protected Vector2f pivotPoint;
    protected float rotation;

    public InvoImage(InvoZone renderZone){
        this(renderZone, 0, new Vector2f(0,0));
    }

    public InvoImage(InvoZone renderZone, float rotation, Vector2f pivotPoint){
        this.mainZone = renderZone;
        this.rotation = rotation;
        this.pivotPoint = pivotPoint;
    }

    public static List<Runnable> reloadListeners = new ArrayList<>();
    public static Map<ResourceLocation, InvoImageTexture> cachedTextureMap = new HashMap<>();
    public static Map<ResourceLocation, InvoImageTexture> croppedCacheTextureMap = new HashMap<>();

    public static InvoImage fromTag(CompoundTag tag){
        if (tag.isEmpty()) return fromTexture(MissingTextureAtlasSprite.getLocation());

        switch (tag.getString(IMAGE_TYPE)){
            case InvoImageColor.COLOR_IMAGE -> {
                return new InvoImageColor(tag);
            }
            case InvoImageItem.ITEM_IMAGE ->{
                return new InvoImageItem(tag);
            }
            case InvoImageSprite.SPRITE_IMAGE ->{
                return new InvoImageSprite(tag);
            }
            case InvoImageTexture.TEXTURE_IMAGE -> {
                return new InvoImageTexture(tag);
            }
            case InvoImageCanvas.CANVAS_IMAGE -> {
                return new InvoImageCanvas(tag);
            }
            default ->{
                return fromTexture(MissingTextureAtlasSprite.getLocation());
            }
        }
    }

    public static InvoImageTexture fromTexture(ResourceLocation location) {
        List<InvoImageTexture> spriteList = fromTextures(location);
        return spriteList.isEmpty() ? InvoImageTexture.MISSING : spriteList.get(0);
    }

    public static InvoImageTexture fromTexture(List<String> keyWords) {
        List<InvoImageTexture> spriteList = fromTextures(keyWords);
        return spriteList.isEmpty() ? InvoImageTexture.MISSING : spriteList.get(0);
    }

    public static List<InvoImageTexture> fromTextures(ResourceLocation location){
        List<String> list = new ArrayList<>();
        list.add(location.getNamespace());
        list.addAll(Arrays.stream(location.getPath().split("/")).toList());
        return fromTextures(list);
    }
    public static List<InvoImageTexture> fromTextures(List<String> keyWords) {
        int keywordCharCount = keyWords.stream().mapToInt(String::length).sum();

        List<ResourceLocation> allResources = new ArrayList<>(cachedTextureMap.keySet());
        allResources = allResources.stream().filter(resource ->
                keyWords.stream().allMatch(keyWord -> resource.toString().contains(keyWord))).collect(Collectors.toList());
        allResources.sort(Comparator.comparingInt(a -> Math.abs(keywordCharCount - a.toString().length())));

        return allResources.stream().map(resource -> cachedTextureMap.get(resource).copy()).toList();
    }

    public static void addReloadListener(Runnable reloadListener){
        reloadListeners.add(reloadListener);
    }

    public static void getAllTextures(boolean clearCache){
        if (clearCache) cachedTextureMap.clear();

        if (!cachedTextureMap.isEmpty()) return;

        TextureAtlas mainAtlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
        TextureManager tManager = ClientUtil.getTextureManager();
        ResourceManager rManager = ClientUtil.getMinecraft().getResourceManager();

        List<ResourceLocation> allResources = new ArrayList<>(rManager.
                listResources("textures", r -> r.getPath().endsWith(".png")).keySet());

        try {
            allResources.forEach(resource -> {
                TextureAtlasSprite sprite = mainAtlas.getSprite(resource);
                if (sprite.contents().name() == resource) cachedTextureMap.put(resource, new InvoImageSprite(sprite));
                else {
                    tManager.getTexture(resource);
                    try (InputStream stream = rManager.open(resource)) {
                        NativeImage image = NativeImage.read(stream);
                        cachedTextureMap.put(resource, new InvoImageTexture(resource, image));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            cachedTextureMap.put(InvoImageTexture.MISSING.resourceLocation, InvoImageTexture.MISSING);
        }
        catch (Exception e){
            LOGGER.warn("Can't grab textures at this time...");
            e.printStackTrace();
            cachedTextureMap.clear();
            return;
        }

        LOGGER.error("[INVOCORE] How many textures are there? " + (cachedTextureMap.size()));

        reloadListeners.forEach(Runnable::run);
    }

    public static InvoImageColor fromColor(Color color){
        return new InvoImageColor(new InvoZone(0,1,0,1), color);
    }
    public static InvoImageItem fromItem(ItemStack stack){
        return new InvoImageItem(new InvoZone(0,16,0,16), stack);
    }

    public abstract InvoImage copy();

    public InvoZone getMainZoneCopy() {
        return this.mainZone.copy();
    }
    public void setMainZone(InvoZone updatedZone, boolean movePivot) {
        InvoZone oldZone = this.mainZone.copy();
        this.mainZone.copy(updatedZone);
        if (movePivot) pivotPoint = InvoZone.changeRelativeMultiply(this.pivotPoint, oldZone, updatedZone);
    }

    public void setPivot(Vector2f pivotPoint){
        this.pivotPoint = pivotPoint;
    }

    public Vector2f getPivotPoint(){
        return this.pivotPoint;
    }

    public void setRotation(float rotation){
        this.rotation = (float) Math.toRadians(rotation);
    }
    public float getRotation(){
        return this.rotation;
    }

    public void rotate(PoseStack stack, InvoZone renderZone){
        Vector2f pivot = InvoZone.changeRelativeMultiply(this.pivotPoint, this.getMainZoneCopy(), renderZone);
        stack.pushPose();
        stack.translate(pivot.x, pivot.y, 0);
        stack.mulPose(Axis.ZP.rotation(rotation));
        stack.translate(-pivot.x, -pivot.y, 0);
    }

    public void render(PoseStack stack){
        this.render(stack, this.mainZone);
    }

    public abstract void render(PoseStack stack, InvoZone renderZone);

    public CompoundTag serializeNBT(){
        CompoundTag tag = new CompoundTag();
        tag.put(MAIN_ZONE, this.mainZone.serializeNBT());

        tag.putFloat(ROTATION_FLOAT, this.rotation);

        tag.putString(PIVOT_VECTOR, this.pivotPoint.x() + ":" + this.pivotPoint.y());
        return tag;
    }

    public void deserializeNBT(CompoundTag tag){
        this.setMainZone(InvoZone.fromTag(tag.getCompound(MAIN_ZONE)), false);
        this.rotation = tag.getFloat(ROTATION_FLOAT);

        String[] stringArray = tag.getString(PIVOT_VECTOR).split(":");
        this.pivotPoint = new Vector2f(Float.parseFloat(stringArray[0]), Float.parseFloat(stringArray[1]));
    }

    public InvoImageCanvas canvas(List<InvoImage> imageList){
       InvoZone canvasZone = this.getMainZoneCopy();
       if (!(this instanceof InvoImageCanvas)) {
           for (InvoImage image : imageList) {
               canvasZone.merge(image.getMainZoneCopy());
           }
       }
        return canvas(canvasZone, imageList);
    }

    public InvoImageCanvas canvas(InvoZone canvasZone){
        return canvas(canvasZone, new ArrayList<>());
    }

    public InvoImageCanvas canvas(InvoZone canvasZone, List<InvoImage> imageList){
        List<InvoImage> modifiedList = new ArrayList<>(imageList.stream().map(InvoImage::copy).toList());
        modifiedList.add(0, this.copy());
        return new InvoImageCanvas(canvasZone, modifiedList);
    }
}