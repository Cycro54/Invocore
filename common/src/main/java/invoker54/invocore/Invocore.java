package invoker54.invocore;

import invoker54.invocore.common.ModLogger;
import invoker54.invocore.init.KeybindInit;

import java.util.concurrent.atomic.AtomicBoolean;

public final class Invocore {
    public static final String MOD_ID = "invocore";
    public static final AtomicBoolean debugMode = new AtomicBoolean(true);
    public static ModLogger LOGGER = ModLogger.getLogger(Invocore.class, debugMode);

    public static void commonInit() {
        // Write common init code here.
    }

    public static void clientInit(){
//        ClientGuiEvent.RENDER_HUD.register(new TestEventCommon());
        KeybindInit.init();
//        InvoZone copyZone = new InvoZone(2131, 3141.5145123F, 421, 411249014.2414143F);
    }
}
