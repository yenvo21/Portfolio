import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Class manage all sale reports - print receipts, search receipt, Report sale by date, report sale by month
 *
 * @Author: Yen Vo
 */
public class ReportManager {

    /**
     * Print all RECEIPTS
     */
    public static void printAllReceipts() {
        String headerFormat = "%-10s %-10s %-12s %-12s %-14s " +  // Receipt space
                                "%-10s %-20s %-5s %-10s %-12s\n"; // Details spaces

        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            // Prepare the SQL statement to select all Receipts
            String query = "SELECT " +
                                "RECEIPT_ID," +
                                "RECEIPT.CUSTOMER_ID," +
                                "FIRST_NAME," +
                                "LAST_NAME," +
                                "PURCHASE_DATE" +
                            " FROM RECEIPT " +
                                "INNER JOIN CUSTOMER ON RECEIPT.CUSTOMER_ID=CUSTOMER.CUSTOMER_ID";
            ResultSet result = stat.executeQuery(query);
            // Print headers
            System.out.printf(headerFormat
                    , "ReceiptId", "CustomerId", "FirstName", "LastName", "PurchaseDate"
                    , "ProductId", "ProductName", "Qty", "Price", "Total");

            // Print header line
            System.out.printf(headerFormat
                    , "---------", "----------", "---------", "--------", "------------"
                    , "---------", "--------------", "---", "-----", "-------");

            while (result.next()) {
                String receiptId = result.getString("RECEIPT_ID");
                String customerId = result.getString("CUSTOMER_ID");
                String firstName = result.getString("FIRST_NAME");
                String lastName = result.getString("LAST_NAME");
                String purchaseDate = result.getString("PURCHASE_DATE");
                // Print Receipt
                System.out.printf(headerFormat
                        , receiptId, customerId, firstName, lastName, purchaseDate
                        ,"","","","","");// leave empty for details space

                // Print Receipt Details
                printReceiptDetails(receiptId);
            }
            result.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void printReceiptById(String receiptId) {
        String headerFormat = "%-10s %-10s %-12s %-12s %-14s " +  // Receipt space
                "%-10s %-20s %-5s %-10s %-12s\n";                 // Details spaces

        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            // Prepare the SQL statement to search Receipt by ID
            String query = "SELECT " +
                    "RECEIPT_ID," +
                    "RECEIPT.CUSTOMER_ID," +
                    "FIRST_NAME," +
                    "LAST_NAME," +
                    "PURCHASE_DATE" +
                    " FROM RECEIPT " +
                    "INNER JOIN CUSTOMER ON RECEIPT.CUSTOMER_ID=CUSTOMER.CUSTOMER_ID " +
                    "WHERE RECEIPT_ID = '" + receiptId + "'";
            ResultSet result = stat.executeQuery(query);
            // Print headers
            System.out.printf(headerFormat
                    , "ReceiptId", "CustomerId", "FirstName", "LastName", "PurchaseDate"
                    , "ProductId", "ProductName", "Qty", "Price", "Total");

            // Print header line
            System.out.printf(headerFormat
                    , "---------", "----------", "---------", "--------", "------------"
                    , "---------", "--------------", "---", "-----", "-------");

            if (result.next()) {
                String customerId = result.getString("CUSTOMER_ID");
                String firstName = result.getString("FIRST_NAME");
                String lastName = result.getString("LAST_NAME");
                String purchaseDate = result.getString("PURCHASE_DATE");
                // Print Receipt
                System.out.printf(headerFormat
                        , receiptId, customerId, firstName, lastName, purchaseDate
                        ,"","","","","");// leave empty for details space

                // Print Receipt Details
                printReceiptDetails(receiptId);
            } else {
                System.out.println("----Receipt ID [" + receiptId + "] is not found----");
            }
            result.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void reportSaleByDate() {

        String headerFormat = "%-10s %-10s %-12s %-12s %-14s %-12s\n";
        String dataFormat = "%-10s %-10s %-12s %-12s %-14s %-10.2f\n";
        System.out.println("*******Report Sale by Date*******");
        Scanner console = new Scanner(System.in);
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            System.out.print("Enter a Day for sale report (MM/DD/YYYY): ");
            String mmddyyStr = console.nextLine();
            String[] monthDayYear = mmddyyStr.split("/");

            // Create Receipt report for given date
            String query = "SELECT " +
                    "RECEIPT.RECEIPT_ID," +
                    "RECEIPT.CUSTOMER_ID," +
                    "FIRST_NAME," +
                    "LAST_NAME," +
                    "PURCHASE_DATE," +
                    "SUM(QUANTITY*UNIT_PRICE) AS SALE " +
                    "FROM RECEIPT " +
                    "INNER JOIN CUSTOMER ON RECEIPT.CUSTOMER_ID=CUSTOMER.CUSTOMER_ID " +
                    "INNER JOIN RECEIPT_DETAIL ON RECEIPT.RECEIPT_ID=RECEIPT_DETAIL.RECEIPT_ID " +
                    "WHERE MONTH(PURCHASE_DATE)="+ monthDayYear[0] +
                    " AND DAY(PURCHASE_DATE)="+ monthDayYear[1] +
                    " AND YEAR(PURCHASE_DATE)="+ monthDayYear[2] +
                    " GROUP BY RECEIPT.RECEIPT_ID, RECEIPT.CUSTOMER_ID, FIRST_NAME, LAST_NAME, PURCHASE_DATE";

            ResultSet result = stat.executeQuery(query);
            // Print headers
            System.out.printf(headerFormat
                    , "ReceiptId", "CustomerId", "FirstName", "LastName", "PurchaseDate", "Sale");

            // Print header line
            System.out.printf(headerFormat
                    , "---------", "----------", "---------", "--------", "------------", "---------");
            double total = 0.0;
            while (result.next()) {
                String receiptId = result.getString("RECEIPT_ID");
                String customerId = result.getString("CUSTOMER_ID");
                String firstName = result.getString("FIRST_NAME");
                String lastName = result.getString("LAST_NAME");
                String purchaseDate = result.getString("PURCHASE_DATE");
                Double sale = result.getDouble("SALE");
                // Print Sale data report
                System.out.printf(dataFormat
                        , receiptId, customerId, firstName, lastName, purchaseDate, sale);
                total += sale;
            }
            result.close();
            // Print footer line
            System.out.printf(headerFormat
                    , "", "", "", "", "------------", "---------");
            System.out.printf(dataFormat
                    , "", "", "", "", "Total:", total);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void reportSaleByMonth() {
        String headerFormat = "%-10s %-10s %-12s %-12s %-14s %-12s\n";
        String dataFormat = "%-10s %-10s %-12s %-12s %-14s %-10.2f\n";
        System.out.println("*******Report Sale by Month*******");
        Scanner console = new Scanner(System.in);
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            System.out.print("Enter a Month for sale report (MM/YYYY): ");
            String mmyyStr = console.nextLine();
            String[] monthYears = mmyyStr.split("/");

            // Create Receipt report for given month
            String query = "SELECT " +
                            "RECEIPT.RECEIPT_ID," +
                            "RECEIPT.CUSTOMER_ID," +
                            "FIRST_NAME," +
                            "LAST_NAME," +
                            "PURCHASE_DATE," +
                            "SUM(QUANTITY*UNIT_PRICE) AS SALE " +
                    "FROM RECEIPT " +
                    "INNER JOIN CUSTOMER ON RECEIPT.CUSTOMER_ID=CUSTOMER.CUSTOMER_ID " +
                    "INNER JOIN RECEIPT_DETAIL ON RECEIPT.RECEIPT_ID=RECEIPT_DETAIL.RECEIPT_ID " +
                    "WHERE MONTH(PURCHASE_DATE)="+ monthYears[0] +" AND YEAR(PURCHASE_DATE)="+ monthYears[1] +
                    " GROUP BY RECEIPT.RECEIPT_ID, RECEIPT.CUSTOMER_ID, FIRST_NAME, LAST_NAME, PURCHASE_DATE";

            ResultSet result = stat.executeQuery(query);
            // Print headers
            System.out.printf(headerFormat
                    , "ReceiptId", "CustomerId", "FirstName", "LastName", "PurchaseDate", "Sale");

            // Print header line
            System.out.printf(headerFormat
                    , "---------", "----------", "---------", "--------", "------------", "---------");
            double total = 0.0;
            while (result.next()) {
                String receiptId = result.getString("RECEIPT_ID");
                String customerId = result.getString("CUSTOMER_ID");
                String firstName = result.getString("FIRST_NAME");
                String lastName = result.getString("LAST_NAME");
                String purchaseDate = result.getString("PURCHASE_DATE");
                Double sale = result.getDouble("SALE");
                // Print Sale data report
                System.out.printf(dataFormat
                        , receiptId, customerId, firstName, lastName, purchaseDate, sale);
                total += sale;
            }
            result.close();
            // Print footer line
            System.out.printf(headerFormat
                    , "", "", "", "", "------------", "---------");
            System.out.printf(dataFormat
                    , "", "", "", "", "Total:", total);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Print Receipt Details for given ReceiptId
     * @param receiptId
     */
    private static void printReceiptDetails(String receiptId) {
        String detailsFormat = "%-10s %-10s %-12s %-12s %-14s " + // Receipt spaces
                "%-10s %-20s %-5d %-10.2f %-10.2f\n";             // Details spaces
        String footerFormat = "%-10s %-10s %-12s %-12s %-14s " +
                "%-10s %-20s %-5s %-10s %-12s\n";
        String grandFormat = "%-10s %-10s %-12s %-12s %-14s " +
                "%-10s %-20s %-5s %-10s %-10.2f\n";

        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            // Prepare the SQL statement to select all products
            String query = "SELECT " +
                                "RECEIPT_DETAIL.PRODUCT_ID," +
                                "PRODUCT_NAME," +
                                "QUANTITY," +
                                "UNIT_PRICE " +
                            "FROM RECEIPT_DETAIL " +
                            "INNER JOIN PRODUCT ON RECEIPT_DETAIL.PRODUCT_ID=PRODUCT.PRODUCT_ID " +
                            "WHERE RECEIPT_ID='" + receiptId + "'";
            ResultSet result = stat.executeQuery(query);

            Double grandTotal = 0.0;
            while (result.next()) {
                String productId = result.getString("PRODUCT_ID");
                String productName = result.getString("PRODUCT_NAME");
                Integer quantity = result.getInt("QUANTITY");
                Double unitPrice = result.getDouble("UNIT_PRICE");
                Double total = quantity * unitPrice;
                grandTotal += total;
                System.out.printf(detailsFormat
                        , "", "", "", "", "" // Receipt spaces
                        , productId, productName, quantity, unitPrice, total);
            }
            result.close();
            // Print footer line
            System.out.printf(footerFormat
                    , "", "", "", "", ""
                    , "", "--------------", "", "", "-------");
            // Print Grand total
            System.out.printf(grandFormat
                            , "", "", "", "", ""
                            , "", "Grand Total:", "", "", grandTotal);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
