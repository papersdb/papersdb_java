package test.edu.ualberta.cs.papersdb.server.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.model.Ranking;

public class JdbcUtils {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Paper getPaper(String title) {
        String sql = "select * from paper where title = ?";
        RowMapper<Paper> mapper = new RowMapper<Paper>() {
            public Paper mapRow(ResultSet rs, int rowNum) throws SQLException {
                Paper paper = new Paper();
                paper.setId(rs.getLong("id"));
                paper.setTitle(rs.getString("title"));
                paper.setRanking(Ranking.valueOf(rs.getString("ranking_id")));
                return paper;
            }

        };

        // return jdbcTemplate.queryForObject(sql, new Object[] { title },
        // new BeanPropertyRowMapper<Paper>(Paper.class));
        return jdbcTemplate.queryForObject(sql, mapper, title);
    }

}
