package com.database.DbProject.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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

  public Connection getConnection(final String dbServerName, final String dbName)
      throws SQLException, ClassNotFoundException {
    String url, username, password, driver, conString = "";
    switch (dbServerName) {
      case "rds":
        url = env.getRequiredProperty("app.db.rds.url");
        username = env.getRequiredProperty("app.db.rds.username");
        password = env.getRequiredProperty("app.db.rds.password");
        driver = env.getRequiredProperty("app.db.rds.driver");
        conString = String.format("%s%s%s", url, dbName, "?allowMultiQueries=true");
        break;
      case "mongodb":
        url = env.getRequiredProperty("app.db.mongo.url");
        username = env.getRequiredProperty("app.db.mongo.username");
        password = env.getRequiredProperty("app.db.mongo.password");
        driver = env.getRequiredProperty("app.db.mongo.driver");
        conString = String.format("%s%s%s", url, dbName, "?authSource=admin");
        break;
      case "mysql":
      default:
        url = env.getRequiredProperty("app.db.mysql.url");
        username = env.getRequiredProperty("app.db.mysql.username");
        password = env.getRequiredProperty("app.db.mysql.password");
        driver = env.getRequiredProperty("app.db.mysql.driver");
        conString = String.format("%s%s%s", url, dbName, "?allowMultiQueries=true");
        break;
    }

    Connection conn = null;
    Class.forName(driver);

    //Open a connection and define properties.
    Properties props = new Properties();

    props.setProperty("user", username);
    props.setProperty("password", password);
    conn = DriverManager.getConnection(conString, props);
    return conn;
  }

  public MongoClient getMongoDbConnection() {
    String url = String
        .format("mongodb://%s:%s@%s?", env.getRequiredProperty("app.db.mongo.username"),
            env.getRequiredProperty("app.db.mongo.password"),
            env.getRequiredProperty("app.db.mongo.url").split("//")[1], "?authSource=admin");
    System.out.println(url);
    MongoClientURI uri = new MongoClientURI(
        url);
    MongoClient mongoClient = new MongoClient(uri);
    return mongoClient;
  }
}
