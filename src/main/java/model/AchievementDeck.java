package model;

import java.util.LinkedList;
import java.util.List;

public class AchievementDeck extends Deck{
    private LinkedList<AchievementCard> deck;
    /**
     * builds the deck with 16 GoldCards and initializes the alreadyDrawed deck, which contains the cards already extracted
     * from the deck
     */
    public AchievementDeck() {
        deck = new LinkedList<>();
        deck.addAll(List.of(
                new AchievementCard(2, "red diagonal"),
                new AchievementCard(2, "green diagonal"),
                new AchievementCard(2, "blue diagonal"),
                new AchievementCard(2, "purple diagonal"),
                new AchievementCard(3, "red l"),
                new AchievementCard(3, "blue l"),
                new AchievementCard(3, "green l"),
                new AchievementCard(3, "purple l"),
                new AchievementCard(2, "fungi"),
                new AchievementCard(2, "plant"),
                new AchievementCard(2, "animal"),
                new AchievementCard(2, "insect"),
                new AchievementCard(3, "symbols"),
                new AchievementCard(2, "manoscript"),
                new AchievementCard(2, "inkwell"),
                new AchievementCard(2, "quill")
        ));
    }
    public AchievementCard drawCard() {
        AchievementCard drew = null;
        int drew_index = rand.nextInt(deck.size());
        drew = deck.get(drew_index);
        deck.remove(drew_index);
        return drew;
    }

    public AchievementCard drawCard(Player player) {
        AchievementCard drew = null;
        int drew_index = rand.nextInt(deck.size() + 1);
        drew = deck.get(drew_index);
        deck.remove(drew_index);
        return drew;
    }
}
