package project.OurRecipe.Domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;

@Data
@NoArgsConstructor
public class Member {
    private int ID;
    @NotBlank
    private String MemberID;
    @NotBlank
    private String Password;
    @NotBlank
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String Email;
    @NotBlank
    private String Nickname;
    private String Role;
    private String Provider;
    private String ProviderID;
    private Date MemberCreateDate;
    private Time MemberCreateTime;

    @Builder
    public Member(int ID,String MemberID, String Password, String Email, String Nickname,String Role, String Provider, String ProviderID,
                  Date MemberCreateDate, Time MemberCreateTime) {
        this.ID=ID;
        this.MemberID = MemberID;
        this.Password = Password;
        this.Email = Email;
        this.Nickname=Nickname;
        this.Role = Role;
        this.Provider = Provider;
        this.ProviderID = ProviderID;
        this.MemberCreateDate = MemberCreateDate;
        this.MemberCreateTime = MemberCreateTime;

    }
}
