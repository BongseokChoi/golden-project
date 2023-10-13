package com.iandna.project.logic.main;

import com.iandna.project.config.model.MapData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {
  MapData selectCustTokenInfo(MapData param);

  String selectFrcsCustNoByShopId(String value);
}
