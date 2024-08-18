/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alydiaade.flooringmastery.dao;

/**
 * If a particular file cannot be found e.g. taxes or products.
 * @author lydiaadejumo
 */
public class OrderPersistenceException extends Exception {

    public OrderPersistenceException(String message) {
        super(message);
    }

    public OrderPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
