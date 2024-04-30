package org.example;

import network.client.Client;
import network.server.Server;

//private final Runnable action;
//public Action(Runnable action){
//    this.action=action;
//}
////public void execute(){
////    action.run();
////}
//public void execute(){
//
//}
//package org.example;
//
//import Controller.Controller;
//import model.*;
//import model.exceptions.NotEnoughPlayersException;
//import network.server.Server;
//import view.*;
//
//import java.io.IOException;
//import java.util.*;
//
///**
// * Hello world!
// *
// */
public class App {
    public static void main(String[] args) {
        if (args[0].equalsIgnoreCase("0")){
            new Server();
        }
        else{
            new Client();
        }
    }
}

       //t.printView(player);
       //t.printPlayerBoard(player);
//=======
////package org.example;
//>>>>>>> Stashed changes
////
////import Controller.Controller;
////import model.*;
////import model.exceptions.NotEnoughPlayersException;
////import view.*;
////
////import java.io.IOException;
////import java.util.*;
////
/////**
//// * Hello world!
//// *
//// */
////public class App
////{
////   public static void main( String[] args ) throws IOException, NotEnoughPlayersException {
////
////       Game game = new Game(4);
////       Player player = new Player("p1", game);
////       Player player2 = new Player("p2", game);
////       game.addPlayer(player);
////       game.addPlayer(player2);
////       StarterCard s2 = new StarterCard(new Corner[]{new Corner(Symbols.PLANT), new Corner(Symbols.ANIMAL), new Corner(Symbols.INSECT), new Corner(Symbols.FUNGI)}, 2, new CardBack(new ArrayList<>(List.of(Symbols.FUNGI)), Color.WHITE, new Corner[]{new Corner(Symbols.ANIMAL), new Corner(Symbols.EMPTY), new Corner(Symbols.FUNGI), new Corner(Symbols.EMPTY)}));
////       Tui t = new Tui();
////       s2.setCurrentSide();
////       t.printTitle();
////       t.printCard(player, s2);
////       GoldCard b = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.CORNER, 7, new int[]{0, 3, 0, 0}, null);
////       GoldCard c = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.CORNER, 17, new int[]{0, 3, 0, 0}, null);
////       GoldCard d = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.CORNER, 27, new int[]{0, 3, 0, 0}, null);
////       GoldCard e = new GoldCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, Condition.CORNER, 37, new int[]{0, 3, 0, 0}, null);
////       t.printCard(player, b);
////       t.printCard(player, c);
////       t.printCard(player, d);
////       t.printCard(player, e);
////       Achievement a1 = new AchievementItem(3, new ArrayList<>(List.of(Symbols.INKWELL, Symbols.QUILL, Symbols.MANUSCRIPT)));
////       Achievement a2 = new AchievementResources(Symbols.FUNGI);
////       Achievement a3 = new AchievementDiagonal(Color.PURPLE);
////       Achievement a4 = new AchievementL(Color.GREEN);
////       t.printCard(a1);
////       t.printCard(a2);
////       t.printCard(a3);
////       t.printCard(a4);
////
////       game.startGame();
////       Controller con = new Controller(game);
////       player.setPlayerState(PlayerState.PLAY_CARD);
////       ResourceCard c1 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 3, 0);
////       ResourceCard c2 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 17, 0);
////       ResourceCard c3 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 25, 0);
////       ResourceCard c4 = new ResourceCard(new Corner[]{new Corner(Symbols.EMPTY), new Corner(Symbols.NOCORNER), new Corner(Symbols.NOCORNER), new Corner(Symbols.QUILL)}, 35, 0);
////       con.placeCard(player, c1, new int[]{0, 0}, CornerEnum.BL);
////       con.placeCard(player, c2, new int[]{0, 0}, CornerEnum.TL);
////       con.placeCard(player, c3, new int[]{0, 0}, CornerEnum.TR);
////       con.placeCard(player, c4, new int[]{1, 1}, CornerEnum.TR);
////       player.removeFromHand(player.getCardInHand()[0]);
////       player.addInHand(c1);
////       player.removeFromHand(player.getCardInHand()[1]);
////       player.addInHand(c2);
////       player.removeFromHand(player.getCardInHand()[2]);
////       player.addInHand(c3);
////       System.out.println(player.getCardInHand()[0].getClass());
////       System.out.println(player.getCardInHand()[0].getColor());
////       System.out.println(c1.getClass());
////       System.out.println(c1.getColor());
////       System.out.println(player.getCardInHand()[1].getClass());
////       System.out.println(player.getCardInHand()[1].getColor());
////       System.out.println(c2.getClass());
////       System.out.println(c2.getColor());
////       System.out.println(player.getCardInHand()[2].getClass());
////       System.out.println(player.getCardInHand()[2].getColor());
////       System.out.println(c3.getClass());
////       System.out.println(c3.getColor());
////       t.printView(player);
////       //t.printPlayerBoard(player);
//////
////       //Scanner input = new Scanner(System.in);
////       //int[] coord = new int[2];
////       //while(true){
////       //    System.out.println("Which card do you want to display?");
////       //    System.out.println("Type row number");
////       //    coord[0] = input.nextInt();
////       //    System.out.println("Type column number");
////       //    coord[1] = input.nextInt();
////       //    t.printCardFromPlayerboard(player, coord);
////       //}
////
////
////       //System.out.println(FUNGI + "\n");
////       //System.out.println(PLANT + "\n");
////       //System.out.println(ANIMAL + "\n");
////       //System.out.println(INSECT + "\n");
////       //System.out.println(QUILL + "\n");
////       //System.out.println(INKWELL + "\n");
////       //System.out.println(MANUSCRIPT + "\n");
//////
////       //int ROW = 3;
////       //int COLUMN = 11;
////       //String[][] card = new String[ROW][COLUMN];
////       //card[0][0] = "⎸";
////       //card[0][1] = FUNGI;
////       //card[0][2] = EMPTY_SPACE;
////       //card[0][3] = EMPTY_SPACE;
////       //card[0][4] = "1";
////       //card[0][5] = " | ";
////       //card[0][6] = QUILL;
////       //card[0][7] = EMPTY_SPACE;
////       //card[0][8] = EMPTY_SPACE;
////       //card[0][9] = ANIMAL;
////       //card[0][10] = "⎹";
//////
////       //card[1][0] = "⎸";
////       //card[1][1] = EMPTY_SPACE;
////       //card[1][2] = EMPTY_SPACE;
////       //card[1][3] = EMPTY_SPACE;
////       //card[1][4] = FUNGI;
////       //card[1][5] = INSECT;
////       //card[1][6] = PLANT;
////       //card[1][7] = EMPTY_SPACE;
////       //card[1][8] = EMPTY_SPACE;
////       //card[1][9] = EMPTY_SPACE;
////       //card[1][10] = "  ⎹";
//////
////       //card[2][0] = "⎸";
////       //card[2][1] = EMPTY_CORNER;
////       //card[2][2] = EMPTY_SPACE;
////       //card[2][3] = PLANT;
////       //card[2][4] = PLANT;
////       //card[2][5] = PLANT;
////       //card[2][6] = FUNGI;
////       //card[2][7] = FUNGI;
////       //card[2][8] = EMPTY_SPACE;
////       //card[2][9] = MANUSCRIPT;
////       //card[2][10] = "⎹";
////
////       //    Game game = new Game();
////   //    Player player1 = new Player("pippo", game.getGameBoard());
////   //    Player player2 = new Player("pluto", game.getGameBoard());
////   //    game.addPlayer(player1);
////   //    game.addPlayer(player2);
////   //    game.startGame();
////   //    System.out.println("carte risorsa");
////   //    for (int i = 0; i < game.getGameBoard().getCommonResource().length ; i++){
////   //        System.out.println(game.getGameBoard().getCommonResource()[i].getCardNumber() + " " + game.getGameBoard().getCommonResource()[i].getClass());
////   //    }
////   //    System.out.println("carte oro");
////   //    for (int i = 0; i < game.getGameBoard().getCommonGold().length ; i++){
////   //        System.out.println(game.getGameBoard().getCommonGold()[i].getCardNumber() + " " + game.getGameBoard().getCommonGold()[i].getClass());
////   //    }
////   //    System.out.println("carte obiettivo");
////   //    for (int i = 0; i < game.getGameBoard().getCommonAchievement().length ; i++){
////   //        System.out.println(game.getGameBoard().getCommonAchievement()[i].getClass());
////   //    }
////   //    for (Player p : game.getPlayers()){
////   //        System.out.println("giocatore : " + p.getUsername());
////   //        System.out.println("starter card");
////   //        System.out.println(p.getPlayerBoard().getStarterCard().getCardNumber() + " " + p.getPlayerBoard().getStarterCard().getClass());
////   //        System.out.println("mano");
////   //        for(int i = 0; i < p.getCardInHand().length; i++){
////   //            System.out.println(p.getCardInHand()[i].getCardNumber() + " " + p.getCardInHand()[i].getClass());
////   //        }
////   //        System.out.println(p.getChosenObj().getClass());
////   //    }
//////
////   }
////
////
/////*
////    public StarterCard drawCard() {
////        StarterCard drew = null;
////        int drew_index = rand.nextInt(deck.size());
////        drew = deck.get(drew_index);
////        deck.remove(drew_index);
////        return drew;
////    }*/
////}
////