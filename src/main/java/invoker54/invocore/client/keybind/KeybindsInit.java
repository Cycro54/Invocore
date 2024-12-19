package invoker54.invocore.client.keybind;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class KeybindsInit {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ArrayList<CustomKeybind> keyBinds = new ArrayList<>();

    public static CustomKeybind addBind(CustomKeybind keybind){
        keyBinds.add(keybind);
        return keybind;
    }
}
