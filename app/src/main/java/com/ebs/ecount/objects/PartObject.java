package com.ebs.ecount.objects;

/**
 * Created by cbe-teclwsp-008 on 1/12/16.
 */
public class PartObject {

    private int po;
    private String part;
    private String receipt_qty;
    private String po_qty;
    private String sec_bin;
    private String prior;
    private String decription;
    private String unitprice;
    private String document;
    private String binlocation;

    private String branch;
    private String mfg;
    private String status;
    private int enter_receiveQty;

    private String old_part;
    private String transfer;

    private int epoorderno;
    private int oeitenum;

    private int totalOrdQty;

    private String old_sec_bin;
    private String old_binlocation;
    private Boolean flag = false;

    private Boolean isReplacedPart = false;


    //
    private Double weight = 0.0;
    private int order = 0;



    public PartObject(int po, String part, String receipt_qty, String po_qty, String sec_bin, String prior, String decription, String unitprice, String document, String binlocation
    , String branch, String mfg, String status, int enter_receiveQty, String old_part, String transfer, int epoorderno, int oeitenum,int totalOrdQty,
                      String old_binlocation,String old_sec_bin,Boolean flag,Boolean isReplacedPart) {
        this.po = po;
        this.part = part;
        this.receipt_qty = receipt_qty;
        this.po_qty = po_qty;
        this.sec_bin = sec_bin;
        this.prior = prior;
        this.decription = decription;
        this.unitprice = unitprice;
        this.document = document;
        this.binlocation = binlocation;
        this.branch = branch;
        this.mfg = mfg;
        this.status = status;
        this.enter_receiveQty = enter_receiveQty;
        this.old_part = old_part;
        this.transfer = transfer;
        this.epoorderno = epoorderno;
        this.oeitenum = oeitenum;
        this.totalOrdQty = totalOrdQty;
        this.old_binlocation = old_binlocation;
        this.old_sec_bin = old_sec_bin;
        this.flag = flag;
        this.isReplacedPart = isReplacedPart;

    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Boolean getReplacedPart() {
        return isReplacedPart;
    }

    public void setReplacedPart(Boolean replacedPart) {
        isReplacedPart = replacedPart;
    }

    public String getPart() {
        return part;
    }

    public String getReceipt_qty() {
        return receipt_qty;
    }

    public String getPo_qty() {
        return po_qty;
    }

    public String getPrior() {
        return prior;
    }


    public String getDocument() {
        return document;
    }

    public String getBinlocation() {
        return binlocation;
    }

    public String getSec_bin() {
        return sec_bin;
    }

    public String getDecription() {
        return decription;
    }

    public int getPo() {
        return po;
    }

    public void setPo(int po) {
        this.po = po;
    }

    public String getBranch() {
        return branch;
    }

    public String getMfg() {
        return mfg;
    }

    public String getStatus() {
        return status;
    }

    public String getOld_part() {
        return old_part;
    }

    public String getTransfer() {
        return transfer;
    }

    public int getEnter_receiveQty() {
        return enter_receiveQty;
    }

    public void setEnter_receiveQty(int enter_receiveQty) {
        this.enter_receiveQty = enter_receiveQty;
    }

    public int getEpoorderno() {
        return epoorderno;
    }

    public void setEpoorderno(int epoorderno) {
        this.epoorderno = epoorderno;
    }

    public int getOeitenum() {
        return oeitenum;
    }

    ///////////////////////////


    public void setPart(String part) {
        this.part = part;
    }

    public void setReceipt_qty(String receipt_qty) {
        this.receipt_qty = receipt_qty;
    }

    public void setPo_qty(String po_qty) {
        this.po_qty = po_qty;
    }

    public void setSec_bin(String sec_bin) {
        this.sec_bin = sec_bin;
    }

    public void setPrior(String prior) {
        this.prior = prior;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setBinlocation(String binlocation) {
        this.binlocation = binlocation;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOld_part(String old_part) {
        this.old_part = old_part;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public void setOeitenum(int oeitenum) {
        this.oeitenum = oeitenum;
    }

    public int getTotalOrdQty() {
        return totalOrdQty;
    }

    public void setTotalOrdQty(int totalOrdQty) {
        this.totalOrdQty = totalOrdQty;
    }

    public String getOld_sec_bin() {
        return old_sec_bin;
    }

    public void setOld_sec_bin(String old_sec_bin) {
        this.old_sec_bin = old_sec_bin;
    }

    public String getOld_binlocation() {
        return old_binlocation;
    }

    public void setOld_binlocation(String old_binlocation) {
        this.old_binlocation = old_binlocation;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getUnit() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }
}
