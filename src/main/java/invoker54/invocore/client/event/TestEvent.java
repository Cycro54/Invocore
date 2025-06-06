//package invoker54.invocore.client.event;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import invoker54.invocore.Invocore;
//import invoker54.invocore.client.util.ClientUtil;
//import invoker54.invocore.client.util.TextUtil;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.util.Mth;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import java.awt.*;
//import java.util.Locale;
//
//@Mod.EventBusSubscriber(modid = Invocore.MOD_ID, value =  Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class TestEvent {
//    private static float aFloat = 0;
//    private static float bFloat = 0;
//
//    @SubscribeEvent
//    public static void test(RegisterGuiOverlaysEvent event){
//        event.registerAboveAll("fallen_single_player_screen", (gui, guiGraphics, partialTicks, width, height) -> {
//            aFloat += partialTicks;
//            bFloat += partialTicks;
//
//            float result = (float) (Math.sin(aFloat/32f)+1)/2F;
//            float result2 = Mth.lerp(result, 4,32);
//            PoseStack stack = guiGraphics.pose();
//
//
//            ClientUtil.blitColor(stack,0, width, 0, height, new Color(94, 94, 94, 142).getRGB());
//            ClientUtil.blitColor(stack, 0, 64, 0, 64, new Color(1,1,1, 123).getRGB());
//            ClientUtil.blitItem(stack, 0, 64, 0, 64, new ItemStack(Items.GOLDEN_APPLE));
//            ClientUtil.blitItem(stack, 64, 10, 0, 10, new ItemStack(Items.GOLD_BLOCK));
//            ClientUtil.blitItem(stack, 116, result2, Mth.lerp(result, 0, height/2F), result2, new ItemStack(Items.GOLD_BLOCK));
//            ClientUtil.blitColor(stack,(width/2F) - 1, 2, 0, height, new Color(1,1,1,255).getRGB());
//            ClientUtil.blitColor(stack,0, 1, 0, height, new Color(1,1,1,255).getRGB());
//            ClientUtil.blitColor(stack,width - 1, 1, 0, height, new Color(1,1,1,255).getRGB());
//
//            ClientUtil.blitColor(stack,width/4F, 1, 0, height, new Color(1,1,1,255).getRGB());
//            ClientUtil.blitColor(stack,((width/4F) + width/2F) - 1, 1, 0, height, new Color(1,1,1,255).getRGB());
//
//
//            ClientUtil.blitColor(stack, width/4F, width/4F, height - (height/8F)*2, height/8F, new Color(0, 84, 7, 255).getRGB());
//
//                        MutableComponent txt = Component.literal("Don't you understand that I am trying to help you?");
////            TextUtil.renderText(stack, txt, 3,true,
////                    width/4F, width/4F, height/5F, height/8F,0, TextUtil.txtAlignment.LEFT);
//            ClientUtil.blitColor(stack, width/4F, width/4F, height/5F, height/8F, new Color(0, 84, 7, 158).getRGB());
//
//                    txt = Component.literal("Don't you understand that ");
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