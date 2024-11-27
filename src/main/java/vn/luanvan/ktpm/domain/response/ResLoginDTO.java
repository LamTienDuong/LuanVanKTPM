package vn.luanvan.ktpm.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.luanvan.ktpm.domain.Address;
import vn.luanvan.ktpm.domain.Role;

import java.util.List;

public class ResLoginDTO {
    @JsonProperty("access_token")
    private  String access_token;
    private UserLogin user;

    public static class UserLogin {
        private long id;
        private String email;
        private String name;
        private String avatar;
        private String phone;
        private Role role;
        private boolean active;
        private List<Address> address;

        public UserLogin() {}

        public UserLogin(long id, String email, String name, String avatar, String phone, Role role, boolean active) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.avatar = avatar;
            this.phone = phone;
            this.role = role;
            this.active = active;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public List<Address> getAddress() {
            return address;
        }

        public void setAddress(List<Address> address) {
            this.address = address;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    public static class UserGetAccount{
        private UserLogin user;

        public UserGetAccount() {
        }

        public UserGetAccount(UserLogin user) {
            this.user = user;
        }

        public UserLogin getUser() {
            return user;
        }

        public void setUser(UserLogin user) {
            this.user = user;
        }
    }

    public static class UserInsideToken {
        private long id;
        private String email;
        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }
}
