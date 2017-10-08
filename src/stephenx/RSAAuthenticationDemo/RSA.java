package stephenx.RSAAuthenticationDemo;

import java.math.BigInteger;
import java.util.Random;

/**
 *  RSA Algorithm from CLR using Big Integer.
 * 
 * 1. Select at random two large prime numbers p and q.
 * 2. Compute n by the equation n = p * q.
 * 3. Compute phi(n)=  (p - 1) * ( q - 1)
 * 4. Select a small odd integer e that is relatively prime to phi(n).
 * 5. Compute d as the multiplicative inverse of e modulo phi(n). A theorem in
 *    number theory asserts that d exists and is uniquely defined.
 * 6. Publish the pair P = (e,n) as the RSA public key.
 * 7. Keep secret the pair S = (d,n) as the RSA secret key.
 * 8. To encrypt a message M compute C = M^e (mod n)
 * 9. To decrypt a message C compute M = C^d (mod n)
 */
 
public class RSA {
    
    /**
     * Encrypt content using the public key.
     * @param content byte array to be encrypted
     * @param n the modulus in the public key
     * @param e the exponent in the public key
     * @return a byte array representing the encrypted content
     */
    public static byte[] encrypt(byte[] content, BigInteger n, BigInteger e) {
        // To encrypt a message M compute C = M^e (mod n)
        BigInteger m = new BigInteger(content);
        BigInteger c = m.modPow(e, n);
        
        return c.toByteArray();
    }
    
    
    /**
     * Decrypt content using the private key.
     * @param encrypted the encrypted content as a byte array
     * @param n the modulus in the private key
     * @param d the exponent in the private key
     * @return the decrypted content as a byte array
     */
    public static byte[] decrypt(byte[] encrypted, BigInteger n, BigInteger d) {
        // To decrypt a message C compute M = C^d (mod n)
        BigInteger c = new BigInteger(encrypted);
        BigInteger clear = c.modPow(d, n);  // clear is the resulting clear text
        
        return clear.toByteArray();
    }
  
    
    /**
     * Generate public / private keys using Java BigInteger. Recommend using Java's
     * keytool instead.
     * @param args 
     */
    public static void main(String[] args) {
        // Each public and private key consists of an exponent and a modulus
        BigInteger n; // n is the modulus for both the private and public keys
        BigInteger e; // e is the exponent of the public key
        BigInteger d; // d is the exponent of the private key

        Random rnd = new Random();

        // Step 1: Generate two large random primes.
        // We use 16 bytes here, but best practice for security is 2048 bits.
        // Change 128 to 2048, recompile, and run the program again and you will
        // notice it takes much longer to do the math with that many bits.
        // Note: this is not a secure and reliable way to get big prime numbers
        // in practice.
        BigInteger p = new BigInteger(16*8,100,rnd);
        BigInteger q = new BigInteger(16*8,100,rnd);

        // Step 2: Compute n by the equation n = p * q.
        n = p.multiply(q);

        // Step 3: Compute phi(n) = (p-1) * (q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Step 4: Select a small odd integer e that is relatively prime to phi(n).
        // By convention the prime 65537 is used as the public exponent.
        e = new BigInteger ("65537");

        // Step 5: Compute d as the multiplicative inverse of e modulo phi(n).
        d = e.modInverse(phi);

        System.out.println(" e = " + e);  // Step 6: (e,n) is the RSA public key
        System.out.println(" d = " + d);  // Step 7: (d,n) is the RSA private key
        System.out.println(" n = " + n);  // Modulus for both keys

        // Encode a simple message.
        String s = "RSA is very cool!";
        byte[] c = encrypt(s.getBytes(), n, e);
        BigInteger c_encoded = new BigInteger(c);

        // Then decrypt it.
        String clearStr = new String(decrypt(c, n, d));

        System.out.println("Cypher text = " + c_encoded);
        System.out.println("Clear text = " + clearStr);
    }
}
