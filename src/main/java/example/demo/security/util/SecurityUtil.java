package example.demo.security.util;

import example.demo.security.auth.CustomMemberDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
//서바스단에서 현재 로그인한 유저의 아이디를 가져오는 util클래스입니다.
@Component
public class SecurityUtil {
    public static Long getCurrentMemberId(){
        final Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || authentication.getName()==null){
            throw new RuntimeException("No authentication info");
        }
        CustomMemberDetails memberDetails=(CustomMemberDetails) authentication.getPrincipal();
        Long memberId=memberDetails.getMember().getMemberId();
        return memberId;
    }
}
