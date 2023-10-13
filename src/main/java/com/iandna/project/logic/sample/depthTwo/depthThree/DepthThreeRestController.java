package com.iandna.project.logic.sample.depthTwo.depthThree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sample/depthTwo/depthThree")
public class DepthThreeRestController {
  private static final Logger logger = LoggerFactory.getLogger(DepthThreeRestController.class);
  private final DepthThreeService depthThreeService;

  @Autowired
  public DepthThreeRestController(DepthThreeService depthThreeService) {
    this.depthThreeService = depthThreeService;
  }
}
