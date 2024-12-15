package invoker54.invocore.client.keybind;

import invoker54.invocore.Invocore;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Invocore.MOD_ID)
public class KeybindsInit {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ArrayList<CustomKeybind> keyBinds = new ArrayList<>();

    public static CustomKeybind addBind(CustomKeybind keybind){
        keyBinds.add(keybind);
        return keybind;
    }
}
