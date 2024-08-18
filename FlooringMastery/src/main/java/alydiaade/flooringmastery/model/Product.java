package alydiaade.flooringmastery.model;

import java.math.BigDecimal;

/**
 *
 * @author lydiaadejumo
 */
public class Product {
    
    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;

    /**
     * The product constructor, which will need the following detail for the product for set up.
     * @param productType -  the product type
     * @param costPerSquareFoot - the cost per square foot
     * @param laborCostPerSquareFoot  - the labour cost per square foot
     */
    public Product(String productType, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot) {
        this.productType = productType;
        this.costPerSquareFoot = costPerSquareFoot.setScale(2);
        this.laborCostPerSquareFoot = laborCostPerSquareFoot.setScale(2);
    }
    
    /**
     * This will return the productType
     * @return returns the product type
     */
    public String getProductType() {
        return productType;
    }

    /**
     * This will return the cost per square foot for the product type
     * @return returns the cost per square foot
     */
    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    /**
     * This will return the labour cost per square foot for the product type
     * @return returns the labour cost per square foot.
     */
    public BigDecimal getLaborCostPerSquareFoot() {
        return laborCostPerSquareFoot;
    }
    
    
}
