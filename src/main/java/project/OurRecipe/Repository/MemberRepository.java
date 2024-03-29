package project.OurRecipe.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import project.OurRecipe.Domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    @Autowired private JdbcTemplate jdbcTemplate;

    public int MemberSave(Member member){
        String sql = "insert into Member(ID, MemberID," +
                " Password, Email, Nickname, Role," +
                " Provider, ProviderID,"+
                " MemberCreateDate, MemberCreateTime) values (?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql,
                member.getID(),
                member.getMemberID(),
                member.getPassword(),
                member.getEmail(),
                member.getNickname(),
                member.getRole(),
                member.getProvider(),
                member.getProviderID(),
                member.getMemberCreateDate(),
                member.getMemberCreateTime());

    }
    public void UpdateNickname(String Nickname, String MemberID){
        String sql ="update member set Nickname = ? where MemberID = ?";
        jdbcTemplate.update(sql,Nickname, MemberID);
    }
    public int MemberCount(){
        String sql = "select count(*) from Member";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public Member findByMemberID(String MemberID){
        String sql = "select * from member where MemberID = ?";

        return jdbcTemplate.queryForObject(sql,MemberRowMapper(),MemberID);
    }
    public Optional<Member> findByProviderAndProviderId(String provider, String providerID){
        List<Member> result = jdbcTemplate.query("select * from member where provider= ? and providerID= ?",
                MemberRowMapper(), provider,providerID);

        return result.stream().findAny();
    }
    public int CountMemberID(String MemberID){
        String sql="select count(*) from member where MemberID= ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class,MemberID);
    }
    public int CountMemberEmail(String Email){
        String sql="select count(*) from member where Email= ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class,Email);
    }
    public int CountMemberNickname(String Nickname){
        String sql="select count(*) from member where Nickname= ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class,Nickname);
    }
    private RowMapper<Member> MemberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member(
                    rs.getInt("ID"),
                    rs.getString("MemberID"),
                    rs.getString("Password"),
                    rs.getString("Email"),
                    rs.getString("Nickname"),
                    rs.getString("Role"),
                    rs.getString("Provider"),
                    rs.getString("ProviderID"),
                    rs.getDate("MemberCreateDate"),
                    rs.getTime("MemberCreateTime"));
            return member;
        };
    }
}
