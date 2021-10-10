package com.database.DbProject.dao;

import com.database.DbProject.config.DbConfig;
import com.database.DbProject.dto.SqlResponse;
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
  private static String DB_SV_NAME = "mysql";
  private static String DB_NAME = "Instacart";

  //cached previous result to support pagination
  private static SqlResponse response = new SqlResponse();

  public SqlResponse getSqlOutput(final String query) {
    response = new SqlResponse();

    //db call and fill response
    try (Connection connection = ds.getConnection(DB_SV_NAME, DB_NAME);
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

  public boolean changeDbServer(final String dbServer) {
    DB_SV_NAME = dbServer;
    return true;
  }

  public boolean changeDb(final String db) {
    DB_NAME = db;
    return true;
  }

}

