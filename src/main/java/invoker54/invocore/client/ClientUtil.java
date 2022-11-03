package invoker54.invocore.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ClientUtil {
    public static final Minecraft mC = Minecraft.getInstance();
    public static final TextureManager TEXTURE_MANAGER = Minecraft.getInstance().textureManager;
    public static final ItemRenderer ITEM_RENDERER = Minecraft.getInstance().getItemRenderer();
    public static final DecimalFormat d1 = new DecimalFormat("0.0");
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    //This will face the player dependent on the players position, NOT camera orientation.
    public static void drawWorldLine(MatrixStack stack, Vector3d origin, Vector3d target, float lineWidth, int color){
        stack.push();
        Vector3d cam = mC.gameRenderer.getActiveRenderInfo().getProjectedView().inverse();
        stack.translate(cam.getX(), cam.getY(), cam.getZ());
        cam = cam.inverse();
        Matrix4f lastPos = stack.getLast().getMatrix();

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        //This gives me the up/down vector of the plane
        Vector3d directionVector = target.subtractReverse(cam).crossProduct(origin.subtractReverse(cam)).normalize();

        Vector3d originUP = origin.add(directionVector.scale(lineWidth/2F));
        Vector3d originDOWN = origin.add(directionVector.scale(-lineWidth/2F));
        Vector3d targetUP = target.add(directionVector.scale(lineWidth/2F));
        Vector3d targetDOWN = target.add(directionVector.scale(-lineWidth/2F));

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(lastPos, (float) originUP.getX(), (float) originUP.getY(), (float) originUP.getZ()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(lastPos, (float)targetUP.getX(), (float)targetUP.getY(), (float)targetUP.getZ()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(lastPos, (float)targetDOWN.getX(), (float)targetDOWN.getY(), (float)targetDOWN.getZ()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(lastPos, (float)originDOWN.getX(), (float)originDOWN.getY(), (float)originDOWN.getZ()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        stack.pop();
    }
    public static void drawWorldLine(MatrixStack stack, Vector3d origin, Vector3d target, float lineWidth, float u0, float imageWidth, float v0, float imageHeight, float imageScale){
        stack.push();
        Vector3d cam = mC.gameRenderer.getActiveRenderInfo().getProjectedView().inverse();
//        Vector3d cam = mC.player.position().inverse();
        stack.translate(cam.getX(), cam.getY(), cam.getZ());
        cam = cam.inverse();
        Matrix4f lastPos = stack.getLast().getMatrix();

        u0 /= imageScale;
        float u1 = u0 + (imageWidth/imageScale);
        v0 /= imageScale;
        float v1 = v0 + (imageHeight/imageScale);
        //This gives me the up/down vector of the plane
        Vector3d directionVector = target.subtractReverse(cam).crossProduct(origin.subtractReverse(cam)).normalize();

        Vector3d originUP = origin.add(directionVector.scale(lineWidth/2F));
        Vector3d originDOWN = origin.add(directionVector.scale(-lineWidth/2F));
        Vector3d targetUP = target.add(directionVector.scale(lineWidth/2F));
        Vector3d targetDOWN = target.add(directionVector.scale(-lineWidth/2F));

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(lastPos, (float) originUP.getX(), (float) originUP.getY(), (float) originUP.getZ()).tex(u0, v0).endVertex();
        bufferbuilder.pos(lastPos, (float)targetUP.getX(), (float)targetUP.getY(), (float)targetUP.getZ()).tex(u1, v0).endVertex();
        bufferbuilder.pos(lastPos, (float)targetDOWN.getX(), (float)targetDOWN.getY(), (float)targetDOWN.getZ()).tex(u1, v1).endVertex();
        bufferbuilder.pos(lastPos, (float)originDOWN.getX(), (float)originDOWN.getY(), (float)originDOWN.getZ()).tex(u0, v1).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        stack.pop();
    }
    public static void blitImage(MatrixStack stack, int x0, int width, int y0, int height, float u0, float imageWidth, float v0, float imageHeight, float imageScale){
        Matrix4f lastPos = stack.getLast().getMatrix();
        int x1 = x0 + width;
        int y1 = y0 + height;
        u0 /= imageScale;
        float u1 = u0 + (imageWidth/imageScale);
        v0 /= imageScale;
        float v1 = v0 + (imageHeight/imageScale);

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(lastPos, (float)x0, (float)y1, (float)0).tex(u0, v1).endVertex();
        bufferbuilder.pos(lastPos, (float)x1, (float)y1, (float)0).tex(u1, v1).endVertex();
        bufferbuilder.pos(lastPos, (float)x1, (float)y0, (float)0).tex(u1, v0).endVertex();
        bufferbuilder.pos(lastPos, (float)x0, (float)y0, (float)0).tex(u0, v0).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.enableDepthTest();
    }
    public static void blitColor(MatrixStack stack, int x0, int width, int y0, int height, int color){
        Matrix4f lastPos = stack.getLast().getMatrix();
        int x1 = x0 + width;
        int y1 = y0 + height;

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(lastPos, (float)x0, (float)y1, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(lastPos, (float)x1, (float)y1, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(lastPos, (float)x1, (float)y0, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.pos(lastPos, (float)x0, (float)y0, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static Vector3d smoothLerp(Vector3d oldPos, Vector3d newPos, boolean useDelta){
//        LOGGER.debug("PARTIAL TICK IN CLIENT UTIL IS: " + ClientUtil.mC.getFrameTime());
        return new Vector3d(
                smoothLerp(oldPos.x, newPos.x, useDelta),
                smoothLerp(oldPos.y, newPos.y, useDelta),
                smoothLerp(oldPos.z, newPos.z, useDelta));
    }
    public static double smoothLerp(double oldDouble, double newDouble, boolean useDelta){
        return MathHelper.lerp(useDelta ? Ticker.getDelta(true,true) : ClientUtil.mC.getRenderPartialTicks(),oldDouble,newDouble);
    }

    public static void copyEntityMovement(LivingEntity copier, LivingEntity toCopy){
        copier.moveForced(toCopy.getPositionVec());
        copier.prevPosX = toCopy.prevPosX;
        copier.lastTickPosX = toCopy.lastTickPosX;
        copier.prevPosY = toCopy.prevPosY;
        copier.lastTickPosY = toCopy.lastTickPosY;
        copier.prevPosZ = toCopy.prevPosZ;
        copier.lastTickPosZ = toCopy.lastTickPosZ;
        copier.setMotion(toCopy.getMotion());
        copier.setRotationYawHead(toCopy.getRotationYawHead());
        copier.prevRotationYawHead = toCopy.prevRotationYawHead;
        copier.setRenderYawOffset(toCopy.renderYawOffset);
        copier.prevRenderYawOffset = toCopy.prevRenderYawOffset;
    }

    public static boolean inBounds (float xSpot, float ySpot, Bounds bounds){
        if (xSpot < bounds.x0 || xSpot > bounds.x1) return false;
        if (ySpot < bounds.y0 || ySpot > bounds.y1) return false;

        return  true;
    }
    protected static final ArrayList<Bounds> cropBounds = new ArrayList<>();
    public static void beginCrop (double x, double width, double y, double height, boolean fresh){
        if (fresh) cropBounds.add(new Bounds((int) x, (int) width, (int) y, (int) height));
//        Bounds bounds = cropBounds.get(cropBounds.size() - 1);
//        XPShop.LOGGER.debug((String.valueOf(x)) + (bounds.x0));
//        XPShop.LOGGER.debug((String.valueOf(width)) + (bounds.x1 - bounds.x0));
//        XPShop.LOGGER.debug((String.valueOf(y)) + (bounds.y0));
//        XPShop.LOGGER.debug((String.valueOf(height)) + (bounds.y1 - bounds.y0));
        double scale = mC.getMainWindow().getGuiScaleFactor();
        int windowHeight = mC.getMainWindow().getScaledHeight();

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

        public SimpleButton(int x, int y, int width, int height, ITextComponent textComponent, IPressable onPress) {
            super(x, y, width, height, textComponent, onPress);
            this.visible = true;
        }

        @Override
        public void renderWidget(MatrixStack stack, int xMouse, int yMouse, float partialTicks) {
            if (hidden) return;

            FontRenderer fontrenderer = mC.fontRenderer;
            TEXTURE_MANAGER.bindTexture(WIDGETS_LOCATION);
            int i = this.getYImage(this.isHovered());
            i = 46 + i * 20;

            //left part of the button
            ClientUtil.blitImage(stack, this.x,  this.width / 2, this.y, this.height,
                    0, this.width / 2f, i, 20, 256);
//            //left part of the button
//            this.blit(stack, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);

            //right part of the button
            ClientUtil.blitImage(stack, this.x + this.width / 2,  this.width/2, this.y, this.height,
                    200 - (this.width/2), this.width/2, i, 20, 256);
//            //right part of the button
//            this.blit(stack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);

            int j = getFGColor();
            drawCenteredString(stack, fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
        }
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
        return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;
    }

    public static class Image {

        protected ResourceLocation location;
        protected int u0;
        protected int v0;
        protected float imageWidth;
        protected float imageHeight;
        public int x0;
        protected int origWidth;
        protected int actualWidth;
        public int y0;
        protected int actualHeight;
        protected int origHeight;
        protected int scale;

        public Image(ResourceLocation loc, int u0, float imageWidth, int v0, float imageHeight, int scale){
            this.location = loc;
            this.u0 = u0;
            this.imageWidth = imageWidth;
            this.actualWidth = (int) imageWidth;
            this.origWidth = (int) imageWidth;
            this.v0 = v0;
            this.imageHeight = imageHeight;
            this.actualHeight = (int) imageHeight;
            this.origHeight = (int) imageHeight;
            this.scale = scale;
        }

        public void resetScale(){
            this.actualWidth = this.origWidth;
            this.actualHeight = this.origHeight;
        }

        public static int centerOnImageX(Image image, int targWidth){
            return image.x0 + ((image.actualWidth - targWidth)/2);
        }
        public static int centerOnImageY(Image image, int targHeight){
            return image.y0 + ((image.actualHeight - targHeight)/2);
        }
        public void centerImageX(int x0, int width){
//            LOGGER.debug("THIS IS MY OLD X ORIGIN: " + this.x0);
            this.x0 = (x0 + ((width - actualWidth)/2));
//            LOGGER.debug("THIS IS MY NEW X ORIGIN: " + this.x0);
        }
        public void centerImageY(int y0, int height){
            this.y0 = (y0 + ((height - actualHeight)/2));
        }

        public boolean isMouseOver(int mouseX, int mouseY){
            return mouseX >= x0 && mouseX <= (x0 + actualWidth) && mouseY >= y0 && mouseY <= (y0 + actualHeight);
        }

        public int getWidth(){
            return this.actualWidth;
        }
        public int getHeight(){
            return this.actualHeight;
        }

        public void moveTo(int x0, int y0){
            this.x0 = x0;
            this.y0 = y0;
        }

        public void setImageSize(float imageWidth, float imageHeight){
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
        }

        public void setActualSize(int actualWidth, int actualHeight){
            this.actualWidth = actualWidth;
            this.actualHeight = actualHeight;
        }

        public void RenderImage(MatrixStack stack){
            TEXTURE_MANAGER.bindTexture(this.location);
            blitImage(stack, x0, actualWidth, y0, actualHeight, u0, imageWidth, v0, imageHeight, scale);
            TEXTURE_MANAGER.deleteTexture(this.location);
        }
    }
}
