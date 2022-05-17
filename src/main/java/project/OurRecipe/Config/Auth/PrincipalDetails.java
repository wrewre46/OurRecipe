package project.OurRecipe.Config.Auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;
import org.springframework.stereotype.Component;
import project.OurRecipe.Domain.Member;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다. (Security ContextHolder)
//오브젝트 타입=> Authentication 타입 객체
//Authentication 안에는 User정보가 있어야 한다.
//User오브젝트타입 ==> UserDetails 타입 객체
//Security Session => Authentication => UserDetails
//Security Session에는 Authentication 객체가 들어갈 수 있는데 스프링 시큐리티로 로그인하면 UserDetails객체로 리턴이 오고
//OAuth로 로그인하면 OAuth2User로 리턴이 오기때문에 PrincipalDetails에서 인터페이스로 받아서 상황에 따라 Authentication에 객체로
//받게 해줄 수 있다.
@Data
@Component
@NoArgsConstructor
public class PrincipalDetails implements UserDetails, OAuth2User{
	private Member member;
	private Map<String, Object> attributes;
	public PrincipalDetails(Member member) {
		this.member = member;
	}
	public PrincipalDetails(Member member, Map<String, Object> attributes) {
		this.member = member;
		this.attributes=attributes;
	}
	//해당 Member의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {			
			@Override
			public String getAuthority() {
				return member.getRole();
			}
		}); 		
		return collect;
	}
	@Override
	public String getPassword() {
		
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		
		return member.getMemberID();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		//우리 사이트가 1년동안 회원이 로그인을 안하면 휴면 계정으로 하기로 한다
		//현재시간-로그인시간 => 1년이 지나면 false 반환
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return attributes;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
