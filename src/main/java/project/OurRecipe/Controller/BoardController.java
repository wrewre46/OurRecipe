package project.OurRecipe.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.OurRecipe.Config.Auth.PrincipalDetails;

import project.OurRecipe.Domain.*;
import project.OurRecipe.Repository.*;
import project.OurRecipe.Service.BoardService;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/boards")
public class BoardController {
    @Autowired private BoardRepository boardRepository;
    @Autowired private PageRepository pageRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BoardCommentRepository boardCommentRepository;
    @Autowired private BoardService boardService;
    @Autowired private RecommendRepository recommendRepository;
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping()
    public String Boards(Model model){
        List<Integer> PageBlock = new ArrayList<>();
        Page page = new Page(1,(int)Math.ceil((double)pageRepository.TotalBoards()/8));
        for(int i = page.getStartPage(); i<=page.getEndPage();i++){
            PageBlock.add(i);
        }
        List<Board> HotBoards = boardRepository.HotBoards();
        List<Board> boards = pageRepository.BoardsPerPage(page.getNowPage());
        model.addAttribute("Page",page);
        model.addAttribute("PageBlock",PageBlock);
        model.addAttribute("boards",boards);
        model.addAttribute("HotBoards", HotBoards);
        return "board/boards";
    }
    @GetMapping("/{BoardID}")
    public String board(@PathVariable int BoardID,Model model,@AuthenticationPrincipal PrincipalDetails principalDetails){
        int Page = (int)Math.ceil((double)(pageRepository.FindPage(BoardID)/8))+1;
        List<BoardComment> comments = boardCommentRepository.CommentAll(BoardID);
        try{
            Member member = principalDetails.getMember();
            Board board = boardRepository.findByBoardID(BoardID);
            log.info("MemberID={}", member.getMemberID());
            log.info("BoardMemberID={}", board.getMemberID());
            model.addAttribute("recommend", recommendRepository.GetCheckRecommend(BoardID, member));
            model.addAttribute("comments", comments);
            model.addAttribute("page",Page);
            model.addAttribute("board", board);
            return "board/board";
        }catch (Exception E){
            Board board = boardRepository.findByBoardID(BoardID);
            model.addAttribute("comments", comments);
            model.addAttribute("page",Page);
            model.addAttribute("board", board);

            return "board/board";
        }
    }
    @PostMapping("/{BoardID}")
    public String board(@PathVariable int BoardID, @Validated @ModelAttribute BoardComment boardComment,
                        @AuthenticationPrincipal PrincipalDetails principalDetails){
        log.info("BoardComment={}", boardComment.getComment());
        Member CommentMember = principalDetails.getMember();
        boardComment.setBoardID(BoardID);
        boardComment.setMemberID(CommentMember.getMemberID());
        boardComment.setMemberNickname(CommentMember.getNickname());
        boardComment.setCommentCreateDate(Date.valueOf(LocalDate.now()));
        boardComment.setCommentCreateTime(Time.valueOf(LocalTime.now()));
        boardCommentRepository.SaveComment(boardComment);
        return "redirect:/boards/{BoardID}";
    }
    @Secured("ROLE_USER")
    @GetMapping("/write")
    public String BoardWrite(Model model){
        model.addAttribute("board",new Board());
        return "board/write";
    }
    @PostMapping("/write")
    public String BoardWrite(@RequestParam String BoardTitle,
                             @RequestParam String BoardContent,
                             @RequestParam MultipartFile BoardFileImgName,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        Member member = principalDetails.getMember();
        log.info("MemberID={}",member.getMemberID());
        Board board = new Board(member.getMemberID(),
                                member.getNickname(),
                                boardRepository.BoardCount()+1,
                                BoardTitle,
                                BoardContent,
                                Date.valueOf(LocalDate.now()),
                                Time.valueOf(LocalTime.now()));
        if(!BoardFileImgName.isEmpty()){
            UUID uuid = UUID.randomUUID();
            String UploadName = uuid+"_"+BoardFileImgName.getOriginalFilename();
            String fullPath = fileDir + UploadName ;
            log.info("파일 저장 fullPath={}", fullPath);
            board.setBoardFileImgName(UploadName);
            BoardFileImgName.transferTo(new File(fullPath));
        }
        boardRepository.BoardSave(board);
        model.addAttribute("board", board);
        redirectAttributes.addAttribute("BoardID", board.getBoardID());

        return "redirect:/boards/{BoardID}";
    }
    @Secured("ROLE_USER")
    @GetMapping("/{BoardID}/delete")
    public String BoardDelete(@PathVariable int BoardID,Model model,RedirectAttributes redirectAttributes){
        int Page = (int)Math.ceil((double)(pageRepository.FindPage(BoardID)/8))+1;
        boardRepository.DeleteBoard(BoardID);
        model.addAttribute("BoardID", BoardID);
        redirectAttributes.addAttribute("Page", Page);
        return "redirect:/boards/page={Page}";
    }

    @Secured("ROLE_USER")
    @GetMapping("/{BoardID}/edit")
    public String BoardEdit(@PathVariable int BoardID, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Member member = principalDetails.getMember();
        Board board = boardRepository.findByBoardID(BoardID);
        model.addAttribute("member",member);
        model.addAttribute("board",board);
        if(member.getMemberID().equals(board.getMemberID())){
            return "board/edit";
        }
        else return "redirect:/boards/{BoardID}";
    }
    @Secured("ROLE_USER")
    @GetMapping("/{BoardID}/recommend")
    public String Recommend(@PathVariable int BoardID, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Member member = principalDetails.getMember();
        Recommend recommend = new Recommend();
        boardService.BoardRecommend(BoardID, member, recommend);
        return "redirect:/boards/{BoardID}";
    }
    @Secured("ROLE_USER")
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
