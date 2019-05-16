package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-009 on 9/12/16.
 */
public class GetProcessId extends SoapUtils {


    public static final String TAG_PROCESSID = "processid";
    public static final String TAG_ROWCNT = "rowcnt";
    public static final String TAG_MSG = "message";
    public static final String TAG_PRELIMINARY = "preliminary";
    private int processid;
    private String message;
    private int rowcnt;
    private String preliminary;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static GetProcessId parseProcessid(SoapObject soapObject) throws Exception{
        GetProcessId processid= new GetProcessId();
        processid.setProcessid(Integer.parseInt(getProperty(soapObject,TAG_PROCESSID)));
        processid.setRowcnt(Integer.parseInt(getProperty(soapObject,TAG_ROWCNT)));
        processid.setMessage(getProperty(soapObject,TAG_MSG));
        processid.setPreliminary(getProperty(soapObject,TAG_PRELIMINARY));

        return processid;

    }

    public int getProcessid() {
        return processid;
    }

    public void setProcessid(int processid) {
        this.processid = processid;
    }

    public int getRowcnt() {
        return rowcnt;
    }

    public void setRowcnt(int rowcnt) {
        this.rowcnt = rowcnt;
    }

    public String getPreliminary() {
        return preliminary;
    }

    public void setPreliminary(String preliminary) {
        this.preliminary = preliminary;
    }
}
