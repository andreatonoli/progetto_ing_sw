package Controller;
import model.*;
import network.messages.GenericMessage;
import network.messages.PlayerStateMessage;
import network.server.Server;
import observer.Observable;

import java.io.Serializable;

public class TurnHandler extends Observable implements Serializable {
    private Game game;
    private transient Server server;

    public TurnHandler(Game game, Server server){
        this.game=game;
        this.server=server;
    }

    public void changePlayerState(Player player){
        int i = (player.getPlayerState().ordinal() + 1) % 3;
        if (i == 0){
            game.setPlayerInTurn();
        }
        player.setPlayerState(PlayerState.values()[i]);
        notify(this.server.getClientFromName(player.getUsername()),new PlayerStateMessage(player.getPlayerState()));
    }

}
