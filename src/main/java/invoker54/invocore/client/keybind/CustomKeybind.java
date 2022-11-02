package invoker54.invocore.client.keybind;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class CustomKeybind {
    public KeyBinding keyBind;
    public IClicked iClicked;

    public CustomKeybind(String name, int key, String MOD_ID, IClicked iClicked){
        keyBind = new KeyBinding("key." + MOD_ID + "." + name, key,"key.category." + MOD_ID);
        //keyBind = new KeyBinding(name, key,"XP Shop");
        ClientRegistry.registerKeyBinding(keyBind);
        this.iClicked = iClicked;
    }
    public CustomKeybind(KeyBinding keyBind, IClicked iClicked){
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
