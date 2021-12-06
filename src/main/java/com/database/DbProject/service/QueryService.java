package com.database.DbProject.service;

import com.database.DbProject.dao.QueryDao;
import com.database.DbProject.dto.SqlResponse;
import com.database.DbProject.utility.CommonUtility;
import java.util.List;
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

  public boolean changeDbServer(final String dbServer) {
    return queryDao.changeDbServer(dbServer);
  }

  public boolean changeDb(final String db) {
    return queryDao.changeDb(db);
  }

  public List<Map<String, Object>> getDbList() {
    return queryDao.getDbList();
  }

  public String getDbServer() {
    return queryDao.getDbServer();
  }
}
