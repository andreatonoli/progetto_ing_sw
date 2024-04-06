package model;
public enum Color {
    RED(1),
    GREEN(3),
    BLUE(0),
    PURPLE(2),
    WHITE(4);
    Integer associatedIndex;
    Color(Integer associatedIndex){
        this.associatedIndex = associatedIndex;
    }
    public static Color getAssociatedColor(Color color){
        for (Color c : Color.values()){
            if (color.associatedIndex == c.ordinal()){
                return c;
            }
        }
        return Color.WHITE;
    }
}
