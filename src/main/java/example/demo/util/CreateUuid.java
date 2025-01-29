package example.demo.util;

import example.demo.error.RestApiException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class CreateUuid {
    public static String createShortUuid(){
        try {
            String uuidString = UUID.randomUUID().toString();
            byte[] uuidStringBytes = uuidString.getBytes(StandardCharsets.UTF_8);

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(uuidStringBytes); // 해시 계산 수행

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 4; j++) { // 앞 4바이트만 사용
                sb.append(String.format("%02x", hashBytes[j]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RestApiException(UtilErrorCode.INTERNAL_ERROR); // 예외 처리
        }
    }
}
