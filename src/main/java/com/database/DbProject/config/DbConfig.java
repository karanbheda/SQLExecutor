package com.database.DbProject.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {


  private static HikariConfig config = new HikariConfig();
  private static HikariDataSource ds;

  @Bean(value = "mysql")
  public DataSource dataSource() {
    config
        .setJdbcUrl("jdbc:mysql://database-1.cmra5f09g0at.us-east-2.rds.amazonaws.com:3306/dbname");
    config.setUsername("admin");
    config.setPassword("CS527Rutgers");
    config.setDriverClassName("com.mysql.cj.jdbc.Driver");
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    ds = new HikariDataSource(config);
    return ds;
  }

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }

}
