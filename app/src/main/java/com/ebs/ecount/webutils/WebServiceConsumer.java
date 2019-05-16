package com.ebs.ecount.webutils;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

import com.ebs.ecount.objects.GetAttachments;
import com.ebs.ecount.objects.GetBinLocation;
import com.ebs.ecount.objects.GetDealerBranch;
import com.ebs.ecount.objects.GetEPONumber;
import com.ebs.ecount.objects.GetEPOParts;
import com.ebs.ecount.objects.GetEquipmentBranch;
import com.ebs.ecount.objects.GetEquipmentList;
import com.ebs.ecount.objects.GetEquipmentProcessid;
import com.ebs.ecount.objects.GetFreightDetails;
import com.ebs.ecount.objects.GetLandingcostDetails;
import com.ebs.ecount.objects.GetManufacturers;
import com.ebs.ecount.objects.GetMultiplePONumber;
import com.ebs.ecount.objects.GetPartsDetails;
import com.ebs.ecount.objects.GetPartsQuantity;
import com.ebs.ecount.objects.GetPmPartDetails;
import com.ebs.ecount.objects.GetProcessId;
import com.ebs.ecount.objects.GetVarianceList;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.objects.ValidateOrders;
import com.ebs.ecount.objects.ValidateParts;
import com.ebs.ecount.utils.Sessiondata;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by cbe-teclwsp-008 on 7/12/16.
 */
public class WebServiceConsumer {

    private static WebServiceConsumer instance = null;
    ArrayList<GetFreightDetails> freight = new ArrayList<GetFreightDetails>();
    private SoapObject requestSoap;
    private SoapSerializationEnvelope envelope;

    public static WebServiceConsumer getInstance() {
        if (instance == null) {
            instance = new WebServiceConsumer();
        }
        return instance;
    }

    private SoapSerializationEnvelope getEnvelope(SoapObject soapObject) {
        SoapSerializationEnvelope envelope1 = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope1.dotNet = true;
        envelope1.setOutputSoapObject(soapObject);
        return envelope1;
    }

    private void callService(String method, SoapSerializationEnvelope envelope)
            throws Exception {
        try {
            int timeout = 120 * 1000;
            HttpTransportSE httpTransport = new HttpTransportSE(
                    eCountConstants.SOAP_ADDRESS, timeout);
            httpTransport.call(method, envelope);
            httpTransport.getServiceConnection().disconnect();
            httpTransport = null;
            System.gc();
        } catch (Exception e) {
            throw e;
        }
    }

    public LoginObject authenticateUser(String username, String password) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE,
                eCountConstants.METHOD_AUTHENTICATE_USER);

        requestSoap.addProperty("username", username);
        requestSoap.addProperty("password", password);

        envelope = getEnvelope(requestSoap);

        try {

            callService(eCountConstants.SOAP_ACTION_AUTHENTICATE_USER, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            Log.d("SoapRequestauntheticate"
                    , "" + envelope.getResponse().toString());

            int numProps = response.getPropertyCount();
            LoginObject user = null;

            if (numProps > 0) {
                user = LoginObject.parseLogin((SoapObject) response.getProperty(0));
            }
            return user;

        } catch (Exception e) {
            Log.d("SoapRequestauntheticate"
                    , "" + requestSoap.toString());
            throw e;
        }

    }

    public ArrayList<GetDealerBranch> Getdealerbranch(String usertoken) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE,
                eCountConstants.METHOD_GET_DEALER_BRANCH);

        requestSoap.addProperty("usrToken", usertoken);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_DEALER_BRANCH, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numProps = response.getPropertyCount();
            ArrayList<GetDealerBranch> branch = new ArrayList<GetDealerBranch>();

            for (int i = 0; i < numProps; i++) {

                GetDealerBranch user = GetDealerBranch.parseBranch((SoapObject) response.getProperty(i));
                {
                    branch.add(user);
                }
            }

            return branch;

        } catch (Exception e) {
            Log.d("SoapRequest_branch"
                    , "" + requestSoap.toString());
            throw e;
        }
    }

    public String removeActiveUser(String usertoken, int userid) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_REMOVE_ACTIVEUSER);

        requestSoap.addProperty("usertoken", usertoken);
        requestSoap.addProperty("userid", userid);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_REMOVE_ACTIVEUSER, envelope);

            Log.d("Response_Logout", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }



    public ArrayList<GetProcessId> GetprocessId(String usertoken, String process_id, int startindex, int endindex) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PROCESSID);
        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", process_id);
        requestSoap.addProperty("startindex", startindex);
        requestSoap.addProperty("endindex", endindex);


        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PROCESSID, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetProcessId> processid = new ArrayList<GetProcessId>();

            for (int i = 0; i < numprocess; i++) {

                GetProcessId user = GetProcessId.parseProcessid((SoapObject) response.getProperty(i));
                {
                    processid.add(user);
                }
            }
            return processid;

        } catch (Exception e) {

            Log.d("SoapRequestcountproc_id", "" + requestSoap.toString());
            throw e;

        }
    }

    @SuppressLint("LongLogTag")
    public ArrayList<GetProcessId> GetprocessIdV3(String usertoken, String process_id, int startindex, int endindex, String userBranch) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PROCESSIDV4);
        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", process_id);
        requestSoap.addProperty("startindex", startindex);
        requestSoap.addProperty("endindex", endindex);
        requestSoap.addProperty("userBranch", userBranch);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PROCESSIDV4, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetProcessId> processid = new ArrayList<GetProcessId>();

            for (int i = 0; i < numprocess; i++) {

                GetProcessId user = GetProcessId.parseProcessid((SoapObject) response.getProperty(i));
                {
                    processid.add(user);
                }
            }
            return processid;

        } catch (Exception e) {

            Log.d("SoapRequestcountproc_idV2", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetBinLocation> GetBinlocation(String usrToken, String partno, int processid, String mfg, String branch) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_BINLOCATION);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("partNo", partno);
        requestSoap.addProperty("processId", processid);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("branch", branch);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_BINLOCATION, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numprop = response.getPropertyCount();

            int numprocess = response.getPropertyCount();

            ArrayList<GetBinLocation> binlocation = new ArrayList<GetBinLocation>();

            for (int i = 0; i < numprocess; i++) {

                GetBinLocation user = GetBinLocation.parseBinlocation((SoapObject) response.getProperty(i));
                {
                    binlocation.add(user);
                }
            }
            return binlocation;

        } catch (Exception e) {
            throw e;
        }
    }


    public ArrayList<GetPartsQuantity> Getpartsqty(String usertoken, int processid, String startbin, String endbin) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PARTSQUANTITY);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("startbin", startbin);
        requestSoap.addProperty("endbin", endbin);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PARTSQUANTITY, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetPartsQuantity> processqty = new ArrayList<GetPartsQuantity>();

            for (int i = 0; i < numprocess; i++) {

                GetPartsQuantity user = GetPartsQuantity.parseQty((SoapObject) response.getProperty(i));
                {
                    processqty.add(user);
                }
            }

            return processqty;

        } catch (Exception e) {

            Log.d("SoapRequest_partsqty", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetPartsQuantity> GetpartsqtyV3(String usertoken, int processid, String startbin, String endbin, String userBranch, Boolean partsNotCounted, int startIndex, int endIndex) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PARTSQUANTITYV3);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("startbin", startbin);
        requestSoap.addProperty("endbin", endbin);
        requestSoap.addProperty("branch", userBranch);
        requestSoap.addProperty("partsNotCounted", partsNotCounted);
        requestSoap.addProperty("startIndex", startIndex);
        requestSoap.addProperty("endIndex", endIndex);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PARTSQUANTITYV3, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetPartsQuantity> processqty = new ArrayList<GetPartsQuantity>();

            for (int i = 0; i < numprocess; i++) {

                GetPartsQuantity user = GetPartsQuantity.parseQty((SoapObject) response.getProperty(i));
                {
                    processqty.add(user);
                }
            }
            return processqty;

        } catch (Exception e) {

            Log.d("SoapRequest_partsqtyV3", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetPartsQuantity> GetpartsqtyV4(String usertoken, int processid, String startbin, String endbin, String userBranch, Boolean partsNotCounted, int startIndex, int endIndex, String partNum) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PARTSQUANTITYV4);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("startbin", startbin);
        requestSoap.addProperty("endbin", endbin);
        requestSoap.addProperty("branch", userBranch);
        requestSoap.addProperty("startIndex", startIndex);
        requestSoap.addProperty("endIndex", endIndex);
        requestSoap.addProperty("partsNotCounted", partsNotCounted);
        requestSoap.addProperty("partNum", partNum);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PARTSQUANTITYV4, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            Log.d("SoapRequest_partsqtyV4", "" + requestSoap.toString());

            ArrayList<GetPartsQuantity> processqty = new ArrayList<GetPartsQuantity>();

            for (int i = 0; i < numprocess; i++) {

                GetPartsQuantity user = GetPartsQuantity.parseQty((SoapObject) response.getProperty(i));
                {
                    processqty.add(user);
                }
            }
            return processqty;

        } catch (Exception e) {

            Log.d("SoapRequest_partsqtyV4", "" + requestSoap.toString());
            throw e;

        }
    }

    public String SetPartsQuantity(String usertoken, int processid, String startbin, String endbin, String partno, String qty, String mfg) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_PARTSQUANTITY);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("startbin", startbin);
        requestSoap.addProperty("endbin", endbin);
        requestSoap.addProperty("partno", partno);
        requestSoap.addProperty("qty", qty);
        requestSoap.addProperty("mfg", mfg);

        Log.d("usertoken", "" + usertoken);
        Log.d("ProcessId", "" + processid);
        Log.d("startbin", "" + startbin);
        Log.d("endbin", "" + endbin);
        Log.d("partno", "" + partno);
        Log.d("qty", "" + qty);
        Log.d("mfg", "" + mfg);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_PARTSQUANTITY, envelope);

            Log.d("Response_setprocqty", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String SetPartsQuantityV2(String usertoken, int processid, String startbin, String endbin, String partno, String qty, String mfg) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_PARTSQUANTITYV2);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("startbin", startbin);
        requestSoap.addProperty("endbin", endbin);
        requestSoap.addProperty("partno", partno);
        requestSoap.addProperty("qty", qty);
        requestSoap.addProperty("mfg", mfg);

        Log.d("usertoken", "" + usertoken);
        Log.d("ProcessId", "" + processid);
        Log.d("startbin", "" + startbin);
        Log.d("endbin", "" + endbin);
        Log.d("partno", "" + partno);
        Log.d("qty", "" + qty);
        Log.d("mfg", "" + mfg);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_PARTSQUANTITYV2, envelope);

            Log.d("Response_setprocqtyV2", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String SetPartsQuantityV4(String usertoken, int processid, String startbin, String endbin, String partno, String qty, String mfg, Boolean uncount, String notes, int variance) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_PARTSQUANTITYV4);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("startbin", startbin);
        requestSoap.addProperty("endbin", endbin);
        requestSoap.addProperty("partno", partno);
        requestSoap.addProperty("qty", qty);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("uncount", uncount);
        requestSoap.addProperty("notes", notes);
        requestSoap.addProperty("variance", variance);

        Log.d("usertoken", "" + usertoken);
        Log.d("ProcessId", "" + processid);
        Log.d("startbin", "" + startbin);
        Log.d("endbin", "" + endbin);
        Log.d("partno", "" + partno);
        Log.d("qty", "" + qty);
        Log.d("mfg", "" + mfg);
        Log.d("uncount", "" + uncount);
        Log.d("notes", "" + notes);
        Log.d("variance", "" + variance);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_PARTSQUANTITYV4, envelope);

            Log.d("Response_setprocqtyV4", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public GetEPONumber geteponum(String usrToken, String branch, int ponum) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_EPONUMBER);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("ponum", ponum);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_EPONUMBER, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numProps = response.getPropertyCount();

            GetEPONumber eponum = null;

            if (numProps > 0) {
                eponum = GetEPONumber.parseEPONum((SoapObject) response.getProperty(0));
            }
            Log.d("Response_GetEPONumber", "" + response.toString());

            return eponum;
        } catch (Exception e) {
            throw e;
        }
    }

    public ArrayList<GetEPOParts> GetEPOParts(String usertoken, String branch, int ponum, int startindex, int endindex) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_EPOPARTS);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("ponum", ponum);
        requestSoap.addProperty("startindex", startindex);
        requestSoap.addProperty("endindex", endindex);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_EPOPARTS, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetEPOParts> array_GetEPOParts = new ArrayList<GetEPOParts>();

            for (int i = 0; i < numprocess; i++) {

                GetEPOParts parts = GetEPOParts.parseEPOParts((SoapObject) response.getProperty(i));
                {
                    array_GetEPOParts.add(parts);
                }

            }

            return array_GetEPOParts;

        } catch (Exception e) {

            Log.d("SoapRequest_EpoParts", "" + requestSoap.toString());
            throw e;

        }
    }

    @SuppressLint("LongLogTag")
    public GetPartsDetails getPartsdetls(String usrToken, String partno, int orderno) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PARTSDETAILSV2);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("partno", partno);
        requestSoap.addProperty("orderno", orderno);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PARTSDETAILSV2, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numProps = response.getPropertyCount();

            GetPartsDetails partsdetails = null;

            if (numProps > 0) {
                partsdetails = GetPartsDetails.parsepartsdtls((SoapObject) response.getProperty(0));
            }
            Log.d("Response_GetPartsDetails", "" + response.toString());

            return partsdetails;
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressLint("LongLogTag")
    public GetPartsDetails getPartsdetlsV3_obj(String usrToken, String orderno, String partno) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PARTSDETAILSV4);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("partno", partno);

        Log.d("Request_usertoken", " GetPartsDetails " + usrToken);
        Log.d("Request_partno", " GetPartsDetails " + partno);
        Log.d("Request_orderno", " GetPartsDetails " + orderno);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PARTSDETAILSV4, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numProps = response.getPropertyCount();

            GetPartsDetails partsdetails = null;

            if (numProps > 0) {
                partsdetails = GetPartsDetails.parsepartsdtls((SoapObject) response.getProperty(0));
            }
            Log.d("SoapRequest_parysdeV4obj", "" + response.toString());

            return partsdetails;
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressLint("LongLogTag")
    public ArrayList<GetPartsDetails> getPartsdetlsV3_list(String usertoken, String orderno, String partno) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PARTSDETAILSV4);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("partno", partno);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PARTSDETAILSV4, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetPartsDetails> array_GetEPOParts = new ArrayList<GetPartsDetails>();
            GetPartsDetails parts = null;

            for (int i = 0; i < numprocess; i++) {

                parts = GetPartsDetails.parsepartsdtls((SoapObject) response.getProperty(i));
                {
                    array_GetEPOParts.add(parts);
                }

            }

            return array_GetEPOParts;

        } catch (Exception e) {

            Log.d("SoapRequest_parysdeV4_list", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetPartsDetails> getPartsdetlsV3(String usertoken, String orderno, String partno) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PARTSDETAILSV3);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("partno", partno);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PARTSDETAILSV3, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetPartsDetails> array_GetEPOParts = new ArrayList<GetPartsDetails>();
            GetPartsDetails parts = null;

            for (int i = 0; i < numprocess; i++) {

                parts = GetPartsDetails.parsepartsdtls((SoapObject) response.getProperty(i));
                {
                    array_GetEPOParts.add(parts);
                }

            }


            if (Sessiondata.getInstance().getGetEPOParts() == null) {
                Sessiondata.getInstance().setGetEPOParts(array_GetEPOParts);
            } else {
                Sessiondata.getInstance().getGetEPOParts().addAll(array_GetEPOParts);
            }


            return array_GetEPOParts;

        } catch (Exception e) {

            Log.d("SoapRequest_parysdeV3", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetPartsDetails> getPartsdetlsV4(String usertoken, String orderno, String partno) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PARTSDETAILSV4);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("partno", partno);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PARTSDETAILSV4, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();


            ArrayList<GetPartsDetails> array_GetEPOParts = new ArrayList<GetPartsDetails>();
            GetPartsDetails parts = null;

            for (int i = 0; i < numprocess; i++) {

                parts = GetPartsDetails.parsepartsdtls((SoapObject) response.getProperty(i));
                {
                    array_GetEPOParts.add(parts);
                }

            }


//            if (Sessiondata.getInstance().getGetEPOParts() == null){
//                Sessiondata.getInstance().setGetEPOParts(array_GetEPOParts);
//            }else {
//                Sessiondata.getInstance().getGetEPOParts().addAll(array_GetEPOParts);
//            }


            if (array_GetEPOParts.size() == 1) {
                if (array_GetEPOParts.get(0).getOrderno() != 0) {

                    if (Sessiondata.getInstance().getGetEPOParts() == null) {
                        Sessiondata.getInstance().setGetEPOParts(array_GetEPOParts);
                    } else {
                        Sessiondata.getInstance().getGetEPOParts().addAll(array_GetEPOParts);
                    }

                }

            } else {

                if (Sessiondata.getInstance().getGetEPOParts() == null) {
                    Sessiondata.getInstance().setGetEPOParts(array_GetEPOParts);
                } else {
                    Sessiondata.getInstance().getGetEPOParts().addAll(array_GetEPOParts);
                }
            }


            return array_GetEPOParts;

        } catch (Exception e) {

            Log.d("SoapRequest_parysdeV4", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetPartsDetails> getPartsdetailV4_2(String usertoken, String orderno, String partno) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PARTSDETAILSV4);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("partno", partno);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PARTSDETAILSV4, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetPartsDetails> array_GetEPOParts = new ArrayList<GetPartsDetails>();
            GetPartsDetails parts = null;

            for (int i = 0; i < numprocess; i++) {

                parts = GetPartsDetails.parsepartsdtls((SoapObject) response.getProperty(i));
                {
                    array_GetEPOParts.add(parts);
                }

            }

//            if (Sessiondata.getInstance().getGetEPOParts() == null){
//                Sessiondata.getInstance().setGetEPOParts(array_GetEPOParts);
//            }else {
//                Sessiondata.getInstance().getGetEPOParts().addAll(array_GetEPOParts);
//            }

            return array_GetEPOParts;

        } catch (Exception e) {

            Log.d("SoapRequest_parysdeV4", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetFreightDetails> Getfreightdetails(String usertoken, int orderno, String part_no, int oeporder) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_FREIGHTDETAILS);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("partno", part_no);
        requestSoap.addProperty("oepordno", oeporder);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_FREIGHTDETAILS, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            for (int i = 0; i < numprocess; i++) {

                GetFreightDetails user = GetFreightDetails.parseFreight((SoapObject) response.getProperty(i));
                {
                    freight.add(user);
                }
            }

            return freight;

        } catch (Exception e) {

            Log.d("SoapRequest_freightdtl", "" + requestSoap.toString());
            throw e;

        }
    }


    public ArrayList<GetMultiplePONumber> GetmultipleNumber(String usertoken, String partno, String branch, String orderno) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_MULTIPLE_PO_NUMBER);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("partno", partno);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("orderno", orderno);

        Log.d("Request_usertoken", " MultiplePO " + usertoken);
        Log.d("Request_partno", " MultiplePO " + partno);
        Log.d("Request_branch", " MultiplePO " + branch);
        Log.d("Request_orderno", " MultiplePO " + orderno);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_MULTIPLE_PO_NUMBER, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetMultiplePONumber> processmultiplepo = new ArrayList<GetMultiplePONumber>();

            for (int i = 0; i < numprocess; i++) {

                GetMultiplePONumber user = GetMultiplePONumber.parseMultipleNo((SoapObject) response.getProperty(i));
                {
                    processmultiplepo.add(user);
                }
            }

            return processmultiplepo;

        } catch (Exception e) {

            Log.d("SoapRequest_multiplepo", "" + requestSoap.toString());
            throw e;

        }
    }

    public String SetFreightDetails(String usertoken, int orderno, String branch, String inbound, String outbound, int oeitemnum) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_FREIGHTDETAILS);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("inbound", String.valueOf(inbound));
        requestSoap.addProperty("outbound", String.valueOf(outbound));
        requestSoap.addProperty("oeitemnum", oeitemnum);

        Log.d("ordered_res", "" + orderno);
        Log.d("freigth_res", "" + inbound);
        Log.d("freight_res", "" + outbound);
        Log.d("oeitemnum", "" + oeitemnum);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_FREIGHTDETAILS, envelope);

            Log.d("Response_setfreightdet", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String SetRepalcePartDetails(String usertoken, int order_no, String mfr, String old_partno, String new_partno, int qty, String bin_loc, String transfer, String user, int oeitenum) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_REPLACE_PARTDETAILS);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", order_no);
        requestSoap.addProperty("mfr", mfr);
        requestSoap.addProperty("oldpartno", old_partno);
        requestSoap.addProperty("newpartno", new_partno);
        requestSoap.addProperty("qtyrec", qty);
        requestSoap.addProperty("binloc", bin_loc);
        requestSoap.addProperty("transferhist", transfer);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("oeitemnum", oeitenum);


        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_REPLACE_PARTDETAILS, envelope);

            Log.d("ReplacePartDetails", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String SetRepalcePartDetailsV2(String usertoken, int order_no, String mfr, String old_partno, String new_partno, int qty, String bin_loc, String transfer, String user, int oeitenum, int procqty) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_REPLACE_PARTDETAILSV2);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", order_no);
        requestSoap.addProperty("mfr", mfr);
        requestSoap.addProperty("oldpartno", old_partno);
        requestSoap.addProperty("newpartno", new_partno);
        requestSoap.addProperty("qtyrec", qty);
        requestSoap.addProperty("binloc", bin_loc);
        requestSoap.addProperty("transferhist", transfer);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("oeitemnum", oeitenum);
        requestSoap.addProperty("procqty", procqty);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_REPLACE_PARTDETAILSV2, envelope);

            Log.d("ReplacePartDetails", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String SetRepalcePartDetailsV3(String usertoken, int order_no, String mfr, String old_partno, String new_partno, int qty, String bin_loc, String transfer, String user, int oeitenum, int procqty, int oepordnum) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_REPLACE_PARTDETAILSV3);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", order_no);
        requestSoap.addProperty("mfr", mfr);
        requestSoap.addProperty("oldpartno", old_partno);
        requestSoap.addProperty("newpartno", new_partno);
        requestSoap.addProperty("qtyrec", qty);
        requestSoap.addProperty("binloc", bin_loc);
        requestSoap.addProperty("transferhist", transfer);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("oeitemnum", oeitenum);
        requestSoap.addProperty("procqty", procqty);
        requestSoap.addProperty("oepordnum", oepordnum);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_REPLACE_PARTDETAILSV3, envelope);

            Log.d("ReplacePartDetails", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }


    public String SetRepalcePartDetailsV4(String usertoken, int order_no, String mfr,
                                          String old_partno, String new_partno,
                                          int qty, String bin_loc, String transfer,
                                          String user, int oeitenum, int procqty, int oepordnum,String status,
                                          String vendor_refernce, Boolean print_lable) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_REPLACE_PARTDETAILSV4);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", order_no);
        requestSoap.addProperty("mfr", mfr);
        requestSoap.addProperty("oldpartno", old_partno);
        requestSoap.addProperty("newpartno", new_partno);
        requestSoap.addProperty("qtyrec", qty);
        requestSoap.addProperty("binloc", bin_loc);
        requestSoap.addProperty("transferhist", transfer);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("oeitemnum", oeitenum);
        requestSoap.addProperty("procqty", procqty);
        requestSoap.addProperty("oepordnum", oepordnum);
        requestSoap.addProperty("status", status);
        requestSoap.addProperty("vendorreference", vendor_refernce);
        requestSoap.addProperty("printlabel", print_lable);

        envelope = getEnvelope(requestSoap);

        Log.d("request_replacePart",requestSoap.toString());

        try {
            callService(eCountConstants.SOAP_ACTION_SET_REPLACE_PARTDETAILSV4, envelope);

            Log.d("ReplacePartDetails", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressLint("LongLogTag")
    public String SetPartDetails(String usertoken, int orderno, String branch, String mfg, String partno, int qtyrec, String status, String user, int oeitenum, double unitprice) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_PARTSDETAILSV2);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("partno", partno);
        requestSoap.addProperty("qtyrec", qtyrec);
        requestSoap.addProperty("status", status);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("oeitemnum", oeitenum);
        requestSoap.addProperty("unitprice", String.valueOf(unitprice));

        Log.d("usertoken", "" + usertoken);
        Log.d("orderno", "" + orderno);
        Log.d("branch", "" + branch);
        Log.d("mfg", "" + mfg);
        Log.d("partno", "" + partno);
        Log.d("qtyrec", "" + qtyrec);
        Log.d("status", "" + status);
        Log.d("oeitemnum", "" + oeitenum);
        Log.d("user", "" + user);
        Log.d("unitprice", "" + String.valueOf(unitprice));

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_PARTSDETAILSV2, envelope);

            Log.d("Response_setprocDETAILSV2", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressLint("LongLogTag")
    public String SetPartDetailsV3(String usertoken, int oepordnum, int orderno, String branch, String mfg, String partno, int qtyrec, String status, String user, int oeitenum, double unitprice) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_PARTSDETAILSV3);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("oepordnum", oepordnum);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("partno", partno);
        requestSoap.addProperty("qtyrec", qtyrec);
        requestSoap.addProperty("status", status);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("oeitemnum", oeitenum);
        requestSoap.addProperty("unitprice", String.valueOf(unitprice));

        Log.d("usertoken", "" + usertoken);
        Log.d("orderno", "" + orderno);
        Log.d("branch", "" + branch);
        Log.d("mfg", "" + mfg);
        Log.d("partno", "" + partno);
        Log.d("qtyrec", "" + qtyrec);
        Log.d("status", "" + status);
        Log.d("oeitemnum", "" + oeitenum);
        Log.d("user", "" + user);
        Log.d("unitprice", "" + String.valueOf(unitprice));

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_PARTSDETAILSV3, envelope);

            Log.d("Response_setprocDETAILSV3", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressLint("LongLogTag")
    public String SetPartDetailsV4(String usertoken, int oepordnum, int orderno, String branch, String mfg, String partno, int qtyrec, String status, String user, int oeitenum, double unitprice, String ordiclocmain, String ordiclocsec, String iclocmain, String iclocsec, String  vendor_reference, Boolean print_lable) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_PARTSDETAILSV4);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("oepordnum", oepordnum);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("partno", partno);
        requestSoap.addProperty("qtyrec", qtyrec);
        requestSoap.addProperty("status", status);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("oeitemnum", oeitenum);
        requestSoap.addProperty("unitprice", String.valueOf(unitprice));
        requestSoap.addProperty("ordiclocmain", ordiclocmain);
        requestSoap.addProperty("ordiclocsec", ordiclocsec);
        requestSoap.addProperty("iclocmain", iclocmain);
        requestSoap.addProperty("iclocsec", iclocsec);
        requestSoap.addProperty("vendorreference", vendor_reference);
        requestSoap.addProperty("printlabel", print_lable);

        Log.d("usertoken", "" + usertoken);
        Log.d("orderno", "" + orderno);
        Log.d("branch", "" + branch);
        Log.d("mfg", "" + mfg);
        Log.d("partno", "" + partno);
        Log.d("qtyrec", "" + qtyrec);
        Log.d("status", "" + status);
        Log.d("oeitemnum", "" + oeitenum);
        Log.d("user", "" + user);
        Log.d("ordiclocmain", "" + ordiclocmain);
        Log.d("ordiclocsec", "" + ordiclocsec);
        Log.d("iclocmain", "" + iclocmain);
        Log.d("iclocsec", "" + iclocsec);
        Log.d("unitprice", "" + String.valueOf(unitprice));

        envelope = getEnvelope(requestSoap);

        Log.d("Request_setpartDETAILSV4", "" + requestSoap.toString());

        try {
            callService(eCountConstants.SOAP_ACTION_SET_PARTSDETAILSV4, envelope);

            Log.d("Response_setprocDETAILSV4", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }


    public ArrayList<GetEquipmentProcessid> GetEquipmentProcessid(String usertoken) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_EQUIPMENT_PROCESSID);
        requestSoap.addProperty("usrToken", usertoken);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_EQUIPMENT_PROCESSID, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetEquipmentProcessid> equipmentProcessids = new ArrayList<GetEquipmentProcessid>();

            for (int i = 0; i < numprocess; i++) {

                GetEquipmentProcessid user = GetEquipmentProcessid.parseEquipmentProcessId((SoapObject) response.getProperty(i));
                {
                    equipmentProcessids.add(user);
                }
            }
            return equipmentProcessids;

        } catch (Exception e) {

            Log.d("SoapRequestcountproc_id", "" + requestSoap.toString());
            throw e;

        }

    }

    @SuppressLint("LongLogTag")
    public ArrayList<GetEquipmentProcessid> GetEquipmentProcessidV1(String usertoken, String userBranch) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_EQUIPMENT_PROCESSIDV1);
        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("userBranch", userBranch);
//        requestSoap.addProperty("Description", Description);

        Log.d("branch",""+userBranch);
        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_EQUIPMENT_PROCESSIDV1, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            Log.d("response",""+envelope.getResponse().toString());
            int numprocess = response.getPropertyCount();

            ArrayList<GetEquipmentProcessid> equipmentProcessids = new ArrayList<GetEquipmentProcessid>();

            for (int i = 0; i < numprocess; i++) {

                GetEquipmentProcessid user = GetEquipmentProcessid.parseEquipmentProcessId((SoapObject) response.getProperty(i));
                {
                    equipmentProcessids.add(user);
                }
            }
            return equipmentProcessids;

        } catch (Exception e) {

            Log.d("SoapRequestcountproc_idV1", "" + requestSoap.toString());
            throw e;

        }

    }

    public ArrayList<GetEquipmentList> GetEquipmentList(String usertoken, int processid, String equipid, String mfg, String model, String serialno) throws Exception {


        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_EQUIPMENTLIST);
        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("model", model);
        requestSoap.addProperty("serialno", serialno);


        envelope = getEnvelope(requestSoap);
        try {
            callService(eCountConstants.SOAP_ACTION_GET_EQUIPMENTLIST, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetEquipmentList> equipmentLists = new ArrayList<GetEquipmentList>();

            for (int i = 0; i < numprocess; i++) {

                GetEquipmentList user = GetEquipmentList.parseEquipmentList((SoapObject) response.getProperty(i));
                {
                    equipmentLists.add(user);
                }
            }
            return equipmentLists;

        } catch (Exception e) {

            Log.d("SoapRequestcountproc_id", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetEquipmentList> GetEquipmentListV2(String usertoken, int processid, String equipid, String mfg, String model, String serialno) throws Exception {


        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_EQUIPMENTLISTV2);
        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("model", model);
        requestSoap.addProperty("serialno", serialno);


        envelope = getEnvelope(requestSoap);
        try {
            callService(eCountConstants.SOAP_ACTION_GET_EQUIPMENTLISTV2, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetEquipmentList> equipmentLists = new ArrayList<GetEquipmentList>();

            for (int i = 0; i < numprocess; i++) {

                GetEquipmentList user = GetEquipmentList.parseEquipmentList((SoapObject) response.getProperty(i));
                {
                    equipmentLists.add(user);
                }
            }
            return equipmentLists;

        } catch (Exception e) {

            Log.d("SoapRequestcountprocV2", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetEquipmentList> GetEquipmentListV4(String usertoken, int processid, String equipid, String mfg, String model, String serialno) throws Exception {

        Sessiondata.getInstance().setCounted_Uncounted_status(false);
        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_EQUIPMENTLISTV3);
        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("model", model);
        requestSoap.addProperty("serialno", serialno);

        envelope = getEnvelope(requestSoap);
        try {
            callService(eCountConstants.SOAP_ACTION_GET_EQUIPMENTLISTV3, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            Log.d("response", "" + envelope.getResponse().toString());
            int numprocess = response.getPropertyCount();

            ArrayList<GetEquipmentList> equipmentLists = new ArrayList<GetEquipmentList>();

            for (int i = 0; i < numprocess; i++) {

                GetEquipmentList user = GetEquipmentList.parseEquipmentList((SoapObject) response.getProperty(i));
                {
                    equipmentLists.add(user);
                }
            }
            return equipmentLists;

        } catch (Exception e) {

            Log.d("SoapRequestcountprocV3", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetEquipmentList> GetEquipmentCountedList(String usertoken, int processid, String equipid, String mfg, String model, String serialno) throws Exception {


        Sessiondata.getInstance().setCounted_Uncounted_status(true);
        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_COUNTEDEQUIPMENTLIST);
        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("model", model);
        requestSoap.addProperty("serialno", serialno);

        envelope = getEnvelope(requestSoap);
        try {
            callService(eCountConstants.SOAP_ACTION_GET_COUNTEDEQUIPMENTLIST, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetEquipmentList> equipmentLists = new ArrayList<GetEquipmentList>();

            for (int i = 0; i < numprocess; i++) {

                GetEquipmentList user = GetEquipmentList.parseEquipmentList((SoapObject) response.getProperty(i));
                {
                    equipmentLists.add(user);
                }
            }
            return equipmentLists;

        } catch (Exception e) {

            Log.d("SoapRequestcountedequp", "" + requestSoap.toString());
            throw e;

        }
    }


    public ArrayList<GetAttachments> attachmentsdetails(String usrToken, String equipid) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_ATTACHMENTS);
        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("equipid", equipid);

        envelope = getEnvelope(requestSoap);
        try {
            callService(eCountConstants.SOAP_ACTION_GET_ATTACHMENTS, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numporp = response.getPropertyCount();

            ArrayList<GetAttachments> attachments = new ArrayList<GetAttachments>();

            for (int i = 0; i < numporp; i++) {

                GetAttachments attachment = GetAttachments.parseAttachment((SoapObject) response.getProperty(i));
                {
                    attachments.add(attachment);
                }
            }

            Log.d("Get Attachmnts", "Response-" + response.toString());

            return attachments;
        } catch (Exception e) {
            throw e;
        }
    }

    public String UpdateEquipment(String usertoken, String equipid, int processid, String user, String branch) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_UPDATE_EQUIPMENT);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("branch", branch);


        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_UPDATE_EQUIPMENT, envelope);

            Log.d("Response_update_equp", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String UpdateEquipmentV2(String usertoken, String equipid, int processid, String user, String branch) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_UPDATE_EQUIPMENTV2);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("branch", branch);


        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_UPDATE_EQUIPMENTV2, envelope);

            Log.d("Response_update_equpV2", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String UpdateEquipmentV3(String usertoken, String equipid, int processid, String user, String branch, int scannedtype) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_UPDATE_EQUIPMENTV3);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("branch", branch);

        String Transfer_Equipment = Sessiondata.getInstance().getTransfer_Equipment();

        if (Transfer_Equipment.equals("T"))
        {

            Sessiondata.getInstance().setTransfer_Equipment("");

            if (scannedtype == 1)
            {
                requestSoap.addProperty("scannedtype", 7);
            }
            else if (scannedtype == 2)
            {
                requestSoap.addProperty("scannedtype", 8);
            }
        }
        else
        {
            requestSoap.addProperty("scannedtype", scannedtype);
        }




        envelope = getEnvelope(requestSoap);

        Log.d("UPDATE EquipmentV3",""+branch);
        Log.d("REQUESTUPDATE",""+requestSoap.toString());

        try {
            callService(eCountConstants.SOAP_ACTION_UPDATE_EQUIPMENTV3, envelope);

            Log.d("Response_update_equpV3", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
//            return null;

        } catch (Exception e) {
            throw e;
        }
    }

    public String DetachEquipment(String usertoken, String equipid) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_DETACH_EQUIPMENT);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("equipid", equipid);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_DETACH_EQUIPMENT, envelope);

            Log.d("Response_detach_equp", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();

        } catch (Exception e) {
            throw e;
        }

    }


    public GetEquipmentBranch equipmentBranch(String usrToken, String equipid) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_EQUP_BRANCH);
        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("equipid", equipid);

        envelope = getEnvelope(requestSoap);
        try {
            callService(eCountConstants.SOAP_ACTION_GET_EQUP_BRANCH, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numporp = response.getPropertyCount();

            GetEquipmentBranch EquipmentBranch = null;

            if (numporp > 0) {
                EquipmentBranch = GetEquipmentBranch.parseEquipmentBranch((SoapObject) response.getProperty(0));
            }
            Log.d("Get_EquipmentBranch", "Response-" + response.toString());

            return EquipmentBranch;

        } catch (Exception e) {
            throw e;
        }
    }


    public String SetEquipmentImages(String usertoken, int detailId, String equipid, String imagepath, String content, Long offset) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_EQUIPMENT_IMAGE);
        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("detailid", detailId);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("imgpath", imagepath);
        requestSoap.addProperty("content", content);
        requestSoap.addProperty("offset", offset);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_EQUIPMENT_IMAGE, envelope);

            Log.d("Set_Equipment_Images", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }

    }

    public String SetEquipmentImagesV2(String usertoken, int processid, int detailId, String equipid, String imagepath, String content, Long offset) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_EQUIPMENT_IMAGEV2);
        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("detailid", detailId);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("imgpath", imagepath);
        requestSoap.addProperty("content", content);
        requestSoap.addProperty("offset", offset);

        envelope = getEnvelope(requestSoap);

        try {

            callService(eCountConstants.SOAP_ACTION_SET_EQUIPMENT_IMAGEV2, envelope);

            Log.d("Set_Equipment_ImagesV2", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }

    }


    @SuppressLint("LongLogTag")
    public String ValidateParts_v1(String usertoken, String partno, String mfg) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_VALIDATE_PARTS_V1);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("partno", partno);
        requestSoap.addProperty("mfg", mfg);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_VALIDATE_PARTS_V1, envelope);

            Log.d("Response_ValidateParts_v1", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }




    @SuppressLint("LongLogTag")
    public String ValidateHandwriteEquipnumber(String usertoken, String kequipnum, String kserialnum) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_VALIDATE_HANDWRITE_EQUIPNUMBER);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("kequipnum", kequipnum);
        requestSoap.addProperty("kserialnum", kserialnum);


        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_VALIDATE_HANDWRITE_EQUIPNUMBER, envelope);

            Log.d("Response_ValidateHandwriteEquipnumber", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();

        } catch (Exception e) {
            throw e;
        }

    }



    public String AddEquipment(String usertoken, int processid, String equipid, String mfr, String model, String serialno, String branch, String notes, String user) throws Exception {


        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_ADD_EQUIPMENT);
        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("mfg", mfr);
        requestSoap.addProperty("model", model);
        requestSoap.addProperty("serialno", serialno);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("notes", notes);
        requestSoap.addProperty("user", user);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_ADD_EQUIPMENT, envelope);

            Log.d("Response_Add_Equipment", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }

    }

    public String SetEquipmentDetails(String usertoken, String equipid, int processid, String user, String branch) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_EQUP_DETAILS);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("branch", branch);


        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_EQUP_DETAILS, envelope);

            Log.d("Response_setequpdetails", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();

        } catch (Exception e) {
            throw e;
        }

    }

    public String SetEquipmentDetailsV2(String usertoken, String equipid, int processid, String user, String branch) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_EQUP_DETAILSV2);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("branch", branch);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_EQUP_DETAILSV2, envelope);

            Log.d("Response_setequpdetails", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();

        } catch (Exception e) {
            throw e;
        }

    }

    @SuppressLint("LongLogTag")
    public String SetEquipmentDetailsV3(String usertoken, String equipid, int processid, String user, String branch, int scannedtype) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_EQUP_DETAILSV3);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("branch", branch);

        String Transfer_Equipment = Sessiondata.getInstance().getTransfer_Equipment();

        if (Transfer_Equipment.equals("T"))
        {

            Sessiondata.getInstance().setTransfer_Equipment("");

            if (scannedtype == 1)
            {
                requestSoap.addProperty("scannedtype", 7);
            }
            else if (scannedtype == 2)
            {
                requestSoap.addProperty("scannedtype", 8);
            }
        }
        else
        {
            requestSoap.addProperty("scannedtype", scannedtype);

        }

        Log.d("branch",""+branch);
        envelope = getEnvelope(requestSoap);
        Log.d("REQUESTUPDATE",""+requestSoap.toString());

        try {
            callService(eCountConstants.SOAP_ACTION_SET_EQUP_DETAILSV3, envelope);

            Log.d("Response_setequpdetailsV3", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
//            return null;

        } catch (Exception e) {
            throw e;
        }

    }

    public ArrayList<GetLandingcostDetails> getLandingcostDetails(String usertoken, String branch, int orderno) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE,
                eCountConstants.METHOD_GET_LANDINGCOST_DETAILS);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("orderno", orderno);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_LANDINGCOST_DETAILS, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numProps = response.getPropertyCount();

            ArrayList<GetLandingcostDetails> landcostdetails = new ArrayList<GetLandingcostDetails>();

            for (int i = 0; i < numProps; i++) {

                GetLandingcostDetails user = GetLandingcostDetails.parseGetLandingcostDetails((SoapObject) response.getProperty(i));
                {
                    landcostdetails.add(user);
                }
            }

            return landcostdetails;

        } catch (Exception e) {
            Log.d("SoapRequest_LandingCost"
                    , "" + requestSoap.toString());
            throw e;
        }
    }

    public ValidateParts validateparts(String usrToken, int orderno, String partno) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_VALIDATE_PARTS);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("partno", partno);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_VALIDATE_PARTS, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numProps = response.getPropertyCount();

            ValidateParts valid = null;

            if (numProps > 0) {
                valid = ValidateParts.parseValidateParts((SoapObject) response.getProperty(0));
            }
            Log.d("Response_ValidateParts", "" + response.toString());

            return valid;
        } catch (Exception e) {
            throw e;
        }
    }

    public String SetLandingCost(String usrToken, double landingcost) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_LANDINGCOST);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("landingcost", landingcost);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_SET_LANDINGCOST, envelope);

            Log.d("SetLandingCost", "Response: " + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String SetLandingCostDetails(String usrToken, int headid, int orderno, String partno, String branch) throws Exception {
        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_LANDINGCOST_DETAILS);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("headid", headid);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("partno", partno);
        requestSoap.addProperty("branch", branch);

        envelope = getEnvelope(requestSoap);
        try {
            callService(eCountConstants.SOAP_ACTION_SET_LANDINGCOST_DETAILS, envelope);

            Log.d("SetLandingCostDetails", "Response :" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String SetPartsHeader(String usrToken, String branch, int orderno, double landingcost, String user) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_SET_PARTS_HEADER);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("landingcost", String.valueOf(landingcost));
        requestSoap.addProperty("user", user);


        envelope = getEnvelope(requestSoap);
        try {
            callService(eCountConstants.SOAP_ACTION_SET_PARTS_HEADER, envelope);
            Log.d("SetPartsHeader", "response" + envelope.getResponse().toString());
            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressLint("LongLogTag")
    public String SetNewPartNumber(String usrToken, int processId, String branch, String mfg, String kPart, String primaryBin, String secondaryBin, int qty, String notes) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_ADDNEWPART);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("processId", processId);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("kPart", kPart);
        requestSoap.addProperty("primaryBin", primaryBin);
        requestSoap.addProperty("secondaryBin", secondaryBin);
        requestSoap.addProperty("qty", qty);
        requestSoap.addProperty("notes", notes);

        Log.d("add_newpart_usrToken", usrToken);
        Log.d("add_newpart_processId", String.valueOf(processId));
        Log.d("add_newpart_branch", branch);
        Log.d("add_newpart_mfg", mfg);
        Log.d("add_newpart_kPart", kPart);
        Log.d("add_newpart_primaryBin", primaryBin);
        Log.d("add_newpart_secondaryBin", secondaryBin);
        Log.d("add_newpart_qty", String.valueOf(qty));
        Log.d("add_newpart_notes", notes);

        SoapObject requestSoapnew = new SoapObject(eCountConstants.NAME_SPACE,
                "images");

        requestSoap.addSoapObject(requestSoapnew);

        ArrayList<byte[]> base64s = new ArrayList<>();

        base64s = Sessiondata.getInstance().getHw_attachedFilesData();

        if (base64s.size() != 0) {
            for (int i = 0; i < base64s.size(); i++) {

                SoapObject req_imageBaseStr = new SoapObject(eCountConstants.NAME_SPACE,
                        "PartImages");

                String encodedImage = Base64.encodeToString(base64s.get(i), Base64.DEFAULT);

                req_imageBaseStr.addProperty("imageBaseStr", encodedImage);
                Log.d("add_newpart_With data", encodedImage);

                requestSoapnew.addSoapObject(req_imageBaseStr);

            }
        } else {
            SoapObject req_imageBaseStr = new SoapObject(eCountConstants.NAME_SPACE,
                    "PartImages");

            req_imageBaseStr.addProperty("imageBaseStr", "");
            Log.d("add_newpart_Without data", "" + req_imageBaseStr);

            requestSoapnew.addSoapObject(req_imageBaseStr);
        }

        envelope = getEnvelope(requestSoap);
        try {
            callService(eCountConstants.SOAP_ACTION_METHOD_ADDNEWPART, envelope);
            Log.d("SetAddnewpart", "response" + envelope.getResponse().toString());
            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }


    @SuppressLint("LongLogTag")
    public String SetNewPartNumberV1(String usrToken, int processId, String branch, String mfg, String kPart, String primaryBin, String secondaryBin, int qty, String notes) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_ADDNEWPARTV1);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("processId", processId);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("kPart", kPart);
        requestSoap.addProperty("primaryBin", primaryBin);
        requestSoap.addProperty("secondaryBin", secondaryBin);
        requestSoap.addProperty("qty", qty);
        requestSoap.addProperty("notes", notes);

        Log.d("add_newpart_usrToken", usrToken);
        Log.d("add_newpart_processId", String.valueOf(processId));
        Log.d("add_newpart_branch", branch);
        Log.d("add_newpart_mfg", mfg);
        Log.d("add_newpart_kPart", kPart);
        Log.d("add_newpart_primaryBin", primaryBin);
        Log.d("add_newpart_secondaryBin", secondaryBin);
        Log.d("add_newpart_qty", String.valueOf(qty));
        Log.d("add_newpart_notes", notes);

        SoapObject requestSoapnew = new SoapObject(eCountConstants.NAME_SPACE,
                "images");

        requestSoap.addSoapObject(requestSoapnew);

        ArrayList<byte[]> base64s = new ArrayList<>();

        base64s = Sessiondata.getInstance().getHw_attachedFilesData();

        if (base64s.size() != 0) {
            for (int i = 0; i < base64s.size(); i++) {

                SoapObject req_imageBaseStr = new SoapObject(eCountConstants.NAME_SPACE,
                        "PartImages");

                String encodedImage = Base64.encodeToString(base64s.get(i), Base64.DEFAULT);

                req_imageBaseStr.addProperty("imageBaseStr", encodedImage);
                Log.d("add_newpart_With datav1", encodedImage);

                requestSoapnew.addSoapObject(req_imageBaseStr);

            }
        } else {
            SoapObject req_imageBaseStr = new SoapObject(eCountConstants.NAME_SPACE,
                    "PartImages");

            req_imageBaseStr.addProperty("imageBaseStr", "");
            Log.d("add_newpart_Without datav1", "" + req_imageBaseStr);

            requestSoapnew.addSoapObject(req_imageBaseStr);
        }

        envelope = getEnvelope(requestSoap);
        try {
            callService(eCountConstants.SOAP_ACTION_METHOD_ADDNEWPARTV1, envelope);
            Log.d("SetAddnewpartv1", "response" + envelope.getResponse().toString());
            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public ValidateOrders ValidateOrders(String usrToken, int orderno, String partno) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_VALIDATE_ORDERS);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("orderno", orderno);
        requestSoap.addProperty("partno", partno);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_VALIDATE_ORDERS, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numProps = response.getPropertyCount( );

            ValidateOrders valid = null;

            if (numProps > 0) {
                valid = ValidateOrders.parseValidateOrders((SoapObject) response.getProperty(0));
            }

            Log.d("Response_ValdOrders", "" + response.toString());

            return valid;
        } catch (Exception e) {
            throw e;
        }
    }

    public ArrayList<GetManufacturers> GetManufacturerses(String usrToken, int processid, String partno, String branch) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GETMANUFACTURERS);

        requestSoap.addProperty("usrToken", usrToken);
        requestSoap.addProperty("processId", processid);
        requestSoap.addProperty("partNo", partno);
        requestSoap.addProperty("branch", branch);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_METHOD_GETMANUFACTURERA, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();

            int numprop = response.getPropertyCount();


            int numprocess = response.getPropertyCount();

            ArrayList<GetManufacturers> getManufacturerses = new ArrayList<GetManufacturers>();

            for (int i = 0; i < numprocess; i++) {

                GetManufacturers user = GetManufacturers.parsegetmanufaturers((SoapObject) response.getProperty(i));
                {
                    getManufacturerses.add(user);
                }
            }
            return getManufacturerses;

        } catch (Exception e) {
            throw e;
        }
    }

    public ArrayList<GetVarianceList> GetVarianceList(String usertoken, int processid, String startbin, String endbin, String userBranch, String partNum, int startindex, int endindex) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_VARIANCE);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("startbin", startbin);
        requestSoap.addProperty("endbin", endbin);
        requestSoap.addProperty("branch", userBranch);
//        requestSoap.addProperty("partno",partNum);
        requestSoap.addProperty("partno", partNum);
        requestSoap.addProperty("startindex", startindex);
        requestSoap.addProperty("endindex", endindex);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_METHOD_VARIANCE, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            int numprocess = response.getPropertyCount();

            ArrayList<GetVarianceList> varianceLists = new ArrayList<GetVarianceList>();

            for (int i = 0; i < numprocess; i++) {

                GetVarianceList user = GetVarianceList.parseQty((SoapObject) response.getProperty(i));
                {
                    varianceLists.add(user);
                }
            }
            return varianceLists;

        } catch (Exception e) {

            Log.d("SoapRequest_variance", "" + requestSoap.toString());
            throw e;

        }
    }

    public ArrayList<GetDealerBranch> GetBranchV2(String usertoken, String type) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE,
                eCountConstants.METHOD_GET_BRANCH_V2);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("type", type);

        Log.d("usrToken", usertoken);
        Log.d("type", type);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_GET_BRANCH_V2, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            Log.d("response", envelope.getResponse().toString());
            int numProps = response.getPropertyCount();
            ArrayList<GetDealerBranch> branch = new ArrayList<GetDealerBranch>();

            for (int i = 0; i < numProps; i++) {

                GetDealerBranch user = GetDealerBranch.parseBranch((SoapObject) response.getProperty(i));
                {
                    branch.add(user);
                }
            }

            return branch;

        } catch (Exception e) {
            Log.d("SoapRequest_branchV2"
                    , "" + requestSoap.toString());
            throw e;
        }
    }

    public String UncountEquipment(String usertoken, String equipid, int processid, String user, String branch) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_UNCOUNTEDEQUIPMENT);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("equipid", equipid);
        requestSoap.addProperty("processid", processid);
        requestSoap.addProperty("user", user);
        requestSoap.addProperty("branch", branch);


        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_UNCOUNTEDEQUIPMENT, envelope);

            Log.d("Response_uncount_equp", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

    @SuppressLint("LongLogTag")
    public String CheckPartsAvailability(String usertoken, String partnum, String mfg) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_CHECK_PARTS_AVAILABILITY);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("partnum", partnum);
        requestSoap.addProperty("mfg", mfg);

        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_CHECK_PARTS_AVAILABILITY, envelope);

            Log.d("Response_checkpartsavailability", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }


    public String InsertPMParts(String usertoken, String mfg, String partnum, String description, String comm,
                                String mfgclass, String unitstandard,
                                String weight,
                                String sell1, String sell2, String sell3, String sell4, String sell5,
                                String cost) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_INSERT_PM_PARTS);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("partnum", partnum);
        requestSoap.addProperty("description", description);
        requestSoap.addProperty("comm", comm);
        requestSoap.addProperty("mfgclass", mfgclass);
        requestSoap.addProperty("unitstandard", unitstandard);
        requestSoap.addProperty("weight", weight);
        requestSoap.addProperty("sell1", sell1);
        requestSoap.addProperty("sell2", sell2);
        requestSoap.addProperty("sell3", sell3);
        requestSoap.addProperty("sell4", sell4);
        requestSoap.addProperty("sell5", sell5);
        requestSoap.addProperty("cost", cost);


        envelope = getEnvelope(requestSoap);

        try {
            callService(eCountConstants.SOAP_ACTION_INSERT_PM_PARTS, envelope);

            Log.d("Response_InsertPm_Parts", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }


    public GetPmPartDetails GetPMPartDetails(String usertoken, String partsno, String kmfg)throws Exception{

        GetPmPartDetails getPmPartDetails = new GetPmPartDetails();

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PM_PARTS);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("partno", partsno);
        requestSoap.addProperty("kmfg", kmfg);

        envelope = getEnvelope(requestSoap);

        try {

            callService(eCountConstants.SOAP_ACTION_GET_PM_PARTS, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            Log.d("Response_get_Pm_Parts", "" + envelope.getResponse().toString());


            int count = response.getPropertyCount();

            if(count > 0){
                getPmPartDetails = GetPmPartDetails.parsePmpartdetail((SoapObject) response.getProperty(0));
            }

            return getPmPartDetails;

        } catch (Exception e) {
            throw e;
        }


    }



    @SuppressLint("LongLogTag")
    public String GetPrintLabelPermission(String usertoken) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PRINT_LABEL_PERMISSION);

        requestSoap.addProperty("usrToken", usertoken);


        envelope = getEnvelope(requestSoap);

        Log.d("Request_GetPrintLabelPermission", "" + requestSoap.toString());

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PRINT_LABEL_PERMISSION, envelope);

            Log.d("Response_GetPrintLabelPermission", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }


    @SuppressLint("LongLogTag")
    public String PrintLabelsForPart(String usertoken,String branch,String mfg,String partnumber,
                                     String description,int orderqty,int receivedqty
            ,String ponumber,String oeprodernum, String location) throws Exception {

        requestSoap = new SoapObject(eCountConstants.NAME_SPACE, eCountConstants.METHOD_GET_PRINT_LABELS_PART);

        requestSoap.addProperty("usrToken", usertoken);
        requestSoap.addProperty("branch", branch);
        requestSoap.addProperty("mfg", mfg);
        requestSoap.addProperty("partnumber", partnumber);
        requestSoap.addProperty("description", description);
        requestSoap.addProperty("orderqty", orderqty);
        requestSoap.addProperty("receivedqty", receivedqty);
        requestSoap.addProperty("ponumber", ponumber);
        requestSoap.addProperty("oeprodernum", oeprodernum);
        requestSoap.addProperty("location", location);


        envelope = getEnvelope(requestSoap);

        Log.d("Request_PrintLabelsForPart", "" + requestSoap.toString());

        try {
            callService(eCountConstants.SOAP_ACTION_GET_PRINT_LABELS_PART, envelope);

            Log.d("Response_PrintLabelsForPart", "" + envelope.getResponse().toString());

            return envelope.getResponse().toString();
        } catch (Exception e) {
            throw e;
        }
    }

}
