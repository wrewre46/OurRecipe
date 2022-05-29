package project.OurRecipe.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Time;

@Data
@NoArgsConstructor
public class BoardComment {
    private int BoardID;
    private String MemberID;
    private String MemberNickname;
    @NotBlank
    private String Comment;
    private Date CommentCreateDate;
    private Time CommentCreateTime;
    private int CommentAvailable;

    public BoardComment(int boardID, String memberID, String memberNickname, String comment, Date commentCreateDate, Time commentCreateTime) {
        BoardID = boardID;
        MemberID = memberID;
        MemberNickname = memberNickname;
        Comment = comment;
        CommentCreateDate = commentCreateDate;
        CommentCreateTime = commentCreateTime;
    }
}
