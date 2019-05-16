package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-008 on 29/12/16.
 */
public class GetMultiplePONumber extends SoapUtils {

    public static final String TAG_PARTNO = "partno";
    public static final String TAG_BRANCH = "branch";
    public static final String TAG_KMFG = "kmfg";
    public static final String TAG_PONUM = "ponum";
    public static final String TAG_POQTY ="poqty";
    public static final String TAG_EPORDERNO = "oepordno";
    public static final String TAG_OEITENUM = "oeitemnum";

    public static final String TAG_MSG = "message";

    private int ponum;
    private int poqty;
    private String message;
    private String partno;
    private String branch;
    private String kmfg;
    private int eporder;
    private int oeitemnum;

    private String receiveqty;

    public static GetMultiplePONumber parseMultipleNo(SoapObject soapObject) throws Exception{

        GetMultiplePONumber pomultiple = new GetMultiplePONumber();

        pomultiple.setPartno(getProperty(soapObject,TAG_PARTNO));
        pomultiple.setBranch(getProperty(soapObject,TAG_BRANCH));
        pomultiple.setKmfg(getProperty(soapObject,TAG_KMFG));
        pomultiple.setPonum(Integer.parseInt(getProperty(soapObject,TAG_PONUM)));
        pomultiple.setPoqty(Integer.parseInt(getProperty(soapObject,TAG_POQTY)));
        pomultiple.setEporder(Integer.parseInt(getProperty(soapObject,TAG_EPORDERNO)));
        pomultiple.setOeitemnum(Integer.parseInt(getProperty(soapObject,TAG_OEITENUM)));
        pomultiple.setMessage(getProperty(soapObject,TAG_MSG));

        return pomultiple;
    }


    public int getPonum() {
        return ponum;
    }

    public void setPonum(int ponum) {
        this.ponum = ponum;
    }

    public int getPoqty() {
        return poqty;
    }

    public void setPoqty(int poqty) {
        this.poqty = poqty;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPartno() {
        return partno;
    }

    public void setPartno(String partno) {
        this.partno = partno;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getKmfg() {
        return kmfg;
    }

    public void setKmfg(String kmfg) {
        this.kmfg = kmfg;
    }

    public String getReceiveqty() {
        return receiveqty;
    }

    public void setReceiveqty(String receiveqty) {
        this.receiveqty = receiveqty;
    }

    public int getEporder() {
        return eporder;
    }

    public void setEporder(int eporder) {
        this.eporder = eporder;
    }

    public int getOeitemnum() {
        return oeitemnum;
    }

    public void setOeitemnum(int oeitemnum) {
        this.oeitemnum = oeitemnum;
    }
}
