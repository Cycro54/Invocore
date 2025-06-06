package invoker54.invocore.client.event;

import invoker54.invocore.Invocore;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.keybind.CustomKeybind;
import invoker54.invocore.client.keybind.KeybindsInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(value = Dist.CLIENT, modid = Invocore.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class InputEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event){
        onInput(event.getAction(), event.getKey());
    }

    @SubscribeEvent
    public static void onMousePre(InputEvent.MouseButton.Pre event){
        onInput(event.getAction(), event.getButton());
    }

    @SubscribeEvent
    public static void onMousePost(InputEvent.MouseButton.Post event){
        onInput(event.getAction(), event.getButton());
    }

    private static void onInput(int action, int key){
        //LOGGER.debug("Is there a world?? " + (ClientUtil.getWorld().level == null));
        if (ClientUtil.getWorld() == null) return;

        for (CustomKeybind cKeyBind : KeybindsInit.keyBinds){
            if (cKeyBind.keyBind.getKey().getValue() == key){
                cKeyBind.pressed(action);
            }
        }
    }
}
