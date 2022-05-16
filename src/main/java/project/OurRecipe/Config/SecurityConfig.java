package project.OurRecipe.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.OurRecipe.Config.Oauth.PrincipalOauth2UserService;
import project.OurRecipe.Service.UserLoginFailHandler;
import project.OurRecipe.Service.UserLoginSuccessHandler;


@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다.
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled = true) //secured 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	@Autowired
	UserLoginSuccessHandler userLoginSuccessHandler;
	@Autowired
	UserLoginFailHandler userLoginFailHandler;
	
	@Bean //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//중복 로그인 방지
		http.sessionManagement().maximumSessions(1)
				.maxSessionsPreventsLogin(false)
				.expiredUrl("/")
				.sessionRegistry(sessionRegistry());
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
		.and()
			.formLogin()
			.loginPage("/login")//login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인 진행
			.loginProcessingUrl("/login").successHandler(userLoginSuccessHandler).failureHandler(userLoginFailHandler)


		.and()
				.logout()
				.logoutSuccessUrl("/login")
				.invalidateHttpSession(true).deleteCookies("JSESSIONID")
				.clearAuthentication(true)
				.permitAll()
				.deleteCookies("JSESSIONID","remember-me")

		.and()
			.oauth2Login()
			.loginPage("/login")
				.successHandler(userLoginSuccessHandler)
				//구글 로그인이 완료된 뒤의 후처리 필요.
			//1. 코드받기(인증), 2. 엑세스 토큰 받음(권한이 생김), 3.사용자프로필 정보를 가져와서 
		    //4. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 한다. 또는, 그 정보가 부족하면 추가적인 회원가입 창이 나와서 회원가입을 진행해야한다.
		    //Tip. (구글)코드를 받는게 아니라 엑세스토큰+사용자 프로필 정보를 받는다
			.userInfoEndpoint()
			.userService(principalOauth2UserService);

		
	}
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

}
