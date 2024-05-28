<<<<<<< HEAD
package org.example;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws Exception {

        //Game game = new Game(2);
        //Player player = new Player("p1", game);
        //Player player2 = new Player("p2", game);
        //Player player3 = new Player("p3", game);
        //PlayerBean pb = new PlayerBean("p1");
        //PlayerBean pb2 = new PlayerBean("p2");
        //PlayerBean pb3 = new PlayerBean("p3");
        //ArrayList<PlayerBean> players = new ArrayList<>(List.of(pb, pb2, pb3));
        //game.addPlayer(player);
        //game.addPlayer(player2);
        //game.addPlayer(player3);
        //StarterCard s2 = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI, Symbols.PLANT, Symbols.ANIMAL)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
        //Tui t = new Tui();
        //s2.setCurrentSide();
//
        //player.getPlayerBoard().setStarterCard(s2);
        //game.startGame();
        //Controller con = new Controller(2);
        //player.setPlayerState(PlayerState.PLAY_CARD);
        //ResourceCard c1 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, 2);
        //ResourceCard c2 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL)}, 17, 0);
        //ResourceCard c3 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 25, 0);
        //ResourceCard c4 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 35, 0);
        //ResourceCard c5 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.NOCORNER), new Corner(Symbols.MANUSCRIPT)}, 35, 2);
        //Achievement a1 = new AchievementResources(Symbols.FUNGI);
//
        //player.placeCard(c1, new int[]{0, 0}, CornerEnum.BL);
        //player.setPlayerState(PlayerState.PLAY_CARD);
        //player.placeCard(c2, new int[]{0, 0}, CornerEnum.TL);
        //player.setChosenObj(a1);
        ////player.setPlayerState(PlayerState.PLAY_CARD);
        ////player.placeCard(c3, new int[]{0, 0}, CornerEnum.TR);
        ////player.setPlayerState(PlayerState.PLAY_CARD);
        ////player.placeCard(c4, new int[]{1, 1}, CornerEnum.TR);
        //player.setPlayerState(PlayerState.PLAY_CARD);
        //player.placeCard(c4, new int[]{-1, 1}, CornerEnum.TL);
        //player.setPlayerState(PlayerState.PLAY_CARD);
        //player.placeCard(c3, new int[]{-2, 2}, CornerEnum.TL);
        //player.removeFromHand(player.getCardInHand()[0]);
        //player.addInHand(c1);
        //player.removeFromHand(player.getCardInHand()[1]);
        //player.addInHand(c2);
        //player.removeFromHand(player.getCardInHand()[2]);
        //player.addInHand(c5);
        //player2.sendMessage(player, "messaggio1 da p2");
        //player3.sendMessage(player, "messaggio2 da p3");
        //player2.sendMessage(player, "messaggio3 da p2");
        //player2.sendMessage(player, "messaggio4 da p2");
        //player2.sendMessage(player, "messaggio5 da p2");
        //player2.sendMessage(player, "messaggio6 da p2");
        //player2.sendMessage(player, "messaggio7 da p2");
        //player2.sendMessage(player, "messaggio8 da p2");
        //pb.setState(player.getPlayerState());
        //pb2.setState(player2.getPlayerState());
        //pb3.setState(player3.getPlayerState());
        //game.getGameBoard().getResourceDeck().getFirst().setCurrentSide();
        //game.getGameBoard().getGoldDeck().getFirst().setCurrentSide();
        //t.printViewWithCommands(player.getPlayerBoard(), player.getCardInHand(), player.getUsername(), game.getGameBoard().getCommonResource(), game.getGameBoard().getResourceDeck().getFirst(), game.getGameBoard().getCommonGold(), game.getGameBoard().getGoldDeck().getFirst(), game.getGameBoard().getCommonAchievement(), player.getChosenObj(), players, player.getChat());
        //game.getGameBoard().getResourceDeck().getFirst().setCurrentSide();
        //game.getGameBoard().getGoldDeck().getFirst().setCurrentSide();
    }
}
=======
//package org.example;
//
//import it.polimi.ingsw.Controller.Controller;
//import it.polimi.ingsw.model.*;
//import it.polimi.ingsw.model.card.*;
//import it.polimi.ingsw.model.enums.*;
//import it.polimi.ingsw.model.exceptions.*;
//import it.polimi.ingsw.model.player.Player;
//import it.polimi.ingsw.network.client.PlayerBean;
//import it.polimi.ingsw.network.server.Server;
//import it.polimi.ingsw.view.*;
//
//import java.io.IOException;
//import java.util.*;
//
///**
// * Hello world!
// *
// */
//public class App
//{
//    public static void main( String[] args ) throws IOException, NotEnoughPlayersException, OccupiedCornerException, NotInTurnException, AlreadyUsedPositionException, CostNotSatisfiedException {
//
//        Game game = new Game(2);
//        Player player = new Player("p1", game);
//        Player player2 = new Player("p2", game);
//        Player player3 = new Player("p3", game);
//        PlayerBean pb = new PlayerBean("p1");
//        PlayerBean pb2 = new PlayerBean("p2");
//        PlayerBean pb3 = new PlayerBean("p3");
//        ArrayList<PlayerBean> players = new ArrayList<>(List.of(pb, pb2, pb3));
//        game.addPlayer(player);
//        game.addPlayer(player2);
//        game.addPlayer(player3);
//        StarterCard s2 = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI, Symbols.PLANT, Symbols.ANIMAL)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
//        Tui t = new Tui();
//        s2.setCurrentSide();
//
//        player.getPlayerBoard().setStarterCard(s2);
//        game.startGame();
//        Controller con = new Controller(2);
//        player.setPlayerState(PlayerState.PLAY_CARD);
//        ResourceCard c1 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, 2);
//        ResourceCard c2 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL)}, 17, 0);
//        ResourceCard c3 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 25, 0);
//        ResourceCard c4 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 35, 0);
//        ResourceCard c5 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.NOCORNER), new Corner(Symbols.MANUSCRIPT)}, 35, 2);
//        Achievement a1 = new AchievementResources(Symbols.FUNGI);
//
//        player.placeCard(c1, new int[]{0, 0}, CornerEnum.BL);
//        player.setPlayerState(PlayerState.PLAY_CARD);
//        player.placeCard(c2, new int[]{0, 0}, CornerEnum.TL);
//        player.setChosenObj(a1);
//        //player.setPlayerState(PlayerState.PLAY_CARD);
//        //player.placeCard(c3, new int[]{0, 0}, CornerEnum.TR);
//        //player.setPlayerState(PlayerState.PLAY_CARD);
//        //player.placeCard(c4, new int[]{1, 1}, CornerEnum.TR);
//        player.setPlayerState(PlayerState.PLAY_CARD);
//        player.placeCard(c4, new int[]{-1, 1}, CornerEnum.TL);
//        player.setPlayerState(PlayerState.PLAY_CARD);
//        player.placeCard(c3, new int[]{-2, 2}, CornerEnum.TL);
//        player.removeFromHand(player.getCardInHand()[0]);
//        player.addInHand(c1);
//        player.removeFromHand(player.getCardInHand()[1]);
//        player.addInHand(c2);
//        player.removeFromHand(player.getCardInHand()[2]);
//        player.addInHand(c5);
//        player2.sendMessage(player, "messaggio1 da p2");
//        player3.sendMessage(player, "messaggio2 da p3");
//        player2.sendMessage(player, "messaggio3 da p2");
//        player2.sendMessage(player, "messaggio4 da p2");
//        player2.sendMessage(player, "messaggio5 da p2");
//        player2.sendMessage(player, "messaggio6 da p2");
//        player2.sendMessage(player, "messaggio7 da p2");
//        player2.sendMessage(player, "messaggio8 da p2");
//        pb.setState(player.getPlayerState());
//        pb2.setState(player2.getPlayerState());
//        pb3.setState(player3.getPlayerState());
//        game.getGameBoard().getResourceDeck().getFirst().setCurrentSide();
//        game.getGameBoard().getGoldDeck().getFirst().setCurrentSide();
//        t.printViewWithCommands(player.getPlayerBoard(), player.getCardInHand(), player.getUsername(), game.getGameBoard().getCommonResource(), game.getGameBoard().getResourceDeck().getFirst(), game.getGameBoard().getCommonGold(), game.getGameBoard().getGoldDeck().getFirst(), game.getGameBoard().getCommonAchievement(), player.getChosenObj(), players, player.getChat());
//        game.getGameBoard().getResourceDeck().getFirst().setCurrentSide();
//        game.getGameBoard().getGoldDeck().getFirst().setCurrentSide();
//    }
//}
>>>>>>> network
