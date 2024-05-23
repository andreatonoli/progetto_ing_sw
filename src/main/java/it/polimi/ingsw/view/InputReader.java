package it.polimi.ingsw.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class InputReader implements Callable<String> {
    //TODO: studiare meglio BufferedReader
    private final BufferedReader br;
    public InputReader(){
        br = new BufferedReader(new InputStreamReader(System.in));
    }
    @Override
    public String call() throws IOException {
        while(!Thread.currentThread().isInterrupted()){
            if (br.ready()){
                return br.readLine();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return null;
    }
}
