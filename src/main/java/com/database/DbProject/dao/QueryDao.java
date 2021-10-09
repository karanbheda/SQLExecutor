package com.database.DbProject.dao;

import com.database.DbProject.dto.SqlResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
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


  private static final String USERNAME = "awsuser";
  private static final String PASSWORD = "CS527Rutgers";
  private static final String JDBC_URL =
      "jdbc:redshift://redshift-cluster-1.cmrkw7vai7ws.us-east-2.redshift.amazonaws.com:5439/dev";
  private static final String CLASS_NAME = "com.amazon.redshift.jdbc.Driver";

  //cached previous result to support pagination
  public static SqlResponse response = new SqlResponse();

  public SqlResponse getSqlOutput(final String query) {
    response = new SqlResponse();

    //db call and fill response
    try (Connection connection = ds.getConnection();
        PreparedStatement ps = connection.prepareStatement(query)) {
      //query might not be select, in that case no RS. Check for type of query
      long startTime = System.currentTimeMillis();
      //Run the rest of the program
      try (ResultSet rs = ps.executeQuery()) {
        response.setResponseTime((System.currentTimeMillis() - startTime) / 1000d);
        ResultSetMetaData metaData = rs.getMetaData();
        List<Map<String, Object>> sqlData = new ArrayList<>();

        while (rs.next()) {
          Map<String, Object> rowData = new HashMap<>();
          for (int i = 1; i <= metaData.getColumnCount(); i++) {
            rowData.put(metaData.getColumnName(i), rs.getObject(metaData.getColumnName(i)));
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
    Connection conn = null;
    Statement stmt = null;
    try {
      //Dynamically load driver at runtime.
      //Redshift JDBC 4.1 driver: com.amazon.redshift.jdbc41.Driver
      //Redshift JDBC 4 driver: com.amazon.redshift.jdbc4.Driver
      Class.forName("com.amazon.redshift.jdbc.Driver");

      //Open a connection and define properties.
      System.out.println("Connecting to database...");
      Properties props = new Properties();

      //Uncomment the following line if using a keystore.
      //props.setProperty("ssl", "true");
      props.setProperty("user", USERNAME);
      props.setProperty("password", PASSWORD);
      conn = DriverManager.getConnection(JDBC_URL, props);

      //Try a simple query.
      System.out.println("Listing system tables...");
      stmt = conn.createStatement();
      String sql;
      sql = "select * from information_schema.tables;";
      ResultSet rs = stmt.executeQuery(sql);

      //Get the data from the result set.
      while (rs.next()) {
        //Retrieve two columns.
        String catalog = rs.getString("table_catalog");
        String name = rs.getString("table_name");

        //Display values.
        System.out.print("Catalog: " + catalog);
        System.out.println(", Name: " + name);
      }
      rs.close();
      stmt.close();
      conn.close();
    } catch (Exception ex) {
      //For convenience, handle all errors here.
      ex.printStackTrace();
    } finally {
      //Finally block to close resources.
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (Exception ex) {
      }// nothing we can do
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    System.out.println("Finished connectivity test.");
    return true;
  }
}

