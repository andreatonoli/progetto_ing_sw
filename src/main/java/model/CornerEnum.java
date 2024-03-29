package model;

public enum CornerEnum {
    TL(-1, 1),
    TR(1, 1),
    BR(1, -1),
    BL(-1,-1);
    int x;
    int y;
    CornerEnum(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    //TODO: scrivere commento
    /**
     *
     * @return
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
