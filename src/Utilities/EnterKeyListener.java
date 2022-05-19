package src.Utilities;

public class EnterKeyListener
{ 
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