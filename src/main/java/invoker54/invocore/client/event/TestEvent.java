//package invoker54.invocore.client.event;
//
//import com.mojang.blaze3d.platform.Lighting;
//import com.mojang.blaze3d.vertex.PoseStack;
//import invoker54.invocore.Invocore;
//import invoker54.invocore.client.ClientUtil;
//import invoker54.invocore.client.TextUtil;
//import invoker54.invocore.client.Ticker;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.Mth;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
//import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
//
//import java.awt.*;
//import java.util.Locale;
//
//@EventBusSubscriber(modid = Invocore.MOD_ID, value =  Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
//public class TestEvent {
//
//    @SubscribeEvent
//    public static void test(RegisterGuiLayersEvent event){
//        event.registerAbove(VanillaGuiLayers.CHAT, ResourceLocation.withDefaultNamespace("fallen_single_player_screen"), (guiGraphics, tracker) -> {
//            float result = (float) (Math.sin(Ticker.getDelta(false, false) /32f)+1)/2F;
//            float resultSize = Mth.lerp(result, 4,32);
//            float result2 = (float) (Math.sin(tracker.getGameTimeDeltaPartialTick(true) /32f)+1)/2F;
//            float result2Size = Mth.lerp(result2, 4,32);
//            int width = guiGraphics.guiWidth();
//            int height = guiGraphics.guiHeight();
//            PoseStack stack = guiGraphics.pose();
//
//            Lighting.setupForFlatItems();
//            float scalex = 3;
//            float scaley = 3F;
//            float scalez = ((scaley + scalex)/2F);
//
////            ClientUtil.blitColor(stack,0, width, 0, height, new Color(94, 94, 94, 142).getRGB());
//            ClientUtil.blitColor(stack, 0, 64, 0, 64, new Color(1,1,1, 123).getRGB());
//            if (!ClientUtil.getPlayer().getItemInHand(InteractionHand.MAIN_HAND).isEmpty()){
////                stack.pushPose();
////                stack.scale(scalex,scaley,scalez);
//////                Lighting.setupFor3DItems();
////                guiGraphics.renderItem(ClientUtil.getPlayer().getItemInHand(InteractionHand.MAIN_HAND), (int) (64/scalex), (int) ((height/2)/scaley));
////                stack.popPose();
//                ClientUtil.blitColor(stack, (width/2F)-0.5F, 64, (float) height /2, 32, new Color(0, 0, 0, 255).getRGB());
//
//                ClientUtil.blitItem(stack, (width/2F)-0.5F, 64, (float) height /2, 32,
//                        ClientUtil.getPlayer().getItemInHand(InteractionHand.MAIN_HAND));
//            }
//            ClientUtil.blitItem(stack, 0, 16, 0, 16, new ItemStack(Items.GOLDEN_APPLE));
//            ClientUtil.blitItem(stack, 64, 20, 0, 20, new ItemStack(Items.GOLD_BLOCK));
//            ClientUtil.blitItem(stack, width/3F, resultSize, Mth.lerp(result, 0, height/2F), resultSize, new ItemStack(Items.GOLD_BLOCK));
//            ClientUtil.blitItem(stack, (width*2)/3F, result2Size, Mth.lerp(result2, 0, height/2F), result2Size, new ItemStack(Items.GOLD_BLOCK));
//            MutableComponent txt = Component.literal("Don't you understand that ");
//        txt.withStyle(ChatFormatting.LIGHT_PURPLE).
//                append(Component.literal("\nI'm trying to help you? Foolish.".toUpperCase(Locale.ROOT))
//                        .withStyle(ChatFormatting.AQUA,ChatFormatting.BOLD,ChatFormatting.OBFUSCATED));
////        ClientUtil.mC.fontRenderer.drawText(stack, txt, width / 4F, height / 5F, new Color(255,255,255,0).getRGB());
//        TextUtil.renderText(stack, txt, 0, true,
//                width / 4F, width / 4F, height / 5F, height / 8F, 0, TextUtil.txtAlignment.MIDDLE);
//
//        });
//    }
//}