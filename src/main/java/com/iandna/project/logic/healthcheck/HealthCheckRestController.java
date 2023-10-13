package com.iandna.project.logic.healthcheck;

import com.iandna.project.config.exception.CommonJsonException;
import com.iandna.project.config.model.ResponseModel;
import com.iandna.project.config.model.MapData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthCheckRestController {
  private static final Logger logger = LoggerFactory.getLogger(HealthCheckRestController.class);
  private final HealthCheckService healthCheckService;

  @Autowired
  public HealthCheckRestController(HealthCheckService healthCheckService) {
    this.healthCheckService = healthCheckService;
  }

  @GetMapping("/was")
  public ResponseModel getHealthCheck(@RequestParam Map<String, Object> paramData, HttpServletRequest request) {
    ResponseModel responseModel;

    try {
      responseModel = new ResponseModel();
      MapData result = new MapData(1, "성공");
      responseModel.setResult(result);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new CommonJsonException("E999", "알 수 없는 오류가 발생했습니다.");
    }

    return responseModel;
  }

  @GetMapping("/db")
  public ResponseModel getHealthCheckForRDS(@RequestParam Map< String, Object> paramData, HttpServletRequest request) {
    ResponseModel responseModel;

    try {
      MapData param = new MapData();

      responseModel = healthCheckService.getHealthCheck(param);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new CommonJsonException("E999", "알 수 없는 오류가 발생했습니다.");
    }

    return responseModel;
  }
}
