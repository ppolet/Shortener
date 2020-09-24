
package shortener;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Helper {
    public static String generateRandomString(){
        return new BigInteger(130, new SecureRandom()).toString(36).toLowerCase();
    }
    
    public static void printMessage(String message){
        System.out.println(message);
    }
}
