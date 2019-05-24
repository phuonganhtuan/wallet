package utils;

/**
 *
 * @author tungnguyen
 */
public class StringUtils {

    /**
     * Format a number string to separate 3 digits number
     *
     * @param separator separator as a string
     * @param numberInput input string
     * @return the number with 3 digits separated
     */
    public static String threeDigitsSeparate(String separator, String numberInput) {
        String result = "";

        if (!numberInput.equals("")) {
            int length = numberInput.length();
            int i = 0;
            for (i = length - 3; i >= 0; i -= 3) {
                String unit = (i == 0
                        ? numberInput.substring(i, i + 3)
                        : separator + numberInput.substring(i, i + 3));
                result = unit.concat(result);
            }
            if (i < 0) {
                result = numberInput.substring(0, i + 3).concat(result);
            }
        }

        return result;
    }
    
    /**
     * Remove the separator from three-digits formatted number
     * @param separator separator as a string
     * @param numberInput input number as a string
     * @return result as a string
     */
    public static String removeSeparator(String separator, String numberInput) {
        return numberInput.replaceAll(separator, "");
    }
}
