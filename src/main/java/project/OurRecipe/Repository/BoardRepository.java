package project.OurRecipe.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import project.OurRecipe.Domain.Board;

import java.util.List;

@Repository
public class BoardRepository {
    @Autowired private JdbcTemplate jdbcTemplate;

    public int BoardSave(Board board){
        String sql = "insert into Board(MemberID,MemberNickname, BoardID, BoardTitle, BoardContent,WriteDate,WriteTime) values (?,?,?,?,?,?,?)";
        return jdbcTemplate.update(
                sql,board.getMemberID(),
                board.getMemberNickname(),
                board.getBoardID(),
                board.getBoardTitle(),
                board.getBoardContent(),
                board.getWriteDate(),
                board.getWriteTime());
    }
    public int BoardCount(){
        String sql = "select count(*) from Board";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<Board> findAll(){
        return jdbcTemplate.query("select * from board ", BoardRowMapper());
    }
    public Board findByBoardID(int BoardID){
        String sql = "select * from board where BoardID = ?";

        return jdbcTemplate.queryForObject(sql,BoardRowMapper(),BoardID);
    }
    public Board BoardUpdate(int BoardID, String BoardTitle, String BoardContent){
         Board BoardUpdate = findByBoardID(BoardID);
         BoardUpdate.setBoardTitle(BoardTitle);
         BoardUpdate.setBoardContent(BoardContent);
         String sql = "update Board set BoardTitle = ?, BoardContent = ? WHERE BoardID = ?";
         jdbcTemplate.update(sql,BoardUpdate.getBoardTitle(), BoardUpdate.getBoardContent(),BoardID);
         return BoardUpdate;

    }
    public void UpdateNickname(String Nickname, String MemberID){
        String sql ="update board set MemberNickname = ? where MemberID = ?";
        jdbcTemplate.update(sql,Nickname, MemberID);
    }
    public void DeleteBoard(int BoardID){
        String sql = "update Board set BoardAvailable = 0 where BoardID = ?";
        jdbcTemplate.update(sql,BoardID);
    }
    public int GetBoardRecommendCount(int BoardID){
        String sql = "select RecommendCount from board where BoardID = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, BoardID);
    }
    public void UpdateBoardRecommendCount(int RecommendCount , int BoardID){
        String sql = "update Board set RecommendCount = ? where BoardID =?";
        jdbcTemplate.update(sql,RecommendCount,BoardID);
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
