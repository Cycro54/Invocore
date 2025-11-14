package invoker54.invocore.common.network.message;//package invoker54.invocore.common.network.message;
//
//import invoker54.invocore.common.network.InvoNetworkFunction;
//import invoker54.invocore.common.network.NetworkHandler;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Consumer;
//import java.util.function.Supplier;
//
//public class ClientMsg {
//    public String id;
//    public CompoundTag tag;
//
//    public ClientMsg(String id, CompoundTag tag){
//        this.id = id;
//        this.tag = tag;
//    }
//
//    public static void Encode(ClientMsg msg, FriendlyByteBuf buf){
//        buf.writeUtf(msg.id);
//        buf.writeNbt(msg.tag);
//    }
//
//    public static ClientMsg Decode(FriendlyByteBuf buf){
//        return new ClientMsg(buf.readUtf(), buf.readNbt());
//    }
//
//    //This is how the Network Handler will handle the message
//    public static void handle(ClientMsg msg, Supplier<NetworkEvent.Context> contextSupplier){
//        NetworkEvent.Context context = contextSupplier.get();
//
//        context.enqueueWork(() -> {
//            InvoNetworkFunction function = NetworkHandler.networkMap.get(msg.id);
//            if (function == null) throw new NullPointerException("FUNCTION: " + msg.id + " IS MISSING!");
//            function.run(context.getSender(), msg.tag);
//        });
//        context.setPacketHandled(true);
//    }
//}
