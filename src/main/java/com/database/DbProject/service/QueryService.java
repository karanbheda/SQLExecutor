package com.database.DbProject.service;

import com.database.DbProject.dao.QueryDao;
import com.database.DbProject.dto.SqlResponse;
import com.database.DbProject.utility.CommonUtility;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

  @Autowired
  QueryDao queryDao;

  @Autowired
  CommonUtility commonUtility;

  public SqlResponse getSqlOutput(Map<String, Object> request) {
    String query = (String) request.get("query");
    Integer pageNo = (Integer) request.get("page");
    Integer rows = (Integer) request.get("records");
    boolean fromCache = (boolean) request.getOrDefault("fromCache", false);

    SqlResponse response = new SqlResponse();
    if ((!query.isEmpty() || fromCache) && pageNo >= 0 && rows > 0) {
      SqlResponse rawResponse;
      if (fromCache) {
        rawResponse = queryDao.getPreviousOutput();
      } else {
        rawResponse = queryDao.getSqlOutput(query);
      }

      response.setTotalRecords(rawResponse.getTotalRecords());
      response.setMessage(rawResponse.getMessage());
      response.setResponseTime(rawResponse.getResponseTime());
      //perform pagination
      if (rawResponse.getData() != null && rawResponse.getData().size() > 0) {
        int startIdx = (pageNo - 1) * rows;
        int endIdx = Math.min(rawResponse.getData().size(), startIdx + rows);

        response.setData(rawResponse.getData().subList(startIdx, endIdx));
      }
    }

    return response;
  }

  public SqlResponse getPreviousOutput() {
    //perform pagination

    return queryDao.getSqlOutput("asd");
  }
}