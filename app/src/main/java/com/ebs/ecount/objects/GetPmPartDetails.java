package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

public class GetPmPartDetails extends SoapUtils{

    private String description= "";
    private double unit_price = 0.0;

    private static final String TAG_DESCRIPTION = "pmdesc";
    private static final String TAG_UNIT_PRICE = "pmsell05";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public static GetPmPartDetails parsePmpartdetail(SoapObject soapObject) throws Exception{

        GetPmPartDetails getPmPartDetails =new GetPmPartDetails();

        getPmPartDetails.setDescription(getProperty(soapObject,TAG_DESCRIPTION));
        getPmPartDetails.setUnit_price(Double.parseDouble(getProperty(soapObject,TAG_UNIT_PRICE)));

        return getPmPartDetails;
    }

}
