package test.edu.ualberta.cs.papersdb.server.dao.hibernate;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ImportResource("classpath:/test/edu/ualberta/cs/papersdb/server/dao/hibernate/test-config.xml")
@EnableTransactionManagement
@Profile("dev")
public class StandaloneDataConfig {

    @Value("${jdbc.driverClassName}")
    private String driverClassName;

    @Value("${jdbc.url}")
    private String url;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private boolean hibernateShowSql;

    // @Value("${hibernate.hbm2ddl.auto}")
    // private String hibernateHbm2ddlAuto;

    @Bean
    public LocalSessionFactoryBean localSessionFactory() {
        final LocalSessionFactoryBean sessionFactory =
            new LocalSessionFactoryBean();
        sessionFactory.setDataSource(this.dataSource());
        sessionFactory.setPackagesToScan(
            new String[] { "edu.ualberta.cs.papersdb.model" });
        sessionFactory.setHibernateProperties(this.hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource =
            new DriverManagerDataSource();
        dataSource.setDriverClassName(this.driverClassName);
        dataSource.setUrl(this.url);
        dataSource.setUsername("dummy");
        dataSource.setPassword("ozzy498");

        return dataSource;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        final HibernateTransactionManager txManager =
            new HibernateTransactionManager();
        txManager.setSessionFactory(localSessionFactory().getObject());

        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public PersistenceExceptionTranslator exceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    final Properties hibernateProperties() {
        return new Properties() {
            private static final long serialVersionUID = 1L;
            {
                this.put("persistence.dialect",
                    StandaloneDataConfig.this.hibernateDialect);
                // this.put("hibernate.hbm2ddl.auto",
                // PersistenceHibernateConfig.this.hibernateHbm2ddlAuto);
                this.put("hibernate.dialect",
                    StandaloneDataConfig.this.hibernateDialect);
                this.put("hibernate.show_sql",
                    StandaloneDataConfig.this.hibernateShowSql);
            }
        };
    }

}
