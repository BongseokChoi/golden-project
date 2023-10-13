package com.iandna.project.logic.sample.depthTwo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sample/depthTwo")
public class DepthTwoRestController {
  private static final Logger logger = LoggerFactory.getLogger(DepthTwoRestController.class);
  private final DepthTwoService depthTwoService;

  @Autowired
  public DepthTwoRestController(DepthTwoService depthTwoService) {
    this.depthTwoService = depthTwoService;
  }
}
