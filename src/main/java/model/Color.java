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
    public Color getAssociatedColor(Color color){
        for (Color c : Color.values()){
            if (c.associatedIndex == color.ordinal()){
                return c;
            }
        }
        return Color.WHITE;
    }
}
