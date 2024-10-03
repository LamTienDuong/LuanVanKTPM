package vn.luanvan.ktpm.domain.response.file;

import java.time.Instant;
import java.util.List;

public class ResUploadMultipartFileDTO {
    private List<String> fileName;
    private Instant uploadAt;

    public ResUploadMultipartFileDTO() {}

    public ResUploadMultipartFileDTO(List<String> fileName, Instant uploadAt) {
        this.fileName = fileName;
        this.uploadAt = uploadAt;
    }

    public List<String> getFileName() {
        return fileName;
    }

    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }

    public Instant getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(Instant uploadAt) {
        this.uploadAt = uploadAt;
    }
}
