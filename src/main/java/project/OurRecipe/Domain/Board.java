package project.OurRecipe.Domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter@Setter
public class Board {
    private String UserID;
    private int BoardID;
    private String BoardTitle;
    private String BoardContent;
    private boolean BoardAvailable;
    private LocalDateTime WriteTime;
    private int RecommendCount;

}
