package project.OurRecipe.Domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import project.OurRecipe.Repository.PageRepository;

@Getter@Setter
@NoArgsConstructor
public class Page {
    @Autowired PageRepository pageRepository;
    private int NowPage;
    private int StartPage;
    private int EndPage;
    private int TotalBoards;
    private int TotalPage;
    private int PrevPage;
    private int NextPage;
    public Page(int nowPage, int totalPage) {
        this.NowPage = nowPage;
        this.TotalPage = totalPage;
        StartPage = 5*((int) Math.ceil((double) NowPage/5)-1)+1;
        EndPage = StartPage+4;
        PrevPage=NowPage-1;
        NextPage=NowPage+1;
        //페이지 이동시 이전, 다음을 위한 조건문
        if(PrevPage<=0) PrevPage=1;
        if(NextPage>TotalPage) NextPage=totalPage;
        //PageBlock을 위한 조건문
        if(EndPage>TotalPage) EndPage = totalPage;



    }
}
