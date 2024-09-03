package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

public class ResUpdateResumeDTO {
    private Instant updateAt;
    private String updateBy;

    public Instant getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
