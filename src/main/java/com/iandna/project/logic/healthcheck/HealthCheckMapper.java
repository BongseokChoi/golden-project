package com.iandna.project.logic.healthcheck;

import com.iandna.project.config.model.MapData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthCheckMapper {
  MapData selectAppNoti(MapData param);
}
