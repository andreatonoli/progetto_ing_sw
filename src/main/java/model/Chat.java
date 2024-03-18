package model;

import java.util.List;

public class Chat {

    //id del game
    private Game game;

    //ogni player ha la sua chat
    private Player player;

    //lista dei player
    private List<Player> players;

    public Chat() {

    }

    public void sendMessage(Player sender, Player receiver, boolean global, String message){
        if (global){
            for (Player p : players){
                //attributo da inserire nella classe Player per visualizzare il messaggio nella propria chat
                //p.displayMessage(sender, message);
            }
        }
        else{
            //receiver.displayMessage(sender, message);
        }
    }
}
