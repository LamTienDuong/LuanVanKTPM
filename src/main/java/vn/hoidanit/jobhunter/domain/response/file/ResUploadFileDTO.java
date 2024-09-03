package vn.hoidanit.jobhunter.domain.response.file;

import java.time.Instant;

public class ResUploadFileDTO {
    private String fileName;
    private Instant uploadAt;

    public String getFileName() {
        return fileName;
    }

    public ResUploadFileDTO() {
    }

    public ResUploadFileDTO(String fileName, Instant uploadAt) {
        this.fileName = fileName;
        this.uploadAt = uploadAt;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Instant getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(Instant uploadAt) {
        this.uploadAt = uploadAt;
    }
}
