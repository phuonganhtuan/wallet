package utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author tungnguyen
 */
public class EncryptTool {
    
    /**
     * Get a random hex string with length of 40
     * @return hex string
     */
    public static String getRandomSalt() {
        byte[] saltByte = new byte[20];
        new SecureRandom().nextBytes(saltByte);
        return Hex.encodeHexString(saltByte);
    }
    
    /**
     * Get an input string and hash it to a hex string, using MD5 algorithm
     * @param input string input
     * @return result is a hex string
     */
    public static String MD5Hash(String input) {
        String result = "";
        
        try {
            result = Hex.encodeHexString(
                MessageDigest.getInstance("MD5").digest(input.getBytes())
            );
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        
        return result;
    }
}
