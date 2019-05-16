package com.ebs.ecount.objects;

import com.ebs.ecount.webutils.SoapUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by techunity on 26/12/16.
 */
public class GetPartsDetails extends SoapUtils {

    public static final String TAG_PARTNO = "partno";
    public static final String TAG_ORDERNO ="orderno";
    public static final String TAG_PRIOR ="prior";
    public static final String TAG_PRICE ="price";
    public static final String TAG_DOUCMENT ="document";
    public static final String TAG_STARTBIN ="startbin";
    public static final String TAG_ENDBIN ="endbin";
    public static final String TAG_POQTY ="poqty";
    public static final String TAG_DESCRIPTION ="description";
    public static final String TAG_BRANCH ="branch";
    public static final String TAG_OEPORDER = "oepordno";
    public static final String TAG_MFG = "mfg";
    public static final String TAG_MSG ="message";
    public static final String TAG_ITEMNUM = "oeitemnum";
    public static final String TAG_TOTALORDQTY = "totalOrdQty";
    public static final String TAG_CSTATUS = "cstatus";
    public static final String TAG_WEIGHT = "weight";

    private String partno;
    private int orderno;
    private int prior;
    private String price;
    private int document;
    private String startbin;
    private String endbin;
    private int poqty;
    private String description;
    private String branch;
    private String mfg;
    private int  oepordno;
    private int oeitemnum;
    private int totalOrdQty;
    private String cstatus;
    private String message;
    private Double weight;

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getPartno() {
        return partno;
    }

    public void setPartno(String partno) {
        this.partno = partno;
    }

    public int getOrderno() {
        return orderno;
    }

    public void setOrderno(int orderno) {
        this.orderno = orderno;
    }

    public int getPrior() {
        return prior;
    }

    public void setPrior(int prior) {
        this.prior = prior;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getDocument() {
        return document;
    }

    public void setDocument(int document) {
        this.document = document;
    }

    public String getStartbin() {
        return startbin;
    }

    public void setStartbin(String startbin) {
        this.startbin = startbin;
    }

    public String getEndbin() {
        return endbin;
    }

    public void setEndbin(String endbin) {
        this.endbin = endbin;
    }

    public int getPoqty() {
        return poqty;
    }

    public void setPoqty(int poqty) {
        this.poqty = poqty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static GetPartsDetails parsepartsdtls(SoapObject soapObject) throws Exception{

        GetPartsDetails partsdtls = new GetPartsDetails();

        partsdtls.setPartno(getProperty(soapObject,TAG_PARTNO));
        partsdtls.setOrderno(Integer.parseInt(getProperty(soapObject,TAG_ORDERNO)));
        partsdtls.setPrior(Integer.parseInt(getProperty(soapObject,TAG_PRIOR)));
        partsdtls.setPrice(getProperty(soapObject,TAG_PRICE));
        partsdtls.setDocument(Integer.parseInt(getProperty(soapObject,TAG_DOUCMENT)));
        partsdtls.setStartbin(getProperty(soapObject,TAG_STARTBIN));
        partsdtls.setEndbin(getProperty(soapObject,TAG_ENDBIN));
        partsdtls.setPoqty(Integer.parseInt(getProperty(soapObject,TAG_POQTY)));
        partsdtls.setDescription(getProperty(soapObject,TAG_DESCRIPTION));
        partsdtls.setBranch(getProperty(soapObject,TAG_BRANCH));
        partsdtls.setMfg(getProperty(soapObject,TAG_MFG));
        partsdtls.setOepordno(Integer.parseInt(getProperty(soapObject,TAG_OEPORDER)));
        partsdtls.setOeitemnum(Integer.parseInt(getProperty(soapObject,TAG_ITEMNUM)));
        partsdtls.setTotalOrdQty(Integer.parseInt(getProperty(soapObject,TAG_TOTALORDQTY)));
        partsdtls.setWeight(Double.valueOf(getProperty(soapObject,TAG_WEIGHT)));


        if(!getProperty(soapObject,TAG_CSTATUS).contains("R")){

            if(partsdtls.getPrior() >= partsdtls.getPoqty()){
                partsdtls.setCstatus("C");
            }else {
                partsdtls.setCstatus(getProperty(soapObject,TAG_CSTATUS));
            }

        }else {

            partsdtls.setCstatus(getProperty(soapObject,TAG_CSTATUS));
        }







        partsdtls.setMessage(getProperty(soapObject,TAG_MSG));

        return partsdtls;
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

    public int getTotalOrdQty() {
        return totalOrdQty;
    }

    public void setTotalOrdQty(int totalOrdQty) {
        this.totalOrdQty = totalOrdQty;
    }

    public String getCstatus() {
        return cstatus;
    }

    public void setCstatus(String cstatus) {
        this.cstatus = cstatus;
    }
}
