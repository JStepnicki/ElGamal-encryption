import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class ElGamal {

    BigInteger p; // zazwyczaj to N ale w wykaldzie bylo jako "p"
    BigInteger g; // public keys
    BigInteger r; // public keys
    BigInteger h; // public keys
    BigInteger a; // private key
    BigInteger s1;
    BigInteger rPrim;
    private final int keyLength = 2048;
    private final Random random = new SecureRandom();

    public void generateKeys() {
        p = BigInteger.probablePrime(keyLength+1,random);
        g = new BigInteger(keyLength, random);
        a = new BigInteger(keyLength, random); // to będzie klucz prywatny
        h = g.modPow(a,p); // (g^a)%p tylko na BigIntach
        BigInteger pSubstractOne = p.subtract(BigInteger.ONE);

// podobno generuje tu juz podpis
        r = new BigInteger(keyLength, random);
        s1 = g.modPow(r,p);// (g^r)%p tylko na BigIntach
        while(true) {
            if(r.gcd(pSubstractOne).equals(BigInteger.ONE))//sprawdzamy czy NWD(r,p-1) = 1 jezlie nie losujemy inne r
                break;
            else
                r = r.nextProbablePrime();
        }
        rPrim = r.modInverse(pSubstractOne);//(r^-1)%(p-1) ;D

    }

}
