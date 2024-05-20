package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.network.server.Server;

import java.util.ArrayList;
import java.util.List;

public class ReconnectionMessage extends Message {
    private final GameBean gameBean;

    private final PlayerBean playerBean;

    private final ArrayList<PlayerBean> opponents;
    public ReconnectionMessage(GameBoard board, Player player, List<Player> opponents){
        super(MessageType.RECONNECTION, Server.serverName);

        //Creating gameBean
        gameBean = new GameBean();
        for (int i=0; i<2; i++) {
            gameBean.setCommonGold(i, board.getCommonGold()[i]);
        }
        for (int i=0; i<2; i++) {
            gameBean.setCommonResources(i, board.getCommonResource()[i]);
        }
        for (int i=0; i<2; i++) {
            gameBean.setCommonAchievement(i, board.getCommonAchievement()[i]);
        }
        gameBean.setGoldDeckRetro(board.getGoldDeckRetro());
        gameBean.setResourceDeckRetro(board.getResourceDeckRetro());

        //Creating playerBean
        playerBean = new PlayerBean(player.getUsername());
        playerBean.setBoard(player.getPlayerBoard());
        playerBean.setPionColor(player.getPionColor());
        playerBean.setHand(player.getCardInHand());
        playerBean.setPoints(player.getPoints());
        playerBean.setAchievement(player.getChosenObj());
        playerBean.setStarterCard(null);

        //Creating opponents
        this.opponents = new ArrayList<>();
        for (Player p : opponents){
            PlayerBean pb = new PlayerBean(p.getUsername());
            pb.setPionColor(p.getPionColor());
            pb.setState(p.getPlayerState());
            pb.setBoard(p.getPlayerBoard());
            pb.setPoints(p.getPoints());
            this.opponents.add(pb);
        }
    }

    public GameBean getGameBean() {
        return gameBean;
    }

    public PlayerBean getPlayerBean() {
        return playerBean;
    }

    public ArrayList<PlayerBean> getOpponents(){
        return opponents;
    }

    public String toString(){
        return "ReconnectionMessage{"+
                "player: " + playerBean +
                "game: " + gameBean +
                "opponents: " + opponents +
                '}';
    }
}
