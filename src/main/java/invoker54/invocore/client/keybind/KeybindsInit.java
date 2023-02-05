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
    public static final ArrayList<CustomKeybind> gearBinds = new ArrayList<>();

//    public static CustomKeybind cycleGear;

    public static void registerKeys(){
        //Example: Cycle selected item for combat and utility gear

        /*
        cycleGear = addBind(new CustomKeybind("cycle_gear", GLFW.GLFW_KEY_R, (action) ->{
            if(action != GLFW.GLFW_PRESS) return;

            if(ClientUtil.mC.screen != null) return;

            ItemStack item = ClientUtil.mC.player.getMainHandItem();
            GearCap cap = GearCap.getCap(item);
            if (cap == null) return;

            NetworkHandler.INSTANCE.sendToServer(new CycleGearMsg());
        }));
        */
    }

    public static CustomKeybind addBind(CustomKeybind keybind){
        gearBinds.add(keybind);
        return keybind;
    }
}
