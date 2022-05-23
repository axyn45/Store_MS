package src.Utilities;

public class DataValidation {
    private ConsoleColor color=new ConsoleColor();

    public boolean isValidBarcode(String barcode) {
        if (barcode.length() != 6)
            return false;
        return true;
    }

    public String[] isValidDate(String date) {
        // check if date is valid
        String[] arrOfDate = date.split("-");
        if (arrOfDate.length != 3) {
            return null;
        }
        if (arrOfDate[0].length() != 4 || arrOfDate[1].length() != 2 || arrOfDate[2].length() != 2) {
            return null;
        }
        return arrOfDate;
    }

    public int isValidPrice(String price) {
        String[] dotSub = price.split("\\.|,");
        if (dotSub.length == 2) {
            if (dotSub[1].length() <= 2) {
                String price_x100 = dotSub[0] + (dotSub[1].length()==2?dotSub[1]:dotSub[1]+"0");
                return Integer.parseInt(price_x100);
            } else {
                return -1;
            }
        } else if(dotSub.length==1){
            return Integer.parseInt(dotSub[0]+"00");
        }
        else return -1;
    }

    public boolean isValidPassword(String password) {
        boolean notNull = false;
        boolean lenChk = false;
        boolean containsLC = false;
        boolean containsUC = false;
        boolean containsDigit = false;

        if (password != null && password != "") {
            notNull = true;
        } else {
            color.printRedText("Password cannot be empty!");
        }

        if (password.length() >= 6) {
            lenChk = true;
        } else {
            color.printRedText("Password must be at least 6 characters long!");
        }

        if (password.matches(".*[a-z].*")) {
            containsLC = true;
        } else {
            color.printRedText("Password must contain at least one lowercase letter!");
        }

        if (password.matches(".*[A-Z].*")) {
            containsUC = true;
        } else {
            color.printRedText("Password must contain at least one uppercase letter!");
        }

        if (password.matches(".*\\d.*")) {
            containsDigit = true;
        } else {
            color.printRedText("Password must contain at least one digit!");
        }

        if (notNull && lenChk && containsLC && containsUC && containsDigit) {
            return true;
        } else {
            return false;
        }
    }
}
