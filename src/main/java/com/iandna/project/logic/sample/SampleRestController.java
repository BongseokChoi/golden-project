package com.iandna.project.logic.sample;

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
@RequestMapping("/api/sample")
public class SampleRestController {
  private static final Logger logger = LoggerFactory.getLogger(SampleRestController.class);
  private final SampleService sampleService;

  @Autowired
  public SampleRestController(SampleService sampleService) {
    this.sampleService = sampleService;
  }

  @GetMapping("/list")
  public ResponseModel getSampleList(@RequestParam Map<String, Object> paramData, HttpServletRequest request) {
    ResponseModel responseModel;

    try {
      MapData param = (MapData) request.getAttribute("paramData");
      param.putAll(paramData);

      responseModel = sampleService.getSampleList(param);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new CommonJsonException("E999", "알 수 없는 오류가 발생했습니다.");
    }

    return responseModel;
  }
}
