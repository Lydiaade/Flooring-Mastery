package alydiaade.flooringmastery.view;

import alydiaade.flooringmastery.model.Order;
import alydiaade.flooringmastery.model.Product;
import alydiaade.flooringmastery.model.Taxes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

/**
 * This is the class that interacts with the user, by displaying/printing all 
 * the relevant messages. It utilises the user IO console methods to effectively
 * interact with the user.
 * @author lydiaadejumo
 */

public class FlooringMasteryView {
    
    UserIO io;

    /**
     * The constructor which also instantiates the io.
     * @param io  - the user input console
     */
    public FlooringMasteryView(UserIO io) {
        this.io = io;
    }
    
    /**
     * This will display the opening message to the user.
     */
    public void displayFlooringBannerLoading() {
        io.print("\nLoading Flooring Mastery... \nTSG Corp");
    }
    
    /**
     * This method displays the menu to the user, which includes all the possible
     * options available to the user of which they will input their choice.
     * @return - the users choice will be returned to the controller.
     */
    public int displayMenu() {
        io.print("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("*  <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("*\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        int option = io.readInt("\nPlease pick an option:", 1, 6);
        return option;
    }
    
    /**
     * This will display the respective banner for each option selected.
     * @param message - the message to be displayed on the previous user choice.
     */
    public void displayOptionBanner(String message) {
        io.print("\n=======  " + message.toUpperCase() + "  =======\n");
    }
    
    /**
     * This method is utilised to gauge which order/delivery date the user would
     * like to see.
     * @return - the order date will be returned to the controller.
     */
    public LocalDate getAllOrderDates() {
        LocalDate orderDate = io.readDate("Please enter the date for the orders you would like displayed.");
        return orderDate;
    }
    
    /**
     * This method is used to display all the orders on a particular date that 
     * has been previously inputted by the user.
     * @param orderDate - the order/delivery date given the previously
     * @param allOrders - all the orders retrieved for the orderDate
     */
    public void displayOrders(LocalDate orderDate, List<Order> allOrders) {
        io.print("Order deliveries on date: " + orderDate.toString());
        allOrders.forEach((eachOrder) -> {
            displayEachOrder(eachOrder);
        });
    }
    
    /**
     * This method is used to display the information for orders, which includes 
     * the name, dates (delivery, order creation), state name, tax rates, costs
     * and if the order is currently active.
     * @param eachOrder - the order that needs to be displayed
     */
    public void displayEachOrder(Order eachOrder) {
        if (eachOrder.isActiveOrder()) {
            io.print("\n == ACTIVE ORDER == ");
        } else {
            io.print("\n == INACTIVE ORDER == ");
        }
        io.print(eachOrder.getOrderNumber() + ". " + 
                    eachOrder.getCustomerName());
        io.print("Order Creation Date: " + eachOrder.getOrderCreation().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        io.print("Delivery Date: " + eachOrder.getOrderDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        io.print("State: " + eachOrder.getTaxInfo().getStateName());
        io.print("Tax Rate: $" + eachOrder.getTaxInfo().getTaxRate());
        io.print("Product Type: " + eachOrder.getProduct().getProductType());
        io.print("Cost Per Square Foot: $" + eachOrder.getProduct().getCostPerSquareFoot());
        io.print("Labor Cost Per Square Foot: $" + eachOrder.getProduct().getLaborCostPerSquareFoot());
        io.print("Area: " + eachOrder.getArea() + " sq ft.");
        io.print("Material Costs: $" + eachOrder.getMaterialCost());
        io.print("Labor Costs: $" + eachOrder.getLaborCost());
        io.print("Tax: $" + eachOrder.getTax());
        io.print("_________________________");
        io.print("Total Costs: $" + eachOrder.getTotal());
    }
    
    /**
     * The create new order method is used of a user would like to create a new 
     * method, they would have to insert all the relevant information for that 
     * order to be created. The order date must be within the next 5 years.
     * @param allTaxes - the taxes list that is on the system is required to reduce
     * the chance of error when when the user is inputting tax names. Also making
     * it easier to set all respective details required to calculate costs.
     * @param allProducts - the product list that is on the system is required to 
     * reduce the chance of error when the user is inputting product type. 
     * Also making it easier to set all respective details required to calculate costs
     * @return - returns the newly created order.
     */
    public Order createNewOrder(List<Taxes> allTaxes, List<Product> allProducts) {
        LocalDate date = io.readFutureDate("\nPlease enter the delivery date.");
        io.print("\nNAME");
        String firstName = io.readString("Enter your first name.");
        String lastName = io.readString("Enter your surname name.");
        String name = firstName + " " + lastName;
        
        io.print("\nSTATE");
        io.print("Possible states:");
        for (int i=0; i < allTaxes.size(); i ++) {
            io.print(i+1 + ". " + allTaxes.get(i).getStateName());
        }
        int taxOption = io.readInt("Enter the state that corresponds with your location", 1, allTaxes.size());
        Taxes selectedTax = allTaxes.get(taxOption-1);
        
        io.print("\nPRODUCT TYPE");
        io.print("Possible product types:");
        for (int i=0; i < allProducts.size(); i ++) {
            io.print(i+1 + ". " + allProducts.get(i).getProductType());
        }
        int productOption = io.readInt("Enter the state that corresponds with your location", 1, allProducts.size());
        Product selectedProduct = allProducts.get(productOption-1);
        
        io.print("\nAREA");
        BigDecimal min = new BigDecimal(100);
        BigDecimal area = io.readBigDecimal("Enter the area of which the flooring should cover.", min);
        
        Order newOrder = new Order(LocalDate.now(), date, name, selectedTax, selectedProduct, area);
        return newOrder;
    }
    
    /**
     * This method checks if the user would like to place a new order
     * @return - the choice of the user (true (yes) or false (no))
     */
    public boolean continueWithOrder() {
        boolean continueOrder = io.readboolean("\nPlease confirm if you would like to proceed with this order.");
        return continueOrder;
    }
    
    /**
     * Displays message that the new order has been placed.
     */
    public void displayNewOrderSuccessBanner() {
        io.print("\n====  NEW ORDER SUCCESSFULLY ADDDED  ====\n");
    }
    
    /**
     * Displays message that order has been cancelled.
     */
    public void displayOrderFailureBanner() {
        io.print("\n====  ORDER CANCELLED  ====\n");
    }
    
    /**
     * This method is utilised to obtain a order delivery date for editing or removal
     * @return - returns the order delivery date
     */
    public LocalDate getOrderDate() {
        LocalDate orderDate = io.readDate("Please enter the order delivery date.");
        return orderDate;
    }
    
    /**
     * This method would get the order number from the user.
     * @return - returns the inputted order number.
     */
    public int getOrderNumber() {
        int orderNum = io.readInt("Please enter the order number.");
        return orderNum;
    }
    
    /**
     * This method asks the user if they would like to proceed with the order cancellation.
     * @return - the choice of the user (true (yes) or false (no))
     */
    public boolean continueWithOrderCancellation() {
        boolean continueOrder = io.readboolean("\nPlease confirm if you would like to proceed with this order cancellation.");
        return continueOrder;
    }
    
    /**
     * Displays message that the order has not been cancelled.
     */
    public void displayOrderCancellationFailureBanner() {
        io.print("\n====  ORDER CANCELLATION -- CANCELLED (Your order is still intact ====\n");
    }
    
    /**
     * If the user is to edit an orders details, this method is shown. It obtains
     * the new input the options that can be changed else it would leave the order
     * property as is.
     * @param oldOrder - The order that the user would like to edit.
     * @param allTaxes - the taxes list that is on the system is required to reduce
     * the chance of error when when the user is inputting tax names. Also making
     * it easier to set all respective details required to calculate costs.
     * @param allProducts - the product list that is on the system is required to 
     * reduce the chance of error when the user is inputting product type. 
     * Also making it easier to set all respective details required to calculate costs
     * @return - returns the edited order to the controller
     */
    public Order editOrder(Order oldOrder, List<Taxes> allTaxes, List<Product> allProducts) {
        io.print("\nPlease note that you cannot change the order delivery date");
        Order currentOrder = oldOrder;
        io.print("\nNAME"); 
        String name = io.readString("Enter your name (" + oldOrder.getCustomerName() + ").");
        if (!name.isBlank()) {
            currentOrder.setCustomerName(name);
        }
        
        io.print("\nSTATE");
        String previousState = oldOrder.getTaxInfo().getStateName();
        io.print("Possible states:");
        for (int i=0; i < allTaxes.size(); i ++) {
            if (allTaxes.get(i).getStateName().equals(previousState)) {
                io.print(i+1 + ". " + allTaxes.get(i).getStateName() + " (previously selected).");
            } else {
                io.print(i+1 + ". " + allTaxes.get(i).getStateName());
            }
        }
        int taxOption = io.readInt("Enter the state that corresponds with your location", 1, allTaxes.size());
        Taxes selectedTax = allTaxes.get(taxOption-1);
        currentOrder.setTaxInfo(selectedTax);
        
        io.print("\nPRODUCT TYPE");
        String previousProductType = oldOrder.getProduct().getProductType();
        io.print("Possible product types:");
        for (int i=0; i < allProducts.size(); i ++) {
            if (allProducts.get(i).getProductType().equals(previousProductType)) {
                io.print(i+1 + ". " + allProducts.get(i).getProductType() + " (previously selected).");
            } else {
                io.print(i+1 + ". " + allProducts.get(i).getProductType());
            }
        }
        int productOption = io.readInt("Enter the state that corresponds with your location", 1, allProducts.size());
        Product selectedProduct = allProducts.get(productOption-1);
        currentOrder.setProduct(selectedProduct);
        
        io.print("\nAREA");
        BigDecimal min = new BigDecimal(100);
        BigDecimal area = io.readBigDecimal("Enter the area of which the flooring should cover (previously - " + oldOrder.getArea()+ " sq ft).", min);
        currentOrder.setArea(area);
        return currentOrder;
    }
    
    /**
     * Ask the user if they would like to proceed in changing the order details.
     * @return - the choice of the user (true (yes) or false (no))
     */
    public boolean confirmOrderChange() {
        boolean orderChange = io.readboolean("\nPlease confirm if you would like to proceed with this order update.");
        return orderChange;
    }
    
    /**
     * Displays message if the order has been updated.
     */
    public void displayOrderUpdateSuccessBanner() {
        io.print("\n====  ORDER SUCCESSFULLY UPDATED ====\n");
    }
    
    /**
     * Displays message if the order update has been cancelled.
     */
    public void displayOrderUpdateCancellationBanner() {
        io.print("\n====  ORDER UPDATE CANCELLED ====\n");
    }
    
    public void displayExportMessage(List<Order> exportOrders) {
        io.print("Number of active orders exported to Back Up file: " + exportOrders.size());
        io.print("\nExported Active Orders:");
        for (Order activeOrder : exportOrders) {
            io.print("\n" + activeOrder.getOrderNumber() + ". " + activeOrder.getCustomerName());
            io.print("Order Delivery Date: " + activeOrder.getOrderDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        }
    }
    
    /**
     * Displays exit message to student
     */
    public void displayExitMessage() {
        io.print("\nUtilisation of Flooring Mastery Application complete.\n");
        io.print("============== SHUTTING DOWN ==============");
    }
    
    /**
     * Displays unknown command prompt
     */
    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }
    
    /**
     * If there is an error, the error banner will be printed then
     * the error message will be displayed.
     * @param errorMsg - error message that needs to be displayed, specific to error.
     */
    public void displayErrorMessage(String errorMsg) {
        io.print("\n===== ERROR =====");
        io.print(errorMsg);
    }
    
}
