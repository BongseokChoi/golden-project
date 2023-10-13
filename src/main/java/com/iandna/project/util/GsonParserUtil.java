package com.iandna.project.util;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class GsonParserUtil {
  Gson gson = new Gson();

  public String parseString(Object obj) {
    return gson.toJson(obj);
  }
}
