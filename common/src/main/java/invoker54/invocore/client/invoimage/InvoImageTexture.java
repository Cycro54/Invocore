package invoker54.invocore.client.invoimage;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import invoker54.invocore.Invocore;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.util.InvoZone;
import invoker54.invocore.common.ModLogger;
import invoker54.invocore.common.util.ResourceUtil;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;

import java.io.IOException;
import java.io.InputStream;

public class InvoImageTexture extends InvoImage{
    public static final InvoImageTexture MISSING = new InvoImageTexture(ResourceUtil.create(Invocore.MOD_ID, "textures/missing_texture.png"),
            0, 16, 0, 16, 16,16);

    public static final ModLogger LOGGER = ModLogger.getLogger(InvoImageTexture.class, Invocore.debugMode);
    public static final String TEXTURE_IMAGE = "TEXTURE_IMAGE";
    public static final String TEXTURE_RESOURCE_LOCATION = "TEXTURE_RESOURCE_LOCATION";
    public static final String ORIGINAL_TEXTURE_ZONE = "ORIGINAL_TEXTURE_ZONE";
    public static final String IMAGE_ZONE = "IMAGE_ZONE";
    public static final String SUB_IMAGE_ZONE = "SUB_IMAGE_ZONE";
    public static final String IMAGE_SCALE_WIDTH_FLOAT = "IMAGE_SCALE_WIDTH";
    public static final String IMAGE_SCALE_HEIGHT_FLOAT = "IMAGE_SCALE_HEIGHT";
    public static final String MAX_IMAGE_SIZE_FLOAT = "MAX_IMAGE_SIZE_FLOAT";
    public static final String OUTER_OPERATION_ENUM = "OUTER_OPERATION_ENUM";
    public static final String INNER_OPERATION_ENUM = "INNER_OPERATION_ENUM";

    public static int globalResourceReloadCount = 1;

    protected int localResourceReloadCount = 0;
    protected ResourceLocation resourceLocation;
    public final InvoZone originalTextureZone = new InvoZone(0,0,0,0);
    protected final InvoZone imageZone = new InvoZone(0,0,0,0);
    protected final InvoZone subImageZone = new InvoZone(0,0,0,0);
    protected boolean invertZoneW = false;
    protected boolean invertZoneH = false;

    protected float imageScaleWidth;
    protected float imageScaleHeight;
    protected ImageOperation outerOperation = ImageOperation.STRETCH;
    protected ImageOperation innerOperation = ImageOperation.STRETCH;

    protected float maxImageSize = 1;

    public InvoImageTexture(CompoundTag tag){
        super(new InvoZone(0,1,0,1));
        this.deserializeNBT(tag);
    }

    public InvoImageTexture(ResourceLocation location, NativeImage image){
        this(location,
                0, image.getWidth(), 0, image.getHeight(),
                image.getWidth(), image.getHeight());
    }

    public InvoImageTexture(ResourceLocation resourceLocation, float u0, float imageWidth, float v0, float imageHeight, float imageScaleWidth, float imageScaleHeight) {
        super(new InvoZone(u0, imageWidth, v0, imageHeight));
        this.resourceLocation = resourceLocation;
        this.imageZone.copy(this.mainZone.copy());
        this.subImageZone.copy(this.mainZone.copy());
        this.originalTextureZone.copy(this.mainZone.copy());
        this.imageScaleWidth = imageScaleWidth;
        this.imageScaleHeight = imageScaleHeight;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public boolean isMissing(){
        return MISSING.getResourceLocation().toString().equals(this.getResourceLocation().toString());
    }

    public void setMaxImageSize(float maxImageSize){
        this.maxImageSize = Math.max(1, maxImageSize);
    }

    public float getMaxImageSize(){
        return this.maxImageSize;
    }

    public void setOuterOperation(ImageOperation operation) {
        this.outerOperation = operation;
    }

    public void setInnerOperation(ImageOperation operation) {
        this.innerOperation = operation;
    }

    public void resetImageZone() {
        this.imageZone.copy(this.originalTextureZone);
        this.subImageZone.copy(this.originalTextureZone);
    }

    public void resetMainZone() {
        this.mainZone.copy(this.imageZone);
    }

    public InvoZone getImageZoneCopy() {
        return this.imageZone.copy();
    }
    public void setImageZone(InvoZone updatedZone) {
        InvoZone oldZone = this.imageZone.copy();
        this.imageZone.copy(updatedZone.copy().absolute());
        this.subImageZone.changeRelativeMultiply(oldZone, this.imageZone);

        if (Math.signum(updatedZone.width()) == -1) invertZoneW = !invertZoneW;
        if (Math.signum(updatedZone.height()) == -1) invertZoneH = !invertZoneH;
    }

    @Override
    public void setMainZone(InvoZone updatedZone, boolean movePivot) {
        super.setMainZone(updatedZone.copy().absolute(), movePivot);

        if (Math.signum(updatedZone.width()) == -1) invertZoneW = !invertZoneW;
        if (Math.signum(updatedZone.height()) == -1) invertZoneH = !invertZoneH;
    }

    public InvoZone getSubImageZoneCopy() {
        return this.subImageZone.copy();
    }
    public InvoZone setSubImageZone(InvoZone updatedZone) {
        this.subImageZone.copy(updatedZone.copy().absolute().setBound(this.imageZone.copy(), true));
        if (!this.subImageZone.isSame(this.imageZone)) this.subImageZone.setBound(this.imageZone.copy().inflate(-1),true);
//        LOGGER.warn("Subimage is now: " + this.subImageZone);
        return this.subImageZone;
    }

    @Override
    public InvoImageTexture copy() {
        InvoImageTexture copyImage = (InvoImageTexture) InvoImage.fromTag(this.serializeNBT());
        copyImage.localResourceReloadCount = globalResourceReloadCount;
        return copyImage;
    }

    public void invertImage(boolean invertZoneW, boolean invertZoneH){
        this.invertZoneW = invertZoneW;
        this.invertZoneH = invertZoneH;
    }

    public InvoZone getAdjustedImageZone(){
        return this.imageZone.copy().multiply(this.maxImageSize);
    }

    public InvoZone getSubRenderZone(){
        return this.getSubRenderZone(this.getMainZoneCopy());
    }

    public InvoZone getSubRenderZone(InvoZone renderZone){
        InvoZone imageCopyZone = this.imageZone.copy().multiply(this.maxImageSize);
        imageCopyZone.setBound(renderZone);
        InvoZone subZone = this.getSubImageZoneCopy().changeRelativeMultiply(this.imageZone, imageCopyZone);
        subZone.changeRelativeMultiply(imageCopyZone, renderZone);

        return subZone;
    }

    public void renderWithSubZone(PoseStack stack){
        this.renderWithSubZone(stack, this.getSubRenderZone());
    }

    public void renderWithSubZone(PoseStack stack, InvoZone subRenderZone){
        this.render(stack, subRenderZone.copy().changeRelativeMultiply(this.subImageZone, this.imageZone).center(subRenderZone));
    }

    @Override
    public void render(PoseStack stack, InvoZone renderZone) {
        if (this.imageZone.isZero()) return;

        boolean invertW = invertZoneW;
        boolean invertH = invertZoneH;
        if (Math.signum(renderZone.width()) == -1) invertW = !invertW;
        if (Math.signum(renderZone.height()) == -1) invertH = !invertH;

        renderZone = renderZone.copy().absolute();
        InvoZone imageZone = this.getImageZoneCopy();

        refreshImage();

        RenderSystem.setShaderTexture(0, this.resourceLocation);

        if (this.imageZone.isSame(this.subImageZone)) {
            switch (this.outerOperation) {
                case NONE -> {
//                    LOGGER.warn("NONE ");
                    return;
                }
                case STRETCH -> {
//                    LOGGER.warn("STRETCH ");
                    if (invertW) imageZone.invertX();
                    if (invertH) imageZone.invertY();

                    //The pivot point is based on the main zone
                    this.rotate(stack, renderZone);
                    ClientUtil.blitImage(stack, renderZone, imageZone, this.imageScaleWidth, this.imageScaleHeight);
                    stack.popPose();
//                    LOGGER.warn("THIS HAS BEEN RENDERED!!!");
                }
                case TILE, MIRRORTILE -> {
//                    LOGGER.warn("TILE ");
                    this.renderTiles(stack, renderZone, this.outerOperation == ImageOperation.MIRRORTILE, invertW, invertH);
                }
            }
        } else {
//            LOGGER.warn("NINE SLICE ");
            this.renderNineSlice(stack, this.subImageZone.copy(), renderZone, invertW, invertH);
        }
    }

    public void refreshImage(){
        if (this.localResourceReloadCount == globalResourceReloadCount) return;
        this.localResourceReloadCount = globalResourceReloadCount;

        TextureManager tManager = ClientUtil.getTextureManager();
        tManager.byPath.remove(this.resourceLocation);
        tManager.getTexture(this.resourceLocation).bind();

        try (InputStream stream = ClientUtil.getMinecraft().getResourceManager().open(this.resourceLocation)){
            NativeImage image = NativeImage.read(stream);

            InvoZone updatedZone = new InvoZone(0, image.getWidth(), 0, image.getHeight());

            this.imageScaleWidth = updatedZone.width();
            this.imageScaleHeight = updatedZone.height();

            this.imageZone.changeRelativeMultiply(this.originalTextureZone, updatedZone);
            this.subImageZone.changeRelativeMultiply(this.originalTextureZone, updatedZone);
            this.originalTextureZone.copy(updatedZone);
        } catch (IOException e) {
            LOGGER.error("[Invocore] Couldn't find " + this.getResourceLocation());
            this.localResourceReloadCount--;
        }

    }

    public InvoImageTexture crop(InvoZone imageCropZone, boolean invertZoneW, boolean invertZoneH) {
        InvoImageTexture cropImage = this.copy();
        cropImage.setInnerOperation(ImageOperation.STRETCH);
        cropImage.setOuterOperation(ImageOperation.STRETCH);
//        LOGGER.warn("What's the copy render zone: " + renderZone);

//        cropImage.setImageWithRenderWidth(renderCropZone.width());
//        cropImage.setImageWithRenderHeight(renderCropZone.height());
//        cropImage.shiftImageX(imageCropZone.x() - cropImage.imageZone.x());
//        cropImage.shiftImageY(imageCropZone.y() - cropImage.imageZone.y());
        cropImage.setImageZone(imageCropZone);
        cropImage.setSubImageZone(cropImage.getImageZoneCopy());

        cropImage.invertZoneW = invertZoneW;
        cropImage.invertZoneH = invertZoneH;
        
        return cropImage;
    }

    private void renderNineSlice(PoseStack stack, InvoZone subImageZone, InvoZone renderZone, boolean invertZoneW, boolean invertZoneH) {
        InvoZone sizedImageZone = this.imageZone.copy().multiply(this.maxImageSize).setBound(renderZone, true);
        InvoZone sizedSubImageZone = getSubRenderZone(sizedImageZone);

        float originalSize = this.maxImageSize;
        float adjustedMaxSize = sizedImageZone.width()/this.originalTextureZone.width();
        this.setMaxImageSize(adjustedMaxSize);

        //Top Left
        InvoImageTexture topLeftImage = this.crop(InvoZone.fromPoints(imageZone.topLeft(), subImageZone.topLeft()), invertZoneW, invertZoneH);
        topLeftImage.setMainZone(topLeftImage.getImageZoneCopy().changeRelativeMultiply(this.imageZone, sizedImageZone)
                .changeRelativeAdd(sizedImageZone, renderZone, InvoZone.ANCHORPOINT.TOP_LEFT),false);
        topLeftImage.invertImage(invertZoneW, invertZoneH);
        if (invertZoneW) topLeftImage.mainZone.mirrorX(renderZone.middleX());
        if (invertZoneH) topLeftImage.mainZone.mirrorY(renderZone.middleY());
        topLeftImage.render(stack);

//        //Bottom Left
        InvoImageTexture bottomLeftImage = this.crop(InvoZone.fromPoints(imageZone.bottomLeft(), subImageZone.bottomLeft()), invertZoneW, invertZoneH);
        bottomLeftImage.setMainZone(bottomLeftImage.getImageZoneCopy().changeRelativeMultiply(this.imageZone, sizedImageZone)
                .changeRelativeAdd(sizedImageZone, renderZone, InvoZone.ANCHORPOINT.BOTTOM_LEFT), false);
        bottomLeftImage.invertImage(invertZoneW, invertZoneH);
        if (invertZoneW) bottomLeftImage.mainZone.mirrorX(renderZone.middleX());
        if (invertZoneH) bottomLeftImage.mainZone.mirrorY(renderZone.middleY());
        bottomLeftImage.render(stack);

        //Bottom Right
        InvoImageTexture bottomRightImage = this.crop(InvoZone.fromPoints(imageZone.bottomRight(), subImageZone.bottomRight()), invertZoneW, invertZoneH);
        bottomRightImage.setMainZone(bottomRightImage.getImageZoneCopy().changeRelativeMultiply(this.imageZone, sizedImageZone)
                .changeRelativeAdd(sizedImageZone, renderZone, InvoZone.ANCHORPOINT.BOTTOM_RIGHT), false);
        bottomRightImage.invertImage(invertZoneW, invertZoneH);
        if (invertZoneW) bottomRightImage.mainZone.mirrorX(renderZone.middleX());
        if (invertZoneH) bottomRightImage.mainZone.mirrorY(renderZone.middleY());
        bottomRightImage.render(stack);

        //Top Right
        InvoImageTexture topRightImage = this.crop(InvoZone.fromPoints(imageZone.topRight(), subImageZone.topRight()), invertZoneW, invertZoneH);
        topRightImage.setMainZone(topRightImage.getImageZoneCopy().changeRelativeMultiply(this.imageZone, sizedImageZone)
                .changeRelativeAdd(sizedImageZone, renderZone, InvoZone.ANCHORPOINT.TOP_RIGHT),false);
        topRightImage.invertImage(invertZoneW, invertZoneH);
        if (invertZoneW) topRightImage.mainZone.mirrorX(renderZone.middleX());
        if (invertZoneH) topRightImage.mainZone.mirrorY(renderZone.middleY());
        topRightImage.render(stack);

        InvoImageTexture topImage = this.crop(InvoZone.fromPoints(subImageZone.topLeft(), new Vector2f(subImageZone.right(), imageZone.y())), invertZoneW, invertZoneH);

        InvoImageTexture bottomImage = this.crop(InvoZone.fromPoints(subImageZone.bottomLeft(), new Vector2f(subImageZone.right(), imageZone.down())), invertZoneW, invertZoneH);

        InvoImageTexture leftImage = this.crop(InvoZone.fromPoints(subImageZone.topLeft(), new Vector2f(imageZone.x(), subImageZone.down())), invertZoneW, invertZoneH);

        InvoImageTexture rightImage = this.crop(InvoZone.fromPoints(subImageZone.topRight(), new Vector2f(imageZone.right(), subImageZone.down())), invertZoneW, invertZoneH);

        switch (this.outerOperation) {
            case NONE -> {
            }
            case STRETCH -> {
                topImage.render(stack, topLeftImage.mainZone.copy().intersect(topRightImage.mainZone));

                bottomImage.render(stack, bottomLeftImage.mainZone.intersect(bottomRightImage.mainZone));

                leftImage.render(stack, topLeftImage.mainZone.intersect(bottomLeftImage.mainZone));

                rightImage.render(stack, topRightImage.mainZone.intersect(bottomRightImage.mainZone));
            }
            case TILE, MIRRORTILE -> {
                boolean isMirror = this.outerOperation == ImageOperation.MIRRORTILE;

//                LOGGER.warn("Top: " + topImage.imageZone.toString());
                topImage.renderTiles(stack, topLeftImage.mainZone.copy().intersect(topRightImage.mainZone), isMirror, invertZoneW, invertZoneH);
//                LOGGER.warn("Bottom: " + bottomImage.imageZone.toString());
                bottomImage.renderTiles(stack, bottomLeftImage.mainZone.intersect(bottomRightImage.mainZone), isMirror, invertZoneW, invertZoneH);
//                LOGGER.warn("Left: " + leftImage.imageZone.toString());
                leftImage.renderTiles(stack, topLeftImage.mainZone.intersect(bottomLeftImage.mainZone), isMirror, invertZoneW, invertZoneH);
//                LOGGER.warn("Right: " + rightImage.imageZone.toString());
                rightImage.renderTiles(stack, topRightImage.mainZone.intersect(bottomRightImage.mainZone), isMirror, invertZoneW, invertZoneH);
            }
        }

        InvoImageTexture middleImage = this.crop(subImageZone, invertZoneW, invertZoneH);
        middleImage.setOuterOperation(this.innerOperation);

//        LOGGER.warn("DO INNER OPERATIONS ");

        switch (this.innerOperation) {
            case NONE -> {
            }
            case STRETCH -> {
                middleImage.render(stack, topLeftImage.mainZone.intersect(bottomRightImage.mainZone));
            }
            case TILE, MIRRORTILE -> {
                boolean isMirror = this.innerOperation == ImageOperation.MIRRORTILE;
                middleImage.renderTiles(stack, topLeftImage.mainZone.intersect(bottomRightImage.mainZone), isMirror, invertZoneW, invertZoneH);
            }
        }

        this.setMaxImageSize(originalSize);
    }

    private void renderTiles(PoseStack stack, InvoZone tileZone, boolean mirror, boolean invertZoneW, boolean invertZoneH) {
        tileZone = tileZone.absolute();
        if (tileZone.isZero()) return;

        float currentX = tileZone.x();
        float currentY = tileZone.y();
        boolean mirrorX = false;
        boolean mirrorY = false;

        while (currentY != tileZone.down()) {
            InvoImageTexture copyImage = this.copy();
            copyImage.invertZoneW = invertZoneW;
            copyImage.invertZoneH = invertZoneH;

            copyImage.setSubImageZone(copyImage.getImageZoneCopy());
            copyImage.setOuterOperation(ImageOperation.STRETCH);
            copyImage.setInnerOperation(ImageOperation.STRETCH);
            InvoZone copyZone = copyImage.getAdjustedImageZone();

            copyZone.setWidth(Math.min(copyZone.width(), tileZone.right() - currentX))
                    .setHeight(Math.min(copyZone.height(), tileZone.down() - currentY));
//            LOGGER.warn("What's height: " + copyZone.height());
            copyImage.setImageZone(copyZone.copy().changeRelativeMultiply(copyImage.getAdjustedImageZone(), copyImage.imageZone));
            copyZone.setX(currentX).setY(currentY);

            if (mirror && mirrorX) copyImage.setImageZone(copyImage.getImageZoneCopy().invertX());
            if (mirror && mirrorY) copyImage.setImageZone(copyImage.getImageZoneCopy().invertY());

            copyImage.render(stack, copyZone);

            currentX = copyZone.copy().absolute().right();

            mirrorX = !mirrorX;

            if (currentX == tileZone.right()) {
                currentX = tileZone.x();
                currentY = copyZone.copy().absolute().down();

                mirrorX = false;
                mirrorY = !mirrorY;
            }


        }
    }

    public void shiftImageX(float newImageX) {
        InvoZone adjustedImageZone = this.getAdjustedImageZone();
        float movePercentage = (newImageX - adjustedImageZone.x()) / adjustedImageZone.width();

        this.setImageZone(this.getImageZoneCopy()
                .shiftXY(this.imageZone.width() * movePercentage, 0));
    }

    public void shiftImageY(float newRenderY) {
        InvoZone adjustedImageZone = this.getAdjustedImageZone();
        float movePercentage = (newRenderY - adjustedImageZone.y()) / adjustedImageZone.height();

        this.setImageZone(this.getImageZoneCopy()
                .shiftXY(0, this.imageZone.height() * movePercentage));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putString(InvoImage.IMAGE_TYPE, TEXTURE_IMAGE);
        tag.putString(TEXTURE_RESOURCE_LOCATION, this.getResourceLocation().toString());
        tag.put(ORIGINAL_TEXTURE_ZONE, this.originalTextureZone.serializeNBT());
        tag.put(IMAGE_ZONE, this.imageZone.serializeNBT());
        tag.put(SUB_IMAGE_ZONE, this.subImageZone.serializeNBT());
        tag.putFloat(IMAGE_SCALE_WIDTH_FLOAT, this.imageScaleWidth);
        tag.putFloat(IMAGE_SCALE_HEIGHT_FLOAT, this.imageScaleHeight);
        tag.putFloat(MAX_IMAGE_SIZE_FLOAT, this.maxImageSize);
        tag.putString(OUTER_OPERATION_ENUM, this.outerOperation.name());
        tag.putString(INNER_OPERATION_ENUM, this.innerOperation.name());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
//        LOGGER.warn("What's my main zone? " + this.mainZone.toString());
        this.resourceLocation = ResourceUtil.create(tag.getString(TEXTURE_RESOURCE_LOCATION));
        this.originalTextureZone.copy(InvoZone.fromTag(tag.getCompound(ORIGINAL_TEXTURE_ZONE)));
        this.setImageZone(InvoZone.fromTag(tag.getCompound(IMAGE_ZONE)));
        this.setSubImageZone(InvoZone.fromTag(tag.getCompound(SUB_IMAGE_ZONE)));
        this.imageScaleWidth = tag.getFloat(IMAGE_SCALE_WIDTH_FLOAT);
        this.imageScaleHeight = tag.getFloat(IMAGE_SCALE_HEIGHT_FLOAT);
        this.maxImageSize = tag.getFloat(MAX_IMAGE_SIZE_FLOAT);
        this.outerOperation = ImageOperation.valueOf(tag.getString(OUTER_OPERATION_ENUM));
        this.innerOperation = ImageOperation.valueOf(tag.getString(INNER_OPERATION_ENUM));
    }
}
