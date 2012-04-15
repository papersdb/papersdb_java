package test.edu.ualberta.cs.papersdb.server.dao.hibernate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
public class TestPaper extends TestHibernate {

    private PaperDAO paperDAO;

    private String name;

    @Autowired
    public void setPaperDAO(PaperDAO paperDAO) {
        this.paperDAO = paperDAO;
    }

    @Before
    public void setUp() throws Exception {
        name = getMethodNameR();
    }

    @Test
    public void testCreate() {
        Paper paper = new Paper();
        paper.setTitle(name);
        Set<Collaboration> allCollaborations =
            new HashSet<Collaboration>(Arrays.asList(
                Collaboration.WITH_PDF, Collaboration.WITH_STUDENT,
                Collaboration.WITH_EXTERNAL_ML_COLLEAGUE,
                Collaboration.WITH_EXTERNAL_NON_ML_COLLEAGUE));
        paper.setCollaborations(allCollaborations);
        paper.setRanking(Ranking.TOP_TIER);
        paperDAO.save(paper);
        paperDAO.flush();

        // use JDBC to check the database
        Paper result = jdbcUtils.getPaper(name);
        Assert.assertEquals(name, result.getTitle());
        Assert.assertEquals(Ranking.TOP_TIER, result.getRanking());
    }

    @Test
    public void testGetByTitle() {
        Paper paper = new Paper();
        paper.setTitle(name);
        paperDAO.save(paper);

        Paper result = paperDAO.getByTitle(name);
        Assert.assertEquals(name, result.getTitle());
    }

    @Test
    public void testGetPapersTitleMatching() {
        int sameNamePapers = getR().nextInt(5) + 3;
        for (int i = 0; i < sameNamePapers; ++i) {
            Paper paper = new Paper();
            paper.setTitle(getMethodNameR());
            paperDAO.save(paper);
        }

        // generate another paper with different title, use BigInteger to
        // generate a random string
        Paper paper = new Paper();
        paper.setTitle(new BigInteger(130, getR()).toString());
        paperDAO.save(paper);

        Set<Paper> papers =
            paperDAO.getPapersTitleMatching(getMethodName(), 0, 10);
        Assert.assertEquals(sameNamePapers, papers.size());
    }

    @Test
    public void testGetByDoi() {
        String title = getMethodNameR();
        Paper paper = new Paper();
        paper.setTitle(title);
        paper.setDoi(name);
        paperDAO.save(paper);

        Paper result = paperDAO.getByDoi(name);
        Assert.assertEquals(name, result.getDoi());
    }
}
