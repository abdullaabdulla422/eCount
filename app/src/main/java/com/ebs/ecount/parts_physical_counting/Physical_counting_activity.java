package com.ebs.ecount.parts_physical_counting;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.ebs.ecount.initial.LoginActivity;
import com.ebs.ecount.objects.GetBinLocation;
import com.ebs.ecount.objects.GetDealerBranch;
import com.ebs.ecount.objects.GetManufacturers;
import com.ebs.ecount.objects.GetPartsQuantity;
import com.ebs.ecount.objects.GetProcessId;
import com.ebs.ecount.objects.GetVarianceList;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.parts_physical_inventory.AttachImageActivity;
import com.ebs.ecount.parts_physical_inventory.equipment_inventory_activity;
import com.ebs.ecount.uidesigns.ProgressBar;
import com.ebs.ecount.uidesigns.SimpleScannerActivity;
import com.ebs.ecount.uidesigns.SweetAlertDialog;
import com.ebs.ecount.utils.Sessiondata;
import com.ebs.ecount.webutils.WebServiceConsumer;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by techunity on 21/11/16.
 */
public class Physical_counting_activity extends Activity implements OnConnectionCompletedListener, ReaderDeviceListener {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private static final DeviceType[] deviceTypeValues = DeviceType.values();
    public static ReaderDevice readerDevice;
    public static boolean isDevicePicked = false;
    public static boolean dialogAppeared = false;
    public static String selectedDevice = "";
    public static boolean fragmentActive = false;
    EditText process_id, starting_bin, ending_bin ,dialog_mfg;
    TextView text_header;
    Button load, submit, back, filter_btn, hand_writes, partnotcounted, variance;
    ListView physica_Counitng_processid;
    RecyclerView physical_conting_list;
    ImageView filter_process, start_bin_search, end_bin_search;
    LoginObject loginObject = null;
    ArrayList<String> mgf;
    Boolean sweetalrt = true;
    Boolean sweetalrterror = true;
    Boolean sweetalrtsuccess = true;
    Boolean Sweetalrt_list = true;
    Boolean Sweetalrt_pre = true;
    Boolean sweet_load = true;
    SweetAlertDialog sweetalt;
    private CustomAdapter adapter_mfg;
    SweetAlertDialog Sweetalt_list, Sweetalt_pre;
    SweetAlertDialog sweet_load_alert;
    Dialog mDialoglist=null;
    int scanner;
    ArrayList<GetPartsQuantity> partqty;
    int Session = 0;
    int processidmfg;
    Typeface header_face, txt_face;
    ArrayList<GetPartsQuantity> countingdata;
    ArrayList<GetVarianceList> variancedata;
    ArrayList<GetProcessId> processid;
    ArrayList<GetProcessId> processidfilter;
    ArrayList<GetPartsQuantity> partsqty;
    ArrayList<GetVarianceList> varianceLists;
    ArrayList<GetPartsQuantity> partsqty_variance;
    ArrayList<GetPartsQuantity> partsqty_filter;
    ArrayList<GetPartsQuantity> partsqtyscroll = new ArrayList<>();
    ArrayList<GetVarianceList> varianceScroll = new ArrayList<>();
    ArrayList<GetProcessId> processscroll = new ArrayList<>();
    ArrayList<GetPartsQuantity> partsqty_hw;
    ArrayList<GetPartsQuantity> partsqty_submit;
    LinearLayout count_label, count_label_new;
    Boolean load_partsnotcounted;
    ArrayList<GetBinLocation> binLocations;
    ArrayList<GetManufacturers> manufacturerses;
    ArrayList<GetProcessId> dummy_processid;
    String Part_No;
    int Processid;
    int Processids;
    int process_list;
    TextView equp_inventory_search;
    LinearLayout filter_layout;
    ImageView filter_arrow;
    String str_bin = "", str_end_bin = "";
    int submit_value = 0;
    Dialog mDialog = null, Dialog = null;
    Dialog mDialogwarning;
    Dialog mDialogmsg;
    Dialog mDialognodata;
    View viewline;
    LinearLayout layout;
    ValueAnimator mAnimator;
    String empty = "";
    AutoCompleteTextView process_no;
    String start_bin, end_bin, partNum;
    String partquantity;
    String search_processid = "";
    String start_bin_asy;
    String end_bin_asy;
    String userToken;
    int startindex = 0;
    int endindex = 20;
    int startindex_listmore = 0;
    int endindex_listmore = 50;
    ListView list;
    ArrayList<String> arrayquantity;
    ArrayList<String> arraynotes;
    ArrayList<String> startbinloc;
    ArrayList<String> endbinloc;
    ArrayList<String> arraypart_no;
    ArrayList<String> mfr;
    ArrayList<String> var_arrayquantity;
    ArrayList<String> var_arraynotes;
    ArrayList<String> var_startbinloc;
    ArrayList<String> var_endbinloc;
    ArrayList<String> var_arraypart_no;
    ArrayList<String> var_mfr;
    ArrayList<String> quanty;
    ArrayList<String> var_quanty;
    ArrayList<Integer> proc_id;
    ArrayList<String> partno_list;
    ArrayList<String> var_partno_list;
    ArrayList<String> mfg_list;
    ArrayList<String> var_mfg_list;
    int load_btn_Value = 0;
    int load_btn_Value_HW = 0;
    int submit_btn_value = 0;
    String branch;
    EditText partnum;
    Boolean partsNotCounted = true;
    String branch_mfg;
    Boolean scroll = true;
    Boolean scroll_processid = true;
    Dialog mDialogscan = null;
    int isDeviceConnected = 1;
    HashMap<String, UsbDevice> usbDevices;
    boolean isLoading = false;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;
    RecyclerView.LayoutManager layoutManager = null;
    String chk_preliminary = "";
    AutoCompleteTextView branch_no;
    TextView txt_br;
    ImageView img_branch;
    String usertoken;
    ArrayList<GetDealerBranch> dealerbranch;
    ArrayList<GetDealerBranch> branchlist;
    String branch_name = "";
    SwipeController swipeController = null;
    String partquantity_match;
    String UnC_start, UnC_end, UnC_part, UnC_qty, UnC_mfg, UnC_notes;
    int UnC_variance;
    Boolean UnC_uncount;
    int UnC_ProcessId;
    boolean side_scroll = false;
    EditText partno;
    int uncountIndex = 0;
    TextView txt_partno;
    ImageView partno_search;
    LinearLayout varianceContainer;
    boolean submit_uncount;
    String submit_notes;
    int submit_variance;
    Boolean sweetbranch = true;
    SweetAlertDialog dialogbranch;
    int find_adapter = 1;
    String partno_submit = "";
    String process_no_filter = "";
    String enter_res_preliminary = "";
    int touch_proc = 0, click_part = 0;
    private TextView mArrow;
    private int count;
    private boolean[] thumbnailsselection;
    private InputMethodManager mInputMethodMgr = null;
    private String ALERT_TITLE = "Alert";
    private String ALERT_FIELDS_FILLED = "You are required to enter all the fields!";
    private Class<?> mClss;
    private boolean mIsStarting;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private Context mContext;
    private int lastFocussedPosition = -1;
    private Handler handler = new Handler();
    private CountingAdapter contactAdapter;
    private VarianceAdapter varianceAdapter;
    private CustomAdapter_Branch adapter_branch;
    private CustomAdapter_processid adapter_processid;
    private Paint p = new Paint();
    private View view;
    private int edit_position;
    private LinearLayoutManager mManager;
    private String validateParts_v1;
    private String part_no;
    private Boolean allow_submit = false;
    private String part_no_bin = "";

    public static DeviceType deviceTypeFromInt(int i) {
        return deviceTypeValues[i];
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts_physical_counting);

        mContext = this;

        header_face = Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");
        txt_face = Typeface.createFromAsset(getAssets(), "fonts/helr45w.ttf");

        starting_bin = (EditText) findViewById(R.id.startBin);
        ending_bin = (EditText) findViewById(R.id.endBin);

        txt_br = (TextView) findViewById(R.id.txt_branch);
        branch_no = (AutoCompleteTextView) findViewById(R.id.branch_no);
        img_branch = (ImageView) findViewById(R.id.branch_list);

        txt_partno = (TextView) findViewById(R.id.txt_part);
        partno = (EditText) findViewById(R.id.partno);
        partno_search = (ImageView) findViewById(R.id.partno_search);

        varianceContainer = (LinearLayout) findViewById(R.id.varianceContainer);
        variance = (Button) findViewById(R.id.variance);

        txt_partno.setTypeface(header_face);
        partno.setTypeface(txt_face);

        txt_br.setTypeface(header_face);
        branch_no.setEnabled(false);
        branch_no.setTypeface(txt_face);

        partsqty_variance = new ArrayList<>();
        partsqty_filter = new ArrayList<>();

        variance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConenction()) {
                    find_adapter = 1;

                    startindex_listmore = 0;
                    endindex_listmore = 50;

                    submit_uncount = false;
                    submit_variance = 1;

                    varianceLists = new ArrayList<>();
                    varianceScroll = new ArrayList<>();

                    Processid = Integer.parseInt(process_no.getText().toString());
                    partNum = partno.getText().toString();
                    start_bin = starting_bin.getText().toString();
                    end_bin = ending_bin.getText().toString();
                    branch_name = Sessiondata.getInstance().getCounting_branchno();

                    new AsyncGetVarianceList().execute();
                } else {
                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        img_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConenction()) {

                    new AsyncGetDealerBranch().execute();

                } else {

                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });

        process_no = (AutoCompleteTextView) findViewById(R.id.process_id);
        load = (Button) findViewById(R.id.physical_count_filter);
        submit = (Button) findViewById(R.id.physical_count_submit);
        hand_writes = (Button) findViewById(R.id.hand_writes);

        back = (Button) findViewById(R.id.physical_count_back);

        partnotcounted = (Button) findViewById(R.id.partnotcounted);

        back.setTypeface(txt_face);
        count_label = (LinearLayout) findViewById(R.id.count_label);
        count_label_new = (LinearLayout) findViewById(R.id.count_label_new);

        filter_arrow = (ImageView) findViewById(R.id.filter_arrow);
        filter_btn = (Button) findViewById(R.id.filter_btn);
        filter_btn.setTypeface(header_face);
        viewline = findViewById(R.id.view);
        layout = (LinearLayout) findViewById(R.id.submitContainer);
        text_header = (TextView) findViewById(R.id.text_header);
        text_header.setTypeface(header_face);
        TextView txt_processid = (TextView) findViewById(R.id.txt_processid);
        TextView txt_startingbin = (TextView) findViewById(R.id.txt_startingbin);
        TextView txt_endingbin = (TextView) findViewById(R.id.txt_endingbin);
        TextView txt_part = (TextView) findViewById(R.id.part_text_demo);
        TextView txt_mfr = (TextView) findViewById(R.id.mfr_text_demo);
        TextView txt_desc = (TextView) findViewById(R.id.desc_text_demo);
        TextView txt_qty = (TextView) findViewById(R.id.qty_text_demo);

        physical_conting_list = (RecyclerView) findViewById(R.id.physica_Counitng_List);
        physica_Counitng_processid = (ListView) findViewById(R.id.physica_Counitng_processid);
        filter_layout = (LinearLayout) findViewById(R.id.filter_layout);

        arrayquantity = new ArrayList<>();
        arraynotes = new ArrayList<>();
        startbinloc = new ArrayList<>();
        endbinloc = new ArrayList<>();
        arraypart_no = new ArrayList<>();
        mfr = new ArrayList<>();

        var_arrayquantity = new ArrayList<>();
        var_arraynotes = new ArrayList<>();
        var_startbinloc = new ArrayList<>();
        var_endbinloc = new ArrayList<>();
        var_arraypart_no = new ArrayList<>();
        var_mfr = new ArrayList<>();

        processid = new ArrayList<>();
        processidfilter = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String startbin = bundle.getString("value1");

            if (Sessiondata.getInstance().getCount_value().equalsIgnoreCase("start")) {
                Sessiondata.getInstance().setCounting_startbin(startbin);
            }
        }

        Bundle bundleendbin = getIntent().getExtras();
        if (bundleendbin != null) {

            String endbin = bundleendbin.getString("value2");
            if (Sessiondata.getInstance().getCount_value().equalsIgnoreCase("end")) {
                Sessiondata.getInstance().setCounting_endbin(endbin);
            }

        }

        Bundle bundlepartno = getIntent().getExtras();
        if (bundlepartno != null) {

            String part = bundlepartno.getString("value4");
            if (Sessiondata.getInstance().getCount_value().equalsIgnoreCase("Part_No")) {
                click_part = 1;
                Sessiondata.getInstance().setCounting_partnew(part);
            }

        }

        Sessiondata.getInstance().getHw_generalimages().clear();
        Sessiondata.getInstance().getHw_attachedFilesData().clear();

        starting_bin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Sessiondata.getInstance().setScanner_counting1(4);
                Sessiondata.getInstance().setScanner_counting2(0);
                Sessiondata.getInstance().setScanner_partno(0);
                Sessiondata.getInstance().setCount_value("start");
                Sessiondata.getInstance().setFlagphy_addpart(1);

                return false;
            }
        });

        ending_bin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Sessiondata.getInstance().setScanner_counting1(0);
                Sessiondata.getInstance().setScanner_counting2(5);
                Sessiondata.getInstance().setScanner_partno(0);
                Sessiondata.getInstance().setCount_value("end");
                Sessiondata.getInstance().setFlagphy_addpart(1);

                return false;
            }
        });

        partno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Sessiondata.getInstance().setScanner_counting1(0);
                Sessiondata.getInstance().setScanner_counting2(0);
                Sessiondata.getInstance().setScanner_partno(1);
                Sessiondata.getInstance().setCount_value("Part_No");
                Sessiondata.getInstance().setFlagphy_addpart(1);

                return false;
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

                String branch_name = branch_no.getText().toString();

                //if (click_part == 1){
                //click_part = 0;
                if (!branch_name.isEmpty()) {

                    process_no_filter = process_no.getText().toString();

                    if (process_no_filter.toString().equalsIgnoreCase("")) {
                            /*if(Sweetalrt_list) {
                                Sweetalrt_list = false;

                                Sweetalt_list=  new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Alert!")
                                        .setContentText("Please Choose/Enter the PID before scan Part")
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
                            }*/
                    } else {
                        String str_part = partno.getText().toString();

//                            if (!str_part.isEmpty()){
//                            if (!str_part.isEmpty()){

                        if (checkInternetConenction()) {

                            find_adapter = 2;

                            //partsNotCounted = false;
                            partsNotCounted = true;
                            Sessiondata.getInstance().setLoaded_partsnotcounted(partsNotCounted);

                            Processid = Integer.parseInt(process_no.getText().toString());
                            partNum = partno.getText().toString();
                            start_bin = starting_bin.getText().toString();
                            end_bin = ending_bin.getText().toString();

                            submit_uncount = false;
                            submit_variance = 0;

                            partsqty = new ArrayList<GetPartsQuantity>();

                            if (load_btn_Value == 0) {

                                load_btn_Value = 1;
                                //new AsyncPhysicalCountingListFilter().execute();
                                new AsyncPhysicalCountingListFilter_Part().execute();
                            }
                        } else {
                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
//                        }
                }
                    /*else {
                        if(Sweetalrt_list) {
                            Sweetalrt_list = false;

                            Sweetalt_list=  new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                    .setTitleText("Alert!")
                                    .setContentText("Please Choose the Branch before scan Part")
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
                    }*/
                //}


            }
        });


        Bundle bundlepart = getIntent().getExtras();
        if (bundlepart != null) {
            String part = bundlepart.getString("value3");

            if (Sessiondata.getInstance().getPart_value().toString().equalsIgnoreCase("part")) {


                if ((Dialog == null) || !Dialog.isShowing()) {
                    Dialog = new Dialog(Physical_counting_activity.this);
                    Dialog.setCanceledOnTouchOutside(false);
                    Dialog.setCancelable(false);
                    Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Dialog.setContentView(R.layout.dialog_handwrites);

                    TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                    txt_header.setTypeface(header_face);
                    TextView dialog_txt_header = (TextView) Dialog.findViewById(R.id.dialog_txt_header);
                    dialog_txt_header.setTypeface(header_face);
                    partnum = (EditText) Dialog.findViewById(R.id.partnum);


                    TextView textView_mgf = (TextView) Dialog.findViewById(R.id.textView_mgf);
                    textView_mgf.setTypeface(header_face);


                    ImageView img_mfg = (ImageView) Dialog.findViewById(R.id.img_mfr);
                    dialog_mfg = (EditText) Dialog.findViewById(R.id.mfr);
                    dialog_mfg.setTypeface(txt_face);


                    img_mfg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(partnum.getText().toString().length()!=0){
                                new AsyncGetmanufaturersList().execute();
                            }else{
                                Toast.makeText(Physical_counting_activity.this,"Part # should not be Empty",Toast.LENGTH_LONG).show();
                            }

                        }
                    });





                    partnum.setTypeface(txt_face);

                    partnum.setText(part);

                    partnum.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            Sessiondata.getInstance().setScanner_partnumber(7);
                            Sessiondata.getInstance().setPart_value("part");
                            return false;
                        }
                    });

                    ImageView scan_part = (ImageView) Dialog.findViewById(R.id.scan_part);

                    TextView dialog_social_submit = (TextView) Dialog.findViewById(R.id.dialog_submit);
                    dialog_social_submit.setTypeface(header_face);

                    TextView dialog_social_cancel = (TextView) Dialog.findViewById(R.id.dialog_social_cancel);
                    dialog_social_cancel.setTypeface(header_face);

                    scan_part.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Sessiondata.getInstance().setScanner_partreceipt(0);
                            Sessiondata.getInstance().setScanner_partreceiving(0);
                            Sessiondata.getInstance().setScanner_replace(0);
                            Sessiondata.getInstance().setScanner_counting1(0);
                            Sessiondata.getInstance().setScanner_counting2(0);
                            Sessiondata.getInstance().setScanner_inventory(0);
                            Sessiondata.getInstance().setScanner_partno(0);

                            Sessiondata.getInstance().setScanner_partnumber(7);
                            Sessiondata.getInstance().setScanner_hwstartbin(0);
                            Sessiondata.getInstance().setScanner_hwendbin(0);

                            Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                            Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                            Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                            Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                            Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                            Sessiondata.getInstance().setCounting_partnum(partnum.getText().toString());

                            Sessiondata.getInstance().setPart_value("part");
                            Sessiondata.getInstance().setCount_value("");

                            Sessiondata.getInstance().setLoad_value("Load");
                            Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                            Sessiondata.getInstance().setLoad_value_onresume("");
                            Sessiondata.getInstance().setHw_value_notmatch("");


                            launchActivity(SimpleScannerActivity.class);
                        }
                    });

                    dialog_social_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (allow_submit)
                            {
                                allow_submit = false;

                                if (partnum.getText().toString().length() == 0) {


                                    if (Sweetalrt_list) {
                                        Sweetalrt_list = false;

                                        Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                .setTitleText("Alert!")
                                                .setContentText("Please enter the Part Number")
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

                                        if (load_btn_Value_HW == 0) {

                                            load_btn_Value_HW = 1;
                                            partno_submit = partnum.getText().toString();

                                            new AsyncPhysicalCountingListSubmit().execute();
                                        }
                                        Dialog.dismiss();

                                    } else {

                                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                    }

                                }
                            }
                            else
                            {
                                Toast.makeText(Physical_counting_activity.this, "Please select the Manufacture", Toast.LENGTH_LONG).show();
                            }


                        }
                    });

                    dialog_social_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog.dismiss();
                        }
                    });

                    Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    Dialog.show();


                }
            }
        }

        txt_part.setTypeface(header_face);
        txt_mfr.setTypeface(header_face);
        txt_desc.setTypeface(header_face);
        txt_qty.setTypeface(header_face);
        txt_startingbin.setTypeface(header_face);
        txt_processid.setTypeface(header_face);
        txt_endingbin.setTypeface(header_face);
        submit.setTypeface(header_face);
        hand_writes.setTypeface(header_face);
        load.setTypeface(header_face);
        partnotcounted.setTypeface(header_face);
        process_no.setTypeface(txt_face);
        starting_bin.setTypeface(txt_face);
        ending_bin.setTypeface(txt_face);

        branch = Sessiondata.getInstance().getLoginObject().getBranch();

        if (branch.toString().length() != 0) {
            for (int i = 0; i < branch.length(); i++) {
                Character character = branch.charAt(i);
                if (character.toString().equals("-")) {
                    branch = branch.substring(0, i);
                    Log.d("branch_trim", "" + branch);
                    break;
                }
            }
        }

        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sessiondata.getInstance().setMfg("");
                Sessiondata.getInstance().setCounting_branchname("");
                Sessiondata.getInstance().setCounting_processid("");
                Sessiondata.getInstance().setCounting_partnum("");
                Sessiondata.getInstance().setCounting_startbin("");
                Sessiondata.getInstance().setCounting_partnew("");
                Sessiondata.getInstance().setCounting_endbin("");
                Sessiondata.getInstance().setCount_value("");
                Sessiondata.getInstance().setLoad_value("");

                Sessiondata.getInstance().setPart_value("");
                Sessiondata.getInstance().setHw_value_notmatch("");

                Sessiondata.getInstance().setProcessid_match("");
                Sessiondata.getInstance().setPart_match("");
                Sessiondata.getInstance().setBranch_mfg("");

                Sessiondata.getInstance().setLoad_branch("");
                Sessiondata.getInstance().setLoad_processid(0);
                Sessiondata.getInstance().setLoad_startbin("");
                Sessiondata.getInstance().setLoad_endbin("");
                Sessiondata.getInstance().setLoad_partNum("");
                Sessiondata.getInstance().setLoad_value_onresume("");
                Sessiondata.getInstance().setLoad_check_enable(0);
                Sessiondata.getInstance().setLoad_checkvar_enable(0);
                Sessiondata.getInstance().setLoaded_partsnotcounted(true);

                Sessiondata.getInstance().setFlagphy_addpart(0);

                Sessiondata.getInstance().setCounting_branchno("");
                Sessiondata.getInstance().setChk_preliminary("");

                if (Sessiondata.getInstance().getPartsquantity() != null) {
                    Sessiondata.getInstance().getPartsquantity().clear();
                }

                Intent intent = new Intent(Physical_counting_activity.this, Dashboard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });


        filter_process = (ImageView) findViewById(R.id.filter_process);
        start_bin_search = (ImageView) findViewById(R.id.startbin_search);
        end_bin_search = (ImageView) findViewById(R.id.endbin_search);

        process_no.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_proc = 1;
                Log.d("OnTouch", "process_no" + touch_proc);

                return false;
            }

        });

        process_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String branch_name = branch_no.getText().toString();

                if (touch_proc == 1) {
                    if (!branch_name.isEmpty()) {

                        process_no_filter = process_no.getText().toString();

                        if (process_no_filter.toString().equalsIgnoreCase("")) {

                        } else {
                            if (checkInternetConenction()) {

                                new AsyncGetProcessIdFilter().execute();

                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        }
                    } else {
                        if (Sweetalrt_list) {
                            Sweetalrt_list = false;

                            Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                    .setTitleText("Alert!")
                                    .setContentText("Please Choose the Branch before enter PID")
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
            }
        });

        partnotcounted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                side_scroll = false;

                isLoading = false;

                partsqtyscroll = new ArrayList<>();
                startindex_listmore = 0;
                endindex_listmore = 50;
                uncountIndex = 0;

                if (process_no.getText().toString().equalsIgnoreCase("")) {

                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
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
                    find_adapter = 2;

                    partsNotCounted = true;
                    Sessiondata.getInstance().setLoaded_partsnotcounted(partsNotCounted);

                    Processid = Integer.parseInt(process_no.getText().toString());
                    partNum = partno.getText().toString();
                    start_bin = starting_bin.getText().toString();
                    end_bin = ending_bin.getText().toString();
                    branch_name = Sessiondata.getInstance().getCounting_branchno();

                    partsqty = new ArrayList<GetPartsQuantity>();

                    submit_variance = 0;

                    if (checkInternetConenction()) {

                        if (load_btn_Value == 0) {

                            load_btn_Value = 1;
                            new AsyncPhysicalCountingList().execute();
                        }

                    } else {
                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        hand_writes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (process_no.getText().toString().equalsIgnoreCase("")) {

                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
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

                    Processid = Integer.parseInt(process_no.getText().toString());
                    partNum = partno.getText().toString();
                    start_bin = starting_bin.getText().toString();
                    end_bin = ending_bin.getText().toString();
                    branch_name = Sessiondata.getInstance().getCounting_branchno();

                    partsqty_hw = new ArrayList<GetPartsQuantity>();
                    partsqty_submit = new ArrayList<>();

                    if (checkInternetConenction()) {

                        if (load_btn_Value_HW == 0) {

                            load_btn_Value_HW = 1;
                            new AsyncPhysicalCountingListHW().execute();

                        }

                    } else {
                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start_bin = starting_bin.getText().toString();
                end_bin = ending_bin.getText().toString();
                partNum = partno.getText().toString();

                if (submit_value == 0) {


                    if (process_no.getText().toString().equalsIgnoreCase("")) {

                        if (sweetalrt) {
                            sweetalrt = false;

                            sweetalt = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
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
                    } else if (partsqty != null) {
                        if (partsqty.size() == 0) {
                            if (sweetalrt) {
                                sweetalrt = false;

                                sweetalt = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Alert!")
                                        .setContentText("Please load the data before submit")
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


                            if (checkInternetConenction()) {

                                if (submit_btn_value == 0) {
                                    submit_btn_value = 1;
                                    new AsyncSetPartsQuantity().execute();
                                }


                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        }
                    } else {
                        if (sweetalrt) {
                            sweetalrt = false;

                            sweetalt = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                    .setTitleText("Alert!")
                                    .setContentText("Please load the data before submit")
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

                    if (process_no.getText().toString().equalsIgnoreCase("")) {


                        if (sweetalrt) {
                            sweetalrt = false;

                            sweetalt = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
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
                        Processids = Integer.parseInt(process_no.getText().toString());

                        if (checkInternetConenction()) {

                            if (submit_btn_value == 0) {
                                submit_btn_value = 1;
                                new AsyncSetPartsQuantity().execute();
                            }


                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }
                }
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

        filter_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String branch_name = branch_no.getText().toString();

                if (!branch_name.isEmpty()) {
                    if (checkInternetConenction()) {

                        new AsyncGetProcessId().execute();

                    } else {

                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                    }
                } else {
                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
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

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                side_scroll = true;
                uncountIndex = 0;
                isLoading = false;

                partsqtyscroll = new ArrayList<>();
                startindex_listmore = 0;
                endindex_listmore = 50;

                if (process_no.getText().toString().equalsIgnoreCase("")) {

                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
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
                    if (processid != null) {

                        if (enter_res_preliminary.isEmpty()) {
                            enter_res_preliminary = Sessiondata.getInstance().getChk_preliminary();
                        }
                        if (enter_res_preliminary.toString().contains("P")) {
                            if (Sweetalrt_pre) {
                                Sweetalrt_pre = false;

                                Sweetalt_pre = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Alert!")
                                        .setContentText("Preliminary count")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                Sweetalrt_pre = true;

                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }

                                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                                    Sessiondata.getInstance().getVarianceLists().clear();
                                                }

                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                countingdata = new ArrayList<GetPartsQuantity>();
                                                isLoading = false;
                                                loadDatawithoutscroll(countingdata);

                                                count_label.setVisibility(View.GONE);
                                                count_label_new.setVisibility(View.GONE);
                                                varianceContainer.setVisibility(View.GONE);

                                                sDialog.dismiss();
                                            }
                                        });
                                Sweetalt_pre.setCancelable(false);
                                Sweetalt_pre.show();
                            }
                        } else if (enter_res_preliminary.toString().contains("F") || enter_res_preliminary.toString().contains("")) {
                            find_adapter = 0;

                            partsNotCounted = false;
                            Sessiondata.getInstance().setLoaded_partsnotcounted(partsNotCounted);

                            Processid = Integer.parseInt(process_no.getText().toString());
                            partNum = partno.getText().toString();
                            start_bin = starting_bin.getText().toString();
                            end_bin = ending_bin.getText().toString();
                            branch_name = Sessiondata.getInstance().getCounting_branchno();

                            submit_uncount = false;
                            submit_variance = 0;

                            partsqty = new ArrayList<GetPartsQuantity>();

                            if (checkInternetConenction()) {

                                if (load_btn_Value == 0) {

                                    load_btn_Value = 1;
                                    new AsyncPhysicalCountingList().execute();
                                }

                            } else {
                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        find_adapter = 0;

                        partsNotCounted = false;
                        Sessiondata.getInstance().setLoaded_partsnotcounted(partsNotCounted);

                        Processid = Integer.parseInt(process_no.getText().toString());
                        partNum = partno.getText().toString();
                        start_bin = starting_bin.getText().toString();
                        end_bin = ending_bin.getText().toString();
                        branch_name = Sessiondata.getInstance().getCounting_branchno();


                        submit_uncount = false;
                        submit_variance = 0;

                        partsqty = new ArrayList<GetPartsQuantity>();

                        if (checkInternetConenction()) {

                            if (load_btn_Value == 0) {

                                load_btn_Value = 1;
                                new AsyncPhysicalCountingList().execute();
                            }

                        } else {
                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        start_bin_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sessiondata.getInstance().setScanner_partreceipt(0);
                Sessiondata.getInstance().setScanner_partreceiving(0);
                Sessiondata.getInstance().setScanner_replace(0);
                Sessiondata.getInstance().setScanner_counting1(4);
                Sessiondata.getInstance().setScanner_counting2(0);
                Sessiondata.getInstance().setScanner_inventory(0);
                Sessiondata.getInstance().setScanner_partno(0);

                Sessiondata.getInstance().setScanner_partnumber(0);
                Sessiondata.getInstance().setScanner_hwstartbin(0);
                Sessiondata.getInstance().setScanner_hwendbin(0);

                Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                Sessiondata.getInstance().setCount_value("start");

                Sessiondata.getInstance().setLoad_value("Load");
                if (process_no.getText().toString().length() != 0) {
                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                }
                Sessiondata.getInstance().setLoad_value_onresume("");
                Sessiondata.getInstance().setPart_value("");
                Sessiondata.getInstance().setHw_value_notmatch("");

                launchActivity(SimpleScannerActivity.class);

                scanner = 1;
            }
        });

        partno_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sessiondata.getInstance().setScanner_partreceipt(0);
                Sessiondata.getInstance().setScanner_partreceiving(0);
                Sessiondata.getInstance().setScanner_replace(0);
                Sessiondata.getInstance().setScanner_counting1(0);
                Sessiondata.getInstance().setScanner_counting2(0);
                Sessiondata.getInstance().setScanner_inventory(0);
                Sessiondata.getInstance().setScanner_partno(1);

                Sessiondata.getInstance().setScanner_partnumber(0);
                Sessiondata.getInstance().setScanner_hwstartbin(0);
                Sessiondata.getInstance().setScanner_hwendbin(0);

                Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                Sessiondata.getInstance().setCount_value("Part_No");

                Sessiondata.getInstance().setLoad_value("Load");
                if (process_no.getText().toString().length() != 0) {
                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                }
                Sessiondata.getInstance().setLoad_value_onresume("");
                Sessiondata.getInstance().setPart_value("");
                Sessiondata.getInstance().setHw_value_notmatch("");

                launchActivity(SimpleScannerActivity.class);

                scanner = 2;
            }
        });

        end_bin_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sessiondata.getInstance().setScanner_partreceipt(0);
                Sessiondata.getInstance().setScanner_partreceiving(0);
                Sessiondata.getInstance().setScanner_replace(0);
                Sessiondata.getInstance().setScanner_counting1(0);
                Sessiondata.getInstance().setScanner_counting2(5);
                Sessiondata.getInstance().setScanner_inventory(0);
                Sessiondata.getInstance().setScanner_partno(0);

                Sessiondata.getInstance().setScanner_partnumber(0);
                Sessiondata.getInstance().setScanner_hwstartbin(0);
                Sessiondata.getInstance().setScanner_hwendbin(0);

                Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                Sessiondata.getInstance().setCount_value("end");

                Sessiondata.getInstance().setLoad_value("Load");
                if (process_no.getText().toString().length() != 0) {
                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                }
                Sessiondata.getInstance().setLoad_value_onresume("");
                Sessiondata.getInstance().setPart_value("");
                Sessiondata.getInstance().setHw_value_notmatch("");

                launchActivity(SimpleScannerActivity.class);

                scanner = 2;
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sessiondata.getInstance().setMfg("");
                Sessiondata.getInstance().setCounting_branchname("");
                Sessiondata.getInstance().setCounting_processid("");
                Sessiondata.getInstance().setCounting_partnum("");
                Sessiondata.getInstance().setCounting_startbin("");
                Sessiondata.getInstance().setCounting_endbin("");
                Sessiondata.getInstance().setCounting_partnew("");
                Sessiondata.getInstance().setCount_value("");
                Sessiondata.getInstance().setLoad_value("");

                Sessiondata.getInstance().setPart_value("");
                Sessiondata.getInstance().setHw_value_notmatch("");

                //new

                Sessiondata.getInstance().setProcessid_match("");
                Sessiondata.getInstance().setPart_match("");
                Sessiondata.getInstance().setBranch_mfg("");

                Sessiondata.getInstance().setLoad_branch("");
                Sessiondata.getInstance().setLoad_processid(0);
                Sessiondata.getInstance().setLoad_startbin("");
                Sessiondata.getInstance().setLoad_endbin("");
                Sessiondata.getInstance().setLoad_partNum("");
                Sessiondata.getInstance().setLoad_value_onresume("");
                Sessiondata.getInstance().setLoad_check_enable(0);
                Sessiondata.getInstance().setLoad_checkvar_enable(0);
                Sessiondata.getInstance().setLoaded_partsnotcounted(true);

                Sessiondata.getInstance().setFlagphy_addpart(0);

                Sessiondata.getInstance().setCounting_branchno("");
                Sessiondata.getInstance().setChk_preliminary("");

                if (Sessiondata.getInstance().getPartsquantity() != null) {
                    Sessiondata.getInstance().getPartsquantity().clear();
                }

                Intent intent = new Intent(Physical_counting_activity.this, Dashboard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });

        countingdata = new ArrayList<GetPartsQuantity>();
        variancedata = new ArrayList<>();

        physical_conting_list.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new CountingAdapter(countingdata, Physical_counting_activity.this);
        physical_conting_list.setAdapter(contactAdapter);
        physical_conting_list.smoothScrollToPosition(physical_conting_list.getAdapter().getItemCount());

        physical_conting_list.setLayoutManager(new LinearLayoutManager(this));
        varianceAdapter = new VarianceAdapter(variancedata, Physical_counting_activity.this);
        physical_conting_list.setAdapter(varianceAdapter);
        physical_conting_list.getLayoutManager().smoothScrollToPosition(physical_conting_list, new RecyclerView.State(), physical_conting_list.getAdapter().getItemCount());


        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {

                if (partsqtyscroll != null) {

                    if (partsqtyscroll.size() != 0) {
                        if (checkInternetConenction()) {
//                            if(partsqtyscroll.get(position).getPartno() != null) {
                            UnC_start = partsqtyscroll.get(position).getPrimaryBinLocation();
                            UnC_end = partsqtyscroll.get(position).getSecondaryBinLocation();
                            UnC_part = partsqtyscroll.get(position).getPartno();
                            UnC_qty = partsqtyscroll.get(position).getLoadQty();
                            UnC_mfg = partsqtyscroll.get(position).getMfr();
                            UnC_notes = partsqtyscroll.get(position).getNotes();
                            UnC_ProcessId = Integer.parseInt(process_no.getText().toString());
                            UnC_uncount = true;
                            UnC_variance = 0;
                            uncountIndex = position - 1;

                            Log.d("UnC_start", "" + UnC_start);
                            Log.d("UnC_end", "" + UnC_end);
                            Log.d("UnC_part", "" + UnC_part);
                            Log.d("UnC_qty", "" + UnC_qty);
                            Log.d("UnC_mfg", "" + UnC_mfg);
                            Log.d("UnC_notes", "" + UnC_notes);
                            Log.d("UnC_ProcessId", "" + UnC_ProcessId);
                            Log.d("UnC_uncount", "" + UnC_uncount);

                            new AsyncSetPartsQuantityV3().execute();
//                            }
                        } else {
                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }

            /*public void onLeftClicked(int position){
                //Toast.makeText(Physical_counting_activity.this,"Edit",Toast.LENGTH_LONG).show();
            }*/

        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(physical_conting_list);

        physical_conting_list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                if (find_adapter == 0) {
                    if (side_scroll == true) {
                        swipeController.onDraw(c);
                    }
                }
            }
        });


        physical_conting_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scrolling up

                    if (find_adapter == 0) {
                        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) physical_conting_list.getLayoutManager();
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        Log.d("totalItemCount", "" + totalItemCount);
                        Log.d("lastVisibleItem", "" + lastVisibleItem);
                        Log.d("visibleThreshold", "" + visibleThreshold);

                        int count = 0;
                        if (contactAdapter != null) {
                            count = contactAdapter.getItemCount();
                        }

                        int counts;
                        if (Sessiondata.getInstance().getPartsquantity() != null) {
                            Log.d("Array Size1111", "" + Sessiondata.getInstance().getPartsquantity().size());
                            counts = Sessiondata.getInstance().getPartsquantity().get(0).getRowcnt();
                        } else {
                            counts = count;
                        }

                        Log.d("curr_count_list", "" + count);
                        Log.d("tot_count_list", "" + counts);

                        if (!isLoading && totalItemCount <= (lastVisibleItem + 3)) {
                            if (count <= counts - 1) {

                                isLoading = true;

                                startindex_listmore = endindex_listmore + 1;
                                endindex_listmore = endindex_listmore + 50;

                                if (checkInternetConenction()) {
                                    if (submit_btn_value == 0) {
                                        submit_btn_value = 1;
                                        new AsyncPhysicalCountingListScrollNew().execute();
                                    }
                                } else {
                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } else if (find_adapter == 2) {
                        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) physical_conting_list.getLayoutManager();
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        Log.d("totalItemCount", "" + totalItemCount);
                        Log.d("lastVisibleItem", "" + lastVisibleItem);
                        Log.d("visibleThreshold", "" + visibleThreshold);

                        int count = 0;
                        if (contactAdapter != null) {
                            count = contactAdapter.getItemCount();
                        }

                        int counts;
                        if (Sessiondata.getInstance().getPartsquantity() != null) {
                            Log.d("Array Size1111", "" + Sessiondata.getInstance().getPartsquantity().size());
                            counts = Sessiondata.getInstance().getPartsquantity().get(0).getRowcnt();
                        } else {
                            counts = count;
                        }

                        Log.d("curr_count_list", "" + count);
                        Log.d("tot_count_list", "" + counts);

                        if (!isLoading && totalItemCount <= (lastVisibleItem + 3)) {
                            if (count <= counts - 1) {

                                isLoading = true;

                                startindex_listmore = endindex_listmore + 1;
                                endindex_listmore = endindex_listmore + 50;

                                if (checkInternetConenction()) {
                                    if (submit_btn_value == 0) {
                                        submit_btn_value = 1;
                                        new AsyncPhysicalCountingListScrollNew().execute();
                                    }
                                } else {
                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } else {
                        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) physical_conting_list.getLayoutManager();
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        Log.d("totalItemCount", "" + totalItemCount);
                        Log.d("lastVisibleItem", "" + lastVisibleItem);
                        Log.d("visibleThreshold", "" + visibleThreshold);

                        int count = 0;
                        if (varianceAdapter != null) {
                            count = varianceAdapter.getItemCount();
                        }

                        int counts;
                        if (Sessiondata.getInstance().getVarianceLists() != null) {
                            Log.d("Array Size1111", "" + Sessiondata.getInstance().getVarianceLists().size());
                            counts = Sessiondata.getInstance().getVarianceLists().get(0).getRowcnt();
                        } else {
                            counts = count;
                        }

                        Log.d("curr_count_list", "" + count);
                        Log.d("tot_count_list", "" + counts);

                        if (!isLoading && totalItemCount <= (lastVisibleItem + 3)) {
                            if (count <= counts - 1) {

                                isLoading = true;

                                startindex_listmore = endindex_listmore + 1;
                                endindex_listmore = endindex_listmore + 50;

                                if (checkInternetConenction()) {
                                    if (submit_btn_value == 0) {
                                        submit_btn_value = 1;
                                        new AsyncGetVarianceListScroll().execute();
                                    }
                                } else {
                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }

                } else {
                    // Scrolling down
                }
            }
        });
    }

    // load initial data
    private void loadData(final ArrayList<GetPartsQuantity> countingdata) {
        isLoading = false;
        countingdata.add(null);
        contactAdapter.notifyItemInserted(countingdata.size() - 1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                countingdata.remove(countingdata.size() - 1);
                contactAdapter.notifyItemRemoved(countingdata.size());

                physical_conting_list.setLayoutManager(new LinearLayoutManager(Physical_counting_activity.this));
                contactAdapter = new CountingAdapter(countingdata, Physical_counting_activity.this);
                physical_conting_list.setAdapter(contactAdapter);
                physical_conting_list.scrollToPosition(startindex_listmore - 1);

                contactAdapter.notifyDataSetChanged();

            }
        }, 500);
    }

    private void loadDatawithoutscroll(ArrayList<GetPartsQuantity> countingdata) {
        isLoading = false;
        physical_conting_list.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new CountingAdapter(countingdata, Physical_counting_activity.this);
        physical_conting_list.setAdapter(contactAdapter);
        physical_conting_list.scrollToPosition(uncountIndex);
        contactAdapter.notifyDataSetChanged();
    }

    private void loadDatawithoutscroll_variance(final ArrayList<GetVarianceList> variancedata) {

        variancedata.add(null);
        varianceAdapter.notifyItemInserted(variancedata.size() - 1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                variancedata.remove(variancedata.size() - 1);
                varianceAdapter.notifyItemRemoved(variancedata.size());

                physical_conting_list.setLayoutManager(new LinearLayoutManager(Physical_counting_activity.this));
                varianceAdapter = new VarianceAdapter(variancedata, Physical_counting_activity.this);
                physical_conting_list.setAdapter(varianceAdapter);
                physical_conting_list.scrollToPosition(startindex_listmore - 1);

                varianceAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 500);
    }

    private void loadData_variance(ArrayList<GetVarianceList> variancedata) {

        physical_conting_list.setLayoutManager(new LinearLayoutManager(this));
        varianceAdapter = new VarianceAdapter(variancedata, Physical_counting_activity.this);
        physical_conting_list.setAdapter(varianceAdapter);
        physical_conting_list.scrollToPosition(startindex_listmore - 1);
        contactAdapter.notifyDataSetChanged();
        isLoading = false;
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

    private void expand() {
        filter_layout.setVisibility(View.VISIBLE);
        mAnimator.start();
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

    @Override
    protected void onPause() {
        //Screen turn off
        super.onPause();

        Sessiondata.getInstance().setLoad_value_onresume("Reload_Onresume");

        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

        startindex_listmore = 0;
        endindex_listmore = 50;
        isLoading = false;

        if (readerDevice != null && readerDevice.getConnectionState() == ConnectionState.Connected) {
            readerDevice.disconnect();
        }

    }

    @Override
    protected void onUserLeaveHint() {
        //Application minimize
        Log.d("onUserLeaveHint", "Home button pressed");
        super.onUserLeaveHint();
        Sessiondata.getInstance().setLoad_value_onresume("Reload_Onresume");

        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

        startindex_listmore = 0;
        endindex_listmore = 50;
        isLoading = false;

    }

    @Override
    protected void onResume() {
        //Application Resume
        super.onResume();

        Sweetalrt_list = true;
        sweet_load = true;
        isLoading = false;

        Log.d("process_no_resume", "" + Sessiondata.getInstance().getCounting_processid());
        Log.d("starting_bin_resume", "" + Sessiondata.getInstance().getCounting_startbin());
        Log.d("ending_bin_resume", "" + Sessiondata.getInstance().getCounting_endbin());
        Log.d("branchno_resume", "" + Sessiondata.getInstance().getCounting_branchname());
        Log.d("partno_resume", "" + Sessiondata.getInstance().getCounting_partnew());

        branch_no.setText(Sessiondata.getInstance().getCounting_branchname());
        process_no.setText(Sessiondata.getInstance().getCounting_processid());
        starting_bin.setText(Sessiondata.getInstance().getCounting_startbin());
        ending_bin.setText(Sessiondata.getInstance().getCounting_endbin());
        if (Sessiondata.getInstance().getCounting_partnew().length() != 0) {
            partno.setText(Sessiondata.getInstance().getCounting_partnew());
        }


        partsqtyscroll = new ArrayList<>();
        partsqty = new ArrayList<GetPartsQuantity>();

        countingdata = new ArrayList<GetPartsQuantity>();
        variancedata = new ArrayList<>();

        physical_conting_list.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new CountingAdapter(countingdata, Physical_counting_activity.this);
        physical_conting_list.setAdapter(contactAdapter);
        physical_conting_list.smoothScrollToPosition(physical_conting_list.getAdapter().getItemCount());

        physical_conting_list.setLayoutManager(new LinearLayoutManager(this));
        varianceAdapter = new VarianceAdapter(variancedata, Physical_counting_activity.this);
        physical_conting_list.setAdapter(varianceAdapter);
        physical_conting_list.getLayoutManager().smoothScrollToPosition(physical_conting_list, new RecyclerView.State(), physical_conting_list.getAdapter().getItemCount());

        Bundle bundleonresuem = getIntent().getExtras();

        if (Sessiondata.getInstance().getLoad_check_enable() == 1) {
            if (bundleonresuem != null) {
                String list = bundleonresuem.getString("LoadList");
                if (Sessiondata.getInstance().getLoad_value().toString().contains("Load")) {
                    if (list.toString().contains("Onresume")) {

                        if (checkInternetConenction()) {

                            if (load_btn_Value == 0) {

                                load_btn_Value = 1;
                                startindex_listmore = 0;
                                endindex_listmore = 50;

                                new AsyncPhysicalCountingListOnResume().execute();
                            }

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            } else if (Sessiondata.getInstance().getLoad_value_onresume().toString().contains("Reload_Onresume")) {
                if (checkInternetConenction()) {

                    if (load_btn_Value == 0) {

                        load_btn_Value = 1;
                        startindex_listmore = 0;
                        endindex_listmore = 50;

                        new AsyncPhysicalCountingListOnResume().execute();
                    }

                } else {

                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                }
            } else {
                count_label_new.setVisibility(View.GONE);
                count_label.setVisibility(View.GONE);

                countingdata = new ArrayList<GetPartsQuantity>();

                loadDatawithoutscroll(countingdata);
            }
        } else if (Sessiondata.getInstance().getLoad_checkvar_enable() == 1) {
            if (checkInternetConenction()) {

                startindex_listmore = 0;
                endindex_listmore = 50;

                find_adapter = 1;

                varianceContainer.setVisibility(View.VISIBLE);

                new AsyncGetVarianceListOnResume().execute();

            } else {

                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

            }
        } else {
            count_label_new.setVisibility(View.GONE);
            count_label.setVisibility(View.GONE);

            countingdata = new ArrayList<GetPartsQuantity>();

            loadDatawithoutscroll(countingdata);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        initDevice();
    }

    private void initDevice() {

        Physical_counting_activity.readerDevice = ReaderDevice.getMXDevice(mContext);
        readerDevice.startAvailabilityListening();
        selectedDevice = "MX Scanner";
        Physical_counting_activity.readerDevice.setReaderDeviceListener(this);
        Physical_counting_activity.readerDevice.enableImage(true);
        Physical_counting_activity.readerDevice.connect(Physical_counting_activity.this);
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

                if (Sessiondata.getInstance().getScanner_counting1() == 4) {
                    Sessiondata.getInstance().setCounting_startbin(result);

                } else if (Sessiondata.getInstance().getScanner_counting2() == 5) {
                    Sessiondata.getInstance().setCounting_endbin(result);

                } else if (Sessiondata.getInstance().getScanner_partnumber() == 7) {
                    partnum.setText(result);

                } else if (Sessiondata.getInstance().getScanner_partno() == 1) {
                    Sessiondata.getInstance().setCounting_partnew(result);
                }


                starting_bin.setText(Sessiondata.getInstance().getCounting_startbin());
                ending_bin.setText(Sessiondata.getInstance().getCounting_endbin());
                partno.setText(Sessiondata.getInstance().getCounting_partnew());

            }
        }, 500);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(starting_bin.getWindowToken(), 0);
    }

    @Override
    public void onAvailabilityChanged(ReaderDevice reader) {
        if (reader.getAvailability() == ReaderDevice.Availability.AVAILABLE) {
            Physical_counting_activity.readerDevice.connect(Physical_counting_activity.this);
        } else {
            // DISCONNECTED USB DEVICE
            Physical_counting_activity.readerDevice.connect(Physical_counting_activity.this);
            Physical_counting_activity.readerDevice.disconnect();
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
        Physical_counting_activity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.C128, true, null);
        Physical_counting_activity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.DATAMATRIX, true, null);
        Physical_counting_activity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.UPC_EAN, true, null);
        Physical_counting_activity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.QR, true, null);

        //example sendCommand
        Physical_counting_activity.readerDevice.getDataManSystem().sendCommand("SET SYMBOL.MICROPDF417 ON");
        Physical_counting_activity.readerDevice.getDataManSystem().sendCommand("SET IMAGE.SIZE 0");

    }

    private boolean checkInternetConenction() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Sessiondata.getInstance().setCounting_branchname("");
        Sessiondata.getInstance().setCounting_processid("");
        Sessiondata.getInstance().setCounting_partnum("");
        Sessiondata.getInstance().setCounting_startbin("");
        Sessiondata.getInstance().setCounting_partnew("");
        Sessiondata.getInstance().setCounting_endbin("");
        Sessiondata.getInstance().setCount_value("");
        Sessiondata.getInstance().setLoad_value("");

        Sessiondata.getInstance().setCounting_partnum("");

        Sessiondata.getInstance().setPart_value("");
        Sessiondata.getInstance().setHw_value_notmatch("");

        Sessiondata.getInstance().setProcessid_match("");
        Sessiondata.getInstance().setPart_match("");
        Sessiondata.getInstance().setBranch_mfg("");


        Sessiondata.getInstance().setLoad_branch("");
        Sessiondata.getInstance().setLoad_processid(0);
        Sessiondata.getInstance().setLoad_startbin("");
        Sessiondata.getInstance().setLoad_endbin("");
        Sessiondata.getInstance().setLoad_partNum("");
        Sessiondata.getInstance().setLoad_value_onresume("");
        Sessiondata.getInstance().setLoad_check_enable(0);
        Sessiondata.getInstance().setLoad_checkvar_enable(0);
        Sessiondata.getInstance().setLoaded_partsnotcounted(true);

        Sessiondata.getInstance().setFlagphy_addpart(0);

        Sessiondata.getInstance().setCounting_branchno("");
        Sessiondata.getInstance().setChk_preliminary("");

        if (Sessiondata.getInstance().getPartsquantity() != null) {
            Sessiondata.getInstance().getPartsquantity().clear();
        }

        Intent intent = new Intent(Physical_counting_activity.this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
    }

    public void ClearSession() {

        partsqty.clear();
        starting_bin.setText("");
        ending_bin.setText("");
        partno.setText("");

        submit_value = 0;

        branch_no.setText("");
        process_no.setText("");
        Sessiondata.getInstance().setCount_value("");
        Sessiondata.getInstance().setLoad_value("");
        Sessiondata.getInstance().setCounting_branchname("");
        Sessiondata.getInstance().setCounting_processid("");
        Sessiondata.getInstance().setCounting_partnum("");
        Sessiondata.getInstance().setCounting_startbin("");
        Sessiondata.getInstance().setCounting_partnew("");
        Sessiondata.getInstance().setCounting_endbin("");

        Sessiondata.getInstance().setPart_value("");
        Sessiondata.getInstance().setHw_value_notmatch("");

        //new

        Sessiondata.getInstance().setProcessid_match("");
        Sessiondata.getInstance().setPart_match("");
        Sessiondata.getInstance().setBranch_mfg("");

        count_label.setVisibility(View.GONE);
        count_label_new.setVisibility(View.GONE);

        loadDatawithoutscroll(countingdata);

    }


    public static enum DeviceType {MX_1000}

    public class AsyncSetPartsQuantity extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Processids = Integer.parseInt(process_no.getText().toString());

                start_bin_asy = starting_bin.getText().toString();
                end_bin_asy = ending_bin.getText().toString();

                arrayquantity.clear();
                arraynotes.clear();
                startbinloc.clear();
                endbinloc.clear();
                arraypart_no.clear();
                mfr.clear();
                Log.d("processid_UserToken", "" + userToken);

                String part_no, quantity, mf, startBin, endBin, Notes;

                Log.d("size", "" + partsqty.size());

                for (int i = 0; i < partsqty.size(); i++) {
                    Log.d("part", "" + partsqty.get(i).getPartno());
                    Log.d("process_id", "" + partsqty.get(i).getLoadQty());
                    Log.d("mfg", "" + partsqty.get(i).getMfr());

                    String qtys = partsqty.get(i).getLoadQty();
                    String start = partsqty.get(i).getPrimaryBinLocation();
                    String end = partsqty.get(i).getSecondaryBinLocation();
                    String part_nos = partsqty.get(i).getPartno();
                    String mfg = partsqty.get(i).getMfr();
                    String old_qty = quanty.get(i);
                    String notes = partsqty.get(i).getNotes();

                    Log.d("old_qty:", "" + old_qty + " partsqty:" + qtys);

                    if (!old_qty.toString().equalsIgnoreCase(qtys)) {

                        Log.d("1old_qty", "" + old_qty.toString());
                        Log.d("1new_qty", "" + qtys.toString());
                        Log.d("1new_parts", "" + part_nos.toString());
                        Log.d("1new_mfr", "" + mfr.toString());

                        arrayquantity.add(qtys);
                        arraynotes.add(notes);
                        startbinloc.add(start);
                        endbinloc.add(end);
                        arraypart_no.add(part_nos);
                        mfr.add(mfg);

                        Log.d("array_qty", "" + arrayquantity);
                        Log.d("array_partno", "" + arraypart_no);
                        Log.d("startbinloc", "" + startbinloc);
                        Log.d("endbinloc", "" + endbinloc);
                        Log.d("array_mfg", "" + mfr);
                        Log.d("arraynotes", "" + arraynotes);

                    }
                }
                Log.d("final_size", "" + arrayquantity.size());

                for (int j = 0; j < arrayquantity.size(); j++) {

                    part_no = arraypart_no.get(j);
                    quantity = arrayquantity.get(j);
                    startBin = startbinloc.get(j);
                    endBin = endbinloc.get(j);
                    Notes = arraynotes.get(j);


                    mf = mfr.get(j);

                    Log.d("submit_processid", "" + Processids);
                    Log.d("submit_startbin", "" + startBin);
                    Log.d("submit_endbin", "" + endBin);
                    Log.d("submit_partno", "" + part_no);
                    Log.d("submit_qty", "" + quantity);
                    Log.d("submit_mgf", "" + mf);
                    Log.d("submit_Notes", "" + Notes);
                    Log.d("submit_variance", "" + submit_variance);

                    partquantity = WebServiceConsumer.getInstance().SetPartsQuantityV4(userToken, Processids, startBin, endBin, part_no, quantity, mf, submit_uncount, Notes, submit_variance);
                }
            } catch (SocketTimeoutException e) {
                partquantity = null;
            } catch (Exception e) {
                partquantity = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            submit_btn_value = 0;

            if (partquantity != null) {

                if (partquantity.toString().contains("Session")) {

                    String Result = partquantity;
                    String replace = Result.replace("Error - ", "");
                    Session = 3;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (arrayquantity.size() == 0) {

                    } else if (partquantity.toString().contains("Conversion")) {
                        if (sweetalrterror) {
                            sweetalrterror = false;

                            sweetalt = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText(partquantity + "!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(null)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrterror = true;
                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    } else {

                    }
                }
            } else {

            }
        }
    }

    public class AsyncSetPartsQuantityVariance extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Processids = Integer.parseInt(process_no.getText().toString());

                start_bin_asy = starting_bin.getText().toString();
                end_bin_asy = ending_bin.getText().toString();

                var_arrayquantity.clear();
                var_arraynotes.clear();
                var_startbinloc.clear();
                var_endbinloc.clear();
                var_arraypart_no.clear();
                var_mfr.clear();
                Log.d("processid_UserToken", "" + userToken);

                String part_no, quantity, mf, startBin, endBin, Notes;


                for (int i = 0; i < varianceLists.size(); i++) {
                    Log.d("part", "" + varianceLists.get(i).getPartno());
                    Log.d("process_id", "" + varianceLists.get(i).getLoadQty());
                    Log.d("mfg", "" + varianceLists.get(i).getMfr());

                    String qtys = varianceLists.get(i).getLoadQty();
                    String start = varianceLists.get(i).getPrimaryBinLocation();
                    String end = varianceLists.get(i).getSecondaryBinLocation();
                    String part_nos = varianceLists.get(i).getPartno();
                    String mfg = varianceLists.get(i).getMfr();
                    String old_qty = var_quanty.get(i);
                    String notes = varianceLists.get(i).getNotes();

                    Log.d("old_qty:", "" + old_qty + " partsqty:" + qtys);

                    if (!old_qty.toString().equalsIgnoreCase(qtys)) {

                        Log.d("1old_qty", "" + old_qty.toString());
                        Log.d("1new_qty", "" + qtys.toString());
                        Log.d("1new_parts", "" + part_nos.toString());
                        Log.d("1new_mfr", "" + mfr.toString());

                        var_arrayquantity.add(qtys);
                        var_arraynotes.add(notes);
                        var_startbinloc.add(start);
                        var_endbinloc.add(end);
                        var_arraypart_no.add(part_nos);
                        var_mfr.add(mfg);

                        Log.d("array_qty", "" + var_arrayquantity);
                        Log.d("array_partno", "" + var_arraypart_no);
                        Log.d("startbinloc", "" + var_startbinloc);
                        Log.d("endbinloc", "" + var_endbinloc);
                        Log.d("array_mfg", "" + var_mfr);
                        Log.d("arraynotes", "" + var_arraynotes);

                    }
                }
                Log.d("final_size", "" + var_arrayquantity.size());

                for (int j = 0; j < var_arrayquantity.size(); j++) {

                    part_no = var_arraypart_no.get(j);
                    quantity = var_arrayquantity.get(j);
                    startBin = var_startbinloc.get(j);
                    endBin = var_endbinloc.get(j);
                    Notes = var_arraynotes.get(j);


                    mf = var_mfr.get(j);

                    Log.d("submit_processid", "" + Processids);
                    Log.d("submit_startbin", "" + startBin);
                    Log.d("submit_endbin", "" + endBin);
                    Log.d("submit_partno", "" + part_no);
                    Log.d("submit_qty", "" + quantity);
                    Log.d("submit_mgf", "" + mf);
                    Log.d("submit_Notes", "" + Notes);
                    Log.d("submit_variance", "" + submit_variance);

                    partquantity = WebServiceConsumer.getInstance().SetPartsQuantityV4(userToken, Processids, startBin, endBin, part_no, quantity, mf, submit_uncount, Notes, submit_variance);
                }
            } catch (SocketTimeoutException e) {
                partquantity = null;
            } catch (Exception e) {
                partquantity = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            submit_btn_value = 0;

            if (partquantity != null) {

                if (partquantity.toString().contains("Session")) {

                    String Result = partquantity;
                    String replace = Result.replace("Error - ", "");
                    Session = 14;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (arrayquantity.size() == 0) {

                    } else if (partquantity.toString().contains("Conversion")) {
                        if (sweetalrterror) {

                            sweetalrterror = false;

                            sweetalt = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText(partquantity + "!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(null)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrterror = true;
                                            sDialog.dismiss();
                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }
                    } else {

                    }
                }
            } else {

            }
        }
    }

    public class AsyncSetPartsQuantityV3 extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                Log.d("UnC_uncount", "" + UnC_uncount);
                Log.d("UnC_variance", "" + UnC_variance);
                Log.d("UnC_notes", "" + UnC_notes);

                partquantity_match = WebServiceConsumer.getInstance().SetPartsQuantityV4(userToken, UnC_ProcessId, UnC_start, UnC_end, UnC_part, UnC_qty, UnC_mfg, UnC_uncount, UnC_notes, UnC_variance);

            } catch (java.net.SocketTimeoutException e) {
                partquantity_match = null;
            } catch (Exception e) {
                partquantity_match = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ProgressBar.dismiss();

            if (partquantity_match != null) {

                if (partquantity_match.toString().contains("Session")) {

                    String Result = partquantity_match;
                    String replace = Result.replace("Error - ", "");
                    Session = 10;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {


                    side_scroll = true;

                    isLoading = false;

                    partsqtyscroll = new ArrayList<>();
                    partsNotCounted = false;
                    Sessiondata.getInstance().setLoaded_partsnotcounted(partsNotCounted);

                    Processid = Integer.parseInt(process_no.getText().toString());
                    partNum = partno.getText().toString();
                    start_bin = starting_bin.getText().toString();
                    end_bin = ending_bin.getText().toString();
                    branch_name = Sessiondata.getInstance().getCounting_branchno();

                    submit_uncount = false;
                    submit_variance = 0;
                    partsqty = new ArrayList<GetPartsQuantity>();

                    if (checkInternetConenction()) {

                        new AsyncPhysicalCountingUncount().execute();

                    } else {

                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                    }

                }
            } else {

            }
        }
    }

    public class AsyncGetProcessIdFilter extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                startindex = 0;
                endindex = 20;
                Log.d("startindex_PI", "" + startindex);
                Log.d("endindex_PI", "" + endindex);
                Log.d("processid_UserToken", "" + userToken);

                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("Get_branch", "" + branch_name);
                processidfilter = WebServiceConsumer.getInstance().GetprocessIdV3(userToken, process_no_filter, startindex, endindex, branch_name);

            } catch (SocketTimeoutException e) {

                processidfilter = null;
            } catch (Exception e) {
                processidfilter = null;
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setProcessid(processidfilter);

            if (processidfilter != null) {

                if (processidfilter.size() == 1) {

                    if (processidfilter.get(0).getMessage().length() != 0) {


                        String Result = processidfilter.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (processidfilter.get(0).getMessage().toString().contains("Session")) {
                            Session = 16;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {
                        enter_res_preliminary = processidfilter.get(0).getPreliminary();
                        Sessiondata.getInstance().setChk_preliminary(enter_res_preliminary);
                        Log.d("enter_res_preliminary", "" + enter_res_preliminary);
                    }
                } else {
                    enter_res_preliminary = "";
                    Sessiondata.getInstance().setChk_preliminary("");
                    Log.d("enter_res_preliminary", "" + enter_res_preliminary);
                }

            } else {
                enter_res_preliminary = "";
                Sessiondata.getInstance().setChk_preliminary("");
                Log.d("enter_res_preliminary", " null " + enter_res_preliminary);
            }

        }
    }

    public class AsyncGetProcessId extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                search_processid = "";
                startindex = 0;
                endindex = 20;
                Log.d("startindex_PI", "" + startindex);
                Log.d("endindex_PI", "" + endindex);

                Log.d("processid_UserToken", "" + userToken);

                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("Get_branch", "" + branch_name);
                processid = WebServiceConsumer.getInstance().GetprocessIdV3(userToken, search_processid, startindex, endindex, branch_name);

            } catch (SocketTimeoutException e) {

                processid = null;
            } catch (Exception e) {
                processid = null;
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setProcessid(processid);
            ProgressBar.dismiss();

            if (processid != null) {

                if (processid.size() == 1) {

                    if (processid.get(0).getMessage().length() != 0) {


                        String Result = processid.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (processid.get(0).getMessage().toString().contains("Session")) {
                            Session = 0;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {
                        if ((Dialog == null) || !Dialog.isShowing()) {

                            Dialog = new Dialog(Physical_counting_activity.this);
                            Dialog.setCanceledOnTouchOutside(false);
                            Dialog.setCancelable(false);
                            Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            Dialog.setContentView(R.layout.dialog_processid);

                            TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                            EditText processid_auto = (EditText) Dialog.findViewById(R.id.search_box);

                            LinearLayout top = (LinearLayout) Dialog.findViewById(R.id.top);
                            TextView empty = (TextView) Dialog.findViewById(R.id.empty);
                            empty.setTypeface(txt_face);

                            processid_auto.setTypeface(txt_face);

                            txt_header.setTypeface(header_face);
                            txt_header.setText("Select ProcessId");
                            list = (ListView) Dialog.findViewById(R.id.list);

                            dummy_processid = new ArrayList<>();
                            dummy_processid = (Sessiondata.getInstance().getProcessid());

                            if (dummy_processid.size() == 0) {
                                top.setVisibility(View.GONE);
                                empty.setVisibility(View.VISIBLE);
                                empty.setText("No PID");
                                list.setVisibility(View.GONE);
                            } else {
                                top.setVisibility(View.VISIBLE);
                                empty.setVisibility(View.GONE);
                                list.setVisibility(View.VISIBLE);

                                adapter_processid = new CustomAdapter_processid(Physical_counting_activity.this, dummy_processid);
                                list.setAdapter(adapter_processid);
                                justifyListViewHeightBasedOnChildren(list);

                                list.setOnScrollListener(new AbsListView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(AbsListView view, int scrollState) {


                                        int threshold = 1;

                                        int count = list.getCount();

                                        int counts;
                                        if (Sessiondata.getInstance().getProcessid() != null) {
                                            counts = Sessiondata.getInstance().getProcessid().get(0).getRowcnt();
                                        } else {
                                            counts = list.getCount();
                                        }

                                        Log.d("curr_process_list", "" + count);
                                        Log.d("tot_process_list", "" + counts);

                                        if (scrollState == SCROLL_STATE_IDLE) {
                                            if (list.getLastVisiblePosition() + 1 >= count - threshold) {
                                                if (count <= counts - 1) {
                                                    if (checkInternetConenction()) {

                                                        if (scroll_processid == true) {
                                                            endindex = endindex + 20;

                                                            new AsyncGetProcessIdmore().execute();
                                                        }

                                                    } else {

                                                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                                    }
                                                }

                                            }
                                        }

                                    }

                                    @Override
                                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                                    }
                                });

                                processid_auto.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        search_processid = s.toString();

                                        if (checkInternetConenction()) {

                                            new AsyncGetProcessIdmoreFilter().execute();

                                        } else {

                                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                        }

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                            }

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


                        }
                    }


                } else {
                    if ((Dialog == null) || !Dialog.isShowing()) {

                        Dialog = new Dialog(Physical_counting_activity.this);
                        Dialog.setCanceledOnTouchOutside(false);
                        Dialog.setCancelable(false);
                        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Dialog.setContentView(R.layout.dialog_processid);

                        TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                        EditText processid_auto = (EditText) Dialog.findViewById(R.id.search_box);

                        LinearLayout top = (LinearLayout) Dialog.findViewById(R.id.top);
                        TextView empty = (TextView) Dialog.findViewById(R.id.empty);
                        empty.setTypeface(txt_face);

                        processid_auto.setTypeface(txt_face);

                        txt_header.setTypeface(header_face);
                        txt_header.setText("Select ProcessId");
                        list = (ListView) Dialog.findViewById(R.id.list);

                        dummy_processid = new ArrayList<>();
                        dummy_processid = (Sessiondata.getInstance().getProcessid());

                        if (dummy_processid.size() == 0) {
                            top.setVisibility(View.GONE);
                            empty.setVisibility(View.VISIBLE);
                            empty.setText("No PID");
                            list.setVisibility(View.GONE);
                        } else {
                            top.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.GONE);
                            list.setVisibility(View.VISIBLE);

                            adapter_processid = new CustomAdapter_processid(Physical_counting_activity.this, dummy_processid);
                            list.setAdapter(adapter_processid);
                            justifyListViewHeightBasedOnChildren(list);

                            list.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {


                                    int threshold = 1;

                                    int count = list.getCount();

                                    int counts;
                                    if (Sessiondata.getInstance().getProcessid() != null) {
                                        counts = Sessiondata.getInstance().getProcessid().get(0).getRowcnt();
                                    } else {
                                        counts = list.getCount();
                                    }

                                    Log.d("curr_process_list", "" + count);
                                    Log.d("tot_process_list", "" + counts);

                                    if (scrollState == SCROLL_STATE_IDLE) {
                                        if (list.getLastVisiblePosition() + 1 >= count - threshold) {
                                            if (count <= counts - 1) {
                                                if (checkInternetConenction()) {

                                                    if (scroll_processid == true) {
                                                        endindex = endindex + 20;

                                                        new AsyncGetProcessIdmore().execute();
                                                    }

                                                } else {

                                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                                }
                                            }

                                        }
                                    }

                                }

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                                }
                            });

                            processid_auto.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    search_processid = s.toString();

                                    if (checkInternetConenction()) {

                                        new AsyncGetProcessIdmoreFilter().execute();

                                    } else {

                                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                    }

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        }

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


                    }

                }

            } else {
                if ((Dialog == null) || !Dialog.isShowing()) {

                    Dialog = new Dialog(Physical_counting_activity.this);
                    Dialog.setCanceledOnTouchOutside(false);
                    Dialog.setCancelable(false);
                    Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Dialog.setContentView(R.layout.dialog_processid);

                    TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                    EditText processid_auto = (EditText) Dialog.findViewById(R.id.search_box);

                    LinearLayout top = (LinearLayout) Dialog.findViewById(R.id.top);
                    TextView empty = (TextView) Dialog.findViewById(R.id.empty);
                    empty.setTypeface(txt_face);

                    processid_auto.setTypeface(txt_face);

                    txt_header.setTypeface(header_face);
                    txt_header.setText("Select ProcessId");
                    list = (ListView) Dialog.findViewById(R.id.list);

                    dummy_processid = new ArrayList<>();
                    dummy_processid = (Sessiondata.getInstance().getProcessid());

                    if (dummy_processid.size() == 0) {
                        top.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                        empty.setText("No PID");
                        list.setVisibility(View.GONE);
                    } else {
                        top.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);

                        adapter_processid = new CustomAdapter_processid(Physical_counting_activity.this, dummy_processid);
                        list.setAdapter(adapter_processid);
                        justifyListViewHeightBasedOnChildren(list);

                        list.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {


                                int threshold = 1;

                                int count = list.getCount();

                                int counts;
                                if (Sessiondata.getInstance().getProcessid() != null) {
                                    counts = Sessiondata.getInstance().getProcessid().get(0).getRowcnt();
                                } else {
                                    counts = list.getCount();
                                }

                                Log.d("curr_process_list", "" + count);
                                Log.d("tot_process_list", "" + counts);

                                if (scrollState == SCROLL_STATE_IDLE) {
                                    if (list.getLastVisiblePosition() + 1 >= count - threshold) {
                                        if (count <= counts - 1) {
                                            if (checkInternetConenction()) {

                                                if (scroll_processid == true) {
                                                    endindex = endindex + 20;

                                                    new AsyncGetProcessIdmore().execute();
                                                }

                                            } else {

                                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                            }
                                        }

                                    }
                                }

                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                            }
                        });

                        processid_auto.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                search_processid = s.toString();

                                if (checkInternetConenction()) {

                                    new AsyncGetProcessIdmoreFilter().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    }

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


                }
            }

        }
    }

    public class AsyncGetProcessIdmoreFilter extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                Log.d("processid_UserToken", "" + userToken);
                Log.d("startindex_PImore", "" + startindex);
                Log.d("endindex_PImore", "" + endindex);

                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("Get_branchmoreFilter", "" + branch_name);
                processid = WebServiceConsumer.getInstance().GetprocessIdV3(userToken, search_processid, startindex, endindex, branch_name);

            } catch (SocketTimeoutException e) {

                processid = null;
            } catch (Exception e) {
                processid = null;
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setProcessid(processid);

            if (processid != null) {

                if (processid.size() == 1) {

                    if (processid.get(0).getMessage().length() != 0) {


                        String Result = processid.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");

                        if (processid.get(0).getMessage().toString().contains("Session")) {
                            Session = 18;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {

                        dummy_processid = (Sessiondata.getInstance().getProcessid());
                        adapter_processid = new CustomAdapter_processid(Physical_counting_activity.this, dummy_processid);

                        list.setAdapter(adapter_processid);
                        justifyListViewHeightBasedOnChildren(list);


                    }
                } else {
                    dummy_processid = (Sessiondata.getInstance().getProcessid());
                    adapter_processid = new CustomAdapter_processid(Physical_counting_activity.this, dummy_processid);

                    list.setAdapter(adapter_processid);
                    justifyListViewHeightBasedOnChildren(list);
                }

            } else {

            }

        }
    }

    public class AsyncGetProcessIdmore extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            scroll_processid = false;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                Log.d("processid_UserToken", "" + userToken);
                Log.d("startindex_PImore", "" + startindex);
                Log.d("endindex_PImore", "" + endindex);

                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("Get_branchmore", "" + branch_name);
                processid = WebServiceConsumer.getInstance().GetprocessIdV3(userToken, search_processid, startindex, endindex, branch_name);
            } catch (SocketTimeoutException e) {

                processid = null;
            } catch (Exception e) {
                processid = null;
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            scroll_processid = true;

            Sessiondata.getInstance().setProcessid(processid);

            if (processid != null) {

                if (processid.size() == 1) {

                    if (processid.get(0).getMessage().length() != 0) {


                        String Result = processid.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");

                        if (processid.get(0).getMessage().toString().contains("Session")) {
                            Session = 0;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {

                        dummy_processid = (Sessiondata.getInstance().getProcessid());
                        adapter_processid = new CustomAdapter_processid(Physical_counting_activity.this, dummy_processid);

                        list.setAdapter(adapter_processid);
                        list.setSelectionFromTop(endindex - 20, 0);
                        justifyListViewHeightBasedOnChildren(list);


                    }
                } else {
                    dummy_processid = (Sessiondata.getInstance().getProcessid());
                    adapter_processid = new CustomAdapter_processid(Physical_counting_activity.this, dummy_processid);

                    list.setAdapter(adapter_processid);
                    list.setSelectionFromTop(endindex - 20, 0);
                    justifyListViewHeightBasedOnChildren(list);
                }

            } else {

            }

        }
    }

    public class CustomAdapter_Branch extends BaseAdapter {
        ArrayList<GetDealerBranch> result;
        private LayoutInflater inflater = null;

        public CustomAdapter_Branch(Context context, ArrayList<GetDealerBranch> list) {
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
                    String process_list = holder.process_list.getText().toString();
                    branch_no.setText(process_list);
                    branch_name = branch_no.getText().toString();
                    process_no.setText("");

                    starting_bin.setText("");
                    ending_bin.setText("");
                    if (partno.getText().toString().length() != 0) {
                        partno.setText("");
                    }


                    Sessiondata.getInstance().setCounting_branchname(branch_name);
                    Sessiondata.getInstance().setCounting_processid("");
                    Sessiondata.getInstance().setCounting_startbin("");
                    Sessiondata.getInstance().setCounting_endbin("");
                    Sessiondata.getInstance().setCounting_partnew("");


                    for (int ii = 0; ii < branch_name.length(); ii++) {

                        Character character = branch_name.charAt(ii);

                        if (character.toString().equals("-")) {

                            branch_name = branch_name.substring(0, ii);

                            Log.d("branch_trim", "" + branch_name);
                            break;
                        }
                    }

                    Sessiondata.getInstance().setCounting_branchno(branch_name);
                    Sessiondata.getInstance().setLoad_branch(branch_name);

                    if (Sessiondata.getInstance().getPartsquantity() != null) {
                        Sessiondata.getInstance().getPartsquantity().clear();
                    }
                    Sessiondata.getInstance().setLoad_check_enable(0);
                    Sessiondata.getInstance().setLoad_checkvar_enable(0);

                    countingdata = new ArrayList<GetPartsQuantity>();
                    isLoading = false;
                    loadDatawithoutscroll(countingdata);

                    count_label.setVisibility(View.GONE);
                    count_label_new.setVisibility(View.GONE);

                    varianceContainer.setVisibility(View.GONE);

                    mDialog.dismiss();
                }
            });

            return rowview_processid;

        }

        public class Holder {
            TextView process_list;
        }

    }

    public class CustomAdapter_processid extends BaseAdapter {
        ArrayList<GetProcessId> result;
        private LayoutInflater inflater = null;

        public CustomAdapter_processid(Context context, ArrayList<GetProcessId> list) {
            super();
            result = list;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            if (result != null) {
                return result.size();
            } else {
                return 0;
            }
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
            final View rowview_processid;

            rowview_processid = inflater.inflate(R.layout.activity_processid_childrow, null);

            holder.process_list = (TextView) rowview_processid.findViewById(R.id.process_list);
            holder.linear = (LinearLayout) rowview_processid.findViewById(R.id.linear);
            holder.process_list.setText(Integer.toString(result.get(position).getProcessid()));
            Log.d("ProcessId", "" + holder.process_list.getText().toString());
            holder.process_list.setTypeface(txt_face);
            holder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    touch_proc = 0;

                    if (Sessiondata.getInstance().getPartsquantity() != null) {
                        Sessiondata.getInstance().getPartsquantity().clear();
                    }

                    Sessiondata.getInstance().setLoad_check_enable(0);
                    Sessiondata.getInstance().setLoad_checkvar_enable(0);

                    countingdata = new ArrayList<GetPartsQuantity>();

                    isLoading = false;
                    loadDatawithoutscroll(countingdata);

                    process_list = result.get(position).getProcessid();
                    process_no.setText(Integer.toString(process_list));
                    chk_preliminary = result.get(position).getPreliminary();
                    enter_res_preliminary = result.get(position).getPreliminary();
                    Log.d("chk_preliminary", "" + chk_preliminary);
                    Sessiondata.getInstance().setChk_preliminary(enter_res_preliminary);
                    starting_bin.setText("");
                    ending_bin.setText("");
                    if (partno.getText().toString().length() != 0) {
                        partno.setText("");
                    }

                    count_label.setVisibility(View.GONE);
                    count_label_new.setVisibility(View.GONE);

                    varianceContainer.setVisibility(View.GONE);


                    Dialog.dismiss();

                }
            });

            return rowview_processid;
        }

        public class Holder {
            TextView process_list, description;
            LinearLayout linear;

        }
    }

    public class AsyncGetBinLocation extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("UserToken", "" + userToken);

                binLocations = WebServiceConsumer.getInstance().GetBinlocation(userToken, Part_No, process_list, "", "");

            } catch (SocketTimeoutException e) {

                binLocations = null;
            } catch (Exception e) {
                binLocations = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setBinlocation(binLocations);

            if (binLocations != null) {
                if (binLocations.size() == 1) {


                    if (binLocations.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = binLocations.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (binLocations.get(0).getMessage().toString().contains("Session")) {
                            Session = 1;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        }
                    } else {
                        process_no.setText(Integer.toString(process_list));
                        starting_bin.setText(binLocations.get(0).getStartbin());
                        ending_bin.setText(binLocations.get(0).getEndbin());
                        Dialog.dismiss();

                        countingdata = new ArrayList<GetPartsQuantity>();

                        loadDatawithoutscroll(countingdata);

                        ProgressBar.dismiss();
                    }
                } else {
                    process_no.setText(Integer.toString(process_list));
                    starting_bin.setText(binLocations.get(0).getStartbin());
                    ending_bin.setText(binLocations.get(0).getEndbin());
                    Dialog.dismiss();

                    countingdata = new ArrayList<GetPartsQuantity>();

                    loadDatawithoutscroll(countingdata);

                    ProgressBar.dismiss();
                }


            } else {
                ProgressBar.dismiss();
            }

        }
    }



    public class AsyncGetBinLocationDialog extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                part_no_bin = partnum.getText().toString();
                String pro_id = process_no.getText().toString();
                process_list = Integer.parseInt(pro_id);
                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("UserToken", "" + userToken);
                String load_branch = Sessiondata.getInstance().getLoad_branch();

                binLocations = WebServiceConsumer.getInstance().GetBinlocation(userToken, part_no_bin, process_list, dialog_mfg.getText().toString(), load_branch);

            } catch (SocketTimeoutException e) {

                binLocations = null;
            } catch (Exception e) {
                binLocations = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setBinlocation(binLocations);
            ProgressBar.dismiss();
            if (binLocations != null) {



                if(binLocations.size()==0){

                    allow_submit = true;

                }
                else if (binLocations.size() == 1) {




                    if (binLocations.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = binLocations.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (binLocations.get(0).getMessage().toString().contains("Session")) {
                            Session = 22;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        }
                    } else {
//                        process_no.setText(Integer.toString(process_list));
//                        starting_bin.setText(binLocations.get(0).getStartbin());
//                        ending_bin.setText(binLocations.get(0).getEndbin());
//                        Dialog.dismiss();
//
//                        countingdata = new ArrayList<GetPartsQuantity>();
//
//                        loadDatawithoutscroll(countingdata);

                        String Startbin_location = binLocations.get(0).getStartbin();
                        String Endbin_location = binLocations.get(0).getEndbin();



                        if (Startbin_location.equals("") && Endbin_location.equals(""))
                        {
                            allow_submit = true;

                        }
                        else
                        {


                           /* Part (SHOW PART NUMBER) is already found:
                            Primary BIN: ##
                            Secondary BIN ## <-- Do not show if EMPTY/NULL*/

                            String message = "";

                            if (Endbin_location.equals(""))
                            {
                                message  = "Part " + part_no_bin + " is already found: \n"
                                          + " Primary BIN :" + Startbin_location ;

                            }
                            else
                            {
                                message  = "Part " + part_no_bin + " is already found: \n"
                                        + " Primary BIN :" + Startbin_location + " \n"
                                        + "Secondary BIN :" + Endbin_location ;
                            }




                            SweetAlertDialog sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                    .setTitleText("Message!") //Entered/Scanned Part# is already found in the bin Location Primary Bin : '' Secondary Bin : ''
                                    .setContentText(message)
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(null)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {

                                            sDialog.dismiss();
                                            Dialog.dismiss();

                                        }
                                    });
                            sweet_load_alert.setCancelable(false);
                            sweet_load_alert.show();
                        }



                        ProgressBar.dismiss();

                    }
                } else {
//                    process_no.setText(Integer.toString(process_list));
//                    starting_bin.setText(binLocations.get(0).getStartbin());
//                    ending_bin.setText(binLocations.get(0).getEndbin());
//                    Dialog.dismiss();
//
//                    countingdata = new ArrayList<GetPartsQuantity>();
//
//                    loadDatawithoutscroll(countingdata);


                    String Startbin_location = binLocations.get(0).getStartbin();
                    String Endbin_location = binLocations.get(0).getEndbin();


                    if (Startbin_location.equals("") && Endbin_location.equals(""))
                    {
                        allow_submit = true;

                    }
                    else
                    {
                        String message = "";

                        if (Endbin_location.equals(""))
                        {
                            message  = "Part " + part_no_bin + " is already found: \n"
                                    + " Primary BIN :" + Startbin_location ;

                        }
                        else
                        {
                            message  = "Part " + part_no_bin + " is already found: \n"
                                    + " Primary BIN :" + Startbin_location + " \n"
                                    + "Secondary BIN :" + Endbin_location ;
                        }


                        SweetAlertDialog sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Message!") //Entered/Scanned Part# is already found in the bin Location Primary Bin : '' Secondary Bin : ''

                                .setContentText(message)
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.dismiss();
                                        Dialog.dismiss();

                                    }
                                });
                        sweet_load_alert.setCancelable(false);
                        sweet_load_alert.show();
                    }



                }
                ProgressBar.dismiss();
            }

        }
    }


    public class AsyncGetVarianceListOnResume extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                int load_processid = Sessiondata.getInstance().getLoad_processid();
                String load_startbin = Sessiondata.getInstance().getLoad_startbin();
                String load_endbin = Sessiondata.getInstance().getLoad_endbin();
                String load_branch = Sessiondata.getInstance().getLoad_branch();
                String load_partnum = Sessiondata.getInstance().getLoad_partNum();

                Log.d("UserToken", "" + userToken);
                Log.d("load_branch", "" + load_branch);
                Log.d("load_processid", "" + load_processid);
                Log.d("load_startbin", "" + load_startbin);
                Log.d("load_endbin", "" + load_endbin);
                Log.d("load_partnum", "" + load_partnum);

                side_scroll = false;

                varianceLists = WebServiceConsumer.getInstance().GetVarianceList(userToken, load_processid, load_startbin, load_endbin, load_branch, load_partnum, startindex_listmore, endindex_listmore);

            } catch (SocketTimeoutException e) {

                varianceLists = null;
            } catch (Exception e) {
                varianceLists = null;
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setVarianceLists(varianceLists);

            if (varianceLists != null) {
                for (int i = 0; i < varianceLists.size(); i++) {

                    int rowcount = varianceLists.get(i).getRowcnt();

                    Log.d("Resp_Rowcount", "" + rowcount);

                    if (rowcount != 0) {
                        varianceScroll.add(varianceLists.get(i));

                        Log.d("variance_Description", "" + varianceScroll.get(i).getDescription());
                        Log.d("variance_LoadQty", "" + varianceScroll.get(i).getLoadQty());
                        Log.d("variance_Partno", "" + varianceScroll.get(i).getPartno());
                        Log.d("variance_mfr", "" + varianceScroll.get(i).getMfr());
                        Log.d("variance_pbin", "" + varianceScroll.get(i).getPrimaryBinLocation());
                        Log.d("variance_sbin", "" + varianceScroll.get(i).getSecondaryBinLocation());
                        Log.d("variance_OnHandQty", "" + varianceScroll.get(i).getOnHandQty());
                        Log.d("variancet_Rowcnt", "" + varianceScroll.get(i).getRowcnt());
                        Log.d("variance_Qty", "" + varianceScroll.get(i).getQty());
                        Log.d("variance_Rownum", "" + varianceScroll.get(i).getRownum());
                    }

                }
            }

            var_quanty = new ArrayList<>();
            var_partno_list = new ArrayList<>();
            var_mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getVarianceLists() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getVarianceLists().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getVarianceLists().get(i).getRowcnt();
                    Log.d("Resp_Rowcount", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        var_quanty.add(Sessiondata.getInstance().getVarianceLists().get(i).getLoadQty());
                        var_partno_list.add(Sessiondata.getInstance().getVarianceLists().get(i).getPartno());
                        var_mfg_list.add(Sessiondata.getInstance().getVarianceLists().get(i).getMfr());
                    }
                }
            }

            if (varianceLists != null) {

                if (varianceLists.size() == 1) {

                    if (varianceLists.get(0).getMessage().length() != 0) {

                        ProgressBar.dismiss();

                        if (varianceLists.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            if (partsNotCounted == true) {

                                Sessiondata.getInstance().setLoad_check_enable(0);
                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                Sessiondata.getInstance().setVarianceLists(null);

                                count_label_new.setVisibility(View.VISIBLE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                variancedata = new ArrayList<GetVarianceList>();

                                loadData_variance(variancedata);

                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                    Sessiondata.getInstance().getVarianceLists().clear();
                                }

                            } else {
                                Sessiondata.getInstance().setVarianceLists(null);

                                count_label_new.setVisibility(View.GONE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                variancedata = new ArrayList<GetVarianceList>();


                                loadData_variance(variancedata);

                                String processvalues = String.valueOf(process_no.getText().toString());

                                if (sweet_load) {
                                    sweet_load = false;

                                    sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Bin Location not included for PID " + processvalues)
                                            .setCancelText("Ok")
                                            .showCancelButton(false)
                                            .setConfirmClickListener(null)
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override

                                                public void onClick(SweetAlertDialog sDialog) {

                                                    if (Sessiondata.getInstance().getVarianceLists() != null) {
                                                        Sessiondata.getInstance().getVarianceLists().clear();
                                                    }

                                                    Sessiondata.getInstance().setLoad_check_enable(0);
                                                    Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                    sweet_load = true;

                                                    sDialog.dismiss();

                                                }
                                            });
                                    sweet_load_alert.setCancelable(false);
                                    sweet_load_alert.show();
                                }

                            }
                        } else if (varianceLists.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")) {

                            Sessiondata.getInstance().setVarianceLists(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();

                            loadData_variance(variancedata);

                            String processvalues = String.valueOf(process_no.getText().toString());

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText("Bin Location not included for PID " + processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                                    Sessiondata.getInstance().getVarianceLists().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (varianceLists.get(0).getMessage().toString().contains("Procesd Id not found")) {
                            Sessiondata.getInstance().setVarianceLists(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();


                            loadData_variance(variancedata);

                            String processvalues = varianceLists.get(0).getMessage().toString();

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText(processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                                    Sessiondata.getInstance().getVarianceLists().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (varianceLists.get(0).getMessage().toString().contains("Data Conversion Error")) {
                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();


                            loadData_variance(variancedata);

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Data Conversion Error")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                                    Sessiondata.getInstance().getVarianceLists().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (varianceLists.get(0).getMessage().toString().contains("Parts number not found") ||
                                varianceLists.get(0).getMessage().toString().contains("Part number not found")) {

                            Sessiondata.getInstance().setVarianceLists(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();


                            loadData_variance(variancedata);

                            if (!partno.getText().toString().isEmpty()) {
                                if (sweetbranch) {
                                    sweetbranch = false;
                                    dialogbranch = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Part is not on PID. Would you like to add it and update the count?")
                                            .setCancelText("Cancel")
                                            .setConfirmText("Ok")
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

                                                    Sessiondata.getInstance().setHw_value_notmatch("hwpart_id");

                                                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());


                                                    Log.d("process_no_1NM", "" + Sessiondata.getInstance().getCounting_processid());
                                                    Log.d("starting_bin_1NM", "" + Sessiondata.getInstance().getCounting_startbin());
                                                    Log.d("ending_bin_1NM", "" + Sessiondata.getInstance().getCounting_endbin());

                                                    Sessiondata.getInstance().setLoad_value("Load");
                                                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                                                    Sessiondata.getInstance().setLoad_value_onresume("");

                                                    sDialog.dismiss();

                                                    Intent intent = new Intent(Physical_counting_activity.this, activity_handwrites_notmatch.class);
                                                    intent.putExtra("process_Id", process_no.getText().toString());
                                                    intent.putExtra("part_number", partno.getText().toString());
                                                    intent.putExtra("primary_bin", starting_bin.getText().toString());
                                                    intent.putExtra("secondary_bin", ending_bin.getText().toString());
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                                                }
                                            });
                                    dialogbranch.setCancelable(false);
                                    dialogbranch.show();
                                }
                            } else {

                                String message = partsqty.get(0).getMessage().toString();

                                if (Sweetalrt_list) {
                                    Sweetalrt_list = false;

                                    Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText(message)
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
                        } else {

                            String Result = varianceLists.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (varianceLists.get(0).getMessage().toString().contains("Session")) {
                                Session = 13;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    } else {


                        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                        Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_check_enable(0);
                        Sessiondata.getInstance().setLoad_checkvar_enable(1);

                        count_label_new.setVisibility(View.GONE);
                        count_label.setVisibility(View.VISIBLE);

                        loadData_variance(varianceScroll);

                        submit_value = 1;

                        ProgressBar.dismiss();


                    }
                } else if (varianceLists.size() == 0) {

                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.GONE);

                    submit_value = 0;

                    variancedata = new ArrayList<GetVarianceList>();


                    loadData_variance(variancedata);

                    ProgressBar.dismiss();

                } else {

                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                    Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_check_enable(0);
                    Sessiondata.getInstance().setLoad_checkvar_enable(1);


                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.VISIBLE);

                    loadData_variance(varianceScroll);

                    submit_value = 1;

                    ProgressBar.dismiss();

                }
            } else {
                count_label_new.setVisibility(View.GONE);
                count_label.setVisibility(View.GONE);

                variancedata = new ArrayList<GetVarianceList>();


                loadData_variance(variancedata);

                if (Sessiondata.getInstance().getVarianceLists() != null) {
                    Sessiondata.getInstance().getVarianceLists().clear();
                }
                Sessiondata.getInstance().setLoad_check_enable(0);
                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                ProgressBar.dismiss();

            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncGetVarianceList extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();

                Log.d("UserToken", "" + userToken);
                Log.d("branch", "" + branch_name);
                Log.d("startindex_listmore", "" + startindex_listmore);
                Log.d("endindex_listmore", "" + endindex_listmore);
                Log.d("partNum", "" + partNum);

                varianceLists = WebServiceConsumer.getInstance().GetVarianceList(userToken, Processid, start_bin, end_bin, branch_name, partNum, startindex_listmore, endindex_listmore);

            } catch (SocketTimeoutException e) {

                varianceLists = null;
            } catch (Exception e) {
                varianceLists = null;
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setVarianceLists(varianceLists);

            if (varianceLists != null) {
                for (int i = 0; i < varianceLists.size(); i++) {

                    int rowcount = varianceLists.get(i).getRowcnt();

                    Log.d("Resp_Rowcount", "" + rowcount);

                    if (rowcount != 0) {

                        varianceScroll.add(varianceLists.get(i));
                        Log.d("variance_Description", "" + varianceScroll.get(i).getDescription());
                        Log.d("variance_LoadQty", "" + varianceScroll.get(i).getLoadQty());
                        Log.d("variance_Partno", "" + varianceScroll.get(i).getPartno());
                        Log.d("variance_mfr", "" + varianceScroll.get(i).getMfr());
                        Log.d("variance_pbin", "" + varianceScroll.get(i).getPrimaryBinLocation());
                        Log.d("variance_sbin", "" + varianceScroll.get(i).getSecondaryBinLocation());
                        Log.d("variance_OnHandQty", "" + varianceScroll.get(i).getOnHandQty());
                        Log.d("variancet_Rowcnt", "" + varianceScroll.get(i).getRowcnt());
                        Log.d("variance_Qty", "" + varianceScroll.get(i).getQty());
                        Log.d("variance_Rownum", "" + varianceScroll.get(i).getRownum());
                    }

                }
            }

            var_quanty = new ArrayList<>();
            var_partno_list = new ArrayList<>();
            var_mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getVarianceLists() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getVarianceLists().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getVarianceLists().get(i).getRowcnt();
                    Log.d("Resp_Rowcount", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        var_quanty.add(Sessiondata.getInstance().getVarianceLists().get(i).getLoadQty());
                        var_partno_list.add(Sessiondata.getInstance().getVarianceLists().get(i).getPartno());
                        var_mfg_list.add(Sessiondata.getInstance().getVarianceLists().get(i).getMfr());
                    }
                }
            }

            if (varianceLists != null) {

                if (varianceLists.size() == 1) {

                    if (varianceLists.get(0).getMessage().length() != 0) {

                        ProgressBar.dismiss();

                        if (varianceLists.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            if (partsNotCounted == true) {

                                Sessiondata.getInstance().setLoad_check_enable(0);
                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                Sessiondata.getInstance().setVarianceLists(null);


                                count_label_new.setVisibility(View.VISIBLE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                variancedata = new ArrayList<GetVarianceList>();

                                loadData_variance(variancedata);

                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                    Sessiondata.getInstance().getVarianceLists().clear();
                                }

                            } else {
                                Sessiondata.getInstance().setVarianceLists(null);

                                count_label_new.setVisibility(View.GONE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                variancedata = new ArrayList<GetVarianceList>();


                                loadData_variance(variancedata);

                                String processvalues = String.valueOf(process_no.getText().toString());

                                if (sweet_load) {
                                    sweet_load = false;

                                    sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Bin Location not included for PID " + processvalues)
                                            .setCancelText("Ok")
                                            .showCancelButton(false)
                                            .setConfirmClickListener(null)
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override

                                                public void onClick(SweetAlertDialog sDialog) {

                                                    if (Sessiondata.getInstance().getVarianceLists() != null) {
                                                        Sessiondata.getInstance().getVarianceLists().clear();
                                                    }

                                                    Sessiondata.getInstance().setLoad_check_enable(0);
                                                    Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                    sweet_load = true;

                                                    sDialog.dismiss();

                                                }
                                            });
                                    sweet_load_alert.setCancelable(false);
                                    sweet_load_alert.show();
                                }

                            }
                        } else if (varianceLists.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")) {

                            Sessiondata.getInstance().setVarianceLists(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();

                            loadData_variance(variancedata);

                            String processvalues = String.valueOf(process_no.getText().toString());

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText("Bin Location not included for PID " + processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                                    Sessiondata.getInstance().getVarianceLists().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (varianceLists.get(0).getMessage().toString().contains("Procesd Id not found")) {
                            Sessiondata.getInstance().setVarianceLists(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();


                            loadData_variance(variancedata);

                            String processvalues = varianceLists.get(0).getMessage().toString();

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText(processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                                    Sessiondata.getInstance().getVarianceLists().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (varianceLists.get(0).getMessage().toString().contains("Parts number not found") ||
                                varianceLists.get(0).getMessage().toString().contains("Part number not found")) {

                            Sessiondata.getInstance().setVarianceLists(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();


                            loadData_variance(variancedata);

                            if (!partno.getText().toString().isEmpty()) {
                                if (sweetbranch) {
                                    sweetbranch = false;
                                    dialogbranch = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Part is not on PID. Would you like to add it and update the count?")
                                            .setCancelText("Cancel")
                                            .setConfirmText("Ok")
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

                                                    Sessiondata.getInstance().setHw_value_notmatch("hwpart_id");

                                                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                                                    Log.d("process_no_1NM", "" + Sessiondata.getInstance().getCounting_processid());
                                                    Log.d("starting_bin_1NM", "" + Sessiondata.getInstance().getCounting_startbin());
                                                    Log.d("ending_bin_1NM", "" + Sessiondata.getInstance().getCounting_endbin());

                                                    Sessiondata.getInstance().setLoad_value("Load");
                                                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                                                    Sessiondata.getInstance().setLoad_value_onresume("");

                                                    sDialog.dismiss();

                                                    Intent intent = new Intent(Physical_counting_activity.this, activity_handwrites_notmatch.class);
                                                    intent.putExtra("process_Id", process_no.getText().toString());
                                                    intent.putExtra("part_number", partno.getText().toString());
                                                    intent.putExtra("primary_bin", starting_bin.getText().toString());
                                                    intent.putExtra("secondary_bin", ending_bin.getText().toString());
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                                                }
                                            });
                                    dialogbranch.setCancelable(false);
                                    dialogbranch.show();
                                }
                            } else {

                                String message = partsqty.get(0).getMessage().toString();

                                if (Sweetalrt_list) {
                                    Sweetalrt_list = false;

                                    Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText(message)
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
                        } else if (varianceLists.get(0).getMessage().toString().contains("Data Conversion Error")) {
                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();


                            loadData_variance(variancedata);

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Data Conversion Error")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                                    Sessiondata.getInstance().getVarianceLists().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else {

                            String Result = varianceLists.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (varianceLists.get(0).getMessage().toString().contains("Session")) {
                                Session = 12;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    } else {

                        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                        Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_check_enable(0);
                        Sessiondata.getInstance().setLoad_checkvar_enable(1);

                        count_label_new.setVisibility(View.GONE);
                        count_label.setVisibility(View.VISIBLE);

                        loadData_variance(varianceScroll);

                        submit_value = 1;

                        ProgressBar.dismiss();


                    }
                } else if (varianceLists.size() == 0) {

                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.GONE);

                    submit_value = 0;

                    variancedata = new ArrayList<GetVarianceList>();


                    loadData_variance(variancedata);


                    ProgressBar.dismiss();

                } else {

                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                    Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_check_enable(0);
                    Sessiondata.getInstance().setLoad_checkvar_enable(1);


                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.VISIBLE);

                    loadData_variance(varianceScroll);

                    submit_value = 1;

                    ProgressBar.dismiss();

                }
            } else {
                count_label_new.setVisibility(View.GONE);
                count_label.setVisibility(View.GONE);

                variancedata = new ArrayList<GetVarianceList>();


                loadData_variance(variancedata);

                if (Sessiondata.getInstance().getVarianceLists() != null) {
                    Sessiondata.getInstance().getVarianceLists().clear();
                }
                Sessiondata.getInstance().setLoad_check_enable(0);
                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                ProgressBar.dismiss();

            }

        }
    }

    public class AsyncGetVarianceListScroll extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                int load_processid = Sessiondata.getInstance().getLoad_processid();
                String load_startbin = Sessiondata.getInstance().getLoad_startbin();
                String load_endbin = Sessiondata.getInstance().getLoad_endbin();
                String load_branch = Sessiondata.getInstance().getLoad_branch();
                String load_partnum = Sessiondata.getInstance().getLoad_partNum();

                Log.d("UserToken", "" + userToken);
                Log.d("load_branch", "" + load_branch);
                Log.d("load_processid", "" + load_processid);
                Log.d("load_startbin", "" + load_startbin);
                Log.d("load_endbin", "" + load_endbin);
                Log.d("load_partnum", "" + load_partnum);
                Log.d("startindex_listmore", "" + startindex_listmore);
                Log.d("endindex_listmore", "" + endindex_listmore);


                varianceLists = WebServiceConsumer.getInstance().GetVarianceList(userToken, load_processid, load_startbin, load_endbin, load_branch, load_partnum, startindex_listmore, endindex_listmore);

            } catch (SocketTimeoutException e) {

                varianceLists = null;
            } catch (Exception e) {
                varianceLists = null;
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            submit_btn_value = 0;
            Sessiondata.getInstance().setVarianceLists(varianceLists);

            if (varianceLists != null) {
                for (int i = 0; i < varianceLists.size(); i++) {

                    int rowcount = varianceLists.get(i).getRowcnt();

                    Log.d("Resp_Rowcount", "" + rowcount);

                    if (rowcount != 0) {
                        varianceScroll.add(varianceLists.get(i));

                        Log.d("variance_Description", "" + varianceScroll.get(i).getDescription());
                        Log.d("variance_LoadQty", "" + varianceScroll.get(i).getLoadQty());
                        Log.d("variance_Partno", "" + varianceScroll.get(i).getPartno());
                        Log.d("variance_mfr", "" + varianceScroll.get(i).getMfr());
                        Log.d("variance_pbin", "" + varianceScroll.get(i).getPrimaryBinLocation());
                        Log.d("variance_sbin", "" + varianceScroll.get(i).getSecondaryBinLocation());
                        Log.d("variance_OnHandQty", "" + varianceScroll.get(i).getOnHandQty());
                        Log.d("variancet_Rowcnt", "" + varianceScroll.get(i).getRowcnt());
                        Log.d("variance_Qty", "" + varianceScroll.get(i).getQty());
                        Log.d("variance_Rownum", "" + varianceScroll.get(i).getRownum());
                    }

                }
            }

            var_quanty = new ArrayList<>();
            var_partno_list = new ArrayList<>();
            var_mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getVarianceLists() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getVarianceLists().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getVarianceLists().get(i).getRowcnt();
                    Log.d("Resp_Rowcount", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        // quanty.add(Sessiondata.getInstance().getVarianceLists().get(i).getQty());
                        var_quanty.add(Sessiondata.getInstance().getVarianceLists().get(i).getLoadQty());
                        var_partno_list.add(Sessiondata.getInstance().getVarianceLists().get(i).getPartno());
                        var_mfg_list.add(Sessiondata.getInstance().getVarianceLists().get(i).getMfr());
                    }
                }
            }

            if (varianceLists != null) {

                if (varianceLists.size() == 1) {

                    if (varianceLists.get(0).getMessage().length() != 0) {

                        ProgressBar.dismiss();

                        if (varianceLists.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            if (partsNotCounted == true) {

                                Sessiondata.getInstance().setLoad_check_enable(0);
                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                Sessiondata.getInstance().setVarianceLists(null);


                                count_label_new.setVisibility(View.VISIBLE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                variancedata = new ArrayList<GetVarianceList>();

                                loadDatawithoutscroll_variance(variancedata);

                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                    Sessiondata.getInstance().getVarianceLists().clear();
                                }

                            } else {
                                Sessiondata.getInstance().setVarianceLists(null);

                                count_label_new.setVisibility(View.GONE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                variancedata = new ArrayList<GetVarianceList>();


                                loadDatawithoutscroll_variance(variancedata);

                            }
                        } else if (varianceLists.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")) {

                            Sessiondata.getInstance().setVarianceLists(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();

                            loadDatawithoutscroll_variance(variancedata);

                        } else if (varianceLists.get(0).getMessage().toString().contains("Procesd Id not found")) {
                            Sessiondata.getInstance().setVarianceLists(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();


                            loadDatawithoutscroll_variance(variancedata);

                        } else if (varianceLists.get(0).getMessage().toString().contains("Parts number not found") ||
                                varianceLists.get(0).getMessage().toString().contains("Part number not found")) {

                            Sessiondata.getInstance().setVarianceLists(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();


                            loadDatawithoutscroll_variance(variancedata);

                            if (!partno.getText().toString().isEmpty()) {

                            } else {

                                String message = partsqty.get(0).getMessage().toString();

                                if (Sweetalrt_list) {
                                    Sweetalrt_list = false;

                                    Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText(message)
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
                        } else if (varianceLists.get(0).getMessage().toString().contains("Data Conversion Error")) {
                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;

                            variancedata = new ArrayList<GetVarianceList>();


                            loadDatawithoutscroll_variance(variancedata);


                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Data Conversion Error")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getVarianceLists() != null) {
                                                    Sessiondata.getInstance().getVarianceLists().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else {

                            String Result = varianceLists.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (varianceLists.get(0).getMessage().toString().contains("Session")) {
                                Session = 19;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    } else {


                        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                        Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());


                        Sessiondata.getInstance().setLoad_check_enable(0);
                        Sessiondata.getInstance().setLoad_checkvar_enable(1);

                        count_label_new.setVisibility(View.GONE);
                        count_label.setVisibility(View.VISIBLE);

                        loadDatawithoutscroll_variance(varianceScroll);

                        submit_value = 1;

                        ProgressBar.dismiss();


                    }
                } else if (varianceLists.size() == 0) {

                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.GONE);

                    submit_value = 0;

                    variancedata = new ArrayList<GetVarianceList>();


                    loadDatawithoutscroll_variance(variancedata);

                    ProgressBar.dismiss();

                } else {

                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                    Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_check_enable(0);
                    Sessiondata.getInstance().setLoad_checkvar_enable(1);


                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.VISIBLE);

                    loadDatawithoutscroll_variance(varianceScroll);

                    submit_value = 1;

                    ProgressBar.dismiss();

                }
            } else {
                count_label_new.setVisibility(View.GONE);
                count_label.setVisibility(View.GONE);

                variancedata = new ArrayList<GetVarianceList>();


                loadDatawithoutscroll_variance(variancedata);

                if (Sessiondata.getInstance().getVarianceLists() != null) {
                    Sessiondata.getInstance().getVarianceLists().clear();
                }
                Sessiondata.getInstance().setLoad_check_enable(0);
                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                ProgressBar.dismiss();

            }

        }
    }

    public class AsyncPhysicalCountingList extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("UserToken", "" + userToken);
                Log.d("branch", "" + branch_name);
                Log.d("startindex_listmore", "" + startindex_listmore);
                Log.d("endindex_listmoress" +
                        "", "" + endindex_listmore);
                Log.d("partNum", "" + partNum);

                partsqty = WebServiceConsumer.getInstance().GetpartsqtyV4(userToken, Processid, start_bin, end_bin, branch_name, partsNotCounted, startindex_listmore, endindex_listmore, partNum);

            } catch (SocketTimeoutException e) {

                partsqty = null;
            } catch (Exception e) {
                partsqty = null;
                e.printStackTrace();
            }


            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            load_btn_Value = 0;
            Sessiondata.getInstance().setPartsquantity(partsqty);

            if (partsqty != null) {
                for (int i = 0; i < partsqty.size(); i++) {

                    int rowcount = partsqty.get(i).getRowcnt();

                    Log.d("Resp_Rowcount", "" + rowcount);

                    if (rowcount != 0) {
                        partsqtyscroll.add(partsqty.get(i));

                        Log.d("phycount_Description", "" + partsqtyscroll.get(i).getDescription());
                        Log.d("phycount_LoadQty", "" + partsqtyscroll.get(i).getLoadQty());
                        Log.d("phycount_Partno", "" + partsqtyscroll.get(i).getPartno());
                        Log.d("phycount_mfr", "" + partsqtyscroll.get(i).getMfr());
                        Log.d("phycount_pbin", "" + partsqtyscroll.get(i).getPrimaryBinLocation());
                        Log.d("phycount_sbin", "" + partsqtyscroll.get(i).getSecondaryBinLocation());
                        Log.d("phycount_OnHandQty", "" + partsqtyscroll.get(i).getOnHandQty());
                        Log.d("phycount_Rowcnt", "" + partsqtyscroll.get(i).getRowcnt());
                        Log.d("phycount_Qty", "" + partsqtyscroll.get(i).getQty());
                        Log.d("phycount_Rownum", "" + partsqtyscroll.get(i).getRownum());
                    }
                    contactAdapter.notifyDataSetChanged();

                }
            }

            quanty = new ArrayList<>();
            partno_list = new ArrayList<>();
            mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getPartsquantity() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getPartsquantity().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getPartsquantity().get(i).getRowcnt();
                    Log.d("Resp_Rowcount", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        quanty.add(Sessiondata.getInstance().getPartsquantity().get(i).getLoadQty());
                        partno_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getPartno());
                        mfg_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getMfr());
                    }
                }
            }

            if (partsqty != null) {

                if (partsqty.size() == 1) {

                    if (partsqty.get(0).getMessage().length() != 0) {

                        ProgressBar.dismiss();

                        if (partsqty.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            if (partsNotCounted == true) {

                                Sessiondata.getInstance().setLoad_check_enable(1);
                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                Sessiondata.getInstance().setPartsquantity(null);


                                count_label_new.setVisibility(View.VISIBLE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                countingdata = new ArrayList<GetPartsQuantity>();

                                loadDatawithoutscroll(countingdata);

                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                    Sessiondata.getInstance().getPartsquantity().clear();
                                }

                            } else {

                                Sessiondata.getInstance().setPartsquantity(null);

                                count_label_new.setVisibility(View.GONE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                countingdata = new ArrayList<GetPartsQuantity>();

                                loadDatawithoutscroll(countingdata);

                                String processvalues = String.valueOf(process_no.getText().toString());

                                if (checkInternetConenction()) {
                                    new AsyncPhysicalCountingListVarianceChk().execute();
                                } else {
                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else if (partsqty.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")) {

                            Sessiondata.getInstance().setPartsquantity(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                            String processvalues = String.valueOf(process_no.getText().toString());

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText("Bin Location not included for PID " + processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty.get(0).getMessage().toString().contains("Procesd Id not found")) {
                            Sessiondata.getInstance().setPartsquantity(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                            String processvalues = partsqty.get(0).getMessage().toString();

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText(processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty.get(0).getMessage().toString().contains("Data Conversion Error")) {
                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Data Conversion Error")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty.get(0).getMessage().toString().contains("Parts number not found") ||
                                partsqty.get(0).getMessage().toString().contains("Part number not found")) {

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();
                            loadDatawithoutscroll(countingdata);

                            if (!partno.getText().toString().isEmpty()) {
                                if (sweetbranch) {
                                    sweetbranch = false;
                                    dialogbranch = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Part is not on PID. Would you like to add it and update the count?")
                                            .setCancelText("Cancel")
                                            .setConfirmText("Ok")
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

                                                    Sessiondata.getInstance().setHw_value_notmatch("hwpart_id");

                                                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                                                    Log.d("process_no_1NM", "" + Sessiondata.getInstance().getCounting_processid());
                                                    Log.d("starting_bin_1NM", "" + Sessiondata.getInstance().getCounting_startbin());
                                                    Log.d("ending_bin_1NM", "" + Sessiondata.getInstance().getCounting_endbin());

                                                    Sessiondata.getInstance().setLoad_value("Load");
                                                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                                                    Sessiondata.getInstance().setLoad_value_onresume("");

                                                    sDialog.dismiss();

                                                    Intent intent = new Intent(Physical_counting_activity.this, activity_handwrites_notmatch.class);
                                                    intent.putExtra("process_Id", process_no.getText().toString());
                                                    intent.putExtra("part_number", partno.getText().toString());
                                                    intent.putExtra("primary_bin", starting_bin.getText().toString());
                                                    intent.putExtra("secondary_bin", ending_bin.getText().toString());
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                                                }
                                            });
                                    dialogbranch.setCancelable(false);
                                    dialogbranch.show();
                                }
                            } else {

                                String message = partsqty.get(0).getMessage().toString();

                                if (Sweetalrt_list) {
                                    Sweetalrt_list = false;

                                    Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText(message)
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
                        } else {

                            String Result = partsqty.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (partsqty.get(0).getMessage().toString().contains("Session")) {
                                Session = 2;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            } else {

                                count_label_new.setVisibility(View.GONE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                countingdata = new ArrayList<GetPartsQuantity>();

                                loadDatawithoutscroll(countingdata);

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText(Result + "")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
//                                                if(Sessiondata.getInstance().getPartsquantity() !=null){
//                                                    Sessiondata.getInstance().getPartsquantity().clear();
//                                                }
//                                                Sessiondata.getInstance().setLoad_check_enable(0);
//                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);
//
//                                                sweet_load=true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();

                            }
                        }
                    } else {


                        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                        Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_check_enable(1);
                        Sessiondata.getInstance().setLoad_checkvar_enable(0);


                        count_label_new.setVisibility(View.GONE);
                        count_label.setVisibility(View.VISIBLE);


                        loadDatawithoutscroll(partsqty);

                        submit_value = 1;

                        ProgressBar.dismiss();

                        if (partsNotCounted == false) {
                            new AsyncPhysicalCountingListVarianceChk().execute();
                        }

                    }
                } else if (partsqty.size() == 0) {

                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.GONE);

                    submit_value = 0;
                    countingdata = new ArrayList<GetPartsQuantity>();


                    loadDatawithoutscroll(countingdata);

                    ProgressBar.dismiss();

                    if (partsNotCounted == false) {
                        new AsyncPhysicalCountingListVarianceChk().execute();
                    }

                } else {

                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                    Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_check_enable(1);
                    Sessiondata.getInstance().setLoad_checkvar_enable(0);


                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.VISIBLE);


                    loadDatawithoutscroll(partsqtyscroll);
                    submit_value = 1;

                    ProgressBar.dismiss();

                    if (partsNotCounted == false) {
                        new AsyncPhysicalCountingListVarianceChk().execute();
                    }
                }
            } else {
                count_label_new.setVisibility(View.GONE);
                count_label.setVisibility(View.GONE);

                countingdata = new ArrayList<GetPartsQuantity>();

                loadDatawithoutscroll(countingdata);

                if (Sessiondata.getInstance().getPartsquantity() != null) {
                    Sessiondata.getInstance().getPartsquantity().clear();
                }
                Sessiondata.getInstance().setLoad_check_enable(0);
                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                ProgressBar.dismiss();

                if (partsNotCounted == false) {
                    new AsyncPhysicalCountingListVarianceChk().execute();
                }

            }

        }
    }

    public class AsyncPhysicalCountingUncount extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("UserToken", "" + userToken);
                Log.d("branch", "" + branch_name);
                Log.d("startindex_listmore", "" + startindex_listmore);
                Log.d("endindex_listmoress" +
                        "", "" + endindex_listmore);
                Log.d("partNum", "" + partNum);

                partsqty = WebServiceConsumer.getInstance().GetpartsqtyV4(userToken, Processid, start_bin, end_bin, branch_name, partsNotCounted, 0, endindex_listmore, partNum);

            } catch (SocketTimeoutException e) {

                partsqty = null;
            } catch (Exception e) {
                partsqty = null;
                e.printStackTrace();
            }


            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            Sessiondata.getInstance().setPartsquantity(partsqty);

            if (partsqty != null) {
                for (int i = 0; i < partsqty.size(); i++) {

                    int rowcount = partsqty.get(i).getRowcnt();

                    Log.d("Resp_Rowcount", "" + rowcount);

                    if (rowcount != 0) {
                        partsqtyscroll.add(partsqty.get(i));

                        Log.d("phycount_Description", "" + partsqtyscroll.get(i).getDescription());
                        Log.d("phycount_LoadQty", "" + partsqtyscroll.get(i).getLoadQty());
                        Log.d("phycount_Partno", "" + partsqtyscroll.get(i).getPartno());
                        Log.d("phycount_mfr", "" + partsqtyscroll.get(i).getMfr());
                        Log.d("phycount_pbin", "" + partsqtyscroll.get(i).getPrimaryBinLocation());
                        Log.d("phycount_sbin", "" + partsqtyscroll.get(i).getSecondaryBinLocation());
                        Log.d("phycount_OnHandQty", "" + partsqtyscroll.get(i).getOnHandQty());
                        Log.d("phycount_Rowcnt", "" + partsqtyscroll.get(i).getRowcnt());
                        Log.d("phycount_Qty", "" + partsqtyscroll.get(i).getQty());
                        Log.d("phycount_Rownum", "" + partsqtyscroll.get(i).getRownum());
                    }

                }
                ProgressBar.dismiss();
                loadDatawithoutscroll(partsqtyscroll);
            }

            quanty = new ArrayList<>();
            partno_list = new ArrayList<>();
            mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getPartsquantity() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getPartsquantity().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getPartsquantity().get(i).getRowcnt();
                    Log.d("Resp_Rowcount", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        // quanty.add(Sessiondata.getInstance().getPartsquantity().get(i).getQty());
                        quanty.add(Sessiondata.getInstance().getPartsquantity().get(i).getLoadQty());
                        partno_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getPartno());
                        mfg_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getMfr());
                    }
                }
            }

            if (partsqty != null) {

                if (partsqty.size() == 1) {

                    if (partsqty.get(0).getMessage().length() != 0) {

                        ProgressBar.dismiss();

                        if (partsqty.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            if (partsNotCounted == true) {

                                Sessiondata.getInstance().setLoad_check_enable(1);
                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                Sessiondata.getInstance().setPartsquantity(null);


                                count_label_new.setVisibility(View.VISIBLE);
                                count_label.setVisibility(View.GONE);


                                countingdata = new ArrayList<GetPartsQuantity>();

                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                    Sessiondata.getInstance().getPartsquantity().clear();
                                }

                            }
                        } else {
                            String Result = partsqty_variance.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (partsqty_variance.get(0).getMessage().toString().contains("Session")) {
                                Session = 17;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }


                    }
                }
            }
        }
    }

    public class AsyncPhysicalCountingListVarianceChk extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();

                int load_processid = Sessiondata.getInstance().getLoad_processid();
                String load_startbin = Sessiondata.getInstance().getLoad_startbin();
                String load_endbin = Sessiondata.getInstance().getLoad_endbin();
                String load_branch = Sessiondata.getInstance().getLoad_branch();
                String load_partnum = Sessiondata.getInstance().getLoad_partNum();


                Log.d("Varience_chk ", "UserToken " + userToken);
                Log.d("Varience_chk ", "branch " + load_branch);
                Log.d("Varience_chk ", "startindex " + startindex_listmore);
                Log.d("Varience_chk ", "endindex " + endindex_listmore);
                Log.d("Varience_chk ", "Processid " + load_processid);
                Log.d("Varience_chk ", "start_bin " + load_startbin);
                Log.d("Varience_chk ", "end_bin " + load_endbin);
                Log.d("Varience_chk ", "partNum " + load_partnum);

                partsqty_variance = WebServiceConsumer.getInstance().GetpartsqtyV4(userToken, load_processid, load_startbin, load_endbin, load_branch, true, 0, 50, load_partnum);

            } catch (SocketTimeoutException e) {

                partsqty_variance = null;
            } catch (Exception e) {
                partsqty_variance = null;
                e.printStackTrace();
            }


            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (partsqty_variance != null) {

                if (partsqty_variance.size() == 1) {

                    if (partsqty_variance.get(0).getMessage().length() != 0) {

                        ProgressBar.dismiss();

                        if (partsqty_variance.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            varianceContainer.setVisibility(View.VISIBLE);

                        } else if (partsqty_variance.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")) {
                            varianceContainer.setVisibility(View.GONE);
                        } else if (partsqty_variance.get(0).getMessage().toString().contains("Procesd Id not found")) {
                            varianceContainer.setVisibility(View.GONE);
                        } else if (partsqty_variance.get(0).getMessage().toString().contains("Data Conversion Error")) {
                            varianceContainer.setVisibility(View.GONE);
                        } else if (partsqty_variance.get(0).getMessage().toString().contains("Parts number not found") ||
                                partsqty_variance.get(0).getMessage().toString().contains("Part number not found")) {
                            varianceContainer.setVisibility(View.GONE);
                        } else {

                            String Result = partsqty_variance.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (partsqty_variance.get(0).getMessage().toString().contains("Session")) {
                                Session = 9;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    } else {
                        varianceContainer.setVisibility(View.GONE);
                        ProgressBar.dismiss();
                    }
                } else if (partsqty_variance.size() == 0) {

                    varianceContainer.setVisibility(View.GONE);
                    ProgressBar.dismiss();
                } else {
                    ProgressBar.dismiss();
                    varianceContainer.setVisibility(View.GONE);
                }
            } else {
                ProgressBar.dismiss();
                varianceContainer.setVisibility(View.GONE);
            }

        }
    }

    public class AsyncPhysicalCountingListFilter extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();

                Log.d("Filter ", "UserToken " + userToken);
                Log.d("Filter ", "branch " + branch_name);
                Log.d("Filter ", "startindex " + startindex_listmore);
                Log.d("Filter ", "endindex " + endindex_listmore);
                Log.d("Filter ", "Processid " + Processid);
                Log.d("Filter ", "start_bin " + start_bin);
                Log.d("Filter ", "end_bin " + end_bin);
                Log.d("Filter ", "partNum " + partNum);

                partsqty_filter = WebServiceConsumer.getInstance().GetpartsqtyV4(userToken, Processid, start_bin, end_bin, branch_name, false, 0, 50, partNum);

            } catch (SocketTimeoutException e) {

                partsqty_filter = null;
            } catch (Exception e) {
                partsqty_filter = null;
                e.printStackTrace();
            }


            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            load_btn_Value = 0;
            Sessiondata.getInstance().setPartsquantity(partsqty_filter);

            if (partsqty_filter != null) {
                for (int i = 0; i < partsqty_filter.size(); i++) {

                    int rowcount = partsqty_filter.get(i).getRowcnt();

                    Log.d("Resp_Rowcount", "" + rowcount);

                    if (rowcount != 0) {
                        partsqtyscroll.add(partsqty_filter.get(i));

                        Log.d("phycount_Description", "" + partsqtyscroll.get(i).getDescription());
                        Log.d("phycount_LoadQty", "" + partsqtyscroll.get(i).getLoadQty());
                        Log.d("phycount_Partno", "" + partsqtyscroll.get(i).getPartno());
                        Log.d("phycount_mfr", "" + partsqtyscroll.get(i).getMfr());
                        Log.d("phycount_pbin", "" + partsqtyscroll.get(i).getPrimaryBinLocation());
                        Log.d("phycount_sbin", "" + partsqtyscroll.get(i).getSecondaryBinLocation());
                        Log.d("phycount_OnHandQty", "" + partsqtyscroll.get(i).getOnHandQty());
                        Log.d("phycount_Rowcnt", "" + partsqtyscroll.get(i).getRowcnt());
                        Log.d("phycount_Qty", "" + partsqtyscroll.get(i).getQty());
                        Log.d("phycount_Rownum", "" + partsqtyscroll.get(i).getRownum());
                    }

                }
            }

            quanty = new ArrayList<>();
            partno_list = new ArrayList<>();
            mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getPartsquantity() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getPartsquantity().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getPartsquantity().get(i).getRowcnt();
                    Log.d("Resp_Rowcount", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        quanty.add(Sessiondata.getInstance().getPartsquantity().get(i).getLoadQty());
                        partno_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getPartno());
                        mfg_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getMfr());
                    }
                }
            }

            if (partsqty_filter != null) {

                if (partsqty_filter.size() == 1) {

                    if (partsqty_filter.get(0).getMessage().length() != 0) {


                        if (partsqty_filter.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            if (partsNotCounted == true) {

                                Sessiondata.getInstance().setLoad_check_enable(1);
                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                Sessiondata.getInstance().setPartsquantity(null);


                                count_label_new.setVisibility(View.VISIBLE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                countingdata = new ArrayList<GetPartsQuantity>();


                                loadDatawithoutscroll(countingdata);

                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                    Sessiondata.getInstance().getPartsquantity().clear();
                                }

                            } else {

                                Sessiondata.getInstance().setPartsquantity(null);

                                count_label_new.setVisibility(View.GONE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                countingdata = new ArrayList<GetPartsQuantity>();


                                loadDatawithoutscroll(countingdata);

                                if (checkInternetConenction()) {
                                    new AsyncPhysicalCountingListVarianceChk().execute();
                                } else {
                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else if (partsqty_filter.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")) {

                            Sessiondata.getInstance().setPartsquantity(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                            String processvalues = String.valueOf(process_no.getText().toString());

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText("Bin Location not included for PID " + processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty_filter.get(0).getMessage().toString().contains("Procesd Id not found")) {
                            Sessiondata.getInstance().setPartsquantity(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                            String processvalues = partsqty_filter.get(0).getMessage().toString();

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText(processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty_filter.get(0).getMessage().toString().contains("Data Conversion Error")) {
                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Data Conversion Error")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty_filter.get(0).getMessage().toString().contains("Parts number not found") ||
                                partsqty_filter.get(0).getMessage().toString().contains("Part number not found")) {

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();
                            loadDatawithoutscroll(countingdata);

                            if (!partno.getText().toString().isEmpty()) {
                                if (sweetbranch) {
                                    sweetbranch = false;
                                    dialogbranch = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Part is not on PID. Would you like to add it and update the count?")
                                            .setCancelText("Cancel")
                                            .setConfirmText("Ok")
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

                                                    Sessiondata.getInstance().setHw_value_notmatch("hwpart_id");

                                                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());


                                                    Log.d("process_no_1NM", "" + Sessiondata.getInstance().getCounting_processid());
                                                    Log.d("starting_bin_1NM", "" + Sessiondata.getInstance().getCounting_startbin());
                                                    Log.d("ending_bin_1NM", "" + Sessiondata.getInstance().getCounting_endbin());

                                                    Sessiondata.getInstance().setLoad_value("Load");
                                                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                                                    Sessiondata.getInstance().setLoad_value_onresume("");

                                                    sDialog.dismiss();

                                                    Intent intent = new Intent(Physical_counting_activity.this, activity_handwrites_notmatch.class);
                                                    intent.putExtra("process_Id", process_no.getText().toString());
                                                    intent.putExtra("part_number", partno.getText().toString());
                                                    intent.putExtra("primary_bin", starting_bin.getText().toString());
                                                    intent.putExtra("secondary_bin", ending_bin.getText().toString());
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                                                }
                                            });
                                    dialogbranch.setCancelable(false);
                                    dialogbranch.show();
                                }
                            } else {

                                String message = partsqty_filter.get(0).getMessage().toString();

                                if (Sweetalrt_list) {
                                    Sweetalrt_list = false;

                                    Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText(message)
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
                        } else {

                            String Result = partsqty_filter.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (partsqty_filter.get(0).getMessage().toString().contains("Session")) {
                                Session = 11;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    } else {

                        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                        Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_check_enable(1);
                        Sessiondata.getInstance().setLoad_checkvar_enable(0);


                        count_label_new.setVisibility(View.GONE);
                        count_label.setVisibility(View.VISIBLE);


                        loadDatawithoutscroll(partsqty_filter);

                        submit_value = 1;

                        if (partsNotCounted == false) {
                            new AsyncPhysicalCountingListVarianceChk().execute();
                        }

                    }
                } else if (partsqty_filter.size() == 0) {

                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.GONE);

                    submit_value = 0;
                    countingdata = new ArrayList<GetPartsQuantity>();


                    loadDatawithoutscroll(countingdata);

                    if (partsNotCounted == false) {
                        new AsyncPhysicalCountingListVarianceChk().execute();
                    }

                } else {

                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                    Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_check_enable(1);
                    Sessiondata.getInstance().setLoad_checkvar_enable(0);


                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.VISIBLE);


                    loadDatawithoutscroll(partsqtyscroll);
                    submit_value = 1;

                    if (partsNotCounted == false) {
                        new AsyncPhysicalCountingListVarianceChk().execute();
                    }
                }
            } else {
                count_label_new.setVisibility(View.GONE);
                count_label.setVisibility(View.GONE);

                countingdata = new ArrayList<GetPartsQuantity>();


                loadDatawithoutscroll(countingdata);

                if (Sessiondata.getInstance().getPartsquantity() != null) {
                    Sessiondata.getInstance().getPartsquantity().clear();
                }
                Sessiondata.getInstance().setLoad_check_enable(0);
                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                if (partsNotCounted == false) {
                    new AsyncPhysicalCountingListVarianceChk().execute();
                }

            }

        }
    }

    public class AsyncPhysicalCountingListFilter_Part extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();

                Log.d("Filter ", "UserToken " + userToken);
                Log.d("Filter ", "branch " + branch_name);
                Log.d("Filter ", "startindex " + startindex_listmore);
                Log.d("Filter ", "endindex " + endindex_listmore);
                Log.d("Filter ", "Processid " + Processid);
                Log.d("Filter ", "start_bin " + start_bin);
                Log.d("Filter ", "end_bin " + end_bin);
                Log.d("Filter ", "partNum " + partNum);

                partsqty_filter = WebServiceConsumer.getInstance().GetpartsqtyV4(userToken, Processid, start_bin, end_bin, branch_name, false, 0, 50, partNum);

            } catch (SocketTimeoutException e) {

                partsqty_filter = null;
            } catch (Exception e) {
                partsqty_filter = null;
                e.printStackTrace();
            }


            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            load_btn_Value = 0;
            Sessiondata.getInstance().setPartsquantity(partsqty_filter);

            if (partsqty_filter != null) {
                for (int i = 0; i < partsqty_filter.size(); i++) {

                    int rowcount = partsqty_filter.get(i).getRowcnt();

                    Log.d("Resp_Rowcount", "" + rowcount);

                    if (rowcount != 0) {
                        partsqtyscroll.add(partsqty_filter.get(i));

                        Log.d("phycount_Description", "" + partsqtyscroll.get(i).getDescription());
                        Log.d("phycount_LoadQty", "" + partsqtyscroll.get(i).getLoadQty());
                        Log.d("phycount_Partno", "" + partsqtyscroll.get(i).getPartno());
                        Log.d("phycount_mfr", "" + partsqtyscroll.get(i).getMfr());
                        Log.d("phycount_pbin", "" + partsqtyscroll.get(i).getPrimaryBinLocation());
                        Log.d("phycount_sbin", "" + partsqtyscroll.get(i).getSecondaryBinLocation());
                        Log.d("phycount_OnHandQty", "" + partsqtyscroll.get(i).getOnHandQty());
                        Log.d("phycount_Rowcnt", "" + partsqtyscroll.get(i).getRowcnt());
                        Log.d("phycount_Qty", "" + partsqtyscroll.get(i).getQty());
                        Log.d("phycount_Rownum", "" + partsqtyscroll.get(i).getRownum());
                    }

                }
            }

            quanty = new ArrayList<>();
            partno_list = new ArrayList<>();
            mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getPartsquantity() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getPartsquantity().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getPartsquantity().get(i).getRowcnt();
                    Log.d("Resp_Rowcount", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        quanty.add(Sessiondata.getInstance().getPartsquantity().get(i).getLoadQty());
                        partno_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getPartno());
                        mfg_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getMfr());
                    }
                }
            }

            if (partsqty_filter != null) {

                if (partsqty_filter.size() == 1) {

                    if (partsqty_filter.get(0).getMessage().length() != 0) {


                        if (partsqty_filter.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            if (partsNotCounted == true) {

                                Sessiondata.getInstance().setLoad_check_enable(1);
                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                Sessiondata.getInstance().setPartsquantity(null);


                                /*count_label_new.setVisibility(View.VISIBLE);
                                count_label.setVisibility(View.GONE);

                                submit_value=0;

                                countingdata = new ArrayList<GetPartsQuantity>();


                                loadDatawithoutscroll(countingdata);*/

                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                    Sessiondata.getInstance().getPartsquantity().clear();
                                }

                                if (checkInternetConenction()) {
                                    find_adapter = 0;
                                    partsNotCounted = false;
                                    Sessiondata.getInstance().setLoaded_partsnotcounted(partsNotCounted);
                                    submit_uncount = false;
                                    submit_variance = 0;
                                    new AsyncPhysicalCountingListFilter().execute();
                                } else {
                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }

                            } else {

                                Sessiondata.getInstance().setPartsquantity(null);

                                count_label_new.setVisibility(View.GONE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                countingdata = new ArrayList<GetPartsQuantity>();


                                loadDatawithoutscroll(countingdata);

                                if (checkInternetConenction()) {
                                    new AsyncPhysicalCountingListVarianceChk().execute();
                                } else {
                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else if (partsqty_filter.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")) {

                            Sessiondata.getInstance().setPartsquantity(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                            String processvalues = String.valueOf(process_no.getText().toString());

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText("Bin Location not included for PID " + processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty_filter.get(0).getMessage().toString().contains("Procesd Id not found")) {
                            Sessiondata.getInstance().setPartsquantity(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                            String processvalues = partsqty_filter.get(0).getMessage().toString();

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText(processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty_filter.get(0).getMessage().toString().contains("Data Conversion Error")) {
                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Data Conversion Error")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;

                                                sDialog.dismiss();

                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty_filter.get(0).getMessage().toString().contains("Parts number not found") ||
                                partsqty_filter.get(0).getMessage().toString().contains("Part number not found")) {

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();
                            loadDatawithoutscroll(countingdata);

                            if (!partno.getText().toString().isEmpty()) {
                                if (sweetbranch) {
                                    sweetbranch = false;
                                    dialogbranch = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Part is not on PID. Would you like to add it and update the count?")
                                            .setCancelText("Cancel")
                                            .setConfirmText("Ok")
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

                                                    Sessiondata.getInstance().setHw_value_notmatch("hwpart_id");

                                                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());


                                                    Log.d("process_no_1NM", "" + Sessiondata.getInstance().getCounting_processid());
                                                    Log.d("starting_bin_1NM", "" + Sessiondata.getInstance().getCounting_startbin());
                                                    Log.d("ending_bin_1NM", "" + Sessiondata.getInstance().getCounting_endbin());

                                                    Sessiondata.getInstance().setLoad_value("Load");
                                                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                                                    Sessiondata.getInstance().setLoad_value_onresume("");

                                                    sDialog.dismiss();

                                                    Intent intent = new Intent(Physical_counting_activity.this, activity_handwrites_notmatch.class);
                                                    intent.putExtra("process_Id", process_no.getText().toString());
                                                    intent.putExtra("part_number", partno.getText().toString());
                                                    intent.putExtra("primary_bin", starting_bin.getText().toString());
                                                    intent.putExtra("secondary_bin", ending_bin.getText().toString());
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                                                }
                                            });
                                    dialogbranch.setCancelable(false);
                                    dialogbranch.show();
                                }
                            } else {

                                String message = partsqty_filter.get(0).getMessage().toString();

                                if (Sweetalrt_list) {
                                    Sweetalrt_list = false;

                                    Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText(message)
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
                        } else {

                            String Result = partsqty_filter.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (partsqty_filter.get(0).getMessage().toString().contains("Session")) {
                                Session = 20;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    } else {

                        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                        Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_check_enable(1);
                        Sessiondata.getInstance().setLoad_checkvar_enable(0);


                        count_label_new.setVisibility(View.GONE);
                        count_label.setVisibility(View.VISIBLE);


                        loadDatawithoutscroll(partsqty_filter);

                        submit_value = 1;

                        /*if (partsNotCounted == false){
                            new AsyncPhysicalCountingListVarianceChk().execute();
                        }*/

                        if (partsNotCounted == true) {
                            if (checkInternetConenction()) {
                                find_adapter = 0;
                                partsNotCounted = false;
                                Sessiondata.getInstance().setLoaded_partsnotcounted(partsNotCounted);
                                submit_uncount = false;
                                submit_variance = 0;
                                new AsyncPhysicalCountingListFilter().execute();
                            } else {
                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            new AsyncPhysicalCountingListVarianceChk().execute();
                        }

                    }
                } else if (partsqty_filter.size() == 0) {

                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.GONE);

                    submit_value = 0;
                    countingdata = new ArrayList<GetPartsQuantity>();


                    loadDatawithoutscroll(countingdata);

                    /*if (partsNotCounted == false){
                        new AsyncPhysicalCountingListVarianceChk().execute();
                    }*/

                    if (partsNotCounted == true) {
                        if (checkInternetConenction()) {
                            find_adapter = 0;
                            partsNotCounted = false;
                            Sessiondata.getInstance().setLoaded_partsnotcounted(partsNotCounted);
                            submit_uncount = false;
                            submit_variance = 0;
                            new AsyncPhysicalCountingListFilter().execute();
                        } else {
                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        new AsyncPhysicalCountingListVarianceChk().execute();
                    }

                } else {

                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                    Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_check_enable(1);
                    Sessiondata.getInstance().setLoad_checkvar_enable(0);


                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.VISIBLE);


                    loadDatawithoutscroll(partsqtyscroll);
                    submit_value = 1;

                    if (partsNotCounted == false) {
                        new AsyncPhysicalCountingListVarianceChk().execute();
                    }
                }
            } else {
                count_label_new.setVisibility(View.GONE);
                count_label.setVisibility(View.GONE);

                countingdata = new ArrayList<GetPartsQuantity>();


                loadDatawithoutscroll(countingdata);

                if (Sessiondata.getInstance().getPartsquantity() != null) {
                    Sessiondata.getInstance().getPartsquantity().clear();
                }
                Sessiondata.getInstance().setLoad_check_enable(0);
                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                if (partsNotCounted == true) {
                    if (checkInternetConenction()) {
                        find_adapter = 0;
                        partsNotCounted = false;
                        Sessiondata.getInstance().setLoaded_partsnotcounted(partsNotCounted);
                        submit_uncount = false;
                        submit_variance = 0;
                        new AsyncPhysicalCountingListFilter().execute();
                    } else {
                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    new AsyncPhysicalCountingListVarianceChk().execute();
                }

            }

        }
    }

    public class AsyncPhysicalCountingListOnResume extends AsyncTask<Void, Void, Void> {
        int load_processid;
        String load_startbin, load_endbin, load_partnum;

        protected void onPreExecute() {

            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                load_processid = Sessiondata.getInstance().getLoad_processid();
                load_startbin = Sessiondata.getInstance().getLoad_startbin();
                load_endbin = Sessiondata.getInstance().getLoad_endbin();
                load_partsnotcounted = Sessiondata.getInstance().getLoaded_partsnotcounted();
                String load_branch = Sessiondata.getInstance().getLoad_branch();
                load_partnum = Sessiondata.getInstance().getLoad_partNum();

                Log.d("UserToken", "" + userToken);
                Log.d("load_processid", "" + load_processid);
                Log.d("load_startbin", "" + load_startbin);
                Log.d("load_endbin", "" + load_endbin);
                Log.d("load_branch", "" + load_branch);
                Log.d("load_partsnotcountedm", "" + load_partsnotcounted);
                Log.d("load_partnum", "" + load_partnum);

                if (load_partsnotcounted == false) {
                    side_scroll = true;
                } else {
                    side_scroll = false;
                }
                partsqty = WebServiceConsumer.getInstance().GetpartsqtyV4(userToken, load_processid, load_startbin, load_endbin, load_branch, load_partsnotcounted, startindex_listmore, endindex_listmore, load_partnum);

            } catch (SocketTimeoutException e) {

                partsqty = null;
            } catch (Exception e) {
                partsqty = null;
                e.printStackTrace();
            }


            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            load_btn_Value = 0;
            Sessiondata.getInstance().setPartsquantity(partsqty);

            if (partsqty != null) {
                for (int i = 0; i < partsqty.size(); i++) {

                    int rowcount = partsqty.get(i).getRowcnt();
                    Log.d("Resp_Rowcount_onresume", "" + rowcount);

                    if (rowcount != 0) {
                        partsqtyscroll.add(partsqty.get(i));
                    }
                }
            }


            quanty = new ArrayList<>();
            partno_list = new ArrayList<>();
            mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getPartsquantity() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getPartsquantity().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getPartsquantity().get(i).getRowcnt();
                    Log.d("Resp_Rowcount_onresume", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        quanty.add(Sessiondata.getInstance().getPartsquantity().get(i).getLoadQty());
                        partno_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getPartno());
                        mfg_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getMfr());
                    }
                }
            }


            if (partsqty != null) {


                if (partsqty.size() == 1) {

                    if (partsqty.get(0).getMessage().length() != 0) {

                        ProgressBar.dismiss();

                        if (partsqty.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            if (load_partsnotcounted == true) {

                                count_label_new.setVisibility(View.VISIBLE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                countingdata = new ArrayList<GetPartsQuantity>();

                                loadDatawithoutscroll(countingdata);

                            } else {

                                count_label_new.setVisibility(View.GONE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                countingdata = new ArrayList<GetPartsQuantity>();

                                loadDatawithoutscroll(countingdata);

                                if (checkInternetConenction()) {
                                    new AsyncPhysicalCountingListVarianceChk().execute();
                                } else {
                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }

                            }
                        } else if (partsqty.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")) {

                            Sessiondata.getInstance().setPartsquantity(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);


                        } else if (partsqty.get(0).getMessage().toString().contains("Procesd Id not found")) {
                            Sessiondata.getInstance().setPartsquantity(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                        } else if (partsqty.get(0).getMessage().toString().contains("Parts number not found") ||
                                partsqty.get(0).getMessage().toString().contains("Part number not found")) {

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();
                            loadDatawithoutscroll(countingdata);

                            if (!partno.getText().toString().isEmpty()) {
                                if (sweetbranch) {
                                    sweetbranch = false;
                                    dialogbranch = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Part is not on PID. Would you like to add it and update the count?")
                                            .setCancelText("Cancel")
                                            .setConfirmText("Ok")
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

                                                    Sessiondata.getInstance().setHw_value_notmatch("hwpart_id");

                                                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());


                                                    Log.d("process_no_1NM", "" + Sessiondata.getInstance().getCounting_processid());
                                                    Log.d("starting_bin_1NM", "" + Sessiondata.getInstance().getCounting_startbin());
                                                    Log.d("ending_bin_1NM", "" + Sessiondata.getInstance().getCounting_endbin());

                                                    Sessiondata.getInstance().setLoad_value("Load");
                                                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                                                    Sessiondata.getInstance().setLoad_value_onresume("");

                                                    sDialog.dismiss();

                                                    Intent intent = new Intent(Physical_counting_activity.this, activity_handwrites_notmatch.class);
                                                    intent.putExtra("process_Id", process_no.getText().toString());
                                                    intent.putExtra("part_number", partno.getText().toString());
                                                    intent.putExtra("primary_bin", starting_bin.getText().toString());
                                                    intent.putExtra("secondary_bin", ending_bin.getText().toString());
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                                                }
                                            });
                                    dialogbranch.setCancelable(false);
                                    dialogbranch.show();
                                }
                            } else {

                                String message = partsqty.get(0).getMessage().toString();

                                if (Sweetalrt_list) {
                                    Sweetalrt_list = false;

                                    Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText(message)
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
                        } else if (partsqty.get(0).getMessage().toString().contains("Data Conversion Error")) {
                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();


                            loadDatawithoutscroll(countingdata);

                        } else {

                            String Result = partsqty.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (partsqty.get(0).getMessage().toString().contains("Session")) {
                                Session = 7;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    } else {

                        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                        Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                        Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                        count_label_new.setVisibility(View.GONE);
                        count_label.setVisibility(View.VISIBLE);


                        loadDatawithoutscroll(Sessiondata.getInstance().getPartsquantity());

                        submit_value = 1;

                        ProgressBar.dismiss();

                        if (load_partsnotcounted == false) {
                            new AsyncPhysicalCountingListVarianceChk().execute();
                        }
                    }
                } else if (partsqty.size() == 0) {

                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.GONE);

                    submit_value = 0;
                    countingdata = new ArrayList<GetPartsQuantity>();

                    loadDatawithoutscroll(countingdata);

                    ProgressBar.dismiss();

                    if (load_partsnotcounted == false) {
                        new AsyncPhysicalCountingListVarianceChk().execute();
                    }

                } else {

                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                    Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());


                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.VISIBLE);

                    loadDatawithoutscroll(Sessiondata.getInstance().getPartsquantity());
                    submit_value = 1;

                    ProgressBar.dismiss();

                    if (load_partsnotcounted == false) {
                        new AsyncPhysicalCountingListVarianceChk().execute();
                    }

                }
            } else {
                count_label_new.setVisibility(View.GONE);
                count_label.setVisibility(View.GONE);

                countingdata = new ArrayList<GetPartsQuantity>();


                loadDatawithoutscroll(countingdata);

                if (Sessiondata.getInstance().getPartsquantity() != null) {
                    Sessiondata.getInstance().getPartsquantity().clear();
                }

                ProgressBar.dismiss();

                if (load_partsnotcounted == false) {
                    new AsyncPhysicalCountingListVarianceChk().execute();
                }

            }

        }
    }





    public class AsyncPhysicalCountingListSubmit extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("UserToken", "" + userToken);
                Log.d("branch", "" + branch_name);
                Log.d("startindex_listmore", "" + startindex_listmore);
                Log.d("endindex_listmore", "" + endindex_listmore);
                Log.d("partNum", "" + partno_submit);

                partsqty_submit = WebServiceConsumer.getInstance().GetpartsqtyV4(userToken, Processid, start_bin, end_bin, branch_name, partsNotCounted, startindex_listmore, endindex_listmore, partno_submit);

            } catch (SocketTimeoutException e) {

                partsqty_submit = null;
            } catch (Exception e) {
                partsqty_submit = null;
                e.printStackTrace();
            }


            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            load_btn_Value_HW = 0;
            Sessiondata.getInstance().setPartsquantity(partsqty_submit);

            Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
            Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
            Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
            Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
            Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

            Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));

            quanty = new ArrayList<>();
            partno_list = new ArrayList<>();
            mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getPartsquantity() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getPartsquantity().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getPartsquantity().get(i).getRowcnt();
                    Log.d("Resp_Rowcount_HW", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        quanty.add(Sessiondata.getInstance().getPartsquantity().get(i).getLoadQty());
                        partno_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getPartno());
                        mfg_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getMfr());
                    }
                }
            }


            if (partsqty_submit != null) {


                if (partsqty_submit.size() == 1) {

                    if (partsqty_submit.get(0).getMessage().length() != 0) {

                        ProgressBar.dismiss();

                        if (partsqty_submit.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            if (checkInternetConenction()) {
                                new AsyncGetmanufaturers().execute();
                            } else {
                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        } else if (partsqty_submit.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")) {
                            if (checkInternetConenction()) {
                                new AsyncGetmanufaturers().execute();
                            } else {
                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        } else if (partsqty_submit.get(0).getMessage().toString().equalsIgnoreCase("Procesd Id not found")) {

                            if (checkInternetConenction()) {
                                new AsyncGetmanufaturers().execute();
                            } else {
                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        } else if (partsqty_submit.get(0).getMessage().toString().contains("Parts number not found") ||
                                partsqty_submit.get(0).getMessage().toString().contains("Part number not found")) {
                            if (checkInternetConenction()) {
                                new AsyncGetmanufaturers().execute();
                            } else {
                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        } else if (partsqty_submit.get(0).getMessage().toString().contains("Data Conversion Error")) {
                            if (checkInternetConenction()) {
                                new AsyncGetmanufaturers().execute();
                            } else {
                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            String Result = partsqty_submit.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (partsqty_submit.get(0).getMessage().toString().contains("Session")) {
                                Session = 15;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    } else {

                        if (checkInternetConenction()) {
                            new AsyncGetmanufaturers().execute();
                        } else {
                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if (checkInternetConenction()) {
                        new AsyncGetmanufaturers().execute();
                    } else {
                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                if (checkInternetConenction()) {
                    new AsyncGetmanufaturers().execute();
                } else {
                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    public class AsyncPhysicalCountingListHW extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("UserToken", "" + userToken);
                Log.d("branch", "" + branch_name);
                Log.d("startindex_listmore", "" + startindex_listmore);
                Log.d("endindex_listmore", "" + endindex_listmore);
                Log.d("partNum", "" + partNum);

                partsqty_hw = WebServiceConsumer.getInstance().GetpartsqtyV4(userToken, Processid, start_bin, end_bin, branch_name, partsNotCounted, startindex_listmore, endindex_listmore, partNum);

            } catch (SocketTimeoutException e) {

                partsqty_hw = null;
            } catch (Exception e) {
                partsqty_hw = null;
                e.printStackTrace();
            }


            return null;
        }


        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            load_btn_Value_HW = 0;
            Sessiondata.getInstance().setPartsquantity(partsqty_hw);

            Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
            Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
            Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
            Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
            Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());


            Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));

            quanty = new ArrayList<>();
            partno_list = new ArrayList<>();
            mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getPartsquantity() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getPartsquantity().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getPartsquantity().get(i).getRowcnt();
                    Log.d("Resp_Rowcount_HW", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        quanty.add(Sessiondata.getInstance().getPartsquantity().get(i).getLoadQty());
                        partno_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getPartno());
                        mfg_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getMfr());
                    }
                }
            }


            if (partsqty_hw != null) {


                if (partsqty_hw.size() == 1) {

                    if (partsqty_hw.get(0).getMessage().length() != 0) {

                        ProgressBar.dismiss();

                        if (partsqty_hw.get(0).getMessage().toString().equalsIgnoreCase("Procesd Id not found")) {

                            Sessiondata.getInstance().setPartsquantity(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadDatawithoutscroll(countingdata);

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText("Procesd Id not found")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;
                                                sDialog.dismiss();
                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty_hw.get(0).getMessage().toString().length() != 0) {
                            if ((Dialog == null) || !Dialog.isShowing()) {
                                Dialog = new Dialog(Physical_counting_activity.this);
                                Dialog.setCanceledOnTouchOutside(false);
                                Dialog.setCancelable(false);
                                Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                Dialog.setContentView(R.layout.dialog_handwrites);


                                TextView textView_mgf = (TextView) Dialog.findViewById(R.id.textView_mgf);
                                textView_mgf.setTypeface(header_face);

                                dialog_mfg = (EditText) Dialog.findViewById(R.id.mfr);
                                dialog_mfg.setTypeface(txt_face);
                                ImageView img_mfg = (ImageView) Dialog.findViewById(R.id.img_mfr);




                                img_mfg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(partnum.getText().toString().length()!=0){
                                            new AsyncGetmanufaturersList().execute();
                                        }else{

                                            Toast.makeText(Physical_counting_activity.this,"Part # should not be Empty",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                                TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                                txt_header.setTypeface(header_face);
                                TextView dialog_txt_header = (TextView) Dialog.findViewById(R.id.dialog_txt_header);
                                dialog_txt_header.setTypeface(header_face);
                                partnum = (EditText) Dialog.findViewById(R.id.partnum);

                                partnum.setTypeface(txt_face);

                                partnum.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Sessiondata.getInstance().setScanner_partnumber(7);
                                        Sessiondata.getInstance().setPart_value("part");
                                        return false;
                                    }
                                });

                                ImageView scan_part = (ImageView) Dialog.findViewById(R.id.scan_part);

                                TextView dialog_social_submit = (TextView) Dialog.findViewById(R.id.dialog_submit);
                                dialog_social_submit.setTypeface(header_face);

                                TextView dialog_social_cancel = (TextView) Dialog.findViewById(R.id.dialog_social_cancel);
                                dialog_social_cancel.setTypeface(header_face);

                                scan_part.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {



                                        Sessiondata.getInstance().setScanner_partreceipt(0);
                                        Sessiondata.getInstance().setScanner_partreceiving(0);
                                        Sessiondata.getInstance().setScanner_replace(0);
                                        Sessiondata.getInstance().setScanner_counting1(0);
                                        Sessiondata.getInstance().setScanner_counting2(0);
                                        Sessiondata.getInstance().setScanner_inventory(0);
                                        Sessiondata.getInstance().setScanner_partno(0);

                                        Sessiondata.getInstance().setScanner_partnumber(7);
                                        Sessiondata.getInstance().setScanner_hwstartbin(0);
                                        Sessiondata.getInstance().setScanner_hwendbin(0);

                                        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                                        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                                        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                                        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                                        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                                        Sessiondata.getInstance().setCounting_partnum(partnum.getText().toString());

                                        Sessiondata.getInstance().setCount_value("");
                                        Sessiondata.getInstance().setPart_value("part");

                                        Sessiondata.getInstance().setLoad_value("Load");
                                        Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                                        Sessiondata.getInstance().setLoad_value_onresume("");
                                        Sessiondata.getInstance().setHw_value_notmatch("");

                                        launchActivity(SimpleScannerActivity.class);
                                    }
                                });

                                dialog_social_submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (allow_submit)
                                        {
                                            allow_submit = false;

                                            if (partnum.getText().toString().length() == 0) {


                                                if (Sweetalrt_list) {
                                                    Sweetalrt_list = false;

                                                    Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                            .setTitleText("Alert!")
                                                            .setContentText("Please enter the Part Number")
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

                                                    if (load_btn_Value_HW == 0) {

                                                        load_btn_Value_HW = 1;
                                                        partno_submit = partnum.getText().toString();

                                                        new AsyncPhysicalCountingListSubmit().execute();
                                                    }
                                                    Dialog.dismiss();

                                                } else {

                                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                                }


                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(Physical_counting_activity.this, "Please select the Manufacture", Toast.LENGTH_LONG).show();
                                        }



                                    }
                                });

                                dialog_social_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Dialog.dismiss();
                                    }
                                });

                                Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                                Dialog.show();


                            }
                        } else if (partsqty.get(0).getMessage().toString().contains("Parts number not found") ||
                                partsqty.get(0).getMessage().toString().contains("Part number not found")) {

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();
                            loadDatawithoutscroll(countingdata);
                        } else {

                            String Result = partsqty_hw.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (partsqty_hw.get(0).getMessage().toString().contains("Session")) {
                                Session = 6;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    } else {
                        if ((Dialog == null) || !Dialog.isShowing()) {
                            Dialog = new Dialog(Physical_counting_activity.this);
                            Dialog.setCanceledOnTouchOutside(false);
                            Dialog.setCancelable(false);
                            Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            Dialog.setContentView(R.layout.dialog_handwrites);


                            TextView textView_mgf = (TextView) Dialog.findViewById(R.id.textView_mgf);
                            textView_mgf.setTypeface(header_face);
                            dialog_mfg = (EditText) Dialog.findViewById(R.id.mfr);
                            dialog_mfg.setTypeface(txt_face);
                            ImageView img_mfg = (ImageView) Dialog.findViewById(R.id.img_mfr);




                            img_mfg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(partnum.getText().toString().length()!=0){
                                        new AsyncGetmanufaturersList().execute();
                                    }else{
                                        Toast.makeText(Physical_counting_activity.this,"Part # should not be Empty",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                            TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                            txt_header.setTypeface(header_face);
                            TextView dialog_txt_header = (TextView) Dialog.findViewById(R.id.dialog_txt_header);
                            dialog_txt_header.setTypeface(header_face);
                            partnum = (EditText) Dialog.findViewById(R.id.partnum);

                            partnum.setTypeface(txt_face);

                            partnum.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    Sessiondata.getInstance().setScanner_partnumber(7);
                                    Sessiondata.getInstance().setPart_value("part");
                                    return false;
                                }
                            });

                            ImageView scan_part = (ImageView) Dialog.findViewById(R.id.scan_part);

                            TextView dialog_social_submit = (TextView) Dialog.findViewById(R.id.dialog_submit);
                            dialog_social_submit.setTypeface(header_face);

                            TextView dialog_social_cancel = (TextView) Dialog.findViewById(R.id.dialog_social_cancel);
                            dialog_social_cancel.setTypeface(header_face);

                            scan_part.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Sessiondata.getInstance().setScanner_partreceipt(0);
                                    Sessiondata.getInstance().setScanner_partreceiving(0);
                                    Sessiondata.getInstance().setScanner_replace(0);
                                    Sessiondata.getInstance().setScanner_counting1(0);
                                    Sessiondata.getInstance().setScanner_counting2(0);
                                    Sessiondata.getInstance().setScanner_inventory(0);
                                    Sessiondata.getInstance().setScanner_partno(0);

                                    Sessiondata.getInstance().setScanner_partnumber(7);
                                    Sessiondata.getInstance().setScanner_hwstartbin(0);
                                    Sessiondata.getInstance().setScanner_hwendbin(0);

                                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                                    Sessiondata.getInstance().setCounting_partnum(partnum.getText().toString());

                                    Sessiondata.getInstance().setCount_value("");
                                    Sessiondata.getInstance().setPart_value("part");

                                    Sessiondata.getInstance().setLoad_value("Load");
                                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                                    Sessiondata.getInstance().setLoad_value_onresume("");
                                    Sessiondata.getInstance().setHw_value_notmatch("");

                                    launchActivity(SimpleScannerActivity.class);
                                }
                            });

                            dialog_social_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (allow_submit)
                                    {
                                        allow_submit = false;

                                        if (partnum.getText().toString().length() == 0) {


                                            if (Sweetalrt_list) {
                                                Sweetalrt_list = false;

                                                Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                        .setTitleText("Alert!")
                                                        .setContentText("Please enter the Part Number")
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

                                                if (load_btn_Value_HW == 0) {

                                                    load_btn_Value_HW = 1;
                                                    partno_submit = partnum.getText().toString();
                                                    new AsyncPhysicalCountingListSubmit().execute();
                                                }
                                                Dialog.dismiss();

                                            } else {

                                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                            }


                                        }

                                    }
                                    else
                                    {
                                        Toast.makeText(Physical_counting_activity.this, "Please select the Manufacture", Toast.LENGTH_LONG).show();
                                    }


                                }
                            });

                            dialog_social_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Dialog.dismiss();
                                }
                            });

                            Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                            Dialog.show();


                        }

                        ProgressBar.dismiss();
                    }
                } else {
                    if ((Dialog == null) || !Dialog.isShowing()) {
                        Dialog = new Dialog(Physical_counting_activity.this);
                        Dialog.setCanceledOnTouchOutside(false);
                        Dialog.setCancelable(false);
                        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        Dialog.setContentView(R.layout.dialog_handwrites);

                        TextView textView_mgf = (TextView) Dialog.findViewById(R.id.textView_mgf);
                        textView_mgf.setTypeface(header_face);

                        dialog_mfg = (EditText) Dialog.findViewById(R.id.mfr);
                        dialog_mfg.setTypeface(txt_face);
                        ImageView img_mfg = (ImageView) Dialog.findViewById(R.id.img_mfr);




                        img_mfg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(partnum.getText().toString().length()!=0){
                                    new AsyncGetmanufaturersList().execute();
                                }else{
                                    Toast.makeText(Physical_counting_activity.this,"Part # should not be Empty",Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                        TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                        txt_header.setTypeface(header_face);
                        TextView dialog_txt_header = (TextView) Dialog.findViewById(R.id.dialog_txt_header);
                        dialog_txt_header.setTypeface(header_face);
                        partnum = (EditText) Dialog.findViewById(R.id.partnum);

                        partnum.setTypeface(txt_face);

                        partnum.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                Sessiondata.getInstance().setScanner_partnumber(7);
                                Sessiondata.getInstance().setPart_value("part");
                                return false;
                            }
                        });

                        ImageView scan_part = (ImageView) Dialog.findViewById(R.id.scan_part);

                        TextView dialog_social_submit = (TextView) Dialog.findViewById(R.id.dialog_submit);
                        dialog_social_submit.setTypeface(header_face);

                        TextView dialog_social_cancel = (TextView) Dialog.findViewById(R.id.dialog_social_cancel);
                        dialog_social_cancel.setTypeface(header_face);

                        scan_part.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Sessiondata.getInstance().setScanner_partreceipt(0);
                                Sessiondata.getInstance().setScanner_partreceiving(0);
                                Sessiondata.getInstance().setScanner_replace(0);
                                Sessiondata.getInstance().setScanner_counting1(0);
                                Sessiondata.getInstance().setScanner_counting2(0);
                                Sessiondata.getInstance().setScanner_inventory(0);
                                Sessiondata.getInstance().setScanner_partno(0);

                                Sessiondata.getInstance().setScanner_partnumber(7);
                                Sessiondata.getInstance().setScanner_hwstartbin(0);
                                Sessiondata.getInstance().setScanner_hwendbin(0);

                                Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                                Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                                Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                                Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                                Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                                Sessiondata.getInstance().setCounting_partnum(partnum.getText().toString());

                                Sessiondata.getInstance().setCount_value("");
                                Sessiondata.getInstance().setPart_value("part");

                                Sessiondata.getInstance().setLoad_value("Load");
                                Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                                Sessiondata.getInstance().setLoad_value_onresume("");
                                Sessiondata.getInstance().setHw_value_notmatch("");

                                launchActivity(SimpleScannerActivity.class);
                            }
                        });

                        dialog_social_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (allow_submit)
                                {
                                    allow_submit = false;

                                    if (partnum.getText().toString().length() == 0) {


                                        if (Sweetalrt_list) {
                                            Sweetalrt_list = false;

                                            Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Please enter the Part Number")
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

                                            if (load_btn_Value_HW == 0) {

                                                load_btn_Value_HW = 1;
                                                partno_submit = partnum.getText().toString();
                                                new AsyncPhysicalCountingListSubmit().execute();
                                            }
                                            Dialog.dismiss();

                                        } else {

                                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                        }


                                    }

                                }
                                else
                                {
                                    Toast.makeText(Physical_counting_activity.this, "Please select the Manufacture", Toast.LENGTH_LONG).show();
                                }


                            }
                        });

                        dialog_social_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog.dismiss();
                            }
                        });

                        Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        Dialog.show();


                    }

                    ProgressBar.dismiss();

                }
            } else {
                if ((Dialog == null) || !Dialog.isShowing()) {
                    Dialog = new Dialog(Physical_counting_activity.this);
                    Dialog.setCanceledOnTouchOutside(false);
                    Dialog.setCancelable(false);
                    Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Dialog.setContentView(R.layout.dialog_handwrites);

                    TextView textView_mgf = (TextView) Dialog.findViewById(R.id.textView_mgf);
                    textView_mgf.setTypeface(header_face);

                    dialog_mfg = (EditText) Dialog.findViewById(R.id.mfr);
                    dialog_mfg.setTypeface(txt_face);
                    ImageView img_mfg = (ImageView) Dialog.findViewById(R.id.img_mfr);




                    img_mfg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(partnum.getText().toString().length()!=0){
                                new AsyncGetmanufaturersList().execute();
                            }else{
                                Toast.makeText(Physical_counting_activity.this,"Part # should not be Empty",Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                    TextView txt_header = (TextView) Dialog.findViewById(R.id.txt_header);
                    txt_header.setTypeface(header_face);
                    TextView dialog_txt_header = (TextView) Dialog.findViewById(R.id.dialog_txt_header);
                    dialog_txt_header.setTypeface(header_face);
                    partnum = (EditText) Dialog.findViewById(R.id.partnum);

                    partnum.setTypeface(txt_face);

                    partnum.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            Sessiondata.getInstance().setScanner_partnumber(7);
                            Sessiondata.getInstance().setPart_value("part");
                            return false;
                        }
                    });

                    ImageView scan_part = (ImageView) Dialog.findViewById(R.id.scan_part);

                    TextView dialog_social_submit = (TextView) Dialog.findViewById(R.id.dialog_submit);
                    dialog_social_submit.setTypeface(header_face);

                    TextView dialog_social_cancel = (TextView) Dialog.findViewById(R.id.dialog_social_cancel);
                    dialog_social_cancel.setTypeface(header_face);

                    scan_part.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Sessiondata.getInstance().setScanner_partreceipt(0);
                            Sessiondata.getInstance().setScanner_partreceiving(0);
                            Sessiondata.getInstance().setScanner_replace(0);
                            Sessiondata.getInstance().setScanner_counting1(0);
                            Sessiondata.getInstance().setScanner_counting2(0);
                            Sessiondata.getInstance().setScanner_inventory(0);
                            Sessiondata.getInstance().setScanner_partno(0);

                            Sessiondata.getInstance().setScanner_partnumber(7);
                            Sessiondata.getInstance().setScanner_hwstartbin(0);
                            Sessiondata.getInstance().setScanner_hwendbin(0);

                            Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                            Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                            Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                            Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                            Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                            Sessiondata.getInstance().setCounting_partnum(partnum.getText().toString());

                            Sessiondata.getInstance().setCount_value("");
                            Sessiondata.getInstance().setPart_value("part");

                            Sessiondata.getInstance().setLoad_value("Load");
                            Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                            Sessiondata.getInstance().setLoad_value_onresume("");
                            Sessiondata.getInstance().setHw_value_notmatch("");

                            launchActivity(SimpleScannerActivity.class);
                        }
                    });

                    dialog_social_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (allow_submit)
                            {
                                allow_submit  = false;

                                if (partnum.getText().toString().length() == 0) {


                                    if (Sweetalrt_list) {
                                        Sweetalrt_list = false;

                                        Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                .setTitleText("Alert!")
                                                .setContentText("Please enter the Part Number")
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

                                        if (load_btn_Value_HW == 0) {

                                            load_btn_Value_HW = 1;
                                            partno_submit = partnum.getText().toString();

                                            new AsyncPhysicalCountingListSubmit().execute();
                                        }
                                        Dialog.dismiss();

                                    } else {

                                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                    }


                                }

                            }
                            else
                            {
                                Toast.makeText(Physical_counting_activity.this, "Please select the Manufacture", Toast.LENGTH_LONG).show();
                            }


                        }
                    });

                    dialog_social_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Dialog.dismiss();
                        }
                    });

                    Dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    Dialog.show();


                }

                ProgressBar.dismiss();

            }

        }
    }

    public class AsyncGetDealerBranch extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + usertoken);

                dealerbranch = WebServiceConsumer.getInstance().GetBranchV2(
                        usertoken, "P");

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
                        if (dealerbranch.get(0).getMessage().toString().contains("Session")) {
                            Session = 8;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }

                        }
                    } else {
                        ProgressBar.dismiss();
                        if (mDialog == null) {
                            mDialog = new Dialog(Physical_counting_activity.this);
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.setCancelable(false);
                            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            mDialog.setContentView(R.layout.dialog_list_select);

                            TextView txt_header = (TextView) mDialog.findViewById(R.id.txt_header);
                            txt_header.setText("Select Branch");
                            txt_header.setTypeface(header_face);

                            ListView list = (ListView) mDialog.findViewById(R.id.list);

                            branchlist = new ArrayList<>();

                            branchlist = Sessiondata.getInstance().getDealerbranch();

                            adapter_branch = new CustomAdapter_Branch(Physical_counting_activity.this, branchlist);
                            list.setAdapter(adapter_branch);

                            TextView dialog_social_ok = (TextView) mDialog.findViewById(R.id.dialog_social_ok);
                            dialog_social_ok.setTypeface(header_face);

                            dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                }
                            });

                            mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                            mDialog.show();
                        } else if (!mDialog.isShowing()) {
                            mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                            mDialog.show();
                        }

                    }
                } else {
                    ProgressBar.dismiss();
                    if (mDialog == null) {
                        mDialog = new Dialog(Physical_counting_activity.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialog.setContentView(R.layout.dialog_list_select);

                        TextView txt_header = (TextView) mDialog.findViewById(R.id.txt_header);
                        txt_header.setText("Select Branch");
                        txt_header.setTypeface(header_face);

                        ListView list = (ListView) mDialog.findViewById(R.id.list);

                        branchlist = new ArrayList<>();

                        branchlist = Sessiondata.getInstance().getDealerbranch();

                        adapter_branch = new CustomAdapter_Branch(Physical_counting_activity.this, branchlist);
                        list.setAdapter(adapter_branch);

                        TextView dialog_social_ok = (TextView) mDialog.findViewById(R.id.dialog_social_ok);
                        dialog_social_ok.setTypeface(header_face);

                        dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        });

                        mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        mDialog.show();
                    } else if (!mDialog.isShowing()) {
                        mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        mDialog.show();
                    }

                }

            } else {
                ProgressBar.dismiss();
                if (mDialog == null) {
                    mDialog = new Dialog(Physical_counting_activity.this);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialog.setContentView(R.layout.dialog_list_select);

                    TextView txt_header = (TextView) mDialog.findViewById(R.id.txt_header);
                    txt_header.setText("Select Branch");
                    txt_header.setTypeface(header_face);

                    ListView list = (ListView) mDialog.findViewById(R.id.list);

                    branchlist = new ArrayList<>();

                    branchlist = Sessiondata.getInstance().getDealerbranch();

                    adapter_branch = new CustomAdapter_Branch(Physical_counting_activity.this, branchlist);
                    list.setAdapter(adapter_branch);

                    TextView dialog_social_ok = (TextView) mDialog.findViewById(R.id.dialog_social_ok);
                    dialog_social_ok.setTypeface(header_face);

                    dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });

                    mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    mDialog.show();
                } else if (!mDialog.isShowing()) {
                    mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    mDialog.show();
                }

            }
        }
    }

    public class AsyncPhysicalCountingListScrollNew extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
            scroll = false;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("UserToken", "" + userToken);
                Log.d("branch", "" + branch_name);
                Log.d("startindex_listmore", "" + startindex_listmore);
                Log.d("endindex_listmore", "" + endindex_listmore);

                int load_processid = Sessiondata.getInstance().getLoad_processid();
                String load_startbin = Sessiondata.getInstance().getLoad_startbin();
                String load_endbin = Sessiondata.getInstance().getLoad_endbin();
                load_partsnotcounted = Sessiondata.getInstance().getLoaded_partsnotcounted();
                String load_branch = Sessiondata.getInstance().getLoad_branch();
                String load_partNum = Sessiondata.getInstance().getLoad_partNum();
                Log.d("load_partNum", "" + load_partNum);

                partsqty = WebServiceConsumer.getInstance().GetpartsqtyV4(userToken, load_processid, load_startbin, load_endbin, load_branch, load_partsnotcounted, startindex_listmore, endindex_listmore, load_partNum);

            } catch (SocketTimeoutException e) {

                partsqty = null;
            } catch (Exception e) {
                partsqty = null;
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            scroll = true;

            submit_btn_value = 0;
            load_btn_Value = 0;
            Sessiondata.getInstance().setPartsquantity(partsqty);


            if (partsqty != null) {
                for (int i = 0; i < partsqty.size(); i++) {

                    int rowcount = partsqty.get(i).getRowcnt();
                    Log.d("Resp_Rowcount_Scrollnew", "" + rowcount);
                    if (rowcount != 0) {
                        partsqtyscroll.add(partsqty.get(i));

                        Log.d("Scrool_Description", "" + partsqtyscroll.get(i).getDescription());
                        Log.d("Scrool_LoadQty", "" + partsqtyscroll.get(i).getLoadQty());
                        Log.d("Scrool_Partno", "" + partsqtyscroll.get(i).getPartno());
                        Log.d("Scrool_mfr", "" + partsqtyscroll.get(i).getMfr());
                        Log.d("Scrool_pbin", "" + partsqtyscroll.get(i).getPrimaryBinLocation());
                        Log.d("Scrool_sbin", "" + partsqtyscroll.get(i).getSecondaryBinLocation());
                        Log.d("Scrool_OnHandQty", "" + partsqtyscroll.get(i).getOnHandQty());
                        Log.d("Scrool_Rowcnt", "" + partsqtyscroll.get(i).getRowcnt());
                        Log.d("Scrool_Qty", "" + partsqtyscroll.get(i).getQty());
                        Log.d("Scrool_Rownum", "" + partsqtyscroll.get(i).getRownum());
                    }
                }
            }


            quanty = new ArrayList<>();
            partno_list = new ArrayList<>();
            mfg_list = new ArrayList<>();

            if (Sessiondata.getInstance().getPartsquantity() != null) {
                for (int i = 0; i < Sessiondata.getInstance().getPartsquantity().size(); i++) {
                    int Session_rowcount = Sessiondata.getInstance().getPartsquantity().get(i).getRowcnt();
                    Log.d("Resp_Rowcount_Scrollnew", "" + Session_rowcount);
                    if (Session_rowcount != 0) {
                        quanty.add(Sessiondata.getInstance().getPartsquantity().get(i).getLoadQty());
                        partno_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getPartno());
                        mfg_list.add(Sessiondata.getInstance().getPartsquantity().get(i).getMfr());
                    }
                }
            }


            if (partsqty != null) {


                if (partsqty.size() == 1) {

                    if (partsqty.get(0).getMessage().length() != 0) {

                        ProgressBar.dismiss();

                        if (partsqty.get(0).getMessage().toString().contains("All Parts Counted for this Process Id")) {
                            if (partsNotCounted == true) {

                                Sessiondata.getInstance().setLoad_check_enable(1);
                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                count_label_new.setVisibility(View.VISIBLE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                countingdata = new ArrayList<GetPartsQuantity>();

                                loadData(countingdata);
                            } else {
                                Sessiondata.getInstance().setPartsquantity(null);

                                count_label_new.setVisibility(View.GONE);
                                count_label.setVisibility(View.GONE);

                                submit_value = 0;

                                countingdata = new ArrayList<GetPartsQuantity>();


                                loadData(countingdata);

                            }
                        } else if (partsqty.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")) {
                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadData(countingdata);
                            String processvalues = String.valueOf(process_no.getText().toString());

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText("Bin Location not included for PID " + processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;
                                                sDialog.dismiss();
                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty.get(0).getMessage().toString().contains("Procesd Id not found")) {
                            Sessiondata.getInstance().setPartsquantity(null);

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadData(countingdata);

                            String processvalues = partsqty.get(0).getMessage().toString();

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Message!")
                                        .setContentText(processvalues)
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                if (Sessiondata.getInstance().getPartsquantity() != null) {
                                                    Sessiondata.getInstance().getPartsquantity().clear();
                                                }
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;
                                                sDialog.dismiss();
                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else if (partsqty.get(0).getMessage().toString().contains("Parts number not found") ||
                                partsqty.get(0).getMessage().toString().contains("Part number not found")) {

                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();
                            loadDatawithoutscroll(countingdata);

                            if (!partno.getText().toString().isEmpty()) {
                                if (sweetbranch) {
                                    sweetbranch = false;
                                    dialogbranch = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Message!")
                                            .setContentText("Part is not on PID. Would you like to add it and update the count?")
                                            .setCancelText("Cancel")
                                            .setConfirmText("Ok")
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

                                                    Sessiondata.getInstance().setHw_value_notmatch("hwpart_id");

                                                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                                                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                                                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                                                    Log.d("process_no_1NM", "" + Sessiondata.getInstance().getCounting_processid());
                                                    Log.d("starting_bin_1NM", "" + Sessiondata.getInstance().getCounting_startbin());
                                                    Log.d("ending_bin_1NM", "" + Sessiondata.getInstance().getCounting_endbin());

                                                    Sessiondata.getInstance().setLoad_value("Load");
                                                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                                                    Sessiondata.getInstance().setLoad_value_onresume("");

                                                    sDialog.dismiss();

                                                    Intent intent = new Intent(Physical_counting_activity.this, activity_handwrites_notmatch.class);
                                                    intent.putExtra("process_Id", process_no.getText().toString());
                                                    intent.putExtra("part_number", partno.getText().toString());
                                                    intent.putExtra("primary_bin", starting_bin.getText().toString());
                                                    intent.putExtra("secondary_bin", ending_bin.getText().toString());
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                                                }
                                            });
                                    dialogbranch.setCancelable(false);
                                    dialogbranch.show();
                                }
                            } else {

                                String message = partsqty.get(0).getMessage().toString();

                                if (Sweetalrt_list) {
                                    Sweetalrt_list = false;

                                    Sweetalt_list = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText(message)
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
                        } else if (partsqty.get(0).getMessage().toString().contains("Data Conversion Error")) {
                            count_label_new.setVisibility(View.GONE);
                            count_label.setVisibility(View.GONE);

                            submit_value = 0;
                            countingdata = new ArrayList<GetPartsQuantity>();

                            loadData(countingdata);

                            if (sweet_load) {
                                sweet_load = false;

                                sweet_load_alert = new SweetAlertDialog(Physical_counting_activity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Data Conversion Error")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {
                                                Sessiondata.getInstance().setLoad_check_enable(0);
                                                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                                                sweet_load = true;
                                                sDialog.dismiss();
                                            }
                                        });
                                sweet_load_alert.setCancelable(false);
                                sweet_load_alert.show();
                            }
                        } else {

                            String Result = partsqty.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (partsqty.get(0).getMessage().toString().contains("Session")) {
                                Session = 5;
                                if (checkInternetConenction()) {

                                    new AsyncSessionLoginTask().execute();

                                } else {

                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    } else {
                        Sessiondata.getInstance().setLoad_check_enable(1);
                        Sessiondata.getInstance().setLoad_checkvar_enable(0);


                        Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());


                        count_label_new.setVisibility(View.GONE);
                        count_label.setVisibility(View.VISIBLE);

                        loadData(partsqtyscroll);

                        submit_value = 1;

                        ProgressBar.dismiss();
                    }
                } else if (partsqty.size() == 0) {

                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.GONE);

                    submit_value = 0;
                    countingdata = new ArrayList<GetPartsQuantity>();

                    loadData(countingdata);

                    ProgressBar.dismiss();

                } else {
                    Sessiondata.getInstance().setLoad_check_enable(1);
                    Sessiondata.getInstance().setLoad_checkvar_enable(0);

                    Sessiondata.getInstance().setLoad_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setLoad_partNum(partno.getText().toString());

                    count_label_new.setVisibility(View.GONE);
                    count_label.setVisibility(View.VISIBLE);

                    loadData(partsqtyscroll);
                    submit_value = 1;

                    ProgressBar.dismiss();

                }
            } else {
                Sessiondata.getInstance().setLoad_check_enable(0);
                Sessiondata.getInstance().setLoad_checkvar_enable(0);

                count_label_new.setVisibility(View.GONE);
                count_label.setVisibility(View.GONE);

                countingdata = new ArrayList<GetPartsQuantity>();

                loadData(countingdata);
                ProgressBar.dismiss();

            }

        }
    }

    public class AsyncSessionLoginTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Physical_counting_activity.this);
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

                    if (mDialogmsg == null) {
                        mDialogmsg = new Dialog(Physical_counting_activity.this);
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
                                Intent intent = new Intent(Physical_counting_activity.this, LoginActivity.class);
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

                    } else if (!mDialogmsg.isShowing()) {
                        mDialogmsg.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        mDialogmsg.show();
                    }

                } else {
                    ProgressBar.dismiss();
                    if (Session == 0) {
                        if (checkInternetConenction()) {

                            new AsyncGetProcessId().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 1) {
                        if (checkInternetConenction()) {

                            new AsyncGetBinLocation().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 2) {
                        if (checkInternetConenction()) {

                            new AsyncPhysicalCountingList().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 3) {
                        if (checkInternetConenction()) {

                            if (submit_btn_value == 0) {
                                submit_btn_value = 1;
                                new AsyncSetPartsQuantity().execute();
                            }

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 4) {
                        if (checkInternetConenction()) {

                            if (load_btn_Value_HW == 0) {

                                load_btn_Value_HW = 1;
                                partno_submit = partnum.getText().toString();
                                new AsyncPhysicalCountingListSubmit().execute();
                            }

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 5) {
                        if (checkInternetConenction()) {

                            if (submit_btn_value == 0) {
                                submit_btn_value = 1;
                                new AsyncPhysicalCountingListScrollNew().execute();
                            }


                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 6) {
                        if (checkInternetConenction()) {

                            new AsyncPhysicalCountingListHW().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 7) {
                        if (checkInternetConenction()) {

                            if (load_btn_Value == 0) {

                                load_btn_Value = 1;
                                new AsyncPhysicalCountingListOnResume().execute();
                            }

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 8) {
                        if (checkInternetConenction()) {

                            new AsyncGetDealerBranch().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 9) {
                        if (checkInternetConenction()) {

                            new AsyncPhysicalCountingListVarianceChk().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 10) {
                        if (checkInternetConenction()) {

                            new AsyncSetPartsQuantityV3().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 11) {
                        if (checkInternetConenction()) {

                            new AsyncPhysicalCountingListFilter().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 12) {
                        if (checkInternetConenction()) {

                            new AsyncGetVarianceList().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 13) {
                        if (checkInternetConenction()) {

                            new AsyncGetVarianceListOnResume().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 14) {
                        if (checkInternetConenction()) {
                            if (submit_btn_value == 0) {
                                submit_btn_value = 1;
                                new AsyncSetPartsQuantityVariance().execute();
                            }
                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 15) {
                        if (checkInternetConenction()) {

                            new AsyncPhysicalCountingListSubmit().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 16) {
                        if (checkInternetConenction()) {

                            new AsyncGetProcessIdFilter().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 17) {
                        if (checkInternetConenction()) {

                            new AsyncPhysicalCountingUncount().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 18) {
                        if (checkInternetConenction()) {

                            new AsyncGetProcessIdmoreFilter().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 19) {
                        if (checkInternetConenction()) {

                            if (submit_btn_value == 0) {
                                submit_btn_value = 1;
                                new AsyncGetVarianceListScroll().execute();
                            }

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 20) {
                        if (checkInternetConenction()) {

                            if (submit_btn_value == 0) {
                                submit_btn_value = 1;
                                new AsyncPhysicalCountingListFilter_Part().execute();
                            }

                        }


                        else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }



                    }

                    else if (Session == 21) {
                        if (checkInternetConenction()) {

                            new AsyncGetmanufaturersList().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }

                    else if (Session == 22) {
                        if (checkInternetConenction()) {

                            new AsyncGetBinLocationDialog().execute();

                        } else {

                            Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }



                }
            } else {

                if (mDialognodata == null) {
                    mDialognodata = new Dialog(Physical_counting_activity.this);
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
                } else if (!mDialognodata.isShowing()) {
                    mDialognodata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                    mDialognodata.show();
                }

            }
        }
    }

    public class AsyncGetmanufaturers extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("UserToken", "" + userToken);
                Log.d("Get_branch", "" + branch_name);
                processidmfg = Integer.parseInt(process_no.getText().toString());
                String part_mgf = partnum.getText().toString();
                branch_mfg = branch_name;


                manufacturerses = WebServiceConsumer.getInstance().GetManufacturerses(userToken, processidmfg, part_mgf, branch_mfg);

            } catch (SocketTimeoutException e) {

                manufacturerses = null;
            } catch (Exception e) {
                manufacturerses = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setGetManufacturerses(manufacturerses);
            ProgressBar.dismiss();


            if (manufacturerses != null) {

                if (manufacturerses.size() == 0) {
                    Sessiondata.getInstance().setHw_value_notmatch("hwpart_id");

                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());


                    Log.d("process_no_1NM", "" + Sessiondata.getInstance().getCounting_processid());
                    Log.d("starting_bin_1NM", "" + Sessiondata.getInstance().getCounting_startbin());
                    Log.d("ending_bin_1NM", "" + Sessiondata.getInstance().getCounting_endbin());

                    Sessiondata.getInstance().setLoad_value("Load");
                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                    Sessiondata.getInstance().setLoad_value_onresume("");

                    Intent intent = new Intent(Physical_counting_activity.this, activity_handwrites_notmatch.class);
                    intent.putExtra("process_Id", process_no.getText().toString());
                    intent.putExtra("part_number", partnum.getText().toString());
                    intent.putExtra("primary_bin", starting_bin.getText().toString());
                    intent.putExtra("secondary_bin", ending_bin.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);


                } else if (manufacturerses.size() == 1) {


                    if (manufacturerses.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = manufacturerses.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (manufacturerses.get(0).getMessage().toString().contains("Session")) {
                            Session = 4;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        }
                    } else {

                        Sessiondata.getInstance().setProcessid_match(process_no.getText().toString());
                        Sessiondata.getInstance().setPart_match(partnum.getText().toString());
                        Sessiondata.getInstance().setBranch_mfg(branch_mfg);

                        Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                        Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                        Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                        Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                        Log.d("process_no_1M", "" + Sessiondata.getInstance().getCounting_processid());
                        Log.d("starting_bin_1M", "" + Sessiondata.getInstance().getCounting_startbin());
                        Log.d("ending_bin_1M", "" + Sessiondata.getInstance().getCounting_endbin());

                        Sessiondata.getInstance().setLoad_value("Load");
                        Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                        Sessiondata.getInstance().setLoad_value_onresume("");

                        Intent intent = new Intent(Physical_counting_activity.this, activity_handwrites_match.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                    }
                } else {
                    Sessiondata.getInstance().setProcessid_match(process_no.getText().toString());
                    Sessiondata.getInstance().setPart_match(partnum.getText().toString());
                    Sessiondata.getInstance().setBranch_mfg(branch_mfg);

                    Sessiondata.getInstance().setCounting_branchname(branch_no.getText().toString());
                    Sessiondata.getInstance().setCounting_processid(process_no.getText().toString());
                    Sessiondata.getInstance().setCounting_endbin(ending_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_startbin(starting_bin.getText().toString());
                    Sessiondata.getInstance().setCounting_partnew(partno.getText().toString());

                    Log.d("process_no_1M", "" + Sessiondata.getInstance().getCounting_processid());
                    Log.d("starting_bin_1M", "" + Sessiondata.getInstance().getCounting_startbin());
                    Log.d("ending_bin_1M", "" + Sessiondata.getInstance().getCounting_endbin());

                    Sessiondata.getInstance().setLoad_value("Load");
                    Sessiondata.getInstance().setLoad_processid(Integer.parseInt(process_no.getText().toString()));
                    Sessiondata.getInstance().setLoad_value_onresume("");

                    Intent intent = new Intent(Physical_counting_activity.this, activity_handwrites_match.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                }


            } else {


            }

        }
    }




    public class AsyncGetmanufaturersList extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                branch_name = Sessiondata.getInstance().getCounting_branchno();
                Log.d("UserToken", "" + userToken);
                Log.d("Get_branch", "" + branch_name);
                processidmfg = Integer.parseInt(process_no.getText().toString());
                String part_mgf = partnum.getText().toString();
                branch_mfg = branch_name;


                manufacturerses = WebServiceConsumer.getInstance().GetManufacturerses(userToken, processidmfg, part_mgf, branch_mfg);

            } catch (SocketTimeoutException e) {

                manufacturerses = null;
            } catch (Exception e) {
                manufacturerses = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Sessiondata.getInstance().setGetManufacturerses(manufacturerses);
            ProgressBar.dismiss();


            if (manufacturerses != null) {

                if (manufacturerses.size() == 0) {


                    Toast.makeText(Physical_counting_activity.this,"No Manufacture for the selected Part",Toast.LENGTH_LONG).show();
                    allow_submit = true;

                } else if (manufacturerses.size() == 1) {


                    if (manufacturerses.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = manufacturerses.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (manufacturerses.get(0).getMessage().toString().contains("Session")) {
                            Session = 21;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        }
                    } else {

                        if ((mDialoglist == null) || !mDialoglist.isShowing()) {
                            mDialoglist = new Dialog(Physical_counting_activity.this);
                            mDialoglist.setCanceledOnTouchOutside(false);
                            mDialoglist.setCancelable(false);
                            mDialoglist.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            mDialoglist.setContentView(R.layout.dialog_list_select);

                            TextView txt_header = (TextView) mDialoglist.findViewById(R.id.txt_header);
                            TextView dialog_social_ok = (TextView) mDialoglist.findViewById(R.id.dialog_social_ok);
                            dialog_social_ok.setTypeface(header_face);
                            txt_header.setText("Select Manufaturers");
                            txt_header.setTypeface(header_face);

                            ListView list = (ListView) mDialoglist.findViewById(R.id.list);

                            int size = Sessiondata.getInstance().getGetManufacturerses().size();

                            mgf = new ArrayList<String>();

                            for (int i = 0; i < size; i++) {

                                mgf.add(Sessiondata.getInstance().getGetManufacturerses().get(i).getMfgName());

                            }

                            adapter_mfg = new Physical_counting_activity.CustomAdapter(Physical_counting_activity.this, mgf);
                            list.setAdapter(adapter_mfg);
                            justifyListViewHeightBasedOnChildren(list);


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
                    if ((mDialoglist == null) || !mDialoglist.isShowing()) {
                        mDialoglist = new Dialog(Physical_counting_activity.this);
                        mDialoglist.setCanceledOnTouchOutside(false);
                        mDialoglist.setCancelable(false);
                        mDialoglist.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialoglist.setContentView(R.layout.dialog_list_select);

                        TextView txt_header = (TextView) mDialoglist.findViewById(R.id.txt_header);
                        TextView dialog_social_ok = (TextView) mDialoglist.findViewById(R.id.dialog_social_ok);
                        dialog_social_ok.setTypeface(header_face);
                        txt_header.setText("Select Manufaturers");
                        txt_header.setTypeface(header_face);

                        ListView list = (ListView) mDialoglist.findViewById(R.id.list);

                        int size = Sessiondata.getInstance().getGetManufacturerses().size();

                        mgf = new ArrayList<String>();

                        for (int i = 0; i < size; i++) {

                            mgf.add(Sessiondata.getInstance().getGetManufacturerses().get(i).getMfgName());

                        }

                        adapter_mfg = new Physical_counting_activity.CustomAdapter(Physical_counting_activity.this, mgf);
                        list.setAdapter(adapter_mfg);
                        justifyListViewHeightBasedOnChildren(list);


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


            }

        }
    }

    class VarianceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private static final int QUANTITY = 5;
        boolean loadmore = true;
        UserViewHolder vh;
        private OnLoadMoreListener mOnLoadMoreListener;
        private List<GetVarianceList> contacts;
        private int lastVisibleItem, totalItemCount;
        private Activity activity;


        public VarianceAdapter(List<GetVarianceList> contacts, Activity activity) {
            this.contacts = contacts;
            this.activity = activity;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Physical_counting_activity.this).inflate(R.layout.activity_physical_counting_childrow, parent, false);
            return new UserViewHolder(view, new MyCustomEditTextListener());
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof UserViewHolder) {
                GetVarianceList partsqty = contacts.get(position);

                vh = (UserViewHolder) holder;

                if (partsqty != null) {
                    vh.quantity.setTag(contacts.get(position));
                    vh.part.setTag(contacts.get(position));
                    vh.onhandqty.setTag(contacts.get(position));
                    vh.startbinloc.setTag(contacts.get(position));
                    vh.endbinloc.setTag(contacts.get(position));
                    vh.description.setTag(contacts.get(position));
                    vh.mfr.setTag(contacts.get(position));

                    vh.quantityEditTextListener.updatePosition(vh.getLayoutPosition());

                    vh.part.setText(partsqty.getPartno());
                    vh.quantity.setText(partsqty.getLoadQty());
                    vh.onhandqty.setText(partsqty.getOnHandQty());
                    vh.startbinloc.setText(partsqty.getPrimaryBinLocation());
                    vh.endbinloc.setText(partsqty.getSecondaryBinLocation());
                    vh.description.setText(partsqty.getDescription());
                    vh.mfr.setText(partsqty.getMfr());
                    vh.onhandqty.setText(partsqty.getOnHandQty());

                    final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) physical_conting_list.getLayoutManager();

                    vh.count = linearLayoutManager.findLastVisibleItemPosition();

                    if (varianceAdapter != null) {
                        vh.tot_count = varianceAdapter.getItemCount();
                    }


                    if (Sessiondata.getInstance().getVarianceLists() != null) {
                        vh.counts = Sessiondata.getInstance().getVarianceLists().get(0).getRowcnt();
                    } else {
                        vh.counts = vh.tot_count;
                    }

                    Log.d("vh.tot_count", "" + vh.tot_count);
                    Log.d("vh.count", "" + vh.count);
                    Log.d("vh.counts", "" + vh.counts);
                    Log.d("vh.counts", " position " + position);

                    if (vh.counts == 1) {
                        vh.quantity.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    } else {
                        vh.quantity.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    }

                    vh.quantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            boolean handled = false;
                            if (actionId == KeyEvent.KEYCODE_CALL || actionId == KeyEvent.KEYCODE_ENDCALL) {
                                if (checkInternetConenction()) {
                                    if (submit_btn_value == 0) {
                                        submit_btn_value = 1;
                                        new AsyncSetPartsQuantityVariance().execute();
                                    }
                                } else {
                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }

                                if (vh.counts <= position + 2) {
                                    vh.quantity.setImeOptions(EditorInfo.IME_ACTION_DONE);
                                } else {
                                    vh.quantity.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                                }

                                handled = false;
                            }
                            return handled;

                        }
                    });


                }
            }

        }

        @Override
        public int getItemCount() {
            return contacts == null ? 0 : contacts.size();
        }


        class UserViewHolder extends RecyclerView.ViewHolder {

            public VarianceAdapter partsQty;
            public TextView part, description, mfr, startbinloc, endbinloc, onhandqty, desc_label, onhandqty_label;
            public EditText quantity;
            private MyCustomEditTextListener quantityEditTextListener;
            private int count = 0;
            private int tot_count = 0;
            private int counts = 0;


            public UserViewHolder(View convertView, MyCustomEditTextListener textListener) {
                super(convertView);

                part = (TextView) convertView.findViewById(R.id.part);
                quantity = (EditText) convertView.findViewById(R.id.quantity);
                description = (TextView) convertView.findViewById(R.id.description);
                mfr = (TextView) convertView.findViewById(R.id.mfr);
                onhandqty_label = (TextView) convertView.findViewById(R.id.Oh_label);
                onhandqty = (TextView) convertView.findViewById(R.id.onhandqty);
                startbinloc = (TextView) convertView.findViewById(R.id.startBinloc);
                endbinloc = (TextView) convertView.findViewById(R.id.endBinloc);
                onhandqty_label.setTypeface(header_face);
                onhandqty.setTypeface(header_face);
                startbinloc.setTypeface(header_face);
                endbinloc.setTypeface(header_face);

                part.setTypeface(header_face);
                quantity.setTypeface(txt_face);
                description.setTypeface(header_face);
                mfr.setTypeface(header_face);

                onhandqty_label.setVisibility(View.GONE);
                onhandqty.setVisibility(View.GONE);

                quantityEditTextListener = textListener;
                quantityEditTextListener.setEditTextType(QUANTITY);
                this.quantity.addTextChangedListener(quantityEditTextListener);
            }
        }


        private class MyCustomEditTextListener implements TextWatcher {
            private int position;
            private int type;

            public void updatePosition(int position) {
                this.position = position;

            }

            public void setEditTextType(int type) {
                this.type = type;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {

                Log.d("pos", "" + position);
                if (type == QUANTITY) {
                    if (s.toString().length() == 0) {
                        contacts.get(position).setLoadQty("");
                    } else {
                        contacts.get(position).setLoadQty(s.toString());
                    }
                    Log.d("Change ", "qty " + contacts.get(position).getLoadQty());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        }
    }


    class CountingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private static final int QUANTITY = 5;
        UserViewHolder vh;
        private OnLoadMoreListener mOnLoadMoreListener;
        private ArrayList<GetPartsQuantity> contacts;
        private Activity activity;


        public CountingAdapter(ArrayList<GetPartsQuantity> contacts, Activity activity) {
            this.contacts = contacts;
            this.activity = activity;

        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Physical_counting_activity.this).inflate(R.layout.activity_physical_counting_childrow, parent, false);
            return new UserViewHolder(view, new MyCustomEditTextListener());
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof UserViewHolder) {
                GetPartsQuantity partsqty = contacts.get(position);

                vh = (UserViewHolder) holder;

                if (partsqty != null) {

                    vh.quantity.setTag(contacts.get(position));
                    vh.part.setTag(contacts.get(position));
                    vh.onhandqty.setTag(contacts.get(position));
                    vh.startbinloc.setTag(contacts.get(position));
                    vh.endbinloc.setTag(contacts.get(position));
                    vh.description.setTag(contacts.get(position));
                    vh.mfr.setTag(contacts.get(position));

                    vh.quantityEditTextListener.updatePosition(vh.getLayoutPosition());


                    vh.part.setText(partsqty.getPartno());
                    vh.quantity.setText(partsqty.getLoadQty());
                    vh.onhandqty.setText(partsqty.getOnHandQty());
                    vh.startbinloc.setText(partsqty.getPrimaryBinLocation());
                    vh.endbinloc.setText(partsqty.getSecondaryBinLocation());
                    vh.description.setText(partsqty.getDescription());
                    vh.mfr.setText(partsqty.getMfr());
                    vh.onhandqty.setText(partsqty.getOnHandQty());


                    final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) physical_conting_list.getLayoutManager();

                    vh.count = linearLayoutManager.findLastVisibleItemPosition();

                    if (contactAdapter != null) {
                        vh.tot_count = contactAdapter.getItemCount();
                    }


                    if (Sessiondata.getInstance().getPartsquantity() != null) {
                        vh.counts = Sessiondata.getInstance().getPartsquantity().get(0).getRowcnt();
                    } else {
                        vh.counts = vh.tot_count;
                    }

                    Log.d("vh.tot_count", "" + vh.tot_count);
                    Log.d("vh.count", "" + vh.count);
                    Log.d("vh.counts", "" + vh.counts);
                    Log.d("vh.counts", " position " + position);

                    if (vh.counts == 1) {
                        vh.quantity.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    } else {
                        vh.quantity.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    }


                    vh.quantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            boolean handled = false;
                            if (actionId == KeyEvent.KEYCODE_CALL || actionId == KeyEvent.KEYCODE_ENDCALL) {
                                if (checkInternetConenction()) {
                                    if (submit_btn_value == 0) {
                                        submit_btn_value = 1;
                                        new AsyncSetPartsQuantity().execute();
                                    }
                                } else {
                                    Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                }

                                if (vh.counts <= position + 2) {
                                    vh.quantity.setImeOptions(EditorInfo.IME_ACTION_DONE);
                                } else {
                                    vh.quantity.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                                }

                                handled = false;
                            }
                            return handled;

                        }
                    });


                }

            }

        }

        @Override
        public int getItemCount() {
            return contacts == null ? 0 : contacts.size();
        }


        class UserViewHolder extends RecyclerView.ViewHolder {

            public GetPartsQuantity partsQty;
            public TextView part, description, mfr, startbinloc, endbinloc, onhandqty, desc_label, onhandqty_label;
            public EditText quantity;
            private MyCustomEditTextListener quantityEditTextListener;
            private int count = 0;
            private int tot_count = 0;
            private int counts = 0;


            public UserViewHolder(View convertView, MyCustomEditTextListener textListener) {
                super(convertView);

                part = (TextView) convertView.findViewById(R.id.part);
                quantity = (EditText) convertView.findViewById(R.id.quantity);
                description = (TextView) convertView.findViewById(R.id.description);
                mfr = (TextView) convertView.findViewById(R.id.mfr);

                onhandqty_label = (TextView) convertView.findViewById(R.id.Oh_label);
                onhandqty = (TextView) convertView.findViewById(R.id.onhandqty);
                startbinloc = (TextView) convertView.findViewById(R.id.startBinloc);
                endbinloc = (TextView) convertView.findViewById(R.id.endBinloc);

                onhandqty_label.setTypeface(header_face);
                onhandqty.setTypeface(header_face);
                startbinloc.setTypeface(header_face);
                endbinloc.setTypeface(header_face);

                part.setTypeface(header_face);
                quantity.setTypeface(txt_face);
                description.setTypeface(header_face);
                mfr.setTypeface(header_face);

                quantityEditTextListener = textListener;
                quantityEditTextListener.setEditTextType(QUANTITY);
                this.quantity.addTextChangedListener(quantityEditTextListener);
            }
        }


        private class MyCustomEditTextListener implements TextWatcher {
            private int position;
            private int type;

            public void updatePosition(int position) {
                this.position = position;

            }

            public void setEditTextType(int type) {
                this.type = type;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                Log.d("pos", "" + position);
                if (type == QUANTITY) {
                    if (s.toString().length() == 0) {
                        contacts.get(position).setLoadQty("");
                    } else {
                        contacts.get(position).setLoadQty(s.toString());
                    }
                    Log.d("Change ", "qty " + contacts.get(position).getLoadQty());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        }
    }


    public class CustomAdapter extends BaseAdapter {
        ArrayList<String> result;
        Context context;
        int[] imageId;
        private LayoutInflater inflater = null;

        public CustomAdapter(Context context, ArrayList<String> list) {
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
            TextView mfg,description;
            LinearLayout linear;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final CustomAdapter.Holder holder = new CustomAdapter.Holder();
            final View rowview_processid;

            rowview_processid = inflater.inflate(R.layout.activity_processid_childrow,null);

            holder.mfg = (TextView) rowview_processid.findViewById(R.id.process_list);
            holder.linear = (LinearLayout) rowview_processid.findViewById(R.id.linear);
            holder.mfg.setText(String.valueOf(result.get(position).toString()));
            Log.d("mfg", "" + holder.mfg.getText().toString());
            holder.mfg.setTypeface(txt_face);

            holder.mfg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkInternetConenction()) {

                        dialog_mfg.setText(result.get(position).toString());

                        Sessiondata.getInstance().setMfg(result.get(position));


                        mDialoglist.dismiss();

                        new AsyncGetBinLocationDialog().execute();

                    } else {

                        Toast.makeText(Physical_counting_activity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                    }


                }
            });



            return rowview_processid;
        }
    }
}