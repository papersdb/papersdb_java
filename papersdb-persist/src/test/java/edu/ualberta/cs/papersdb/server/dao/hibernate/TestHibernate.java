package edu.ualberta.cs.papersdb.server.dao.hibernate;

import java.util.Random;

import javax.sql.DataSource;

import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import edu.ualberta.cs.papersdb.server.dao.jdbc.JdbcUtils;

public class TestHibernate extends
		AbstractTransactionalJUnit4SpringContextTests {
	private static final Random R = new Random();

	@Rule
	public final TestName testName = new TestName();

	private JdbcUtils jdbcUtils;

	protected String getMethodName() {
		return testName.getMethodName();
	}

	protected String getMethodNameR() {
		return testName.getMethodName() + R.nextInt();
	}

	protected static Random getR() {
		return R;
	}

	@Override
	@Autowired
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		this.jdbcUtils = new JdbcUtils();
		this.jdbcUtils.setDataSource(dataSource);
	}

	public JdbcUtils getJdbcUtils() {
		return jdbcUtils;
	}

}
