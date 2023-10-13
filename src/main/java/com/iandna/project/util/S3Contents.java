package com.iandna.project.util;

import java.util.Date;

public class S3Contents
{
  private String bucketName;
  private String key;
  private String extension;
  private long size;
  private Date lastModified;

  public String getBucketName()
  {
    return this.bucketName;
  }

  public void setBucketName(String bucketName)
  {
    this.bucketName = bucketName;
  }

  public String getKey()
  {
    return this.key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public String getExtension()
  {
    return this.extension;
  }

  public void setExtension(String extension)
  {
    this.extension = extension;
  }

  public long getSize()
  {
    return this.size;
  }

  public void setSize(long size)
  {
    this.size = size;
  }

  public Date getLastModified()
  {
    return this.lastModified;
  }

  public void setLastModified(Date lastModified)
  {
    this.lastModified = lastModified;
  }
}
