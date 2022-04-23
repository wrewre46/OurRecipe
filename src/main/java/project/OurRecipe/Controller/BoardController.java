package project.OurRecipe.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.OurRecipe.Domain.Board;
import project.OurRecipe.Repository.BoardRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/boards")
public class BoardController {
    @Autowired private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping()
    public String Boards(Model model){
        List<Board> boards=boardRepository.findAll();
        model.addAttribute("boards", boards);
        return "board/boards";
    }
    @GetMapping("/{BoardID}")
    public String board(@PathVariable int BoardID, Model model){
        Board board = boardRepository.findByBoardID(BoardID);
        model.addAttribute("board", board);
        return "board/board";

    }
    @GetMapping("/write")
    public String BoardWrite(){
        return "board/write";
    }
    @PostMapping("/write")
    public String BoardWrite(@RequestParam String BoardTitle,
                           @RequestParam String BoardContent,
                           Model model, RedirectAttributes redirectAttributes){
        Board board = new Board("userID",
                                boardRepository.BoardCount()+1,
                                BoardTitle,
                                BoardContent,
                                Date.valueOf(LocalDate.now()),
                                Time.valueOf(LocalTime.now()));
        boardRepository.BoardSave(board);
        model.addAttribute("board", board);
        redirectAttributes.addAttribute("BoardID", board.getBoardID());
        return "redirect:/boards/{BoardID}";
    }

    @GetMapping("/{BoardID}/edit")
    public String BoardEdit(@PathVariable int BoardID, Model model){
        Board board = boardRepository.findByBoardID(BoardID);
        model.addAttribute("board",board);
        return "board/edit";
    }
    @PostMapping("/{BoardID}/edit")
    public String BoardEdit(@PathVariable int BoardID,
                            @RequestParam String BoardTitle,
                            @RequestParam String BoardContent,
                            Model model){
        Board board = boardRepository.BoardUpdate(BoardID,BoardTitle,BoardContent);
        model.addAttribute("board", board);
        return "redirect:/boards/{BoardID}";
    }




}
