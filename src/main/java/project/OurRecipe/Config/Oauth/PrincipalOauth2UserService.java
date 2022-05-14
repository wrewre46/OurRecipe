package project.OurRecipe.Config.Oauth;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.OurRecipe.Config.Auth.PrincipalDetails;
import project.OurRecipe.Config.Oauth.provider.GoogleUserInfo;
import project.OurRecipe.Config.Oauth.provider.OAuth2UserInfo;
import project.OurRecipe.Domain.Member;
import project.OurRecipe.Repository.MemberRepository;


@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	//구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Autowired
	private MemberRepository memberRepository;


	// userRequest 는 code를 받아서 accessToken을 응답 받은 객체
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회
		// code를 통해 구성한 정보
		System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());
		// token을 통해 응답받은 회원정보
		System.out.println("oAuth2User : " + oAuth2User);
	
		return processOAuth2User(userRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		// Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
		OAuth2UserInfo oAuth2UserInfo = null;
		if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		}else {
			System.out.println("구글만 지원 가능합니다.");
		}

		//System.out.println("oAuth2UserInfo.getProvider() : " + oAuth2UserInfo.getProvider());
		//System.out.println("oAuth2UserInfo.getProviderId() : " + oAuth2UserInfo.getProviderId());
		Optional<Member> memberOptional =
				memberRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

		Member member;
		if (memberOptional.isPresent()) {
			member = memberOptional.get();
			// member가 존재하면 update 해주기
			member.setEmail(oAuth2UserInfo.getEmail());
			//memberRepository.MemberSave(member);
		} else {
			// member의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
			member = Member.builder()
					.ID(memberRepository.MemberCount()+1)
					.MemberID(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
					.Nickname(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
					.Password("NULL")
					.Email(oAuth2UserInfo.getEmail())
					.Role("ROLE_USER")
					.Provider(oAuth2UserInfo.getProvider())
					.ProviderID(oAuth2UserInfo.getProviderId())
					.MemberCreateDate(Date.valueOf(LocalDate.now()))
					.MemberCreateTime(Time.valueOf(LocalTime.now()))
					.build();
			memberRepository.MemberSave(member);
		}

		return new PrincipalDetails(member, oAuth2User.getAttributes());
	}
}
