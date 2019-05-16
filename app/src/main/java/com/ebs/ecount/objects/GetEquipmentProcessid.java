package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-009 on 3/1/17.
 */
public class GetEquipmentProcessid extends SoapUtils{

        public static final String TAG_PROCESSID = "processid";
        public static final String TAG_MESSAGE = "message";
        public static final String TAG_PROCESSID_AND_BRANCH = "processidandbranch";
        public static final String TAG_PROCESSDESCREIPTION = "processdescription";

    private int processid;
    private String message;
    private String processidAnd_Branch;
    private String processdescription;


    public String getProcessidAnd_Branch() {
        return processidAnd_Branch;
    }

    public void setProcessidAnd_Branch(String processidAnd_Branch) {
        this.processidAnd_Branch = processidAnd_Branch;
    }

    public int getProcessid() {
        return processid;
    }

    public void setProcessid(int processid) {
        this.processid = processid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static GetEquipmentProcessid parseEquipmentProcessId(SoapObject soapObject) throws Exception{


        GetEquipmentProcessid equipmentProcessid=new GetEquipmentProcessid();

        equipmentProcessid.setProcessid(Integer.parseInt(getProperty(soapObject,TAG_PROCESSID)));
        equipmentProcessid.setMessage(getProperty(soapObject,TAG_MESSAGE));
        equipmentProcessid.setProcessidAnd_Branch(getProperty(soapObject,TAG_PROCESSID_AND_BRANCH));
        equipmentProcessid.setProcessdescription(getProperty(soapObject,TAG_PROCESSDESCREIPTION));

        return equipmentProcessid;

    }

    public String getProcessdescription() {
        return processdescription;
    }

    public void setProcessdescription(String processdescription) {
        this.processdescription = processdescription;
    }
}
