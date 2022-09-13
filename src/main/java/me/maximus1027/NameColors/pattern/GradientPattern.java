package me.maximus1027.NameColors.pattern;

import com.github.mittenmc.serverutils.Colors;

public class GradientPattern extends Pattern {

    public GradientPattern(String pattern) {
        String[] arr = pattern.split(" ");
        patternArr = new String[2];
        patternArr[0] = arr[0];
        patternArr[1] = arr[1];
    }

    @Override
    public String withPattern(String input) {
        return Colors.conv(patternArr[0] + input + patternArr[1]);
    }
}
