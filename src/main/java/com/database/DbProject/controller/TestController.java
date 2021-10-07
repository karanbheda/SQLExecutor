package com.database.DbProject.controller;

import com.database.DbProject.dto.SqlResponse;
import com.database.DbProject.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* @RestController is a controller but enforces REST framework*/
@RestController
@RequestMapping("/testApi")
public class TestController {

  @Autowired
  QueryService service;

  /* since this is under RestController, it automatically spits out JSON
    This is configurable inside @GetMapping
   */
  @GetMapping("test")
  public SqlResponse test() {
    return service.getSqlOutput();
  }

}
