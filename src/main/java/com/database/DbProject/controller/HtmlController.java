package com.database.DbProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/* @Controller defines entry point of APIs. It is the basic annotation
 * that does not enforce any rules on the APIs under it
 *
 * @RequestMapping helps you define type of request, name of request mapping,
 * expected type of response, etc*/
@Controller
@RequestMapping("/home")
public class HtmlController {

  /* API to spit out html page */
  @GetMapping("/index")
  public ModelAndView test() {
    ModelAndView view = new ModelAndView();
    view.setViewName("indexnew");
    return view;
  }
}
