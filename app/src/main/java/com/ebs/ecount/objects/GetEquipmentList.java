package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-009 on 3/1/17.
 */
public class GetEquipmentList extends SoapUtils {

    public static final String TAG_EQUIPID = "equipid";
    public static final String TAG_MFG = "mfg";
    public static final String TAG_MODEL = "model";
    public static final String TAG_SERIALNO = "serialno";
    public static final String TAG_STATUS = "status";
    public static final String TAG_BRANCH = "branch";
    public static final String TAG_DESC = "description";
    public static final String TAG_EQUPSTATUS = "eqpstatus";
    public static final String TAG_MSG = "message";
    public static final String TAG_SCANNEDTYPE = "scannedtype";
    public static final String TAG_EQPTYPE= "eqptype";
    public static final String TAG_SUBSTAT= "substat";
    public static final String TAG_EQPTBLSTATUS= "eqptblstatus";

    private String equipid;
    private String mfg;
    private String model;
    private String serialno;
    private String status;
    private String branch;
    private String message;
    private String description;
    private String eqpstatus;
    private String eqptype;
    private String substat;
    private String eqptblstatus;
    private int scannedtype;

    public String getEqptblstatus() {
        return eqptblstatus;
    }

    public void setEqptblstatus(String eqptblstatus) {
        this.eqptblstatus = eqptblstatus;
    }

    public String getSubstat() {
        return substat;
    }

    public void setSubstat(String substat) {
        this.substat = substat;
    }

    public String getEqptype() {
        return eqptype;
    }

    public void setEqptype(String eqptype) {
        this.eqptype = eqptype;
    }

    public String getEquipid() {
        return equipid;
    }

    public void setEquipid(String equipid) {
        this.equipid = equipid;
    }

    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

   public static GetEquipmentList parseEquipmentList(SoapObject soapObject) throws Exception{

       GetEquipmentList getEquipmentList=new GetEquipmentList();

       getEquipmentList.setEquipid(getProperty(soapObject,TAG_EQUIPID));
       getEquipmentList.setModel(getProperty(soapObject,TAG_MODEL));
       getEquipmentList.setMessage(getProperty(soapObject,TAG_MSG));
       getEquipmentList.setSerialno(getProperty(soapObject,TAG_SERIALNO));
       getEquipmentList.setBranch(getProperty(soapObject,TAG_BRANCH));
       getEquipmentList.setMfg(getProperty(soapObject,TAG_MFG));
       getEquipmentList.setStatus(getProperty(soapObject,TAG_STATUS));
       getEquipmentList.setDescription(getProperty(soapObject,TAG_DESC));
       getEquipmentList.setEqpstatus(getProperty(soapObject,TAG_EQUPSTATUS));
       getEquipmentList.setEqptype(getProperty(soapObject,TAG_EQPTYPE));
       getEquipmentList.setSubstat(getProperty(soapObject,TAG_SUBSTAT));
       getEquipmentList.setEqptblstatus(getProperty(soapObject,TAG_EQPTBLSTATUS));

       int type = Integer.parseInt(getProperty(soapObject,TAG_SCANNEDTYPE));
       if (type == 7)
       {
           getEquipmentList.setScannedtype(1);
       }
       else if (type == 8)
       {
           getEquipmentList.setScannedtype(2);
       }
       else
       {
           getEquipmentList.setScannedtype(type);
       }



       return getEquipmentList;


   }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEqpstatus() {
        return eqpstatus;
    }

    public void setEqpstatus(String eqpstatus) {
        this.eqpstatus = eqpstatus;
    }

    public int getScannedtype() {
        return scannedtype;
    }

    public void setScannedtype(int scannedtype) {
        this.scannedtype = scannedtype;
    }
}
