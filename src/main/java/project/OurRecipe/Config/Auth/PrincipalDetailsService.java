package project.OurRecipe.Config.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.OurRecipe.Domain.Member;
import project.OurRecipe.Repository.MemberRepository;

// 시큐리티 설정에서 loginProcessingUrl("/login"); 으로 해놓았기 때문에
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행된다.
@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private MemberRepository memberRepository;
	
	//시큐리티 session에 들어갈 수 있는거는 Authentication타입 , Authentication에는 UserDetails타입이 들어감
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member memberEntity = memberRepository.findByMemberID(username);
		if(memberEntity !=null) {
			return new PrincipalDetails(memberEntity);
		}
		return null;
	}

}
