package com.database.DbProject.dto;

import java.util.List;
import java.util.Map;

public class SqlResponse {

  List<Map<String, Object>> data;

  int responseTime;

  String message;

  public List<Map<String, Object>> getData() {
    return data;
  }

  public void setData(List<Map<String, Object>> data) {
    this.data = data;
  }

  public int getResponseTime() {
    return responseTime;
  }

  public void setResponseTime(int responseTime) {
    this.responseTime = responseTime;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
