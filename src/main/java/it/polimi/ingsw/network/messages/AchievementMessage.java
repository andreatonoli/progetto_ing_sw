package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.card.Achievement;
import it.polimi.ingsw.network.server.Server;

public class AchievementMessage extends Message {
    Achievement[] achievements;
    public AchievementMessage(MessageType mType, Achievement[] achievements){
        super(mType, Server.serverName);
        this.achievements = new Achievement[2];
        System.arraycopy(achievements, 0, this.achievements , 0, 2);
    }

    public Achievement[] getAchievements() {
        return achievements;
    }

    @Override
    public String toString() {
        return "AchievementMessage{" +
                "achievements: " + achievements +
                '}';
    }
}
