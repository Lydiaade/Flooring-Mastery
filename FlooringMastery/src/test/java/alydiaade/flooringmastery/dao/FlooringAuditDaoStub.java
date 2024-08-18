package alydiaade.flooringmastery.dao;

import alydiaade.flooringmastery.model.Order;
import java.io.FileNotFoundException;

/**
 *
 * @author lydiaadejumo
 */
public class FlooringAuditDaoStub implements FlooringAuditDao {

    @Override
    public void writeOrderEntryLog(Order currentOrder) throws OrderPersistenceException {
        //Does nothing
    }

    @Override
    public void writeNewOrderEntryLog(Order currentOrder) throws OrderPersistenceException {
        //Does nothing
    }

    @Override
    public int readsCurrentOrderNumber() throws FileNotFoundException {
        return 5;
    }

    @Override
    public void logsNumberOfOrders(int NumOfOrders) throws OrderPersistenceException {
        //Does nothing
    }
    
}
