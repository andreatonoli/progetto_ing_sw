package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.network.server.Server;

public class ReconnectionMessage extends Message {
    private GameBean gameBean;

    private PlayerBean playerBean;
    public ReconnectionMessage(GameBoard board, Player player){
        super(MessageType.RECONNECTION, Server.serverName);
        setGameBean(board);
        setPlayerBean(player);
    }

    public GameBean getGameBean() {
        return gameBean;
    }

    private void setGameBean(GameBoard game) {
        gameBean = new GameBean();
        for (int i=0; i<2; i++) {
            gameBean.setCommonGold(i,game.getCommonGold()[i]);
        }
        for (int i=0; i<2; i++) {
            gameBean.setCommonResources(i,game.getCommonResource()[i]);
        }
        for (int i=0; i<2; i++) {
            gameBean.setCommonAchievement(i,game.getCommonAchievement()[i]);
        }
        gameBean.setGoldDeckRetro(game.getGoldDeckRetro());
        gameBean.setResourceDeckRetro(game.getResourceDeckRetro());
    }

    public PlayerBean getPlayerBean() {
        return playerBean;
    }

    private void setPlayerBean(Player player) {
        playerBean = new PlayerBean(player.getUsername());
        playerBean.setBoard(player.getPlayerBoard());
        playerBean.setPionColor(player.getPionColor());
        playerBean.setHand(player.getCardInHand());
        playerBean.setPoints(player.getPoints());
        playerBean.setAchievement(player.getChosenObj());
        playerBean.setStarterCard(null);
    }

    public String toString(){
        return "ReconnectionMessage{"+
                "player: " + playerBean +
                "game: " + gameBean +
                '}';
    }
}
