package invoker54.invocore.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public interface InvoNetworkFunction {
    void run(Player player, CompoundTag tag);
}
