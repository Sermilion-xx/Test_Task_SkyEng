package ru.skyeng.skyenglogin.Network;

import android.support.annotation.NonNull;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 26/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class SEUser implements Comparable<SEUser>{

    private String email;
    private String password;
    private String tempPassword;

    public SEUser(String email, String password) {
        this.email = email;
        this.password = password;
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

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    @Override
    public int compareTo(@NonNull SEUser user) {
        boolean email = this.getEmail().equals(user.getEmail());
        boolean password = this.getPassword().equals(user.getPassword());
        boolean tempPass = this.getTempPassword().equals(user.getPassword());
        if(email && (password || tempPass)){
            return 0;
        }else{
           return -1;
        }
    }
}
