/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alydiaade.flooringmastery.dao;

/**
 * This exception is utilised if an order cannot be found in the respective
 * order file.
 * @author lydiaadejumo
 */
public class NoSuchOrderException extends Exception {

    public NoSuchOrderException(String message) {
        super(message);
    }

    public NoSuchOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
