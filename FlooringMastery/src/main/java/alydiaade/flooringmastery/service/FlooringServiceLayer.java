package alydiaade.flooringmastery.service;

import alydiaade.flooringmastery.dao.NoSuchOrderException;
import alydiaade.flooringmastery.dao.NoSuchOrderFileException;
import alydiaade.flooringmastery.dao.OrderPersistenceException;
import alydiaade.flooringmastery.model.Order;
import alydiaade.flooringmastery.model.Product;
import alydiaade.flooringmastery.model.Taxes;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author lydiaadejumo
 */
public interface FlooringServiceLayer {
    
    /**
     * This will get the next order number if a new order is being made
     * @return order number of the newest order.
     */
    public int getOrderNumber();
    /**
     * This method will list all the orders based on the date that has been inputted.
     * @param date - the date requested by the user.
     * @return returns all the orders listed from that date.
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderFileException - if there is
     * no order to return
     */
    public List<Order> getOrders(LocalDate date) throws NoSuchOrderFileException;
    
    /**
     * This method will get a particular order based on the date and the orderNum.
     * @param date -  the date of the order
     * @param orderNum - the order number for the order being requested
     * @return - returns the specified order, if it exists
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderException - if a order 
     * cannot be found in an order file
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderFileException - if there is
     * no order file with that date
     * @throws alydiaade.flooringmastery.service.OrderCancelledException - if an
     * order has already been cancelled this error will flag it
     */
    public Order getOrder(LocalDate date, int orderNum) throws NoSuchOrderFileException, NoSuchOrderException, OrderCancelledException;
    
    /**
     * Creates a new order based on the input of the user.
     * @param newOrder - new order with details filled out.
     * @return - the created order.
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if the 
     * file is unable to add the new order to a file
     */
    public Order createOrder(Order newOrder) throws OrderPersistenceException;
    
    /**
     * This method allows for the edited order, done by the user to be inputted 
     * into the system.
     * @param editedOrder - the newly edited order.
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderFileException - in case an
     * order cannot be found
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if the 
     * file is unable to add the new order to a file
     */
    public void editOrder(Order editedOrder) throws NoSuchOrderFileException, OrderPersistenceException;
    
    /**
     * Exports all the orders from order file and those that are still active 
     * will be added to the backup file.
     * @return - returns the list of active orders
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderFileException - in case an
     * order cannot be found
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException -  in case 
     * a file cannot be written to
     */
    public List<Order> exportOrders() throws NoSuchOrderFileException, OrderPersistenceException;
    
    /**
     * This method returns a list of products that are available.
     * @return - returns the list of products.
     */
    public List<Product> getProductList();
    
    /**
     * This method returns a list of taxes per state that are available.
     * @return - returns the list of taxes
     */
    public List<Taxes> getTaxList();
    
    /**
     * This method is utilised to calculate the order costs using the user
     * information inputted by the user.
     * Calculates all remaining properties of the order which are listed below:
     * MaterialCost = (Area * CostPerSquareFoot)
     * LaborCost = (Area * LaborCostPerSquareFoot)
     * Tax = (MaterialCost + LaborCost) * (TaxRate/100)
     * Tax rates are stored as whole numbers
     * Total = (MaterialCost + LaborCost + Tax)
     * @param order - the current order state without the costs.
     * @return - the updates user details with all the calculations completed.
     */
    public Order calculateCosts(Order order);
    
}
