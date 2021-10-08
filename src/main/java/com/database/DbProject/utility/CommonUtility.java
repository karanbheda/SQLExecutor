package com.database.DbProject.utility;

public class CommonUtility {

  public boolean isNumeric(String s) {
    return s.chars().allMatch(Character::isDigit);
  }
}
