package invoker54.invocore.client.event;

import invoker54.invocore.Invocore;
import invoker54.invocore.client.ClientUtil;
import invoker54.invocore.client.keybind.CustomKeybind;
import invoker54.invocore.client.keybind.KeybindsInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Invocore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InputEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event){
        onInput(event.getAction(), event.getKey());
    }

    @SubscribeEvent
    public static void onMousePress(InputEvent.MouseButton event){
        onInput(event.getAction(), event.getButton());
    }

    private static void onInput(int action, int key){
        //LOGGER.debug("Is there a world?? " + (ClientUtil.mC.level == null));
        if (ClientUtil.mC.level == null) return;

        for (CustomKeybind cKeyBind : KeybindsInit.gearBinds){
            if (cKeyBind.keyBind.getKey().getValue() == key){
                cKeyBind.pressed(action);
            }
        }
    }
}
