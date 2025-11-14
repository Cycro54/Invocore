package invoker54.invocore.mixin;

import invoker54.invocore.client.invoimage.InvoImageSprite;
import net.minecraft.client.ResourceLoadStateTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceLoadStateTracker.class)
public class ResourceLoadStateTrackerMixin {

    @Inject(
            method = "finishReload",
            at = {
                    @At(value = "RETURN")
            })
    private void finishReload(CallbackInfo ci){
        //All this does is tell InvoImageSprites to check if the sprite they are rendering changed its size
        InvoImageSprite.globalResourceReloadCount++;
    }
}
