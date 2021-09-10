package com.database.DbProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class HtmlController {

  @GetMapping("/index")
  public ModelAndView test() {
    ModelAndView view = new ModelAndView();
    view.setViewName("index");
    return view;
  }
}
