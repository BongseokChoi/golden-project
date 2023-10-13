package com.iandna.project.util;

import com.amazonaws.util.Base64;
import com.iandna.project.config.model.MapData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

@Component
public class ImgUploadUtil {

  private Logger logger = LoggerFactory.getLogger(ImgUploadUtil.class);

  @Value("${s3.server.file.path}")
  private String savePath;
  @Value("${s3.server.file.url}")
  private String serverFileUrl;
  @Value("${s3.upload.bucket}")
  private String s3UploadBucket;

  @Autowired
  private ImageUtil imgUtil;

  @Value("${dynamic.s3-bucket}")
  private String dynamicBucketUrl;

  public MapData imgUpload(MapData param, HttpServletRequest request) throws Exception {
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;

    String s3UploadPath = param.getString("s3UploadPath");


    File dir = new File(savePath);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    this.logger.debug("dir : " + dir);

    long time = System.currentTimeMillis();
    SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dayTime = new SimpleDateFormat("HHmmss");
    String str = day.format(new Date(time));
    String str2 = dayTime.format(new Date(time));
    String imgUrl = null;

    for(int i = 1; i < 16; i++) {

      if (param.getString("imgFileValid" + i).equals("true")) {
        MultipartFile file = multipartRequest.getFile("imgFile" + i);
        logger.info("****file**** : " + file);


        UUID uuid = UUID.randomUUID();
        String exName = StringUtils.substring(file.getOriginalFilename(), StringUtils.lastIndexOf(file.getOriginalFilename(), "."));
        String fileName = "pcc_" + str + str2 + "_img" + i + uuid.toString().substring(0, 5) + exName;

        FileUtil.uploadS3File(savePath, s3UploadBucket, s3UploadPath, file, fileName);

        if (i == 1) {
          imgUrl = serverFileUrl + s3UploadPath + fileName;
        } else {
          imgUrl = imgUrl + "^" + serverFileUrl + s3UploadPath + fileName;
        }
      }
    }
    param.set("imgUrl", imgUrl);
    return param;
  }


  public String uploadImageFile(HttpServletRequest request, String s3UploadPath){
    String rtnVal = null;
    try{
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;

      File dir = new File(savePath);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      MultipartFile file = multipartRequest.getFile("file");
      long time = System.currentTimeMillis();
      SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
      SimpleDateFormat dayTime = new SimpleDateFormat("HHmmss");
      String str = day.format(new Date(time));
      String str2 = dayTime.format(new Date(time));
      UUID uuid = UUID.randomUUID();
      String exName = StringUtils.substring(file.getOriginalFilename(), StringUtils.lastIndexOf(file.getOriginalFilename(), "."));
      String fileName = StringUtils.replace(s3UploadPath, "/", "") + str + str2 + "_img" + uuid.toString().substring(0, 5) + exName;

      FileUtil.uploadS3File(savePath, s3UploadBucket, s3UploadPath, file, fileName);
      rtnVal = serverFileUrl + s3UploadPath + fileName;
      dir.delete();
    } catch(Exception e)
    {
      String test = e.toString();
      e.printStackTrace();
    }

    return rtnVal;
  }

  public String uploadImageFile(HttpServletRequest request, String s3UploadPath, String imgFileName){
    String rtnVal = "";
    try{
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;

      File dir = new File(savePath);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      MultipartFile file = multipartRequest.getFile(imgFileName);
      if(file == null || file.isEmpty()) {
        return null;
      }
      //이미지 가로사이즈 680px
      //file = imgUtil.imgResizeByWidth(file, 680);
      long time = System.currentTimeMillis();
      SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
      SimpleDateFormat dayTime = new SimpleDateFormat("HHmmss");
      String str = day.format(new Date(time));
      String str2 = dayTime.format(new Date(time));
      String exName = StringUtils.substring(file.getOriginalFilename(), StringUtils.lastIndexOf(file.getOriginalFilename(), "."));
      UUID uuid = UUID.randomUUID();
      //String fileName = StringUtils.replace(s3UploadPath, "/", "") + str + str2 + uuid.toString().substring(0, 5) + file.getOriginalFilename() + exName;
      String fileName = StringUtils.replace(s3UploadPath, "/", "") + str + str2 + "_img_" + uuid.toString().substring(0, 5) + exName;

      FileUtil.uploadS3File(savePath, s3UploadBucket, s3UploadPath, file, fileName);
      rtnVal = serverFileUrl + s3UploadPath + fileName;
    } catch(Exception e)
    {
      e.printStackTrace();
    }

    return rtnVal;
  }

  /*
  * 다중 업로드 시 MultipartFile type 매개변수 전달(for, while 용)
  * */
  public String uploadImageFileByMultipart(HttpServletRequest request, String s3UploadPath, String imgFileName, MultipartFile file){
    String rtnVal = "";
    try{
      File dir = new File(savePath);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      if(file == null || file.isEmpty()) {
        return null;
      }
      //이미지 가로사이즈 680px
      //file = imgUtil.imgResizeByWidth(file, 680);
      long time = System.currentTimeMillis();
      SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
      SimpleDateFormat dayTime = new SimpleDateFormat("HHmmss");
      String str = day.format(new Date(time));
      String str2 = dayTime.format(new Date(time));
      String exName = StringUtils.substring(file.getOriginalFilename(), StringUtils.lastIndexOf(file.getOriginalFilename(), "."));
      UUID uuid = UUID.randomUUID();
      //String fileName = StringUtils.replace(s3UploadPath, "/", "") + str + str2 + uuid.toString().substring(0, 5) + file.getOriginalFilename() + exName;
      String fileName = StringUtils.replace(s3UploadPath, "/", "") + str + str2 + "_img_" + uuid.toString().substring(0, 5) + exName;

      FileUtil.uploadS3File(savePath, s3UploadBucket, s3UploadPath, file, fileName);
      rtnVal = serverFileUrl + s3UploadPath + fileName;
    } catch(Exception e)
    {
      e.printStackTrace();
    }

    return rtnVal;
  }

  public MapData uploadImageFiles(HttpServletRequest request, String s3UploadPath, String customKey){
    MapData result = new MapData();
    try{
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;

      File dir = new File(savePath);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      Iterator files = multipartRequest.getFileNames();
      String fileName = null;
      StringBuffer resultString = new StringBuffer();
      while(files != null && files.hasNext()) {

        String inputName = (String)files.next();
        MultipartFile multipart_file = multipartRequest.getFile(inputName);

        if(inputName.startsWith(customKey) && multipart_file.getSize() != 0) {
          // 이미지 사이즈 가로 680px 고정
          MultipartFile multiFile = imgUtil.imgResizeByWidth(multipart_file, 680);

          long time = System.currentTimeMillis();
          SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
          SimpleDateFormat dayTime = new SimpleDateFormat("HHmmss");
          String str = day.format(new Date(time));
          String str2 = dayTime.format(new Date(time));
          UUID uuid = UUID.randomUUID();
          fileName = StringUtils.replace(s3UploadPath, "/", "") + str + str2 + uuid.toString().substring(0, 5) + multiFile.getOriginalFilename();

          FileUtil.uploadS3File(savePath, s3UploadBucket, s3UploadPath, multiFile, fileName);
          if(!multiFile.isEmpty()) {
            result.set(fileName, serverFileUrl + s3UploadPath + fileName);
          } else {
            result.set(fileName, "noImage");
          }
          resultString.append(result.getString(fileName));
          resultString.append("|");
        }
      }
      if(resultString.length() > 0){
        resultString.deleteCharAt(resultString.length() - 1);
      }
      result.set("result_string", resultString.toString());
    } catch(Exception e) {
      e.printStackTrace();
    }

    return result;
  }

  public MapData filesToBase64(MultipartHttpServletRequest multipartRequest) throws Exception	{
    MapData result = new MapData();

    Iterator files = multipartRequest.getFileNames();
    String fileName = null;

    while(files.hasNext()) {
      fileName = (String)files.next();
      MultipartFile multiFile = multipartRequest.getFile(fileName);
      BufferedImage image = ImageIO.read(multiFile.getInputStream());
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      if(image != null) {
        ImageIO.write(image, "jpg", baos);

        String encodedImage = Base64.encodeAsString(baos.toByteArray());

        result.set(multiFile.getName(), encodedImage);
      }
      baos.close();
    }
    return result;
  }

  public String fileToBase64(MultipartHttpServletRequest multipartRequest, String fileName) throws Exception	{
    String encodedImage = null;
    /* 변환 할 파일 한개 */
    MultipartFile multiFile = multipartRequest.getFile(fileName);
    BufferedImage image = ImageIO.read(multiFile.getInputStream());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    if(image != null) {
      ImageIO.write(image, "jpg", baos);

      encodedImage = Base64.encodeAsString(baos.toByteArray());

//			byte[] blob = multiFile.getBytes();
    }
    baos.close();

    return encodedImage;
  }

  public String changeImages(MultipartHttpServletRequest multipartRequest, ArrayList<String> origin_image_list, String[] change_image_list, String filePath, String fileName) {
    String img_url = "";
    if(origin_image_list == null || origin_image_list.size() == 0) {
      origin_image_list = new ArrayList<String>();
    }
    ArrayList<Integer> cg_list = new ArrayList<Integer>();
    for(String cg_img : change_image_list) {
      int cg_idx = Integer.parseInt(StringUtils.replace(cg_img, fileName, ""));
      cg_list.add(cg_idx); // -> 뒤에 숫자만 남음 ex) ["img_url2", "img_url5"] ==> [2,5]
    }
    for(int idx : cg_list) {
      logger.info("##### fileName : " + fileName + idx);
      origin_image_list.set(idx - 1, uploadImageFile(multipartRequest, filePath, fileName+"idx"));
    }
    for(String url : origin_image_list) {
      img_url += url;
      img_url += "^";
    }

    if(!img_url.isEmpty()) {
      img_url.substring(0, img_url.length()-2);
    }
    return img_url;
  }

  public String uploadImageFileWithResize(MultipartFile file, String s3UploadPath, String imgFileName, String headStr, int width){
    String rtnVal = "";
    try{
      String savePath = this.savePath;
      String s3UploadBucket = this.savePath;

      File dir = new File(savePath);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      String fileName = makeFileName(imgFileName, headStr);
      logger.debug("final fileName : " + fileName);
      FileUtil.uploadS3File(savePath, s3UploadBucket, s3UploadPath, file, fileName);
      rtnVal = this.serverFileUrl + s3UploadPath + fileName;
    } catch(Exception e)
    {
      String test = e.toString();
      e.printStackTrace();
    }
    return rtnVal;
  }

  public static String makeFileName(String originFileName, String headStr) {
    long time = System.currentTimeMillis();
    SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dayTime = new SimpleDateFormat("HHmmss");
    String str = day.format(new Date(time));
    String str2 = dayTime.format(new Date(time));
    String exName = StringUtils.substring(originFileName,
            StringUtils.lastIndexOf(originFileName, "."), originFileName.length());
    String fileName = headStr + "_" + StringUtils.substring(originFileName, 0, StringUtils.lastIndexOf(originFileName, ".")) + "_" + str + str2 + "_img" + exName;
    return fileName;
  }

  public String uploadImageFileByMultipartByAlbum(HttpServletRequest request, String s3UploadPath, String imgFileName, MultipartFile file){
    String rtnVal = "";
    try{
      File dir = new File(savePath);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      if(file == null || file.isEmpty()) {
        return null;
      }
      //이미지 가로사이즈 680px
      //file = imgUtil.imgResizeByWidth(file, 680);
      long time = System.currentTimeMillis();
      SimpleDateFormat day = new SimpleDateFormat("yyyyMMdd");
      SimpleDateFormat dayTime = new SimpleDateFormat("HHmmss");
      String str = day.format(new Date(time));
      String str2 = dayTime.format(new Date(time));
      String exName = StringUtils.substring(file.getOriginalFilename(), StringUtils.lastIndexOf(file.getOriginalFilename(), "."));
      UUID uuid = UUID.randomUUID();
      //String fileName = StringUtils.replace(s3UploadPath, "/", "") + str + str2 + uuid.toString().substring(0, 5) + file.getOriginalFilename() + exName;
      //String fileName = StringUtils.replace(s3UploadPath, "/", "") + str + str2 + "_img_" + uuid.toString().substring(0, 5) + exName;
      String fileName = "D_" + str + str2 + "_" + file.getOriginalFilename();

      FileUtil.uploadS3File("/home/ec2-user/diary_app_server/file/", "iandna-contents" + dynamicBucketUrl, s3UploadPath, file, fileName);
      rtnVal = "https://s3.ap-northeast-2.amazonaws.com/iandna-contents" + dynamicBucketUrl + "/" + s3UploadPath + fileName;
    } catch(Exception e)
    {
      e.printStackTrace();
    }

    return rtnVal;
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
  }
}
