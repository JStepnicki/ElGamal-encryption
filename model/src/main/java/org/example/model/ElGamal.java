package org.example.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class ElGamal {

    BigInteger p;
    BigInteger g; // public keys
    BigInteger r; // public keys
    BigInteger h; // public keys
    BigInteger a; // private key
    BigInteger rPrim;
    BigInteger p_1;
    private final int keyLength = 2048;
    private final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    private final Random random = new SecureRandom();

    public ElGamal() throws NoSuchAlgorithmException {
    }

    public void generateKeys() {
        p = BigInteger.probablePrime(keyLength, random);
        g = new BigInteger(keyLength, random);
        do {
            g = new BigInteger(keyLength, random);
        } while(g.compareTo(p) >= 0);
        a = new BigInteger(keyLength, random); // to będzie klucz prywatny
        do {
            a = new BigInteger(keyLength, random);
        } while(a.compareTo(p) >= 0);

        h = g.modPow(a, p); // (g^a)%p tylko na BigIntach
        p_1 = p.subtract(BigInteger.ONE);
    }


    public BigInteger[] sign(byte[] document) {
        messageDigest.update(document);
        BigInteger DocumentHash = new BigInteger(1, messageDigest.digest());
        r = BigInteger.probablePrime(keyLength, random);
        while(true) {
            if(r.gcd(p_1).equals(BigInteger.ONE)) {
                break;
            }
            else {
                r = r.nextProbablePrime();
            }
        }
        BigInteger s1 = g.modPow(r, p);// (g^r)%p tylko na BigIntach
        rPrim = r.modInverse(p_1);//(r^-1)%(p-1) ;D
        BigInteger s2 = DocumentHash.subtract(a.multiply(s1)).multiply(rPrim).mod(p_1);// (hash - a*s1) * r'mod(n-1)
        BigInteger[] result = new BigInteger[2];
        result[0] = s1;
        result[1] = s2;
        return result;
    }

    public boolean verify(byte[] document, BigInteger[] signature) {
        messageDigest.update(document);
        BigInteger DocumentHash = new BigInteger(1, messageDigest.digest());
        BigInteger result1 = g.modPow(DocumentHash, p); // g^hash %p
        BigInteger result2 = h.modPow(signature[0], p).multiply(signature[0].modPow(signature[1], p)).mod(p); //h^s1 * s1^s2 %N
        return result1.compareTo(result2) == 0;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getH() {
        return h;
    }

    public BigInteger getA() {
        return a;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    public void setG(BigInteger g) {
        this.g = g;
    }

    public void setH(BigInteger h) {
        this.h = h;
    }

    public void setA(BigInteger a) {
        this.a = a;
    }
}
