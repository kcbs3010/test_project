/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryptionapp;

import javax.mail.internet.MimeUtility;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author kunal
 */
public class EncryptionApp {

    private final static String SECRETKEY = "LUCKY11@_KRIO_SOLUTIONS_05_04_18";
    private final static String HEX = "0123456789abcdef";

    public static String encrypt(String strToEncrypt) {
        try {
            SecretKey key = new SecretKeySpec(SECRETKEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return toHex(cipher.doFinal(strToEncrypt.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            Logger.getLogger(EncryptionApp.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public static String decrypt(String encryptedText) {
        try {
            SecretKey key = new SecretKeySpec(SECRETKEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(toByte(encryptedText)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            Logger.getLogger(EncryptionApp.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    private static byte[] toByte(String hexString) {
        try {
            int len = hexString.length() / 2;
            byte[] result = new byte[len];
            for (int i = 0; i < len; i++) {
                result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
            }
            return result;
        } catch (Exception e) {
            Logger.getLogger(EncryptionApp.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    private static String toHex(byte[] stringBytes) {
        try {
            StringBuilder result = new StringBuilder(2 * stringBytes.length);
            for (int i = 0; i < stringBytes.length; i++) {
                result.append(HEX.charAt((stringBytes[i] >> 4) & 0x0f)).append(HEX.charAt(stringBytes[i] & 0x0f));
            }
            return result.toString();
        } catch (Exception e) {
            Logger.getLogger(EncryptionApp.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public static String encodeFileToBase64Binary(File file) {
        String str = "";
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream base64OutputStream = MimeUtility.encode(baos, "base64");
            base64OutputStream.write(bytes);
            base64OutputStream.close();
            str = baos.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;

    }
}
