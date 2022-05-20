package src.Utilities;

public class DataValidation {
    public boolean isValidBarcode(String barcode) {
        if(barcode.length()!=6)
            return false;
        return true;
    }

    public String[] isValidDate(String date) {
        // check if date is valid
        String[] arrOfDate = date.split("-");
        if(arrOfDate.length!=3){
            return null;
        }
        if(arrOfDate[0].length()!=4||arrOfDate[1].length()!=2||arrOfDate[2].length()!=2){
            return null;
        }
        return arrOfDate;
    }

    public int isValidPrice(String price) {
        String[] arrOfPrice = price.split("\\.");
        if(arrOfPrice.length>2){
            return -1;
        }
        if(arrOfPrice[1].length()>2){
            return -1;
        }
        for(int i=arrOfPrice[1].length();i<2;i++){
            arrOfPrice[1]="0"+arrOfPrice[1];
        }
        String price_x100=arrOfPrice[0]+arrOfPrice[1];
        return Integer.parseInt(price_x100);
    }

    public boolean isValidPassword(String password) {
        boolean notNull=false;
        boolean lenChk=false;
        boolean containsLC=false;
        boolean containsUC=false;
        boolean containsDigit=false;

        if (password != null && password != "") {
            notNull=true;
        }
        else{
            System.out.println("Password cannot be empty!");
        }

        if(password.length()>=6) {
            lenChk=true;
        }
        else{
            System.out.println("Password must be at least 6 characters long!");
        }

        if(password.matches(".*[a-z].*")) {
            containsLC=true;
        }
        else{
            System.out.println("Password must contain at least one lowercase letter!");
        }

        if(password.matches(".*[A-Z].*")) {
            containsUC=true;
        }
        else{
            System.out.println("Password must contain at least one uppercase letter!");
        }

        if(password.matches(".*\\d.*")) {
            containsDigit=true;
        }
        else{
            System.out.println("Password must contain at least one digit!");
        }

        if(notNull && lenChk && containsLC && containsUC && containsDigit) {
            return true;
        }
        else {
            return false;
        }
    }
}
