/*
package example.demo.domain.member;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class SecurityMember implements UserDetails {
    private final Member member;
    @Override
    public String getUsername() {
        return member.getUserName();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(()->member.getMemberStatus().toString());
    }

}
*/
