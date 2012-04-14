package test.edu.ualberta.cs.papersdb.server.dao.hibernate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import test.edu.ualberta.cs.papersdb.server.dao.jdbc.JdbcUtils;
import edu.ualberta.cs.papersdb.model.Collaboration;
import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.model.Ranking;
import edu.ualberta.cs.papersdb.server.dao.PaperDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { StandaloneDataConfig.class, DAOConfig.class })
@ActiveProfiles("dev")
@TransactionConfiguration
@TestExecutionListeners({})
@Transactional
public class TestPaper extends AbstractTransactionalJUnit4SpringContextTests {

    private PaperDAO paperDAO;

    private JdbcUtils jdbcUtils;

    private static final String[] TEST_TITLES = {
        "Title 1", "Title 2", "Title 3"
    };

    @Autowired
    public void setPaperDAO(PaperDAO paperDAO) {
        this.paperDAO = paperDAO;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcUtils = new JdbcUtils();
        this.jdbcUtils.setDataSource(dataSource);
    }

    @Test
    public void testCreate() {
        Paper paper = new Paper();
        paper.setTitle(TEST_TITLES[0]);
        Set<Collaboration> allCollaborations =
            new HashSet<Collaboration>(Arrays.asList(
                Collaboration.WITH_PDF, Collaboration.WITH_STUDENT,
                Collaboration.WITH_EXTERNAL_ML_COLLEAGUE,
                Collaboration.WITH_EXTERNAL_NON_ML_COLLEAGUE));
        paper.setCollaborations(allCollaborations);
        paper.setRanking(Ranking.TOP_TIER);
        paperDAO.save(paper);
        paperDAO.flush();

        // check the database
        Paper result = jdbcUtils.getPaper(TEST_TITLES[0]);
        Assert.assertEquals(TEST_TITLES[0], result.getTitle());
        Assert.assertEquals(Ranking.TOP_TIER, result.getRanking());

    }

    @Test
    public void testGetByTitle() {
        Paper paper = new Paper();
        paper.setTitle(TEST_TITLES[0]);
        Set<Collaboration> allCollaborations =
            new HashSet<Collaboration>(Arrays.asList(
                Collaboration.WITH_PDF, Collaboration.WITH_STUDENT,
                Collaboration.WITH_EXTERNAL_ML_COLLEAGUE,
                Collaboration.WITH_EXTERNAL_NON_ML_COLLEAGUE));
        paper.setCollaborations(allCollaborations);
        paper.setRanking(Ranking.TOP_TIER);
        paper.setPublic(false);
        paperDAO.save(paper);

        Paper result = paperDAO.getByTitle(TEST_TITLES[0]);
        Assert.assertEquals(TEST_TITLES[0], result.getTitle());
        Assert.assertEquals(allCollaborations.size(), result
            .getCollaborations().size());
        Assert.assertEquals(Ranking.TOP_TIER, result.getRanking());
        Assert.assertEquals(0, result.getAuthors().size());
        Assert.assertEquals(0, result.getRelatedPapers().size());
        Assert.assertEquals(0, result.getRelatedUrls().size());
        Assert.assertEquals(0, result.getAttachments().size());
    }
}
