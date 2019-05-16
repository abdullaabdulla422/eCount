package com.ebs.ecount.parts_receipt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebs.ecount.R;
import com.ebs.ecount.initial.LoginActivity;
import com.ebs.ecount.objects.GetFreightDetails;
import com.ebs.ecount.objects.GetPartsDetails;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.objects.PartObject;
import com.ebs.ecount.objects.Total_PriceWeight;
import com.ebs.ecount.swipe.FreightListViewSwipeGesture;
import com.ebs.ecount.uidesigns.ProgressBar;
import com.ebs.ecount.uidesigns.SweetAlertDialog;
import com.ebs.ecount.uidesigns.fonts.RobotoTextView;
import com.ebs.ecount.utils.Sessiondata;
import com.ebs.ecount.webutils.WebServiceConsumer;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by techunity on 22/11/16.
 */

public class AddFreightActivity extends Activity {

    Button back, btn_done, btn_spread;
    Dialog mDialogspread;
    Dialog vendor_referenceDialog;
    Dialog mDialogadd = null;
    EditText inbound, outbound, LandingCost;
    Context appContext;
    int Session = 0;
    LoginObject loginObject = null;
    LinearLayout branch_layout, order_layout;
    View view_line;
    Boolean sweetalrt = true;
    SweetAlertDialog alertinbound, Podelete;
    Typeface header_face, txt_face;
    String replace_part;
    Double landingCost = 0.0;
    Dialog mDialogSession;
    Dialog mDialognodata;
    String SetPartsHeader;
    Boolean sweetalrtsuccess = true;
    SweetAlertDialog sweetalt;
    String SetPartDetailsResultone;
    String branch_get, mfg_get, partno_get, user;
    String status_get;
    String Type = "F";
    int receiveQty, ordereds_get, receiveQty_get, oeitenum;
    String unitprice = "";
    String usertoken, SetFreightDetails;
    String Branchnum;
    int Ordernum;
    double Inboundvalue, Outboundalue;
    int orderno, oeitemnumber;
    String partno, branch;
    String Inbound, Outbound;
    int pos = 0;
    String old_inbound, old_outbound;
    Boolean Epo_Status;
    Boolean Landing_Cost;
    String Str_Landing_Cost;
    ArrayList<PartObject> partdata;
    ArrayList<GetPartsDetails> array_partsdetails;
    int order_rep, qty_no_rep, oeitunum, procqty = 0, epoordernum = 0;
    String old_part_rep, part_no_rep, mfr_rep, bin_loc_rep, transfer_his_rep, status_rep;
    SweetAlertDialog sweetdialog;
    int ordered;
    ArrayList<PartObject> dummydata;
    GetPartsDetails PartsDetails;
    int submit_btn_value = 0;
    ArrayList<Integer> getponum_new;
    ArrayList<Integer> getponum_value_new;
    private String vendor_reference = "";
    private Boolean print_lable = false;
    private ArrayList<GetFreightDetails> freightdata;
    private ListAdapternew mAdapter;
    private ListView mRecyclerView;
    FreightListViewSwipeGesture.TouchCallbacks swipeListener = new FreightListViewSwipeGesture.TouchCallbacks() {

        @Override
        public void FullSwipeListView() {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "Action_2", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void HalfSwipeListView(final int position) {

            if (Landing_Cost && !Epo_Status) {

                if (sweetalrt) {
                    sweetalrt = false;

                    Podelete = new SweetAlertDialog(AddFreightActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                            .setTitleText("Alert!")
                            .setContentText("Unable to modify Inbound and Outbound for this Freight!")
                            .setCancelText("Ok")
                            .showCancelButton(true)

                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrt = true;
                                    sDialog.dismiss();
                                }
                            });
                    Podelete.setCancelable(false);
                    Podelete.show();
                }
            } else if (!Landing_Cost && !Epo_Status) {
                if (sweetalrt) {
                    sweetalrt = false;
                    alertinbound = new SweetAlertDialog(AddFreightActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                            .setTitleText("Alert!")
                            .setContentText("Unable to modify Inbound and Outbound for this Freight!")
                            .setCancelText("Ok")
                            .showCancelButton(true)

                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrt = true;
                                    sDialog.dismiss();
                                }
                            });
                    alertinbound.setCancelable(false);
                    alertinbound.show();
                }
            } else if (freightdata.get(position).getOrdertype().toLowerCase().contains("e") ||
                    freightdata.get(position).getOrdertype().toLowerCase().contains("t") ||
                    freightdata.get(position).getOrdertype().toLowerCase().contains("p")) {

                if ((mDialogadd == null) || !mDialogadd.isShowing()) {
                    mDialogadd = new Dialog(AddFreightActivity.this);
                    mDialogadd.setCanceledOnTouchOutside(false);
                    mDialogadd.setCancelable(false);
                    mDialogadd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialogadd.setContentView(R.layout.dialog_add_freight);


                    branch_layout = (LinearLayout) mDialogadd.findViewById(R.id.branch_layout);
                    order_layout = (LinearLayout) mDialogadd.findViewById(R.id.order_layout);
                    view_line = mDialogadd.findViewById(R.id.view);

                    final EditText inboundval = (EditText) mDialogadd.findViewById(R.id.inboundval);
                    final EditText outboundval = (EditText) mDialogadd.findViewById(R.id.outboundval);


                    Log.d("outbound value", "" + Sessiondata.getInstance().getOutBound_data());


                    TextView mDialogFreeCancelButton = (TextView) mDialogadd.findViewById(R.id.freight_dialog_social_cancel);

                    TextView mDialogFreeOKButton = (TextView) mDialogadd.findViewById(R.id.freight_dialog_social_ok);

                    TextView txt_header = (TextView) mDialogadd.findViewById(R.id.txt_header);
                    TextView txt_br = (TextView) mDialogadd.findViewById(R.id.txt_br);
                    TextView txt_ordered = (TextView) mDialogadd.findViewById(R.id.txt_ordered);
                    TextView branch = (TextView) mDialogadd.findViewById(R.id.branch_no);
                    TextView order = (TextView) mDialogadd.findViewById(R.id.order_no);
                    RobotoTextView txt_inbound = (RobotoTextView) mDialogadd.findViewById(R.id.dialog_social_username);
                    RobotoTextView txt_outbound = (RobotoTextView) mDialogadd.findViewById(R.id.dialog_social_password);
                    txt_header.setTypeface(header_face);
                    txt_br.setTypeface(header_face);
                    branch.setTypeface(txt_face);
                    order.setTypeface(txt_face);
                    txt_ordered.setTypeface(header_face);
                    txt_inbound.setTypeface(header_face);
                    txt_outbound.setTypeface(header_face);
                    inboundval.setTypeface(txt_face);
                    outboundval.setTypeface(txt_face);
                    mDialogFreeCancelButton.setTypeface(header_face);
                    mDialogFreeOKButton.setTypeface(header_face);

                    String in = freightdata.get(position).getInbound();
                    String out = freightdata.get(position).getOutbound();

                    if (in.equals("0") || in.equals("0.00")) {
                        inboundval.setText("");
                    } else {
                        inboundval.setText(in);
                    }

                    if (out.equals("0") || out.equals("0.00")) {
                        outboundval.setText("");
                    } else {
                        outboundval.setText(out);
                    }


                    branch.setText(freightdata.get(position).getBranch());
                    Ordernum = (freightdata.get(position).getOrderno());
                    order.setText(String.valueOf(freightdata.get(position).getOrderno()));
                    Log.d("myordernumber", "" + freightdata.get(position).getOrderno());
                    Branchnum = String.valueOf(freightdata.get(position).getBranch());

                    inboundval.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

                    outboundval.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});


                    mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            hideKeyboardFrom(AddFreightActivity.this, view);

                            pos = position;
                            String inboundvalue = inboundval.getText().toString();
                            if (inboundvalue.isEmpty() || inboundvalue == null) {
                                inboundvalue = "0.00";
                            } else {
                                inboundvalue = String.format("%.2f", Double.valueOf(inboundvalue));
                            }


                            String outboundvalue = outboundval.getText().toString();

                            if (outboundvalue.isEmpty() || outboundvalue == null) {
                                outboundvalue = "0.00";
                            } else {
                                outboundvalue = String.format("%.2f", Double.valueOf(outboundvalue));
                            }


                            if (inboundvalue.equalsIgnoreCase("")) {
                                inboundvalue = "0.00";
                            }
                            if (outboundvalue.equalsIgnoreCase("")) {
                                outboundvalue = "0.00";
                            }

                            Inboundvalue = Double.parseDouble(inboundvalue);
                            Outboundalue = Double.parseDouble(outboundvalue);


                            Sessiondata.getInstance().setInBound_data(Double.parseDouble(inboundvalue));
                            Sessiondata.getInstance().setOutBound_data(Double.parseDouble(outboundvalue));

                            freightdata.get(pos).setInbound(inboundvalue);
                            freightdata.get(pos).setOutbound(outboundvalue);

                            Sessiondata.getInstance().setAddFreight(1);

                            mAdapter = new ListAdapternew(AddFreightActivity.this, freightdata);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                            inboundval.setText(String.valueOf(Sessiondata.getInstance().getInBound_data()));
                            outboundval.setText(String.valueOf(Sessiondata.getInstance().getOutBound_data()));

                            mDialogadd.dismiss();
                        }
                    });

                    mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            mDialogadd.dismiss();
                        }
                    });

                    mDialogadd.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                    mDialogadd.show();
                }
            } else {

            }
        }

        @Override
        public void LoadDataForScroll() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub

        }
    };
    private ArrayList<String> inbounddata;
    private ArrayList<String> outbounddata;
    private ArrayList<Total_PriceWeight> totalPriceWeights_list = new ArrayList<Total_PriceWeight>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_freight_list);

        appContext = this.getApplicationContext();

        back = (Button) findViewById(R.id.back);
        btn_done = (Button) findViewById(R.id.btn_done);
        btn_spread = (Button) findViewById(R.id.spread);

        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        inbounddata = new ArrayList<>();
        outbounddata = new ArrayList<>();

        partdata = new ArrayList<PartObject>();

        partdata = Sessiondata.getInstance().getPartObjects();

        totalPriceWeights_list = getCalculatedValue(partdata);


        Epo_Status = Sessiondata.getInstance().getEPO_Status();


        Log.d("Epo_Status", "" + Epo_Status);

        Str_Landing_Cost = Sessiondata.getInstance().getLoginObject().getLandingcost();
        dummydata = Sessiondata.getInstance().getPartObjects();

        if (Str_Landing_Cost.toString().contains("True")) {
            Landing_Cost = true;
        } else {
            Landing_Cost = false;
        }

        Log.d("Landing_Cost", "" + Landing_Cost);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sessiondata.getInstance().getFreightlists().clear();

                Intent intent = new Intent(AddFreightActivity.this, PartsReceivingDetailsActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);

            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Sessiondata.getInstance().getFreightlists() != null) {
                    Sessiondata.getInstance().getFreightlists().clear();
                }
                if (Sessiondata.getInstance().getFreightlistsnew() != null) {
                    Sessiondata.getInstance().getFreightlistsnew().clear();
                }
                Intent intent = new Intent(AddFreightActivity.this, PartsReceivingDetailsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });

        mRecyclerView = (ListView) findViewById(R.id.frieght_list);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConenction()) {

                    if (submit_btn_value == 0) {
                        submit_btn_value = 1;
                        vendor_refernce();
//                        new AsynSetPartsHeader().execute();
                    }

                } else {

                    Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_spread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if (mDialogspread == null) {
                        mDialogspread = new Dialog(AddFreightActivity.this);
                        mDialogspread.setCanceledOnTouchOutside(false);
                        mDialogspread.setCancelable(false);
                        mDialogspread.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialogspread.setContentView(R.layout.dialog_spread);
                        LandingCost = (EditText) mDialogspread.findViewById(R.id.inbound);

                        TextView mDialogFreeCancelButton = (TextView) mDialogspread.findViewById(R.id.dialog_social_cancel);

                        TextView mDialogFreeOKButton = (TextView) mDialogspread.findViewById(R.id.dialog_social_ok);

                        TextView txt_header = (TextView) mDialogspread.findViewById(R.id.txt_header);
                        RobotoTextView txt_landingcost = (RobotoTextView) mDialogspread.findViewById(R.id.dialog_social_username);
                        txt_header.setTypeface(header_face);
                        txt_landingcost.setTypeface(header_face);
                        LandingCost.setTypeface(txt_face);
                        mDialogFreeCancelButton.setTypeface(header_face);
                        mDialogFreeOKButton.setTypeface(header_face);
                        LandingCost.setText("0.0");

                        final Dialog finalMDialog = mDialogspread;
                        mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Type = "L";
                                landingCost = Double.parseDouble(LandingCost.getText().toString());
                                finalMDialog.dismiss();
                            }
                        });

                        mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                finalMDialog.dismiss();
                            }
                        });

                        mDialogspread.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        mDialogspread.show();
                    } else if (!mDialogspread.isShowing()) {
                        mDialogspread.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        mDialogspread.show();
                    }
                }

            }
        });


        freightdata = new ArrayList<>();

        freightdata = Sessiondata.getInstance().getFreightlists();

        ArrayList<GetFreightDetails> getFreightDetails = new ArrayList<GetFreightDetails>();
        getFreightDetails = freightdata;

        if (totalPriceWeights_list != null) {
            for (int i = 0; i < totalPriceWeights_list.size(); i++) {
                int order = totalPriceWeights_list.get(i).getOrder();

                for (int j = 0; j < freightdata.size(); j++) {
                    int freight_order = freightdata.get(j).getOepordno();
                    if (order == freight_order) {
                        Double total_price = totalPriceWeights_list.get(i).getTotal_price();
                        double total_weight = (totalPriceWeights_list.get(i).getTotal_weight());
//                        int d = (int) total_weight;

                        getFreightDetails.get(j).setCost(total_price);
                        getFreightDetails.get(j).setWeight(total_weight);
                    }

                }

            }

        }

        mAdapter = new ListAdapternew(this, freightdata);
        mRecyclerView.setAdapter(mAdapter);

        for (int i = 0; i < freightdata.size(); i++) {
            inbounddata.add(freightdata.get(i).getInbound());
            outbounddata.add(freightdata.get(i).getOutbound());
            Log.d("oldinbound", "" + inbounddata.get(i));
            Log.d("oldoutbound", "" + outbounddata.get(i));
        }

        if (Landing_Cost && Epo_Status) {
            btn_spread.setVisibility(View.GONE);
            final FreightListViewSwipeGesture touchListener = new FreightListViewSwipeGesture(
                    mRecyclerView, swipeListener, this);
            touchListener.SwipeType = FreightListViewSwipeGesture.Single;    //Set two options at background of list item

            mRecyclerView.setOnTouchListener(touchListener);


        } else if (Landing_Cost && !Epo_Status) {

        } else if (!Landing_Cost && Epo_Status) {
            btn_spread.setVisibility(View.GONE);
            final FreightListViewSwipeGesture touchListener = new FreightListViewSwipeGesture(
                    mRecyclerView, swipeListener, this);
            touchListener.SwipeType = FreightListViewSwipeGesture.Single;    //Set two options at background of list item

            mRecyclerView.setOnTouchListener(touchListener);

        } else {
            btn_spread.setVisibility(View.GONE);
        }
        TextView header = (TextView) findViewById(R.id.header);
        header_face = Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");
        txt_face = Typeface.createFromAsset(getAssets(), "fonts/helr45w.ttf");
        header.setTypeface(header_face);
        back.setTypeface(txt_face);
        btn_spread.setTypeface(txt_face);
        btn_done.setTypeface(header_face);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Landing_Cost && Epo_Status) {
            btn_spread.setVisibility(View.GONE);
        } else if (Landing_Cost && !Epo_Status) {

        } else if (!Landing_Cost && Epo_Status) {
            btn_spread.setVisibility(View.GONE);
        } else {
            btn_spread.setVisibility(View.GONE);
        }
    }


    private boolean checkInternetConenction() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Sessiondata.getInstance().getFreightlists() != null) {
            Sessiondata.getInstance().getFreightlists().clear();
        }
        if (Sessiondata.getInstance().getFreightlistsnew() != null) {
            Sessiondata.getInstance().getFreightlistsnew().clear();
        }
        Intent intent = new Intent(AddFreightActivity.this, PartsReceivingDetailsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
    }

    public void ClearSession() {

        Sessiondata.getInstance().setReplaceadapter(0);
        Sessiondata.getInstance().setPartReceive(0);
        Sessiondata.getInstance().setAddFreight(0);
        Sessiondata.getInstance().setInBound_data(0.0);
        Sessiondata.getInstance().setOutBound_data(0.0);
        Sessiondata.getInstance().setEPO_Status(false);


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


    public class AsynSetPartsHeader extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(AddFreightActivity.this);
        }


        @Override
        protected Void doInBackground(Void... params) {

            try {

                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                user = Sessiondata.getInstance().getLoginObject().getUsername();
                Log.d("send_landingCost", "" + landingCost);
                Log.d("send_Type", "" + Type);

                getponum_new = new ArrayList<>();
                getponum_value_new = new ArrayList<>();

                int po = 0;
                int po_value = 0;
                String branch_new = "";
                int order_new = 0;

                for (int i = 0; i < dummydata.size(); i++) {

                    ordered = dummydata.get(i).getPo();
                    po_value = 0;
                    getponum_new.add(ordered);

                    if (getponum_new.size() > 1) {

                        if (po != ordered) {

                            for (int j = 0; j < getponum_value_new.size(); j++) {

                                if (po_value == 0) {

                                    int size = getponum_value_new.size();

                                    if (ordered != getponum_value_new.get(j)) {

                                        if (size == j + 1) {

                                            getponum_value_new.add(ordered);
                                            po_value = 1;

                                            order_new = dummydata.get(i).getPo();
                                            branch_new = dummydata.get(i).getBranch();

                                            Log.d("order_new", "" + order_new);
                                            Log.d("branch_new", "" + branch_new);

                                            SetPartsHeader = WebServiceConsumer.getInstance().SetPartsHeader(usertoken, branch_new, order_new, landingCost, user);

                                        }

                                    } else {
                                        po_value = 1;
                                    }
                                } else {

                                }
                            }

                        } else {

                        }
                    } else {

                        po = ordered;
                        getponum_value_new.add(ordered);
                        order_new = dummydata.get(i).getPo();
                        branch_new = dummydata.get(i).getBranch();

                        Log.d("order_new", "" + order_new);
                        Log.d("branch_new", "" + branch_new);

                        SetPartsHeader = WebServiceConsumer.getInstance().SetPartsHeader(usertoken, branch_new, order_new, landingCost, user);


                    }

                }

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

            submit_btn_value = 0;

            if (SetPartsHeader != null) {
                Log.d("SetPartsHeader", "" + SetPartsHeader.toString());

                if (SetPartsHeader.toString().contains("Session")) {
                    ProgressBar.dismiss();

                    String Result = SetPartsHeader;
                    String replace = Result.replace("Error - ", "");

                    Session = 0;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (checkInternetConenction()) {

                        new AsynReplacePartDetailsTask().execute();

                    } else {

                        Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }
            } else {
                if (checkInternetConenction()) {


                    new AsynReplacePartDetailsTask().execute();

                } else {

                    Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public class AsynReplacePartDetailsTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                user = Sessiondata.getInstance().getLoginObject().getUsername();

                Log.d("SetPartsHeader1", "" + Integer.parseInt(SetPartsHeader));

                if (partdata != null) {

                    Log.d("CountReplacePartsize", "" + partdata.size());
                    for (int i = 0; i < partdata.size(); i++) {

                        status_rep = partdata.get(i).getStatus();

                        if (partdata.get(i).getReplacedPart() != null) {
                            if (partdata.get(i).getReplacedPart()) {

                                if (status_rep.contains("R")) {
                                    status_rep = "I";
//                                partdata.get(i).setStatus("I");
                                } else if (status_rep.contains("I")) {
                                    status_rep = "R";
//                                partdata.get(i).setStatus("R");
                                } else if (status_rep.contains("C")) {
                                    status_rep = "C";
//                                partdata.get(i).setStatus("R");
                                }

                            }
                        }

                        if ((!status_rep.toString().contains("I")) && dummydata.get(i).getReplacedPart()) {

                            if (status_rep.equals("R")) {
                                status_rep = "I";
                            }

                            order_rep = partdata.get(i).getPo();
                            old_part_rep = partdata.get(i).getOld_part();
                            part_no_rep = partdata.get(i).getPart();
                            qty_no_rep = Integer.parseInt(partdata.get(i).getPo_qty());
                            mfr_rep = partdata.get(i).getMfg();
                            bin_loc_rep = partdata.get(i).getBinlocation();
                            transfer_his_rep = partdata.get(i).getTransfer();
                            oeitunum = partdata.get(i).getOeitenum();
                            epoordernum = dummydata.get(i).getEpoorderno();

                            if (!partdata.get(i).getReceipt_qty().equals("")) {
                                procqty = Integer.parseInt(partdata.get(i).getReceipt_qty());
                            } else {
                                procqty = 0;
                            }
                            Log.d("transfer_his", "" + transfer_his_rep);

//                        replace_part= WebServiceConsumer.getInstance().SetRepalcePartDetails(usertoken,order_rep,mfr_rep,old_part_rep,part_no_rep,qty_no_rep,bin_loc_rep,transfer_his_rep,user,oeitunum);
//                        replace_part= WebServiceConsumer.getInstance().SetRepalcePartDetailsV2(usertoken,order_rep,mfr_rep,old_part_rep,part_no_rep,qty_no_rep,bin_loc_rep,transfer_his_rep,user,oeitunum,procqty);
//                        replace_part= WebServiceConsumer.getInstance().SetRepalcePartDetailsV3(usertoken,order_rep,mfr_rep,old_part_rep,part_no_rep,qty_no_rep,bin_loc_rep,transfer_his_rep,user,oeitunum,procqty,epoordernum);
//                        replace_part = WebServiceConsumer.getInstance().SetRepalcePartDetailsV4(usertoken, order_rep, mfr_rep, old_part_rep, part_no_rep, qty_no_rep, bin_loc_rep, transfer_his_rep, user, oeitunum, procqty, epoordernum,"R");
                            replace_part = WebServiceConsumer.getInstance().SetRepalcePartDetailsV4(usertoken, order_rep, mfr_rep, old_part_rep, part_no_rep, qty_no_rep, bin_loc_rep, transfer_his_rep, user, oeitunum, procqty, epoordernum, status_rep,
                                    vendor_reference, print_lable);
                        }
                    }
                }
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

            if (replace_part != null) {
                Log.d("ReplacePart", "" + replace_part.toString());

                if (replace_part.toString().contains("Session")) {

                    String Result = replace_part;
                    String replace = Result.replace("Error - ", "");

                    Session = 1;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (checkInternetConenction()) {


                        new AsyncSetPartDetailsResult().execute();

                    } else {

                        Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }


                }

            } else {
                if (checkInternetConenction()) {


                    new AsyncSetPartDetailsResult().execute();

                } else {

                    Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class AsyncSetPartDetailsResult extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                user = Sessiondata.getInstance().getLoginObject().getUsername();

                Log.d("SetPartsHeader2", "" + Integer.parseInt(SetPartsHeader));

                if (partdata != null) {
                    Log.d("CountSetPartDetailssize", "" + partdata.size());
                    for (int i = 0; i < partdata.size(); i++) {
                        int oepordnum = partdata.get(i).getEpoorderno();
                        ordereds_get = partdata.get(i).getPo();
                        branch_get = partdata.get(i).getBranch();
                        mfg_get = partdata.get(i).getMfg();
                        partno_get = partdata.get(i).getPart();

                        Double unit_price;
                        if (partdata.get(i).getUnit().isEmpty()) {
                            unit_price = 0.0;
                        } else {
                            unit_price = Double.parseDouble(partdata.get(i).getUnit());
                        }

                        if (unit_price == 0 || unit_price == 0.0) {
                            unit_price = 0.0;
                        } else {
                            unit_price = Double.parseDouble(partdata.get(i).getUnit());
                        }

                        Log.d("receiveQty_get", "" + partdata.get(i).getReceipt_qty().toString());
                        if (partdata.get(i).getReceipt_qty().toString().isEmpty()) {
                            receiveQty_get = 0;
                        } else {
                            receiveQty_get = Integer.parseInt(partdata.get(i).getReceipt_qty());
                        }

                        status_get = partdata.get(i).getStatus();

                        if (partdata.get(i).getReplacedPart() != null) {
                            if (partdata.get(i).getReplacedPart()) {

                                if (status_get.contains("R")) {
                                    status_get = "I";
//                                    partdata.get(i).setStatus("I");
                                } else if (status_get.contains("I")) {
                                    status_get = "R";
//                                    partdata.get(i).setStatus("R");
                                }

                            }
                        }

                        oeitenum = partdata.get(i).getOeitenum();
                        Log.d("oepordnum_get", "" + oepordnum);
                        Log.d("ordereds_get", "" + ordereds_get);
                        Log.d("partno_get", "" + partno_get);
                        Log.d("status", "" + status_get);
                        Log.d("branch", "" + branch_get);
                        Log.d("mfg_get", "" + mfg_get);
                        Log.d("receiveQty_get", "" + receiveQty_get);
                        Log.d("getOeitemnum", "" + oeitenum);
                        Log.d("unit_price", "" + unit_price);

                        String ordiclocmain = partdata.get(i).getOld_binlocation();
                        String ordiclocsec = partdata.get(i).getOld_sec_bin();
                        String iclocmain = partdata.get(i).getBinlocation();
                        String iclocsec = partdata.get(i).getSec_bin();

                        int receipt;
                        if (partdata.get(i).getReceipt_qty().toString().isEmpty()) {
                            receipt = 0;
                        } else {
                            receipt = Integer.parseInt(partdata.get(i).getReceipt_qty().toString());
                        }
                        Log.d("getReceipt_qty", " 1 " + partdata.get(i).getReceipt_qty().toString());

                        Boolean flag = partdata.get(i).getFlag();

                        if (receipt == 0 && status_get.toString().contains("C") && flag == true) {
                            Log.d("Success ", " 1 " + i);
                            SetPartDetailsResultone = WebServiceConsumer.getInstance().SetPartDetailsV4(usertoken, oepordnum, ordereds_get, branch_get, mfg_get, partno_get, receiveQty_get, status_get, user, oeitenum, unit_price, ordiclocmain, ordiclocsec, iclocmain, iclocsec, vendor_reference, print_lable);
                        } else if (receipt != 0) {
                            if (!status_get.toString().contains("R") && (!partdata.get(i).getReplacedPart())) {
                                Log.d("Success ", " 1 " + i);
                                SetPartDetailsResultone = WebServiceConsumer.getInstance().SetPartDetailsV4(usertoken, oepordnum, ordereds_get, branch_get, mfg_get, partno_get, receiveQty_get, status_get, user, oeitenum, unit_price, ordiclocmain, ordiclocsec, iclocmain, iclocsec, vendor_reference, print_lable);
                            }
                        }
                    }
                }

            } catch (java.net.SocketTimeoutException e) {
                SetPartDetailsResultone = null;
            } catch (Exception e) {
                SetPartDetailsResultone = null;
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (SetPartDetailsResultone != null) {

                if (SetPartDetailsResultone.toString().contains("Session")) {

                    String Result = SetPartDetailsResultone;
                    String replace = Result.replace("Error - ", "");

                    Session = 2;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {

                    if (Landing_Cost && Epo_Status) {

                        if (checkInternetConenction()) {
                            new AsyncSetFreightDetails().execute();
                        } else {
                            Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }

                    } else if (Landing_Cost && !Epo_Status) {
                        ProgressBar.dismiss();

                        if (sweetalrtsuccess) {
                            sweetalrtsuccess = false;

                            sweetalt = new SweetAlertDialog(AddFreightActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Parts Detail Updated Successfully!")
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {

                                            sweetalrtsuccess = true;

                                            ClearSession();

                                            Intent intent = new Intent(AddFreightActivity.this, PartReceiptActivity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);

                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }

                    } else if (!Landing_Cost && Epo_Status) {
                        if (checkInternetConenction()) {
                            new AsyncSetFreightDetails().execute();
                        } else {
                            Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (checkInternetConenction()) {
                            new AsyncSetFreightDetails().execute();
                        } else {
                            Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } else {
                if (checkInternetConenction()) {
                    new AsyncSetFreightDetails().execute();
                } else {
                    Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class AsyncSetFreightDetails extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                Log.d("SetPartsHeader3", "" + Integer.parseInt(SetPartsHeader));

                Log.d("CountSetFreightDetails", "" + freightdata.size());

                for (int i = 0; i < freightdata.size(); i++) {

                    orderno = freightdata.get(i).getOrderno();
                    branch = freightdata.get(i).getBranch();
                    Inbound = freightdata.get(i).getInbound();
                    Outbound = freightdata.get(i).getOutbound();
                    oeitemnumber = freightdata.get(i).getOeitemnum();

                    old_inbound = inbounddata.get(i);
                    old_outbound = outbounddata.get(i);
                    Log.d("oldinbound1", "" + old_inbound);
                    Log.d("oldoutbound1", "" + old_outbound);

                    Log.d("inbound1", "" + Inbound);
                    Log.d("outbound1", "" + Outbound);

                    if (!Inbound.equals(old_inbound) || !Outbound.equals(old_outbound)) {

                        SetFreightDetails = WebServiceConsumer.getInstance().SetFreightDetails(usertoken, orderno, branch, Inbound, Outbound, oeitemnumber);

                    }
                }
            } catch (java.net.SocketTimeoutException e) {
                SetFreightDetails = null;
            } catch (Exception e) {
                SetFreightDetails = null;
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ProgressBar.dismiss();

            if (SetFreightDetails != null) {

                if (SetFreightDetails.toString().contains("Session")) {

                    String Result = SetFreightDetails;
                    String replace = Result.replace("Error - ", "");
                    Session = 3;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {

                    if (sweetalrtsuccess) {
                        sweetalrtsuccess = false;

                        sweetalt = new SweetAlertDialog(AddFreightActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success!")
                                .setContentText("Parts Detail Updated Successfully!")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {

                                        sweetalrtsuccess = true;
                                        ClearSession();
                                        Intent intent = new Intent(AddFreightActivity.this, PartReceiptActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);

                                    }
                                });
                        sweetalt.setCancelable(false);
                        sweetalt.show();
                    }

                }
            } else {
                if (sweetalrtsuccess) {
                    sweetalrtsuccess = false;

                    sweetalt = new SweetAlertDialog(AddFreightActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success!")
                            .setContentText("Parts Detail Updated Successfully!")
                            .setCancelText("Ok")
                            .showCancelButton(false)
                            .setCancelClickListener(null)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override

                                public void onClick(SweetAlertDialog sDialog) {

                                    sweetalrtsuccess = true;
                                    ClearSession();
                                    Intent intent = new Intent(AddFreightActivity.this, PartReceiptActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);

                                }
                            });
                    sweetalt.setCancelable(false);
                    sweetalt.show();
                }

            }
        }
    }

    public class AsyncSessionLoginTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(AddFreightActivity.this);
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
                        mDialogSession = new Dialog(AddFreightActivity.this);
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
                                Intent intent = new Intent(AddFreightActivity.this, LoginActivity.class);
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

                            new AsynSetPartsHeader().execute();

                        } else {

                            Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    } else if (Session == 1) {
                        if (checkInternetConenction()) {

                            new AsynReplacePartDetailsTask().execute();

                        } else {

                            Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    } else if (Session == 2) {
                        if (checkInternetConenction()) {

                            new AsyncSetPartDetailsResult().execute();

                        } else {

                            Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    } else if (Session == 3) {
                        if (checkInternetConenction()) {

                            new AsyncSetFreightDetails().execute();

                        } else {

                            Toast.makeText(AddFreightActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }


                }
            } else {
                ProgressBar.dismiss();

                if (mDialognodata == null) {
                    mDialognodata = new Dialog(AddFreightActivity.this);
                    mDialognodata.setCanceledOnTouchOutside(false);
                    mDialognodata.setCancelable(false);
                    mDialognodata.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialognodata.setContentView(R.layout.dialog_no_data);

                    TextView mDialogFreeOKButton = (TextView) mDialognodata.findViewById(R.id.dialog_social_ok);

                    TextView txt_dialog = (TextView) mDialognodata.findViewById(R.id.txt_dialog);
                    TextView mDialogtxt_header = (TextView) mDialognodata.findViewById(R.id.txt_header);

                    String Result = loginObject.getMessage();

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

    public class ListAdapternew extends BaseAdapter {
        private ArrayList<GetFreightDetails> data;
        private LayoutInflater inflater = null;

        public ListAdapternew(Context context, ArrayList<GetFreightDetails> basicList) {
            data = basicList;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.freight_childrow, parent, false);
                holder.inbound_value = (TextView) convertView.findViewById(R.id.inbound_value);
                holder.outbound_value = (TextView) convertView.findViewById(R.id.outbound_value);
                holder.cust_name = (TextView) convertView.findViewById(R.id.customer_value);
                holder.order_value = (TextView) convertView.findViewById(R.id.order_value);
                holder.weight_value = (TextView) convertView.findViewById(R.id.weight_value);
                holder.value = (TextView) convertView.findViewById(R.id.value);
                holder.branch_value = (TextView) convertView.findViewById(R.id.branch_value);
                holder.eporder = (TextView) convertView.findViewById(R.id.eporder);

                holder.txt_cust = (TextView) convertView.findViewById(R.id.txt_cust);
                holder.txt_ordered = (TextView) convertView.findViewById(R.id.txt_ordered);
                holder.txt_weight = (TextView) convertView.findViewById(R.id.txt_weight);
                holder.txt_value = (TextView) convertView.findViewById(R.id.txt_value);
                holder.txt_indound = (TextView) convertView.findViewById(R.id.txt_indound);
                holder.txt_outbound = (TextView) convertView.findViewById(R.id.txt_outbound);
                holder.txt_branch = (TextView) convertView.findViewById(R.id.txt_branch);
                holder.txt_eporder = (TextView) convertView.findViewById(R.id.txt_eporder);

                holder.txt_cust.setTypeface(header_face);
                holder.txt_ordered.setTypeface(header_face);
                holder.txt_weight.setTypeface(header_face);
                holder.txt_value.setTypeface(header_face);
                holder.txt_indound.setTypeface(header_face);
                holder.txt_outbound.setTypeface(header_face);
                holder.txt_branch.setTypeface(header_face);
                holder.txt_eporder.setTypeface(header_face);


                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.inbound_value.setText(String.valueOf(data.get(position).getInbound()));
            holder.outbound_value.setText(String.valueOf(data.get(position).getOutbound()));
            holder.cust_name.setText(data.get(position).getCustname());
            String weight_format = String.valueOf(data.get(position).getWeight());
            weight_format = String.format("%.3f", Double.valueOf(weight_format));
            holder.weight_value.setText(weight_format);
            holder.order_value.setText(String.valueOf(data.get(position).getOrderno()));
            holder.branch_value.setText(String.valueOf(data.get(position).getBranch()));
            String cost_format = String.valueOf(data.get(position).getCost());
            cost_format = String.format("%.2f", Double.valueOf(cost_format));
            holder.value.setText(cost_format);

            if (data.get(position).getOepordno() == 0) {
                holder.eporder.setText("");
            } else {
                holder.eporder.setText(String.valueOf(data.get(position).getOepordno()));
            }

            holder.inbound_value.setTypeface(txt_face);
            holder.outbound_value.setTypeface(txt_face);
            holder.cust_name.setTypeface(txt_face);
            holder.order_value.setTypeface(txt_face);
            holder.weight_value.setTypeface(txt_face);
            holder.value.setTypeface(txt_face);
            holder.branch_value.setTypeface(txt_face);
            holder.eporder.setTypeface(txt_face);


            return convertView;
        }

        public class ViewHolder {
            public TextView cust_name, order_value, weight_value, value, inbound_value, outbound_value, branch_value, eporder;
            public TextView txt_cust, txt_ordered, txt_weight, txt_value, txt_indound, txt_outbound, txt_branch, txt_eporder;


        }
    }


    public void vendor_refernce() {

        vendor_referenceDialog = new Dialog(AddFreightActivity.this);
        vendor_referenceDialog.setCanceledOnTouchOutside(false);
        vendor_referenceDialog.setCancelable(false);
        vendor_referenceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        vendor_referenceDialog.setContentView(R.layout.vendor_reference);

        TextView submit = (TextView) vendor_referenceDialog.findViewById(R.id.dialog_submit);
        TextView cancel = (TextView) vendor_referenceDialog.findViewById(R.id.dialog_social_cancel);
        final EditText edit_vendor_reference = (EditText) vendor_referenceDialog.findViewById(R.id.edit_vendor_reference);

        final CheckBox checkBox = (CheckBox) vendor_referenceDialog.findViewById(R.id.checkbox_vendor_reference);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vendor_reference = edit_vendor_reference.getText().toString();

                if (checkBox.isChecked()) {
                    print_lable = true;
                } else {
                    print_lable = false;
                }

                new AsynSetPartsHeader().execute();
                vendor_referenceDialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_btn_value = 0;
                vendor_referenceDialog.dismiss();

            }
        });


        vendor_referenceDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;
        vendor_referenceDialog.show();


    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int i, int i1) {
            mPattern = Pattern.compile("[0-9]*+((\\.[0-9]?)?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private ArrayList<Total_PriceWeight> getCalculatedValue(ArrayList<PartObject> partdata) {

        ArrayList<Total_PriceWeight> total_list = new ArrayList<Total_PriceWeight>();

        ArrayList<Total_PriceWeight> same_order_list = new ArrayList<Total_PriceWeight>();
        ArrayList<Total_PriceWeight> temp_list = new ArrayList<Total_PriceWeight>();


        for (PartObject object : partdata) {
            Total_PriceWeight model = new Total_PriceWeight();
//            int order = object.getOrder();
            int order = object.getEpoorderno();
            int qty = Integer.parseInt(object.getReceipt_qty());
            double price = Double.valueOf(object.getUnit());
            Double weight = object.getWeight();

            Double total_weight = (weight * qty);
            Double total_price = (price * qty);

            model.setTotal_price(total_price);
            model.setTotal_weight(total_weight);
            model.setOrder(order);

            total_list.add(model);

        }

        temp_list = total_list;


        for (int i = 0; i < total_list.size(); i++) {
            ArrayList<Integer> integers = new ArrayList<Integer>();

            Boolean check = false;
//            int order = total_list.get(i).getOrder();
            int order = total_list.get(i).getOrder();
            double price = total_list.get(i).getTotal_price();
            double weight = total_list.get(i).getTotal_weight();


            for (int j = 0; j < temp_list.size(); j++) {

                if (i != j) {
                    int temp_order = temp_list.get(j).getOrder();
                    double temp_price = temp_list.get(j).getTotal_price();
                    double temp_weight = temp_list.get(j).getTotal_weight();

                    if (order != 0) {
                        if (order == temp_order) {
                            Log.d("Order_1", " = " + i + " == " + j);

                            price = price + temp_price;
                            weight = weight + temp_weight;
                            total_list.set(j, new Total_PriceWeight(0.0, 0.0, 0));
                        }
                    }

                }
            }

            if (order != 0) {
//                if (weight != 0.0 && price != 0.0)
//                {
                Total_PriceWeight model = new Total_PriceWeight();
                model.setTotal_weight(weight);
                model.setTotal_price(price);
                model.setOrder(order);
                same_order_list.add(model);

//                }
            }
        }

        return same_order_list;

    }

    private ArrayList<Total_PriceWeight> getValue() {

        ArrayList<Total_PriceWeight> list = new ArrayList<Total_PriceWeight>();

        Total_PriceWeight weight0 = new Total_PriceWeight(5.0, 5.0, 10);
        Total_PriceWeight weight1 = new Total_PriceWeight(5.0, 5.0, 11);
        Total_PriceWeight weight2 = new Total_PriceWeight(5.0, 5.0, 12);//15
        Total_PriceWeight weight3 = new Total_PriceWeight(5.0, 5.0, 13);
        Total_PriceWeight weight4 = new Total_PriceWeight(5.0, 5.0, 14);
        Total_PriceWeight weight5 = new Total_PriceWeight(5.0, 5.0, 15);
        Total_PriceWeight weight6 = new Total_PriceWeight(5.0, 5.0, 16);//20
        Total_PriceWeight weight7 = new Total_PriceWeight(5.0, 5.0, 17);
        Total_PriceWeight weight8 = new Total_PriceWeight(5.0, 5.0, 18);
        Total_PriceWeight weight9 = new Total_PriceWeight(5.0, 5.0, 19);//15

        list.add(weight0);
        list.add(weight1);
        list.add(weight2);
        list.add(weight3);
        list.add(weight4);
        list.add(weight5);
        list.add(weight6);
        list.add(weight7);
        list.add(weight8);
        list.add(weight9);

        return list;
    }

}

