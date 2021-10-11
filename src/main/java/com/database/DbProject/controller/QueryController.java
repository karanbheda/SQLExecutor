package com.database.DbProject.controller;

import com.database.DbProject.dto.SqlResponse;
import com.database.DbProject.service.QueryService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}