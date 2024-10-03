package vn.luanvan.ktpm.domain.response;

import vn.luanvan.ktpm.util.constant.GenderEnum;

import java.time.Instant;

public class ResCreateUserDTO {
    private long id;
    private String name;
    private String phone;
    private String email;
    private GenderEnum gender;// MALE/FEMALE

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

}
