package example.demo.util;

import example.demo.error.RestApiException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class CreateRandom {
    //비밀번호용 특수문자
    private static final char[] specialChars={'!', '@', '#', '$', '%'};
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

    public static String createRandomNumber(){
        Random random=new Random();
        String randomNum="";
        for (int i=0;i<6;i++){
            String rand=Integer.toString(random.nextInt(10));
            randomNum+=rand;
        }
        return randomNum;
    }

    public static String createNewPassword(){
        try {
            String uuidString = UUID.randomUUID().toString();
            byte[] uuidStringBytes = uuidString.getBytes(StandardCharsets.UTF_8);

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(uuidStringBytes); // 해시 계산 수행

            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 4; j++) { // 앞 4바이트만 사용
                sb.append(String.format("%02x", hashBytes[j]));
            }

            // 특수문자 배열
            Random random = new Random();

            // 특수문자 3개 추가
            for (int i = 0; i < 3; i++) {
                sb.insert(random.nextInt(sb.length()), specialChars[random.nextInt(specialChars.length)]);
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RestApiException(UtilErrorCode.INTERNAL_ERROR); // 예외 처리
        }
    }
}
