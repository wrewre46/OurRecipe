package project.OurRecipe.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.OurRecipe.Config.Auth.PrincipalDetails;
import project.OurRecipe.Domain.Member;
import project.OurRecipe.Repository.MemberRepository;
import project.OurRecipe.Validation.MemberValidation;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;


@Controller
@Slf4j
public class LoginController {
	
	@Autowired private MemberRepository memberRepository;
	@Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired private MemberValidation memberValidation;
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
	
	@GetMapping("/login")
	public String loginForm(HttpServletRequest request) {

			String uri = request.getHeader("Referer");
			try {

				if (!uri.contains("/login")) {
					request.getSession().setAttribute("prevPage",
							request.getHeader("Referer"));
					return "login/loginForm";
				}

			} catch (NullPointerException E) {
				return "login/loginForm";
			}
		return "login/loginForm";
	}

	@GetMapping("/join")
	public String joinForm(Model model) {
		model.addAttribute("member",new Member());
		return "login/joinForm";
	}
	@PostMapping("/join")
	public String join(@Validated @ModelAttribute Member member, BindingResult bindingResult, Model model) {
		memberValidation.validate(member,bindingResult);
		if(bindingResult.hasErrors()){
			log.info("error={}", bindingResult);
			return "login/joinForm";
		}
		member.setID(memberRepository.MemberCount()+1);
		member.setRole("ROLE_USER");
		member.setMemberCreateDate(Date.valueOf(LocalDate.now()));
		member.setMemberCreateTime(Time.valueOf(LocalTime.now()));
		String rawPassword=member.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		member.setPassword(encPassword);
		memberRepository.MemberSave(member);
		return "redirect:/login";
	}
//	@GetMapping("/*")
//	public String header(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
//		Member member = principalDetails.getMember();
//		log.info("MemberNickname={}", member.getNickname());
//		model.addAttribute("member", member);
//		return "fragments/header";
//
//	}
	@Secured("ROLE_ADMIN")//접근 권한 없으면 이 함수를 실행시킬 수 없다.
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}

}


