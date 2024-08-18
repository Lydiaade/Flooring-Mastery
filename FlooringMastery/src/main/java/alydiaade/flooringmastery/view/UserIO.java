package alydiaade.flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This is the user interface, outlining the methods of which the app will obtain
 * information from the user.
 * @author lydiaadejumo
 */

public interface UserIO {
    
    /**
     * This method will print out a particular message
     * @param message - the message that needs to be printed
     */
    public void print(String message);
    
    /**
     * This method will print out a particular message and read the user input string.
     * @param message - the message that needs to be printed
     * @return - the string that has been inputted.
     */
    public String readString(String message);
    
    /**
     * This method will print out a particular message and read a users input 
     * which will change to a boolean.
     * @param message - the message that needs to be printed
     * @return - returns the boolean to the system for later use.
     */
    public boolean readboolean(String message);
    
    /**
     * This method will print out a particular message and read a big decimal 
     * inputted by the user however it repeats until the correct data has been 
     * inputted.
     * @param message - the message that needs to be printed
     * @param min - minimum value for input
     * @return - The big decimal the user has inputted
     */
    public BigDecimal readBigDecimal(String message, BigDecimal min);
    
    /**
     * This method will print out a particular message and read an integer 
     * inputted by the user however it repeats until the correct data has been 
     * inputted.
     * @param message - the message that needs to be printed
     * @return - The integer the user has inputted
     */
    public int readInt(String message);
    
    /**
     * This method will print out a particular message and read an integer 
     * inputted by the user however it repeats until the correct data has been 
     * inputted, in this case the integer must be in the range of min and max
     * @param message - the message that needs to be printed
     * @param min - the minimum integer accepted
     * @param max - the maximum integer accepted
     * @return - The integer the user has inputted
     */
    public int readInt(String message, int min, int max);
    
    /**
     * This method will print out a particular message and read a local date
     * inputted by the user however it repeats until the correct data has been 
     * inputted, which must be a date in the future.
     * @param message - the message that needs to be printed
     * @return - The localDate the user has inputted
     */
    public LocalDate readDate(String message);
    
    /**
     * This method will print out a particular message and read a local date
     * inputted by the user however it can only be a date in the future for new orders.
     * @param message - the message that needs to be printed
     * @return - The localDate the user has inputted
     */
    public LocalDate readFutureDate(String message);
}
