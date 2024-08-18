package alydiaade.flooringmastery.dao;

import alydiaade.flooringmastery.model.Order;
import alydiaade.flooringmastery.model.Product;
import alydiaade.flooringmastery.model.Taxes;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author lydiaadejumo
 */
public class FlooringOrderDaoImplTest {
    
    FlooringOrderDao testDao;
    
    public FlooringOrderDaoImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testDao = ctx.getBean("flooringDao", FlooringOrderDao.class);
    }
    
    @AfterAll
    public static void setUpClass() {
        File newOrder = new File("src/test/resources/Orders/Orders_12082020.txt");
        if (newOrder.exists()) {
            newOrder.delete();
        }
    }
    
    @BeforeEach
    public void setUp() {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter("src/test/resources/Orders/Orders_11042020.txt", false));
            out.println("OrderNumber,OrderCreationDate,OrderDate,CustomerName,"
                    + "State,TaxRate,ProductType,Area,CostPerSquareFoot,"
                    + "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,"
                    + "Total,isActive");
            out.println("1,06-01-2020,11-04-2020,Ada Lovelace,CA,25.00,Tile,"
                    + "249.00,3.50,4.15,871.50,1033.35,476.21,2381.06,true");
            out.println("2,06-25-2020,11-04-2020,Doctor Who,WA,9.25,Wood,243.00,"
                    + "5.15,4.75,1251.45,1154.25,216.51,2622.21,true");
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(FlooringOrderDaoImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Testing if the DAO can get all the orders in a test order file.
     */
    @Test
    public void testGetAllOrders() {
        //ARRANGE
        LocalDate orderDate = LocalDate.parse("11-04-2020", 
                java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        
        //ACT
        try {
            List<Order> allOrders = testDao.getAllOrders(orderDate);
            //ASSESS
            //assertEquals()
        } catch (NoSuchOrderFileException ex) {
            fail("The file exists and shouldn't throw an error.");
        }
        
    }
    
    /**
     * Testing if the DAO can get all the orders in a test order file but 
     * should throw an error because the file does not exist
     */
    @Test
    public void testGetAllOrdersNoOrderFile() {
        //ARRANGE
        LocalDate orderDate = LocalDate.parse("10-04-2020", 
                java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        
        //ACT & ASSESS
        try {
            List<Order> allOrders = testDao.getAllOrders(orderDate);
            fail("The file does not exist and should throw an error.");
        } catch (NoSuchOrderFileException ex) { 
        }
    }
    
    /**
     * Testing if the DAO cen get an order from orders file.
     */
    @Test
    public void testGetOrder() {
        //ARRANGE
        LocalDate orderDate = LocalDate.parse("2020-11-04");
        int orderNum = 2;
        
        //ACT & ASSESS
        try {
            Order currentOrder = testDao.getOrder(orderDate, orderNum);
            
        } catch (NoSuchOrderFileException | NoSuchOrderException ex) { 
            fail("The file exists and shouldn't throw an error.");
        }
    }
    
    /**
     * Testing if the DAO cen get an order from orders file but 
     * should throw an error because the file does not exist.
     */
    @Test
    public void testGetOrderNoOrderFile() {
        //ARRANGE
        LocalDate orderDate = LocalDate.now();
        int orderNum = 1;
        
        //ACT & ASSESS
        try {
            Order currentOrder = testDao.getOrder(orderDate, orderNum);
            fail("The file does not exist and should throw the NoSuchOrderFileException");
        } catch (NoSuchOrderFileException ex) { 
            
        } catch (NoSuchOrderException ex) { 
            fail("The file does not exist and should throw the NoSuchOrderFileException");
        }
    }
    
    /**
     * Testing if the DAO cen get an order from orders file but 
     * should throw an error because the file does not have that order number.
     */
    @Test
    public void testGetOrderNoOrderNumber() {
        //ARRANGE
        LocalDate orderDate = LocalDate.parse("11-04-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        int orderNum = 3;
        
        //ACT & ASSESS
        try {
            Order currentOrder = testDao.getOrder(orderDate, orderNum);
            fail("The file does exist, but not that order number and should throw the NoSuchOrderException");
        } catch (NoSuchOrderFileException ex) { 
            fail("The file does exist, but not that order number and should throw the NoSuchOrderException");
        } catch (NoSuchOrderException ex) { 
        }
    }
    
    /**
     * Testing if the DAO can add a new order to a respective existing file;
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if there is an error finding the file.
     */
    @Test
    public void testAddOrder() throws OrderPersistenceException {
        //ARRANGE
        List<Product> products = testDao.getProducts();
        List<Taxes> taxes = testDao.getTaxes();
        String personName = "Sharon Tony";
        Taxes personTax = taxes.get(1);
        Product personProduct = products.get(2);
        LocalDate orderDate = LocalDate.parse("11-04-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        LocalDate orderCreated = LocalDate.parse("07-05-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        BigDecimal personArea = new BigDecimal(200);
        int orderNum = 3;
        
        Order newOrder = new Order(orderCreated, orderDate, personName, 
                personTax, personProduct,personArea);
        newOrder.setOrderNumber(orderNum);
        
        /*
        All the costs have to be calculated in this layer since the service 
        layer would usually carry out the calculate cost method for this
        */
        //Material Cost
        BigDecimal materialCost = newOrder.getArea().multiply(newOrder.getProduct().getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        //Labour Cost
        BigDecimal laborCost = newOrder.getArea().multiply(newOrder.getProduct().getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        //Tax
        BigDecimal addMCandLC = materialCost.add(laborCost);
        BigDecimal taxRatePercentage = newOrder.getTaxInfo().getTaxRate().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        BigDecimal tax = addMCandLC.multiply(taxRatePercentage).setScale(2, RoundingMode.HALF_UP);
        //Total
        BigDecimal total = materialCost.add(laborCost).add(tax).setScale(2, RoundingMode.HALF_UP);
        
        newOrder.setMaterialCost(materialCost);
        newOrder.setLaborCost(laborCost);
        newOrder.setTax(tax);
        newOrder.setTotal(total);
        
        //ACT
        testDao.addOrder(newOrder);
        try {
            Order retrievedOrder = testDao.getOrder(orderDate, orderNum);
            
            //ASSESS
            assertEquals(personName, retrievedOrder.getCustomerName(), "The "
                    + "person name should be: " + personName);
            assertEquals(personTax.getState(), 
                    retrievedOrder.getTaxInfo().getState(), "The state "
                            + "abbreviation should be: " + personTax.getState());
            assertEquals(personProduct.getProductType(), 
                    retrievedOrder.getProduct().getProductType(), "The product "
                            + "type should be: " + personProduct.getProductType());
            assertEquals(orderCreated, retrievedOrder.getOrderCreation(), "The "
                    + "order creation should be: " + orderCreated);
            assertEquals(newOrder.getTotal(), retrievedOrder.getTotal(), 
                    "The total for the order should be: " + newOrder.getTotal());
            assertEquals(newOrder.getTax(), retrievedOrder.getTax(), 
                    "The tax amount for the order should be: " + newOrder.getTax());
        } catch (NoSuchOrderFileException | NoSuchOrderException e) {
            fail("The file exists and shouldn't throw an error.");
        }
    }
    
    /**
     * Testing if the DAO can add a new order to a new file and give out the same results
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if there is an error finding the file.
     */
    @Test
    public void testAddOrderNewFile() throws OrderPersistenceException {
        //ARRANGE
        List<Product> products = testDao.getProducts();
        List<Taxes> taxes = testDao.getTaxes();
        String personName = "Gordon Brown";
        Taxes personTax = taxes.get(0);
        Product personProduct = products.get(0);
        LocalDate orderDate = LocalDate.parse("12-08-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        LocalDate orderCreated = LocalDate.parse("08-05-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        BigDecimal personArea = new BigDecimal(100);
        int orderNum = 4;
        
        Order newOrder = new Order(orderCreated, orderDate, personName, 
                personTax, personProduct,personArea);
        newOrder.setOrderNumber(orderNum);
        
        /*
        All the costs have to be calculated in this layer since the service 
        layer would usually carry out the calculate cost method for this
        */
        //Material Cost
        BigDecimal materialCost = newOrder.getArea().multiply(newOrder.getProduct().getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        //Labour Cost
        BigDecimal laborCost = newOrder.getArea().multiply(newOrder.getProduct().getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        //Tax
        BigDecimal addMCandLC = materialCost.add(laborCost);
        BigDecimal taxRatePercentage = newOrder.getTaxInfo().getTaxRate().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        BigDecimal tax = addMCandLC.multiply(taxRatePercentage).setScale(2, RoundingMode.HALF_UP);
        //Total
        BigDecimal total = materialCost.add(laborCost).add(tax).setScale(2, RoundingMode.HALF_UP);
        
        newOrder.setMaterialCost(materialCost);
        newOrder.setLaborCost(laborCost);
        newOrder.setTax(tax);
        newOrder.setTotal(total);
        
        //ACT
        testDao.addOrder(newOrder);
        try {
            Order retrievedOrder = testDao.getOrder(orderDate, orderNum);
            
            //ASSESS
            assertEquals(personName, retrievedOrder.getCustomerName(), "Person"
                    + " name should be: " + personName);
            assertEquals(personTax.getState(), 
                    retrievedOrder.getTaxInfo().getState(),"State should be: " 
                            + personTax.getState());
            assertEquals(personProduct.getProductType(), 
                    retrievedOrder.getProduct().getProductType(), "Product type"
                            + " should be: " + personProduct.getProductType());
            assertEquals(orderCreated, retrievedOrder.getOrderCreation(), " The "
                    + "order creation date should be: " + orderCreated);
            assertEquals(newOrder.getTotal(), retrievedOrder.getTotal(), 
                    "The total should be equal for both at " + newOrder.getTotal());
        } catch (NoSuchOrderFileException | NoSuchOrderException e) {
            fail("The file should exist and shouldn't throw an error.");
        }
    }
    
    /**
     * Testing if the DAO cen get edit an order from order file.
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if there
     * is an error whilst finding the order file
     */
    @Test
    public void testEditOrder() throws OrderPersistenceException {
        //ARRANGE
        List<Product> products = testDao.getProducts();
        String personName = "Beyonce Knowles";
        Product personProduct = products.get(1);
        LocalDate orderDate = LocalDate.parse("11-04-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        BigDecimal personArea = new BigDecimal(300);
        int orderNum = 2;
        
        
        try {
            //ACT
            Order currentOrder = testDao.getOrder(orderDate, orderNum);
            Order oldDetails = currentOrder;
            currentOrder.setArea(personArea);
            currentOrder.setProduct(personProduct);
            currentOrder.setCustomerName(personName);
            testDao.editOrder(currentOrder);
            Order retrievedOrder = testDao.getOrder(orderDate, orderNum);
            
            //ASSESS
            assertEquals(currentOrder.getArea(), retrievedOrder.getArea(), "The "
                    + "order that is retrieved should have the new order details,"
                    + " area: " + currentOrder.getArea());
            assertEquals(personProduct.getProductType(), 
                    retrievedOrder.getProduct().getProductType(), "The order that"
                            + " is retrieved should have the new order details, "
                            + "product type: " + personProduct.getProductType());
            
        } catch (NoSuchOrderFileException | NoSuchOrderException e) {
            fail("The file exists and shouldn't throw an error.");
        }
    }
    
    /**
     * Testing if the DAO can remove an order, so changing the file to inactive
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if there
     * is an error whilst finding the order file
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderFileException - if a 
     * particular file order cannot be found
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderException -  would check
     * if the order exists in that file, which should exist.
     */
    @Test
    public void testRemoveOrder() throws OrderPersistenceException, NoSuchOrderFileException, NoSuchOrderException {
        //ARRANGE
        LocalDate orderDate = LocalDate.parse("11-04-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        int orderNum = 2;
        
        //ACT
        Order currentOrder = testDao.getOrder(orderDate, orderNum);
        currentOrder.setActiveOrder(false);
        testDao.editOrder(currentOrder);
        Order orderRetrieved = testDao.getOrder(orderDate, orderNum);
        
        //ASSESS
        assertFalse(orderRetrieved.isActiveOrder(), "The retrieved order should be inactive.");
        
    }
    
    /**
     * Testing if the DAO can get the export files, so the files that are inactive
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if there
     * is an error whilst finding the order file
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderFileException - if a 
     * particular file order cannot be found
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderException - if an order 
     * cannot be found in the file
     */
    @Test
    public void testExportOrder() throws OrderPersistenceException, NoSuchOrderFileException, NoSuchOrderException {
        //ARRANGE
        LocalDate orderDate = LocalDate.parse("11-04-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        int orderNum = 2;
        
        //ACT
        List<Order> exportedOrders = testDao.exportOrders();
        
        //ASSESS
        assertEquals(3, exportedOrders.size(), "There should be 3 orders in total");
        assertEquals(1, exportedOrders.get(0).getOrderNumber(), "The order returned should be order number 1");
        assertEquals(2, exportedOrders.get(1).getOrderNumber(), "The order returned should be order number 2");
        assertEquals(4, exportedOrders.get(2).getOrderNumber(), "The order returned should be order number 4");
        //Each order should be active
        for (Order eachOrder : exportedOrders) {
            assertTrue(eachOrder.isActiveOrder(), "Every order should be active.");
        }
        
        //ACT - we are now going to deactivate/remove an order
        Order currentOrder = testDao.getOrder(orderDate, orderNum);
        currentOrder.setActiveOrder(false);
        testDao.editOrder(currentOrder);
        Order orderRetrieved = testDao.getOrder(orderDate, orderNum);
        List<Order> newlyExportedOrders = testDao.exportOrders();
        
        //ASSESS
        assertFalse(orderRetrieved.isActiveOrder(), "Order number 2 should be inactive");
        assertEquals(2, newlyExportedOrders.size(), "There should be 3 orders in total");
        assertEquals(1, newlyExportedOrders.get(0).getOrderNumber(), "The order returned should be order number 1");
        assertEquals(4, newlyExportedOrders.get(1).getOrderNumber(), "The order returned should be order number 4");
        
        //Each order should be active
        for (Order eachOrder : exportedOrders) {
            assertTrue(eachOrder.isActiveOrder(), "Every order should be active.");
        }
        
    }
}
