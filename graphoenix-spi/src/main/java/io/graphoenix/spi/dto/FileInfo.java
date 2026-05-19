package io.graphoenix.spi.dto;

public class FileInfo {

  private String filename;

  private String contentType;

  private Integer contentLength;

  public FileInfo() {}

  public FileInfo(String filename, String contentType, Integer contentLength) {
    this.filename = filename;
    this.contentType = contentType;
    this.contentLength = contentLength;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public Integer getContentLength() {
    return contentLength;
  }

  public void setContentLength(Integer contentLength) {
    this.contentLength = contentLength;
  }
}
