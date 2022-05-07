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
        log.info("NowPage={}", NowPage);
        List<Integer> PageBlock = new ArrayList<>();
        int TotalPage = pageRepository.TotalBoards()/8+1;
        if(NowPage<1) {
            NowPage=1;
            return "redirect:/boards/page=1";
        }
        if(NowPage>TotalPage)NowPage=TotalPage;
        log.info("NowPage={}", NowPage);
        Page page = new Page(NowPage,TotalPage);
        for(int i = page.getStartPage(); i<=page.getEndPage();i++){
            PageBlock.add(i);
        }
        List<Board> boards = pageRepository.BoardsPerPage(page.getNowPage());
        log.info("getPrevPage={}",page.getPrevPage());
        log.info("getNextPage={}",page.getNextPage());
        model.addAttribute("Page",page);
        model.addAttribute("PageBlock",PageBlock);
        model.addAttribute("boards",boards);

        log.info("getTotalPage()={}", page.getTotalPage());
        return "board/boards";
    }
}
