package example.demo.util;

import example.demo.error.RestApiException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class CreateRandom {
    //비밀번호용 특수문자
    private static final char[] SPECIAL_CHARS={'!', '@', '#', '$', '%','*','&'};
    private static final String ALPHA_NUMERIC="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH=12;
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
            byte[] hashBytes = messageDigest.digest(uuidStringBytes);

            StringBuilder sb = new StringBuilder();

            // 4자리 해시 값 추가 (16진수 변환)
            for (int j = 0; j < 4; j++) {
                sb.append(String.format("%02x", hashBytes[j]));
            }

            Random random = new Random();

            // 랜덤 특수문자 추가
            sb.insert(random.nextInt(sb.length()), SPECIAL_CHARS[random.nextInt(SPECIAL_CHARS.length)]);

            // 랜덤 숫자 추가
            sb.insert(random.nextInt(sb.length()), (char) ('0' + random.nextInt(10)));

            // 랜덤 알파벳 추가
            sb.insert(random.nextInt(sb.length()), ALPHA_NUMERIC.charAt(random.nextInt(ALPHA_NUMERIC.length())));

            // 나머지 랜덤 문자로 채우기 (총 PASSWORD_LENGTH 길이까지)
            while (sb.length() < PASSWORD_LENGTH) {
                sb.insert(random.nextInt(sb.length()), ALPHA_NUMERIC.charAt(random.nextInt(ALPHA_NUMERIC.length())));
            }

            return sb.toString();
        }catch (NoSuchAlgorithmException e) {
            throw new RestApiException(UtilErrorCode.INTERNAL_ERROR); // 예외 처리
        }
    }
}
