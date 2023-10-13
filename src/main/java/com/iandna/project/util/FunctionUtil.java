package com.iandna.project.util;

import com.iandna.project.config.model.MapData;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

@Component
public class FunctionUtil {
  public MapData getMapData() {
    Supplier<MapData> param = MapData::new;

    return param.get();
  }

  public String convertIntToStr(Integer intValue) {
    Function<Integer, String> intToStr = String::valueOf;

    return intToStr.apply(intValue);
  }

  public Integer convertStrToInt(String strValue) {
    ToIntFunction<String> strToInt = Integer::parseInt;

    return strToInt.applyAsInt(strValue);
  }
}
