package model.enums;

/**
 * PLAY_CARD => player is placing a card
 * DRAW_CARD => Player is drawing a card
 * NOT_IN_TURN => Player waiting his turn
 */
public enum PlayerState {
    PLAY_CARD("place a card"),
    DRAW_CARD("draw one of the visible cards or draw a card from a deck"),
    NOT_IN_TURN("wait for your next turn");
    private String text;
    PlayerState(String text){
        this.text=text;
    }

    @Override
    public String toString() {
        return text;
    }
}
