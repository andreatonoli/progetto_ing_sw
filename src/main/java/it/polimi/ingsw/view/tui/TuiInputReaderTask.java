package it.polimi.ingsw.view.tui;
import java.io.*;
import java.util.concurrent.Callable;

/**
 * Class that reads the input from the user and sends them to a thread in the TUI.
 */
public class TuiInputReaderTask implements Callable<String>{
    public String call() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        do {
            try {
                // wait until we have data to complete a readLine()
                while (!br.ready()) {
                    Thread.sleep(200);
                }
                input = br.readLine();
            } catch (InterruptedException e) {
                return null;
            }
        } while ("".equals(input));
        return input;
    }
}
