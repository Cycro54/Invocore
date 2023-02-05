package invoker54.invocore.client.keybind;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class CustomKeybind {
    public KeyMapping keyBind;
    public IClicked iClicked;

    public CustomKeybind(String name, int key, String MOD_ID, IClicked iClicked){
        keyBind = new KeyMapping("key." + MOD_ID + "." + name, key,"key.category." + MOD_ID);
        //keyBind = new KeyBinding(name, key,"XP Shop");
        ClientRegistry.registerKeyBinding(keyBind);
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
