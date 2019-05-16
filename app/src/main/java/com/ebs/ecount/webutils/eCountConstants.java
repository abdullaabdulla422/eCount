package com.ebs.ecount.webutils;

/**
 * Created by cbe-teclwsp-008 on 7/12/16.
 */


public class eCountConstants {

    //public static String SOAP_BASE_ADDRESS = "http://portal.ebs-next.com/ecount";
    //public static String SOAP_BASE_ADDRESS = "https://portal.ebs-next.com/ecount";
    public static String SOAP_BASE_ADDRESS = "https://portal.ebs-next.com/ecounttest";
    //public static String SOAP_BASE_ADDRESS = "http://192.168.1.95/ecount";
    public static String SOAP_ADDRESS = SOAP_BASE_ADDRESS
                + "/servicelayer/ecountservicelayer.asmx";

    public static String NAME_SPACE = "http://portal.ebs-next.com/eCount/";

    public static String METHOD_AUTHENTICATE_USER = "AuthenticateUser";
    public static String SOAP_ACTION_AUTHENTICATE_USER = NAME_SPACE
            + METHOD_AUTHENTICATE_USER;

    public static String METHOD_GET_DEALER_BRANCH = "GetDealerBranch";
    public static String SOAP_ACTION_GET_DEALER_BRANCH = NAME_SPACE
            + METHOD_GET_DEALER_BRANCH;

    public static String METHOD_REMOVE_ACTIVEUSER="RemoveActiveUser";
    public static String SOAP_ACTION_REMOVE_ACTIVEUSER = NAME_SPACE
            + METHOD_REMOVE_ACTIVEUSER;

    public static String METHOD_GET_PROCESSID = "GetProcessid";
    public static String SOAP_ACTION_GET_PROCESSID = NAME_SPACE + METHOD_GET_PROCESSID;

    public static String METHOD_GET_PROCESSIDV2 = "GetProcessidV2";
    public static String SOAP_ACTION_GET_PROCESSIDV2 = NAME_SPACE + METHOD_GET_PROCESSIDV2;

    public static String METHOD_GET_PROCESSIDV3 = "GetProcessidV3";
    public static String SOAP_ACTION_GET_PROCESSIDV3 = NAME_SPACE + METHOD_GET_PROCESSIDV3;

    public static String METHOD_GET_PROCESSIDV4 = "GetProcessidV4";
    public static String SOAP_ACTION_GET_PROCESSIDV4 = NAME_SPACE + METHOD_GET_PROCESSIDV4;

    public static String METHOD_GET_BINLOCATION="GetBinLocation";
    public static String SOAP_ACTION_GET_BINLOCATION = NAME_SPACE
            + METHOD_GET_BINLOCATION;

    public static String METHOD_GET_PARTSQUANTITY= "GetPartsQuantity";
    public static String SOAP_ACTION_GET_PARTSQUANTITY = NAME_SPACE + METHOD_GET_PARTSQUANTITY;

    public static String METHOD_GET_PARTSQUANTITYV3= "GetPartsQuantityV3";
    public static String SOAP_ACTION_GET_PARTSQUANTITYV3 = NAME_SPACE + METHOD_GET_PARTSQUANTITYV3;

    public static String METHOD_GET_PARTSQUANTITYV4= "GetPartsQuantityV4";
    public static String SOAP_ACTION_GET_PARTSQUANTITYV4 = NAME_SPACE + METHOD_GET_PARTSQUANTITYV4;

    public static String METHOD_GET_PARTSQUANTITYV2= "GetPartsQuantityV2";
    public static String SOAP_ACTION_GET_PARTSQUANTITYV2 = NAME_SPACE + METHOD_GET_PARTSQUANTITYV2;

    public static String METHOD_SET_PARTSQUANTITY= "SetPartsQuantity";
    public static String SOAP_ACTION_SET_PARTSQUANTITY = NAME_SPACE + METHOD_SET_PARTSQUANTITY;

    public static String METHOD_SET_PARTSQUANTITYV2 = "SetPartsQuantityV2";
    public static String SOAP_ACTION_SET_PARTSQUANTITYV2 = NAME_SPACE + METHOD_SET_PARTSQUANTITYV2;


    public static String METHOD_SET_PARTSQUANTITYV4= "SetPartsQuantityV4";
    public static String SOAP_ACTION_SET_PARTSQUANTITYV4 = NAME_SPACE + METHOD_SET_PARTSQUANTITYV4;

    public static String METHOD_SET_PARTSQUANTITYV3= "SetPartsQuantityV3";
    public static String SOAP_ACTION_SET_PARTSQUANTITYV3 = NAME_SPACE + METHOD_SET_PARTSQUANTITYV3;

//    public static String METHOD_GET_EPONUMBER = "GetEPONumber";
    public static String METHOD_GET_EPONUMBER = "GetOrderTypeForPO";
    public static String SOAP_ACTION_GET_EPONUMBER = NAME_SPACE + METHOD_GET_EPONUMBER;

    public static String METHOD_GET_EPOPARTS = "GetEPOParts";
    public static String SOAP_ACTION_GET_EPOPARTS = NAME_SPACE + METHOD_GET_EPOPARTS;

    public static String METHOD_GET_PARTSDETAILS = "GetPartsDetails";
    public static String SOAP_ACTION_GET_PARTSDETAILS = NAME_SPACE + METHOD_GET_PARTSDETAILS;

    public static String METHOD_GET_PARTSDETAILSV2 = "GetPartsDetails_V2";
    public static String SOAP_ACTION_GET_PARTSDETAILSV2 = NAME_SPACE + METHOD_GET_PARTSDETAILSV2;

    public static String METHOD_GET_PARTSDETAILSV3 = "GetPartsDetails_V3";
    public static String SOAP_ACTION_GET_PARTSDETAILSV3 = NAME_SPACE + METHOD_GET_PARTSDETAILSV3;

//    public static String METHOD_GET_PARTSDETAILSV4 = "GetPartsDetails_V4";
    public static String METHOD_GET_PARTSDETAILSV4 = "GetPartsDetails_V5";
    public static String SOAP_ACTION_GET_PARTSDETAILSV4 = NAME_SPACE + METHOD_GET_PARTSDETAILSV4;

    public static String METHOD_GET_FREIGHTDETAILS = "GetFreightDetails";
    public static String SOAP_ACTION_GET_FREIGHTDETAILS = NAME_SPACE + METHOD_GET_FREIGHTDETAILS;

    public static String METHOD_SET_FREIGHTDETAILS = "SetFreightDetails";
    public static String SOAP_ACTION_SET_FREIGHTDETAILS = NAME_SPACE + METHOD_SET_FREIGHTDETAILS;

    public static String METHOD_SET_REPLACE_PARTDETAILS = "SetRepalcePartDetails";
    public static String SOAP_ACTION_SET_REPLACE_PARTDETAILS = NAME_SPACE + METHOD_SET_REPLACE_PARTDETAILS;

    public static String METHOD_SET_REPLACE_PARTDETAILSV2 = "SetRepalcePartDetailV2";
    public static String SOAP_ACTION_SET_REPLACE_PARTDETAILSV2 = NAME_SPACE + METHOD_SET_REPLACE_PARTDETAILSV2;

    public static String METHOD_SET_REPLACE_PARTDETAILSV3 = "SetRepalcePartDetailV3";
    public static String SOAP_ACTION_SET_REPLACE_PARTDETAILSV3 = NAME_SPACE + METHOD_SET_REPLACE_PARTDETAILSV3;

//    public static String METHOD_SET_REPLACE_PARTDETAILSV4 = "SetRepalcePartDetailV4";
    public static String METHOD_SET_REPLACE_PARTDETAILSV4 = "SetRepalcePartDetailV5";
    public static String SOAP_ACTION_SET_REPLACE_PARTDETAILSV4 = NAME_SPACE + METHOD_SET_REPLACE_PARTDETAILSV4;

    public static String METHOD_GET_MULTIPLE_PO_NUMBER = "GetMultiplePONumber";
    public static String SOAP_ACTION_GET_MULTIPLE_PO_NUMBER = NAME_SPACE + METHOD_GET_MULTIPLE_PO_NUMBER;

    public static String METHOD_SET_PARTSDETAILS= "SetPartDetails";
    public static String SOAP_ACTION_SET_PARTSDETAILS = NAME_SPACE + METHOD_SET_PARTSDETAILS;

    public static String METHOD_SET_PARTSDETAILSV2= "SetPartDetails_V2";
    public static String SOAP_ACTION_SET_PARTSDETAILSV2 = NAME_SPACE + METHOD_SET_PARTSDETAILSV2;

    public static String METHOD_SET_PARTSDETAILSV3= "SetPartDetails_V3";
    public static String SOAP_ACTION_SET_PARTSDETAILSV3 = NAME_SPACE + METHOD_SET_PARTSDETAILSV3;

//    public static String METHOD_SET_PARTSDETAILSV4= "SetPartDetails_V4";
    public static String METHOD_SET_PARTSDETAILSV4= "SetPartDetailsV5";
    public static String SOAP_ACTION_SET_PARTSDETAILSV4 = NAME_SPACE + METHOD_SET_PARTSDETAILSV4;

    public static String METHOD_GET_EQUIPMENT_PROCESSID="GetEquipmentProcessid";
    public static String SOAP_ACTION_GET_EQUIPMENT_PROCESSID=NAME_SPACE + METHOD_GET_EQUIPMENT_PROCESSID;

    public static String METHOD_GET_EQUIPMENT_PROCESSIDV1="GetEquipmentProcessidV3";//GetEquipmentProcessidV2
    public static String SOAP_ACTION_GET_EQUIPMENT_PROCESSIDV1=NAME_SPACE + METHOD_GET_EQUIPMENT_PROCESSIDV1;

    public static String METHOD_GET_EQUIPMENTLIST = "GetEquipmentList";
    public static String SOAP_ACTION_GET_EQUIPMENTLIST = NAME_SPACE + METHOD_GET_EQUIPMENTLIST;

    public static String METHOD_GET_EQUIPMENTLISTV2 = "GetEquipmentList_V3";
    public static String SOAP_ACTION_GET_EQUIPMENTLISTV2 = NAME_SPACE + METHOD_GET_EQUIPMENTLISTV2;

//    public static String METHOD_GET_COUNTEDEQUIPMENTLIST = "GetEquipmentCountedListV2";
    public static String METHOD_GET_COUNTEDEQUIPMENTLIST = "GetEquipmentCountedListV3";
    public static String SOAP_ACTION_GET_COUNTEDEQUIPMENTLIST = NAME_SPACE + METHOD_GET_COUNTEDEQUIPMENTLIST;

    public static String METHOD_UNCOUNTEDEQUIPMENT = "UnCountEquipmentV2";
    public static String SOAP_ACTION_UNCOUNTEDEQUIPMENT = NAME_SPACE + METHOD_UNCOUNTEDEQUIPMENT;

    public static String METHOD_GET_EQUIPMENTLISTV3 = "GetEquipmentList_V6";
    public static String SOAP_ACTION_GET_EQUIPMENTLISTV3 = NAME_SPACE + METHOD_GET_EQUIPMENTLISTV3;

    public static String METHOD_GET_ATTACHMENTS = "GetAttachments";
    public static String SOAP_ACTION_GET_ATTACHMENTS = NAME_SPACE + METHOD_GET_ATTACHMENTS;

    public static String METHOD_UPDATE_EQUIPMENT = "UpdateEquipment";
    public static String SOAP_ACTION_UPDATE_EQUIPMENT = NAME_SPACE + METHOD_UPDATE_EQUIPMENT;

    public static String METHOD_UPDATE_EQUIPMENTV3 = "UpdateEquipmentV5";
    public static String SOAP_ACTION_UPDATE_EQUIPMENTV3 = NAME_SPACE + METHOD_UPDATE_EQUIPMENTV3;

    public static String METHOD_UPDATE_EQUIPMENTV2 = "UpdateEquipmentV2";
    public static String SOAP_ACTION_UPDATE_EQUIPMENTV2 = NAME_SPACE + METHOD_UPDATE_EQUIPMENTV2;

    public static String METHOD_DETACH_EQUIPMENT = "DetachEquipment";
    public static String SOAP_ACTION_DETACH_EQUIPMENT = NAME_SPACE + METHOD_DETACH_EQUIPMENT;

    public static String METHOD_GET_EQUP_BRANCH = "GetEquipmentBranch";
    public static String SOAP_ACTION_GET_EQUP_BRANCH = NAME_SPACE + METHOD_GET_EQUP_BRANCH;

    public static String METHOD_ADD_EQUIPMENT = "AddEquipment";
    public static String SOAP_ACTION_ADD_EQUIPMENT = NAME_SPACE+ METHOD_ADD_EQUIPMENT;

    public static String METHOD_SET_EQUIPMENT_IMAGE = "SetEquipmentImages";
    public static String SOAP_ACTION_SET_EQUIPMENT_IMAGE = NAME_SPACE + METHOD_SET_EQUIPMENT_IMAGE;

    public static String METHOD_SET_EQUIPMENT_IMAGEV2 = "SetEquipmentImagesV2";
    public static String SOAP_ACTION_SET_EQUIPMENT_IMAGEV2 = NAME_SPACE + METHOD_SET_EQUIPMENT_IMAGEV2;

    public static String METHOD_SET_EQUP_DETAILS = "SetEquipmentDetails";
    public static String SOAP_ACTION_SET_EQUP_DETAILS = NAME_SPACE + METHOD_SET_EQUP_DETAILS;

    public static String METHOD_SET_EQUP_DETAILSV3 = "SetEquipmentDetailsV3";
    public static String SOAP_ACTION_SET_EQUP_DETAILSV3 = NAME_SPACE + METHOD_SET_EQUP_DETAILSV3;

    public static String METHOD_SET_EQUP_DETAILSV2 = "SetEquipmentDetailsV2";
    public static String SOAP_ACTION_SET_EQUP_DETAILSV2 = NAME_SPACE + METHOD_SET_EQUP_DETAILSV2;

    public static String METHOD_GET_LANDINGCOST_DETAILS = "GetLandingcostDetails";
    public static String SOAP_ACTION_GET_LANDINGCOST_DETAILS = NAME_SPACE + METHOD_GET_LANDINGCOST_DETAILS;

    public static String METHOD_VALIDATE_PARTS = "ValidateParts";
    public static String SOAP_ACTION_VALIDATE_PARTS = NAME_SPACE + METHOD_VALIDATE_PARTS;

    public static String METHOD_SET_LANDINGCOST = "SetLandingCost";
    public static String SOAP_ACTION_SET_LANDINGCOST = NAME_SPACE + METHOD_SET_LANDINGCOST;

    public static String METHOD_SET_LANDINGCOST_DETAILS = "SetLandingCostDetails";
    public static String SOAP_ACTION_SET_LANDINGCOST_DETAILS = NAME_SPACE + METHOD_SET_LANDINGCOST_DETAILS;

    public static String METHOD_SET_PARTS_HEADER = "SetPartsHeader";
    public static String SOAP_ACTION_SET_PARTS_HEADER = NAME_SPACE + METHOD_SET_PARTS_HEADER;

    public static String METHOD_VALIDATE_ORDERS = "ValidateOrders";
    public static String SOAP_ACTION_VALIDATE_ORDERS = NAME_SPACE + METHOD_VALIDATE_ORDERS;

    public static String METHOD_GETMANUFACTURERS = "GetManufacturers";
    public static String SOAP_ACTION_METHOD_GETMANUFACTURERA = NAME_SPACE + METHOD_GETMANUFACTURERS;

    public static String METHOD_ADDNEWPART = "AddNewPart";
    public static String SOAP_ACTION_METHOD_ADDNEWPART = NAME_SPACE + METHOD_ADDNEWPART;

    public static String METHOD_ADDNEWPARTV1 = "AddNewPart_V1";
    public static String SOAP_ACTION_METHOD_ADDNEWPARTV1 = NAME_SPACE + METHOD_ADDNEWPARTV1;

    public static String METHOD_VARIANCE = "GetVarianceList";
    public static String SOAP_ACTION_METHOD_VARIANCE = NAME_SPACE + METHOD_VARIANCE;

    public static String METHOD_GET_BRANCH_V2 = "GetBranchV3";
    public static String SOAP_ACTION_GET_BRANCH_V2 = NAME_SPACE
            + METHOD_GET_BRANCH_V2;

    public static String METHOD_CHECK_PARTS_AVAILABILITY = "CheckPartsAvailability";
    public static String SOAP_ACTION_CHECK_PARTS_AVAILABILITY = NAME_SPACE
            + METHOD_CHECK_PARTS_AVAILABILITY;

    public static String METHOD_INSERT_PM_PARTS = "InsertPMPart";
    public static String SOAP_ACTION_INSERT_PM_PARTS = NAME_SPACE
            + METHOD_INSERT_PM_PARTS;

    public static String METHOD_GET_PM_PARTS = "GetPmPartsDetails";
    public static String SOAP_ACTION_GET_PM_PARTS = NAME_SPACE
            + METHOD_GET_PM_PARTS;

    public static String METHOD_GET_PRINT_LABEL_PERMISSION = "GetPrintLabelPermission";
    public static String SOAP_ACTION_GET_PRINT_LABEL_PERMISSION = NAME_SPACE
            + METHOD_GET_PRINT_LABEL_PERMISSION;


    public static String METHOD_GET_PRINT_LABELS_PART = "PrintLabelsForPart";
    public static String SOAP_ACTION_GET_PRINT_LABELS_PART = NAME_SPACE
            + METHOD_GET_PRINT_LABELS_PART;


    public static String METHOD_GET_VALIDATE_HANDWRITE_EQUIPNUMBER = "ValidateHandwriteEquipnumber";
    public static String SOAP_ACTION_GET_VALIDATE_HANDWRITE_EQUIPNUMBER = NAME_SPACE
            + METHOD_GET_VALIDATE_HANDWRITE_EQUIPNUMBER;


    public static String METHOD_GET_VALIDATE_PARTS_V1 = "ValidateParts_v1";
    public static String SOAP_ACTION_GET_VALIDATE_PARTS_V1 = NAME_SPACE
            + METHOD_GET_VALIDATE_PARTS_V1;




    public static void resetSOAPAddress() {
        SOAP_ADDRESS = SOAP_BASE_ADDRESS
                + "/ServiceLayer/eCountServiceLayer.asmx";
    }
}
