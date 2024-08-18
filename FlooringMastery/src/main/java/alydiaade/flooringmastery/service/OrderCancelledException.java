/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alydiaade.flooringmastery.service;

/**
 *
 * @author lydiaadejumo
 */

public class OrderCancelledException extends Exception {

    public OrderCancelledException(String message) {
        super(message);
    }

    public OrderCancelledException(String message, Throwable cause) {
        super(message, cause);
    }
}
