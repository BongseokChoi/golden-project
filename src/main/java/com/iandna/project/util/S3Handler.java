package com.iandna.project.util;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class S3Handler
{
  private static final Log logger = LogFactory.getLog(S3Handler.class);
  private AmazonS3 client;
  private String publicUrl;

  public S3Handler()
  {
    this.client = null;
    this.publicUrl = "";
  }

  @SuppressWarnings("deprecation")
  public void connect()
  {
    AWSCredentials credentials = null;
    try
    {
      credentials = new ProfileCredentialsProvider().getCredentials();
    }
    catch (Exception e)
    {
      throw new AmazonClientException(
              "Cannot load the credentials from the credential profiles file. Please make sure that your credentials file is at the correct location (~/.aws/credentials), and is in valid format.",
              e);
    }
//    this.client = new AmazonS3Client(credentials);
//    this.client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    this.client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_NORTHEAST_2).build();
    if (logger.isInfoEnabled()) {
      logger.info("S3Handler :: " + Regions.AP_NORTHEAST_2 + "에 접속하였습니다.");
    }
  }

  @SuppressWarnings("deprecation")
  public void connect(String accessKey, String secretKey)
  {
    BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

//    this.client = new AmazonS3Client(credentials);
//    this.client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
    this.client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_NORTHEAST_2).build();
    if (logger.isInfoEnabled()) {
      logger.info("S3Handler :: " + Regions.AP_NORTHEAST_2 + "에 접속하였습니다.");
    }
  }

  public void upload(String bucketName, String key, File uploadFile)
          throws Exception
  {
    upload(bucketName, key, uploadFile, false);
  }

  public void upload(String bucketName, String key, File uploadFile, boolean makePublic)
          throws Exception
  {
    if (this.client == null) {
      connect();
    }
    if (logger.isInfoEnabled()) {
      logger.debug("S3Handler :: Uploading a new object to S3 from a file. key=" + key);
    }
    PutObjectRequest obj = new PutObjectRequest(bucketName, key, uploadFile);

    upload(obj, makePublic);
  }

  public void upload(String bucketName, String key, InputStream inputStream)
          throws Exception
  {
    upload(bucketName, key, inputStream, false);
  }

  public void upload(String bucketName, String key, InputStream inputStream, boolean makePublic)
          throws Exception
  {
    if (this.client == null) {
      connect();
    }
    if (logger.isInfoEnabled()) {
      logger.debug("S3Handler :: Uploading a new object to S3 from a file. key=" + key);
    }
    byte[] contentsBytes = IOUtils.toByteArray(inputStream);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(contentsBytes.length);

    PutObjectRequest obj = new PutObjectRequest(bucketName, key, inputStream, metadata);

    upload(obj, makePublic);
  }

  private void upload(PutObjectRequest obj, boolean makePublic)
          throws Exception
  {
    try
    {
      if (makePublic)
      {
        obj.setCannedAcl(CannedAccessControlList.PublicRead);
        this.publicUrl = ("https://" + obj.getBucketName() + ".s3.amazonaws.com/" + obj.getKey());
        if (logger.isDebugEnabled()) {
          logger.debug("S3Handler :: Public URL = " + this.publicUrl);
        }
      }
      this.client.putObject(obj);
      if (logger.isInfoEnabled()) {
        logger.debug("S3Handler :: Upload completed.");
      }
    }
    catch (AmazonServiceException ase)
    {
      logger.error(
              "Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");

      logger.error("Error Message:    " + ase.getMessage());
      logger.error("HTTP Status Code: " + ase.getStatusCode());
      logger.error("AWS Error Code:   " + ase.getErrorCode());
      logger.error("Error Type:       " + ase.getErrorType());
      logger.error("Request ID:       " + ase.getRequestId());
      throw new Exception(ase.getMessage());
    }
    catch (AmazonClientException ace)
    {
      logger.error(
              "Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");

      logger.error("Error Message: " + ace.getMessage());
      throw new Exception(ace.getMessage());
    }
  }

  public String getPublicUrl()
  {
    return this.publicUrl;
  }

  public List<S3Contents> listKeys(String bucketName, String prefix)
          throws Exception
  {
    if (this.client == null) {
      connect();
    }
    List<S3Contents> list = new ArrayList<S3Contents>();
    try
    {
      ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName)
              .withPrefix(prefix);

      ObjectListing objectListing = null;
      do
      {
        objectListing = this.client.listObjects(listObjectsRequest);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries())
        {
          int dot = objectSummary.getKey().lastIndexOf(".");
          if (dot >= 0)
          {
            S3Contents obj = new S3Contents();
            obj.setBucketName(objectSummary.getBucketName());
            obj.setKey(objectSummary.getKey());
            obj.setSize(objectSummary.getSize());
            obj.setExtension(objectSummary.getKey().substring(dot + 1));

            list.add(obj);
          }
        }
        listObjectsRequest.setMarker(objectListing.getNextMarker());
      } while (objectListing.isTruncated());
    }
    catch (AmazonServiceException ase)
    {
      logger.error(
              "Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");

      logger.error("Error Message:    " + ase.getMessage());
      logger.error("HTTP Status Code: " + ase.getStatusCode());
      logger.error("AWS Error Code:   " + ase.getErrorCode());
      logger.error("Error Type:       " + ase.getErrorType());
      logger.error("Request ID:       " + ase.getRequestId());
      throw new Exception(ase.getMessage());
    }
    catch (AmazonClientException ace)
    {
      logger.error(
              "Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");

      logger.error("Error Message: " + ace.getMessage());
      throw new Exception(ace.getMessage());
    }
    return list;
  }

  public String download(String bucketName, String keyName, String localPath)
          throws Exception
  {
    String localFileName = null;
    try
    {
      if (logger.isInfoEnabled()) {
        logger.debug("S3Handler :: Downloading an object. [" + keyName + "]");
      }
      String fileName = keyName.substring(keyName.lastIndexOf("/") + 1);
      localFileName = localPath + "/" + fileName;

      File file = new File(localFileName);

      ObjectMetadata objectMetadata = this.client.getObject(new GetObjectRequest(bucketName, keyName), file);
      if (logger.isDebugEnabled()) {
        logger.debug("S3Handler :: Downloaded an object. [" + keyName + "] to [" + localFileName +
                "]. content type is " + objectMetadata.getContentType());
      }
    }
    catch (AmazonServiceException ase)
    {
      logger.error(
              "Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");

      logger.error("Error Message:    " + ase.getMessage());
      logger.error("HTTP Status Code: " + ase.getStatusCode());
      logger.error("AWS Error Code:   " + ase.getErrorCode());
      logger.error("Error Type:       " + ase.getErrorType());
      logger.error("Request ID:       " + ase.getRequestId());
      throw new Exception(ase.getMessage());
    }
    catch (AmazonClientException ace)
    {
      logger.error(
              "Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");

      logger.error("Error Message: " + ace.getMessage());
      throw new Exception(ace.getMessage());
    }
    return localFileName;
  }

  public String getContents(String bucketName, String keyName)
          throws Exception
  {
    String localFileName = null;
    try
    {
      if (logger.isInfoEnabled()) {
        logger.debug("S3Handler :: Get object contents. [" + keyName + "]");
      }
      S3Object s3object = this.client.getObject(new GetObjectRequest(bucketName, keyName));
      displayTextInputStream(s3object.getObjectContent());
    }
    catch (AmazonServiceException ase)
    {
      logger.error(
              "Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");

      logger.error("Error Message:    " + ase.getMessage());
      logger.error("HTTP Status Code: " + ase.getStatusCode());
      logger.error("AWS Error Code:   " + ase.getErrorCode());
      logger.error("Error Type:       " + ase.getErrorType());
      logger.error("Request ID:       " + ase.getRequestId());
      throw new Exception(ase.getMessage());
    }
    catch (AmazonClientException ace)
    {
      logger.error(
              "Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");

      logger.error("Error Message: " + ace.getMessage());
      throw new Exception(ace.getMessage());
    }
    return localFileName;
  }

  private String displayTextInputStream(InputStream input)
          throws IOException
  {
    StringBuffer sb = new StringBuffer();
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    for (;;)
    {
      String line = reader.readLine();
      if (line == null) {
        break;
      }
      sb.append(line);
    }
    return sb.toString();
  }

  public void delete(String bucketName, String keyName)
          throws Exception
  {
    if (logger.isInfoEnabled()) {
      logger.debug("S3Handler :: Deleting an object. [" + keyName + "]");
    }
    try
    {
      if(this.client == null) {
        connect();
      }

      //final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_2).build();

      DeleteObjectRequest request = new DeleteObjectRequest(bucketName, keyName);

      this.client.deleteObject(request);
      //this.client.deleteObject(bucketName, keyName);
      //this.client.deleteObject(request);
      logger.debug("succeed deleting");
    }
    catch (AmazonServiceException ase)
    {
      logger.error(
              "Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");

      logger.error("Error Message:    " + ase.getMessage());
      logger.error("HTTP Status Code: " + ase.getStatusCode());
      logger.error("AWS Error Code:   " + ase.getErrorCode());
      logger.error("Error Type:       " + ase.getErrorType());
      logger.error("Request ID:       " + ase.getRequestId());
      throw new Exception(ase.getMessage());
    }
    catch (AmazonClientException ace)
    {
      logger.error(
              "Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");

      logger.error("Error Message: " + ace.getMessage());
      throw new Exception(ace.getMessage());
    }
  }
}
