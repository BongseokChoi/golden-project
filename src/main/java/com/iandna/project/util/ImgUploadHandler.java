package com.iandna.project.util;

import com.iandna.project.config.exception.CommonJsonException;
import com.iandna.project.config.model.MapData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class ImgUploadHandler {
  private ImgUploadUtil imgUploadUtil;

  @Autowired
  public ImgUploadHandler(ImgUploadUtil imgUploadUtil) {
    this.imgUploadUtil = imgUploadUtil;
  }

  public String insertImgHis(MapData param, HttpServletRequest request, List<MultipartFile> multipartFiles, String imgName, MultipartFile file) {
    String imgUrl = "";

    try {
      imgUrl = imgUploadUtil.uploadImageFileByMultipart(request, "diary/baby/wound/", imgName, file);
    } catch (Exception e) {
      e.printStackTrace();
      throw new CommonJsonException("E801", "아이 정보 이미지 업로드 중 에러가 발생했습니다.");
    }

    return imgUrl;
  }

  public String insertImgHisByAlbum(MapData param, HttpServletRequest request, List<MultipartFile> multipartFiles, String imgName, MultipartFile file) {
    String imgUrl = "";

    try {
      imgUrl = imgUploadUtil.uploadImageFileByMultipartByAlbum(request, param.getString("albumNo") + "/", imgName, file);
    } catch (Exception e) {
      e.printStackTrace();
      throw new CommonJsonException("E801", "아이 정보 이미지 업로드 중 에러가 발생했습니다.");
    }

    return imgUrl;
  }
}
