/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanc.auth;
// Required packages
import java.util.UUID;
import java.security.Key;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
// Exceptions
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author champ
 */
public class AutherCrypto {

    /**
     * Generate 128 bit value
     * 
     * @return String - calculated id
     */
    public static String generateID()
    {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Generate a 128 bit random vector need for AES encryption  / decryption
     * 
     * @return String - Random vector 
     */
    public static String generateAESIV()
    {
        String encodedKey = "";
        try {
            Key key;
            SecureRandom rand = new SecureRandom();
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(rand);
            generator.init(256);
            key = generator.generateKey();
            Base64 codec = new Base64();
            byte[] bEncodedKey = codec.encode(key.getEncoded());
            encodedKey = new String(bEncodedKey, "UTF-8");
            encodedKey = encodedKey.substring(0, 16);
        } catch(Exception e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return encodedKey;
    }
    
    /**
     * Generate a key of 128 bit need to AES encryption / decryption
     * 
     * @return String - Random key
     */
    public static String generateAESKey()
    {
        String encodedKey = "";
        try {
            Key key;
            SecureRandom rand = new SecureRandom();
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(rand);
            generator.init(256);
            key = generator.generateKey();
            Base64 codec = new Base64();
            byte[] bEncodedKey = codec.encode(key.getEncoded());
            encodedKey = new String(bEncodedKey, "UTF-8");
            encodedKey = encodedKey.substring(0, 16);
        } catch(Exception e) {
            System.out.println("ECCEZIONE: " + e.getMessage());
        }
        return encodedKey;
    }
    
    /**
     * Generate a key of 256 bit require to calculate totp pin
     * 
     * @return String - Generated key
     */
    public static String generateKey() 
    {
        String encodedKey = "";
        try {
            Key key;
            SecureRandom rand = new SecureRandom();
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(rand);
            generator.init(256);
            key = generator.generateKey();
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(key.getEncoded());
            encodedKey = new String(bEncodedKey);
        } catch(NoSuchAlgorithmException n) {
            System.out.println("ECCEZIONE: " + n.getMessage());
        }
        return encodedKey;
    }
    
    /**
     * Generate a random matrix of 49 bytes
     * 
     * @return String - Generated matrix
     */
    public static String generateMatrix()
    {
        String matrix = "";
        String[] dict = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b",
            "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
            "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9"};
        Random randomGenerator = new Random();
        for(int i = 0; i < 49; ++i)
            matrix += dict[randomGenerator.nextInt(61)];
        
        return matrix;
    }
    
    /**
     * Generate a list containing five random integer values
     * 
     * @return List<Integer> - Five random values
     */
    public static String generateRndSequence()
    {
        String aux = "";
        List<Integer> l = new ArrayList<Integer>();
        // Create list
        for(int i = 0; i < 49; ++i)
            l.add(i);
        Random randomGenerator = new Random();
        
        // Select a random index, fetch value and then remove value from list
        for(int i = 0; i < 5; i++) {
            int v = randomGenerator.nextInt(l.size() - 1);
            aux += l.get(v);
            if(i != 4)
                aux += "-";
            l.remove(v);
            System.out.println("Size:" + l.size() + "  " + l);
        }
        System.out.println("**Generate : " + aux);
        return aux;
    }
    
    /**
     * Performa a 256 bit hash using SHA-256 algorithm
     * 
     * @param plainText - Text to hash
     * @return String - Hashed text
     */
    public static String hash256(String plainText)
    {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(plainText);
    }
    
    /**
     * Temporary function which split toDecypt plain text and select first item
     * 
     * @param toDecrypt - Encrypted information
     * @param encryptionKey - Key require to decrypt information with AES algorithm
     * @param encryptionIV - Vector require to decrypt information with AES algorithm
     * @return String - Decrypted information
     */
    public static String decrypt(String toDecrypt, String encryptionKey, String encryptionIV) 
    {
        System.out.println("** Require decrypt for " + toDecrypt + " and returning " + toDecrypt.split("---")[0]);
        return toDecrypt.split("---")[0];
    }
}