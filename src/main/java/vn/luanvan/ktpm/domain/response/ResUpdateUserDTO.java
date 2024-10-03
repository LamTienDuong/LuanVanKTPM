package vn.luanvan.ktpm.domain.response;

import vn.luanvan.ktpm.util.constant.GenderEnum;

import java.time.Instant;

public class ResUpdateUserDTO {
    private long id;
    private String name;
    private String phone;
    private GenderEnum gender;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }
}
