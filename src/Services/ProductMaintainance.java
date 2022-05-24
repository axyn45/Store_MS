package src.Services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import src.DAO.IProductDAO;
import src.DataType.Product;
import src.DataType.User;
import src.Factory.DAOFactory;
import src.Utilities.DataValidation;
import src.Utilities.DatabaseConnection;
import src.Utilities.UIUX;
import src.Utilities.ConsoleColor;

public class ProductMaintainance {
    private DatabaseConnection dbc; // 数据库连接类
    private src.DataType.User user;
    private IProductDAO productDAO;
    private UIUX util = new UIUX();
    private DataValidation validate = new DataValidation();
    private ConsoleColor color = new ConsoleColor();
    private Scanner sc = null;

    public ProductMaintainance(User user, Scanner sc) {
        this.dbc = new DatabaseConnection(); // 连接数据库
        this.sc = sc;
        productDAO = DAOFactory.getIProductDAOInstance(this.dbc.getConnection());
        this.user = user;
    }

    protected void finalize() {
        this.dbc.close();
    }

    public void menu() {
        util.cls();
        if (!user.getRole().equals("admin")) {
            System.out.println(
                    "You are not authorized to access this page! Contact your administrator for more information.");
            System.out.println("Quitting in 2 seconds...");
            util.delay(2000);
            return;
        }
        MaintainanceUI();
        int choice = Integer.parseInt(sc.nextLine());

        while (true) {
            switch (choice) {
            case 1:
                importFromExcel();
                break;

            case 2:
                importFromText();
                break;

            case 3:
                importFromXML();
                break;

            case 4:
                while (manualAdd());
                break;

            case 5:
                export2sheet();
                break;
            case 6:
                export2text();
                break;
            case 7:
                export2xml();
                break;
            case 8:
                updateProduct();
                break;
            case 9:
                while (searchProduct());
                break;
            case 0:
                util.cls();
                return;

            default:
                System.out.println("Invalid choice!");
                break;
            }
            MaintainanceUI();
            sc = new Scanner(System.in);
            choice = sc.nextInt();
        }
    }

    

    public void importFromExcel() {
        List<Product> products = new ArrayList<Product>();

        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new File("products.xls"));
            Sheet sheet = workbook.getSheet(0);
            // System.out.println(sheet.getRows());
            for (int i = 0; i < sheet.getRows(); i++) {
                Product product = new Product();
                product.setBarcode(sheet.getCell(0, i).getContents());
                product.setProductName(sheet.getCell(1, i).getContents());
                product.setPrice_x100(validate.isValidPrice(sheet.getCell(2, i).getContents()));
                product.setSupplier(sheet.getCell(3, i).getContents());
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int listLen = products.size();
        int successCount = 0;
        for (int i = 0; i < listLen; i++) {
            try {
                productDAO.insert(products.get(i));
                successCount++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (successCount != 0) {
            color.printGreenText(successCount + " products added successfully!");
        }
        if (successCount != listLen) {
            color.printRedText(Integer.toString(listLen - successCount) + " products failed to be added!");
        }
        util.delay(2000);
    }

    public void importFromText() {
        File file = null;
        Scanner fsc = null;
        Product product = new Product();
        try {
            file = new File("products.txt");
            fsc = new Scanner(file);
        } catch (Exception e) {
            e.printStackTrace();
            fsc.close();
            return;
        }

        String buffer = null;
        int successCount = 0;
        try {
            while (fsc.hasNextLine()) {
                buffer = fsc.nextLine();
                while (buffer == null || buffer.equals("")) {
                    buffer = fsc.nextLine();
                }
                product.setBarcode(buffer);
                product.setProductName(fsc.nextLine());
                product.setPrice_x100(validate.isValidPrice(fsc.nextLine()));
                product.setSupplier(fsc.nextLine());
                productDAO.insert(product);
                successCount++;
            }
            color.printGreenText(successCount + " products added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            color.printRedText("Encountered a format issue in file! Quitting...");

        }
        fsc.close();
        util.delay(2000);
    }

    public void importFromXML() {
        util.cls();
        try {
            File file = new File("products.xml");
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();
            List<Element> list = rootElement.elements("product");
            int successCount=0;
            for (Element element : list) {
                Product product = new Product();
                product.setBarcode(element.elementTextTrim("barcode"));
                product.setProductName(element.elementTextTrim("name"));
                product.setPrice_x100(Integer.parseInt(element.elementTextTrim("price_x100")));
                product.setSupplier(element.elementTextTrim("supplier"));
                productDAO.insert(product);
                successCount++;
            }
            color.printGreenText(successCount + " products added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            color.printRedText("Encountered an issue in reading file! Quitting...");
        }
        util.delay(2000);
    }

    public boolean manualAdd() {
        Product product = new Product();

        String barcode = manualAdd_getBarcode();
        if (barcode == null) {
            return false;
        }
        product.setBarcode(barcode);

        String name = manualAdd_getName();
        if (name == null) {
            return false;
        }
        product.setProductName(name);

        int price_x100 = manualAdd_getPrice_x100();
        if (price_x100 == -1) {
            return false;
        }
        product.setPrice_x100(price_x100);

        String supplier = manualAdd_getSupplier();
        if (supplier.equals("exit")) {
            return false;
        }
        product.setSupplier(supplier);

        util.cls();
        try {
            productDAO.insert(product);
            color.printCyanText("Barcode: " + product.getBarcode());
            color.printCyanText("Name: " + product.getProductName());
            color.printCyanText("Price: " + util.price2string(product.getPrice_x100()));
            color.printCyanText("Supplier: " + product.getSupplier());
            color.printGreenText("Successfully added!");
            product.toString();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("\nPress ENTER to add another product or type anything to go back...");
        if (!sc.nextLine().equals("")) {
            util.cls();

            return false;
        }
        util.cls();

        return true;

        // Trur for continue, False for go back
    }

    public void export2sheet(){
        util.cls();

        List<Product> products = null;
        try {
            products = productDAO.query(
                    "select barcode,productName,price_x100,supplier from product");
        } catch (Exception e) {
            System.out.println("Error in querying products!");
        }

        File file = null;
        try {
            file = new File("products_export.xls");
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        try {
            workbook = Workbook.createWorkbook(file);
            sheet = workbook.createSheet("Products", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sheet.addCell(new Label(0, 0, "Barcode"));
            sheet.addCell(new Label(1, 0, "Name"));
            sheet.addCell(new Label(2, 0, "Price"));
            sheet.addCell(new Label(3, 0, "Supplier"));

            int listLen = products.size();
            for (int i = 1; i <= listLen; i++) {
                sheet.addCell(new Label(0, i, products.get(i - 1).getBarcode()));
                sheet.addCell(new Label(1, i, products.get(i - 1).getProductName()));
                sheet.addCell(new Label(2, i, util.price2string(products.get(i - 1).getPrice_x100())));
                sheet.addCell(new Label(3, i, products.get(i - 1).getSupplier()));
            }
            workbook.write();
            workbook.close();
            color.printGreenText("Successfully exported to sheet!");
        } catch (Exception e) {
            e.printStackTrace();
            color.printRedText("Error in exporting to sheet!");
        }
        util.delay(2000);
        return;
    }

    public void export2text(){
        util.cls();
        List<Product> products = null;
        try {
            products = productDAO.query(
                    "select barcode,productName,price_x100,supplier from product");
        } catch (Exception e) {
            System.out.println("Error in querying products!");
        }

        File file=new File("products_export.txt");
        try (FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw)) {
            int listLen = products.size();
            for (int i = 0; i < listLen; i++) {
                writer.append(products.get(i).getBarcode());
                writer.append("\n");
                writer.append(products.get(i).getProductName());
                writer.append("\n");
                writer.append(Integer.toString(products.get(i).getPrice_x100()));
                writer.append("\n");
                writer.append(products.get(i).getSupplier());

                writer.append("\n\n");
            }
            writer.close();
            color.printGreenText("Successfully exported to text file!");
        } catch (IOException e) {
            color.printRedText("An error occurred.");
            e.printStackTrace();
        }
        
        util.delay(2000);
    }

    public void export2xml(){
        util.cls();
        List<Product> products = null;
        try{
            products = productDAO.query(
                "select barcode,productName,price_x100,supplier from product");
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File("products_export.xml")), format);
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("class"); // 根节点
        int successCount=0;
        for(Product product:products){
            Element record_xml = rootElement.addElement("product"); // 子节点
            record_xml.addElement("Barcode").addText(product.getBarcode());
            record_xml.addElement("product_name").addText(product.getProductName());
            record_xml.addElement("price_x100").addText(Integer.toString(product.getPrice_x100()));
            record_xml.addElement("supplier").addText(product.getSupplier());
            successCount++;
        }
        xmlWriter.write(document);
        color.printGreenText(successCount + " products exported successfully!");
        }catch(Exception e){
            e.printStackTrace();
            color.printRedText("Error in exporting to xml!");
        }
        util.delay(2000);
    }

    

    

    public void updateProduct(){
        util.cls();

        if (!user.getRole().equals("admin")) {
            System.out.println(
                    "You are not authorized to access this page! Contact your administrator for more information.");
            System.out.println("Quitting in 2 seconds...");
            util.delay(2000);
            return;
        }

        System.out.println("Please enter the product barcode you want to update: ");
        String barcode = sc.nextLine();
        Product product=null;
        
        try{
            product = productDAO.getByBarcode(barcode);
            if(product==null){
                color.printRedText("Product not found!");
            }else{
                String display_barcode="";
                String display_name="";
                String display_price="";
                String display_supplier="";
                display_barcode="Product Barcode: "+product.getBarcode();
                display_name="Product Name: "+product.getProductName();
                display_price="Product Price: "+util.price2string(product.getPrice_x100());
                display_supplier="Supplier: "+product.getSupplier();
                boolean isNameChanged=false;
                boolean isPriceChanged=false;
                boolean isSupplierChanged=false;

                util.cls();
                System.out.println(display_barcode);
                System.out.println(display_name);
                System.out.println(display_price);
                System.out.println(display_supplier);
                color.printCyanText("Tip: Type nothing to leave the field unchanged.");
                System.out.println("Product Name: ");
                String name = sc.nextLine();
                if(!name.equals("")){
                    product.setProductName(name);
                    display_name=display_name+" -> "+name;
                    isNameChanged=true;
                }

                util.cls();
                System.out.println(display_barcode);
                if(!isNameChanged) System.out.println(display_name);else color.printYellowText(display_name);
                System.out.println(display_price);
                System.out.println(display_supplier);
                color.printCyanText("Tip: Type nothing to leave the field unchanged.");
                System.out.println("Price: ");
                String price = sc.nextLine();
                if(!price.equals("")){
                    product.setPrice_x100(validate.isValidPrice(price));
                    display_price=display_price+" -> "+util.price2string(product.getPrice_x100());
                    isPriceChanged=true;
                }

                util.cls();
                System.out.println(display_barcode);
                if(!isNameChanged) System.out.println(display_name);else color.printYellowText(display_name);
                if(!isPriceChanged) System.out.println(display_price);else color.printYellowText(display_price);
                System.out.println(display_supplier);
                color.printCyanText("Tip: Type nothing to leave the field unchanged.");
                System.out.println("Supplier: ");
                String supplier = sc.nextLine();
                if(!supplier.equals("")){
                    product.setSupplier(supplier);
                    display_supplier=display_supplier+" -> "+supplier;
                    isSupplierChanged=true;
                }
                util.cls();
                if(productDAO.update(product)){
                    System.out.println(display_barcode);
                    if(!isNameChanged) System.out.println(display_name);else color.printYellowText(display_name);
                    if(!isPriceChanged) System.out.println(display_price);else color.printYellowText(display_price);
                    if(!isSupplierChanged) System.out.println(display_supplier);else color.printYellowText(display_supplier);
                    color.printGreenText("Product updated successfully!");
                }else{
                    color.printRedText("Product update failed!");
                }

            }
        }catch(Exception e){
            color.printRedText("Product not found!");
            e.printStackTrace();
        }
        util.delay(2000);
        return;
    }

    public boolean searchProduct() {
        System.out.println("Tips: Try type relevant information to search products.");
        System.out.println("      Type \"exit\" to quit searching.");
        System.out.println("Search: ");
        String key = sc.nextLine();

        String search_query;

        List<Product> products;
        if (key.equals("exit")) {

            return false;
        }
        util.cls();
        search_query = "SELECT barcode,productName,price_x100,supplier FROM product WHERE barcode LIKE '%" + key
                + "%' OR productName LIKE '%" + key + "%' OR price_x100 LIKE '%" + key + "%' OR supplier LIKE '%" + key
                + "%'";

        products = null;
        try {
            products = productDAO.query(search_query);
            listProducts(products, key);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No such product.");
        }

        System.out.println("\nPress ENTER to search again or type anything to go back.");
        if (!sc.nextLine().equals("")) {
            util.cls();

            return false;
        }
        util.cls();

        return true;
        // True for continue, False for go back
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public void MaintainanceUI() {
        // menu
        util.cls();
        System.out.println("====Product Maintainance====");
        System.out.println("1. Import from Excel sheet");
        System.out.println("2. Import from text");
        System.out.println("3. Import from XML");
        System.out.println("4. Manually Add Product");
        System.out.println("5. Export to Excel sheet");
        System.out.println("6. Export to text");
        System.out.println("7. Export to XML");
        System.out.println("8. Update Product Information");
        System.out.println("9. Search Product");
        System.out.println("0. Back");
        System.out.println("\nPlease input your choice: ");

    }

    public void listProducts(List<Product> products, String key) {
        util.cls();
        if (products.size() != 0) {
            System.out.println("Found " + products.size() + " products about \"" + key + "\".");
            System.out.println("====Product List====");

            System.out.println("No.\tBarcode\t\tName\t\tPrice\tSupplier");
        } else {
            System.out.println("No such product.");
        }

        for (int i = 1; i <= products.size(); i++) {
            System.out.println(Integer.toString(i) + "\t" + products.get(i - 1).getBarcode() + "\t\t"
                    + products.get(i - 1).getProductName() + "\t\t"
                    + util.price2string(products.get(i - 1).getPrice_x100()) + "\t"
                    + products.get(i - 1).getSupplier());
        }
    }

    public void manualAdd_showWizard() {
        util.cls();
        System.out.println("Product Adding Wizard\n");
    }

    public String manualAdd_getBarcode() {
        manualAdd_showWizard();
        System.out.println("Product Barcode: ");
        String barcode = sc.nextLine();
        if (barcode.equals("exit")) {

            return null;
        }
        while (!validate.isValidBarcode(barcode)) {
            color.printRedText("Invalid barcode!");
            color.printYellowText("A valid barcode contains exactly 6 digits!");
            System.out.println("Retry in 2 seconds...");
            util.delay(2000);
            manualAdd_showWizard();
            System.out.println("Product Barcode: ");
            barcode = sc.nextLine();
            if (barcode.equals("exit")) {

                return null;
            }
        }

        return barcode;
    }

    public String manualAdd_getName() {
        manualAdd_showWizard();
        System.out.println("Product Name: ");
        String name = sc.nextLine();
        if (name.equals("exit")) {

            return null;
        }
        while (name == null) {
            color.printRedText("Product name can't be empty!");
            System.out.println("Retry in 2 seconds...");
            util.delay(2000);
            manualAdd_showWizard();
            System.out.println("Product Name: ");
            name = sc.nextLine();
            if (name.equals("exit")) {

                return null;
            }
        }

        return name;
    }

    public int manualAdd_getPrice_x100() {
        manualAdd_showWizard();
        System.out.println("Product Price: ");
        String price = sc.nextLine();

        if (price.equals("exit")) {

            return -1;
        }
        int price_x100 = validate.isValidPrice(price);
        while (price_x100 == -1) {
            color.printRedText("Invalid price!");
            System.out.println("Retry in 2 seconds...");
            util.delay(2000);
            manualAdd_showWizard();
            System.out.println("Product Price: ");
            price = sc.nextLine();
            if (price.equals("exit")) {

                return -1;
            }
            price_x100 = validate.isValidPrice(price);
        }

        return price_x100;
    }

    public String manualAdd_getSupplier() {
        manualAdd_showWizard();
        System.out.println("Product Supplier: ");

        return sc.nextLine();
    }
}
