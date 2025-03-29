package guru.qa.rococo.utils;

import lombok.SneakyThrows;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class OAuthUtils {

    private static SecureRandom secureRandom = new SecureRandom();

    public static String generateCodeVerifier() {
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
    }

    @SneakyThrows
    public static String generateCodeChallenge(String codeVerifier) {
        byte[] bytes = codeVerifier.getBytes(Charset.forName("US-ASCII"));
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
