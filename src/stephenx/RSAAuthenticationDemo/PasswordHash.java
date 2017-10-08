package stephenx.RSAAuthenticationDemo;

import java.util.Scanner;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Password Hashing Utility with SHA-256.</p>
 * <p>This generates salt and hashes for a given userID and password.</p>
 * 
 * @author Stephen Xie &lt;***@andrew.cmu.edu&gt;
 */
public class PasswordHash {
    
    // used to store userID - hashed password pairs (only for testing)
    private static Map<String, String[]> test = new HashMap<>();
    
    
    /**
     * Used to generate salt and hash for the given user id and password.
     * @param userID user ID
     * @param password raw password
     */
    public static void initializeHash(String userID, String password) {
        // generate salt
        // note: although the handout asked us to generate a random integer
        // as salt, in its example the salt is not an integer. Therefore I'll
        // just follow Java convention and generate a salt string using SecureRandom.
        SecureRandom rand = new SecureRandom();
        byte[] salt = rand.generateSeed(32);
        // the salt has 32 bytes
        String saltEncoded = Base64.getEncoder().encodeToString(salt);
        
        // generate password hash using SHA-256
        String hashedPW = generateHash(password, saltEncoded);
        
        // put to memory for later retrieval
        String[] pwPair = new String[2];
        pwPair[0] = saltEncoded;
        pwPair[1] = hashedPW;
        test.put(userID, pwPair);
        
        // output result
        System.out.println("The following could be stored on a file:");
        System.out.println("user ID = " + userID);
        System.out.println("Salt = " + saltEncoded);
        System.out.println("SHA-256 Hash of (salt + password) = " + hashedPW);
    }
    
    
    /**
     * <p>Generate SHA-256 hash according to the given salt and password.</p>
     * <p><strong>Note: </strong>This can be reused in the main program for hash
     * generation.</p>
     * 
     * @param password raw password
     * @param salt cryptographic salt
     * @return the hashed string (empty if failed to encode hashed value with Base64)
     */
    public static String generateHash(String password, String salt) {
        String result = "";
        try {
            String salt_pw = salt + password;
            byte[] hashedPW = MessageDigest.getInstance("SHA-256").digest(salt_pw.getBytes());

            result = Base64.getEncoder().encodeToString(hashedPW);
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    /**
     * Main driver for providing a command line interface to generate salts and
     * hashes.
     * 
     * @param args
     * @throws NoSuchAlgorithmException 
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner input = new Scanner(System.in);
        
        System.out.println("Enter user ID:");
        String userID = input.nextLine();
        System.out.println("Enter password:");
        String password = input.nextLine();
        
        initializeHash(userID, password);
        
        // -- test result -----------------------------
        System.out.println("Enter user ID for authentication test:");
        String authUserID = input.nextLine();
        System.out.println("Enter password for authentication test:");
        String authPass = input.nextLine();
        
        if (test.containsKey(authUserID)) {
            String salt = test.get(authUserID)[0];
            String hash = test.get(authUserID)[1];
            String authHash = generateHash(authPass, salt);
            if (authHash.equals(hash)) {
                System.out.println("Validated user ID and password pair");
            } else {
                System.out.println("Failed to validate this user ID and password pair.");
            }
        } else {
            System.out.println("Cannot find user record.");
        }
        
    }
    
}
