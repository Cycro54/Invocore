package invoker54.invocore;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import invoker54.invocore.client.event.TestEventCommon;
import invoker54.invocore.client.invoimage.InvoImage;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.common.ModLogger;
import invoker54.invocore.init.KeybindInit;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.atomic.AtomicBoolean;

public final class Invocore {
    public static final String MOD_ID = "invocore";
    public static final AtomicBoolean debugMode = new AtomicBoolean(true);
    public static ModLogger LOGGER = ModLogger.getLogger(debugMode);

    public static void commonInit() {
        // Write common init code here.
    }

    public static void clientInit(){
//        ClientGuiEvent.RENDER_HUD.register(new TestEventCommon());
        KeybindInit.init();
        ClientReloadShadersEvent.EVENT.register((provider, sink) ->
                InvoImage.getAllTextures(true));
//        LOGGER.warn("IS THIS WORKING CORRECTLY???");
//        InvoZone copyZone = new InvoZone(2131, 3141.5145123F, 421, 411249014.2414143F);
    }
}
