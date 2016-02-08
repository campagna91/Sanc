/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth.totp;
// Required packages
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base32;
// Exceptions
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author champ
 */
public class TotpChecker {
    
    TotpChecker() {}
    
    /**
     * Try to validate received pin code
     *
     * @param secret - Shared key between client and server
     * @param code - Received pin to validate
     * @param t - Calculated time steps
     * @return boolean - true if pin match into defined window
     * @throws NoSuchAlgorithmException - If algorithm type is unknown
     * @throws InvalidKeyException - If passed key is not valid
     */
    static boolean checkCode(String secret, long code, long t)
        throws NoSuchAlgorithmException, InvalidKeyException 
    {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);

        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go. 
        int window = 3;
        for (int i = -window; i <= window; ++i) {
            long hash = verifyCode(decodedKey, t + i);
            if (hash == code)
                return true;
        }
        
        // The validation code is invalid.
        return false;
    }

    /**
     * Function provide to calculate pin with encoded secret key and match
     * it with received pin
     * 
     * @param key - Shared key between client and server
     * @param t - Received pin to validate
     * @return boolean - true if pin match to that received
     * @throws NoSuchAlgorithmException - If algorithm type is unknown
     * @throws InvalidKeyException - If passed key is not valid
     */
    private static int verifyCode(byte[] key, long t)
            throws NoSuchAlgorithmException, InvalidKeyException {
            
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }
}
