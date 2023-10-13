package com.iandna.project.logic.healthcheck;

import com.iandna.project.config.exception.CommonJsonException;
import com.iandna.project.config.model.ResponseModel;
import com.iandna.project.config.model.MapData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {
  private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);
  private final HealthCheckMapper healthCheckMapper;

  @Autowired
  public HealthCheckService(HealthCheckMapper healthCheckMapper) {
    this.healthCheckMapper = healthCheckMapper;
  }

  public ResponseModel getHealthCheck(MapData param) {
    ResponseModel responseModel = new ResponseModel();
    MapData result = new MapData(1, "성공");

    try {
      MapData res = healthCheckMapper.selectAppNoti(param);
    } catch (Exception e) {
      e.printStackTrace();
      throw new CommonJsonException("E999", e.getMessage());
    }

    responseModel.setResult(result);
    return responseModel;
  }
}
