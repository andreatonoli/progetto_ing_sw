package it.polimi.ingsw.network.server;

//TODO: cambia nome
public class ActionMessage {
    private Connection applicant;
    private Action command;
    public ActionMessage(Connection applicant, Action command){
        this.applicant = applicant;
        this.command = command;
    }

    public Connection getApplicant() {
        return applicant;
    }

    public Action getCommand() {
        return command;
    }
}
