package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-009 on 22/4/17.
 */

public class GetManufacturers extends SoapUtils {


    public static final String TAG_MFGNAME = "mfgName";
    public static final String TAG_PARTDESC = "partDesc";
    public static final String TAG_MSG = "message";


    private String mfgName;
    private String partDesc;
    private String message;


    public String getMfgName() {
        return mfgName;
    }

    public void setMfgName(String mfgName) {
        this.mfgName = mfgName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static GetManufacturers parsegetmanufaturers (SoapObject soapObject) throws Exception{

       GetManufacturers getManufacturers = new GetManufacturers();

        getManufacturers.setMfgName(getProperty(soapObject,TAG_MFGNAME));
        getManufacturers.setPartDesc(getProperty(soapObject,TAG_PARTDESC));
        getManufacturers.setMessage(getProperty(soapObject,TAG_MSG));

        return  getManufacturers;

    }

    public String getPartDesc() {
        return partDesc;
    }

    public void setPartDesc(String partDesc) {
        this.partDesc = partDesc;
    }
}
