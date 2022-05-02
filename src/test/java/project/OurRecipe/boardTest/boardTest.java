package project.OurRecipe.boardTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.OurRecipe.Domain.Board;
import project.OurRecipe.Repository.BoardRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@SpringBootTest
public class boardTest {
    @Autowired BoardRepository boardRepository;
    @Test
    public void saveTEST() {
        Board board = new Board("kim2",
                boardRepository.BoardCount()+1,
                "hello1",
                "hello test2",
                Date.valueOf(LocalDate.now()),
                Time.valueOf(LocalTime.now()));
        boardRepository.BoardSave(board);
    }

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
}
