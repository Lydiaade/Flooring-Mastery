package alydiaade.flooringmastery.dao;

import alydiaade.flooringmastery.model.Order;
import java.io.FileNotFoundException;

/**
 *
 * @author lydiaadejumo
 */

public interface FlooringAuditDao {
    
    /**
     * Keeps track of all orders that have been sold - if there is a edit of any features.
     * @param currentOrder  - the newest order that has been done
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if 
     * there is an error when reading the file this error will be thrown.
     */
    public void writeOrderEntryLog(Order currentOrder) throws OrderPersistenceException;
    
    /**
     * Keeps track of all new orders that have been sold - if there is a edit of any features.
     * @param currentOrder  - the newest order that has been done
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if 
     * there is an error when reading the file this error will be thrown.
     */
    public void writeNewOrderEntryLog(Order currentOrder) throws OrderPersistenceException;
    
    /**
     * Reads the last order number and adds one.
     * @return the newest order number.
     * @throws java.io.FileNotFoundException -  in case the found cannot be found.
     */
    public int readsCurrentOrderNumber() throws FileNotFoundException;
    
    /**
     * Logs the number of orders at the end of use of app.
     * @param NumOfOrders - number of orders taken from service layer.
     * @throws alydiaade.flooringmastery.dao.OrderPersistenceException - if 
     * there is an error when reading the file this error will be thrown.
     */
    public void logsNumberOfOrders(int NumOfOrders) throws OrderPersistenceException;
}
