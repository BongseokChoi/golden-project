package com.iandna.project.logic.sample;

import com.iandna.project.config.exception.CommonJsonException;
import com.iandna.project.config.model.ResponseModel;
import com.iandna.project.config.model.MapData;
import com.iandna.project.config.model.MapData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SampleService {
  private static final Logger logger = LoggerFactory.getLogger(SampleService.class);

  private final SampleMapper sampleMapper;

  @Autowired
  public SampleService(SampleMapper sampleMapper) {
    this.sampleMapper = sampleMapper;
  }

  public ResponseModel getSampleList(MapData param) {
    ResponseModel responseModel = new ResponseModel();
    MapData result = new MapData(1, "성공");

    try {
      List<MapData> list = new ArrayList<>();
    } catch (Exception e) {
      e.printStackTrace();
      throw new CommonJsonException("E999", e.getMessage());
    }

    responseModel.setResult(result);
    return responseModel;
  }
}
