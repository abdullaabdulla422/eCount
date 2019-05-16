package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-009 on 6/1/17.
 */
public class GetLandingcostDetails extends SoapUtils {

    public static final String TAG_ORDERNO = "orderno";
    public static final String TAG_BRANCH ="branch";
    public static final String TAG_VENDORNO ="vendorno";
    public static final String TAG_ORDERTYPE ="ordertype";
    public static final String TAG_ORDERDATE ="orderdate";
    public static final String TAG_EXPDATE ="expdate";
    public static final String TAG_MFG ="mfg";
    public static final String TAG_PARTNO ="partno";
    public static final String TAG_ORDQTY ="ordqty";
    public static final String TAG_RECQTY ="recqty";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_COST ="cost";
    public static final String TAG_MESSAGE ="message";

    private int orderno;
    private String branch;
    private String vendorno;
    private String ordertype;
    private String orderdate;
    private String expdate;
    private String mfg;
    private String partno;
    private int ordqty;
    private int recqty;
    private String description;
    private Double cost;
    private String message;


    public int getOrderno() {
        return orderno;
    }

    public void setOrderno(int orderno) {
        this.orderno = orderno;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getVendorno() {
        return vendorno;
    }

    public void setVendorno(String vendorno) {
        this.vendorno = vendorno;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }

    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    public String getPartno() {
        return partno;
    }

    public void setPartno(String partno) {
        this.partno = partno;
    }

    public int getOrdqty() {
        return ordqty;
    }

    public void setOrdqty(int ordqty) {
        this.ordqty = ordqty;
    }

    public int getRecqty() {
        return recqty;
    }

    public void setRecqty(int recqty) {
        this.recqty = recqty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static GetLandingcostDetails parseGetLandingcostDetails(SoapObject soapObject)throws Exception{


        GetLandingcostDetails landcost=new GetLandingcostDetails();

        landcost.setOrderno(Integer.parseInt(getProperty(soapObject,TAG_ORDERNO)));
        landcost.setBranch(getProperty(soapObject,TAG_BRANCH));
        landcost.setMessage(getProperty(soapObject,TAG_MESSAGE));
        landcost.setMfg(getProperty(soapObject,TAG_MFG));
        landcost.setPartno(getProperty(soapObject,TAG_PARTNO));
        landcost.setOrdqty(Integer.parseInt(getProperty(soapObject,TAG_ORDQTY)));
        landcost.setRecqty(Integer.parseInt(getProperty(soapObject,TAG_RECQTY)));
        landcost.setCost(Double.parseDouble(getProperty(soapObject,TAG_COST)));
        landcost.setVendorno(getProperty(soapObject,TAG_VENDORNO));
        landcost.setOrdertype(getProperty(soapObject,TAG_ORDERTYPE));
        landcost.setOrderdate(getProperty(soapObject,TAG_ORDERDATE));
        landcost.setExpdate(getProperty(soapObject,TAG_EXPDATE));
        landcost.setDescription(getProperty(soapObject,TAG_DESCRIPTION));

        return landcost;

    }
}
