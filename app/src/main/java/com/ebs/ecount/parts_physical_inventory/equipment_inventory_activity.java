package com.ebs.ecount.parts_physical_inventory;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cognex.dataman.sdk.ConnectionState;
import com.cognex.mobile.barcode.sdk.ReadResult;
import com.cognex.mobile.barcode.sdk.ReadResults;
import com.cognex.mobile.barcode.sdk.ReaderDevice;
import com.cognex.mobile.barcode.sdk.ReaderDevice.OnConnectionCompletedListener;
import com.cognex.mobile.barcode.sdk.ReaderDevice.ReaderDeviceListener;
import com.ebs.ecount.R;
import com.ebs.ecount.initial.Dashboard;
import com.ebs.ecount.objects.GetAttachments;
import com.ebs.ecount.objects.GetDealerBranch;
import com.ebs.ecount.objects.GetEquipmentBranch;
import com.ebs.ecount.objects.GetEquipmentList;
import com.ebs.ecount.objects.GetEquipmentProcessid;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.uidesigns.ProgressBar;
import com.ebs.ecount.uidesigns.SimpleScannerActivity;
import com.ebs.ecount.uidesigns.SweetAlertDialog;
import com.ebs.ecount.utils.Sessiondata;
import com.ebs.ecount.webutils.WebServiceConsumer;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by techunity on 21/11/16.
 */
public class equipment_inventory_activity extends AppCompatActivity implements OnConnectionCompletedListener, ReaderDeviceListener {

    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private static final DeviceType[] deviceTypeValues = DeviceType.values();
    public static ReaderDevice readerDevice;
    public static boolean isDevicePicked = false;
    public static boolean dialogAppeared = false;
    public static String selectedDevice = "";
    public static boolean fragmentActive = false;
    Button back, btn_submit, filter_btn, filter_btn_1, refresh, btn_uncounted, btn_counted;
    ImageView process_id, equpiment_id, serial_scanner;
    Dialog mDialogattachlist = null;
    ListView equp_list;
    Dialog mDialoglist = null;
    String unit_ID;
    Dialog mDialognodata = null;
    Boolean sweetalrtsuccess = true;
    ValueAnimator mAnimator, mAnimator_1;
    ArrayList<GetEquipmentProcessid> equip_processid;
    ArrayList<GetAttachments> attachments_list;
    ArrayList<GetEquipmentList> equip_list;
    ArrayList<GetAttachments> attachment;
    GetEquipmentBranch equp_branch;
    GetEquipmentBranch equp_branch_chk;
    String uncountequp, update_equip, SetEquipmentDetails;
    String str_model, str_make, str_serial;
    LinearLayout filter_layout, filter_layout_1;
    ImageView filter_arrow, filter_arrow_1;
    int detach_pos;
    EditText equp_id, model, serial, make;
    Boolean make_model_TAG = false;
    Typeface header_face, txt_face;
    TextView txt_header;
    LoginObject loginObject = null;
    AutoCompleteTextView processid;
    AutoCompleteTextView description;
    String process_ids;
    String user_token, user, branch;
    String transfer_branch;
    String process_list = "";
    String equipid_text = "";
    String model_text = "";
    String model_length_txt = "";
    String serial_text = "";
    int Session = 0;
    String make_text = "";

    String process_id_text = "";
    String description_text = "";
    String sub_processid = "";
    String usertoken, equipid;
    String DetachEquipment;
    String scan_processid;
    String scan_equip;
    String scan_model;
    String scan_make;
    String scan_serial;
    int scanne_value = 0;
    int msg_show = 0;
    // View footer;
    Boolean sweetalrt = true;
    Boolean sweetalrt_notmatch = true;
    Boolean sweetalrt_count = true;
    Boolean sweetalrt_new = true;
    Boolean sweetalrts1 = true, sweetalrts3 = true;
    Boolean sweetdetach = true;
    Boolean sweetbranch = true;
    Boolean sweetnobranch = true;
    Boolean autoCollapse = false;
    Boolean status_check = false;

    SweetAlertDialog sweetalt, dialogmsg, dialogdetach, dialogbranch, dialognobranch, alert_count, alert_chk, sweetalt_list, dialog_notmatch;
    ArrayList<String> selected_attach;
    //    LinearLayout submit_layout;
    String equp_ids, models, makes, serials;
    String equpbranch, loginbranch;
    int call_setEquip_D = 0;
    int return_once = 0;
    AutoCompleteTextView branch_no;
    TextView txt_br;
    ImageView img_branch;
    ArrayList<GetDealerBranch> dealerbranch;
    ArrayList<GetDealerBranch> branchlist;
    Dialog Dialog;
    CustomAdapter_Branch adapter_branch;
    String branch_name = "";
    Boolean Sweetalrt_list = true;
    SweetAlertDialog Sweetalt_list;
    int alert = 0;
    int scannedtype = 0;
    String uncount_equipid, uncount_branch;
    int ucount_processid;
    Drawable grey_drawable, blue_drawable;
    Boolean handleasync = true;
    private CustomAdapter adapter;
    private Class<?> mClss;
    private Context mContext;

    public static DeviceType deviceTypeFromInt(int i) {
        return deviceTypeValues[i];
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equp_inventory);

        header_face = Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");
        txt_face = Typeface.createFromAsset(getAssets(), "fonts/helr45w.ttf");

        mContext = this;

        equip_list = new ArrayList<GetEquipmentList>();

        txt_br = (TextView) findViewById(R.id.txt_branch);
        branch_no = (AutoCompleteTextView) findViewById(R.id.branch_no);
        img_branch = (ImageView) findViewById(R.id.branch_list);

        txt_br.setTypeface(header_face);
        branch_no.setEnabled(false);
        branch_no.setTypeface(txt_face);



        Log.d("GlobalVariables_Main ", "0_UnCounted" + " 1_Counted");
        Log.d("GlobalVariables_Main ", "" + GlobalVariables.showerrormsg + " checkbtn_load " + GlobalVariables.checkbtn_load);

        img_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConenction()) {

                    new AsyncGetDealerBranch().execute();

                } else {

                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });


        back = (Button) findViewById(R.id.back);

        refresh = (Button) findViewById(R.id.refresh);
        btn_counted = (Button) findViewById(R.id.counted);
        btn_uncounted = (Button) findViewById(R.id.uncounted);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        //  btn_handwrite = (Button) findViewById(R.id.hand_writeID);

        selected_attach = new ArrayList<>();
        txt_header = (TextView) findViewById(R.id.txt_header);
        /*footer = findViewById(R.id.view);*/

        process_id = (ImageView) findViewById(R.id.process_id);
        equpiment_id = (ImageView) findViewById(R.id.equpiment_id);
        equp_list = (ListView) findViewById(R.id.equp_list);
        serial_scanner = (ImageView) findViewById(R.id.serial_scanner);

        filter_arrow = (ImageView) findViewById(R.id.filter_arrow);
        filter_arrow_1 = (ImageView) findViewById(R.id.filter_arrow_1);
        filter_btn = (Button) findViewById(R.id.filter_btn);
        filter_btn_1 = (Button) findViewById(R.id.filter_btn_1);
        equp_id = (EditText) findViewById(R.id.equp_id);
        model = (EditText) findViewById(R.id.model);
        serial = (EditText) findViewById(R.id.serial_no);
        make = (EditText) findViewById(R.id.make);

//        submit_layout = (LinearLayout) findViewById(R.id.submit_layout);

        equp_id.setEnabled(true);
        model.setEnabled(true);
        serial.setEnabled(true);
        make.setEnabled(true);
        equpiment_id.setEnabled(true);
        serial_scanner.setEnabled(true);

        processid = (AutoCompleteTextView) findViewById(R.id.processId);
        description = (AutoCompleteTextView) findViewById(R.id.description);


        process_id.setTag("");

        if (process_list.equals(""))
        {
            process_list = Sessiondata.getInstance().getProcess_list();
            Log.d("getProcess_list", process_list);
        }

        final TextView txt_processid = (TextView) findViewById(R.id.txt_processid);
        TextView txt_equipmentid = (TextView) findViewById(R.id.txt_equipmentid);
//        TextView txt_model = (TextView) findViewById(R.id.txt_model);
        TextView txt_serialno = (TextView) findViewById(R.id.txt_serialno);
        TextView txt_make = (TextView) findViewById(R.id.txt_make);
        TextView text_description = (TextView) findViewById(R.id.text_description);

        txt_processid.setTypeface(header_face);
        text_description.setTypeface(header_face);
//        txt_model.setTypeface(header_face);
        txt_equipmentid.setTypeface(header_face);
        txt_serialno.setTypeface(header_face);
        txt_make.setTypeface(header_face);
        btn_submit.setTypeface(header_face);
        txt_header.setTypeface(header_face);
        filter_btn.setTypeface(header_face);
        filter_btn_1.setTypeface(header_face);
        back.setTypeface(txt_face);
        processid.setTypeface(txt_face);
        equp_id.setTypeface(txt_face);
        make.setTypeface(txt_face);
        model.setTypeface(txt_face);
        serial.setTypeface(txt_face);

        refresh.setTypeface(header_face);
        btn_counted.setTypeface(header_face);
        btn_uncounted.setTypeface(header_face);
        // btn_handwrite.setTypeface(header_face);

        grey_drawable = ContextCompat.getDrawable(mContext, R.drawable.round_grey_corner_label).mutate();
        blue_drawable = ContextCompat.getDrawable(mContext, R.drawable.round_corner_label).mutate();

        processid.setEnabled(true);
        btn_submit.setEnabled(true);
//        submit_layout.setEnabled(false);

        btn_submit.setVisibility(View.VISIBLE);
//        footer.setVisibility(View.GONE);
/*
        submit_layout.setVisibility(View.GONE);
*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btn_counted.setBackground(grey_drawable);
            btn_uncounted.setBackground(grey_drawable);
            btn_counted.setTextColor(Color.BLACK);
            btn_uncounted.setTextColor(Color.BLACK);
        } else {
            btn_counted.setBackgroundDrawable(grey_drawable);
            btn_uncounted.setBackgroundDrawable(grey_drawable);
            btn_counted.setTextColor(Color.BLACK);
            btn_uncounted.setTextColor(Color.BLACK);
        }

        Bundle equpid = getIntent().getExtras();
        if (equpid != null) {
            String equp_value = equpid.getString("value1");
            if (Sessiondata.getInstance().getInventory_scanner().toString().equalsIgnoreCase("start")) {
                scannedtype = 2;
                handleasync = true;
                Log.d("OnReceive", "equp_id_scannedtype" + scannedtype);
                alert = 1;
                Sessiondata.getInstance().setUnitId(equp_value);
            }
        }

        Bundle bundleendbin = getIntent().getExtras();
        if (bundleendbin != null) {
            String endbin = bundleendbin.getString("value2");
            if (Sessiondata.getInstance().getInventory_scanner().toString().equalsIgnoreCase("end")) {
                scannedtype = 2;
                handleasync = true;
                Log.d("OnReceive", "serial_scannedtype" + scannedtype);
                Sessiondata.getInstance().setSerial(endbin);
            }
        }

        Bundle bundlemodel = getIntent().getExtras();
        if (bundlemodel != null) {
            String model = bundlemodel.getString("value3");
            if (Sessiondata.getInstance().getInventory_scanner().toString().equalsIgnoreCase("model")) {
                scannedtype = 2;
                handleasync = true;
                Log.d("OnReceive", "model_scannedtype" + scannedtype);
                Sessiondata.getInstance().setModel(model);
            }
        }

        Bundle bundlemake = getIntent().getExtras();
        if (bundlemake != null) {
            String value4 = bundlemake.getString("value4");
            if (Sessiondata.getInstance().getInventory_scanner().toString().equalsIgnoreCase("make")) {
                scannedtype = 2;
                handleasync = true;
                Log.d("OnReceive", "make_scannedtype" + scannedtype);

                String[] make_model = new String[0];

                if (value4 != null && value4.length() != 0) {
                    make_model = bundlemake.getString("value4").split("-");
                }

                if (make_model != null && make_model.length > 0) {
                    Log.d("make_model", "===" + make_model[0] + "-" + make_model[1]);
                    Sessiondata.getInstance().setMake(make_model[0]);
                    Sessiondata.getInstance().setModel(make_model[1]);
                    Sessiondata.getInstance().setMake_model(make_model[0] + "-" + make_model[1]);
                }
            }
        }


        Sessiondata.getInstance().getWalkaroundgeneralimages().clear();
        Sessiondata.getInstance().getAttachedFilesData().clear();

        Sessiondata.getInstance().setEqup_scanner(0);
        Sessiondata.getInstance().setScanner_inventory(6);
        Sessiondata.getInstance().setInventory_scanner("start");


        processid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scannedtype = 1;
                handleasync = true;
                Log.d("OnTouch", "processId_scannedtype" + scannedtype);

                Log.d("processId", "select");
                return false;
            }

        });

        equp_id.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scannedtype = 1;
                handleasync = true;
                Log.d("OnTouch", "equp_id_scannedtype :" + scannedtype);
                alert = 0;
                Log.d("equp_id", "select");
                Sessiondata.getInstance().setEqup_scanner(0);
                Sessiondata.getInstance().setScanner_inventory(6);
                Sessiondata.getInstance().setInventory_scanner("start");
                return false;
            }

        });
        model.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scannedtype = 1;
                handleasync = true;
                Log.d("OnTouch", "model_scannedtype" + scannedtype);

                Log.d("model", "select");
                Sessiondata.getInstance().setEqup_scanner(2);
                Sessiondata.getInstance().setScanner_inventory(8);
                Sessiondata.getInstance().setInventory_scanner("model");
                return false;
            }

        });
        make.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scannedtype = 1;
                handleasync = true;
                Log.d("OnTouch", "make_scannedtype" + scannedtype);

                Log.d("make", "select");
                Sessiondata.getInstance().setEqup_scanner(1);
                Sessiondata.getInstance().setScanner_inventory(9);
                Sessiondata.getInstance().setInventory_scanner("make");
                return false;
            }

        });
        serial.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scannedtype = 1;
                handleasync = true;
                Log.d("OnTouch", "serial_scannedtype" + scannedtype);

//                Log.d("serial", "select");
//                Sessiondata.getInstance().setEqup_scanner(3);
//                Sessiondata.getInstance().setScanner_inventory(7);
//                Sessiondata.getInstance().setInventory_scanner("end");
                return false;
            }

        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                equp_id.setText("");
                model.setText("");
                make.setText("");
                serial.setText("");
                model_length_txt = "";
                make_text = "";

                equp_id.requestFocus();

                handleasync = true;

                String branch =  branch_no.getText().toString();

                if (branch != null)
                {
                    if (branch.isEmpty() || branch.equals(""))
                    {
                        process_list = "";
                    }

                }

                Sessiondata.getInstance().setUnitId("");
                Sessiondata.getInstance().setModel("");
                Sessiondata.getInstance().setMake("");
                Sessiondata.getInstance().setSerial("");
                Sessiondata.getInstance().setMake_model("");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btn_counted.setBackground(grey_drawable);
                    btn_uncounted.setBackground(grey_drawable);
                    btn_counted.setTextColor(Color.BLACK);
                    btn_uncounted.setTextColor(Color.BLACK);
                } else {
                    btn_counted.setBackgroundDrawable(grey_drawable);
                    btn_uncounted.setBackgroundDrawable(grey_drawable);
                    btn_counted.setTextColor(Color.BLACK);
                    btn_uncounted.setTextColor(Color.BLACK);
                }
            }
        });

      /*  btn_handwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String handwrite_proc = split_string(process_list);

                if (handwrite_proc.equalsIgnoreCase("")) {

                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the PID")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        Sweetalrt_list = true;
                                        sDialog.dismiss();
                                    }
                                });
                        Sweetalt_list.setCancelable(false);
                        Sweetalt_list.show();
                    }
                }
            }
        });*/

        btn_counted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equi_proc = split_string(process_list);


                if (equi_proc.toString().equalsIgnoreCase("")) {
                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the PID")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        Sweetalrt_list = true;
                                        sDialog.dismiss();
                                    }
                                });

                        Sweetalt_list.setCancelable(false);
                        Sweetalt_list.show();
                    }
                } else {
                    if (checkInternetConenction()) {

                        alert = 0;

                        GlobalVariables.checkbtn_load = 1;

                        process_ids = split_string(process_list);
                        equipid_text = equp_id.getText().toString();
                        model_text = model.getText().toString();
                        make_text = make.getText().toString();
                        serial_text = serial.getText().toString();

                        equip_list = new ArrayList<GetEquipmentList>();

                        handleasync = true;

                        new AsyncGetEquipmentListCounted().execute();

                    } else {

                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_uncounted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String equi_proc = split_string(process_list);

                if (equi_proc.toString().equalsIgnoreCase("")) {
                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the PID")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        Sweetalrt_list = true;
                                        sDialog.dismiss();
                                    }
                                });
                        Sweetalt_list.setCancelable(false);
                        Sweetalt_list.show();
                    }
                } else {
                    if (checkInternetConenction()) {

                        alert = 0;

                        handleasync = true;
                        GlobalVariables.checkbtn_load = 0;

                        process_ids = split_string(process_list);
                        equipid_text = equp_id.getText().toString();
                        model_text = model.getText().toString();
                        make_text = make.getText().toString();
                        serial_text = serial.getText().toString();

                        equip_list = new ArrayList<GetEquipmentList>();

                        new AsyncGetEquipmentListUnCounted().execute();

                    } else {

                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                process_ids = split_string(process_list);
                equp_ids = equp_id.getText().toString();
                models = model.getText().toString();
                makes = make.getText().toString();
                serials = serial.getText().toString();


                if (process_ids.equalsIgnoreCase("")) {
                    if (sweetalrt) {

                        sweetalrt = false;

                        sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the PID")
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

                } else {

//                    scan_processid = split_string(process_list);
                    scan_processid = processid.getText().toString();
                    scan_model = model.getText().toString();
                    scan_make = make.getText().toString();
                    scan_serial = serial.getText().toString();
                    scan_equip = equp_id.getText().toString();


                    Sessiondata.getInstance().setMake_model(scan_make);

                    String[] make_model = scan_make.split("-");

                    if (make_model != null) {
                        scan_make = make_model[0];

                        if (make_model.length > 1) {
                            scan_model = make_model[1];
                        }

                    }

                    Sessiondata.getInstance().setUnitId(scan_equip);
                    Sessiondata.getInstance().setMake(scan_make);
                    Sessiondata.getInstance().setModel(scan_model);
                    Sessiondata.getInstance().setSerial(scan_serial);
                    Sessiondata.getInstance().setSub_process(scan_processid);


                    Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());

                    sub_processid = split_string(process_list);
                    int processId = Integer.parseInt(sub_processid);
                    Sessiondata.getInstance().setProcessId(processId);
                    Sessiondata.getInstance().setInventory_equip_id(1);

                    Sessiondata.getInstance().setTemp_unitId(scan_equip);
                    Sessiondata.getInstance().setTemp_make(scan_make);
                    Sessiondata.getInstance().setTemp_model(scan_model);
                    Sessiondata.getInstance().setTemp_serial(scan_serial);

                    Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                }
            }
        });

        equpiment_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanne_value = 1;

//                scan_processid = split_string(process_list);
                scan_processid = processid.getText().toString();
                scan_model = model.getText().toString();
                scan_make = make.getText().toString();
                scan_serial = serial.getText().toString();
                scan_equip = equp_id.getText().toString();

                String[] make_model = scan_make.split("-");

                String temp_make = null;

                if (make_model != null && make_model.length != 0) {
                    temp_make = make_model[0];
                    if (make_model.length > 1)
                        scan_model = make_model[1];
                }


                Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                Sessiondata.getInstance().setModel(scan_model);
                Sessiondata.getInstance().setSerial(scan_serial);
                Sessiondata.getInstance().setMake(temp_make);
                Sessiondata.getInstance().setSub_process(scan_processid);
                Sessiondata.getInstance().setUnitId(scan_equip);
                Sessiondata.getInstance().setMake_model(scan_make);


                Sessiondata.getInstance().setInventory_equip_id(1);
                Sessiondata.getInstance().setScanner_partreceipt(0);
                Sessiondata.getInstance().setScanner_partreceiving(0);
                Sessiondata.getInstance().setScanner_replace(0);
                Sessiondata.getInstance().setScanner_counting1(0);
                Sessiondata.getInstance().setScanner_counting2(0);
                Sessiondata.getInstance().setScanner_partnumber(0);
                Sessiondata.getInstance().setScanner_hwstartbin(0);
                Sessiondata.getInstance().setScanner_hwendbin(0);

                launchActivity(SimpleScannerActivity.class);
            }
        });

        serial_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                scannedtype = 1;
                handleasync = true;
                Log.d("OnTouch", "serial_scannedtype" + scannedtype);

                Log.d("serial", "select");
                Sessiondata.getInstance().setEqup_scanner(3);
                Sessiondata.getInstance().setScanner_inventory(7);
                Sessiondata.getInstance().setInventory_scanner("end");

//                scan_processid = split_string(process_list);
                scan_processid = processid.getText().toString();

//                scan_model = model.getText().toString();
                scan_make = make.getText().toString();
                scan_serial = serial.getText().toString();
                scan_equip = equp_id.getText().toString();


                String[] make_model = scan_make.split("-");

                String temp_make = null;

                if (make_model != null && make_model.length != 0) {
                    temp_make = make_model[0];
                    if (make_model.length > 1)
                        scan_model = make_model[1];
                }


                Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                Sessiondata.getInstance().setModel(scan_model);
                Sessiondata.getInstance().setSerial(scan_serial);
                Sessiondata.getInstance().setMake(temp_make);
                Sessiondata.getInstance().setSub_process(scan_processid);
                Sessiondata.getInstance().setMake_model(scan_make);


                Sessiondata.getInstance().setInventory_equip_id(1);

                Sessiondata.getInstance().setScanner_partreceipt(0);
                Sessiondata.getInstance().setScanner_partreceiving(0);
                Sessiondata.getInstance().setScanner_replace(0);
                Sessiondata.getInstance().setScanner_counting1(0);
                Sessiondata.getInstance().setScanner_counting2(0);

                Sessiondata.getInstance().setScanner_partnumber(0);
                Sessiondata.getInstance().setScanner_hwstartbin(0);
                Sessiondata.getInstance().setScanner_hwendbin(0);

                launchActivity(SimpleScannerActivity.class);
            }


        });
//        if(!selectedDevice.equals("MX Scanner")){

        equp_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                // if (!selectedDevice.equals("MX Scanner")) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        equipid_text = s.toString();

                        String ss = s.toString();
                        if (!ss.equals(ss.toUpperCase())) {
                            ss = ss.toUpperCase();
                            equp_id.setText(ss);
                        }
                        equp_id.setSelection(equp_id.getText().length());

                        String equi_proc = split_string(process_list);

                        if (equi_proc.toString().equalsIgnoreCase("")) {

                        } else {
                            if (checkInternetConenction()) {

                                equip_list = new ArrayList<GetEquipmentList>();

                                if (GlobalVariables.checkbtn_load == 0) {
                                    Log.d("UncountFilter", "true");
                                    GlobalVariables.showerrormsg = true;
                                    if (handleasync == true) {
                                        handleasync = false;
                                        new AsyncGetEquipmentListUncountFilter().execute();
                                    }
                                } else {
                                    Log.d("CountFilter", "true");
                                    GlobalVariables.showerrormsg = true;
                                    if (handleasync == true) {
                                        handleasync = false;
                                        new AsyncGetEquipmentListCountedFilter().execute();
                                    }
                                }

                            } else {

                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, 250);

            }
            // }
        });


        model.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        model_text = s.toString();
                        String model_proc = split_string(process_list);

                        if (model_proc.toString().equalsIgnoreCase("")) {

                        } else {

                            if (checkInternetConenction()) {

                                equip_list = new ArrayList<GetEquipmentList>();

                                if (GlobalVariables.checkbtn_load == 0) {
                                    Log.d("UncountFilter", "true");
                                    GlobalVariables.showerrormsg = true;
                                    if (handleasync == true) {
                                        handleasync = false;
                                        new AsyncGetEquipmentListUncountFilter().execute();
                                    }

                                } else {
                                    Log.d("CountFilter", "true");
                                    GlobalVariables.showerrormsg = true;
                                    if (handleasync == true) {
                                        handleasync = false;
                                        new AsyncGetEquipmentListCountedFilter().execute();
                                    }

                                }

                            } else {

                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, 750);
            }


            @Override
            public void afterTextChanged(Editable s) {

                String ss = s.toString();
                if (!ss.equals(ss.toUpperCase())) {
                    ss = ss.toUpperCase();
                    model.setText(ss);
                }
                model.setSelection(model.getText().length());

            }
        });

        serial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        serial_text = s.toString();
                        String serial_proc = split_string(process_list);

                        if (serial_proc.toString().equalsIgnoreCase("")) {

                        } else {


                            if (checkInternetConenction()) {

                                equip_list = new ArrayList<GetEquipmentList>();

                                if (GlobalVariables.checkbtn_load == 0) {
                                    Log.d("UncountFilter", "true");
                                    GlobalVariables.showerrormsg = true;
                                    if (handleasync == true) {
                                        handleasync = false;
                                        new AsyncGetEquipmentListUncountFilter().execute();
                                    }
                                } else {
                                    Log.d("CountFilter", "true");
                                    GlobalVariables.showerrormsg = true;
                                    if (handleasync == true) {
                                        handleasync = false;
                                        new AsyncGetEquipmentListCountedFilter().execute();
                                    }
                                }

                            } else {

                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }, 750);


            }

            @Override
            public void afterTextChanged(Editable s) {

                String ss = s.toString();
                if (!ss.equals(ss.toUpperCase())) {
                    ss = ss.toUpperCase();
                    serial.setText(ss);
                }
                serial.setSelection(serial.getText().length());
            }
        });

//        make.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        make.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        make_text = s.toString();
                        String make_proc = split_string(process_list);
                        String temp_make_text = s.toString();


                        if (make_proc.equalsIgnoreCase("")) {

                        } else {
                            if (!temp_make_text.contains("-")) {
                                if (temp_make_text.length() == 3) {

                                    temp_make_text = temp_make_text + "-";
                                    make.setText(temp_make_text);
                                    make.setSelection(make.getText().length());

                                } else {
                                    make_text = temp_make_text;
                                }
                            }


                            if (temp_make_text.contains("-")) {
                                String[] make_model = temp_make_text.split("-");


                                if (make_model != null && make_model.length != 0) {
                                    make_text = make_model[0];
                                    if (make_model.length > 1)
                                        model_text = make_model[1];

                                    if (model_text.length() == 12) {
                                        model_length_txt = make_text + "-" + model_text;
                                        Log.d("model_text", "Success");
                                    } else if (model_text.length() >= 12) {
                                        make.setText(model_length_txt);
                                    } else if (model_text.length() <= 12) {
                                        model_length_txt = make.getText().toString();
                                    }


                                }

                            }

                            if (checkInternetConenction()) {

                                equip_list = new ArrayList<GetEquipmentList>();

                                if (GlobalVariables.checkbtn_load == 0) {
                                    Log.d("UncountFilter", "true");
                                    GlobalVariables.showerrormsg = true;
                                    if (handleasync) {
                                        handleasync = false;
                                        new AsyncGetEquipmentListUncountFilter().execute();
                                    }
                                } else {
                                    Log.d("CountFilter", "true");
                                    GlobalVariables.showerrormsg = true;
                                    if (handleasync) {
                                        handleasync = false;
                                        new AsyncGetEquipmentListCountedFilter().execute();
                                    }
                                }

                            } else {

                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, 750);


            }

            @Override
            public void afterTextChanged(Editable s) {


//                make.setText(make_text);

                String ss = s.toString();
                if (!ss.equals(ss.toUpperCase())) {
                    ss = ss.toUpperCase();
                    make.setText(ss);
                }
                make.setSelection(make.getText().length());
            }
        });

        processid.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(final Editable s) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        process_id_text = s.toString();

                        String proce_proc = String.valueOf(split_string(process_list));

                        String[] s1 = proce_proc.split("-");


//                        process_ids = proce_proc;
                        process_ids = s1[0].trim();

                        if (proce_proc.toString().isEmpty()) {

                            btn_submit.setEnabled(true);
                         /*   submit_layout.setEnabled(false);
                            submit_layout.setVisibility(View.GONE);*/
                            btn_submit.setVisibility(View.VISIBLE);

                            /*footer.setVisibility(View.GONE);*/

                            equp_id.setText("");
                            model.setText("");
                            serial.setText("");
                            make.setText("");

                            Sessiondata.getInstance().setSub_process("");
                            Sessiondata.getInstance().setMake("");
                            Sessiondata.getInstance().setSerial("");
                            Sessiondata.getInstance().setModel("");
                            Sessiondata.getInstance().setUnitId("");

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                btn_counted.setBackground(grey_drawable);
                                btn_uncounted.setBackground(grey_drawable);
                                btn_counted.setTextColor(Color.BLACK);
                                btn_uncounted.setTextColor(Color.BLACK);
                            } else {
                                btn_counted.setBackgroundDrawable(grey_drawable);
                                btn_uncounted.setBackgroundDrawable(grey_drawable);
                                btn_counted.setTextColor(Color.BLACK);
                                btn_uncounted.setTextColor(Color.BLACK);
                            }

                            equip_list = new ArrayList<GetEquipmentList>();
                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);

                        } else {

                            if (checkInternetConenction()) {

                                equip_list = new ArrayList<GetEquipmentList>();

                                if (GlobalVariables.checkbtn_load == 0) {
                                    Log.d("UncountFilter", "true");
                                    GlobalVariables.showerrormsg = true;
                                    if (handleasync) {
                                        handleasync = false;
                                        new AsyncGetEquipmentListUncountFilter().execute();
                                    }
                                } else {
                                    Log.d("CountFilter", "true");
                                    GlobalVariables.showerrormsg = true;
                                    if (handleasync) {
                                        handleasync = false;
                                        new AsyncGetEquipmentListCountedFilter().execute();
                                    }
                                }

                            } else {

                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    }


                }, 750);


            }
        });


        equp_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (equip_list != null && equip_list.size() != 0) {

                    detach_pos = position;

                    Log.d("status_clk", "" + equip_list.get(position).getStatus().toString());

                    String equpstatuss = equip_list.get(position).getEqpstatus();

                    String status = equip_list.get(position).getStatus();
                    String Eqptblstatus = equip_list.get(position).getEqptblstatus();
                    String pid = split_string(process_list);

                    Log.d("equpstatus_clk", "" + equpstatuss);

                    equpbranch = equip_list.get(position).getBranch();
                    loginbranch = branch_no.getText().toString();

                    for (int ii = 0; ii < loginbranch.length(); ii++) {

                        Character character = loginbranch.charAt(ii);

                        if (character.toString().equals("-")) {

                            loginbranch = loginbranch.substring(0, ii);

                            Log.d("branch_trim", "" + loginbranch);
                            break;
                        }
                    }

                    Log.d("equpbranch_clk", "" + equpbranch);
                    Log.d("loginbranch_clk", "" + loginbranch);



//                    {
                    if (equpstatuss.contains("Counted")) {
                        final String equip = equip_list.get(position).getEquipid();

                        if (!Eqptblstatus.equals("") && !Eqptblstatus.contains(status)) {


                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                    .setTitleText("Alert!")
                                    .setContentText("Status not match for this Process ID - " + process_list)
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(null)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismiss();

                                            if (sweetalrt_new) {
                                                sweetalrt_new = false;

                                                if (scannedtype == 2) {
                                                    alert_chk = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                            .setTitleText("Alert!")
                                                            .setContentText("Equipment is Already Scanned!")
                                                            .setCancelText("Ok")
                                                            .setConfirmText("UnCount")
                                                            .showCancelButton(true)
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                    sweetalrt_new = true;
                                                                    sweetAlertDialog.dismiss();

                                                                    if (checkInternetConenction()) {

                                                                        uncount_equipid = equip_list.get(position).getEquipid();
                                                                        uncount_branch = branch_no.getText().toString();
                                                                        ucount_processid = Integer.parseInt(split_string(process_list));

                                                                        new AsyncUnCountEquipment().execute();


                                                                    } else {

                                                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                    }


                                                                }
                                                            })
                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override

                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    sweetalrt_new = true;
                                                                    sDialog.dismiss();
                                                                }
                                                            });
                                                    alert_chk.setCancelable(false);
                                                    alert_chk.show();

                                                } else {
                                                    alert_chk = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                            .setTitleText("Alert!")
                                                            .setContentText("Do you want to uncount this equipment " + equip + " ?")
                                                            .setCancelText("No")
                                                            .setConfirmText("Yes")
                                                            .showCancelButton(true)
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                    sweetalrt_new = true;
                                                                    sweetAlertDialog.dismiss();

                                                                    if (checkInternetConenction()) {

                                                                        uncount_equipid = equip_list.get(position).getEquipid();
                                                                        uncount_branch = branch_no.getText().toString();
                                                                        ucount_processid = Integer.parseInt(split_string(process_list));

                                                                        new AsyncUnCountEquipment().execute();


                                                                    } else {

                                                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                    }


                                                                }
                                                            })
                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override

                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    sweetalrt_new = true;
                                                                    sDialog.dismiss();
                                                                }
                                                            });
                                                    alert_chk.setCancelable(false);
                                                    alert_chk.show();

                                                }

                                            }

                                        }
                                    });
                            sweetAlertDialog.setCancelable(false);
                            sweetAlertDialog.show();
                        }
                        else
                        {
                            if (sweetalrt_new) {
                                sweetalrt_new = false;

                                if (scannedtype == 2) {
                                    alert_chk = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText("Equipment is Already Scanned!")
                                            .setCancelText("Ok")
                                            .setConfirmText("UnCount")
                                            .showCancelButton(true)
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                    sweetalrt_new = true;
                                                    sweetAlertDialog.dismiss();

                                                    if (checkInternetConenction()) {

                                                        uncount_equipid = equip_list.get(position).getEquipid();
                                                        uncount_branch = branch_no.getText().toString();
                                                        ucount_processid = Integer.parseInt(split_string(process_list));

                                                        new AsyncUnCountEquipment().execute();


                                                    } else {

                                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                    }


                                                }
                                            })
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override

                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sweetalrt_new = true;
                                                    sDialog.dismiss();
                                                }
                                            });
                                    alert_chk.setCancelable(false);
                                    alert_chk.show();

                                } else {
                                    alert_chk = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText("Do you want to uncount this equipment " + equip + " ?")
                                            .setCancelText("No")
                                            .setConfirmText("Yes")
                                            .showCancelButton(true)
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                    sweetalrt_new = true;
                                                    sweetAlertDialog.dismiss();

                                                    if (checkInternetConenction()) {

                                                        uncount_equipid = equip_list.get(position).getEquipid();
                                                        uncount_branch = branch_no.getText().toString();
                                                        ucount_processid = Integer.parseInt(split_string(process_list));

                                                        new AsyncUnCountEquipment().execute();


                                                    } else {

                                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                    }


                                                }
                                            })
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override

                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sweetalrt_new = true;
                                                    sDialog.dismiss();
                                                }
                                            });
                                    alert_chk.setCancelable(false);
                                    alert_chk.show();

                                }

                            }

                        }


                    } else {
                        int Scaned_Type = equip_list.get(position).getScannedtype();
                        Log.d("Scaned_Type", "" + equip_list.get(position).getScannedtype());

                        if (Scaned_Type == 2) {
                            if (Sweetalrt_list) {
                                Sweetalrt_list = false;

                                Sweetalt_list = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Alert!")
                                        .setContentText("Equipment is Already Scanned!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                Sweetalrt_list = true;
                                                sDialog.dismiss();
                                            }
                                        });
                                Sweetalt_list.setCancelable(false);
                                Sweetalt_list.show();
                            }

                        } else if (Scaned_Type == 1) {
                            if (Sweetalrt_list) {
                                Sweetalrt_list = false;

                                Sweetalt_list = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Alert!")
                                        .setContentText("Equipment is Already Entered!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                Sweetalrt_list = true;
                                                sDialog.dismiss();
                                            }
                                        });
                                Sweetalt_list.setCancelable(false);
                                Sweetalt_list.show();
                            }
                        } else {
                            Log.d("status_clk", "" + equip_list.get(position).getStatus().toString());

                            String equpstatus = equip_list.get(position).getEqpstatus();
                            Log.d("equpstatus_clk", "" + equpstatus);

                            equpbranch = equip_list.get(position).getBranch();
                            loginbranch = branch_no.getText().toString();

                            for (int ii = 0; ii < loginbranch.length(); ii++) {

                                Character character = loginbranch.charAt(ii);

                                if (character.toString().equals("-")) {

                                    loginbranch = loginbranch.substring(0, ii);

                                    Log.d("branch_trim", "" + loginbranch);
                                    break;
                                }
                            }

                            Log.d("equpbranch_clk", "" + equpbranch);
                            Log.d("loginbranch_clk", "" + loginbranch);

                            if (equip_list.get(position).getStatus().toString().contains("AV") || equip_list.get(position).getStatus().toString().contains("av") ||
                                    equip_list.get(position).getStatus().toString().contains("MA") || equip_list.get(position).getStatus().toString().contains("ma") ||
                                    equip_list.get(position).getStatus().toString().contains("RE") || equip_list.get(position).getStatus().toString().contains("re")) {

                                unit_ID = equip_list.get(position).getEquipid();
                                str_model = equip_list.get(position).getModel();
                                str_make = equip_list.get(position).getMfg();
                                str_serial = equip_list.get(position).getSerialno();

                                Log.d("unit_ID", "" + unit_ID);

                                if (loginbranch.toString().equalsIgnoreCase(equpbranch)) {

                                    call_setEquip_D = 1;

                                    if (equpstatus.toString().contains("From Equip")) {

                                        if (sweetalrt_new) {
                                            sweetalrt_new = false;

                                            alert_chk = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Equipment is not on PID, Would you like to add to PID and Count?")
                                                    .setCancelText("No")
                                                    .setConfirmText("Yes")
                                                    .showCancelButton(true)
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                            sweetalrt_new = true;
                                                            sweetAlertDialog.dismiss();

                                                            if (checkInternetConenction()) {

                                                                new AsyncUpdateEquipment().execute();


                                                            } else {

                                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                            }


                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_new = true;
                                                            sDialog.dismiss();
                                                        }
                                                    });
                                            alert_chk.setCancelable(false);
                                            alert_chk.show();
                                        }

                                    } else if (equpstatus.contains("From Count")) {

                                        if (!Eqptblstatus.equals("") && !Eqptblstatus.contains(status)) {

                                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Status not match for this Process ID - " + process_list )
                                                    .setCancelText("Ok")
                                                    .showCancelButton(false)
                                                    .setConfirmClickListener(null)
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismiss();

                                                            if (checkInternetConenction()) {

                                                                detach_pos = position;
                                                                new AsyncGetAttachments_new().execute();

                                                            } else {

                                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                            sweetAlertDialog.setCancelable(false);
                                            sweetAlertDialog.show();
                                        }
                                        else
                                        {
                                            if (checkInternetConenction()) {

                                                detach_pos = position;
                                                new AsyncGetAttachments_new().execute();

                                            } else {
                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }
                                        }


                                    }
                                } else {

                                    call_setEquip_D = 0;

                                   if (equpstatus.toString().contains("From Equip")) {

                                        if (sweetbranch) {
                                            sweetbranch = false;
                                            dialogbranch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                    .setTitleText("Message!")
                                                    .setContentText("Equipment found in branch location " + equpbranch + ". Do you want to transfer to branch " + loginbranch + " ?")
                                                    .setCancelText("No")
                                                    .setConfirmText("Yes")
                                                    .showCancelButton(true)

                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetbranch = true;
                                                            sDialog.dismiss();
                                                        }
                                                    })
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetbranch = true;
                                                            Sessiondata.getInstance().setTransfer_Equipment("T");

                                                            if (checkInternetConenction()) {

                                                                msg_show = 2;
                                                                new AsyncUpdateEquipment_transfer().execute();
                                                                sDialog.dismiss();

                                                            } else {

                                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                            dialogbranch.setCancelable(false);
                                            dialogbranch.show();
                                        }

                                    } else if (equpstatus.toString().contains("From Count")) {

                                        if (!Eqptblstatus.equals("") && !Eqptblstatus.contains(status)) {

                                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Status not match for this Process ID - " + process_list)
                                                    .setCancelText("Ok")
                                                    .showCancelButton(false)
                                                    .setConfirmClickListener(null)
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismiss();

                                                            if (sweetbranch) {
                                                                sweetbranch = false;
                                                                dialogbranch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                        .setTitleText("Message!")
                                                                        .setContentText("Equipment found in branch location " + equpbranch + ". Do you want to transfer to branch " + loginbranch + " ?")
                                                                        .setCancelText("No")
                                                                        .setConfirmText("Yes")
                                                                        .showCancelButton(true)

                                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sweetbranch = true;
                                                                                sDialog.dismiss();
                                                                            }
                                                                        })
                                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sweetbranch = true;
                                                                                Sessiondata.getInstance().setTransfer_Equipment("T");

                                                                                if (checkInternetConenction()) {

                                                                                    msg_show = 2;
                                                                                    new AsyncUpdateEquipment_transfer().execute();
                                                                                    sDialog.dismiss();

                                                                                } else {

                                                                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }
                                                                        });
                                                                dialogbranch.setCancelable(false);
                                                                dialogbranch.show();
                                                            }

                                                        }
                                                    });
                                            sweetAlertDialog.setCancelable(false);
                                            sweetAlertDialog.show();
                                        }
                                        else
                                        {
                                            if (sweetbranch) {
                                                sweetbranch = false;
                                                dialogbranch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                        .setTitleText("Message!")
                                                        .setContentText("Equipment found in branch location " + equpbranch + ". Do you want to transfer to branch " + loginbranch + " ?")
                                                        .setCancelText("No")
                                                        .setConfirmText("Yes")
                                                        .showCancelButton(true)

                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sweetbranch = true;
                                                                sDialog.dismiss();
                                                            }
                                                        })
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sweetbranch = true;
                                                                Sessiondata.getInstance().setTransfer_Equipment("T");

                                                                if (checkInternetConenction()) {

                                                                    msg_show = 2;

                                                                    new AsyncUpdateEquipment_transfer().execute();
                                                                    sDialog.dismiss();

                                                                } else {

                                                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                                dialogbranch.setCancelable(false);
                                                dialogbranch.show();
                                            }

                                        }

                                    }
                                }
                                Log.d("call_setEquip_D(AV,MA,RE)", "" + call_setEquip_D);
                            } else {

                                if (equip_list.get(position).getStatus().toString().contains("ON") || equip_list.get(position).getStatus().toString().contains("on")) {

                                    unit_ID = equip_list.get(position).getEquipid();
                                    detach_pos = position;

                                    if (loginbranch.toString().equalsIgnoreCase(equpbranch)) {
                                        call_setEquip_D = 1;
                                    } else {
                                        call_setEquip_D = 0;
                                    }

                                    Log.d("call_setEquip_D(ON)", "" + call_setEquip_D);

                                    if (equpstatus.toString().contains("From Equip")) {
                                        if (sweetalrt_new) {
                                            sweetalrt_new = false;

                                            alert_chk = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Equipment is not on PID, Would you like to add to PID and Count?")
                                                    .setCancelText("No")
                                                    .setConfirmText("Yes")
                                                    .showCancelButton(true)
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                            sweetalrt_new = true;
                                                            sweetAlertDialog.dismiss();

                                                            if (checkInternetConenction()) {

                                                                new AsyncUpdateEquipment().execute();


                                                            } else {

                                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                            }


                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_new = true;
                                                            sDialog.dismiss();
                                                        }
                                                    });
                                            alert_chk.setCancelable(false);
                                            alert_chk.show();
                                        }
                                    } else if (equpstatus.toString().contains("From Count")) {



                                        if (!Eqptblstatus.equals("") && !Eqptblstatus.contains(status)) {


                                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Status not match for this Process ID - " + process_list)
                                                    .setCancelText("Ok")
                                                    .showCancelButton(false)
                                                    .setConfirmClickListener(null)
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismiss();
                                                            if (sweetalrt) {
                                                                sweetalrt = false;

                                                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                                        .setTitleText("Alert!")
                                                                        .setContentText("Equipment is on Rent!")
                                                                        .setCancelText("Ok")
                                                                        .showCancelButton(false)
                                                                        .setConfirmClickListener(null)
                                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override

                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sweetalrt = true;
                                                                                sDialog.dismiss();

                                                                                if (sweetalrt_count) {
                                                                                    sweetalrt_count = false;

                                                                                    alert_count = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                                            .setTitleText("Info!")
                                                                                            .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                                                                            .setCancelText("No")
                                                                                            .setConfirmText("Yes")
                                                                                            .showCancelButton(true)
                                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                @Override
                                                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                                                    sweetalrt_count = true;
                                                                                                    sweetAlertDialog.dismiss();

                                                                                                    if (checkInternetConenction()) {

                                                                                                        new AsyncUpdateEquipment().execute();


                                                                                                    } else {

                                                                                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                                                    }


                                                                                                }
                                                                                            })
                                                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                @Override

                                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                                    sweetalrt_count = true;
                                                                                                    sDialog.dismiss();
                                                                                                }
                                                                                            });
                                                                                    alert_count.setCancelable(false);
                                                                                    alert_count.show();
                                                                                }
                                                                            }
                                                                        });
                                                                sweetalt.setCancelable(false);
                                                                sweetalt.show();
                                                            }

                                                        }
                                                    });
                                            sweetAlertDialog.setCancelable(false);
                                            sweetAlertDialog.show();
                                        }
                                        else
                                        {
                                            if (sweetalrt) {
                                                sweetalrt = false;

                                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                        .setTitleText("Alert!")
                                                        .setContentText("Equipment is on Rent!")
                                                        .setCancelText("Ok")
                                                        .showCancelButton(false)
                                                        .setConfirmClickListener(null)
                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override

                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sweetalrt = true;
                                                                sDialog.dismiss();

                                                                if (sweetalrt_count) {
                                                                    sweetalrt_count = false;

                                                                    alert_count = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                            .setTitleText("Info!")
                                                                            .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                                                            .setCancelText("No")
                                                                            .setConfirmText("Yes")
                                                                            .showCancelButton(true)
                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                                    sweetalrt_count = true;
                                                                                    sweetAlertDialog.dismiss();

                                                                                    if (checkInternetConenction()) {

                                                                                        new AsyncUpdateEquipment().execute();


                                                                                    } else {

                                                                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                                    }


                                                                                }
                                                                            })
                                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override

                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_count = true;
                                                                                    sDialog.dismiss();
                                                                                }
                                                                            });
                                                                    alert_count.setCancelable(false);
                                                                    alert_count.show();
                                                                }
                                                            }
                                                        });
                                                sweetalt.setCancelable(false);
                                                sweetalt.show();
                                            }

                                        }
                                    }
                                } else if (equip_list.get(position).getStatus().toString().contains("SO") || equip_list.get(position).getStatus().toString().contains("so")) {


                                    unit_ID = equip_list.get(position).getEquipid();
                                    detach_pos = position;

                                    call_setEquip_D = 1;

                                    if (sweetalrt) {
                                        sweetalrt = false;

                                        sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                .setTitleText("Info!")
                                                .setContentText("Equipment is Sold! Do you want to count this Equipment# " + unit_ID + " ?")//Equipment is Sold. Do you want to count it?
                                                .setCancelText("No")
                                                .setConfirmText("Yes")
                                                .showCancelButton(true)
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                        sweetalrt = true;

                                                        sweetAlertDialog.dismiss();
                                                        if (checkInternetConenction()) {
                                                            new AsyncUpdateEquipment().execute();
                                                        } else {
                                                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                        }


                                                    }
                                                })
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
                                } else if (equip_list.get(position).getStatus().toString().contains("DE") || equip_list.get(position).getStatus().toString().contains("de")) {

                                    if (loginbranch.toString().equalsIgnoreCase(equpbranch)) {
                                        call_setEquip_D = 1;
                                    } else {
                                        call_setEquip_D = 0;
                                    }

                                    Log.d("call_setEquip_D(DE)", "" + call_setEquip_D);

                                    unit_ID = equip_list.get(position).getEquipid();
                                    detach_pos = position;

                                    if (equpstatus.toString().contains("From Equip")) {
                                        if (sweetalrt_new) {
                                            sweetalrt_new = false;

                                            alert_chk = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Equipment is not on PID, Would you like to add to PID and Count?")
                                                    .setCancelText("No")
                                                    .setConfirmText("Yes")
                                                    .showCancelButton(true)
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                            sweetalrt_new = true;
                                                            sweetAlertDialog.dismiss();

                                                            if (checkInternetConenction()) {

                                                                new AsyncUpdateEquipment().execute();


                                                            } else {

                                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                            }


                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_new = true;
                                                            sDialog.dismiss();
                                                        }
                                                    });
                                            alert_chk.setCancelable(false);
                                            alert_chk.show();
                                        }
                                    } else if (equpstatus.toString().contains("From Count")) {

                                        if (!Eqptblstatus.equals("") && !Eqptblstatus.contains(status)) {


                                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Status not match for this Process ID - " + process_list)
                                                    .setCancelText("Ok")
                                                    .showCancelButton(false)
                                                    .setConfirmClickListener(null)
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismiss();

                                                            if (sweetalrt) {
                                                                sweetalrt = false;

                                                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                                        .setTitleText("Alert!")
                                                                        .setContentText("Equipment is on Demo!")
                                                                        .setCancelText("Ok")
                                                                        .showCancelButton(false)
                                                                        .setConfirmClickListener(null)
                                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override

                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sweetalrt = true;
                                                                                sDialog.dismiss();

                                                                                if (sweetalrt_count) {
                                                                                    sweetalrt_count = false;

                                                                                    alert_count = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                                            .setTitleText("Info!")
                                                                                            .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                                                                            .setCancelText("No")
                                                                                            .setConfirmText("Yes")
                                                                                            .showCancelButton(true)
                                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                @Override
                                                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                                                    sweetalrt_count = true;
                                                                                                    sweetAlertDialog.dismiss();

                                                                                                    if (checkInternetConenction()) {

                                                                                                        new AsyncUpdateEquipment().execute();


                                                                                                    } else {

                                                                                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                                                    }
                                                                                                }
                                                                                            })
                                                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                @Override

                                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                                    sweetalrt_count = true;
                                                                                                    sDialog.dismiss();
                                                                                                }
                                                                                            });
                                                                                    alert_count.setCancelable(false);
                                                                                    alert_count.show();
                                                                                }
                                                                            }
                                                                        });
                                                                sweetalt.setCancelable(false);
                                                                sweetalt.show();
                                                            }

                                                        }
                                                    });
                                            sweetAlertDialog.setCancelable(false);
                                            sweetAlertDialog.show();
                                        }
                                        else
                                        {
                                            if (sweetalrt) {
                                                sweetalrt = false;

                                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                        .setTitleText("Alert!")
                                                        .setContentText("Equipment is on Demo!")
                                                        .setCancelText("Ok")
                                                        .showCancelButton(false)
                                                        .setConfirmClickListener(null)
                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override

                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sweetalrt = true;
                                                                sDialog.dismiss();

                                                                if (sweetalrt_count) {
                                                                    sweetalrt_count = false;

                                                                    alert_count = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                            .setTitleText("Info!")
                                                                            .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                                                            .setCancelText("No")
                                                                            .setConfirmText("Yes")
                                                                            .showCancelButton(true)
                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                                    sweetalrt_count = true;
                                                                                    sweetAlertDialog.dismiss();

                                                                                    if (checkInternetConenction()) {

                                                                                        new AsyncUpdateEquipment().execute();


                                                                                    } else {

                                                                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                }
                                                                            })
                                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override

                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_count = true;
                                                                                    sDialog.dismiss();
                                                                                }
                                                                            });
                                                                    alert_count.setCancelable(false);
                                                                    alert_count.show();
                                                                }
                                                            }
                                                        });
                                                sweetalt.setCancelable(false);
                                                sweetalt.show();
                                            }

                                        }


                                    }
                                } else if (equip_list.get(position).getStatus().toString().contains("TR") || equip_list.get(position).getStatus().toString().contains("tr")) {

                                    if (loginbranch.toString().equalsIgnoreCase(equpbranch)) {
                                        call_setEquip_D = 1;
                                    } else {
                                        call_setEquip_D = 0;
                                    }

                                    Log.d("call_setEquip_D(TR)", "" + call_setEquip_D);

                                    unit_ID = equip_list.get(position).getEquipid();
                                    detach_pos = position;

                                    if (equpstatus.toString().contains("From Equip")) {
                                        if (sweetalrt_new) {
                                            sweetalrt_new = false;

                                            alert_chk = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Equipment is not on PID, Would you like to add to PID and Count?")
                                                    .setCancelText("No")
                                                    .setConfirmText("Yes")
                                                    .showCancelButton(true)
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                            sweetalrt_new = true;
                                                            sweetAlertDialog.dismiss();

                                                            if (checkInternetConenction()) {

                                                                new AsyncUpdateEquipment().execute();


                                                            } else {

                                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                            }


                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_new = true;
                                                            sDialog.dismiss();
                                                        }
                                                    });
                                            alert_chk.setCancelable(false);
                                            alert_chk.show();
                                        }
                                    } else if (equpstatus.toString().contains("6")) {
                                        if (sweetalrt) {
                                            sweetalrt = false;
                                            sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Equipment is in Transfer Status!")
                                                    .setCancelText("Ok")
                                                    .showCancelButton(false)
                                                    .setConfirmClickListener(null)
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt = true;
                                                            sDialog.dismiss();

                                                            if (sweetalrt_count) {
                                                                sweetalrt_count = false;

                                                                alert_count = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                        .setTitleText("Info!")
                                                                        .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                                                        .setCancelText("No")
                                                                        .setConfirmText("Yes")
                                                                        .showCancelButton(true)
                                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override
                                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                                sweetalrt_count = true;
                                                                                sweetAlertDialog.dismiss();

                                                                                if (checkInternetConenction()) {

                                                                                    new AsyncUpdateEquipment().execute();


                                                                                } else {

                                                                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }
                                                                        })
                                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override

                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sweetalrt_count = true;
                                                                                sDialog.dismiss();
                                                                            }
                                                                        });
                                                                alert_count.setCancelable(false);
                                                                alert_count.show();
                                                            }
                                                        }
                                                    });
                                            sweetalt.setCancelable(false);
                                            sweetalt.show();
                                        }
                                    }
                                } else if (equip_list.get(position).getStatus().toString().contains("PI") || equip_list.get(position).getStatus().toString().contains("pi")) {

                                    if (loginbranch.toString().equalsIgnoreCase(equpbranch)) {
                                        call_setEquip_D = 1;
                                    } else {
                                        call_setEquip_D = 0;
                                    }

                                    Log.d("call_setEquip_D(PI)", "" + call_setEquip_D);

                                    unit_ID = equip_list.get(position).getEquipid();
                                    detach_pos = position;

                                    if (equpstatus.toString().contains("From Equip")) {
                                        if (sweetalrt_new) {
                                            sweetalrt_new = false;

                                            alert_chk = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Equipment is not on PID, Would you like to add to PID and Count?")
                                                    .setCancelText("No")
                                                    .setConfirmText("Yes")
                                                    .showCancelButton(true)
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                            sweetalrt_new = true;
                                                            sweetAlertDialog.dismiss();

                                                            if (checkInternetConenction()) {

                                                                new AsyncUpdateEquipment().execute();


                                                            } else {

                                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                            }


                                                        }
                                                    })
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_new = true;
                                                            sDialog.dismiss();
                                                        }
                                                    });
                                            alert_chk.setCancelable(false);
                                            alert_chk.show();
                                        }
                                    } else if (equpstatus.toString().contains("From Count")) {

                                        if (!Eqptblstatus.equals("") && !Eqptblstatus.contains(status)) {


                                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Status not match for this Process ID - " + process_list)
                                                    .setCancelText("Ok")
                                                    .showCancelButton(false)
                                                    .setConfirmClickListener(null)
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override

                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismiss();

                                                            if (sweetalrt) {
                                                                sweetalrt = false;
                                                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                                        .setTitleText("Alert!")
                                                                        .setContentText("Equipment is in Pickup Status!")
                                                                        .setCancelText("Ok")
                                                                        .showCancelButton(false)
                                                                        .setConfirmClickListener(null)
                                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                            @Override

                                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                                sweetalrt = true;
                                                                                sDialog.dismiss();

                                                                                if (sweetalrt_count) {
                                                                                    sweetalrt_count = false;

                                                                                    alert_count = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                                            .setTitleText("Info!")
                                                                                            .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                                                                            .setCancelText("No")
                                                                                            .setConfirmText("Yes")
                                                                                            .showCancelButton(true)
                                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                @Override
                                                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                                                    sweetalrt_count = true;
                                                                                                    sweetAlertDialog.dismiss();

                                                                                                    if (checkInternetConenction()) {

                                                                                                        new AsyncUpdateEquipment().execute();


                                                                                                    } else {

                                                                                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                                                    }
                                                                                                }
                                                                                            })
                                                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                                @Override

                                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                                    sweetalrt_count = true;
                                                                                                    sDialog.dismiss();
                                                                                                }
                                                                                            });
                                                                                    alert_count.setCancelable(false);
                                                                                    alert_count.show();
                                                                                }
                                                                            }
                                                                        });
                                                                sweetalt.setCancelable(false);
                                                                sweetalt.show();
                                                            }

                                                        }
                                                    });
                                            sweetAlertDialog.setCancelable(false);
                                            sweetAlertDialog.show();
                                        }
                                        else
                                        {
                                            if (sweetalrt) {
                                                sweetalrt = false;
                                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                        .setTitleText("Alert!")
                                                        .setContentText("Equipment is in Pickup Status!")
                                                        .setCancelText("Ok")
                                                        .showCancelButton(false)
                                                        .setConfirmClickListener(null)
                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override

                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sweetalrt = true;
                                                                sDialog.dismiss();

                                                                if (sweetalrt_count) {
                                                                    sweetalrt_count = false;

                                                                    alert_count = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                            .setTitleText("Info!")
                                                                            .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                                                            .setCancelText("No")
                                                                            .setConfirmText("Yes")
                                                                            .showCancelButton(true)
                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                                                    sweetalrt_count = true;
                                                                                    sweetAlertDialog.dismiss();

                                                                                    if (checkInternetConenction()) {

                                                                                        new AsyncUpdateEquipment().execute();


                                                                                    } else {

                                                                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                }
                                                                            })
                                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override

                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_count = true;
                                                                                    sDialog.dismiss();
                                                                                }
                                                                            });
                                                                    alert_count.setCancelable(false);
                                                                    alert_count.show();
                                                                }
                                                            }
                                                        });
                                                sweetalt.setCancelable(false);
                                                sweetalt.show();
                                            }

                                        }

                                    }
                                }
                            }
                        }
                    }
                }

            }
        });

        filter_layout = (LinearLayout) findViewById(R.id.filter_layout);
        filter_layout_1 = (LinearLayout) findViewById(R.id.filter_layout_1);

        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearSession();
                Intent myintent = new Intent(equipment_inventory_activity.this, Dashboard.class);
                startActivity(myintent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });


        filter_layout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        filter_layout.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        filter_layout.setVisibility(View.VISIBLE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(
                                0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec
                                .makeMeasureSpec(0,
                                        View.MeasureSpec.UNSPECIFIED);
                        filter_layout.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0,
                                filter_layout.getMeasuredHeight());
                        return true;
                    }
                });


        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filter_layout.getVisibility() == View.VISIBLE) {
                    collapse();
                } else {
                    expand();
                }
                if (filter_arrow.getTag() != null && filter_arrow.getTag().toString().equals("filter_arrow")) {
                    filter_arrow.setImageResource(R.drawable.down);
                    filter_arrow.setTag("filter_down");
                } else {
                    filter_arrow.setImageResource(R.drawable.right);
                    filter_arrow.setTag("filter_arrow");
                }
            }
        });


        filter_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filter_layout.getVisibility() == View.VISIBLE) {
                    collapse();
                } else {
                    expand();
                }
                if (filter_arrow.getTag() != null && filter_arrow.getTag().toString().equals("filter_arrow")) {
                    filter_arrow.setImageResource(R.drawable.down);
                    filter_arrow.setTag("filter_down");
                } else {
                    filter_arrow.setImageResource(R.drawable.right);
                    filter_arrow.setTag("filter_arrow");
                }
            }
        });


        //Branch And ProcessId layout

        filter_layout_1.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        filter_layout_1.getViewTreeObserver().removeOnPreDrawListener(this);
                        filter_layout_1.setVisibility(View.VISIBLE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(
                                0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec
                                .makeMeasureSpec(0,
                                        View.MeasureSpec.UNSPECIFIED);
                        filter_layout_1.measure(widthSpec, heightSpec);

                        mAnimator_1 = slideAnimator_1(0, filter_layout_1.getMeasuredHeight());
                        return true;
                    }
                });


        filter_btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (filter_layout_1.getVisibility() == View.VISIBLE) {
                    collapse_1();
                } else {
                    expand_1();
                }

                if (filter_arrow_1.getTag() != null && filter_arrow_1.getTag().toString().equals("filter_arrow")) {
                    filter_arrow_1.setImageResource(R.drawable.down);
                    filter_arrow_1.setTag("filter_down");
                } else {
                    filter_arrow_1.setImageResource(R.drawable.right);
                    filter_arrow_1.setTag("filter_arrow");
                }

            }
        });

        filter_arrow_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (filter_layout_1.getVisibility() == View.VISIBLE) {
                    collapse_1();
                } else {
                    expand_1();
                }
                if (filter_arrow_1.getTag() != null && filter_arrow_1.getTag().toString().equals("filter_arrow")) {
                    filter_arrow_1.setImageResource(R.drawable.down);
                    filter_arrow_1.setTag("filter_down");
                } else {
                    filter_arrow_1.setImageResource(R.drawable.right);
                    filter_arrow_1.setTag("filter_arrow");
                }

            }
        });


        process_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String branch_names = branch_no.getText().toString();

                if (!branch_names.isEmpty()) {

                    if (checkInternetConenction()) {

                        if (branch_name.isEmpty()) {
                            branch_name = branch_no.getText().toString();

                            for (int ii = 0; ii < branch_name.length(); ii++) {

                                Character character = branch_name.charAt(ii);

                                if (character.toString().equals("-")) {

                                    branch_name = branch_name.substring(0, ii);

                                    Log.d("branch_trim", "" + branch_name);
                                    break;
                                }
                            }
                        }

                        handleasync = true;

                        new AsyncGetEquipmentProcessid().execute();

                    } else {

                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {

                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please Choose the Branch before Select PID")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        Sweetalrt_list = true;
                                        sDialog.dismiss();
                                    }
                                });
                        Sweetalt_list.setCancelable(false);
                        Sweetalt_list.show();
                    }
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearSession();
                Intent myintent = new Intent(equipment_inventory_activity.this, Dashboard.class);
                startActivity(myintent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });

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

        equipment_inventory_activity.readerDevice = ReaderDevice.getMXDevice(mContext);
        readerDevice.startAvailabilityListening();
        selectedDevice = "MX Scanner";

        equipment_inventory_activity.readerDevice.setReaderDeviceListener(this);
        equipment_inventory_activity.readerDevice.enableImage(true);
        equipment_inventory_activity.readerDevice.connect(equipment_inventory_activity.this);
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

    public void FinalResult(final String result) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Sessiondata.getInstance().getScanner_inventory() == 6) {
                    List<String> results = Arrays.asList(result.split(","));
                    if (results.size() == 1) {
                        Sessiondata.getInstance().setUnitId(result);

                    } else {
                        Sessiondata.getInstance().setUnitId(results.get(Sessiondata.getInstance().getEqup_scanner()));

                    }

                } else if (Sessiondata.getInstance().getScanner_inventory() == 7) {
                    List<String> results = Arrays.asList(result.split(","));
                    if (results.size() == 1) {
                        Sessiondata.getInstance().setSerial(result);

                    } else {
                        Log.d("Value_Postion", "" + Sessiondata.getInstance().getEqup_scanner());

                        Sessiondata.getInstance().setSerial(results.get(Sessiondata.getInstance().getEqup_scanner()));

                    }
                } else if (Sessiondata.getInstance().getScanner_inventory() == 8) {
                    List<String> results = Arrays.asList(result.split(","));
                    if (results.size() == 1) {

                        Sessiondata.getInstance().setModel(result);

                    } else {
                        Log.d("Value_Postion", "" + Sessiondata.getInstance().getEqup_scanner());
                        Sessiondata.getInstance().setModel(results.get(Sessiondata.getInstance().getEqup_scanner()));

                    }
                } else if (Sessiondata.getInstance().getScanner_inventory() == 9) {
                    List<String> results = Arrays.asList(result.split(","));
                    if (results.size() == 1) {
                        Sessiondata.getInstance().setMake(result);

                    } else {
                        Log.d("Value_Postion", "" + Sessiondata.getInstance().getEqup_scanner());
                        Sessiondata.getInstance().setMake(results.get(Sessiondata.getInstance().getEqup_scanner()));

                    }
                }

                processid.setText(Sessiondata.getInstance().getSub_process());
                equp_id.setText(Sessiondata.getInstance().getUnitId());
                model.setText(Sessiondata.getInstance().getModel());
                serial.setText(Sessiondata.getInstance().getSerial());
                make.setText(Sessiondata.getInstance().getMake_model());
            }

        }, 500);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(equp_id.getWindowToken(), 0);


  /*      equp_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (selectedDevice.equals("MX Scanner")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            equipid_text = s.toString();

                            String ss = s.toString();
                            if (!ss.equals(ss.toUpperCase())) {
                                ss = ss.toUpperCase();
                                equp_id.setText(ss);
                            }
                            equp_id.setSelection(equp_id.getText().length());

                            String equi_proc = split_string(process_list);

                            if (equi_proc.toString().equalsIgnoreCase("")) {

                            } else {
                                if (checkInternetConenction()) {

                                    equip_list = new ArrayList<GetEquipmentList>();

                                    if (GlobalVariables.checkbtn_load == 0) {
                                        Log.d("UncountFilter", "true");
                                        GlobalVariables.showerrormsg = true;
                                        if (handleasync == true) {
                                            handleasync = false;
                                            new AsyncGetEquipmentListUncountFilter().execute();
                                        }
                                    } else {
                                        Log.d("CountFilter", "true");
                                        GlobalVariables.showerrormsg = true;
                                        if (handleasync == true) {
                                            handleasync = false;
                                            new AsyncGetEquipmentListCountedFilter().execute();
                                        }
                                    }

                                } else {

                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }, 250);

                }
            }
        });*/
    }

    @Override
    public void onAvailabilityChanged(ReaderDevice reader) {
        if (reader.getAvailability() == ReaderDevice.Availability.AVAILABLE) {
            equipment_inventory_activity.readerDevice.connect(equipment_inventory_activity.this);
        } else {
            // DISCONNECTED USB DEVICE
            equipment_inventory_activity.readerDevice.connect(equipment_inventory_activity.this);
            equipment_inventory_activity.readerDevice.disconnect();
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

        //example setSymbologyEnabled
        equipment_inventory_activity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.C128, true, null);
        equipment_inventory_activity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.DATAMATRIX, true, null);
        equipment_inventory_activity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.UPC_EAN, true, null);
        equipment_inventory_activity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.QR, true, null);

        //example sendCommand
        equipment_inventory_activity.readerDevice.getDataManSystem().sendCommand("SET SYMBOL.MICROPDF417 ON");
        equipment_inventory_activity.readerDevice.getDataManSystem().sendCommand("SET IMAGE.SIZE 0");

    }

    private void expand() {
        filter_layout.setVisibility(View.VISIBLE);

        mAnimator.start();
    }

    private void expand_1() {
        filter_layout_1.setVisibility(View.VISIBLE);

        mAnimator_1.start();
    }


    private void collapse() {


        int finalHeight = filter_layout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {

                filter_layout.setVisibility(View.GONE);


            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    private void collapse_1() {


        int finalHeight = filter_layout_1.getHeight();

        ValueAnimator mAnimator = slideAnimator_1(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {

                filter_layout_1.setVisibility(View.GONE);


            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = filter_layout
                        .getLayoutParams();
                layoutParams.height = value;
                filter_layout.setLayoutParams(layoutParams);
            }
        });

        return animator;
    }


    private ValueAnimator slideAnimator_1(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = filter_layout_1
                        .getLayoutParams();
                layoutParams.height = value;
                filter_layout_1.setLayoutParams(layoutParams);
            }
        });

        return animator;
    }

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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void justifyListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    public void ClearSession() {
        Sessiondata.getInstance().setEqu_branch_name("");
        Sessiondata.getInstance().setUnitId("");
        Sessiondata.getInstance().setModel("");
        Sessiondata.getInstance().setMake("");
        Sessiondata.getInstance().setSerial("");
        Sessiondata.getInstance().setSub_process("");
        Sessiondata.getInstance().setMake_model("");
        Sessiondata.getInstance().setInventory_equip_id(0);

        Sessiondata.getInstance().setEqup_scanner(0);

        Sessiondata.getInstance().setInventory_scanner("");
        GlobalVariables.showerrormsg = false;
        GlobalVariables.checkbtn_load = 0;

        handleasync = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        handleasync = true;

        if (process_list.equals(""))
        {
            process_list = Sessiondata.getInstance().getProcess_list();
            Log.d("getProcess_list", process_list);
        }

        if (Sessiondata.getInstance().getInventory_equip_id() == 1) {

            if (!Sessiondata.getInstance().getEqu_branch_name().isEmpty()) {
                branch_no.setText(Sessiondata.getInstance().getEqu_branch_name());
            }
            if (!Sessiondata.getInstance().getSub_process().isEmpty()) {
                processid.setText(Sessiondata.getInstance().getSub_process());
            }
            if (!Sessiondata.getInstance().getUnitId().isEmpty()) {
//                equp_id.setText("");
                equp_id.setText(Sessiondata.getInstance().getUnitId());
            }
//            if (!Sessiondata.getInstance().getModel().isEmpty()) {
////                mode
// .setText(Sessiondata.getInstance().getModel());
//            }
            if (!Sessiondata.getInstance().getSerial().isEmpty()) {
//                serial.setText("");
                serial.setText(Sessiondata.getInstance().getSerial());
            }
            if (!Sessiondata.getInstance().getMake_model().isEmpty()) {
//                make.setText("");
                Log.d("getMake_model_1", " " + Sessiondata.getInstance().getMake_model());
                make.setText(Sessiondata.getInstance().getMake_model());
            }

            Log.d("OnResume", " ProcessId " + Sessiondata.getInstance().getSub_process());
        }

    }

    private boolean checkInternetConenction() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        } else {
            return false;

        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void clearandload() {

        equp_id.setText("");
        model.setText("");
        make.setText("");
        serial.setText("");

        handleasync = true;

        Sessiondata.getInstance().setUnitId("");
        Sessiondata.getInstance().setModel("");
        Sessiondata.getInstance().setMake("");
        Sessiondata.getInstance().setSerial("");
        Sessiondata.getInstance().setMake_model("");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            btn_counted.setBackground(grey_drawable);
            btn_uncounted.setBackground(grey_drawable);
            btn_counted.setTextColor(Color.BLACK);
            btn_uncounted.setTextColor(Color.BLACK);
        } else {
            btn_counted.setBackgroundDrawable(grey_drawable);
            btn_uncounted.setBackgroundDrawable(grey_drawable);
            btn_counted.setTextColor(Color.BLACK);
            btn_uncounted.setTextColor(Color.BLACK);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ClearSession();
        Intent myintent = new Intent(equipment_inventory_activity.this, Dashboard.class);
        startActivity(myintent);
        overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
    }

    public static enum DeviceType {MX_1000}

    public class CustomAdapter extends BaseAdapter {
        ArrayList<GetEquipmentList> result;

        private LayoutInflater inflater = null;

        public CustomAdapter(Context context, ArrayList<GetEquipmentList> list) {
            result = list;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public CustomAdapter() {

        }

        @Override
        public int getCount() {

            int size = 0;
            if (result != null) {
                size = result.size();
            }

            return size;
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @SuppressLint("LongLogTag")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Holder holder = new Holder();
            View rowView;

            rowView = inflater.inflate(R.layout.inventory_childrow, null);

            holder.unit_id = (TextView) rowView.findViewById(R.id.unit_id);
            holder.model = (TextView) rowView.findViewById(R.id.model_value);
            holder.make = (TextView) rowView.findViewById(R.id.make_value);
            holder.serial_no = (TextView) rowView.findViewById(R.id.serial_value);

//            holder.txt_unitid = (TextView) rowView.findViewById(R.id.txt_unitid);
//            holder.txt_make = (TextView) rowView.findViewById(R.id.txt_make);
//            holder.txt_model = (TextView) rowView.findViewById(R.id.txt_model);
//            holder.txt_serial = (TextView) rowView.findViewById(R.id.txt_serial);
            holder.desc_value = (TextView) rowView.findViewById(R.id.desc_value);

            holder.txt_branch_type_status = (TextView) rowView.findViewById(R.id.txt_branch_type_status);

            holder.unit_id.setTypeface(txt_face);
            holder.txt_branch_type_status.setTypeface(txt_face);
            holder.model.setTypeface(txt_face);
            holder.make.setTypeface(txt_face);
            holder.serial_no.setTypeface(txt_face);
            holder.desc_value.setTypeface(txt_face);

//            holder.txt_unitid.setTypeface(header_face);
//            holder.txt_make.setTypeface(header_face);
//            holder.txt_model.setTypeface(header_face);
//            holder.txt_serial.setTypeface(header_face);

            holder.unit_id.setText(result.get(position).getEquipid());
            holder.model.setText(result.get(position).getModel());
            holder.make.setText(result.get(position).getMfg());
            holder.serial_no.setText(result.get(position).getSerialno());
            holder.desc_value.setText(result.get(position).getDescription());

            String subStat = "";
            if (result.get(position).getSubstat() != null && result.get(position).getSubstat().length() != 0) {
                subStat = "-" + result.get(position).getSubstat();
            } else {
                subStat = "";
            }



            String str = "";

            if (Sessiondata.getInstance().getCounted_Uncounted_status())
            {
//                Log.d("getCounted_Uncounted_status", "Status");

                str = result.get(position).getBranch() + "/" +
                        result.get(position).getEqptype() + "/" +
                        result.get(position).getStatus() + subStat;

            }
            else
            {
//                Log.d("getCounted_Uncounted_status", " :Eqptblstatus" );

                str = result.get(position).getBranch() + "/" +
                        result.get(position).getEqptype() + "/" +
                        result.get(position).getEqptblstatus() + subStat;
            }

            //17893

            holder.txt_branch_type_status.setText(str);

            handleasync = true;

            if (autoCollapse && result != null && result.size() != 0) {
                collapse_1();
                autoCollapse = false;
            }


            return rowView;
        }

        public class Holder {
            TextView unit_id, model, make, serial_no, txt_unitid, txt_make, txt_model, txt_serial, desc_value, txt_branch_type_status;
        }

    }

    public class CustomAdapter_processid extends CustomAdapter {

        ArrayList<GetEquipmentProcessid> result;
        Context context;
        int[] imageId;
        private LayoutInflater inflater = null;

        public CustomAdapter_processid(Context context, ArrayList<GetEquipmentProcessid> list) {
            super();
            result = list;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Holder holder = new Holder();
            View rowview_processid;

            rowview_processid = inflater.inflate(R.layout.activity_branch_childrow, null);

            holder.process_list = (TextView) rowview_processid.findViewById(R.id.process_list);
            holder.process_list.setText(String.valueOf(result.get(position).getProcessidAnd_Branch()));
            holder.process_list.setTypeface(header_face);

            handleasync = true;

            holder.process_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btn_submit.setEnabled(true);
                   /* submit_layout.setEnabled(false);
                    submit_layout.setVisibility(View.GONE);*/
                    btn_submit.setVisibility(View.VISIBLE);
                    /*footer.setVisibility(View.GONE);*/


//                     process_list = holder.process_list.getText().toString();

                    process_list = String.valueOf(result.get(position).getProcessid());
                    autoCollapse = true;

                    processid.setText(holder.process_list.getText().toString());
                    processid.setTag(process_list);
                    description.setText(result.get(position).getProcessdescription());

                    Sessiondata.getInstance().setProcess_list(process_list);

//                    processid.setText(process_list);

                    scannedtype = 1;
                    Log.d("OnItemClick", "processId_scannedtype" + scannedtype);

                    Sessiondata.getInstance().setSub_process(process_list);


                    branch_name = branch_no.getText().toString();
                    branch_no.setText(branch_name);

                    equp_id.setText("");
                    model.setText("");
                    make.setText("");
                    serial.setText("");

                    Sessiondata.getInstance().setEqu_branch_name(branch_name);
                    Sessiondata.getInstance().setUnitId("");
                    Sessiondata.getInstance().setModel("");
                    Sessiondata.getInstance().setMake("");
                    Sessiondata.getInstance().setSerial("");

                    for (int ii = 0; ii < branch_name.length(); ii++) {

                        Character character = branch_name.charAt(ii);

                        if (character.toString().equals("-")) {

                            branch_name = branch_name.substring(0, ii);
                            Log.d("branch_trim", "" + branch_name);
                            break;
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        btn_counted.setBackground(grey_drawable);
                        btn_uncounted.setBackground(grey_drawable);
                        btn_counted.setTextColor(Color.BLACK);
                        btn_uncounted.setTextColor(Color.BLACK);
                    } else {
                        btn_counted.setBackgroundDrawable(grey_drawable);
                        btn_uncounted.setBackgroundDrawable(grey_drawable);
                        btn_counted.setTextColor(Color.BLACK);
                        btn_uncounted.setTextColor(Color.BLACK);
                    }

                    if (checkInternetConenction()) {

                        GlobalVariables.checkbtn_load = 0;

                        equip_list = new ArrayList<GetEquipmentList>();

                        new AsyncGetEquipmentList().execute();
                        mDialoglist.dismiss();

                    } else {

                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            });


            return rowview_processid;

        }

        public class Holder {
            TextView process_list;


        }


    }

    public class CustomAdapter_attach extends CustomAdapter {

        ArrayList<GetAttachments> result;
        private LayoutInflater inflater = null;

        public CustomAdapter_attach(Context context, ArrayList<GetAttachments> list) {
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Holder holder = new Holder();
            View rowview_attach;

            rowview_attach = inflater.inflate(R.layout.activity_attachlist_childrow, null);

            holder.attachment_list = (RelativeLayout) rowview_attach.findViewById(R.id.select_list);
            holder.attach_label = (TextView) rowview_attach.findViewById(R.id.attch_label);
            holder.equp_label = (TextView) rowview_attach.findViewById(R.id.equp_label);
            holder.attach_id = (TextView) rowview_attach.findViewById(R.id.attch_id);
            holder.equp_id = (TextView) rowview_attach.findViewById(R.id.equp_id);

            holder.attach_id.setText(String.valueOf(result.get(position).getAttachments()));
            holder.equp_id.setText(String.valueOf(result.get(position).getEquipid()));

            holder.attach_id.setTypeface(txt_face);
            holder.equp_id.setTypeface(txt_face);
            holder.equp_label.setTypeface(header_face);
            holder.attach_label.setTypeface(header_face);


            return rowview_attach;

        }

        public class Holder {
            TextView attach_label, attach_id, equp_label, equp_id;
            RelativeLayout attachment_list;
        }


    }

    private class AsyncDetachEquipment extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                DetachEquipment = WebServiceConsumer.getInstance().DetachEquipment(usertoken, equipid);
            } catch (SocketTimeoutException e) {
                DetachEquipment = null;
            } catch (Exception e) {
                DetachEquipment = null;
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ProgressBar.dismiss();

            if (DetachEquipment != null) {
                if (DetachEquipment.toString().contains("Session")) {

                    String Result = DetachEquipment;
                    String replace = Result.replace("Error - ", "");

                    Session = 5;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (checkInternetConenction()) {

                        new AsyncUpdateEquipment().execute();

                    } else {

                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }
            } else {
                if (checkInternetConenction()) {

                    new AsyncUpdateEquipment().execute();

                } else {

                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    public class AsyncGetEquipmentProcessid extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);
                Log.d("branch_name", "" + branch_name);

                equip_processid = WebServiceConsumer.getInstance().GetEquipmentProcessidV1(user_token, branch_name);

            } catch (SocketTimeoutException e) {
                equip_processid = null;
            } catch (Exception e) {
                equip_processid = null;
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            Sessiondata.getInstance().setGetEquipmentProcessids(equip_processid);


            CustomAdapter adapter_processid;
            if (equip_processid != null) {

                if (equip_processid.size() == 1) {

                    if (equip_processid.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = equip_processid.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (equip_processid.get(0).getMessage().toString().contains("Session")) {
                            Session = 1;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {

                        ProgressBar.dismiss();
                        if ((mDialoglist == null) || !mDialoglist.isShowing()) {
                            mDialoglist = new Dialog(equipment_inventory_activity.this);
                            mDialoglist.setCanceledOnTouchOutside(false);
                            mDialoglist.setCancelable(false);
                            mDialoglist.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            mDialoglist.setContentView(R.layout.dialog_processid_search);

                            TextView txt_header = (TextView) mDialoglist.findViewById(R.id.txt_header);
                            TextView dialog_social_ok = (TextView) mDialoglist.findViewById(R.id.dialog_social_ok);
                            dialog_social_ok.setTypeface(header_face);
                            txt_header.setText("Select ProcessId");
                            txt_header.setTypeface(header_face);

                            ListView list = (ListView) mDialoglist.findViewById(R.id.list);

                            equip_processid = new ArrayList<>();
                            equip_processid = Sessiondata.getInstance().getGetEquipmentProcessids();


                            TextView empty = (TextView) mDialoglist.findViewById(R.id.empty);
                            empty.setTypeface(txt_face);

                            if (equip_processid.size() == 0) {

                                empty.setVisibility(View.VISIBLE);
                                empty.setText("No PID");
                                list.setVisibility(View.GONE);
                            } else {

                                empty.setVisibility(View.GONE);
                                list.setVisibility(View.VISIBLE);

                                adapter_processid = new CustomAdapter_processid(equipment_inventory_activity.this, equip_processid);
                                list.setAdapter(adapter_processid);
                                justifyListViewHeightBasedOnChildren(list);

                            }

                            mDialoglist.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                            mDialoglist.show();

                            dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialoglist.dismiss();
                                }
                            });

                        }

                    }

                } else {

                    ProgressBar.dismiss();
                    if ((mDialoglist == null) || !mDialoglist.isShowing()) {
                        mDialoglist = new Dialog(equipment_inventory_activity.this);
                        mDialoglist.setCanceledOnTouchOutside(false);
                        mDialoglist.setCancelable(false);
                        mDialoglist.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialoglist.setContentView(R.layout.dialog_processid_search);

                        TextView txt_header = (TextView) mDialoglist.findViewById(R.id.txt_header);
                        TextView dialog_social_ok = (TextView) mDialoglist.findViewById(R.id.dialog_social_ok);
                        dialog_social_ok.setTypeface(header_face);
                        txt_header.setText("Select ProcessId");
                        txt_header.setTypeface(header_face);

                        ListView list = (ListView) mDialoglist.findViewById(R.id.list);

                        equip_processid = new ArrayList<>();
                        equip_processid = Sessiondata.getInstance().getGetEquipmentProcessids();


                        TextView empty = (TextView) mDialoglist.findViewById(R.id.empty);
                        empty.setTypeface(txt_face);

                        if (equip_processid.size() == 0) {

                            empty.setVisibility(View.VISIBLE);
                            empty.setText("No PID");
                            list.setVisibility(View.GONE);
                        } else {

                            empty.setVisibility(View.GONE);
                            list.setVisibility(View.VISIBLE);

                            adapter_processid = new CustomAdapter_processid(equipment_inventory_activity.this, equip_processid);
                            list.setAdapter(adapter_processid);
                            justifyListViewHeightBasedOnChildren(list);

                        }

                        mDialoglist.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        mDialoglist.show();

                        dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialoglist.dismiss();
                            }
                        });

                    }
                }

            } else {

                ProgressBar.dismiss();

                if ((mDialoglist == null) || !mDialoglist.isShowing()) {
                    mDialoglist = new Dialog(equipment_inventory_activity.this);
                    mDialoglist.setCanceledOnTouchOutside(false);
                    mDialoglist.setCancelable(false);
                    mDialoglist.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialoglist.setContentView(R.layout.dialog_processid_search);

                    TextView txt_header = (TextView) mDialoglist.findViewById(R.id.txt_header);
                    TextView dialog_social_ok = (TextView) mDialoglist.findViewById(R.id.dialog_social_ok);
                    dialog_social_ok.setTypeface(header_face);
                    txt_header.setText("Select ProcessId");
                    txt_header.setTypeface(header_face);

                    ListView list = (ListView) mDialoglist.findViewById(R.id.list);

                    equip_processid = new ArrayList<>();
                    equip_processid = Sessiondata.getInstance().getGetEquipmentProcessids();

                    TextView empty = (TextView) mDialoglist.findViewById(R.id.empty);
                    empty.setTypeface(txt_face);

                    if (equip_processid.size() == 0) {

                        empty.setVisibility(View.VISIBLE);
                        empty.setText("No PID");
                        list.setVisibility(View.GONE);
                    } else {

                        empty.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);

                        adapter_processid = new CustomAdapter_processid(equipment_inventory_activity.this, equip_processid);
                        list.setAdapter(adapter_processid);
                        justifyListViewHeightBasedOnChildren(list);

                    }

                    mDialoglist.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    mDialoglist.show();

                    dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialoglist.dismiss();
                        }
                    });

                }

            }
        }
    }

    public class AsyncGetEquipmentList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);
                int process_ids_text = Integer.parseInt(split_string(process_list));

                equip_list = WebServiceConsumer.getInstance().GetEquipmentListV4(user_token, process_ids_text, "", "", "", "");
                Log.d("equip_list", equip_list + "");

            } catch (SocketTimeoutException e) {
                equip_list = null;
            } catch (Exception e) {
                equip_list = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressBar.dismiss();

            Sessiondata.getInstance().setGetEquipmentLists(equip_list);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btn_counted.setBackground(grey_drawable);
                btn_uncounted.setBackground(blue_drawable);
                btn_counted.setTextColor(getResources().getColor(R.color.black));
                btn_uncounted.setTextColor(getResources().getColor(R.color.white));
            } else {
                btn_counted.setBackgroundDrawable(grey_drawable);
                btn_uncounted.setBackgroundDrawable(blue_drawable);
                btn_counted.setTextColor(getResources().getColor(R.color.black));
                btn_uncounted.setTextColor(getResources().getColor(R.color.white));
            }

            if (equip_list != null) {

                equp_id.setEnabled(true);
                model.setEnabled(true);
                serial.setEnabled(true);
                make.setEnabled(true);
                equpiment_id.setEnabled(true);

                if (equip_list.size() == 1) {

                    if (equip_list.get(0).getMessage().length() != 0) {
                        String Result = equip_list.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (equip_list.get(0).getMessage().toString().contains("Session")) {
                            Session = 0;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {
                        String eqstatus = equip_list.get(0).getEqpstatus();
                        Log.d("eqstatus_list_single1", "" + eqstatus);

                        if (eqstatus.toString().contains("Counted")) {
                            btn_submit.setEnabled(true);
                          /*  submit_layout.setEnabled(false);
                            submit_layout.setVisibility(View.GONE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.GONE);*/


                            equip_list = new ArrayList<>();
                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);

                            new AsyncGetEquipmentListCountedFilter().execute();

                        } else {
                            btn_submit.setEnabled(true);
                          /*  submit_layout.setEnabled(false);
                            submit_layout.setVisibility(View.GONE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.GONE);*/

                            equip_list = new ArrayList<>();
                            equip_list = Sessiondata.getInstance().getGetEquipmentLists();

                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);

                        }

                    }

                } else {

                    equip_list = new ArrayList<>();
                    equip_list = Sessiondata.getInstance().getGetEquipmentLists();

                    if (equip_list.size() == 0) {

                        btn_submit.setEnabled(true);
                      /*  submit_layout.setEnabled(true);
                        submit_layout.setVisibility(View.VISIBLE);*/
                        btn_submit.setVisibility(View.VISIBLE);
                        /*footer.setVisibility(View.VISIBLE);*/

                    } else {

                        btn_submit.setEnabled(true);
                      /*  submit_layout.setEnabled(false);
                        submit_layout.setVisibility(View.GONE);*/
                        btn_submit.setVisibility(View.VISIBLE);
                        /*footer.setVisibility(View.GONE);*/

                        ArrayList<GetEquipmentList> equimlist = new ArrayList<>();
                        for (int i = 0; i < equip_list.size(); i++) {
                            String eqstatus = equip_list.get(i).getEqpstatus();
                            Log.d("eqstatus_list_many", "" + eqstatus);

                            if (!eqstatus.toString().contains("Counted")) {

                                equimlist.add(equip_list.get(i));
                            }

                        }
                        if (equimlist.size() == 0) {
                            new AsyncGetEquipmentListCounted().execute();
                        } else {
                            equip_list = equimlist;
                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        }


                    }

                    adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                    equp_list.setAdapter(adapter);

                }

            } else {

                btn_submit.setEnabled(true);
              /*  submit_layout.setEnabled(true);
                submit_layout.setVisibility(View.VISIBLE);*/
                btn_submit.setVisibility(View.VISIBLE);
                /*footer.setVisibility(View.VISIBLE);*/

            }

        }
    }

    public class AsyncGetEquipmentListCountedFilter extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);
                int process_ids_text = Integer.parseInt(process_ids);

                equip_list = WebServiceConsumer.getInstance().GetEquipmentCountedList(user_token, process_ids_text, equipid_text, make_text, model_text, serial_text);

            } catch (SocketTimeoutException e) {
                equip_list = null;
            } catch (Exception e) {
                equip_list = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Sessiondata.getInstance().setGetEquipmentLists(equip_list);
            ProgressBar.dismiss();

            String pid = processid.getText().toString();
            Sessiondata.getInstance().setSub_process(pid);

            Log.d("GlobalVariables ", "CountFilter " + GlobalVariables.showerrormsg + " checkbtn_load " + GlobalVariables.checkbtn_load);


            if (equip_list != null) {

                if (equip_list.size() == 1) {

                    handleasync = true;

                    if (equip_list.get(0).getMessage().length() != 0) {

                        String Result = equip_list.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (equip_list.get(0).getMessage().toString().contains("Session")) {
                            Session = 17;
                            if (return_once == 0) {
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }
                            }

                            if (return_once == 1) {
                                return_once = 0;
                            }
                        }
                    } else {

                        GlobalVariables.checkbtn_load = 1;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            btn_counted.setBackground(blue_drawable);
                            btn_uncounted.setBackground(grey_drawable);
                            btn_counted.setTextColor(getResources().getColor(R.color.white));
                            btn_uncounted.setTextColor(getResources().getColor(R.color.black));
                        } else {
                            btn_counted.setBackgroundDrawable(blue_drawable);
                            btn_uncounted.setBackgroundDrawable(grey_drawable);
                            btn_counted.setTextColor(getResources().getColor(R.color.white));
                            btn_uncounted.setTextColor(getResources().getColor(R.color.black));
                        }

                        //for (int i=0;i<equip_list.size();i++){
                        String eqstatus = equip_list.get(0).getEqpstatus();
                        Log.d("eqstatus_list_single2", "" + eqstatus);

                        if (!eqstatus.toString().contains("Counted")) {
                            btn_submit.setEnabled(true);
                          /*  submit_layout.setEnabled(false);
                            submit_layout.setVisibility(View.GONE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.GONE);
                             */
                            equip_list = new ArrayList<>();
                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        } else {
                            btn_submit.setEnabled(true);
                           /* submit_layout.setEnabled(false);
                            submit_layout.setVisibility(View.GONE);*/
                            btn_submit.setVisibility(View.VISIBLE);
//                            footer.setVisibility(View.GONE);/**/

                            equip_list = new ArrayList<>();
                            equip_list = Sessiondata.getInstance().getGetEquipmentLists();

                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        }

                    }

                } else {

                    equip_list = new ArrayList<>();
                    equip_list = Sessiondata.getInstance().getGetEquipmentLists();

                    if (equip_list.size() == 0) {

                        if (GlobalVariables.showerrormsg == true) {
                            if (checkInternetConenction()) {
                                Log.d("CountFilter", "true");
                                GlobalVariables.showerrormsg = false;

                                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                                process_ids = split_string(process_list);
                                equipid_text = equp_id.getText().toString();
                                model_text = model.getText().toString();
                                make_text = make.getText().toString();
                                serial_text = serial.getText().toString();

                                new AsyncGetEquipmentListUncountFilter().execute();

                            } else {
                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        } else {

                            handleasync = true;
                            GlobalVariables.checkbtn_load = 0;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                btn_counted.setBackground(grey_drawable);
                                btn_uncounted.setBackground(blue_drawable);
                                btn_counted.setTextColor(getResources().getColor(R.color.black));
                                btn_uncounted.setTextColor(getResources().getColor(R.color.white));
                            } else {
                                btn_counted.setBackgroundDrawable(grey_drawable);
                                btn_uncounted.setBackgroundDrawable(blue_drawable);
                                btn_counted.setTextColor(getResources().getColor(R.color.black));
                                btn_uncounted.setTextColor(getResources().getColor(R.color.white));
                            }

                            Log.d("CountFilter", "false");
                            btn_submit.setEnabled(true);
//                            submit_layout.setEnabled(true);
//                            submit_layout.setVisibility(View.VISIBLE);
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.VISIBLE);*/

                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);

                            if (!equipid_text.toString().isEmpty() && !make_text.toString().isEmpty() &&
                                    !model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                    process_ids = split_string(process_list);
                                    equp_ids = equp_id.getText().toString();
                                    models = model.getText().toString();
                                    makes = make.getText().toString();
                                    serials = serial.getText().toString();

                                    sub_processid = split_string(process_list);

                                    if (sweetalrt_notmatch) {
                                        sweetalrt_notmatch = false;
                                        dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                .setTitleText("Message!")
                                                .setContentText("Does not exist, Do you want to add to Hand Writes?")
                                                .setCancelText("No")
                                                .setConfirmText("Yes")
                                                .showCancelButton(true)

                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;
                                                        sDialog.dismiss();
                                                    }
                                                })

                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;

//                                                        scan_processid = split_string(process_list);
                                                        scan_processid = processid.getText().toString();

                                                        scan_model = model.getText().toString();
                                                        scan_make = make.getText().toString();
                                                        scan_serial = serial.getText().toString();
                                                        scan_equip = equp_id.getText().toString();

                                                        Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                        Sessiondata.getInstance().setUnitId(scan_equip);
                                                        Sessiondata.getInstance().setMake(scan_make);
                                                        Sessiondata.getInstance().setModel(scan_model);
                                                        Sessiondata.getInstance().setSerial(scan_serial);
                                                        int processId = Integer.parseInt(split_string(scan_processid));
                                                        Sessiondata.getInstance().setSub_process(scan_processid);
                                                        Sessiondata.getInstance().setProcessId(processId);
                                                        Sessiondata.getInstance().setInventory_equip_id(1);

                                                        Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                        Sessiondata.getInstance().setTemp_make(scan_make);
                                                        Sessiondata.getInstance().setTemp_model(scan_model);
                                                        Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                        Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                        sDialog.dismiss();

                                                    }

                                                });

                                        dialog_notmatch.setCancelable(false);
                                        dialog_notmatch.show();
                                    }
                                }

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                    Sessiondata.getInstance().setInventory_dialoghandle(0);
                                }
                            } else if (!equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                    model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                    process_ids = split_string(process_list);
                                    equp_ids = equp_id.getText().toString();
                                    models = model.getText().toString();
                                    makes = make.getText().toString();
                                    serials = serial.getText().toString();

                                    sub_processid = split_string(process_list);

                                    if (sweetalrt_notmatch) {
                                        sweetalrt_notmatch = false;
                                        dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                .setTitleText("Message!")
                                                .setContentText("Equipment is not in PID, Do you want to add it and Count?")
                                                .setCancelText("No")
                                                .setConfirmText("Yes")
                                                .showCancelButton(true)

                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;
                                                        sDialog.dismiss();
                                                    }
                                                })

                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;

//                                                        scan_processid = split_string(process_list);
                                                        scan_processid = processid.getText().toString();

                                                        scan_model = model.getText().toString();
                                                        scan_make = make.getText().toString();
                                                        scan_serial = serial.getText().toString();
                                                        scan_equip = equp_id.getText().toString();

                                                        Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                        Sessiondata.getInstance().setUnitId(scan_equip);
                                                        Sessiondata.getInstance().setMake(scan_make);
                                                        Sessiondata.getInstance().setModel(scan_model);
                                                        Sessiondata.getInstance().setSerial(scan_serial);
                                                        int processId = Integer.parseInt(split_string(scan_processid));
                                                        Sessiondata.getInstance().setSub_process(scan_processid);
                                                        Sessiondata.getInstance().setProcessId(processId);
                                                        Sessiondata.getInstance().setInventory_equip_id(1);

                                                        Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                        Sessiondata.getInstance().setTemp_make(scan_make);
                                                        Sessiondata.getInstance().setTemp_model(scan_model);
                                                        Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                        Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                        sDialog.dismiss();

                                                    }

                                                });

                                        dialog_notmatch.setCancelable(false);
                                        dialog_notmatch.show();
                                    }
                                }

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                    Sessiondata.getInstance().setInventory_dialoghandle(0);
                                }
                            } else if (!equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                    model_text.toString().isEmpty() && serial_text.toString().isEmpty()) {

                                btn_submit.setEnabled(true);
//                                submit_layout.setEnabled(true);
//                                submit_layout.setVisibility(View.VISIBLE);
                                btn_submit.setVisibility(View.VISIBLE);
                                /*footer.setVisibility(View.VISIBLE);*/

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {
                                    if (alert == 1) {
                                        if (sweetalrt_notmatch) {
                                            sweetalrt_notmatch = false;
                                            dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                    .setTitleText("Message!")
                                                    .setContentText("Scanned equipment does not exist. Do you want to re-scan?")
                                                    .setCancelText("No")
                                                    .setConfirmText("Re-Scan")
                                                    .showCancelButton(true)

                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_notmatch = true;
                                                            sDialog.dismiss();

                                                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                                                process_ids = split_string(process_list);
                                                                equp_ids = equp_id.getText().toString();
                                                                models = model.getText().toString();
                                                                makes = make.getText().toString();
                                                                serials = serial.getText().toString();

                                                                sub_processid = split_string(process_list);

                                                                if (sweetalrt_notmatch) {
                                                                    sweetalrt_notmatch = false;
                                                                    dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                            .setTitleText("Message!")
                                                                            .setContentText("Equipment is not in PID, Do you want to add it and Count?")
                                                                            .setCancelText("No")
                                                                            .setConfirmText("Yes")
                                                                            .showCancelButton(true)

                                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_notmatch = true;
                                                                                    sDialog.dismiss();
                                                                                }
                                                                            })

                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_notmatch = true;

//                                                                                    scan_processid = split_string(process_list);
                                                                                    scan_processid = processid.getText().toString();

                                                                                    scan_model = model.getText().toString();
                                                                                    scan_make = make.getText().toString();
                                                                                    scan_serial = serial.getText().toString();
                                                                                    scan_equip = equp_id.getText().toString();

                                                                                    Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                                                    Sessiondata.getInstance().setUnitId(scan_equip);
                                                                                    Sessiondata.getInstance().setMake(scan_make);
                                                                                    Sessiondata.getInstance().setModel(scan_model);
                                                                                    Sessiondata.getInstance().setSerial(scan_serial);
                                                                                    int processId = Integer.parseInt(split_string(scan_processid));
                                                                                    Sessiondata.getInstance().setSub_process(scan_processid);
                                                                                    Sessiondata.getInstance().setProcessId(processId);
                                                                                    Sessiondata.getInstance().setInventory_equip_id(1);

                                                                                    Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                                                    Sessiondata.getInstance().setTemp_make(scan_make);
                                                                                    Sessiondata.getInstance().setTemp_model(scan_model);
                                                                                    Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                                                    Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                                                    startActivity(intent);
                                                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                                                    sDialog.dismiss();

                                                                                }

                                                                            });

                                                                    dialog_notmatch.setCancelable(false);
                                                                    dialog_notmatch.show();
                                                                }
                                                            }

                                                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                                                Sessiondata.getInstance().setInventory_dialoghandle(0);
                                                            }
                                                        }
                                                    })

                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_notmatch = true;

                                                            equp_id.setText("");

                                                            Sessiondata.getInstance().setUnitId("");

                                                            Sessiondata.getInstance().setEqup_scanner(0);
                                                            Sessiondata.getInstance().setScanner_inventory(6);
                                                            Sessiondata.getInstance().setInventory_scanner("start");

                                                            scanne_value = 1;

//                                                            scan_processid = split_string(process_list);
                                                            scan_processid = processid.getText().toString();

                                                            scan_model = model.getText().toString();
                                                            scan_make = make.getText().toString();
                                                            scan_serial = serial.getText().toString();
                                                            scan_equip = equp_id.getText().toString();

                                                            Sessiondata.getInstance().setModel(scan_model);
                                                            Sessiondata.getInstance().setSerial(scan_serial);
                                                            Sessiondata.getInstance().setMake(scan_make);
                                                            Sessiondata.getInstance().setSub_process(scan_processid);
                                                            Sessiondata.getInstance().setInventory_equip_id(1);
                                                            Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());

                                                            Sessiondata.getInstance().setScanner_partreceipt(0);
                                                            Sessiondata.getInstance().setScanner_partreceiving(0);
                                                            Sessiondata.getInstance().setScanner_replace(0);
                                                            Sessiondata.getInstance().setScanner_counting1(0);
                                                            Sessiondata.getInstance().setScanner_counting2(0);

                                                            Sessiondata.getInstance().setScanner_partnumber(0);
                                                            Sessiondata.getInstance().setScanner_hwstartbin(0);
                                                            Sessiondata.getInstance().setScanner_hwendbin(0);

                                                            launchActivity(SimpleScannerActivity.class);

                                                            sDialog.dismiss();

                                                        }

                                                    });

                                            dialog_notmatch.setCancelable(false);
                                            dialog_notmatch.show();
                                        }
                                    } else {
                                        if (sweetalrt_notmatch) {
                                            sweetalrt_notmatch = false;
                                            dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                    .setTitleText("Message!")
                                                    .setContentText("Entered equipment does not exist. Would you like to re-enter?")
                                                    .setCancelText("No")
                                                    .setConfirmText("Re-Enter")
                                                    .showCancelButton(true)

                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_notmatch = true;
                                                            sDialog.dismiss();

                                                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                                                process_ids = split_string(process_list);
                                                                equp_ids = equp_id.getText().toString();
                                                                models = model.getText().toString();
                                                                makes = make.getText().toString();
                                                                serials = serial.getText().toString();

                                                                sub_processid = split_string(process_list);

                                                                if (sweetalrt_notmatch) {
                                                                    sweetalrt_notmatch = false;
                                                                    dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                            .setTitleText("Message!")
                                                                            .setContentText("Equipment is not in PID, Do you want to add it and Count?")
                                                                            .setCancelText("No")
                                                                            .setConfirmText("Yes")
                                                                            .showCancelButton(true)

                                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_notmatch = true;
                                                                                    sDialog.dismiss();
                                                                                }
                                                                            })

                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_notmatch = true;

//                                                                                    scan_processid = split_string(process_list);
                                                                                    scan_processid = processid.getText().toString();

                                                                                    scan_model = model.getText().toString();
                                                                                    scan_make = make.getText().toString();
                                                                                    scan_serial = serial.getText().toString();
                                                                                    scan_equip = equp_id.getText().toString();

                                                                                    Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                                                    Sessiondata.getInstance().setUnitId(scan_equip);
                                                                                    Sessiondata.getInstance().setMake(scan_make);
                                                                                    Sessiondata.getInstance().setModel(scan_model);
                                                                                    Sessiondata.getInstance().setSerial(scan_serial);
                                                                                    int processId = Integer.parseInt(split_string(scan_processid));
                                                                                    Sessiondata.getInstance().setSub_process(scan_processid);
                                                                                    Sessiondata.getInstance().setProcessId(processId);
                                                                                    Sessiondata.getInstance().setInventory_equip_id(1);

                                                                                    Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                                                    Sessiondata.getInstance().setTemp_make(scan_make);
                                                                                    Sessiondata.getInstance().setTemp_model(scan_model);
                                                                                    Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                                                    Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                                                    startActivity(intent);
                                                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                                                    sDialog.dismiss();

                                                                                }

                                                                            });

                                                                    dialog_notmatch.setCancelable(false);
                                                                    dialog_notmatch.show();
                                                                }
                                                            }

                                                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                                                Sessiondata.getInstance().setInventory_dialoghandle(0);
                                                            }
                                                        }
                                                    })

                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_notmatch = true;

                                                            equp_id.setText("");
                                                            Sessiondata.getInstance().setUnitId("");
                                                            Sessiondata.getInstance().setTemp_unitId("");

                                                            sDialog.dismiss();

                                                        }

                                                    });

                                            dialog_notmatch.setCancelable(false);
                                            dialog_notmatch.show();
                                        }
                                    }
                                }
                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                    Sessiondata.getInstance().setInventory_dialoghandle(0);
                                }

                            } else if (equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                    model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                    process_ids = split_string(process_list);
                                    equp_ids = equp_id.getText().toString();
                                    models = model.getText().toString();
                                    makes = make.getText().toString();
                                    serials = serial.getText().toString();

                                    sub_processid = split_string(process_list);

                                    if (sweetalrt_notmatch) {
                                        sweetalrt_notmatch = false;
                                        dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                .setTitleText("Message!")
                                                .setContentText("Equipment is not in PID, Do you want to add it and Count?")
                                                .setCancelText("No")
                                                .setConfirmText("Yes")
                                                .showCancelButton(true)

                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;
                                                        sDialog.dismiss();
                                                    }
                                                })

                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;

//                                                        scan_processid = split_string(process_list);
                                                        scan_processid = processid.getText().toString();


                                                        scan_model = model.getText().toString();
                                                        scan_make = make.getText().toString();
                                                        scan_serial = serial.getText().toString();
                                                        scan_equip = equp_id.getText().toString();

                                                        Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                        Sessiondata.getInstance().setUnitId(scan_equip);
                                                        Sessiondata.getInstance().setMake(scan_make);
                                                        Sessiondata.getInstance().setModel(scan_model);
                                                        Sessiondata.getInstance().setSerial(scan_serial);
                                                        int processId = Integer.parseInt(split_string(scan_processid));
                                                        Sessiondata.getInstance().setSub_process(scan_processid);
                                                        Sessiondata.getInstance().setProcessId(processId);
                                                        Sessiondata.getInstance().setInventory_equip_id(1);

                                                        Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                        Sessiondata.getInstance().setTemp_make(scan_make);
                                                        Sessiondata.getInstance().setTemp_model(scan_model);
                                                        Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                        Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                        sDialog.dismiss();

                                                    }

                                                });

                                        dialog_notmatch.setCancelable(false);
                                        dialog_notmatch.show();
                                    }
                                }

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                    Sessiondata.getInstance().setInventory_dialoghandle(0);
                                }
                            }
                        }

                    } else {

                        handleasync = true;
                        GlobalVariables.checkbtn_load = 1;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            btn_counted.setBackground(blue_drawable);
                            btn_uncounted.setBackground(grey_drawable);
                            btn_counted.setTextColor(getResources().getColor(R.color.white));
                            btn_uncounted.setTextColor(getResources().getColor(R.color.black));
                        } else {
                            btn_counted.setBackgroundDrawable(blue_drawable);
                            btn_uncounted.setBackgroundDrawable(grey_drawable);
                            btn_counted.setTextColor(getResources().getColor(R.color.white));
                            btn_uncounted.setTextColor(getResources().getColor(R.color.black));
                        }

                        btn_submit.setEnabled(true);
                /*        submit_layout.setEnabled(false);
                        submit_layout.setVisibility(View.GONE);*/
                        btn_submit.setVisibility(View.VISIBLE);
                        /*footer.setVisibility(View.GONE);*/

                        ArrayList<GetEquipmentList> equimlist = new ArrayList<>();
                        for (int i = 0; i < equip_list.size(); i++) {
                            String eqstatus = equip_list.get(i).getEqpstatus();
                            Log.d("eqstatus_list_many", "" + eqstatus);

                            if (!eqstatus.toString().contains("Counted")) {

                                equimlist.add(equip_list.get(i));


                            }

                        }
                        if (equimlist.size() == 0) {
                            new AsyncGetEquipmentListCounted().execute();
                        } else {
                            equip_list = equimlist;
                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        }

                        equip_list = equimlist;
                        adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                        equp_list.setAdapter(adapter);
                    }

                    adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                    equp_list.setAdapter(adapter);
                }

            } else {
                handleasync = true;
            }

        }
    }

    public class AsyncGetEquipmentListUncountFilter extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);
                int process_ids_text = Integer.parseInt(split_string(process_ids));

                equip_list = WebServiceConsumer.getInstance().GetEquipmentListV4(user_token, process_ids_text, equipid_text, make_text, model_text, serial_text);
                Log.d("equip_list", equip_list + "");


            } catch (SocketTimeoutException e) {
                equip_list = null;
            } catch (Exception e) {
                equip_list = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Sessiondata.getInstance().setGetEquipmentLists(equip_list);
            ProgressBar.dismiss();

            String pid = processid.getText().toString();
            Sessiondata.getInstance().setSub_process(pid);

            Log.d("GlobalVariables ", "UnCountFilter " + GlobalVariables.showerrormsg + " checkbtn_load " + GlobalVariables.checkbtn_load);


            if (equip_list != null) {

                if (equip_list.size() == 1) {

                    if (equip_list.get(0).getMessage().length() != 0) {
                        handleasync = true;

                        String Result = equip_list.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (equip_list.get(0).getMessage().toString().contains("Session")) {
                            Session = 3;
                            if (return_once == 0) {
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }
                            }

                            if (return_once == 1) {
                                return_once = 0;
                            }
                        }
                    } else {

                        handleasync = true;
                        GlobalVariables.checkbtn_load = 0;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            btn_counted.setBackground(grey_drawable);
                            btn_uncounted.setBackground(blue_drawable);
                            btn_counted.setTextColor(getResources().getColor(R.color.black));
                            btn_uncounted.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            btn_counted.setBackgroundDrawable(grey_drawable);
                            btn_uncounted.setBackgroundDrawable(blue_drawable);
                            btn_counted.setTextColor(getResources().getColor(R.color.black));
                            btn_uncounted.setTextColor(getResources().getColor(R.color.white));
                        }

                        String eqstatus = equip_list.get(0).getEqpstatus();
                        Log.d("eqstatus_list_single3", "" + eqstatus);

                        if (eqstatus.toString().contains("Counted")) {
                            btn_submit.setEnabled(true);
                           /* submit_layout.setEnabled(false);
                            submit_layout.setVisibility(View.GONE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.GONE);*/

                            equip_list = new ArrayList<>();
                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                            new AsyncGetEquipmentListCountedFilter().execute();

                        } else {
                            btn_submit.setEnabled(true);
//                            submit_layout.setEnabled(false);
//                            submit_layout.setVisibility(View.GONE);
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.GONE);*/

                            equip_list = new ArrayList<>();
                            equip_list = Sessiondata.getInstance().getGetEquipmentLists();

                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        }
                    }

                } else {
                    equip_list = new ArrayList<>();
                    equip_list = Sessiondata.getInstance().getGetEquipmentLists();


                    if (equip_list.size() == 0) {

                        if (GlobalVariables.showerrormsg == true) {
                            if (checkInternetConenction()) {
                                Log.d("UncountFilter", "true");
                                GlobalVariables.showerrormsg = false;

                                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                                process_ids = split_string(process_list);
                                equipid_text = equp_id.getText().toString();
                                model_text = model.getText().toString();
                                make_text = make.getText().toString();
                                serial_text = serial.getText().toString();

                                new AsyncGetEquipmentListCountedFilter().execute();

                            } else {
                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            handleasync = true;
                            GlobalVariables.checkbtn_load = 1;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                btn_counted.setBackground(blue_drawable);
                                btn_uncounted.setBackground(grey_drawable);
                                btn_counted.setTextColor(getResources().getColor(R.color.white));
                                btn_uncounted.setTextColor(getResources().getColor(R.color.black));
                            } else {
                                btn_counted.setBackgroundDrawable(blue_drawable);
                                btn_uncounted.setBackgroundDrawable(grey_drawable);
                                btn_counted.setTextColor(getResources().getColor(R.color.white));
                                btn_uncounted.setTextColor(getResources().getColor(R.color.black));
                            }

                            Log.d("UncountFilter", "false");
                            btn_submit.setEnabled(true);
                        /*    submit_layout.setEnabled(true);
                            submit_layout.setVisibility(View.VISIBLE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.VISIBLE);*/

                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);

                            if (!equipid_text.toString().isEmpty() && !make_text.toString().isEmpty() &&
                                    !model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                    process_ids = split_string(process_list);
                                    equp_ids = equp_id.getText().toString();
                                    models = model.getText().toString();
                                    makes = make.getText().toString();
                                    serials = serial.getText().toString();

                                    sub_processid = split_string(process_list);

                                    if (sweetalrt_notmatch) {
                                        sweetalrt_notmatch = false;
                                        dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                .setTitleText("Message!")
                                                .setContentText("Does not exist, Do you want to add to Hand Writes?")
                                                .setCancelText("No")
                                                .setConfirmText("Yes")
                                                .showCancelButton(true)

                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;
                                                        sDialog.dismiss();
                                                    }
                                                })

                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;

//                                                        scan_processid = split_string(process_list);
                                                        scan_processid = processid.getText().toString();

                                                        scan_model = model.getText().toString();
                                                        scan_make = make.getText().toString();
                                                        scan_serial = serial.getText().toString();
                                                        scan_equip = equp_id.getText().toString();


                                                        Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                        Sessiondata.getInstance().setUnitId(scan_equip);
                                                        Sessiondata.getInstance().setMake(scan_make);
                                                        Sessiondata.getInstance().setModel(scan_model);
                                                        Sessiondata.getInstance().setSerial(scan_serial);
                                                        int processId = Integer.parseInt(split_string(scan_processid));
                                                        Sessiondata.getInstance().setSub_process(scan_processid);
                                                        Sessiondata.getInstance().setProcessId(processId);
                                                        Sessiondata.getInstance().setInventory_equip_id(1);

                                                        Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                        Sessiondata.getInstance().setTemp_make(scan_make);
                                                        Sessiondata.getInstance().setTemp_model(scan_model);
                                                        Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                        Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                        sDialog.dismiss();

                                                    }

                                                });

                                        dialog_notmatch.setCancelable(false);
                                        dialog_notmatch.show();
                                    }
                                }

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                    Sessiondata.getInstance().setInventory_dialoghandle(0);
                                }
                            } else if (!equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                    model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                    process_ids = split_string(process_list);
                                    equp_ids = equp_id.getText().toString();
                                    models = model.getText().toString();
                                    makes = make.getText().toString();
                                    serials = serial.getText().toString();

                                    sub_processid = split_string(process_list);

                                    if (sweetalrt_notmatch) {
                                        sweetalrt_notmatch = false;
                                        dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                .setTitleText("Message!")
                                                .setContentText("Equipment is not in PID, Do you want to add it and Count?")
                                                .setCancelText("No")
                                                .setConfirmText("Yes")
                                                .showCancelButton(true)

                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;
                                                        sDialog.dismiss();
                                                    }
                                                })

                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;

//                                                        scan_processid = split_string(process_list);
                                                        scan_processid = processid.getText().toString();

                                                        scan_model = model.getText().toString();
                                                        scan_make = make.getText().toString();
                                                        scan_serial = serial.getText().toString();
                                                        scan_equip = equp_id.getText().toString();

                                                        Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                        Sessiondata.getInstance().setUnitId(scan_equip);
                                                        Sessiondata.getInstance().setMake(scan_make);
                                                        Sessiondata.getInstance().setModel(scan_model);
                                                        Sessiondata.getInstance().setSerial(scan_serial);
                                                        int processId = Integer.parseInt(split_string(scan_processid));
                                                        Sessiondata.getInstance().setSub_process(scan_processid);
                                                        Sessiondata.getInstance().setProcessId(processId);
                                                        Sessiondata.getInstance().setInventory_equip_id(1);

                                                        Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                        Sessiondata.getInstance().setTemp_make(scan_make);
                                                        Sessiondata.getInstance().setTemp_model(scan_model);
                                                        Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                        Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                        sDialog.dismiss();

                                                    }

                                                });

                                        dialog_notmatch.setCancelable(false);
                                        dialog_notmatch.show();
                                    }
                                }

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                    Sessiondata.getInstance().setInventory_dialoghandle(0);
                                }
                            } else if (!equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                    model_text.toString().isEmpty() && serial_text.toString().isEmpty()) {

                                btn_submit.setEnabled(true);
                               /* submit_layout.setEnabled(true);
                                submit_layout.setVisibility(View.VISIBLE);*/
                                btn_submit.setVisibility(View.VISIBLE);
                                /*footer.setVisibility(View.VISIBLE);*/

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {
                                    if (alert == 1) {
                                        if (sweetalrt_notmatch) {
                                            sweetalrt_notmatch = false;
                                            dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                    .setTitleText("Message!")
                                                    .setContentText("Scanned equipment does not exist. Do you want to re-scan?")
                                                    .setCancelText("No")
                                                    .setConfirmText("Re-Scan")
                                                    .showCancelButton(true)

                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_notmatch = true;
                                                            sDialog.dismiss();

                                                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                                                process_ids = split_string(process_list);
                                                                equp_ids = equp_id.getText().toString();
                                                                models = model.getText().toString();
                                                                makes = make.getText().toString();
                                                                serials = serial.getText().toString();

                                                                sub_processid = split_string(process_list);

                                                                if (sweetalrt_notmatch) {
                                                                    sweetalrt_notmatch = false;
                                                                    dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                            .setTitleText("Message!")
                                                                            .setContentText("Equipment is not in PID, Do you want to add it and Count?")
                                                                            .setCancelText("No")
                                                                            .setConfirmText("Yes")
                                                                            .showCancelButton(true)

                                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_notmatch = true;
                                                                                    sDialog.dismiss();
                                                                                }
                                                                            })

                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_notmatch = true;

//                                                                                    scan_processid = split_string(process_list);
                                                                                    scan_processid = processid.getText().toString();

                                                                                    scan_model = model.getText().toString();
                                                                                    scan_make = make.getText().toString();
                                                                                    scan_serial = serial.getText().toString();
                                                                                    scan_equip = equp_id.getText().toString();

                                                                                    Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                                                    Sessiondata.getInstance().setUnitId(scan_equip);
                                                                                    Sessiondata.getInstance().setMake(scan_make);
                                                                                    Sessiondata.getInstance().setModel(scan_model);
                                                                                    Sessiondata.getInstance().setSerial(scan_serial);
                                                                                    int processId = Integer.parseInt(split_string(scan_processid));
                                                                                    Sessiondata.getInstance().setSub_process(scan_processid);
                                                                                    Sessiondata.getInstance().setProcessId(processId);
                                                                                    Sessiondata.getInstance().setInventory_equip_id(1);

                                                                                    Sessiondata.getInstance().setTemp_unitId(equp_ids);
                                                                                    Sessiondata.getInstance().setTemp_make(scan_make);
                                                                                    Sessiondata.getInstance().setTemp_model(scan_model);
                                                                                    Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                                                    Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                                                    startActivity(intent);
                                                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                                                    sDialog.dismiss();

                                                                                }

                                                                            });

                                                                    dialog_notmatch.setCancelable(false);
                                                                    dialog_notmatch.show();
                                                                }
                                                            }

                                                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                                                Sessiondata.getInstance().setInventory_dialoghandle(0);
                                                            }
                                                        }
                                                    })

                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_notmatch = true;

                                                            equp_id.setText("");

                                                            Sessiondata.getInstance().setUnitId("");

                                                            Sessiondata.getInstance().setEqup_scanner(0);
                                                            Sessiondata.getInstance().setScanner_inventory(6);
                                                            Sessiondata.getInstance().setInventory_scanner("start");

                                                            scanne_value = 1;

//                                                            scan_processid = split_string(process_list);
                                                            scan_processid = processid.getText().toString();

                                                            scan_model = model.getText().toString();
                                                            scan_make = make.getText().toString();
                                                            scan_serial = serial.getText().toString();
                                                            scan_equip = equp_id.getText().toString();

                                                            Sessiondata.getInstance().setModel(scan_model);
                                                            Sessiondata.getInstance().setSerial(scan_serial);
                                                            Sessiondata.getInstance().setMake(scan_make);
                                                            Sessiondata.getInstance().setSub_process(scan_processid);
                                                            Sessiondata.getInstance().setInventory_equip_id(1);
                                                            Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());

                                                            Sessiondata.getInstance().setScanner_partreceipt(0);
                                                            Sessiondata.getInstance().setScanner_partreceiving(0);
                                                            Sessiondata.getInstance().setScanner_replace(0);
                                                            Sessiondata.getInstance().setScanner_counting1(0);
                                                            Sessiondata.getInstance().setScanner_counting2(0);

                                                            Sessiondata.getInstance().setScanner_partnumber(0);
                                                            Sessiondata.getInstance().setScanner_hwstartbin(0);
                                                            Sessiondata.getInstance().setScanner_hwendbin(0);

                                                            launchActivity(SimpleScannerActivity.class);

                                                            sDialog.dismiss();

                                                        }

                                                    });

                                            dialog_notmatch.setCancelable(false);
                                            dialog_notmatch.show();
                                        }
                                    } else {
                                        if (sweetalrt_notmatch) {
                                            sweetalrt_notmatch = false;
                                            dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                    .setTitleText("Message!")
                                                    .setContentText("Entered equipment does not exist. Would you like to re-enter?")
                                                    .setCancelText("No")
                                                    .setConfirmText("Re-Enter")
                                                    .showCancelButton(true)

                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_notmatch = true;
                                                            sDialog.dismiss();

                                                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                                                process_ids = split_string(process_list);
                                                                equp_ids = equp_id.getText().toString();
                                                                models = model.getText().toString();
                                                                makes = make.getText().toString();
                                                                serials = serial.getText().toString();

                                                                sub_processid = split_string(process_list);

                                                                if (sweetalrt_notmatch) {
                                                                    sweetalrt_notmatch = false;
                                                                    dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                                            .setTitleText("Message!")
                                                                            .setContentText("Equipment is not in PID, Do you want to add it and Count?")
                                                                            .setCancelText("No")
                                                                            .setConfirmText("Yes")
                                                                            .showCancelButton(true)

                                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_notmatch = true;
                                                                                    sDialog.dismiss();
                                                                                }
                                                                            })

                                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                @Override
                                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                                    sweetalrt_notmatch = true;

//                                                                                    scan_processid = split_string(process_list);
                                                                                    scan_processid = processid.getText().toString();

                                                                                    scan_model = model.getText().toString();
                                                                                    scan_make = make.getText().toString();
                                                                                    scan_serial = serial.getText().toString();
                                                                                    scan_equip = equp_id.getText().toString();

                                                                                    Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                                                    Sessiondata.getInstance().setUnitId(scan_equip);
                                                                                    Sessiondata.getInstance().setMake(scan_make);
                                                                                    Sessiondata.getInstance().setModel(scan_model);
                                                                                    Sessiondata.getInstance().setSerial(scan_serial);
                                                                                    int processId = Integer.parseInt(split_string(scan_processid));
                                                                                    Sessiondata.getInstance().setSub_process(scan_processid);
                                                                                    Sessiondata.getInstance().setProcessId(processId);
                                                                                    Sessiondata.getInstance().setInventory_equip_id(1);

                                                                                    Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                                                    Sessiondata.getInstance().setTemp_make(scan_make);
                                                                                    Sessiondata.getInstance().setTemp_model(scan_model);
                                                                                    Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                                                    Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                                                    startActivity(intent);
                                                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                                                    sDialog.dismiss();

                                                                                }

                                                                            });

                                                                    dialog_notmatch.setCancelable(false);
                                                                    dialog_notmatch.show();
                                                                }
                                                            }

                                                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                                                Sessiondata.getInstance().setInventory_dialoghandle(0);
                                                            }
                                                        }
                                                    })

                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt_notmatch = true;

                                                            equp_id.setText("");
                                                            Sessiondata.getInstance().setUnitId("");
                                                            Sessiondata.getInstance().setTemp_unitId("");

                                                            sDialog.dismiss();

                                                        }

                                                    });

                                            dialog_notmatch.setCancelable(false);
                                            dialog_notmatch.show();
                                        }
                                    }
                                }
                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                    Sessiondata.getInstance().setInventory_dialoghandle(0);
                                }

                            } else if (equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                    model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                    process_ids = split_string(process_list);
                                    equp_ids = equp_id.getText().toString();
                                    models = model.getText().toString();
                                    makes = make.getText().toString();
                                    serials = serial.getText().toString();

                                    sub_processid = split_string(process_list);

                                    if (sweetalrt_notmatch) {
                                        sweetalrt_notmatch = false;
                                        dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                                .setTitleText("Message!")
                                                .setContentText("Equipment is not in PID, Do you want to add it and Count?")
                                                .setCancelText("No")
                                                .setConfirmText("Yes")
                                                .showCancelButton(true)

                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;
                                                        sDialog.dismiss();
                                                    }
                                                })

                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt_notmatch = true;

//                                                        scan_processid = split_string(process_list);
                                                        scan_processid = processid.getText().toString();

                                                        scan_model = model.getText().toString();
                                                        scan_make = make.getText().toString();
                                                        scan_serial = serial.getText().toString();
                                                        scan_equip = equp_id.getText().toString();

                                                        Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                        Sessiondata.getInstance().setUnitId(scan_equip);
                                                        Sessiondata.getInstance().setMake(scan_make);
                                                        Sessiondata.getInstance().setModel(scan_model);
                                                        Sessiondata.getInstance().setSerial(scan_serial);
                                                        int processId = Integer.parseInt(split_string(scan_processid));
                                                        Sessiondata.getInstance().setSub_process(scan_processid);
                                                        Sessiondata.getInstance().setProcessId(processId);
                                                        Sessiondata.getInstance().setInventory_equip_id(1);

                                                        Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                        Sessiondata.getInstance().setTemp_make(scan_make);
                                                        Sessiondata.getInstance().setTemp_model(scan_model);
                                                        Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                        Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                        sDialog.dismiss();

                                                    }

                                                });

                                        dialog_notmatch.setCancelable(false);
                                        dialog_notmatch.show();
                                    }
                                }

                                if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                    Sessiondata.getInstance().setInventory_dialoghandle(0);
                                }
                            }
                        }

                    } else {
                        handleasync = true;
                        GlobalVariables.checkbtn_load = 0;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            btn_counted.setBackground(grey_drawable);
                            btn_uncounted.setBackground(blue_drawable);
                            btn_counted.setTextColor(getResources().getColor(R.color.black));
                            btn_uncounted.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            btn_counted.setBackgroundDrawable(grey_drawable);
                            btn_uncounted.setBackgroundDrawable(blue_drawable);
                            btn_counted.setTextColor(getResources().getColor(R.color.black));
                            btn_uncounted.setTextColor(getResources().getColor(R.color.white));
                        }

                        btn_submit.setEnabled(true);
                       /* submit_layout.setEnabled(false);
                        submit_layout.setVisibility(View.GONE);*/
                        btn_submit.setVisibility(View.VISIBLE);
                        /*footer.setVisibility(View.GONE);*/

                        ArrayList<GetEquipmentList> equimlist = new ArrayList<>();

                        if (equip_list != null) {
                            for (int i = 0; i < equip_list.size(); i++) {
                                String eqstatus = equip_list.get(i).getEqpstatus();
                                Log.d("eqstatus_list_many", "" + eqstatus);

                                if (!eqstatus.toString().contains("Counted")) {

                                    equimlist.add(equip_list.get(i));


                                }

                            }
                        }


                        if (equimlist.size() == 0) {
                            new AsyncGetEquipmentListCounted().execute();
                        } else {
                            equip_list = equimlist;
                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        }

                        equip_list = equimlist;
                        adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                        equp_list.setAdapter(adapter);
                    }

                    adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                    equp_list.setAdapter(adapter);
                }

            } else {
                handleasync = true;
            }

        }
    }

    public class AsyncGetEquipmentListCounted extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);
                int process_ids_text = Integer.parseInt(process_ids);

                equip_list = WebServiceConsumer.getInstance().GetEquipmentCountedList(user_token, process_ids_text, equipid_text, make_text, model_text, serial_text);

            } catch (SocketTimeoutException e) {
                equip_list = null;
            } catch (Exception e) {
                equip_list = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Sessiondata.getInstance().setGetEquipmentLists(equip_list);
            ProgressBar.dismiss();

            String pid = processid.getText().toString();
            Sessiondata.getInstance().setSub_process(pid);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btn_counted.setBackground(blue_drawable);
                btn_uncounted.setBackground(grey_drawable);
                btn_counted.setTextColor(Color.WHITE);
                btn_uncounted.setTextColor(Color.BLACK);
            } else {
                btn_counted.setBackgroundDrawable(blue_drawable);
                btn_uncounted.setBackgroundDrawable(grey_drawable);
                btn_counted.setTextColor(Color.WHITE);
                btn_uncounted.setTextColor(Color.BLACK);
            }

            if (equip_list != null) {

                if (equip_list.size() == 1) {

                    if (equip_list.get(0).getMessage().length() != 0) {

                        String Result = equip_list.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (equip_list.get(0).getMessage().toString().contains("Session")) {
                            Session = 15;
                            if (return_once == 0) {
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }
                            }

                            if (return_once == 1) {
                                return_once = 0;
                            }
                        }
                    } else {

                        String eqstatus = equip_list.get(0).getEqpstatus();
                        Log.d("eqstatus_list_single4", "" + eqstatus);

                        if (!eqstatus.toString().contains("Counted")) {
                            btn_submit.setEnabled(true);
                           /* submit_layout.setEnabled(false);
                            submit_layout.setVisibility(View.GONE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.GONE);*/

                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        } else {
                            btn_submit.setEnabled(true);
                           /* submit_layout.setEnabled(false);
                            submit_layout.setVisibility(View.GONE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.GONE);*/

                            equip_list = new ArrayList<>();
                            equip_list = Sessiondata.getInstance().getGetEquipmentLists();

                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        }

                    }

                } else {
                    equip_list = new ArrayList<>();
                    equip_list = Sessiondata.getInstance().getGetEquipmentLists();


                    if (equip_list.size() == 0) {

                        btn_submit.setEnabled(true);
                       /* submit_layout.setEnabled(true);
                        submit_layout.setVisibility(View.VISIBLE);*/
                        btn_submit.setVisibility(View.VISIBLE);
                        /*footer.setVisibility(View.VISIBLE);*/

                        adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                        equp_list.setAdapter(adapter);

                        if (!equipid_text.toString().isEmpty() && !make_text.toString().isEmpty() &&
                                !model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {

                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                process_ids = split_string(process_list);
                                equp_ids = equp_id.getText().toString();
                                models = model.getText().toString();
                                makes = make.getText().toString();
                                serials = serial.getText().toString();

                                sub_processid = split_string(process_list);

                                if (sweetalrt_notmatch) {
                                    sweetalrt_notmatch = false;
                                    dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Does not exist, Do you want to add to Hand Writes?")
                                            .setCancelText("No")
                                            .setConfirmText("Yes")
                                            .showCancelButton(true)

                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sweetalrt_notmatch = true;
                                                    sDialog.dismiss();
                                                }
                                            })

                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sweetalrt_notmatch = true;


//                                                    scan_processid = split_string(process_list);
                                                    scan_processid = processid.getText().toString();

                                                    scan_model = model.getText().toString();
                                                    scan_make = make.getText().toString();
                                                    scan_serial = serial.getText().toString();
                                                    scan_equip = equp_id.getText().toString();


                                                    Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setUnitId(scan_equip);
                                                    Sessiondata.getInstance().setMake(scan_make);
                                                    Sessiondata.getInstance().setModel(scan_model);
                                                    Sessiondata.getInstance().setSerial(scan_serial);
                                                    int processId = Integer.parseInt(split_string(scan_processid));
                                                    Sessiondata.getInstance().setSub_process(scan_processid);
                                                    Sessiondata.getInstance().setProcessId(processId);
                                                    Sessiondata.getInstance().setInventory_equip_id(1);

                                                    Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                    Sessiondata.getInstance().setTemp_make(scan_make);
                                                    Sessiondata.getInstance().setTemp_model(scan_model);
                                                    Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                    Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                    sDialog.dismiss();

                                                }

                                            });

                                    dialog_notmatch.setCancelable(false);
                                    dialog_notmatch.show();
                                }
                            }

                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                Sessiondata.getInstance().setInventory_dialoghandle(0);
                            }
                        } else if (!equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {


                        } else if (!equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                model_text.toString().isEmpty() && serial_text.toString().isEmpty()) {


                            btn_submit.setEnabled(true);
                          /*  submit_layout.setEnabled(true);
                            submit_layout.setVisibility(View.VISIBLE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.VISIBLE);*/

                        } else if (equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {

                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                process_ids = split_string(process_list);
                                equp_ids = equp_id.getText().toString();
                                models = model.getText().toString();
                                makes = make.getText().toString();
                                serials = serial.getText().toString();

                                sub_processid = split_string(process_list);

                                if (sweetalrt_notmatch) {
                                    sweetalrt_notmatch = false;
                                    dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Equipment is not in PID, Do you want to add it and Count?")
                                            .setCancelText("No")
                                            .setConfirmText("Yes")
                                            .showCancelButton(true)

                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sweetalrt_notmatch = true;
                                                    sDialog.dismiss();
                                                }
                                            })

                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sweetalrt_notmatch = true;

                                                    scan_processid = split_string(process_list);
                                                    scan_model = model.getText().toString();
                                                    scan_make = make.getText().toString();
                                                    scan_serial = serial.getText().toString();
                                                    scan_equip = equp_id.getText().toString();

                                                    Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setUnitId(scan_equip);
                                                    Sessiondata.getInstance().setMake(scan_make);
                                                    Sessiondata.getInstance().setModel(scan_model);
                                                    Sessiondata.getInstance().setSerial(scan_serial);
                                                    int processId = Integer.parseInt(split_string(scan_processid));
                                                    Sessiondata.getInstance().setSub_process(scan_processid);
                                                    Sessiondata.getInstance().setProcessId(processId);
                                                    Sessiondata.getInstance().setInventory_equip_id(1);

                                                    Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                    Sessiondata.getInstance().setTemp_make(scan_make);
                                                    Sessiondata.getInstance().setTemp_model(scan_model);
                                                    Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                    Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                    sDialog.dismiss();

                                                }

                                            });

                                    dialog_notmatch.setCancelable(false);
                                    dialog_notmatch.show();
                                }
                            }

                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                Sessiondata.getInstance().setInventory_dialoghandle(0);
                            }
                        }
                    } else {

                        btn_submit.setEnabled(true);
                      /*  submit_layout.setEnabled(false);
                        submit_layout.setVisibility(View.GONE);*/
                        btn_submit.setVisibility(View.VISIBLE);
                        /*footer.setVisibility(View.GONE);*/

                        ArrayList<GetEquipmentList> equimlist = new ArrayList<>();
                        for (int i = 0; i < equip_list.size(); i++) {
                            String eqstatus = equip_list.get(i).getEqpstatus();
                            Log.d("eqstatus_list_many", "" + eqstatus);

                            if (eqstatus.toString().contains("Counted")) {

                                equimlist.add(equip_list.get(i));


                            }

                        }

                        equip_list = equimlist;
                        adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                        equp_list.setAdapter(adapter);
                    }

                    adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                    equp_list.setAdapter(adapter);
                }

            } else {

            }

        }
    }

    public class AsyncGetEquipmentListUnCounted extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);

                int process_ids_text = Integer.parseInt(split_string(process_ids));

                equip_list = WebServiceConsumer.getInstance().GetEquipmentListV4(user_token, process_ids_text, equipid_text, make_text, model_text, serial_text);
                Log.d("equip_list", equip_list + "");


            } catch (SocketTimeoutException e) {
                equip_list = null;
            } catch (Exception e) {
                equip_list = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Sessiondata.getInstance().setGetEquipmentLists(equip_list);
            ProgressBar.dismiss();

            String pid = processid.getText().toString();
            Sessiondata.getInstance().setSub_process(pid);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btn_counted.setBackground(grey_drawable);
                btn_uncounted.setBackground(blue_drawable);
                btn_counted.setTextColor(getResources().getColor(R.color.black));
                btn_uncounted.setTextColor(getResources().getColor(R.color.white));
            } else {
                btn_counted.setBackgroundDrawable(grey_drawable);
                btn_uncounted.setBackgroundDrawable(blue_drawable);
                btn_counted.setTextColor(getResources().getColor(R.color.black));
                btn_uncounted.setTextColor(getResources().getColor(R.color.white));
            }

            if (equip_list != null) {

                if (equip_list.size() == 1) {

                    if (equip_list.get(0).getMessage().length() != 0) {

                        String Result = equip_list.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (equip_list.get(0).getMessage().toString().contains("Session")) {
                            Session = 16;
                            if (return_once == 0) {
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }
                            }

                            if (return_once == 1) {
                                return_once = 0;
                            }
                        }
                    } else {
                        String eqstatus = equip_list.get(0).getEqpstatus();
                        Log.d("eqstatus_list_single5", "" + eqstatus);

                        if (eqstatus.toString().contains("Counted")) {
                            btn_submit.setEnabled(true);
                          /*  submit_layout.setEnabled(false);
                            submit_layout.setVisibility(View.GONE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.GONE);*/

                            equip_list = new ArrayList<>();

                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        } else {
                            btn_submit.setEnabled(true);
                           /* submit_layout.setEnabled(false);
                            submit_layout.setVisibility(View.GONE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.GONE);*/

                            equip_list = new ArrayList<>();
                            equip_list = Sessiondata.getInstance().getGetEquipmentLists();

                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        }

                    }

                } else {
                    equip_list = new ArrayList<>();
                    equip_list = Sessiondata.getInstance().getGetEquipmentLists();


                    if (equip_list.size() == 0) {

                        btn_submit.setEnabled(true);
                        /*submit_layout.setEnabled(true);
                        submit_layout.setVisibility(View.VISIBLE);*/
                        btn_submit.setVisibility(View.VISIBLE);
                        /*footer.setVisibility(View.VISIBLE);*/

                        adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                        equp_list.setAdapter(adapter);

                        if (!equipid_text.toString().isEmpty() && !make_text.toString().isEmpty() &&
                                !model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {

                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                process_ids = split_string(process_list);
                                equp_ids = equp_id.getText().toString();
                                models = model.getText().toString();
                                makes = make.getText().toString();
                                serials = serial.getText().toString();

                                sub_processid = split_string(process_list);

                                if (sweetalrt_notmatch) {
                                    sweetalrt_notmatch = false;
                                    dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Does not exist, Do you want to add to Hand Writes?")
                                            .setCancelText("No")
                                            .setConfirmText("Yes")
                                            .showCancelButton(true)

                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sweetalrt_notmatch = true;
                                                    sDialog.dismiss();
                                                }
                                            })

                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sweetalrt_notmatch = true;

//                                                    scan_processid = split_string(process_list);
                                                    scan_processid = processid.getText().toString();

                                                    scan_model = model.getText().toString();
                                                    scan_make = make.getText().toString();
                                                    scan_serial = serial.getText().toString();
                                                    scan_equip = equp_id.getText().toString();

                                                    Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setUnitId(scan_equip);
                                                    Sessiondata.getInstance().setMake(scan_make);
                                                    Sessiondata.getInstance().setModel(scan_model);
                                                    Sessiondata.getInstance().setSerial(scan_serial);
                                                    int processId = Integer.parseInt(split_string(scan_processid));
                                                    Sessiondata.getInstance().setSub_process(scan_processid);
                                                    Sessiondata.getInstance().setProcessId(processId);
                                                    Sessiondata.getInstance().setInventory_equip_id(1);

                                                    Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                    Sessiondata.getInstance().setTemp_make(scan_make);
                                                    Sessiondata.getInstance().setTemp_model(scan_model);
                                                    Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                    Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                    sDialog.dismiss();

                                                }

                                            });

                                    dialog_notmatch.setCancelable(false);
                                    dialog_notmatch.show();
                                }
                            }

                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                Sessiondata.getInstance().setInventory_dialoghandle(0);
                            }
                        } else if (!equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {


                        } else if (!equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                model_text.toString().isEmpty() && serial_text.toString().isEmpty()) {


                            btn_submit.setEnabled(true);
                           /* submit_layout.setEnabled(true);
                            submit_layout.setVisibility(View.VISIBLE);*/
                            btn_submit.setVisibility(View.VISIBLE);
                            /*footer.setVisibility(View.VISIBLE);*/


                        } else if (equipid_text.toString().isEmpty() && make_text.toString().isEmpty() &&
                                model_text.toString().isEmpty() && !serial_text.toString().isEmpty()) {

                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 0) {

                                process_ids = split_string(process_list);
                                equp_ids = equp_id.getText().toString();
                                models = model.getText().toString();
                                makes = make.getText().toString();
                                serials = serial.getText().toString();

                                sub_processid = split_string(process_list);

                                if (sweetalrt_notmatch) {
                                    sweetalrt_notmatch = false;
                                    dialog_notmatch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Equipment is not in PID, Do you want to add it and Count?")
                                            .setCancelText("No")
                                            .setConfirmText("Yes")
                                            .showCancelButton(true)

                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sweetalrt_notmatch = true;
                                                    sDialog.dismiss();
                                                }
                                            })

                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sweetalrt_notmatch = true;

//                                                    scan_processid = split_string(process_list);
                                                    scan_processid = processid.getText().toString();

                                                    scan_model = model.getText().toString();
                                                    scan_make = make.getText().toString();
                                                    scan_serial = serial.getText().toString();
                                                    scan_equip = equp_id.getText().toString();

                                                    Sessiondata.getInstance().setEqu_branch_name(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setUnitId(scan_equip);
                                                    Sessiondata.getInstance().setMake(scan_make);
                                                    Sessiondata.getInstance().setModel(scan_model);
                                                    Sessiondata.getInstance().setSerial(scan_serial);
                                                    int processId = Integer.parseInt(split_string(scan_processid));
                                                    Sessiondata.getInstance().setSub_process(scan_processid);
                                                    Sessiondata.getInstance().setProcessId(processId);
                                                    Sessiondata.getInstance().setInventory_equip_id(1);


                                                    Sessiondata.getInstance().setTemp_unitId(scan_equip);
                                                    Sessiondata.getInstance().setTemp_make(scan_make);
                                                    Sessiondata.getInstance().setTemp_model(scan_model);
                                                    Sessiondata.getInstance().setTemp_serial(scan_serial);

                                                    Intent intent = new Intent(equipment_inventory_activity.this, AttachImageActivity.class);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                    sDialog.dismiss();

                                                }

                                            });

                                    dialog_notmatch.setCancelable(false);
                                    dialog_notmatch.show();
                                }
                            }

                            if (Sessiondata.getInstance().getInventory_dialoghandle() == 1) {
                                Sessiondata.getInstance().setInventory_dialoghandle(0);
                            }
                        }
                    } else {

                        btn_submit.setEnabled(true);
                      /*  submit_layout.setEnabled(false);
                        submit_layout.setVisibility(View.GONE);*/
                        btn_submit.setVisibility(View.VISIBLE);
                        /*footer.setVisibility(View.GONE);*/

                        ArrayList<GetEquipmentList> equimlist = new ArrayList<>();
                        for (int i = 0; i < equip_list.size(); i++) {
                            String eqstatus = equip_list.get(i).getEqpstatus();
                            Log.d("eqstatus_list_many", "" + eqstatus);

                            if (!eqstatus.toString().contains("Counted")) {

                                equimlist.add(equip_list.get(i));


                            }

                        }
                        if (equimlist.size() == 0) {
                            new AsyncGetEquipmentListCounted().execute();
                        } else {
                            equip_list = equimlist;
                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                            equp_list.setAdapter(adapter);
                        }

                        equip_list = equimlist;
                        adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                        equp_list.setAdapter(adapter);
                    }

                    adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                    equp_list.setAdapter(adapter);
                }

            } else {

            }

        }
    }

    public class AsyncGetAttachments_new extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {

            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);

                attachment = WebServiceConsumer.getInstance().attachmentsdetails(user_token, unit_ID);

            } catch (SocketTimeoutException e) {
                attachment = null;
            } catch (Exception e) {
                attachment = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Sessiondata.getInstance().setAttachment(attachment);

            if (attachment != null) {

                if (attachment.size() == 1) {

                    if (attachment.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = attachment.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (attachment.get(0).getMessage().toString().contains("Session")) {
                            Session = 9;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {
                        ProgressBar.dismiss();


                        if (sweetdetach) {
                            sweetdetach = false;
                            dialogdetach = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                    .setTitleText("Message!")
                                    .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                    .setCancelText("Yes")
                                    .setConfirmText("No")
                                    .showCancelButton(true)

                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            if (checkInternetConenction()) {
                                                msg_show = 2;
                                                new AsyncUpdateEquipment().execute();
                                                sweetdetach = true;
                                                sDialog.dismiss();
                                            } else {

                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            if (checkInternetConenction()) {
                                                sweetdetach = true;
                                                sDialog.dismiss();

                                            } else {

                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            dialogdetach.setCancelable(false);
                            dialogdetach.show();
                        }

                    }
                } else {
                    ProgressBar.dismiss();

                    if (attachment.size() == 0) {

                        if (sweetdetach) {
                            sweetdetach = false;
                            dialogdetach = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                    .setTitleText("Message!")
                                    .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                    .setCancelText("Yes")
                                    .setConfirmText("No")
                                    .showCancelButton(true)

                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            if (checkInternetConenction()) {
                                                msg_show = 2;
                                                new AsyncUpdateEquipment().execute();
                                                sweetdetach = true;
                                                sDialog.dismiss();
                                            } else {

                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            if (checkInternetConenction()) {

                                                sweetdetach = true;
                                                sDialog.dismiss();

                                            } else {

                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            dialogdetach.setCancelable(false);
                            dialogdetach.show();
                        }

                    } else {
                        if ((mDialogattachlist == null) || !mDialogattachlist.isShowing()) {
                            mDialogattachlist = new Dialog(equipment_inventory_activity.this);
                            mDialogattachlist.setCanceledOnTouchOutside(false);
                            mDialogattachlist.setCancelable(false);
                            mDialogattachlist.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            mDialogattachlist.setContentView(R.layout.dialog_attch_list);

                            TextView mDialogFreeCancelButton = (TextView) mDialogattachlist.findViewById(R.id.dialog_social_cancel);

                            TextView mDialogFreeOKButton = (TextView) mDialogattachlist.findViewById(R.id.update);

                            TextView txt_header = (TextView) mDialogattachlist.findViewById(R.id.txt_header);
                            txt_header.setText("Attachments");
                            txt_header.setTypeface(header_face);
                            mDialogFreeOKButton.setTypeface(header_face);
                            mDialogFreeCancelButton.setTypeface(header_face);

                            ListView list = (ListView) mDialogattachlist.findViewById(R.id.list);

                            attachments_list = new ArrayList<>();
                            attachments_list = Sessiondata.getInstance().getAttachment();

                            CustomAdapter adapter_attach = new CustomAdapter_attach(equipment_inventory_activity.this, attachments_list);
                            list.setAdapter(adapter_attach);

                            mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (checkInternetConenction()) {

                                        new AsyncUpdateEquipmentforAttach().execute();

                                    } else {

                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                    }

                                    mDialogattachlist.dismiss();
                                }
                            });

                            mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialogattachlist.dismiss();
                                }
                            });

                            mDialogattachlist.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                            mDialogattachlist.show();

                        }
                    }

                }
            } else {
                ProgressBar.dismiss();

                if (sweetdetach) {
                    sweetdetach = false;
                    dialogdetach = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                            .setTitleText("Message!")
                            .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                            .setCancelText("Yes")
                            .setConfirmText("No")
                            .showCancelButton(true)

                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    if (checkInternetConenction()) {
                                        msg_show = 2;
                                        new AsyncUpdateEquipment().execute();
                                        sweetdetach = true;
                                        sDialog.dismiss();
                                    } else {

                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    if (checkInternetConenction()) {
                                        sweetdetach = true;
                                        sDialog.dismiss();

                                    } else {

                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    dialogdetach.setCancelable(false);
                    dialogdetach.show();
                }
            }
        }
    }

    public class AsyncGetAttachments extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {

            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);

                attachment = WebServiceConsumer.getInstance().attachmentsdetails(user_token, unit_ID);

            } catch (SocketTimeoutException e) {
                attachment = null;
            } catch (Exception e) {
                attachment = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Sessiondata.getInstance().setAttachment(attachment);

            if (attachment != null) {

                if (attachment.size() == 1) {

                    if (attachment.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = attachment.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (attachment.get(0).getMessage().toString().contains("Session")) {
                            Session = 2;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {
                        ProgressBar.dismiss();

                        if (attachment.get(0).getAttachments().toString().length() == 0) {

                            int process_ids = Integer.parseInt(split_string(process_list));

                            if (sweetalrts1) {
                                sweetalrts1 = false;
                                dialogmsg = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText("Equipment not included in PID " + process_ids + ".Check for other branch!")
                                        .setCancelText("No,cancel")
                                        .setConfirmText("Yes,check it")
                                        .showCancelButton(true)

                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sweetalrts1 = true;
                                                sDialog.dismiss();
                                            }
                                        })

                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sweetalrts1 = true;
                                                if (checkInternetConenction()) {

                                                    new AsyncGetEquipmentBranch().execute();
                                                    sDialog.dismiss();

                                                } else {

                                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                                }
                                            }

                                        });

                                dialogmsg.setCancelable(false);
                                dialogmsg.show();

                            }

                        } else {
                            int process_ids = Integer.parseInt(split_string(process_list));

                            if (sweetdetach) {
                                sweetdetach = false;
                                dialogdetach = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                        .setCancelText("Yes")
                                        .setConfirmText("No")
                                        .showCancelButton(true)

                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (checkInternetConenction()) {
                                                    msg_show = 2;
                                                    new AsyncUpdateEquipment().execute();
                                                    sweetdetach = true;
                                                    sDialog.dismiss();
                                                } else {

                                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        })
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (checkInternetConenction()) {
                                                    sweetdetach = true;
                                                    sDialog.dismiss();

                                                } else {

                                                    Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                dialogdetach.setCancelable(false);
                                dialogdetach.show();
                            }


                        }

                    }
                } else {
                    ProgressBar.dismiss();

                    if (attachment.size() == 0) {

                        int process_ids = Integer.parseInt(split_string(process_list));

                        if (sweetalrts3) {
                            sweetalrts3 = false;
                            dialogmsg = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Message!")
                                    .setContentText("Equipment not included in PID " + process_ids + ".Check for other branch!")
                                    .setCancelText("No,cancel")
                                    .setConfirmText("Yes,check it")
                                    .showCancelButton(true)

                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrts3 = true;
                                            sDialog.dismiss();
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            if (checkInternetConenction()) {
                                                sweetalrts3 = true;
                                                new AsyncGetEquipmentBranch().execute();
                                                sDialog.dismiss();

                                            } else {

                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            dialogmsg.setCancelable(false);
                            dialogmsg.show();
                        }
                    } else {
                        if ((mDialogattachlist == null) || !mDialogattachlist.isShowing()) {
                            mDialogattachlist = new Dialog(equipment_inventory_activity.this);
                            mDialogattachlist.setCanceledOnTouchOutside(false);
                            mDialogattachlist.setCancelable(false);
                            mDialogattachlist.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            mDialogattachlist.setContentView(R.layout.dialog_attch_list);

                            TextView mDialogFreeCancelButton = (TextView) mDialogattachlist.findViewById(R.id.dialog_social_cancel);

                            TextView mDialogFreeOKButton = (TextView) mDialogattachlist.findViewById(R.id.update);

                            TextView txt_header = (TextView) mDialogattachlist.findViewById(R.id.txt_header);
                            txt_header.setText("Attachments");
                            txt_header.setTypeface(header_face);
                            mDialogFreeOKButton.setTypeface(header_face);
                            mDialogFreeCancelButton.setTypeface(header_face);

                            ListView list = (ListView) mDialogattachlist.findViewById(R.id.list);

                            attachments_list = new ArrayList<>();
                            attachments_list = Sessiondata.getInstance().getAttachment();

                            CustomAdapter adapter_attach = new CustomAdapter_attach(equipment_inventory_activity.this, attachments_list);
                            list.setAdapter(adapter_attach);

                            mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (checkInternetConenction()) {

                                        new AsyncUpdateEquipmentforAttach().execute();

                                    } else {

                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                    }

                                    mDialogattachlist.dismiss();
                                }
                            });

                            mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialogattachlist.dismiss();
                                }
                            });

                            mDialogattachlist.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                            mDialogattachlist.show();

                        }
                    }

                }
            } else {
                ProgressBar.dismiss();

                int process_ids = Integer.parseInt(split_string(process_list));

                if (sweetalrts3) {
                    sweetalrts3 = false;
                    dialogmsg = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Message!")
                            .setContentText("Equipment not included in PID " + process_ids + ".Check for other branch!")
                            .setCancelText("No,cancel")
                            .setConfirmText("Yes,check it")
                            .showCancelButton(true)

                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrts3 = true;
                                    sDialog.dismiss();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrts3 = true;
                                    if (checkInternetConenction()) {

                                        new AsyncGetEquipmentBranch().execute();
                                        sDialog.dismiss();

                                    } else {

                                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    dialogmsg.setCancelable(false);
                    dialogmsg.show();
                }
            }
        }
    }

    public class AsyncGetEquipmentBranch_transfer extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);

                equp_branch = WebServiceConsumer.getInstance().equipmentBranch(user_token, unit_ID);

            } catch (SocketTimeoutException e) {
                equp_branch = null;
            } catch (Exception e) {
                equp_branch = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Sessiondata.getInstance().setBranch(equp_branch);

            if (equp_branch != null) {

                if (equp_branch.getMessage().length() != 0) {
                    ProgressBar.dismiss();

                    String Result = equp_branch.getMessage();
                    String replace = Result.replace("Error - ", "");
                    if (equp_branch.getMessage().toString().contains("Session")) {
                        Session = 10;
                        if (checkInternetConenction()) {

                            new AsyncSessionLoginTask().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }
                } else {
                    ProgressBar.dismiss();

                    String equp_branchs = equp_branch.getBranch();
                    String equp_branchs_login = branch_no.getText().toString();
                    Log.d("equp_branchs", "" + equp_branchs);
                    Log.d("equp_branchs_login", "" + equp_branchs_login);

                    for (int ii = 0; ii < equp_branchs_login.length(); ii++) {

                        Character character = equp_branchs_login.charAt(ii);

                        if (character.toString().equals("-")) {

                            equp_branchs_login = equp_branchs_login.substring(0, ii);

                            Log.d("branch_trim", "" + equp_branchs_login);
                            break;
                        }
                    }

                    if (!equp_branchs_login.toString().contains(equp_branchs)) {

                        if (sweetbranch) {
                            sweetbranch = false;
                            dialogbranch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                    .setTitleText("Message!")
                                    .setContentText("Equipment found in branch location " + equp_branchs + ". Do you want to transfer to branch " + loginbranch + " ?")
                                    .setCancelText("No")
                                    .setConfirmText("Yes")
                                    .showCancelButton(true)

                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetbranch = true;
                                            sDialog.dismiss();
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetbranch = true;
                                            Sessiondata.getInstance().setTransfer_Equipment("T");

                                            if (checkInternetConenction()) {

                                                new AsyncSetEquipmentDetails_transfer().execute();
                                                sDialog.dismiss();

                                            } else {

                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            dialogbranch.setCancelable(false);
                            dialogbranch.show();
                        }
                    } else {
                        if (sweetalrt_count) {
                            sweetalrt_count = false;

                            alert_count = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                    .setTitleText("Equipment Available in Current Branch!")
                                    .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                    .setCancelText("No")
                                    .setConfirmText("Yes")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            sweetalrt_count = true;
                                            sweetAlertDialog.dismiss();

                                            if (checkInternetConenction()) {

                                                new AsyncSetEquipmentDetails_transfer().execute();


                                            } else {

                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrt_count = true;
                                            sDialog.dismiss();
                                        }
                                    });
                            alert_count.setCancelable(false);
                            alert_count.show();
                        }
                    }

                }
            } else {
                ProgressBar.dismiss();

                if (sweetnobranch) {
                    sweetnobranch = false;

                    dialognobranch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setContentText("Branch not found!")
                            .setCancelText("Ok")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override

                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetnobranch = true;
                                    sDialog.dismiss();
                                }
                            });
                    dialognobranch.setCancelable(false);
                    dialognobranch.show();
                }
            }
        }
    }

    public class AsyncGetEquipmentBranch extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);

                equp_branch = WebServiceConsumer.getInstance().equipmentBranch(user_token, unit_ID);

            } catch (SocketTimeoutException e) {
                equp_branch = null;
            } catch (Exception e) {
                equp_branch = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Sessiondata.getInstance().setBranch(equp_branch);

            if (equp_branch != null) {

                if (equp_branch.getMessage().length() != 0) {
                    ProgressBar.dismiss();

                    String Result = equp_branch.getMessage();
                    String replace = Result.replace("Error - ", "");
                    if (equp_branch.getMessage().toString().contains("Session")) {
                        Session = 4;
                        if (checkInternetConenction()) {

                            new AsyncSessionLoginTask().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }
                } else {
                    ProgressBar.dismiss();

                    String equp_branchs = equp_branch.getBranch();
                    String equp_branchs_login = branch_no.getText().toString();
                    Log.d("equp_branchs", "" + equp_branchs);
                    Log.d("equp_branchs_login", "" + equp_branchs_login);

                    if (!equp_branchs_login.toString().contains(equp_branchs)) {

                        if (sweetbranch) {
                            sweetbranch = false;
                            dialogbranch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                    .setTitleText("Message!")
                                    .setContentText("Equipment found in branch location " + equp_branchs + ". Do you want to transfer to branch " + loginbranch + " ?")
                                    .setCancelText("No")
                                    .setConfirmText("Yes")
                                    .showCancelButton(true)

                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetbranch = true;
                                            sDialog.dismiss();
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetbranch = true;
                                            Sessiondata.getInstance().setTransfer_Equipment("T");

                                            if (checkInternetConenction()) {

                                                new AsyncUpdateEquipment().execute();
                                                sDialog.dismiss();

                                            } else {

                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            dialogbranch.setCancelable(false);
                            dialogbranch.show();
                        }
                    } else {
                        if (sweetalrt_count) {
                            sweetalrt_count = false;

                            alert_count = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.INFO_TYPE)
                                    .setTitleText("Equipment Available in Current Branch!")
                                    .setContentText("Do you want to count this Equipment# " + unit_ID + " ?")
                                    .setCancelText("No")
                                    .setConfirmText("Yes")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            Log.d("rLog", "Do you want to count this Equipment# " + unit_ID + " ?");

                                            sweetalrt_count = true;
                                            sweetAlertDialog.dismiss();


                                            msg_show = 3;

                                            if (checkInternetConenction()) {

                                                new AsyncUpdateEquipment().execute();


                                            } else {

                                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrt_count = true;
                                            sDialog.dismiss();
                                        }
                                    });
                            alert_count.setCancelable(false);
                            alert_count.show();
                        }
                    }

                }
            } else {
                ProgressBar.dismiss();

                if (sweetnobranch) {
                    sweetnobranch = false;

                    dialognobranch = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setContentText("Branch not found!")
                            .setCancelText("Ok")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override

                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetnobranch = true;
                                    sDialog.dismiss();
                                }
                            });
                    dialognobranch.setCancelable(false);
                    dialognobranch.show();
                }
            }
        }
    }

    public class AsyncUpdateEquipment_transfer extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                user = Sessiondata.getInstance().getLoginObject().getUsername();
                branch = branch_no.getText().toString();
                int processids = Integer.parseInt(process_ids.toString());
                Log.d("branch_usertoken", "" + user_token);
                Log.d("scannedtype", "transfer" + scannedtype);
                update_equip = WebServiceConsumer.getInstance().UpdateEquipmentV3(user_token, unit_ID, processids, user, branch, scannedtype);

            } catch (SocketTimeoutException e) {
                update_equip = null;
            } catch (Exception e) {
                update_equip = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressBar.dismiss();

            if (update_equip != null) {
                if (update_equip.toString().contains("Session")) {

                    String Result = update_equip;
                    String replace = Result.replace("Error - ", "");

                    Session = 12;

                    if (checkInternetConenction()) {


                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (call_setEquip_D == 1) {
                        if (msg_show == 1) {
                            if (sweetalrtsuccess) {
                                sweetalrtsuccess = false;

                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success!")
                                        .setContentText("Equipment Detached!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                sweetalrtsuccess = true;
                                                msg_show = 0;
                                                equip_list.remove(detach_pos);
                                                adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                                equp_list.setAdapter(adapter);
                                                sDialog.dismiss();
                                            }
                                        });
                                sweetalt.setCancelable(false);
                                sweetalt.show();
                            }
                        } else {
                            if (sweetalrtsuccess) {
                                sweetalrtsuccess = false;

                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success!")
                                        .setContentText("Equipment Counted!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {

                                                Sessiondata.getInstance().setTransfer_Equipment("");

                                                sweetalrtsuccess = true;
                                                msg_show = 0;
                                                equip_list.remove(detach_pos);

                                                if (equip_list.size() == 0) {
                                                    Log.d("equip_list", " 0");

                                                    clearandload();

                                                } else {
                                                    Log.d("equip_list", " " + equip_list.size());
                                                    adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                                    equp_list.setAdapter(adapter);
                                                }
                                                sDialog.dismiss();
                                            }
                                        });
                                sweetalt.setCancelable(false);
                                sweetalt.show();
                            }
                        }
                    } else {
                        if (checkInternetConenction()) {

                            new AsyncSetEquipmentDetails_transfer().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            } else {
                if (call_setEquip_D == 1) {
                    if (msg_show == 1) {
                        if (sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Equipment Detached!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrtsuccess = true;
                                            msg_show = 0;
                                            equip_list.remove(detach_pos);
                                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                            equp_list.setAdapter(adapter);
                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    } else {
                        if (sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Equipment Counted!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {

                                            Sessiondata.getInstance().setTransfer_Equipment("");

                                            sweetalrtsuccess = true;
                                            msg_show = 0;
                                            equip_list.remove(detach_pos);
                                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                            equp_list.setAdapter(adapter);
                                            if (equip_list.size() == 0) {
                                                Log.d("equip_list", " 0");

                                                clearandload();

                                            } else {
                                                Log.d("equip_list", " " + equip_list.size());
                                                adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                                equp_list.setAdapter(adapter);
                                            }
                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    }
                } else {
                    if (checkInternetConenction()) {

                        new AsyncSetEquipmentDetails_transfer().execute();

                    } else {

                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public class AsyncUnCountEquipment extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                user = Sessiondata.getInstance().getLoginObject().getUsername();


                equp_branch_chk = Sessiondata.getInstance().getBranch();

                Log.d("uncount_user_token", "" + user_token);
                Log.d("uncount_equipid", "" + uncount_equipid);
                Log.d("uncount_processid", "" + ucount_processid);
                Log.d("uncount_user", "" + user);
                Log.d("uncount_branch_name", "" + uncount_branch);

                uncountequp = WebServiceConsumer.getInstance().UncountEquipment(user_token, uncount_equipid, ucount_processid, user, uncount_branch);
            } catch (SocketTimeoutException e) {
                uncountequp = null;
            } catch (Exception e) {
                uncountequp = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressBar.dismiss();

            if (uncountequp != null) {
                if (uncountequp.toString().contains("Session")) {

                    String Result = uncountequp;
                    String replace = Result.replace("Error - ", "");

                    Session = 14;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (sweetalrtsuccess) {
                        sweetalrtsuccess = false;

                        sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success!")
                                .setContentText("Equipment UnCounted!")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        sweetalrtsuccess = true;
                                        msg_show = 0;
                                        if (equip_list.size() != 0)
                                        equip_list.remove(detach_pos);

                                        if (equip_list.size() != 0) {
                                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                            equp_list.setAdapter(adapter);
                                        } else {
                                            clearandload();
                                        }

                                        sDialog.dismiss();
                                    }
                                });
                        sweetalt.setCancelable(false);
                        sweetalt.show();
                    }

                }

            } else {
                if (sweetalrtsuccess) {
                    sweetalrtsuccess = false;

                    sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success!")
                            .setContentText("Equipment UnCounted!")
                            .setCancelText("Ok")
                            .showCancelButton(false)
                            .setCancelClickListener(null)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override

                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrtsuccess = true;
                                    msg_show = 0;
                                    equip_list.remove(detach_pos);

                                    if (equip_list.size() != 0) {
                                        adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                        equp_list.setAdapter(adapter);
                                    } else {
                                        clearandload();
                                    }

                                    sDialog.dismiss();
                                }
                            });
                    sweetalt.setCancelable(false);
                    sweetalt.show();
                }
            }
        }
    }

    public class AsyncUpdateEquipment extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (msg_show == 1 || msg_show == 2 || msg_show == 3) {

                    user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                    user = Sessiondata.getInstance().getLoginObject().getUsername();
                    branch = branch_no.getText().toString();
                    int processids = Integer.parseInt(process_ids.toString());
                    Log.d("branch_usertoken", "" + user_token);
                    Log.d("scannedtype", "update" + scannedtype);
                    Log.d("rLog", "scannedtype update " + scannedtype + " in " + msg_show + " Condition");

                    update_equip = WebServiceConsumer.getInstance().UpdateEquipmentV3(user_token, unit_ID, processids, user, branch, scannedtype);

                } else {

                    user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                    user = Sessiondata.getInstance().getLoginObject().getUsername();
                    int processids = Integer.parseInt(process_ids.toString());

                    equp_branch_chk = Sessiondata.getInstance().getBranch();
                    if (equp_branch_chk != null) {
                        if (equp_branch_chk.getBranch() == null) {
                            transfer_branch = branch_no.getText().toString();
                        } else {
                            transfer_branch = Sessiondata.getInstance().getBranch().getBranch();
                        }
                    } else {
                        transfer_branch = branch_no.getText().toString();
                    }

                    Log.d("branch_usertoken", "" + user_token);
                    Log.d("branch_count_new", "" + transfer_branch);
                    Log.d("scannedtype", "update" + scannedtype);
                    Log.d("rLog", "scannedtype update " + scannedtype + " in else Condition");


                    update_equip = WebServiceConsumer.getInstance().UpdateEquipmentV3(user_token, unit_ID, processids, user, branch, scannedtype);
                }
            } catch (SocketTimeoutException e) {
                update_equip = null;
            } catch (Exception e) {
                update_equip = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressBar.dismiss();

            if (update_equip != null) {
                if (update_equip.toString().contains("Session")) {

                    String Result = update_equip;
                    String replace = Result.replace("Error - ", "");

                    Session = 6;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (call_setEquip_D == 1) {
                        if (msg_show == 1) {
                            if (sweetalrtsuccess) {
                                sweetalrtsuccess = false;

                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success!")
                                        .setContentText("Equipment Detached!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                sweetalrtsuccess = true;
                                                msg_show = 0;
                                                equip_list.remove(detach_pos);
                                                adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                                equp_list.setAdapter(adapter);
                                                sDialog.dismiss();
                                            }
                                        });
                                sweetalt.setCancelable(false);
                                sweetalt.show();
                            }
                        } else {
                            if (sweetalrtsuccess) {
                                sweetalrtsuccess = false;

                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success!")
                                        .setContentText("Equipment Counted!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {

                                                Sessiondata.getInstance().setTransfer_Equipment("");

                                                sweetalrtsuccess = true;
                                                msg_show = 0;
                                                equip_list.remove(detach_pos);
                                                adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                                equp_list.setAdapter(adapter);
                                                if (equip_list.size() == 0) {
                                                    Log.d("equip_list", " 0");

                                                    clearandload();

                                                } else {
                                                    Log.d("equip_list", " " + equip_list.size());
                                                    adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                                    equp_list.setAdapter(adapter);
                                                }
                                                sDialog.dismiss();
                                            }
                                        });
                                sweetalt.setCancelable(false);
                                sweetalt.show();
                            }
                        }
                    } else {
                        if (checkInternetConenction()) {

                            new AsyncSetEquipmentDetails().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            } else {
                if (call_setEquip_D == 1) {
                    if (msg_show == 1) {
                        if (sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Equipment Detached!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrtsuccess = true;
                                            msg_show = 0;
                                            equip_list.remove(detach_pos);
                                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                            equp_list.setAdapter(adapter);

                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    } else {
                        if (sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Equipment Counted!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {

                                            Sessiondata.getInstance().setTransfer_Equipment("");

                                            sweetalrtsuccess = true;
                                            msg_show = 0;
                                            equip_list.remove(detach_pos);
                                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                            equp_list.setAdapter(adapter);
                                            if (equip_list.size() == 0) {
                                                Log.d("equip_list", " 0");

                                                clearandload();

                                            } else {
                                                Log.d("equip_list", " " + equip_list.size());
                                                adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                                equp_list.setAdapter(adapter);
                                            }
                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    }
                } else {
                    if (checkInternetConenction()) {

                        new AsyncSetEquipmentDetails().execute();

                    } else {

                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public class AsyncUpdateEquipmentforAttach extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                user = Sessiondata.getInstance().getLoginObject().getUsername();
                branch = branch_no.getText().toString();
                int processids = Integer.parseInt(process_ids.toString());

                Log.d("branch_usertoken", "" + user_token);
                Log.d("scannedtype", "_forattach" + scannedtype);

                for (int i = 0; i < attachments_list.size(); i++) {
                    Log.d("equp_id", "" + attachments_list.get(i).getEquipid().toString());
                    String equp_id = attachments_list.get(i).getEquipid().toString();
                    update_equip = WebServiceConsumer.getInstance().UpdateEquipmentV3(user_token, equp_id, processids, user, branch, scannedtype);
                }
            } catch (SocketTimeoutException e) {
                update_equip = null;
            } catch (Exception e) {
                update_equip = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressBar.dismiss();

            selected_attach.clear();

            if (update_equip != null) {
                if (update_equip.toString().contains("Session")) {

                    String Result = update_equip;
                    String replace = Result.replace("Error - ", "");

                    Session = 7;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {

                    if (call_setEquip_D == 1) {
                        if (msg_show == 1) {
                            if (sweetalrtsuccess) {
                                sweetalrtsuccess = false;

                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success!")
                                        .setContentText("Equipment Detached!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener(){
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                sweetalrtsuccess = true;
                                                msg_show = 0;
                                                equip_list.remove(detach_pos);
                                                adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                                equp_list.setAdapter(adapter);
                                                sDialog.dismiss();
                                            }
                                        });
                                sweetalt.setCancelable(false);
                                sweetalt.show();
                            }
                        } else {
                            if (sweetalrtsuccess) {
                                sweetalrtsuccess = false;

                                sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Success!")
                                        .setContentText("Equipment Counted!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {

                                                Sessiondata.getInstance().setTransfer_Equipment("");

                                                sweetalrtsuccess = true;
                                                msg_show = 0;
                                                equip_list.remove(detach_pos);
                                                adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                                equp_list.setAdapter(adapter);
                                                if (equip_list.size() == 0) {
                                                    Log.d("equip_list", " 0");

                                                    clearandload();

                                                } else {
                                                    Log.d("equip_list", " " + equip_list.size());
                                                    adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                                    equp_list.setAdapter(adapter);
                                                }
                                                sDialog.dismiss();
                                            }
                                        });
                                sweetalt.setCancelable(false);
                                sweetalt.show();
                            }
                        }
                    } else {

                        if (checkInternetConenction()) {

                            new AsyncSetEquipmentDetails().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }

                    }
                }

            } else {

            }
        }
    }

    public class AsyncSetEquipmentDetails_transfer extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                user = Sessiondata.getInstance().getLoginObject().getUsername();
                branch = branch_no.getText().toString();
                int processids = Integer.parseInt(process_ids.toString());

                Log.d("branch_usertoken", "" + user_token);
                Log.d("scannedtype", "setEqup_transfer" + scannedtype);

                SetEquipmentDetails = WebServiceConsumer.getInstance().SetEquipmentDetailsV3(user_token, unit_ID, processids, user, branch, scannedtype);

            } catch (SocketTimeoutException e) {
                attachment = null;
            } catch (Exception e) {
                attachment = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressBar.dismiss();

            if (SetEquipmentDetails != null) {
                if (SetEquipmentDetails.toString().contains("Session")) {

                    String Result = SetEquipmentDetails;
                    String replace = Result.replace("Error - ", "");

                    Session = 11;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {

                    if (msg_show == 1) {
                        if (sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Equipment Detached!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrtsuccess = true;
                                            msg_show = 0;
                                            equip_list.remove(detach_pos);
                                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                            equp_list.setAdapter(adapter);
                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    } else {
                        if (sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Equipment transferred to Branch " + loginbranch + " and counted")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrtsuccess = true;
                                            msg_show = 0;
                                            equip_list.remove(detach_pos);
                                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                            equp_list.setAdapter(adapter);
                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    }
                }
            } else {


            }
        }
    }

    public class AsyncSetEquipmentDetails extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                user = Sessiondata.getInstance().getLoginObject().getUsername();
                branch = branch_no.getText().toString();
                int processids = Integer.parseInt(process_ids.toString());

                Log.d("branch_usertoken", "" + user_token);
                Log.d("scannedtype", "setEquip" + scannedtype);

                SetEquipmentDetails = WebServiceConsumer.getInstance().SetEquipmentDetailsV3(user_token, unit_ID, processids, user, branch, scannedtype);

            } catch (SocketTimeoutException e) {
                attachment = null;
            } catch (Exception e) {
                attachment = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressBar.dismiss();

            if (SetEquipmentDetails != null) {
                if (SetEquipmentDetails.toString().contains("Session")) {

                    String Result = SetEquipmentDetails;
                    String replace = Result.replace("Error - ", "");

                    Session = 8;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {

                    if (msg_show == 1) {
                        if (sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Equipment Detached!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrtsuccess = true;
                                            msg_show = 0;
                                            equip_list.remove(detach_pos);
                                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                            equp_list.setAdapter(adapter);
                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    } else {
                        if (sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt = new SweetAlertDialog(equipment_inventory_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Equipment transferred to Branch " + loginbranch + " and counted")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrtsuccess = true;
                                            msg_show = 0;
                                            equip_list.remove(detach_pos);
                                            adapter = new CustomAdapter(equipment_inventory_activity.this, equip_list);
                                            equp_list.setAdapter(adapter);
                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    }
                }
            } else {


            }
        }
    }

    public class AsyncGetDealerBranch extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + usertoken);

                dealerbranch = WebServiceConsumer.getInstance().GetBranchV2(
                        usertoken, "E");

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

            if (dealerbranch != null) {

                if (dealerbranch.size() == 1) {

                    if (dealerbranch.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = dealerbranch.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (dealerbranch.get(0).getMessage().contains("Session")) {
                            Session = 13;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {
                        ProgressBar.dismiss();
                        if (Dialog == null) {
                            Dialog = new Dialog(equipment_inventory_activity.this);
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

                            adapter_branch = new CustomAdapter_Branch(equipment_inventory_activity.this, branchlist);
                            list.setAdapter(adapter_branch);

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
                        Dialog = new Dialog(equipment_inventory_activity.this);
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

                        adapter_branch = new CustomAdapter_Branch(equipment_inventory_activity.this, branchlist);
                        list.setAdapter(adapter_branch);

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
                    Dialog = new Dialog(equipment_inventory_activity.this);
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

                    adapter_branch = new CustomAdapter_Branch(equipment_inventory_activity.this, branchlist);
                    list.setAdapter(adapter_branch);

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

    public class CustomAdapter_Branch extends CustomAdapter {
        ArrayList<GetDealerBranch> result;
        ;
        private LayoutInflater inflater = null;

        public CustomAdapter_Branch(Context context, ArrayList<GetDealerBranch> list) {
            super();
            result = list;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (result != null)
            return result.size();
            else
            return 0;
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {

            return position;
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

            handleasync = true;

            holder.process_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    process_list = "";

                    btn_submit.setEnabled(true);
                   /* submit_layout.setEnabled(false);
                    submit_layout.setVisibility(View.GONE);*/
                    btn_submit.setVisibility(View.VISIBLE);
//                    footer.setVisibility(View.GONE);

                    String process_list = holder.process_list.getText().toString();
                    branch_no.setText(process_list);
                    branch_name = branch_no.getText().toString();

                    processid.setText("");
                    equp_id.setText("");
                    model.setText("");
                    serial.setText("");
                    make.setText("");
                    branch_no.setText(branch_name);

                    Sessiondata.getInstance().setEqu_branch_name(branch_name);
                    Sessiondata.getInstance().setSub_process("");
                    Sessiondata.getInstance().setMake("");
                    Sessiondata.getInstance().setSerial("");
                    Sessiondata.getInstance().setModel("");
                    Sessiondata.getInstance().setUnitId("");

                    for (int ii = 0; ii < branch_name.length(); ii++) {

                        Character character = branch_name.charAt(ii);

                        if (character.toString().equals("-")) {

                            branch_name = branch_name.substring(0, ii);

                            Log.d("branch_trim", "" + branch_name);
                            break;
                        }
                    }

                    GlobalVariables.checkbtn_load = 0;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        btn_counted.setBackground(grey_drawable);
                        btn_uncounted.setBackground(grey_drawable);
                        btn_counted.setTextColor(Color.BLACK);
                        btn_uncounted.setTextColor(Color.BLACK);
                    } else {
                        btn_counted.setBackgroundDrawable(grey_drawable);
                        btn_uncounted.setBackgroundDrawable(grey_drawable);
                        btn_counted.setTextColor(Color.BLACK);
                        btn_uncounted.setTextColor(Color.BLACK);
                    }

                    Dialog.dismiss();

                }
            });

            return rowview_processid;

        }

        public class Holder {
            TextView process_list;
        }

    }

    public class AsyncSessionLoginTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(equipment_inventory_activity.this);
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

                if (loginObject.getUserid() == 0) {

                } else {
                    Sessiondata.getInstance().setLoginObject(loginObject);
                }

                Log.d("New_Session_Usertoken", "" + Sessiondata.getInstance().getLoginObject().getUsertoken());

                if (loginObject.getUserid() == 0) {
                    ProgressBar.dismiss();


                } else {
                    ProgressBar.dismiss();

                    if (Session == 0) {
                        if (checkInternetConenction()) {

                            new AsyncGetEquipmentList().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    } else if (Session == 1) {
                        if (checkInternetConenction()) {

                            if (branch_name.isEmpty()) {
                                branch_name = branch_no.getText().toString();

                                for (int ii = 0; ii < branch_name.length(); ii++) {

                                    Character character = branch_name.charAt(ii);

                                    if (character.toString().equals("-")) {

                                        branch_name = branch_name.substring(0, ii);

                                        Log.d("branch_trim", "" + branch_name);
                                        break;
                                    }
                                }
                            }

                            new AsyncGetEquipmentProcessid().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 2) {
                        if (checkInternetConenction()) {

                            new AsyncGetAttachments().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 3) {
                        if (checkInternetConenction()) {
                            Log.d("UncountFilter_Session", "true");
                            GlobalVariables.checkbtn_load = 0;
                            return_once = 1;
                            GlobalVariables.showerrormsg = true;
                            new AsyncGetEquipmentListUncountFilter().execute();
                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 4) {
                        if (checkInternetConenction()) {

                            new AsyncGetEquipmentBranch().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 5) {
                        if (checkInternetConenction()) {

                            new AsyncDetachEquipment().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 6) {
                        if (checkInternetConenction()) {

                            new AsyncUpdateEquipment().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 7) {
                        if (checkInternetConenction()) {

                            new AsyncUpdateEquipmentforAttach().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 8) {
                        if (checkInternetConenction()) {

                            new AsyncSetEquipmentDetails().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 9) {
                        if (checkInternetConenction()) {

                            new AsyncGetAttachments_new().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 10) {
                        if (checkInternetConenction()) {

                            new AsyncGetEquipmentBranch_transfer().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 11) {
                        if (checkInternetConenction()) {

                            new AsyncSetEquipmentDetails_transfer().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 12) {
                        if (checkInternetConenction()) {

                            new AsyncUpdateEquipment_transfer().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 13) {
                        if (checkInternetConenction()) {

                            new AsyncGetDealerBranch().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 14) {
                        if (checkInternetConenction()) {

                            new AsyncUnCountEquipment().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 15) {
                        if (checkInternetConenction()) {
                            return_once = 1;
                            new AsyncGetEquipmentListCounted().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 16) {
                        if (checkInternetConenction()) {
                            return_once = 1;
                            new AsyncGetEquipmentListUnCounted().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 17) {
                        if (checkInternetConenction()) {
                            Log.d("CountFilter_Session", "true");
                            GlobalVariables.checkbtn_load = 1;
                            return_once = 1;
                            GlobalVariables.showerrormsg = true;
                            new AsyncGetEquipmentListCountedFilter().execute();

                        } else {

                            Toast.makeText(equipment_inventory_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } else {
                ProgressBar.dismiss();

                if ((mDialognodata == null) || !mDialognodata.isShowing()) {
                    mDialognodata = new Dialog(equipment_inventory_activity.this);
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
                }

            }

        }

    }

    public String split_string(String string) {
        String[] s = string.split("-");
        Log.d("split_string", s[0]);
        return s[0];

    }

}