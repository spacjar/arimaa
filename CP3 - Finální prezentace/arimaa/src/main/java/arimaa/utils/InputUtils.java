package arimaa.utils;

import java.util.Scanner;

public class InputUtils {

    public static String getUserInput(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        String userInput = scanner.nextLine();
        return userInput;
    }

    public static Number stringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
            return null;
        }
    }

    public static int getIntFromInput(String message) {
        Integer number = null;
        while (number == null) {
            String userInput = getUserInput(message);
            try {
                number = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return number;
    }
}
