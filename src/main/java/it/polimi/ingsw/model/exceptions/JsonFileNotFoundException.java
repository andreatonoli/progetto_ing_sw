package it.polimi.ingsw.model.exceptions;

/**
 * This exception is thrown when the JSON files necessary to run the game are not found
 */
public class JsonFileNotFoundException extends Exception{
    private final String path;
    public JsonFileNotFoundException(String path){
        super();
        this.path = path;
    }

    @Override
    public String getMessage(){
        return "File " + path + "not found";
    }
}
