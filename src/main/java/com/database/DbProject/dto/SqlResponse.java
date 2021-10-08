package com.database.DbProject.dto;

import java.util.List;
import java.util.Map;

public class SqlResponse {

  private List<Map<String, Object>> data;

  private double responseTime;

  private String message;

  private int totalRecords;

  public List<Map<String, Object>> getData() {
    return data;
  }

  public void setData(List<Map<String, Object>> data) {
    this.data = data;
  }

  public double getResponseTime() {
    return responseTime;
  }

  public void setResponseTime(double responseTime) {
    this.responseTime = responseTime;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getTotalRecords() {
    return totalRecords;
  }

  public void setTotalRecords(int totalRecords) {
    this.totalRecords = totalRecords;
  }

  @Override
  public String toString() {
    return "SqlResponse{" +
        "data=" + data +
        ", responseTime=" + responseTime +
        ", message='" + message + '\'' +
        ", totalRecords=" + totalRecords +
        '}';
  }
}
