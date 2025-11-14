package invoker54.invocore.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import invoker54.invocore.common.ModLogger;
import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomKeybind {
    public static final ModLogger LOGGERT = ModLogger.getLogger(CustomKeybind.class, new AtomicBoolean(true));
    public KeyMapping keyBind;
    public Runnable onClick;

    public static final List<KeyMapping> KEY_MAPPING_LIST = new ArrayList<>();

    public CustomKeybind(String name, InputConstants.Type type, int key, String MOD_ID, Runnable onClick){
        this(new KeyMapping("key." + MOD_ID + "." + name, type, key,"key.category." + MOD_ID), onClick);
    }
    public CustomKeybind(KeyMapping keyBind, Runnable onClick) {
        this.keyBind = keyBind;
        this.onClick = onClick;
        KEY_MAPPING_LIST.add(keyBind);
        KeyMappingRegistry.register(this.keyBind);
        ClientTickEvent.CLIENT_LEVEL_PRE.register(instance -> {
            while (this.keyBind.consumeClick()) {
                this.onClick.run();
            }});
//        if (keyMappingEvent == null) return;
//        keyMappingEvent.register(keyBind);
    }

}
