package invoker54.invocore.client.invoimage;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import invoker54.invocore.Invocore;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.util.InvoZone;
import invoker54.invocore.common.ModLogger;
import invoker54.invocore.common.util.ResourceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class InvoImageSprite extends InvoImageTexture {
    public static final ModLogger LOGGER = ModLogger.getLogger(InvoImageSprite.class, Invocore.debugMode);
    public static final String SPRITE_IMAGE = "SPRITE_IMAGE";
    public static final String SPRITE_RESOURCE_LOCATION = "SPRITE_RESOURCE_LOCATION";

    protected ResourceLocation spriteLocation;

    public InvoImageSprite(CompoundTag tag){
        super(tag);
    }

    public InvoImageSprite(TextureAtlasSprite sprite){
        this(sprite.contents().name(), sprite.atlasLocation(),
                sprite.getX(), sprite.contents().width(),
                sprite.getY(), sprite.contents().height(),
                sprite.contents().width() / (sprite.getU1() - sprite.getU0()),
                sprite.contents().height() / (sprite.getV1() - sprite.getV0()));
    }

    public InvoImageSprite(ResourceLocation spriteLocation, ResourceLocation atlasLocation, float u0, float imageWidth, float v0, float imageHeight, float imageScaleWidth, float imageScaleHeight) {
        super(atlasLocation, u0, imageWidth, v0, imageHeight, imageScaleWidth, imageScaleHeight);
        this.spriteLocation = spriteLocation;
    }

    public ResourceLocation getSpriteLocation() {
        return this.spriteLocation;
    }

    public void setAtlasLocation(ResourceLocation loc) {
        this.resourceLocation = loc;
        localResourceReloadCount -= 1;
    }

    @Override
    public InvoImageSprite copy() {
        return (InvoImageSprite) super.copy();
    }

    @Override
    public void refreshImage(){
        if (this.localResourceReloadCount == globalResourceReloadCount) return;
        this.localResourceReloadCount = globalResourceReloadCount;

        TextureAtlas mainAtlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);

        TextureAtlasSprite sprite = mainAtlas.getSprite(this.getResourceLocation());
        InvoImageSprite updatedSpriteImage = new InvoImageSprite(sprite);

        this.imageScaleWidth = sprite.contents().width() / (sprite.getU1() - sprite.getU0());
        this.imageScaleHeight = sprite.contents().height() / (sprite.getV1() - sprite.getV0());

//        LOGGER.error("What's my sprite location: " + this.spriteLocation.toString());
//        LOGGER.error("What's my atlas location: " + this.atlasLocation.toString());
//        LOGGER.error("Current Image zone: " + this.getImageZoneCopy());
//        LOGGER.error("Updated Image zone: " + updatedSpriteImage.getImageZoneCopy());
//        LOGGER.error("Moved Image zone: " + updatedSpriteImage.getImageZoneCopy());
//        LOGGER.error("ending it!");

        this.imageZone.changeRelativeMultiply(this.originalTextureZone, updatedSpriteImage.getImageZoneCopy());
        this.subImageZone.changeRelativeMultiply(this.originalTextureZone, updatedSpriteImage.getImageZoneCopy());
        this.originalTextureZone.copy(updatedSpriteImage.originalTextureZone);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putString(InvoImage.IMAGE_TYPE, SPRITE_IMAGE);
        tag.putString(SPRITE_RESOURCE_LOCATION, this.getSpriteLocation().toString());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
        this.spriteLocation = ResourceUtil.create(tag.getString(SPRITE_RESOURCE_LOCATION));
    }
}
