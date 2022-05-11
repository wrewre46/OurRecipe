package project.OurRecipe.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.OurRecipe.Domain.Board;
import project.OurRecipe.Domain.Page;
import project.OurRecipe.Repository.PageRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class PageController {
    @Autowired PageRepository pageRepository;
    @GetMapping("/boards/page={NowPage}")
    public String page(@PathVariable int NowPage, Board board,Model model){
        List<Integer> PageBlock = new ArrayList<>();
        int TotalPage = (int)Math.ceil((double)pageRepository.TotalBoards()/8);
        log.info("TotalBoards={}",pageRepository.TotalBoards());
        log.info("TotalPage={}",TotalPage);
        if(NowPage<1) {
            NowPage=1;
            return "redirect:/boards/page=1";
        }
        if(NowPage>TotalPage)NowPage=TotalPage;
        Page page = new Page(NowPage,TotalPage);
        for(int i = page.getStartPage(); i<=page.getEndPage();i++){
            PageBlock.add(i);
        }
        List<Board> boards = pageRepository.BoardsPerPage(page.getNowPage());
        model.addAttribute("Page",page);
        model.addAttribute("PageBlock",PageBlock);
        model.addAttribute("boards",boards);
        return "board/boards";
    }
}
