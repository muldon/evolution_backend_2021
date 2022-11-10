//*********************************************************************
//
//                       Copyright (C) 2022-2022
//                       Carnegie Learning Inc.
//
//                       All Rights Reserved.
//
//This program is the subject of trade secrets and intellectual
//property rights owned by Carnegie Learning.
//
//This legend must continue to appear in the source code despite
//modifications or enhancements by any party.
//
//*********************************************************************

package com.github.eduoliveiradev.evolution.utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * OAuth handling utils
 */
public class OAuthUtils {
	private static final String PKCS_1_PEM_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
	private static final String PKCS_1_PEM_FOOTER = "-----END RSA PRIVATE KEY-----";
	private static final String PKCS_8_PEM_HEADER = "-----BEGIN PRIVATE KEY-----";
	private static final String PKCS_8_PEM_FOOTER = "-----END PRIVATE KEY-----";
	private static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";
	private static final String PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";
	

    static final Logger log = LoggerFactory.getLogger(OAuthUtils.class);

    private OAuthUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static RSAPublicKey loadPublicKey(String key) throws GeneralSecurityException {
        String publicKeyContent = key.replace("\\n", "").replace("\n", "").replace(PUBLIC_KEY_HEADER, "").replace(PUBLIC_KEY_FOOTER, "");
        KeyFactory kf = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        return (RSAPublicKey) kf.generatePublic(keySpecX509);
    }

     
    /*
    * Load the private key handling the formats (PKCS#1 and PKCS#8)
    */    
    public static PrivateKey loadPrivateKey(String keyDataString) throws GeneralSecurityException, IOException {

        if (keyDataString.contains(PKCS_1_PEM_HEADER)) {
            // OpenSSL / PKCS#1 Base64 PEM encoded file
            keyDataString = keyDataString.replace(PKCS_1_PEM_HEADER, "");
            keyDataString = keyDataString.replace(PKCS_1_PEM_FOOTER, "");
            keyDataString = keyDataString.replaceAll("\\s", "");
            return readPkcs1PrivateKey(Base64.getDecoder().decode(keyDataString));
        }

        // We assume it's a PKCS#8 DER encoded binary file        
        keyDataString = keyDataString.replace(PKCS_8_PEM_HEADER, "");
        keyDataString = keyDataString.replace(PKCS_8_PEM_FOOTER, "");
        keyDataString = keyDataString.replaceAll("\\s", "");
        return readPkcs8PrivateKey(Base64.getDecoder().decode(keyDataString));
        
    }

    private static PrivateKey readPkcs8PrivateKey(byte[] pkcs8Bytes) throws GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SunRsaSign");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8Bytes);
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Unexpected key format!", e);
        }
    }

    private static PrivateKey readPkcs1PrivateKey(byte[] pkcs1Bytes) throws GeneralSecurityException {
        // We can't use Java internal APIs to parse ASN.1 structures, so we build a PKCS#8 key Java can understand
        int pkcs1Length = pkcs1Bytes.length;
        int totalLength = pkcs1Length + 22;
        byte[] pkcs8Header = new byte[] {
                0x30, (byte) 0x82, (byte) ((totalLength >> 8) & 0xff), (byte) (totalLength & 0xff), // Sequence + total length
                0x2, 0x1, 0x0, // Integer (0)
                0x30, 0xD, 0x6, 0x9, 0x2A, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xF7, 0xD, 0x1, 0x1, 0x1, 0x5, 0x0, // Sequence: 1.2.840.113549.1.1.1, NULL
                0x4, (byte) 0x82, (byte) ((pkcs1Length >> 8) & 0xff), (byte) (pkcs1Length & 0xff) // Octet string + length
        };
        byte[] pkcs8bytes = join(pkcs8Header, pkcs1Bytes);
        return readPkcs8PrivateKey(pkcs8bytes);
    }
    
    private static byte[] join(byte[] byteArray1, byte[] byteArray2){
        byte[] bytes = new byte[byteArray1.length + byteArray2.length];
        System.arraycopy(byteArray1, 0, bytes, 0, byteArray1.length);
        System.arraycopy(byteArray2, 0, bytes, byteArray1.length, byteArray2.length);
        return bytes;
    }


}
