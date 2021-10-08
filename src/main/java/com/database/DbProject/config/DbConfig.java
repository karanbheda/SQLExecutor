package com.database.DbProject.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DbConfig {

  @Autowired
  Environment env;

  private static HikariConfig config = new HikariConfig();
  private static HikariDataSource mysqlDs;
  private static HikariDataSource rdsDs;

  @Bean(value = "mysql")
  public DataSource mysqlDatSource() {
    config
        .setJdbcUrl(env.getRequiredProperty("app.db.mysql.url"));
    config.setUsername(env.getRequiredProperty("app.db.mysql.username"));
    config.setPassword(env.getRequiredProperty("app.db.mysql.password"));
    config.setDriverClassName(env.getRequiredProperty("app.db.mysql.driver"));
    config.addDataSourceProperty("cachePrepStmts", env.getRequiredProperty("app.db.mysql.cache"));
    config.addDataSourceProperty("prepStmtCacheSize",
        env.getRequiredProperty("app.db.mysql.cacheSize"));
    config.addDataSourceProperty("prepStmtCacheSqlLimit",
        env.getRequiredProperty("app.db.mysql.cacheLimit"));
    mysqlDs = new HikariDataSource(config);
    return mysqlDs;
  }

  public static Connection getConnection() throws SQLException {
    return mysqlDs.getConnection();
  }

  /*@Bean(value = "redshift")
  public DataSource rdsDataSource() {
    config
        .setJdbcUrl(env.getRequiredProperty("app.db.rds.url"));
    config.setUsername(env.getRequiredProperty("app.db.rds.username"));
    config.setPassword(env.getRequiredProperty("app.db.rds.password"));
    config.setDriverClassName(env.getRequiredProperty("app.db.rds.driver"));
    rdsDs = new HikariDataSource(config);
    return rdsDs;
  }*/

  /*@Bean
  public RdsInstanceConfigurer instanceConfigurer() {
    return new RdsInstanceConfigurer() {
      @Override
      public DataSourceFactory getDataSourceFactory() {
        TomcatJdbcDataSourceFactory dataSourceFactory = new TomcatJdbcDataSourceFactory();
        dataSourceFactory.setInitialSize(10);
        dataSourceFactory.setValidationQuery("SELECT 1 FROM DUAL");
        return dataSourceFactory;
      }
    };
  }*/

}
