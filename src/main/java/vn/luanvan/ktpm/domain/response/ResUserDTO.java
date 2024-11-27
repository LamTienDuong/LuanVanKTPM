package vn.luanvan.ktpm.domain.response;

import vn.luanvan.ktpm.domain.Address;
import vn.luanvan.ktpm.domain.Role;
import vn.luanvan.ktpm.util.constant.GenderEnum;

import java.time.Instant;
import java.util.List;

public class ResUserDTO {
    private long id;
    private String name;
    private String phone;
    private String email;
    private GenderEnum gender;
    private Role role;
    private List<Address> addressList;

    private boolean active;

    public ResUserDTO() {
    }


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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
