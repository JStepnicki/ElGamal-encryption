package org.example.model;

public class Utils {

    public static byte[] hexToBytes(String text) {
        StringBuilder padded = new StringBuilder(text);
        if (padded.length() % 2 != 0) {
            padded.insert(0, "0");
        }
        text = padded.toString();
        text = text.toUpperCase();
        int len = text.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(text.charAt(i), 16) << 4)
                    + Character.digit(text.charAt(i + 1), 16));
        }
        return data;
    }

}
