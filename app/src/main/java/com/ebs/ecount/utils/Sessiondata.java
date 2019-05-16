package com.ebs.ecount.utils;

import com.ebs.ecount.objects.GetAttachments;
import com.ebs.ecount.objects.GetBinLocation;
import com.ebs.ecount.objects.GetDealerBranch;
import com.ebs.ecount.objects.GetEPONumber;
import com.ebs.ecount.objects.GetEquipmentBranch;
import com.ebs.ecount.objects.GetEquipmentList;
import com.ebs.ecount.objects.GetEquipmentProcessid;
import com.ebs.ecount.objects.GetFreightDetails;
import com.ebs.ecount.objects.GetManufacturers;
import com.ebs.ecount.objects.GetMultiplePONumber;
import com.ebs.ecount.objects.GetPartsDetails;
import com.ebs.ecount.objects.GetPartsQuantity;
import com.ebs.ecount.objects.GetProcessId;
import com.ebs.ecount.objects.GetVarianceList;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.objects.PartObject;
import com.ebs.ecount.objects.PoObject;
import com.ebs.ecount.objects.ValidateOrders;
import com.ebs.ecount.objects.ValidateParts;

import java.util.ArrayList;

/**
 * Created by techunity on 21/11/16.
 */

public class Sessiondata {
    private static Sessiondata instance = null;

    private int Equp_image;
    private byte[] imageData;
    private byte[] hw_imageData;
    private ArrayList<byte[]> attachedFilesData = new ArrayList<byte[]>();

    private ArrayList<byte[]> walkaroundgeneralimages = new ArrayList<>();

    private ArrayList<byte[]> hw_generalimages = new ArrayList<>();
    private ArrayList<byte[]> hw_attachedFilesData = new ArrayList<byte[]>();

    private String branch_get = "";

    private int Scanner_partreceipt;
    private int Scanner_partreceiving;
    private int Scanner_replace;
    private int Scanner_counting1;
    private int Scanner_counting2;
    private int Scanner_partno;
    private int Scanner_inventory = 6;
    private int Scanner_partnumber;
    private int Scanner_hwstartbin;
    private int Scanner_hwendbin;

    private String PO_Array;

    private String branch_trim;

    private Double inBound_data;
    private Double outBound_data;
    private ArrayList<PartObject> partObjects;
    private ArrayList<GetAttachments> attachment;
    private GetEquipmentBranch branch;

    private int equp_scanner = 0;


    private String equ_branch_name = "";
    private String unitId = "";
    private String model = "";
    private String make = "";
    private String serial = "";
    private String make_model = "";
    private String process_list = "";
    private String Transfer_Equipment = "";
    private String temp_unitId = "";
    private String Permission = "";



    public String getTransfer_Equipment() {
        return Transfer_Equipment;
    }

    public void setTransfer_Equipment(String transfer_Equipment) {
        Transfer_Equipment = transfer_Equipment;
    }

    public String getProcess_list() {
        return process_list;
    }

    public void setProcess_list(String process_list) {
        this.process_list = process_list;
    }

    public String getTemp_unitId() {
        return temp_unitId;
    }

    public void setTemp_unitId(String temp_unitId) {
        this.temp_unitId = temp_unitId;
    }

    public String getTemp_model() {
        return temp_model;
    }

    public void setTemp_model(String temp_model) {
        this.temp_model = temp_model;
    }

    public String getTemp_make() {
        return temp_make;
    }

    public void setTemp_make(String temp_make) {
        this.temp_make = temp_make;
    }

    public String getTemp_serial() {
        return temp_serial;
    }

    public void setTemp_serial(String temp_serial) {
        this.temp_serial = temp_serial;
    }

    private String temp_model = "";
    private String temp_make = "";
    private String temp_serial = "";

    private String sub_process="";
    private int processId;
    private String addEquipmentId;
    private String count_value;
    private String load_value;
    private String load_value_onresume;

    private String hw_value;
    private String part_value;

    private String inventory_scanner = "start";

    private String hw_value_notmatch;

    private String replace_part="";
    private String replace_mfr="";
    private String replace_qty="";
    private String replace_binlocation="";
    private  String replace_oldpart="";
    private String replace_order="";
    private String replace_value="";
    private String replace_transfer="";

    private String po_receipt="";
    private String po_value="";

    private String po_recive="";
    private String po_recive_value="";

    private int flagphy_addpart;
    private String chk_preliminary;
    private String counting_branchno;
    private String counting_branchname;
    private String counting_processid;
    private String counting_startbin;
    private String counting_endbin;
    private String counting_partnew;
    private String counting_partnum;

    private String hw_partnumber;
    private String hw_processid;

    private String hw_mfr;
    private String hw_qty;
    private String hw_notes;

    //new

    private int load_processid;
    private String load_branch="";
    private String load_startbin="";
    private String load_endbin="";
    private String load_partNum = "";
    private Boolean loaded_partsnotcounted;
    private int load_check_enable=0;
    private int load_checkvar_enable=0;

    private String hw_endbin;
    private String hw_startbin;

    private LoginObject loginObject;
    private ArrayList<GetDealerBranch> dealerbranch;
    private ArrayList<GetEquipmentProcessid> getEquipmentProcessids;
    private ArrayList<GetEquipmentList> getEquipmentLists;

    private ArrayList<GetMultiplePONumber> multiplePONumbers;
    private ArrayList<GetFreightDetails> freightlists;
    private ArrayList<GetFreightDetails> freightlistsnew;
    private ArrayList<GetProcessId> processid;
    private ArrayList<GetBinLocation> binlocation;
    private ArrayList<GetManufacturers> getManufacturerses;
    private ArrayList<GetPartsQuantity> partsquantity;
    private ArrayList<GetPartsDetails> GetEPOParts;
    private ArrayList<GetVarianceList> varianceLists;
    private GetEPONumber EPONum;
    private ValidateParts validateParts;
    private ValidateOrders validateOrders;
    private ArrayList<PoObject> Array_EPONum = null;
    private GetPartsDetails partsDetails;

    private ArrayList<GetPartsDetails> partsDetails_list;

    private String processid_match;
    private String part_match;

    private String branch_mfg;

    private String temp_username;
    private String temp_password;

    private int addpart = 0;

    private int replaceadapter=0;

    private int partReceive=0;

    private int addFreight=0;

    private int inventory_equip_id=0;

    private int inventory_dialoghandle = 0;

    private Boolean EPO_Status = false;
    private Boolean EPO_OrderTye = false;
    private Boolean counted_Uncounted_status = false;

    public Boolean getCounted_Uncounted_status() {
        return counted_Uncounted_status;
    }

    public void setCounted_Uncounted_status(Boolean counted_Uncounted_status) {
        this.counted_Uncounted_status = counted_Uncounted_status;
    }

    private String Part_number = "";
    private String Mfg = "";
    private String startBin = "";
    private String endBin = "";

    public String getStartBin() {
        return startBin;
    }

    public void setStartBin(String startBin) {
        this.startBin = startBin;
    }

    public String getEndBin() {
        return endBin;
    }

    public void setEndBin(String endBin) {
        this.endBin = endBin;
    }

    public String getMfg() {
        return Mfg;
    }

    public void setMfg(String mfg) {
        Mfg = mfg;
    }



    public static Sessiondata getInstance() {
      if (instance == null){
          instance = new Sessiondata();
      }
        return instance;
    }


    public int getEqup_image() {
        return Equp_image;
    }

    public void setEqup_image(int equp_image) {
        Equp_image = equp_image;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public ArrayList<byte[]> getAttachedFilesData() {
        return attachedFilesData;
    }

    public void setAttachedFilesData(ArrayList<byte[]> attachedFilesData) {
        this.attachedFilesData = attachedFilesData;
    }

    public ArrayList<byte[]> getWalkaroundgeneralimages() {
        return walkaroundgeneralimages;
    }

    public void setWalkaroundgeneralimages(ArrayList<byte[]> walkaroundgeneralimages) {
        this.walkaroundgeneralimages = walkaroundgeneralimages;
    }


    public int getAddpart() {
        return addpart;
    }

    public void setAddpart(int addpart) {
        this.addpart = addpart;
    }

    public LoginObject getLoginObject() {
        return loginObject;
    }

    public void setLoginObject(LoginObject loginObject) {
        this.loginObject = loginObject;
    }

    public ArrayList<GetDealerBranch> getDealerbranch() {
        return dealerbranch;
    }

    public void setDealerbranch(ArrayList<GetDealerBranch> dealerbranch) {
        this.dealerbranch = dealerbranch;
    }


    public String getTemp_username() {
        return temp_username;
    }

    public void setTemp_username(String temp_username) {
        this.temp_username = temp_username;
    }

    public String getTemp_password() {
        return temp_password;
    }

    public void setTemp_password(String temp_password) {
        this.temp_password = temp_password;
    }


    public ArrayList<GetProcessId> getProcessid() {
        return processid;
    }

    public void setProcessid(ArrayList<GetProcessId> processid) {
        this.processid = processid;
    }



    public ArrayList<GetPartsQuantity> getPartsquantity() {
        return partsquantity;
    }

    public void setPartsquantity(ArrayList<GetPartsQuantity> partsquantity) {
        this.partsquantity = partsquantity;
    }

    public GetEPONumber getEPONum() {
        return EPONum;
    }

    public void setEPONum(GetEPONumber EPONum) {
        this.EPONum = EPONum;
    }

    public GetPartsDetails getPartsDetails() {
        return partsDetails;
    }

    public void setPartsDetails(GetPartsDetails partsDetails) {
        this.partsDetails = partsDetails;
    }

    public ArrayList<GetFreightDetails> getFreightlists() {
        return freightlists;
    }

    public void setFreightlists(ArrayList<GetFreightDetails> freightlists) {
        this.freightlists = freightlists;
    }

    public ArrayList<GetMultiplePONumber> getMultiplePONumbers() {
        return multiplePONumbers;
    }

    public void setMultiplePONumbers(ArrayList<GetMultiplePONumber> multiplePONumbers) {
        this.multiplePONumbers = multiplePONumbers;
    }

    public int getReplaceadapter() {
        return replaceadapter;
    }

    public void setReplaceadapter(int replaceadapter) {
        this.replaceadapter = replaceadapter;
    }

    public ArrayList<PartObject> getPartObjects() {
        return partObjects;
    }

    public void setPartObjects(ArrayList<PartObject> partObjects) {
        this.partObjects = partObjects;
    }


    public ArrayList<GetFreightDetails> getFreightlistsnew() {
        return freightlistsnew;
    }

    public void setFreightlistsnew(ArrayList<GetFreightDetails> freightlistsnew) {
        this.freightlistsnew = freightlistsnew;
    }


    public int getPartReceive() {
        return partReceive;
    }

    public void setPartReceive(int partReceive) {
        this.partReceive = partReceive;
    }


    public int getAddFreight() {
        return addFreight;
    }

    public void setAddFreight(int addFreight) {
        this.addFreight = addFreight;
    }


    public Double getInBound_data() {
        return inBound_data;
    }

    public void setInBound_data(Double inBound_data) {
        this.inBound_data = inBound_data;
    }

    public Double getOutBound_data() {
        return outBound_data;
    }

    public void setOutBound_data(Double outBound_data) {
        this.outBound_data = outBound_data;
    }

    public ArrayList<GetEquipmentProcessid> getGetEquipmentProcessids() {
        return getEquipmentProcessids;
    }

    public void setGetEquipmentProcessids(ArrayList<GetEquipmentProcessid> getEquipmentProcessids) {
        this.getEquipmentProcessids = getEquipmentProcessids;
    }

    public ArrayList<GetEquipmentList> getGetEquipmentLists() {
        return getEquipmentLists;
    }

    public void setGetEquipmentLists(ArrayList<GetEquipmentList> getEquipmentLists) {
        this.getEquipmentLists = getEquipmentLists;
    }


    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getMake_model() {
        return make_model;
    }

    public void setMake_model(String make_model) {
        this.make_model = make_model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }


    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public String getAddEquipmentId() {
        return addEquipmentId;
    }

    public void setAddEquipmentId(String addEquipmentId) {
        this.addEquipmentId = addEquipmentId;
    }

    public GetEquipmentBranch getBranch() {
        return branch;
    }

    public void setBranch(GetEquipmentBranch branch) {
        this.branch = branch;
    }

    public ArrayList<GetAttachments> getAttachment() {
        return attachment;
    }

    public void setAttachment(ArrayList<GetAttachments> attachment) {
        this.attachment = attachment;
    }

    public Boolean getEPO_Status() {
        return EPO_Status;
    }

    public void setEPO_Status(Boolean EPO_Status) {
        this.EPO_Status = EPO_Status;
    }

    public ArrayList<PoObject> getArray_EPONum() {
        return Array_EPONum;
    }

    public void setArray_EPONum(ArrayList<PoObject> array_EPONum) {
        Array_EPONum = array_EPONum;
    }

    public ValidateParts getValidateParts() {
        return validateParts;
    }

    public void setValidateParts(ValidateParts validateParts) {
        this.validateParts = validateParts;
    }

    public String getSub_process() {
        return sub_process;
    }

    public void setSub_process(String sub_process) {
        this.sub_process = sub_process;
    }

    public int getInventory_equip_id() {
        return inventory_equip_id;
    }

    public void setInventory_equip_id(int inventory_equip_id) {
        this.inventory_equip_id = inventory_equip_id;
    }

    public String getBranch_get() {
        return branch_get;
    }

    public void setBranch_get(String branch_get) {
        this.branch_get = branch_get;
    }

    public String getBranch_trim() {
        return branch_trim;
    }

    public void setBranch_trim(String branch_trim) {
        this.branch_trim = branch_trim;
    }

    public ValidateOrders getValidateOrders() {
        return validateOrders;
    }

    public void setValidateOrders(ValidateOrders validateOrders) {
        this.validateOrders = validateOrders;
    }

    public int getScanner_partreceipt() {
        return Scanner_partreceipt;
    }

    public void setScanner_partreceipt(int scanner_partreceipt) {
        Scanner_partreceipt = scanner_partreceipt;
    }

    public int getScanner_partreceiving() {
        return Scanner_partreceiving;
    }

    public void setScanner_partreceiving(int scanner_partreceiving) {
        Scanner_partreceiving = scanner_partreceiving;
    }

    public int getScanner_replace() {
        return Scanner_replace;
    }

    public void setScanner_replace(int scanner_replace) {
        Scanner_replace = scanner_replace;
    }

    public int getScanner_counting1() {
        return Scanner_counting1;
    }

    public void setScanner_counting1(int scanner_counting1) {
        Scanner_counting1 = scanner_counting1;
    }

    public int getScanner_counting2() {
        return Scanner_counting2;
    }

    public void setScanner_counting2(int scanner_counting2) {
        Scanner_counting2 = scanner_counting2;
    }

    public int getScanner_inventory() {
        return Scanner_inventory;
    }

    public void setScanner_inventory(int scanner_inventory) {
        Scanner_inventory = scanner_inventory;
    }

    public String getCounting_processid() {
        return counting_processid;
    }

    public void setCounting_processid(String counting_processid) {
        this.counting_processid = counting_processid;
    }

    public String getCounting_startbin() {
        return counting_startbin;
    }

    public void setCounting_startbin(String counting_startbin) {
        this.counting_startbin = counting_startbin;
    }

    public String getCounting_endbin() {
        return counting_endbin;
    }

    public void setCounting_endbin(String counting_endbin) {
        this.counting_endbin = counting_endbin;
    }

    public String getCount_value() {
        return count_value;
    }

    public void setCount_value(String count_value) {
        this.count_value = count_value;
    }

    public String getReplace_part() {
        return replace_part;
    }

    public void setReplace_part(String replace_part) {
        this.replace_part = replace_part;
    }

    public String getReplace_mfr() {
        return replace_mfr;
    }

    public void setReplace_mfr(String replace_mfr) {
        this.replace_mfr = replace_mfr;
    }

    public String getReplace_qty() {
        return replace_qty;
    }

    public void setReplace_qty(String replace_qty) {
        this.replace_qty = replace_qty;
    }

    public String getReplace_binlocation() {
        return replace_binlocation;
    }

    public void setReplace_binlocation(String replace_binlocation) {
        this.replace_binlocation = replace_binlocation;
    }

    public String getReplace_oldpart() {
        return replace_oldpart;
    }

    public void setReplace_oldpart(String replace_oldpart) {
        this.replace_oldpart = replace_oldpart;
    }

    public String getReplace_order() {
        return replace_order;
    }

    public void setReplace_order(String replace_order) {
        this.replace_order = replace_order;
    }

    public String getReplace_value() {
        return replace_value;
    }

    public void setReplace_value(String replace_value) {
        this.replace_value = replace_value;
    }

    public String getPo_receipt() {
        return po_receipt;
    }

    public void setPo_receipt(String po_receipt) {
        this.po_receipt = po_receipt;
    }

    public String getPo_value() {
        return po_value;
    }

    public void setPo_value(String po_value) {
        this.po_value = po_value;
    }

    public String getPo_recive() {
        return po_recive;
    }

    public void setPo_recive(String po_recive) {
        this.po_recive = po_recive;
    }

    public String getPo_recive_value() {
        return po_recive_value;
    }

    public void setPo_recive_value(String po_recive_value) {
        this.po_recive_value = po_recive_value;
    }

    public String getReplace_transfer() {
        return replace_transfer;
    }

    public void setReplace_transfer(String replace_transfer) {
        this.replace_transfer = replace_transfer;
    }

    public String getPO_Array() {
        return PO_Array;
    }

    public void setPO_Array(String PO_Array) {
        this.PO_Array = PO_Array;
    }


    public byte[] getHw_imageData() {
        return hw_imageData;
    }

    public void setHw_imageData(byte[] hw_imageData) {
        this.hw_imageData = hw_imageData;
    }

    public ArrayList<byte[]> getHw_generalimages() {
        return hw_generalimages;
    }

    public void setHw_generalimages(ArrayList<byte[]> hw_generalimages) {
        this.hw_generalimages = hw_generalimages;
    }

    public ArrayList<byte[]> getHw_attachedFilesData() {
        return hw_attachedFilesData;
    }

    public void setHw_attachedFilesData(ArrayList<byte[]> hw_attachedFilesData) {
        this.hw_attachedFilesData = hw_attachedFilesData;
    }

    public int getScanner_hwstartbin() {
        return Scanner_hwstartbin;
    }

    public void setScanner_hwstartbin(int scanner_hwstartbin) {
        Scanner_hwstartbin = scanner_hwstartbin;
    }

    public int getScanner_hwendbin() {
        return Scanner_hwendbin;
    }

    public void setScanner_hwendbin(int scanner_hwendbin) {
        Scanner_hwendbin = scanner_hwendbin;
    }

    public int getScanner_partnumber() {
        return Scanner_partnumber;
    }

    public void setScanner_partnumber(int scanner_partnumber) {
        Scanner_partnumber = scanner_partnumber;
    }

    public String getHw_endbin() {
        return hw_endbin;
    }

    public void setHw_endbin(String hw_endbin) {
        this.hw_endbin = hw_endbin;
    }

    public String getHw_startbin() {
        return hw_startbin;
    }

    public void setHw_startbin(String hw_startbin) {
        this.hw_startbin = hw_startbin;
    }

    public String getHw_value() {
        return hw_value;
    }

    public void setHw_value(String hw_value) {
        this.hw_value = hw_value;
    }

    public String getPart_value() {
        return part_value;
    }

    public void setPart_value(String part_value) {
        this.part_value = part_value;
    }

    public String getProcessid_match() {
        return processid_match;
    }

    public void setProcessid_match(String processid_match) {
        this.processid_match = processid_match;
    }

    public String getPart_match() {
        return part_match;
    }

    public void setPart_match(String part_match) {
        this.part_match = part_match;
    }

    public ArrayList<GetBinLocation> getBinlocation() {
        return binlocation;
    }

    public void setBinlocation(ArrayList<GetBinLocation> binlocation) {
        this.binlocation = binlocation;
    }

    public ArrayList<GetManufacturers> getGetManufacturerses() {
        return getManufacturerses;
    }

    public void setGetManufacturerses(ArrayList<GetManufacturers> getManufacturerses) {
        this.getManufacturerses = getManufacturerses;
    }

    public String getBranch_mfg() {
        return branch_mfg;
    }

    public void setBranch_mfg(String branch_mfg) {
        this.branch_mfg = branch_mfg;
    }

    public String getHw_partnumber() {
        return hw_partnumber;
    }

    public void setHw_partnumber(String hw_partnumber) {
        this.hw_partnumber = hw_partnumber;
    }

    public String getHw_mfr() {
        return hw_mfr;
    }

    public void setHw_mfr(String hw_mfr) {
        this.hw_mfr = hw_mfr;
    }

    public String getHw_notes() {
        return hw_notes;
    }

    public void setHw_notes(String hw_notes) {
        this.hw_notes = hw_notes;
    }

    public String getHw_processid() {
        return hw_processid;
    }

    public void setHw_processid(String hw_processid) {
        this.hw_processid = hw_processid;
    }

    public String getHw_qty() {
        return hw_qty;
    }

    public void setHw_qty(String hw_qty) {
        this.hw_qty = hw_qty;
    }

    public String getHw_value_notmatch() {
        return hw_value_notmatch;
    }

    public void setHw_value_notmatch(String hw_value_notmatch) {
        this.hw_value_notmatch = hw_value_notmatch;
    }

    public int getLoad_processid() {
        return load_processid;
    }

    public void setLoad_processid(int load_processid) {
        this.load_processid = load_processid;
    }

    public String getLoad_branch() {
        return load_branch;
    }

    public void setLoad_branch(String load_branch) {
        this.load_branch = load_branch;
    }

    public String getLoad_startbin() {
        return load_startbin;
    }

    public void setLoad_startbin(String load_startbin) {
        this.load_startbin = load_startbin;
    }

    public String getLoad_endbin() {
        return load_endbin;
    }

    public void setLoad_endbin(String load_endbin) {
        this.load_endbin = load_endbin;
    }

    public int getLoad_check_enable() {
        return load_check_enable;
    }

    public void setLoad_check_enable(int load_check_enable) {
        this.load_check_enable = load_check_enable;
    }

    public Boolean getLoaded_partsnotcounted() {
        return loaded_partsnotcounted;
    }

    public void setLoaded_partsnotcounted(Boolean loaded_partsnotcounted) {
        this.loaded_partsnotcounted = loaded_partsnotcounted;
    }

    public String getLoad_value() {
        return load_value;
    }

    public void setLoad_value(String load_value) {
        this.load_value = load_value;
    }

    public String getCounting_partnum() {
        return counting_partnum;
    }

    public void setCounting_partnum(String counting_partnum) {
        this.counting_partnum = counting_partnum;
    }

    public String getLoad_value_onresume() {
        return load_value_onresume;
    }

    public void setLoad_value_onresume(String load_value_onresume) {
        this.load_value_onresume = load_value_onresume;
    }

    public String getInventory_scanner() {
        return inventory_scanner;
    }

    public void setInventory_scanner(String inventory_scanner) {
        this.inventory_scanner = inventory_scanner;
    }

    public int getInventory_dialoghandle() {
        return inventory_dialoghandle;
    }

    public void setInventory_dialoghandle(int inventory_dialoghandle) {
        this.inventory_dialoghandle = inventory_dialoghandle;
    }

    public int getEqup_scanner() {
        return equp_scanner;
    }

    public void setEqup_scanner(int equp_scanner) {
        this.equp_scanner = equp_scanner;
    }


    public String getCounting_branchname() {
        return counting_branchname;
    }

    public void setCounting_branchname(String counting_branchname) {
        this.counting_branchname = counting_branchname;
    }

    public String getEqu_branch_name() {
        return equ_branch_name;
    }

    public void setEqu_branch_name(String equ_branch_name) {
        this.equ_branch_name = equ_branch_name;
    }

    public int getScanner_partno() {
        return Scanner_partno;
    }

    public void setScanner_partno(int scanner_partno) {
        Scanner_partno = scanner_partno;
    }

    public String getCounting_partnew() {
        return counting_partnew;
    }

    public void setCounting_partnew(String counting_partnew) {
        this.counting_partnew = counting_partnew;
    }

    public String getLoad_partNum() {
        return load_partNum;
    }

    public void setLoad_partNum(String load_partNum) {
        this.load_partNum = load_partNum;
    }

    public String getCounting_branchno() {
        return counting_branchno;
    }

    public void setCounting_branchno(String counting_branchno) {
        this.counting_branchno = counting_branchno;
    }

    public String getChk_preliminary() {
        return chk_preliminary;
    }

    public void setChk_preliminary(String chk_preliminary) {
        this.chk_preliminary = chk_preliminary;
    }

    public int getFlagphy_addpart() {
        return flagphy_addpart;
    }

    public void setFlagphy_addpart(int flagphy_addpart) {
        this.flagphy_addpart = flagphy_addpart;
    }

    public ArrayList<GetVarianceList> getVarianceLists() {
        return varianceLists;
    }

    public void setVarianceLists(ArrayList<GetVarianceList> varianceLists) {
        this.varianceLists = varianceLists;
    }

    public int getLoad_checkvar_enable() {
        return load_checkvar_enable;
    }

    public void setLoad_checkvar_enable(int load_checkvar_enable) {
        this.load_checkvar_enable = load_checkvar_enable;
    }

    public ArrayList<GetPartsDetails> getGetEPOParts() {
        return GetEPOParts;
    }

    public void setGetEPOParts(ArrayList<GetPartsDetails> getEPOParts) {
        GetEPOParts = getEPOParts;
    }

    public ArrayList<GetPartsDetails> getPartsDetails_list() {
        return partsDetails_list;
    }

    public void setPartsDetails_list(ArrayList<GetPartsDetails> partsDetails_list) {
        this.partsDetails_list = partsDetails_list;
    }

    public String getPart_number() {
        return Part_number;
    }

    public void setPart_number(String part_number) {
        Part_number = part_number;
    }







    public boolean getOrdertype() {
        return  EPO_OrderTye;
    }
    public void setEPO_OrderTye(Boolean EPO_OrderTye) {
        this.EPO_OrderTye = EPO_OrderTye;
    }

    public String getPermission() {
        return Permission;
    }

    public void setPermission(String permission) {
        Permission = permission;
    }
}
