package vn.luanvan.ktpm.domain.response.address;

public class ResAddressDTO {
    private long id;
    private String name;
    private String phone;
    private AddressUser user;

    public static class AddressUser {
        private long id;
        private String name;
        public AddressUser() {}

        public AddressUser(long id, String name) {
            this.id = id;
            this.name = name;
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
    }

    public ResAddressDTO() {}

    public ResAddressDTO(long id, String name, String phone, AddressUser user) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.user = user;
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

    public AddressUser getUser() {
        return user;
    }

    public void setUser(AddressUser user) {
        this.user = user;
    }
}
