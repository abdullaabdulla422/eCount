package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-008 on 4/1/17.
 */
public class GetEquipmentBranch  extends SoapUtils {

    public static final String TAG_PROCESSID = "equipid";
    public static final String TAG_BRANCH = "branch";
    public static final String TAG_MESSAGE = "message";

    private String equipid;
    private String branch;
    private String message;

    public String getEquipid() {
        return equipid;
    }

    public void setEquipid(String equipid) {
        this.equipid = equipid;
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

    public static GetEquipmentBranch parseEquipmentBranch(SoapObject soapObject) throws Exception{


        GetEquipmentBranch equipmentbranch=new GetEquipmentBranch();

        equipmentbranch.setEquipid(getProperty(soapObject,TAG_PROCESSID));
        equipmentbranch.setBranch(getProperty(soapObject,TAG_BRANCH));
        equipmentbranch.setMessage(getProperty(soapObject,TAG_MESSAGE));

        return equipmentbranch;


    }
}
