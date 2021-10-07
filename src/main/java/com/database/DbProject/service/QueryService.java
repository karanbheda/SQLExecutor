package com.database.DbProject.service;

import com.database.DbProject.dao.QueryDao;
import com.database.DbProject.dto.SqlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

  @Autowired
  QueryDao queryDao;

  public SqlResponse getSqlOutput() {
    //perform pagination

    return queryDao.getSqlOutput("asd");
  }

  public SqlResponse getPreviousOutput() {
    //perform pagination

    return queryDao.getSqlOutput("asd");
  }
}
