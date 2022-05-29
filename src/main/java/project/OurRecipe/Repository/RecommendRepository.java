package project.OurRecipe.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import project.OurRecipe.Domain.Board;
import project.OurRecipe.Domain.Member;
import project.OurRecipe.Domain.Recommend;

import java.util.List;

@Repository
public class RecommendRepository {
    @Autowired JdbcTemplate jdbcTemplate;
    public int RecommendAction(int BoardID, Member member){
        String sql = "insert into Recommend(MemberID, MemberNickname, BoardID) values (?,?,?)";
        return jdbcTemplate.update(
                sql,member.getMemberID(),
                member.getNickname(),
                BoardID);
    }
    public int CheckRecommend(int BoardID, Member member){
        String sql = "select count(*) from Recommend where BoardID = ? AND MemberID =?";
        return jdbcTemplate.queryForObject(sql, Integer.class,BoardID, member.getMemberID());
    }
    public int GetCheckRecommend(int BoardID, Member member){
        String sql = "select CheckRecommend from Recommend where BoardID = ? AND MemberID =?";
        return jdbcTemplate.queryForObject(sql, Integer.class,BoardID, member.getMemberID());
    }
    public void UpdateCheckRecommend(int BoardID, Member member, int Recommend){
        String sql = "update Recommend set CheckRecommend = ? WHERE BoardID = ? AND MemberID = ?";
        jdbcTemplate.update(sql,Recommend,BoardID,member.getMemberID());
    }
    public List<Integer> FindRecommended(Member member){
        String sql = "select BoardID from Recommend where MemberID=? AND CheckRecommend = 1";
        return jdbcTemplate.queryForList(sql,Integer.class, member.getMemberID());
    }

}
