package com.study.shop.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * @author Tiger
 * @date 2019-12-11
 * @see com.study.shop.configuration
 **/
@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        String url = "jdbc:mysql://my.service.com:3306/secondMall?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";
        String username = "root";
        String password = "123456";
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setAutoCommit(true);
        dataSource.setMinimumIdle(5);
        dataSource.setIdleTimeout(60000);
        dataSource.setMaximumPoolSize(10);
        dataSource.setMaxLifetime(1800000);
        dataSource.setConnectionTimeout(6000);
        dataSource.setConnectionTestQuery("SELECT 1");
        return dataSource;
    }
}
