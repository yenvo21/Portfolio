import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Class manages Products - Add Product, search product, report products
 *
 * @Author: Yen Vo
 */
public class ProductManager {

    /**
     * Initialize data for PRODUCTS
     *
     * @Author: Yen Vo
     */
    public static void initializeData() {
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {

            // Reading " product.csv"
            Scanner in = new Scanner(new File("Products.csv"));
            in.nextLine(); // skip header (column names)
            // reading the csv file line by line
            while (in.hasNextLine()) {
                String line = in.nextLine();
                // CSV file separates values by commas. The line is partitioned into multiple values
                String[] fields = line.split(",");
                if(fields.length == 6) {

                    String productID = fields[0];
                    String productName = fields[1];
                    String description = fields[2];
                    String onHand = fields[3];
                    String location = fields[4];
                    String price = fields[5];

                    // Create SQL to insert the PRODUCTS
                    String query = createQueryInsertProduct(productID, productName, description, onHand, location, price);
                    stat.execute(query);
                }
            }
            in.close();
        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Print all PRODUCTS
     */
    public static void printAllProducts() {
        String headerFormat = "%-10s %-15s %-70s %-10s %-10s %-10s\n";
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {

            // Prepare the SQL statement to select all products
            String query = "SELECT *  FROM PRODUCT";
            ResultSet result = stat.executeQuery(query);
            // Print headers
            System.out.printf(headerFormat
                    , "ProductId", "Name", "Description", "On hand", "Location", "Price");
            // Print header line
            System.out.printf(headerFormat
                    , "---------", "----", "-----------", "-------", "--------", "-----");
            while (result.next()) {
                String productID = result.getString("PRODUCT_ID");
                String productName = result.getString("PRODUCT_NAME");
                String description = result.getString("DESCRIPTION");
                String onHand = result.getString("ON_HAND");
                String location = result.getString("LOCATION");
                String price = result.getString("PRICE");

                System.out.printf(headerFormat
                        , productID, productName, description, onHand, location, price);
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Add PRODUCT
     */
    public static void addProduct() {
        Scanner console = new Scanner(System.in);
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            System.out.println("Enter New Product:\n");

            //  enter Product
            System.out.print("Product ID: ");
            String productID = console.nextLine();
            System.out.print("Product Name: ");
            String productName = console.nextLine();
            System.out.print("Description: ");
            String description = console.nextLine();
            System.out.print("On Hand : ");
            String onHand = console.nextLine();
            System.out.print("Location: ");
            String location = console.nextLine();
            System.out.print("Price: ");
            String price = console.nextLine();


            // Create insert query for PRODUCT
            String query = createQueryInsertProduct(
                    productID,
                    productName,
                    description,
                    onHand,
                    location,
                    price);

            stat.execute(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void searchProductByName() {
        String headerFormat = "%-10s %-15s %-70s %-10s %-10s %-10s\n";
        System.out.println("********Search Product by name********");
        Scanner console = new Scanner(System.in);
        try (Connection conn = SimpleDataSource.getConnection(); Statement stat = conn.createStatement()) {
            System.out.print("Enter Search Name: ");
            String searchName = console.nextLine();
            // Prepare the SQL statement to select all products
            String query = "SELECT *  FROM PRODUCT WHERE PRODUCT_NAME LIKE '%" + searchName+ "%'";
            ResultSet result = stat.executeQuery(query);
            // Print headers
            System.out.printf(headerFormat
                    , "ProductId", "Name", "Description", "On hand", "Location", "Price");
            // Print header line
            System.out.printf(headerFormat
                    , "---------", "----", "-----------", "-------", "--------", "-----");
            boolean found = false;
            while (result.next()) {
                found = true;
                String productID = result.getString("PRODUCT_ID");
                String productName = result.getString("PRODUCT_NAME");
                String description = result.getString("DESCRIPTION");
                String onHand = result.getString("ON_HAND");
                String location = result.getString("LOCATION");
                String price = result.getString("PRICE");

                System.out.printf(headerFormat
                        , productID, productName, description, onHand, location, price);
            }
            if(!found){
                System.out.println("Product name ["+searchName+"] is not found ");
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static String createQueryInsertProduct(String productID,
                                                    String productName,
                                                    String description,
                                                    String onHand,
                                                    String location,
                                                    String price) {

        // Prepare the SQL statement in String format to insert the PRODUCTS
        String query = "INSERT INTO PRODUCT" +
                "(PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, ON_HAND, LOCATION, PRICE)" +
                " VALUES (" +
                        "'" + productID + "'," +
                        "'" +  productName + "'," +
                        "'" +  description + "'," +
                        onHand + "," +
                        "'" +  location + "'," +
                        price +
                ")";
        return query;
    }
}
