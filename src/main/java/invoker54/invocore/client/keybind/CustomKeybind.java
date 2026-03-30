package invoker54.invocore.client.keybind;

import invoker54.invocore.Invocore;
import invoker54.invocore.common.ModLogger;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@EventBusSubscriber(value = Dist.CLIENT, modid = Invocore.MOD_ID)
public class CustomKeybind {
    public static final ModLogger LOGGERT = ModLogger.getLogger(CustomKeybind.class, new AtomicBoolean(true));
    public KeyMapping keyBind;
    public IClicked iClicked;
    public static RegisterKeyMappingsEvent keyMappingEvent;

    public static final List<KeyMapping> KEY_MAPPING_LIST = new ArrayList<>();
    public static final List<KeyMapping.Category> CATEGORY_LIST = new ArrayList<>();

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event){
        keyMappingEvent = event;
        if (KEY_MAPPING_LIST.isEmpty()) return;
        for (KeyMapping key : KEY_MAPPING_LIST){
            keyMappingEvent.register(key);
        }
        for (KeyMapping.Category category : CATEGORY_LIST){
            event.registerCategory(category);
        }
    }

    public static KeyMapping.Category getCategory(String MODID, String categoryName){
        Identifier id = Identifier.fromNamespaceAndPath(MODID, categoryName);
        for (var category : CATEGORY_LIST){
            if (category.id().toString().equals(id.toString())) return category;
        }
        KeyMapping.Category category = new KeyMapping.Category(id);
        CATEGORY_LIST.add(category);
        return category;
    }

    public CustomKeybind(String name, int key, String MOD_ID, IClicked iClicked){
        this(name, key, MOD_ID, MOD_ID, iClicked);
    }

    public CustomKeybind(String name, int key, String MOD_ID, String categoryName, IClicked iClicked){
        this(new KeyMapping("key." + MOD_ID + "." + name, key, getCategory(MOD_ID, categoryName)), iClicked);
    }
    public CustomKeybind(KeyMapping keyBind, IClicked iClicked){
        this.keyBind = keyBind;
        this.iClicked = iClicked;
        KEY_MAPPING_LIST.add(keyBind);
        if (keyMappingEvent == null) return;
        keyMappingEvent.register(keyBind);
    }

    public void pressed(int action){
        iClicked.onClick(action);
    }

    public interface IClicked {
        void onClick(int action);
    }
}
