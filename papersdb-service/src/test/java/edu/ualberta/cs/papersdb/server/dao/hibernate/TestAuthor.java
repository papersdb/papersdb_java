package edu.ualberta.cs.papersdb.server.dao.hibernate;

import java.math.BigInteger;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import edu.ualberta.cs.papersdb.model.Author;
import edu.ualberta.cs.papersdb.persist.dao.AuthorDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { StandaloneDataConfig.class, DAOConfig.class })
@ActiveProfiles("dev")
@TransactionConfiguration
@Transactional
public class TestAuthor extends TestHibernate {

	private static final Logger log = LoggerFactory.getLogger(TestPaper.class);

	private AuthorDAO authorDAO;

	private String name;

	@Autowired
	public void setAuthorDAO(AuthorDAO authorDAO) {
		this.authorDAO = authorDAO;
	}

	@Before
	public void setUp() throws Exception {
		name = getMethodNameR();
	}

	@Test
	public void create() {
		Author author = new Author();
		author.setFamilyNames(name);
		author.setGivenNames(getMethodNameR());
		authorDAO.save(author);
		authorDAO.flush();

		// use JDBC to check the database
		Author result = getJdbcUtils().getAuthor(name);
		Assert.assertEquals(name, result.getFamilyNames());

		// attempt to save with no names
		author = new Author();
		authorDAO.save(author);

		try {
			authorDAO.flush();
			Assert.fail("no exception when saving without any names");
		} catch (ConstraintViolationException e) {
			log.error(e.getMessage());
			Assert.assertTrue(true);
		}
	}

	@Test
	public void getByFamilyNames() {
		Author author = new Author();
		author.setFamilyNames(name);
		author.setGivenNames(getMethodNameR());
		authorDAO.save(author);

		Author result = authorDAO.getByFamilyName(name);
		Assert.assertEquals(name, result.getFamilyNames());

		try {
			result = authorDAO.getByFamilyName(new BigInteger(130, getR())
					.toString());
			Assert.fail("no exception when getting author with family names not in db");
		} catch (IllegalStateException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void getMatchingFamilyName() {
		int sameNameAuthors = getR().nextInt(5) + 3;
		for (int i = 0; i < sameNameAuthors; ++i) {
			Author author = new Author();
			author.setFamilyNames(name);
			author.setGivenNames(getMethodNameR());
			authorDAO.save(author);
		}

		// generate another author with different title, use BigInteger to
		// generate a random string
		Author author = new Author();
		author.setFamilyNames(new BigInteger(130, getR()).toString());
		author.setGivenNames(getMethodNameR());
		authorDAO.save(author);

		Set<Author> authors = authorDAO.getMatchingFamilyName(getMethodName(),
				0, 10);
		Assert.assertEquals(sameNameAuthors, authors.size());

	}

	@Test
	public void getByEmail() {
		Author author = new Author();
		author.setEmail(name);
		author.setFamilyNames(getMethodNameR());
		author.setGivenNames(getMethodNameR());
		authorDAO.save(author);

		Author result = authorDAO.getByEmail(name);
		Assert.assertEquals(name, result.getEmail());

		try {
			result = authorDAO.getByEmail(new BigInteger(130, getR())
					.toString());
			Assert.fail("no exception when getting author with an email not in db");
		} catch (IllegalStateException e) {
			Assert.assertTrue(true);
		}
	}

}
