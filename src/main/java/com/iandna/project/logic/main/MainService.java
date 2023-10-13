package com.iandna.project.logic.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {
  private static final Logger logger = LoggerFactory.getLogger(MainService.class);
  private final MainMapper mainMapper;

  @Autowired
  public MainService(MainMapper mainMapper) {
    this.mainMapper = mainMapper;
  }
}
