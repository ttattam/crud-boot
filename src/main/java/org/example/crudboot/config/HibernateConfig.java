//package org.example.crudboot.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.hibernate5.HibernateTransactionManager;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import web.model.Role;
//import web.model.User;
//
//import javax.sql.DataSource;
//import java.util.Objects;
//import java.util.Properties;
//
//@Configuration
//@PropertySource("classpath:db.properties")
//@EnableTransactionManagement
//@ComponentScan(basePackages = "web")
//public class HibernateConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(HibernateConfig.class);
//
//    private final Environment env;
//
//    @Autowired
//    public HibernateConfig(Environment env) {
//        this.env = env;
//    }
//
//    @Bean
//    public DataSource getDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//
//        String driverClassName = env.getProperty("db.driver");
//        String url = env.getProperty("db.url");
//        String username = env.getProperty("db.username");
//        String password = env.getProperty("db.password");
//
//        logger.info("Driver ClassName: {}", driverClassName);
//        logger.info("Database URL: {}", url);
//        logger.info("Database Username: {}", username);
//
//        if (driverClassName == null || url == null || username == null || password == null) {
//            throw new IllegalArgumentException("Database connection properties cannot be null");
//        }
//
//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//
//        return dataSource;
//    }
//
//    @Bean
//    public LocalSessionFactoryBean getSessionFactory() {
//        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
//        factoryBean.setDataSource(getDataSource());
//
//        Properties props = new Properties();
//        String showSql = env.getProperty("hibernate.show_sql");
//        String hbm2ddlAuto = env.getProperty("hibernate.hbm2ddl.auto");
//        String dialect = env.getProperty("hibernate.dialect");
//        String formatSql = env.getProperty("hibernate.format_sql");
//
//        logger.info("Hibernate Properties: show_sql={}, hbm2ddl.auto={}, dialect={}, format_sql={}", showSql, hbm2ddlAuto, dialect, formatSql);
//
//        if (showSql == null || hbm2ddlAuto == null || dialect == null) {
//            throw new IllegalArgumentException("Hibernate properties cannot be null");
//        }
//
//        props.put("hibernate.show_sql", showSql);
//        props.put("hibernate.hbm2ddl.auto", hbm2ddlAuto);
//        props.put("hibernate.dialect", dialect);
//        if (formatSql != null) {
//            props.put("hibernate.format_sql", formatSql);
//        }
//
//        factoryBean.setHibernateProperties(props);
//        factoryBean.setAnnotatedClasses(User.class, Role.class);
//
//        return factoryBean;
//    }
//
//    @Bean
//    public HibernateTransactionManager getTransactionManager() {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(Objects.requireNonNull(getSessionFactory().getObject()));
//        return transactionManager;
//    }
//}
