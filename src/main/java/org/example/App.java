package org.example;

import model.*;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Hello world!
 *
 */
public class App 
{
   public static void main( String[] args ) throws IOException {

    }
    public void shuffleDeck(LinkedList<Card> deck){
        Collections.shuffle(deck);
    }
    public Achievement drawCardA(LinkedList<Achievement> achievementDeck) {
        Achievement drew;
        drew = achievementDeck.getFirst();
        achievementDeck.removeFirst();
        return drew;
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
