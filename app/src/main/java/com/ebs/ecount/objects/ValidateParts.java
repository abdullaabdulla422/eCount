package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-009 on 9/1/17.
 */
public class ValidateParts extends SoapUtils {

    public static final String TAG_STATUS ="status";
    public static final String TAG_POQTY = "poqty";
    public static final String TAG_OEPORDER = "oepordno";
    public static final String TAG_ITEMNUM = "oeitemnum";
    public static final String TAG_BRANCH = "branch";
    public static final String TAG_MESSAGE = "message";

    private String status;
    private int  poqty;
    private int  oepordno;
    private int oeitemnum;
    private String message;
    private String branch;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static ValidateParts parseValidateParts (SoapObject soapObject) throws Exception{

        ValidateParts validateParts = new ValidateParts();

        validateParts.setStatus(getProperty(soapObject,TAG_STATUS));
        validateParts.setMessage(getProperty(soapObject,TAG_MESSAGE));
        validateParts.setPoqty(Integer.parseInt(getProperty(soapObject,TAG_POQTY)));
        validateParts.setOepordno(Integer.parseInt(getProperty(soapObject,TAG_OEPORDER)));
        validateParts.setOeitemnum(Integer.parseInt(getProperty(soapObject,TAG_ITEMNUM)));
        validateParts.setBranch(getProperty(soapObject,TAG_BRANCH));

        return validateParts;
    }

    public int getPoqty() {
        return poqty;
    }

    public void setPoqty(int poqty) {
        this.poqty = poqty;
    }

    public int getOepordno() {
        return oepordno;
    }

    public void setOepordno(int oepordno) {
        this.oepordno = oepordno;
    }

    public int getOeitemnum() {
        return oeitemnum;
    }

    public void setOeitemnum(int oeitemnum) {
        this.oeitemnum = oeitemnum;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
