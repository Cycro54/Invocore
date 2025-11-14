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

public class InvoImageSprite extends InvoImage {
    public static final ModLogger LOGGER = ModLogger.getLogger(InvoImageSprite.class, Invocore.debugMode);
    public static final String SPRITE_IMAGE = "SPRITE_IMAGE";
    public static final String ATLAS_RESOURCE_LOCATION = "ATLAS_RESOURCE_LOCATION";
    public static final String SPRITE_RESOURCE_LOCATION = "SPRITE_RESOURCE_LOCATION";
    public static final String ORIGINAL_SPRITE_ZONE = "ORIGINAL_SPRITE_ZONE";
    public static final String IMAGE_ZONE = "IMAGE_ZONE";
    public static final String SUB_IMAGE_ZONE = "SUB_IMAGE_ZONE";
    public static final String IMAGE_SCALE_WIDTH_FLOAT = "IMAGE_SCALE_WIDTH";
    public static final String IMAGE_SCALE_HEIGHT_FLOAT = "IMAGE_SCALE_HEIGHT";
    public static final String OUTER_OPERATION_ENUM = "OUTER_OPERATION_ENUM";
    public static final String INNER_OPERATION_ENUM = "INNER_OPERATION_ENUM";

    public static int globalResourceReloadCount = 1;

    protected int localResourceReloadCount = 0;
    protected ResourceLocation atlasLocation;
    protected ResourceLocation spriteLocation;
    protected final InvoZone originalSpriteZone;
    protected final InvoZone imageZone;
    protected final InvoZone subImageZone;
    protected float imageScaleWidth;
    protected float imageScaleHeight;
    protected ImageOperation outerOperation = ImageOperation.STRETCH;
    protected ImageOperation innerOperation = ImageOperation.STRETCH;

    public enum ImageOperation {
        NONE,
        STRETCH,
        TILE,
        MIRRORTILE
    }

    public InvoImageSprite(CompoundTag tag){
        super(new InvoZone(0,1,0,1));
        this.originalSpriteZone = this.mainZone.copy();
        this.imageZone = this.mainZone.copy();
        this.subImageZone = this.mainZone.copy();
        this.deserializeNBT(tag);
    }

    public InvoImageSprite(TextureAtlasSprite sprite){
        this(sprite.contents().name(), sprite.atlasLocation(),
                sprite.getX(), sprite.contents().width(),
                sprite.getY(), sprite.contents().height(),
                sprite.contents().width() / (sprite.getU1() - sprite.getU0()),
                sprite.contents().height() / (sprite.getV1() - sprite.getV0()));
    }

    public InvoImageSprite(ResourceLocation spriteLocation, ResourceLocation atlasLocation, float u0, float imageWidth, float v0, float imageHeight, float imageScaleWidth, float imageScaleHeight) {
        super(new InvoZone(u0, imageWidth, v0, imageHeight));
        this.spriteLocation = spriteLocation;
        this.atlasLocation = atlasLocation;
        this.imageZone = this.mainZone.copy();
        this.subImageZone = this.mainZone.copy();
        this.originalSpriteZone = this.mainZone.copy();
        this.imageScaleWidth = imageScaleWidth;
        this.imageScaleHeight = imageScaleHeight;
    }

    public ResourceLocation getSpriteLocation() {
        return this.spriteLocation;
    }

    public ResourceLocation getAtlasLocation() {
        return this.atlasLocation;
    }

    public void setAtlasLocation(ResourceLocation loc) {
        this.atlasLocation = loc;
    }

    public void setOuterOperation(ImageOperation operation) {
        this.outerOperation = operation;
    }

    public void setInnerOperation(ImageOperation operation) {
        this.innerOperation = operation;
    }

    public void resetImageZone() {
        this.imageZone.copy(this.originalSpriteZone);
        this.subImageZone.copy(this.originalSpriteZone);
    }

    public void resetMainZone() {
        this.mainZone.setWidth(this.imageZone.width());
        this.mainZone.setHeight(this.imageZone.height());
    }

    public InvoZone getImageZoneCopy() {
        return this.imageZone.copy();
    }
    public void setImageZone(InvoZone updatedZone) {
        InvoZone oldZone = this.imageZone.copy();
        this.imageZone.copy(updatedZone);
        this.subImageZone.changeRelative(oldZone, this.imageZone);
    }

    public InvoZone getSubImageZoneCopy() {
        return this.subImageZone.copy();
    }
    public InvoZone setSubImageZone(InvoZone updatedZone) {
        return this.subImageZone.copy(updatedZone);
    }

    @Override
    public InvoImageSprite copy() {
        InvoImageSprite imageSprite = new InvoImageSprite(
                this.spriteLocation,
                this.atlasLocation,
                this.imageZone.x(),
                this.imageZone.width(),
                this.imageZone.y(),
                this.imageZone.height(),
                this.imageScaleWidth,
                this.imageScaleHeight);
        imageSprite.setMainZone(this.getMainZoneCopy());
        imageSprite.localResourceReloadCount = globalResourceReloadCount;
        return imageSprite;
    }

    @Override
    public void render(PoseStack stack, InvoZone renderZone) {
        refreshSprite();
        RenderSystem.setShaderTexture(0, this.getAtlasLocation());
        
        if (this.imageZone.isSame(this.subImageZone)) {
            switch (this.outerOperation) {
                case NONE -> {
                    return;
                }
                case STRETCH -> {
                    ClientUtil.blitImage(stack, renderZone, imageZone, this.imageScaleWidth, this.imageScaleHeight);
                }
                case TILE -> {
                    this.renderTiles(stack, renderZone, false);
                }
                case MIRRORTILE -> {
                    this.renderTiles(stack, renderZone, true);
                }
            }
        } else {
            this.renderNineSlice(stack, this.subImageZone, renderZone);
        }
    }
    
    public void refreshSprite(){
        if (this.localResourceReloadCount == globalResourceReloadCount) return;
        this.localResourceReloadCount = globalResourceReloadCount;

        TextureAtlas mainAtlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);

        TextureAtlasSprite sprite = mainAtlas.getSprite(this.getSpriteLocation());
        InvoImageSprite updatedSpriteImage = new InvoImageSprite(sprite);

        this.imageScaleWidth = sprite.contents().width() / (sprite.getU1() - sprite.getU0());
        this.imageScaleHeight = sprite.contents().height() / (sprite.getV1() - sprite.getV0());

//        LOGGER.error("What's my sprite location: " + this.spriteLocation.toString());
//        LOGGER.error("What's my atlas location: " + this.atlasLocation.toString());
//        LOGGER.error("Current Image zone: " + this.getImageZoneCopy());
//        LOGGER.error("Updated Image zone: " + updatedSpriteImage.getImageZoneCopy());
//        LOGGER.error("Moved Image zone: " + updatedSpriteImage.getImageZoneCopy());
//        LOGGER.error("ending it!");

        this.imageZone.changeRelative(this.originalSpriteZone, updatedSpriteImage.getImageZoneCopy());
        this.subImageZone.changeRelative(this.originalSpriteZone, updatedSpriteImage.getImageZoneCopy());
        this.originalSpriteZone.copy(updatedSpriteImage.originalSpriteZone);
    }

    public InvoImageSprite crop(InvoZone cropZone) {
        InvoImageSprite cropImage = this.copy();
        cropImage.shiftImageX(cropZone.x());
        cropImage.shiftImageY(cropZone.y());
        cropImage.setRenderAndImageWidth(cropZone.width());
        cropImage.setRenderAndImageHeight(cropZone.height());

        return cropImage;
    }

    public void renderNineSlice(PoseStack stack, InvoZone cutOutZone, InvoZone newRenderZone) {
        InvoZone cropZone = this.mainZone.copy();

        //Top Left
        InvoImageSprite topLeftImage = this.crop(cropZone.setRightWidth(cutOutZone.x()).setDownHeight(cutOutZone.y()));
        topLeftImage.setMainZone(topLeftImage.getMainZoneCopy().setX(newRenderZone.x()).setY(newRenderZone.y()));
        topLeftImage.render(stack);

        //Bottom Left
        InvoImageSprite bottomLeftImage = this.crop(cropZone.setY(cutOutZone.down()).setDownHeight(this.mainZone.down()));
        bottomLeftImage.setMainZone(bottomLeftImage.getMainZoneCopy().setX(newRenderZone.x()).setDown(newRenderZone.down()));
        bottomLeftImage.render(stack);

        //Bottom Right
        InvoImageSprite bottomRightImage = this.crop(cropZone.setX(cutOutZone.right()).setRightWidth(this.mainZone.right()));
        bottomRightImage.setMainZone(bottomRightImage.getMainZoneCopy().setRight(newRenderZone.right()).setDown(newRenderZone.down()));
        bottomRightImage.render(stack);

        //Top Right
        InvoImageSprite topRightImage = this.crop(cropZone.setY(this.mainZone.y()).setDownHeight(cutOutZone.y()));
        topRightImage.setMainZone(topRightImage.getMainZoneCopy().setRight(newRenderZone.right()).setY(newRenderZone.y()));
        topRightImage.render(stack);

        //Top
        InvoImageSprite topImage = this.crop(cutOutZone.copy().setY(this.mainZone.y()).setDownHeight(cutOutZone.y()));
        topImage.setMainZone(topImage.getMainZoneCopy().setX(topLeftImage.mainZone.right()).setY(topLeftImage.mainZone.y()));

        //Bottom
        InvoImageSprite bottomImage = this.crop(cutOutZone.copy().setY(cutOutZone.down()).setDownHeight(this.mainZone.down()));
        bottomImage.setMainZone(bottomImage.getMainZoneCopy().setX(bottomLeftImage.mainZone.right()).setY(bottomLeftImage.mainZone.y()));

        //Left
        InvoImageSprite leftImage = this.crop(cutOutZone.copy().setX(this.mainZone.x()).setRightWidth(cutOutZone.x()));
        leftImage.setMainZone(leftImage.getMainZoneCopy().setX(topLeftImage.mainZone.x()).setY(topLeftImage.mainZone.down()));

        //Right
        InvoImageSprite rightImage = this.crop(cutOutZone.copy().setX(cutOutZone.right()).setRightWidth(this.mainZone.right()));
        rightImage.setMainZone(rightImage.getMainZoneCopy().setX(topRightImage.mainZone.x()).setY(topRightImage.mainZone.down()));

        boolean isOuterTile = this.outerOperation == ImageOperation.TILE
                || this.outerOperation == ImageOperation.MIRRORTILE;

        switch (this.outerOperation) {
            case NONE -> {
            }
            case STRETCH -> {
                topImage.mainZone.setRightWidth(topRightImage.mainZone.x());
                topImage.render(stack);

                bottomImage.mainZone.setRightWidth(bottomRightImage.mainZone.x());
                bottomImage.render(stack);

                leftImage.mainZone.setDownHeight(bottomLeftImage.mainZone.y());
                leftImage.render(stack);

                rightImage.mainZone.setDownHeight(bottomRightImage.mainZone.y());
                rightImage.render(stack);
            }
            case TILE, MIRRORTILE -> {
                boolean isMirror = this.outerOperation == ImageOperation.MIRRORTILE;

                topImage.renderTiles(stack, topImage.getMainZoneCopy().copy().setRightWidth(topRightImage.mainZone.x()), isMirror);
                bottomImage.renderTiles(stack, bottomImage.getMainZoneCopy().copy().setRightWidth(bottomRightImage.mainZone.x()), isMirror);
                leftImage.renderTiles(stack, leftImage.getMainZoneCopy().copy().setDownHeight(bottomLeftImage.mainZone.y()), isMirror);
                rightImage.renderTiles(stack, rightImage.getMainZoneCopy().copy().setDownHeight(bottomRightImage.mainZone.y()), isMirror);
            }
        }

        InvoImageSprite middleImage = this.crop(cutOutZone);
        InvoZone middleZone = middleImage.getMainZoneCopy()
                .setX(topLeftImage.mainZone.right()).setRightWidth(bottomRightImage.mainZone.x())
                .setY(topLeftImage.mainZone.down()).setDownHeight(bottomRightImage.mainZone.y());

        switch (this.innerOperation) {
            case NONE -> {
            }
            case STRETCH -> {
                middleImage.mainZone.copy(middleZone);
                middleImage.render(stack);
            }
            case TILE, MIRRORTILE -> {
                boolean isMirror = this.innerOperation == ImageOperation.MIRRORTILE;
                middleImage.renderTiles(stack, middleZone, isMirror);
            }
        }
    }

    public void renderTiles(PoseStack stack, InvoZone tileZone, boolean mirror) {
        float currentX = tileZone.x();
        float currentY = tileZone.y();
        boolean mirrorX = false;
        boolean mirrorY = false;

        //I'll render by row
        while (currentY != tileZone.down()) {
            InvoImageSprite copyImage = this.copy();
            InvoZone copyZone = copyImage.mainZone;
            float newWidth = Math.min(this.mainZone.width(), tileZone.right() - currentX);
            float newHeight = Math.min(this.mainZone.height(), tileZone.down() - currentY);
            copyZone.setX(currentX).setY(currentY);

            if (mirror && mirrorX) copyImage.setImageZone(copyImage.getImageZoneCopy().invertX());
            if (mirror && mirrorY) copyImage.setImageZone(copyImage.getImageZoneCopy().invertY());

            copyImage.setRenderAndImageWidth(newWidth);
            copyImage.setRenderAndImageHeight(newHeight);

//            boolean isBorderTile = copyZone.x() == tileZone.x() || copyZone.y() == tileZone.y() ||
//                    copyZone.right() == tileZone.right() || copyZone.down() == tileZone.down();

            copyImage.render(stack);
            currentX = copyZone.right();

            mirrorX = !mirrorX;

            if (currentX == tileZone.right()) {
                currentX = tileZone.x();
                currentY = copyZone.down();

                mirrorX = false;
                mirrorY = !mirrorY;
            }
        }
    }

    public void setRenderAndImageWidth(float newRenderWidth) {
        float normalizedImageWidth = this.imageZone.width() / this.mainZone.width();

        this.mainZone.setWidth(newRenderWidth);
        this.setImageZone(this.getImageZoneCopy().setWidth(normalizedImageWidth * newRenderWidth));
        if (this.imageZone.width() < 0) this.setImageZone(this.getImageZoneCopy().mirrorX(this.imageZone.x()));
    }

    public void setRenderAndImageHeight(float newRenderHeight) {
        float normalizedImageHeight = this.imageZone.height() / this.mainZone.height();

        this.mainZone.setHeight(newRenderHeight);
        this.setImageZone(this.getImageZoneCopy().setHeight(normalizedImageHeight * newRenderHeight));
        if (this.imageZone.height() < 0) this.setImageZone(this.getImageZoneCopy().mirrorY(this.imageZone.y()));
    }

    public void shiftImageX(float newRenderX) {
        float movePercentage = (newRenderX - this.mainZone.x()) / this.mainZone.width();

        this.setImageZone(this.getImageZoneCopy()
                .shift(this.imageZone.width() * movePercentage, 0));
    }

    public void shiftImageY(float newRenderY) {
        float movePercentage = (newRenderY - this.mainZone.y()) / this.mainZone.height();

        this.setImageZone(this.getImageZoneCopy()
                .shift(0, this.imageZone.height() * movePercentage));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(InvoImage.IMAGE_TYPE, SPRITE_IMAGE);
        tag.putString(SPRITE_RESOURCE_LOCATION, this.getSpriteLocation().toString());
        tag.putString(ATLAS_RESOURCE_LOCATION, this.getAtlasLocation().toString());
        tag.put(ORIGINAL_SPRITE_ZONE, this.originalSpriteZone.serializeNBT());
        tag.put(IMAGE_ZONE, this.imageZone.serializeNBT());
        tag.put(SUB_IMAGE_ZONE, this.subImageZone.serializeNBT());
        tag.putFloat(IMAGE_SCALE_WIDTH_FLOAT, this.imageScaleWidth);
        tag.putFloat(IMAGE_SCALE_HEIGHT_FLOAT, this.imageScaleHeight);
        tag.putString(OUTER_OPERATION_ENUM, this.outerOperation.name());
        tag.putString(INNER_OPERATION_ENUM, this.innerOperation.name());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.spriteLocation = ResourceUtil.create(tag.getString(SPRITE_RESOURCE_LOCATION));
        this.atlasLocation = ResourceUtil.create(tag.getString(ATLAS_RESOURCE_LOCATION));
        this.originalSpriteZone.copy(InvoZone.fromTag(tag.getCompound(ORIGINAL_SPRITE_ZONE)));
        this.imageZone.copy(InvoZone.fromTag(tag.getCompound(IMAGE_ZONE)));
        this.subImageZone.copy(InvoZone.fromTag(tag.getCompound(SUB_IMAGE_ZONE)));
        this.imageScaleWidth = tag.getFloat(IMAGE_SCALE_WIDTH_FLOAT);
        this.imageScaleHeight = tag.getFloat(IMAGE_SCALE_HEIGHT_FLOAT);
        this.outerOperation = ImageOperation.valueOf(tag.getString(OUTER_OPERATION_ENUM));
        this.innerOperation = ImageOperation.valueOf(tag.getString(INNER_OPERATION_ENUM));
    }
}
