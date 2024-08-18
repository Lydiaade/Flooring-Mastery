package alydiaade.flooringmastery.model;

import java.math.BigDecimal;

/**
 * This class handles all the tax values per state
 * @author lydiaadejumo
 */

public class Taxes {
    
    private String state;
    private String stateName;
    private BigDecimal taxRate;

    /**
     * This is the state class constructor, it needs the state details to be activated
     * @param state - the state abbreviation
     * @param stateName - the state name
     * @param taxRate - the tax rate
     */
    public Taxes(String state, String stateName, BigDecimal taxRate) {
        this.state = state;
        this.stateName = stateName;
        this.taxRate = taxRate;
    }

    /**
     * This will return the corresponding state abbreviation
     * @return returns the state abbreviation
     */
    public String getState() {
        return state;
    }

    /**
     * This will return the corresponding stateName
     * @return returns the state name
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * This will return the corresponding tax name
     * @return returns the state tax rate
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }
    
}
