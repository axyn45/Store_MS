package src;

public class Record {
    private String transaction_id;
    private String barcode;
    private String productName;
    private double price;
    private int quantity;
    private String operator;
    private String time;

    public Record() {
        super();
    }

    public Record(String transaction_id, String barcode, String productName, double price, int quantity, String operator, String time) {
        super();
        this.transaction_id = transaction_id;
        this.barcode = barcode;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.operator = operator;
        this.time = time;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
}
