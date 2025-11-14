package invoker54.invocore.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ClientUtil {
    private static Minecraft mC;

    public static final DecimalFormat d1 = new DecimalFormat("0.0");
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static Font getFont(){
        return getMinecraft().font;
    }
    public static Minecraft getMinecraft(){
        if (mC == null) mC = Minecraft.getInstance();
        return mC;
    }
    public static Player getPlayer() {
        return getMinecraft().player;
    }
    public static Level getWorld(){
        return getMinecraft().level;
    }

    //This will face the player dependent on the players position, NOT camera orientation.
    public static void drawWorldLine(PoseStack stack, Vec3 origin, Vec3 target, float lineWidth, int color){

        stack.pushPose();
        Vec3 cam = getMinecraft().gameRenderer.getMainCamera().getPosition().reverse();
        stack.translate(cam.x(), cam.y(), cam.z());
        cam = cam.reverse();
        Matrix4f lastPos = stack.last().pose();

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        //This gives me the up/down vector of the plane
        Vec3 directionVector = target.vectorTo(cam).cross(origin.vectorTo(cam)).normalize();

        Vec3 originUP = origin.add(directionVector.scale(lineWidth/2F));
        Vec3 originDOWN = origin.add(directionVector.scale(-lineWidth/2F));
        Vec3 targetUP = target.add(directionVector.scale(lineWidth/2F));
        Vec3 targetDOWN = target.add(directionVector.scale(-lineWidth/2F));

        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
//        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(lastPos, (float) originUP.x(), (float) originUP.y(), (float) originUP.z()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, (float)targetUP.x(), (float)targetUP.y(), (float)targetUP.z()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, (float)targetDOWN.x(), (float)targetDOWN.y(), (float)targetDOWN.z()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, (float)originDOWN.x(), (float)originDOWN.y(), (float)originDOWN.z()).color(f, f1, f2, f3).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
//        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        stack.popPose();
    }
    public static void drawWorldLine(PoseStack stack, Vec3 origin, Vec3 target, float lineWidth, float u0, float imageWidth, float v0, float imageHeight, float imageScale){
        stack.pushPose();
        Vec3 cam = getMinecraft().gameRenderer.getMainCamera().getPosition().reverse();
//        Vector3d cam = getWorld().player.position().inverse();
        stack.translate(cam.x(), cam.y(), cam.z());
        cam = cam.reverse();
        Matrix4f lastPos = stack.last().pose();

        u0 /= imageScale;
        float u1 = u0 + (imageWidth/imageScale);
        v0 /= imageScale;
        float v1 = v0 + (imageHeight/imageScale);
        //This gives me the up/down vector of the plane
        Vec3 directionVector = target.vectorTo(cam).cross(origin.vectorTo(cam)).normalize();

        Vec3 originUP = origin.add(directionVector.scale(lineWidth/2F));
        Vec3 originDOWN = origin.add(directionVector.scale(-lineWidth/2F));
        Vec3 targetUP = target.add(directionVector.scale(lineWidth/2F));
        Vec3 targetDOWN = target.add(directionVector.scale(-lineWidth/2F));

        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
//        RenderSystem.enableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(lastPos, (float) originUP.x(), (float) originUP.y(), (float) originUP.z()).uv(u0, v0).endVertex();
        bufferbuilder.vertex(lastPos, (float)targetUP.x(), (float)targetUP.y(), (float)targetUP.z()).uv(u1, v0).endVertex();
        bufferbuilder.vertex(lastPos, (float)targetDOWN.x(), (float)targetDOWN.y(), (float)targetDOWN.z()).uv(u1, v1).endVertex();
        bufferbuilder.vertex(lastPos, (float)originDOWN.x(), (float)originDOWN.y(), (float)originDOWN.z()).uv(u0, v1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        
//        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        stack.popPose();
    }
    public static void blitImage(PoseStack stack, InvoZone renderZone, InvoZone imageZone, float imageScaleX, float imageScaleY){
        blitImage(stack, renderZone.x(), renderZone.width(), renderZone.y(), renderZone.height(),
                imageZone.x(), imageZone.width(), imageZone.y(), imageZone.height(), imageScaleX, imageScaleY);
    }
    public static void blitImage(PoseStack stack, float x0, float width, float y0, float height, float u0, float imageWidth, float v0, float imageHeight, float imageScaleX, float imageScaleY){
        Matrix4f lastPos = stack.last().pose();
        float x1 = x0 + width;
        float y1 = y0 + height;
        u0 /= imageScaleX;
        float u1 = u0 + (imageWidth/imageScaleX);
        v0 /= imageScaleY;
        float v1 = v0 + (imageHeight/imageScaleY);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(lastPos, x0, y1, (float)0).uv(u0, v1).endVertex();
        bufferbuilder.vertex(lastPos, x1, y1, (float)0).uv(u1, v1).endVertex();
        bufferbuilder.vertex(lastPos, x1, y0, (float)0).uv(u1, v0).endVertex();
        bufferbuilder.vertex(lastPos, x0, y0, (float)0).uv(u0, v0).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());

        RenderSystem.enableDepthTest();
    }
    public static void blitColor(PoseStack stack, InvoZone renderZone, int color){
        blitColor(stack, renderZone.x(), renderZone.width(), renderZone.y(), renderZone.height(), color);
    }
    public static void blitColor(PoseStack stack, float x0, float width, float y0, float height, int color){
        Matrix4f lastPos = stack.last().pose();
        float x1 = x0 + width;
        float y1 = y0 + height;

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;

        RenderSystem.enableBlend();
//        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(lastPos, x0, y1, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, x1, y1, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, x1, y0, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, x0, y0, (float)0).color(f, f1, f2, f3).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());

//        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }
    public static void blitItem(PoseStack stack, InvoZone renderZone, ItemStack itemStack){
        blitItem(stack, renderZone.x(), renderZone.width(), renderZone.y(), renderZone.height(), itemStack);
    }
    public static void blitItem(PoseStack stack, float x0, float width, float y0, float height, ItemStack itemStack){
//        getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
//        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableDepthTest();

        Lighting.setupForFlatItems();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        ItemRenderer renderer = getMinecraft().getItemRenderer();
        BakedModel bakedModel = renderer.getModel(itemStack, getWorld(), getPlayer(), 0);

        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(x0, y0, (double)(100.0F + ItemRenderer.ITEM_COUNT_BLIT_OFFSET));
        posestack.translate(width/2, height/2, 0.0D);
        posestack.scale(1.0F, -1.0F, 1.0F);
        posestack.scale(width, height, 16.0F);
        RenderSystem.applyModelViewMatrix();
        boolean flag = !bakedModel.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }
        else {
            Lighting.setupFor3DItems();
        }
        renderer.render(itemStack, ItemDisplayContext.GUI, false, stack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedModel);

        bufferSource.endBatch();
        if (flag) {
            Lighting.setupFor3DItems();
        }
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();

        RenderSystem.enableDepthTest();
    }
//    public static Vec3 smoothLerp(Vec3 oldPos, Vec3 newPos, boolean useDelta){
////        LOGGER.debug("PARTIAL TICK IN CLIENT UTIL IS: " + ClientUtil.getWorld().getFrameTime());
//        return new Vec3(
//                smoothLerp(oldPos.x, newPos.x, useDelta),
//                smoothLerp(oldPos.y, newPos.y, useDelta),
//                smoothLerp(oldPos.z, newPos.z, useDelta));
//    }
//    public static double smoothLerp(double oldDouble, double newDouble, boolean useDelta){
//        return Mth.lerp(useDelta ? Ticker.getDelta(true,true) : ClientUtil.getMinecraft().getFrameTime(),oldDouble,newDouble);
//    }

    public static void copyEntityMovement(LivingEntity copier, LivingEntity toCopy){
        copier.moveTo(toCopy.position());
        copier.xo = toCopy.xo;
        copier.xOld = toCopy.xOld;
        copier.yo = toCopy.yo;
        copier.yOld = toCopy.yOld;
        copier.zo = toCopy.zo;
        copier.zOld = toCopy.zOld;
        copier.setDeltaMovement(toCopy.getDeltaMovement());
        copier.setYHeadRot(toCopy.getYHeadRot());
        copier.yHeadRotO = toCopy.yHeadRotO;
        copier.setYBodyRot(toCopy.yBodyRot);
        copier.yBodyRotO = toCopy.yBodyRotO;
    }

    protected static final ArrayList<Bounds> cropBounds = new ArrayList<>();
    public static void beginCrop (double x, double width, double y, double height, boolean fresh){
        if (fresh) cropBounds.add(new Bounds((int) x, (int) width, (int) y, (int) height));
//        Bounds bounds = cropBounds.get(cropBounds.size() - 1);
//        XPShop.LOGGER.debug((String.valueOf(x)) + (bounds.x0));
//        XPShop.LOGGER.debug((String.valueOf(width)) + (bounds.x1 - bounds.x0));
//        XPShop.LOGGER.debug((String.valueOf(y)) + (bounds.y0));
//        XPShop.LOGGER.debug((String.valueOf(height)) + (bounds.y1 - bounds.y0));
        double scale = getMinecraft().getWindow().getGuiScale();
        int windowHeight = getMinecraft().getWindow().getGuiScaledHeight();

        //This is inverses y since scissor test requires it
        y = windowHeight - (height + y);

//        LOGGER.debug("The y before is: " + y);
//        LOGGER.debug("The height before is: " + height);
        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

//        LOGGER.debug("The y is: " + y);
//        LOGGER.debug("The height is: " + height);

        RenderSystem.enableScissor((int) x, (int) y, (int) width, (int) height);
        //LOGGER.debug("Start " + cropBounds.size());
    }

    public static void endCrop(){
        //LOGGER.debug("End " + cropBounds.size());
        if (cropBounds.size() != 0) cropBounds.remove(cropBounds.size() - 1);
        if (!cropBounds.isEmpty()) {
            Bounds cropBound = cropBounds.get(cropBounds.size() - 1);
            beginCrop(cropBound.x0, (cropBound.x1 - cropBound.x0), cropBound.y0, cropBound.y1 - cropBound.y0, false);
        }
        else {
            RenderSystem.disableScissor();
        }
    }

    public static class Bounds{
        int x0;
        int x1;
        int y0;
        int y1;

        public Bounds(int x, int width, int y, int height){
            this.x0 = x;
            this.x1 = x + width;
            this.y0 = y;
            this.y1 = y + height;
        }

        public Bounds(){}

        public void adjustBounds(int x, int width, int y, int height) {
            this.x0 = x;
            this.x1 = x + width;
            this.y0 = y;
            this.y1 = y + height;

        }

        public int getMinX() {
            return x0;
        }
        public int getMaxX() {
        return x1;
        }
        public int getMinY() {
        return y0;
        }
        public int getMaxY() {
        return y1;
        }
    }
    
    public static class SimpleButton extends Button {

        public boolean hidden = false;

        public SimpleButton(int x, int y, int width, int height, MutableComponent textComponent, OnPress onPress) {
            super(x, y, width, height, textComponent, onPress, (a)->textComponent);
            this.visible = true;
        }

        //TODO: Get this button fully working
//        @Override
//        public void render(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
//            if (hidden) return;
//
//            Font font = mC.font;
//            RenderSystem.setShaderTexture(0,WIDGETS_LOCATION);
////            int i = this.getYImage(this.isHovered);
////            i = 46 + i * 20;
//
//            //left part of the button
//            InvoZone renderZone = new InvoZone(this.getX(), this.width / 2, this.getY(), this.height);
//            InvoZone imageZone = new InvoZone(0, this.width / 2, i, 20);
//            ClientUtil.blitImage(guiGraphics.pose(), renderZone, imageZone, 256);
////            //left part of the button
////            this.blit(stack, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
//
//            //right part of the button
//            renderZone.setX(renderZone.right());
//            imageZone.setX((200 - imageZone.width()));
//            ClientUtil.blitImage(guiGraphics.pose(), renderZone, imageZone, 256);
//
//            int j = getFGColor();
//            drawCenteredString(guiGraphics.pose(), font, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
//        }
    }
    public static String ticksToTime(int ticks){
        //Each second is 20 ticks
        //each minute is 1200 ticks
        //Each hour is 72000 ticks
        //20 = ticks, 60 = seconds, 60 = minutes
        int hours = ticks/72000;
        ticks -= (hours * 7200);
        int minutes = ticks/1200;
        ticks -= (minutes * 1200);
        int seconds = ticks/20;

        return (hours <= 9 ? "0" : "") + hours + ":" +
                (minutes <= 9 ? "0" : "") + minutes + ":" +
                (seconds <= 9 ? "0" : "") + seconds;
    }

    public static String formatValue(double value) {
        if (value == 0) return "0";

        int power;
        String suffix = " KMBT";
        String formattedNumber = "";

        NumberFormat formatter = new DecimalFormat("#,###.#");
        power = (int)StrictMath.log10(value);
        value = value/(Math.pow(10,(power/3)*3));
        formattedNumber=formatter.format(value);
        formattedNumber = formattedNumber + suffix.charAt(power/3);
        return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+ ", "") : formattedNumber;
    }

    public static TextureManager getTextureManager(){
        return mC.getTextureManager();
    }

    public static class Image {
        protected ResourceLocation location;
        protected final InvoZone imageZone;
        protected final InvoZone renderZone;
        protected float scaleX;
        protected float scaleY;

        public Image(ResourceLocation loc, float u0, float imageWidth, float v0, float imageHeight, float scaleX, float scaleY){
            this.location = loc;
            this.imageZone = new InvoZone(u0, imageWidth, v0, imageHeight);
            this.renderZone = new InvoZone(0, imageWidth, 0, imageHeight);
            this.scaleX = scaleX;
            this.scaleY = scaleY;
        }

        public Image(ResourceLocation loc, float u0, float imageWidth, float v0, float imageHeight, float scale){
            this(loc,u0,imageWidth,v0,imageHeight,scale,scale);
        }

        public Image(Image originalImage){
            this(originalImage.location,
                    originalImage.imageZone.x(),
                    originalImage.imageZone.width(),
                    originalImage.imageZone.y(),
                    originalImage.imageZone.height(),
                    originalImage.scaleX,
                    originalImage.scaleY);
            this.renderZone.copy(originalImage.renderZone);
        }

        public void setLocation(ResourceLocation loc){
            this.location = loc;
        }

        public void resetScale(){
            this.renderZone.setWidth(this.imageZone.width());
            this.renderZone.setHeight(this.imageZone.height());
        }

        public InvoZone getRenderZone(){
            return this.renderZone;
        }

        public InvoZone getImageZone(){
            return this.imageZone;
        }

        public boolean isMouseOver(int mouseX, int mouseY){
            return mouseX >= renderZone.x() && mouseX <= (renderZone.x() + renderZone.width())
                    && mouseY >= renderZone.y() && mouseY <= (renderZone.y() + renderZone.height());
        }

        public void render(PoseStack stack){
            RenderSystem.setShaderTexture(0, this.location);
            blitImage(stack, renderZone, imageZone, this.scaleX, this.scaleY);
        }

        public Image crop(InvoZone cropZone){
            Image cropImage = new Image(this);
            cropImage.shiftImageX(cropZone.x());
            cropImage.shiftImageY(cropZone.y());
            cropImage.setRenderAndImageWidth(cropZone.width());
            cropImage.setRenderAndImageHeight(cropZone.height());

            return cropImage;
        }

        public enum NineSliceType{
            NONE,
            MIRROR,
            TILE,
            STRETCH
        }
        public void renderNineSlice(PoseStack stack, InvoZone cutOutZone, InvoZone newRenderZone, boolean tileSides, NineSliceType type){
            InvoZone cropZone = this.renderZone.copy();

            //Top Left
            Image topLeftImage = this.crop(cropZone.setRightWidth(cutOutZone.x()).setDownHeight(cutOutZone.y()));
            topLeftImage.getRenderZone().setX(newRenderZone.x()).setY(newRenderZone.y());
            topLeftImage.render(stack);

            //Bottom Left
            Image bottomLeftImage = this.crop(cropZone.setY(cutOutZone.down()).setDownHeight(this.renderZone.down()));
            bottomLeftImage.getRenderZone().setX(newRenderZone.x()).setDown(newRenderZone.down());
            bottomLeftImage.render(stack);

            //Bottom Right
            Image bottomRightImage = this.crop(cropZone.setX(cutOutZone.right()).setRightWidth(this.renderZone.right()));
            bottomRightImage.getRenderZone().setRight(newRenderZone.right()).setDown(newRenderZone.down());
            bottomRightImage.render(stack);

            //Top Right
            Image topRightImage = this.crop(cropZone.setY(this.renderZone.y()).setDownHeight(cutOutZone.y()));
            topRightImage.getRenderZone().setRight(newRenderZone.right()).setY(newRenderZone.y());
            topRightImage.render(stack);

            //Top
            Image topImage = this.crop(cutOutZone.copy().setY(this.renderZone.y()).setDownHeight(cutOutZone.y()));
            topImage.getRenderZone().setX(topLeftImage.renderZone.right()).setY(topLeftImage.renderZone.y());

            //Bottom
            Image bottomImage = this.crop(cutOutZone.copy().setY(cutOutZone.down()).setDownHeight(this.renderZone.down()));
            bottomImage.getRenderZone().setX(bottomLeftImage.renderZone.right()).setY(bottomLeftImage.renderZone.y());

            //Left
            Image leftImage = this.crop(cutOutZone.copy().setX(this.renderZone.x()).setRightWidth(cutOutZone.x()));
            leftImage.getRenderZone().setX(topLeftImage.renderZone.x()).setY(topLeftImage.renderZone.down());

            //Right
            Image rightImage = this.crop(cutOutZone.copy().setX(cutOutZone.right()).setRightWidth(this.renderZone.right()));
            rightImage.getRenderZone().setX(topRightImage.renderZone.x()).setY(topRightImage.renderZone.down());

            if (tileSides){
                topImage.renderTiles(stack, topImage.getRenderZone().copy().setRightWidth(topRightImage.renderZone.x()), false);
                bottomImage.renderTiles(stack, bottomImage.getRenderZone().copy().setRightWidth(bottomRightImage.renderZone.x()), false);
                leftImage.renderTiles(stack, leftImage.getRenderZone().copy().setDownHeight(bottomLeftImage.renderZone.y()), false);
                rightImage.renderTiles(stack, rightImage.getRenderZone().copy().setDownHeight(bottomRightImage.renderZone.y()), false);
            }
            else {
                topImage.renderZone.setRightWidth(topRightImage.renderZone.x());
                topImage.render(stack);

                bottomImage.renderZone.setRightWidth(bottomRightImage.renderZone.x());
                bottomImage.render(stack);

                leftImage.renderZone.setDownHeight(bottomLeftImage.renderZone.y());
                leftImage.render(stack);

                rightImage.renderZone.setDownHeight(bottomRightImage.renderZone.y());
                rightImage.render(stack);
            }

            Image middleImage = this.crop(cutOutZone);
            InvoZone middleZone = middleImage.getRenderZone().copy()
                    .setX(topLeftImage.renderZone.right()).setRightWidth(bottomRightImage.renderZone.x())
                    .setY(topLeftImage.renderZone.down()).setDownHeight(bottomRightImage.renderZone.y());

            switch (type){
                case TILE -> middleImage.renderTiles(stack, middleZone, false);
                case STRETCH -> {
                    middleImage.renderZone.copy(middleZone);
                    middleImage.render(stack);
                }
            }
        }

        public void renderTiles(PoseStack stack, InvoZone tileZone, boolean hollow){
            float currentX = tileZone.x();
            float currentY = tileZone.y();

            //I'll render by row
            while (currentY != tileZone.down()){

                Image copyImage = new Image(this);
                InvoZone copyZone = copyImage.renderZone;
                float newWidth = Math.min(this.renderZone.width(), tileZone.right() - currentX);
                float newHeight = Math.min(this.renderZone.height(), tileZone.down() - currentY);
                copyZone.setX(currentX).setY(currentY);

                copyImage.setRenderAndImageWidth(newWidth);
                copyImage.setRenderAndImageHeight(newHeight);

                boolean isBorderTile = copyZone.x() == tileZone.x() || copyZone.y() == tileZone.y() ||
                        copyZone.right() == tileZone.right() || copyZone.down() == tileZone.down();

                if (!hollow || isBorderTile) copyImage.render(stack);
                currentX = copyZone.right();

                if (currentX == tileZone.right()) {
                    currentX = tileZone.x();
                    currentY = copyZone.down();
                }
            }
        }

        public void setRenderAndImageWidth(float newRenderWidth){
            float normalizedImageWidth = this.imageZone.width()/this.renderZone.width();

            this.renderZone.setWidth(newRenderWidth);
            this.imageZone.setWidth(normalizedImageWidth * newRenderWidth);
        }

        public void setRenderAndImageHeight(float newRenderHeight){
            float normalizedImageHeight = this.imageZone.height()/this.renderZone.height();

            this.renderZone.setHeight(newRenderHeight);
            this.imageZone.setHeight(normalizedImageHeight * newRenderHeight);
        }

        public void shiftImageX(float newRenderX){
            float movePercentage = (newRenderX-this.renderZone.x())/this.renderZone.width();

            this.imageZone.shift(this.imageZone.width() * movePercentage, 0);
        }

        public void shiftImageY(float newRenderY){
            float movePercentage = (newRenderY-this.renderZone.y())/this.renderZone.height();

            this.imageZone.shift(0, this.imageZone.height() * movePercentage);
        }
    }
//
//    public static class SimpleList extends AbstractSelectionList<ListEntry> {
//        public final List<Component> toolTip = new ArrayList<>();
//        Image background;
//        int screenWidth;
//        int screenHeight;
//        protected ListEntry hoverEntry = null;
//
//
//        //Height is used for how long you want the top and bottom panels to be if you had
//        //renderTopAndBottom set to true.
//        public SimpleList(int x0, int width, int y0, int height, int screenWidth, int screenHeight, Image background) {
//            super(ClientUtil.getWorld(), width, 0, y0, y0 + height, 30);
//            this.x0 = x0;
//            this.x1 = x0 + width;
//            this.setRenderBackground(false);
//            this.setRenderTopAndBottom(false);
//            this.setRenderHeader(false, 0);
//            this.screenWidth = screenWidth;
//            this.screenHeight = screenHeight;
//            this.background = background;
//
//            LOGGER.debug("WHATS MY X0: " + x0);
//            LOGGER.debug("WHATS MY WIDTH: " + width);
//            LOGGER.debug("WHATS MY Y0: " + y0);
//            LOGGER.debug("WHATS MY HEIGHT: " + height);
//        }
//
//        public void recalcWidth(){
//            int width = 0;
//
//            for (ListEntry entry : this.children()){
//                if (entry.getWidth() > width){
//                    width = entry.getWidth();
//                }
//            }
//
//            //Set width
//            this.width = width;
//        }
//
//        public void updatePosition(int x0, int y0, int height) {
//            this.setLeftPos(x0);
//            this.y0 = y0;
//            this.y1 = y0 + height;
//        }
//        @Override
//        public void render(PoseStack stack, int xMouse, int yMouse, float partialTicks) {
//            ItemStack f;
//            if (this.children().isEmpty()) return;
//
//            super.render(stack, xMouse, yMouse, partialTicks);
//
//            ClientUtil.beginCrop(this.x0, this.x1 - this.x0, this.y0, this.y1 - this.y0, true);
//            if (!toolTip.isEmpty() && ClientUtil.getWorld().screen != null) {
//                ClientUtil.getWorld().screen.renderComponentTooltip(stack, this.toolTip, xMouse, yMouse);
//                this.toolTip.clear();
//            }
//            ClientUtil.endCrop();
//        }
//
////        protected int getRowTop(int p_230962_1_) {
////            return this.y0 + 4 - (int)this.getScrollAmount() + p_230962_1_ * this.itemHeight + this.headerHeight;
////        }
//
//        @Override
//        protected void renderList(@NotNull PoseStack stack, int xMouse, int yMouse, float p_238478_6_) {
//            int i = this.getItemCount();
//            hoverEntry = null;
////            Tessellator tessellator = Tessellator.getInstance();
////            BufferBuilder bufferbuilder = tessellator.getBuilder();
//
//            int currY0 = (int) (this.y0 - this.getScrollAmount());
//            for(int index = 0; index < i; ++index) {
//                //Top
//                int k = currY0;
//                //Bottom
////                int l = k + this.getEntry(index).getHeight();
////                  int y0 = p_238478_3_ + index * this.itemHeight + this.headerHeight;
//                int height = this.getEntry(index).getHeight();
//                ListEntry e = this.getEntry(index);
//                int k1 = this.getRowWidth();
////                    if (this.isSelectedItem(index)) {
////                        int l1 = this.x0 + this.width / 2 - k1 / 2;
////                        int i2 = this.x0 + this.width / 2 + k1 / 2;
////                        RenderSystem.disableTexture();
////                        float f = this.isFocused() ? 1.0F : 0.5F;
////                        RenderSystem.color4f(f, f, f, 1.0F);
////                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
////                        bufferbuilder.vertex((double)l1, (double)(y0 + height + 2), 0.0D).endVertex();
////                        bufferbuilder.vertex((double)i2, (double)(y0 + height + 2), 0.0D).endVertex();
////                        bufferbuilder.vertex((double)i2, (double)(y0 - 2), 0.0D).endVertex();
////                        bufferbuilder.vertex((double)l1, (double)(y0 - 2), 0.0D).endVertex();
////                        tessellator.end();
////                        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
////                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
////                        bufferbuilder.vertex((double)(l1 + 1), (double)(y0 + height + 1), 0.0D).endVertex();
////                        bufferbuilder.vertex((double)(i2 - 1), (double)(y0 + height + 1), 0.0D).endVertex();
////                        bufferbuilder.vertex((double)(i2 - 1), (double)(y0 - 1), 0.0D).endVertex();
////                        bufferbuilder.vertex((double)(l1 + 1), (double)(y0 - 1), 0.0D).endVertex();
////                        tessellator.end();
////                        RenderSystem.enableTexture();
////                    }
//
//                int j2 = this.getRowLeft();
//                e.render(stack, index, k, j2, k1, height, xMouse, yMouse, this.isMouseOver((double) xMouse, (double) yMouse) && Objects.equals(this.getEntryAtPosition((double) xMouse, (double) yMouse), e), p_238478_6_);
//                currY0 += e.getHeight();
//                if (e.isMouseOver(xMouse, yMouse)){
//                    hoverEntry = e;
//                }
//
//            }
//        }
//
//        @Override
//        public int getRowLeft() {
//            return x0;
//        }
//
//        @Override
//        public int addEntry(@Nonnull ListEntry entry) {
//            return super.addEntry(entry);
//        }
//
//        @Override
//        protected int getScrollbarPosition() {
//            return x0 + getRowWidth();
//        }
//
//        @Override
//        public int getRowWidth() {
//            return this.width;
//        }
//
//        @Override
//        public boolean mouseClicked(double xMouse, double yMouse, int button) {
//            this.updateScrollingState(xMouse, yMouse, button);
//            if (!this.isMouseOver(xMouse, yMouse)) {
//                return false;
//            } else {
//                if (hoverEntry != null) {
//                    if (hoverEntry.mouseClicked(xMouse, yMouse, button)) {
//                        this.setFocused(hoverEntry);
//                        this.setDragging(true);
//                        return true;
//                    }
//                } else if (button == 0) {
//                    this.clickedHeader((int)(xMouse - (double)(this.x0 + this.width / 2 - this.getRowWidth() / 2)), (int)(yMouse - (double)this.y0) + (int)this.getScrollAmount() - 4);
//                    return true;
//                }
//
//                return false;
//            }
//        }
//
//        @Override
//        public void updateNarration(NarrationElementOutput p_169152_) {
//
//        }
//    }
//
//    public static class ListEntry extends AbstractSelectionList.Entry<ListEntry> {
//        protected SimpleList parent;
//        protected int height = 0;
//        protected int heightPadding = 1;
//        protected boolean isMouseOver = false;
//        public ListEntry(SimpleList parent, int height){
//            this.parent = parent;
//            this.setHeight(height);
//        }
//        public int getWidth(){
//            return 0;
//        }
//        public void setHeight(int newHeight){
//            this.height = newHeight;
//        }
//        public int getHeight(){
//            return this.height + (heightPadding * 2);
//        }
//        @Override
//        public void render(PoseStack stack, int index, int y0, int x0, int rowWidth, int rowHeight, int xMouse, int yMouse, boolean isMouseOver, float partialTicks
//        ) {
//            this.isMouseOver = xMouse >= x0 && xMouse <= (x0 + rowWidth) && yMouse >= y0 && yMouse <= (y0 + rowHeight);
//        }
//
//        @Override
//        public boolean isMouseOver(double xMouse, double yMouse) {
//            return this.isMouseOver;
//        }
//    }
}
