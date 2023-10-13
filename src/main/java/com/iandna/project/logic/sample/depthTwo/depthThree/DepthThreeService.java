package com.iandna.project.logic.sample.depthTwo.depthThree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepthThreeService {
  private static final Logger logger = LoggerFactory.getLogger(DepthThreeService.class);
  private final DepthThreeMapper depthThreeMapper;

  @Autowired
  public DepthThreeService(DepthThreeMapper depthThreeMapper) {
    this.depthThreeMapper = depthThreeMapper;
  }
}
