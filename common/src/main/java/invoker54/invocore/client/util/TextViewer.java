package invoker54.invocore.client.util;

import invoker54.invocore.Invocore;
import invoker54.invocore.common.ModLogger;
import invoker54.invocore.common.util.MathUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringDecomposer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextViewer {
    public static ModLogger LOGGER = ModLogger.getLogger(Invocore.debugMode);
    protected String originalText;
    protected boolean keepFormatCodes;
    protected InvoText.Properties textProperties;
    protected float scaleFactor;
    protected InvoZone textZone;
    protected boolean cutText;
    protected float yOffset;
    protected final List<FormattedText> displayLines;
    protected final List<List<Pair<Character, Float>>> charWidthList;
    protected int maxDisplayIndex;
    protected final List<Integer> indexSkipList;
    //I need to know the width of each char
    //I also need to know the index of each character


    public TextViewer(String originalText, InvoText.Properties textProperties, InvoZone textZone, boolean cutText, boolean keepFormatCodes) {
        this.originalText = originalText;
        this.keepFormatCodes = keepFormatCodes;
        this.textProperties = textProperties;
        this.textZone = textZone;
        this.cutText = cutText;
        this.displayLines = new ArrayList<>();
        this.charWidthList = new ArrayList<>();
        this.indexSkipList = new ArrayList<>();

        this.refreshInfo();
    }
    //123s
    //456s

    public List<Pair<Character, Float>> getCharWidthList(FormattedText text){
        List<Pair<Character, Float>> charWidthList = new ArrayList<>();
        StringDecomposer.iterateFormatted(text, Style.EMPTY, (pos, style, codePoint) -> {
            char textChar = (char) codePoint;
            float subWidth = scaleFactor * ClientUtil.getFont().width(InvoText.literal(String.valueOf(textChar)).setStyle(style).getText(true));
            charWidthList.add(Pair.of(textChar, subWidth));
            return true;
        });
        charWidthList.add(Pair.of('\n', 0f));
        return charWidthList;
    }

    public float getRowWidth(int row){
        return this.charWidthList.get(row).stream().map(Pair::getRight).reduce(Float::sum).orElse(0f);
    }

    public float getLineY(int line){
        float y = this.textZone.y();
        Font font = ClientUtil.getFont();
        float maxHeight = this.textZone.height();
        float scaledLineHeight = font.lineHeight * scaleFactor;

        switch (textProperties.getTxtAlignment()) {
            case TOP_LEFT,TOP_MIDDLE,TOP_RIGHT: {
                float textSpace = scaledLineHeight * line;
                y = y + (textSpace);
                break;
            }
            case MID_LEFT,MID,MID_RIGHT: {
                float spaceLeft = maxHeight - (scaledLineHeight * this.displayLines.size());
                y = y + (scaledLineHeight * line) + (spaceLeft / 2);
                break;
            }
            case BOT_LEFT,BOT_MIDDLE,BOT_RIGHT: {
                float spaceLeft = maxHeight - (scaledLineHeight * displayLines.size());
                y = y + (scaledLineHeight * line) + (spaceLeft);
                break;
            }
        }

        return y;
    }

    public float getStartX(int row){
        float fullRowWidth = this.getRowWidth(row);
        float startX = textZone.x();

        switch (this.textProperties.getTxtAlignment()) {
            case TOP_LEFT, MID_LEFT, BOT_LEFT:
                break;
            case TOP_MIDDLE, MID, BOT_MIDDLE:
                startX += (textZone.width() - fullRowWidth) / 2f;
                break;
            case TOP_RIGHT, MID_RIGHT, BOT_RIGHT:
                startX += textZone.width() - fullRowWidth;
                break;
        }
        return startX;
    }

    //There are 2 different indexes: String index, and display index.
    public int getStringIndex(int displayIndex){
        //123n    456n1n
        //123_ __n456n1

        int lineCount = 0;
        int displayCount = 0;
        //123_n456_n7
        //123n 456n 7n

        for (int a = 0; a < this.displayLines.size(); a++){
            displayCount += this.charWidthList.get(a).size();
            if (displayIndex < displayCount) break;
            lineCount++;
        }

        for (int a = 0; a < lineCount; a++){
            displayIndex += this.indexSkipList.get(a);
        }

        displayIndex = (int) MathUtil.clamp(displayIndex, 0, getMaxOriginalIndex() + 1);
        return displayIndex;
    }
    //Each display line will most likely be a different index (except usually the first display line.)
    //Each display line I will add 1 space, that will shift each index on the next display line by 1,
    //so I have to add -1 to the skipindex count

    public int getDisplayIndex(float pointX, float pointY){
        Font font = ClientUtil.getFont();
        int index = 0;
//        float startY = this.getLineY(0);
        float scaledHeight = font.lineHeight * scaleFactor;
        pointY = (float) MathUtil.clamp(pointY, this.getLineY(0), this.getLineY(this.displayLines.size() - 1) + (scaledHeight));

        int yIndex;
        for (yIndex = 0; pointY > getLineY(yIndex) + scaledHeight; yIndex++){
            int textCount = this.charWidthList.get(yIndex).size();
            index += textCount;
        }

        List<Pair<Character, Float>> selectedPair = this.charWidthList.get(yIndex);
        float startX = this.getStartX(yIndex);

        for (var charWidth : selectedPair){
            if (charWidth.getRight() == 0) break;
            if (pointX > startX + (charWidth.getRight()/2f)){
                startX += charWidth.getRight();
                index++;
                continue;
            }

            break;
        }

//        if (ClientUtil.getWorld().getGameTime() % 40 == 0) {
//            LOGGER.error("What's line:" + (yIndex + 1));
//            LOGGER.error("What's index" + (index));
//            LOGGER.error("What's max Index" + (this.maxDisplayIndex));
//        }

        return index;
    }

    public RenderInfo getRenderInfo(){
       return this.textProperties.text(this.originalText).getRenderInfo(this.textZone, this.keepFormatCodes);
    }

    public void refreshInfo() {
        RenderInfo info = getRenderInfo();
        this.displayLines.clear();
        this.displayLines.addAll(info.textLines);
        this.scaleFactor = info.scaleFactor;

        StringBuilder fullDisplayString = new StringBuilder();
        this.maxDisplayIndex = -1;
        this.charWidthList.clear();
        for (var text : this.displayLines) {
            this.charWidthList.add(getCharWidthList(text));
            this.maxDisplayIndex += text.getString().length() + 1;
            fullDisplayString.append(text.getString());
            fullDisplayString.append("\n");
        }

        Iterator<Integer> originalTextIterator = this.originalText.chars().iterator();
//        LOGGER.warn("Original size: " + this.originalText.length());
        Iterator<Integer> displayTextIterator = fullDisplayString.chars().iterator();
//        LOGGER.warn("Display size: " + fullDisplayString.length());
//        LOGGER.error("original text:" + this.originalText);
//        LOGGER.error("modified text:" + fullDisplayString);

        char originalChar;
        char displayChar;
        int skipAmount = 0;

        //IF they match, keep going
        //If the current display line runs dry, switch to the next display line and finalize the skipIndex
        //If they don't match, add skipAmount and only move the original text forward

        this.indexSkipList.clear();
//        LOGGER.warn("Index skip start: " + this.indexSkipList.size());
        while (originalTextIterator.hasNext()){
            //Go through each char

            //123 123 123 123 123 1 original
            //123s123s123s123s123s1s display
//            LOGGER.warn("Starting");
            originalChar = (char) originalTextIterator.next().intValue();
            displayChar = (char) displayTextIterator.next().intValue();
            //n - string
            //nn - display

            if (displayChar == '\n'){
//                LOGGER.warn("Found break");
                if (originalChar != displayChar) {
//                    LOGGER.warn("They are not the same");
                    skipAmount--;
//                    LOGGER.warn("(1) Display char: " + displayChar);
                    if (displayTextIterator.hasNext()) displayChar = (char) displayTextIterator.next().intValue();
//                    LOGGER.warn("(2) Display char: " + displayChar);
                    while (originalTextIterator.hasNext() && originalChar != displayChar) {
                        skipAmount++;
//                        LOGGER.warn("(1) Original char: " + originalChar);
                        originalChar = (char) originalTextIterator.next().intValue();
//                        LOGGER.warn("(2) Original char: " + originalChar);
                    }
                }
                this.indexSkipList.add(skipAmount);
//                LOGGER.warn("Skip List " + this.indexSkipList.size() + " | " + skipAmount);
                skipAmount = 0;
            }
        }

//        int count = 0;
//        while (displayTextIterator.hasNext()){
//            count++;
//            displayTextIterator.next();
//        }
//        LOGGER.error("How many display chars are left? " + count);
//        LOGGER.error("How long is skip list? " + this.indexSkipList.size());
//        LOGGER.error("How many lines?" + this.displayLines.size());
//        LOGGER.warn("Max index: " + this.maxDisplayIndex);
//        LOGGER.warn("Max String index: " + this.getMaxOriginalIndex());

    }

    public void updateText(String updatedText){
        originalText = updatedText;
        this.refreshInfo();
    }

    public InvoZone getTextZone(int index) {
        return this.getTextZones(index, index).get(0);
    }

    public int getMaxDisplayIndex(){
        return this.maxDisplayIndex;
    }

    public int getMaxOriginalIndex(){
        return this.originalText.length();
    }

    public int getDisplayLineCount(){
        return this.displayLines.size();
    }

    public String getOriginalText(){
        return this.originalText;
    }

    public List<InvoZone> getTextZones(int displayStartIndex, int displayEndIndex) {
        Font font = ClientUtil.getFont();
        float heightScale = font.lineHeight * scaleFactor;
        displayStartIndex = (int) MathUtil.clamp(displayStartIndex, 0, getMaxDisplayIndex());
        displayEndIndex = (int) MathUtil.clamp(displayEndIndex, displayStartIndex, getMaxDisplayIndex());

        List<InvoZone> zoneList = new ArrayList<>();
        int currIndex = -1;

        boolean startZone;
        for (int a = 0; a < this.displayLines.size(); a++) {
            List<Pair<Character, Float>> currPair = this.charWidthList.get(a);
            //123n
            //456n
            //1n
            //123_n456n1

//            LOGGER.error("currIndex: " + currIndex);
//            LOGGER.error("currIndex + currPair: " + (currIndex + currPair.size()));
//            LOGGER.error("displayStartIndex: " + (displayStartIndex));

            //This will skip the line if displayStartIndex isn't on this line.
            if (currIndex + currPair.size() < displayStartIndex) {
                currIndex += currPair.size();
                continue;
            }

            float startX = getStartX(a);
            float endX = startX;

            for (var charWidth : currPair) {
                currIndex++;
                startZone = (currIndex >= displayStartIndex);

                if (!startZone) startX += charWidth.getRight();

                if (currIndex == displayEndIndex) break;

                endX += charWidth.getRight();
            }

            zoneList.add(new InvoZone(startX, endX - startX, this.getLineY(a), heightScale));
            if (currIndex == displayEndIndex) break;

        }

        return zoneList;
    }

    public InvoZone getZoneCopy(){
        return this.textZone.copy();
    }

    public TextViewer setZone(InvoZone updatedZone){
        this.textZone.copy(updatedZone);
        this.updateText(this.originalText);
        return this;
    }

    public static record RenderInfo(float scaleFactor, List<FormattedText> textLines){}
}
