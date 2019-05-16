package com.ebs.ecount.objects;

import android.util.Log;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by techunity on 26/12/16.
 */
public class GetEPONumber extends SoapUtils {

    public static final String TAG_EPONUM = "eponum";
    public static final String TAG_PONUM ="ponum";
    public static final String TAG_MSG = "message";
    public static final String TAG_ORDER_TYPE = "ordertype";

    private int eponum;
    private int ponum;
    private String message;
    private String ordertype;

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

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

    public static GetEPONumber parseEPONum (SoapObject soapObject) throws Exception{

        GetEPONumber eponum = new GetEPONumber();

        eponum.setEponum(Integer.parseInt(getProperty(soapObject,TAG_EPONUM)));
        eponum.setPonum(Integer.parseInt(getProperty(soapObject,TAG_PONUM)));
        eponum.setMessage(getProperty(soapObject,TAG_MSG));
        eponum.setOrdertype(getProperty(soapObject,TAG_ORDER_TYPE));

        Log.d("Status",""+getProperty(soapObject,TAG_MSG));

        return eponum;
    }


}
