package project.OurRecipe.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import project.OurRecipe.Domain.BoardComment;

import java.util.List;

@Repository
public class BoardCommentRepository {
    @Autowired private JdbcTemplate jdbcTemplate;

    public int SaveComment(BoardComment boardComment){
        String sql = "insert into BoardComment(BoardID, MemberID, MemberNickname, Comment, WriteDate, WriteTime) values(?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                boardComment.getBoardID(),
                boardComment.getMemberID(),
                boardComment.getMemberNickname(),
                boardComment.getComment(),
                boardComment.getCommentCreateDate(),
                boardComment.getCommentCreateTime());
    }
    public List<BoardComment> CommentAll(int BoardID){
        String sql = "select * from BoardComment where CommentAvailable=1 AND BoardID = ?";
        return jdbcTemplate.query(sql, BoardCommentRowMapper(), BoardID);
    }
    public void DeleteBoardComment(int BoardID){
        String sql = "update BoardComment set BoardAvailable = 0 where BoardID = ?";
        jdbcTemplate.update(sql,BoardID);
    }
    private RowMapper<BoardComment> BoardCommentRowMapper() {
        return (rs, rowNum) -> {
            BoardComment boardComment = new BoardComment(rs.getInt("BoardID"),
                    rs.getString("MemberID"),
                    rs.getString("MemberNickname"),
                    rs.getString("Comment"),
                    rs.getDate("WriteDate"),
                    rs.getTime("WriteTime"));
            boardComment.setCommentAvailable(rs.getInt("CommentAvailable"));
            return boardComment;
        };
    }
}
