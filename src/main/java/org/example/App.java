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
       //t.printTitle();
       //t.printCard(s2);
       GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.CORNER, 7, new int[]{0, 3, 0, 0}, null);
       GoldCard c = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.CORNER, 17, new int[]{0, 3, 0, 0}, null);
       GoldCard d = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.CORNER, 27, new int[]{0, 3, 0, 0}, null);
       GoldCard e = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.CORNER, 37, new int[]{0, 3, 0, 0}, null);
       //t.printCard(b);
       //t.printCard(c);
       //t.printCard(d);
       //t.printCard(e);
       Achievement a1 = new AchievementItem(3, new ArrayList<>(List.of(Symbols.INKWELL, Symbols.QUILL, Symbols.MANUSCRIPT)));
       Achievement a2 = new AchievementResources(Symbols.FUNGI);
       Achievement a3 = new AchievementDiagonal(Color.PURPLE);
       Achievement a4 = new AchievementL(Color.GREEN);
       //t.printAchievement(a1);
       //t.printAchievement(a2);
       //t.printAchievement(a3);
       //t.printAchievement(a4);

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
       t.printView(player.getPlayerBoard(), player.getCardInHand(), player.getUsername(), game.getGameBoard(), game.getPlayers());
       //t.printPlayerBoard(player);
//
       t.printViewWithCommands(player.getPlayerBoard(), player.getCardInHand(), player.getUsername(), game.getGameBoard(), game.getPlayers());


       //System.out.println(FUNGI + "\n");
       //System.out.println(PLANT + "\n");
       //System.out.println(ANIMAL + "\n");
       //System.out.println(INSECT + "\n");
       //System.out.println(QUILL + "\n");
       //System.out.println(INKWELL + "\n");
       //System.out.println(MANUSCRIPT + "\n");
//
       //int ROW = 3;
       //int COLUMN = 11;
       //String[][] card = new String[ROW][COLUMN];
       //card[0][0] = "⎸";
       //card[0][1] = FUNGI;
       //card[0][2] = EMPTY_SPACE;
       //card[0][3] = EMPTY_SPACE;
       //card[0][4] = "1";
       //card[0][5] = " | ";
       //card[0][6] = QUILL;
       //card[0][7] = EMPTY_SPACE;
       //card[0][8] = EMPTY_SPACE;
       //card[0][9] = ANIMAL;
       //card[0][10] = "⎹";
//
       //card[1][0] = "⎸";
       //card[1][1] = EMPTY_SPACE;
       //card[1][2] = EMPTY_SPACE;
       //card[1][3] = EMPTY_SPACE;
       //card[1][4] = FUNGI;
       //card[1][5] = INSECT;
       //card[1][6] = PLANT;
       //card[1][7] = EMPTY_SPACE;
       //card[1][8] = EMPTY_SPACE;
       //card[1][9] = EMPTY_SPACE;
       //card[1][10] = "  ⎹";
//
       //card[2][0] = "⎸";
       //card[2][1] = EMPTY_CORNER;
       //card[2][2] = EMPTY_SPACE;
       //card[2][3] = PLANT;
       //card[2][4] = PLANT;
       //card[2][5] = PLANT;
       //card[2][6] = FUNGI;
       //card[2][7] = FUNGI;
       //card[2][8] = EMPTY_SPACE;
       //card[2][9] = MANUSCRIPT;
       //card[2][10] = "⎹";

       //    Game game = new Game();
   //    Player player1 = new Player("pippo", game.getGameBoard());
   //    Player player2 = new Player("pluto", game.getGameBoard());
   //    game.addPlayer(player1);
   //    game.addPlayer(player2);
   //    game.startGame();
   //    System.out.println("carte risorsa");
   //    for (int i = 0; i < game.getGameBoard().getCommonResource().length ; i++){
   //        System.out.println(game.getGameBoard().getCommonResource()[i].getCardNumber() + " " + game.getGameBoard().getCommonResource()[i].getClass());
   //    }
   //    System.out.println("carte oro");
   //    for (int i = 0; i < game.getGameBoard().getCommonGold().length ; i++){
   //        System.out.println(game.getGameBoard().getCommonGold()[i].getCardNumber() + " " + game.getGameBoard().getCommonGold()[i].getClass());
   //    }
   //    System.out.println("carte obiettivo");
   //    for (int i = 0; i < game.getGameBoard().getCommonAchievement().length ; i++){
   //        System.out.println(game.getGameBoard().getCommonAchievement()[i].getClass());
   //    }
   //    for (Player p : game.getPlayers()){
   //        System.out.println("giocatore : " + p.getUsername());
   //        System.out.println("starter card");
   //        System.out.println(p.getPlayerBoard().getStarterCard().getCardNumber() + " " + p.getPlayerBoard().getStarterCard().getClass());
   //        System.out.println("mano");
   //        for(int i = 0; i < p.getCardInHand().length; i++){
   //            System.out.println(p.getCardInHand()[i].getCardNumber() + " " + p.getCardInHand()[i].getClass());
   //        }
   //        System.out.println(p.getChosenObj().getClass());
   //    }
//
   }


/*
    public StarterCard drawCard() {
        StarterCard drew = null;
        int drew_index = rand.nextInt(deck.size());
        drew = deck.get(drew_index);
        deck.remove(drew_index);
        return drew;
    }*/
}
