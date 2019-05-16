package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-008 on 7/12/16.
 */
public class LoginObject extends SoapUtils {
    public static final String TAG_USERID = "userid";
    public static final String TAG_USER = "username";
    public static final String TAG_USERTOKEN = "usertoken";
    public static final String TAG_MSG = "message";
    public static final String TAG_BRANCH = "branch";
    public static final String TAG_LANDINGCOST = "landingcost";

    private int userid;
    private String username;
    private String usertoken;
    private String message;
    private String branch;
    private String landingcost;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertoken() {
        return usertoken;
    }

    public void setUsertoken(String usertoken) {
        this.usertoken = usertoken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static LoginObject parseLogin (SoapObject soapObject) throws Exception{

        LoginObject login = new LoginObject();

        login.setUserid(Integer.parseInt(getProperty(soapObject,TAG_USERID)));
        login.setUsername(getProperty(soapObject,TAG_USER));
        login.setUsertoken(getProperty(soapObject,TAG_USERTOKEN));
        login.setMessage(getProperty(soapObject,TAG_MSG));
        login.setBranch(getProperty(soapObject,TAG_BRANCH));
        login.setLandingcost(getProperty(soapObject,TAG_LANDINGCOST));
        return login;

    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getLandingcost() {
        return landingcost;
    }

    public void setLandingcost(String landingcost) {
        this.landingcost = landingcost;
    }
}
