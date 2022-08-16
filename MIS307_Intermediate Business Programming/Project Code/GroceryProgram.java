import java.io.IOException;
import java.util.Scanner;

/**
 * The Main Grocery Program
 *
 * @Author: Yen Vo
 */
public class GroceryProgram {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("##############################################################");
        System.out.println("########             Grocery Program                      ####");
        System.out.println("##############################################################");

        // ----- Initialize Database Connection -----------
        SimpleDataSource.init("database.properties");

        // ------ Initialize Database Schema --------------
        DatabaseManager.createDBTables();

        // ------ Initialize Some Data for Grocery database-----
        // Data for PRODUCTS
        ProductManager.initializeData();
        // Data for CUSTOMERS
        CustomerManager.initializeData();
        // Data for Receipts and Receipt details
        SaleManager.initializeSaleData();

        // ------- Run Program ----------------------------
        runProgram();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Select from the following options");
        System.out.println("(1) Print all Products");
        System.out.println("(2) Print all Customers");
        System.out.println("(3) Print all Receipts");
        System.out.println("(4) Add new Customer");
        System.out.println("(5) Add new Product");
        System.out.println("(6) Add new Receipt");
        System.out.println("(7) Search Customer by name");
        System.out.println("(8) Search Product by name");
        System.out.println("(9) Search Receipt by Receipt Id");
        System.out.println("(10) Print Report Sale by Date");
        System.out.println("(11) Print Report Sale by Month");
        System.out.println("(0) Quit");
        System.out.print("Select menu: ");
    }

    private static void runProgram() {
        String select = null;
        Scanner console = new Scanner(System.in);
        boolean isQuit = false; // this value becomes true when the user selects 0 option
        while (!isQuit) {
            printMenu();
            select = console.next();
            if (select.equals("1")) {
                ProductManager.printAllProducts();
            } else if (select.equals("2")) {
                CustomerManager.printAllCustomers();
            } else if (select.equals("3")) {
                ReportManager.printAllReceipts();
            } else if (select.equals("4")) {
                CustomerManager.addCustomer();
            } else if (select.equals("5")) {
                ProductManager.addProduct();
            } else if (select.equals("6")) {
                SaleManager.addReceipt();
            } else if (select.equals("7")) {
                CustomerManager.searchCustomerByName();
            } else if (select.equals("8")) {
                ProductManager.searchProductByName();
            } else if (select.equals("9")) {
                SaleManager.searchReceiptById();
            } else if (select.equals("10")) {
                ReportManager.reportSaleByDate();
            } else if (select.equals("11")) {
                ReportManager.reportSaleByMonth();
            } else if (select.equals("0")) {
                isQuit = true;
            } else {
                System.out.println("Invalid select option. Please select a valid option above.");
            }
        }
        console.close();
        System.out.println("---- Grocery program exit----");
    }
}
