package model;

public class Corner {
    //Bisogna anche pensare a come gestire i "non angoli"

    private Symbols symbol;

    private CornerState state;

    /**
     * Corner constructor
     * @param symbol is contained in the corner
     * corner's initial state set to visible
     */
    public Corner(Symbols symbol){
        this.symbol = symbol;
        this.state = CornerState.VISIBLE;
    }

    public Symbols getSymbol(){
        return this.symbol;
    }

    public void setSymbol(Symbols symbol){
        this.symbol = symbol;
    }
    public CornerState getState(){
        return this.state;
    }

    public void setState(CornerState state){
        this.state = state;
    }
}
