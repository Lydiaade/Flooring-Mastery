package alydiaade.flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *
 * @author lydiaadejumo
 */
public class UserIOConsoleImpl implements UserIO{
    Scanner sc = new Scanner(System.in);
    
    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String message) {
        System.out.println(message);
        System.out.print(">>> ");
        String value = sc.nextLine();
        return value;
    }

    @Override
    public boolean readboolean(String message) {
        String value;
        boolean booleanValue = false;
        boolean done = false;
        do {
            System.out.println(message);
            System.out.println("Please indicate by typing y or n.");
            System.out.print(">>> ");
            try {
                value = sc.nextLine();
                if (value.toLowerCase().equals("y")) {
                    booleanValue = true;
                    done = true;
                } else if (value.equals("n")) {
                    booleanValue = false;
                    done = true;
                }
            } catch (Exception e) {
                System.out.println("Input error.");
            }
        } while (!done);
        
        return booleanValue;
    }

    @Override
    public BigDecimal readBigDecimal(String message, BigDecimal min) {
        BigDecimal value = new BigDecimal(0);
        do {
            System.out.println(message);
            System.out.println("It must be greater than " + min + " sq ft.");
            System.out.print(">>> ");
            try {
                value = new BigDecimal(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Input error.");
            }
        } while (value.compareTo(min) < 0);
        
        return value;
    }

    @Override
    public int readInt(String message) {
        int value = 0;
        do {
            System.out.println(message);
            System.out.print(">>> ");
            value = Integer.parseInt(sc.nextLine());
        } while (value <= 0);
        
        return value;
    }

    @Override
    public int readInt(String message, int min, int max) {
        int value = 0;
        do {
            System.out.println(message);
            System.out.println("Minimum value: " + min + "| Maximum value: " + max);
            System.out.print(">>> ");
            try {
                value = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Input error.");
            }
        } while (value < min || value > max);
        
        return value;
    }

    @Override
    public LocalDate readDate(String message) {
        LocalDate newDate = null;
        boolean entered;
        do {
            try {
                System.out.println(message);
                System.out.println("Please ensure it is written in the form - MM-dd-yyyy");
                System.out.print(">>> ");
                newDate = LocalDate.parse(sc.nextLine(), java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                entered = true;
            } catch (Exception e) {
                System.out.println("\nInvalid input, please enter the order date in the correct form.\n");
                entered = false;
            }
        } while (!entered);
        
        return newDate;
    }

    @Override
    public LocalDate readFutureDate(String message) {
        LocalDate today = LocalDate.now();
        LocalDate futureDateCap = today.plusYears(5);
        LocalDate newDate = today.minusDays(1);
        do {
            try {
                System.out.println(message);
                System.out.println("Please ensure it is written in the form - MM-dd-yyyy");
                System.out.println("Please note it must be a date in the future, so after " + today.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
                System.out.println("And within 5 years, so before " + futureDateCap.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
                System.out.print(">>> ");
                newDate = LocalDate.parse(sc.nextLine(), java.time.format.DateTimeFormatter.ofPattern("MM-dd-yyyy"));
            } catch (Exception e) {
                System.out.println("\nInvalid input, please enter the order date in the correct form.\n");
            }
        } while (newDate.isBefore(today) || newDate.isAfter(futureDateCap));
        
        return newDate;
    }
    
}
