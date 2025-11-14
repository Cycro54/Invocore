package invoker54.invocore.fabric.client;

import invoker54.invocore.Invocore;
import net.fabricmc.api.ClientModInitializer;

public final class InvocoreFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        Invocore.clientInit();
    }
}
