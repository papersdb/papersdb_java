package test.edu.ualberta.ca.papersdb.server.dao.hibernate;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class PersistenceHibernateConfig {

    // @Value("${jdbc.driverClassName}")
    private String driverClassName = "com.mysql.jdbc.Driver";

    // @Value("${jdbc.url}")
    private String url = "jdbc:mysql://localhost:3306/papersdb";

    // @Value("${hibernate.dialect}")
    String hibernateDialect = "org.hibernate.dialect.MySQL5InnoDBDialect";

    // @Value("${hibernate.show_sql}")
    boolean hibernateShowSql = true;

    @Value("${hibernate.hbm2ddl.auto}")
    String hibernateHbm2ddlAuto;

    @Bean
    public LocalSessionFactoryBean alertsSessionFactoryBean() {
        final LocalSessionFactoryBean sessionFactory =
            new LocalSessionFactoryBean();
        sessionFactory.setDataSource(this.restDataSource());
        sessionFactory.setPackagesToScan(
            new String[] { "edu.ualberta.cs.papersdb.model" });
        sessionFactory.setHibernateProperties(this.hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource restDataSource() {
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
        txManager
            .setSessionFactory(this.alertsSessionFactoryBean().getObject());

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
            {
                this.put("persistence.dialect",
                    PersistenceHibernateConfig.this.hibernateDialect);
                this.put("hibernate.hbm2ddl.auto",
                    PersistenceHibernateConfig.this.hibernateHbm2ddlAuto);
                this.put("hibernate.dialect",
                    PersistenceHibernateConfig.this.hibernateDialect);
                this.put("hibernate.show_sql",
                    PersistenceHibernateConfig.this.hibernateShowSql);
            }
        };
    }
}