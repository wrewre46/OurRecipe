package project.OurRecipe.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
@Slf4j
@Service
public class UserLoginFailHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        String ErrorMessage;
        if (exception instanceof BadCredentialsException) {
            ErrorMessage = URLEncoder("아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해 주세요.");
        } else if (exception instanceof InternalAuthenticationServiceException) {
           ErrorMessage = URLEncoder("아이디, 비밀번호를 확인해 주세요.");
        } else if (exception instanceof UsernameNotFoundException) {
            ErrorMessage = URLEncoder("계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.");
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            ErrorMessage = URLEncoder("인증 요청이 거부되었습니다. 관리자에게 문의하세요.");
        } else {
            ErrorMessage = URLEncoder("알 수 없는 이유로 로그인에 실패하였습니다 관리자에게 문의하세요.");
        }
        log.info("error={}", exception.getMessage());
        setDefaultFailureUrl("/login?error=true&exception="+ErrorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
    public String URLEncoder(String ErrorMessage) throws UnsupportedEncodingException {
        ErrorMessage = URLEncoder.encode(ErrorMessage, "UTF-8");
        return ErrorMessage;
    }
}
