package alydiaade.flooringmastery.controller;

import alydiaade.flooringmastery.dao.NoSuchOrderException;
import alydiaade.flooringmastery.dao.NoSuchOrderFileException;
import alydiaade.flooringmastery.dao.OrderPersistenceException;
import alydiaade.flooringmastery.model.Order;
import alydiaade.flooringmastery.model.Product;
import alydiaade.flooringmastery.model.Taxes;
import alydiaade.flooringmastery.service.FlooringServiceLayer;
import alydiaade.flooringmastery.service.OrderCancelledException;
import alydiaade.flooringmastery.view.FlooringMasteryView;
import java.time.LocalDate;
import java.util.List;

/**
 * This is the controller, that manages the app usage. It controls all the
 * functions whilst utilising the service and view layers
 * @author lydiaadejumo
 */

public class FlooringController {
    
    //Must add service class link
    FlooringServiceLayer service;
    FlooringMasteryView view;

    public FlooringController(FlooringServiceLayer service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }
    
    /**
     * This method will run the program and display all the required options, 
     * managing the users choice.
     */
    public void run() {
        boolean keepGoing = true;
        view.displayFlooringBannerLoading();
        while (keepGoing) {
            int menuSelection;
            menuSelection = getMenuSelection();
            switch(menuSelection) {
                case 1:
                    displayOrders();
                    break;
                case 2:
                    createOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    exportOrders();
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        exitMessage();
    }
    
    /**
     * This will display the menu selection and return the selected option by
     * the user.
     * @return returns the option selected by the user.
     */
    private int getMenuSelection() {
        int optionSelected = view.displayMenu();
        return optionSelected;
    }
    
    /**
     * This will display the orders that have selected by the user based on the
     * local date inputted.
     */
    private void displayOrders() {
        view.displayOptionBanner("Display Orders");
        LocalDate orderDate = view.getAllOrderDates();
        try {
            List<Order> allOrders = service.getOrders(orderDate);
            view.displayOrders(orderDate, allOrders);
        } catch (NoSuchOrderFileException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    /**
     * This is utilised if the user would like to create a new order.
     * The information required included the order date, name, state, product 
     * type and the area.
     */
    private void createOrder() {
        view.displayOptionBanner("Add an Order");
        List<Taxes> allTaxes = service.getTaxList();
        List<Product> allProducts = service.getProductList();
        
        Order newOrder = view.createNewOrder(allTaxes, allProducts);
        newOrder.setOrderNumber(service.getOrderNumber()); 
        Order calculatedOrder = service.calculateCosts(newOrder);
        view.displayEachOrder(calculatedOrder);
        boolean continueOrder = view.continueWithOrder();
        if (continueOrder) {
            try {
                service.createOrder(calculatedOrder);
                view.displayNewOrderSuccessBanner();
            } catch (OrderPersistenceException e) {
                view.displayErrorMessage(e.getMessage());
            }
        } else {
            view.displayOrderFailureBanner();
        }
    }
    
    /**
     * If the user would like to edit an existing order, through they can only
     * edit the name,  state, product type and the area.
     */
    private void editOrder() {
        view.displayOptionBanner("Edit an order");
        LocalDate orderDate = view.getOrderDate();
        int orderNum = view.getOrderNumber();
        List<Taxes> allTaxes = service.getTaxList();
        List<Product> allProducts = service.getProductList();
        try {
            Order currentOrder = service.getOrder(orderDate, orderNum);
            Order edittedOrder = view.editOrder(currentOrder, allTaxes, allProducts);
            Order calculatedOrder = service.calculateCosts(edittedOrder);
            view.displayEachOrder(calculatedOrder);
            boolean orderUpdate = view.confirmOrderChange();
            if (orderUpdate) {
                service.editOrder(calculatedOrder);
                view.displayOrderUpdateSuccessBanner();
            } else {
                view.displayOrderUpdateCancellationBanner();
            }
        } catch (NoSuchOrderException | NoSuchOrderFileException |
                OrderCancelledException | OrderPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
        
    }
    
    /**
     * If a user would like to remove and order, they can simply deem it in 
     * active using this method though it will still be kept in storage.
     */
    private void removeOrder() {
        view.displayOptionBanner("Remove an order");
        LocalDate orderDate = view.getOrderDate();
        int orderNum = view.getOrderNumber();
        try {
            Order currentOrder = service.getOrder(orderDate, orderNum);
            view.displayEachOrder(currentOrder);
            boolean cancellation = view.continueWithOrderCancellation();
            if (cancellation) {
                currentOrder.setActiveOrder(false);
                service.editOrder(currentOrder);
                view.displayOrderFailureBanner();
            } else {
                view.displayOrderCancellationFailureBanner();
            }
        } catch (NoSuchOrderException | NoSuchOrderFileException |
                OrderCancelledException | OrderPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
        
    }
    
    /**
     * This is utilised and will then back up all the files to the Data export file. 
     */
    private void exportOrders() {
        try {
            view.displayOptionBanner("Exporting All Data");
            List<Order> activeOrders = service.exportOrders();
            view.displayExportMessage(activeOrders);
            view.displayOptionBanner("Export Complete");
        } catch (NoSuchOrderFileException | OrderPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    /**
     * This will display the exit message, if the exit option is selected.
     */
    private void exitMessage() {
        view.displayExitMessage();
    }
    
    /**
     * If an unknown command is thrown an unknown command message will be displayed.
     * Ideally this will not be utilised only in case there is a serious breach whilst utilising the app.
     */
    private void unknownCommand() {
        System.out.println("Unknown command.");
    }
}
