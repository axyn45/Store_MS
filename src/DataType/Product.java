package src.DataType;

public class Product {
    private String barcode;
    private String productName;
    private int price_x100;
    private String supplier;

    public Product() {
        super();
    }

    public Product(String barcode, String productName, int price_x100, String supplier) {
        super();
        this.barcode = barcode;
        this.productName = productName;
        this.price_x100 = price_x100;
        this.supplier = supplier;
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

    public String getSupplier() {
        return supplier;
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

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String toString() {
        return "barcode=" + barcode + "\nproductName=" + productName + "\nprice_x100=" + price_x100
                + "\nsupplier=" + supplier + "\n";
    }
}
