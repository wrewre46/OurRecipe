package project.OurRecipe.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import project.OurRecipe.Config.Auth.PrincipalDetails;
import project.OurRecipe.Domain.Member;
import project.OurRecipe.Repository.BoardRepository;
import project.OurRecipe.Repository.MemberRepository;
import project.OurRecipe.Validation.MemberValidation;

@Slf4j
@Controller
@RequestMapping("/MyPage")
public class MyPageController {
    @Autowired private BoardRepository boardRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberValidation memberValidation;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(memberValidation);
    }
    @GetMapping
    public String MyPage(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        Member member = principalDetails.getMember();
        model.addAttribute("member",member);
        return "mypage/mypage";
    }

    @GetMapping("/NicknameForm")
    public String Nickname(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        Member member = memberRepository.findByMemberID(principalDetails.getMember().getMemberID());
        model.addAttribute("member", member);
        return "mypage/nicknameForm";
    }

    @PostMapping("/NicknameForm")
    public String UpdateNickname(@Validated @ModelAttribute Member member, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(bindingResult.hasErrors()){
            log.info("error={}", bindingResult);
            return "mypage/nicknameForm";
        }
        Member Spare_member = principalDetails.getMember();
        log.info("MemberID={}",member.getMemberID());
        log.info("MemberNickname={}", member.getNickname());
        memberRepository.UpdateNickname(member.getNickname(),Spare_member.getMemberID());
        boardRepository.UpdateNickname(member.getNickname(),Spare_member.getMemberID());
        return "redirect:/MyPage/NicknameForm";
    }
}
