package com.ebs.ecount.parts_receipt;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cognex.dataman.sdk.ConnectionState;
import com.cognex.mobile.barcode.sdk.ReadResult;
import com.cognex.mobile.barcode.sdk.ReadResults;
import com.cognex.mobile.barcode.sdk.ReaderDevice;
import com.ebs.ecount.R;
import com.ebs.ecount.initial.Dashboard;
import com.ebs.ecount.initial.LoginActivity;
import com.ebs.ecount.objects.GetDealerBranch;
import com.ebs.ecount.objects.GetEPONumber;
import com.ebs.ecount.objects.GetPartsDetails;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.objects.PoObject;
import com.ebs.ecount.swipe.PartReceiptListViewSwipeGesture;
import com.ebs.ecount.uidesigns.ProgressBar;
import com.ebs.ecount.uidesigns.SimpleScannerActivity;
import com.ebs.ecount.uidesigns.SweetAlertDialog;
import com.ebs.ecount.utils.Sessiondata;
import com.ebs.ecount.webutils.WebServiceConsumer;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import com.cognex.mobile.barcode.sdk.ReaderDevice.ReaderDeviceListener;
import com.cognex.mobile.barcode.sdk.ReaderDevice.OnConnectionCompletedListener;

/**
 * Created by techunity on 22/11/16.
 */
public class PartReceiptActivity extends Activity implements OnConnectionCompletedListener, ReaderDeviceListener {

    Button back, add_po;
    String process_list;
    Button next;
    String po_delete;
    String str_po_no = "";
    EditText po_no;
    ImageView po_search;
    ImageView branch;
    static String branch_name;
    AutoCompleteTextView branch_no;
    ArrayList<GetDealerBranch> dealerbranch;
    GetEPONumber EPOnum;
    String usertoken;
    boolean couldStartActivity = false;

    int Session = 0;
    ArrayList<GetDealerBranch> branchlist;

    private CustomAdapter adapter;
    ListView po_list;
    ArrayList<Integer> selectedList = new ArrayList<Integer>();
    ArrayList<PoObject> dummydata;
    Dialog Dialog;

    SweetAlertDialog sweetalt, sweeterror, PoNoEnder, addPonum, po_alrt, scanpo, podelete;
    Boolean sweetalrt = true;
    Boolean sweetalrt_delete = true;
    Boolean sweetaddpo=true;
    SweetAlertDialog sweetaddalret;
    Boolean add_po_status = false;
    Dialog mDialogSession;
    Dialog mDialognodata;

    LoginObject loginObject = null;
    int next_navigation;
    int Epo;
    int odertype;

    Typeface header_face, txt_face;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    int add_povalue = 0;

    int dialog_view=0;
    Dialog mDialogscan = null;
    Boolean isDeviceConnected = false;
    HashMap<String, UsbDevice> usbDevices;

    private Context mContext;
    public static ReaderDevice readerDevice;


    public static enum DeviceType { MX_1000}
    private static final DeviceType[] deviceTypeValues = DeviceType.values();
    public static DeviceType deviceTypeFromInt(int i) { return deviceTypeValues[i];	}


    //------

    public static String selectedDevice = "";
    public static boolean fragmentActive = false;

    ArrayList<GetPartsDetails> getparts;
    Boolean sweet_load=true;
    SweetAlertDialog sweet_load_alert,Sweetalt_list;
    Boolean Sweetalrt_list=true;

    String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_receipts);

        mContext = this;

        back = (Button) findViewById(R.id.back);
        next = (Button) findViewById(R.id.next);

        po_search = (ImageView) findViewById(R.id.po_search);
        add_po = (Button) findViewById(R.id.add_po);
        po_no = (EditText) findViewById(R.id.po_no);
        po_list = (ListView) findViewById(R.id.po_list);
        branch = (ImageView) findViewById(R.id.branch_list);
        branch_no = (AutoCompleteTextView) findViewById(R.id.branch_no);

        branch_no.setEnabled(false);

        getparts = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String po_number = bundle.getString("value");
            if(Sessiondata.getInstance().getPo_value().toString().equalsIgnoreCase("yes")) {
                Sessiondata.getInstance().setPo_receipt(po_number);
            }
        }


        po_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Sessiondata.getInstance().setScanner_partreceipt(1);
                    Sessiondata.getInstance().setScanner_partreceiving(0);
                    Sessiondata.getInstance().setScanner_replace(0);
                    Sessiondata.getInstance().setScanner_counting1(0);
                    Sessiondata.getInstance().setScanner_counting2(0);
                    Sessiondata.getInstance().setScanner_inventory(0);

                    Sessiondata.getInstance().setScanner_partnumber(0);
                    Sessiondata.getInstance().setScanner_hwstartbin(0);
                    Sessiondata.getInstance().setScanner_hwendbin(0);

                    Sessiondata.getInstance().setPo_receipt(po_no.getText().toString());
                    Sessiondata.getInstance().setPo_value("yes");


                    launchActivity(SimpleScannerActivity.class);

            }
        });

        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearSession();
                Intent intent = new Intent(PartReceiptActivity.this, Dashboard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });

        branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConenction()) {

                    new AsyncGetDealerBranch().execute();

                } else {

                    Toast.makeText(PartReceiptActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });


        branch_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearSession();
                Intent intent = new Intent(PartReceiptActivity.this, Dashboard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });

        if (Sessiondata.getInstance().getArray_EPONum() != null) {
            next_navigation = 1;
            dummydata = Sessiondata.getInstance().getArray_EPONum();
            adapter = new CustomAdapter(this, dummydata);
            po_list.setAdapter(adapter);
        } else {
            dummydata = new ArrayList<>();
            adapter = new CustomAdapter(this, dummydata);
            po_list.setAdapter(adapter);
        }

        final PartReceiptListViewSwipeGesture touchListener = new PartReceiptListViewSwipeGesture(po_list, swipeListener, this);
        touchListener.SwipeType = PartReceiptListViewSwipeGesture.Dismiss;    //Set two options at background of list item
        po_list.setOnTouchListener(touchListener);

        if (Sessiondata.getInstance().getBranch_get().toString().length() == 0) {
            if (Sessiondata.getInstance().getLoginObject().getBranch() != null) {
                branch_no.setText(Sessiondata.getInstance().getLoginObject().getBranch());
                branch_name = branch_no.getText().toString();
            }
        } else {
            branch_no.setText(Sessiondata.getInstance().getBranch_get());
            branch_name = branch_no.getText().toString();
        }

        for (int ii = 0; ii < branch_name.length(); ii++) {

            Character character = branch_name.charAt(ii);

            if (character.toString().equals("-")) {

                branch_name = branch_name.substring(0,ii);

                Log.d("branch_trim", "" + branch_name);
                break;
            }
        }

        if (Sessiondata.getInstance().getEPO_Status()) {
            Epo = 1;
        } else {
            Epo = 0;
        }

        if(Sessiondata.getInstance().getOrdertype()) {
            odertype = 1;
        }else {
            odertype=0;
        }


        add_po.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = po_no.getText().toString();
                name = name.replace(" ", "");

                Log.d("PO Number", "" + name);
                if (name.isEmpty()) {

                    if (sweetaddpo) {
                        sweetaddpo = false;

                        sweetaddalret = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the PO#")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sweetaddpo = true;
                                        sDialog.dismiss();
                                    }
                                });
                        sweetaddalret.setCancelable(false);
                        sweetaddalret.show();
                    }

                } else {

                    if (checkInternetConenction()) {

                        Sessiondata.getInstance().setPo_receipt("");
                        Sessiondata.getInstance().setPo_value("");

                        if(add_povalue==0){
                            add_povalue=1;
                            new AsyncGetEPONum().execute();
                        }
                    } else {
                        Toast.makeText(PartReceiptActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("branch_final", "" + branch_name);
                Sessiondata.getInstance().setBranch_trim(branch_name);
                if (Sessiondata.getInstance().getArray_EPONum() != null) {
                    if (next_navigation == 1) {

                        if (checkInternetConenction()) {

                            if (Sessiondata.getInstance().getGetEPOParts() != null){
                                Sessiondata.getInstance().setGetEPOParts(null);
                            }

                            ArrayList<String> Po_num = new ArrayList<String>();
                            for (int i=0;i<dummydata.size();i++){
                                Po_num.add(Sessiondata.getInstance().getArray_EPONum().get(i).getName());
                            }
                            String PO_array = android.text.TextUtils.join(",",Po_num);
                            Log.d("PO_array",""+PO_array.toString());

                            Sessiondata.getInstance().setPO_Array(PO_array);

                            new AsyncGetEpoParts().execute();
                        } else {
                            Toast.makeText(PartReceiptActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        if (sweetalrt) {
                            sweetalrt = false;
                            sweetalt = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                    .setTitleText("Alert!")
                                    .setContentText("You are requried to add PO#")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(null)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrt = true;
                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    }
                } else {
                    if (sweetalrt) {
                        sweetalrt = false;

                        sweeterror = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("You are requried to add PO#")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener( new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sweetalrt = true;
                                        sDialog.dismiss();
                                    }
                                });
                        sweeterror.setCancelable(false);
                        sweeterror.show();
                    }
                }
            }
        });

        TextView header = (TextView) findViewById(R.id.header);
        TextView txt_br = (TextView) findViewById(R.id.txt_branch);
        TextView txt_po = (TextView) findViewById(R.id.txt_po);
        header_face = Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");
        txt_face = Typeface.createFromAsset(getAssets(), "fonts/helr45w.ttf");
        next.setTypeface(header_face);
        add_po.setTypeface(header_face);
        header.setTypeface(header_face);
        txt_br.setTypeface(header_face);
        txt_po.setTypeface(header_face);
        back.setTypeface(txt_face);
        branch_no.setTypeface(txt_face);
        po_no.setTypeface(txt_face);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initDevice();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (readerDevice != null && readerDevice.getConnectionState() == ConnectionState.Connected) {
            readerDevice.disconnect();
        }
    }

    private void initDevice() {

        PartReceiptActivity.readerDevice = ReaderDevice.getMXDevice(mContext);
        readerDevice.startAvailabilityListening();

        selectedDevice = "MX Scanner";

        PartReceiptActivity.readerDevice.setReaderDeviceListener(this);
        PartReceiptActivity.readerDevice.enableImage(true);
        PartReceiptActivity.readerDevice.connect(PartReceiptActivity.this);
    }

    @Override
    public void onConnectionStateChanged(ReaderDevice reader) {
        if (reader.getConnectionState() == ConnectionState.Connected) {
            readerConnected();
        } else if (reader.getConnectionState() == ConnectionState.Disconnected) {
            readerDisconnected();
        }
    }

    @Override
    public void onReadResultReceived(ReaderDevice reader, ReadResults results) {
        if (results.getCount() > 0) {
            ReadResult result = results.getResultAt(0);

            if (result.isGoodRead()) {
                ReaderDevice.Symbology sym = result.getSymbology();
                if (sym != null) {

                } else {

                }

                FinalResult(result.getReadString());

            } else {

            }

        }

    }



    public void FinalResult(final String result){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Sessiondata.getInstance().setPo_receipt(result);
                po_no.setText(Sessiondata.getInstance().getPo_receipt());

            }
        }, 500);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(po_no.getWindowToken(), 0);
    }


    @Override
    public void onAvailabilityChanged(ReaderDevice reader) {
        if (reader.getAvailability() == ReaderDevice.Availability.AVAILABLE) {
            PartReceiptActivity.readerDevice.connect(PartReceiptActivity.this);
        } else {
            // DISCONNECTED USB DEVICE
            PartReceiptActivity.readerDevice.connect(PartReceiptActivity.this);
            PartReceiptActivity.readerDevice.disconnect();
            readerDisconnected();
        }
    }

    @Override
    public void onConnectionCompleted(ReaderDevice reader, Throwable error) {
        if (error != null) {
            readerDisconnected();
        }
    }

    private void readerDisconnected() {
        Log.d("cmb.SampleApp", "onDisconnected");

    }

    private void readerConnected() {

        Log.d("cmb.SampleApp", "onConnected");

        PartReceiptActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.C128, true, null);
        PartReceiptActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.DATAMATRIX, true, null);
        PartReceiptActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.UPC_EAN, true, null);
        PartReceiptActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.QR, true, null);

        PartReceiptActivity.readerDevice.getDataManSystem().sendCommand("SET SYMBOL.MICROPDF417 ON");
        PartReceiptActivity.readerDevice.getDataManSystem().sendCommand("SET IMAGE.SIZE 0");

    }

    PartReceiptListViewSwipeGesture.TouchCallbacks swipeListener = new PartReceiptListViewSwipeGesture.TouchCallbacks() {

        @Override
        public void FullSwipeListView() {
            // TODO Auto-generated method stub
        }

        @Override
        public void HalfSwipeListView(int position) {
            // TODO Auto-generated method stub
            dummydata.remove(position);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void LoadDataForScroll() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDismiss(final int[] reverseSortedPositions) {
            // TODO Auto-generated method stub

                if (sweetalrt_delete) {
                    sweetalrt_delete = false;

                    for (int i : reverseSortedPositions) {
                        po_delete = dummydata.get(i).getName();
                        Log.d("po_delete", "" + po_delete);
                    }

                    podelete = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.INFO_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You Want to Remove This PO# " + po_delete)
                            .setCancelText("No,Cancel")
                            .setConfirmText("Yes,Remove it")
                            .showCancelButton(true)

                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrt_delete = true;
                                    sDialog.dismiss();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    if (dummydata.size() == 1) {
                                        next_navigation = 0;
                                        Epo = 0;
                                    }

                                    sDialog.setTitleText("Removed!")
                                            .setContentText("PO# " + po_delete + " has been removed!")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                    if (Sessiondata.getInstance().getPartObjects() != null){
                                        Sessiondata.getInstance().getPartObjects().clear();
                                    }

                                    for (int i : reverseSortedPositions) {
                                        sweetalrt_delete = true;
                                        dummydata.remove(i);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                    podelete.setCancelable(false);
                    podelete.show();
                }
        }

        @Override
        public void OnClickListView() {
            // TODO Auto-generated method stub
        }
    };


    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public class AsyncGetDealerBranch extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(PartReceiptActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + usertoken);

                dealerbranch = WebServiceConsumer.getInstance().Getdealerbranch(
                        usertoken);

            } catch (SocketTimeoutException e) {
                dealerbranch = null;
            } catch (Exception e) {
                dealerbranch = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setDealerbranch(dealerbranch);

            CustomAdapter adapter_processid;
            if (dealerbranch != null) {

                if (dealerbranch.size() == 1) {

                    if (dealerbranch.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = dealerbranch.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (dealerbranch.get(0).getMessage().toString().contains("Session")) {
                            Session = 0;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(PartReceiptActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {

                        ProgressBar.dismiss();
                        if (Dialog == null) {
                            Dialog = new Dialog(PartReceiptActivity.this);
                            Dialog.setCanceledOnTouchOutside(false);
                            Dialog.setCancelable(false);
                            Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            Dialog.setContentView(R.layout.dialog_list_select);

                            TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                            txt_header.setText("Select Branch");
                            txt_header.setTypeface(header_face);

                            ListView list = (ListView) Dialog.findViewById(R.id.list);

                            branchlist = new ArrayList<>();

                            branchlist = Sessiondata.getInstance().getDealerbranch();

                            adapter_processid = new CustomAdapter_processid(PartReceiptActivity.this, branchlist);
                            list.setAdapter(adapter_processid);

                            TextView dialog_social_ok = (TextView) Dialog.findViewById(R.id.dialog_social_ok);
                            dialog_social_ok.setTypeface(header_face);

                            dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Dialog.dismiss();
                                }
                            });

                            Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                            Dialog.show();
                        } else if (!Dialog.isShowing()) {
                            Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                            Dialog.show();
                        }

                    }
                } else {
                    ProgressBar.dismiss();
                    if (Dialog == null) {
                        Dialog = new Dialog(PartReceiptActivity.this);
                        Dialog.setCanceledOnTouchOutside(false);
                        Dialog.setCancelable(false);
                        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Dialog.setContentView(R.layout.dialog_list_select);

                        TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                        txt_header.setText("Select Branch");
                        txt_header.setTypeface(header_face);

                        ListView list = (ListView) Dialog.findViewById(R.id.list);

                        branchlist = new ArrayList<>();

                        branchlist = Sessiondata.getInstance().getDealerbranch();

                        adapter_processid = new CustomAdapter_processid(PartReceiptActivity.this, branchlist);
                        list.setAdapter(adapter_processid);

                        TextView dialog_social_ok = (TextView) Dialog.findViewById(R.id.dialog_social_ok);
                        dialog_social_ok.setTypeface(header_face);

                        dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog.dismiss();
                            }
                        });

                        Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        Dialog.show();
                    } else if (!Dialog.isShowing()) {
                        Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        Dialog.show();
                    }

                }

            } else {
                ProgressBar.dismiss();
                if (Dialog == null) {
                    Dialog = new Dialog(PartReceiptActivity.this);
                    Dialog.setCanceledOnTouchOutside(false);
                    Dialog.setCancelable(false);
                    Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Dialog.setContentView(R.layout.dialog_list_select);

                    TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                    txt_header.setText("Select Branch");
                    txt_header.setTypeface(header_face);

                    ListView list = (ListView) Dialog.findViewById(R.id.list);

                    branchlist = new ArrayList<>();

                    branchlist = Sessiondata.getInstance().getDealerbranch();

                    adapter_processid = new CustomAdapter_processid(PartReceiptActivity.this, branchlist);
                    list.setAdapter(adapter_processid);

                    TextView dialog_social_ok = (TextView) Dialog.findViewById(R.id.dialog_social_ok);
                    dialog_social_ok.setTypeface(header_face);

                    dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog.dismiss();
                        }
                    });

                    Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    Dialog.show();
                } else if (!Dialog.isShowing()) {
                    Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    Dialog.show();
                }

            }
        }
    }

    public class AsyncGetEPONum extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(PartReceiptActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + usertoken);

                EPOnum = WebServiceConsumer.getInstance().geteponum(usertoken, branch_no.getText().toString(), Integer.parseInt(po_no.getText().toString()));

            } catch (SocketTimeoutException e) {
                EPOnum = null;
            } catch (Exception e) {
                EPOnum = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            add_povalue=0;

            if (EPOnum != null) {

                ProgressBar.dismiss();

                Sessiondata.getInstance().setEPONum(EPOnum);

                if (EPOnum.getMessage().length() != 0) {
                    String Result = EPOnum.getMessage();
                    String replace = Result.replace("Error - ", "");
                    if (EPOnum.getMessage().toString().contains("Session")) {
                        Session = 1;
                        if (checkInternetConenction()) {

                            new AsyncSessionLoginTask().execute();
                        } else {

                            Toast.makeText(PartReceiptActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }

                    }
                }

//
//                if (!(EPOnum.getOrdertype().contains("E")) && !(EPOnum.getOrdertype().contains("P")) && !(EPOnum.getOrdertype().contains("T"))  && (EPOnum.getPonum() == 0)) {
                if ((EPOnum.getPonum() == 0) && !EPOnum.getMessage().toString().contains("Session")) {
                    if (sweetalrt) {
                        sweetalrt = false;
                        scanpo = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Enter or Scan Correct PO#")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        po_no.setText("");
                                        sweetalrt = true;
                                        sDialog.dismiss();
                                    }
                                });
                        scanpo.setCancelable(false);
                        scanpo.show();
                    }

                } else if(!(EPOnum.getOrdertype().contains("E")) && !(EPOnum.getOrdertype().contains("P")) && !(EPOnum.getOrdertype().contains("T")) &&  EPOnum.getPonum() != 0 && EPOnum.getMessage().equals("C")){
                    if (sweetalrt) {
                        sweetalrt = false;
                        scanpo = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("No unreceived items for this PO#")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        po_no.setText("");
                                        sweetalrt = true;
                                        sDialog.dismiss();
                                    }
                                });
                        scanpo.setCancelable(false);
                        scanpo.show();
                    }

                }
                else if (!(EPOnum.getOrdertype().contains("E")) && !(EPOnum.getOrdertype().contains("P")) && !(EPOnum.getOrdertype().contains("T"))&& EPOnum.getPonum() != 0) { // && EPOnum.getMessage().equals("I")                        && EPOnum.getPonum() != 0) {

                    if (Epo == 1) {
                        if (sweetalrt) {
                            sweetalrt = false;
                            po_alrt = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Cannot add more than one EBO#")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(null)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            po_no.setText("");
                                            sweetalrt = true;
                                            sDialog.dismiss();
                                        }
                                    });
                            po_alrt.setCancelable(false);
                            po_alrt.show();
                        }
                    } else {
                        String enter_po = po_no.getText().toString();
                        Log.d("enter_po", "" + enter_po);

                        if (dummydata.size() != 0) {
                            for (int i = 0; i < dummydata.size(); i++) {
                                if (dummydata.get(i).getName().toString().equalsIgnoreCase(enter_po)) {
                                    add_po_status = true;

                                    if (sweetalrt) {
                                        sweetalrt = false;
                                        po_alrt = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                .setTitleText("Warning!")
                                                .setContentText("Cannot add Same PO#")
                                                .setCancelText("Ok")
                                                .showCancelButton(false)
                                                .setConfirmClickListener(null)
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override

                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        add_po_status = false;
                                                        po_no.setText("");
                                                        sweetalrt = true;
                                                        sDialog.dismiss();
                                                    }
                                                });
                                        po_alrt.setCancelable(false);
                                        po_alrt.show();
                                        break;
                                    }
                                }
                            }

                            if (!add_po_status) {

                                next_navigation = 1;
                                dialog_view=1;
                                Epo = 0;

                                Sessiondata.getInstance().setEPO_Status(false);

                                Log.d("Status_changeF", "" + Sessiondata.getInstance().getEPO_Status());

                                PoObject md = new PoObject(Integer.toString(Sessiondata.getInstance().getEPONum().getPonum()));
                                dummydata.add(md);
                                adapter.notifyDataSetChanged();
                                po_no.setText("");

                                Sessiondata.getInstance().setArray_EPONum(dummydata);

                            }

                        } else {
                            next_navigation = 1;
                            dialog_view=1;
                            Epo = 0;

                            Sessiondata.getInstance().setEPO_Status(false);

                            Log.d("Status_changeF", "" + Sessiondata.getInstance().getEPO_Status());

                            PoObject md = new PoObject(Integer.toString(Sessiondata.getInstance().getEPONum().getPonum()));
                            dummydata.add(md);
                            adapter.notifyDataSetChanged();
                            po_no.setText("");

                            Sessiondata.getInstance().setArray_EPONum(dummydata);
                        }
                    }

                }

                else if ((EPOnum.getOrdertype().contains("E") ||EPOnum.getOrdertype().contains("P")||EPOnum.getOrdertype().contains("T") ||EPOnum.getOrdertype().length()==0) && !EPOnum.getMessage().contains("C") && EPOnum.getPonum()!=0) {

                    if (dummydata.size() == 0) {
                        next_navigation = 1;

                        dialog_view=0;
                        Epo = 1;

                        Sessiondata.getInstance().setEPO_Status(true);

                        Log.d("Status_changeT", "" + Sessiondata.getInstance().getEPO_Status());

                        PoObject md = new PoObject(Integer.toString(Sessiondata.getInstance().getEPONum().getPonum()));
                        dummydata.add(md);
                        adapter.notifyDataSetChanged();
                        po_no.setText("");

                        Sessiondata.getInstance().setArray_EPONum(dummydata);
                    } else {

                        if(dialog_view==1) {


                            if (sweetalrt) {
                                sweetalrt = false;

                                addPonum = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Only non EBO# can be added!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {

                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                po_no.setText("");
                                                sweetalrt = true;
                                                sDialog.dismiss();
                                            }
                                        });
                                addPonum.setCancelable(false);
                                addPonum.show();
                            }
                        }else{

                            if (sweetalrt) {
                                sweetalrt = false;

                                addPonum = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Cannot add more than one EBO#")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {

                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                po_no.setText("");
                                                sweetalrt = true;
                                                sDialog.dismiss();

                                            }
                                        });
                                addPonum.setCancelable(false);
                                addPonum.show();
                            }
                        }
                    }
                }
            } else {
                ProgressBar.dismiss();

                if (sweetalrt) {
                    sweetalrt = false;

                    PoNoEnder = new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setContentText("PO# Entered Invalid!")
                            .setCancelText("Ok")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    po_no.setText("");
                                    sweetalrt = true;
                                    sDialog.dismiss();
                                }
                            });
                    PoNoEnder.setCancelable(false);
                    PoNoEnder.show();
                }
            }
        }
    }

    public void ClearSession() {

        add_po_status = false;
        Sessiondata.getInstance().setReplaceadapter(0);
        Sessiondata.getInstance().setPartReceive(0);
        Sessiondata.getInstance().setAddFreight(0);
        Sessiondata.getInstance().setInBound_data(0.0);
        Sessiondata.getInstance().setOutBound_data(0.0);
        Sessiondata.getInstance().setEPO_Status(false);
        Sessiondata.getInstance().setBranch_get("");
        Sessiondata.getInstance().setBranch_trim("");

        Sessiondata.getInstance().setReplace_part("");
        Sessiondata.getInstance().setReplace_mfr("");
        Sessiondata.getInstance().setReplace_qty("");
        Sessiondata.getInstance().setReplace_oldpart("");
        Sessiondata.getInstance().setReplace_order("");
        Sessiondata.getInstance().setReplace_value("");
        Sessiondata.getInstance().setReplace_transfer("");

        Sessiondata.getInstance().setPo_receipt("");
        Sessiondata.getInstance().setPo_value("");

        Sessiondata.getInstance().setPo_recive("");
        Sessiondata.getInstance().setPo_recive_value("");

        //arraylist
        if (Sessiondata.getInstance().getArray_EPONum() != null) {
            Sessiondata.getInstance().getArray_EPONum().clear();
        }

        if (Sessiondata.getInstance().getPartObjects() != null) {
            Sessiondata.getInstance().getPartObjects().clear();
        }

        if (Sessiondata.getInstance().getFreightlists() != null) {
            Sessiondata.getInstance().getFreightlists().clear();
        }

        if (Sessiondata.getInstance().getDealerbranch() != null) {
            Sessiondata.getInstance().getDealerbranch().clear();
        }

        if (Sessiondata.getInstance().getFreightlistsnew() != null) {
            Sessiondata.getInstance().getFreightlistsnew().clear();
        }

        if (Sessiondata.getInstance().getMultiplePONumbers() != null) {
            Sessiondata.getInstance().getMultiplePONumbers().clear();
        }

        //object
        if (Sessiondata.getInstance().getEPONum() != null) {
            Sessiondata.getInstance().setEPONum(null);
        }
        if (Sessiondata.getInstance().getPartsDetails() != null) {
            Sessiondata.getInstance().setPartsDetails(null);
        }
        if (Sessiondata.getInstance().getValidateParts() != null) {
            Sessiondata.getInstance().setValidateParts(null);
        }

    }

    private boolean checkInternetConenction() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class AsyncSessionLoginTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(PartReceiptActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                loginObject = WebServiceConsumer.getInstance().authenticateUser(
                        Sessiondata.getInstance().getTemp_username(),
                        Sessiondata.getInstance().getTemp_password());

            } catch (SocketTimeoutException e) {
                loginObject = null;
            } catch (Exception e) {
                loginObject = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (loginObject != null) {

                Sessiondata.getInstance().setLoginObject(loginObject);

                Log.d("New_Session_Usertoken", "" + Sessiondata.getInstance().getLoginObject().getUsertoken());

                if (loginObject.getUserid() == 0) {
                    ProgressBar.dismiss();

                    if (mDialogSession == null) {
                        mDialogSession = new Dialog(PartReceiptActivity.this);
                        mDialogSession.setCanceledOnTouchOutside(false);
                        mDialogSession.setCancelable(false);
                        mDialogSession.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialogSession.setContentView(R.layout.activity_message);

                        TextView mDialogFreeCancelButton = (TextView) mDialogSession.findViewById(R.id.dialog_social_cancel);
                        TextView mDialogtxt_header = (TextView) mDialogSession.findViewById(R.id.txt_header);
                        TextView mDialogFreeOKButton = (TextView) mDialogSession.findViewById(R.id.dialog_social_ok);

                        TextView txt_dialog = (TextView) mDialogSession.findViewById(R.id.txt_dialog);
                        String Result = loginObject.getMessage().toString();
                        txt_dialog.setText(Result);
                        txt_dialog.setTypeface(txt_face);
                        mDialogtxt_header.setTypeface(header_face);
                        mDialogFreeOKButton.setTypeface(header_face);
                        mDialogFreeCancelButton.setTypeface(header_face);

                        final Dialog finalMDialog = mDialogSession;
                        mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PartReceiptActivity.this, LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                                finalMDialog.dismiss();
                            }
                        });

                        mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                finalMDialog.dismiss();
                            }
                        });
                        mDialogSession.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        mDialogSession.show();
                    } else if (!mDialogSession.isShowing()) {
                        mDialogSession.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        mDialogSession.show();
                    }


                } else {
                    ProgressBar.dismiss();
                    if (Session == 0) {
                        if (checkInternetConenction()) {

                            new AsyncGetDealerBranch().execute();

                        } else {

                            Toast.makeText(PartReceiptActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    } else if (Session == 1) {

                        if (checkInternetConenction()) {

                            new AsyncGetEPONum().execute();

                        } else {

                            Toast.makeText(PartReceiptActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    } else if (Session == 2) {

                        if (checkInternetConenction()) {

                            new AsyncGetEpoParts().execute();

                        } else {

                            Toast.makeText(PartReceiptActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }


                }
            } else {
                ProgressBar.dismiss();

                if (mDialognodata == null) {
                    mDialognodata = new Dialog(PartReceiptActivity.this);
                    mDialognodata.setCanceledOnTouchOutside(false);
                    mDialognodata.setCancelable(false);
                    mDialognodata.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialognodata.setContentView(R.layout.dialog_no_data);

                    TextView mDialogFreeOKButton = (TextView) mDialognodata.findViewById(R.id.dialog_social_ok);

                    TextView txt_dialog = (TextView) mDialognodata.findViewById(R.id.txt_dialog);
                    TextView mDialogtxt_header = (TextView) mDialognodata.findViewById(R.id.txt_header);

                    String Result = loginObject.getMessage().toString();

                    txt_dialog.setText(Result);

                    txt_dialog.setTypeface(txt_face);
                    mDialogtxt_header.setTypeface(header_face);
                    mDialogFreeOKButton.setTypeface(header_face);

                    final Dialog finalMDialog = mDialognodata;
                    mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            finish();
                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                            finalMDialog.dismiss();
                        }
                    });

                    mDialognodata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    mDialognodata.show();
                } else if (!mDialognodata.isShowing()) {
                    mDialognodata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    mDialognodata.show();
                }

            }
        }
    }

    public class CustomAdapter extends BaseAdapter {
        ArrayList<PoObject> result;
        Context context;
        int[] imageId;
        private LayoutInflater inflater = null;

        public CustomAdapter(Context context, ArrayList<PoObject> list) {

            result = list;
            selectedList = new ArrayList<Integer>();
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public CustomAdapter() {

        }

        @Override
        public int getCount() {

            return result.size();
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.receipt_childrow, parent, false);
                holder = new ViewHolder();

                holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.list_display_view_container);

                holder.po = (TextView) convertView.findViewById(R.id.po);
                holder.txt_po = (TextView) convertView.findViewById(R.id.txt_po);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }



            PoObject m = result.get(position);
            holder.po.setText(m.getName());

            holder.po.setTypeface(txt_face);
            holder.txt_po.setTypeface(header_face);


            return convertView;
        }
    }

    private class ViewHolder {
        private LinearLayout linearLayout;
        private TextView po, txt_po;

        public ViewHolder() {
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ClearSession();
        Intent intent = new Intent(PartReceiptActivity.this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);

    }


    public class CustomAdapter_processid extends CustomAdapter {
        ArrayList<GetDealerBranch> result;
        Context context;
        int[] imageId;
        private LayoutInflater inflater = null;

        public CustomAdapter_processid(Context context, ArrayList<GetDealerBranch> list) {
            super();
            result = list;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return result.size();
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        public class Holder {
            TextView process_list;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Holder holder = new Holder();
            View rowview_processid;

            rowview_processid = inflater.inflate(R.layout.activity_branch_childrow, null);

            holder.process_list = (TextView) rowview_processid.findViewById(R.id.process_list);

            holder.process_list.setText(result.get(position).getBranch());
            Log.d("branch", "" + result.get(position).getBranch());
            holder.process_list.setTypeface(header_face);

            holder.process_list = (TextView) rowview_processid.findViewById(R.id.process_list);
            holder.process_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    process_list = holder.process_list.getText().toString();
                    branch_no.setText(process_list);
                    branch_name = branch_no.getText().toString();

                    Sessiondata.getInstance().setBranch_get(process_list);

                    dummydata = new ArrayList<PoObject>();
                    adapter = new CustomAdapter(PartReceiptActivity.this, dummydata);
                    po_list.setAdapter(adapter);

                    next_navigation = 0;
                    Epo = 0;

                    for (int ii = 0; ii < branch_name.length(); ii++) {

                        Character character = branch_name.charAt(ii);

                        if (character.toString().equals("-")) {

                            branch_name = branch_name.substring(0,ii);

                            Log.d("branch_trim", "" + branch_name);
                            break;
                        }
                    }


                    Dialog.dismiss();
                }
            });

            return rowview_processid;

        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        sweetalrt = true;
        sweetalrt_delete=true;
        couldStartActivity = true;

        if (Sessiondata.getInstance().getBranch_get().toString().length() == 0) {
            if (Sessiondata.getInstance().getLoginObject() != null) {
                if (Sessiondata.getInstance().getLoginObject().getBranch() != null)
                branch_no.setText(Sessiondata.getInstance().getLoginObject().getBranch());
                branch_name = branch_no.getText().toString();
            }
        } else {
            branch_no.setText(Sessiondata.getInstance().getBranch_get());
            branch_name = branch_no.getText().toString();
        }

        for (int ii = 0; ii < branch_name.length(); ii++) {

            Character character = branch_name.charAt(ii);

            if (character.toString().equals("-")) {

                branch_name = branch_name.substring(0,ii);

                Log.d("branch_trim", "" + branch_name);
                break;
            }
        }


        if (dummydata.size() == 0) {
            next_navigation = 0;
        }

        if (Sessiondata.getInstance().getEPO_Status()) {
            Epo = 1;
        } else {
            Epo = 0;
        }

        if(Sessiondata.getInstance().getPo_value().toString().equalsIgnoreCase("yes")) {
            po_no.setText(Sessiondata.getInstance().getPo_receipt().toString());
        }
        po_no.setText(po_no.getText().toString());
    }


    public class AsyncGetEpoParts extends AsyncTask<Void, Void, Void>{

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(PartReceiptActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken= Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("UserToken",""+userToken);

                //getparts= WebServiceConsumer.getInstance().getPartsdetlsV3(userToken,Sessiondata.getInstance().getPO_Array(),"");
                getparts= WebServiceConsumer.getInstance().getPartsdetlsV4(userToken,Sessiondata.getInstance().getPO_Array(),"");

            } catch (SocketTimeoutException e){
                getparts =null;
            }

            catch (Exception e) {
                getparts =null;
                e.printStackTrace();
            }

            return null;
        }



        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if (getparts != null){

                if(getparts.size()==1){

                    if(getparts.get(0).getMessage().length() !=0){

                        ProgressBar.dismiss();

                        if (getparts.get(0).getMessage().toString().contains("Data Conversion Error")){

                            if(sweet_load) {
                                sweet_load = false;

                                sweet_load_alert=  new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Data Conversion Error")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {

                                                sweet_load=true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (getparts.get(0).getMessage().toString().length() !=0 &&
                                !getparts.get(0).getMessage().toString().contains("Session")){

                                String message = getparts.get(0).getMessage().toString();

                                if(Sweetalrt_list) {
                                    Sweetalrt_list = false;

                                    Sweetalt_list=  new SweetAlertDialog(PartReceiptActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText(message)
                                            .setCancelText("Ok")
                                            .showCancelButton(false)
                                            .setConfirmClickListener(null)
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override

                                                public void onClick(SweetAlertDialog sDialog) {
                                                    Sweetalrt_list=true;

                                                    sDialog.dismiss();
                                                }
                                            });
                                    Sweetalt_list.setCancelable(false);
                                    Sweetalt_list.show();
                                }
                            } else {

                            String Result = getparts.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (getparts.get(0).getMessage().toString().contains("Session")) {
                                Session = 2;
                                if(checkInternetConenction()){

                                    new AsyncSessionLoginTask().execute();

                                }else{
                                    Toast.makeText(PartReceiptActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }

                            }
                            }

                        }else {


                        if (Sessiondata.getInstance().getPartObjects() != null){
                            Sessiondata.getInstance().getPartObjects().clear();
                        }

                        PartsReceivingDetailsActivity.flag_submit = 0;
                        Intent intent = new Intent(PartReceiptActivity.this, PartsReceivingDetailsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                        ProgressBar.dismiss();
                    }

                    }else if(getparts.size()==0){
                    ProgressBar.dismiss();
                }else {

                    if (Sessiondata.getInstance().getPartObjects() != null){
                        Sessiondata.getInstance().getPartObjects().clear();
                    }

                    PartsReceivingDetailsActivity.flag_submit = 0;
                    Intent intent = new Intent(PartReceiptActivity.this, PartsReceivingDetailsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                    ProgressBar.dismiss();
                }
            }else{
                ProgressBar.dismiss();

            }

        }
    }
}