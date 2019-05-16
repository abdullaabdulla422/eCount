package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbetechubt-016 on 10/4/18.
 */

public class GetEPOParts extends SoapUtils {

    public static final String TAG_EPONUM = "eponum";
    public static final String TAG_PONUM ="ponum";
    public static final String TAG_MSG = "message";
    public static final String TAG_KPART = "kpart";
    public static final String TAG_ROWNUM = "rownum";
    public static final String TAG_ROWCNT = "rowcnt";

    private int eponum;
    private int ponum;
    private String message;
    private String kpart;
    private long rownum;
    private int rowcnt;

    public int getEponum() {
        return eponum;
    }

    public void setEponum(int eponum) {
        this.eponum = eponum;
    }

    public int getPonum() {
        return ponum;
    }

    public void setPonum(int ponum) {
        this.ponum = ponum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static GetEPOParts parseEPOParts (SoapObject soapObject) throws Exception{

        GetEPOParts eponum = new GetEPOParts();

        eponum.setEponum(Integer.parseInt(getProperty(soapObject,TAG_EPONUM)));
        eponum.setPonum(Integer.parseInt(getProperty(soapObject,TAG_PONUM)));
        eponum.setMessage(getProperty(soapObject,TAG_MSG));
        eponum.setKpart(getProperty(soapObject,TAG_KPART));
        eponum.setRowcnt(Integer.parseInt(getProperty(soapObject,TAG_ROWCNT)));
        eponum.setRownum(Long.parseLong(getProperty(soapObject,TAG_ROWNUM)));

        return eponum;
    }


    public String getKpart() {
        return kpart;
    }

    public void setKpart(String kpart) {
        this.kpart = kpart;
    }

    public long getRownum() {
        return rownum;
    }

    public void setRownum(long rownum) {
        this.rownum = rownum;
    }

    public int getRowcnt() {
        return rowcnt;
    }

    public void setRowcnt(int rowcnt) {
        this.rowcnt = rowcnt;
    }
}
