package alydiaade.flooringmastery.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * This class handles the Order details. Based on what the user inputs the files 
 * will be other properties will be adjusted as required.
 * @author lydiaadejumo
 */
public class Order {
    
    private LocalDate orderDate;
    private LocalDate orderCreation;
    private int orderNumber;
    private String customerName;
    private Product product;
    private Taxes taxInfo;
    private BigDecimal area;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal total;
    private BigDecimal tax;
    private boolean activeOrder = true;
    
    /**
     * This is the constructor for the Order class.There are specific details 
     * required in order to set up the class.
     * @param orderCreation - the date the order was placed.
     * @param orderDate - the date for the order, some time in the future
     * @param customerName - customer name required for process
     * @param taxRate - the tax rate that is being selected based on the state
     * @param product - the product that is being selected
     * @param area - The area of which the flooring is required for.
     */
    public Order(LocalDate orderCreation, LocalDate orderDate, String customerName, Taxes taxRate, Product product, BigDecimal area) {
        this.orderCreation = orderCreation;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.taxInfo = taxRate;
        this.product = product;
        this.area = area;
    }
    
    /**
     * Gets the order creation date
     * @return the order creation date.
     */
    public LocalDate getOrderCreation() {
        return orderCreation;
    }
    
    /**
     * Gets the order date
     * @return order date
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }
    
    /**
     * Gets the order number for the order
     * @return order number
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * Used to set the order number
     * @param OrderNumber - the order number for the product, since the number of 
     * orders is being tracked.
     */
    public void setOrderNumber(int OrderNumber) {
        this.orderNumber = OrderNumber;
    }

    /**
     * Gets the customer name
     * @return customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer name -  useful when editing information
     * @param CustomerName - the new customer name
     */
    public void setCustomerName(String CustomerName) {
        this.customerName = CustomerName;
    }

    /**
     * Sets the tax -  useful when editing information this also includes the 
     * state, state abbreviation and the tax rate
     * @param taxRate - the tax rate class, including state information and tax rate
     */
    public void setTaxInfo(Taxes taxRate){
        this.taxInfo = taxRate;
    }

    
    /**
     * Returns the tax details which includes all the tax info (state, state 
     * abbreviation and tax rate)
     * @return tax details
     */
    public Taxes getTaxInfo() {
        return taxInfo;
    }

    /**
     * Returns the product which includes the product type, cost per square foot 
     * and the labour cost per square foot
     * @return product type
     */
    public Product getProduct(){
        return product;
    }
    
    /**
     * Sets the product type -  useful when editing information
     * @param product - the new/existing product that is being used.
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Returns the area specified by the user
     * @return returns the area
     */
    public BigDecimal getArea() {
        return area;
    }

    /**
     * Sets the area -  useful when editing information
     * @param Area - the new or existing area that is being used.
     */
    public void setArea(BigDecimal Area) {
        this.area = Area;
    }
    
    /**
     * Returns the material costs calculated for the order
     * @return returns the material costs
     */
    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    /**
     * Returns the labour costs calculated for the order
     * @return returns the labour costs
     */
    public BigDecimal getLaborCost() {
        return laborCost;
    }
    
    /**
     * Returns the tax costs incurred for the order
     * @return returns the tax costs
     */
    public BigDecimal getTax() {
        return tax;
    }

    /**
     * Returns the total costs calculated for the order
     * @return returns the total costs
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * Returns if the order is till active - useful for backing up active orders
     * @return true or false if the order is active
     */
    public boolean isActiveOrder() {
        return activeOrder;
    }

    /**
     * Utilised to set the order to active or inactive.
     * @param activeOrder - the active order state
     */
    public void setActiveOrder(boolean activeOrder) {
        this.activeOrder = activeOrder;
    }

    /**
     * Utilised to set the material cost
     * @param materialCost - the material costs
     */
    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    /**
     * Utilised to set the labour cost
     * @param laborCost - the labour costs
     */
    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    /**
     * Utilised to set the total costs
     * @param total - the total amount
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * Utilised in setting the tax for the order
     * @param tax - the tax amount
     */
    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
    
}
