package arimaa.utils;

import java.util.Scanner;

public class InputUtils {

    /**
     * Prompts the user with a message and retrieves their input from the console.
     *
     * @param message the message to display to the user
     * @return the user's input as a String
     */
    public static String getUserInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        String userInput = scanner.nextLine();
        return userInput;
    }


    /**
     * Reads an integer value from a user input.
     * If the user input cannot be converted to an Integer, an error message is printed and the user is prompted again.
     *
     * @param message the message to display to the user
     * @return converted integer value entered by the user
     */
    public static int getIntFromInput(String message) {
        while (true) {
            String userInput = getUserInput(message);

            try {
                return NumberUtils.stringToInteger(userInput);
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    /**
     * Handles the user input for a position and returns it as an Integer.
     * If the user inputs "skip", null is returned.
     * If the user input cannot be converted to an Integer, an error message is printed and the user is prompted again.
     *
     * @param message the message to display to the user
     * @return the user input as an Integer, or null if the user inputs "skip"
     */
    public static Integer handlePositionInput(String message) {
        while (true) {
            String userInput = getUserInput(message);

            if (userInput.equalsIgnoreCase("skip")) {
                return null;
            }

            try {
                return NumberUtils.stringToInteger(userInput);
            } catch (NumberFormatException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
