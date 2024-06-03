package it.polimi.ingsw.view;

public enum TuiColors {
    ANSI_RESET("\u001B[0m"),
    ANSI_RED("\u001B[31;1m"),
    ANSI_GREEN("\u001B[32;1m"),
    ANSI_YELLOW("\u001B[33;1m"),
    ANSI_BLUE("\u001B[34;1m"),
    ANSI_PURPLE("\u001B[35;1m"),
    ANSI_BLACK("\u001B[30;1m"),
    ANSI_WHITE("\u001B[37;1m"),
    ANSI_CLEAR("\033[H\033[2J");

    final String color;

    TuiColors(String color){ this.color = color; }

    public static String getColor(TuiColors tuiColor){ return tuiColor.color; }
}
