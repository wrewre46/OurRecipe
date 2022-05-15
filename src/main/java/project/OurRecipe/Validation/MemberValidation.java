package project.OurRecipe.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.OurRecipe.Domain.Member;
import project.OurRecipe.Repository.MemberRepository;

@Component
public class MemberValidation implements Validator {
    @Autowired private MemberRepository memberRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return Member.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Member member = (Member) target;
        if(memberRepository.CountMemberID(member.getMemberID())>=1){
            errors.rejectValue("MemberID","duplicated");
        }
        if(memberRepository.CountMemberEmail(member.getEmail())>=1){
            errors.rejectValue("Email","duplicated");
        }
        if(memberRepository.CountMemberNickname(member.getNickname())>=1){
            errors.rejectValue("Nickname","duplicated");
        }
    }
}
