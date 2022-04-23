package project.OurRecipe.Domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter@Setter
public class Board {
    private String UserID;
    private int BoardID;
    @NonNull
    private String BoardTitle;
    @NonNull
    private String BoardContent;
    private int BoardAvailable;
    private Date WriteDate;
    private Time WriteTime;
    private int RecommendCount;
    public Board(){}
    public Board(String userID, int boardID, String boardTitle, String boardContent, Date writeDate,Time writeTime) {
        UserID = userID;
        BoardID = boardID;
        BoardTitle = boardTitle;
        BoardContent = boardContent;
        WriteDate = writeDate;
        WriteTime = writeTime;
    }


}
