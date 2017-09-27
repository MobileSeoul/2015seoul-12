package sgt.hansung.com.suh_gongteo;

/**
 * Created by jang on 2015-10-15.
 */
public class user {
    String userId,userPwd,userPwdConfirm,userEmail;

    public user(){
        userId = null;
        userPwd = null;
        userPwdConfirm = null;
        userEmail = null;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public void setUserPwdConfirm(String userPwdConfirm) {
        this.userPwdConfirm = userPwdConfirm;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public String getUserPwdConfirm() {
        return userPwdConfirm;
    }
}
