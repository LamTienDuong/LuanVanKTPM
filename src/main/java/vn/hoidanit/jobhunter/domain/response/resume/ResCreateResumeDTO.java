package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

public class ResCreateResumeDTO {
    private long id;
    private Instant createAt;
    private String createBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
