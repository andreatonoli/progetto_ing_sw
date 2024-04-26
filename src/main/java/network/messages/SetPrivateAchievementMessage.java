package network.messages;

import model.card.Achievement;

public class SetPrivateAchievementMessage extends Message {
    private Achievement achievement;
    public SetPrivateAchievementMessage(String username, Achievement achievement){
        super(MessageType.SET_ACHIEVEMENT, username);
        this.achievement = achievement;
    }

    public Achievement getAchievement(){
        return this.achievement;
    }

    @Override
    public String toString() {
        return "SetPrivateAchievement{" +
                "achievement: " + achievement +
                '}';
    }
}
