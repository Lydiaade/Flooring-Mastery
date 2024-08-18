package alydiaade.flooringmastery.dao;

import alydiaade.flooringmastery.model.Order;
import alydiaade.flooringmastery.model.Product;
import alydiaade.flooringmastery.model.Taxes;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class acts as a stub for the DAO when testing the service layer, to allow
 * test to run effectively without impacting the orders stored on record.
 * @author lydiaadejumo
 */
public class FlooringOrderDaoStub implements FlooringOrderDao {

    private String DELIMITER = ",";
    private String fileLocation;
    private String taxFile;
    private String productFile;
    private Map<String, Taxes> allTaxRates = new HashMap<>();
    private Map<String, Product> allProducts = new HashMap<>();
    private Map<Integer, Order> allOrders = new HashMap<>();
    

    /**
     * The DAO stub constructor locating and loading from the relevant
     * testing text files.
     */
    public FlooringOrderDaoStub() {
        this.fileLocation = "src/test/resources/";
        this.taxFile = fileLocation + "Data/Taxes.txt";
        this.productFile = fileLocation + "Data/Products.txt";
        try {
            loadTaxes();
            loadProducts();
            loadOrders();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringOrderDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This will load all the files required to successfully test the service 
     * layer methods, which are saved to a map function.
     * @throws FileNotFoundException - if the order file cannot be found this 
     * exception will be thrown
     */
    private void loadOrders() throws FileNotFoundException {
        String fileName = fileLocation + "OrdersServiceTest/Orders_serviceTest.txt";
        Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName)));
        sc.nextLine();
        while (sc.hasNext()) {
            String currentLine = sc.nextLine();
            Order newOrder = unmarshallingOrder(currentLine);
            allOrders.put(newOrder.getOrderNumber(), newOrder);
        }
    }
    
    /**
     * This method is utilised to read all the files within the order file in its
     * format and convert them into order objects.
     * @param orderDetails
     * @return 
     */
    private Order unmarshallingOrder(String orderDetails) {
        String[] orderSeperatedDetails = orderDetails.split(DELIMITER);
        //LocalDate OrderDate, String CustomerName, Taxes taxRate, Product Product, BigDecimal Area
        
        //File format -- OrderNumber,OrderCreationDate,OrderDate,CustomerName,State,TaxRate,ProductType,Area,
        //CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,isActive
        String customerName = orderSeperatedDetails[3];
        LocalDate orderCreation = LocalDate.parse(orderSeperatedDetails[1],java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        LocalDate orderDate = LocalDate.parse(orderSeperatedDetails[2],java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        Taxes taxRate = allTaxRates.get(orderSeperatedDetails[4]);
        Product product = allProducts.get(orderSeperatedDetails[6]);
        BigDecimal area = new BigDecimal(orderSeperatedDetails[7]);
        
        Order currentOrder = new Order(orderCreation,orderDate, customerName, taxRate, product, area);
        currentOrder.setOrderNumber(Integer.parseInt(orderSeperatedDetails[0]));
        currentOrder.setOrderNumber(Integer.parseInt(orderSeperatedDetails[0]));
        currentOrder.setLaborCost(new BigDecimal(orderSeperatedDetails[11]));
        currentOrder.setMaterialCost(new BigDecimal(orderSeperatedDetails[10]));
        currentOrder.setTax(new BigDecimal(orderSeperatedDetails[12]));
        currentOrder.setTotal(new BigDecimal(orderSeperatedDetails[13]));
        currentOrder.setActiveOrder(Boolean.parseBoolean(orderSeperatedDetails[14]));
        return currentOrder;
    }
    
    /**
     * This method reads the taxes file and adds all the taxes to the taxes map
     * which can later be utilised.
     * @throws FileNotFoundException  - if the taxes file cannot be found this 
     * exception will be thrown
     */
    private void loadTaxes() throws FileNotFoundException{
        Scanner sc = new Scanner(new BufferedReader(new FileReader(taxFile)));
        sc.nextLine();
        while (sc.hasNext()) {
            String currentLine = sc.nextLine();
            String[] aStatesTax = currentLine.split(DELIMITER);
            Taxes newTax = new Taxes(aStatesTax[0], aStatesTax[1], new BigDecimal(aStatesTax[2]));
            allTaxRates.put(aStatesTax[0], newTax);
        }
    }
    
    /**
     * This method reads the product file and adds all the products to the 
     * products map which can later be utilised.
     * @throws FileNotFoundException  - if the product file cannot be found this 
     * exception will be thrown
     */
    private void loadProducts() throws FileNotFoundException{
        Scanner sc = new Scanner(new BufferedReader(new FileReader(productFile)));
        sc.nextLine();
        while (sc.hasNext()) {
            String currentLine = sc.nextLine();
            String[] aProduct = currentLine.split(DELIMITER);
            Product newProduct = new Product(aProduct[0], new BigDecimal(aProduct[1]), new BigDecimal(aProduct[2]));
            allProducts.put(aProduct[0], newProduct);
        }
    }
    
    @Override
    public List<Order> getAllOrders(LocalDate date) throws NoSuchOrderFileException {
        List<Order> dateOrders = allOrders.values().stream()
                .filter((p) -> p.getOrderDate().isEqual(date))
                .collect(Collectors.toList());
        return dateOrders;
    }

    @Override
    public Order addOrder(Order newOrder) throws OrderPersistenceException {
        allOrders.put(newOrder.getOrderNumber(), newOrder);
        return newOrder;
    }

    @Override
    public List<Order> exportOrders() throws NoSuchOrderFileException, OrderPersistenceException {
        List<Order> activeOrders = allOrders.values().stream()
                .filter((p) -> p.isActiveOrder())
                .collect(Collectors.toList());
        return activeOrders;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNum) throws NoSuchOrderFileException, NoSuchOrderException {
        return allOrders.get(orderNum);
    }

    @Override
    public void editOrder(Order orderToUpdate) throws NoSuchOrderFileException, OrderPersistenceException {
        allOrders.put(orderToUpdate.getOrderNumber(), orderToUpdate);
    }

    @Override
    public List<Product> getProducts() {
        return new ArrayList(allProducts.values());
    }

    @Override
    public List<Taxes> getTaxes() {
        return new ArrayList(allTaxRates.values());
    }
    
}
