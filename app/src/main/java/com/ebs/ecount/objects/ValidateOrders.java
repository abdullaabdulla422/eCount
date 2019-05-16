package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-tecmwsp-017 on 20/1/17.
 */
public class ValidateOrders extends SoapUtils {

    public static final String TAG_STATUS ="status";
    public static final String TAG_MESSAGE = "message";

    private String status;
    private String message;


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


    public static ValidateOrders parseValidateOrders (SoapObject soapObject) throws Exception{

        ValidateOrders validateorders = new ValidateOrders();

        validateorders.setStatus(getProperty(soapObject,TAG_STATUS));
        validateorders.setMessage(getProperty(soapObject,TAG_MESSAGE));


        return validateorders;
    }
}
