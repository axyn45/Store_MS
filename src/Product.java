package src;

public class Product {
    private String barcode;
    private String productName;
    private int price_x100;
    private String supplyer;

    public Product() {
        super();
    }

    public Product(String barcode, String productName, int price_x100, String supplyer) {
        super();
        this.barcode = barcode;
        this.productName = productName;
        this.price_x100 = price_x100;
        this.supplyer = supplyer;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice_x100() {
        return price_x100;
    }

    public String getSupplyer() {
        return supplyer;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice_x100(int price_x100) {
        this.price_x100 = price_x100;
    }

    public void setSupplyer(String supplyer) {
        this.supplyer = supplyer;
    }
}
