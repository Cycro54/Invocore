//package invoker54.invocore.client.event;
//
//import com.mojang.blaze3d.pipeline.RenderPipeline;
//import com.mojang.blaze3d.platform.DepthTestFunction;
//import invoker54.invocore.Invocore;
//import invoker54.invocore.common.ModLogger;
//import net.minecraft.resources.Identifier;
//import net.minecraft.resources.ResourceKey;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.pipeline.PipelineModifier;
//import net.neoforged.neoforge.client.pipeline.RegisterPipelineModifiersEvent;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.concurrent.atomic.AtomicBoolean;
//
//@EventBusSubscriber
//public class PipelineModifierEvent {
//    private static final ModLogger LOGGERT = ModLogger.getLogger(new AtomicBoolean(true));
//
//    // 1. Create the Register
//    public static final ResourceKey<@NotNull PipelineModifier> itemModifier = ResourceKey.create(ResourceKey.createRegistryKey(PipelineModifier.MODIFIERS_KEY.registry()),
//            Identifier.fromNamespaceAndPath(Invocore.MOD_ID, "3d_item_depth_change"));
//
//
//    @SubscribeEvent
//    public static void modifyItem(RegisterPipelineModifiersEvent event){
//        event.register(itemModifier, new ChangeItemModifier());
//    }
//
//    public static class ChangeItemModifier implements PipelineModifier{
//
//        @Override
//        public @NotNull RenderPipeline apply(RenderPipeline renderPipeline, @NotNull Identifier identifier) {
//            LOGGERT.error("What's the pipelines name: " + identifier);
//            return renderPipeline.toBuilder().withLocation(itemModifier.identifier()).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).build();
//        }
//    }
//}
