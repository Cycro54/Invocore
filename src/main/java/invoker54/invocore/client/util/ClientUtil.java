package invoker54.invocore.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import invoker54.invocore.client.Ticker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.NonnullDefault;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientUtil {
    public static final Minecraft mC = Minecraft.getInstance();
    public static final ItemRenderer ITEM_RENDERER = Minecraft.getInstance().getItemRenderer();
    public static final DecimalFormat d1 = new DecimalFormat("0.0");
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    //This will face the player dependent on the players position, NOT camera orientation.
    public static void drawWorldLine(MatrixStack stack, Vector3d origin, Vector3d target, float lineWidth, int color){
        stack.pushPose();
        Vector3d cam = mC.gameRenderer.getMainCamera().getPosition().reverse();
        stack.translate(cam.x(), cam.y(), cam.z());
        cam = cam.reverse();
        Matrix4f lastPos = stack.last().pose();

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        //This gives me the up/down vector of the plane
        Vector3d directionVector = target.vectorTo(cam).cross(origin.vectorTo(cam)).normalize();

        Vector3d originUP = origin.add(directionVector.scale(lineWidth/2F));
        Vector3d originDOWN = origin.add(directionVector.scale(-lineWidth/2F));
        Vector3d targetUP = target.add(directionVector.scale(lineWidth/2F));
        Vector3d targetDOWN = target.add(directionVector.scale(-lineWidth/2F));

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(lastPos, (float) originUP.x(), (float) originUP.y(), (float) originUP.z()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, (float)targetUP.x(), (float)targetUP.y(), (float)targetUP.z()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, (float)targetDOWN.x(), (float)targetDOWN.y(), (float)targetDOWN.z()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, (float)originDOWN.x(), (float)originDOWN.y(), (float)originDOWN.z()).color(f, f1, f2, f3).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        stack.popPose();
    }
    public static void drawWorldLine(MatrixStack stack, Vector3d origin, Vector3d target, float lineWidth, float u0, float imageWidth, float v0, float imageHeight, float imageScale){
        stack.pushPose();
        Vector3d cam = mC.gameRenderer.getMainCamera().getPosition().reverse();
//        Vector3d cam = mC.player.position().inverse();
        stack.translate(cam.x(), cam.y(), cam.z());
        cam = cam.reverse();
        Matrix4f lastPos = stack.last().pose();

        u0 /= imageScale;
        float u1 = u0 + (imageWidth/imageScale);
        v0 /= imageScale;
        float v1 = v0 + (imageHeight/imageScale);
        //This gives me the up/down vector of the plane
        Vector3d directionVector = target.vectorTo(cam).cross(origin.vectorTo(cam)).normalize();

        Vector3d originUP = origin.add(directionVector.scale(lineWidth/2F));
        Vector3d originDOWN = origin.add(directionVector.scale(-lineWidth/2F));
        Vector3d targetUP = target.add(directionVector.scale(lineWidth/2F));
        Vector3d targetDOWN = target.add(directionVector.scale(-lineWidth/2F));

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.enableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(lastPos, (float) originUP.x(), (float) originUP.y(), (float) originUP.z()).uv(u0, v0).endVertex();
        bufferbuilder.vertex(lastPos, (float)targetUP.x(), (float)targetUP.y(), (float)targetUP.z()).uv(u1, v0).endVertex();
        bufferbuilder.vertex(lastPos, (float)targetDOWN.x(), (float)targetDOWN.y(), (float)targetDOWN.z()).uv(u1, v1).endVertex();
        bufferbuilder.vertex(lastPos, (float)originDOWN.x(), (float)originDOWN.y(), (float)originDOWN.z()).uv(u0, v1).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        stack.popPose();
    }
    public static void blitImage(MatrixStack stack, InvoZone renderZone, InvoZone imageZone, float imageScale){
        blitImage(stack, renderZone.x(), renderZone.width(), renderZone.y(), renderZone.height(),
                imageZone.x(), imageZone.width(), imageZone.y(), imageZone.height(), imageScale);
    }
    public static void blitImage(MatrixStack stack, float x0, float width, float y0, float height, float u0, float imageWidth, float v0, float imageHeight, float imageScale){
        Matrix4f lastPos = stack.last().pose();
        float x1 = x0 + width;
        float y1 = y0 + height;
        u0 /= imageScale;
        float u1 = u0 + (imageWidth/imageScale);
        v0 /= imageScale;
        float v1 = v0 + (imageHeight/imageScale);

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(lastPos, x0, y1, (float)0).uv(u0, v1).endVertex();
        bufferbuilder.vertex(lastPos, x1, y1, (float)0).uv(u1, v1).endVertex();
        bufferbuilder.vertex(lastPos, x1, y0, (float)0).uv(u1, v0).endVertex();
        bufferbuilder.vertex(lastPos, x0, y0, (float)0).uv(u0, v0).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.enableDepthTest();
    }
    public static void blitColor(MatrixStack stack, InvoZone renderZone, int color){
        blitColor(stack, renderZone.x(), renderZone.width(), renderZone.y(), renderZone.height(), color);
    }
    public static void blitColor(MatrixStack stack, float x0, float width, float y0, float height, int color){
        Matrix4f lastPos = stack.last().pose();
        float x1 = x0 + width;
        float y1 = y0 + height;

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(lastPos, x0, y1, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, x1, y1, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, x1, y0, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(lastPos, x0, y0, (float)0).color(f, f1, f2, f3).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }
    public static void blitItem(MatrixStack stack, InvoZone renderZone, ItemStack itemStack){
        blitItem(stack, renderZone.x(), renderZone.width(), renderZone.y(), renderZone.height(), itemStack);
    }
    public static void blitItem(MatrixStack stack, float x0, float width, float y0, float height, ItemStack itemStack){
//        RenderHelper.setupGuiFlatDiffuseLighting();
        ItemRenderer renderer = mC.getItemRenderer();
        IBakedModel bakedModel = renderer.getItemModelShaper().getItemModel(itemStack);
        RenderSystem.disableDepthTest();
//        RenderSystem.enableBlend();
//        RenderSystem.defaultAlphaFunc();

        RenderSystem.pushMatrix();
        RenderSystem.enableRescaleNormal();
        RenderSystem.translatef(x0, y0, 0);
        RenderSystem.translatef(width/2, height/2, 100 + renderer.blitOffset);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(width, height, 16);
//        RenderSystem.mulTextureByProjModelView();
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !bakedModel.usesBlockLight();
        if (flag) {
            RenderHelper.setupForFlatItems();
        }

        renderer.render(itemStack, ItemCameraTransforms.TransformType.GUI, false, stack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
        irendertypebuffer$impl.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) {
            RenderHelper.setupFor3DItems();
        }

        RenderSystem.disableBlend();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
//        RenderSystem.mulTextureByProjModelView();
    }
    public static PlayerEntity getPlayer() {
        return ClientUtil.mC.player;
    }
    public static World getWorld(){
        return ClientUtil.mC.level;
    }
    public static Vector3d smoothLerp(Vector3d oldPos, Vector3d newPos, boolean useDelta){
//        LOGGER.debug("PARTIAL TICK IN CLIENT UTIL IS: " + ClientUtil.mC.getFrameTime());
        return new Vector3d(
                smoothLerp(oldPos.x, newPos.x, useDelta),
                smoothLerp(oldPos.y, newPos.y, useDelta),
                smoothLerp(oldPos.z, newPos.z, useDelta));
    }
    public static double smoothLerp(double oldDouble, double newDouble, boolean useDelta){
        return MathHelper.lerp(useDelta ? Ticker.getDelta(true,true) : ClientUtil.mC.getFrameTime(),oldDouble,newDouble);
    }

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
        double scale = mC.getWindow().getGuiScale();
        int windowHeight = mC.getWindow().getGuiScaledHeight();

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

    //TODO: Remove in a later version
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
        public void render(MatrixStack stack, int xMouse, int yMouse, float partialTicks) {
            if (hidden) return;

            FontRenderer font = mC.font;
            getTextureManager().bind(WIDGETS_LOCATION);
            int i = this.getYImage(this.isHovered());
            i = 46 + i * 20;

            //left part of the button
            InvoZone renderZone = new InvoZone(this.x, this.width / 2, this.y, this.height);
            InvoZone imageZone = new InvoZone(0, this.width / 2, i, 20);
            ClientUtil.blitImage(stack, renderZone, imageZone, 256);
//            //left part of the button
//            this.blit(stack, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);

            //right part of the button
            renderZone.setX(renderZone.right());
            imageZone.setX((200 - imageZone.width()));
            ClientUtil.blitImage(stack, renderZone, imageZone, 256);
//            //right part of the button
//            this.blit(stack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);

            int j = getFGColor();
            drawCenteredString(stack, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
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
        return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+ ", "") : formattedNumber;
    }

    @Deprecated
    public static void drawStretchText(MatrixStack stack, String text, float currSize, int targSize, int x, int y, int color, boolean shadow){
        stack.pushPose();
        float newScale = targSize/currSize;
        stack.scale(newScale,newScale,newScale);

        //Must divide the position by the newScale due to the new difference
        x = Math.round(x/newScale);
        y = Math.round(y/newScale);

        if (shadow) ClientUtil.mC.font.drawShadow(stack, text, x, y, color);
        else ClientUtil.mC.font.draw(stack, text, x, y, color);

        stack.popPose();
    }

    public static TextureManager getTextureManager(){
        return mC.getTextureManager();
    }

    public static class Image {
        protected ResourceLocation location;
        protected final InvoZone imageZone;
        protected final InvoZone renderZone;
        protected float scale;

        public Image(ResourceLocation loc, float u0, float imageWidth, float v0, float imageHeight, float scale){
            this.location = loc;
            this.imageZone = new InvoZone(u0, imageWidth, v0, imageHeight);
            this.renderZone = new InvoZone(0, imageWidth, 0, imageHeight);
            this.scale = scale;
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

        public void render(MatrixStack stack){
            getTextureManager().bind(this.location);
            blitImage(stack, renderZone, imageZone, scale);
//            TEXTURE_MANAGER.release(this.location);
        }
    }

    public static class SimpleList extends AbstractList<ListEntry> {
        public final List<ITextComponent> toolTip = new ArrayList<>();
        Image background;
        int screenWidth;
        int screenHeight;
        protected ListEntry hoverEntry = null;


        //Height is used for how long you want the top and bottom panels to be if you had
        //renderTopAndBottom set to true.
        public SimpleList(int x0, int width, int y0, int height, int screenWidth, int screenHeight, Image background) {
            super(ClientUtil.mC, width, 0, y0, y0 + height, 30);
            this.x0 = x0;
            this.x1 = x0 + width;
            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
            this.setRenderHeader(false, 0);
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.background = background;

            LOGGER.debug("WHATS MY X0: " + x0);
            LOGGER.debug("WHATS MY WIDTH: " + width);
            LOGGER.debug("WHATS MY Y0: " + y0);
            LOGGER.debug("WHATS MY HEIGHT: " + height);
        }

        public void recalcWidth(){
            int width = 0;

            for (ListEntry entry : this.children()){
                if (entry.getWidth() > width){
                    width = entry.getWidth();
                }
            }

            //Set width
            this.width = width;
        }

        public void updatePosition(int x0, int y0, int height) {
            this.setLeftPos(x0);
            this.y0 = y0;
            this.y1 = y0 + height;
        }

        @Override
        public void render(MatrixStack stack, int xMouse, int yMouse, float partialTicks) {
            if (this.children().isEmpty()) return;

            super.render(stack, xMouse, yMouse, partialTicks);

            ClientUtil.beginCrop(this.x0, this.x1 - this.x0, this.y0, this.y1 - this.y0, true);
            if (!toolTip.isEmpty() && ClientUtil.mC.screen != null){
                GuiUtils.drawHoveringText(stack, this.toolTip, xMouse, yMouse, screenWidth, screenHeight,-1,mC.font);
                this.toolTip.clear();
            }
            ClientUtil.endCrop();
        }

//        protected int getRowTop(int p_230962_1_) {
//            return this.y0 + 4 - (int)this.getScrollAmount() + p_230962_1_ * this.itemHeight + this.headerHeight;
//        }

        @Override
        protected void renderList(MatrixStack stack, int p_238478_2_, int p_238478_3_, int xMouse, int yMouse, float p_238478_6_) {
            int i = this.getItemCount();
            hoverEntry = null;
//            Tessellator tessellator = Tessellator.getInstance();
//            BufferBuilder bufferbuilder = tessellator.getBuilder();

            int currY0 = (int) (this.y0 - this.getScrollAmount());
            for(int index = 0; index < i; ++index) {
                //Top
                int k = currY0;
                //Bottom
//                int l = k + this.getEntry(index).getHeight();
//                  int y0 = p_238478_3_ + index * this.itemHeight + this.headerHeight;
                int height = this.getEntry(index).getHeight();
                ListEntry e = this.getEntry(index);
                int k1 = this.getRowWidth();
//                    if (this.isSelectedItem(index)) {
//                        int l1 = this.x0 + this.width / 2 - k1 / 2;
//                        int i2 = this.x0 + this.width / 2 + k1 / 2;
//                        RenderSystem.disableTexture();
//                        float f = this.isFocused() ? 1.0F : 0.5F;
//                        RenderSystem.color4f(f, f, f, 1.0F);
//                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
//                        bufferbuilder.vertex((double)l1, (double)(y0 + height + 2), 0.0D).endVertex();
//                        bufferbuilder.vertex((double)i2, (double)(y0 + height + 2), 0.0D).endVertex();
//                        bufferbuilder.vertex((double)i2, (double)(y0 - 2), 0.0D).endVertex();
//                        bufferbuilder.vertex((double)l1, (double)(y0 - 2), 0.0D).endVertex();
//                        tessellator.end();
//                        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
//                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
//                        bufferbuilder.vertex((double)(l1 + 1), (double)(y0 + height + 1), 0.0D).endVertex();
//                        bufferbuilder.vertex((double)(i2 - 1), (double)(y0 + height + 1), 0.0D).endVertex();
//                        bufferbuilder.vertex((double)(i2 - 1), (double)(y0 - 1), 0.0D).endVertex();
//                        bufferbuilder.vertex((double)(l1 + 1), (double)(y0 - 1), 0.0D).endVertex();
//                        tessellator.end();
//                        RenderSystem.enableTexture();
//                    }

                int j2 = this.getRowLeft();
                e.render(stack, index, k, j2, k1, height, xMouse, yMouse, this.isMouseOver((double) xMouse, (double) yMouse) && Objects.equals(this.getEntryAtPosition((double) xMouse, (double) yMouse), e), p_238478_6_);
                currY0 += e.getHeight();
                if (e.isMouseOver(xMouse, yMouse)){
                    hoverEntry = e;
                }

            }
        }

        @Override
        public int getRowLeft() {
            return x0;
        }

        @Override
        public int addEntry(@NonnullDefault ListEntry entry) {
            return super.addEntry(entry);
        }

        @Override
        protected int getScrollbarPosition() {
            return x0 + getRowWidth();
        }

        @Override
        public int getRowWidth() {
            return this.width;
        }

        @Override
        public boolean mouseClicked(double xMouse, double yMouse, int button) {
            this.updateScrollingState(xMouse, yMouse, button);
            if (!this.isMouseOver(xMouse, yMouse)) {
                return false;
            } else {
                if (hoverEntry != null) {
                    if (hoverEntry.mouseClicked(xMouse, yMouse, button)) {
                        this.addEntry(hoverEntry);
                        this.setDragging(true);
                        return true;
                    }
                } else if (button == 0) {
                    this.clickedHeader((int)(xMouse - (double)(this.x0 + this.width / 2 - this.getRowWidth() / 2)), (int)(yMouse - (double)this.y0) + (int)this.getScrollAmount() - 4);
                    return true;
                }

                return false;
            }
        }
    }

    public static class ListEntry extends AbstractList.AbstractListEntry<ListEntry> {
        protected SimpleList parent;
        protected int height = 0;
        protected int heightPadding = 1;
        protected boolean isMouseOver = false;
        public ListEntry(SimpleList parent, int height){
            this.parent = parent;
            this.setHeight(height);
        }
        public int getWidth(){
            return 0;
        }
        public void setHeight(int newHeight){
            this.height = newHeight;
        }
        public int getHeight(){
            return this.height + (heightPadding * 2);
        }
        @Override
        public void render(MatrixStack stack, int index, int y0, int x0, int rowWidth, int rowHeight, int xMouse, int yMouse, boolean isMouseOver, float partialTicks
        ) {
            this.isMouseOver = xMouse >= x0 && xMouse <= (x0 + rowWidth) && yMouse >= y0 && yMouse <= (y0 + rowHeight);
        }

        @Override
        public boolean isMouseOver(double xMouse, double yMouse) {
            return this.isMouseOver;
        }
    }
}
