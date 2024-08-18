/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alydiaade.flooringmastery.service;

import alydiaade.flooringmastery.dao.FlooringAuditDao;
import alydiaade.flooringmastery.dao.FlooringOrderDao;
import alydiaade.flooringmastery.dao.NoSuchOrderException;
import alydiaade.flooringmastery.dao.NoSuchOrderFileException;
import alydiaade.flooringmastery.dao.OrderPersistenceException;
import alydiaade.flooringmastery.model.Order;
import alydiaade.flooringmastery.model.Product;
import alydiaade.flooringmastery.model.Taxes;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the service layer, which handles interactions between the controller
 * and DAOs.
 * @author lydiaadejumo
 */
public class FlooringServiceLayerImpl implements FlooringServiceLayer {

    FlooringOrderDao dao;
    private FlooringAuditDao auditDao;
    int currentOrderNum;

    public FlooringServiceLayerImpl(FlooringOrderDao dao, FlooringAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
        
        try {
            currentOrderNum = auditDao.readsCurrentOrderNumber() + 1;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FlooringServiceLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<Product> getProductList() {
       return dao.getProducts();
    }

    @Override
    public List<Taxes> getTaxList() {
        return dao.getTaxes();
    }

    @Override
    public int getOrderNumber() {
        return currentOrderNum;
    }
    
    @Override
    public List<Order> getOrders(LocalDate date) throws NoSuchOrderFileException {
        return dao.getAllOrders(date);
    }

    @Override
    public Order getOrder(LocalDate date, int orderNum) throws NoSuchOrderFileException, NoSuchOrderException, OrderCancelledException {
        Order currentOrder = dao.getOrder(date, orderNum);
        if (!currentOrder.isActiveOrder()) {
            throw new OrderCancelledException("This order has already been cancelled.");
        } else {
            return currentOrder;
        }
                
    }

    @Override
    public Order createOrder(Order newOrder) throws OrderPersistenceException{
        dao.addOrder(newOrder);
        auditDao.writeNewOrderEntryLog(newOrder);
        auditDao.logsNumberOfOrders(currentOrderNum);
        currentOrderNum += 1;
        return newOrder;
    }

    @Override
    public void editOrder(Order editedOrder) throws NoSuchOrderFileException, OrderPersistenceException {
        dao.editOrder(editedOrder);
        auditDao.writeOrderEntryLog(editedOrder);
    }

    @Override
    public List<Order> exportOrders() throws NoSuchOrderFileException, OrderPersistenceException {
        return dao.exportOrders();
    }

    @Override
    public Order calculateCosts(Order order) {
        //Material Cost
        BigDecimal materialCost = order.getArea().multiply(order.getProduct().getCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        //Labour Cost
        BigDecimal laborCost = order.getArea().multiply(order.getProduct().getLaborCostPerSquareFoot()).setScale(2, RoundingMode.HALF_UP);
        //Tax
        BigDecimal addMCandLC = materialCost.add(laborCost);
        BigDecimal taxRatePercentage = order.getTaxInfo().getTaxRate().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        BigDecimal tax = addMCandLC.multiply(taxRatePercentage).setScale(2, RoundingMode.HALF_UP);
        //Total
        BigDecimal total = materialCost.add(laborCost).add(tax).setScale(2, RoundingMode.HALF_UP);
        
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
        
        return order;
    }

}
