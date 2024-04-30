package org.example;

import Controller.Controller;
import model.*;
import model.exceptions.NotEnoughPlayersException;
import network.server.Server;
import view.*;

import java.io.IOException;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
   public static void main( String[] args ) throws IOException, NotEnoughPlayersException {

       Game game = new Game(2);
       Player player = new Player("p1", game);
       Player player2 = new Player("p2", game);
       Player player3 = new Player("p3", game);
       game.addPlayer(player);
       game.addPlayer(player2);
       game.addPlayer(player3);
       StarterCard s2 = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
       Tui t = new Tui();
       s2.setCurrentSide();

       player.getPlayerBoard().setStarterCard(s2);
       game.startGame();
       Controller con = new Controller(2);
       player.setPlayerState(PlayerState.PLAY_CARD);
       ResourceCard c1 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, 2);
       ResourceCard c2 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.PLANT), new Corner(Symbols.QUILL)}, 17, 0);
       ResourceCard c3 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 25, 0);
       ResourceCard c4 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 35, 0);
       ResourceCard c5 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.NOCORNER), new Corner(Symbols.MANUSCRIPT)}, 35, 2);

       con.placeCard(player, c1, new int[]{0, 0}, CornerEnum.BL);
       player.setPlayerState(PlayerState.PLAY_CARD);
       con.placeCard(player, c2, new int[]{0, 0}, CornerEnum.TL);
       player.setPlayerState(PlayerState.PLAY_CARD);
       con.placeCard(player, c3, new int[]{0, 0}, CornerEnum.TR);
       player.setPlayerState(PlayerState.PLAY_CARD);
       con.placeCard(player, c4, new int[]{1, 1}, CornerEnum.TR);
       player.removeFromHand(player.getCardInHand()[0]);
       player.addInHand(c1);
       player.removeFromHand(player.getCardInHand()[1]);
       player.addInHand(c2);
       player.removeFromHand(player.getCardInHand()[2]);
       player.addInHand(c5);
       player.sendMessage(player2, "ciao");
       t.printViewWithCommands(player.getPlayerBoard(), player.getCardInHand(), player.getUsername(), game.getGameBoard(), game.getPlayers(), player.getChat());
   }
}
