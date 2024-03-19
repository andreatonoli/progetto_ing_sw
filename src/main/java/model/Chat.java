package model;

import model.exceptions.PlayerNotFoundException;
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

    public void forwardMessage(Player sender, Player receiver, boolean global, String message) throws PlayerNotFoundException{
        if (global){
            for (Player p : players){
                //attributo da inserire nella classe Player per visualizzare il messaggio nella propria chat
                //p.displayMessage(sender, message);
            }
        }
        else{
            if(!players.contains(receiver)){
                throw new PlayerNotFoundException();
            }
        }
    }
}
