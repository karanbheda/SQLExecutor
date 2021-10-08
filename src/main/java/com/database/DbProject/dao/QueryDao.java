package com.database.DbProject.dao;

import com.database.DbProject.dto.SqlResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class QueryDao {

  @Autowired
  @Qualifier("mysql")
  DataSource ds;

  private static final String JDBC_URL =
      "jdbc:redshift://redshift-cluster-1.cmrkw7vai7ws.us-east-2.redshift.amazonaws.com:5439/dev";
  private static final String USERNAME = "awsuser";
  private static final String PASSWORD = "CS527Rutgers";
  private static final String CLASS_NAME = "com.amazon.redshift.jdbc42.Driver";

  //cached previous result to support pagination
  public static SqlResponse response = new SqlResponse();

  public SqlResponse getSqlOutput(final String query) {
    response = new SqlResponse();

    //db call and fill response
    try (Connection connection = ds.getConnection();
        PreparedStatement ps = connection.prepareStatement(query)) {
      //query might not be select, in that case no RS. Check for type of query
      try (ResultSet rs = ps.executeQuery()) {
        ResultSetMetaData metaData = rs.getMetaData();
        List<Map<String, Object>> sqlData = new ArrayList<>();

        while (rs.next()) {
          Map<String, Object> rowData = new HashMap<>();
          for (int i = 0; i < metaData.getColumnCount(); i++) {
            rowData.put(metaData.getCatalogName(i), rs.getObject(metaData.getCatalogName(i)));
          }

          sqlData.add(rowData);
        }

        response.setTotalRecords(sqlData.size());
        response.setData(sqlData);
      }

    } catch (Exception e) {
      e.printStackTrace();
      response.setMessage(e.getMessage());
    }

    return response;
  }

  public SqlResponse getPreviousOutput() {
    return response;
  }

  public boolean test() {
    Connection connection = null;
    try {
      Class.forName(CLASS_NAME);
    } catch (ClassNotFoundException e) {
      System.out.println("JDBC Driver class could not loaded");
      System.out.println(e.getMessage());
    }
    Properties properties = getPropertiesForDriverManager();
    try {
      System.out.println("Connecting to the database...");
      connection = DriverManager.getConnection(JDBC_URL, properties);
      int count = 0;
      try (PreparedStatement ps = connection.prepareStatement("select * from aisles");
          ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          count++;
        }
      }
      System.out.println(count);
      return true;
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  private Properties getPropertiesForDriverManager() {
    Properties props = new Properties();
    props.setProperty("user", USERNAME);
    props.setProperty("password", PASSWORD);
    return props;
  }
}

