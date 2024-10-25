package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.enums.PlayerState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.client.GameBean;
import it.polimi.ingsw.network.client.PlayerBean;
import it.polimi.ingsw.network.server.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Message notify the client of the updates that happened while he was disconnected.
 */
public class ReconnectionMessage extends Message {

    /**
     * GameBean containing the game status.
     */
    private final GameBean gameBean;

    /**
     * PlayerBean containing the player status.
     */
    private final PlayerBean playerBean;

    /**
     * List of PlayerBean containing the opponents' status.
     */
    private final ArrayList<PlayerBean> opponents;

    /**
     * Constructor ReconnectionMessage creates a new ReconnectionMessage instance.
     * @param board board from which to retrieve the status of the game.
     * @param player Player instance from which to retrieve the player state.
     * @param opponents list of the opponents.
     */
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
        gameBean.setGoldDeckRetro(board.getGoldDeck().getFirst().getColor());
        gameBean.setResourceDeckRetro(board.getResourceDeck().getFirst().getColor());

        //Creating playerBean
        playerBean = new PlayerBean(player.getUsername());
        playerBean.setBoard(player.getPlayerBoard());
        playerBean.setPionColor(player.getPionColor());
        playerBean.setHand(player.getCardInHand());
        playerBean.setPoints(player.getPoints());
        playerBean.setAchievement(player.getChosenObj());
        playerBean.setState(PlayerState.NOT_IN_TURN);
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
        return this.gameBean;
    }

    public PlayerBean getPlayerBean() {
        return this.playerBean;
    }

    public ArrayList<PlayerBean> getOpponents(){
        return this.opponents;
    }

    public String toString(){
        return "ReconnectionMessage{"+
                "player: " + playerBean +
                "game: " + gameBean +
                "opponents: " + opponents +
                '}';
    }
}
