package it.polimi.ingsw.model.exceptions;

public class JsonFileNotFoundException extends Exception{
    private String path;
    public JsonFileNotFoundException(String path){
        super();
        this.path = path;
    }

    @Override
    public String getMessage(){
        return "File " + path + "not found";
    }
}
