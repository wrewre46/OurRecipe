package project.OurRecipe.Service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.OurRecipe.Repository.BoardRepository;

@Slf4j
@Service
public class BoardService {
    @Autowired private BoardRepository boardRepository;

    public void BoardRecommend(int BoardID){
        int RecommendCount = boardRepository.GetBoardRecommendCount(BoardID)+1;
        boardRepository.UpdateBoardRecommendCount(RecommendCount,BoardID);
    }
}
