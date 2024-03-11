package model;

public class Corner {
    //Nella classe ResourceDeck ho sfruttato un ipotetico costruttore di Corner così definito
    //public Corner(Symbol symbol);
    //Se viene modificata questa firma bisogna cambiare anche le sue invocazioni in ResourceDeck
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
