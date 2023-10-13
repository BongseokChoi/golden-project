package com.iandna.project.logic.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
public class MainRestController {
  private static final Logger logger = LoggerFactory.getLogger(MainRestController.class);
  private final MainService mainService;

  @Autowired
  public MainRestController(MainService mainService) {
    this.mainService = mainService;
  }
}
