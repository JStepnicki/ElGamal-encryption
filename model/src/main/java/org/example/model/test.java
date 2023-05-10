package org.example.model;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Assert;
import org.junit.Test;


public class test {
    @Test
    public void test1() throws NoSuchAlgorithmException {
        ElGamal testowy = new ElGamal();
        testowy.generateKeys();
        byte[] document = new byte[10];
        Random random = new Random();
        random.nextBytes(document);
        BigInteger[] signature = testowy.sign(document);
        assertEquals(true,testowy.verify(document,signature));
        String napis =  new String();
        napis = "test";
        testowy.generateKeys();
        assertEquals(false,testowy.verify(document,signature));
        signature = testowy.sign(document);
        assertEquals(true,testowy.verify(document,signature));
        signature = testowy.sign(document);
        assertEquals(true,testowy.verify(document,signature));
        signature = testowy.sign(document);
        assertEquals(true,testowy.verify(document,signature));
        signature = testowy.sign(document);
        assertEquals(true,testowy.verify(document,signature));
        signature = testowy.sign(document);
        assertEquals(true,testowy.verify(document,signature));
        signature = testowy.sign(document);
        assertEquals(true,testowy.verify(document,signature));
        signature = testowy.sign(document);
        assertEquals(true,testowy.verify(document,signature));



        signature = testowy.sign(napis.getBytes());
        assertEquals(true,testowy.verify(napis.getBytes(),signature));
        signature = testowy.sign(napis.getBytes());
        assertEquals(true,testowy.verify(napis.getBytes(),signature));
        signature = testowy.sign(napis.getBytes());
        assertEquals(true,testowy.verify(napis.getBytes(),signature));
        signature = testowy.sign(napis.getBytes());
        assertEquals(true,testowy.verify(napis.getBytes(),signature));
        signature = testowy.sign(napis.getBytes());
        assertEquals(true,testowy.verify(napis.getBytes(),signature));
        signature = testowy.sign(napis.getBytes());
        assertEquals(true,testowy.verify(napis.getBytes(),signature));
    }
}
