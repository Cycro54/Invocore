package invoker54.invocore.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.events.client.ClientGuiEvent;
import invoker54.invocore.client.invoimage.InvoImage;
import invoker54.invocore.client.invoimage.InvoImageColor;
import invoker54.invocore.client.invoimage.InvoImageSprite;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.util.InvoZone;
import invoker54.invocore.client.util.TextUtil;
import invoker54.invocore.common.ModLogger;
import invoker54.invocore.common.util.ResourceUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestEventCommon implements ClientGuiEvent.RenderHud {
    public static final ModLogger LOGGER = ModLogger.getLogger(TestEventCommon.class, new AtomicBoolean(true));
    private static float aFloat = 0;
    private static float bFloat = 0;
    private static InvoImageSprite sprite;

    @Override
    public void renderHud(GuiGraphics guiGraphics, float tickDelta) {
        aFloat += tickDelta;
        bFloat += tickDelta;
        float resulta = (float) (Math.sin(aFloat / 32f) + 1) / 2F;
        float resultb = (float) (Math.cos(aFloat / 32f) + 1) / 2F;
        float result2 = Mth.lerp(resulta, 4, 32);
        PoseStack stack = guiGraphics.pose();
        float width = guiGraphics.guiWidth();
        float height = guiGraphics.guiHeight();

        if (sprite == null){
            sprite = InvoImage.fromSprite(ResourceUtil.create("block/dirt"));
        }

        ClientUtil.blitColor(stack, 0, width, 0, height, new Color(94, 94, 94, 142).getRGB());
//            ClientUtil.blitColor(stack, 0, 64, 0, 64, new Color(1, 1, 1, 123).getRGB());
        ClientUtil.blitItem(stack, 16, 64, 16, 64, new ItemStack(Items.ITEM_FRAME));
        ClientUtil.blitItem(stack, 64, 10, 0, 10, new ItemStack(Items.GOLD_BLOCK));
        ClientUtil.blitItem(stack, 116, result2, Mth.lerp(resulta, 0, height / 2F), result2, new ItemStack(Items.GOLD_BLOCK));
        ClientUtil.blitColor(stack, (width / 2F) - 1, 2, 0, height, new Color(1, 1, 1, 255).getRGB());
        ClientUtil.blitColor(stack, 0, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
        ClientUtil.blitColor(stack, width - 1, 1, 0, height, new Color(1, 1, 1, 255).getRGB());

        ClientUtil.blitColor(stack, width / 4F, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
        ClientUtil.blitColor(stack, ((width / 4F) + width / 2F) - 1, 1, 0, height, new Color(1, 1, 1, 255).getRGB());


        ClientUtil.blitColor(stack, width / 4F, width / 4F, height - (height / 8F) * 2, height / 8F, new Color(0, 84, 7, 255).getRGB());

        MutableComponent txt = Component.literal("Don't you understand that I am trying to help you?");
        ClientUtil.blitColor(stack, width / 4F, width / 4F, height / 5F, height / 8F, new Color(0, 84, 7, 158).getRGB());

//        guiGraphics.renderItem(new ItemStack(Items.RECOVERY_COMPASS), 16, 72);
//        InvoImage image = InvoImage.fromResource(new ResourceLocation("textures/block/fire_0"));
//        image.getRenderZone().setX(0).setY(0).setWidth(64).setHeight(64);
//        image.getImageZone().setX(1600).setY(368).setWidth(16).setHeight(16);
//            image.getImageZone().setWidth(16).setHeight(16);
//            image.getImageZone().inflate(-4,-4);
//        image.render(stack);
//            image.render(stack);
//            image2.crop(image2.getRenderZone().inflate(-4,4));
//        var sprite = Minecraft.getInstance().getModelManager().
//                getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(new ResourceLocation("block/fire_0"));
//        guiGraphics.blit(sprite.atlasLocation(),
//                32, 0, 0, 0, 128, 128, (int) (16 /(sprite.getU1() - sprite.getU0())), (int) (16 /(sprite.getV1() - sprite.getV0())));
        float sizeThing1 = Mth.lerp(resulta, 4, 80);
        float sizeThing2 = Mth.lerp(resulta, 40, 16);
        InvoImageColor colorImage = InvoImage.fromColor(Color.red);
        colorImage.setMainZone(colorImage.getMainZoneCopy().setX(0).setY(0).setWidthConstraint(64));
        colorImage.render(stack);
        sprite.setOuterOperation(InvoImageSprite.ImageOperation.STRETCH);
        sprite.render(stack, new InvoZone(0,64,0,64));

        InvoImageSprite sprite2 = InvoImage.fromSprite(ResourceUtil.create("block/dirt"));
        sprite2.setOuterOperation(InvoImageSprite.ImageOperation.TILE);
        sprite2.render(stack, new InvoZone(64,128,0,128).setWidthConstraint(Mth.lerp(resulta, 8, 128)));
//        LOGGER.error("What's the sprite size? " + sprite2.serializeNBT().sizeInBytes());
//        sprite.getMainZone().setX(0).setY(0).setWidthConstraint(64);

//        image1.getRenderZone().setX(0).setY(0).setWidthConstraint(16);
//        image1.render(stack);
//        image1.renderTiles(stack, new InvoZone(16,64,16,64).inflate(16,16), true, true);
//        image1.renderTiles(stack, new InvoZone(0, 256, 0,256), false);
        txt = Component.literal("\2474 \247l Don't you understand that");
        txt.append(Component.literal("\n I'm trying to help you? Foolish.".toUpperCase(Locale.ROOT)));
        if (ClientUtil.getWorld().getGameTime() % 30 == 0){
            System.out.println("text: " + txt.getString());
        }
//        LOGGER.error("What's the height: " + (height/8F));
//        LOGGER.error("What's the width: " + (width/4F));
        TextUtil.renderText(stack, txt, true, 2,
                width / 4F, width / 4F, height / 5F, height / 8F, 8, TextUtil.txtAlignment.MIDDLE, 8f, 30f);
    }
}