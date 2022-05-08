package project.OurRecipe.Repository;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import project.OurRecipe.Domain.Board;
import project.OurRecipe.Domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@NoArgsConstructor
public class PageRepository {
    @Autowired private JdbcTemplate jdbcTemplate;

    public int TotalBoards(){
        String sql="select count(*) from board where BoardAvailable = 1";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }
    public int FindPage(int BoardID){
        String sql="select count(BoardID) from board where BoardAvailable=1 AND BoardID > ? ORDER BY BoardID DESC";
        return jdbcTemplate.queryForObject(sql,Integer.class,BoardID);
    }
    public List<Board> BoardsPerPage(int NowPage){
        String sql ="select * from board where BoardAvailable = 1 ORDER BY BoardID DESC LIMIT 8 OFFSET ?";
        return jdbcTemplate.query(sql,BoardRowMapper(),8*NowPage-8);
    }
    private RowMapper<Board> BoardRowMapper() {
        return (rs, rowNum) -> {
            Board board = new Board(rs.getString("MemberID"),
                    rs.getString("MemberNickname"),
                    rs.getInt("BoardID"),
                    rs.getString("BoardTitle"),
                    rs.getString("BoardContent"),
                    rs.getDate("WriteDate"),
                    rs.getTime("WriteTime"));
            board.setRecommendCount(rs.getInt("RecommendCount"));
            board.setBoardAvailable(rs.getInt("BoardAvailable"));
            return board;
        };
    }

}
