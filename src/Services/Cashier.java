package src.Services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import src.DAO.ICashierDAO;
import src.DAO.IProductDAO;
import src.DataType.Record;
import src.DataType.User;
import src.Factory.DAOFactory;
import src.Utilities.ConsoleColor;
import src.Utilities.DataValidation;
import src.Utilities.DatabaseConnection;
import src.Utilities.UIUX;

public class Cashier {
    private DatabaseConnection dbc; // 数据库连接类
    private ICashierDAO cashierDAO; // 由工厂统一提供的 dao 实现类对象
    private IProductDAO productDAO;
    private src.DataType.Product product;
    private src.DataType.Record record = new Record();
    private src.DataType.User user;
    private UIUX util = new UIUX();
    private DataValidation validate = new DataValidation();
    private ConsoleColor color = new ConsoleColor();
    private Scanner sc = null;

    // 从工厂类获取 dao 实现类对象
    public Cashier(User user, Scanner sc) {
        this.dbc = new DatabaseConnection(); // 连接数据库
        this.sc = sc;
        this.cashierDAO = DAOFactory.getICashierDAOInstance(this.dbc.getConnection());
        this.productDAO = DAOFactory.getIProductDAOInstance(this.dbc.getConnection());
        this.user = user;
    }

    protected void finalize() {
        this.dbc.close();
    }

    public void AddTransaction() {
        util.cls();
        String barcode = null;
        for (int i = 0; i == 0;) {
            System.out.println("Please input the barcode: ");
            barcode = sc.nextLine();

            while (!validate.isValidBarcode(barcode)) {
                color.printRedText("Invalid barcode!");
                color.printYellowText("A valid barcode contains exactly 6 digits!");
                System.out.println("Retry in 2 seconds...");
                util.delay(2000);
                util.cls();
                System.out.println("Please input the barcode: ");
                barcode = sc.nextLine();
            }

            try {
                product = productDAO.getByBarcode(barcode);
                i = 1;
            } catch (Exception e) {
                e.printStackTrace();
                color.printRedText("Product not found! Please try again.");
                util.delay(2000);
            }
        }
        record.setTransaction_id(Integer.toString(Integer.parseInt(cashierDAO.getLastTransactionID()) + 1));
        record.setBarcode(barcode);
        record.setProductName(product.getProductName());
        record.setPrice_x100(product.getPrice_x100());
        System.out.println("Please input the quantity: ");
        String qt = sc.nextLine();
        while (qt.equals("")) {
            qt = sc.nextLine();
        }
        record.setQuantity(Integer.parseInt(qt));
        record.setOperator(user.getUserName());
        record.setTime("now()");
        util.cls();
        try {
            cashierDAO.insert(record);
            color.printGreenText("Transaction added successfully!");

        } catch (Exception e) {
            color.printRedText("Error in adding transaction!");

        }
        util.delay(2000);
        return;
    }

    

    public boolean searchByDate() {
        util.cls();
        System.out.println("Please input the date: ");
        String date = sc.nextLine();
        if (date.equals("exit")) {
            return false;
        }
        util.cls();
        String arrOfDate[] = validate.isValidDate(date);
        while (arrOfDate == null) {
            System.out.println("Invalid date!");
            System.out.println("A valid date is in the format of yyyy-mm-dd!");
            System.out.println("Retry in 2 seconds...");
            util.delay(2000);
            util.cls();
            System.out.println("Please input the date: ");
            date = sc.nextLine();
            if (date.equals("exit")) {
                return false;
            }
            arrOfDate = validate.isValidDate(date);
        }
        List<src.DataType.Record> records = null;
        try {
            records = cashierDAO.query(
                    "select transaction_id,barcode,productName,price_x100,quantity,operator,time from salesrecords where time like '%"
                            + date + "%'");
        } catch (Exception e) {
            System.out.println("Error in querying transactions!");
            util.cls();
        }

        ListIterator<src.DataType.Record> it = records.listIterator();
        listDateQuery(arrOfDate, it);
        if (sc.nextLine().equals("")) {
            return true;
        } else
            return false;
    }

    public void dataExportMenu() {
        exportUI();
        int option = Integer.parseInt(sc.nextLine());
        while (true) {
            switch (option) {
            case 1:
                export2sheet();
                break;
            case 2:
                export2text();
                break;
            case 3:
                export2xml();
                break;
            case 4:
                return;
            }
            exportUI();
            option = Integer.parseInt(sc.nextLine());
        }
    }

    public void exportUI() {
        util.cls();

        System.out.println("====Data Export Menu====\n");
        System.out.println("1. Export to Excel");
        System.out.println("2. Export to Text");
        System.out.println("3. Export to XML");
        System.out.println("4. Back");
        System.out.println("\nPlease input your choice: ");
    }

    public void listDateQuery(String[] date, ListIterator<src.DataType.Record> it) {
        util.cls();
        // show menu
        System.out.println("Transaction records of " + date[0] + "-" + date[1] + "-" + date[2]);
        System.out.println("\nTransaction ID\tBarcode\tProduct Name\tPrice\tQuantity\tOperator\tTime");
        System.out.println("--------------\t-------\t------------\t-----\t--------\t--------\t----");
        src.DataType.Record record;
        int amount_x100 = 0;
        List<String> products = new ArrayList<String>();
        int total_quantity = 0;
        while (it.hasNext()) {
            record = it.next();
            if (!products.contains(record.getBarcode())) {
                products.add(record.getBarcode());
                total_quantity += record.getQuantity();
                amount_x100 += record.getQuantity() * record.getPrice_x100();
            }
            System.out.println(record.getTransaction_id() + "\t\t" + record.getBarcode() + "\t"
                    + record.getProductName() + "\t\t" + util.price2string(record.getPrice_x100()) + "\t"
                    + record.getQuantity() + "\t\t" + record.getOperator() + "\t\t" + record.getTime());
        }
        System.out.println(
                "---------------------------------------------------------------------------------------------------------");
        System.out.println("Total quantity: " + total_quantity + "\tTotal products: " + products.size()
                + "\tTotal amount: " + util.price2string(amount_x100));
        System.out.println("\nPress ENTER to continue searching or type anything to go back...");

    }

    public void export2sheet() {
        util.cls();

        List<src.DataType.Record> records = null;
        try {
            records = cashierDAO.query(
                    "select transaction_id,barcode,productName,price_x100,quantity,operator,time from salesrecords");
        } catch (Exception e) {
            System.out.println("Error in querying transactions!");
        }

        File file = null;
        try {
            file = new File("salesrecords.xls");
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WritableWorkbook workbook = null;
        WritableSheet sheet = null;
        try {
            workbook = Workbook.createWorkbook(file);
            sheet = workbook.createSheet("Records", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sheet.addCell(new Label(0, 0, "Transaction ID"));
            sheet.addCell(new Label(1, 0, "Barcode"));
            sheet.addCell(new Label(2, 0, "Name"));
            sheet.addCell(new Label(3, 0, "Price"));
            sheet.addCell(new Label(4, 0, "Quantity"));
            sheet.addCell(new Label(5, 0, "Operator"));
            sheet.addCell(new Label(6, 0, "Time"));

            int listLen = records.size();
            int successCount=0;
            for (int i = 1; i <= listLen; i++) {
                sheet.addCell(new Label(0, i, records.get(i - 1).getTransaction_id()));
                sheet.addCell(new Label(1, i, records.get(i - 1).getBarcode()));
                sheet.addCell(new Label(2, i, records.get(i - 1).getProductName()));
                sheet.addCell(new Label(3, i, util.price2string(records.get(i - 1).getPrice_x100())));
                sheet.addCell(new Label(4, i, Integer.toString(records.get(i - 1).getQuantity())));
                sheet.addCell(new Label(5, i, records.get(i - 1).getOperator()));
                sheet.addCell(new Label(6, i, records.get(i - 1).getTime()));
                successCount++;
            }
            workbook.write();
            workbook.close();
            color.printGreenText("Successfully exported "+successCount+" records to sheet!");

        } catch (Exception e) {
            e.printStackTrace();
            color.printRedText("Error in exporting to sheet!");
        }
        util.delay(2000);
        return;

    }

    public void export2text() {
        util.cls();
        List<src.DataType.Record> records = null;
        try {
            records = cashierDAO.query(
                    "select transaction_id,barcode,productName,price_x100,quantity,operator,time from salesrecords");
        } catch (Exception e) {
            System.out.println("Error in querying transactions!");
        }

        File file = new File("salesrecords.txt");
        try (FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                BufferedWriter writer = new BufferedWriter(osw)) {
            int listLen = records.size();
            int successCount=0;
            for (int i = 0; i < listLen; i++) {
                writer.append(records.get(i).getTransaction_id());
                writer.append("\n");
                writer.append(records.get(i).getBarcode());
                writer.append("\n");
                writer.append(records.get(i).getProductName());
                writer.append("\n");
                writer.append(Integer.toString(records.get(i).getPrice_x100()));
                writer.append("\n");
                writer.append(Integer.toString(records.get(i).getQuantity()));
                writer.append("\n");
                writer.append(records.get(i).getOperator());
                writer.append("\n");
                writer.append(records.get(i).getTime());
                writer.append("\n\n");
                successCount++;
            }
            writer.close();
            color.printGreenText("Successfully exported "+successCount+" records to text file!");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        util.delay(2000);
    }

    public void export2xml() {
        util.cls();
        List<src.DataType.Record> records = null;
        try {
            records = cashierDAO.query(
                    "select transaction_id,barcode,productName,price_x100,quantity,operator,time from salesrecords");
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File("salesrecords.xml")), format);
            Document document = DocumentHelper.createDocument();
            Element rootElement = document.addElement("class"); // 根节点
            int successCount = 0;
            for (Record record : records) {
                Element record_xml = rootElement.addElement("record"); // 子节点
                record_xml.addElement("transaction_id").addText(record.getTransaction_id());
                record_xml.addElement("barcode").addText(record.getBarcode());
                record_xml.addElement("product_name").addText(record.getProductName());
                record_xml.addElement("price_x100").addText(Integer.toString(record.getPrice_x100()));
                record_xml.addElement("quantity").addText(Integer.toString(record.getQuantity()));
                record_xml.addElement("operator").addText(record.getOperator());
                record_xml.addElement("time").addText(record.getTime());
                successCount++;
            }
            xmlWriter.write(document);
            color.printGreenText(successCount + " transaction records exported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            color.printRedText("Error when exporting to xml!");
        }
        util.delay(2000);
    }

    public void deleteTransactionRecord() {
        util.cls();

        if (!user.getRole().equals("admin")) {
            System.out.println(
                    "You are not authorized to access this page! Contact your administrator for more information.");
            System.out.println("Quitting in 2 seconds...");
            util.delay(2000);
            return;
        }

        String transaction_id = null;
        System.out.println("Please input the transaction ID: ");
        transaction_id = sc.nextLine();
        Record record = null;
        try {
            record = cashierDAO.getById(transaction_id);
            if (record == null) {
                color.printRedText("Transaction not found!");
            } else {
                cashierDAO.delete(transaction_id);
                color.printGreenText("Transaction deleted successfully!");
            }
            
        } catch (Exception e) {
            color.printRedText("Error in deleting transaction!");
            e.printStackTrace();
        }
        util.delay(2000);
        return;
    }

    // Returns xx.xx in string format

}
