package com.ebs.ecount.parts_receipt;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.cognex.mobile.barcode.sdk.ReaderDevice.OnConnectionCompletedListener;
import com.cognex.mobile.barcode.sdk.ReaderDevice.ReaderDeviceListener;
import com.ebs.ecount.R;
import com.ebs.ecount.initial.LoginActivity;
import com.ebs.ecount.objects.GetPartsDetails;
import com.ebs.ecount.objects.GetPmPartDetails;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.objects.PartObject;
import com.ebs.ecount.objects.PoObject;
import com.ebs.ecount.objects.ValidateParts;
import com.ebs.ecount.uidesigns.ProgressBar;
import com.ebs.ecount.uidesigns.SimpleScannerActivity;
import com.ebs.ecount.uidesigns.SweetAlertDialog;
import com.ebs.ecount.uidesigns.fonts.RobotoTextView;
import com.ebs.ecount.utils.Sessiondata;
import com.ebs.ecount.webutils.WebServiceConsumer;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by techunity on 22/11/16.
 */
public class ReplacePartsActivity extends Activity implements OnConnectionCompletedListener, ReaderDeviceListener {

    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private static final DeviceType[] deviceTypeValues = DeviceType.values();
    public static ReaderDevice readerDevice;
    public static boolean isDevicePicked = false;
    public static boolean dialogAppeared = false;
    public static String selectedDevice = "";
    public static boolean fragmentActive = false;
    Button back, btn_replace;
    Dialog mDialogwarning;
    Dialog partandorderDialog, orderdialog;
    ArrayList<PartObject> replacedata;
    Boolean sweetalrt = true;
    Boolean sweetreplace = true;
    SweetAlertDialog sweetdialog, sweetalt;
    int dummyvalue;
    int Session = 0;
    LoginObject loginObject = null;
    Dialog mDialogSession;
    Dialog mDialognodata;
    boolean sweetalrtsuccess = true;
    ImageView bin_search;
    String str_po_no = "";
    ValidateParts replace_part;
    EditText part_no, mfr, quantity, bin_location, old_parts_no, order_no;
    ImageView img_orderlist, img_partlist;
    CheckBox transfer_history;
    CheckBox another_Parts;
    String transfer_his;
    Typeface header_face, txt_face;
    LinearLayout layout_order_num, layout_old_part;
    String user_token, user, old_part, part_no_new, mfr_no, bin_loc;
    int order, qty_no;
    String qtyno;
    int replace_btn_value = 0;
    Dialog mDialogscan = null;
    int isDeviceConnected = 1;
    HashMap<String, UsbDevice> usbDevices;
    CustomAdapterOrderNumber ordernumAdapter;
    int eponumber;
    Dialog dialog_Pmparts;
    String str_parts_no = "", str_mfg = "", str_mfgclass = "", str_comm = "", str_weight = "", str_sell1 = "", str_sell2 = "", str_sell3 = "", str_sell4 = "", str_sell5 = "", str_cost = "", str_description = "", str_unitstandard = "";
    Dialog unitstandard_dialog;
    ArrayList<String> unitstandard_list = new ArrayList<>();
    CustomAdapterUnitStandard unitstandardadapter;
    private Class<?> mClss;
    private Context mContext;
    private ArrayList<GetPartsDetails> partnumbers = new ArrayList<GetPartsDetails>();
    private CustomAdapter partsAdapter;
    private SweetAlertDialog partalert;
    private ArrayList<PoObject> polist = new ArrayList<>();

    public static DeviceType deviceTypeFromInt(int i) {
        return deviceTypeValues[i];
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_parts);

        mContext = this;

        header_face = Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");
        txt_face = Typeface.createFromAsset(getAssets(), "fonts/helr45w.ttf");

        back = (Button) findViewById(R.id.back);
        part_no = (EditText) findViewById(R.id.parts_no);
        mfr = (EditText) findViewById(R.id.mfr);
        quantity = (EditText) findViewById(R.id.quantity);
        old_parts_no = (EditText) findViewById(R.id.old_parts_no);
        order_no = (EditText) findViewById(R.id.order_no);
        transfer_history = (CheckBox) findViewById(R.id.transfer_history);
        another_Parts = (CheckBox) findViewById(R.id.AddAnother_part);
        bin_location = (EditText) findViewById(R.id.bin_location);

        layout_order_num = (LinearLayout) findViewById(R.id.layout_Order_num);
        layout_old_part = (LinearLayout) findViewById(R.id.layout_old_part);

        img_orderlist = (ImageView) findViewById(R.id.img_orderlist);
        img_partlist = (ImageView) findViewById(R.id.img_partlist);

        TextView header_txt = (TextView) findViewById(R.id.header_txt);
        TextView txt_part = (TextView) findViewById(R.id.txt_part);
        TextView txt_mfr = (TextView) findViewById(R.id.txt_mfr);
        TextView txt_quantity = (TextView) findViewById(R.id.txt_quantity);
        TextView txt_bin = (TextView) findViewById(R.id.txt_bin);
        TextView txt_oldpartno = (TextView) findViewById(R.id.txt_oldpartno);
        TextView txt_replaceorder = (TextView) findViewById(R.id.txt_replaceorder);
        TextView txt_history = (TextView) findViewById(R.id.txt_history);

        header_txt.setTypeface(header_face);
        txt_part.setTypeface(header_face);
        txt_mfr.setTypeface(header_face);
        txt_quantity.setTypeface(header_face);
        txt_bin.setTypeface(header_face);
        txt_oldpartno.setTypeface(header_face);
        txt_replaceorder.setTypeface(header_face);
        txt_history.setTypeface(header_face);
        back.setTypeface(header_face);
        part_no.setTypeface(txt_face);
        mfr.setTypeface(txt_face);
        quantity.setTypeface(txt_face);
        old_parts_no.setTypeface(txt_face);
        order_no.setTypeface(txt_face);
        bin_location.setTypeface(txt_face);

        old_parts_no.setEnabled(false);
        order_no.setEnabled(false);

        unitstandard_list.add("Each");
        unitstandard_list.add("Gallon");
        unitstandard_list.add("Ft (feet)");
        unitstandard_list.add("Lb (pounds)");
        unitstandard_list.add("Ounce");
        unitstandard_list.add("Quart");
        unitstandard_list.add("Inch");
        unitstandard_list.add("Yard");
        unitstandard_list.add("Case");
        unitstandard_list.add("Meter");
        unitstandard_list.add("Centimeter");
        unitstandard_list.add("Link");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String part_number = bundle.getString("PartNo", "");
            String old_partsnumnber = bundle.getString("OldPartNo", "");
            int order_num = bundle.getInt("Order_no", 0);
            String Mfr = bundle.getString("Mfr", "");
            eponumber = bundle.getInt("Epoorder_no", 0);

            part_no.setText(part_number);
            old_parts_no.setText(old_partsnumnber);
            mfr.setText(Mfr);

            if (order_num == 0) {
                order_no.setText("");
            } else {
                order_no.setText(order_num + "");
            }


        }

        Bundle bundlescan = getIntent().getExtras();
        if (bundlescan != null) {
            String replace = bundlescan.getString("value");
            if (Sessiondata.getInstance().getReplace_value().toString().equalsIgnoreCase("yes")) {
                Sessiondata.getInstance().setReplace_binlocation(replace.toString());
            }
        }

        if (Sessiondata.getInstance().getPartReceive() == 1) {
            part_no.setEnabled(false);
            layout_order_num.setVisibility(View.VISIBLE);
            layout_old_part.setVisibility(View.VISIBLE);
            if (Sessiondata.getInstance().getPartObjects() != null) {
                if (Sessiondata.getInstance().getPartObjects().size() != 0) {
                    replacedata = Sessiondata.getInstance().getPartObjects();
                } else {
                    replacedata = new ArrayList<>();
                }
            } else {
                replacedata = new ArrayList<>();
            }
        } else {
            replacedata = new ArrayList<>();
        }

        if (Sessiondata.getInstance().getPartReceive() == 2) {
            part_no.setEnabled(true);
            layout_order_num.setVisibility(View.GONE);
            layout_old_part.setVisibility(View.GONE);
            if (Sessiondata.getInstance().getPartObjects() != null) {
                if (Sessiondata.getInstance().getPartObjects().size() != 0) {
                    replacedata = Sessiondata.getInstance().getPartObjects();

                } else {
                    replacedata = new ArrayList<>();
                }
            } else {
                replacedata = new ArrayList<>();
            }
        } else {
//            replacedata=new ArrayList<>();
        }

        if (Sessiondata.getInstance().getArray_EPONum() != null) {
            if (Sessiondata.getInstance().getArray_EPONum().size() != 0) {

                polist = Sessiondata.getInstance().getArray_EPONum();
//                order_no.setText(polist.get(0).getName());

            }
        }

        img_orderlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (polist != null) {
                    if (polist.size() != 0) {

                        partandorderDialog = new Dialog(ReplacePartsActivity.this);
                        partandorderDialog.setCanceledOnTouchOutside(false);
                        partandorderDialog.setCancelable(false);
                        partandorderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        partandorderDialog.setContentView(R.layout.dialog_list_select);

                        TextView txt_header = (TextView) partandorderDialog.findViewById(R.id.txt_header);
                        txt_header.setText("Select PO#");
                        txt_header.setTypeface(header_face);

                        ListView list = (ListView) partandorderDialog.findViewById(R.id.list);

                        ordernumAdapter = new CustomAdapterOrderNumber(ReplacePartsActivity.this, polist);
                        list.setAdapter(ordernumAdapter);

                        TextView dialog_social_ok = (TextView) partandorderDialog.findViewById(R.id.dialog_social_ok);
                        dialog_social_ok.setTypeface(header_face);

                        dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                partandorderDialog.dismiss();
                            }
                        });

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                order_no.setText(polist.get(position).getName());
                                partandorderDialog.dismiss();
                            }
                        });


                        partandorderDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                        partandorderDialog.show();


                    }
                }


            }
        });


        img_partlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!order_no.getText().toString().equals("")) {

                    if (checkInternetConenction()) {
                        new GetPartList().execute();
                    } else {
                        Toast.makeText(ReplacePartsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                } else {


                    if (sweetalrt) {
                        sweetalrt = false;
                        partalert = new SweetAlertDialog(ReplacePartsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Please Enter PO#!")
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
                        partalert.setCancelable(false);
                        partalert.show();

                    }
                }

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearSession();
                Intent myintent = new Intent(ReplacePartsActivity.this, PartsReceivingDetailsActivity.class);
                startActivity(myintent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });

        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearSession();
                Intent myintent = new Intent(ReplacePartsActivity.this, PartsReceivingDetailsActivity.class);
                startActivity(myintent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });
        bin_search = (ImageView) findViewById(R.id.bin_search);
        btn_replace = (Button) findViewById(R.id.btn_replace);
        btn_replace.setTypeface(header_face);


        bin_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sessiondata.getInstance().setScanner_partreceipt(0);
                Sessiondata.getInstance().setScanner_partreceiving(0);
                Sessiondata.getInstance().setScanner_replace(3);
                Sessiondata.getInstance().setScanner_counting1(0);
                Sessiondata.getInstance().setScanner_counting2(0);
                Sessiondata.getInstance().setScanner_inventory(0);

                Sessiondata.getInstance().setScanner_partnumber(0);
                Sessiondata.getInstance().setScanner_hwstartbin(0);
                Sessiondata.getInstance().setScanner_hwendbin(0);

                Sessiondata.getInstance().setReplace_part(part_no.getText().toString());
                Sessiondata.getInstance().setReplace_mfr(mfr.getText().toString());
                Sessiondata.getInstance().setReplace_qty(quantity.getText().toString());
                Sessiondata.getInstance().setReplace_oldpart(old_parts_no.getText().toString());
                Sessiondata.getInstance().setReplace_order(order_no.getText().toString());
                Sessiondata.getInstance().setReplace_binlocation(bin_location.getText().toString());

                if (transfer_history.isChecked()) {
                    Sessiondata.getInstance().setReplace_transfer("y");
                } else {
                    Sessiondata.getInstance().setReplace_transfer("n");
                }
                Sessiondata.getInstance().setReplace_value("yes");

                launchActivity(SimpleScannerActivity.class);
            }
        });

        btn_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderno = order_no.getText().toString();
                String old_part = old_parts_no.getText().toString();
                String part_no_new = part_no.getText().toString();
                qtyno = quantity.getText().toString();
                String mfr_no = mfr.getText().toString();
                String bin_loc = bin_location.getText().toString();

                if (transfer_history.isChecked()) {

                    transfer_his = "Y";

                } else {
                    transfer_his = "N";

                }

                if (part_no_new.equalsIgnoreCase("") || mfr_no.equalsIgnoreCase("") || qtyno.equalsIgnoreCase("") || bin_loc.equalsIgnoreCase("") || old_part.equalsIgnoreCase("") || orderno.equalsIgnoreCase("")) {


                    if (sweetreplace) {

                        sweetreplace = false;

                        sweetalt = new SweetAlertDialog(ReplacePartsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("You are required to enter all the fields!")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        sweetreplace = true;
                                        sDialog.dismiss();
                                    }
                                });
                        sweetalt.setCancelable(false);
                        sweetalt.show();
                    }

                } else {
                    if (checkInternetConenction()) {

                        if (replace_btn_value == 0) {
                            replace_btn_value = 1;
                            new AsynReplacePartDetailsTask().execute();
                        }

                    } else {

                        Toast.makeText(ReplacePartsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
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

        ReplacePartsActivity.readerDevice = ReaderDevice.getMXDevice(mContext);
        readerDevice.startAvailabilityListening();
        selectedDevice = "MX Scanner";

        ReplacePartsActivity.readerDevice.setReaderDeviceListener(this);
        ReplacePartsActivity.readerDevice.enableImage(true);
        ReplacePartsActivity.readerDevice.connect(ReplacePartsActivity.this);
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

                Sessiondata.getInstance().setReplace_binlocation(result);

                bin_location.setText(Sessiondata.getInstance().getReplace_binlocation());


            }
        }, 500);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(bin_location.getWindowToken(), 0);
    }

    @Override
    public void onAvailabilityChanged(ReaderDevice reader) {
        if (reader.getAvailability() == ReaderDevice.Availability.AVAILABLE) {
            ReplacePartsActivity.readerDevice.connect(ReplacePartsActivity.this);
        } else {
            // DISCONNECTED USB DEVICE
            ReplacePartsActivity.readerDevice.connect(ReplacePartsActivity.this);
            ReplacePartsActivity.readerDevice.disconnect();
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
        ReplacePartsActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.C128, true, null);
        ReplacePartsActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.DATAMATRIX, true, null);
        ReplacePartsActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.UPC_EAN, true, null);
        ReplacePartsActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.QR, true, null);

        //example sendCommand
        ReplacePartsActivity.readerDevice.getDataManSystem().sendCommand("SET SYMBOL.MICROPDF417 ON");
        ReplacePartsActivity.readerDevice.getDataManSystem().sendCommand("SET IMAGE.SIZE 0");

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

    private boolean checkInternetConenction() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sweetalrt = true;
        sweetreplace = true;
        sweetalrtsuccess = true;

        if (Sessiondata.getInstance().getReplace_value().toString().equalsIgnoreCase("yes")) {
            part_no.setText(Sessiondata.getInstance().getReplace_part());
            mfr.setText(Sessiondata.getInstance().getReplace_mfr());
            quantity.setText(Sessiondata.getInstance().getReplace_qty());
            old_parts_no.setText(Sessiondata.getInstance().getReplace_oldpart());
            order_no.setText(Sessiondata.getInstance().getReplace_order());
            bin_location.setText(Sessiondata.getInstance().getReplace_binlocation());
            if (Sessiondata.getInstance().getReplace_transfer().toString().equalsIgnoreCase("y")) {
                transfer_history.setChecked(true);
            } else if (Sessiondata.getInstance().getReplace_transfer().toString().equalsIgnoreCase("n")) {

                transfer_history.setChecked(false);
            }
        } else {

            mfr.setText(mfr.getText().toString());
            quantity.setText(quantity.getText().toString());
            old_parts_no.setText(old_parts_no.getText().toString());
            order_no.setText(order_no.getText().toString());
            bin_location.setText(bin_location.getText().toString());

        }

    }

    private void dialogInsertPmparts(String part_no_new, String mfg) {

        str_parts_no = "";
        str_mfg = "";
        str_mfgclass = "";
        str_comm = "";
        str_weight = "";
        str_sell1 = "";
        str_sell2 = "";
        str_sell3 = "";
        str_sell4 = "";
        str_sell5 = "";
        str_cost = "";
        str_description = "";
        str_unitstandard = "";

        dialog_Pmparts = new Dialog(ReplacePartsActivity.this);
        dialog_Pmparts.setCanceledOnTouchOutside(false);
        dialog_Pmparts.setCancelable(false);
        dialog_Pmparts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_Pmparts.setContentView(R.layout.dialog_pmpartsetup);


        final EditText partnum = (EditText) dialog_Pmparts.findViewById(R.id.edt_partnum);
        final EditText edt_mfg = (EditText) dialog_Pmparts.findViewById(R.id.edt_mfg);
        final EditText mfg_class = (EditText) dialog_Pmparts.findViewById(R.id.edt_mfgClass);
        final EditText comm = (EditText) dialog_Pmparts.findViewById(R.id.edt_comm);
        final EditText weight = (EditText) dialog_Pmparts.findViewById(R.id.edt_weight);
        final EditText sell1 = (EditText) dialog_Pmparts.findViewById(R.id.edt_sell1);
        final EditText sell2 = (EditText) dialog_Pmparts.findViewById(R.id.edt_sell2);
        final EditText sell3 = (EditText) dialog_Pmparts.findViewById(R.id.edt_sell3);
        final EditText sell4 = (EditText) dialog_Pmparts.findViewById(R.id.edt_sell4);
        final EditText sell5 = (EditText) dialog_Pmparts.findViewById(R.id.edt_sell5);
        final EditText cost = (EditText) dialog_Pmparts.findViewById(R.id.edt_cost);
        final EditText edt_description = (EditText) dialog_Pmparts.findViewById(R.id.edt_description);
        final EditText edt_unitstandard = (EditText) dialog_Pmparts.findViewById(R.id.edt_unitstandard);

        ImageView img_standardlist = (ImageView) dialog_Pmparts.findViewById(R.id.img_standardlist);

        RobotoTextView btn_ok = (RobotoTextView) dialog_Pmparts.findViewById(R.id.dialog_submit);
        RobotoTextView btn_cancel = (RobotoTextView) dialog_Pmparts.findViewById(R.id.dialog_social_cancel);

        partnum.setText(part_no_new);
        edt_mfg.setText(mfg);

        partnum.setEnabled(false);
        edt_mfg.setEnabled(false);


        img_standardlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unitstandard_dialog = new Dialog(ReplacePartsActivity.this);
                unitstandard_dialog.setCanceledOnTouchOutside(false);
                unitstandard_dialog.setCancelable(false);
                unitstandard_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                unitstandard_dialog.setContentView(R.layout.dialog_list_select);

                TextView txt_header = (TextView) unitstandard_dialog.findViewById(R.id.txt_header);
                txt_header.setText("Select Unit Standard");
                txt_header.setTypeface(header_face);

                ListView list = (ListView) unitstandard_dialog.findViewById(R.id.list);

                unitstandardadapter = new CustomAdapterUnitStandard(ReplacePartsActivity.this, unitstandard_list);
                list.setAdapter(unitstandardadapter);

                TextView dialog_social_ok = (TextView) unitstandard_dialog.findViewById(R.id.dialog_social_ok);
                dialog_social_ok.setTypeface(header_face);

                dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unitstandard_dialog.dismiss();
                    }
                });

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 2) {
                            edt_unitstandard.setText("Ft");
                        } else if (position == 3) {
                            edt_unitstandard.setText("Lb");
                        } else {
                            edt_unitstandard.setText(unitstandard_list.get(position));
                        }
                        unitstandard_dialog.dismiss();
                    }
                });


                unitstandard_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                unitstandard_dialog.show();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_parts_no = partnum.getText().toString();
                str_mfg = edt_mfg.getText().toString();
                str_mfgclass = mfg_class.getText().toString();
                str_comm = comm.getText().toString();
                str_weight = weight.getText().toString();
                str_sell1 = sell1.getText().toString();
                str_sell2 = sell2.getText().toString();
                str_sell3 = sell3.getText().toString();
                str_sell4 = sell4.getText().toString();
                str_sell5 = sell5.getText().toString();
                str_cost = cost.getText().toString();
                str_description = edt_description.getText().toString();
                str_unitstandard = edt_unitstandard.getText().toString();
                str_unitstandard = str_unitstandard.length() < 2 ? str_unitstandard : str_unitstandard.substring(0, 2);

                if ((!str_description.equals("")) && (!str_sell1.equals("")) && (!str_cost.equals(""))) {


                    if (str_sell1.equals("0")) {

                        if (sweetalrt) {
                            sweetalrt = false;
                            SweetAlertDialog alertDialog = new SweetAlertDialog(ReplacePartsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Sell1 should not be Zero")
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
                            alertDialog.setCancelable(false);
                            alertDialog.show();

                        }

                    } else if (str_cost.equals("0")) {
                        if (sweetalrt) {
                            sweetalrt = false;
                            SweetAlertDialog alertDialog = new SweetAlertDialog(ReplacePartsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Cost should not be Zero")
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
                            alertDialog.setCancelable(false);
                            alertDialog.show();

                        }

                    } else {

                        if (Double.parseDouble(str_sell1) > Double.parseDouble(str_cost)) {

                            if (checkInternetConenction()) {
                                new InsertPmParts().execute();
                                dialog_Pmparts.dismiss();
                            } else {
                                Toast.makeText(ReplacePartsActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            if (sweetalrt) {
                                sweetalrt = false;
                                SweetAlertDialog alertDialog = new SweetAlertDialog(ReplacePartsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("Cost may not be greater than Sell")
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
                                alertDialog.setCancelable(false);
                                alertDialog.show();

                            }
                        }

                    }


                } else {
//                    Toast.makeText(ReplacePartsActivity.this, "Please Enter ", Toast.LENGTH_SHORT).show();

                    if (sweetalrt) {
                        sweetalrt = false;
                        SweetAlertDialog alertDialog = new SweetAlertDialog(ReplacePartsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Please Enter Required Fields")
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
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }
                }

            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_Pmparts.dismiss();

            }
        });

        dialog_Pmparts.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ClearSession();
        Intent myintent = new Intent(ReplacePartsActivity.this, PartsReceivingDetailsActivity.class);
        startActivity(myintent);
        overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
    }

    private void ClearSession() {

        Sessiondata.getInstance().setReplace_part("");
        Sessiondata.getInstance().setReplace_mfr("");
        Sessiondata.getInstance().setReplace_qty("");
        Sessiondata.getInstance().setReplace_oldpart("");
        Sessiondata.getInstance().setReplace_order("");
        Sessiondata.getInstance().setReplace_value("");
        Sessiondata.getInstance().setReplace_transfer("");

    }

    public static enum DeviceType {MX_1000}

    @SuppressLint("StaticFieldLeak")
    public class AsynReplacePartDetailsTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {

            ProgressBar.showCommonProgressDialog(ReplacePartsActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            user_token = Sessiondata.getInstance().getLoginObject().getUsertoken();
            user = Sessiondata.getInstance().getLoginObject().getUsername();
            order = Integer.parseInt(order_no.getText().toString());
            old_part = String.valueOf(old_parts_no.getText().toString());
            part_no_new = String.valueOf(part_no.getText().toString());
            qty_no = Integer.parseInt(quantity.getText().toString());
            mfr_no = String.valueOf(mfr.getText().toString());
            bin_loc = String.valueOf(bin_location.getText().toString());
            Log.d("transfer_his", "" + transfer_his);

            try {

                replace_part = WebServiceConsumer.getInstance().validateparts(user_token, order, old_part);

            } catch (java.net.SocketTimeoutException e) {
                replace_part = null;
            } catch (Exception e) {
                replace_part = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            replace_btn_value = 0;
            ProgressBar.dismiss();
            Sessiondata.getInstance().setValidateParts(replace_part);

            if (replace_part != null) {

                if (replace_part.getMessage().length() != 0) {
                    String Result = replace_part.getMessage();
                    String replace = Result.replace("Error - ", "");

                    if (replace_part.getMessage().toString().contains("Session")) {
                        Session = 0;
                        if (checkInternetConenction()) {

                            new AsyncSessionLoginTask().execute();

                        } else {

                            Toast.makeText(ReplacePartsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }
                }

                String status = Sessiondata.getInstance().getValidateParts().getStatus();
                String po_qty = String.valueOf(Sessiondata.getInstance().getValidateParts().getPoqty());

                int epoorderno;

                if (Sessiondata.getInstance().getPartReceive() == 1) {
                    epoorderno = Sessiondata.getInstance().getValidateParts().getOepordno();
                } else {
                    epoorderno = eponumber;
                }

                int oeitenum = Sessiondata.getInstance().getValidateParts().getOeitemnum();
                String msg = Sessiondata.getInstance().getValidateParts().getMessage();
                String branch_new = Sessiondata.getInstance().getValidateParts().getBranch();

                Log.d("ReplacePart", "" + replace_part.toString());
                Log.d("ReplacePart_POQTY", "" + po_qty);
                Log.d("OEPORDER", "" + epoorderno);

                if (status.toString().equalsIgnoreCase("False")) {
                    if (sweetalrt) {
                        sweetalrt = false;
                        sweetdialog = new SweetAlertDialog(ReplacePartsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Check Your Order# and Old Part#")
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
                        sweetdialog.setCancelable(false);
                        sweetdialog.show();
                    }
                } else if (status.toString().equalsIgnoreCase("True")) {

                    int showdialog = 0;

                    for (int i = 0; i < replacedata.size(); i++) {

                        if (part_no_new.equals(replacedata.get(i).getPart()) &&
                                order == replacedata.get(i).getPo() &&
                                epoorderno == replacedata.get(i).getEpoorderno()) {

                            showdialog = 1;

                        }
                    }

                    if (showdialog == 1) {
                        showdialog = 0;
                        part_no.setText("");
                        SweetAlertDialog sweetdialog = new SweetAlertDialog(ReplacePartsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Part # Already Exist")
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
                        sweetdialog.setCancelable(false);
                        sweetdialog.show();
                    } else {

                        if (checkInternetConenction()) {

                            new CheckPartsAvailability().execute();

                        } else {

                            Toast.makeText(ReplacePartsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }


                    }

//                    Sessiondata.getInstance().setPart_number(old_part);

                }

            } else {
//                Toast.makeText(ReplacePartsActivity.this, "null value", Toast.LENGTH_LONG).show();

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class CheckPartsAvailability extends AsyncTask<Void, Void, Void> {

        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar.showCommonProgressDialog(ReplacePartsActivity.this);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                result = WebServiceConsumer.getInstance().CheckPartsAvailability(Sessiondata.getInstance().getLoginObject().getUsertoken(),
                        part_no.getText().toString(), mfr.getText().toString());
            } catch (Exception e) {
                result = null;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            order = Integer.parseInt(order_no.getText().toString());
            old_part = String.valueOf(old_parts_no.getText().toString());
            part_no_new = String.valueOf(part_no.getText().toString());
            qty_no = Integer.parseInt(quantity.getText().toString());
            mfr_no = String.valueOf(mfr.getText().toString());
            bin_loc = String.valueOf(bin_location.getText().toString());

            int temp_qty = 1;
            int epoorderno;

            if (Sessiondata.getInstance().getPartReceive() == 1) {
                epoorderno = Sessiondata.getInstance().getValidateParts().getOepordno();
            } else {
                epoorderno = eponumber;
            }

            int oeitenum = Sessiondata.getInstance().getValidateParts().getOeitemnum();
            String branch_new = Sessiondata.getInstance().getValidateParts().getBranch();

            ProgressBar.dismiss();
            if (result != null) {

                if (result.contains("Session")) {
                    Session = 1;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {

                        Toast.makeText(ReplacePartsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                    }

                } else if (result.contains("1")) {

                    new AsyncGetPMPartDetails().execute();

//                    Log.d("Checkavail","Available");
//
//                    int totalOrdQty = 0;
//
//                    for(int i = 0 ; i < replacedata.size() ; i++){
//
//                        if(replacedata.get(i).getPart().equals(old_part) &&
//                                replacedata.get(i).getEpoorderno() == epoorderno){
//                            replacedata.get(i).setStatus("R");
//                            replacedata.get(i).setReplacedPart(true);
//                            replacedata.get(i).setReceipt_qty("");
//
//                            temp_qty = Integer.parseInt(replacedata.get(i).getPo_qty());
//
//                            temp_qty = temp_qty * Integer.parseInt(qtyno);
//
//                        }
//
//                    }
//
//                    PartObject md = new PartObject(order,part_no_new,"",String.valueOf(temp_qty),"","","","","",bin_loc,branch_new,mfr_no,"I",dummyvalue,old_part,transfer_his,epoorderno,oeitenum,totalOrdQty,bin_loc,"",true,true);
//
//                    replacedata.add(md);
//
//                    Sessiondata.getInstance().setPartObjects(replacedata);
//                    Sessiondata.getInstance().setReplaceadapter(1);
//
//                    if(another_Parts.isChecked()){
//
//                        img_orderlist.setEnabled(false);
//                        img_partlist.setEnabled(false);
//                        if(Sessiondata.getInstance().getPartReceive() != 0){
//                            if(Sessiondata.getInstance().getPartReceive() == 1){
//
//                                order_no.setEnabled(false);
//                                old_parts_no.setEnabled(false);
//                                part_no.setText("");
//                                quantity.setText("");
//                                another_Parts.setChecked(false);
////                                mfr.setEnabled(false);
//                                part_no.setEnabled(true);
//
//                            }else if(Sessiondata.getInstance().getPartReceive() == 2){
//
////                                mfr.setEnabled(false);
//                                part_no.setText("");
//                                quantity.setText("");
//                                another_Parts.setChecked(false);
//
//                            }
//                        }
//
//                    }else {
//                        ClearSession();
//                        Intent intent=new Intent(ReplacePartsActivity.this,PartsReceivingDetailsActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
//
//                    }


                } else if (result.contains("0")) {

                    dialogInsertPmparts(part_no_new, mfr_no);

                    Log.d("Checkavail", "Not Available");
                }

            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class InsertPmParts extends AsyncTask<Void, Void, Void> {

        String result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar.showCommonProgressDialog(ReplacePartsActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (str_weight.equals("")) {
                str_weight = "0.0";
            }

            if (str_sell2.equals("")) {
                str_sell2 = "0.0";
            }

            if (str_sell3.equals("")) {
                str_sell3 = "0.0";
            }

            if (str_sell4.equals("")) {
                str_sell4 = "0.0";
            }

            if (str_sell5.equals("")) {
                str_sell5 = "0.0";
            }

            try {
                result = WebServiceConsumer.getInstance().InsertPMParts(Sessiondata.getInstance().getLoginObject().getUsertoken(), str_mfg, str_parts_no, str_description,
                        str_comm, str_mfgclass, str_unitstandard, str_weight, str_sell1, str_sell2, str_sell3, str_sell4, str_sell5, str_cost);
            } catch (Exception e) {
                result = null;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ProgressBar.dismiss();

            order = Integer.parseInt(order_no.getText().toString());
            old_part = String.valueOf(old_parts_no.getText().toString());
            part_no_new = String.valueOf(part_no.getText().toString());
            qty_no = Integer.parseInt(quantity.getText().toString());
            mfr_no = String.valueOf(mfr.getText().toString());
            bin_loc = String.valueOf(bin_location.getText().toString());

//            int temp_qty = 1;
//            int epoorderno;
//
//            if(Sessiondata.getInstance().getPartReceive() == 1){
//                epoorderno=Sessiondata.getInstance().getValidateParts().getOepordno();
//            }else {
//                epoorderno = eponumber;
//            }
//
//            int oeitenum = Sessiondata.getInstance().getValidateParts().getOeitemnum();
//            String branch_new = Sessiondata.getInstance().getValidateParts().getBranch();

            if (result != null) {

                if (result.contains("Session")) {
                    Session = 2;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {

                        Toast.makeText(ReplacePartsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                    }
                } else if (result.contains("-1")) {

                    if (sweetalrt) {
                        sweetalrt = false;
                        SweetAlertDialog alertDialog = new SweetAlertDialog(ReplacePartsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText(result)
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
                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }

//                }else if(result.contains("1")) {
                } else {

                    new AsyncGetPMPartDetails().execute();

                }

            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncGetPMPartDetails extends AsyncTask<Void, Void, Void> {

        GetPmPartDetails getPmPartDetails;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressBar.showCommonProgressDialog(ReplacePartsActivity.this);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                getPmPartDetails = WebServiceConsumer.getInstance().GetPMPartDetails(Sessiondata.getInstance().getLoginObject().getUsertoken(),
                        part_no_new, mfr_no);
            } catch (Exception e) {
                getPmPartDetails = null;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressBar.dismiss();

            if (getPmPartDetails != null) {

                order = Integer.parseInt(order_no.getText().toString());
                old_part = String.valueOf(old_parts_no.getText().toString());
                part_no_new = String.valueOf(part_no.getText().toString());
                qty_no = Integer.parseInt(quantity.getText().toString());
                mfr_no = String.valueOf(mfr.getText().toString());
                bin_loc = String.valueOf(bin_location.getText().toString());

                int temp_qty = 1;
                int epoorderno;

                if (Sessiondata.getInstance().getPartReceive() == 1) {
                    epoorderno = Sessiondata.getInstance().getValidateParts().getOepordno();
                } else {
                    epoorderno = eponumber;
                }

                int oeitenum = Sessiondata.getInstance().getValidateParts().getOeitemnum();
                String branch_new = Sessiondata.getInstance().getValidateParts().getBranch();

                int totalOrdQty = 0;

                for (int i = 0; i < replacedata.size(); i++) {

                    if (replacedata.get(i).getPart().equals(old_part) &&
                            replacedata.get(i).getEpoorderno() == epoorderno) {

                        replacedata.get(i).setStatus("R");
                        replacedata.get(i).setReplacedPart(true);
                        replacedata.get(i).setReceipt_qty("");

                        temp_qty = Integer.parseInt(replacedata.get(i).getPo_qty());
                        temp_qty = temp_qty * Integer.parseInt(qtyno);
                    }

                }

                PartObject md = new PartObject(order, part_no_new, "", String.valueOf(temp_qty), "", "", getPmPartDetails.getDescription(), String.valueOf(getPmPartDetails.getUnit_price()), "", bin_loc, branch_new, mfr_no, "I", dummyvalue, old_part, transfer_his, epoorderno, oeitenum, totalOrdQty, bin_loc, "", true, true);

                replacedata.add(md);

                Sessiondata.getInstance().setPartObjects(replacedata);
                Sessiondata.getInstance().setReplaceadapter(1);

                if (another_Parts.isChecked()) {

                    img_orderlist.setEnabled(false);
                    img_partlist.setEnabled(false);
                    if (Sessiondata.getInstance().getPartReceive() != 0) {
                        if (Sessiondata.getInstance().getPartReceive() == 1) {

                            order_no.setEnabled(false);
                            old_parts_no.setEnabled(false);
                            part_no.setText("");
                            quantity.setText("");
                            another_Parts.setChecked(false);
//                                mfr.setEnabled(false);
                            part_no.setEnabled(true);

                        } else if (Sessiondata.getInstance().getPartReceive() == 2) {

//                                mfr.setEnabled(false);
                            part_no.setText("");
                            quantity.setText("");
                            another_Parts.setChecked(false);

                        }
                    }

                } else {

                    ClearSession();
                    Intent intent = new Intent(ReplacePartsActivity.this, PartsReceivingDetailsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);

                }


            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncSessionLoginTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(ReplacePartsActivity.this);
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
                        mDialogSession = new Dialog(ReplacePartsActivity.this);
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
                                Intent intent = new Intent(ReplacePartsActivity.this, LoginActivity.class);
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
                        mDialogSession.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        mDialogSession.show();
                    } else if (!mDialogSession.isShowing()) {
                        mDialogSession.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        mDialogSession.show();
                    }

                } else {
                    ProgressBar.dismiss();
                    if (Session == 0) {
                        if (checkInternetConenction()) {

                            new AsynReplacePartDetailsTask().execute();

                        } else {

                            Toast.makeText(ReplacePartsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 1) {

                        if (checkInternetConenction()) {

                            new CheckPartsAvailability().execute();

                        } else {

                            Toast.makeText(ReplacePartsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    } else if (Session == 2) {

                        if (checkInternetConenction()) {

                            new InsertPmParts().execute();

                        } else {

                            Toast.makeText(ReplacePartsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }

                }
            } else {
                ProgressBar.dismiss();

                if (mDialognodata == null) {
                    mDialognodata = new Dialog(ReplacePartsActivity.this);
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
                    mDialognodata.show();
                }

            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetPartList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBar.showCommonProgressDialog(ReplacePartsActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                partnumbers = WebServiceConsumer.getInstance().getPartsdetailV4_2(Sessiondata.getInstance().getLoginObject().getUsertoken(), order_no.getText().toString(), "");
            } catch (Exception e) {
                partnumbers = null;
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ProgressBar.dismiss();

            if (partnumbers != null) {

                orderdialog = new Dialog(ReplacePartsActivity.this);
                orderdialog.setCanceledOnTouchOutside(false);
                orderdialog.setCancelable(false);
                orderdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                orderdialog.setContentView(R.layout.dialog_list_select);

                TextView txt_header = (TextView) orderdialog.findViewById(R.id.txt_header);
                txt_header.setText("Select Part#");
                txt_header.setTypeface(header_face);

                ListView list = (ListView) orderdialog.findViewById(R.id.list);

                partsAdapter = new CustomAdapter(ReplacePartsActivity.this, partnumbers);
                list.setAdapter(partsAdapter);

                TextView dialog_social_ok = (TextView) orderdialog.findViewById(R.id.dialog_social_ok);
                dialog_social_ok.setTypeface(header_face);

                dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderdialog.dismiss();
                    }
                });

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        old_parts_no.setText(partnumbers.get(position).getPartno());
                        orderdialog.dismiss();
                    }
                });

                orderdialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                orderdialog.show();


            }
        }

    }

    public class CustomAdapter extends BaseAdapter {
        ArrayList<GetPartsDetails> result;
        Context context;
        int[] imageId;
        private LayoutInflater inflater = null;

        public CustomAdapter(Context context, ArrayList<GetPartsDetails> list) {

            result = list;
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
                convertView = inflater.inflate(R.layout.activity_branch_childrow, parent, false);
                holder = new ViewHolder();

                holder.po = (TextView) convertView.findViewById(R.id.process_list);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            GetPartsDetails m = result.get(position);
            holder.po.setText(m.getPartno());

            holder.po.setTypeface(txt_face);


            return convertView;
        }
    }

    private class ViewHolder {

        private TextView po;

        public ViewHolder() {
        }
    }

    public class CustomAdapterOrderNumber extends BaseAdapter {
        ArrayList<PoObject> result;
        Context context;
        int[] imageId;
        private LayoutInflater inflater = null;

        public CustomAdapterOrderNumber(Context context, ArrayList<PoObject> list) {

            result = list;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public CustomAdapterOrderNumber() {

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
                convertView = inflater.inflate(R.layout.activity_branch_childrow, parent, false);
                holder = new ViewHolder();

                holder.po = (TextView) convertView.findViewById(R.id.process_list);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            PoObject m = result.get(position);
            holder.po.setText(m.getName());

            holder.po.setTypeface(txt_face);


            return convertView;
        }
    }

    public class CustomAdapterUnitStandard extends BaseAdapter {
        ArrayList<String> result;
        Context context;
        private LayoutInflater inflater = null;

        public CustomAdapterUnitStandard(Context context, ArrayList<String> list) {

            result = list;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public CustomAdapterUnitStandard() {

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
                convertView = inflater.inflate(R.layout.activity_branch_childrow, parent, false);
                holder = new ViewHolder();

                holder.po = (TextView) convertView.findViewById(R.id.process_list);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.po.setText(result.get(position));

            holder.po.setTypeface(txt_face);


            return convertView;
        }
    }
}
