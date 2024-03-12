package org.example;

import model.CardBack;
import model.ResourceDeck;
import model.Card;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ResourceDeck rodeck = new ResourceDeck();
        Card carta = rodeck.drawCard();
        System.out.println(carta.toInt());
        //System.out.println(carta.getBack(carta).toInt());
        carta.flipSide(carta);
        System.out.println(carta.toInt());
    }
}
