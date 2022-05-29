package project.OurRecipe.Service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.OurRecipe.Domain.Member;
import project.OurRecipe.Domain.Recommend;
import project.OurRecipe.Repository.BoardRepository;
import project.OurRecipe.Repository.RecommendRepository;

@Slf4j
@Service
public class BoardService {
    @Autowired private BoardRepository boardRepository;
    @Autowired private RecommendRepository recommendRepository;

    public void BoardRecommend(int BoardID, Member member, Recommend recommend){
        int RecommendCount;
        if(recommendRepository.CheckRecommend(BoardID,member)==0){ //추천버튼 최초 실행
            recommendRepository.RecommendAction(BoardID, member);
            RecommendCount = boardRepository.GetBoardRecommendCount(BoardID) + 1;
        }
        else{
            if(recommendRepository.GetCheckRecommend(BoardID, member) == 0) { //추천버튼을 누른적이 있는 경우, 추천을 할 경우
                RecommendCount = boardRepository.GetBoardRecommendCount(BoardID) + 1;
                recommend.setCheckRecommend(1);
                recommendRepository.UpdateCheckRecommend(BoardID, member, recommend.getCheckRecommend());
            }
            else { //추천을 하지 않는 경우
                RecommendCount = boardRepository.GetBoardRecommendCount(BoardID) -1;
                recommend.setCheckRecommend(0);
                recommendRepository.UpdateCheckRecommend(BoardID,member, recommend.getCheckRecommend());
            }
        }
        boardRepository.UpdateBoardRecommendCount(RecommendCount,BoardID);
    }
}
