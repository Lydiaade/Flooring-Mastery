/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alydiaade.flooringmastery.service;

import alydiaade.flooringmastery.dao.NoSuchOrderException;
import alydiaade.flooringmastery.dao.NoSuchOrderFileException;
import alydiaade.flooringmastery.dao.OrderPersistenceException;
import alydiaade.flooringmastery.model.Order;
import alydiaade.flooringmastery.model.Product;
import alydiaade.flooringmastery.model.Taxes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author lydiaadejumo
 */
public class FlooringServiceLayerImplTest {
    
    private FlooringServiceLayer service;
    
    public FlooringServiceLayerImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = ctx.getBean("serviceLayer", FlooringServiceLayer.class);
    }

    /*
    The orders stored in the dao stub order file.
    OrderNumber,OrderCreationDate,OrderDate,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total,isActive
    1,06-01-2020,11-04-2020,Ada Lovelace,CA,25.00,Tile,249.00,3.50,4.15,871.50,1033.35,476.21,2381.06,true
    2,06-20-2020,11-04-2020,Doctor Who,WA,9.25,Wood,243.00,5.15,4.75,1251.45,1154.25,216.51,2622.21,false
    3,06-20-2020,11-04-2020,Josh Hosepipe,TX,4.45,Laminate,200,1.75,2.10,350.00,420.00,30.80,800.80,true
    4,06-25-2020,07-20-2020,Ursula James,WA,9.25,Carpet,400,2.25,2.10,900.00,840.00,156.60,1896.60,true
    5,06-25-2020,07-20-2020,Jack Trowler,WA,9.25,Tile,150,3.50,4.15,525.00,622.50,103.28,1250.78,true
    
    Above are the orders within the serviceLayer test file, since the daoStub 
    won't be writing to a file, there is no need to reset the respective files.
    */
    
    
    /**
     * Test ability to easily return the product list stored in the DAO
     */
    @Test
    public void testGetProductList() {
        //ARRANGE
        /*
        The contents of the product list:
        Carpet,2.25,2.10
        Laminate,1.75,2.10
        Tile,3.50,4.15
        Wood,5.15,4.75
        */
        //ACT
        List<Product> allProducts = service.getProductList();
        
        //ASSESS
        assertEquals(4, allProducts.size(),"The length of the list should be 4");
    }
    
    /**
     * Test ability to easily return the tax list stored in the DAO
     */
    @Test
    public void testGetTaxList() {
        //ARRANGE
        /*
        The contents of the tax list:
        TX,Texas,4.45
        WA,Washington,9.25
        KY,Kentucky,6.00
        CA,Calfornia,25.00
        */
        //ACT
        List<Taxes> allTaxes = service.getTaxList();
        
        //ASSESS
        assertEquals(4, allTaxes.size(), "The length of the list should be 4");
        assertEquals("TX", allTaxes.get(0).getState(), "The state should be TX");
        assertEquals("CA", allTaxes.get(3).getState(), "The state should be CA");
        assertEquals("Washington", allTaxes.get(1).getStateName(), "The state name should be Washington");
        assertEquals("Kentucky", allTaxes.get(2).getStateName(), "The state name should be Kentucky");
        assertEquals(new BigDecimal("4.45"), allTaxes.get(0).getTaxRate(), "The tax rate should be 4.45");
        assertEquals(new BigDecimal("6.00"), allTaxes.get(2).getTaxRate(), "The tax rate should be 6.00");
    }
    
    /**
     * Test ability to easily return the current order number
     */
    @Test
    public void testGetOrderNumber() {
        //ARRANGE & ACT
        int nextOrderNumber = service.getOrderNumber();
        
        //ASSESS
        assertEquals(6,nextOrderNumber, "Should be one greater than the length of the list.");
    }
    
    /**
     * Test ability to easily return the orders in a order file
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderFileException
     */
    @Test
    public void testGetOrders() throws NoSuchOrderFileException {
        //ARRANGE
        LocalDate orderDate1 = LocalDate.parse("2020-11-04");
        LocalDate orderDate2 = LocalDate.parse("2020-07-20");
        
        //ACT
        List<Order> allOrders = service.getOrders(orderDate1);
        List<Order> allOrders2 = service.getOrders(orderDate2);
        
        //ASSESS
        //There should be 3 orders on that date
        assertEquals(3, allOrders.size(),"There should be 3 orders on " + orderDate1);
        assertEquals(2, allOrders2.size(),"There should be 2 orders on " + orderDate2);
    }
    
    /**
     * Test ability to check order state if it is active
     */
    @Test
    public void testGetOrderActive() {
        //ARRANGE
        LocalDate orderDate1 = LocalDate.parse("2020-11-04");
        try {
            //ACT
            Order currentOrder = service.getOrder(orderDate1, 1);
            //ASSESS
            assertTrue(currentOrder.isActiveOrder(), "This should be trues since the order is active.");
        } catch (OrderCancelledException | NoSuchOrderFileException | NoSuchOrderException ex) {
            fail("The order being searched for is an active order so shouldn't return an error.");
        }
    }
    
    /**
     * Test ability to check order state if it is not active, should throw error
     */
    @Test
    public void testGetOrderNotActive() {
        //ARRANGE
        LocalDate orderDate1 = LocalDate.parse("2020-11-04");
        try {
            //ACT
            Order currentOrder = service.getOrder(orderDate1, 2);
            //ASSESS
            fail("The order being searched for is an not an active order so "
                    + "should return an OrderCancelledException error.");
        } catch (OrderCancelledException ex) {
            
        } catch (NoSuchOrderFileException | NoSuchOrderException ex) {
            fail("The order being searched for is an not an active order so "
                    + "should return an OrderCancelledException error.");
        }
    }
    
    /**
     * Test ability to create a new order.
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException
     */
    @Test
    public void testCreateOrder() throws OrderPersistenceException {
        //ARRANGE
        List<Product> products = service.getProductList();
        List<Taxes> taxes = service.getTaxList();
        String personName = "Sharon Tony";
        Taxes personTax = taxes.get(1);
        Product personProduct = products.get(2);
        LocalDate orderDate = LocalDate.parse("11-04-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        LocalDate orderCreated = LocalDate.parse("07-05-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        BigDecimal personArea = new BigDecimal(200);
        
        Order newOrder = new Order(orderCreated, orderDate, personName, 
                personTax, personProduct,personArea);
        Order completedOrder = service.calculateCosts(newOrder);
        
        //ACT
        Order addOrder = service.createOrder(completedOrder);
        try {
            Order retrievedOrder = service.getOrder(orderDate, addOrder.getOrderNumber());
            
            //ASSESS
            assertEquals(personName, retrievedOrder.getCustomerName(),"Should have the name Sharon Tony");
            assertEquals(personArea, retrievedOrder.getArea(), "Should return the area 200");
            assertNotNull(retrievedOrder.getTax(), "The tax cost should not be empty");
            assertNotNull(retrievedOrder.getLaborCost(), "The labour cost should not be empty");
            assertNotNull(retrievedOrder.getTotal(), "The total cost should not be empty");
        
        } catch (NoSuchOrderFileException | NoSuchOrderException | OrderCancelledException ex) {
            fail("Should be able to retrieve the newly added order.");
        }
    }
    
    /**
     * Test ability to create a new order and ensure that there are differences 
     * in the order details
     */
    @Test
    public void testEditOrder() {
        //ARRANGE
        List<Product> products = service.getProductList();
        String personName = "Beyonce Knowles";
        Product personProduct = products.get(1);
        LocalDate orderDate = LocalDate.parse("11-04-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        BigDecimal personArea = new BigDecimal(300);
        int orderNum = 3;
        
        
        try {
            //ACT
            Order currentOrder = service.getOrder(orderDate, orderNum);
            Order oldDetails = currentOrder;
            currentOrder.setArea(personArea);
            currentOrder.setProduct(personProduct);
            currentOrder.setCustomerName(personName);
            Order completedOrder = service.calculateCosts(currentOrder);
            service.editOrder(completedOrder);
            Order retrievedOrder = service.getOrder(orderDate, orderNum);
            
            //ASSESS
            assertEquals(oldDetails.getTaxInfo().getStateName(),
                    retrievedOrder.getTaxInfo().getStateName(), 
                    "The tax state name should be the same since it wasn't editted.");
            assertEquals(currentOrder.getArea(), retrievedOrder.getArea(), "The area should be 300");
            assertEquals(personProduct.getProductType(),
                    retrievedOrder.getProduct().getProductType(), 
                    "The product type should be updated");
            assertNotNull(retrievedOrder.getTotal(), "The total cost should not be empty");
            
        } catch (NoSuchOrderFileException | OrderCancelledException | NoSuchOrderException | OrderPersistenceException  e) {
            fail("The file exists and shouldn't throw an error.");
        }
    }
    
    /**
     * Test ability to remove order from the list by checking the export list
     */
    @Test
    public void testRemoveOrder() {
        //ARRANGE
        LocalDate orderDate = LocalDate.parse("11-04-2020", java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        int orderNum = 3;
        
        try {
            //ACT
            Order currentOrder = service.getOrder(orderDate, orderNum);
            currentOrder.setActiveOrder(false);
            service.editOrder(currentOrder);
            List<Order> allActiveOrders = service.exportOrders();
            //ASSESS
            assertEquals(3,allActiveOrders.size(), "The list size should be three since there are 2 inactive orders.");
            assertFalse(allActiveOrders.contains(currentOrder),"The export list shouldn't contain the order number 3");
            
        } catch (NoSuchOrderFileException | OrderCancelledException | NoSuchOrderException | OrderPersistenceException  e) {
            fail("The file exists and shouldn't throw an error.");
        }
    }
}
