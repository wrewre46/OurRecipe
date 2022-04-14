package project.OurRecipe.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import project.OurRecipe.Domain.Board;

@Repository
public class BoardRepository {
    private final JdbcTemplate jdbcTemplate;

    public BoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public int save(Board board){
        String sql = "insert into Board(UserID, BoardID, BoardTitle, BoardContent, WriteTime) values (?,?,?,?,?)";
        return jdbcTemplate.update(
                sql,board.getUserID(),
                board.getBoardID(),
                board.getBoardTitle(),
                board.getBoardContent(),
                board.getWriteTime());
    }
    public int boardCount(){
        String sql = "select count(*) from Board";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
