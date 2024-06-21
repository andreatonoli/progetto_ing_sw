package it.polimi.ingsw.view.tui;

public enum TuiColors {
    ANSI_RESET("\033[0m"),
    ANSI_RED("\033[31;1m"),
    ANSI_GREEN("\033[32;1m"),
    ANSI_YELLOW("\033[33;1m"),
    ANSI_BLUE("\033[34;1m"),
    ANSI_PURPLE("\033[35;1m"),
    ANSI_BLACK("\033[30;1m"),
    ANSI_WHITE("\033[37;1m"),
    ANSI_CLEAR("\033[H\033[2J");

    private final String color;

    TuiColors(String color){ this.color = color; }

    public static String getColor(TuiColors tuiColor){ return tuiColor.color; }
}
