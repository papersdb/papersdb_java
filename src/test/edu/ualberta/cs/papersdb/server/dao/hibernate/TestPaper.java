package test.edu.ualberta.cs.papersdb.server.dao.hibernate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceHibernateConfig.class })
@TransactionConfiguration
@Transactional
public class TestPaper {

    @Test
    public void testCreate() {
    }

}
