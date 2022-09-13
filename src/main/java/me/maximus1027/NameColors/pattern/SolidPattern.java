package me.maximus1027.NameColors.pattern;

import com.github.mittenmc.serverutils.Colors;

public class SolidPattern extends Pattern {

    public SolidPattern(String pattern) {
        patternArr = pattern.split(" ");
    }

    @Override
    public String withPattern(String input) {
        StringBuilder coloredInput = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            for (String p : patternArr[i % patternArr.length].split("\\|")) {
                if (!p.toLowerCase().contains("solid")) {
                    coloredInput.append("&");
                }
                coloredInput.append(p);
            }
            coloredInput.append(input.charAt((i)));
        }
        return Colors.conv(coloredInput + "&r");
    }
}
