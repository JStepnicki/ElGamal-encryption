import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class ElGamal {

    BigInteger p; // zazwyczaj to N ale w wykaldzie bylo jako "p"
    BigInteger g; // public keys
    BigInteger r; // public keys
    BigInteger h; // public keys
    BigInteger a; // private key
    BigInteger rPrim;
    BigInteger pSubstractOne;
    private final int keyLength = 2048;
    private final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    private final Random random = new SecureRandom();

    public ElGamal() throws NoSuchAlgorithmException {
    }

    public void generateKeys() {
        p = BigInteger.probablePrime(keyLength + 1, random);
        g = new BigInteger(keyLength, random);
        a = new BigInteger(keyLength, random); // to będzie klucz prywatny
        h = g.modPow(a, p); // (g^a)%p tylko na BigIntach
        pSubstractOne = p.subtract(BigInteger.ONE);
    }


    public BigInteger[] sign(byte[] document) {
        messageDigest.update(document);
        BigInteger signature = new BigInteger(1, messageDigest.digest());
        r = new BigInteger(keyLength, random);
        BigInteger s1 = g.modPow(r, p);// (g^r)%p tylko na BigIntach
        while (true) {
            if (r.gcd(pSubstractOne).equals(BigInteger.ONE))//sprawdzamy czy NWD(r,p-1) = 1 jezlie nie losujemy inne r
                break;
            else
                r = r.nextProbablePrime();
        }
        rPrim = r.modInverse(pSubstractOne);//(r^-1)%(p-1) ;D
        BigInteger temp = signature.subtract((a.multiply(s1)));// signature - a * s1
        BigInteger s2 = temp.multiply(rPrim.mod(pSubstractOne)); // temp * rPrim%(p-1)
        BigInteger[] result = new BigInteger[2];
        result[0] = s1;
        result[1] = s2;
        return result;
    }

    public boolean verify(byte[] document, BigInteger[] signature) {
        messageDigest.update(document);
        BigInteger hash = new BigInteger(1, messageDigest.digest());
        BigInteger result1 = g.modPow(hash, p);
        BigInteger result2 = h.modPow(signature[0], p).multiply(signature[0].modPow(signature[1], p)).mod(p);
        return result1.compareTo(result2) == 0;
    }


}
