package edu.ualberta.cs.papersdb;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import edu.ualberta.cs.papersdb.service.PapersdbService;

@ComponentScan(basePackages = "edu.ualberta.cs.papersdb")
@Configuration
@PropertySource("classpath:jdbc.properties")
@SuppressWarnings("nls")
public class Main {

    @Autowired
    Environment env;

    public static void main(String[] args) throws Exception {
        // Configure Log4J
        PropertyConfigurator.configure(Main.class.getClassLoader().getResource(
            "log4j.properties"));

        AnnotationConfigApplicationContext ctx =
            new AnnotationConfigApplicationContext();
        ctx.register(Main.class);
        ctx.refresh();

        final PapersdbService papersdbService =
            ctx.getBean(PapersdbService.class);
        papersdbService.test();
    }

    @Bean
    public LocalSessionFactoryBean localSessionFactory() {
        final LocalSessionFactoryBean sessionFactory =
            new LocalSessionFactoryBean();
        sessionFactory.setDataSource(this.dataSource());
        sessionFactory.setPackagesToScan("edu.ualberta.cs.papersdb.model");
        sessionFactory.setHibernateProperties(this.hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource =
            new DriverManagerDataSource();
        // dataSource.setDriverClassName(this.driverClassName);
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
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
                put("persistence.dialect", env.getProperty("hibernate.dialect"));
                put("hibernate.dialect", env.getProperty("hibernate.dialect"));

                if (env.getProperty("hibernate.show_sql") != null) {
                    put("hibernate.show_sql",
                        new Boolean(env.getProperty("hibernate.show_sql")));
                }

                if (env.getProperty("hibernate.hbm2ddl.auto") != null) {
                    put("hibernate.hbm2ddl.auto",
                        env.getProperty("hibernate.hbm2ddl.auto"));
                }
            }
        };
    }
}
