package VO;

import java.io.Serializable;

/**
 * Created by Jeremie on 2015/5/14.
 */
public class UserVO implements Serializable {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
