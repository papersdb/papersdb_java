package test.edu.ualberta.cs.papersdb.server.dao.hibernate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ualberta.cs.papersdb.model.Paper;
import edu.ualberta.cs.papersdb.server.dao.PaperDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { StandaloneDataConfig.class, DAOConfig.class })
@ActiveProfiles("dev")
@Transactional
public class TestPaper {

    @Autowired
    private PaperDAO paperDAO;

    // public void setPaperDAO(PaperDAO paperDAO) {
    // this.paperDAO = paperDAO;
    // }

    @Test
    public void testCreate() {
        Paper paper = new Paper();
        paper.setTitle("Title 1");
        paperDAO.save(paper);
    }

}