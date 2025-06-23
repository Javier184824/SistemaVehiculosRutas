/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Console;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author JE
 */
public class MenuUtil {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
     * Displays a menu and gets user choice
     * @param title the menu title
     * @param options the menu options
     * @return the selected option index (0-based), or -1 for invalid input
     */
    public static int displayMenu(String title, String... options) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(title);
        System.out.println("=".repeat(50));
        
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s%n", i + 1, options[i]);
        }
        System.out.println("0. Exit/Back");
        System.out.println("-".repeat(50));
        
        return getIntInput("Select an option: ") - 1;
    }
    
    /**
     * Gets integer input from user with validation
     * @param prompt the input prompt
     * @return the integer input
     */
    public static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
            }
        }
    }
    
    /**
     * Gets double input from user with validation
     * @param prompt the input prompt
     * @return the double input
     */
    public static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
            }
        }
    }
    
    /**
     * Gets string input from user
     * @param prompt the input prompt
     * @return the string input (trimmed)
     */
    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Gets non-empty string input from user
     * @param prompt the input prompt
     * @return the non-empty string input
     */
    public static String getNonEmptyStringInput(String prompt) {
        String input;
        do {
            input = getStringInput(prompt);
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }
    
    /**
     * Gets yes/no confirmation from user
     * @param prompt the confirmation prompt
     * @return true for yes, false for no
     */
    public static boolean getConfirmation(String prompt) {
        while (true) {
            String input = getStringInput(prompt + " (y/n): ").toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' for yes or 'n' for no.");
            }
        }
    }
    
    /**
     * Displays an error message
     * @param message the error message
     */
    public static void showError(String message) {
        System.out.println("\n❌ ERROR: " + message);
    }
    
    /**
     * Displays a success message
     * @param message the success message
     */
    public static void showSuccess(String message) {
        System.out.println("\n✅ SUCCESS: " + message);
    }
    
    /**
     * Displays an info message
     * @param message the info message
     */
    public static void showInfo(String message) {
        System.out.println("\n📋 INFO: " + message);
    }
    
    /**
     * Pauses execution until user presses Enter
     * @param message the pause message
     */
    public static void pause(String message) {
        System.out.print("\n" + message + " (Press Enter to continue...)");
        scanner.nextLine();
    }
    
    /**
     * Clears the console (works on most terminals)
     */
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            // Fallback: print empty lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
