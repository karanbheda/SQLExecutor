package com.database.DbProject.dao;

import com.database.DbProject.dto.SqlResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QueryDao {

  @Autowired
  DataSource ds;

  //cached previous result to support pagination
  public static SqlResponse response = new SqlResponse();

  public SqlResponse getSqlOutput(final String query) {
    response = new SqlResponse();

    //db call and fill response
    try (Connection connection = ds.getConnection();
        PreparedStatement ps = connection.prepareStatement("select 1 as x")) {

      //query might not be select, in that case no RS. Check for type of query
      try (ResultSet rs = ps.executeQuery()) {
        ResultSetMetaData metaData = rs.getMetaData();
        while (rs.next()) {
          System.out.println(rs.getInt("x"));
        }
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
}

