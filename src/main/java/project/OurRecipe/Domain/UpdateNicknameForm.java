package project.OurRecipe.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UpdateNicknameForm {
    @NotBlank
    private String Nickname;
}
