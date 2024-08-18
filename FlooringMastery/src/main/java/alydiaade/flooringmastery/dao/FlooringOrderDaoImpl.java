package alydiaade.flooringmastery.dao;

import alydiaade.flooringmastery.model.Order;
import alydiaade.flooringmastery.model.Product;
import alydiaade.flooringmastery.model.Taxes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This is the flooring order DAO class which manages the files in memory 
 * returning all the relevant information require for the app to run
 * @author lydiaadejumo
 */
public class FlooringOrderDaoImpl implements FlooringOrderDao {

    private String DELIMITER = ",";
    private String fileLocation;
    private String taxFile;
    private String productFile;
    private Map<String, Taxes> allTaxRates = new HashMap<>();
    private Map<String, Product> allProducts = new HashMap<>();
    
    /**
     * The constructor utilised in the app for opening a file.
     */
    public FlooringOrderDaoImpl() {
        this.fileLocation = "src/main/resources/";
        this.taxFile = fileLocation + "Data/Taxes.txt";
        this.productFile = fileLocation + "Data/Products.txt";
        setUp();
    }

    /**
     * This is the constructor utilised for tests especially, so it can navigate
     * to the relevant files.
     * @param fileLocation 
     */
    public FlooringOrderDaoImpl(String fileLocation) {
        this.fileLocation = fileLocation;
        this.taxFile = fileLocation + "Data/Taxes.txt";
        this.productFile = fileLocation + "Data/Products.txt";
        setUp();
    }
    
    /**
     * Loads all the relevant files for the taxes and products
     */
    private void setUp(){
        try {
            loadTaxes();
            loadProducts();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringOrderDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<Order> getAllOrders(LocalDate date) throws NoSuchOrderFileException{
        List<Order> allOrdersOnDate = new ArrayList();
        String orderFile = getOrderFileName(date);
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(orderFile)));
            sc.nextLine();
            while (sc.hasNext()) {
                String currentLine = sc.nextLine();
                Order newOrder = unmarshallingOrder(currentLine);
                allOrdersOnDate.add(newOrder);
            }
        } catch (FileNotFoundException e) {
            throw new NoSuchOrderFileException("There are no orders on this date.");
        }
        return allOrdersOnDate;
    }
    
    /**
     * Sets up the file for importing data from the relevant orders folder
     * @param date - the order date/date for delivery
     * @return - returns the order file name in string form to be utilised in another method
     */
    private String getOrderFileName(LocalDate date) {
        String orderFileName = "Orders/Orders_" + date.format(DateTimeFormatter.ofPattern("MMddyyyy"))+ ".txt";
        return fileLocation + orderFileName;
    }

    @Override
    public Order addOrder(Order newOrder) throws OrderPersistenceException{
        String orderFile = getOrderFileName(newOrder.getOrderDate());
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(orderFile, true));
        } catch (IOException e) {
            throw new OrderPersistenceException("Could not add order to file", e);
        }
        //Checks if file is empty
        File myObj = new File(orderFile);
        if (myObj.length() == 0) {
            out.println("OrderNumber,OrderCreationDate,OrderDate,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,isActive");
        }
        String orderToString = marshallingOrder(newOrder);
        out.println(orderToString);
        out.flush();
        out.close();
        return newOrder;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNum) throws NoSuchOrderFileException, NoSuchOrderException {
        Order currentOrder = null;
        try {
            List<Order> allOrders = getAllOrders(date);
            for (Order eachOrder : allOrders) {
                if (eachOrder.getOrderNumber() == orderNum) {
                    currentOrder = eachOrder;
                }
            }
            if (currentOrder == null) {
                throw new NoSuchOrderException("There is no order number: " + 
                        orderNum + " recorded on this date (" +
                        date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + ")." );
            }
        } catch (NoSuchOrderFileException e) {
            throw new NoSuchOrderFileException("There are no orders on this date (" 
                    + date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                    + "), with the order number, " + orderNum);
        }
        return currentOrder;
    }

    @Override
    public void editOrder(Order orderToUpdate) throws NoSuchOrderFileException, OrderPersistenceException {
        List<Order> allOrders = getAllOrders(orderToUpdate.getOrderDate());
        Map<Integer, Order> orderWithOrderNums = new HashMap<>();
        for (Order eachOrder : allOrders) {
            orderWithOrderNums.put(eachOrder.getOrderNumber(), eachOrder);
        }
        orderWithOrderNums.put(orderToUpdate.getOrderNumber(), orderToUpdate);
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(getOrderFileName(orderToUpdate.getOrderDate()), false));
            out.close();
        } catch (IOException e) {
            throw new OrderPersistenceException("Could not update order in file", e);
        }
        List<Order> updatedOrderList = new ArrayList(orderWithOrderNums.values());
        for (Order updatedOrder : updatedOrderList) {
            addOrder(updatedOrder);
        }
    }

    @Override
    public List<Product> getProducts() {
        return new ArrayList(allProducts.values());
    }

    @Override
    public List<Taxes> getTaxes() {
        return new ArrayList(allTaxRates.values());
    }
    
    /**
     * Reads the state tax rates from the file. The file has been set-up in the 
     * form State,StateName,TaxRate
     * @throws FileNotFoundException - if the found cannot be found this error will be thrown.
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
     * Reads the products from the file. The file has been set-up in the 
     * form ProductType,CostPerSquareFoot,LaborCostPerSquareFoot
     * @throws FileNotFoundException - if the found cannot be found this error will be thrown.
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
    
    /**
     * This will unmarshall the data for the orders.
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
        currentOrder.setLaborCost(new BigDecimal(orderSeperatedDetails[11]));
        currentOrder.setMaterialCost(new BigDecimal(orderSeperatedDetails[10]));
        currentOrder.setTax(new BigDecimal(orderSeperatedDetails[12]));
        currentOrder.setTotal(new BigDecimal(orderSeperatedDetails[13]));
        currentOrder.setActiveOrder(Boolean.parseBoolean(orderSeperatedDetails[14]));
        return currentOrder;
    }
    
    /**
     * This will marshal the orders so it is in string form, that way it is can be stored in a text file.
     * @param currentOrder
     * @return the newly created order
     */
    private String marshallingOrder(Order currentOrder) {
        
        //File format -- OrderNumber,OrderCreationDate,OrderDate,CustomerName,State Abbreviation,TaxRate,ProductType,Area,
        //CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,isActive
        String orderToString = currentOrder.getOrderNumber() + DELIMITER;
        
        orderToString += currentOrder.getOrderCreation().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) + DELIMITER;
        
        orderToString += currentOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) + DELIMITER;
        
        orderToString += currentOrder.getCustomerName() + DELIMITER;
        
        orderToString += currentOrder.getTaxInfo().getState() + DELIMITER;
        
        orderToString += currentOrder.getTaxInfo().getTaxRate() + DELIMITER;
        
        orderToString += currentOrder.getProduct().getProductType() + DELIMITER;
        
        orderToString += currentOrder.getArea() + DELIMITER;
        
        orderToString += currentOrder.getProduct().getCostPerSquareFoot() + DELIMITER;
        
        orderToString += currentOrder.getProduct().getLaborCostPerSquareFoot() + DELIMITER;
        
        orderToString += currentOrder.getMaterialCost() + DELIMITER;
        
        orderToString += currentOrder.getLaborCost() + DELIMITER;
        
        orderToString += currentOrder.getTax() + DELIMITER;
        
        orderToString += currentOrder.getTotal() + DELIMITER;
        
        orderToString += currentOrder.isActiveOrder();
        
        return orderToString;
    }

    @Override
    public List<Order> exportOrders() throws NoSuchOrderFileException, OrderPersistenceException {
        Map<Integer, Order> allOrders = new HashMap<>();
        File directoryPath = new File(fileLocation + "Orders/");
        //List of all files and directories
        String[] contents = directoryPath.list();
        for (String content : contents) {
            String contentPath = "Orders/"+content;
            List<Order> ordersInFile = getExportOrdersFiles(contentPath);
            ordersInFile.forEach((eachOrder) -> {
                allOrders.put(eachOrder.getOrderNumber(), eachOrder);
            });
        }
        List<Order> activeOrders = allOrders.values().stream()
                .filter((p) -> p.isActiveOrder())
                .collect(Collectors.toList());
        
        String backupFileName = fileLocation + "Backup/DataExport.txt";
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(backupFileName, false));
        } catch (IOException e) {
            throw new OrderPersistenceException("Could not add order to file", e);
        }
        out.println("OrderNumber,OrderCreationDate,CustomerName,State,"
                + "TaxRate,ProductType,Area,CostPerSquareFoot,"
                + "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate");
        activeOrders.forEach((eachActiveOrder) -> {
            out.println(marshallingBackups(eachActiveOrder));
        });
        out.flush();
        out.close();
        return activeOrders;
    }
    
    /**
     * The method utilised by the export method to get the orders from their
     * relevant files, since the original getAllMethods method has a different
     * format for reading files.
     * @param contentPath - the path to the relevant file
     * @return - returns a list of orders from each order file
     * @throws NoSuchOrderFileException 
     */
    private List<Order> getExportOrdersFiles(String contentPath) throws NoSuchOrderFileException{
        List<Order> allOrdersOnDate = new ArrayList();
        String orderFile = fileLocation + contentPath;
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(orderFile)));
            sc.nextLine();
            while (sc.hasNext()) {
                String currentLine = sc.nextLine();
                Order newOrder = unmarshallingOrder(currentLine);
                allOrdersOnDate.add(newOrder);
            }
        } catch (FileNotFoundException e) {
            throw new NoSuchOrderFileException("There are no orders on this date.");
        }
        return allOrdersOnDate;
    }
    
    /**
     * This method is used to write the orders to the back up file since it is
     * formatted differently to the original orders file, so a different methods
     * was most suitable
     * @param currentOrder - the order that needs to be converted to string form
     * @return - returns the string format of the order
     */
    private String marshallingBackups(Order currentOrder) {
        
        //File format -- OrderNumber,OrderCreationDate,CustomerName,State Abbreviation,TaxRate,ProductType,Area,
        //CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,OrderDate
        String orderToString = currentOrder.getOrderNumber() + DELIMITER;
        
        orderToString += currentOrder.getOrderCreation().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) + DELIMITER;
        
        orderToString += currentOrder.getCustomerName() + DELIMITER;
        
        orderToString += currentOrder.getTaxInfo().getState() + DELIMITER;
        
        orderToString += currentOrder.getTaxInfo().getTaxRate() + DELIMITER;
        
        orderToString += currentOrder.getProduct().getProductType() + DELIMITER;
        
        orderToString += currentOrder.getArea() + DELIMITER;
        
        orderToString += currentOrder.getProduct().getCostPerSquareFoot() + DELIMITER;
        
        orderToString += currentOrder.getProduct().getLaborCostPerSquareFoot() + DELIMITER;
        
        orderToString += currentOrder.getMaterialCost() + DELIMITER;
        
        orderToString += currentOrder.getLaborCost() + DELIMITER;
        
        orderToString += currentOrder.getTax() + DELIMITER;
        
        orderToString += currentOrder.getTotal() + DELIMITER;
        
        orderToString += currentOrder.getOrderDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        
        return orderToString;
    }
}
