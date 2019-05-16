package com.ebs.ecount.objects;

import android.util.Log;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by cbe-teclwsp-008 on 28/12/16.
 */
public class GetFreightDetails extends SoapUtils {

    public static final String TAG_ORDERNO = "orderno";
    public static final String TAG_BRANCH = "branch";
    public static final String TAG_WEIGHT = "weight";
    public static final String TAG_COST = "cost";
    public static final String TAG_CUSTNAME = "custname";
    public static final String TAG_ORDERTYPE = "ordertype";
    public static final String TAG_INBOUND = "inbound";
    public static final String TAG_OUTBOUND = "outbound";
    public static final String TAG_OEPORDNO = "oepordno";
    public static final String TAG_OEITEMNUMBER = "oeitemnum";
    public static final String TAG_MESSAGE = "message";

    private int orderno;
    private String branch;
    private double weight;
    private Double cost;
    private String custname;
    private String ordertype;
    private String inbound;
    private String outbound;
    private int oepordno;
    private String message;
    private int oeitemnum;

    public static GetFreightDetails parseFreight(SoapObject soapObject)throws Exception{

        GetFreightDetails freightDetails = new GetFreightDetails();

        freightDetails.setOrderno(Integer.parseInt(getProperty(soapObject,TAG_ORDERNO)));
        freightDetails.setBranch(getProperty(soapObject,TAG_BRANCH));
        freightDetails.setWeight(Double.valueOf(getProperty(soapObject,TAG_WEIGHT)));
        freightDetails.setCost(Double.parseDouble(getProperty(soapObject,TAG_COST)));
        freightDetails.setCustname(getProperty(soapObject,TAG_CUSTNAME));
        freightDetails.setOrdertype(getProperty(soapObject,TAG_ORDERTYPE));
       // String str = String.format("%.2f", (getProperty(soapObject,TAG_INBOUND)));

//        Double doub = Double.valueOf(getProperty(soapObject,TAG_INBOUND));
//
//        DecimalFormat df = new DecimalFormat("#.##");
//        String s = df.format(doub);
//
//        freightDetails.setInbound(Double.valueOf(s));

        String inboundvalue = String.format("%.2f", Double.valueOf(getProperty(soapObject,TAG_INBOUND)));
        String outboundvalue = String.format("%.2f", Double.valueOf(getProperty(soapObject,TAG_OUTBOUND)));


        Log.d("inboundvalue", inboundvalue + "" );

        freightDetails.setInbound(inboundvalue);
        freightDetails.setOutbound(outboundvalue);


//        freightDetails.setInbound(getProperty(soapObject,TAG_INBOUND));
//        freightDetails.setOutbound(getProperty(soapObject,TAG_OUTBOUND));
        freightDetails.setOepordno(Integer.parseInt(getProperty(soapObject,TAG_OEPORDNO)));
        freightDetails.setOeitemnum(Integer.parseInt(getProperty(soapObject,TAG_OEITEMNUMBER)));
        freightDetails.setMessage(getProperty(soapObject,TAG_MESSAGE));

        return freightDetails;
    }


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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }



    public int getOepordno() {
        return oepordno;
    }

    public void setOepordno(int oepordno) {
        this.oepordno = oepordno;
    }

    public int getOeitemnum() {
        return oeitemnum;
    }

    public void setOeitemnum(int oeitemnum) {
        this.oeitemnum = oeitemnum;
    }

    public String getInbound() {
        return inbound;
    }

    public void setInbound(String inbound) {
        this.inbound = inbound;
    }

    public String getOutbound() {
        return outbound;
    }

    public void setOutbound(String outbound) {
        this.outbound = outbound;
    }
}
