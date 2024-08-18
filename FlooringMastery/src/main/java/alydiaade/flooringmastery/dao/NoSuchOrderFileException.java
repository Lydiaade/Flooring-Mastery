/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alydiaade.flooringmastery.dao;

/**
 * This exception is utilised if an order cannot be found when using the the
 * order date.
 * @author lydiaadejumo
 */
public class NoSuchOrderFileException extends Exception {

    public NoSuchOrderFileException(String message) {
        super(message);
    }

    public NoSuchOrderFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
