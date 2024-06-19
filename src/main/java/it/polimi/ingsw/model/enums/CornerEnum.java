package it.polimi.ingsw.model.enums;

/**
 * Enumeration that represents the corners position. For example Top-Left.
 */
public enum CornerEnum {

    /**
     * Top Left.
     */
    TL(-1, 1),

    /**
     * Top Right.
     */
    TR(1, 1),

    /**
     * Bottom Right.
     */
    BR(1, -1),

    /**
     * Bottom Left.
     */
    BL(-1,-1);

    /**
     * Value to add on the x-axis when we place a card on that corner.
     */
    private final int x;

    /**
     * value to add on the y-axis when we place a card on that corner.
     */
    private final int y;

    /**
     * Corner constructor.
     * @param x value to add on the x-axis when we place a card on that corner.
     * @param y value to add on the y-axis when we place a card on that corner.
     */
    CornerEnum(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Get the value to add on the x-axis when we place a card on that corner.
     * @return value to add on the x-axis when we place a card on that corner.
     */
    public int getX(){
        return this.x;
    }

    /**
     * Get value to add on the y-axis when we place a card on that corner.
     * @return value to add on the y-axis when we place a card on that corner.
     */
    public int getY(){
        return this.y;
    }

    /**
     * Get the corner juxtaposed to this. For example BR.getOppositeCorner() returns TL.
     * @return the opposite corner.
     */
    public CornerEnum getOppositePosition(){
        for (CornerEnum c : CornerEnum.values()){
            if (c.getX() == (-this.x) && c.getY() == (-this.y)){
                return c;
            }
        }
        return null;
    }
}
