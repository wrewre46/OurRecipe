package project.OurRecipe.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import project.OurRecipe.Config.Auth.PrincipalDetails;
import project.OurRecipe.Domain.Board;
import project.OurRecipe.Domain.Member;
import project.OurRecipe.Domain.UpdateNicknameForm;
import project.OurRecipe.Repository.BoardRepository;
import project.OurRecipe.Repository.MemberRepository;
import project.OurRecipe.Repository.RecommendRepository;

import java.util.*;


@Slf4j
@Controller
@RequestMapping("/MyPage")
@Secured("ROLE_USER")
public class MyPageController {
    @Autowired private BoardRepository boardRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private RecommendRepository recommendRepository;

    @GetMapping
    public String MyPage(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        Member member = principalDetails.getMember();
        model.addAttribute("member",member);
        return "mypage/mypage";
    }

    @GetMapping("/NicknameForm")
    public String Nickname(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        model.addAttribute("member",new Member());
        return "mypage/nicknameForm";
    }

    @PostMapping("/NicknameForm")
    public String UpdateNickname(@Validated @ModelAttribute("member") UpdateNicknameForm updateNicknameForm, BindingResult bindingResult,
                                 @AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        Member present_member = principalDetails.getMember();
        if(bindingResult.hasErrors()){
            log.info("error={}", bindingResult);
            return "mypage/nicknameForm";
        }
        else if(memberRepository.CountMemberNickname(updateNicknameForm.getNickname())>=1){
            bindingResult.rejectValue("Nickname","duplicated","이미 닉네임이 존재합니다.");
            log.info("error={}", bindingResult);
            return "mypage/nicknameForm";
        }
        log.info("MemberNickname={}", updateNicknameForm.getNickname());
        principalDetails.getMember().setNickname(updateNicknameForm.getNickname());
        memberRepository.UpdateNickname(updateNicknameForm.getNickname(),present_member.getMemberID());
        boardRepository.UpdateNickname(updateNicknameForm.getNickname(),present_member.getMemberID());
        model.addAttribute("message","변경이 완료되었습니다.");
        return "redirect:/MyPage/NicknameForm";
    }
    @GetMapping("/WrittenArticle")
    public String WrittenArticle(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        Member member = principalDetails.getMember();
        List<Board> byMemberID =boardRepository.findByMemberID(member.getMemberID());
        model.addAttribute("BoardMemberID", byMemberID);
        return "mypage/writtenarticle";
    }
    @GetMapping("RecommendedArticle")
    public String RecommendedArticle(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        Member member = principalDetails.getMember();
        List<Integer> RecommendedBoardID = recommendRepository.FindRecommended(member);
        List<Board> RecommendedArticle = new ArrayList<>();
        for (int index=RecommendedBoardID.size()-1; index>=0;index--) {
            RecommendedArticle.add(boardRepository.findByBoardID(RecommendedBoardID.get(index)));
        }
        model.addAttribute("RecommendArticle", RecommendedArticle);
        return "mypage/recommended";
    }
}
