package alydiaade.flooringmastery.dao;

import alydiaade.flooringmastery.model.Order;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * This method is utilised, to log all the user interactions and updates. It also 
 * records the current total number of orders, which is utilised to update the 
 * new orders and continue on from the current amount.
 * @author lydiaadejumo
 */

public class FlooringAuditDaoImpl implements FlooringAuditDao{

    private String DELIMITER = ":::";
    private String AUDIT_STOCK_FILE = "src/main/resources/Audit_Log.txt";
    
    @Override
    public void writeOrderEntryLog(Order currentOrder) throws OrderPersistenceException{
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(AUDIT_STOCK_FILE, true));
        } catch (IOException e) {
            throw new OrderPersistenceException("Could not persist audit information.", e);
        }

        LocalDate timestamp = LocalDate.now();
        if (currentOrder.isActiveOrder()) {
            out.println(timestamp.toString() + " --> ORDER NUMBER: " + 
                currentOrder.getOrderNumber() + ", order update.");
        } else {
            out.println(timestamp.toString() + " --> ORDER NUMBER: " + 
                currentOrder.getOrderNumber() + ", order cancelled.");
        }
        
        out.flush();
        out.close();
    }
    
    @Override
    public void writeNewOrderEntryLog(Order currentOrder) throws OrderPersistenceException{
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(AUDIT_STOCK_FILE, true));
        } catch (IOException e) {
            throw new OrderPersistenceException("Could not persist audit information.", e);
        }

        LocalDate timestamp = LocalDate.now();
        
        out.println(timestamp.toString() + " --> ORDER NUMBER: " + 
                currentOrder.getOrderNumber() + ", new order recorded today.");
        out.flush();
        out.close();
    }

    @Override
    public int readsCurrentOrderNumber() throws FileNotFoundException {
        Scanner sc = new Scanner(new BufferedReader(new FileReader(AUDIT_STOCK_FILE)));
        String lastLine;
        int orderNumber = 0;
        while (sc.hasNext()) {
            lastLine = sc.nextLine();
            if (lastLine.startsWith("CURRENT TOTAL ORDERS")) {
                String[] lastOrderNumber = lastLine.split(DELIMITER);
                orderNumber = Integer.parseInt(lastOrderNumber[1]);
            }
        }
        return orderNumber;
    }
    
    @Override
    public void logsNumberOfOrders(int NumOfOrders) throws OrderPersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(AUDIT_STOCK_FILE, true));
        } catch (IOException e) {
            throw new OrderPersistenceException("Could not persist audit information.", e);
        }
        
        out.println("CURRENT TOTAL ORDERS" + DELIMITER + NumOfOrders);
        out.flush();
        out.close();
    }
    
}
