package arimaa.utils;

public class NumberUtils {

    /**
     * Converts a string to an integer.
     *
     * @param str the string representation of the number
     * @return the converted integer from the string 
     * @throws NumberFormatException if the string cannot be converted to an integer
     */
    public static Integer stringToInteger(String str) throws NumberFormatException {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number! Cannot convert '" + str + "' to an integer!");
        }
    }
}
