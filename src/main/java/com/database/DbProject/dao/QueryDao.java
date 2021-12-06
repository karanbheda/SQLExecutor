package com.database.DbProject.dao;

import com.database.DbProject.config.DbConfig;
import com.database.DbProject.dto.SqlResponse;
import com.mongodb.MongoClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QueryDao {

  @Autowired
  DbConfig ds;

  //selected database server and database
  private static String DB_SV_NAME = "mongodb";
  private static String DB_NAME = "instacart";

  //cached previous result to support pagination
  private static SqlResponse response = new SqlResponse();

  public SqlResponse getSqlOutput(final String query) {
    response = new SqlResponse();

    //db call and fill response
    try (Connection connection = ds.getConnection(DB_SV_NAME, DB_NAME);
        PreparedStatement ps = connection.prepareStatement(query)) {
      long startTime = System.currentTimeMillis();
      //Run the rest of the program
      try (ResultSet rs = ps.executeQuery()) {
        response.setResponseTime((System.currentTimeMillis() - startTime) / 1000d);
        ResultSetMetaData metaData = rs.getMetaData();
        List<Map<String, Object>> sqlData = new ArrayList<>();

        while (rs.next()) {
          Map<String, Object> rowData = new HashMap<>();
          for (int i = 1; i <= metaData.getColumnCount(); i++) {
            rowData.put(metaData.getColumnLabel(i), rs.getObject(metaData.getColumnName(i)));
          }

          sqlData.add(rowData);
        }

        response.setTotalRecords(sqlData.size());
        response.setData(sqlData);
      }

    } catch (Exception e) {
      e.printStackTrace();
      response.setMessage(e.getMessage());
      response.setData(null);
      response.setTotalRecords(0);
    }

    return response;
  }

  public SqlResponse getPreviousOutput() {
    return response;
  }

  public boolean changeDbServer(final String dbServer) {
    DB_SV_NAME = dbServer;
    switch (DB_SV_NAME) {
      case "mongodb":
      case "rds":
        DB_NAME = "instacart";
        break;
      case "mysql":
      default:
        DB_NAME = "Instacart";
    }
    return true;
  }

  public boolean changeDb(final String db) {
    DB_NAME = db;
    return true;
  }

  private String getDatabaseListQuery() {
    switch (DB_SV_NAME) {
      case "rds":
        return "select datname as database_name\n"
            + "from pg_database\n"
            + "order by oid;";
      case "mongodb":
        return "show dbs";
      case "mysql":
      default:
        return "show databases";
    }
  }

  public List<Map<String, Object>> getDbList() {
    List<Map<String, Object>> databaseList = new ArrayList<>();

    if (DB_SV_NAME.equals("mongodb")) {
      try (MongoClient connection = ds.getMongoDbConnection()) {
        for (String dbName : connection.listDatabaseNames()) {
          Map<String, Object> rowData = new HashMap<>();
          rowData.put("value", dbName);
          rowData.put("isSelected", dbName.equalsIgnoreCase(DB_NAME));
          databaseList.add(rowData);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return databaseList;
    }

    //db call and fill response
    try (Connection connection = ds.getConnection(DB_SV_NAME, DB_NAME);
        PreparedStatement ps = connection.prepareStatement(this.getDatabaseListQuery())) {
      //Run the rest of the program
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Map<String, Object> rowData = new HashMap<>();
          String dbName = rs.getString(1);
          rowData.put("value", dbName);
          rowData.put("isSelected", dbName.equalsIgnoreCase(DB_NAME));
          databaseList.add(rowData);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return databaseList;
  }

  public String getDbServer() {
    return DB_SV_NAME;
  }
}

