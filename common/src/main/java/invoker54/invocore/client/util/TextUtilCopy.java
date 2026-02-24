package invoker54.invocore.client.util;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import invoker54.invocore.Invocore;
import invoker54.invocore.common.ModLogger;
import invoker54.invocore.common.util.MathUtil;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import org.joml.Matrix4f;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class TextUtilCopy {
//    public static ModLogger LOGGER = ModLogger.getLogger(TextUtilCopy.class, Invocore.debugMode);
//    private static int black = new Color(0, 0, 0, 255).getRGB();
//    //    private static final ModLogger LOGGER = ModLogger.getLogger(TextUtil.class, );
//    private static CharacterManagerMixer characterMixer;
//
//    public enum TextAlign {
//        TOP_LEFT,
//        TOP_MIDDLE,
//        TOP_RIGHT,
//        MID_LEFT,
//        MID,
//        MID_RIGHT,
//        BOT_LEFT,
//        BOT_MIDDLE,
//        BOT_RIGHT;
//    }
//
//    public static TextUtilCopy.TextViewer renderText(PoseStack stack, Component text, InvoZone textZone, InvoText.Properties properties, boolean keepSpecialCodes, boolean shouldRender) {
//        return renderText(stack, text, properties.isShadow(), properties.getMaxSplits(), textZone.x(), textZone.width(), textZone.y(), textZone.height(),
//                properties.getPadding(), properties.getTxtAlignment(), properties.getMinTextSize(), properties.getMaxTextSize(), keepSpecialCodes, shouldRender);
//    }
//
//    public static TextUtilCopy.TextViewer renderText(PoseStack stack, Component text, boolean shadow, int maxSplits,
//                                                     InvoZone renderZone, TextAlign alignment, boolean keepSpecialCodes, boolean shouldRender) {
//        return renderText(stack, text, shadow, maxSplits, renderZone.x(), renderZone.width(), renderZone.y(),
//                renderZone.height(), 0, alignment, 0.1f, Float.MAX_VALUE, keepSpecialCodes, shouldRender);
//    }
//
//    public static TextUtilCopy.TextViewer renderText(PoseStack stack, Component text, boolean shadow, int maxSplits,
//                                                     float x0, float maxWidth, float y0, float maxHeight, int padding, TextAlign align,
//                                                     float minTextSize, float maxTextSize, boolean keepSpecialCodes, boolean shouldRender) {
////        if (characterMixer == null) characterMixer = new CharacterManagerMixer();
////        if (text.getString().isEmpty()) text = InvoText.literal(" ").setStyle(text.getStyle()).getText();
//
//        int textWidth = ClientUtil.getFont().width(text);
//
//        //I took this from the if statement
//        // && maxHeight > mC.font.lineHeight
//        //Let's try this again.
//        //I have to make it so the text fits PERFECTLY inside the space provided.
//        //What that means is, I have to cut the text at the correct spots.
//
////            //First grab the X Y ratio for the space
////            double spaceRatio = maxWidth / maxHeight;
////            //Since Y has to be multiples of 9, make the ratio a multiple of 9
////            spaceRatio *= 9;
//
////            //Grab the textArea we will be working with
////            double textArea = 9 * textWidth;
////            //Do the formula u got from mathSolver to get the multiplier that I can use on the spaceRatio
////            double multiplier = textArea / (spaceRatio * 9);
////            multiplier = Math.sqrt(multiplier);
////            //and FINALLY, multiply spaceRatio with the multiplier, and that should be the cutoff point!
////            double cutoffPoint = Math.ceil(multiplier * spaceRatio);
////            double neededSplits = ((double) textWidth /cutoffPoint);
//
//        //I have the amount of space it's going to take up
//        //I have the space it's going to occupy
//        //I have to make it so the space it takes up equals the space it will occupy
//
//        //First grab the X ratio for the text space (for each 1 height, there is xRatio)
//        double textArea = textWidth * ClientUtil.getFont().lineHeight;
//        double spaceArea = maxWidth * maxHeight;
//        double scalingFactor = Math.sqrt(spaceArea / textArea);
//        double textSize = Math.sqrt((ClientUtil.getFont().lineHeight * spaceArea) / textWidth);
//        double updatedTextSize = MathUtil.clamp(textSize, minTextSize, maxTextSize);
//        double percentChange = updatedTextSize / textSize;
//        double updatedTextWidth = Math.sqrt((textWidth * spaceArea) / ClientUtil.getFont().lineHeight) * percentChange;
//        double cutoffPercent = maxWidth / updatedTextWidth;
//        double cutOffPoint = textWidth * cutoffPercent;
//
////        LOGGER.error("Space area: " + spaceArea);
////        LOGGER.error("Text area: " + textArea);
////        LOGGER.error("Text width: " + textWidth);
////        LOGGER.error("Text height: " + ClientUtil.getFont().lineHeight);
////        LOGGER.error("Text Size: " + textSize);
////        LOGGER.error("percentChange: " + percentChange);
////        LOGGER.error("Text new width: " + Math.sqrt((textWidth * spaceArea)/ClientUtil.getFont().lineHeight));
////        LOGGER.error("Cutoff percent: " + cutoffPercent);
////        LOGGER.error("Full Volume: " + (Math.sqrt((textWidth * spaceArea)/ClientUtil.getFont().lineHeight) * textSize));
////        LOGGER.error("Adjusted Volume: " + (updatedTextSize * updatedTextWidth));
//
//        double neededSplits = MathUtil.clamp((int) Math.ceil(maxHeight / updatedTextSize), 1, Integer.MAX_VALUE);
////        double neededSplits = Math.floor(textWidth / cutOffPoint);
//
//        boolean maxSplitExceeded = neededSplits > maxSplits;
//
//        if (maxSplits != 0 && (maxSplitExceeded || maxSplits < 0 && Math.abs(maxSplits) > neededSplits)) {
//            neededSplits = Math.abs(maxSplits);
//            cutOffPoint = textWidth / neededSplits;
//        }
//
////        LOGGER.error("Cut off point? " + cutOffPoint);
////        LOGGER.error("Needed Splits? " + neededSplits );
////        LOGGER.error("Old Text size? " + textSize);
////        LOGGER.error("New Text size? " + updatedTextSize);
//
//
//        //TODO: Try and include text height too, right now it just accounts for text width
////        double textSizeByWidth = (maxWidth / cutOffPoint) * ClientUtil.getFont().lineHeight;
////        double textSizeByHeight = maxHeight / (maxWidth / cutOffPoint);
////        double minSize = Math.min(textSizeByWidth, textSizeByHeight);
////        double maxSize = Math.max(textSizeByWidth, textSizeByHeight);
//////            LOGGER.error("Min size: " + minSize);
//////            LOGGER.error("Max size: " + maxSize);
////
//////            double textSize = 0;
//////            if (neededSplits > 1){
//////                textSize =
//////            }
////
////        if (maxSize < minTextSize) {
////            textWidth = (int) ((maxWidth * neededSplits * ClientUtil.getFont().lineHeight) / minTextSize);
////            cutOffPoint = textWidth / neededSplits;
////        }
////        if (maxSize > maxTextSize) {
////            cutOffPoint = Math.ceil((maxWidth * ClientUtil.getFont().lineHeight) / maxTextSize);
////            neededSplits = Math.ceil(textWidth / cutOffPoint);
////        }
////        list.addAll((Collection<? extends FormattedText>) ClientUtil.getFont().split(text, (int) cutOffPoint));
//
////        text = text.copy().withStyle(ChatFormatting.YELLOW);
////        List<FormattedText> list = new ArrayList<>(ClientUtil.getFont().getSplitter().splitLines(text, (int) Math.floor(cutOffPoint), Style.EMPTY));
////        list.forEach( obj -> {
////                    LOGGER.warn("text: '" + obj.getString() + "'");
////                    LOGGER.warn("length: '" + ClientUtil.getFont().width(obj) + "'");
////                });
////        List<FormattedText> list = new ArrayList<>();
////        Component finalText = text;
////        ClientUtil.getFont().split(text, (int) Math.floor(cutOffPoint)).forEach(sequence -> {
////            LOGGER.error("sequence: " + sequence.toString());
//////            sequence.
////            sequence.accept((pos, style, charCode) ->{
////                LOGGER.warn("SIMPLICITY: '" + Character.getName(charCode) + "'");
////
//////                LOGGER.warn("length: '" + ClientUtil.getFont().width(obj) + "'");
////                return false;
////            });
////        });
////        ClientUtil.getFont().getSplitter().splitLines(text.getString(), (int) Math.floor(cutOffPoint), Style.EMPTY, true, (stylex, i, j) -> {
////            list.add(FormattedText.of(finalText.getString().substring(i, j), stylex));
////        });
//        List<FormattedText> list = ClientUtil.getFont().getSplitter().splitLines(text, (int) Math.floor(cutOffPoint), Style.EMPTY);
//        int count = 0;
//        for (FormattedText text1 : list){
//            count += text1.getString().length();
//        }
//        LOGGER.error("Old length: " + text.getString().length());
//        LOGGER.error("New length: " + count);
////        characterMixer.splitLines(text, (int) cutOffPoint, Style.EMPTY, (int) Math.abs(neededSplits), (A, cutShort) -> {
////            list.add(A);
////        });
//
//        return renderText(stack, text, list, shadow, x0, maxWidth, y0, maxHeight, padding, align, minTextSize, maxTextSize, shouldRender);
//    }
//
//    public static TextUtilCopy.TextViewer renderText(PoseStack stack, FormattedText originalText, List<FormattedText> textLines, boolean shadow,
//                                                     float x0, float maxWidth, float y0, float maxHeight, int padding, TextAlign align,
//                                                     float minTextSize, float maxTextSize, boolean shouldRender) {
//        Font font = ClientUtil.getFont();
//
//        if (stack != null) stack.pushPose();
//
//        float maxTxtHeight = textLines.size() * ClientUtil.getFont().lineHeight;
////        maxTxtHeight += -2 + textLines.size();
////        LOGGER.info("Max Text Height is " + maxTxtHeight);
////        maxTxtHeight += (padding * 2);
////        LOGGER.info("After padding it is " + maxTxtHeight);
//
//        float maxTxtWidth = 0;
//        FormattedText largestComponent = textLines.get(0);
//        for (FormattedText textComponent : textLines) {
//            int currentWidth = font.width(textComponent);
//            if (currentWidth > maxTxtWidth) {
//                maxTxtWidth = currentWidth;
//                largestComponent = textComponent;
//            }
//        }
////        LOGGER.info("Max Text Width is " + maxTxtWidth);
////        maxTxtWidth += (padding * 2);
//        //There is 1 blank space in front of the last c0, this will remove that.
//        maxTxtWidth -= 1;
////        LOGGER.info("After padding it is " + maxTxtWidth);
//
//        float shadowOffset = 0;
//        if (shadow) {
//            shadowOffset = 1;
////            LOGGER.debug("What is offset? " + offset);
//            maxTxtHeight += shadowOffset;
//            maxTxtWidth += shadowOffset;
//        }
//
//        float heightFillAmount = maxHeight / maxTxtHeight;
////        LOGGER.info("Height Left is " + heightLeft);
//        float widthFillAmount = maxWidth / maxTxtWidth;
////        LOGGER.info("Width Left is " + widthLeft);
//        float scaleFactor = 0;
//        boolean isMiddle = align == TextAlign.TOP_MIDDLE || align == TextAlign.MID || align == TextAlign.BOT_MIDDLE;
//
//        if (heightFillAmount < widthFillAmount || heightFillAmount == widthFillAmount) {
//            // LOGGER.error("Height");
////                LOGGER.info("heightFillAmount was smaller than widthFillAmount");
//            //example: maxHeight is 70, txtMaxHeight is 60.
//            //That means maxHeight is 1.16 times larger than the txtMaxHeight
//            scaleFactor = ((maxHeight - (isMiddle ? (padding * 2) : padding)) / maxTxtHeight);
//        } else if (heightFillAmount > widthFillAmount) {
//            // LOGGER.error("Width");
////                LOGGER.info("widthFillAmount was smaller than heightFillAmount");
//            //example: maxWidth is 50, txtMaxWidth is 25.
//            //That means maxWidth is 2 times larger than the txtMaxWidth
//            scaleFactor = ((maxWidth - (isMiddle ? (padding * 2) : padding)) / maxTxtWidth);
//        } else {
//            // LOGGER.error("Neither");
//        }
////        LOGGER.debug("What's padding amount to remove? " + ((1F/maxWidth) * padding * 2));
////        scaleFactor -= ((1F/maxWidth) * padding * 2);
////        LOGGER.debug("What's the scaleFactor factor? " + scaleFactor);
//        scaleFactor = (float) MathUtil.clamp(scaleFactor, (minTextSize / font.lineHeight), (maxTextSize / font.lineHeight));
////        scaleFactor = Math.min(scaleFactor, (scaleFactor/font.lineHeight) * maxTextSize);
//
//        // LOGGER.warn("what's my scaleFactor factor" + scaleFactor);
//        if (shouldRender) {
//            stack.scale(scaleFactor, scaleFactor, scaleFactor);
//
//            //Since I changed the Scale of the text, I have to recalculate the maxTxtHeight and maxTxtWidth
////        maxTxtHeight = (maxTxtHeight/scaleFactor);
//
////        if (((int)ClientUtil.getWorld().getGameTime()) % 40 == 0) {
////            LOGGER.error("Text total space: " + (font.lineHeight * textLines.size()));
////            LOGGER.error("Max Height: " + (maxHeight/scaleFactor));
////            LOGGER.error("Percent taken : " + ((font.lineHeight * textLines.size())/(maxHeight/scaleFactor)));
////        }
//
//            for (int a = 0; a < textLines.size(); ++a) {
//                FormattedText currText = textLines.get(a);
//
//                float y = y0 / scaleFactor;
////            y = y + ((((maxHeight - (maxTxtHeight * scaleFactor)) / 2F) + (a * font.lineHeight * scaleFactor)) / scaleFactor);
//                switch (align) {
//                    case TOP_LEFT:
//                    case TOP_MIDDLE:
//                    case TOP_RIGHT: {
//                        // LOGGER.error("This is ze way");
//                        float textSpace = (font.lineHeight) * a;
//                        y = y + (textSpace);
//                        break;
//                    }
//                    case MID_LEFT:
//                    case MID:
//                    case MID_RIGHT: {
//                        float spaceLeft = (maxHeight / scaleFactor) - (font.lineHeight * textLines.size());
//
////                     LOGGER.error("Space left: " + (maxHeight - (font.lineHeight * textLines.size())));
////                    y = y + ((((maxHeight - (maxTxtHeight * scaleFactor)) / 2F) + (a * font.lineHeight * scaleFactor)) / scaleFactor);
//
//                        y = y + ((font.lineHeight) * a) + (spaceLeft / 2);
//                        break;
//                    }
//                    case BOT_LEFT:
//                    case BOT_MIDDLE:
//                    case BOT_RIGHT: {
//                        // LOGGER.error("This is the last way");
//                        float spaceLeft = (maxHeight / scaleFactor) - (font.lineHeight * textLines.size());
//                        y = y + ((font.lineHeight) * a) + (spaceLeft);
////                    y = y + ((((maxHeight - (maxTxtHeight * scaleFactor)) / 2F) + (a * font.lineHeight * scaleFactor)) / scaleFactor) * 2;
//                        break;
//                    }
//                }
//
////            LOGGER.debug("max height is: "+ maxHeight);
////            LOGGER.debug("base empty space is: " + (maxHeight - (maxTxtHeight * scaleFactor)));
////            LOGGER.debug("resulting y spot is: " + (((maxHeight - (maxTxtHeight * scaleFactor))/2F) + (a * font.lineHeight * scaleFactor)));
//
//                float x = x0;
//                switch (align) {
//                    case TOP_LEFT:
//                    case MID_LEFT:
//                    case BOT_LEFT:
//                        x = (x) / scaleFactor;
//                        break;
//                    case TOP_MIDDLE:
//                    case MID:
//                    case BOT_MIDDLE:
//                        x = ((x + ((maxWidth - ((font.width(currText) - (1 - shadowOffset)) * scaleFactor)) / 2F)) / scaleFactor);
////                    LOGGER.debug("Max Width: " + (maxWidth));
////                    LOGGER.debug("Font Width is now: " + (font.getStringWidth(currText) * scaleFactor));
////                    LOGGER.debug("What's the empty space: " + ((maxWidth) - (font.getStringWidth(currText) * scaleFactor)));
////                    LOGGER.debug("Where will the top left be for the text: " + x);
//                        break;
//                    case TOP_RIGHT:
//                    case MID_RIGHT:
//                    case BOT_RIGHT:
//                        x = (((x + maxWidth) / scaleFactor) - (((padding) + ((font.width(currText) - (1 - shadowOffset)) * scaleFactor)) / scaleFactor));
//                        break;
//                }
//
//                renderText(currText, stack, x, y, shadow);
//            }
//        }
//
//        InvoZone textZone = new InvoZone(x0, maxWidth, y0, maxHeight);
//        switch (align) {
//            case TOP_LEFT:
//            case TOP_MIDDLE:
//            case TOP_RIGHT: {
//                textZone.stretch(true);
//                textZone.setDown(textZone.y() + ((font.lineHeight * scaleFactor) * textLines.size()));
////                float textSpace = (font.lineHeight) * a;
////                y = y + (textSpace);
//                break;
//            }
//            case MID_LEFT:
//            case MID:
//            case MID_RIGHT: {
//                float spaceLeft = (maxHeight / scaleFactor) - (font.lineHeight * textLines.size());
//                textZone.inflate(0, -spaceLeft / 2);
//                break;
//            }
//            case BOT_LEFT:
//            case BOT_MIDDLE:
//            case BOT_RIGHT: {
//                textZone.stretch(true);
//                float spaceLeft = (maxHeight / scaleFactor) - (font.lineHeight * textLines.size());
//                textZone.setY(y0 + (spaceLeft));
//                break;
//            }
//        }
//
//        if (stack != null) stack.popPose();
//        return new TextViewer(3, InvoText.literal("dw"), textZone.stretch(false), scaleFactor, align);
//    }
//
//    public static void renderText(FormattedText text, PoseStack stack, float x, float y, boolean shadow) {
//        MultiBufferSource.BufferSource irendertypebuffer$impl = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
//
////        boolean flag = !player.isDiscrete();
////        float f = player.getBbHeight() * 0.5f;
//        int i = "deadmau5".equals(text.getString()) ? -10 : 0;
//        Matrix4f matrix4f = stack.last().pose();
//
//        //This is the usual number, so let's keep it like that for now
//        int lightCoords = 15728880;
//
//        //float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
//        int j = (int) (0 * 255.0F) << 24;
//        Font fontrenderer = ClientUtil.getFont();
//
//        List<Pair<Style, String>> textList = new ArrayList<>();
//        int offset = 0;
//
//        //I am trying to make it so you can change the opacity of text if it calls for it...
//        text.visit((style, myText) -> {
////            LOGGER.warn("What's b: " + style);
//            textList.add(new Pair<>(style, myText));
//            return Optional.empty();
//        }, Style.EMPTY);
//
//        RenderSystem.disableDepthTest();
//        for (var formatPair : textList) {
//            TextColor color = formatPair.getA().getColor();
//            if (color == null) color = TextColor.fromRgb(0xFFFFFFFF);
//            FormattedText formattedText = FormattedText.of(formatPair.getB(), formatPair.getA());
//            Color textColor = new Color(color.getValue(), true);
//
//            fontrenderer.drawInBatch(Language.getInstance().getVisualOrder(formattedText),
//                    x + offset, y, textColor.getAlpha() << 24 | textColor.getRGB(), shadow, matrix4f, irendertypebuffer$impl,
//                    Font.DisplayMode.SEE_THROUGH, 0, lightCoords);
//
//            offset += ClientUtil.getFont().width(formattedText);
//        }
//        irendertypebuffer$impl.endBatch();
//        RenderSystem.enableDepthTest();
//    }
//
//    public static class CharacterManagerMixer extends StringSplitter {
//
//        public CharacterManagerMixer() {
//            super(ClientUtil.getFont().getSplitter().widthProvider);
//        }
//
//        public void splitLines(FormattedText text, int cutoffPoint, Style p_style, int neededSplits, BiConsumer<FormattedText, Boolean> splitifier) {
//            List<LineComponent> list = Lists.newArrayList();
//            text.visit((p_238355_1_, p_238355_2_) -> {
//                if (!p_238355_2_.isEmpty()) {
//                    LineComponent textComponent = new LineComponent(p_238355_2_, p_238355_1_);
////                    LOGGER.error("I am visiting this: " + textComponent.contents);
//                    list.add(textComponent);
//                }
//
//                return Optional.empty();
//            }, p_style);
//            FlatComponents charactermanager$substyledtext = new FlatComponents(list);
//            boolean complete = false;
//            boolean flag1 = false;
//            boolean keepGoing = false;
//            float averagePoint = cutoffPoint;
//            int count = 1;
//
//            while (!complete) {
//                complete = true;
//                MultilineProcessorMixer multilineProcessor = new MultilineProcessorMixer(cutoffPoint);
//
//
//                for (LineComponent charactermanager$styleoverridingtextcomponent : charactermanager$substyledtext.parts) {
////                    LOGGER.debug("Remaining Content: " + charactermanager$styleoverridingtextcomponent.contents);
//                    boolean flag3 = StringDecomposer.iterateFormatted(
//                            charactermanager$styleoverridingtextcomponent.contents, 0,
//                            charactermanager$styleoverridingtextcomponent.style, p_style, multilineProcessor);
//                    if (!flag3) {
//                        int i = multilineProcessor.getSplitPosition();
//                        Style style = multilineProcessor.getSplitStyle();
//                        char c0 = charactermanager$substyledtext.charAt(i);
//                        boolean shouldReturn = c0 == '\n';
//                        boolean shouldBreak = shouldReturn || c0 == ' ';
//                        flag1 = shouldReturn;
//                        FormattedText formattedText = charactermanager$substyledtext.splitAt(i, shouldBreak ? 1 : 0, style);
////                        LOGGER.error("Next Split: " + formattedText.getString());
//                        neededSplits--;
//                        boolean cutShort = (neededSplits == 0);
//                        if (cutShort) {
//                            formattedText = FormattedText.composite(formattedText, FormattedText.of("-", formattedText.visit((fStyle, fString) -> {
//                                return Optional.of(fStyle.withObfuscated(false));
//                            }, Style.EMPTY).get()));
//                        }
//                        splitifier.accept(formattedText, cutShort);
//                        if (cutShort) return;
//                        keepGoing = !shouldReturn;
//                        complete = false;
//                        break;
//                    }
//
////                    LOGGER.error("Added Length");
//                    multilineProcessor.addToOffset(charactermanager$styleoverridingtextcomponent.contents.length());
//                }
//                averagePoint += multilineProcessor.getWidth();
//                count++;
//            }
////            LOGGER.error("TOtal count: " + count);
//
//            FormattedText FormattedText1 = charactermanager$substyledtext.getRemainder();
//            if (FormattedText1 != null) {
//                splitifier.accept(FormattedText1, keepGoing);
//            } else if (flag1) {
//                splitifier.accept(FormattedText.EMPTY, false);
//            }
////            if (FormattedText1 != null) LOGGER.error("What's the remainder: " + FormattedText1.getString());
//
//        }
//
//        public class MultilineProcessorMixer implements FormattedCharSink {
//            private final float maxWidth;
//            private int lineBreak = -1;
//            private Style lineBreakStyle;
//            private boolean hadNonZeroWidthChar;
//            private float width;
//            private int lastSpace;
//            private Style lastSpaceStyle;
//            private int nextChar;
//            private int offset;
//
//            private boolean hasWord = false;
//            private boolean wordCaptured = false;
//            private float baseShot = 0;
//            private int baseSpace = -1;
//            private float baseWidth = 0;
//            private Style baseStyle;
//            private float overShot = 0;
//
//            public MultilineProcessorMixer(float maxWidth) {
//                this.lineBreakStyle = Style.EMPTY;
//                this.lastSpace = -1;
//                this.lastSpaceStyle = Style.EMPTY;
//                this.maxWidth = Math.max(maxWidth, 1.0F);
//                this.baseStyle = Style.EMPTY;
//            }
//
//            public boolean accept(int length, Style style, int codeNumber) {
////                    LOGGER.error("Length is: " + length);
////                    LOGGER.error("Offset is: " + this.offset);
////                    LOGGER.error("Code Number is: " + codeNumber);
//                int lvt_4_1_ = length + this.offset;
////                    LOGGER.error("Total is: " + (lvt_4_1_));
//                boolean isSpace = false;
//                switch (codeNumber) {
//                    case 10: //\n new line
////                            LOGGER.info("Finish iteration");
//                        return this.finishIteration(lvt_4_1_, style);
//                    case 32: //space
////                            LOGGER.info("This is a space");
//                        this.baseSpace = this.lastSpace;
//                        this.baseStyle = this.lastSpaceStyle;
//                        this.baseWidth = this.getWidth();
//                        this.lastSpace = lvt_4_1_;
//                        this.lastSpaceStyle = style;
//                        isSpace = true;
//                        if (width < maxWidth || !wordCaptured) {
//                            this.baseShot = 0;
//                            this.overShot = 0;
//                        }
//                        if (hasWord) wordCaptured = true;
//                    default: //char
//                        float lvt_5_1_ = CharacterManagerMixer.this.widthProvider.getWidth(codeNumber, style);
////                            LOGGER.info("1. width(?): " + lvt_5_1_);
////                            LOGGER.info("2. Previous width(?): " + this.width);
//                        if (!isSpace) {
//                            hasWord = true;
//                            if (this.width < this.maxWidth) this.baseShot += lvt_5_1_;
//                            else this.overShot += lvt_5_1_;
//                        }
//
//                        this.width += lvt_5_1_;
//
////                            LOGGER.info("3. After width(?): " + this.width);
////                            LOGGER.info("4. NonZeroWidthChar? " + this.hadNonZeroWidthChar);
////                            LOGGER.info("5. curr width > max width: " + (this.width > this.maxWidth));
//                        //If there is a character that isn't 0, width is higher than maxWidth
//                        //there is a word, and it's currently a space character
//                        if (this.hadNonZeroWidthChar && (width >= maxWidth) && isSpace && wordCaptured) {
//
////                                LOGGER.warn("overShot: " + this.overShot);
////                                LOGGER.warn("baseShot: " + this.baseShot);
////                                LOGGER.warn("baseSpace: " + this.baseSpace);
//                            this.width -= lvt_5_1_;
//                            //If I should move the current word to the next line or not
//                            //If text count that's above the maxWidth is greater than the text count below the maxWidth for the current word
//                            //cut it off and move to the next line
////                                if (this.baseShot >= this.overShot){
////                                    LOGGER.warn("Over Shot was less than base Shot, keep it.");
////                                }
////                                else{
////                                    this.lastSpace = this.baseSpace;
////                                    this.lastSpaceStyle = this.baseStyle;
////                                    this.width = this.baseWidth;
////                                    LOGGER.warn("Base shot was less than over shot, cut it off");
////                                }
//                            if (this.baseShot < this.overShot) {
//                                this.lastSpace = this.baseSpace;
//                                this.lastSpaceStyle = this.baseStyle;
//                                this.width = this.baseWidth;
//                            }
////                                return this.lastSpace != -1 ? this.finishIteration(this.lastSpace, this.lastSpaceStyle) : this.finishIteration(lvt_4_1_, style);
//                            return this.finishIteration(this.lastSpace, this.lastSpaceStyle);
//                        } else {
//                            this.hadNonZeroWidthChar |= lvt_5_1_ != 0.0F;
////                                LOGGER.info("6. What's nonZeroWidthChar now? " + this.hadNonZeroWidthChar);
////                                LOGGER.info("7. Previous nextChar? " + this.nextChar);
//                            this.nextChar = lvt_4_1_ + Character.charCount(codeNumber);
////                                LOGGER.info("8. After nextChar? " + this.nextChar);
//                            return true;
//                        }
//                }
//            }
//
//            private boolean finishIteration(int breakIndex, Style style) {
//                this.lineBreak = breakIndex;
//                this.lineBreakStyle = style;
//                return false;
//            }
//
//            private boolean lineBreakFound() {
//                return this.lineBreak != -1;
//            }
//
//            public int getSplitPosition() {
//                return this.lineBreakFound() ? this.lineBreak : this.nextChar;
//            }
//
//            public Style getSplitStyle() {
//                return this.lineBreakStyle;
//            }
//
//            public void addToOffset(int p_238387_1_) {
//                this.offset += p_238387_1_;
//            }
//
//            public float getWidth() {
//                return this.width;
//            }
//        }
//    }
//
//    public record TextViewer(int lineCount, InvoText originalText, InvoZone textZone, float scaleFactor, TextAlign align) {
//        public int getIndex(float x, float y) {
//            int index = 0;
//            int yIndex = 0;
//            float yOffset = y - textZone.y();
//            if (Math.signum(yOffset) != -1) {
//                float yLineSize = yOffset / (ClientUtil.getFont().lineHeight * scaleFactor);
////                yIndex = Math.min((int) Math.floor(yLineSize), textList.size() - 1);
//            }
//
//            for (int a = 0; a < yIndex; a++) {
////                index += textList.get(a).getString().length();
//            }
//
//            FormattedText selectedText = FormattedText.of("2");
////            FormattedText selectedText = textList.get(yIndex);
//            float fullRowWidth = ClientUtil.getFont().width(selectedText) * scaleFactor;
//            AtomicDouble startX = new AtomicDouble(textZone.x());
//            switch (align) {
//                case TOP_LEFT, MID_LEFT, BOT_LEFT:
//                    break;
//                case TOP_MIDDLE, MID, BOT_MIDDLE:
//                    startX.addAndGet((textZone.width() - fullRowWidth) / 2f);
//                    break;
//                case TOP_RIGHT, MID_RIGHT, BOT_RIGHT:
//                    startX.addAndGet(textZone.width() - fullRowWidth);
//                    break;
//            }
//
//            AtomicInteger subIndex = new AtomicInteger();
//            StringDecomposer.iterateFormatted(selectedText, Style.EMPTY, (posInSequence, style, codePoint) -> {
////            mutablefloat.add(this.widthProvider.getWidth(codePoint, style));
//                int subWidth = ClientUtil.getFont().width(InvoText.literal(Character.toString(codePoint))
//                        .setStyle(style).getText());
//
//                double beforeX = startX.get();
//                startX.addAndGet(subWidth * scaleFactor);
//
//                if (startX.get() > x) {
//                    if (MathUtil.percentageLerp(x, beforeX, startX.get()) >= 0.5f) subIndex.getAndIncrement();
//                    return true;
//                }
//
//                subIndex.getAndIncrement();
////                mutablefloat.add(subWidth);
//                return false;
//            });
//
////            selectedText.visit((style, currText) -> {
//////                if (ClientUtil.getWorld().getGameTime() % 30 == 0){
//////                LOGGER.error("currText: " + "'" + currText + "'");
//////                }
////
////                for (int a = 0; a < currText.length(); a++) {
////                    int subWidth = ClientUtil.getFont().width(InvoText.literal(currText.charAt(a) + "")
////                            .setStyle(style).getText(true));
//////                    if (subWidth * scaleFactor <= 0) LOGGER.error("NO SPACE");
////
////                }
////
//////                if (finished) return Optional.of(1);
////
////                return Optional.empty();
////            }, Style.EMPTY);
//
////            AtomicInteger count = new AtomicInteger();
////            textList.forEach(text -> {
////
////                text.visit((style, currText) -> {
////                    count.addAndGet(currText.length());
////                    return Optional.empty();
////                }, Style.EMPTY);
////            });
////            if (ClientUtil.getWorld().getGameTime() % 10 == 0) LOGGER.warn("Count: "+ count.get());
//
//            return index + subIndex.get();
//        }
//
//        public InvoZone getTextZone(int index) {
//            return this.getTextZones(index, index).get(0);
//        }
//
//        public List<InvoZone> getTextZones(int fromIndex, int toIndex) {
//            AtomicBoolean finished = new AtomicBoolean(false);
//            Font font = ClientUtil.getFont();
//
//            int fullIndex = 0;
////            for (FormattedText text : textList) {
////                if (text.getString().isEmpty()) fullIndex++;
////                fullIndex += text.getString().length();
//////                LOGGER.error(text.getString());
////            }
//
//            if (fromIndex > toIndex) {
//                int holder = fromIndex;
//                fromIndex = toIndex;
//                toIndex = holder;
//            }
//
//            fromIndex = (int) MathUtil.clamp(fromIndex, 0, fullIndex);
//            toIndex = (int) MathUtil.clamp(toIndex, fromIndex, fullIndex);
////            LOGGER.warn("What's the fullIndex: " + fullIndex);
////            LOGGER.warn("What's the fromIndex: " + fromIndex);
////            LOGGER.warn("What's the toIndex: " + toIndex);
////            LOGGER.warn("What's the test: " + MathUtil.clamp(500, 0, 400));
////            if (fromIndex == toIndex) return new ArrayList<>();
//
//            AtomicInteger currIndex = new AtomicInteger(0);
//
//            List<InvoZone> zoneList = new ArrayList<>();
//
//            float yTextOffset = textZone.y();
//            float singleTextHeight = font.lineHeight * scaleFactor;
//
////            for (FormattedText text : textList) {
//////                if (text.getString().isEmpty()) text = FormattedText.of("|");
////
////                AtomicDouble startX = new AtomicDouble(textZone.x());
////                float fullRowWidth = font.width(text) * scaleFactor;
////                switch (align) {
////                    case TOP_LEFT, MID_LEFT, BOT_LEFT:
////                        break;
////                    case TOP_MIDDLE, MID, BOT_MIDDLE:
////                        startX.addAndGet((textZone.width() - fullRowWidth) / 2f);
////                        break;
////                    case TOP_RIGHT, MID_RIGHT, BOT_RIGHT:
////                        startX.addAndGet(textZone.width() - fullRowWidth);
////                        break;
////                }
////                AtomicDouble endX = new AtomicDouble(startX.doubleValue());
////                AtomicBoolean foundStart = new AtomicBoolean(false);
////
////                if (fromIndex <= currIndex.get() + text.getString().length()) {
////
//////                    List<Pair<Style, String>> pairList = new ArrayList<>();
//////                    text.visit((style, currText) -> {
//////                        pairList.add(new Pair<>(style, currText));
//////                        return Optional.empty();
//////                    }, Style.EMPTY);
////
////                    AtomicBoolean finalFoundStart = foundStart;
////                    AtomicInteger finalCurrIndex = currIndex;
////                    int finalFromIndex = fromIndex;
////                    int finalToIndex = toIndex;
////                    AtomicDouble finalStartX = startX;
////                    AtomicDouble finalEndX = endX;
////                    StringDecomposer.iterateFormatted(text, Style.EMPTY, (posInSequence, style, codePoint) -> {
////                        int subWidth = ClientUtil.getFont().width(InvoText.literal(Character.toString(codePoint))
////                                .setStyle(style).getText());
////                        LOGGER.error("What's the code point: '"+codePoint+"'");
////
////                        if (!finalFoundStart.get()) {
////                            if (finalCurrIndex.get() >= finalFromIndex) finalFoundStart.set(true);
////                            else finalStartX.addAndGet(subWidth * scaleFactor);
////                        }
////
////                        finalCurrIndex.addAndGet(1);
////                        if (finalCurrIndex.get() > finalToIndex) {
//////                                LOGGER.error("What did I finish on: " + pair.getB());
//////                                LOGGER.error("This is working");
////                            finished.set(true);
////                            return true;
////                        }
////                        finalEndX.addAndGet(subWidth * scaleFactor);
////
////                        if (finished.get()) return true;
////
////                        return false;
////                    });
////
////
//////                    if (text.getString().codePointAt(0)){
//////                        LOGGER.error("It was empty....");
//////                    }
////                } else {
//////                    LOGGER.error("WHATS THE BAD INDEX: " + currIndex);
////                    currIndex.addAndGet(text.getString().length());
////                }
////
////                if (foundStart.get() || currIndex.get() == fullIndex) {
//////                    LOGGER.error("currIndex = fullIndex " + (currIndex == fullIndex));
////                    zoneList.add(InvoZone.fromPoints(new Vector2f((float) startX.get(), yTextOffset),
////                            new Vector2f((float) endX.get(), yTextOffset + singleTextHeight)));
////                }
////
////                if (finished.get()) break;
////
////                yTextOffset += singleTextHeight;
////            }
////            LOGGER.error("Final index: " + currIndex);
////            if (zoneList.isEmpty()) LOGGER.warn("this is empty!");
//
//            return zoneList;
//        }
//    }
}
