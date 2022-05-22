package src.Utilities;

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
    public String price2string(int price_x100){
        return price_x100/100+"."+(price_x100%100<10?"0":"")+price_x100%100;
    }
    public void wait4enter(){
        System.out.println("Press Enter key to continue...");
        try
        {
            System.in.read();
        }  
        catch(Exception e)
        {}  
     }
    
}
