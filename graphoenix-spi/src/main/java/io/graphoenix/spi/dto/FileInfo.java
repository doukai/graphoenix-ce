package io.graphoenix.spi.dto;

public class FileInfo {

    private String filename;

    private String contentType;

    public FileInfo() {
    }

    public FileInfo(String filename, String contentType) {
        this.filename = filename;
        this.contentType = contentType;
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
}
