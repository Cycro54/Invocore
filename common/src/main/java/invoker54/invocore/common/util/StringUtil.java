package invoker54.invocore.common.util;

import invoker54.invocore.Invocore;
import invoker54.invocore.client.util.InvoText;
import invoker54.invocore.common.ModLogger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static ModLogger LOGGER = ModLogger.getLogger(StringUtil.class, Invocore.debugMode);

    public static final Map<Character, ChatFormatting> chatFormattingMap = new HashMap<>(Map.ofEntries(
            Map.entry('0', ChatFormatting.BLACK),
            Map.entry('1', ChatFormatting.DARK_BLUE),
            Map.entry('2', ChatFormatting.DARK_GREEN),
            Map.entry('3', ChatFormatting.DARK_AQUA),
            Map.entry('4', ChatFormatting.DARK_RED),
            Map.entry('5', ChatFormatting.DARK_PURPLE),
            Map.entry('6', ChatFormatting.GOLD),
            Map.entry('7', ChatFormatting.GRAY),
            Map.entry('8', ChatFormatting.DARK_GRAY),
            Map.entry('9', ChatFormatting.BLUE),
            Map.entry('a', ChatFormatting.GREEN),
            Map.entry('b', ChatFormatting.AQUA),
            Map.entry('c', ChatFormatting.RED),
            Map.entry('d', ChatFormatting.LIGHT_PURPLE),
            Map.entry('e', ChatFormatting.YELLOW),
            Map.entry('f', ChatFormatting.WHITE),
            Map.entry('k', ChatFormatting.OBFUSCATED),
            Map.entry('l', ChatFormatting.BOLD),
            Map.entry('m', ChatFormatting.STRIKETHROUGH),
            Map.entry('n', ChatFormatting.UNDERLINE),
            Map.entry('o', ChatFormatting.ITALIC),
            Map.entry('r', ChatFormatting.RESET)
    ));

    private static final char customColorChar = '#';

    private static final Pattern FORMATTING_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR#]");

    public static InvoText formatText(InvoText preFormattedText, boolean keepFormatCodes) {
        String originalText = preFormattedText.getString();
        Matcher matcher = FORMATTING_PATTERN.matcher(originalText);

        InvoText replacementText = InvoText.literal("");
        replacementText.copyProperties(preFormattedText);
        Style style = Style.EMPTY;
        int offset = 0;

        while (matcher.find()) {
            //If the codeString has a forward slash, skip it.
            if (matcher.start() != 0 && (originalText.charAt(matcher.start() - 1) == '/')) continue;

            String prevString = originalText.substring(offset, Math.max(0, matcher.start()));
            if (!prevString.isEmpty()){
                replacementText.append(InvoText.literal(prevString).getText().withStyle(style));
                offset += prevString.length();
            }

            //"&r&1This is just a test./n&r&#(2421412)It should be working correctly.\n"
//            LOGGER.warn("Code found: " + matcher.group());
            char codeChar = originalText.charAt(matcher.end()-1);
//            LOGGER.warn("What's the char: " + codeChar);

            if (chatFormattingMap.containsKey(codeChar)){
                style = style.applyFormat(chatFormattingMap.get(codeChar));
            }
            else if (codeChar == customColorChar){
                try {
                    String subString = originalText.substring(matcher.end(), originalText.indexOf(")", matcher.end()));
                    if (!keepFormatCodes) offset += subString.length() + 1;
                    subString = subString.replaceAll("[()]", "");

//                    LOGGER.warn("hex code: " + subString);
                    int hexInt = Long.decode(subString).intValue();
                    Color customColor = new Color(hexInt,true);
//                    LOGGER.warn("What's my alpha: " + new Color(TextColor.fromRgb(customColor.getRGB()).getValue()).getAlpha());

                    style = style.withColor(customColor.getRGB());
                }
                catch (IllegalArgumentException e){
                    LOGGER.error("This isn't right... ");
                    e.printStackTrace();
                }
            }

            if (!keepFormatCodes) offset += matcher.group().length();
        }

        String prevString = originalText.substring(offset);
        if (!prevString.isEmpty()){
            replacementText.append(InvoText.literal(prevString).getUnformattedText().withStyle(style));
        }

        return replacementText;
    }
}
