package project.OurRecipe.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project.OurRecipe.Config.Auth.PrincipalDetails;
import project.OurRecipe.Domain.Member;
import project.OurRecipe.Repository.MemberRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;


@Controller
public class LoginController {
	
	@Autowired private MemberRepository memberRepository;
	@Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication,
			@AuthenticationPrincipal PrincipalDetails userDetails) {
		System.out.println("/test/login ===========");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("authentication:" + principalDetails.getMember());
		System.out.println("userDetails:" + userDetails.getUsername());
		return "세션 정보 확인하기";
		
	}
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication, 
			@AuthenticationPrincipal OAuth2User oauth) {
		System.out.println("/test/login ===========");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("authentication:" + oauth2User.getAttributes());
		System.out.println("oauth2User:" + oauth.getAttributes());
		
		return "OAuth 세션 정보 확인하기";
		
	}
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails : "+principalDetails.getMember());
		return "user";
	}
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "login/loginForm";
	}
	@GetMapping("/joinForm")
	public String joinForm() {
		return "login/joinForm";
	}
	@PostMapping("/join")
	public String join(@RequestParam String MemberID,
					   @RequestParam String Password,
					   @RequestParam String Email,
					   @RequestParam String Nickname) {
		Member member = new Member(
				memberRepository.MemberCount()+1,
				MemberID,
				Password,
				Email,
				Nickname,
				"ROLE_USER",
				null,null,
				Date.valueOf(LocalDate.now()),
				Time.valueOf(LocalTime.now()));
		String rawPassword=member.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		member.setPassword(encPassword);
		memberRepository.MemberSave(member);
		return "redirect:/loginForm";
	}
	@Secured("ROLE_ADMIN")//접근 권한 없으면 이 함수를 실행시킬 수 없다.
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "data 정보";
	}
}


