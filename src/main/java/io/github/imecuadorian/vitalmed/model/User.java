package io.github.imecuadorian.vitalmed.model;

import io.github.imecuadorian.library.*;

public class User {

    private final Generic<String, String> information;
    private final Generic<String, String> data;

    public User(String id, String fullName, String email, String password, String phone, String mobile, String address) {
        this.information = new Generic<>(id, fullName, email, password);
        this.data = new Generic<>(phone, mobile, address);
    }

    public String getId() {
        return information.getT1();
    }
    public String getFullName() {
        return information.getT2();
    }
    public String getEmail() {
        return information.getS1();
    }
    public String getPassword() {
        return information.getS2();
    }
    public String getPhone() {
        return data.getT1();
    }
    public String getMobile() {
        return data.getT2();
    }

    public String getAddress() {
        return data.getS1();
    }

    public boolean validateLogin(String email, String password) {
        return this.information.getS1().equals(email) && this.information.getS2().equals(password);
    }
    public void updatePassword(String newPassword) {
        this.information.setS2(newPassword);
    }
}
