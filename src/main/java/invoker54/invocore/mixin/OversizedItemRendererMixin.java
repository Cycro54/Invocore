package invoker54.invocore.mixin;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.util.InvoZone;
import invoker54.invocore.common.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.OversizedItemRenderer;
import net.minecraft.client.gui.render.state.GuiItemRenderState;
import net.minecraft.client.gui.render.state.pip.OversizedItemRenderState;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(OversizedItemRenderer.class)
public abstract class OversizedItemRendererMixin<T extends PictureInPictureRenderState> {
    @Shadow
    private @Nullable Object modelOnTextureIdentity;
    @Unique
    private static final ModLogger invocore_LOGGERT = ModLogger.getLogger(new AtomicBoolean(true));

    @Inject(
            method = "renderToTexture(Lnet/minecraft/client/gui/render/state/pip/OversizedItemRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = {
                    @At(value = "HEAD")
            }, cancellable = true)
    private void renderTexture(OversizedItemRenderState state, PoseStack stack, CallbackInfo ci) {
        if (!(state.guiItemRenderState() instanceof ClientUtil.InvoItemState)) return;
        ci.cancel();
        ClientUtil.InvoItemState invoItemState = (ClientUtil.InvoItemState) state.guiItemRenderState();
        InvoZone renderZone = invoItemState.getRenderZone();
        stack.last().set(new PoseStack.Pose());
        int guiScale = ClientUtil.getMinecraft().getWindow().getGuiScale();
        invocore_LOGGERT.error("What's gui scale: " + guiScale);
//        float f = (float)(renderZone.width()) / 2.0F;
//        float f1 = (float)(renderZone.height()) / 2.0F;
//        float f2 = (float)renderZone.x() + 8.0F;
//        float f3 = (float)renderZone.y() + 8.0F;
//        stack.mulPose(Axis.ZP.rotation(45));
        stack.translate(((renderZone.width()) * guiScale)/2f, ((renderZone.height()) * guiScale)/2f,0);
        stack.scale(renderZone.width()*guiScale, renderZone.height()*guiScale, renderZone.height()*guiScale);
//        stack.translate(-(renderZone.width() * guiScale) / 2.0F, 0, 0.0F);
//        stack.translate(-(f2 - f) / 16.0F,0,0);
//        stack.scale(-(renderZone.width()/2) * guiScale,renderZone.height() * guiScale, 100 * ClientUtil.getMinecraft().getWindow().getGuiScale());
        //Base translation
//        stack.translate(0.5f,0,0);
        //Now I need to add an adjust based on the scale difference
//        stack.translate((renderZone.width() - 16)/16f,0,0);
        //        stack.translate(renderZone.x() * guiScale, renderZone.y(), 16);
//        stack.translate(renderZone.width(), 0,0);
//        stack.translate(((renderZone.width() + ) /2f) * guiScale,((renderZone.height() + renderZone.y()) * guiScale)/2f, 0);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        GuiItemRenderState guiitemrenderstate = state.guiItemRenderState();
//        ScreenRectangle screenrectangle = guiitemrenderstate.oversizedItemBounds();
//        Objects.requireNonNull(screenrectangle);
//        float f = (float)(screenrectangle.left() + screenrectangle.right()) / 2.0F;
//        float f1 = (float)(screenrectangle.top() + screenrectangle.bottom()) / 2.0F;
//        float f2 = (float)guiitemrenderstate.x() + 8.0F;
//        float f3 = (float)guiitemrenderstate.y() + 8.0F;
//        float largestDimension = Math.max(invoItemState.getCustomWidth(), invoItemState.getCustomHeight());
//        float newHeight = invoItemState.getCustomHeight()/largestDimension;
//        float newWidth = invoItemState.getCustomWidth()/largestDimension;
//        stack.scale(newWidth, newHeight, 1);
//        stack.translate((newWidth - 1)/2, (newHeight - 1)/2,0);
//        stack.translate(-0.5f,0,0);
        TrackingItemStackRenderState trackingitemstackrenderstate = invoItemState.itemStackRenderState();
        boolean flag = !trackingitemstackrenderstate.usesBlockLight();
        if (flag) {
            Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
        } else {
            Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
        }

        FeatureRenderDispatcher featurerenderdispatcher = Minecraft.getInstance().gameRenderer.getFeatureRenderDispatcher();
        SubmitNodeStorage submitnodestorage = featurerenderdispatcher.getSubmitNodeStorage();
        trackingitemstackrenderstate.submit(stack, submitnodestorage, 15728880, OverlayTexture.NO_OVERLAY, 0);
        featurerenderdispatcher.renderAllFeatures();
        this.modelOnTextureIdentity = trackingitemstackrenderstate.getModelIdentity();

    }
}
