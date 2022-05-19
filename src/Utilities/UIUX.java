package src.Utilities;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UIUX {
    public void cls(){
        System.out.print("\033[2J");
        System.out.flush();
    }
    public void delay(int time_ms) {
        try {
            Thread.sleep(time_ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
}
