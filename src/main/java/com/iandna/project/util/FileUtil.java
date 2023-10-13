package com.iandna.project.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

//import com.onetwocm.aws.S3Handler;
@Component
public class FileUtil {

  private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

  private static boolean s3Upload = false;
  private static String accessKey;
  private static String secretKey;

  @Value("true")
  public void setPrivateS3Upload(String value)
  {
    s3Upload = BooleanUtils.toBoolean(value);
  }

  @Value("${s3.access.key}")
  public void setPrivateAccessKey(String value)
  {
    accessKey = value;
  }

  @Value("${s3.secret.key}")
  public void setPrivateSecretKey(String value)
  {
    secretKey = value;
  }

  public static void writeFile(String filePath, MultipartFile file, String fileNm) {
    OutputStream out = null;
    try {
      File oldfile = new File(filePath + fileNm);
      if(oldfile.exists()) {
        oldfile.delete();
        logger.debug("이미지[" + oldfile.getAbsolutePath() + "]가 이미 등록되있어 삭제처리합니다.");
      }

      out = new FileOutputStream(filePath + fileNm);
      BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
      byte[] buffer = new byte[8106];
      int read;
      while ((read = bis.read(buffer)) > 0) {
        out.write(buffer, 0, read);
      }
    } catch (IOException ioe) {
      logger.error("이미지 업로드 중 오류가 발생하였습니다.", ioe);
    } finally {
      IOUtils.closeQuietly(out);
    }
  }

  public static void uploadS3File(String uploadPath, String s3UploadBucket, String s3UploadPath, MultipartFile multifile, String fileNm) throws Exception {
    OutputStream out = null;
    String filePath = uploadPath + fileNm;

    try {
      File oldfile = new File(filePath);
      if(oldfile.exists()) {
        oldfile.delete();
        logger.debug("이미지[" + oldfile.getAbsolutePath() + "]가 이미 등록되있어 삭제처리합니다.");
      }

      out = new FileOutputStream(filePath);
      BufferedInputStream bis = new BufferedInputStream(multifile.getInputStream());
      byte[] buffer = new byte[8106];
      int read;
      while ((read = bis.read(buffer)) > 0) {
        out.write(buffer, 0, read);
      }
    } catch (IOException ioe) {
      logger.error("이미지 업로드 중 오류가 발생하였습니다.", ioe);
      throw ioe;
    } finally {
      IOUtils.closeQuietly(out);
    }

    logger.debug("filePath : [" + filePath + "]");
    logger.debug("s3Upload : [" + s3Upload + "]");

    if(s3Upload) {
      File file = new File(filePath);

      S3Handler handler = new S3Handler();
      handler.connect(accessKey, secretKey);
      handler.upload(s3UploadBucket, s3UploadPath + fileNm, file, true);
    }
  }

}
