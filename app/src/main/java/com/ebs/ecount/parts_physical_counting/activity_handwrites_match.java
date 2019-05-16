package com.ebs.ecount.parts_physical_counting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebs.ecount.R;
import com.ebs.ecount.initial.LoginActivity;
import com.ebs.ecount.objects.GetBinLocation;
import com.ebs.ecount.objects.GetPartsQuantity;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.uidesigns.ProgressBar;
import com.ebs.ecount.uidesigns.SweetAlertDialog;
import com.ebs.ecount.utils.Sessiondata;
import com.ebs.ecount.webutils.WebServiceConsumer;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by cbe-teclwsp-009 on 13/4/17.
 */

public class activity_handwrites_match extends Activity {

    TextView notes_label,txt_header, txt_processid, txt_branch, txt_part, txt_mfr, txt_description, txt_quantity,txt_binloc, txt_startingbin, txt_endingbin;
    EditText notes,processid, branch, partno, mfr, description, quantity,bin_location, primary_bin_loc, secondary_bin_loc;
    ImageView img_mfr, img_back,img_bin;
    Button btn_submit, back;
    Typeface header_face, txt_face;

    Boolean Sweetalrt_list=true;
    SweetAlertDialog Sweetalt_list;

    ArrayList<GetPartsQuantity> load_partsqty;

    String userToken,Part_No;
    int process_list;
    ArrayList<GetBinLocation> binLocations;;

    int Session = 0;
    Dialog mDialoglist=null;
    Dialog mDialoglist_loc=null;

    ArrayList<String> mgf;
    private CustomAdapter adapter_mfg;
    private CustomAdapter_binloc customAdapter_binloc;
    Dialog mDialognodata;
    LoginObject loginObject = null;
    ArrayList<String> resultbinlocation;
    ArrayList<String> primarybin;
    ArrayList<String> secondarybin;
    ListView list;
    String partquantity_match;
    Boolean sweetalrtsuccess = true;

    SweetAlertDialog sweetalt;
    Boolean sweetalrterror = true;

    Boolean sweetalrt = true;

    SweetAlertDialog sweetalt_error;
    Dialog mDialogmsg;
    int size_binloc;

    String submit_processid,submit_branch,submit_part,submit_mfg,submit_qty,submit_primary,submit_secondary,submit_bin_location,submit_notes;
    boolean submit_uncount;
    int submit_variance;
    int size_bins;
    String StartBin_concat,EndBin_concat,binloc;
    String validateParts_v1;
    String user_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partsmatch);

        header_face = Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");
        txt_face = Typeface.createFromAsset(getAssets(), "fonts/helr45w.ttf");

        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_processid = (TextView) findViewById(R.id.txt_processid);
        txt_branch = (TextView) findViewById(R.id.txt_branch);
        txt_part = (TextView) findViewById(R.id.txt_part);
        txt_mfr = (TextView) findViewById(R.id.txt_mfr);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_quantity = (TextView) findViewById(R.id.txt_quantity);
        txt_startingbin = (TextView) findViewById(R.id.txt_startingbin);
        txt_endingbin = (TextView) findViewById(R.id.txt_endingbin);
        txt_binloc = (TextView) findViewById(R.id.txt_binloc);

        notes_label = (TextView) findViewById(R.id.txt_notes);
        notes = (EditText) findViewById(R.id.notes);

        processid = (EditText) findViewById(R.id.processid);
        branch = (EditText) findViewById(R.id.branch);
        partno = (EditText) findViewById(R.id.partno);
        mfr = (EditText) findViewById(R.id.mfr);
        description = (EditText) findViewById(R.id.description);
        quantity = (EditText) findViewById(R.id.quantity);
        primary_bin_loc = (EditText) findViewById(R.id.primary_bin_loc);
        secondary_bin_loc = (EditText) findViewById(R.id.secondary_bin_loc);
        bin_location = (EditText) findViewById(R.id.bin_location);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        back = (Button) findViewById(R.id.back);

        img_mfr = (ImageView) findViewById(R.id.img_mfr);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_bin = (ImageView) findViewById(R.id.img_bin);

        notes_label.setTypeface(header_face);
        txt_header.setTypeface(header_face);
        txt_processid.setTypeface(header_face);
        txt_branch.setTypeface(header_face);
        txt_part.setTypeface(header_face);
        txt_mfr.setTypeface(header_face);
        txt_description.setTypeface(header_face);
        txt_quantity.setTypeface(header_face);
        txt_startingbin.setTypeface(header_face);
        txt_endingbin.setTypeface(header_face);
        txt_binloc.setTypeface(header_face);
        btn_submit.setTypeface(header_face);
        back.setTypeface(txt_face);
        notes.setTypeface(txt_face);

        processid.setTypeface(txt_face);
        branch.setTypeface(txt_face);
        partno.setTypeface(txt_face);
        mfr.setTypeface(txt_face);
        description.setTypeface(txt_face);
        quantity.setTypeface(txt_face);
        primary_bin_loc.setTypeface(txt_face);
        secondary_bin_loc.setTypeface(txt_face);
        bin_location.setTypeface(txt_face);


        mfr.setText(Sessiondata.getInstance().getMfg());


        processid.setText(Sessiondata.getInstance().getProcessid_match());

        branch.setText(Sessiondata.getInstance().getCounting_branchname());
        partno.setText(Sessiondata.getInstance().getPart_match());


        if (Sessiondata.getInstance().getPartsquantity() != null){
            if (Sessiondata.getInstance().getPartsquantity().size() == 1){
                notes.setText(Sessiondata.getInstance().getPartsquantity().get(0).getNotes());
            }
        }

        if (Sessiondata.getInstance().getGetManufacturerses() != null){
            if (Sessiondata.getInstance().getGetManufacturerses().size() == 1){
                description.setText(Sessiondata.getInstance().getGetManufacturerses().get(0).getPartDesc());
            }
        }

        description.addTextChangedListener(new TextWatcher() {
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
                    description.setText(ss);
                }
                description.setSelection(description.getText().length());
            }
        });

        bin_location.addTextChangedListener(new TextWatcher() {
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
                    bin_location.setText(ss);
                }
                bin_location.setSelection(bin_location.getText().length());
            }

        });

        branch.addTextChangedListener(new TextWatcher() {
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
                    branch.setText(ss);
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

        img_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mfr.getText().toString().equalsIgnoreCase("")){


                    if(Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please choose the manufacturer")
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

                }else if (Sessiondata.getInstance().getBinlocation() != null){
                    size_bins = Sessiondata.getInstance().getBinlocation().size();
                    Log.d("size_bins",""+ size_bins);

                    if (size_bins == 1){

                            if (Sessiondata.getInstance().getBinlocation().get(0).getStartbin().toString().equalsIgnoreCase("")
                                    && Sessiondata.getInstance().getBinlocation().get(0).getEndbin().toString().equalsIgnoreCase("")){

                                activity_handwrites_match.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Sweetalrt_list) {
                                            Sweetalrt_list = false;

                                            Sweetalt_list=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Cannot Found Bin Location")
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
                                    }
                                });


                            }else {
                                activity_handwrites_match.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if ((mDialoglist_loc == null) || !mDialoglist_loc.isShowing()) {
                                            mDialoglist_loc = new Dialog(activity_handwrites_match.this);
                                            mDialoglist_loc.setCanceledOnTouchOutside(false);
                                            mDialoglist_loc.setCancelable(false);
                                            mDialoglist_loc.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            mDialoglist_loc.setContentView(R.layout.dialog_list_select);

                                            TextView txt_header = (TextView) mDialoglist_loc.findViewById(R.id.txt_header);
                                            TextView dialog_social_ok = (TextView) mDialoglist_loc.findViewById(R.id.dialog_social_ok);
                                            dialog_social_ok.setTypeface(header_face);
                                            txt_header.setText("Select Bin Location");
                                            txt_header.setTypeface(header_face);

                                            ListView list = (ListView) mDialoglist_loc.findViewById(R.id.list);

                                            int size_bin = Sessiondata.getInstance().getBinlocation().size();

                                            resultbinlocation = new ArrayList<>();
                                            primarybin = new ArrayList<String>();
                                            secondarybin = new ArrayList<String>();


                                            for (int i = 0; i < size_bin; i++) {

                                                if (!Sessiondata.getInstance().getBinlocation().get(i).getStartbin().toString().equalsIgnoreCase("")){
                                                    StartBin_concat = Sessiondata.getInstance().getBinlocation().get(i).getStartbin();
                                                }else {
                                                    StartBin_concat = "";
                                                }

                                                if (!Sessiondata.getInstance().getBinlocation().get(i).getEndbin().toString().equalsIgnoreCase("")){
                                                    EndBin_concat = Sessiondata.getInstance().getBinlocation().get(i).getEndbin();
                                                }else {
                                                    EndBin_concat = "";
                                                }

                                                if (!StartBin_concat.toString().equalsIgnoreCase("") && !EndBin_concat.toString().equalsIgnoreCase("")){
                                                    binloc = StartBin_concat+ "," + EndBin_concat;
                                                    resultbinlocation.add(binloc);
                                                    primarybin.add(StartBin_concat);
                                                    secondarybin.add(EndBin_concat);
                                                    customAdapter_binloc = new CustomAdapter_binloc(activity_handwrites_match.this, resultbinlocation);
                                                    list.setAdapter(customAdapter_binloc);
                                                    justifyListViewHeightBasedOnChildren(list);
                                                }else if (!StartBin_concat.toString().equalsIgnoreCase("") && EndBin_concat.toString().equalsIgnoreCase("")) {
                                                    binloc = StartBin_concat;
                                                    resultbinlocation.add(binloc);
                                                    primarybin.add(StartBin_concat);
                                                    secondarybin.add(EndBin_concat);
                                                    customAdapter_binloc = new CustomAdapter_binloc(activity_handwrites_match.this, resultbinlocation);
                                                    list.setAdapter(customAdapter_binloc);
                                                    justifyListViewHeightBasedOnChildren(list);
                                                }else if (StartBin_concat.toString().equalsIgnoreCase("") && !EndBin_concat.toString().equalsIgnoreCase("")) {
                                                    binloc = EndBin_concat;
                                                    resultbinlocation.add(binloc);
                                                    primarybin.add(StartBin_concat);
                                                    secondarybin.add(EndBin_concat);
                                                    customAdapter_binloc = new CustomAdapter_binloc(activity_handwrites_match.this, resultbinlocation);
                                                    list.setAdapter(customAdapter_binloc);
                                                    justifyListViewHeightBasedOnChildren(list);
                                                }
                                            }

                                            mDialoglist_loc.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                                            mDialoglist_loc.show();

                                            dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mDialoglist_loc.dismiss();
                                                }
                                            });
                                        }
                                    }
                                });

                            }



                    }else {
                        if ((mDialoglist_loc == null) || !mDialoglist_loc.isShowing()) {
                            mDialoglist_loc = new Dialog(activity_handwrites_match.this);
                            mDialoglist_loc.setCanceledOnTouchOutside(false);
                            mDialoglist_loc.setCancelable(false);
                            mDialoglist_loc.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            mDialoglist_loc.setContentView(R.layout.dialog_list_select);

                            TextView txt_header = (TextView) mDialoglist_loc.findViewById(R.id.txt_header);
                            TextView dialog_social_ok = (TextView) mDialoglist_loc.findViewById(R.id.dialog_social_ok);
                            dialog_social_ok.setTypeface(header_face);
                            txt_header.setText("Select Bin Location");
                            txt_header.setTypeface(header_face);

                            ListView list = (ListView) mDialoglist_loc.findViewById(R.id.list);

                            int size_bin = Sessiondata.getInstance().getBinlocation().size();

                            resultbinlocation = new ArrayList<>();
                            primarybin = new ArrayList<String>();
                            secondarybin = new ArrayList<String>();


                            for (int i = 0; i < size_bin; i++) {

                                if (!Sessiondata.getInstance().getBinlocation().get(i).getStartbin().toString().equalsIgnoreCase("")){
                                    StartBin_concat = Sessiondata.getInstance().getBinlocation().get(i).getStartbin();
                                }else {
                                    StartBin_concat = "";
                                }

                                if (!Sessiondata.getInstance().getBinlocation().get(i).getEndbin().toString().equalsIgnoreCase("")){
                                    EndBin_concat = Sessiondata.getInstance().getBinlocation().get(i).getEndbin();
                                }else {
                                    EndBin_concat = "";
                                }

                                if (!StartBin_concat.toString().equalsIgnoreCase("") && !EndBin_concat.toString().equalsIgnoreCase("")){
                                    binloc = StartBin_concat+ "," + EndBin_concat;
                                    resultbinlocation.add(binloc);
                                    primarybin.add(StartBin_concat);
                                    secondarybin.add(EndBin_concat);
                                    customAdapter_binloc = new CustomAdapter_binloc(activity_handwrites_match.this, resultbinlocation);
                                    list.setAdapter(customAdapter_binloc);
                                    justifyListViewHeightBasedOnChildren(list);
                                }else if (!StartBin_concat.toString().equalsIgnoreCase("") && EndBin_concat.toString().equalsIgnoreCase("")) {
                                    binloc = StartBin_concat;
                                    resultbinlocation.add(binloc);
                                    primarybin.add(StartBin_concat);
                                    secondarybin.add(EndBin_concat);
                                    customAdapter_binloc = new CustomAdapter_binloc(activity_handwrites_match.this, resultbinlocation);
                                    list.setAdapter(customAdapter_binloc);
                                    justifyListViewHeightBasedOnChildren(list);
                                }else if (StartBin_concat.toString().equalsIgnoreCase("") && !EndBin_concat.toString().equalsIgnoreCase("")) {
                                    binloc = EndBin_concat;
                                    resultbinlocation.add(binloc);
                                    primarybin.add(StartBin_concat);
                                    secondarybin.add(EndBin_concat);
                                    customAdapter_binloc = new CustomAdapter_binloc(activity_handwrites_match.this, resultbinlocation);
                                    list.setAdapter(customAdapter_binloc);
                                    justifyListViewHeightBasedOnChildren(list);
                                }
                            }

                            mDialoglist_loc.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
                            mDialoglist_loc.show();

                            dialog_social_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialoglist_loc.dismiss();
                                }
                            });
                        }
                    }
                } else {

                    if(Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Cannot Found Bin Location")
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

                }

            }
        });

        img_mfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(processid.getText().toString().equalsIgnoreCase("")){

                    if(Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the PID")
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

                }else if(branch.getText().toString().equalsIgnoreCase("")){

                    if(Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the branch")
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

                }else if(partno.getText().toString().equalsIgnoreCase("")){

                    if(Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the Part Number")
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

                }else {
                    if ((mDialoglist == null) || !mDialoglist.isShowing()) {
                        mDialoglist = new Dialog(activity_handwrites_match.this);
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

                        adapter_mfg = new CustomAdapter(activity_handwrites_match.this, mgf);
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

            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearSession();
                Intent myintent= new Intent(activity_handwrites_match.this,Physical_counting_activity.class);
                myintent.putExtra("LoadList","Onresume");
                startActivity(myintent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearSession();
                Intent myintent= new Intent(activity_handwrites_match.this,Physical_counting_activity.class);
                myintent.putExtra("LoadList","Onresume");
                startActivity(myintent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                submit_processid = processid.getText().toString();

                submit_branch = branch.getText().toString();

                submit_part = partno.getText().toString();
                submit_mfg = mfr.getText().toString();
                submit_qty = quantity.getText().toString();
                submit_primary = primary_bin_loc.getText().toString();
                submit_secondary = secondary_bin_loc.getText().toString();
                submit_bin_location = bin_location.getText().toString();
                submit_notes = notes.getText().toString();
                submit_uncount = false;
                submit_variance = 0;

                if (Sessiondata.getInstance().getBinlocation() != null){
                    size_binloc = Sessiondata.getInstance().getBinlocation().size();
                }else {
                    size_binloc = 0;
                }

                if (submit_processid.toString().equalsIgnoreCase("")) {


                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
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

                } else if (submit_branch.toString().equalsIgnoreCase("")) {

                    if (Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list = new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the branch")
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


                } else if (submit_part.toString().equalsIgnoreCase("")) {

                            if (Sweetalrt_list) {
                                Sweetalrt_list = false;

                                Sweetalt_list = new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
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


                } else if (submit_mfg.toString().equalsIgnoreCase("")) {

                            if (Sweetalrt_list) {
                                Sweetalrt_list = false;

                                Sweetalt_list = new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Alert!")
                                        .setContentText("Please choose the manufacturer")
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

                                Sweetalt_list = new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
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
                else if (Sessiondata.getInstance().getBinlocation() != null) {

                    size_bins = Sessiondata.getInstance().getBinlocation().size();

                    if (size_bins == 1) {

                        if (Sessiondata.getInstance().getBinlocation().get(0).getStartbin().toString().equalsIgnoreCase("")
                                && Sessiondata.getInstance().getBinlocation().get(0).getEndbin().toString().equalsIgnoreCase("")) {

                            if (checkInternetConenction()) {
                                Sessiondata.getInstance().setMfg("");

//                                new AsyncSetPartsQuantity().execute();
                                new AsyncValidateParts_v1().execute();

                            } else {

                                Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }


                        } else {

                            if (submit_primary.toString().equalsIgnoreCase("") && submit_secondary.toString().equalsIgnoreCase("")){

                                activity_handwrites_match.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Sweetalrt_list) {
                                            Sweetalrt_list = false;

                                            Sweetalt_list = new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Please choose the Bin Location")
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
                                });
                            }else {

                                if (checkInternetConenction()) {

                                    Sessiondata.getInstance().setMfg("");

//                                    new AsyncSetPartsQuantity().execute();
                                    new AsyncValidateParts_v1().execute();

                                } else {

                                    Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }
                            }
                        }

                    }else {
                        if (submit_primary.toString().equalsIgnoreCase("") && submit_secondary.toString().equalsIgnoreCase("")){

                            activity_handwrites_match.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (Sweetalrt_list) {
                                        Sweetalrt_list = false;

                                        Sweetalt_list = new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                .setTitleText("Alert!")
                                                .setContentText("Please choose the Bin Location")
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
                            });
                        }else {

                            if (checkInternetConenction()) {

//                                new AsyncSetPartsQuantity().execute();
                                new AsyncValidateParts_v1().execute();

                            } else {

                                Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }
                else {

                    if (checkInternetConenction()) {

//                        new AsyncSetPartsQuantity().execute();
                        new AsyncValidateParts_v1().execute();

                    } else {

                        Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                    }


                    }
                }


        });
    }




    public class AsyncValidateParts_v1 extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(activity_handwrites_match.this);

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

                    Session = 3;

                    if(checkInternetConenction()){

                        new AsyncSessionLoginTask().execute();

                    }else{
                        Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {

                    Log.d("validateEquipnumber", ""+ validateParts_v1);



                    if (validateParts_v1.equals("1"))
                    {

                        SweetAlertDialog sweetAlertDialog=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.WARNINGMSG_TYPE)
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

                        new AsyncSetPartsQuantity().execute();

                    }
                    else
                    {
                        Log.d("validateEquipnumber", ""+ validateParts_v1);

                    }

                }




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

                            mfr.setText(result.get(position).toString());

                            if (Sessiondata.getInstance().getGetManufacturerses() != null){
                                if (Sessiondata.getInstance().getGetManufacturerses().size() == 1){
                                    description.setText(Sessiondata.getInstance().getGetManufacturerses().get(0).getPartDesc());
                                }else {
                                    description.setText(Sessiondata.getInstance().getGetManufacturerses().get(position).getPartDesc());
                                }
                            }

                            bin_location.setText("");
                            primary_bin_loc.setText("");
                            secondary_bin_loc.setText("");


                            mDialoglist.dismiss();

                            new AsyncGetBinLocation().execute();

                        } else {

                            Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }


                }
            });



            return rowview_processid;
        }
    }

    public class CustomAdapter_binloc extends CustomAdapter {
        ArrayList<String> result;
        Context context;
        int[] imageId;
        private LayoutInflater inflater = null;

        public CustomAdapter_binloc(Context context, ArrayList<String> list) {
            super(context,list);

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
            TextView binlocation,description;
            LinearLayout linear;

        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final CustomAdapter_binloc.Holder holder = new CustomAdapter_binloc.Holder();
            final View rowview_binlocation;

            rowview_binlocation = inflater.inflate(R.layout.activity_binloc_childrow,null);

            holder.binlocation = (TextView) rowview_binlocation.findViewById(R.id.binloca);
            holder.linear = (LinearLayout) rowview_binlocation.findViewById(R.id.linear);
            holder.binlocation.setText(String.valueOf(result.get(position).toString()));
            Log.d("binlocation", "" + holder.binlocation.getText().toString());
            holder.binlocation.setTypeface(txt_face);



            holder.binlocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bin_location.setText(result.get(position).toString());
                    primary_bin_loc.setText(primarybin.get(position).toString());
                    secondary_bin_loc.setText(secondarybin.get(position).toString());
                    mDialoglist_loc.dismiss();

                }
            });



            return rowview_binlocation;
        }
    }

    public void ClearSession(){

        Sessiondata.getInstance().setPart_value("");
        Sessiondata.getInstance().setCount_value("");
        Sessiondata.getInstance().setMfg("");

    }

    public void ClearSession_aftersubmit(){

        Sessiondata.getInstance().setCounting_partnew("");
        Sessiondata.getInstance().setCounting_startbin("");
        Sessiondata.getInstance().setCounting_endbin("");
        Sessiondata.getInstance().setPart_value("");
        Sessiondata.getInstance().setCount_value("");

    }

    public class AsyncGetBinLocation extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(activity_handwrites_match.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                userToken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("UserToken", "" + userToken);

                String branchnew = branch.getText().toString();

                for (int ii = 0; ii < branchnew.length(); ii++) {

                    Character character = branchnew.charAt(ii);

                    if (character.toString().equals("-")) {

                        branchnew = branchnew.substring(0,ii);

                        Log.d("bin_trim_m", "" + branchnew);
                        break;
                    }
                }

                Log.d("bin_branch_m", "" + branchnew);

                String mgfnew = mfr.getText().toString();
                String Part_No = partno.getText().toString();
                int process_listnew = Integer.parseInt(processid.getText().toString());

                binLocations = WebServiceConsumer.getInstance().GetBinlocation(userToken, Part_No, process_listnew, mgfnew, branchnew);

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

                    if (sweetalrt) {
                        sweetalrt = false;

                        sweetalt_error = new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Cannot Bin Location!")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        bin_location.setText("");
                                        primary_bin_loc.setText("");
                                        secondary_bin_loc.setText("");
                                        sweetalrt = true;
                                        sDialog.dismiss();
                                    }
                                });
                        sweetalt_error.setCancelable(false);
                        sweetalt_error.show();
                    }

                }else if(binLocations.size() == 1){

                    if (binLocations.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = binLocations.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (binLocations.get(0).getMessage().toString().contains("Session")) {
                              Session = 0;

                            if (checkInternetConenction()) {

                                   new AsyncSessionLoginTask().execute();

                            } else {

                                Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        }
                    } else {


                    }
                }else {


                }
                }else{

                }
            }



    }

    public class AsyncSetPartsQuantity extends AsyncTask<Void, Void, Void>{

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(activity_handwrites_match.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                userToken= Sessiondata.getInstance().getLoginObject().getUsertoken();
                int process_sub = Integer.parseInt(submit_processid.toString());

                Log.d("submit_notes",""+submit_notes);
                Log.d("submit_uncount",""+submit_uncount);
                Log.d("submit_qty",""+submit_qty);
                Log.d("submit_mfg",""+submit_mfg);
                Log.d("submit_part",""+submit_part);
                Log.d("submit_primary",""+submit_primary);
                Log.d("submit_secondary",""+submit_secondary);
                Log.d("process_sub",""+process_sub);
                Log.d("submit_variance",""+submit_variance);

                partquantity_match = WebServiceConsumer.getInstance().SetPartsQuantityV4(userToken,process_sub,submit_primary,submit_secondary,submit_part,submit_qty,submit_mfg,submit_uncount,submit_notes,submit_variance);

            }
            catch (java.net.SocketTimeoutException e) {
                partquantity_match = null;
            }
            catch (Exception e) {
                partquantity_match = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            ProgressBar.dismiss();

            if(partquantity_match != null) {

                if (partquantity_match.toString().contains("Session")) {

                    String Result = partquantity_match;
                    String replace = Result.replace("Error - ", "");
                    Session = 1;

                    if(checkInternetConenction()){

                        new AsyncSessionLoginTask().execute();

                    }else{
                        Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }else {
                     if (partquantity_match.toString().contains("Conversion")){
                        if(sweetalrterror) {
                            sweetalrterror = false;

                            sweetalt=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText(partquantity_match+"!")
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
                    }
                    else {

                        if(sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Message!")
                                    .setContentText("Part quantity has been updated successfully!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrtsuccess=true;
                                            ClearSession_aftersubmit();
                                            Intent myintent= new Intent(activity_handwrites_match.this,Physical_counting_activity.class);
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
                }
            }else {

                if (sweetalrt) {
                    sweetalrt = false;

                    Sweetalt_list = new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error!")
                            .setContentText("Part quantity is not updated successfully!")
                            .setCancelText("Ok")
                            .showCancelButton(false)
                            .setConfirmClickListener(null)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override

                                public void onClick(SweetAlertDialog sDialog) {
                                    ClearSession_aftersubmit();
                                    sweetalrt = true;
                                    Intent myintent= new Intent(activity_handwrites_match.this,Physical_counting_activity.class);
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
            ProgressBar.showCommonProgressDialog(activity_handwrites_match.this);
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
                        mDialogmsg = new Dialog(activity_handwrites_match.this);
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
                                Intent intent = new Intent(activity_handwrites_match.this,LoginActivity.class);
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

                            new AsyncGetBinLocation().execute();

                        }else{

                            Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }else if(Session == 1){
                        if(checkInternetConenction()){

                            new AsyncSetPartsQuantity().execute();

                        }else{

                            Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }else if(Session == 2) {
                        if (checkInternetConenction()) {

                            new AsyncPhysicalCountingList().execute();

                        } else {

                            Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }
                    else if(Session == 3) {
                        if (checkInternetConenction()) {

                            new AsyncValidateParts_v1().execute();

                        } else {

                            Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            } else {

                if (mDialognodata == null){
                    mDialognodata = new Dialog(activity_handwrites_match.this);
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


        @Override
        public void onBackPressed() {
            super.onBackPressed();
            ClearSession();
            Intent myintent = new Intent(activity_handwrites_match.this, Physical_counting_activity.class);
            myintent.putExtra("LoadList","Onresume");
            startActivity(myintent);
            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
        }

        @Override
        protected void onResume() {
            super.onResume();
            Sweetalrt_list=true;
        }

        private boolean checkInternetConenction() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

    public void justifyListViewHeightBasedOnChildren (ListView listView)
    {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null)
        {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++)
        {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    public class AsyncPhysicalCountingList extends AsyncTask<Void, Void, Void>{

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(activity_handwrites_match.this);
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
                Log.d("branch",""+branch);
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

                            Intent myintent= new Intent(activity_handwrites_match.this,Physical_counting_activity.class);
                            startActivity(myintent);
                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                        }else if (load_partsqty.get(0).getMessage().toString().contains("Procesd Id not found")){
                            Sessiondata.getInstance().setPartsquantity(null);

                            ClearSession();

                            Intent myintent= new Intent(activity_handwrites_match.this,Physical_counting_activity.class);
                            startActivity(myintent);
                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                        }else if (load_partsqty.get(0).getMessage().toString().contains("Procesd Id not found in Bin Location")){
                            Sessiondata.getInstance().setPartsquantity(null);

                            ClearSession();

                            Intent myintent= new Intent(activity_handwrites_match.this,Physical_counting_activity.class);
                            startActivity(myintent);
                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                        }else if (load_partsqty.get(0).getMessage().toString().contains("Data Conversion Error")){
                            Sessiondata.getInstance().setPartsquantity(null);

                            ClearSession();

                            Intent myintent= new Intent(activity_handwrites_match.this,Physical_counting_activity.class);
                            startActivity(myintent);
                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
                        }
                        else {

                            String Result = load_partsqty.get(0).getMessage();
                            String replace = Result.replace("Error - ", "");

                            if (load_partsqty.get(0).getMessage().toString().contains("Session")) {
                                Session = 2;
                                if(checkInternetConenction()){

                                    new AsyncSessionLoginTask().execute();

                                }else{

                                    Toast.makeText(activity_handwrites_match.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    }else{


                        if(sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Message!")
                                    .setContentText("Part quantity has been updated successfully!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrtsuccess=true;
                                            ClearSession_aftersubmit();
                                            Intent myintent= new Intent(activity_handwrites_match.this,Physical_counting_activity.class);
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

                    ProgressBar.dismiss();

                }else {

                    if(sweetalrtsuccess) {
                        sweetalrtsuccess = false;

                        sweetalt=  new SweetAlertDialog(activity_handwrites_match.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Message!")
                                .setContentText("Part quantity has been updated successfully!")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        sweetalrtsuccess=true;
                                        ClearSession();
                                            Intent myintent= new Intent(activity_handwrites_match.this,Physical_counting_activity.class);
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


                ProgressBar.dismiss();

            }

        }
    }

}