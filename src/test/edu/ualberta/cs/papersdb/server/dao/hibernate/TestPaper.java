package test.edu.ualberta.cs.papersdb.server.dao.hibernate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import edu.ualberta.cs.papersdb.model.Author;
import edu.ualberta.cs.papersdb.model.Collaboration;
import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.model.Publisher;
import edu.ualberta.cs.papersdb.model.Ranking;
import edu.ualberta.cs.papersdb.model.publication.JournalPub;
import edu.ualberta.cs.papersdb.server.dao.PaperDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { StandaloneDataConfig.class, DAOConfig.class })
@ActiveProfiles("dev")
@TransactionConfiguration
@Transactional
public class TestPaper extends TestHibernate {

    private static final Logger log = LoggerFactory.getLogger(
        TestPaper.class);

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
    public void create() {
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
        Paper result = getJdbcUtils().getPaper(name);
        Assert.assertEquals(name, result.getTitle());
        Assert.assertEquals(Ranking.TOP_TIER, result.getRanking());

        // attempt to save with no title
        paper = new Paper();

        try {
            paperDAO.save(paper);
            Assert.fail("no exception when saving without a title");
        } catch (ConstraintViolationException e) {
            log.error(e.getMessage());
            Assert.assertTrue(true);
        }
    }

    @Test
    public void saveDuplicateTitle() {
        Paper paper = new Paper();
        paper.setTitle(name);
        paperDAO.save(paper);
        paperDAO.flush();

        // add another paper with same title
        paper = new Paper();
        paper.setTitle(name);

        try {
            paperDAO.save(paper);
            Assert
                .fail("no exception when saving new paper with existing title");
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getByTitle() {
        Paper paper = new Paper();
        paper.setTitle(name);
        paperDAO.save(paper);
        paperDAO.flush();

        Paper result = paperDAO.getByTitle(name);
        Assert.assertEquals(name, result.getTitle());

        result = paperDAO.getByTitle(new BigInteger(130, getR()).toString());
        Assert.assertNull(result);
    }

    @Test
    public void getPapersTitleMatching() {
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
    public void getByDoi() {
        String title = getMethodNameR();
        Paper paper = new Paper();
        paper.setTitle(title);
        paper.setDOI(name);
        paperDAO.save(paper);

        Paper result = paperDAO.getByDoi(name);
        Assert.assertEquals(name, result.getDOI());
    }

    @Test
    public void saveDuplicateDOI() {
        Paper paper = new Paper();
        paper.setTitle(name);
        paper.setDOI(name);
        paperDAO.save(paper);
        paperDAO.flush();

        // add another paper with same DOI
        paper = new Paper();
        paper.setTitle(getMethodNameR());
        paper.setDOI(name);

        try {
            paperDAO.save(paper);
            Assert
                .fail("no exception when saving new paper with existing DOI");
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            Assert.assertTrue(true);
        }
    }

    @Test
    public void update() {
        Paper paper = new Paper();
        paper.setTitle(name);
        paperDAO.save(paper);
        paperDAO.flush();

        // use JDBC to check the database
        Paper result = getJdbcUtils().getPaper(name);
        Assert.assertNotNull(result);

        String newTitle = getMethodNameR();
        paper = paperDAO.getById(result.getId());
        paper.setTitle(newTitle);
        paperDAO.save(paper);
        paperDAO.flush();

        // use JDBC to check the database
        result = getJdbcUtils().getPaper(newTitle);
        Assert.assertNotNull(result);
    }

    @Test
    public void delete() {
        Paper paper = new Paper();
        paper.setTitle(name);
        paperDAO.save(paper);
        paperDAO.flush();

        // use JDBC to check the database
        Paper result = getJdbcUtils().getPaper(name);
        Assert.assertNotNull(result);

        paper = paperDAO.getById(result.getId());
        paperDAO.delete(paper);
        paperDAO.flush();

        Assert.assertEquals(0, countRowsInTable("paper"));
    }

    @Test
    public void paperAuthors() {
        Set<Author> authors = new HashSet<Author>();
        int numAuthors = getR().nextInt(6) + 1;
        for (int i = 0; i < numAuthors; ++i) {
            Author author = new Author();
            author.setFamilyNames(name);
            author.setGivenNames(getMethodNameR());
            getJdbcUtils().addAuthor(author);
            authors.add(author);
        }

        Paper paper = new Paper();
        paper.setTitle(name);
        paper.getAuthors().addAll(authors);
        paper = paperDAO.save(paper);
        paperDAO.flush();

        Assert.assertEquals(numAuthors, countRowsInTable("author"));

        Author author;

        for (int i = 0; i < numAuthors; ++i) {
            author = authors.iterator().next();
            authors.remove(author);
            paper.getAuthors().remove(author);
            paper = paperDAO.save(paper);
            paperDAO.flush();

            Assert.assertEquals(authors.size(), paper.getAuthors().size());
        }
    }

    @Test
    public void paperPublication() {
        Paper paper = new Paper();
        paper.setTitle(name);
        paper = paperDAO.save(paper);
        paperDAO.flush();

        Publisher publisher = new Publisher();
        publisher.setName(name);
        publisher.setRanking(Ranking.TOP_TIER);
        getJdbcUtils().addPublisher(publisher);
        Assert
            .assertNotNull("autogen key shouldn't be null", publisher.getId());

        JournalPub pub = new JournalPub();
        pub.setName(name);
        pub.setDate(new Date());
        pub.setPaper(paper);
        pub.setPublisher(publisher);
        getJdbcUtils().addPublication(pub);
        Assert.assertNotNull("autogen key shouldn't be null", pub.getId());

        paperDAO.clear();
        paper = paperDAO.getById(paper.getId());
        Assert.assertEquals(pub.getId(), paper.getPublication().getId());

        // test delete
        paperDAO.delete(paper);
        paperDAO.flush();
        Assert.assertEquals(0, countRowsInTable("publication"));
    }

    @Test
    public void relatedPapers() {
        Paper parentPaper = new Paper();
        parentPaper.setTitle(name);
        parentPaper = paperDAO.save(parentPaper);

        HashSet<Paper> relatedPapers = new HashSet<Paper>(0);
        for (int i = 0, n = getR().nextInt(5) + 2; i < n; ++i) {
            Paper paper = new Paper();
            paper.setTitle(getMethodNameR());
            paper = paperDAO.save(paper);
            relatedPapers.add(paper);

            parentPaper.getRelatedPapers().clear();
            parentPaper.getRelatedPapers().addAll(relatedPapers);
            parentPaper = paperDAO.save(parentPaper);
            paperDAO.flush();

            Assert.assertEquals(relatedPapers.size(), parentPaper
                .getRelatedPapers().size());
        }

        while (relatedPapers.size() > 0) {
            Paper paper = relatedPapers.iterator().next();
            relatedPapers.remove(paper);

            parentPaper.getRelatedPapers().remove(paper);
            paperDAO.save(parentPaper);

            paperDAO.delete(paper);
            paperDAO.flush();

            parentPaper = paperDAO.getById(parentPaper.getId());
            Assert.assertEquals(relatedPapers.size(), parentPaper
                .getRelatedPapers().size());
        }

        Assert.assertEquals(1, countRowsInTable("paper"));

        // add 1 related paper
        Paper relatedPaper = new Paper();
        relatedPaper.setTitle(getMethodNameR());
        relatedPaper = paperDAO.save(relatedPaper);
        paperDAO.flush();

        parentPaper.getRelatedPapers().clear();
        parentPaper.getRelatedPapers().add(relatedPaper);
        paperDAO.save(parentPaper);
        paperDAO.flush();

        Assert.assertEquals(2, countRowsInTable("paper"));

        // delete parent paper and ensure related paper still there
        paperDAO.delete(parentPaper);
        paperDAO.flush();
        Assert.assertEquals(1, countRowsInTable("paper"));

        Set<Paper> papers = paperDAO.getAll();
        Assert.assertEquals(1, papers.size());
        Assert.assertEquals(relatedPaper.getId(),
            papers.iterator().next().getId());
    }
}
