package io.jaeyeon.jdrive.global.security.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class JwtKeyGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Generate a secure 256-bit key
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Generated secret key: " + secretString);

        // 혹은 더 간단히
        String randomKey = Encoders.BASE64.encode(SecureRandom.getInstanceStrong().generateSeed(32));
        System.out.println("Alternative secret key: " + randomKey);
    }
}
