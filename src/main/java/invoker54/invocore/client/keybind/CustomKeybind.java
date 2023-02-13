package invoker54.invocore.client.keybind;

import invoker54.invocore.Invocore;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Invocore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomKeybind {
    public KeyMapping keyBind;
    public IClicked iClicked;

    public static final List<KeyMapping> KEY_MAPPING_LIST = new ArrayList<>();

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event){
        for (KeyMapping key : KEY_MAPPING_LIST){
            event.register(key);
        }
    }

    public CustomKeybind(String name, int key, String MOD_ID, IClicked iClicked){
        keyBind = new KeyMapping("key." + MOD_ID + "." + name, key,"key.category." + MOD_ID);
        //keyBind = new KeyBinding(name, key,"XP Shop");
        KEY_MAPPING_LIST.add(keyBind);
        this.iClicked = iClicked;
    }
    public CustomKeybind(KeyMapping keyBind, IClicked iClicked){
        this.keyBind = keyBind;

        this.iClicked = iClicked;
    }

    public void pressed(int action){
        iClicked.onClick(action);
    }

    public interface IClicked {
        void onClick(int action);
    }
}
