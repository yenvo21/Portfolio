import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Class manages Sale - Initialize receipt data, Add receipt, search receipt
 *
 * @Author: Yen Vo
 */
public class SaleManager {

    /**
     * Initialize data for RECEIPT and RECEIPT_DETAIL
     */
    public static void initializeSaleData() {
        // Initialize data for RECEIPT
        initializeReceiptData();
        // Initialize data for RECEIPT_DETAIL
        initializeReceiptDetailsData();
    }

    private static void initializeReceiptData() {
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            // Reading " Customer.csv"
            Scanner in = new Scanner(new File("Receipts.csv"));
            in.nextLine(); // skip header (column names)
            // reading the csv file line by line
            while (in.hasNextLine()) {
                String line = in.nextLine();
                // CSV file separates values by commas. The line is partitioned into multiple values
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    String receiptId = fields[0];
                    String purchaseDate = fields[1];
                    String customerId = fields[2];

                    // Prepare the SQL to insert the RECEIPT
                    String query = createQueryInsertReceipt(receiptId, purchaseDate, customerId);
                    stat.execute(query);
                }
            }
            in.close();
        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static void initializeReceiptDetailsData() {
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            // Reading " Customer.csv"
            Scanner in = new Scanner(new File("ReceiptDetails.csv"));
            in.nextLine(); // skip header (column names)
            // reading the csv file line by line
            while (in.hasNextLine()) {
                String line = in.nextLine();
                // CSV file separates values by commas. The line is partitioned into multiple values
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    String receiptId = fields[0];
                    String productId = fields[1];
                    String quantity = fields[2];
                    String unitPrice = fields[3];

                    // Create Insert SQL to insert the RECEIPT_DETAIL
                    String query = createQueryInsertReceiptDetail(receiptId, productId, quantity, unitPrice);
                    stat.execute(query);
                }
            }
            in.close();
        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void addReceipt() {
        Scanner console = new Scanner(System.in);
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            System.out.println("Enter New Receipt: ");
            // Ask for Receipt inputs
            System.out.print("Receipt ID: ");
            String receiptId = console.nextLine();;
            System.out.print("Purchase Date MM/DD/YYYY: ");
            String purchaseDate = console.nextLine();;
            System.out.print("Customer ID: ");
            String customerId = console.nextLine();;

            String sqlReceipt = createQueryInsertReceipt(receiptId, purchaseDate, customerId);
            stat.execute(sqlReceipt);

            // Ask for Receipt Details
            System.out.println("Enter Receipt Details for ReceiptId[" + receiptId + "]: ");
            boolean enterDetails = true;
            int count = 0;
            while(enterDetails) {
                count++;
                addReceiptDetail(receiptId, stat, count);

                System.out.print("Enter another Receipt Detail ?('Y'=continue / else=Quit): ");
                String cont = console.nextLine();
                if(!cont.equalsIgnoreCase("Y")) {
                    enterDetails = false;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void addReceiptDetail(String receiptId, Statement statement, int detailCount) throws SQLException {
        Scanner console = new Scanner(System.in);
        System.out.println("Enter New Receipt Detail[" + detailCount + "]: ");
        // Ask for Receipt Details inputs
        System.out.println("Product ID: ");
        String productId = console.nextLine();
        System.out.println("Quantity: ");
        String quantity = console.nextLine();
        System.out.println("Unit Price: ");
        String unitPrice = console.nextLine();
        String sqlReceipt = createQueryInsertReceiptDetail(receiptId, productId, quantity, unitPrice);
        statement.execute(sqlReceipt);
    }

    public static void searchReceiptById() {
        Scanner console = new Scanner(System.in);
        System.out.print("Enter Receipt ID: ");

        // Ask for enter Receipt ID
        String receiptId = console.nextLine();
        ReportManager.printReceiptById(receiptId);
    }

    private static String createQueryInsertReceipt(String receiptId,
                                                   String purchaseDate,
                                                   String customerId) {

        // Prepare the SQL statement in String format to insert the RECEIPT
        String query = "INSERT INTO RECEIPT" +
                "(RECEIPT_ID, PURCHASE_DATE, CUSTOMER_ID)" +
                " VALUES (" +
                        "'" + receiptId + "'," +
                        "'" + purchaseDate + "'," +
                        "'" + customerId  + "'" +
                ")";
        return query;
    }

    private static String createQueryInsertReceiptDetail(
            String receiptId,
            String productId,
            String quantity,
            String unitPrice) {

        // Prepare the SQL statement in String format with quotes to insert the RECEIPT_DETAIL
        String query = "INSERT INTO RECEIPT_DETAIL" +
                "(RECEIPT_ID, PRODUCT_ID, QUANTITY, UNIT_PRICE)" +
                " VALUES (" +
                        "'" + receiptId + "'," +
                        "'" + productId + "'," +
                        quantity + "," +
                        unitPrice +
                ")";
        return query;
    }
}
