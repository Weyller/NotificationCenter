package ca.qc.lbpsb.fusion.fcmnotification.Model;

import java.io.Serializable;

/**
 * Created by w.desir on 12/13/2017.
 */

public class User implements Serializable {


    private static final long serialVersionUID = 1L;

    String email;
    String password;
    UserType type;

    public User() {
    }

    public User(UserType type) {

        this.type = type;
    }

    public User(String email, String password, UserType type) {
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", type=" + String.valueOf(type) +
                '}';
    }
}