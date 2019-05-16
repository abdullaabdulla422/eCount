package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by techunity on 4/1/17.
 */
public class GetAttachments extends SoapUtils {

    public static final String TAG_ATTACHMENTS = "attachments";
    public static final String TAG_STKTYPE = "stktype";
    public static final String TAG_EQUIPID = "equipid";
    public static final String TAG_MSG = "message";

    private String attachments;
    private String stktype;
    private String equipid;
    private String message;


    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getStktype() {
        return stktype;
    }

    public void setStktype(String stktype) {
        this.stktype = stktype;
    }

    public String getEquipid() {
        return equipid;
    }

    public void setEquipid(String equipid) {
        this.equipid = equipid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static GetAttachments parseAttachment (SoapObject soapObject) throws Exception{

        GetAttachments getAttach = new GetAttachments();

        getAttach.setAttachments(getProperty(soapObject,TAG_ATTACHMENTS));
        getAttach.setStktype(getProperty(soapObject,TAG_STKTYPE));
        getAttach.setEquipid(getProperty(soapObject,TAG_EQUIPID));
        getAttach.setMessage(getProperty(soapObject,TAG_MSG));

        return getAttach;
    }
}
