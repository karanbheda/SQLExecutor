package com.database.DbProject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* @RestController is a controller but enforces REST framework*/
@RestController
@RequestMapping("/testApi")
public class TestController {

  /* since this is under RestController, it automatically spits out JSON
    This is configurable inside @GetMapping
   */
  @GetMapping("test")
  public String test() {
    return "Hello world";
  }

}
