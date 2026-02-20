package invoker54.invocore.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.events.client.ClientGuiEvent;
import invoker54.invocore.client.invoimage.InvoImage;
import invoker54.invocore.client.invoimage.InvoImageTexture;
import invoker54.invocore.client.util.ClientUtil;
import invoker54.invocore.client.util.InvoText;
import invoker54.invocore.client.util.InvoZone;
import invoker54.invocore.client.util.TextUtil;
import invoker54.invocore.common.ModLogger;
import invoker54.invocore.common.util.MathUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.joml.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestEventCommon implements ClientGuiEvent.RenderHud {
    public static final ModLogger LOGGER = ModLogger.getLogger(TestEventCommon.class, new AtomicBoolean(true));
    private static float aFloat = 0;
    private static float bFloat = 0;
    private static InvoImageTexture sprite;

    @Override
    public void renderHud(GuiGraphics guiGraphics, float tickDelta) {
        if (!ClientUtil.getMinecraft().isPaused() && ClientUtil.getMinecraft().screen == null) aFloat += tickDelta/2;
        if (!ClientUtil.getMinecraft().isPaused() && ClientUtil.getMinecraft().screen == null) bFloat += tickDelta;
        float resulta = (float) (Math.sin(aFloat / 32f) + 1) / 2F;
        float resultb = (float) (Math.cos(bFloat / 32f) + 1) / 2F;
        float result2 = Mth.lerp(resulta, 4, 32);
        PoseStack stack = guiGraphics.pose();
        float width = guiGraphics.guiWidth();
        float height = guiGraphics.guiHeight();
//
//        InvoZone colorZone = new InvoZone(0, width, 0, height);
//        ClientUtil.blitColor(stack, colorZone, new Color(94, 94, 94, 142).getRGB());
////            ClientUtil.blitColor(stack, 0, 64, 0, 64, new Color(1, 1, 1, 123).getRGB());
////        ClientUtil.blitItem(stack, 180, -64, 16, 64, new ItemStack(Items.ITEM_FRAME));
////        ClientUtil.blitItem(stack, 64, 10, 0, 10, new ItemStack(Items.GOLD_BLOCK));
////        ClientUtil.blitItem(stack, 116, result2, Mth.lerp(resulta, 0, height / 2F), result2, new ItemStack(Items.GOLD_BLOCK));
////        ClientUtil.blitColor(stack, (width / 2F) - 1, 2, 0, height, new Color(1, 1, 1, 255).getRGB());
////        ClientUtil.blitColor(stack, 0, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
////        ClientUtil.blitColor(stack, width - 1, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
//
//        ClientUtil.blitColor(stack, colorZone.copy().setWidth(1).centerX(colorZone.middleX()), new Color(1, 1, 1, 255).getRGB());
//        ClientUtil.blitColor(stack, colorZone.copy().setX(0).setWidth(1), new Color(1, 1, 1, 255).getRGB());
//        ClientUtil.blitColor(stack, colorZone.copy().setX(width-1).setWidth(1), new Color(1, 1, 1, 255).getRGB());
//
////        ClientUtil.blitColor(stack, width / 4F, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
////        ClientUtil.blitColor(stack, ((width / 4F) + width / 2F) - 1, 1, 0, height, new Color(1, 1, 1, 255).getRGB());
//
////        ClientUtil.blitColor(stack, width / 4F, width / 4F, height - (height / 8F) * 2, height / 8F, new Color(0, 84, 7, 255).getRGB());
//
////        MutableComponent txt = Component.literal("Don't you understand that I am trying to help you?");
////        ClientUtil.blitColor(stack, width / 4F, width / 4F, height / 5F, height / 8F, new Color(0, 84, 7, 158).getRGB());
//
////        guiGraphics.renderItem(new ItemStack(Items.RECOVERY_COMPASS), 16, 72);
////        InvoImage image = InvoImage.fromResource(new ResourceLocation("textures/block/fire_0"));
////        image.getRenderZone().setX(0).setY(0).setWidth(64).setHeight(64);
////        image.getImageZone().setX(1600).setY(368).setWidth(16).setHeight(16);
////            image.getImageZone().setWidth(16).setHeight(16);
////            image.getImageZone().inflate(-4,-4);
////        image.render(stack);
////            image.render(stack);
////            image2.crop(image2.getRenderZone().inflate(-4,4));
////        var sprite = Minecraft.getInstance().getModelManager().
////                getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(new ResourceLocation("block/fire_0"));
////        guiGraphics.blit(sprite.atlasLocation(),
////                32, 0, 0, 0, 128, 128, (int) (16 /(sprite.getU1() - sprite.getU0())), (int) (16 /(sprite.getV1() - sprite.getV0())));
//        float sizeThing1 = Mth.lerp(resulta, 4, 80);
//        float sizeThing2 = Mth.lerp(resulta, 40, 16);
////        InvoImageColor colorImage = InvoImage.fromColor(Color.red);
////        colorImage.setMainZone(colorImage.getMainZoneCopy().setX(0).setY(0).setWidthConstraint(64));
////        colorImage.render(stack);
//
//        InvoImageTexture sprite2 = InvoImage.fromTexture(ResourceUtil.create(Invocore.MOD_ID,"cog_wheel"));
//        sprite2.setOuterOperation(ImageOperation.TILE);
////        sprite2.canvas(sprite2.getMainZoneCopy()).render(stack, new InvoZone(64,128,0,128).setWidthConstraint(Mth.lerp(resulta, 8, 128)));
////        LOGGER.error("What's the sprite size? " + sprite2.serializeNBT().sizeInBytes());
////        sprite.getMainZone().setX(0).setY(0).setWidthConstraint(64);
//
////        image1.getRenderZone().setX(0).setY(0).setWidthConstraint(16);
////        image1.render(stack);
////        image1.renderTiles(stack, new InvoZone(16,64,16,64).inflate(16,16), true, true);
////        image1.renderTiles(stack, new InvoZone(0, 256, 0,256), false);
////        txt = Component.literal("\2474 \247l Don't you understand that");
////        txt.append(Component.literal("\n I'm trying to help you? Foolish.".toUpperCase(Locale.ROOT)));
////        if (ClientUtil.getWorld().getGameTime() % 30 == 0){
////            System.out.println("text: " + txt.getString());
////        }
//
////        LOGGER.warn("What's alpha: " + ((int)(255 * resulta)));
//
////        LOGGER.warn("Custom color" + (new Color(59, 65, 159, 169).getRGB()));
//        InvoText exampleText = InvoText.literal("&r&#("+new Color(251, 0, 0, 255).getRGB()
//                +")This is just a test.\n&r&1It should be working correctly.\n");
//        exampleText.getProperties().setMaxTextSize(30);
//        exampleText.getProperties().setMinTextSize(8);
////        exampleText.render(stack, new InvoZone(width/2f,64f,height/2f,64f));
//        exampleText = StringUtil.formatText(exampleText, true);
//        exampleText.render(stack, new InvoZone(width/2f,64f,height/2f,64f));
//
////        LOGGER.error("What's the height: " + (height/8F));
////        LOGGER.error("What's the width: " + (width/4F));
////        TextUtil.renderText(stack, txt, true, 0,
////                width / 4F, width / 4F, height / 5F, height / 8F, 0, TextUtil.txtAlignment.MIDDLE, 8f, 30f);
//
//        InvoImageTexture background = InvoImage.fromTexture(
//                ResourceUtil.create(Invocore.MOD_ID, "missing"));
//        background.setOuterOperation(ImageOperation.TILE);
//        background.setInnerOperation(ImageOperation.TILE);
//        background.setSubImageZone(background.getImageZoneCopy().inflate(-4));
////        background.setMainZone(new InvoZone(64, 16, 0, 16));
////        colorImage.render(stack, background.getSubRenderZone());
//        background.setPivot(new InvoZone(32, 32, 32, 16).middle());
//        InvoImage blackImage = InvoImageColor.fromColor(Color.BLACK);
//        blackImage.setMainZone(InvoZone.fromPoint(background.getPivotPoint()).stretch(true).setDown(64)
//                .setWidth(2).stretch(false).shiftXY(-0.5f,0), false);
//        blackImage.setPivot(new Vector2f(blackImage.getMainZoneCopy().x(), blackImage.getMainZoneCopy().y()));
//        blackImage.setRotation(Mth.lerp(resulta, 0, 360));
//        blackImage.render(stack);
//        background.setMainZone(new InvoZone(32, 32, 64, 80), false);
//        background.setMaxImageSize(Mth.lerp(resulta, 1f, 2));
//        InvoImage.fromColor(Color.YELLOW).render(stack, new InvoZone(140, 100, 80, 100));
//        background.render(stack, new InvoZone(140, 100, 80, 100));
//
////        background.setPivotRotation(background.getMainZoneCopy().middle().add());
//        InvoImage colorImage = InvoImageColor.fromColor(Color.RED);
//        colorImage.setMainZone(background.getSubRenderZone(background.getMainZoneCopy()), true);
//        InvoImage image = background.canvas(background.getSubRenderZone(background.getMainZoneCopy()), List.of(colorImage));
//
////        colorImage.setMainZone(background.getSubRenderZone());
////        image = image.canvas(List.of(colorImage));
////        InvoImage image = background.canvas(background.getMainZoneCopy());
////        InvoImageColor.fromColor(Color.GREEN).render(stack, new InvoZone(64, 16, 0, 16));
////        LOGGER.error("Start rendering");
////        image.render(stack);
////        colorImage.render(stack);
////        image.canvas(List.of(colorImage)).render(stack, new InvoZone(64, 32, 32, Mth.lerp(resulta, 2, 32)));
////        LOGGER.error("What's Texture: " + background.originalTextureZone);
////        LOGGER.error("What's main? " + background.getMainZoneCopy());
////        LOGGER.error("What's image? " + background.getImageZoneCopy());
////        LOGGER.error("What's sub? " + background.getSubImageZoneCopy());
////        colorImage.setMainZone();
//        image.setPivot(background.getPivotPoint());
//        image.setRotation(Mth.lerp(resulta, 0, 360));
//        image.render(stack);
////        InvoImageColor.fromColor(Color.GRAY).render(stack, InvoZone.fromPoint(image.getPivotPoint()).inflate(1));
//        InvoImageItem stick = InvoImage.fromItem(new ItemStack(Items.STICK));
//        stick.setMainZone(new InvoZone(32, 16, 32, 16), false);
//        stick.setPivot(stick.getMainZoneCopy().bottomLeft());
//        stick.setRotation(Mth.lerp(resulta, 0, 360));
//        stick.render(stack);

//        [x:142.5, width:142.0, y:24.0, height:192.0, stretch:false]
//        InvoZone redZone = new InvoZone(142.5f, 142, 24, 192);
//        InvoImage.fromColor(Color.RED).render(stack, redZone);
//        //[x:142.0, width:137.0, y:24.0, height:18.0, stretch:false]
//        InvoZone greenZone = new InvoZone(142, 137, 24, 18);
//        InvoImage.fromColor(Color.GREEN).render(stack, greenZone);
//        InvoZone neutralZone = redZone.intersect(greenZone);
//        InvoImage.fromColor(Color.GRAY).render(stack, neutralZone);
//
//        if (neutralZone.inBounds(redZone, true)) LOGGER.warn("In bound");
//        else LOGGER.warn("Out of bounds");
//        InvoImageColor.fromColor(Color.GREEN).render(stack, InvoZone.fromPoint(image.getPivotPoint()));

//        InvoImageColor.fromColor(Color.RED).render(stack, new InvoZone(32, 1, 32, 1));
//        colorImage.setMainZone(background.getSubRenderZone());
//        LOGGER.error("image zone: " + image.getImageZoneCopy().toString());
//        LOGGER.error("sub zone: " + image.getSubImageZoneCopy().toString());
//        LOGGER.error("render zone: " + image.getImageZoneCopy().changeRelative(image.getImageZoneCopy(), image.getSubImageZoneCopy()).toString());
//        colorImage.render(stack, image.getSubImageZoneCopy().changeRelative(image.getImageZoneCopy(), image.getMainZoneCopy()));
//        image = image.canvas(List.of(colorImage));
//        image.setMainZone(new InvoZone(192, 32, 32, 100));
//        image.render(stack);
//        InvoImage.fromColor(Color.RED).render(stack, new InvoZone(64, 80, 80, 64));
        InvoZone paddedZone = new InvoZone(0, guiGraphics.guiWidth(), 0, guiGraphics.guiHeight());
        paddedZone = paddedZone.copy().inflate(-paddedZone.width()/4, -paddedZone.height()/4).center(paddedZone);
        paddedZone.setWidth(96);

//        String s = "Lorem &lipsum dolor sit amet, consectetur adipiscing elit. Aliquam eget metus ultrices, interdum neque non, sodales diam. Morbi hendrerit urna lorem, sed faucibus nisl venenatis a. Cras rutrum felis accumsan lacinia tempus. Nunc egestas, magna ac sagittis vehicula, neque quam ultricies augue, ut gravida quam ipsum at est.";
      String s = "Lorem&l ipsum dolor sit amet, consectetur adipiscing elit. ac sagittis vehicula, neque quam ultricies augue, ut gravida quam ipsum at est.";
        InvoImage.fromColor(Color.BLACK).render(stack, paddedZone);
        guiGraphics.drawWordWrap(ClientUtil.getFont(), InvoText.literal(s).getText(false),
                (int)paddedZone.x(), (int)paddedZone.y(), (int)paddedZone.width(),  new Color(61, 135, 135,233).getRGB());

//        InvoImage.fromColor(Color.RED).render(stack, paddedZone.copy().setWidth(30).setHeight(10));
        InvoText text = new InvoText.Properties().setShadow(false).setTxtAlignment(TextUtil.TextAlign.MID_RIGHT)
                .setShadow(false).setTextSize(9).text(InvoText.literal(s));
        TextUtil.TextViewer textViewer = text.getTextViewer(true, paddedZone);
        InvoZone textZone = textViewer.textZone();
        InvoImage.fromColor(new Color(255, 0, 0, 86)).render(stack, textZone);
        text.renderWithActualZone(stack, true, paddedZone, textZone.setDown(paddedZone.down()));
        InvoZone pointZone = InvoZone.fromPoint(new Vector2f(MathUtil.lerp(resultb, textZone.right()-3, textZone.right()),
//        InvoZone pointZone = InvoZone.fromPoint(new Vector2f(textZone.right(),
                textZone.down())).inflate(1);
//                MathUtil.lerp(resulta, textZone.y(), textZone.down()))).inflate(1);
//        MathUtil.lerp(resulta, textZone.y(), textZone.down())
        List<InvoZone> zoneList = textViewer.getTextZones(0, textViewer.getIndex(pointZone.middleX(), pointZone.middleY()));
//                            if (ClientUtil.getWorld().getGameTime() % 40 == 0) {
//                                LOGGER.error("What's index: " + textViewer.getIndex(pointZone.middleX(), pointZone.middleY()));
//                            }
        zoneList.forEach(zone -> {
                    InvoImage.fromColor(new Color(244, 84, 244, 121)).render(stack, zone);

//                    if (ClientUtil.getWorld().getGameTime() % 10 == 0) {
//                    }
                }
        );
        InvoImage.fromColor(Color.BLUE).render(stack, pointZone);

        text.render(stack, paddedZone.copy().setX(paddedZone.right()));
        if (ClientUtil.getWorld().getGameTime() % 10 == 0){
//            LOGGER.error("index: " + textViewer.getIndex(pointZone.middleX(), pointZone.middleY()));
//            LOGGER.error("Text length: " + s.length());
        }
//        new InvoText.Properties().setShadow(false).setTxtAlignment(TextUtil.TextAlign.MID).setShadow(false).setTextSize(8)
//                .text(InvoText.literal(s)).render(stack, paddedZone);

//        LOGGER.warn("what's width: " + ClientUtil.getFont().width("blah blah"));
    }
}