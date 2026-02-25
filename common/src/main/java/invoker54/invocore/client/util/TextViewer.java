package invoker54.invocore.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import invoker54.invocore.Invocore;
import invoker54.invocore.common.ModLogger;
import invoker54.invocore.common.util.MathUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringDecomposer;
import net.minecraft.world.InteractionHand;
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

    public RenderInfo getRenderInfo(){
       return this.textProperties.text(this.originalText).getRenderInfo(this.textZone, this.keepFormatCodes);
    }

    public void refreshInfo() {
        RenderInfo info = getRenderInfo();
        this.displayLines.clear();
        this.displayLines.addAll(info.textLines);
        this.scaleFactor = info.scaleFactor;
        this.indexSkipList.clear();

        int offset = -1;
        StringBuilder fullDisplayString = new StringBuilder();
        this.charWidthList.clear();
        for (var text : this.displayLines) {
            this.charWidthList.add(getCharWidthList(text));
            fullDisplayString.append(text.getString());
            fullDisplayString.append("\n");
            if (offset != -1){
                char c = this.originalText.charAt(offset + 1);
                if (c == '\n' || c == ' '){
                    offset++;
                    this.indexSkipList.add(0);
                }
                else this.indexSkipList.add(-1);
            }
            offset += text.getString().length();
        }
        if (offset < originalText.length()-1){
            this.displayLines.add(FormattedText.EMPTY);
            this.charWidthList.add(getCharWidthList(FormattedText.of("")));
            this.indexSkipList.add(0);
            fullDisplayString.append("\n");
            offset++;
            LOGGER.warn("Adding an extra line...");
        }
        LOGGER.warn("Offset: " + offset);
        LOGGER.warn("original length thing: " + (originalText.length()-1));
        LOGGER.warn("Formatted length: " + fullDisplayString.length());
        this.maxDisplayIndex = fullDisplayString.length()-1;
    }

    public void render(PoseStack stack){
        TextUtil.renderText(stack, this.displayLines, textProperties, this.getZoneCopy(), true);
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

    public int getMaxStringIndex(){
        return this.originalText.length();
    }

    public int getDisplayLineCount(){
        return this.displayLines.size();
    }

    public String getOriginalText(){
        return this.originalText;
    }

    public int getDisplayIndex(int stringIndex){
        if (stringIndex == 0) return 0;
        if (stringIndex == this.getMaxStringIndex()) return this.getMaxDisplayIndex();
        //How to convert string into display index???

        //ab__  string
        //ab\n \n display (skip: 1)

        //string index is 3
        //The converted index should be 2

        //Iterate through the display list, and between each text do the skiplist.
        //Iteration index = -1
        //display index = -1
        //stringIndex = 3

        //iteration = 2;
        //displayIndex = 2;
        //iteration = 3;
        //next loop
        //iteration = 4
        //I have to add 1 to display index

        int iterationIndex = -1;
        int displayIndex = -1;
        for (int a = 0; a < this.charWidthList.size(); a++) {
            List<Pair<Character, Float>> displayLine = this.charWidthList.get(a);
            iterationIndex += displayLine.size();
            if (stringIndex <= iterationIndex){
                displayIndex += displayLine.size() - (iterationIndex - stringIndex);
                return displayIndex;
            }

            displayIndex += displayLine.size();
            iterationIndex += this.indexSkipList.get(a);
            if (stringIndex <= iterationIndex){
                if (iterationIndex - ((this.indexSkipList.get(a)/2)) <= stringIndex) displayIndex++;
                return displayIndex;
            }
        }

        LOGGER.warn("[Invocore] This index shouldn't be reached! " + displayIndex);
        return displayIndex;
    }

    //There are 2 different indexes: String index, and display index.
    public int getStringIndex(int displayIndex){
        //123n    456n1n
        //123_ __n456n1
        LOGGER.debug("Display index: " + displayIndex);

        int lineCount = 0;
        int displayCount = -1;
        //123_n456_n7
        //123n 456n 7n

        for (int a = 0; a < this.displayLines.size(); a++){
            LOGGER.debug("(1) Display Count:"+displayCount);
            displayCount += this.charWidthList.get(a).size();
            LOGGER.debug("(2) Display Count:"+displayCount);
            if (displayIndex <= displayCount) break;
            lineCount++;
        }

        LOGGER.debug("Line Count:" + lineCount);

        for (int a = 0; a < lineCount; a++){
            displayIndex += this.indexSkipList.get(a);
        }

//        displayIndex = (int) MathUtil.clamp(displayIndex, 0, getMaxOriginalIndex());
        return displayIndex;
    }

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
            if (charWidth.getRight() == 0){
                index++;
                break;
            }
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

    public List<InvoZone> getTextZones(int displayStartIndex, int displayEndIndex) {
        Font font = ClientUtil.getFont();
        float heightScale = font.lineHeight * scaleFactor;
        displayStartIndex = (int) MathUtil.clamp(displayStartIndex, 0, getMaxDisplayIndex());
        displayEndIndex = (int) MathUtil.clamp(displayEndIndex, displayStartIndex, getMaxDisplayIndex());

        List<InvoZone> zoneList = new ArrayList<>();
        int currIndex = -1;
//        LOGGER.debug("Max display index: " + this.getMaxDisplayIndex());
        int count = 0;
        for (int a = 0; a < this.displayLines.size(); a++) {
            count += this.charWidthList.get(a).size();
        }
        LOGGER.debug("max possible index: " + count);

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

    public static class IndexCursor{
        public TextViewer parent;
        public int displayIndex;
        public int stringIndex;

        public IndexCursor(TextViewer parent, int displayIndex, int stringIndex){
            this.parent = parent;
            this.displayIndex = displayIndex;
            this.stringIndex = stringIndex;

        }

        public IndexCursor copy(IndexCursor otherCursor){
            this.displayIndex = otherCursor.displayIndex;
            this.stringIndex = otherCursor.stringIndex;
            return this;
        }

        public IndexCursor copy(){
            return new IndexCursor(this.parent, this.displayIndex, this.stringIndex);
        }

        public int getDisplayIndex(){
            return this.displayIndex;
        }

        public int getStringIndex(){
            return this.stringIndex;
        }

        public IndexCursor shiftByString(int shiftAmount){
            return this.setStringIndex(this.stringIndex + shiftAmount);
        }

        public IndexCursor setStringIndex(int index){
            this.stringIndex = (int) MathUtil.clamp(index, 0, this.parent.getMaxStringIndex());
            this.displayIndex = this.parent.getDisplayIndex(this.stringIndex);
            return this;
        }

        public IndexCursor setDisplayIndex(int index){
            this.displayIndex = (int) MathUtil.clamp(index, 0, this.parent.getMaxDisplayIndex());
            this.stringIndex = this.parent.getStringIndex(this.displayIndex);
            return this;
        }

        public IndexCursor shiftByDisplay(int shiftAmount){
            return this.setDisplayIndex(this.displayIndex + shiftAmount);
        }

        public static IndexCursor byStringIndex(int stringIndex, TextViewer parent){
            return new IndexCursor(parent, parent.getDisplayIndex(stringIndex), stringIndex);
        }

        public static IndexCursor  byDisplayIndex(int displayIndex, TextViewer parent){
            return new IndexCursor(parent, displayIndex, parent.getStringIndex(displayIndex));
        }
    }
}
