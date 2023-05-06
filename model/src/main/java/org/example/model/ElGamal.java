package org.example.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class ElGamal {

    BigInteger N;
    BigInteger g; // public keys
    BigInteger r; // public keys
    BigInteger h; // public keys
    BigInteger a; // private key
    BigInteger rPrim;
    BigInteger pSubstrateOne;
    private final int keyLength = 2048;
    private final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    private final Random random = new SecureRandom();

    public ElGamal() throws NoSuchAlgorithmException {
    }

    public void generateKeys() {
        N = BigInteger.probablePrime(keyLength + 1, random);
        g = new BigInteger(keyLength, random);
        a = new BigInteger(keyLength, random); // to będzie klucz prywatny
        h = g.modPow(a, N); // (g^a)%N tylko na BigIntach
        pSubstrateOne = N.subtract(BigInteger.ONE);
    }


    public BigInteger[] sign(byte[] document) {
        messageDigest.update(document);
        BigInteger DocumentHash = new BigInteger(1, messageDigest.digest());
        r = BigInteger.probablePrime(keyLength, random);
        BigInteger s1 = g.modPow(r, N);// (g^r)%p tylko na BigIntach
        rPrim = r.modInverse(pSubstrateOne);//(r^-1)%(N-1) ;D
        BigInteger s2 = DocumentHash.subtract(a.multiply(s1)).multiply(rPrim).mod(pSubstrateOne);
        BigInteger[] result = new BigInteger[2];
        result[0] = s1;
        result[1] = s2;
        return result;
    }

    public boolean verify(byte[] document, BigInteger[] signature) {
        messageDigest.update(document);
        BigInteger DocumentHash = new BigInteger(1, messageDigest.digest());
        BigInteger result1 = g.modPow(DocumentHash, N); // g^hash %p
        BigInteger result2 = h.modPow(signature[0], N).multiply(signature[0].modPow(signature[1], N)).mod(N); //h^s1 * s1^s2 %N
        return result1.compareTo(result2) == 0;
    }

    public BigInteger getN() {
        return N;
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

    public void setN(BigInteger n) {
        N = n;
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
