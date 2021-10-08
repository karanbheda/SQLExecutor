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
    String page = (String) request.get("page");
    String records = (String) request.get("records");

    SqlResponse response = new SqlResponse();
    if (commonUtility.isNumeric(page) && commonUtility.isNumeric(records)) {
      response = queryDao.getSqlOutput(query);

      //perform pagination
      if (response.getData() != null && response.getData().size() > 0) {
        int pageNo = Integer.parseInt(page);
        int rows = Integer.parseInt(records);
        int startIdx = (pageNo - 1) * rows;
        int endIdx = Math.min(response.getData().size(), startIdx + rows);

        response.setData(response.getData().subList(startIdx, endIdx));
      }
    }

    return response;
  }

  public SqlResponse getPreviousOutput() {
    //perform pagination

    return queryDao.getSqlOutput("asd");
  }
}
