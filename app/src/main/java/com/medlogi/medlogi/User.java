package com.medlogi.medlogi;

public class User
{
    String mobile_no,password;
    String data;
    public User(String mobile_no,String password) {
        this.mobile_no=mobile_no;
        this.password=password;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getPassword() {
        return password;
    }

    public String getData() {
        return data;
    }
}
