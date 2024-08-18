package alydiaade.flooringmastery.dao;

import alydiaade.flooringmastery.model.Order;
import alydiaade.flooringmastery.model.Product;
import alydiaade.flooringmastery.model.Taxes;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author lydiaadejumo
 */

public interface FlooringOrderDao {
    
    /**
     * Gets all orders based on the local date inputted in the format MMDDYYYY.The file format should be "Orders_MMDDYYYY.txt".
     * @param date - utilised in getting all orders for that corresponding date.
     * @return returns a list of all the orders for a particular date.
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderFileException - if the file cannot be found this exception is thrown.
     */
    public List<Order> getAllOrders(LocalDate date) throws NoSuchOrderFileException;
    
    /**
     * Utilised to add an order to the respective date file.
     * @param newOrder - The new order that needs to be added.
     * @return returns the order, that has just been added
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if the
     * file it is written to doesn't open
     */
    public Order addOrder(Order newOrder) throws OrderPersistenceException;
    
    /**
     * This will export all the active orders to the backup file
     * @return - returns the active orders.
     * @throws NoSuchOrderFileException - If the file cannot be found this exception will be thrown.
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if it is
     * unable to write to a field
     */
    public List<Order> exportOrders() throws NoSuchOrderFileException, OrderPersistenceException;
    
    /**
     * Gets an order for the user
     * @param date - requires the date that the order was made
     * @param orderNum - requires the order number to retrieve the file order
     * @return the order, it it exists otherwise null.
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderFileException - this would 
     * occur if there is no such order file in existence.
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderException -  this would
     * occur if there is no order with that number
     */
    public Order getOrder(LocalDate date, int orderNum) throws NoSuchOrderFileException, NoSuchOrderException;
    
    /**
     * Updates an orders information based on the updated order which could be if
     * a file is still active or if the file has updated details.
     * @param orderToUpdate - the order that needs it details updated
     * @throws alydiaade.flooringmastery.dao.NoSuchOrderFileException - this would 
     * occur if there is no such order file in existence.
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if the
     * file cannot be written to.
     */
    public void editOrder(Order orderToUpdate) throws NoSuchOrderFileException, OrderPersistenceException;
    
    /**
     * This would get all the products in the system
     * @return the list of products
     */
    public List<Product> getProducts();
    
    /**
     * This would get all the taxes per state in the system
     * @return the list of state taxes
     */
    public List<Taxes> getTaxes();
}
