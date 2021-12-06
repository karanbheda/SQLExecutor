package com.database.DbProject.controller;

import com.database.DbProject.dto.SqlResponse;
import com.database.DbProject.service.QueryService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/* @RestController is a controller but enforces REST framework*/
@RestController
@RequestMapping("/queryApi")
public class QueryController {

  @Autowired
  QueryService service;

  /* since this is under RestController, it automatically spits out JSON
    This is configurable inside @GetMapping
   */
  @PostMapping("query")
  public SqlResponse runQuery(@RequestBody Map<String, Object> request) {
    return service.getSqlOutput(request);
  }

  @PostMapping("changeDbServer")
  public boolean changeDbServer(@RequestParam("name") String dbServer) {
    return service.changeDbServer(dbServer);
  }

  @PostMapping("changeDb")
  public boolean changeDb(@RequestParam("name") String db) {
    return service.changeDb(db);
  }

  @GetMapping("getDbList")
  public List<Map<String, Object>> getDbList() {
    return service.getDbList();
  }

  @GetMapping("getDbServer")
  public String getDbServer() {
    return service.getDbServer();
  }
}
