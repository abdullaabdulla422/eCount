package com.ebs.ecount.parts_physical_counting;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.hardware.usb.UsbDevice;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cognex.dataman.sdk.ConnectionState;
import com.cognex.mobile.barcode.sdk.ReadResult;
import com.cognex.mobile.barcode.sdk.ReadResults;
import com.cognex.mobile.barcode.sdk.ReaderDevice;
import com.ebs.ecount.R;
import com.ebs.ecount.initial.LoginActivity;
import com.ebs.ecount.objects.GetPartsQuantity;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.parts_physical_inventory.AttachImageActivity;
import com.ebs.ecount.uidesigns.ProgressBar;
import com.ebs.ecount.uidesigns.SimpleScannerActivity;
import com.ebs.ecount.uidesigns.SweetAlertDialog;
import com.ebs.ecount.utils.Sessiondata;
import com.ebs.ecount.webutils.WebServiceConsumer;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import com.cognex.mobile.barcode.sdk.ReaderDevice.ReaderDeviceListener;
import com.cognex.mobile.barcode.sdk.ReaderDevice.OnConnectionCompletedListener;

/**
 * Created by cbe-teclwsp-009 on 13/4/17.
 */

public class activity_handwrites_notmatch extends Activity implements OnConnectionCompletedListener, ReaderDeviceListener {

    Gallery gallery;
    Button back;
    ArrayList<byte[]> attachedFilesData;
    Bundle extras;
    activity_handwrites_notmatch.ImageAdapter adapter;
    ArrayList<Integer> removableList = new ArrayList<Integer>();
    ArrayList<GetPartsQuantity> load_partsqty;

    Boolean sweetalrtsuccess=true;

    private boolean FAB_Status = false;

    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;
    Animation show_fab_3;
    Animation hide_fab_3;
    private Boolean isFabOpen = false;

    private Uri fileUri;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int PICK_FROM_FILE = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final String IMAGE_DIRECTORY_NAME = "eCount/Camera";

    public double latitude, longitude;
    Boolean sweetalrterror = true;
    ExifInterface exif;
    File mediaFile;
    Bitmap bitmap;
    View fab;
    SubActionButton button1,button2,button3;
    FloatingActionMenu rightLowerMenu;
    ImageView rlIcon1,rlIcon2,rlIcon3;
    Typeface header_face,txt_face;

    int Session = 0;
    LoginObject loginObject = null;
    Dialog mDialognodata;

    String branch;
    int processids;
    String mfg;
    Boolean sweetalrt = true;
    SweetAlertDialog sweetalt;

    EditText processid,branchs,partno,mfr,description,quantity,primary_bin_loc,secondary_bin_loc,notes;
    TextView txt_header,attach_label,notes_label,sec_bin_loc,primary_bin_label,qty_label,desc_label,mfr_label,
            part_label,branch_label,processid_label;

    ImageView secondary_search,primarybin_search;

    private static final int ZBAR_CAMERA_PERMISSION = 1;

    String userToken,addnewpart;
    Dialog mDialogmsg;
    String branch_name;

    String branch_name_nm;
    String bundle_process_Id,bundle_part_number,bundle_primarybin,bundle_secondarybin;
    SweetAlertDialog Sweetalt_list;
    String submit_processid,submit_branch,submit_part,submit_mfg,submit_qty,submit_primary,submit_secondary,submit_notes;
    Boolean Sweetalrt_list=true;

    int isDeviceConnected = 1;
    Dialog mDialogscan = null;
    HashMap<String, UsbDevice> usbDevices;

    private Context mContext;
    public static ReaderDevice readerDevice;
    private String validateParts_v1;
    private String user_token;


    public static enum DeviceType { MX_1000}
    private static final DeviceType[] deviceTypeValues = DeviceType.values();
    public static DeviceType deviceTypeFromInt(int i) { return deviceTypeValues[i];	}

    public static boolean isDevicePicked = false;
    public static boolean dialogAppeared = false;
    public static String selectedDevice = "";
    public static boolean fragmentActive = false;
    private Class<?> mClss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts_no_match);

        mContext = this;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            String[] perms = {"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE"};

            int permsRequestCode = 200;

            requestPermissions(perms, permsRequestCode);
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        header_face= Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");
        txt_face = Typeface.createFromAsset(getAssets(), "fonts/helr45w.ttf");

        processid = (EditText) findViewById(R.id.processid);
        branchs = (EditText) findViewById(R.id.branch);
        partno = (EditText) findViewById(R.id.partno);
        mfr = (EditText) findViewById(R.id.mfr);
        quantity = (EditText) findViewById(R.id.quantity);
        primary_bin_loc = (EditText) findViewById(R.id.primary_bin_loc);
        secondary_bin_loc = (EditText) findViewById(R.id.secondary_bin_loc);
        notes = (EditText) findViewById(R.id.notes);


        mfr.setText(Sessiondata.getInstance().getMfg());

        primarybin_search = (ImageView) findViewById(R.id.primarybin_search);
        secondary_search = (ImageView) findViewById(R.id.secondary_search);

        Sessiondata.getInstance().setScanner_partno(0);

        primary_bin_loc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Sessiondata.getInstance().setHw_value("start");
                Sessiondata.getInstance().setHw_value_notmatch("");
                Sessiondata.getInstance().setScanner_hwstartbin(8);
                Sessiondata.getInstance().setScanner_hwendbin(0);
                return false;
            }
        });

        secondary_bin_loc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Sessiondata.getInstance().setHw_value("end");
                Sessiondata.getInstance().setHw_value_notmatch("");
                Sessiondata.getInstance().setScanner_hwstartbin(0);
                Sessiondata.getInstance().setScanner_hwendbin(9);
                return false;
            }
        });


        txt_header = (TextView) findViewById(R.id.txt_header);
        attach_label = (TextView) findViewById(R.id.txt_attachment);
        notes_label = (TextView) findViewById(R.id.txt_notes);
        sec_bin_loc = (TextView) findViewById(R.id.sec_bin_loc);
        primary_bin_label = (TextView) findViewById(R.id.primary_bin_label);
        qty_label = (TextView) findViewById(R.id.qty_label);
        mfr_label = (TextView) findViewById(R.id.mfr_label);
        part_label = (TextView) findViewById(R.id.part_label);
        branch_label = (TextView) findViewById(R.id.branch_label);
        processid_label = (TextView) findViewById(R.id.processid_label);


        attach_label.setTypeface(header_face);
        notes_label.setTypeface(header_face);
        sec_bin_loc.setTypeface(header_face);
        primary_bin_label.setTypeface(header_face);
        qty_label.setTypeface(header_face);
        mfr_label.setTypeface(header_face);
        part_label.setTypeface(header_face);
        processid_label.setTypeface(header_face);
        branch_label.setTypeface(header_face);



        Button submit = (Button)findViewById(R.id.btn_submit);
        notes = (EditText) findViewById(R.id.notes);
        submit.setTypeface(header_face);

        txt_header.setTypeface(header_face);


        processid.setTypeface(txt_face);
        branchs.setTypeface(txt_face);
        partno.setTypeface(txt_face);
        mfr.setTypeface(txt_face);
        quantity.setTypeface(txt_face);
        primary_bin_loc.setTypeface(txt_face);
        secondary_bin_loc.setTypeface(txt_face);
        notes.setTypeface(txt_face);

        branchs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss=s.toString();
                if(!ss.equals(ss.toUpperCase()))
                {
                    ss=ss.toUpperCase();
                    branchs.setText(ss);
                }
            }
        });

        partno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss=s.toString();
                if(!ss.equals(ss.toUpperCase()))
                {
                    ss=ss.toUpperCase();
                    partno.setText(ss);
                }
                partno.setSelection(partno.getText().length());
            }
        });

        mfr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss=s.toString();
                if(!ss.equals(ss.toUpperCase()))
                {
                    ss=ss.toUpperCase();
                    mfr.setText(ss);
                }
                mfr.setSelection(mfr.getText().length());
            }
        });

        primary_bin_loc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss=s.toString();
                if(!ss.equals(ss.toUpperCase()))
                {
                    ss=ss.toUpperCase();
                    primary_bin_loc.setText(ss);
                }
                primary_bin_loc.setSelection(primary_bin_loc.getText().length());
            }
        });

        secondary_bin_loc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss=s.toString();
                if(!ss.equals(ss.toUpperCase()))
                {
                    ss=ss.toUpperCase();
                    secondary_bin_loc.setText(ss);
                }
                secondary_bin_loc.setSelection(secondary_bin_loc.getText().length());
            }
        });

        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss=s.toString();
                if(!ss.equals(ss.toUpperCase()))
                {
                    ss=ss.toUpperCase();
                    notes.setText(ss);
                }
                notes.setSelection(notes.getText().length());
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String startbin = bundle.getString("value4");

            if(Sessiondata.getInstance().getHw_value().toString().equalsIgnoreCase("start")) {
                Sessiondata.getInstance().setHw_startbin(startbin);
            }
        }

        Bundle bundleendbin = getIntent().getExtras();
        if (bundleendbin != null){

            String endbin = bundleendbin.getString("value5");

            if(Sessiondata.getInstance().getHw_value().toString().equalsIgnoreCase("end")) {
                Sessiondata.getInstance().setHw_endbin(endbin);
            }
        }

        Bundle addpart = getIntent().getExtras();

        if (addpart != null){

            bundle_process_Id = addpart.getString("process_Id");
            bundle_part_number = addpart.getString("part_number");
            bundle_primarybin = addpart.getString("primary_bin");
            bundle_secondarybin = addpart.getString("secondary_bin");

            if(Sessiondata.getInstance().getHw_value_notmatch().toString().equalsIgnoreCase("hwpart_id")) {

                processid.setText(bundle_process_Id);
                partno.setText(bundle_part_number);
                primary_bin_loc.setText(bundle_primarybin);
                secondary_bin_loc.setText(bundle_secondarybin);

                Sessiondata.getInstance().setHw_partnumber(bundle_part_number);
                Sessiondata.getInstance().setHw_processid(bundle_process_Id);
                Sessiondata.getInstance().setHw_startbin(bundle_primarybin);
                Sessiondata.getInstance().setHw_endbin(bundle_secondarybin);
            }
        }

        branch_name = Sessiondata.getInstance().getCounting_branchname();

        branchs.setText(branch_name);

        for (int ii = 0; ii < branch_name.length(); ii++) {

            Character character = branch_name.charAt(ii);

            if (character.toString().equals("-")) {

                branch_name_nm = branch_name.substring(0,ii);

                Log.d("branch_trim", "" + branch_name_nm);
                break;
            }
        }


        primarybin_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Sessiondata.getInstance().setScanner_partreceipt(0);
                    Sessiondata.getInstance().setScanner_partreceiving(0);
                    Sessiondata.getInstance().setScanner_replace(0);
                    Sessiondata.getInstance().setScanner_counting1(0);
                    Sessiondata.getInstance().setScanner_counting2(0);
                    Sessiondata.getInstance().setScanner_inventory(0);

                    Sessiondata.getInstance().setScanner_partnumber(0);
                    Sessiondata.getInstance().setScanner_hwstartbin(8);
                    Sessiondata.getInstance().setScanner_hwendbin(0);

                    Sessiondata.getInstance().setHw_startbin(primary_bin_loc.getText().toString());
                    Sessiondata.getInstance().setHw_endbin(secondary_bin_loc.getText().toString());
                    Sessiondata.getInstance().setHw_value("start");
                    Sessiondata.getInstance().setHw_value_notmatch("");

                    Sessiondata.getInstance().setHw_partnumber(partno.getText().toString());
                    Sessiondata.getInstance().setHw_processid(processid.getText().toString());
                    Sessiondata.getInstance().setHw_mfr(mfr.getText().toString());
                    Sessiondata.getInstance().setHw_notes(notes.getText().toString());
                    Sessiondata.getInstance().setHw_qty(quantity.getText().toString());


                    launchActivity(SimpleScannerActivity.class);
            }
        });

        secondary_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Sessiondata.getInstance().setScanner_partreceipt(0);
                    Sessiondata.getInstance().setScanner_partreceiving(0);
                    Sessiondata.getInstance().setScanner_replace(0);
                    Sessiondata.getInstance().setScanner_counting1(0);
                    Sessiondata.getInstance().setScanner_counting2(0);
                    Sessiondata.getInstance().setScanner_inventory(0);

                    Sessiondata.getInstance().setScanner_partnumber(0);
                    Sessiondata.getInstance().setScanner_hwstartbin(0);
                    Sessiondata.getInstance().setScanner_hwendbin(9);

                    Sessiondata.getInstance().setHw_startbin(primary_bin_loc.getText().toString());
                    Sessiondata.getInstance().setHw_endbin(secondary_bin_loc.getText().toString());
                    Sessiondata.getInstance().setHw_value("end");
                    Sessiondata.getInstance().setHw_value_notmatch("");

                    Sessiondata.getInstance().setHw_partnumber(partno.getText().toString());
                    Sessiondata.getInstance().setHw_processid(processid.getText().toString());
                    Sessiondata.getInstance().setHw_mfr(mfr.getText().toString());
                    Sessiondata.getInstance().setHw_notes(notes.getText().toString());
                    Sessiondata.getInstance().setHw_qty(quantity.getText().toString());

                    launchActivity(SimpleScannerActivity.class);
            }
        });

        fab = findViewById(R.id.add);

        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);

        hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
        show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
        hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        ImageView item1 = new ImageView(this);
        item1.setImageResource(R.drawable.ic_action_picture);


        ImageView item2 = new ImageView(this);
        item2.setImageResource(R.drawable.ic_action_camera);

        ImageView item3 = new ImageView(this);
        item3.setImageResource(R.color.color_transparent);

        button1 = itemBuilder.setContentView(item1).build();
        button2 = itemBuilder.setContentView(item2).build();
        button3 = itemBuilder.setContentView(item3).build();

        button3.setBackgroundResource(R.color.color_transparent);

        new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(fab)

                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);

        rLSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue_selector));

        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);

        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);

        rLSubBuilder.setLayoutParams(blueContentParams);

        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        rLSubBuilder.setLayoutParams(blueParams);

        rlIcon1 = new ImageView(this);
        rlIcon2 = new ImageView(this);
        rlIcon3 = new ImageView(this);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_picture));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_camera));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.color.color_transparent));

        rlIcon3.setBackgroundResource(R.color.color_transparent);

        button1 = rLSubBuilder.setContentView(rlIcon1).build();
        button2 = rLSubBuilder.setContentView(rlIcon2).build();
        button3 = rLSubBuilder.setContentView(rlIcon3).build();

        button3.setBackgroundResource(R.color.color_transparent);

        rightLowerMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(button1, blueContentParams).build())
                .addSubActionView(rLSubBuilder.setContentView(button2, blueContentParams).build())
                .addSubActionView(button3)
                .attachTo(fab)
                .build();

        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                fab.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
                rlIcon1.setAnimation(show_fab_1);
                rlIcon2.setAnimation(show_fab_2);
                FAB_Status = true;

            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                fab.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
                rlIcon1.setAnimation(hide_fab_1);
                rlIcon2.setAnimation(hide_fab_2);
                FAB_Status = false;
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                submit_processid = processid.getText().toString();

                submit_branch = branchs.getText().toString();

                submit_part = partno.getText().toString();
                submit_mfg = mfr.getText().toString();
                submit_qty = quantity.getText().toString();
                submit_primary = primary_bin_loc.getText().toString();
                submit_secondary = secondary_bin_loc.getText().toString();
                submit_notes = notes.getText().toString();


                if (submit_mfg.toString().equalsIgnoreCase("")) {

                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(activity_handwrites_notmatch.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the manufacturer")
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


                } else if (submit_qty.toString().equalsIgnoreCase("")) {

                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(activity_handwrites_notmatch.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the qty")
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
                else if (submit_notes.toString().equalsIgnoreCase("")) {

                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(activity_handwrites_notmatch.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the notes")
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

                }else {

                    if (checkInternetConenction()) {
                        Sessiondata.getInstance().setMfg("");
//                        new AsyncAddNewPartNumber().execute();
                        new AsyncValidateParts_v1().execute();

                    } else {

                        Toast.makeText(activity_handwrites_notmatch.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                    }


                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sessiondata.getInstance().setHw_startbin(primary_bin_loc.getText().toString());
                Sessiondata.getInstance().setHw_endbin(secondary_bin_loc.getText().toString());

                Sessiondata.getInstance().setHw_mfr(mfr.getText().toString());
                Sessiondata.getInstance().setHw_notes(notes.getText().toString());
                Sessiondata.getInstance().setHw_qty(quantity.getText().toString());


                FAB_Status = false;
                rightLowerMenu.close(true);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
                rlIcon1.setAnimation(hide_fab_1);
                rlIcon2.setAnimation(hide_fab_2);

                Sessiondata.getInstance().setHw_imageData(null);
                fileUri = null;
                showFileChooser();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sessiondata.getInstance().setHw_startbin(primary_bin_loc.getText().toString());
                Sessiondata.getInstance().setHw_endbin(secondary_bin_loc.getText().toString());

                Sessiondata.getInstance().setHw_mfr(mfr.getText().toString());
                Sessiondata.getInstance().setHw_notes(notes.getText().toString());
                Sessiondata.getInstance().setHw_qty(quantity.getText().toString());

                FAB_Status = false;
                rightLowerMenu.close(true);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
                rlIcon1.setAnimation(hide_fab_1);
                rlIcon2.setAnimation(hide_fab_2);

                captureImage();
            }
        });


        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            Sessiondata.getInstance().setHw_imageData(null);
        }

        back = (Button) findViewById(R.id.back);
        back.setTypeface(txt_face);

        gallery = (Gallery) findViewById(R.id.Image_gallery);

        attachedFilesData = new ArrayList<>();

        attachedFilesData = Sessiondata.getInstance().getHw_generalimages();

        Sessiondata.getInstance().setHw_imageData(null);
        attachedFilesData.addAll(Sessiondata.getInstance().getHw_attachedFilesData());

        adapter = new activity_handwrites_notmatch.ImageAdapter(activity_handwrites_notmatch.this);
        adapter.notifyDataSetChanged();
        gallery.setAdapter(adapter);

        extras = getIntent().getExtras();

        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FAB_Status) {
                    ClearSession();
                    Sessiondata.getInstance().setLoad_value("Load");
                    Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                    myintent.putExtra("LoadList","Onresume");
                    startActivity(myintent);

                   overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                }else {
                    FAB_Status = false;
                    rightLowerMenu.close(true);

                    PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                    ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                    animation.start();
                    rlIcon1.setAnimation(hide_fab_1);
                    rlIcon2.setAnimation(hide_fab_2);
                }

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FAB_Status) {
                    ClearSession();
                    Sessiondata.getInstance().setLoad_value("Load");
                    Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                    myintent.putExtra("LoadList","Onresume");
                    startActivity(myintent);
                    overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                }else {
                    FAB_Status = false;
                    rightLowerMenu.close(true);

                    PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                    ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                    animation.start();
                    rlIcon1.setAnimation(hide_fab_1);
                    rlIcon2.setAnimation(hide_fab_2);
                }
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
        activity_handwrites_notmatch.readerDevice = ReaderDevice.getMXDevice(mContext);
        readerDevice.startAvailabilityListening();
        selectedDevice = "MX Scanner";
        activity_handwrites_notmatch.readerDevice.setReaderDeviceListener(this);
        activity_handwrites_notmatch.readerDevice.enableImage(true);
        activity_handwrites_notmatch.readerDevice.connect(activity_handwrites_notmatch.this);
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

 if (Sessiondata.getInstance().getScanner_hwstartbin() == 8){
     Sessiondata.getInstance().setHw_startbin(result);

                }else if (Sessiondata.getInstance().getScanner_hwendbin() == 9){
     Sessiondata.getInstance().setHw_endbin(result);

                }

                primary_bin_loc.setText(Sessiondata.getInstance().getHw_startbin());
                secondary_bin_loc.setText(Sessiondata.getInstance().getHw_endbin());

            }
        }, 500);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(primary_bin_loc.getWindowToken(), 0);
    }


    @Override
    public void onAvailabilityChanged(ReaderDevice reader) {
        if (reader.getAvailability() == ReaderDevice.Availability.AVAILABLE) {
            activity_handwrites_notmatch.readerDevice.connect(activity_handwrites_notmatch.this);
        } else {
            // DISCONNECTED USB DEVICE
            activity_handwrites_notmatch.readerDevice.connect(activity_handwrites_notmatch.this);
            activity_handwrites_notmatch.readerDevice.disconnect();
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
        activity_handwrites_notmatch.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.C128, true, null);
        activity_handwrites_notmatch.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.DATAMATRIX, true, null);
        activity_handwrites_notmatch.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.UPC_EAN, true, null);
        activity_handwrites_notmatch.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.QR, true, null);

        //example sendCommand
        activity_handwrites_notmatch.readerDevice.getDataManSystem().sendCommand("SET SYMBOL.MICROPDF417 ON");
        activity_handwrites_notmatch.readerDevice.getDataManSystem().sendCommand("SET IMAGE.SIZE 0");

    }

    public void ClearSession(){
        Sessiondata.getInstance().setMfg("");
        Sessiondata.getInstance().setHw_startbin("");
        Sessiondata.getInstance().setHw_endbin("");
        Sessiondata.getInstance().setHw_value("");

        Sessiondata.getInstance().setScanner_hwstartbin(0);
        Sessiondata.getInstance().setScanner_hwendbin(0);
        Sessiondata.getInstance().setHw_imageData(null);

        if (Sessiondata.getInstance().getHw_generalimages() != null){
            Sessiondata.getInstance().getHw_generalimages().clear();
        }
        if (Sessiondata.getInstance().getHw_attachedFilesData() != null){
            Sessiondata.getInstance().getHw_attachedFilesData().clear();
        }
        if (Sessiondata.getInstance().getHw_imageData() != null){
            Sessiondata.getInstance().getHw_imageData();
        }

        Sessiondata.getInstance().setPart_value("");
        Sessiondata.getInstance().setCount_value("");

        Sessiondata.getInstance().setHw_partnumber("");
        Sessiondata.getInstance().setHw_processid("");
        Sessiondata.getInstance().setHw_mfr("");
        Sessiondata.getInstance().setHw_notes("");
        Sessiondata.getInstance().setHw_qty("");

    }

    public void ClearSession_aftersubmit(){
        Sessiondata.getInstance().setHw_startbin("");
        Sessiondata.getInstance().setHw_endbin("");
        Sessiondata.getInstance().setHw_value("");

        Sessiondata.getInstance().setScanner_hwstartbin(0);
        Sessiondata.getInstance().setScanner_hwendbin(0);
        Sessiondata.getInstance().setHw_imageData(null);

        if (Sessiondata.getInstance().getHw_generalimages() != null){
            Sessiondata.getInstance().getHw_generalimages().clear();
        }
        if (Sessiondata.getInstance().getHw_attachedFilesData() != null){
            Sessiondata.getInstance().getHw_attachedFilesData().clear();
        }
        if (Sessiondata.getInstance().getHw_imageData() != null){
            Sessiondata.getInstance().getHw_imageData();
        }

        Sessiondata.getInstance().setCounting_startbin("");
        Sessiondata.getInstance().setCounting_endbin("");
        Sessiondata.getInstance().setCounting_partnew("");

        Sessiondata.getInstance().setPart_value("");
        Sessiondata.getInstance().setCount_value("");

        Sessiondata.getInstance().setHw_partnumber("");
        Sessiondata.getInstance().setHw_processid("");
        Sessiondata.getInstance().setHw_mfr("");
        Sessiondata.getInstance().setHw_notes("");
        Sessiondata.getInstance().setHw_qty("");

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

    private void addImageToSessionData() {
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            exif = new ExifInterface(fileUri.getPath());
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, String.valueOf(latitude));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, String.valueOf(longitude));

            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            exif.saveAttributes();

            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bitmap.getWidth(), (float) bitmap.getHeight() / 2);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            Sessiondata.getInstance().setHw_imageData(setExifMetaData(stream));

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Toast.makeText(this, ioe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "General ex : "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private byte[] setExifMetaData(ByteArrayOutputStream stream) throws IOException {
        String newFile = fileUri.getPath()+"_cmp.jpg";
        FileOutputStream fout = new FileOutputStream(newFile);
        fout.write(stream.toByteArray());
        fout.flush();
        fout.close();

        ExifInterface newExif = new ExifInterface(newFile);

        newExif = setExifAttribute(exif, newExif);

        newExif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
        newExif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));

        newExif.saveAttributes();
        FileInputStream inStream = new FileInputStream(newFile);
        byte [] buffer = new byte[inStream.available()];
        inStream.read(buffer);
        inStream.close();
        return buffer;
    }

    private ExifInterface setExifAttribute(ExifInterface exif, ExifInterface newExif) {

        if(exif.getAttribute(ExifInterface.TAG_DATETIME) != null ) {
            newExif.setAttribute(ExifInterface.TAG_DATETIME, exif.getAttribute(ExifInterface.TAG_DATETIME));
        }

        return newExif;
    }

    private void addImageToSessionDataImage() {
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory
                    .decodeFile(fileUri.getPath(), options);

            exif = new ExifInterface(fileUri.getPath());
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
                    String.valueOf(latitude));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
                    String.valueOf(longitude));
            String orientString = exif
                    .getAttribute(ExifInterface.TAG_ORIENTATION);

            int orientation = orientString != null ? Integer
                    .parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
            int rotationAngle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                rotationAngle = 270;

            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bitmap.getWidth(),
                    (float) bitmap.getHeight());

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            byte[] bytearrays = stream.toByteArray();
            stream.flush();
            stream.close();
            Sessiondata.getInstance().setHw_imageData(bytearrays);

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Toast.makeText(this, ioe.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "General ex : " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        try {

            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    PICK_FROM_FILE);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static String getPath(Context context, Uri uri)
            throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection,
                        null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                throw e;
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private boolean isDeviceSupportCamera() {
        return (getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA));
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile(AttachImageActivity.MEDIA_TYPE_IMAGE));
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void captureImage() {

        Intent inten = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri();
        inten.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        inten.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
                GradientDrawable.Orientation.LEFT_RIGHT);
        startActivityForResult(inten, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }


    @Override
    protected void onResume() {
        super.onResume();

        byte[] byteArray;
        if ((byteArray = Sessiondata.getInstance().getHw_imageData()) != null) {
            attachedFilesData.add(byteArray);
            Sessiondata.getInstance().setHw_imageData(null);
        }

        {

            adapter = new activity_handwrites_notmatch.ImageAdapter(activity_handwrites_notmatch.this);
            adapter.notifyDataSetChanged();
            gallery.setAdapter(adapter);
        }

        sweetalrt=true;

        primary_bin_loc.setText(Sessiondata.getInstance().getHw_startbin());
        secondary_bin_loc.setText(Sessiondata.getInstance().getHw_endbin());

        partno.setText(Sessiondata.getInstance().getHw_partnumber());
        processid.setText(Sessiondata.getInstance().getHw_processid());
        mfr.setText(Sessiondata.getInstance().getHw_mfr());
        notes.setText(Sessiondata.getInstance().getHw_notes());
        quantity.setText(Sessiondata.getInstance().getHw_qty());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int PICK_IMAGE_REQUEST = 2;
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                addImageToSessionData();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        else if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    fileUri = Uri.parse("file://"
                            + Environment
                            .getExternalStoragePublicDirectory(getPath(
                                    this, data.getData())));
                    fileUri = Uri.fromFile(new File(getPath(this,
                            data.getData())));
                    if (fileUri != null) {
                        String path = fileUri.toString();
                        if (path.toLowerCase().startsWith("file://")) {
                            path = (new File(URI.create(path)))
                                    .getAbsolutePath();
                        }
                    }
                    addImageToSessionDataImage();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity_handwrites_notmatch.this,
                            "Exception in choosing file", Toast.LENGTH_LONG)
                            .show();
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image selection", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to Choose image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                super.onMenuItemSelected(featureId, item);
                break;
        }
        return true;

    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        LayoutInflater inflator;
        ArrayList<byte[]> list;

        public ImageAdapter(Context c) {
            context = c;
            TypedArray a = obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();

            removableList = new ArrayList<Integer>();
            list = attachedFilesData;
            inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return attachedFilesData.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = inflator.inflate(R.layout.activity_add_images, null);
            final ImageView img_camera = (ImageView) view.findViewById(R.id.img_camera);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            byte[] data = list.get(position);
            Log.d("Image List",""+ Arrays.toString(data));
            final Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,
                    data.length, options);
            img_camera.setImageBitmap(bmp);

            Log.d("Image bitmap",""+bmp);
            img_camera.setBackgroundResource(itemBackground);
            img_camera.setScaleType(ImageView.ScaleType.FIT_CENTER);

            final ImageView selector = (ImageView) view.findViewById(R.id.close_btn);

            selector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    activity_handwrites_notmatch.ImageAdapter adapter = new activity_handwrites_notmatch.ImageAdapter(activity_handwrites_notmatch.this);
                    adapter.notifyDataSetChanged();
                    gallery.setAdapter(adapter);
                }
            });

            return view;
        }
    }

    @Override
    public void onBackPressed() {

        if (!FAB_Status) {
            ClearSession();
            Sessiondata.getInstance().setLoad_value("Load");
            Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
            myintent.putExtra("LoadList","Onresume");
            startActivity(myintent);
            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
        }else {
            FAB_Status = false;
            rightLowerMenu.close(true);

            PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
            ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
            animation.start();
            rlIcon1.setAnimation(hide_fab_1);
            rlIcon2.setAnimation(hide_fab_2);
        }
    }

    private boolean checkInternetConenction() {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class AsyncValidateParts_v1 extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar.showCommonProgressDialog(activity_handwrites_notmatch.this);

        }

        @Override
        protected Void doInBackground(Void... voids) {


//            String part_no = partno.getText().toString();
//            String mfg = mfr.getText().toString();

            try {

                user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + user_token);

                validateParts_v1 = WebServiceConsumer.getInstance().ValidateParts_v1(user_token, submit_part, submit_mfg);

            } catch (SocketTimeoutException e) {
                validateParts_v1 = null;
            } catch (Exception e) {
                validateParts_v1 = null;
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressBar.dismiss();

            if (validateParts_v1 != null)
            {

                if (validateParts_v1.toLowerCase().contains("session")) {

                    Session = 2;

                    if(checkInternetConenction()){

                        new AsyncSessionLoginTask().execute();

                    }else{
                        Toast.makeText(activity_handwrites_notmatch.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {

                    Log.d("validateEquipnumber", ""+ validateParts_v1);



                    if (validateParts_v1.equals("1"))
                    {

                        SweetAlertDialog sweetAlertDialog=  new SweetAlertDialog(activity_handwrites_notmatch.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Part is already exist!")
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
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.show();

                    }
                    else if (validateParts_v1.equals("0"))
                    {

//                        load_btn_Value_HW=1;

                        new AsyncAddNewPartNumber().execute();

                    }
                    else
                    {
                        Log.d("validateEquipnumber", ""+ validateParts_v1);

                    }

                }




            }



        }
    }

    public class AsyncAddNewPartNumber extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(activity_handwrites_notmatch.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken= Sessiondata.getInstance().getLoginObject().getUsertoken();

                Sessiondata.getInstance().setHw_attachedFilesData((ArrayList<byte[]>) attachedFilesData.clone());

                Log.d("submit_branch_trim", "" + branch_name_nm);

                addnewpart = WebServiceConsumer.getInstance().SetNewPartNumberV1(userToken,Integer.parseInt(processid.getText().toString())
                        ,branch_name_nm,mfr.getText().toString(),partno.getText().toString(),primary_bin_loc.getText().toString()
                        ,secondary_bin_loc.getText().toString(),Integer.parseInt(quantity.getText().toString()),notes.getText().toString());

            }
            catch (java.net.SocketTimeoutException e) {
                addnewpart = null;
            }
            catch (Exception e) {
                addnewpart = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            ProgressBar.dismiss();

            if(addnewpart != null) {

                if (addnewpart.toString().contains("Session")) {

                    String Result = addnewpart;
                    String replace = Result.replace("Error - ", "");
                    Session = 0;

                    if(checkInternetConenction()){

                        new AsyncSessionLoginTask().execute();

                    }else{
                        Toast.makeText(activity_handwrites_notmatch.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }else  if (addnewpart.toString().equalsIgnoreCase("0")) {

                    if(sweetalrterror) {
                        sweetalrterror = false;

                        sweetalt=  new SweetAlertDialog(activity_handwrites_notmatch.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText(addnewpart+"!")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        sweetalrterror=true;
                                        sDialog.dismiss();
                                    }
                                });
                        sweetalt.setCancelable(false);
                        sweetalt.show();
                    }
                } else {

                        if(sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt=  new SweetAlertDialog(activity_handwrites_notmatch.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Message!")
                                    .setContentText("Part has been added successfully!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrtsuccess=true;
                                            ClearSession_aftersubmit();

                                            Sessiondata.getInstance().setLoad_value("Load");

                                            Sessiondata.getInstance().setFlagphy_addpart(0);

                                           Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                                            myintent.putExtra("LoadList","Onresume");
                                            startActivity(myintent);
                                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                                            sDialog.dismiss();

                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                }

            }else {

                if (sweetalrt) {
                    sweetalrt = false;

                    Sweetalt_list = new SweetAlertDialog(activity_handwrites_notmatch.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setContentText("Part is not added successfully!")
                            .setCancelText("Ok")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override

                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrt = true;
                                    ClearSession();
                                    Sessiondata.getInstance().setLoad_value("Load");

                                    Sessiondata.getInstance().setFlagphy_addpart(1);

                                    Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                                    myintent.putExtra("LoadList","Onresume");
                                    startActivity(myintent);
                                    overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                                    sDialog.dismiss();
                                }
                            });
                    Sweetalt_list.setCancelable(false);
                    Sweetalt_list.show();
                }
            }
        }
    }

    public class AsyncSessionLoginTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(activity_handwrites_notmatch.this);
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

                Log.d("New_Session_Usertoken",""+Sessiondata.getInstance().getLoginObject().getUsertoken());

                if (loginObject.getUserid() == 0) {
                    ProgressBar.dismiss();

                    if (mDialogmsg == null){
                        mDialogmsg = new Dialog(activity_handwrites_notmatch.this);
                        mDialogmsg.setCanceledOnTouchOutside(false);
                        mDialogmsg.setCancelable(false);
                        mDialogmsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialogmsg.setContentView(R.layout.activity_message);

                        TextView mDialogFreeCancelButton = (TextView) mDialogmsg.findViewById(R.id.dialog_social_cancel);

                        TextView mDialogFreeOKButton = (TextView) mDialogmsg.findViewById(R.id.dialog_social_ok);

                        TextView txt_dialog = (TextView) mDialogmsg.findViewById(R.id.txt_dialog);
                        String Result = loginObject.getMessage().toString();
                        txt_dialog.setText(Result);

                        final Dialog finalMDialog = mDialogmsg;
                        mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(activity_handwrites_notmatch.this,LoginActivity.class);
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
                        mDialogmsg.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        mDialogmsg.show();

                    }
                    else if (!mDialogmsg.isShowing()){
                        mDialogmsg.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        mDialogmsg.show();
                    }

                } else {
                    ProgressBar.dismiss();
                    if(Session == 0){
                        if(checkInternetConenction()){

                            new AsyncAddNewPartNumber().execute();

                        }else{

                            Toast.makeText(activity_handwrites_notmatch.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }else if(Session == 1) {
                        if (checkInternetConenction()) {

                            new AsyncPhysicalCountingList().execute();

                        } else {

                            Toast.makeText(activity_handwrites_notmatch.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }
                    else if(Session == 2) {
                        if (checkInternetConenction()) {

                            new AsyncValidateParts_v1().execute();

                        } else {

                            Toast.makeText(activity_handwrites_notmatch.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }

                }
            } else {

                if (mDialognodata == null){
                    mDialognodata = new Dialog(activity_handwrites_notmatch.this);
                    mDialognodata.setCanceledOnTouchOutside(false);
                    mDialognodata.setCancelable(false);
                    mDialognodata.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialognodata.setContentView(R.layout.dialog_no_data);

                    TextView mDialogFreeOKButton = (TextView) mDialognodata.findViewById(R.id.dialog_social_ok);

                    TextView txt_dialog = (TextView) mDialognodata.findViewById(R.id.txt_dialog);

                    String Result = loginObject.getMessage().toString();

                    txt_dialog.setText(Result);


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

                    ProgressBar.dismiss();
                }
                else if (!mDialognodata.isShowing()){
                    mDialognodata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    mDialognodata.show();
                }

            }
        }
    }


    public class AsyncPhysicalCountingList extends AsyncTask<Void, Void, Void>{

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(activity_handwrites_notmatch.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken= Sessiondata.getInstance().getLoginObject().getUsertoken();
                int load_processid = Integer.parseInt(Sessiondata.getInstance().getCounting_processid());
                String load_startbin = Sessiondata.getInstance().getCounting_startbin();
                String load_endbin= Sessiondata.getInstance().getCounting_endbin();
                Boolean load_partsnotcounted = Sessiondata.getInstance().getLoaded_partsnotcounted();
                String load_branch = Sessiondata.getInstance().getLoad_branch();
                String load_partNum = Sessiondata.getInstance().getCounting_partnew();

                int startindex_listmore =0;
                int endindex_listmore = 50;

                Log.d("UserToken",""+userToken);
                Log.d("load_processid",""+load_processid);
                Log.d("load_startbin",""+load_startbin);
                Log.d("load_endbin",""+load_endbin);
                Log.d("load_branch",""+load_branch);
                Log.d("load_partsnotcounted",""+load_partsnotcounted);
                Log.d("startindex_listmore",""+startindex_listmore);
                Log.d("endindex_listmore",""+endindex_listmore);
                Log.d("load_partNum",""+load_partNum);

                load_partsqty= WebServiceConsumer.getInstance().GetpartsqtyV4(userToken,load_processid,load_startbin,load_endbin,load_branch,load_partsnotcounted,startindex_listmore,endindex_listmore,load_partNum);

            } catch (SocketTimeoutException e){

                load_partsqty =null;
            }

            catch (Exception e) {
                load_partsqty =null;
                e.printStackTrace();
            }


            return null;
        }



        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setPartsquantity(load_partsqty);


            if (load_partsqty != null){


                if(load_partsqty.size()==1){

                    if(load_partsqty.get(0).getMessage().length() !=0){

                        ProgressBar.dismiss();

                        if (load_partsqty.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")){

                            Sessiondata.getInstance().setPartsquantity(null);

                            ClearSession();

                            Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                            startActivity(myintent);
                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);

                        }else if (load_partsqty.get(0).getMessage().toString().contains("Procesd Id not found")){
                            Sessiondata.getInstance().setPartsquantity(null);

                            ClearSession();

                            Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                            startActivity(myintent);
                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                        }
                        else if (load_partsqty.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")){

                            Sessiondata.getInstance().setPartsquantity(null);

                            ClearSession();

                            Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                            startActivity(myintent);
                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);


                        }else if (load_partsqty.get(0).getMessage().toString().contains("Data Conversion Error")){
                            Sessiondata.getInstance().setPartsquantity(null);

                            ClearSession();

                            Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                            startActivity(myintent);
                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                        }
                        else {


                            String Result = load_partsqty.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (load_partsqty.get(0).getMessage().toString().contains("Session")) {
                                Session = 1;
                                if(checkInternetConenction()){

                                    new AsyncSessionLoginTask().execute();

                                }else{

                                    Toast.makeText(activity_handwrites_notmatch.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    }else{

                        if(sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt=  new SweetAlertDialog(activity_handwrites_notmatch.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Message!")
                                    .setContentText("Part has been added successfully!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrtsuccess=true;
                                            ClearSession_aftersubmit();
                                            Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                                           startActivity(myintent);
                                           overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                                            sDialog.dismiss();

                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }


                        ProgressBar.dismiss();
                    }
                }else if(load_partsqty.size()==0){

                    Sessiondata.getInstance().setPartsquantity(null);

                    ClearSession();

                    Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                    startActivity(myintent);
                    overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);


                    ProgressBar.dismiss();

                }else {



                    if(sweetalrtsuccess) {
                        sweetalrtsuccess = false;

                        sweetalt=  new SweetAlertDialog(activity_handwrites_notmatch.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Message!")
                                .setContentText("Part has been added successfully!")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        sweetalrtsuccess=true;
                                        ClearSession_aftersubmit();

                                            Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                                            startActivity(myintent);
                                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                                        sDialog.dismiss();

                                    }
                                });
                        sweetalt.setCancelable(false);
                        sweetalt.show();
                    }

                    ProgressBar.dismiss();

                }
            }else{

                Sessiondata.getInstance().setPartsquantity(null);

                ClearSession();

                Intent myintent= new Intent(activity_handwrites_notmatch.this,Physical_counting_activity.class);
                startActivity(myintent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);

                ProgressBar.dismiss();

            }

        }
    }
}