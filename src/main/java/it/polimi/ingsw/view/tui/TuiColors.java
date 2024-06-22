package it.polimi.ingsw.view.tui;

/**
 * Enumeration of colors for the TUI.
 */
public enum TuiColors {
    ANSI_RESET("\033[0m"),
    ANSI_RED("\033[31;1m"),
    ANSI_GREEN("\033[32;1m"),
    ANSI_GOLD("\033[33;1m"),
    ANSI_BLUE("\033[34;1m"),
    ANSI_PURPLE("\033[35;1m"),
    ANSI_BLACK("\033[30;1m"),
    ANSI_WHITE("\033[37;1m"),
    ANSI_YELLOW("\033[48;2;255;170;0m"),
    ANSI_GRAY("\033[48;5;247m"),
    ANSI_CLEAR("\033[H\033[2J");

    /**
     * Ansi escape sequence representing the color.
     */
    private final String color;

    /**
     * Constructor of the enumeration.
     * @param color is the color to be set.
     */
    TuiColors(String color){ this.color = color; }

    /**
     * Method used to get the color.
     * @param tuiColor is the color to be returned.
     * @return the color.
     */
    public static String getColor(TuiColors tuiColor){ return tuiColor.color; }
}
