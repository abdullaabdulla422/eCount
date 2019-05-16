package com.ebs.ecount.objects;

/**
 * Created by cbetechubt-016 on 26/4/18.
 */

public class MultiplePOWithStatus {

    private String status;
    private int receiveqty;

    private int ponum;
    private int poqty;
    private String message;
    private String partno;
    private String branch;
    private String kmfg;
    private int eporder;
    private int oeitemnum;

    private int prior;

    private String price;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReceiveqty(int receiveqty) {
        this.receiveqty = receiveqty;
    }

    public void setPonum(int ponum) {
        this.ponum = ponum;
    }

    public void setPoqty(int poqty) {
        this.poqty = poqty;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPartno(String partno) {
        this.partno = partno;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setKmfg(String kmfg) {
        this.kmfg = kmfg;
    }

    public void setEporder(int eporder) {
        this.eporder = eporder;
    }

    public void setOeitemnum(int oeitemnum) {
        this.oeitemnum = oeitemnum;
    }

    public MultiplePOWithStatus(String status,int prior,String price, int receiveqty, int ponum, int poqty, String message,
                                String partno, String branch, String kmfg, int eporder, int oeitemnum) {
        this.status = status;
        this.prior = prior;
        this.price = price;
        this.receiveqty = receiveqty;
        this.ponum = ponum;
        this.poqty = poqty;
        this.message = message;
        this.partno = partno;
        this.branch = branch;
        this.kmfg = kmfg;
        this.eporder = eporder;
        this.oeitemnum = oeitemnum;
    }

    public String getStatus() {
        return status;
    }

    public int getReceiveqty() {
        return receiveqty;
    }

    public int getPonum() {
        return ponum;
    }

    public int getPoqty() {
        return poqty;
    }

    public String getMessage() {
        return message;
    }

    public String getPartno() {
        return partno;
    }

    public String getBranch() {
        return branch;
    }

    public String getKmfg() {
        return kmfg;
    }

    public int getEporder() {
        return eporder;
    }

    public int getOeitemnum() {
        return oeitemnum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPrior() {
        return prior;
    }

    public void setPrior(int prior) {
        this.prior = prior;
    }
}

