package com.database.DbProject.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DbConfig {

  @Autowired
  Environment env;

//  @Bean(value = "mysql")
//  public DataSource mysqlDatSource() {
//    config
//        .setJdbcUrl(env.getRequiredProperty("app.db.mysql.url"));
//    config.setUsername(env.getRequiredProperty("app.db.mysql.username"));
//    config.setPassword(env.getRequiredProperty("app.db.mysql.password"));
//    config.setDriverClassName(env.getRequiredProperty("app.db.mysql.driver"));
//    config.addDataSourceProperty("cachePrepStmts", env.getRequiredProperty("app.db.mysql.cache"));
//    config.addDataSourceProperty("prepStmtCacheSize",
//        env.getRequiredProperty("app.db.mysql.cacheSize"));
//    config.addDataSourceProperty("prepStmtCacheSqlLimit",
//        env.getRequiredProperty("app.db.mysql.cacheLimit"));
//    mysqlDs = new HikariDataSource(config);
//    return mysqlDs;
//  }

  public Connection getConnection(final String dbServerName, final String dbName)
      throws SQLException, ClassNotFoundException {
    String url, username, password, driver;
    switch (dbServerName) {
      case "rds":
        url = env.getRequiredProperty("app.db.rds.url");
        username = env.getRequiredProperty("app.db.rds.username");
        password = env.getRequiredProperty("app.db.rds.password");
        driver = env.getRequiredProperty("app.db.rds.driver");
        break;
      case "mongodb":
        url = env.getRequiredProperty("app.db.mongo.url");
        username = env.getRequiredProperty("app.db.mongo.username");
        password = env.getRequiredProperty("app.db.mongo.password");
        driver = env.getRequiredProperty("app.db.mongo.driver");
        break;
      case "mysql":
      default:
        url = env.getRequiredProperty("app.db.mysql.url");
        username = env.getRequiredProperty("app.db.mysql.username");
        password = env.getRequiredProperty("app.db.mysql.password");
        driver = env.getRequiredProperty("app.db.mysql.driver");
        break;
    }

    Connection conn = null;
    Class.forName(driver);

    //Open a connection and define properties.
    Properties props = new Properties();

    props.setProperty("user", username);
    props.setProperty("password", password);
    conn = DriverManager.getConnection(url + dbName, props);

    return conn;
  }


}
