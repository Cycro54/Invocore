package invoker54.invocore.mixin;

import com.mojang.blaze3d.opengl.GlRenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.common.ModLogger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(GlRenderPipeline.class)
public class GlRenderPipelineMixin {
    @Unique
    private static final ModLogger invocore_LOGGERT = ModLogger.getLogger(new AtomicBoolean(true));

    @Inject(
            method = "info",
            at = {
                    @At(value = "RETURN")
            }, cancellable = true)
    private void pipeline(CallbackInfoReturnable<RenderPipeline> cir){
//        invocore_LOGGERT.warn("1 is the number");
        if (ClientUtil.pipelineConvertor == null) return;
//        invocore_LOGGERT.warn("2 is the war");
        cir.setReturnValue(ClientUtil.pipelineConvertor.apply(cir.getReturnValue()));
//        invocore_LOGGERT.warn("What's this thing...");
    }
}
