package it.polimi.ingsw.model.enums;

/**
 * Color enumeration used to indicate the card's color or the color chosen by the player
 */
public enum Color {

    RED(1, "\033[0;101m"),

    GREEN(3, "\033[0;102m"),

    BLUE(0, "\033[0;104m"),

    PURPLE(2, "\033[0;105m"),

    WHITE(4, "\033[48;5;252m"),

    YELLOW(5, "\033[43m"),

    ORANGE(6, "\033[48;5;208m");

    /**
     * Index of the color which is linked in the L shape objective. //TODO: commentare meglio
     */
    private final Integer associatedIndex;

    /**
     * ANSI escape code of the color. It is necessary to show the correct color in the TUI.
     */
    private final String background;

    /**
     * Color constructor.
     *
     * @param associatedIndex Index of the color which is linked in the L shape objective. //TODO: commentare meglio
     * @param background      ANSI escape code of the color. It is necessary to show the correct color in the TUI.
     */
    Color(Integer associatedIndex, String background){
        this.associatedIndex = associatedIndex;
        this.background = background;
    }

    /**
     * Getter of the associatedColor attribute.
     * @param color //TODO: il colore di cui prendere l'associatedColor.
     * @return the {@param color} associated color.
     */
    public static Color getAssociatedColor(Color color){
        for (Color c : Color.values()){
            if (color.associatedIndex == c.ordinal()){
                return c;
            }
        }
        //Useless return, the nested if will never fail
        return Color.WHITE;
    }

    /**
     * Gets the ANSI escape sequence to represent the color.
     * @param color color to get the ANSI code. //TODO: scrivere meglio
     * @return the ANSI escape sequence of the color.
     */
    public static String getBackground(Color color){ return color.background; }
}
