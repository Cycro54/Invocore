package invoker54.invocore.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.util.InvoZone;
import invoker54.invocore.client.util.TextUtil;
import invoker54.invocore.common.ModLogger;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.awt.*;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

//@EventBusSubscriber(modid = Invocore.MOD_ID, value = Dist.CLIENT)
public class TestEvent {
    public static ModLogger LOGGERT = ModLogger.getLogger(TestEvent.class, new AtomicBoolean(true));
    private static float aFloat = 0;
    private static float bFloat = 0;

//    @SubscribeEvent
    public static void test(RegisterGuiLayersEvent event){
//        LOGGERT.warn("This ran??? ");

        event.registerAbove(VanillaGuiLayers.CHAT, Identifier.withDefaultNamespace("fallen_single_player_screen"), (guiGraphics, tracker) -> {
            ClientUtil.Image dirtIMG = new ClientUtil.Image(Identifier.withDefaultNamespace("textures/block/dirt.png"), 0, 16, 0, 16);
//            if (true) return;
            if (!ClientUtil.getMinecraft().isPaused() && ClientUtil.getMinecraft().screen == null) aFloat += tracker.getGameTimeDeltaPartialTick(false) / 2;
            if (!ClientUtil.getMinecraft().isPaused() && ClientUtil.getMinecraft().screen == null) bFloat += tracker.getGameTimeDeltaPartialTick(false);
            float resulta = (float) (Math.sin(aFloat / 16f) + 1) / 2F;
            float resultb = (float) (Math.cos(bFloat / 32f) + 1) / 2F;
            float result2 = Mth.lerp(resulta, 4, 32);
            int width = guiGraphics.guiWidth();
            int height = guiGraphics.guiHeight();

//            Lighting.setupForFlatItems();
            float scalex = 3;
            float scaley = 3F;
            float scalez = ((scaley + scalex)/2F);
            InvoZone renderZone = dirtIMG.getRenderZone();
            renderZone.setX(64).setY(64).setWidth(32).setHeight(32);
//            guiGraphics.enableScissor((int) renderZone.x(), (int) renderZone.y(), (int) renderZone.right(), (int) (renderZone.down()-8f));
            ClientUtil.blit2DColor(guiGraphics,new InvoZone(0, 100, 0, 100), new Color(255, 0, 0, 142).getRGB());
            ClientUtil.blit2DColor(guiGraphics, new InvoZone(0, 64, 0, 64), new Color(1,1,1, 123).getRGB());
//            guiGraphics.disableScissor();
            dirtIMG.render(guiGraphics);
            float variable = Mth.lerp(resulta, 16f, 64f);
            float variable2 = Mth.lerp(resulta, 0, 360);
            InvoZone breeZone = new InvoZone(8, 16, 8, 16);
            if (!ClientUtil.getPlayer().getItemInHand(InteractionHand.MAIN_HAND).isEmpty()){
//                stack.pushPose();
//                stack.scale(scalex,scaley,scalez);
////                Lighting.setupFor3DItems();
//                guiGraphics.renderItem(ClientUtil.getPlayer().getItemInHand(InteractionHand.MAIN_HAND), (int) (64/scalex), (int) ((height/2)/scaley));
//                stack.popPose();
//                ClientUtil.blit2DColor(guiGraphics, new InvoZone((width/2F)-0.5F, 64, (float) height /2, 32), new Color(0, 0, 0, 255).getRGB());

//                ClientUtil.blit2DItem(guiGraphics, new InvoZone((width/2F)-0.5F, 64, (float) height /2, 32),
//                        ClientUtil.getPlayer().getItemInHand(InteractionHand.MAIN_HAND));
                ClientUtil.blit2DItem(guiGraphics, breeZone.copy().gridStep(3,0),
                        ClientUtil.getPlayer().getItemInHand(InteractionHand.MAIN_HAND));
            }
//            ClientUtil.blitItem(guiGraphics, new InvoZone(0, 16, 0, 16), new ItemStack(Items.GOLDEN_APPLE));
//            ClientUtil.blitItem(guiGraphics, new InvoZone(64, 20, 0, 20), new ItemStack(Items.GOLD_BLOCK));
//            ClientUtil.blitItem(guiGraphics, new InvoZone(width/3F, resultSize, Mth.lerp(result, 0, height/2F), resultSize), new ItemStack(Items.GOLD_BLOCK));

            InvoZone fourZone = breeZone.copy().multiply(0.5f);
            ClientUtil.blit2DColor(guiGraphics, fourZone, Color.PINK.getRGB());
//            guiGraphics.pose().pushMatrix().translate(0,0).scale(variable2,variable2);
//            guiGraphics.renderItem(new ItemStack(Items.GOLD_BLOCK), 0,16);
//            guiGraphics.pose().popMatrix();
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(breeZone.x() + (breeZone.width()/2f), breeZone.y() + (breeZone.height()/2f));
            guiGraphics.pose().rotate((float) Math.toRadians(variable2));
            guiGraphics.pose().translate(-(breeZone.x() + (breeZone.width()/2f)), -(breeZone.y() + (breeZone.height()/2f)));
            ClientUtil.blit2DColor(guiGraphics, breeZone, Color.GRAY.getRGB());
            ClientUtil.blit2DItem(guiGraphics, breeZone, new ItemStack(Items.GOLD_BLOCK));
            guiGraphics.pose().popMatrix();
            MutableComponent txt = Component.literal("Don't you understand that ");
//            guiGraphics.guiSprites.getTextures().keySet().forEach(s -> LOGGERT.warn(s.toString()));
//            LOGGERT.warn("Dirt is here! " + (dirtIMG == null));

        txt.withStyle(ChatFormatting.LIGHT_PURPLE).
                append(Component.literal("\nI'm trying to help you? Foolish.".toUpperCase(Locale.ROOT))
                        .withStyle(ChatFormatting.AQUA,ChatFormatting.BOLD,ChatFormatting.OBFUSCATED));
//        ClientUtil.mC.fontRenderer.drawText(stack, txt, width / 4F, height / 5F, new Color(255,255,255,0).getRGB());
            ClientUtil.blit2DColor(guiGraphics, new InvoZone(
                    width / 4F, width / 4F, height / 5F, height / 8F), Color.BLACK.getRGB());
        TextUtil.render2DText(guiGraphics, txt, true,0, new InvoZone(
                width / 4F, width / 4F, height / 5F, height / 8F), TextUtil.txtAlignment.RIGHT);
//        ClientUtil.renderAll();
        });
    }

//    @SubscribeEvent
    public static void renderWorldFallTimer(RenderLevelStageEvent.AfterWeather event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        Minecraft mC = ClientUtil.getMinecraft();
//        boolean iAmFallen = FallenData.get(ClientUtil.getPlayer()).isFallen();
        if (ClientUtil.getWorld() == null) return;

        for (Entity entity : ClientUtil.getMinecraft().level.entitiesForRendering()) {
            if (!(entity instanceof Player player)) continue;
            if (!entity.equals(mC.player)) continue;

            float yOffset = entity.getBbHeight() * 0.40f;
            float sizeOffset = 0.5F;

            PoseStack stack = event.getPoseStack();
            stack.pushPose();
            Vec3 difference = entity.position().subtract(mC.gameRenderer.getMainCamera().position());
            stack.translate(difference.x, difference.y + yOffset, difference.z);
            stack.mulPose(mC.getEntityRenderDispatcher().camera.rotation());
//            stack.scale(0.025F, -0.025F, 0.025F);
            stack.scale(1, -1, 1);
//            stack.scale(sizeOffset, sizeOffset, sizeOffset);
            InvoZone renderZone = new InvoZone(-0.5f, 1, -1.2f, 2);

            ClientUtil.blit3DColor(stack, renderZone, new Color(73, 255, 0, 123).getRGB());

//            RenderSystem.pushPipelineModifier(PipelineModifierEvent.itemModifier);
//            LOGGERT.warn("This is the start of blit item");
//            LOGGERT.warn("This is the end of blit item");
//            ClientUtil.Image dirtIMG = new ClientUtil.Image(Identifier.withDefaultNamespace("textures/block/dirt.png"), 0, 16, 0, 16);
//            dirtIMG.getRenderZone().copy(renderZone);
//            dirtIMG.render(stack);
//            ClientUtil.renderAll();

            MutableComponent txt = Component.literal("Don't you understand that ");
            txt.withStyle(ChatFormatting.LIGHT_PURPLE).
                    append(Component.literal("\nI'm trying to help you? Foolish.".toUpperCase(Locale.ROOT))
                            .withStyle(ChatFormatting.AQUA,ChatFormatting.BOLD,ChatFormatting.OBFUSCATED));
//            ClientUtil.blit3DColor(stack, new InvoZone(0, 10, 0, 10), Color.BLACK.getRGB()).run();
            TextUtil.render3DText(stack, txt, true,0, renderZone, TextUtil.txtAlignment.RIGHT);
            ClientUtil.blit3DItem(stack, renderZone, Items.WOODEN_AXE.getDefaultInstance(), null, null, 0);

            stack.popPose();
            //            RenderSystem.popPipelineModifier();
        }
    }
}