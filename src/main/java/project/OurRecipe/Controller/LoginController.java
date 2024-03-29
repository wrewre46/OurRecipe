package project.OurRecipe.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
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
	@Autowired private PrincipalDetails principalDetails;
	@InitBinder
	public void init(WebDataBinder dataBinder) {
		log.info("init binder {}", dataBinder);
		dataBinder.addValidators(memberValidation);
	}
	@GetMapping("/login")
	public String loginForm(@RequestParam(value = "error", required = false)String error,
							@RequestParam(value = "exception", required = false)String exception,
							Model model, HttpServletRequest request) {
			model.addAttribute("error", error);
			model.addAttribute("exception", exception);
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
	public String join(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
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
	@Secured("ROLE_ADMIN")//접근 권한 없으면 이 함수를 실행시킬 수 없다.
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}



}


