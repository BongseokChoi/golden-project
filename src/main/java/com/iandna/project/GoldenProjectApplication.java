package com.iandna.project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GoldenProjectApplication extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(GoldenProjectApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(GoldenProjectApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> System.out.println("Dispatcher Servlet Warm-Up");
  }

}
