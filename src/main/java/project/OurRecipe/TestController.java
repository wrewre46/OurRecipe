package project.OurRecipe;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;

@Controller
public class TestController {
    private final JdbcTemplate jdbcTemplate;

    public TestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public String select(int num) {
        String sql = "SELECT BoardName FROM board WHERE BoardID=?";

        String BoardName = (String) jdbcTemplate.queryForObject(
                sql, String.class, new Object[] { num });

        return BoardName;
    }

    @GetMapping("/")
    public String test(Model model){
        model.addAttribute("test", select(1));
        return "index";
    }
}
