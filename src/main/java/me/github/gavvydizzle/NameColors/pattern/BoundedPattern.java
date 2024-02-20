package me.github.gavvydizzle.NameColors.pattern;

import com.github.mittenmc.serverutils.Colors;

/**
 * Represents a pattern that requires a beginning and ending tag to function properly.
 * Currently, this is used for the GRADIENT and RAINBOW patterns
 */
public class BoundedPattern extends Pattern {

    public BoundedPattern(String pattern) {
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
