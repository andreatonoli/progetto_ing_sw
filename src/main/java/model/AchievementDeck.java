package model;

import java.util.HashSet;
import java.util.Set;

public class AchievementDeck implements Deck{
    private Set<AchievementCard> deck;
    private HashSet<AchievementCard> alreadyDrawed; //forse useless
    /**
     * builds the deck with 16 GoldCards and initializes the alreadyDrawed deck, which contains the cards already extracted
     * from the deck
     */
    public AchievementDeck() {
        deck = Set.of(
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
        );
    }
}
