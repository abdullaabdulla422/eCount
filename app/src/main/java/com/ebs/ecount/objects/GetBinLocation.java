package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by techunity on 9/12/16.
 */
public class GetBinLocation extends SoapUtils {

    public static final String TAG_STARTBIN = "startbin";
    public static final String TAG_ENDBIN = "endbin";
    public static final String TAG_MSG = "message";

    private String startbin;
    private String endbin;
    private String message;

    public String getStartbin() {
        return startbin;
    }

    public void setStartbin(String startbin) {
        this.startbin = startbin;
    }

    public String getEndbin() {
        return endbin;
    }

    public void setEndbin(String endbin) {
        this.endbin = endbin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static GetBinLocation parseBinlocation (SoapObject soapObject) throws Exception {

        GetBinLocation binLocation = new GetBinLocation();

        binLocation.setStartbin(getProperty(soapObject,TAG_STARTBIN));
        binLocation.setEndbin(getProperty(soapObject,TAG_ENDBIN));
        binLocation.setMessage(getProperty(soapObject,TAG_MSG));

        return binLocation;
    }




}
