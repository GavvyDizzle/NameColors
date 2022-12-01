package me.github.gavvydizzle.NameColors.pattern;

public abstract class Pattern {

    protected String[] patternArr;

    /**
     * Converts the given string to have the colors of this NameColor's pattern
     * @param input The string to change the colors of
     * @return The string with new colors
     */
    public abstract String withPattern(String input);

}
