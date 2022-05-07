package project.OurRecipe.Domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter@Setter
@NoArgsConstructor
public class Board {
    private String MemberID;
    private String MemberNickname;
    private int BoardID;
    @NonNull
    private String BoardTitle;
    @NonNull
    private String BoardContent;
    private int BoardAvailable;
    private Date WriteDate;
    private Time WriteTime;
    private int RecommendCount;
    public Board(String memberID,String memberNickname, int boardID, String boardTitle, String boardContent, Date writeDate, Time writeTime) {
        MemberID = memberID;
        MemberNickname=memberNickname;
        BoardID = boardID;
        BoardTitle = boardTitle;
        BoardContent = boardContent;
        WriteDate = writeDate;
        WriteTime = writeTime;
    }


}
