package invoker54.invocore.client.invoimage;

import com.mojang.blaze3d.vertex.PoseStack;
import invoker54.invocore.Invocore;
import invoker54.invocore.client.util.InvoZone;
import invoker54.invocore.common.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class InvoImage {
    public static ModLogger LOGGER = ModLogger.getLogger(InvoImage.class, Invocore.debugMode);
    public static final String IMAGE_TYPE = "IMAGE_TYPE";

    protected final InvoZone mainZone;

    public InvoImage(InvoZone renderZone){
        this.mainZone = renderZone;
    }

    public static InvoImage fromTag(CompoundTag tag){
        if (tag.isEmpty()) return fromSprite(MissingTextureAtlasSprite.getLocation());

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
            default ->{
                return fromSprite(MissingTextureAtlasSprite.getLocation());
            }
        }
    }

    public static InvoImageSprite fromSprite(ResourceLocation location) {
        return fromSprites(location).get(0);
    }

    public static InvoImageSprite fromSprite(List<String> keyWords) {
        return fromSprites(keyWords).get(0);
    }

    public static List<InvoImageSprite> fromSprites(ResourceLocation location){
        List<String> list = new ArrayList<>();
        list.add(location.getNamespace());
        list.addAll(Arrays.stream(location.getPath().split("/")).toList());
        return fromSprites(list);
    }
    public static List<InvoImageSprite> fromSprites(List<String> keyWords) {
        int keywordCharCount = keyWords.stream().mapToInt(String::length).sum();
        TextureAtlas mainAtlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);

        List<ResourceLocation> allResources = new ArrayList<>(mainAtlas.texturesByName.keySet());
        allResources = allResources.stream().filter(resource ->
                keyWords.stream().allMatch(keyWord -> resource.toString().contains(keyWord))).collect(Collectors.toList());
        List<InvoImageSprite> imageList = new ArrayList<>();

        if (allResources.isEmpty()){
            allResources.add(MissingTextureAtlasSprite.getLocation());
        }

        allResources.sort(Comparator.comparingInt(a -> Math.abs(keywordCharCount - a.toString().length())));
        allResources.forEach(resource -> {
            TextureAtlasSprite sprite = mainAtlas.getSprite(resource);

            imageList.add(new InvoImageSprite(sprite));
        });
        return imageList;
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
    public void setMainZone(InvoZone updatedZone) {this.mainZone.copy(updatedZone);}

    public void render(PoseStack stack){
        this.render(stack, this.mainZone);
    }

    public abstract void render(PoseStack stack, InvoZone renderZone);

    public abstract CompoundTag serializeNBT();

    public abstract void deserializeNBT(CompoundTag tag);
}