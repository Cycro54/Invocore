package invoker54.invocore.client.util;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import invoker54.invocore.client.Ticker;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.pip.OversizedItemRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.client.renderer.state.gui.ColoredRectangleRenderState;
import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
import net.minecraft.client.renderer.state.gui.pip.OversizedItemRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClientUtil {
    private static Minecraft mC;

    public static final DecimalFormat d1 = new DecimalFormat("0.0");
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static Function<RenderPipeline, RenderPipeline> pipelineConvertor;
//    public static final List<Runnable> cachedRenderList = new ArrayList<>();

    public static Font getFont() {
        return getMinecraft().font;
    }

    public static Minecraft getMinecraft() {
        if (mC == null) mC = Minecraft.getInstance();
        return mC;
    }

    public static Player getPlayer() {
        return getMinecraft().player;
    }

    public static Level getWorld() {
        return getMinecraft().level;
    }

    //This will face the player dependent on the players position, NOT camera orientation.
//    public static void drawWorldLine(Matrix3x2fStack stack, Vec3 origin, Vec3 target, float lineWidth, int color){
//
//        stack.pushPose();
//        Vec3 cam = getMinecraft().gameRenderer.getMainCamera().getPosition().reverse();
//        stack.translate(cam.x(), cam.y(), cam.z());
//        cam = cam.reverse();
//        Matrix4f lastPos = stack.last().pose();
//
//        float f3 = (float)(color >> 24 & 255) / 255.0F;
//        float f = (float)(color >> 16 & 255) / 255.0F;
//        float f1 = (float)(color >> 8 & 255) / 255.0F;
//        float f2 = (float)(color & 255) / 255.0F;
//        //This gives me the up/down vector of the plane
//        Vec3 directionVector = target.vectorTo(cam).cross(origin.vectorTo(cam)).normalize();
//
//        Vec3 originUP = origin.add(directionVector.scale(lineWidth/2F));
//        Vec3 originDOWN = origin.add(directionVector.scale(-lineWidth/2F));
//        Vec3 targetUP = target.add(directionVector.scale(lineWidth/2F));
//        Vec3 targetDOWN = target.add(directionVector.scale(-lineWidth/2F));
//
//        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//        RenderSystem.disableCull();
//        RenderSystem.enableBlend();
////        RenderSystem.disableTexture();
//        RenderSystem.defaultBlendFunc();
////        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
//        bufferbuilder.addVertex(lastPos, (float) originUP.x(), (float) originUP.y(), (float) originUP.z()).setColor(f, f1, f2, f3);
//        bufferbuilder.addVertex(lastPos, (float)targetUP.x(), (float)targetUP.y(), (float)targetUP.z()).setColor(f, f1, f2, f3);
//        bufferbuilder.addVertex(lastPos, (float)targetDOWN.x(), (float)targetDOWN.y(), (float)targetDOWN.z()).setColor(f, f1, f2, f3);
//        bufferbuilder.addVertex(lastPos, (float)originDOWN.x(), (float)originDOWN.y(), (float)originDOWN.z()).setColor(f, f1, f2, f3);
//        BufferUploader.drawWithShader(bufferbuilder.build());
////        RenderSystem.enableTexture();
//        RenderSystem.disableBlend();
//        RenderSystem.enableCull();
//        stack.popPose();
//    }
//    public static void drawWorldLine(Matrix3x2fStack stack, Vec3 origin, Vec3 target, float lineWidth, float u0, float imageWidth, float v0, float imageHeight, float imageScale){
//        stack.pushPose();
//        Vec3 cam = getMinecraft().gameRenderer.getMainCamera().getPosition().reverse();
////        Vector3d cam = getWorld().player.position().inverse();
//        stack.translate(cam.x(), cam.y(), cam.z());
//        cam = cam.reverse();
//        Matrix4f lastPos = stack.last().pose();
//
//        u0 /= imageScale;
//        float u1 = u0 + (imageWidth/imageScale);
//        v0 /= imageScale;
//        float v1 = v0 + (imageHeight/imageScale);
//        //This gives me the up/down vector of the plane
//        Vec3 directionVector = target.vectorTo(cam).cross(origin.vectorTo(cam)).normalize();
//
//        Vec3 originUP = origin.add(directionVector.scale(lineWidth/2F));
//        Vec3 originDOWN = origin.add(directionVector.scale(-lineWidth/2F));
//        Vec3 targetUP = target.add(directionVector.scale(lineWidth/2F));
//        Vec3 targetDOWN = target.add(directionVector.scale(-lineWidth/2F));
//
//        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        RenderSystem.disableCull();
//        RenderSystem.enableBlend();
////        RenderSystem.enableTexture();
//        RenderSystem.defaultBlendFunc();
//        bufferbuilder.addVertex(lastPos, (float) originUP.x(), (float) originUP.y(), (float) originUP.z()).setUv(u0, v0);
//        bufferbuilder.addVertex(lastPos, (float)targetUP.x(), (float)targetUP.y(), (float)targetUP.z()).setUv(u1, v0);
//        bufferbuilder.addVertex(lastPos, (float)targetDOWN.x(), (float)targetDOWN.y(), (float)targetDOWN.z()).setUv(u1, v1);
//        bufferbuilder.addVertex(lastPos, (float)originDOWN.x(), (float)originDOWN.y(), (float)originDOWN.z()).setUv(u0, v1);
//        BufferUploader.drawWithShader(bufferbuilder.build());
//

    /// /        RenderSystem.disableTexture();
//        RenderSystem.disableBlend();
//        RenderSystem.enableCull();
//        stack.popPose();
//    }
//    public static void blitImage(Matrix3x2fStack stack, AbstractTexture texture, InvoZone renderZone, InvoZone imageZone, float fullImageWidth, float fullImageHeight){
//        Minecraft mc = getMinecraft();
//        int xPos = (int)mc.mouseHandler.getScaledXPos(mc.getWindow());
//        int yPos = (int)mc.mouseHandler.getScaledYPos(mc.getWindow());
//        GuiGraphicsExtractor graphics = new GuiGraphicsExtractor(getMinecraft(), new GuiRenderState(),xPos, yPos);
//        graphics.pose().set(stack);
//
//        blitImage(graphics, texture, renderZone.x(), renderZone.width(), renderZone.y(), renderZone.height(),
//                imageZone.x(), imageZone.width(), imageZone.y(), imageZone.height(), fullImageWidth, fullImageHeight);
//    }
    public record Custom3DRenderState(Consumer<VertexConsumer> consumer,
                                      RenderType renderType) implements SubmitNodeCollector.CustomGeometryRenderer {

        @Override
        public void render(PoseStack.@NotNull Pose pose, @NotNull VertexConsumer vertexConsumer) {
            consumer.accept(vertexConsumer);
        }
    }
//
//    public static void renderAll() {
//        cachedRenderList.forEach(Runnable::run);
//        cachedRenderList.clear();
//    }

    public static void blit2DImage(GuiGraphicsExtractor graphics, Image image) {
        InvoZone renderZone = image.getRenderZone().copy();
        InvoZone uvZone = image.getUVZone();
        AbstractTexture texture = ClientUtil.getMinecraft().getTextureManager().getTexture(image.location);

        Matrix3x2fStack poseStack = graphics.pose().pushMatrix();
        poseStack.translate(renderZone.x(), renderZone.y());
        poseStack.scale(Math.abs(renderZone.width()), Math.abs(renderZone.height()));
        graphics.guiRenderState.addGuiElement(new BlitRenderState(RenderPipelines.GUI_TEXTURED,
                TextureSetup.singleTexture(texture.getTextureView(), texture.getSampler()),
                new Matrix3x2f(graphics.pose()), 0, 0, 1, 1,
                uvZone.x(), uvZone.right(), uvZone.y(), uvZone.down(), -1, graphics.peekScissorStack()));
        poseStack.popMatrix();
    }

    public static void blit3DImage(PoseStack stack, Image image) {
        blit3DImage(stack, RenderType.create("InvoImage", RenderSetup.builder(RenderPipelines.GUI_TEXTURED)
                .withTexture("Sampler0", image.location).createRenderSetup()), image);
    }

    public static void blit3DImage(PoseStack poseStack, RenderType renderType, Image image) {
        InvoZone renderZone = image.getRenderZone().copy();
        InvoZone uvZone = image.getUVZone();
//    AbstractTexture texture = ClientUtil.getMinecraft().getTextureManager().getTexture(image.location);
        poseStack.pushPose();
//            poseStack.translate(renderZone.x(), renderZone.y(), 0);
//            poseStack.scale(Math.abs(renderZone.width()), Math.abs(renderZone.height()), 1);
        ClientUtil.getMinecraft().gameRenderer.getSubmitNodeStorage().submitCustomGeometry(poseStack, renderType, new Custom3DRenderState(
                (vertexConsumer -> {
                    vertexConsumer.addVertex(poseStack.last(), renderZone.x(), renderZone.down(), (float) 0).setColor(Color.WHITE.getRGB()).setUv(uvZone.x(), uvZone.down());
                    vertexConsumer.addVertex(poseStack.last(), renderZone.right(), renderZone.down(), (float) 0).setColor(Color.WHITE.getRGB()).setUv(uvZone.right(), uvZone.down());
                    vertexConsumer.addVertex(poseStack.last(), renderZone.right(), renderZone.y(), (float) 0).setColor(Color.WHITE.getRGB()).setUv(uvZone.right(), uvZone.y());
                    vertexConsumer.addVertex(poseStack.last(), renderZone.x(), renderZone.y(), (float) 0).setColor(Color.WHITE.getRGB()).setUv(uvZone.x(), uvZone.y());
                })
                , renderType));
        ClientUtil.getMinecraft().gameRenderer.getFeatureRenderDispatcher().renderAllFeatures();
        ClientUtil.getMinecraft().renderBuffers().bufferSource().endBatch();
        poseStack.popPose();
    }

    public static void blit2DColor(GuiGraphicsExtractor graphics, InvoZone renderZone, int color) {
        Matrix3x2fStack poseStack = graphics.pose().pushMatrix();
        poseStack.translate(renderZone.x(), renderZone.y());
        poseStack.scale(Math.abs(renderZone.width()), Math.abs(renderZone.height()));
        graphics.guiRenderState.addGuiElement(new ColoredRectangleRenderState(RenderPipelines.GUI,
                TextureSetup.noTexture(), new Matrix3x2f(graphics.pose()), 0, 0, 1, 1, color, color, graphics.peekScissorStack()));
        poseStack.popMatrix();
    }

    public static void blit3DColor(PoseStack poseStack, InvoZone renderZone, int color) {
        blit3DColor(poseStack, RenderType.create("InvoColor", RenderSetup.builder(RenderPipelines.GUI).createRenderSetup()), renderZone, color);
    }

    public static void blit3DColor(PoseStack poseStack, RenderType renderType, InvoZone renderZone, int color) {
        poseStack.pushPose();
//            poseStack.translate(renderZone.x(), renderZone.y(), 0);
//            poseStack.scale(Math.abs(renderZone.width()), Math.abs(renderZone.height()), 1);
        ClientUtil.getMinecraft().gameRenderer.getSubmitNodeStorage().submitCustomGeometry(poseStack, renderType, new Custom3DRenderState(
                (vertexConsumer -> {
                    vertexConsumer.addVertex(poseStack.last(), renderZone.x(), renderZone.down(), (float) 0).setColor(color);
                    vertexConsumer.addVertex(poseStack.last(), renderZone.right(), renderZone.down(), (float) 0).setColor(color);
                    vertexConsumer.addVertex(poseStack.last(), renderZone.right(), renderZone.y(), (float) 0).setColor(color);
                    vertexConsumer.addVertex(poseStack.last(), renderZone.x(), renderZone.y(), (float) 0).setColor(color);
                })
                , renderType));
        ClientUtil.getMinecraft().gameRenderer.getFeatureRenderDispatcher().renderAllFeatures();
        ClientUtil.getMinecraft().renderBuffers().bufferSource().endBatch();
        poseStack.popPose();
    }

    public static class InvoItemState extends GuiItemRenderState {
        private final InvoZone renderZone;

        public InvoItemState(Matrix3x2f pose, TrackingItemStackRenderState itemStackRenderState, InvoZone renderZone,@Nullable ScreenRectangle scissorArea) {
            super(pose, itemStackRenderState, 0, 0, scissorArea);
            this.renderZone = renderZone;
//            LOGGER.error("dawd: " + this.itemStackRenderState().isOversizedInGui());
        }

        public InvoZone getRenderZone(){
            return this.renderZone;
        }

//        @Override
//        public @NotNull Matrix3x2f pose() {
//            return new Matrix3x2f(super.pose()).scale(0.5f, 0.5f);
//        }

//        @Override
//        public @Nullable ScreenRectangle bounds() {
//            return new InvoZone(0, customWidth, 0, customHeight).rect();
//        }

        @Override
        public @Nullable ScreenRectangle oversizedItemBounds() {
//            float originalArea = 16 * 16;
//            float modifiedArea = customWidth * customHeight;
//            ScreenRectangle overBounds = super.oversizedItemBounds();
//            if (overBounds == null) return null;
//            LOGGER.error("This is running at least");
            return this.renderZone.copy().rect(false);
        }
    }

//    public static class InvoThingy extends TrackingItemStackRenderState{
//        @Override
//        public AABB getModelBoundingBox() {
//            return super.getModelBoundingBox().contract(-1,-1,-1);
//        }
//    }

    public static void blit2DItem(GuiGraphicsExtractor graphics, InvoZone renderZone, ItemStack itemStack) {
        Matrix3x2fStack poseStack = graphics.pose().pushMatrix();
//            poseStack.translate(renderZone.x(), renderZone.y());
//            poseStack.scale(1,1);

        if (!itemStack.isEmpty()) {
//            TrackingItemStackRenderState trackingitemstackrenderstate = new TrackingItemStackRenderState();
//            setDepthTestOverride(DEPTH_TEST_OVERRIDE.NO_DEPTH_TEST);
            try {
                TrackingItemStackRenderState trackingitemstackrenderstate = new TrackingItemStackRenderState();
                getMinecraft().getItemModelResolver().updateForTopItem(trackingitemstackrenderstate, itemStack, ItemDisplayContext.GUI, null, null, 0);
                OversizedItemRenderer oversizeditemrenderer = new OversizedItemRenderer(getMinecraft().renderBuffers().bufferSource());
                GuiItemRenderState itemRenderState = new InvoItemState(new Matrix3x2f(poseStack),
                        trackingitemstackrenderstate, renderZone, graphics.peekScissorStack());
                ScreenRectangle screenrectangle = renderZone.rect(true);
                OversizedItemRenderState oversizeditemrenderstate = new OversizedItemRenderState(
                        itemRenderState, screenrectangle.left(), screenrectangle.top(), screenrectangle.right(), screenrectangle.bottom());
                oversizeditemrenderer.prepare(oversizeditemrenderstate, graphics.guiRenderState, getMinecraft().getWindow().getGuiScale());
//                    altStack.translate(renderZone.x(), renderZone.y(), 0);
//                    graphics.renderItem();
//                    trackingitemstackrenderstate.submit(altStack, getMinecraft().gameRenderer.getSubmitNodeStorage(), 15728880, OverlayTexture.NO_OVERLAY, 0);
//                    trackingitemstackrenderstate.setOversizedInGui(true);
//                    graphics.guiRenderState.submitItem();
//                    LOGGER.error("Is it oversized? " + trackingitemstackrenderstate.isOversizedInGui());
//                    ClientUtil.getMinecraft().gameRenderer.getFeatureRenderDispatcher().renderAllFeatures();
//                    ClientUtil.getMinecraft().renderBuffers().bufferSource().endBatch();
//                    oversizeditemrenderer.close();
//                    oversizeditemrenderer.invalidateTexture();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", () -> String.valueOf(itemStack.getItem()));
                crashreportcategory.setDetail("Item Components", () -> String.valueOf(itemStack.getComponents()));
                crashreportcategory.setDetail("Item Foil", () -> String.valueOf(itemStack.hasFoil()));
                throw new ReportedException(crashreport);
            }
//            setDepthTestOverride(DEPTH_TEST_OVERRIDE.DEFAULT);
//                altStack.popPose();
        }

//            graphics.submitPictureInPictureRenderState(itemStack, 0, 0);
        poseStack.popMatrix();
    }

    public static void blit3DItem(PoseStack poseStack, InvoZone renderZone, ItemStack itemStack, Level level, Entity entity, int seed) {
        PoseStack altStack = new PoseStack();
        altStack.last().set(poseStack.last());
        pipelineConvertor = (t) -> t.toBuilder().withoutStencilTest().build();
//            LOGGER.error("Starting");
        if (!itemStack.isEmpty()) {
//            TrackingItemStackRenderState trackingitemstackrenderstate = new TrackingItemStackRenderState();
//            setDepthTestOverride(DEPTH_TEST_OVERRIDE.NO_DEPTH_TEST);
            ItemStackRenderState trackingitemstackrenderstate = new ItemStackRenderState();
            getMinecraft().getItemModelResolver().updateForTopItem(trackingitemstackrenderstate, itemStack, ItemDisplayContext.GUI, level, entity, seed);
            altStack.pushPose();
            try {
//                    altStack.translate(renderZone.x(), renderZone.y(), 0);
                altStack.scale(renderZone.width(), -renderZone.height(), 1);
                trackingitemstackrenderstate.submit(altStack, getMinecraft().gameRenderer.getSubmitNodeStorage(), 15728880, OverlayTexture.NO_OVERLAY, 0);
                ClientUtil.getMinecraft().gameRenderer.getFeatureRenderDispatcher().renderAllFeatures();
                ClientUtil.getMinecraft().renderBuffers().bufferSource().endBatch();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", () -> String.valueOf(itemStack.getItem()));
                crashreportcategory.setDetail("Item Components", () -> String.valueOf(itemStack.getComponents()));
                crashreportcategory.setDetail("Item Foil", () -> String.valueOf(itemStack.hasFoil()));
                throw new ReportedException(crashreport);
            }
//            setDepthTestOverride(DEPTH_TEST_OVERRIDE.DEFAULT);
            altStack.popPose();
        }
//            LOGGER.error("Ending");
        pipelineConvertor = null;
    }

//        public static void blitItem(GuiGraphicsExtractor graphics, float x0, float width, float y0, float height, ItemStack stack){
//        getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
//        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
//        RenderSystem.enableBlend();
//        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.disableDepthTest();
//
//        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
//        ItemRenderer renderer = mC.getItemRenderer();
//        BakedModel bakedModel = renderer.getModel(stack, null, null, 0);
//
//        Matrix4fStack poseStack = RenderSystem.getModelViewStack();
//        poseStack.pushMatrix();
//        poseStack.translate(x0, y0, (100.0F));
//        poseStack.translate(width/2, height/2, 0.0F);
//        boolean flag = !bakedModel.usesBlockLight();
//        if (flag) {
//            Lighting.setupForFlatItems();
//            poseStack.scale(1.0F, -1.0F, 1.0F);
//        }
//        else {
//            Lighting.setupFor3DItems();
//        }
//        poseStack.scale(width, height, 1.0F);

    /// /        poseStack.translate(x0, y0, 300.0F);
    /// /        poseStack.scale(width, height, 1.0F);
    /// /        poseStack.translate(width/2, height/2, 0.0F);
//        RenderSystem.applyModelViewMatrix();
//        renderer.render(stack, ItemDisplayContext.GUI, false, stack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
//        bufferSource.endBatch();
//        if (flag) {
//            Lighting.setupFor3DItems();
//        }
//        poseStack.popMatrix();
//        RenderSystem.applyModelViewMatrix();
//
//        RenderSystem.enableDepthTest();
//        
//    }
    public static Vec3 smoothLerp(Vec3 oldPos, Vec3 newPos, boolean useDelta) {
//        LOGGER.debug("PARTIAL TICK IN CLIENT UTIL IS: " + ClientUtil.getWorld().getFrameTime());
        return new Vec3(
                smoothLerp(oldPos.x, newPos.x, useDelta),
                smoothLerp(oldPos.y, newPos.y, useDelta),
                smoothLerp(oldPos.z, newPos.z, useDelta));
    }

    public static double smoothLerp(double oldDouble, double newDouble, boolean useDelta) {
        return Mth.lerp(useDelta ? Ticker.getDelta(true, true) : ClientUtil.getMinecraft().getFrameTimeNs(), oldDouble, newDouble);
    }

//    public static void copyEntityMovement(LivingEntity copier, LivingEntity toCopy){
//        copier.moveTo(toCopy.position());
//        copier.xo = toCopy.xo;
//        copier.xOld = toCopy.xOld;
//        copier.yo = toCopy.yo;
//        copier.yOld = toCopy.yOld;
//        copier.zo = toCopy.zo;
//        copier.zOld = toCopy.zOld;
//        copier.setDeltaMovement(toCopy.getDeltaMovement());
//        copier.setYHeadRot(toCopy.getYHeadRot());
//        copier.yHeadRotO = toCopy.yHeadRotO;
//        copier.setYBodyRot(toCopy.yBodyRot);
//        copier.yBodyRotO = toCopy.yBodyRotO;
//    }

    public static boolean inBounds(float xSpot, float ySpot, Bounds bounds) {
        if (xSpot < bounds.x0 || xSpot > bounds.x1) return false;
        if (ySpot < bounds.y0 || ySpot > bounds.y1) return false;

        return true;
    }

    protected static final ArrayList<Bounds> cropBounds = new ArrayList<>();
//    public static void beginCrop (double x, double width, double y, double height, boolean fresh){
//        if (fresh) cropBounds.add(new Bounds((int) x, (int) width, (int) y, (int) height));
////        Bounds bounds = cropBounds.get(cropBounds.size() - 1);
////        XPShop.LOGGER.debug((String.valueOf(x)) + (bounds.x0));
////        XPShop.LOGGER.debug((String.valueOf(width)) + (bounds.x1 - bounds.x0));
////        XPShop.LOGGER.debug((String.valueOf(y)) + (bounds.y0));
////        XPShop.LOGGER.debug((String.valueOf(height)) + (bounds.y1 - bounds.y0));
//        double scale = getMinecraft().getWindow().getGuiScale();
//        int windowHeight = getMinecraft().getWindow().getGuiScaledHeight();
//
//        //This is inverses y since scissor test requires it
//        y = windowHeight - (height + y);
//
////        LOGGER.debug("The y before is: " + y);
////        LOGGER.debug("The height before is: " + height);
//        x *= scale;
//        y *= scale;
//        width *= scale;
//        height *= scale;
//

    /// /        LOGGER.debug("The y is: " + y);
    /// /        LOGGER.debug("The height is: " + height);
//
//        RenderSystem.enableScissor((int) x, (int) y, (int) width, (int) height);
//        //LOGGER.debug("Start " + cropBounds.size());
//    }
//
//    public static void endCrop(){
//        //LOGGER.debug("End " + cropBounds.size());
//        if (cropBounds.size() != 0) cropBounds.remove(cropBounds.size() - 1);
//        if (!cropBounds.isEmpty()) {
//            Bounds cropBound = cropBounds.get(cropBounds.size() - 1);
//            beginCrop(cropBound.x0, (cropBound.x1 - cropBound.x0), cropBound.y0, cropBound.y1 - cropBound.y0, false);
//        }
//        else {
//            RenderSystem.disableScissor();
//        }
//    }

    public static class Bounds {
        int x0;
        int x1;
        int y0;
        int y1;

        public Bounds(int x, int width, int y, int height) {
            this.x0 = x;
            this.x1 = x + width;
            this.y0 = y;
            this.y1 = y + height;
        }

        public Bounds() {
        }

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

    //    public static class SimpleButton extends Button {
//
//        public boolean hidden = false;
//
//        public SimpleButton(int x, int y, int width, int height, MutableComponent textComponent, OnPress onPress) {
//            super(x, y, width, height, textComponent, onPress, (a)->textComponent);
//            this.visible = true;
//        }
//
//        @Override
//        protected void renderWidget(GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
//            super.renderWidget(GuiGraphicsExtractor, mouseX, mouseY, partialTick);
//        }
//    }
    public static String ticksToTime(int ticks) {
        //Each second is 20 ticks
        //each minute is 1200 ticks
        //Each hour is 72000 ticks
        //20 = ticks, 60 = seconds, 60 = minutes
        int hours = ticks / 72000;
        ticks -= (hours * 7200);
        int minutes = ticks / 1200;
        ticks -= (minutes * 1200);
        int seconds = ticks / 20;

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
        power = (int) StrictMath.log10(value);
        value = value / (Math.pow(10, (power / 3) * 3));
        formattedNumber = formatter.format(value);
        formattedNumber = formattedNumber + suffix.charAt(power / 3);
        return formattedNumber.length() > 4 ? formattedNumber.replaceAll("\\.[0-9]+ ", "") : formattedNumber;
    }

    public static TextureManager getTextureManager() {
        return mC.getTextureManager();
    }

    public static class Image {
        protected Identifier location;
        protected final InvoZone imageZone;
        protected final InvoZone renderZone;
        protected ImageType type = ImageType.Stretch;
        protected final float fullImageWidth;
        protected final float fullImageHeight;

        public enum ImageType {
            Stretch,
            Tile,
            NineSlice
        }

        public Image(Identifier loc, float u0, float imageWidth, float v0, float imageHeight) {
            this(loc, u0, imageWidth, v0, imageHeight, imageWidth, imageHeight);
        }

        public Image(Identifier loc, float u0, float imageWidth, float v0, float imageHeight, float fullImageWidth, float fullImageHeight) {
            this.location = loc;
            this.imageZone = new InvoZone(u0, imageWidth, v0, imageHeight);
            this.renderZone = new InvoZone(0, imageWidth, 0, imageHeight);
            this.fullImageWidth = fullImageWidth;
            this.fullImageHeight = fullImageHeight;
        }

        public void resetScale() {
            this.renderZone.setWidth(this.imageZone.width());
            this.renderZone.setHeight(this.imageZone.height());
        }

        public InvoZone getRenderZone() {
            return this.renderZone;
        }

        public InvoZone getImageZone() {
            return this.imageZone;
        }

        public InvoZone getUVZone() {
            return this.imageZone.copy().changeRelativeMultiply(
                    new InvoZone(0, fullImageWidth, 0, fullImageHeight), new InvoZone(0, 1, 0, 1));
        }

        public boolean isMouseOver(int mouseX, int mouseY) {
            return mouseX >= renderZone.x() && mouseX <= (renderZone.x() + renderZone.width())
                    && mouseY >= renderZone.y() && mouseY <= (renderZone.y() + renderZone.height());
        }

        public void setType(ImageType imageType) {
            this.type = imageType;
        }

        public void render(GuiGraphicsExtractor graphics) {
            blit2DImage(graphics, this);
//            TEXTURE_MANAGER.release(this.location);
        }

        public void render(PoseStack poseStack) {
            blit3DImage(poseStack, this);
//            TEXTURE_MANAGER.release(this.location);
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
//        public void render(Matrix3x2fStack stack, int xMouse, int yMouse, float partialTicks) {
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
//        protected void renderList(@NotNull Matrix3x2fStack stack, int xMouse, int yMouse, float p_238478_6_) {
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
////                        bufferbuilder.addVertex((double)l1, (double)(y0 + height + 2), 0.0D);
////                        bufferbuilder.addVertex((double)i2, (double)(y0 + height + 2), 0.0D);
////                        bufferbuilder.addVertex((double)i2, (double)(y0 - 2), 0.0D);
////                        bufferbuilder.addVertex((double)l1, (double)(y0 - 2), 0.0D);
////                        tessellator.end();
////                        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
////                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
////                        bufferbuilder.addVertex((double)(l1 + 1), (double)(y0 + height + 1), 0.0D);
////                        bufferbuilder.addVertex((double)(i2 - 1), (double)(y0 + height + 1), 0.0D);
////                        bufferbuilder.addVertex((double)(i2 - 1), (double)(y0 - 1), 0.0D);
////                        bufferbuilder.addVertex((double)(l1 + 1), (double)(y0 - 1), 0.0D);
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
//        public void render(Matrix3x2fStack stack, int index, int y0, int x0, int rowWidth, int rowHeight, int xMouse, int yMouse, boolean isMouseOver, float partialTicks
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
