package com.kiegame.mobile.repository.entity.submit;

/**
 * Created by: var_rain.
 * Created date: 2020/1/14.
 * Description:
 */
public class SUserLogin {

    // *登录名
    private String loginCode;
    // *登录密吗
    private String loginPass;
    // *设备类型: 1 Android 2 IOS
    private int loginType;
    // 登录备注
    private String loginNote;

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getLoginNote() {
        return loginNote;
    }

    public void setLoginNote(String loginNote) {
        this.loginNote = loginNote;
    }
}
