package test.edu.ualberta.cs.papersdb.server.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import edu.ualberta.cs.papersdb.model.Author;
import edu.ualberta.cs.papersdb.model.Collaboration;
import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.model.Ranking;

public class JdbcUtils {

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert insertAuthor;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertAuthor =
            new SimpleJdbcInsert(dataSource).withTableName("author")
                .usingGeneratedKeyColumns("id");
    }

    /*
     * Can also use: jdbcTemplate.queryForObject(sql, new Object[] { title },
     * new BeanPropertyRowMapper<Paper>(Paper.class));
     */
    public Paper getPaper(String title) {
        String sql = "SELECT * FROM paper WHERE title = ?";
        RowMapper<Paper> mapper = new RowMapper<Paper>() {
            public Paper mapRow(ResultSet rs, int rowNum) throws SQLException {
                Paper paper = new Paper();
                paper.setId(rs.getLong("id"));
                paper.setTitle(rs.getString("title"));
                if (rs.getString("ranking_id") != null) {
                    paper
                        .setRanking(Ranking.valueOf(rs.getString("ranking_id")));
                }
                return paper;
            }
        };

        Paper paper = getJdbcTemplate().queryForObject(sql, mapper, title);
        Set<Collaboration> collaborations = new HashSet<Collaboration>(0);

        sql =
            "SELECT collaboration_id FROM paper_collaboration WHERE paper_id = ?";

        List<Map<String, Object>> rows =
            getJdbcTemplate().queryForList(sql, paper.getId());
        for (Map<String, Object> row : rows) {
            collaborations.add(Collaboration.valueOf((String) row
                .get("collaboration_id")));
        }

        paper.getCollaborations().addAll(collaborations);

        return paper;
    }

    public Author getAuthor(String names) {
        String sql = "select * from author where family_names = ?";
        RowMapper<Author> mapper = new RowMapper<Author>() {
            public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
                Author author = new Author();
                author.setId(rs.getLong("id"));
                author.setEmail(rs.getString("email"));
                author.setFamilyNames(rs.getString("family_names"));
                author.setGivenNames(rs.getString("given_names"));
                author.setTitle(rs.getString("title"));
                return author;
            }

        };

        return getJdbcTemplate().queryForObject(sql, mapper, names);
    }

    public void addAuthor(Author author) {
        // Map<String, Object> parameters = new HashMap<String, Object>(2);
        // parameters.put("family_names", author.getFamilyNames());
        // parameters.put("given_names", author.getGivenNames());
        SqlParameterSource parameters =
            new BeanPropertySqlParameterSource(author);
        Number newId = insertAuthor.executeAndReturnKey(parameters);
        author.setId(newId.longValue());
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
