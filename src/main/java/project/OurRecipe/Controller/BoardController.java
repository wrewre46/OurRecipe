package project.OurRecipe.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.OurRecipe.Config.Auth.PrincipalDetails;
import project.OurRecipe.Config.Auth.PrincipalDetailsService;
import project.OurRecipe.Domain.Board;
import project.OurRecipe.Domain.Member;
import project.OurRecipe.Domain.Page;
import project.OurRecipe.Repository.BoardRepository;
import project.OurRecipe.Repository.MemberRepository;
import project.OurRecipe.Repository.PageRepository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Controller
@RequestMapping("/boards")
public class BoardController {
    @Autowired private BoardRepository boardRepository;
    @Autowired private PageRepository pageRepository;
    @Autowired private MemberRepository memberRepository;
    @GetMapping()
    public String Boards(Model model){
        List<Integer> PageBlock = new ArrayList<>();
        Page page = new Page(1,pageRepository.TotalBoards()/8 + 1);
        for(int i = page.getStartPage(); i<=page.getEndPage();i++){
            PageBlock.add(i);
        }
        List<Board> boards = pageRepository.BoardsPerPage(page.getNowPage());
        model.addAttribute("Page",page);
        model.addAttribute("PageBlock",PageBlock);
        model.addAttribute("boards",boards);
        return "board/boards";
    }
    @GetMapping("/{BoardID}")
    public String board(@PathVariable int BoardID, Model model, @AuthenticationPrincipal PrincipalDetails userDetails){
        Member member = userDetails.getMember();
        Board board = boardRepository.findByBoardID(BoardID);
        model.addAttribute("member",member);
        model.addAttribute("board", board);
        return "board/board";

    }
    @GetMapping("/write")
    public String BoardWrite(Model model){
        model.addAttribute("board",new Board());
        return "board/write";
    }
    @PostMapping("/write")
    public String BoardWrite(@RequestParam String BoardTitle,
                           @RequestParam String BoardContent,
                           Model model, RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal PrincipalDetails userDetails){
        Member member = userDetails.getMember();
        log.info("MemberID={}",member.getMemberID());
        Board board = new Board(member.getMemberID(),
                                member.getNickname(),
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
    public String BoardEdit(@PathVariable int BoardID, Model model, @AuthenticationPrincipal PrincipalDetails userDetails){
        Member member = userDetails.getMember();
        Board board = boardRepository.findByBoardID(BoardID);
        model.addAttribute("member",member);
        model.addAttribute("board",board);
        if(member.getMemberID().equals(board.getMemberID())){
            return "board/edit";
        }
        else return "error";
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
