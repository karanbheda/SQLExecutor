package com.database.DbProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* This is the entry point of the application
	@SpringBootApplication does all the magic - creating containers, scanning components, etc
 */
@SpringBootApplication
public class DbProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(DbProjectApplication.class, args);
  }

}
