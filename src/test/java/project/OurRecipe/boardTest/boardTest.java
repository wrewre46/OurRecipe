package project.OurRecipe.boardTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.OurRecipe.Domain.Board;
import project.OurRecipe.Domain.Page;
import project.OurRecipe.Repository.BoardRepository;
import project.OurRecipe.Repository.PageRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@SpringBootTest
public class boardTest {
    @Autowired BoardRepository boardRepository;
    @Autowired
    PageRepository pageRepository;

    @Test
    public void findAllTest(){
        List<Board> board = boardRepository.findAll();
        for(int i = 0 ; i<board.size(); i++){
            System.out.print(board.get(i).getMemberID()+" ");
            System.out.print(board.get(i).getBoardID()+" ");
            System.out.print(board.get(i).getBoardTitle()+" ");
            System.out.print(board.get(i).getBoardContent()+" ");
            System.out.print(board.get(i).getBoardAvailable()+" ");
            System.out.print(board.get(i).getWriteDate()+" ");
            System.out.print(board.get(i).getWriteTime()+" ");
            System.out.print(board.get(i).getRecommendCount()+" ");
            System.out.println();
        }

    }
    @Test
    void pageboards(){
        Page page = new Page();
        page.setNowPage(2);
        List<Board> board = pageRepository.BoardsPerPage(page.getNowPage());
        for (Board board1 : board) {
            System.out.print(board1.getBoardID()+" ");
            System.out.print(board1.getBoardTitle()+" ");
            System.out.print(board1.getBoardContent()+" ");
            System.out.println();

        }
    }
    @Test
    void PageBlockTest(){
        System.out.println(pageRepository.TotalBoards());
        Page page = new Page(1,pageRepository.TotalBoards()/8 + 1);
        System.out.println("StartPage : " + page.getStartPage());
        System.out.println("EndPage : " + page.getEndPage());
    }
}
