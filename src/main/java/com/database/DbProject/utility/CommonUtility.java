package com.database.DbProject.utility;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonUtility {

  public boolean isNumeric(String s) {
    return s.chars().allMatch(Character::isDigit);
  }
}
