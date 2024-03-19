package org.example;

import model.*;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
   public static void main( String[] args )
    {
        Game game = new Game();
        try {
            Board board = new Board(game);
            Player pippo = new Player("pippo", board);
            pippo.setGame(game);
            game.addPlayer(pippo);
            pippo.sendMessage(true, "ciao pipo");
        } catch (IOException e){
            System.out.println("suca");
        }

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
