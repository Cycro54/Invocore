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
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class TextUtil {
    public static ModLogger LOGGER = ModLogger.getLogger(TextUtil.class, Invocore.debugMode);
    private static int black = new java.awt.Color(0, 0, 0, 255).getRGB();
    //    private static final ModLogger LOGGER = ModLogger.getLogger(TextUtil.class, );
    private static CharacterManagerMixer characterMixer;

    public enum TextAlign {
        TOP_LEFT,
        TOP_MIDDLE,
        TOP_RIGHT,
        MID_LEFT,
        MID,
        MID_RIGHT,
        BOT_LEFT,
        BOT_MIDDLE,
        BOT_RIGHT;
    }

//    public static TextViewer.RenderInfo renderText(PoseStack stack, InvoZone textZone, Component text, boolean shouldRender) {
//        return renderText(stack, textZone, text, shouldRender);
//    }
//
//    public static TextViewer.RenderInfo renderText(PoseStack stack, Component text, boolean shadow, int maxSplits,
//                                                 InvoZone renderZone, TextAlign alignment, boolean shouldRender) {
//        return renderText(stack, text, shadow, maxSplits, renderZone.x(), renderZone.width(), renderZone.y(),
//                renderZone.height(), 0, alignment, 0.1f, Float.MAX_VALUE, shouldRender);
//    }

    public static TextViewer.RenderInfo renderText(PoseStack stack, InvoZone textZone, Component text, InvoText.Properties properties, boolean shouldRender) {
//        if (text.getString().isEmpty()) text = InvoText.literal(" ").setStyle(text.getStyle()).getText();
        int textWidth = ClientUtil.getFont().width(text);

        //I took this from the if statement
        // && maxHeight > mC.font.lineHeight
        //Let's try this again.
        //I have to make it so the text fits PERFECTLY inside the space provided.
        //What that means is, I have to cut the text at the correct spots.

//            //First grab the X Y ratio for the space
//            double spaceRatio = maxWidth / maxHeight;
//            //Since Y has to be multiples of 9, make the ratio a multiple of 9
//            spaceRatio *= 9;

//            //Grab the textArea we will be working with
//            double textArea = 9 * textWidth;
//            //Do the formula u got from mathSolver to get the multiplier that I can use on the spaceRatio
//            double multiplier = textArea / (spaceRatio * 9);
//            multiplier = Math.sqrt(multiplier);
//            //and FINALLY, multiply spaceRatio with the multiplier, and that should be the cutoff point!
//            double cutoffPoint = Math.ceil(multiplier * spaceRatio);
//            double neededSplits = ((double) textWidth /cutoffPoint);
        textZone = textZone.copy().inflate(-properties.getPadding());
//        float maxWidth = textZone.width();
//        float maxHeight = textZone.height();
        float minTextSize = properties.getMinTextSize();
        float maxTextSize = properties.getMaxTextSize();
        float maxSplits = properties.getMaxSplits();
        boolean hasShadow = properties.isShadow();

        //I have the amount of space it's going to take up
        //I have the space it's going to occupy
        //I have to make it so the space it takes up equals the space it will occupy

        //First grab the X ratio for the text space (for each 1 height, there is xRatio)
        double textArea = textWidth * ClientUtil.getFont().lineHeight;
        double spaceArea = textZone.width() * textZone.height();
        double scalingFactor = Math.sqrt(spaceArea / textArea);
        double textSize = Math.sqrt((ClientUtil.getFont().lineHeight * spaceArea) / textWidth);
        double updatedTextSize = MathUtil.clamp(textSize, minTextSize, maxTextSize);
        double percentChange = updatedTextSize / textSize;
        double updatedTextWidth = Math.sqrt((textWidth * spaceArea) / ClientUtil.getFont().lineHeight) * percentChange;
        double cutoffPercent = textZone.width() / updatedTextWidth;
        double cutOffPoint = textWidth * cutoffPercent;

        double neededSplits = MathUtil.clamp((int) Math.ceil(textZone.height() / updatedTextSize), 1, Integer.MAX_VALUE);
//        double neededSplits = Math.floor(textWidth / cutOffPoint);

        boolean maxSplitExceeded = neededSplits > maxSplits;

        if (maxSplits != 0 && (maxSplitExceeded || maxSplits < 0 && Math.abs(maxSplits) > neededSplits)) {
            neededSplits = Math.abs(maxSplits);
            cutOffPoint = textWidth / neededSplits;
        }

        List<FormattedText> list = ClientUtil.getFont().getSplitter().splitLines(text, (int) Math.ceil(cutOffPoint), Style.EMPTY);
//        characterMixer.splitLines(text, (int) cutOffPoint, Style.EMPTY, (int) Math.abs(neededSplits), (A, cutShort) -> {
//            list.add(A);
//        });

        return renderText(stack, list, properties, textZone, shouldRender);
    }

    public static TextViewer.RenderInfo renderText(PoseStack stack, List<FormattedText> textLines, InvoText.Properties properties,
                                                   InvoZone textZone, boolean shouldRender) {
        Font font = ClientUtil.getFont();
        if (textLines.isEmpty()) textLines.add(FormattedText.of(""));

        if (stack != null) stack.pushPose();

        float maxTxtHeight = textLines.size() * ClientUtil.getFont().lineHeight;

        float maxTxtWidth = 0;
        for (FormattedText textComponent : textLines) {
            int currentWidth = font.width(textComponent);
            if (currentWidth > maxTxtWidth) {
                maxTxtWidth = currentWidth;
            }
        }

        maxTxtWidth -= 1;

        float shadowOffset = 0;
        if (properties.isShadow()) {
            shadowOffset = 1;
            maxTxtHeight += shadowOffset;
            maxTxtWidth += shadowOffset;
        }

        float heightFillAmount = textZone.height() / maxTxtHeight;
        float widthFillAmount = textZone.width() / maxTxtWidth;
        float scaleFactor = 0;
        boolean isMiddle = properties.getTxtAlignment() == TextAlign.TOP_MIDDLE || properties.getTxtAlignment() == TextAlign.MID || properties.getTxtAlignment() == TextAlign.BOT_MIDDLE;

        if (heightFillAmount < widthFillAmount || heightFillAmount == widthFillAmount) {
//            scaleFactor = ((textZone.height() - (isMiddle ? (padding * 2) : padding)) / maxTxtHeight);
            scaleFactor = (textZone.height() / maxTxtHeight);
        } else if (heightFillAmount > widthFillAmount) {
//            scaleFactor = ((textZone.width() - (isMiddle ? (padding * 2) : padding)) / maxTxtWidth);
            scaleFactor = (textZone.width() / maxTxtWidth);
        }
        scaleFactor = (float) MathUtil.clamp(scaleFactor, (properties.getMinTextSize() / font.lineHeight), (properties.getMaxTextSize() / font.lineHeight));
        if (shouldRender) {
            stack.scale(scaleFactor, scaleFactor, scaleFactor);

            for (int a = 0; a < textLines.size(); ++a) {
                FormattedText currText = textLines.get(a);

                float y = textZone.y() / scaleFactor;
//            y = y + ((((maxHeight - (maxTxtHeight * scaleFactor)) / 2F) + (a * font.lineHeight * scaleFactor)) / scaleFactor);
                switch (properties.getTxtAlignment()) {
                    case TOP_LEFT,TOP_MIDDLE,TOP_RIGHT: {
                        float textSpace = (font.lineHeight) * a;
                        y = y + (textSpace);
                        break;
                    }
                    case MID_LEFT,MID,MID_RIGHT: {
                        float spaceLeft = (textZone.height() / scaleFactor) - (font.lineHeight * textLines.size());
                        y = y + ((font.lineHeight) * a) + (spaceLeft / 2);
                        break;
                    }
                    case BOT_LEFT,BOT_MIDDLE,BOT_RIGHT: {
                        float spaceLeft = (textZone.height() / scaleFactor) - (font.lineHeight * textLines.size());
                        y = y + ((font.lineHeight) * a) + (spaceLeft);
                        break;
                    }
                }

                float x = textZone.x();
                switch (properties.getTxtAlignment()) {
                    case TOP_LEFT,MID_LEFT,BOT_LEFT:
                        x = (x) / scaleFactor;
                        break;
                    case TOP_MIDDLE,MID,BOT_MIDDLE:
                        x = ((x + ((textZone.width() - ((font.width(currText) - (1 - shadowOffset)) * scaleFactor)) / 2F)) / scaleFactor);
                        break;
                    case TOP_RIGHT,MID_RIGHT,BOT_RIGHT:
                        x = (((x + textZone.width()) / scaleFactor) - (((font.width(currText) - (1 - shadowOffset)) * scaleFactor) / scaleFactor));
                        break;
                }

                renderText(currText, stack, x, y, properties.isShadow());
            }
        }

//        InvoZone textZone = new InvoZone(x0, textZone.width(), y0, textZone.height());
//        switch (properties.getTxtAlignment()) {
//            case TOP_LEFT,TOP_MIDDLE,TOP_RIGHT: {
//                textZone.stretch(true);
//                textZone.setDown(textZone.y() + ((font.lineHeight * scaleFactor) * textLines.size()));
////                float textSpace = (font.lineHeight) * a;
////                y = y + (textSpace);
//                break;
//            }
//            case MID_LEFT,MID,MID_RIGHT: {
//                float spaceLeft = (textZone.height() / scaleFactor) - (font.lineHeight * textLines.size());
//                textZone.inflate(0, -spaceLeft / 2);
//                break;
//            }
//            case BOT_LEFT,BOT_MIDDLE,BOT_RIGHT: {
//                textZone.stretch(true);
//                float spaceLeft = (textZone.height() / scaleFactor) - (font.lineHeight * textLines.size());
//                textZone.setY(textZone.y() + (spaceLeft));
//                break;
//            }
//        }

        if (stack != null) stack.popPose();
        return new TextViewer.RenderInfo(scaleFactor, textLines);
    }

    public static void renderText(FormattedText text, PoseStack stack, float x, float y, boolean shadow) {
        MultiBufferSource.BufferSource irendertypebuffer$impl = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

//        boolean flag = !player.isDiscrete();
//        float f = player.getBbHeight() * 0.5f;
//        int i = "deadmau5".equals(text.getString()) ? -10 : 0;
        Matrix4f matrix4f = stack.last().pose();

        //This is the usual number, so let's keep it like that for now
        int lightCoords = 15728880;

        //float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
//        int j = (int) (0 * 255.0F) << 24;
        Font fontrenderer = ClientUtil.getFont();

        List<Pair<Style, String>> textList = new ArrayList<>();
        int offset = 0;

        //I am trying to make it so you can change the opacity of text if it calls for it...
        text.visit((style, myText) -> {
//            LOGGER.warn("What's b: " + style);
            textList.add(new Pair<>(style, myText));
            return Optional.empty();
        }, Style.EMPTY);

        RenderSystem.disableDepthTest();
        for (var formatPair : textList) {
            TextColor color = formatPair.getA().getColor();
            if (color == null) color = TextColor.fromRgb(0xFFFFFFFF);
            FormattedText formattedText = FormattedText.of(formatPair.getB(), formatPair.getA());
            Color textColor = new Color(color.getValue(), true);

            fontrenderer.drawInBatch(Language.getInstance().getVisualOrder(formattedText),
                    x + offset, y, textColor.getAlpha() << 24 | textColor.getRGB(), shadow, matrix4f, irendertypebuffer$impl,
                    Font.DisplayMode.SEE_THROUGH, 0, lightCoords);

            offset += ClientUtil.getFont().width(formattedText);
        }
        irendertypebuffer$impl.endBatch();
        RenderSystem.enableDepthTest();
    }

    public static class CharacterManagerMixer extends StringSplitter {

        public CharacterManagerMixer() {
            super(ClientUtil.getFont().getSplitter().widthProvider);
        }

        public void splitLines(FormattedText text, int cutoffPoint, Style p_style, int neededSplits, BiConsumer<FormattedText, Boolean> splitifier) {
            List<LineComponent> list = Lists.newArrayList();
            text.visit((p_238355_1_, p_238355_2_) -> {
                if (!p_238355_2_.isEmpty()) {
                    LineComponent textComponent = new LineComponent(p_238355_2_, p_238355_1_);
//                    LOGGER.error("I am visiting this: " + textComponent.contents);
                    list.add(textComponent);
                }

                return Optional.empty();
            }, p_style);
            FlatComponents charactermanager$substyledtext = new FlatComponents(list);
            boolean complete = false;
            boolean flag1 = false;
            boolean keepGoing = false;
            float averagePoint = cutoffPoint;
            int count = 1;

            while (!complete) {
                complete = true;
                MultilineProcessorMixer multilineProcessor = new MultilineProcessorMixer(cutoffPoint);


                for (LineComponent charactermanager$styleoverridingtextcomponent : charactermanager$substyledtext.parts) {
//                    LOGGER.debug("Remaining Content: " + charactermanager$styleoverridingtextcomponent.contents);
                    boolean flag3 = StringDecomposer.iterateFormatted(
                            charactermanager$styleoverridingtextcomponent.contents, 0,
                            charactermanager$styleoverridingtextcomponent.style, p_style, multilineProcessor);
                    if (!flag3) {
                        int i = multilineProcessor.getSplitPosition();
                        Style style = multilineProcessor.getSplitStyle();
                        char c0 = charactermanager$substyledtext.charAt(i);
                        boolean shouldReturn = c0 == '\n';
                        boolean shouldBreak = shouldReturn || c0 == ' ';
                        flag1 = shouldReturn;
                        FormattedText formattedText = charactermanager$substyledtext.splitAt(i, shouldBreak ? 1 : 0, style);
//                        LOGGER.error("Next Split: " + formattedText.getString());
                        neededSplits--;
                        boolean cutShort = (neededSplits == 0);
                        if (cutShort) {
                            formattedText = FormattedText.composite(formattedText, FormattedText.of("-", formattedText.visit((fStyle, fString) -> {
                                return Optional.of(fStyle.withObfuscated(false));
                            }, Style.EMPTY).get()));
                        }
                        splitifier.accept(formattedText, cutShort);
                        if (cutShort) return;
                        keepGoing = !shouldReturn;
                        complete = false;
                        break;
                    }

//                    LOGGER.error("Added Length");
                    multilineProcessor.addToOffset(charactermanager$styleoverridingtextcomponent.contents.length());
                }
                averagePoint += multilineProcessor.getWidth();
                count++;
            }
//            LOGGER.error("TOtal count: " + count);

            FormattedText FormattedText1 = charactermanager$substyledtext.getRemainder();
            if (FormattedText1 != null) {
                splitifier.accept(FormattedText1, keepGoing);
            } else if (flag1) {
                splitifier.accept(FormattedText.EMPTY, false);
            }
//            if (FormattedText1 != null) LOGGER.error("What's the remainder: " + FormattedText1.getString());

        }

        public class MultilineProcessorMixer implements FormattedCharSink {
            private final float maxWidth;
            private int lineBreak = -1;
            private Style lineBreakStyle;
            private boolean hadNonZeroWidthChar;
            private float width;
            private int lastSpace;
            private Style lastSpaceStyle;
            private int nextChar;
            private int offset;

            private boolean hasWord = false;
            private boolean wordCaptured = false;
            private float baseShot = 0;
            private int baseSpace = -1;
            private float baseWidth = 0;
            private Style baseStyle;
            private float overShot = 0;

            public MultilineProcessorMixer(float maxWidth) {
                this.lineBreakStyle = Style.EMPTY;
                this.lastSpace = -1;
                this.lastSpaceStyle = Style.EMPTY;
                this.maxWidth = Math.max(maxWidth, 1.0F);
                this.baseStyle = Style.EMPTY;
            }

            public boolean accept(int length, Style style, int codeNumber) {
//                    LOGGER.error("Length is: " + length);
//                    LOGGER.error("Offset is: " + this.offset);
//                    LOGGER.error("Code Number is: " + codeNumber);
                int lvt_4_1_ = length + this.offset;
//                    LOGGER.error("Total is: " + (lvt_4_1_));
                boolean isSpace = false;
                switch (codeNumber) {
                    case 10: //\n new line
//                            LOGGER.info("Finish iteration");
                        return this.finishIteration(lvt_4_1_, style);
                    case 32: //space
//                            LOGGER.info("This is a space");
                        this.baseSpace = this.lastSpace;
                        this.baseStyle = this.lastSpaceStyle;
                        this.baseWidth = this.getWidth();
                        this.lastSpace = lvt_4_1_;
                        this.lastSpaceStyle = style;
                        isSpace = true;
                        if (width < maxWidth || !wordCaptured) {
                            this.baseShot = 0;
                            this.overShot = 0;
                        }
                        if (hasWord) wordCaptured = true;
                    default: //char
                        float lvt_5_1_ = CharacterManagerMixer.this.widthProvider.getWidth(codeNumber, style);
//                            LOGGER.info("1. width(?): " + lvt_5_1_);
//                            LOGGER.info("2. Previous width(?): " + this.width);
                        if (!isSpace) {
                            hasWord = true;
                            if (this.width < this.maxWidth) this.baseShot += lvt_5_1_;
                            else this.overShot += lvt_5_1_;
                        }

                        this.width += lvt_5_1_;

//                            LOGGER.info("3. After width(?): " + this.width);
//                            LOGGER.info("4. NonZeroWidthChar? " + this.hadNonZeroWidthChar);
//                            LOGGER.info("5. curr width > max width: " + (this.width > this.maxWidth));
                        //If there is a character that isn't 0, width is higher than maxWidth
                        //there is a word, and it's currently a space character
                        if (this.hadNonZeroWidthChar && (width >= maxWidth) && isSpace && wordCaptured) {

//                                LOGGER.warn("overShot: " + this.overShot);
//                                LOGGER.warn("baseShot: " + this.baseShot);
//                                LOGGER.warn("baseSpace: " + this.baseSpace);
                            this.width -= lvt_5_1_;
                            //If I should move the current word to the next line or not
                            //If text count that's above the maxWidth is greater than the text count below the maxWidth for the current word
                            //cut it off and move to the next line
//                                if (this.baseShot >= this.overShot){
//                                    LOGGER.warn("Over Shot was less than base Shot, keep it.");
//                                }
//                                else{
//                                    this.lastSpace = this.baseSpace;
//                                    this.lastSpaceStyle = this.baseStyle;
//                                    this.width = this.baseWidth;
//                                    LOGGER.warn("Base shot was less than over shot, cut it off");
//                                }
                            if (this.baseShot < this.overShot) {
                                this.lastSpace = this.baseSpace;
                                this.lastSpaceStyle = this.baseStyle;
                                this.width = this.baseWidth;
                            }
//                                return this.lastSpace != -1 ? this.finishIteration(this.lastSpace, this.lastSpaceStyle) : this.finishIteration(lvt_4_1_, style);
                            return this.finishIteration(this.lastSpace, this.lastSpaceStyle);
                        } else {
                            this.hadNonZeroWidthChar |= lvt_5_1_ != 0.0F;
//                                LOGGER.info("6. What's nonZeroWidthChar now? " + this.hadNonZeroWidthChar);
//                                LOGGER.info("7. Previous nextChar? " + this.nextChar);
                            this.nextChar = lvt_4_1_ + Character.charCount(codeNumber);
//                                LOGGER.info("8. After nextChar? " + this.nextChar);
                            return true;
                        }
                }
            }

            private boolean finishIteration(int breakIndex, Style style) {
                this.lineBreak = breakIndex;
                this.lineBreakStyle = style;
                return false;
            }

            private boolean lineBreakFound() {
                return this.lineBreak != -1;
            }

            public int getSplitPosition() {
                return this.lineBreakFound() ? this.lineBreak : this.nextChar;
            }

            public Style getSplitStyle() {
                return this.lineBreakStyle;
            }

            public void addToOffset(int p_238387_1_) {
                this.offset += p_238387_1_;
            }

            public float getWidth() {
                return this.width;
            }
        }
    }

//    public record TextViewer(List<Pair<FormattedText, List<Float>>> textLines, int maxIndex, InvoZone textZone, float scaleFactor, TextAlign align) {
//        public static TextViewer getInstance(List<FormattedText> textLines, InvoZone textZone, float scaleFactor, TextAlign align){
//            int maxIndex = -1;
//            List<Pair<FormattedText, List<Float>>> pairList = new ArrayList<>();
//
//            for (int a = 0; a < textLines.size(); a++){
//                FormattedText currText = textLines.get(a);
//                maxIndex += currText.getString().length();
//
//                List<Float> textWidthList = new ArrayList<>();
//                StringDecomposer.iterateFormatted(currText, Style.EMPTY, (pos, style, codePoint) ->{
//                    int subWidth = ClientUtil.getFont().width(invoker54.invocore.client.util.InvoText.literal(Character.toString(codePoint)).setStyle(style).getText(true));
//                    textWidthList.add(subWidth * scaleFactor);
//                    return true;
//                });
//                textWidthList.add(0f);
//                maxIndex++;
//
//                //When to add a space
//                //123s
//                //s
//                //123s - max:9
//
//                //123s - max:4
//
//                pairList.add(new Pair<>(currText, textWidthList));
//            }
//
//            return new TextViewer(pairList, maxIndex, textZone, scaleFactor, align);
//        }
//
//        public float getRowWidth(Pair<FormattedText, List<Float>> pair){
//            return pair.getB().stream().reduce(Float::sum).orElse(0f);
//        }
//
//        public float getStartX(Pair<FormattedText, List<Float>> pair){
//            float fullRowWidth = this.getRowWidth(pair);
//            float startX = textZone.x();
//
//            switch (align) {
//                case TOP_LEFT, MID_LEFT, BOT_LEFT:
//                    break;
//                case TOP_MIDDLE, MID, BOT_MIDDLE:
//                    startX += (textZone.width() - fullRowWidth) / 2f;
//                    break;
//                case TOP_RIGHT, MID_RIGHT, BOT_RIGHT:
//                    startX += textZone.width() - fullRowWidth;
//                    break;
//            }
//            return startX;
//        }
//
//        public int getIndex(float x, float y){
//            Font font = ClientUtil.getFont();
//            int index = -1;
//            y = (float) MathUtil.clamp(y, textZone.y(), textZone.down() - 1);
//            float heightScale = font.lineHeight * scaleFactor;
//
//            int yIndex;
//            for (yIndex = 0; y > textZone.y() + ((yIndex + 1) * heightScale); yIndex++){
//                int textCount = textLines.get(yIndex).getB().size();
//                index += textCount;
//            }
//
//            Pair<FormattedText, List<Float>> selectedPair = textLines.get(yIndex);
//            float startX = this.getStartX(selectedPair);
//
//            for (var charWidth : selectedPair.getB()){
//                if (x > startX + (charWidth/2f)){
//                    startX += charWidth;
//                    index++;
//                    continue;
//                }
//
//                break;
//            }
//
//            return index;
//        }
//
//        public InvoZone getTextZone(int index) {
//            return this.getTextZones(index, index).get(0);
//        }
//
//        public List<InvoZone> getTextZones(int startIndex, int endIndex) {
//            Font font = ClientUtil.getFont();
//            float heightScale = font.lineHeight * scaleFactor;
//            startIndex = (int) MathUtil.clamp(startIndex, 0, maxIndex);
//            endIndex = (int) MathUtil.clamp(endIndex, startIndex, maxIndex);
//
//            List<InvoZone> zoneList = new ArrayList<>();
//            int currIndex = -1;
//
//            boolean startZone;
//            for (int a = 0; a < this.textLines.size(); a++) {
//                Pair<FormattedText, List<Float>> currPair = this.textLines.get(a);
//
//                //This will skip the line if startIndex isn't on this line.
//                if (currIndex + currPair.getB().size() < startIndex) {
//                    currIndex += currPair.getB().size();
//                    continue;
//                }
//
//                float startX = getStartX(currPair);
//                float endX = startX;
//
//                for (var charWidth : currPair.getB()) {
//                    currIndex++;
//                    startZone = (currIndex >= startIndex);
//
//                    if (!startZone) startX += charWidth;
//
//                    if (currIndex == endIndex) break;
//
//                    endX += charWidth;
//                }
//
//                zoneList.add(new InvoZone(startX, endX - startX, textZone.y() + (heightScale * a), heightScale));
//                if (currIndex == endIndex) break;
//
//            }
//
//            return zoneList;
//        }
//
//    }
}
