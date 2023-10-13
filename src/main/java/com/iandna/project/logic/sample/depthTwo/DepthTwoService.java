package com.iandna.project.logic.sample.depthTwo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepthTwoService {
  private static final Logger logger = LoggerFactory.getLogger(DepthTwoService.class);
  private final DepthTwoMapper depthTwoMapper;

  @Autowired
  public DepthTwoService(DepthTwoMapper depthTwoMapper) {
    this.depthTwoMapper = depthTwoMapper;
  }
}
