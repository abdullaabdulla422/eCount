package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-008 on 8/12/16.
 */
public class GetDealerBranch extends SoapUtils{

    public static final String TAG_BRANCH = "branch";
    public static final String TAG_MSG = "message";

    private String branch;
    private String message;

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

    public static GetDealerBranch parseBranch(SoapObject soapObject) throws Exception{

        GetDealerBranch branch = new GetDealerBranch();

        branch.setBranch(getProperty(soapObject,TAG_BRANCH));
        branch.setMessage(getProperty(soapObject,TAG_MSG));

        return branch;
    }
}
