package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by cbe-teclwsp-008 on 9/12/16.
 */
public class GetPartsQuantity extends SoapUtils {

    public static final String TAG_PARTNO = "partno";
    public static final String TAG_QTY = "qty";
    public static final String TAG_DESC = "description";
    public static final String TAG_MSG = "message";
    public static final String TAG_MFR = "mfg";
    public static final String TAG_PRIMARYBINLOC = "primaryBinLocation";
    public static final String TAG_SECONDARYBINLOC = "secondaryBinLocation";
    public static final String TAG_ONHANDQTY = "onHandQty";
    public static final String TAG_ROWNUM = "rownum";
    public static final String TAG_ROWCNT = "rowcnt";
    public static final String TAG_LOADQTY = "loadQty";
    public static final String TAG_NOTES = "notes";

    private String partno;
    private String qty;
    private String message;
    private String description;
    private String mfr;
    private String primaryBinLocation;
    private String secondaryBinLocation;
    private String onHandQty;
    private String loadQty;
    private long rownum;
    private int rowcnt;
    private String notes;

    public String getPartno() {
        return partno;
    }

    public void setPartno(String partno) {
        this.partno = partno;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static GetPartsQuantity parseQty(SoapObject soapObject) throws Exception{
        GetPartsQuantity qty = new GetPartsQuantity();

        qty.setPartno(getProperty(soapObject,TAG_PARTNO));
        qty.setQty(getProperty(soapObject,TAG_QTY));
        qty.setDescription(getProperty(soapObject,TAG_DESC));
        qty.setMessage(getProperty(soapObject, TAG_MSG));
        qty.setMfr(getProperty(soapObject,TAG_MFR));
        qty.setPrimaryBinLocation(getProperty(soapObject,TAG_PRIMARYBINLOC));
        qty.setSecondaryBinLocation(getProperty(soapObject,TAG_SECONDARYBINLOC));
        qty.setOnHandQty(getProperty(soapObject,TAG_ONHANDQTY));
        qty.setLoadQty(getProperty(soapObject,TAG_LOADQTY));
        qty.setRowcnt(Integer.parseInt(getProperty(soapObject,TAG_ROWCNT)));
        qty.setRownum(Long.parseLong(getProperty(soapObject,TAG_ROWNUM)));
        qty.setNotes(getProperty(soapObject,TAG_NOTES));

        return qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMfr() {
        return mfr;
    }

    public void setMfr(String mfr) {
        this.mfr = mfr;
    }

    public String getPrimaryBinLocation() {
        return primaryBinLocation;
    }

    public void setPrimaryBinLocation(String primaryBinLocation) {
        this.primaryBinLocation = primaryBinLocation;
    }

    public String getSecondaryBinLocation() {
        return secondaryBinLocation;
    }

    public void setSecondaryBinLocation(String secondaryBinLocation) {
        this.secondaryBinLocation = secondaryBinLocation;
    }

    public String getLoadQty() {
        return loadQty;
    }

    public void setLoadQty(String loadQty) {
        this.loadQty = loadQty;
    }

    public String getOnHandQty() {
        return onHandQty;
    }

    public void setOnHandQty(String onHandQty) {
        this.onHandQty = onHandQty;
    }

    public long getRownum() {
        return rownum;
    }

    public void setRownum(long rownum) {
        this.rownum = rownum;
    }

    public int getRowcnt() {
        return rowcnt;
    }

    public void setRowcnt(int rowcnt) {
        this.rowcnt = rowcnt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
