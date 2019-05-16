package com.ebs.ecount.parts_receipt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.usb.UsbDevice;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import com.cognex.dataman.sdk.ConnectionState;
import com.cognex.mobile.barcode.sdk.ReadResult;
import com.cognex.mobile.barcode.sdk.ReadResults;
import com.cognex.mobile.barcode.sdk.ReaderDevice;
import com.ebs.ecount.R;
import com.ebs.ecount.initial.LoginActivity;
import com.ebs.ecount.objects.GetFreightDetails;
import com.ebs.ecount.objects.GetMultiplePONumber;
import com.ebs.ecount.objects.GetPartsDetails;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.objects.MultiplePOWithStatus;
import com.ebs.ecount.objects.PartObject;
import com.ebs.ecount.objects.ValidateOrders;
import com.ebs.ecount.swipe.ListViewSwipeGesture;
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
 * Created by cbetechubt-016 on 27/4/18.
 */

public class PartsReceivingDetailsActivity extends Activity implements ReaderDevice.OnConnectionCompletedListener, ReaderDevice.ReaderDeviceListener {
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private static final DeviceType[] deviceTypeValues = DeviceType.values();
    public static ReaderDevice readerDevice;
    public static boolean isDevicePicked = false;
    public static boolean dialogAppeared = false;

    public static String selectedDevice = "";
    public static boolean fragmentActive = false;
    public static int flag_submit = 0;
    Button Clear, Complete, back;
    ImageView po_search;
    Dialog mDialogs = null;

    Dialog vendor_referenceDialog;
    private String vendor_reference = "";
    Boolean print_lable = false;

    Dialog mDialogponotequalnew = null;
    EditText part_no;
    String str_po_no = "";
    int oeitunumber_value = 0;
    int Multipe_POFirst;
    EditText inbound, outbound, LandingCost;
    String landed_values;
    String Type;
    int submit_land = 0;
    int replace_partchk = 0;
    Dialog mDialogspread, mDialoglandcost;
    Button add_part;
    ListView part_list;
    String usertoken;
    Boolean sweetaddpo = true;
    String entered_partno, chk_orderno;
    SweetAlertDialog sweetaddalret;
    int validateindex;
    int validatePosition = 0;
    int validateindex_lineitem;
    int validatePosition_lineitem = 0;
    ValidateOrders validateOrders;
    TextView txt_landcost, landcost_value;
    LinearLayout landed_cost;
    ArrayList<PartObject> partdata;
    String old_part_rep, part_no_rep, mfr_rep, bin_loc_rep, transfer_his_rep, status_rep;
    int order_rep, qty_no_rep, oeitunum, procqty = 0, epoordernum = 0;
    int receiveQtys, ordereds_gets, receiveQty_gets, oeitenum;
    String branch_gets, mfg_gets, partno_gets, unitprice;
    String status_gets;
    Boolean Epo_Status;
    Boolean Landing_Cost;
    String Str_Landing_Cost = "";
    String SetPartsHeader;
    String replace_part;
    ArrayList<GetPartsDetails> array_partsdetails;
    GetPartsDetails PartsDetails;
    ArrayList<GetPartsDetails> PartsDetails_list;
    LoginObject loginObject = null;
    ArrayList<PartObject> dummydata;
    Dialog mlistdialog;
    SweetAlertDialog mDialogreplace;
    SweetAlertDialog mDialogponotequal;
    Dialog mMultipleDialog;
    Dialog mDialogmsg;
    Boolean sweetalrtsuccess = true;
    SweetAlertDialog sweetaltsuccess, sweetdialog, sweetalt;
    Dialog mDialognodata;
    Dialog mDialogwarning;
    Dialog finalMlistdialog;
    SweetAlertDialog ponoenter, addpono, Podelete;
    Boolean sweetalrt = true;
    ArrayList<Integer> selectedList = new ArrayList<Integer>();
    int po;
    String part, receiptQty, po_Qty, sec_bin, prior, decription, unit, document, bin;
    Typeface header_face, txt_face;
    ArrayList<GetFreightDetails> freightdetails;
    int ordered;
    int oeporderno;
    ArrayList<GetFreightDetails> add_freightdetails;
    ArrayList<GetMultiplePONumber> data;
    ArrayList<GetMultiplePONumber> MultipleQtydata;
    ArrayList<MultiplePOWithStatus> MultipleQtywithstatus;
    ListView ListMultiplepo;
    String SetPartDetailsResult;
    String SetPartDetailsResultnew;
    String SetPartDetailsResultone;
    CustomAdapter_multiplepo_new adapter_po_new;
    Double landingCost = 0.0;
    Double land_value;

    String status;
    int receiveQty;
    int epoorderno = 0;
    int oeitunumber = 0;
    String part_delete;
    String branch, mfg, partno, user;
    String branch_value;
    int qtyrec, ordereds;
    int position = 0;
    int index;
    int Session = 0;
    int finalindex;
    Double landed_submit = 0.0;
    int land_cost = 0;
    Boolean add_part_status = false;
    SweetAlertDialog part_alrt;
    ArrayList<Integer> Listreceive_qty = new ArrayList<>();
    ArrayList<Integer> ListUnitPrice = new ArrayList<>();
    int multiple_receive_value = 0, single_receive_value = 0;
    String Partnew;
    int receivepart_value = 0;
    int complete_btn_value = 0;
    ArrayList<Integer> getponum;
    ArrayList<Integer> getordernew;
    ArrayList<Integer> getponum_value;
    ArrayList<Integer> getordernew_value;
    ArrayList<Integer> getponum_new;
    ArrayList<Integer> getponum_value_new;
    Dialog mDialogscan = null;
    int isDeviceConnected = 1;
    HashMap<String, UsbDevice> usbDevices;
    ArrayList<GetPartsDetails> getparts;
    int click_pos = 0;
    String item_unitprice = "";
    String item_startbin = "";
    String item_endbin = "";
    int LineItem_Po = 0;
    String LineItem_Part = "";
    boolean[] thumbnailsselection_new;
    ArrayList<MultiplePOWithStatus> received_list;
    MultiplePOWithStatus multiplePOWithStatus;
    ArrayList<Integer> list_pos, po_QTY, received_QTY;
    ArrayList<PartObject> sorted_partno;
    ArrayList<MultiplePOWithStatus> store_multiplepovalue;
    Button submit_entered_items, blanket_receipt;
    Drawable grey_drawable, blue_drawable;
    ListViewSwipeGesture touchListener;
    Boolean flag = false;
    int submit_items = 0;
    int has_replacement_Part = 0;
    private CustomAdapter adapter;
    private int count;

    String partsbranch, partsMfg, partnumber, partsdescription, partsponumber, partsoeprodernum, partslocation;
    int partsorderqty, partsreceivedqty;

    String PrintLabelsForPart;
    ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {

//        @Override
//        public void FullSwipeListView(final int position, String status) {
//            // TODO Auto-generated method stub
//
//
//
//
//        }

        @Override
        public void FullSwipeListView(final int position, String status) {

            if (status.equalsIgnoreCase("INCOMPLETE")) {

                if (sweetalrt) {
                    sweetalrt = false;

                    Podelete = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You Want to Incomplete This Part# " + part_delete)
                            .setCancelText("No,Cancel")
                            .setConfirmText("Yes,Incomplete it")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrt = true;
                                    sDialog.dismiss();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    dummydata.get(position).setStatus("I");
                                    dummydata.get(position).setReceipt_qty("");
                                    adapter.notifyDataSetChanged();

                                    sweetalrt = true;

                                    sDialog.dismiss();

                                }
                            });


                    Podelete.setCancelable(false);
                    Podelete.show();
                }
            } else if (status.equalsIgnoreCase("COMPLETE")) {

                if (sweetalrt) {
                    sweetalrt = false;

                    part_delete = dummydata.get(position).getPart();
                    Podelete = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You Want to Complete This Part# " + part_delete)
                            .setCancelText("No,Cancel")
                            .setConfirmText("Yes,Complete it")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrt = true;
                                    sDialog.dismiss();

                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.setTitleText("Completed!")
                                            .setContentText("Part# " + part_delete + " has been Completed!")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    sweetalrt = true;
                                    dummydata.get(position).setStatus("C");
                                    dummydata.get(position).setFlag(true);
                                    adapter.notifyDataSetChanged();
                                    sDialog.dismiss();
                                }
                            });
                    Podelete.setCancelable(false);
                    Podelete.show();
                }

            }

        }

        @Override
        public void HalfSwipeListView(final int position, String status) {
            // TODO Auto-generated method stub

            if (sweetalrt) {
                sweetalrt = false;

                part_delete = dummydata.get(position).getPart();
                Log.d("part_delete", "" + part);

                if (status.contains("REPLACE")) {

                    Podelete = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You Want to Replace This Part# " + part_delete)
                            .setCancelText("No,Cancel")
                            .setConfirmText("Yes,Replace it")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrt = true;
                                    sDialog.dismiss();

                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.setTitleText("Removed!")
//                                        .setContentText("Part# " + part_delete + " has been removed!")
//                                        .setConfirmText("OK")
//                                        .showCancelButton(false)
//                                        .setCancelClickListener(null)
//                                        .setConfirmClickListener(null)
//                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//
//                                        sweetalrt = true;
//                                        dummydata.remove(position);
//                                        adapter.notifyDataSetChanged();
                                    Sessiondata.getInstance().setPo_recive("");
                                    Sessiondata.getInstance().setPo_recive_value("");
                                    sweetalrt = true;
                                    Sessiondata.getInstance().setPartObjects(dummydata);
                                    Sessiondata.getInstance().setPartReceive(2);
                                    Intent intent = new Intent(PartsReceivingDetailsActivity.this, ReplacePartsActivity.class);
                                    intent.putExtra("PartNo", "");
                                    intent.putExtra("OldPartNo", dummydata.get(position).getPart());
                                    intent.putExtra("Mfr", dummydata.get(position).getMfg());
                                    intent.putExtra("Order_no", dummydata.get(position).getPo());
                                    intent.putExtra("Epoorder_no", dummydata.get(position).getEpoorderno());
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                    sDialog.dismiss();

                                }
                            });

                } else if (status.contains("REMOVE")) {


                    Podelete = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You Want to Remove This Part# " + part_delete)
                            .setCancelText("No,Cancel")
                            .setConfirmText("Yes,Remove it")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrt = true;
                                    sDialog.dismiss();

                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.setTitleText("Removed!")
                                            .setContentText("Part# " + part_delete + " has been removed!")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                    String qty = dummydata.get(position).getReceipt_qty();
                                    String old_partnumber = dummydata.get(position).getOld_part();
                                    int order_no = dummydata.get(position).getEpoorderno();

                                    sweetalrt = true;
                                    dummydata.remove(position);

//                                    dummydata.get(i).setReceipt_qty(String.valueOf( Integer.parseInt() ));


//                                    for(int i = 0 ; i < dummydata.size() ; i++){
//
//                                        if(dummydata.get(i).getPart().equals(old_partnumber) &&
//                                                dummydata.get(i).getStatus().contains("R") &&
//                                               dummydata.get(i).getEpoorderno() == order_no ){
//
//                                            dummydata.get(i).setReceipt_qty(String.valueOf( Integer.parseInt(dummydata.get(i).getReceipt_qty()) - Integer.parseInt(qty) ));
//
//                                        }
//
//                                    }
//
//

                                    for (int i = 0; i < dummydata.size(); i++) {

                                        if (dummydata.get(i).getStatus().contains("R") &&
                                                dummydata.get(i).getReplacedPart()
                                                && dummydata.get(i).getEpoorderno() == order_no) {

                                            for (int j = 0; j < dummydata.size(); j++) {

                                                if (dummydata.get(j).getReplacedPart() &&
                                                        dummydata.get(j).getStatus().contains("I") &&
                                                        dummydata.get(j).getOld_part().equals(dummydata.get(i).getPart()) &&
                                                        dummydata.get(j).getEpoorderno() == order_no) {

                                                    has_replacement_Part = 1;
                                                }

                                            }

                                            if (has_replacement_Part == 1) {
                                                has_replacement_Part = 0;
                                            } else {
                                                has_replacement_Part = 0;
                                                dummydata.get(i).setStatus("I");
                                                dummydata.get(i).setReplacedPart(false);

                                            }

                                        }

                                    }

                                    adapter.notifyDataSetChanged();

//                                    Sessiondata.getInstance().setPo_recive("");
//                                    Sessiondata.getInstance().setPo_recive_value("");
//                                    sweetalrt = true;
//                                    Sessiondata.getInstance().setPartObjects(dummydata);
//                                    Sessiondata.getInstance().setPartReceive(2);
//                                    Intent intent = new Intent(PartsReceivingDetailsActivity.this, ReplacePartsActivity.class);
//                                    intent.putExtra("PartNo","");
//                                    intent.putExtra("OldPartNo",dummydata.get(position).getPart());
//                                    intent.putExtra("Mfr",dummydata.get(position).getMfg());
//                                    intent.putExtra("Order_no",dummydata.get(position).getPo());
//                                    startActivity(intent);
//                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                    sDialog.dismiss();


                                }
                            });


                }
                //   if (dummydata.get(i).getStatus().contains("R")){

                // }


                Podelete.setCancelable(false);
                Podelete.show();


            }
        }


        @Override
        public void LoadDataForScroll() {
            // TODO Auto-generated method stub
        }

        @Override
        public void onDismiss(final int[] reverseSortedPositions) {
            // TODO Auto-generated method stub
            if (sweetalrt) {
                sweetalrt = false;

                for (int i : reverseSortedPositions) {
                    part_delete = dummydata.get(i).getPart();
                    Log.d("part_delete", "" + part);
                }
                Podelete = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You Want to Replace This Part# " + part_delete)
                        .setCancelText("No,Cancel")
                        .setConfirmText("Yes,Replace it")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sweetalrt = true;
                                sDialog.dismiss();

                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {


//                                sDialog.setTitleText("Removed!")
//                                        .setContentText("Part# " + part_delete + " has been removed!")
//                                        .setConfirmText("OK")
//                                        .showCancelButton(false)
//                                        .setCancelClickListener(null)
//                                        .setConfirmClickListener(null)
//                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                                for (int i : reverseSortedPositions) {
//                                    sweetalrt = true;
//                                    dummydata.remove(i);
//
//                                    adapter.notifyDataSetChanged();
//                                }

                                Sessiondata.getInstance().setPo_recive("");
                                Sessiondata.getInstance().setPo_recive_value("");
                                sweetalrt = true;
                                Sessiondata.getInstance().setPartObjects(dummydata);
                                Sessiondata.getInstance().setPartReceive(2);
                                Intent intent = new Intent(PartsReceivingDetailsActivity.this, ReplacePartsActivity.class);
                                intent.putExtra("PartNo", "");
                                intent.putExtra("OldPartNo", dummydata.get(position).getPart());
                                intent.putExtra("Mfr", dummydata.get(position).getMfg());
                                intent.putExtra("Order_no", dummydata.get(position).getPo());
                                intent.putExtra("Epoorder_no", dummydata.get(position).getEpoorderno());
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                sDialog.dismiss();

                            }
                        });
                Podelete.setCancelable(false);
                Podelete.show();
            }
        }

        @Override
        public void OnClickListView(final int position) {
            // TODO Auto-generated method stub

            click_pos = position;
            Log.d("click", " _pos " + click_pos);

            if (dummydata != null) {

//                if (dummydata.get(position).getStatus().equals("R") &
//                        (dummydata.get(position).getReplacedPart())) {
//                    return;
//                }

                if (dummydata.get(position).getStatus().equals("R")) {
                    return;
                }

                String chk_status = dummydata.get(click_pos).getStatus().toString();
                Log.d("click", " chk_status " + chk_status);

                String chk_prior, chk_Po_qty, chk_Po_status;
                int prior_conv, poqty_conv;

                chk_prior = dummydata.get(click_pos).getPrior().toString();
                chk_Po_qty = dummydata.get(click_pos).getPo_qty().toString();

                chk_Po_status = dummydata.get(click_pos).getStatus().toString();
                if (chk_prior.isEmpty()) {
                    prior_conv = 0;
                } else {
                    prior_conv = Integer.parseInt(chk_prior);
                }

                if (chk_Po_qty.isEmpty()) {
                    poqty_conv = 0;
                } else {
                    poqty_conv = Integer.parseInt(chk_Po_qty);
                }

                String clk_Part = dummydata.get(click_pos).getPart().toString();

                Log.d("click", " chk_Totalorder_qty " + chk_Po_qty + " chk_prior " + chk_prior);
                Log.d("click", " clk_Part " + clk_Part);


                if (prior_conv >= poqty_conv && poqty_conv != 0 && poqty_conv != 0 ||
                        chk_status.equals("C")) {
                    if (sweetalrt) {
                        sweetalrt = false;

                        sweetaddalret = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Message!")
                                .setContentText("This Part# " + clk_Part + " is already received!")
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
                        sweetaddalret.setCancelable(false);
                        sweetaddalret.show();
                    }
                } else {

//                    || (dummydata.get(click_pos).getReplacedPart() && dummydata.get(click_pos).getStatus().contains("I"))
                    if ((!dummydata.get(click_pos).getReceipt_qty().toString().isEmpty()) || (dummydata.get(click_pos).getReplacedPart() && dummydata.get(click_pos).getStatus().contains("I"))) {

                        single_receive_value = 0;

                        if ((mDialogs == null) || !mDialogs.isShowing()) {
                            mDialogs = new Dialog(PartsReceivingDetailsActivity.this);
                            mDialogs.setCanceledOnTouchOutside(false);
                            mDialogs.setCancelable(false);
                            mDialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            mDialogs.setContentView(R.layout.dialog_add_part_details);

                            TextView part_nos = (TextView) mDialogs.findViewById(R.id.part_no);
                            final EditText po_no = (EditText) mDialogs.findViewById(R.id.po_no);
                            final EditText edt_sec_bin = (EditText) mDialogs.findViewById(R.id.ordered);
                            final EditText edt_prior = (EditText) mDialogs.findViewById(R.id.prior);
                            final EditText edt_description = (EditText) mDialogs.findViewById(R.id.current);
                            final EditText edt_unit_price = (EditText) mDialogs.findViewById(R.id.unit_price);
                            final EditText edt_document = (EditText) mDialogs.findViewById(R.id.document);
                            final EditText edt_bin_location = (EditText) mDialogs.findViewById(R.id.bin_location);
                            final EditText edt_po_qty = (EditText) mDialogs.findViewById(R.id.po_qty);
                            final EditText edt_receipt_qty = (EditText) mDialogs.findViewById(R.id.receipt_qty);
                            final EditText eporder = (EditText) mDialogs.findViewById(R.id.eporder);
                            final EditText unit_weight = (EditText) mDialogs.findViewById(R.id.unit_weight);

                            TextView mDialogFreeCancelButton = (TextView) mDialogs.findViewById(R.id.dialog_social_cancel);

                            TextView mDialogFreeOKButton = (TextView) mDialogs.findViewById(R.id.dialog_social_ok);

                            part_nos.setText(dummydata.get(click_pos).getPart());
                            part_nos.setTypeface(header_face);
                            po_no.setText(String.valueOf(dummydata.get(click_pos).getPo()));
                            po_no.setTypeface(header_face);
                            edt_prior.setText(dummydata.get(click_pos).getPrior());
                            edt_prior.setTypeface(header_face);
                            edt_unit_price.setText(dummydata.get(click_pos).getUnit());
                            edt_unit_price.setTypeface(header_face);
                            edt_document.setText(dummydata.get(click_pos).getDocument());
                            edt_document.setTypeface(header_face);
                            edt_bin_location.setText(dummydata.get(click_pos).getBinlocation());
                            edt_bin_location.setTypeface(header_face);
                            edt_po_qty.setText(dummydata.get(click_pos).getPo_qty());
                            edt_po_qty.setTypeface(header_face);
                            unit_weight.setTypeface(header_face);
                            String weight_format = String.valueOf(dummydata.get(click_pos).getWeight());
                            weight_format = String.format("%.3f",Double.valueOf(weight_format));
                            unit_weight.setText(weight_format);


                            partsbranch = dummydata.get(click_pos).getBranch();
                            partsMfg = dummydata.get(click_pos).getMfg();
                            partnumber = dummydata.get(click_pos).getPart();
                            partsdescription = dummydata.get(click_pos).getDecription();
                            partsorderqty = Integer.parseInt(dummydata.get(click_pos).getPo_qty());
//                            partsreceivedqty = dummydata.get(click_pos).get
                            partsponumber = String.valueOf(dummydata.get(click_pos).getPo());
                            partsoeprodernum = String.valueOf(dummydata.get(click_pos).getEpoorderno());
                            partslocation = dummydata.get(click_pos).getBinlocation();


                            if (dummydata.get(click_pos).getEpoorderno() == 0) {
                                eporder.setText("");
                            } else {
                                eporder.setText(String.valueOf(dummydata.get(click_pos).getEpoorderno()));
                            }
                            eporder.setTypeface(header_face);
                            edt_receipt_qty.setText(dummydata.get(click_pos).getReceipt_qty());
                            edt_receipt_qty.setTypeface(header_face);
                            edt_description.setText(dummydata.get(click_pos).getDecription());
                            edt_description.setTypeface(header_face);
                            edt_sec_bin.setText(dummydata.get(click_pos).getSec_bin());
                            edt_sec_bin.setTypeface(header_face);


                            part = part_nos.getText().toString();

                            edt_receipt_qty.setEnabled(true);

                            edt_receipt_qty.requestFocus();

                            edt_receipt_qty.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                keyboard.showSoftInput(edt_receipt_qty, 0);
                                                            }
                                                        }
                                    , 200);

                            po_no.setEnabled(false);
                            edt_prior.setEnabled(false);
                            edt_description.setEnabled(false);
                            edt_unit_price.setEnabled(true);
                            edt_document.setEnabled(false);
                            edt_po_qty.setEnabled(false);
                            eporder.setEnabled(false);
                            part_nos.setEnabled(false);

                            TextView txt_header = (TextView) mDialogs.findViewById(R.id.txt_header);
                            TextView txtpart = (TextView) mDialogs.findViewById(R.id.txt_part);
                            TextView txtpo = (TextView) mDialogs.findViewById(R.id.txt_po);
                            TextView txt_ordered = (TextView) mDialogs.findViewById(R.id.txt_ordered);
                            TextView txt_prior = (TextView) mDialogs.findViewById(R.id.txt_prior);
                            TextView txt_current = (TextView) mDialogs.findViewById(R.id.txt_current);
                            TextView txt_unit = (TextView) mDialogs.findViewById(R.id.txt_unit);
                            TextView txt_doc = (TextView) mDialogs.findViewById(R.id.txt_doc);
                            TextView txt_poqty = (TextView) mDialogs.findViewById(R.id.txt_poqty);
                            TextView txt_bin = (TextView) mDialogs.findViewById(R.id.txt_bin);
                            TextView txt_receiptqty = (TextView) mDialogs.findViewById(R.id.txt_receiptqty);
                            TextView txt_eporder = (TextView) mDialogs.findViewById(R.id.txt_eporder);
                            TextView txt_weight = (TextView) mDialogs.findViewById(R.id.txt_weight);

                            txt_header.setTypeface(header_face);
                            txtpart.setTypeface(header_face);
                            txtpo.setTypeface(header_face);
                            txt_ordered.setTypeface(header_face);
                            txt_prior.setTypeface(header_face);
                            txt_current.setTypeface(header_face);
                            txt_unit.setTypeface(header_face);
                            txt_doc.setTypeface(header_face);
                            txt_poqty.setTypeface(header_face);
                            txt_bin.setTypeface(header_face);
                            txt_receiptqty.setTypeface(header_face);
                            txt_eporder.setTypeface(header_face);
                            txt_weight.setTypeface(header_face);

                            mDialogFreeCancelButton.setTypeface(header_face);
                            mDialogFreeOKButton.setTypeface(header_face);

                            mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {

                                    item_unitprice = edt_unit_price.getText().toString();
                                    item_startbin = edt_bin_location.getText().toString();
                                    item_endbin = edt_sec_bin.getText().toString();

                                    entered_partno = dummydata.get(click_pos).getPart();

                                    Sessiondata.getInstance().setPo_recive("");
                                    Sessiondata.getInstance().setPo_recive_value("");
                                    receiptQty = edt_receipt_qty.getText().toString();

                                    po = Integer.parseInt(po_no.getText().toString());

                                    po_Qty = edt_po_qty.getText().toString();
                                    decription = edt_description.getText().toString();
                                    prior = edt_prior.getText().toString();
                                    sec_bin = edt_sec_bin.getText().toString();
                                    unit = edt_unit_price.getText().toString();
                                    document = edt_document.getText().toString();
                                    bin = edt_bin_location.getText().toString();

                                    if (receiptQty != null && !receiptQty.equals("")) {

                                        if (Sessiondata.getInstance().getPermission().contains("True")) {
                                            partsreceivedqty = Integer.parseInt(edt_receipt_qty.getText().toString());
                                            new AsyncPrintLabelsForPart().execute();
                                        }
                                    }

                                    if (dummydata.get(click_pos).getEpoorderno() == 0) {
                                        epoorderno = 0;
                                    } else {
                                        epoorderno = Integer.parseInt(eporder.getText().toString());
                                    }

                                    int chk_multi_po = 0;
                                    for (int i = 0; i < dummydata.size(); i++) {
                                        String parts = dummydata.get(click_pos).getPart();
                                        String Qty = dummydata.get(i).getPo_qty();
                                        String ReceiveQty = dummydata.get(i).getPrior();

                                        int final_Qty;
                                        if (Qty.isEmpty()) {
                                            final_Qty = 0;
                                        } else {
                                            final_Qty = Integer.parseInt(Qty);
                                        }

                                        int final_receipt;
                                        if (ReceiveQty.isEmpty()) {
                                            final_receipt = 0;
                                        } else {
                                            final_receipt = Integer.parseInt(ReceiveQty);
                                        }

                                        if (dummydata.get(i).getPart().toString().contains(parts)) {
                                            if (final_receipt <= final_Qty
                                                    && final_receipt != final_Qty) {
                                                chk_multi_po++;
                                            }
                                        }
                                    }

                                    if (chk_multi_po > 1 && chk_multi_po != 0) {
                                        if (receiptQty != null && !receiptQty.equals("")) {
                                            mDialogs.dismiss();
                                            if ((mMultipleDialog == null) || !mMultipleDialog.isShowing()) {
                                                mMultipleDialog = new Dialog(PartsReceivingDetailsActivity.this);
                                                mMultipleDialog.setCanceledOnTouchOutside(false);
                                                mMultipleDialog.setCancelable(false);
                                                mMultipleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                mMultipleDialog.setContentView(R.layout.dialog_get_multiple_po);

                                                TextView txt_header = (TextView) mMultipleDialog.findViewById(R.id.txt_header);
                                                txt_header.setTypeface(header_face);
                                                txt_header.setText("Multiple PO Number");
                                                ListMultiplepo = (ListView) mMultipleDialog.findViewById(R.id.list);

                                                int received_Qty = Integer.parseInt(receiptQty);
                                                Log.d("Changed ", "Enter_received_Qty " + received_Qty);

                                                String part = dummydata.get(click_pos).getPart();
                                                Log.d("Changed ", "MultiplePO_Part " + part);


                                                list_pos = new ArrayList<>();
                                                sorted_partno = new ArrayList<>();
                                                po_QTY = new ArrayList<>();
                                                received_QTY = new ArrayList<>();

                                                //Sort part no
                                                for (int i = 0; i < dummydata.size(); i++) {

                                                    String Qty = dummydata.get(i).getPo_qty();
                                                    String ReceiveQty = dummydata.get(i).getPrior();

                                                    int final_Qty;
                                                    if (Qty.isEmpty()) {
                                                        final_Qty = 0;
                                                    } else {
                                                        final_Qty = Integer.parseInt(Qty);
                                                    }

                                                    int final_receipt;
                                                    if (ReceiveQty.isEmpty()) {
                                                        final_receipt = 0;
                                                    } else {
                                                        final_receipt = Integer.parseInt(ReceiveQty);
                                                    }

                                                    if (dummydata.get(i).getPart().toString().contains(part)) {
                                                        if (final_receipt <= final_Qty
                                                                && final_receipt != final_Qty) {
                                                            sorted_partno.add(dummydata.get(i));
                                                            list_pos.add(i);
                                                            po_QTY.add(Integer.parseInt(dummydata.get(i).getPo_qty()));
                                                            received_QTY.add(Integer.parseInt(dummydata.get(i).getPrior()));
                                                        }
                                                    }
                                                }

                                                Log.d("Changed ", "ArraySize " + sorted_partno.size());

                                                //Split po_qty value
                                                received_list = new ArrayList<>();
                                                MultipleQtywithstatus = new ArrayList<>();
                                                Listreceive_qty = new ArrayList<>();

                                                for (int k = 0; k < po_QTY.size(); k++) {
                                                    int receivedQty_list = received_QTY.get(k);
                                                    int poQty = po_QTY.get(k);
                                                    int receivedQty = poQty - receivedQty_list;
                                                    Log.d("Changed ", "PO_QTY " + poQty);
                                                    Log.d("Changed ", "received_List " + receivedQty_list);
                                                    Log.d("Changed ", "receivedQty " + receivedQty);
                                                    if (receivedQty < received_Qty) {
                                                        if (k == (po_QTY.size()) - 1) {
                                                            int po = sorted_partno.get(k).getPo();
                                                            String parts = sorted_partno.get(k).getPart();
                                                            String branch = sorted_partno.get(k).getBranch();
                                                            String mfg = sorted_partno.get(k).getMfg();
                                                            int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                            int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                            int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                            String price = sorted_partno.get(k).getUnit();

                                                            int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                            int received = prior + received_Qty;

                                                            String Status;
                                                            if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                Status = "R";
                                                            } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                                Status = "C";
                                                            } else {
                                                                if (received >= poqty) {
                                                                    Status = "C";
                                                                } else {
                                                                    Status = "I";
                                                                }
                                                            }

                                                            Log.d("Changed ", "Status " + Status);

                                                            if (received_Qty < 0) {
                                                                received_Qty = 0;
                                                            }

                                                            multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                    branch, mfg, epoorderno, oeitunumber);
                                                            received_list.add(multiplePOWithStatus);
                                                            Listreceive_qty.add(k, received_Qty);
                                                        } else {
                                                            int po = sorted_partno.get(k).getPo();
                                                            String parts = sorted_partno.get(k).getPart();
                                                            String branch = sorted_partno.get(k).getBranch();
                                                            String mfg = sorted_partno.get(k).getMfg();
                                                            int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                            int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                            int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                            String price = sorted_partno.get(k).getUnit();

                                                            int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                            int received = prior + receivedQty;

                                                            String Status;
                                                            if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                Status = "R";
                                                            } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                                Status = "C";
                                                            } else {

                                                                if (received >= poqty) {
                                                                    Status = "C";
                                                                } else {
                                                                    Status = "I";
                                                                }

                                                            }

                                                            Log.d("Changed ", "Status " + Status);

                                                            if (receivedQty < 0) {
                                                                receivedQty = 0;
                                                            }

                                                            multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, receivedQty, po, poqty, "", parts,
                                                                    branch, mfg, epoorderno, oeitunumber);
                                                            received_list.add(multiplePOWithStatus);
                                                            Listreceive_qty.add(k, receivedQty);
                                                            if (k != 0) {
                                                                if (received_Qty == 0) {
                                                                    received_Qty = 0;
                                                                } else {
                                                                    received_Qty = received_Qty - receivedQty;
                                                                }
                                                            } else {
                                                                received_Qty = received_Qty - receivedQty;
                                                            }
                                                        }
                                                    } else {
                                                        if (k == (po_QTY.size()) - 1) {
                                                            int po = sorted_partno.get(k).getPo();
                                                            String parts = sorted_partno.get(k).getPart();
                                                            String branch = sorted_partno.get(k).getBranch();
                                                            String mfg = sorted_partno.get(k).getMfg();
                                                            int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                            int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                            int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                            String price = sorted_partno.get(k).getUnit();

                                                            int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                            int received = prior + received_Qty;
                                                            String Status;
                                                            if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                Status = "R";
                                                            } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                                Status = "C";
                                                            } else {
                                                                if (received >= poqty) {
                                                                    Status = "C";
                                                                } else {
                                                                    Status = "I";
                                                                }
                                                            }

                                                            Log.d("Changed ", "Status " + Status);

                                                            if (received_Qty < 0) {
                                                                received_Qty = 0;
                                                            }

                                                            multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                    branch, mfg, epoorderno, oeitunumber);
                                                            received_list.add(multiplePOWithStatus);
                                                            Listreceive_qty.add(k, received_Qty);
                                                        } else {
                                                            int po = sorted_partno.get(k).getPo();
                                                            String parts = sorted_partno.get(k).getPart();
                                                            String branch = sorted_partno.get(k).getBranch();
                                                            String mfg = sorted_partno.get(k).getMfg();
                                                            int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                            int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                            int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                            String price = sorted_partno.get(k).getUnit();

                                                            int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                            int received = prior + receivedQty;
                                                            String Status;
                                                            if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                Status = "R";
                                                            } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                                Status = "C";
                                                            } else {
                                                                if (received >= poqty) {
                                                                    Status = "C";
                                                                } else {
                                                                    Status = "I";
                                                                }
                                                            }

                                                            Log.d("Changed ", "Status " + Status);

                                                            if (received_Qty < 0) {
                                                                received_Qty = 0;
                                                            }

                                                            multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                    branch, mfg, epoorderno, oeitunumber);
                                                            received_list.add(multiplePOWithStatus);
                                                            Listreceive_qty.add(k, received_Qty);

                                                            if (k != 0) {
                                                                if (received_Qty == 0) {
                                                                    received_Qty = 0;
                                                                } else {
                                                                    received_Qty = received_Qty - receivedQty;
                                                                }
                                                            } else {
                                                                received_Qty = received_Qty - receivedQty;
                                                            }

                                                        }
                                                    }

                                                }

                                                for (int i = 0; i < received_list.size(); i++) {
                                                    int remove_receivedQty = received_list.get(i).getReceiveqty();
                                                    if (remove_receivedQty != 0) {
                                                        MultipleQtywithstatus.add(received_list.get(i));
                                                    }
                                                }

                                                Log.d("Changed ", "received_list " + received_list.size());
                                                adapter_po_new = new CustomAdapter_multiplepo_new(PartsReceivingDetailsActivity.this, MultipleQtywithstatus);
                                                ListMultiplepo.setAdapter(adapter_po_new);
                                                ListMultiplepo.setTextFilterEnabled(true);
                                                count = MultipleQtywithstatus.size();
                                                thumbnailsselection_new = new boolean[count];

                                                TextView mDialogFreeOKButton = (TextView) mMultipleDialog.findViewById(R.id.dialog_proceed);
                                                mDialogFreeOKButton.setTypeface(header_face);

                                                TextView mDialogFreeCancelButton = (TextView) mMultipleDialog.findViewById(R.id.dialog_social_cancel);
                                                mDialogFreeCancelButton.setTypeface(header_face);

                                                mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View view) {

                                                        mMultipleDialog.dismiss();

                                                        int status_dep = 0;

                                                        if (MultipleQtywithstatus != null) {
                                                            if (MultipleQtywithstatus.size() != 0) {
                                                                for (int i = 0; i < MultipleQtywithstatus.size(); i++) {
                                                                    String status = MultipleQtywithstatus.get(i).getStatus();
                                                                    int poqty = MultipleQtywithstatus.get(i).getPoqty();
                                                                    int receiptqty = Listreceive_qty.get(i);

                                                                    int prior = MultipleQtywithstatus.get(i).getPrior();
                                                                    int received = prior + receiptqty;
                                                                    if (received == poqty) {

                                                                    } else if (received < poqty) {

                                                                    } else if (received > poqty) {
                                                                        status_dep = 1;
                                                                    }

                                                                }

                                                                if (status_dep == 1) {

                                                                    MultiplePO_Incomplete();
                                                                } else {
                                                                    MultiplePO_Complete();
                                                                }
                                                            }
                                                        }

                                                    }
                                                });

                                                mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View view) {
                                                        receiptQty = "";
                                                        item_unitprice = "";
                                                        item_startbin = "";
                                                        item_endbin = "";
                                                        mMultipleDialog.dismiss();
                                                    }
                                                });

                                                mMultipleDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                                                mMultipleDialog.show();
                                            }
                                        } else {
                                            mDialogs.dismiss();

                                            if (sweetalrt) {
                                                sweetalrt = false;
                                                ponoenter = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                        .setTitleText("Alert!")
                                                        .setContentText("You are requried to enter the receipt quantity!")
                                                        .setCancelText("Ok")
                                                        .showCancelButton(true)

                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sweetalrt = true;
                                                                sDialog.dismiss();
                                                            }
                                                        });
                                                ponoenter.setCancelable(false);
                                                ponoenter.show();
                                            }

                                        }
                                    } else {
                                        mDialogs.dismiss();



                                        int chk_multi_pos = 0;
                                        for (int i = 0; i < dummydata.size(); i++) {
                                            String parts = dummydata.get(click_pos).getPart();
                                            String Qty = dummydata.get(i).getPo_qty();
                                            String ReceiveQty = dummydata.get(i).getPrior();

                                            int final_Qty;
                                            if (Qty.isEmpty()) {
                                                final_Qty = 0;
                                            } else {
                                                final_Qty = Integer.parseInt(Qty);
                                            }

                                            int final_receipt;
                                            if (ReceiveQty.isEmpty()) {
                                                final_receipt = 0;
                                            } else {
                                                final_receipt = Integer.parseInt(ReceiveQty);
                                            }

                                            if (dummydata.get(i).getPart().toString().contains(parts)) {
                                                if (final_receipt <= final_Qty
                                                        && final_receipt != final_Qty) {
                                                    chk_multi_pos++;
                                                }
                                            }
                                        }

                                        Boolean chk_avail_qty = false;
                                        int chk_receivedQty;
                                        if (receiptQty.isEmpty()) {
                                            chk_receivedQty = 0;
                                        } else {
                                            chk_receivedQty = Integer.parseInt(receiptQty);
                                        }
                                        int chk_poqty;
                                        if (po_Qty.isEmpty()) {
                                            chk_poqty = 0;
                                        } else {
                                            chk_poqty = Integer.parseInt(po_Qty);
                                        }

                                        String Str_priors = dummydata.get(click_pos).getPrior();

                                        int chk_priors;
                                        if (Str_priors.isEmpty()) {
                                            chk_priors = 0;
                                        } else {
                                            chk_priors = Integer.parseInt(Str_priors);
                                        }

                                        int chk_received = chk_priors + chk_receivedQty;
                                        if (chk_received > chk_poqty) {
                                            chk_avail_qty = true;
                                        } else {
                                            chk_avail_qty = false;
                                        }
                                        Log.d("chk_avail_qty ", " " + chk_avail_qty);

                                        if (receiptQty != null && !receiptQty.equals("")) {


                                            if (chk_multi_pos == 1) {
                                                if (Integer.parseInt(po_Qty) != chk_received) {
                                                    if ((Integer.parseInt(po_Qty) < chk_received) && chk_avail_qty == true) {
                                                        if (sweetalrt) {
                                                            sweetalrt = false;

                                                            mDialogponotequal = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                                                                    .setTitleText("Message!")
                                                                    .setContentText("PO qty does not match with receive qty, Do you want to continue?")
                                                                    .setCancelText("No")
                                                                    .setConfirmText("Yes")
                                                                    .showCancelButton(true)
                                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                            if (checkInternetConenction()) {
                                                                                sweetalrt = true;
                                                                                single_receive_value = 1;
//                                                                                new AsyncGetPartDetls_LineItemClk().execute();
                                                                                sDialog.dismiss();


                                                                            } else {

                                                                                Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                                                            }
                                                                        }
                                                                    })
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sDialog) {

                                                                            int po = dummydata.get(click_pos).getPo();
                                                                            String part = dummydata.get(click_pos).getPart();
                                                                            String po_Qty = String.valueOf(dummydata.get(click_pos).getPo_qty());
                                                                            String sec_bin = dummydata.get(click_pos).getSec_bin();
                                                                            String prior = String.valueOf(dummydata.get(click_pos).getPrior());
                                                                            String decription = dummydata.get(click_pos).getDecription();
                                                                            String document = String.valueOf(dummydata.get(click_pos).getDocument());
                                                                            String bin = dummydata.get(click_pos).getBinlocation();
                                                                            String status;
                                                                            int receivedQty;
                                                                            if (receiptQty.isEmpty()) {
                                                                                receivedQty = 0;
                                                                            } else {
                                                                                receivedQty = Integer.parseInt(receiptQty);
                                                                            }
                                                                            int poqty;
                                                                            if (po_Qty.isEmpty()) {
                                                                                poqty = 0;
                                                                            } else {
                                                                                poqty = Integer.parseInt(po_Qty);
                                                                            }

                                                                            String Str_priors = dummydata.get(click_pos).getPrior();

                                                                            int chk_priors;
                                                                            if (Str_priors.isEmpty()) {
                                                                                chk_priors = 0;
                                                                            } else {
                                                                                chk_priors = Integer.parseInt(Str_priors);
                                                                            }


                                                                            int received = chk_priors + receivedQty;

                                                                            if (dummydata.get(click_pos).getStatus().toString().contains("R")) {
                                                                                status = "R";
                                                                            } else if (dummydata.get(click_pos).getStatus().toString().contains("C")) {
                                                                                status = "C";
                                                                            } else {
                                                                                if (received >= poqty) {
                                                                                    if (received >= poqty) {
                                                                                        status = "C";
                                                                                    } else {
                                                                                        status = "I";
                                                                                    }
                                                                                } else {
                                                                                    status = "I";
                                                                                }
                                                                            }

                                                                            Log.d("Final ", "Status " + status);

                                                                            String branch = dummydata.get(click_pos).getBranch();
                                                                            String mfg = dummydata.get(click_pos).getMfg();
                                                                            int epoorderno = dummydata.get(click_pos).getEpoorderno();
                                                                            int oeitunumber = dummydata.get(click_pos).getOeitenum();

                                                                            String unit = item_unitprice;

                                                                            dummydata.get(click_pos).setPo(po);
                                                                            dummydata.get(click_pos).setPart(part);
                                                                            dummydata.get(click_pos).setPo_qty(po_Qty);
                                                                            dummydata.get(click_pos).setSec_bin(item_endbin);
                                                                            dummydata.get(click_pos).setPrior(prior);
                                                                            dummydata.get(click_pos).setDecription(decription);
                                                                            dummydata.get(click_pos).setUnitprice(item_unitprice);
                                                                            dummydata.get(click_pos).setDocument(document);
                                                                            dummydata.get(click_pos).setOld_sec_bin(sec_bin);
                                                                            dummydata.get(click_pos).setOld_binlocation(bin);
                                                                            dummydata.get(click_pos).setBinlocation(item_startbin);
                                                                            dummydata.get(click_pos).setStatus(status);
                                                                            dummydata.get(click_pos).setBranch(branch);
                                                                            dummydata.get(click_pos).setMfg(mfg);
                                                                            dummydata.get(click_pos).setEpoorderno(epoorderno);
                                                                            dummydata.get(click_pos).setOeitenum(oeitunumber);
                                                                            dummydata.get(click_pos).setReceipt_qty(receiptQty);

                                                                            dummydata.get(click_pos).setEnter_receiveQty(Integer.parseInt(receiptQty));
                                                                            dummydata.get(click_pos).setTransfer("");
                                                                            dummydata.get(click_pos).setOld_part("");

                                                                            receiptQty = "";
                                                                            item_unitprice = "";

                                                                            item_startbin = "";
                                                                            item_endbin = "";

                                                                            sweetalrt = true;
                                                                            sDialog.dismiss();

                                                                            Sessiondata.getInstance().setPartObjects(dummydata);
                                                                            Sessiondata.getInstance().setPartReceive(1);
                                                                            adapter.notifyDataSetChanged();

                                                                        }
                                                                    });
                                                            mDialogponotequal.setCancelable(false);
                                                            mDialogponotequal.show();
                                                        }
                                                    } else {

                                                        if (dummydata.get(click_pos).getStatus().toString().contains("R")) {
                                                            status = "R";
                                                        } else if (dummydata.get(click_pos).getStatus().toString().contains("C")) {
                                                            status = "C";
                                                        } else {
                                                            status = "I";
                                                        }
                                                        sweetalrt = true;

                                                        ReceivePartLineItem_Single();
                                                    }
                                                } else {
                                                    if (dummydata.get(click_pos).getStatus().toString().contains("R")) {
                                                        status = "R";
                                                    } else {
                                                        status = "C";
                                                    }

                                                    sweetalrt = true;

                                                    ReceivePartLineItem_Single();
                                                }
                                            } else {
                                                if ((mMultipleDialog == null) || !mMultipleDialog.isShowing()) {
                                                    mMultipleDialog = new Dialog(PartsReceivingDetailsActivity.this);
                                                    mMultipleDialog.setCanceledOnTouchOutside(false);
                                                    mMultipleDialog.setCancelable(false);
                                                    mMultipleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                    mMultipleDialog.setContentView(R.layout.dialog_get_multiple_po);

                                                    TextView txt_header = (TextView) mMultipleDialog.findViewById(R.id.txt_header);
                                                    txt_header.setTypeface(header_face);
                                                    txt_header.setText("Multiple PO Number");
                                                    ListMultiplepo = (ListView) mMultipleDialog.findViewById(R.id.list);

                                                    int received_Qty = Integer.parseInt(receiptQty);
                                                    Log.d("Changed ", "Enter_received_Qty " + received_Qty);

                                                    String part = dummydata.get(click_pos).getPart();
                                                    Log.d("Changed ", "MultiplePO_Part " + part);


                                                    list_pos = new ArrayList<>();
                                                    sorted_partno = new ArrayList<>();
                                                    po_QTY = new ArrayList<>();
                                                    received_QTY = new ArrayList<>();

                                                    ArrayList<PartObject> Part_List = new ArrayList();
                                                    for (int i = 0; i < dummydata.size(); i++) {
                                                        String parts = dummydata.get(click_pos).getPart();
                                                        String Qty = dummydata.get(i).getPo_qty();

                                                        String ReceiveQty = dummydata.get(i).getPrior();

                                                        int final_Qty;
                                                        if (Qty.isEmpty()) {
                                                            final_Qty = 0;
                                                        } else {
                                                            final_Qty = Integer.parseInt(Qty);
                                                        }

                                                        int final_receipt;
                                                        if (ReceiveQty.isEmpty()) {
                                                            final_receipt = 0;
                                                        } else {
                                                            final_receipt = Integer.parseInt(ReceiveQty);
                                                        }


                                                        if (dummydata.get(i).getPart().toString().contains(parts)) {
                                                            if (final_receipt <= final_Qty
                                                                    && final_receipt != final_Qty) {
                                                                Part_List.add(dummydata.get(i));
                                                                list_pos.add(i);
                                                            }
                                                        }
                                                    }

                                                    if (Part_List != null) {
                                                        if (Part_List.size() != 0) {
                                                            sorted_partno.add(Part_List.get(0));
                                                            po_QTY.add(Integer.parseInt(Part_List.get(0).getPo_qty()));

                                                            if (Part_List.get(0).getPrior().equals("")) {
                                                                received_QTY.add(Integer.parseInt("0"));
                                                            } else {
                                                                received_QTY.add(Integer.parseInt(Part_List.get(0).getPrior()));
                                                            }

                                                        } else {
                                                            list_pos.add(click_pos);
                                                            sorted_partno.add(dummydata.get(click_pos));
                                                            po_QTY.add(Integer.parseInt(dummydata.get(click_pos).getPo_qty()));
                                                            if (dummydata.get(click_pos).getPrior().equals("")) {
                                                                received_QTY.add(Integer.parseInt("0"));
                                                            } else {
                                                                received_QTY.add(Integer.parseInt(dummydata.get(click_pos).getPrior()));
                                                            }

                                                        }
                                                    } else {
                                                        list_pos.add(click_pos);
                                                        sorted_partno.add(dummydata.get(click_pos));
                                                        po_QTY.add(Integer.parseInt(dummydata.get(click_pos).getPo_qty()));
                                                        if (dummydata.get(click_pos).getPrior().equals("")) {
                                                            received_QTY.add(Integer.parseInt("0"));
                                                        } else {
                                                            received_QTY.add(Integer.parseInt(dummydata.get(click_pos).getPrior()));
                                                        }
                                                    }


                                                    Log.d("Changed ", "ArraySize " + sorted_partno.size());

                                                    //Split po_qty value
                                                    received_list = new ArrayList<>();
                                                    MultipleQtywithstatus = new ArrayList<>();
                                                    Listreceive_qty = new ArrayList<>();

                                                    for (int k = 0; k < po_QTY.size(); k++) {
                                                        int receivedQty_list = received_QTY.get(k);
                                                        int poQty = po_QTY.get(k);
                                                        int receivedQty = poQty - receivedQty_list;
                                                        Log.d("Changed ", "PO_QTY " + poQty);
                                                        Log.d("Changed ", "received_List " + receivedQty_list);
                                                        Log.d("Changed ", "receivedQty " + receivedQty);
                                                        if (receivedQty < received_Qty) {
                                                            if (k == (po_QTY.size()) - 1) {
                                                                int po = sorted_partno.get(k).getPo();
                                                                String parts = sorted_partno.get(k).getPart();
                                                                String branch = sorted_partno.get(k).getBranch();
                                                                String mfg = sorted_partno.get(k).getMfg();
                                                                int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                                int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                                int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                                String price = sorted_partno.get(k).getUnit();

                                                                int prior = 0;
                                                                if (sorted_partno.get(k).getPrior().equals("")) {

                                                                } else {
                                                                    prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                                }

                                                                int received = prior + received_Qty;
                                                                String Status;
                                                                if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                    Status = "R";
                                                                } else if (dummydata.get(click_pos).getStatus().toString().contains("C")) {
                                                                    Status = "C";
                                                                } else {
                                                                    if (received >= poqty) {
                                                                        Status = "C";
                                                                    } else {
                                                                        Status = "I";
                                                                    }
                                                                }
                                                                Log.d("Changed ", "Status " + Status);

                                                                if (received_Qty < 0) {
                                                                    received_Qty = 0;
                                                                }

                                                                multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                        branch, mfg, epoorderno, oeitunumber);
                                                                received_list.add(multiplePOWithStatus);
                                                                Listreceive_qty.add(k, received_Qty);
                                                            } else {
                                                                int po = sorted_partno.get(k).getPo();
                                                                String parts = sorted_partno.get(k).getPart();
                                                                String branch = sorted_partno.get(k).getBranch();
                                                                String mfg = sorted_partno.get(k).getMfg();
                                                                int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                                int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                                int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                                String price = sorted_partno.get(k).getUnit();

                                                                int prior = 0;

                                                                if (sorted_partno.get(k).getPrior().equals("")) {

                                                                } else {
                                                                    prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                                }


                                                                int received = prior + receivedQty;
                                                                String Status;
                                                                if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                    Status = "R";
                                                                } else if (dummydata.get(click_pos).getStatus().toString().contains("C")) {
                                                                    Status = "C";
                                                                } else {
                                                                    if (received >= poqty) {
                                                                        Status = "C";
                                                                    } else {
                                                                        Status = "I";
                                                                    }
                                                                }

                                                                Log.d("Changed ", "Status " + Status);

                                                                if (receivedQty < 0) {
                                                                    receivedQty = 0;
                                                                }

                                                                multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, receivedQty, po, poqty, "", parts,
                                                                        branch, mfg, epoorderno, oeitunumber);
                                                                received_list.add(multiplePOWithStatus);
                                                                Listreceive_qty.add(k, receivedQty);

                                                                if (k != 0) {
                                                                    if (received_Qty == 0) {
                                                                        received_Qty = 0;
                                                                    } else {
                                                                        received_Qty = received_Qty - receivedQty;
                                                                    }
                                                                } else {
                                                                    received_Qty = received_Qty - receivedQty;
                                                                }
                                                            }
                                                        } else {
                                                            if (k == (po_QTY.size()) - 1) {
                                                                int po = sorted_partno.get(k).getPo();
                                                                String parts = sorted_partno.get(k).getPart();
                                                                String branch = sorted_partno.get(k).getBranch();
                                                                String mfg = sorted_partno.get(k).getMfg();
                                                                int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                                int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                                int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                                String price = sorted_partno.get(k).getUnit();


                                                                int prior = 0;

                                                                if (sorted_partno.get(k).getPrior().equals("")) {

                                                                } else {
                                                                    prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                                }


                                                                int received = prior + received_Qty;
                                                                String Status;
                                                                if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                    Status = "R";
                                                                } else if (dummydata.get(click_pos).getStatus().toString().contains("C")) {
                                                                    Status = "C";
                                                                } else {
                                                                    if (received >= poqty) {
                                                                        Status = "C";
                                                                    } else {
                                                                        Status = "I";
                                                                    }
                                                                }

                                                                Log.d("Changed ", "Status " + Status);

                                                                if (received_Qty < 0) {
                                                                    received_Qty = 0;
                                                                }

                                                                multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                        branch, mfg, epoorderno, oeitunumber);
                                                                received_list.add(multiplePOWithStatus);
                                                                Listreceive_qty.add(k, received_Qty);
                                                            } else {
                                                                int po = sorted_partno.get(k).getPo();
                                                                String parts = sorted_partno.get(k).getPart();
                                                                String branch = sorted_partno.get(k).getBranch();
                                                                String mfg = sorted_partno.get(k).getMfg();
                                                                int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                                int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                                int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                                String price = sorted_partno.get(k).getUnit();

                                                                int prior = 0;
//                                                                int prior = Integer.parseInt(sorted_partno.get(k).getPrior());

                                                                if (sorted_partno.get(k).getPrior().equals("")) {

                                                                } else {
                                                                    prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                                }

                                                                int received = prior + receivedQty;
                                                                String Status;
                                                                if (received >= poqty) {
                                                                    Status = "C";
                                                                } else {
                                                                    Status = "I";
                                                                }
                                                                Log.d("Changed ", "Status " + Status);

                                                                if (received_Qty < 0) {
                                                                    received_Qty = 0;
                                                                }

                                                                multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                        branch, mfg, epoorderno, oeitunumber);
                                                                received_list.add(multiplePOWithStatus);
                                                                Listreceive_qty.add(k, received_Qty);

                                                                if (k != 0) {
                                                                    if (received_Qty == 0) {
                                                                        received_Qty = 0;
                                                                    } else {
                                                                        received_Qty = received_Qty - receivedQty;
                                                                    }
                                                                } else {
                                                                    received_Qty = received_Qty - receivedQty;
                                                                }

                                                            }
                                                        }

                                                    }

                                                    for (int i = 0; i < received_list.size(); i++) {
                                                        int remove_receivedQty = received_list.get(i).getReceiveqty();
                                                        if (remove_receivedQty != 0) {
                                                            MultipleQtywithstatus.add(received_list.get(i));
                                                        }
                                                    }

                                                    Log.d("Changed ", "received_list " + received_list.size());
                                                    adapter_po_new = new CustomAdapter_multiplepo_new(PartsReceivingDetailsActivity.this, MultipleQtywithstatus);
                                                    ListMultiplepo.setAdapter(adapter_po_new);
                                                    ListMultiplepo.setTextFilterEnabled(true);
                                                    count = MultipleQtywithstatus.size();
                                                    thumbnailsselection_new = new boolean[count];

                                                    TextView mDialogFreeOKButton = (TextView) mMultipleDialog.findViewById(R.id.dialog_proceed);
                                                    mDialogFreeOKButton.setTypeface(header_face);

                                                    TextView mDialogFreeCancelButton = (TextView) mMultipleDialog.findViewById(R.id.dialog_social_cancel);
                                                    mDialogFreeCancelButton.setTypeface(header_face);

                                                    mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                                                        @Override
                                                        public void onClick(View view) {

                                                            mMultipleDialog.dismiss();

                                                            int status_dep = 0;

                                                            if (MultipleQtywithstatus != null) {
                                                                if (MultipleQtywithstatus.size() != 0) {
                                                                    for (int i = 0; i < MultipleQtywithstatus.size(); i++) {
                                                                        int poqty = MultipleQtywithstatus.get(i).getPoqty();
                                                                        int receiptqty = Listreceive_qty.get(i);


                                                                        int prior = MultipleQtywithstatus.get(i).getPrior();

                                                                        int received = prior + receiptqty;
                                                                        if (received == poqty) {

                                                                        } else if (received < poqty) {

                                                                        } else if (received > poqty) {
                                                                            status_dep = 1;
                                                                        }
                                                                    }

                                                                    if (status_dep == 1) {
                                                                        MultiplePO_Incomplete();
                                                                    } else {
                                                                        MultiplePO_Complete();
                                                                    }
                                                                }
                                                            }

                                                        }
                                                    });

                                                    mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                                                        @Override
                                                        public void onClick(View view) {
                                                            receiptQty = "";
                                                            item_unitprice = "";
                                                            item_startbin = "";
                                                            item_endbin = "";
                                                            mMultipleDialog.dismiss();
                                                        }
                                                    });

                                                    mMultipleDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                                                    mMultipleDialog.show();
                                                }

                                            }
                                        } else {
                                            if (sweetalrt) {
                                                sweetalrt = false;
                                                ponoenter = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                        .setTitleText("Alert!")
                                                        .setContentText("You are requried to enter the receipt quantity!")
                                                        .setCancelText("Ok")
                                                        .showCancelButton(true)

                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sweetalrt = true;
                                                                sDialog.dismiss();
                                                            }
                                                        });
                                                ponoenter.setCancelable(false);
                                                ponoenter.show();
                                            }

                                        }
                                    }

                                }
                            });

                            mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    receiptQty = "";
                                    item_unitprice = "";
                                    item_startbin = "";
                                    item_endbin = "";
                                    mDialogs.dismiss();
                                }
                            });
                            mDialogs.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                            mDialogs.show();
                        }
                    } else {
                        single_receive_value = 0;

                        LineItem_Po = dummydata.get(position).getPo();
                        LineItem_Part = dummydata.get(position).getPart();

                        if (checkInternetConenction()) {

                            new AsynValidateOrders_LineItemClk().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }

        }
    };


    public class AsyncPrintLabelsForPart extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(PartsReceivingDetailsActivity.this);


        }

        @Override
        protected Void doInBackground(Void... params) {

            usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
            try {

                PrintLabelsForPart = WebServiceConsumer.getInstance().PrintLabelsForPart(
                        usertoken,partsbranch,partsMfg,partnumber,partsdescription, partsorderqty,partsreceivedqty,
                        partsponumber,partsoeprodernum,partslocation
                );

            } catch (SocketTimeoutException e) {
                PrintLabelsForPart = null;
            } catch (Exception e) {
                PrintLabelsForPart = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            ProgressBar.dismiss();

            if (PrintLabelsForPart != null) {



            } else {


                ProgressBar.dismiss();


            }
        }
    }

    private ArrayList<GetMultiplePONumber> multiplepo;
    private Class<?> mClss;
    private Context mContext;

    public static DeviceType deviceTypeFromInt(int i) {
        return deviceTypeValues[i];
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts_receiving_details);

        mContext = this;

        Clear = (Button) findViewById(R.id.clear);

        blanket_receipt = (Button) findViewById(R.id.blanket_receipt);
        submit_entered_items = (Button) findViewById(R.id.submit_entered_items);

        grey_drawable = ContextCompat.getDrawable(mContext, R.drawable.round_grey_corner_label).mutate();
        blue_drawable = ContextCompat.getDrawable(mContext, R.drawable.round_corner_label).mutate();

        back = (Button) findViewById(R.id.back);
        part_no = (EditText) findViewById(R.id.part_no);
        add_part = (Button) findViewById(R.id.add_part);
        po_search = (ImageView) findViewById(R.id.po_search);
        part_list = (ListView) findViewById(R.id.part_list);
        array_partsdetails = new ArrayList<>();
        add_freightdetails = new ArrayList<>();

        PartsDetails_list = new ArrayList<>();
        ListUnitPrice = new ArrayList<>();
        Listreceive_qty = new ArrayList<>();

        txt_landcost = (TextView) findViewById(R.id.txt_landedcost);
        landcost_value = (TextView) findViewById(R.id.landedcost_value);

        landed_cost = (LinearLayout) findViewById(R.id.landed_cost_label);

        partdata = new ArrayList<PartObject>();
        getparts = new ArrayList<>();
        dummydata = new ArrayList<>();

        MultipleQtywithstatus = new ArrayList<>();
        store_multiplepovalue = new ArrayList<>();


        if (status == null) status = "";

        partdata = Sessiondata.getInstance().getPartObjects();

        if (Sessiondata.getInstance().getAddpart() == 1) {
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String part_number = bundle.getString("value");
            if (Sessiondata.getInstance().getPo_recive_value().toString().equalsIgnoreCase("yes")) {
                Sessiondata.getInstance().setPo_recive(part_number);
            }
        }

        if (Sessiondata.getInstance().getLoginObject() != null)
        {
            if (Sessiondata.getInstance().getLoginObject().getLandingcost() != null)
            {

                Str_Landing_Cost = Sessiondata.getInstance().getLoginObject().getLandingcost();
                Landing_Cost = Str_Landing_Cost.toString().contains("True");
            }
        }

        Epo_Status = Sessiondata.getInstance().getEPO_Status();
        Log.d("Epo_Status", "" + Epo_Status);

        if (!Landing_Cost && !Epo_Status) {
            submit_items = 1;
        }

        blanket_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag_submit = 2;
                if (submit_items == 1) {

                    if (dummydata != null) {
                        if (dummydata.size() != 0) {

                            if (checkInternetConenction()) {

                                flag = update_lineitems();

                                if (complete_btn_value == 0) {
                                    complete_btn_value = 1;

                                    landed_submit = 0.0;
                                    Type = "F";
                                    vendor_refernce();
//                                    new AsynSetPartsHeadersubmit().execute();
                                }

                            } else {

                                Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            if (sweetalrtsuccess) {
                                sweetalrtsuccess = false;

                                sweetalt = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Warning!")
                                        .setContentText("No Parts has been received!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {

                                                sweetalrtsuccess = true;

                                            }
                                        });
                                sweetalt.setCancelable(false);
                                sweetalt.show();

                            }
                        }
                    }

                } else {
                    if (checkInternetConenction()) {

                        if (complete_btn_value == 0) {
                            complete_btn_value = 1;

                            new AsyncGetFreightDetails().execute();
                        }

                    } else {

                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        submit_entered_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_submit = 1;

                if (submit_items == 1) {

                    if (dummydata != null) {
                        if (dummydata.size() != 0) {

                            if (checkInternetConenction()) {

                                flag = update_lineitems();

                                if (complete_btn_value == 0) {
                                    complete_btn_value = 1;

                                    landed_submit = 0.0;
                                    Type = "F";
                                    vendor_refernce();
//                                    new AsynSetPartsHeadersubmit().execute();
                                }

                            } else {

                                Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (sweetalrtsuccess) {
                                sweetalrtsuccess = false;

                                sweetalt = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                        .setTitleText("Warning!")
                                        .setContentText("No Parts has been received!")
                                        .setCancelText("Ok")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override

                                            public void onClick(SweetAlertDialog sDialog) {

                                                sweetalrtsuccess = true;

                                            }
                                        });
                                sweetalt.setCancelable(false);
                                sweetalt.show();

                            }
                        }
                    }

                } else {
                    if (checkInternetConenction()) {

                        if (complete_btn_value == 0) {
                            complete_btn_value = 1;

                            new AsyncGetFreightDetails().execute();
                        }


                    } else {

                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                part_no.setText("");
                Sessiondata.getInstance().setPo_recive("");
            }
        });

        part_no.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return false;
            }

        });

        po_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sessiondata.getInstance().setScanner_partreceipt(0);
                Sessiondata.getInstance().setScanner_partreceiving(2);
                Sessiondata.getInstance().setScanner_replace(0);
                Sessiondata.getInstance().setScanner_counting1(0);
                Sessiondata.getInstance().setScanner_counting2(0);
                Sessiondata.getInstance().setScanner_inventory(0);

                Sessiondata.getInstance().setScanner_partnumber(0);
                Sessiondata.getInstance().setScanner_hwstartbin(0);
                Sessiondata.getInstance().setScanner_hwendbin(0);

                Sessiondata.getInstance().setPo_recive(part_no.getText().toString());
                Sessiondata.getInstance().setPo_recive_value("yes");

                launchActivity(SimpleScannerActivity.class);
            }
        });

        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sessiondata.getInstance().setPo_recive("");
                Sessiondata.getInstance().setPo_recive_value("");

                Sessiondata.getInstance().setReplaceadapter(0);

                Intent intent = new Intent(PartsReceivingDetailsActivity.this, PartReceiptActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });


        landcost_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDialoglandcost == null) {
                    mDialoglandcost = new Dialog(PartsReceivingDetailsActivity.this);
                    mDialoglandcost.setCanceledOnTouchOutside(false);
                    mDialoglandcost.setCancelable(false);
                    mDialoglandcost.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialoglandcost.setContentView(R.layout.dialog_spread);
                    LandingCost = (EditText) mDialoglandcost.findViewById(R.id.inbound);

                    TextView mDialogFreeCancelButton = (TextView) mDialoglandcost.findViewById(R.id.dialog_social_cancel);

                    TextView mDialogFreeOKButton = (TextView) mDialoglandcost.findViewById(R.id.dialog_social_ok);

                    TextView txt_header = (TextView) mDialoglandcost.findViewById(R.id.txt_header);
                    RobotoTextView txt_landingcost = (RobotoTextView) mDialoglandcost.findViewById(R.id.dialog_social_username);
                    txt_header.setTypeface(header_face);
                    txt_landingcost.setTypeface(header_face);
                    LandingCost.setTypeface(txt_face);
                    mDialogFreeCancelButton.setTypeface(header_face);
                    mDialogFreeOKButton.setTypeface(header_face);
                    LandingCost.setText(landcost_value.getText().toString());

                    final Dialog finalMDialog_cost = mDialoglandcost;
                    mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Type = "L";
                            if (LandingCost.getText().toString().equalsIgnoreCase("")) {

                                if (sweetaddpo == true) {
                                    sweetaddpo = false;

                                    sweetaddalret = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Alert!")
                                            .setContentText("Please enter the landed cost")
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
                            } else if (LandingCost.getText().toString().equalsIgnoreCase("0.0") || LandingCost.getText().toString().equalsIgnoreCase("0")) {

                                if (sweetaddpo == true) {
                                    sweetaddpo = false;
                                    sweetaddalret = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Warning!")
                                            .setContentText("Landed Cost Cannot be Zero!")
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
                                landingCost = Double.parseDouble(LandingCost.getText().toString());
                            }
                            landed_cost.setVisibility(View.VISIBLE);
                            land_cost = 1;
                            if (land_cost == 1) {

                                landcost_value.setText(landingCost.toString());
                            }
                            finalMDialog_cost.dismiss();
                        }
                    });

                    mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            land_cost = 1;
                            finalMDialog_cost.dismiss();
                        }
                    });

                    mDialoglandcost.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                    mDialoglandcost.show();
                } else if (!mDialoglandcost.isShowing()) {
                    mDialoglandcost.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                    mDialoglandcost.show();

                }

            }
        });


        add_part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = part_no.getText().toString();
                multiple_receive_value = 0;
                single_receive_value = 0;

                if (name.isEmpty()) {

                    if (sweetaddpo) {
                        sweetaddpo = false;

                        sweetaddalret = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the Part#")
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

                    entered_partno = part_no.getText().toString();

                    if (checkInternetConenction()) {

                        if (receivepart_value == 0) {
                            receivepart_value = 1;
                            validatePosition = 0;
                            new AsynValidateOrders().execute();
                        }


                    } else {
                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sessiondata.getInstance().setPo_recive("");
                Sessiondata.getInstance().setPo_recive_value("");

                Sessiondata.getInstance().setReplaceadapter(0);

                Intent intent = new Intent(PartsReceivingDetailsActivity.this, PartReceiptActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }
        });

        if (Sessiondata.getInstance().getReplaceadapter() == 1) {
            dummydata = Sessiondata.getInstance().getPartObjects();
            adapter = new CustomAdapter(this, dummydata);
            part_list.setAdapter(adapter);
        } else if (Sessiondata.getInstance().getPartObjects() != null) {
            if (Sessiondata.getInstance().getPartObjects().size() != 0) {
                dummydata = Sessiondata.getInstance().getPartObjects();
                adapter = new CustomAdapter(this, dummydata);
                part_list.setAdapter(adapter);

            } else {
                getparts = Sessiondata.getInstance().getGetEPOParts();

                if (getparts != null) {
                    if (getparts.size() != 0) {
                        for (int i = 0; i < getparts.size(); i++) {
                            int po = getparts.get(i).getOrderno();
                            String part = getparts.get(i).getPartno();
                            String receiptQty = "";
                            String sec_bin = getparts.get(i).getEndbin();
                            String decription = getparts.get(i).getDescription();
                            String unit = getparts.get(i).getPrice();
                            String document = String.valueOf(getparts.get(i).getDocument());
                            String bin = getparts.get(i).getStartbin();

                            String po_Qty = String.valueOf(getparts.get(i).getPoqty());
                            String prior = String.valueOf(getparts.get(i).getPrior());

                            int final_Qty;
                            if (po_Qty.isEmpty()) {
                                final_Qty = 0;
                            } else {
                                final_Qty = Integer.parseInt(po_Qty);
                            }
                            int final_prior;
                            if (prior.isEmpty()) {
                                final_prior = 0;
                            } else {
                                final_prior = Integer.parseInt(prior);
                            }

                            Boolean flag = true;
                            /*if (final_prior >= final_Qty){
                                flag = false;
                                status = "C";
                            }else {
                                flag = true;
                                status = "I";
                            }*/

                            String status = getparts.get(i).getCstatus();

                            if (status.toString().contains("C")) {
                                flag = false;
                                status = "C";
                            } else if (status.toString().contains("I")) {
                                flag = true;
                                status = "I";
                            } else if (status.toString().contains("R")) {
                                flag = false;

                                status = "R";
                            }

                            String branch = getparts.get(i).getBranch();
                            String mfg = getparts.get(i).getMfg();
                            int receiveQty = 0;
                            int epoorderno = getparts.get(i).getOepordno();
                            int oeitunumber = getparts.get(i).getOeitemnum();

                            int totalOrdQty = getparts.get(i).getTotalOrdQty();

                            PartObject md = new PartObject(po, part, receiptQty, po_Qty, sec_bin,
                                    prior, decription, unit, document, bin, branch, mfg, status, receiveQty, "", "", epoorderno, oeitunumber, totalOrdQty, bin, sec_bin, flag, false);

                            Double weight = getparts.get(i).getWeight();
                            int order = getparts.get(i).getOepordno();
                            md.setWeight(weight);
                            md.setOrder(order);

                            dummydata.add(md);
                        }

                        adapter = new CustomAdapter(this, dummydata);
                        part_list.setAdapter(adapter);

                        Sessiondata.getInstance().setPartObjects(dummydata);
                        Sessiondata.getInstance().setPartReceive(1);

                        dummydata = Sessiondata.getInstance().getPartObjects();
                    }
                }
            }
        } else {

            getparts = Sessiondata.getInstance().getGetEPOParts();

            if (getparts != null) {
                if (getparts.size() != 0) {
                    for (int i = 0; i < getparts.size(); i++) {
                        int po = getparts.get(i).getOrderno();
                        String part = getparts.get(i).getPartno();
                        String receiptQty = "";
                        String sec_bin = getparts.get(i).getEndbin();
                        String decription = getparts.get(i).getDescription();
                        String unit = getparts.get(i).getPrice();
                        String document = String.valueOf(getparts.get(i).getDocument());
                        String bin = getparts.get(i).getStartbin();

                        String po_Qty = String.valueOf(getparts.get(i).getPoqty());
                        String prior = String.valueOf(getparts.get(i).getPrior());

                        int final_Qty;
                        if (po_Qty.isEmpty()) {
                            final_Qty = 0;
                        } else {
                            final_Qty = Integer.parseInt(po_Qty);
                        }
                        int final_prior;
                        if (prior.isEmpty()) {
                            final_prior = 0;
                        } else {
                            final_prior = Integer.parseInt(prior);
                        }

                        Boolean flag = true;
                        /*if (final_prior >= final_Qty){
                            status = "C";
                            flag = false;
                        }else {
                            flag = true;
                            status = "I";
                        }*/


                        String status = getparts.get(i).getCstatus();

                        if (status.toString().contains("C")) {
                            flag = false;
                            status = "C";
                        } else if (status.toString().contains("I")) {
                            flag = true;
                            status = "I";
                        } else if (status.toString().contains("R")) {
                            flag = false;
                            status = "R";
                        }

                        String branch = getparts.get(i).getBranch();
                        String mfg = getparts.get(i).getMfg();
                        int receiveQty = 0;
                        int epoorderno = getparts.get(i).getOepordno();
                        int oeitunumber = getparts.get(i).getOeitemnum();

                        int totalOrdQty = getparts.get(i).getTotalOrdQty();



                        PartObject md = new PartObject(po, part, receiptQty, po_Qty, sec_bin,
                                prior, decription, unit, document, bin, branch, mfg, status, receiveQty, "", "", epoorderno, oeitunumber, totalOrdQty, bin, sec_bin, flag, false);

                        Double weight = getparts.get(i).getWeight();
                        int order = getparts.get(i).getOepordno();
                        md.setWeight(weight);
                        md.setOrder(order);
                        dummydata.add(md);
                    }

                    adapter = new CustomAdapter(this, dummydata);
                    part_list.setAdapter(adapter);

                    Sessiondata.getInstance().setPartObjects(dummydata);
                    Sessiondata.getInstance().setPartReceive(1);

                    dummydata = Sessiondata.getInstance().getPartObjects();
                }
            }

        }

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        Log.d("Width ", "" + width + " Height " + height);

        touchListener = new ListViewSwipeGesture(
                part_list, swipeListener, this, dummydata, width, height);
        touchListener.SwipeType = ListViewSwipeGesture.Double;

        part_list.setOnTouchListener(touchListener);

        TextView header = (TextView) findViewById(R.id.header);
        TextView txt_part = (TextView) findViewById(R.id.txt_part);
        header_face = Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");
        txt_face = Typeface.createFromAsset(getAssets(), "fonts/helr45w.ttf");
        add_part.setTypeface(header_face);
        header.setTypeface(header_face);
        txt_part.setTypeface(header_face);
        txt_landcost.setTypeface(header_face);
        submit_entered_items.setTypeface(header_face);
        blanket_receipt.setTypeface(header_face);
        back.setTypeface(txt_face);
        part_no.setTypeface(txt_face);

    }

    private void ClearSession() {

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

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Sessiondata.getInstance().setPo_recive("");
        Sessiondata.getInstance().setPo_recive_value("");

        Sessiondata.getInstance().setReplaceadapter(0);

        Intent intent = new Intent(PartsReceivingDetailsActivity.this, PartReceiptActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
    }

    private boolean checkInternetConenction() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    @Override
    protected void onResume() {
        super.onResume();

        sweetalrt = true;

        if (Sessiondata.getInstance().getReplaceadapter() == 1) {

            dummydata = Sessiondata.getInstance().getPartObjects();

//            if(!Sessiondata.getInstance().getPart_number().equals("")){
//
//                for(int i = 0 ; i < dummydata.size() ; i++){
//
//                    if(dummydata.get(i).getPart().contains(Sessiondata.getInstance().getPart_number())){
//                        dummydata.get(i).setStatus("R");
//                    }
//
//                }
//
//                Sessiondata.getInstance().setPart_number("");
//            }

            adapter = new CustomAdapter(this, dummydata);
            part_list.setAdapter(adapter);

            part_no.setText("");
        }

        if (Sessiondata.getInstance().getPo_recive_value().toString().equalsIgnoreCase("yes")) {

            part_no.setText(Sessiondata.getInstance().getPo_recive());
        }

    }

    private void initDevice() {

        PartsReceivingDetailsActivity.readerDevice = ReaderDevice.getMXDevice(mContext);

        readerDevice.startAvailabilityListening();

        PartsReceivingDetailsActivity.readerDevice.setReaderDeviceListener(this);
        PartsReceivingDetailsActivity.readerDevice.enableImage(true);
        PartsReceivingDetailsActivity.readerDevice.connect(PartsReceivingDetailsActivity.this);
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

                Sessiondata.getInstance().setPo_recive(result);

                part_no.setText(Sessiondata.getInstance().getPo_recive());

            }
        }, 500);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(part_no.getWindowToken(), 0);
    }

    @Override
    public void onAvailabilityChanged(ReaderDevice reader) {
        if (reader.getAvailability() == ReaderDevice.Availability.AVAILABLE) {
            PartsReceivingDetailsActivity.readerDevice.connect(PartsReceivingDetailsActivity.this);
        } else {
            // DISCONNECTED USB DEVICE
            PartsReceivingDetailsActivity.readerDevice.connect(PartsReceivingDetailsActivity.this);
            PartsReceivingDetailsActivity.readerDevice.disconnect();
            readerDisconnected();
        }
    }

    @Override
    public void onConnectionCompleted(ReaderDevice reader, Throwable error) {
        if (error != null) {
            readerDisconnected();
        }
    }

    // the methods below are NOT from the Cognex SDK
    private void readerDisconnected() {
        Log.d("cmb.SampleApp", "onDisconnected");
    }

    private void readerConnected() {

        Log.d("cmb.SampleApp", "onConnected");

        //example setSymbologyEnabled
        PartsReceivingDetailsActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.C128, true, null);
        PartsReceivingDetailsActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.DATAMATRIX, true, null);
        PartsReceivingDetailsActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.UPC_EAN, true, null);
        PartsReceivingDetailsActivity.readerDevice.setSymbologyEnabled(ReaderDevice.Symbology.QR, true, null);

        //example sendCommand
        PartsReceivingDetailsActivity.readerDevice.getDataManSystem().sendCommand("SET SYMBOL.MICROPDF417 ON");
        PartsReceivingDetailsActivity.readerDevice.getDataManSystem().sendCommand("SET IMAGE.SIZE 0");

    }

    private ReaderDevice.Symbology symbologyFromString(String symbol) {
        symbol = symbol.toUpperCase();

        if (symbol.equals("SYMBOL.DATAMATRIX"))
            return ReaderDevice.Symbology.DATAMATRIX;

        if (symbol.equals("SYMBOL.QR"))
            return ReaderDevice.Symbology.QR;

        if (symbol.equals("SYMBOL.C128"))
            return ReaderDevice.Symbology.C128;

        if (symbol.equals("SYMBOL.UPC-EAN"))
            return ReaderDevice.Symbology.UPC_EAN;

        if (symbol.equals("SYMBOL.C39"))
            return ReaderDevice.Symbology.C39;

        if (symbol.equals("SYMBOL.C93"))
            return ReaderDevice.Symbology.C93;

        if (symbol.equals("SYMBOL.I2O5"))
            return ReaderDevice.Symbology.I2O5;

        if (symbol.equals("SYMBOL.CODABAR"))
            return ReaderDevice.Symbology.CODABAR;

        if (symbol.equals("SYMBOL.EAN-UCC"))
            return ReaderDevice.Symbology.EAN_UCC;

        if (symbol.equals("SYMBOL.PHARMACODE"))
            return ReaderDevice.Symbology.PHARMACODE;

        if (symbol.equals("SYMBOL.MAXICODE"))
            return ReaderDevice.Symbology.MAXICODE;

        if (symbol.equals("SYMBOL.PDF417"))
            return ReaderDevice.Symbology.PDF417;

        if (symbol.equals("SYMBOL.MICROPDF417"))
            return ReaderDevice.Symbology.MICROPDF417;

        if (symbol.equals("SYMBOL.DATABAR"))
            return ReaderDevice.Symbology.DATABAR;

        if (symbol.equals("SYMBOL.POSTNET"))
            return ReaderDevice.Symbology.POSTNET;

        if (symbol.equals("SYMBOL.PLANET"))
            return ReaderDevice.Symbology.PLANET;

        if (symbol.equals("SYMBOL.4STATE-JAP"))
            return ReaderDevice.Symbology.FOUR_STATE_JAP;

        if (symbol.equals("SYMBOL.4STATE-AUS"))
            return ReaderDevice.Symbology.FOUR_STATE_AUS;

        if (symbol.equals("SYMBOL.4STATE-UPU"))
            return ReaderDevice.Symbology.FOUR_STATE_UPU;

        if (symbol.equals("SYMBOL.4STATE-IMB"))
            return ReaderDevice.Symbology.FOUR_STATE_IMB;

        if (symbol.equals("SYMBOL.VERICODE"))
            return ReaderDevice.Symbology.VERICODE;

        if (symbol.equals("SYMBOL.RPC"))
            return ReaderDevice.Symbology.RPC;

        if (symbol.equals("SYMBOL.MSI"))
            return ReaderDevice.Symbology.MSI;

        if (symbol.equals("SYMBOL.AZTECCODE"))
            return ReaderDevice.Symbology.AZTECCODE;

        if (symbol.equals("SYMBOL.DOTCODE"))
            return ReaderDevice.Symbology.DOTCODE;

        if (symbol.equals("SYMBOL.C25"))
            return ReaderDevice.Symbology.C25;

        if (symbol.equals("SYMBOL.C39-CONVERT-TO-C32"))
            return ReaderDevice.Symbology.C39_CONVERT_TO_C32;

        if (symbol.equals("SYMBOL.OCR"))
            return ReaderDevice.Symbology.OCR;

        if (symbol.equals("SYMBOL.4STATE-RMC"))
            return ReaderDevice.Symbology.FOUR_STATE_RMC;

        Log.e("cmb.SampleApp", "symbologyFromString was null for " + symbol);
        return null;
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

    public void MultiplePO_Dialog() {
        if ((mMultipleDialog == null) || !mMultipleDialog.isShowing()) {
            mMultipleDialog = new Dialog(PartsReceivingDetailsActivity.this);
            mMultipleDialog.setCanceledOnTouchOutside(false);
            mMultipleDialog.setCancelable(false);
            mMultipleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mMultipleDialog.setContentView(R.layout.dialog_get_multiple_po);

            TextView txt_header = (TextView) mMultipleDialog.findViewById(R.id.txt_header);
            txt_header.setTypeface(header_face);
            txt_header.setText("Multiple PO Number");
            ListMultiplepo = (ListView) mMultipleDialog.findViewById(R.id.list);

            int received_Qty = Integer.parseInt(receiptQty);
            Log.d("Changed ", "Enter_received_Qty " + received_Qty);

            String part = "";
            if (dummydata != null) {
                part = dummydata.get(click_pos).getPart();
            }

            Log.d("Changed ", "MultiplePO_Part " + part);


            list_pos = new ArrayList<>();
            sorted_partno = new ArrayList<>();
            po_QTY = new ArrayList<>();
            received_QTY = new ArrayList<>();

            //Sort part no
            for (int i = 0; i < dummydata.size(); i++) {

                String Qty = dummydata.get(i).getPo_qty();
                String ReceiveQty = dummydata.get(i).getPrior();

                int final_Qty;
                if (Qty.isEmpty()) {
                    final_Qty = 0;
                } else {
                    final_Qty = Integer.parseInt(Qty);
                }

                int final_receipt;
                if (ReceiveQty.isEmpty()) {
                    final_receipt = 0;
                } else {
                    final_receipt = Integer.parseInt(ReceiveQty);
                }

                if (dummydata.get(i).getPart().toString().contains(part)) {
                    if (final_receipt <= final_Qty
                            && final_receipt != final_Qty) {
                        sorted_partno.add(dummydata.get(i));
                        list_pos.add(i);
                        po_QTY.add(Integer.parseInt(dummydata.get(i).getPo_qty()));
                        received_QTY.add(Integer.parseInt(dummydata.get(i).getPrior()));
                    }
                }
            }

            Log.d("Changed ", "ArraySize " + sorted_partno.size());

            //Split po_qty value

            received_list = new ArrayList<>();
            MultipleQtywithstatus = new ArrayList<>();
            Listreceive_qty = new ArrayList<>();

            for (int k = 0; k < po_QTY.size(); k++) {
                int receivedQty_list = received_QTY.get(k);
                int poQty = po_QTY.get(k);
                int receivedQty = poQty - receivedQty_list;
                Log.d("Changed ", "PO_QTY " + poQty);
                Log.d("Changed ", "received_List " + receivedQty_list);
                Log.d("Changed ", "receivedQty " + receivedQty);
                if (receivedQty < received_Qty) {
                    if (k == (po_QTY.size()) - 1) {
                        int po = sorted_partno.get(k).getPo();
                        String parts = sorted_partno.get(k).getPart();
                        String branch = sorted_partno.get(k).getBranch();
                        String mfg = sorted_partno.get(k).getMfg();
                        int epoorderno = sorted_partno.get(k).getEpoorderno();
                        int oeitunumber = sorted_partno.get(k).getOeitenum();
                        int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                        String price = sorted_partno.get(k).getUnit();

                        int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                        int received = prior + received_Qty;
                        String Status;
                        if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                            Status = "R";
                        } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                            Status = "C";
                        } else {
                            if (received >= poqty) {
                                Status = "C";
                            } else {
                                Status = "I";
                            }
                        }

                        Log.d("Changed ", "Status " + Status);

                        if (received_Qty < 0) {
                            received_Qty = 0;
                        }

                        int final_enteredQty;
                        if (store_multiplepovalue != null) {
                            final_enteredQty = store_multiplepovalue.get(k).getReceiveqty();
                        } else {
                            final_enteredQty = received_Qty;
                        }


                        multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, final_enteredQty, po, poqty, "", parts,
                                branch, mfg, epoorderno, oeitunumber);
                        received_list.add(multiplePOWithStatus);


                        Listreceive_qty.add(k, final_enteredQty);

                    } else {
                        int po = sorted_partno.get(k).getPo();
                        String parts = sorted_partno.get(k).getPart();
                        String branch = sorted_partno.get(k).getBranch();
                        String mfg = sorted_partno.get(k).getMfg();
                        int epoorderno = sorted_partno.get(k).getEpoorderno();
                        int oeitunumber = sorted_partno.get(k).getOeitenum();
                        int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                        String price = sorted_partno.get(k).getUnit();

                        int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                        int received = prior + receivedQty;
                        String Status;
                        if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                            Status = "R";
                        } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                            Status = "C";
                        } else {
                            if (received >= poqty) {
                                Status = "C";
                            } else {
                                Status = "I";
                            }
                        }


                        Log.d("Changed ", "Status " + Status);

                        if (receivedQty < 0) {
                            receivedQty = 0;
                        }

                        int final_enteredQty;
                        if (store_multiplepovalue != null) {
                            final_enteredQty = store_multiplepovalue.get(k).getReceiveqty();
                        } else {
                            final_enteredQty = receivedQty;
                        }

                        multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, final_enteredQty, po, poqty, "", parts,
                                branch, mfg, epoorderno, oeitunumber);

                        received_list.add(multiplePOWithStatus);


                        Listreceive_qty.add(k, final_enteredQty);

                        if (k != 0) {
                            if (received_Qty == 0) {
                                received_Qty = 0;
                            } else {
                                received_Qty = received_Qty - receivedQty;
                            }
                        } else {
                            received_Qty = received_Qty - receivedQty;
                        }
                    }
                } else {
                    if (k == (po_QTY.size()) - 1) {
                        int po = sorted_partno.get(k).getPo();
                        String parts = sorted_partno.get(k).getPart();
                        String branch = sorted_partno.get(k).getBranch();
                        String mfg = sorted_partno.get(k).getMfg();
                        int epoorderno = sorted_partno.get(k).getEpoorderno();
                        int oeitunumber = sorted_partno.get(k).getOeitenum();
                        int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                        String price = sorted_partno.get(k).getUnit();

                        int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                        int received = prior + received_Qty;
                        String Status;
                        if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                            Status = "R";
                        } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                            Status = "C";
                        } else {
                            if (received >= poqty) {
                                Status = "C";
                            } else {
                                Status = "I";
                            }
                        }

                        Log.d("Changed ", "Status " + Status);

                        if (received_Qty < 0) {
                            received_Qty = 0;
                        }

                        int final_enteredQty;
                        if (store_multiplepovalue != null) {
                            final_enteredQty = store_multiplepovalue.get(k).getReceiveqty();
                        } else {
                            final_enteredQty = received_Qty;
                        }

                        multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, final_enteredQty, po, poqty, "", parts,
                                branch, mfg, epoorderno, oeitunumber);
                        received_list.add(multiplePOWithStatus);


                        Listreceive_qty.add(k, final_enteredQty);
                    } else {
                        int po = sorted_partno.get(k).getPo();
                        String parts = sorted_partno.get(k).getPart();
                        String branch = sorted_partno.get(k).getBranch();
                        String mfg = sorted_partno.get(k).getMfg();
                        int epoorderno = sorted_partno.get(k).getEpoorderno();
                        int oeitunumber = sorted_partno.get(k).getOeitenum();
                        int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                        String price = sorted_partno.get(k).getUnit();

                        int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                        int received = prior + receivedQty;
                        String Status;
                        if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                            Status = "R";
                        } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                            Status = "C";
                        } else {
                            if (received >= poqty) {
                                Status = "C";
                            } else {
                                Status = "I";
                            }
                        }

                        Log.d("Changed ", "Status " + Status);

                        if (received_Qty < 0) {
                            received_Qty = 0;
                        }

                        int final_enteredQty;
                        if (store_multiplepovalue != null) {
                            final_enteredQty = store_multiplepovalue.get(k).getReceiveqty();
                        } else {
                            final_enteredQty = received_Qty;
                        }

                        multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, final_enteredQty, po, poqty, "", parts,
                                branch, mfg, epoorderno, oeitunumber);
                        received_list.add(multiplePOWithStatus);


                        Listreceive_qty.add(k, final_enteredQty);


                        if (k != 0) {
                            if (received_Qty == 0) {
                                received_Qty = 0;
                            } else {
                                received_Qty = received_Qty - receivedQty;
                            }
                        } else {
                            received_Qty = received_Qty - receivedQty;
                        }

                    }
                }

            }

            for (int i = 0; i < received_list.size(); i++) {
                int remove_receivedQty = received_list.get(i).getReceiveqty();
                if (remove_receivedQty != 0) {
                    MultipleQtywithstatus.add(received_list.get(i));
                }
            }

            Log.d("Changed ", "received_list " + received_list.size());
            adapter_po_new = new CustomAdapter_multiplepo_new(PartsReceivingDetailsActivity.this, MultipleQtywithstatus);
            ListMultiplepo.setAdapter(adapter_po_new);
            ListMultiplepo.setTextFilterEnabled(true);
            count = MultipleQtywithstatus.size();
            thumbnailsselection_new = new boolean[count];

            TextView mDialogFreeOKButton = (TextView) mMultipleDialog.findViewById(R.id.dialog_proceed);
            mDialogFreeOKButton.setTypeface(header_face);

            TextView mDialogFreeCancelButton = (TextView) mMultipleDialog.findViewById(R.id.dialog_social_cancel);
            mDialogFreeCancelButton.setTypeface(header_face);

            mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    mMultipleDialog.dismiss();

                    int status_dep = 0;

                    if (MultipleQtywithstatus != null) {
                        if (MultipleQtywithstatus.size() != 0) {
                            for (int i = 0; i < MultipleQtywithstatus.size(); i++) {
                                String status = MultipleQtywithstatus.get(i).getStatus();
                                int poqty = MultipleQtywithstatus.get(i).getPoqty();
                                int receiptqty = Listreceive_qty.get(i);

                                int prior = MultipleQtywithstatus.get(i).getPrior();
                                int received = prior + receiptqty;
                                if (received == poqty) {

                                } else if (received < poqty) {

                                } else if (received > poqty) {
                                    status_dep = 1;
                                }
                            }

                            if (status_dep == 1) {

                                MultiplePO_Incomplete();

                            } else {
                                MultiplePO_Complete();
                            }
                        }
                    }

                }
            });

            mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    receiptQty = "";
                    item_unitprice = "";
                    item_startbin = "";
                    item_endbin = "";
                    mMultipleDialog.dismiss();
                }
            });

            mMultipleDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            mMultipleDialog.show();
        }
    }

    public void MultiplePO_Incomplete() {

        if (sweetalrt) {
            sweetalrt = false;

            mDialogponotequal = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                    .setTitleText("Message!")
                    .setContentText("PO qty does not match with receive qty, Do you want to continue?")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            if (checkInternetConenction()) {
                                sweetalrt = true;
                                store_multiplepovalue = new ArrayList<>();
                                store_multiplepovalue = received_list;

//                                MultiplePO_Dialog();
                                sDialog.dismiss();

                            } else {

                                Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                            }
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sweetalrt = true;
                            if (received_list != null) {
                                if (received_list.size() != 0) {
                                    for (int j = 0; j < received_list.size(); j++) {
                                        int pos = list_pos.get(j);

                                        int po_Qtys = 0;
                                        String status = "";

                                        if (received_list != null) {
                                            po_Qtys = received_list.get(j).getReceiveqty();
                                            status = received_list.get(j).getStatus();
                                        }

                                        String branch, mfg;
                                        int totalOrdQty;

                                        po = sorted_partno.get(j).getPo();
                                        part = sorted_partno.get(j).getPart();
                                        item_endbin = sorted_partno.get(j).getSec_bin();
                                        prior = sorted_partno.get(j).getPrior();
                                        decription = sorted_partno.get(j).getDecription();
                                        item_unitprice = sorted_partno.get(j).getUnit();
                                        document = sorted_partno.get(j).getDocument();
                                        item_startbin = sorted_partno.get(j).getBinlocation();
                                        branch = sorted_partno.get(j).getBranch();
                                        mfg = sorted_partno.get(j).getMfg();
                                        epoorderno = sorted_partno.get(j).getEpoorderno();
                                        oeitunumber = sorted_partno.get(j).getOeitenum();
                                        String poqty = sorted_partno.get(j).getPo_qty();
                                        totalOrdQty = sorted_partno.get(j).getTotalOrdQty();


                                        dummydata.get(pos).setStatus(status);
                                        dummydata.get(pos).setReceipt_qty(String.valueOf(po_Qtys));
                                        dummydata.get(pos).setEnter_receiveQty(po_Qtys);

                                        dummydata.get(pos).setPo(po);
                                        dummydata.get(pos).setPart(part);
                                        dummydata.get(pos).setSec_bin(item_endbin);
                                        dummydata.get(pos).setPrior(prior);
                                        dummydata.get(pos).setDecription(decription);
                                        dummydata.get(pos).setUnitprice(item_unitprice);
                                        dummydata.get(pos).setDocument(document);
                                        dummydata.get(click_pos).setOld_sec_bin(sec_bin);
                                        dummydata.get(click_pos).setOld_binlocation(bin);
                                        dummydata.get(pos).setBinlocation(item_startbin);
                                        dummydata.get(pos).setBranch(branch);
                                        dummydata.get(pos).setMfg(mfg);
                                        dummydata.get(pos).setEpoorderno(epoorderno);
                                        dummydata.get(pos).setOeitenum(oeitunumber);
                                        dummydata.get(pos).setPo_qty(poqty);

                                        dummydata.get(pos).setTotalOrdQty(totalOrdQty);


                                        dummydata.get(pos).setTransfer("");
                                        dummydata.get(pos).setOld_part("");

                                        Sessiondata.getInstance().setPartObjects(dummydata);
                                        Sessiondata.getInstance().setPartReceive(1);
                                        adapter.notifyDataSetChanged();
                                    }
                                    item_unitprice = "";
                                    item_startbin = "";
                                    item_endbin = "";
                                    receiptQty = "";
                                }
                            }


                            sDialog.dismiss();
                        }
                    });
            mDialogponotequal.setCancelable(false);
            mDialogponotequal.show();
        }
    }

    public void MultiplePO_Complete() {
        if (received_list != null) {
            if (received_list.size() != 0) {
                for (int j = 0; j < received_list.size(); j++) {
                    int pos = list_pos.get(j);

                    int po_Qtys = 0;
                    String status = "";

                    if (received_list != null) {
                        po_Qtys = received_list.get(j).getReceiveqty();
                        status = received_list.get(j).getStatus();
                    }

                    String branch, mfg;
                    int totalOrdQty;

                    po = sorted_partno.get(j).getPo();
                    part = sorted_partno.get(j).getPart();
                    item_endbin = sorted_partno.get(j).getSec_bin();
                    prior = sorted_partno.get(j).getPrior();
                    decription = sorted_partno.get(j).getDecription();
                    item_unitprice = sorted_partno.get(j).getUnit();
                    document = sorted_partno.get(j).getDocument();
                    item_startbin = sorted_partno.get(j).getBinlocation();
                    branch = sorted_partno.get(j).getBranch();
                    mfg = sorted_partno.get(j).getMfg();
                    epoorderno = sorted_partno.get(j).getEpoorderno();
                    oeitunumber = sorted_partno.get(j).getOeitenum();
                    String poqty = sorted_partno.get(j).getPo_qty();
                    totalOrdQty = sorted_partno.get(j).getTotalOrdQty();


                    dummydata.get(pos).setStatus(status);
                    dummydata.get(pos).setReceipt_qty(String.valueOf(po_Qtys));
                    dummydata.get(pos).setEnter_receiveQty(po_Qtys);

                    dummydata.get(pos).setPo(po);
                    dummydata.get(pos).setPart(part);
                    dummydata.get(pos).setSec_bin(item_endbin);
                    dummydata.get(pos).setPrior(prior);
                    dummydata.get(pos).setDecription(decription);
                    dummydata.get(pos).setUnitprice(item_unitprice);
                    dummydata.get(pos).setDocument(document);
                    dummydata.get(click_pos).setOld_sec_bin(sec_bin);
                    dummydata.get(click_pos).setOld_binlocation(bin);
                    dummydata.get(pos).setBinlocation(item_startbin);
                    dummydata.get(pos).setBranch(branch);
                    dummydata.get(pos).setMfg(mfg);
                    dummydata.get(pos).setEpoorderno(epoorderno);
                    dummydata.get(pos).setOeitenum(oeitunumber);
                    dummydata.get(pos).setPo_qty(poqty);

                    dummydata.get(pos).setTotalOrdQty(totalOrdQty);


                    dummydata.get(pos).setTransfer("");
                    dummydata.get(pos).setOld_part("");

                    Sessiondata.getInstance().setPartObjects(dummydata);
                    Sessiondata.getInstance().setPartReceive(1);
                    adapter.notifyDataSetChanged();
                }
                item_unitprice = "";
                item_startbin = "";
                item_endbin = "";
                receiptQty = "";
            }
        }
    }

    public void ReceivePartLineItem_Single() {

        if (dummydata != null) {
            int po = dummydata.get(click_pos).getPo();
            String part = dummydata.get(click_pos).getPart();
            String old_part = dummydata.get(click_pos).getOld_part();
            String po_Qty = String.valueOf(dummydata.get(click_pos).getPo_qty());
            String sec_bin = dummydata.get(click_pos).getSec_bin();
            String prior = String.valueOf(dummydata.get(click_pos).getPrior());
            String decription = dummydata.get(click_pos).getDecription();
            String unit = dummydata.get(click_pos).getUnit();
            String document = String.valueOf(dummydata.get(click_pos).getDocument());
            String bin = dummydata.get(click_pos).getBinlocation();
            String branch = dummydata.get(click_pos).getBranch();
            String mfg = dummydata.get(click_pos).getMfg();
            int receiveQty = 0;
            int epoorderno = dummydata.get(click_pos).getEpoorderno();
            int oeitunumber = dummydata.get(click_pos).getOeitenum();

            int totalOrdQty = dummydata.get(click_pos).getTotalOrdQty();

            dummydata.get(click_pos).setStatus(status);
            dummydata.get(click_pos).setReceipt_qty(String.valueOf(receiptQty));
            dummydata.get(click_pos).setEnter_receiveQty(Integer.parseInt(receiptQty));

            dummydata.get(click_pos).setPo(po);
            dummydata.get(click_pos).setPart(part);
            dummydata.get(click_pos).setOld_part(old_part);
            dummydata.get(click_pos).setSec_bin(item_endbin);
            dummydata.get(click_pos).setPrior(prior);
            dummydata.get(click_pos).setDecription(decription);
            dummydata.get(click_pos).setUnitprice(item_unitprice);
            dummydata.get(click_pos).setDocument(document);
            dummydata.get(click_pos).setOld_sec_bin(sec_bin);
            dummydata.get(click_pos).setOld_binlocation(bin);
            dummydata.get(click_pos).setBinlocation(item_startbin);
            dummydata.get(click_pos).setBranch(branch);
            dummydata.get(click_pos).setMfg(mfg);
            dummydata.get(click_pos).setEpoorderno(epoorderno);
            dummydata.get(click_pos).setOeitenum(oeitunumber);
            dummydata.get(click_pos).setPo_qty(po_Qty);

            dummydata.get(click_pos).setTotalOrdQty(totalOrdQty);

            dummydata.get(click_pos).setTransfer("");
//            dummydata.get(click_pos).setOld_part("");

            Sessiondata.getInstance().setPartObjects(dummydata);
            Sessiondata.getInstance().setPartReceive(1);
            adapter.notifyDataSetChanged();

        }

        item_unitprice = "";
        item_startbin = "";
        item_endbin = "";
        receiptQty = "";

    }

    public void ReceivePart_Single() {

        if (PartsDetails_list != null) {
            int po = PartsDetails_list.get(0).getOrderno();
            String part = PartsDetails_list.get(0).getPartno();
            String receiptQty = "";
            String po_Qty = String.valueOf(PartsDetails_list.get(0).getPoqty());
            String sec_bin = PartsDetails_list.get(0).getEndbin();
            String prior = String.valueOf(PartsDetails_list.get(0).getPrior());
            String decription = PartsDetails_list.get(0).getDescription();
            String unit = PartsDetails_list.get(0).getPrice();
            String document = String.valueOf(PartsDetails_list.get(0).getDocument());
            String bin = PartsDetails_list.get(0).getStartbin();
            String status;
            int receivedQty;
            if (receiptQty.isEmpty()) {
                receivedQty = 0;
            } else {
                receivedQty = Integer.parseInt(receiptQty);
            }
            int poqty;
            if (po_Qty.isEmpty()) {
                poqty = 0;
            } else {
                poqty = Integer.parseInt(po_Qty);
            }

            int priors = PartsDetails_list.get(0).getPrior();
            int received = priors + receivedQty;


            /*Boolean flag;
            if (received >= poqty){
                flag = false;
                status = "C";
            }else {
                flag = true;
                status = "I";
            }*/

            String branch = PartsDetails_list.get(0).getBranch();
            String mfg = PartsDetails_list.get(0).getMfg();
            int receiveQty = 0;
            int epoorderno = PartsDetails_list.get(0).getOepordno();
            int oeitunumber = PartsDetails_list.get(0).getOeitemnum();

            int totalOrdQty = PartsDetails_list.get(0).getTotalOrdQty();

            status = PartsDetails_list.get(0).getCstatus();
            Boolean flag = true;

            if (status.toString().contains("C")) {
                flag = false;
                status = "C";
            } else if (status.toString().contains("I")) {
                flag = true;
                status = "I";
            } else if (status.toString().contains("R")) {
                flag = false;
                status = "R";
            }

            Log.d("Final ", "Status " + status);

            PartObject md = new PartObject(po, part, receiptQty, po_Qty, sec_bin,
                    prior, decription, unit, document, bin, branch, mfg, status, receiveQty, "", "", epoorderno, oeitunumber, totalOrdQty, bin, sec_bin, flag, false);
            dummydata.add(md);
            adapter = new CustomAdapter(this, dummydata);
            part_list.setAdapter(adapter);

            Sessiondata.getInstance().setPartObjects(dummydata);
            Sessiondata.getInstance().setPartReceive(1);

            dummydata = Sessiondata.getInstance().getPartObjects();
        }

        item_unitprice = "";
        item_startbin = "";
        item_endbin = "";
        receiptQty = "";
        part_no.setText("");
    }

    public void ReceivePart_multiple() {

        if (PartsDetails_list != null) {
            if (PartsDetails_list.size() != 0) {
                for (int i = 0; i < PartsDetails_list.size(); i++) {
                    int po = PartsDetails_list.get(i).getOrderno();
                    String part = PartsDetails_list.get(i).getPartno();
                    String receiptQty = "";
                    String po_Qty = String.valueOf(PartsDetails_list.get(i).getPoqty());
                    String sec_bin = PartsDetails_list.get(i).getEndbin();
                    String prior = String.valueOf(PartsDetails_list.get(i).getPrior());
                    String decription = PartsDetails_list.get(i).getDescription();
                    String unit = PartsDetails_list.get(i).getPrice();
                    String document = String.valueOf(PartsDetails_list.get(i).getDocument());
                    String bin = PartsDetails_list.get(i).getStartbin();
                    String status;
                    int receivedQty;
                    if (receiptQty.isEmpty()) {
                        receivedQty = 0;
                    } else {
                        receivedQty = Integer.parseInt(receiptQty);
                    }
                    int poqty;
                    if (po_Qty.isEmpty()) {
                        poqty = 0;
                    } else {
                        poqty = Integer.parseInt(po_Qty);
                    }

                    int priors = PartsDetails_list.get(i).getPrior();
                    int received = priors + receivedQty;

                    /*Boolean flag;
                    if (received >= poqty){
                        flag = false;
                        status = "C";
                    }else {
                        flag = true;
                        status = "I";
                    }*/

                    String branch = PartsDetails_list.get(i).getBranch();
                    String mfg = PartsDetails_list.get(i).getMfg();
                    int receiveQty = 0;
                    int epoorderno = PartsDetails_list.get(i).getOepordno();
                    int oeitunumber = PartsDetails_list.get(i).getOeitemnum();

                    int totalOrdQty = PartsDetails_list.get(i).getTotalOrdQty();

                    status = PartsDetails_list.get(0).getCstatus();
                    Boolean flag = true;

                    if (status.toString().contains("C")) {
                        flag = false;
                        status = "C";
                    } else if (status.toString().contains("I")) {
                        flag = true;
                        status = "I";
                    } else if (status.toString().contains("R")) {
                        flag = false;
                        status = "R";
                    }
                    Log.d("Final ", "Status " + status);

                    PartObject md = new PartObject(po, part, receiptQty, po_Qty, sec_bin,
                            prior, decription, unit, document, bin, branch, mfg, status, receiveQty, "", "", epoorderno, oeitunumber, totalOrdQty, bin, sec_bin, flag, false);
                    dummydata.add(md);
                }

                adapter = new CustomAdapter(this, dummydata);
                part_list.setAdapter(adapter);

                Sessiondata.getInstance().setPartObjects(dummydata);
                Sessiondata.getInstance().setPartReceive(1);

                dummydata = Sessiondata.getInstance().getPartObjects();
            }
        }
        item_unitprice = "";
        item_startbin = "";
        item_endbin = "";
        receiptQty = "";
        part_no.setText("");
    }

    public Boolean update_lineitems() {
        Boolean flag = false;
        if (flag_submit == 1) {
            //submit entered items
            if (dummydata != null) {
                for (int i = 0; i < dummydata.size(); i++) {
                    String str_receipt = dummydata.get(i).getReceipt_qty();
                    String po_Qty = dummydata.get(i).getPo_qty();
                    String prior = dummydata.get(i).getPrior();

                    int receipt;
                    if (str_receipt.isEmpty()) {
                        receipt = 0;
                    } else {
                        receipt = Integer.parseInt(str_receipt);
                    }

                    int final_Qty;
                    if (po_Qty.isEmpty()) {
                        final_Qty = 0;
                    } else {
                        final_Qty = Integer.parseInt(po_Qty);
                    }
                    int final_prior;
                    if (prior.isEmpty()) {
                        final_prior = 0;
                    } else {
                        final_prior = Integer.parseInt(prior);
                    }

                    String status = dummydata.get(i).getStatus();
                    dummydata.get(i).setStatus(status);
                    dummydata.get(i).setEnter_receiveQty(receipt);
                    dummydata.get(i).setReceipt_qty(String.valueOf(receipt));

                    adapter.notifyDataSetChanged();
                    flag = true;
                }
            }
        } else if (flag_submit == 2) {
            //blanket receipt
            if (dummydata != null) {
                for (int i = 0; i < dummydata.size(); i++) {
                    String str_receipt = dummydata.get(i).getReceipt_qty();
                    String po_Qty = dummydata.get(i).getPo_qty();
                    String prior = dummydata.get(i).getPrior();

                    int receipt;
                    if (str_receipt.isEmpty()) {
                        receipt = 0;
                    } else {
                        receipt = Integer.parseInt(str_receipt);
                    }

                    int final_Qty;
                    if (po_Qty.isEmpty()) {
                        final_Qty = 0;
                    } else {
                        final_Qty = Integer.parseInt(po_Qty);
                    }
                    int final_prior;
                    if (prior.isEmpty()) {
                        final_prior = 0;
                    } else {
                        final_prior = Integer.parseInt(prior);
                    }

                    String status = dummydata.get(i).getStatus();

                    if (receipt == 0) {
                        if (status.toString().contains("R")) {
                            dummydata.get(i).setStatus("R");
                            dummydata.get(i).setEnter_receiveQty(final_Qty);
                            dummydata.get(i).setReceipt_qty(po_Qty);
                        } else if (status.toString().contains("C")) {
                            dummydata.get(i).setStatus("C");
                            dummydata.get(i).setEnter_receiveQty(receipt);
                            dummydata.get(i).setReceipt_qty(String.valueOf(receipt));
                        } else {
                            //2>=5
                            if (final_prior >= final_Qty) {
                                dummydata.get(i).setEnter_receiveQty(final_Qty);
                                dummydata.get(i).setReceipt_qty(po_Qty);
                            } else {
                                int final_receipt = final_Qty - final_prior;
                                dummydata.get(i).setEnter_receiveQty(final_receipt);
                                dummydata.get(i).setReceipt_qty(String.valueOf(final_receipt));
                            }
                            dummydata.get(i).setStatus("C");
                        }
                    } else {
                        if (final_prior >= final_Qty) {
                            dummydata.get(i).setStatus(status);
                            dummydata.get(i).setEnter_receiveQty(receipt);
                            dummydata.get(i).setReceipt_qty(String.valueOf(receipt));
                        } else {
                            dummydata.get(i).setStatus(status);
                            dummydata.get(i).setEnter_receiveQty(receipt);
                            dummydata.get(i).setReceipt_qty(String.valueOf(receipt));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    flag = true;
                }
            }
        }
        return flag;
    }

    public static enum DeviceType {MX_1000}

    public class AsynValidateOrders extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {

            ProgressBar.showCommonProgressDialog(PartsReceivingDetailsActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("validateorder_usertoken", "" + usertoken);

                for (validateindex = validatePosition; validateindex < Sessiondata.getInstance().getArray_EPONum().size(); validatePosition++) {

                    chk_orderno = Sessiondata.getInstance().getArray_EPONum().get(validatePosition).getName();
                    Log.d("chk_orderno", "" + chk_orderno);
                    Log.d("chk_partno", "" + entered_partno);

                    validateOrders = WebServiceConsumer.getInstance().ValidateOrders(usertoken, Integer.parseInt(chk_orderno), entered_partno);
                    break;
                }
            } catch (java.net.SocketTimeoutException e) {
                validateOrders = null;
            } catch (Exception e) {
                validateOrders = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            receivepart_value = 0;

            ProgressBar.dismiss();

            Sessiondata.getInstance().setValidateOrders(validateOrders);

            if (validateOrders != null) {

                if (validateOrders.getMessage().length() != 0) {


                    String Result = validateOrders.getMessage();
                    String replace = Result.replace("Error - ", "");
                    if (validateOrders.getMessage().toString().contains("Session")) {

                        Session = 3;
                        if (checkInternetConenction()) {

                            new AsyncSessionLoginTask().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }
                }

                String status = Sessiondata.getInstance().getValidateOrders().getStatus();
                String msg = Sessiondata.getInstance().getValidateOrders().getMessage();

                Log.d("validateOrders", "" + validateOrders.toString());

                if (status.toString().equalsIgnoreCase("False")) {

                    if (validatePosition == Sessiondata.getInstance().getArray_EPONum().size()) {
                        if (dummydata.size() == 0) {
                            if (sweetalrt) {
                                sweetalrt = false;
                                mDialogreplace = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                                        .setTitleText("Replace Parts!")
                                        .setContentText("Part# doesn't match.Are you sure you want to replace part?")
                                        .setCancelText("No,Cancel")
                                        .setConfirmText("Yes,Replace it")
                                        .showCancelButton(true)

                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                sweetalrt = true;
                                                sDialog.dismiss();
                                            }
                                        })
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                Sessiondata.getInstance().setPo_recive("");
                                                Sessiondata.getInstance().setPo_recive_value("");
                                                sweetalrt = true;
                                                Sessiondata.getInstance().setPartReceive(1);
                                                Intent intent = new Intent(PartsReceivingDetailsActivity.this, ReplacePartsActivity.class);
                                                intent.putExtra("PartNo", part_no.getText().toString());
                                                intent.putExtra("OldPartNo", "");
                                                intent.putExtra("Mfr", "");
                                                intent.putExtra("Order_no", "");
                                                intent.putExtra("Epoorder_no", "");
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                sDialog.dismiss();
                                            }
                                        });
                                mDialogreplace.setCancelable(false);
                                mDialogreplace.show();
                            }
                        } else if (dummydata.size() != 0) {
                            for (int i = 0; i < dummydata.size(); i++) {
                                Log.d("part_chk", "" + dummydata.get(i).getPart().toString());
                                if (dummydata.get(i).getPart().toString().equalsIgnoreCase(entered_partno)) {
                                    add_part_status = true;

                                    if (sweetalrt) {
                                        sweetalrt = false;
                                        part_alrt = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                .setTitleText("Warning!")
                                                .setContentText("Cannot add Same Part#")
                                                .setCancelText("Ok")
                                                .showCancelButton(false)
                                                .setConfirmClickListener(null)
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override

                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        add_part_status = false;
                                                        part_no.setText("");
                                                        sweetalrt = true;
                                                        sDialog.dismiss();
                                                    }
                                                });
                                        part_alrt.setCancelable(false);
                                        part_alrt.show();
                                        break;
                                    }
                                }
                            }
                            if (!add_part_status) {
                                if (sweetalrt) {
                                    sweetalrt = false;
                                    mDialogreplace = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                                            .setTitleText("Replace Parts!")
                                            .setContentText("Part# doesn't match.Are you sure you want to replace part?")
                                            .setCancelText("No,Cancel")
                                            .setConfirmText("Yes,Replace it")
                                            .showCancelButton(true)

                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {

                                                    sweetalrt = true;
                                                    sDialog.dismiss();
                                                }
                                            })
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    Sessiondata.getInstance().setPo_recive("");
                                                    Sessiondata.getInstance().setPo_recive_value("");
                                                    sweetalrt = true;
                                                    Sessiondata.getInstance().setPartReceive(1);
                                                    Intent intent = new Intent(PartsReceivingDetailsActivity.this, ReplacePartsActivity.class);
                                                    intent.putExtra("PartNo", part_no.getText().toString());
                                                    intent.putExtra("OldPartNo", "");
                                                    intent.putExtra("Mfr", "");
                                                    intent.putExtra("Order_no", "");
                                                    intent.putExtra("Epoorder_no", "");
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                    sDialog.dismiss();
                                                }
                                            });
                                    mDialogreplace.setCancelable(false);
                                    mDialogreplace.show();
                                }
                            }
                        } else {
                            if (sweetalrt) {
                                sweetalrt = false;
                                mDialogreplace = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                                        .setTitleText("Replace Parts!")
                                        .setContentText("Part# doesn't match.Are you sure you want to replace part?")
                                        .setCancelText("No,Cancel")
                                        .setConfirmText("Yes,Replace it")
                                        .showCancelButton(true)

                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                sweetalrt = true;
                                                sDialog.dismiss();
                                            }
                                        })
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                Sessiondata.getInstance().setPo_recive("");
                                                Sessiondata.getInstance().setPo_recive_value("");
                                                sweetalrt = true;
                                                Sessiondata.getInstance().setPartReceive(1);
                                                Intent intent = new Intent(PartsReceivingDetailsActivity.this, ReplacePartsActivity.class);
                                                intent.putExtra("PartNo", part_no.getText().toString());
                                                intent.putExtra("OldPartNo", "");
                                                intent.putExtra("Mfr", "");
                                                intent.putExtra("Order_no", "");
                                                intent.putExtra("Epoorder_no", "");
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
                                                sDialog.dismiss();
                                            }
                                        });
                                mDialogreplace.setCancelable(false);
                                mDialogreplace.show();
                            }
                        }
                    } else {
                        if (checkInternetConenction()) {
                            validateindex++;
                            validatePosition = validateindex;

                            new AsynValidateOrders().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }
                } else if (status.toString().equalsIgnoreCase("True")) {

                    validateindex++;
                    validatePosition = validateindex;

                    position = 0;

                    if (checkInternetConenction()) {

                        entered_partno = part_no.getText().toString();
                        Multipe_POFirst = Integer.parseInt(chk_orderno);

                        new AsyncGetPartDetls_list().execute();


                    } else {

                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                    }

                }

            } else {
                Toast.makeText(PartsReceivingDetailsActivity.this, "null value", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class AsynValidateOrders_LineItemClk extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {

            ProgressBar.showCommonProgressDialog(PartsReceivingDetailsActivity.this);
        }


        @Override
        protected Void doInBackground(Void... params) {

            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("validateorder_usertoken", "" + usertoken);

                Log.d("chk_orderno", "" + chk_orderno);
                Log.d("chk_partno", "" + entered_partno);

                validateOrders = WebServiceConsumer.getInstance().ValidateOrders(usertoken, LineItem_Po, LineItem_Part);

            } catch (java.net.SocketTimeoutException e) {
                validateOrders = null;
            } catch (Exception e) {
                validateOrders = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            receivepart_value = 0;

            ProgressBar.dismiss();

            Sessiondata.getInstance().setValidateOrders(validateOrders);

            if (validateOrders != null) {

                if (validateOrders.getMessage().length() != 0) {


                    String Result = validateOrders.getMessage();
                    String replace = Result.replace("Error - ", "");
                    if (validateOrders.getMessage().toString().contains("Session")) {

                        Session = 11;
                        if (checkInternetConenction()) {

                            new AsyncSessionLoginTask().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }

                    }
                }

                String status = Sessiondata.getInstance().getValidateOrders().getStatus();
                String msg = Sessiondata.getInstance().getValidateOrders().getMessage();

                Log.d("validateOrders", "" + validateOrders.toString());

                if (status.toString().equalsIgnoreCase("False")) {

                    if (sweetalrt) {
                        sweetalrt = false;

                        sweetaddalret = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Message!")
                                .setContentText("This Part# " + LineItem_Part + " is already received!")
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
                        sweetaddalret.setCancelable(false);
                        sweetaddalret.show();
                    }
                } else if (status.toString().equalsIgnoreCase("True")) {

                    if (checkInternetConenction()) {

                        single_receive_value = 0;

                        entered_partno = LineItem_Part;
                        Multipe_POFirst = LineItem_Po;

                        new AsyncGetPartDetls_LineItemClk().execute();
                    } else {
                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                Toast.makeText(PartsReceivingDetailsActivity.this, "null value", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class AsyncGetFreightDetails extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(PartsReceivingDetailsActivity.this);
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("branch_usertoken", "" + usertoken);
                Log.d("ordered_count", "" + array_partsdetails.size());

                getponum = new ArrayList<>();
                getordernew = new ArrayList<>();
                getponum_value = new ArrayList<>();
                getordernew_value = new ArrayList<>();

                String part_NO;
                int oepoder_NO;
                int po = 0;
                int oeiorderno = 0;
                int po_value = 0;

                for (int i = 0; i < dummydata.size(); i++) {

                    ordered = dummydata.get(i).getPo();
                    oeporderno = dummydata.get(i).getEpoorderno();

                    po_value = 0;
                    getponum.add(ordered);
                    getordernew.add(oeporderno);

                    if (getponum.size() > 1) {

                        if (po != ordered) {

                            for (int j = 0; j < getponum_value.size(); j++) {

                                if (po_value == 0) {

                                    int size = getponum_value.size();

                                    if (ordered != getponum_value.get(j)) {

                                        if (size == j + 1) {

                                            Log.d("ponumbertype1", "" + ordered);
                                            getponum_value.add(ordered);
                                            po_value = 1;

                                            part_NO = dummydata.get(i).getPart();
                                            oepoder_NO = dummydata.get(i).getEpoorderno();


                                            Log.d("NEW", "" + ordered);
                                            Log.d("Get_Freight_PARTNO", "" + part_NO);
                                            Log.d("Get_Freight_oepoder_NO", "" + oepoder_NO);
                                            freightdetails = WebServiceConsumer.getInstance().Getfreightdetails(
                                                    usertoken, ordered, part_NO, oepoder_NO);

                                        }

                                    } else {
                                        po_value = 1;
                                    }
                                } else {

                                }
                            }

                        } else if (oeiorderno != oeporderno) {

                            for (int j = 0; j < getordernew_value.size(); j++) {

                                if (po_value == 0) {

                                    int size = getordernew_value.size();

                                    if (oeporderno != getordernew_value.get(j)) {

                                        if (size == j + 1) {

                                            Log.d("ponumbertype1", "" + oeporderno);
                                            getordernew_value.add(oeporderno);
                                            po_value = 1;

                                            part_NO = dummydata.get(i).getPart();
                                            oepoder_NO = dummydata.get(i).getEpoorderno();


                                            Log.d("NEW", "" + ordered);
                                            Log.d("Get_Freight_PARTNO", "" + part_NO);
                                            Log.d("Get_Freight_oepoder_NO", "" + oepoder_NO);
                                            freightdetails = WebServiceConsumer.getInstance().Getfreightdetails(
                                                    usertoken, ordered, part_NO, oepoder_NO);

                                        }

                                    } else {
                                        po_value = 1;
                                    }
                                } else {

                                }
                            }
                        }
                    } else {

                        po = ordered;
                        oeiorderno = oeporderno;
                        getponum_value.add(ordered);
                        getordernew_value.add(oeporderno);


                        part_NO = dummydata.get(i).getPart();
                        oepoder_NO = dummydata.get(i).getEpoorderno();

                        Log.d("ponumbertype2", "" + ordered);
                        Log.d("NEW", "" + ordered);
                        Log.d("Get_Freight_PARTNO", "" + part_NO);
                        Log.d("Get_Freight_oepoder_NO", "" + oepoder_NO);
                        freightdetails = WebServiceConsumer.getInstance().Getfreightdetails(
                                usertoken, ordered, part_NO, oepoder_NO);

                    }

                }
            } catch (SocketTimeoutException e) {
                freightdetails = null;
                e.printStackTrace();
            } catch (Exception e) {
                freightdetails = null;
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            complete_btn_value = 0;
            Sessiondata.getInstance().setFreightlists(freightdetails);
            Sessiondata.getInstance().setFreightlistsnew(freightdetails);

            if (freightdetails != null) {

                if (freightdetails.size() == 1) {

                    if (freightdetails.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = freightdetails.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (freightdetails.get(0).getMessage().contains("Session")) {

                            freightdetails.clear();

                            Session = 2;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {
                                Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }

                        }
                    } else {
                        ProgressBar.dismiss();
                        Sessiondata.getInstance().setPo_recive("");
                        Sessiondata.getInstance().setPo_recive_value("");

                        if (Landing_Cost && Epo_Status) {

                            flag = update_lineitems();

                            Sessiondata.getInstance().setPartObjects(dummydata);

                            landed_cost.setVisibility(View.GONE);
                            Intent intent = new Intent(PartsReceivingDetailsActivity.this, AddFreightActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                        } else if (Landing_Cost && !Epo_Status) {

                            if (mDialogspread == null) {

                                mDialogspread = new Dialog(PartsReceivingDetailsActivity.this);
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
                                        if (LandingCost.getText().toString().equalsIgnoreCase("")) {

                                            if (sweetaddpo == true) {
                                                sweetaddpo = false;

                                                sweetaddalret = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                        .setTitleText("Alert!")
                                                        .setContentText("Please enter the landed cost")
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
                                        } else if (LandingCost.getText().toString().equalsIgnoreCase("0.0") || LandingCost.getText().toString().equalsIgnoreCase("0")) {

                                            if (sweetaddpo == true) {
                                                sweetaddpo = false;

                                                sweetaddalret = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                        .setTitleText("Warning!")
                                                        .setContentText("Landed Cost Cannot be Zero!")
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

                                                flag = update_lineitems();

                                                if (complete_btn_value == 0) {

                                                    landed_submit = Double.valueOf(LandingCost.getText().toString());
                                                    complete_btn_value = 1;
                                                    vendor_refernce();
//                                                    new AsynSetPartsHeadersubmit().execute();
                                                }

                                            } else {
                                                Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                            }

                                            finalMDialog.dismiss();
                                        }

                                    }
                                });

                                mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        submit_items = 0;
                                        finalMDialog.dismiss();
                                    }
                                });

                                mDialogspread.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                                mDialogspread.show();
                            } else if (!mDialogspread.isShowing()) {
                                mDialogspread.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                                mDialogspread.show();
                            }


                        } else if (!Landing_Cost && Epo_Status) {
                            flag = update_lineitems();

                            Sessiondata.getInstance().setPartObjects(dummydata);

                            landed_cost.setVisibility(View.GONE);
                            Intent intent = new Intent(PartsReceivingDetailsActivity.this, AddFreightActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                        } else {

                        }


                    }
                } else {
                    ProgressBar.dismiss();
                    Sessiondata.getInstance().setPo_recive("");
                    Sessiondata.getInstance().setPo_recive_value("");

                    if (Landing_Cost && Epo_Status) {
                        flag = update_lineitems();

                        Sessiondata.getInstance().setPartObjects(dummydata);

                        landed_cost.setVisibility(View.GONE);
                        Intent intent = new Intent(PartsReceivingDetailsActivity.this, AddFreightActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                    } else if (Landing_Cost && !Epo_Status) {

                        if (mDialogspread == null) {
                            mDialogspread = new Dialog(PartsReceivingDetailsActivity.this);
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
                                    if (LandingCost.getText().toString().equalsIgnoreCase("")) {

                                        if (sweetaddpo == true) {
                                            sweetaddpo = false;

                                            sweetaddalret = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("Please enter the landed cost")
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
                                    } else if (LandingCost.getText().toString().equalsIgnoreCase("0.0") || LandingCost.getText().toString().equalsIgnoreCase("0")) {

                                        if (sweetaddpo == true) {
                                            sweetaddpo = false;

                                            sweetaddalret = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Warning!")
                                                    .setContentText("Landed Cost Cannot be Zero!")
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

                                            flag = update_lineitems();

                                            if (complete_btn_value == 0) {
                                                landed_submit = Double.valueOf(LandingCost.getText().toString());
                                                complete_btn_value = 1;
                                                vendor_refernce();
//                                                new AsynSetPartsHeadersubmit().execute();
                                            }

                                        } else {
                                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                        }

                                        finalMDialog.dismiss();
                                    }


                                }
                            });

                            mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {

                                    submit_items = 0;
                                    finalMDialog.dismiss();
                                }
                            });

                            mDialogspread.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                            mDialogspread.show();
                        } else if (!mDialogspread.isShowing()) {
                            mDialogspread.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                            mDialogspread.show();
                        }


                    } else if (!Landing_Cost && Epo_Status) {

                        flag = update_lineitems();

                        Sessiondata.getInstance().setPartObjects(dummydata);

                        landed_cost.setVisibility(View.GONE);
                        Intent intent = new Intent(PartsReceivingDetailsActivity.this, AddFreightActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                    } else {

                    }
                }

            } else {
                ProgressBar.dismiss();

                if (sweetalrt) {
                    sweetalrt = false;
                    addpono = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                            .setTitleText("Alert!")
                            .setContentText("You are requried to add the parts#")
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
                    addpono.setCancelable(false);
                    addpono.show();
                }

            }
        }
    }

    public class AsyncGetPartDetls extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(PartsReceivingDetailsActivity.this);
        }

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... params) {
            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("partsreceiving_usertoken", "" + usertoken);
                Log.d("part_entered", "" + entered_partno);
                Log.d("Multipe_POFirstSend", "" + Multipe_POFirst);

                PartsDetails = WebServiceConsumer.getInstance().getPartsdetlsV3_obj(usertoken, String.valueOf(Multipe_POFirst), entered_partno);

            } catch (SocketTimeoutException e) {
                PartsDetails = null;
            } catch (Exception e) {
                PartsDetails = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (PartsDetails != null) {

                Sessiondata.getInstance().setPartsDetails(PartsDetails);
                Log.d("AsyncGetPartDetls", "" + PartsDetails);

                array_partsdetails.add(PartsDetails);

                if (PartsDetails.getMessage().length() != 0) {
                    ProgressBar.dismiss();

                    String Result = PartsDetails.getMessage();
                    String replace = Result.replace("Error - ", "");
                    if (PartsDetails.getMessage().toString().contains("Session")) {
                        Session = 0;
                        if (checkInternetConenction()) {

                            new AsyncSessionLoginTask().execute();

                        } else {
                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    ProgressBar.dismiss();

                    String enter_part = part_no.getText().toString();
                    String entered_part = enter_part.replaceAll("\\s", "");
                    Log.d("part_enter", "" + entered_part);

                    if (dummydata.size() != 0) {
                        for (int i = 0; i < dummydata.size(); i++) {
                            Log.d("part_chk", "" + dummydata.get(i).getPart().toString());
                            if (dummydata.get(i).getPart().toString().equalsIgnoreCase(entered_part)) {
                                add_part_status = true;

                                if (sweetalrt) {
                                    sweetalrt = false;
                                    part_alrt = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                            .setTitleText("Warning!")
                                            .setContentText("Cannot add Same Part#")
                                            .setCancelText("Ok")
                                            .showCancelButton(false)
                                            .setConfirmClickListener(null)
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override

                                                public void onClick(SweetAlertDialog sDialog) {
                                                    add_part_status = false;
                                                    part_no.setText("");
                                                    sweetalrt = true;
                                                    sDialog.dismiss();
                                                }
                                            });
                                    part_alrt.setCancelable(false);
                                    part_alrt.show();
                                    break;
                                }
                            }
                        }
                        if (!add_part_status) {

                            if ((mDialogs == null) || !mDialogs.isShowing()) {
                                mDialogs = new Dialog(PartsReceivingDetailsActivity.this);
                                mDialogs.setCanceledOnTouchOutside(false);
                                mDialogs.setCancelable(false);
                                mDialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                mDialogs.setContentView(R.layout.dialog_add_part_details);

                                TextView part_nos = (TextView) mDialogs.findViewById(R.id.part_no);
                                final EditText po_no = (EditText) mDialogs.findViewById(R.id.po_no);
                                final EditText edt_sec_bin = (EditText) mDialogs.findViewById(R.id.ordered);
                                final EditText edt_prior = (EditText) mDialogs.findViewById(R.id.prior);
                                final EditText edt_description = (EditText) mDialogs.findViewById(R.id.current);
                                final EditText edt_unit_price = (EditText) mDialogs.findViewById(R.id.unit_price);
                                final EditText edt_document = (EditText) mDialogs.findViewById(R.id.document);
                                final EditText edt_bin_location = (EditText) mDialogs.findViewById(R.id.bin_location);
                                final EditText edt_po_qty = (EditText) mDialogs.findViewById(R.id.po_qty);
                                final EditText edt_receipt_qty = (EditText) mDialogs.findViewById(R.id.receipt_qty);
                                final EditText eporder = (EditText) mDialogs.findViewById(R.id.eporder);
                                final EditText unit_weight = (EditText) mDialogs.findViewById(R.id.unit_weight);

                                TextView mDialogFreeCancelButton = (TextView) mDialogs.findViewById(R.id.dialog_social_cancel);

                                TextView mDialogFreeOKButton = (TextView) mDialogs.findViewById(R.id.dialog_social_ok);

                                part_nos.setText(PartsDetails.getPartno());
                                part_nos.setTypeface(header_face);
                                po_no.setText(Integer.toString(PartsDetails.getOrderno()));
                                po_no.setTypeface(header_face);
                                edt_prior.setText(Integer.toString(PartsDetails.getPrior()));
                                edt_prior.setTypeface(header_face);
                                edt_unit_price.setText(PartsDetails.getPrice());
                                edt_unit_price.setTypeface(header_face);
                                edt_document.setText(Integer.toString(PartsDetails.getDocument()));
                                edt_document.setTypeface(header_face);
                                edt_bin_location.setText(PartsDetails.getStartbin());
                                edt_bin_location.setTypeface(header_face);
                                edt_po_qty.setText(Integer.toString(PartsDetails.getPoqty()));
                                edt_po_qty.setTypeface(header_face);
                                eporder.setText(String.valueOf(PartsDetails.getOepordno()));
                                eporder.setTypeface(header_face);
                                edt_receipt_qty.setTypeface(header_face);
                                edt_description.setText(PartsDetails.getDescription());
                                edt_description.setTypeface(header_face);
                                edt_sec_bin.setText(PartsDetails.getEndbin());
                                edt_sec_bin.setTypeface(header_face);
                                unit_weight.setTypeface(header_face);
                                String weight_format = String.valueOf(dummydata.get(click_pos).getWeight());
                                weight_format = String.format("%.3f",Double.valueOf(weight_format));
                                unit_weight.setText(weight_format);


                                part = part_nos.getText().toString();

                                edt_receipt_qty.setEnabled(true);

                                edt_receipt_qty.requestFocus();

                                edt_receipt_qty.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                    keyboard.showSoftInput(edt_receipt_qty, 0);
                                                                }
                                                            }
                                        , 200);

                                po_no.setEnabled(false);
                                edt_prior.setEnabled(false);
                                edt_description.setEnabled(false);
                                edt_unit_price.setEnabled(true);
                                edt_document.setEnabled(false);
                                edt_po_qty.setEnabled(false);
                                eporder.setEnabled(false);
                                part_nos.setEnabled(false);


                                TextView txt_header = (TextView) mDialogs.findViewById(R.id.txt_header);
                                TextView txtpart = (TextView) mDialogs.findViewById(R.id.txt_part);
                                TextView txtpo = (TextView) mDialogs.findViewById(R.id.txt_po);
                                TextView txt_ordered = (TextView) mDialogs.findViewById(R.id.txt_ordered);
                                TextView txt_prior = (TextView) mDialogs.findViewById(R.id.txt_prior);
                                TextView txt_current = (TextView) mDialogs.findViewById(R.id.txt_current);
                                TextView txt_unit = (TextView) mDialogs.findViewById(R.id.txt_unit);
                                TextView txt_doc = (TextView) mDialogs.findViewById(R.id.txt_doc);
                                TextView txt_poqty = (TextView) mDialogs.findViewById(R.id.txt_poqty);
                                TextView txt_bin = (TextView) mDialogs.findViewById(R.id.txt_bin);
                                TextView txt_receiptqty = (TextView) mDialogs.findViewById(R.id.txt_receiptqty);
                                TextView txt_eporder = (TextView) mDialogs.findViewById(R.id.txt_eporder);
                                TextView txt_weight = (TextView) mDialogs.findViewById(R.id.txt_weight);

                                txt_header.setTypeface(header_face);
                                txtpart.setTypeface(header_face);
                                txtpo.setTypeface(header_face);
                                txt_ordered.setTypeface(header_face);
                                txt_prior.setTypeface(header_face);
                                txt_current.setTypeface(header_face);
                                txt_unit.setTypeface(header_face);
                                txt_doc.setTypeface(header_face);
                                txt_poqty.setTypeface(header_face);
                                txt_bin.setTypeface(header_face);
                                txt_receiptqty.setTypeface(header_face);
                                txt_eporder.setTypeface(header_face);
                                txt_weight.setTypeface(header_face);

                                mDialogFreeCancelButton.setTypeface(header_face);
                                mDialogFreeOKButton.setTypeface(header_face);

                                partsbranch = PartsDetails.getBranch();
                                partsMfg = PartsDetails.getMfg();
                                partnumber = PartsDetails.getPartno();
                                partsdescription = PartsDetails.getDescription();
                                partsorderqty = PartsDetails.getPoqty();
//                            partsreceivedqty = dummydata.get(click_pos).get
                                partsponumber = String.valueOf(PartsDetails.getOrderno());
                                partsoeprodernum = String.valueOf(PartsDetails.getOepordno());
                                partslocation = PartsDetails.getStartbin();



                                if (single_receive_value == 1) {
                                    edt_receipt_qty.setText(receiptQty);
                                    Log.d("single_receive_value", "" + edt_receipt_qty.getText().toString());
                                } else {
                                    edt_receipt_qty.setText("");
                                }


                                mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        Sessiondata.getInstance().setPo_recive("");
                                        Sessiondata.getInstance().setPo_recive_value("");
                                        receiptQty = edt_receipt_qty.getText().toString();

                                        if (receiptQty != null && !receiptQty.equals("")) {

                                            if (Sessiondata.getInstance().getPermission().contains("True")) {
                                                partsreceivedqty = Integer.parseInt(edt_receipt_qty.getText().toString());
                                                new AsyncPrintLabelsForPart().execute();
                                            }
                                        }

                                        po = Integer.parseInt(po_no.getText().toString());

                                        po_Qty = edt_po_qty.getText().toString();
                                        decription = edt_description.getText().toString();
                                        prior = edt_prior.getText().toString();
                                        sec_bin = edt_sec_bin.getText().toString();
                                        unit = edt_unit_price.getText().toString();
                                        document = edt_document.getText().toString();
                                        bin = edt_bin_location.getText().toString();
                                        epoorderno = Integer.parseInt(eporder.getText().toString());


                                        if (receiptQty != null && !receiptQty.equals("")) {
                                            if (Integer.parseInt(po_Qty) != Integer.parseInt(receiptQty)) {

                                                if (Integer.parseInt(po_Qty) > Integer.parseInt(receiptQty)) {
                                                    mDialogs.dismiss();
                                                    if (sweetalrt) {
                                                        sweetalrt = false;

                                                        if (multiplepo != null) {
                                                            ordereds = multiplepo.get(0).getPonum();
                                                        }

                                                        mDialogponotequal = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                                                                .setTitleText("Message!")
                                                                .setContentText("PO qty does not match with receive qty, Do you want to continue?")
                                                                .setCancelText("No")
                                                                .setConfirmText("Yes")
                                                                .showCancelButton(true)
                                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {

                                                                        if (checkInternetConenction()) {
                                                                            sweetalrt = true;
                                                                            single_receive_value = 1;
//                                                                            new AsyncGetPartDetls().execute();
                                                                            sDialog.dismiss();

                                                                        } else {

                                                                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                                                        }
                                                                    }
                                                                })
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        status = "I";

                                                                        sweetalrt = true;
                                                                        sDialog.dismiss();
                                                                        ReceivePart_Single();


                                                                    }
                                                                });
                                                        mDialogponotequal.setCancelable(false);
                                                        mDialogponotequal.show();
                                                    }
                                                } else {

                                                    mDialogs.dismiss();


                                                }

                                            } else {


                                                String branch = PartsDetails.getBranch();
                                                String mfg = PartsDetails.getMfg();
                                                int totalOrdQty = PartsDetails.getTotalOrdQty();

                                                String status = PartsDetails.getCstatus();
                                                Boolean flag = true;

                                                if (status.toString().contains("C")) {
                                                    flag = false;
                                                    status = "C";
                                                } else if (status.toString().contains("I")) {
                                                    flag = true;
                                                    status = "I";
                                                } else if (status.toString().contains("R")) {
                                                    flag = false;
                                                    status = "R";
                                                }

                                                //PartObject md = new PartObject(po, part, receiptQty, po_Qty, sec_bin, prior, decription, unit, document, bin, branch, mfg, "C", receiveQty, "", "",epoorderno,oeitunumber,totalOrdQty,bin,sec_bin,true);
                                                PartObject md = new PartObject(po, part, receiptQty, po_Qty, sec_bin, prior, decription, unit, document, bin, branch, mfg, status, receiveQty, "", "", epoorderno, oeitunumber, totalOrdQty, bin, sec_bin, flag, false);
                                                Log.d("qty_receipt", "" + receiptQty);
                                                Log.d("qty_po", "" + po_Qty);
                                                dummydata.add(md);
                                                Sessiondata.getInstance().setPartObjects(dummydata);
                                                Sessiondata.getInstance().setPartReceive(1);
                                                adapter.notifyDataSetChanged();
                                                part_no.setText("");

                                                mDialogs.dismiss();
                                            }
                                        } else {
                                            if (sweetalrt) {
                                                sweetalrt = false;
                                                ponoenter = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                        .setTitleText("Alert!")
                                                        .setContentText("You are requried to enter the receipt quantity!")
                                                        .setCancelText("Ok")
                                                        .showCancelButton(true)

                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sweetalrt = true;
                                                                sDialog.dismiss();
                                                            }
                                                        });
                                                ponoenter.setCancelable(false);
                                                ponoenter.show();
                                            }

                                        }


                                    }
                                });

                                mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        receiptQty = "";
                                        item_unitprice = "";
                                        item_startbin = "";
                                        item_endbin = "";
                                        mDialogs.dismiss();
                                    }
                                });
                                mDialogs.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                                mDialogs.show();
                            }
                        }
                    } else {
                        if ((mDialogs == null) || !mDialogs.isShowing()) {
                            mDialogs = new Dialog(PartsReceivingDetailsActivity.this);
                            mDialogs.setCanceledOnTouchOutside(false);
                            mDialogs.setCancelable(false);
                            mDialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            mDialogs.setContentView(R.layout.dialog_add_part_details);

                            TextView part_nos = (TextView) mDialogs.findViewById(R.id.part_no);
                            final EditText po_no = (EditText) mDialogs.findViewById(R.id.po_no);
                            final EditText edt_sec_bin = (EditText) mDialogs.findViewById(R.id.ordered);
                            final EditText edt_prior = (EditText) mDialogs.findViewById(R.id.prior);
                            final EditText edt_description = (EditText) mDialogs.findViewById(R.id.current);
                            final EditText edt_unit_price = (EditText) mDialogs.findViewById(R.id.unit_price);
                            final EditText edt_document = (EditText) mDialogs.findViewById(R.id.document);
                            final EditText edt_bin_location = (EditText) mDialogs.findViewById(R.id.bin_location);
                            final EditText edt_po_qty = (EditText) mDialogs.findViewById(R.id.po_qty);
                            final EditText edt_receipt_qty = (EditText) mDialogs.findViewById(R.id.receipt_qty);
                            final EditText eporder = (EditText) mDialogs.findViewById(R.id.eporder);
                            final EditText unit_weight = (EditText) mDialogs.findViewById(R.id.unit_weight);

                            TextView mDialogFreeCancelButton = (TextView) mDialogs.findViewById(R.id.dialog_social_cancel);

                            TextView mDialogFreeOKButton = (TextView) mDialogs.findViewById(R.id.dialog_social_ok);

                            part_nos.setText(PartsDetails.getPartno());
                            part_nos.setTypeface(header_face);
                            po_no.setText(Integer.toString(PartsDetails.getOrderno()));
                            po_no.setTypeface(header_face);
                            edt_prior.setText(Integer.toString(PartsDetails.getPrior()));
                            edt_prior.setTypeface(header_face);
                            edt_unit_price.setText(PartsDetails.getPrice());
                            edt_unit_price.setTypeface(header_face);
                            edt_document.setText(Integer.toString(PartsDetails.getDocument()));
                            edt_document.setTypeface(header_face);
                            edt_bin_location.setText(PartsDetails.getStartbin());
                            edt_bin_location.setTypeface(header_face);
                            edt_po_qty.setText(Integer.toString(PartsDetails.getPoqty()));
                            edt_po_qty.setTypeface(header_face);
                            edt_receipt_qty.setTypeface(header_face);
                            edt_description.setText(PartsDetails.getDescription());
                            edt_description.setTypeface(header_face);
                            edt_sec_bin.setText(PartsDetails.getEndbin());
                            edt_sec_bin.setTypeface(header_face);
                            eporder.setText(String.valueOf(PartsDetails.getOepordno()));
                            eporder.setTypeface(header_face);
                            unit_weight.setTypeface(header_face);
                            String weight_format = String.valueOf(dummydata.get(click_pos).getWeight());
                            weight_format = String.format("%.3f",Double.valueOf(weight_format));
                            unit_weight.setText(weight_format);

                            part = part_nos.getText().toString();


                            partsbranch = PartsDetails.getBranch();
                            partsMfg = PartsDetails.getMfg();
                            partnumber = PartsDetails.getPartno();
                            partsdescription = PartsDetails.getDescription();
                            partsorderqty = PartsDetails.getPoqty();
//                            partsreceivedqty = dummydata.get(click_pos).get
                            partsponumber = String.valueOf(PartsDetails.getOrderno());
                            partsoeprodernum = String.valueOf(PartsDetails.getOepordno());
                            partslocation = PartsDetails.getStartbin();

                            edt_receipt_qty.setEnabled(true);

                            edt_receipt_qty.requestFocus();

                            edt_receipt_qty.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                keyboard.showSoftInput(edt_receipt_qty, 0);
                                                            }
                                                        }
                                    , 200);

                            po_no.setEnabled(false);
                            edt_prior.setEnabled(false);
                            edt_description.setEnabled(false);
                            edt_unit_price.setEnabled(true);
                            edt_document.setEnabled(false);
                            edt_po_qty.setEnabled(false);
                            eporder.setEnabled(false);
                            part_nos.setEnabled(false);

                            TextView txt_header = (TextView) mDialogs.findViewById(R.id.txt_header);
                            TextView txtpart = (TextView) mDialogs.findViewById(R.id.txt_part);
                            TextView txtpo = (TextView) mDialogs.findViewById(R.id.txt_po);
                            TextView txt_ordered = (TextView) mDialogs.findViewById(R.id.txt_ordered);
                            TextView txt_prior = (TextView) mDialogs.findViewById(R.id.txt_prior);
                            TextView txt_current = (TextView) mDialogs.findViewById(R.id.txt_current);
                            TextView txt_unit = (TextView) mDialogs.findViewById(R.id.txt_unit);
                            TextView txt_doc = (TextView) mDialogs.findViewById(R.id.txt_doc);
                            TextView txt_poqty = (TextView) mDialogs.findViewById(R.id.txt_poqty);
                            TextView txt_bin = (TextView) mDialogs.findViewById(R.id.txt_bin);
                            TextView txt_receiptqty = (TextView) mDialogs.findViewById(R.id.txt_receiptqty);
                            TextView txt_weight = (TextView) mDialogs.findViewById(R.id.txt_weight);

                            txt_header.setTypeface(header_face);
                            txtpart.setTypeface(header_face);
                            txtpo.setTypeface(header_face);
                            txt_ordered.setTypeface(header_face);
                            txt_prior.setTypeface(header_face);
                            txt_current.setTypeface(header_face);
                            txt_unit.setTypeface(header_face);
                            txt_doc.setTypeface(header_face);
                            txt_poqty.setTypeface(header_face);
                            txt_bin.setTypeface(header_face);
                            txt_receiptqty.setTypeface(header_face);
                            txt_weight.setTypeface(header_face);
                            mDialogFreeCancelButton.setTypeface(header_face);
                            mDialogFreeOKButton.setTypeface(header_face);

                            if (single_receive_value == 1) {
                                edt_receipt_qty.setText(receiptQty);
                                Log.d("single_receive_value", "" + edt_receipt_qty.getText().toString());
                            } else {
                                edt_receipt_qty.setText("");
                            }


                            mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {


                                    receiptQty = edt_receipt_qty.getText().toString();


                                    if (receiptQty != null && !receiptQty.equals("")) {

                                        if (Sessiondata.getInstance().getPermission().contains("True")) {
                                            partsreceivedqty = Integer.parseInt(edt_receipt_qty.getText().toString());
                                            new AsyncPrintLabelsForPart().execute();
                                        }
                                    }

                                    po = Integer.parseInt(po_no.getText().toString());

                                    po_Qty = edt_po_qty.getText().toString();
                                    decription = edt_description.getText().toString();
                                    prior = edt_prior.getText().toString();
                                    sec_bin = edt_sec_bin.getText().toString();
                                    unit = edt_unit_price.getText().toString();
                                    document = edt_document.getText().toString();
                                    bin = edt_bin_location.getText().toString();
                                    epoorderno = Integer.parseInt(eporder.getText().toString());
                                    oeitunumber = PartsDetails.getOeitemnum();
                                    if (receiptQty != null && !receiptQty.equals("")) {
                                        if (Integer.parseInt(po_Qty) != Integer.parseInt(receiptQty)) {

                                            if (Integer.parseInt(po_Qty) > Integer.parseInt(receiptQty)) {
                                                mDialogs.dismiss();
                                                if (sweetalrt) {
                                                    sweetalrt = false;

                                                    if (multiplepo != null) {
                                                        ordereds = multiplepo.get(0).getPonum();
                                                    }
                                                    mDialogponotequal = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                                                            .setTitleText("Message!")
                                                            .setContentText("PO qty does not match with receive qty, Do you want to continue?")
                                                            .setCancelText("No")
                                                            .setConfirmText("Yes")
                                                            .showCancelButton(true)
                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {

                                                                    if (checkInternetConenction()) {
                                                                        sweetalrt = true;
                                                                        single_receive_value = 1;
//                                                                        new AsyncGetPartDetls().execute();
                                                                        sDialog.dismiss();

                                                                    } else {

                                                                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                                                    }
                                                                }
                                                            })
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    status = "I";

                                                                    sweetalrt = true;
                                                                    sDialog.dismiss();
                                                                    ReceivePart_Single();


                                                                }
                                                            });
                                                    mDialogponotequal.setCancelable(false);
                                                    mDialogponotequal.show();
                                                }
                                            } else {

                                                mDialogs.dismiss();

                                                if (checkInternetConenction()) {
                                                    sweetalrt = true;
                                                    new AsyncGetPartDetls().execute();
                                                } else {
                                                    Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                                                }


                                            }


                                        } else {

                                            int totalOrdQty = PartsDetails.getTotalOrdQty();
                                            String branch = PartsDetails.getBranch();
                                            String mfg = PartsDetails.getMfg();

                                            String status = PartsDetails.getCstatus();
                                            Boolean flag = true;

                                            if (status.toString().contains("C")) {
                                                flag = false;
                                                status = "C";
                                            } else if (status.toString().contains("I")) {
                                                flag = true;
                                                status = "I";
                                            } else if (status.toString().contains("R")) {
                                                flag = false;
                                                status = "R";
                                            }

                                            //PartObject md = new PartObject(po, part, receiptQty, po_Qty, sec_bin, prior, decription, unit, document, bin, branch, mfg, "C", receiveQty, "", "",epoorderno,oeitunumber,totalOrdQty,bin,sec_bin,true);
                                            PartObject md = new PartObject(po, part, receiptQty, po_Qty, sec_bin, prior, decription, unit, document, bin, branch, mfg, status, receiveQty, "", "", epoorderno, oeitunumber, totalOrdQty, bin, sec_bin, flag, false);
                                            Log.d("qty_receipt", "" + receiptQty);
                                            Log.d("qty_po", "" + po_Qty);
                                            dummydata.add(md);
                                            Sessiondata.getInstance().setPartObjects(dummydata);
                                            Sessiondata.getInstance().setPartReceive(1);
                                            adapter.notifyDataSetChanged();
                                            part_no.setText("");

                                            mDialogs.dismiss();
                                        }
                                    } else {
                                        if (sweetalrt) {
                                            sweetalrt = false;
                                            ponoenter = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert")
                                                    .setContentText("You are requried to enter the receipt quantity!")
                                                    .setCancelText("Ok")
                                                    .showCancelButton(true)

                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt = true;
                                                            sDialog.dismiss();
                                                        }
                                                    });
                                            ponoenter.setCancelable(false);
                                            ponoenter.show();
                                        }
                                    }
                                }
                            });

                            mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    receiptQty = "";
                                    item_unitprice = "";
                                    item_startbin = "";
                                    item_endbin = "";
                                    mDialogs.dismiss();
                                }
                            });
                            mDialogs.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                            mDialogs.show();
                        }
                    }
                }
            } else {
                ProgressBar.dismiss();

            }
        }
    }

    public class AsyncGetPartDetls_list extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(PartsReceivingDetailsActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("partsreceiving_usertoken", "" + usertoken);
                Log.d("part_entered", "" + entered_partno);
                Log.d("Multipe_POFirstSend", "" + Multipe_POFirst);

                PartsDetails_list = WebServiceConsumer.getInstance().getPartsdetlsV3_list(usertoken, Sessiondata.getInstance().getPO_Array(), entered_partno);

            } catch (SocketTimeoutException e) {
                PartsDetails_list = null;
            } catch (Exception e) {
                PartsDetails_list = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (PartsDetails_list != null) {

                Sessiondata.getInstance().setPartsDetails_list(PartsDetails_list);
                Log.d("AsyncGetPartDetls", "" + PartsDetails_list);

                if (PartsDetails_list.size() != 0) {
                    if (PartsDetails_list.get(0).getMessage().length() != 0) {
                        ProgressBar.dismiss();

                        String Result = PartsDetails_list.get(0).getMessage();
                        String replace = Result.replace("Error - ", "");
                        if (PartsDetails_list.get(0).getMessage().toString().contains("Session")) {
                            Session = 12;
                            if (checkInternetConenction()) {

                                new AsyncSessionLoginTask().execute();

                            } else {
                                Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        ProgressBar.dismiss();

                        String enter_part = part_no.getText().toString();
                        String entered_part = enter_part.replaceAll("\\s", "");
                        Log.d("part_enter", "" + entered_part);

                        if (dummydata.size() != 0) {
                            for (int i = 0; i < dummydata.size(); i++) {
                                Log.d("part_chk", "" + dummydata.get(i).getPart().toString());
                                if (dummydata.get(i).getPart().toString().equalsIgnoreCase(entered_part)) {
                                    add_part_status = true;

                                    if (sweetalrt) {
                                        sweetalrt = false;
                                        part_alrt = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                .setTitleText("Warning!")
                                                .setContentText("Cannot add Same Part#")
                                                .setCancelText("Ok")
                                                .showCancelButton(false)
                                                .setConfirmClickListener(null)
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override

                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        add_part_status = false;
                                                        part_no.setText("");
                                                        sweetalrt = true;
                                                        sDialog.dismiss();
                                                    }
                                                });
                                        part_alrt.setCancelable(false);
                                        part_alrt.show();
                                        break;
                                    }
                                }
                            }
                            if (!add_part_status) {
                                if (PartsDetails_list != null) {
                                    if (PartsDetails_list.size() == 1) {
                                        ReceivePart_Single();
                                    } else if (PartsDetails_list.size() != 0 &&
                                            PartsDetails_list.size() > 1) {
                                        ReceivePart_multiple();
                                    }
                                }
                            }
                        } else {
                            if (PartsDetails_list != null) {
                                if (PartsDetails_list.size() == 1) {
                                    ReceivePart_Single();
                                } else if (PartsDetails_list.size() != 0 &&
                                        PartsDetails_list.size() > 1) {
                                    ReceivePart_multiple();
                                }
                            }
                        }
                    }
                }
            } else {
                ProgressBar.dismiss();

            }
        }
    }

    public class AsyncGetPartDetls_LineItemClk extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(PartsReceivingDetailsActivity.this);
        }

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... params) {
            try {
                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();
                Log.d("partsreceiving_usertoken", "" + usertoken);
                Log.d("part_entered", "" + entered_partno);
                Log.d("Multipe_POFirstSend", "" + Multipe_POFirst);
//                PartsDetails_list = WebServiceConsumer.getInstance().getPartsdetlsV3_list(usertoken, Sessiondata.getInstance().getPO_Array(),entered_partno);
            } catch (Exception e) {
                PartsDetails = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (PartsDetails == null) {

                ProgressBar.dismiss();

                String enter_part = part_no.getText().toString();
                String entered_part = enter_part.replaceAll("\\s", "");
                Log.d("part_enter", "" + entered_part);

                if (dummydata.size() != 0) {
                    if (!add_part_status) {

                        if ((mDialogs == null) || !mDialogs.isShowing()) {
                            mDialogs = new Dialog(PartsReceivingDetailsActivity.this);
                            mDialogs.setCanceledOnTouchOutside(false);
                            mDialogs.setCancelable(false);
                            mDialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            mDialogs.setContentView(R.layout.dialog_add_part_details);

                            TextView part_nos = (TextView) mDialogs.findViewById(R.id.part_no);
                            final EditText po_no = (EditText) mDialogs.findViewById(R.id.po_no);
                            final EditText edt_sec_bin = (EditText) mDialogs.findViewById(R.id.ordered);
                            final EditText edt_prior = (EditText) mDialogs.findViewById(R.id.prior);
                            final EditText edt_description = (EditText) mDialogs.findViewById(R.id.current);
                            final EditText edt_unit_price = (EditText) mDialogs.findViewById(R.id.unit_price);
                            final EditText edt_document = (EditText) mDialogs.findViewById(R.id.document);
                            final EditText edt_bin_location = (EditText) mDialogs.findViewById(R.id.bin_location);
                            final EditText edt_po_qty = (EditText) mDialogs.findViewById(R.id.po_qty);
                            final EditText edt_receipt_qty = (EditText) mDialogs.findViewById(R.id.receipt_qty);
                            final EditText eporder = (EditText) mDialogs.findViewById(R.id.eporder);
                            final EditText unit_weight = (EditText) mDialogs.findViewById(R.id.unit_weight);

                            TextView mDialogFreeCancelButton = (TextView) mDialogs.findViewById(R.id.dialog_social_cancel);

                            TextView mDialogFreeOKButton = (TextView) mDialogs.findViewById(R.id.dialog_social_ok);

                            part_nos.setText(dummydata.get(click_pos).getPart());
                            part_nos.setTypeface(header_face);
                            po_no.setText(Integer.toString(dummydata.get(click_pos).getPo()));
                            po_no.setTypeface(header_face);
                            edt_prior.setText(dummydata.get(click_pos).getPrior());
                            edt_prior.setTypeface(header_face);
                            edt_unit_price.setTypeface(header_face);
                            edt_document.setText(dummydata.get(click_pos).getDocument());
                            edt_document.setTypeface(header_face);
                            edt_bin_location.setText(dummydata.get(click_pos).getBinlocation());
                            edt_bin_location.setTypeface(header_face);
                            edt_po_qty.setText(dummydata.get(click_pos).getPo_qty());
                            edt_po_qty.setTypeface(header_face);
                            eporder.setText(String.valueOf(dummydata.get(click_pos).getEpoorderno()));
                            eporder.setTypeface(header_face);
                            edt_receipt_qty.setTypeface(header_face);
                            edt_description.setText(dummydata.get(click_pos).getDecription());
                            edt_description.setTypeface(header_face);
                            edt_sec_bin.setText(dummydata.get(click_pos).getSec_bin());
                            edt_sec_bin.setTypeface(header_face);
                            unit_weight.setTypeface(header_face);
                            String weight_format = String.valueOf(dummydata.get(click_pos).getWeight());
                            weight_format = String.format("%.3f",Double.valueOf(weight_format));
                            unit_weight.setText(weight_format);


                            partsbranch = dummydata.get(click_pos).getBranch();
                            partsMfg = dummydata.get(click_pos).getMfg();
                            partnumber = dummydata.get(click_pos).getPart();
                            partsdescription = dummydata.get(click_pos).getDecription();
                            partsorderqty = Integer.parseInt(dummydata.get(click_pos).getPo_qty());
//                            partsreceivedqty = dummydata.get(click_pos).get
                            partsponumber = String.valueOf(dummydata.get(click_pos).getPo());
                            partsoeprodernum = String.valueOf(dummydata.get(click_pos).getEpoorderno());
                            partslocation = dummydata.get(click_pos).getBinlocation();


                            part = part_nos.getText().toString();

                            edt_receipt_qty.setEnabled(true);

                            edt_receipt_qty.requestFocus();

                            edt_receipt_qty.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                keyboard.showSoftInput(edt_receipt_qty, 0);
                                                            }
                                                        }
                                    , 200);

                            po_no.setEnabled(false);
                            edt_prior.setEnabled(false);
                            edt_description.setEnabled(false);
                            edt_unit_price.setEnabled(true);
                            edt_document.setEnabled(false);
                            edt_po_qty.setEnabled(false);
                            eporder.setEnabled(false);
                            part_nos.setEnabled(false);


                            TextView txt_header = (TextView) mDialogs.findViewById(R.id.txt_header);
                            TextView txtpart = (TextView) mDialogs.findViewById(R.id.txt_part);
                            TextView txtpo = (TextView) mDialogs.findViewById(R.id.txt_po);
                            TextView txt_ordered = (TextView) mDialogs.findViewById(R.id.txt_ordered);
                            TextView txt_prior = (TextView) mDialogs.findViewById(R.id.txt_prior);
                            TextView txt_current = (TextView) mDialogs.findViewById(R.id.txt_current);
                            TextView txt_unit = (TextView) mDialogs.findViewById(R.id.txt_unit);
                            TextView txt_doc = (TextView) mDialogs.findViewById(R.id.txt_doc);
                            TextView txt_poqty = (TextView) mDialogs.findViewById(R.id.txt_poqty);
                            TextView txt_bin = (TextView) mDialogs.findViewById(R.id.txt_bin);
                            TextView txt_receiptqty = (TextView) mDialogs.findViewById(R.id.txt_receiptqty);
                            TextView txt_eporder = (TextView) mDialogs.findViewById(R.id.txt_eporder);
                            TextView txt_weight = (TextView) mDialogs.findViewById(R.id.txt_weight);

                            txt_header.setTypeface(header_face);
                            txtpart.setTypeface(header_face);
                            txtpo.setTypeface(header_face);
                            txt_ordered.setTypeface(header_face);
                            txt_prior.setTypeface(header_face);
                            txt_current.setTypeface(header_face);
                            txt_unit.setTypeface(header_face);
                            txt_doc.setTypeface(header_face);
                            txt_poqty.setTypeface(header_face);
                            txt_bin.setTypeface(header_face);
                            txt_receiptqty.setTypeface(header_face);
                            txt_eporder.setTypeface(header_face);
                            txt_weight.setTypeface(header_face);

                            mDialogFreeCancelButton.setTypeface(header_face);
                            mDialogFreeOKButton.setTypeface(header_face);

                            if (single_receive_value == 1) {
                                edt_receipt_qty.setText(receiptQty);
                                edt_unit_price.setText(item_unitprice);
                                edt_bin_location.setText(item_startbin);
                                edt_sec_bin.setText(item_endbin);
                                Log.d("single_receive_value", "" + edt_receipt_qty.getText().toString());
                            } else {
                                edt_receipt_qty.setText("");
                                edt_unit_price.setText(dummydata.get(click_pos).getUnit());
                                edt_bin_location.setText(dummydata.get(click_pos).getBinlocation());
                                edt_sec_bin.setText(dummydata.get(click_pos).getSec_bin());
                            }

                            mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {

                                    item_unitprice = edt_unit_price.getText().toString();
                                    item_startbin = edt_bin_location.getText().toString();
                                    item_endbin = edt_sec_bin.getText().toString();




                                    Sessiondata.getInstance().setPo_recive("");
                                    Sessiondata.getInstance().setPo_recive_value("");
                                    receiptQty = edt_receipt_qty.getText().toString();

                                    if (receiptQty != null && !receiptQty.equals("")) {

                                        if (Sessiondata.getInstance().getPermission().contains("True")) {
                                            partsreceivedqty = Integer.parseInt(edt_receipt_qty.getText().toString());
                                            new AsyncPrintLabelsForPart().execute();
                                        }
                                    }

                                    po = Integer.parseInt(po_no.getText().toString());

                                    po_Qty = edt_po_qty.getText().toString();
                                    decription = edt_description.getText().toString();
                                    prior = edt_prior.getText().toString();
                                    sec_bin = edt_sec_bin.getText().toString();
                                    unit = edt_unit_price.getText().toString();
                                    document = edt_document.getText().toString();
                                    bin = edt_bin_location.getText().toString();
                                    epoorderno = Integer.parseInt(eporder.getText().toString());


                                    if (receiptQty != null && !receiptQty.equals("")) {

                                        mDialogs.dismiss();

                                        int chk_multi_pos = 0;
                                        for (int i = 0; i < dummydata.size(); i++) {
                                            String parts = dummydata.get(click_pos).getPart();
                                            String Qty = dummydata.get(i).getPo_qty();
                                            String ReceiveQty = dummydata.get(i).getPrior();

                                            int final_Qty;
                                            if (Qty.isEmpty()) {
                                                final_Qty = 0;
                                            } else {
                                                final_Qty = Integer.parseInt(Qty);
                                            }

                                            int final_receipt;
                                            if (ReceiveQty.isEmpty()) {
                                                final_receipt = 0;
                                            } else {
                                                final_receipt = Integer.parseInt(ReceiveQty);
                                            }

                                            if (dummydata.get(i).getPart().toString().contains(parts)) {
                                                if (final_receipt <= final_Qty
                                                        && final_receipt != final_Qty) {
                                                    chk_multi_pos++;
                                                }
                                            }
                                        }

                                        Boolean chk_avail_qty = false;
                                        int chk_receivedQty;
                                        if (receiptQty.isEmpty()) {
                                            chk_receivedQty = 0;
                                        } else {
                                            chk_receivedQty = Integer.parseInt(receiptQty);
                                        }
                                        int chk_poqty;
                                        if (po_Qty.isEmpty()) {
                                            chk_poqty = 0;
                                        } else {
                                            chk_poqty = Integer.parseInt(po_Qty);
                                        }

                                        String Str_priors = dummydata.get(click_pos).getPrior();

                                        int chk_priors;
                                        if (Str_priors.isEmpty()) {
                                            chk_priors = 0;
                                        } else {
                                            chk_priors = Integer.parseInt(Str_priors);
                                        }

                                        int chk_received = chk_priors + chk_receivedQty;
                                        if (chk_received > chk_poqty) {
                                            chk_avail_qty = true;
                                        } else {
                                            chk_avail_qty = false;
                                        }
                                        Log.d("chk_avail_qty ", " " + chk_avail_qty);

                                        if (chk_multi_pos == 1) {

                                            if (Integer.parseInt(po_Qty) != chk_received) {

                                                if ((Integer.parseInt(po_Qty) < chk_received) && chk_avail_qty == true) {
                                                    if (sweetalrt) {
                                                        sweetalrt = false;

                                                        mDialogponotequal = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                                                                .setTitleText("Message!")
                                                                .setContentText("PO qty does not match with receive qty, Do you want to continue?")
                                                                .setCancelText("No")
                                                                .setConfirmText("Yes")
                                                                .showCancelButton(true)
                                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        if (checkInternetConenction()) {
                                                                            sweetalrt = true;
                                                                            single_receive_value = 1;
//                                                                                new AsyncGetPartDetls_LineItemClk().execute();
                                                                            sDialog.dismiss();

                                                                        } else {

                                                                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                                                        }
                                                                    }
                                                                })
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {

                                                                        int po = dummydata.get(click_pos).getPo();
                                                                        String part = dummydata.get(click_pos).getPart();
                                                                        String po_Qty = String.valueOf(dummydata.get(click_pos).getPo_qty());
                                                                        String sec_bin = dummydata.get(click_pos).getSec_bin();
                                                                        String prior = String.valueOf(dummydata.get(click_pos).getPrior());
                                                                        String decription = dummydata.get(click_pos).getDecription();
                                                                        String document = String.valueOf(dummydata.get(click_pos).getDocument());
                                                                        String bin = dummydata.get(click_pos).getBinlocation();
                                                                        String status;
                                                                        int receivedQty;
                                                                        if (receiptQty.isEmpty()) {
                                                                            receivedQty = 0;
                                                                        } else {
                                                                            receivedQty = Integer.parseInt(receiptQty);
                                                                        }
                                                                        int poqty;
                                                                        if (po_Qty.isEmpty()) {
                                                                            poqty = 0;
                                                                        } else {
                                                                            poqty = Integer.parseInt(po_Qty);
                                                                        }

                                                                        String Str_priors = dummydata.get(click_pos).getPrior();

                                                                        int chk_priors;
                                                                        if (Str_priors.isEmpty()) {
                                                                            chk_priors = 0;
                                                                        } else {
                                                                            chk_priors = Integer.parseInt(Str_priors);
                                                                        }

                                                                        int received = chk_priors + receivedQty;
                                                                        if (dummydata.get(click_pos).getStatus().toString().contains("R")) {
                                                                            status = "R";
                                                                        } else if (dummydata.get(click_pos).getStatus().toString().contains("C")) {
                                                                            status = "C";
                                                                        } else {
                                                                            if (received >= poqty) {
                                                                                status = "C";
                                                                            } else {
                                                                                status = "I";
                                                                            }
                                                                        }

                                                                        Log.d("Final ", "Status " + status);

                                                                        String branch = dummydata.get(click_pos).getBranch();
                                                                        String mfg = dummydata.get(click_pos).getMfg();
                                                                        int epoorderno = dummydata.get(click_pos).getEpoorderno();
                                                                        int oeitunumber = dummydata.get(click_pos).getOeitenum();

                                                                        String unit = item_unitprice;

                                                                        dummydata.get(click_pos).setPo(po);
                                                                        dummydata.get(click_pos).setPart(part);
                                                                        dummydata.get(click_pos).setPo_qty(po_Qty);
                                                                        dummydata.get(click_pos).setSec_bin(item_endbin);
                                                                        dummydata.get(click_pos).setPrior(prior);
                                                                        dummydata.get(click_pos).setDecription(decription);
                                                                        dummydata.get(click_pos).setUnitprice(item_unitprice);
                                                                        dummydata.get(click_pos).setDocument(document);
                                                                        dummydata.get(click_pos).setOld_sec_bin(sec_bin);
                                                                        dummydata.get(click_pos).setOld_binlocation(bin);
                                                                        dummydata.get(click_pos).setBinlocation(item_startbin);
                                                                        dummydata.get(click_pos).setStatus(status);
                                                                        dummydata.get(click_pos).setBranch(branch);
                                                                        dummydata.get(click_pos).setMfg(mfg);
                                                                        dummydata.get(click_pos).setEpoorderno(epoorderno);
                                                                        dummydata.get(click_pos).setOeitenum(oeitunumber);
                                                                        dummydata.get(click_pos).setReceipt_qty(receiptQty);

                                                                        dummydata.get(click_pos).setEnter_receiveQty(Integer.parseInt(receiptQty));
                                                                        dummydata.get(click_pos).setTransfer("");
                                                                        dummydata.get(click_pos).setOld_part("");

                                                                        receiptQty = "";
                                                                        item_unitprice = "";
                                                                        item_startbin = "";
                                                                        item_endbin = "";

                                                                        sweetalrt = true;
                                                                        sDialog.dismiss();

                                                                        Sessiondata.getInstance().setPartObjects(dummydata);
                                                                        Sessiondata.getInstance().setPartReceive(1);
                                                                        adapter.notifyDataSetChanged();

                                                                    }
                                                                });
                                                        mDialogponotequal.setCancelable(false);
                                                        mDialogponotequal.show();
                                                    }
                                                } else {
                                                    if (dummydata.get(click_pos).getStatus().toString().contains("R")) {
                                                        status = "R";
                                                    } else if (dummydata.get(click_pos).getStatus().toString().contains("C")) {
                                                        status = "C";
                                                    } else {
                                                        status = "I";
                                                    }

                                                    sweetalrt = true;

                                                    ReceivePartLineItem_Single();
                                                }
                                            } else {
                                                if (dummydata.get(click_pos).getStatus().toString().contains("R")) {
                                                    status = "R";
                                                } else {
                                                    status = "C";
                                                }

                                                sweetalrt = true;

                                                ReceivePartLineItem_Single();
                                            }
                                        } else if (chk_multi_pos >= 1) {
                                            if ((mMultipleDialog == null) || !mMultipleDialog.isShowing()) {
                                                mMultipleDialog = new Dialog(PartsReceivingDetailsActivity.this);
                                                mMultipleDialog.setCanceledOnTouchOutside(false);
                                                mMultipleDialog.setCancelable(false);
                                                mMultipleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                mMultipleDialog.setContentView(R.layout.dialog_get_multiple_po);

                                                TextView txt_header = (TextView) mMultipleDialog.findViewById(R.id.txt_header);
                                                txt_header.setTypeface(header_face);
                                                txt_header.setText("Multiple PO Number");
                                                ListMultiplepo = (ListView) mMultipleDialog.findViewById(R.id.list);

                                                int received_Qty = Integer.parseInt(receiptQty);
                                                Log.d("Changed ", "Enter_received_Qty " + received_Qty);

                                                String part = dummydata.get(click_pos).getPart();
                                                Log.d("Changed ", "MultiplePO_Part " + part);


                                                list_pos = new ArrayList<>();
                                                sorted_partno = new ArrayList<>();
                                                po_QTY = new ArrayList<>();
                                                received_QTY = new ArrayList<>();

                                                //Sort part no
                                                for (int i = 0; i < dummydata.size(); i++) {

                                                    String Qty = dummydata.get(i).getPo_qty();
                                                    String ReceiveQty = dummydata.get(i).getPrior();

                                                    int final_Qty;
                                                    if (Qty.isEmpty()) {
                                                        final_Qty = 0;
                                                    } else {
                                                        final_Qty = Integer.parseInt(Qty);
                                                    }

                                                    int final_receipt;
                                                    if (ReceiveQty.isEmpty()) {
                                                        final_receipt = 0;
                                                    } else {
                                                        final_receipt = Integer.parseInt(ReceiveQty);
                                                    }

                                                    if (dummydata.get(i).getPart().toString().contains(part)) {
                                                        if (final_receipt <= final_Qty
                                                                && final_receipt != final_Qty) {
                                                            sorted_partno.add(dummydata.get(i));
                                                            list_pos.add(i);
                                                            po_QTY.add(Integer.parseInt(dummydata.get(i).getPo_qty()));
                                                            received_QTY.add(Integer.parseInt(dummydata.get(i).getPrior()));
                                                        }
                                                    }
                                                }

                                                Log.d("Changed ", "ArraySize " + sorted_partno.size());

                                                //Split po_qty value
                                                received_list = new ArrayList<>();
                                                MultipleQtywithstatus = new ArrayList<>();
                                                Listreceive_qty = new ArrayList<>();

                                                for (int k = 0; k < po_QTY.size(); k++) {
                                                    int receivedQty_list = received_QTY.get(k);
                                                    int poQty = po_QTY.get(k);
                                                    int receivedQty = poQty - receivedQty_list;
                                                    Log.d("Changed ", "PO_QTY " + poQty);
                                                    Log.d("Changed ", "received_List " + receivedQty_list);
                                                    Log.d("Changed ", "receivedQty " + receivedQty);
                                                    if (receivedQty < received_Qty) {
                                                        if (k == (po_QTY.size()) - 1) {
                                                            int po = sorted_partno.get(k).getPo();
                                                            String parts = sorted_partno.get(k).getPart();
                                                            String branch = sorted_partno.get(k).getBranch();
                                                            String mfg = sorted_partno.get(k).getMfg();
                                                            int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                            int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                            int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                            String price = sorted_partno.get(k).getUnit();

                                                            int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                            int received = prior + received_Qty;
                                                            String Status;
                                                            if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                Status = "R";
                                                            } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                                Status = "C";
                                                            } else {
                                                                if (received >= poqty) {
                                                                    Status = "C";
                                                                } else {
                                                                    Status = "I";
                                                                }
                                                            }

                                                            Log.d("Changed ", "Status " + Status);

                                                            if (received_Qty < 0) {
                                                                received_Qty = 0;
                                                            }

                                                            multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                    branch, mfg, epoorderno, oeitunumber);
                                                            received_list.add(multiplePOWithStatus);
                                                            Listreceive_qty.add(k, received_Qty);
                                                        } else {
                                                            int po = sorted_partno.get(k).getPo();
                                                            String parts = sorted_partno.get(k).getPart();
                                                            String branch = sorted_partno.get(k).getBranch();
                                                            String mfg = sorted_partno.get(k).getMfg();
                                                            int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                            int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                            int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                            String price = sorted_partno.get(k).getUnit();

                                                            int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                            int received = prior + receivedQty;
                                                            String Status;
                                                            if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                Status = "R";
                                                            } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                                Status = "C";
                                                            } else {
                                                                if (received >= poqty) {
                                                                    Status = "C";
                                                                } else {
                                                                    Status = "I";
                                                                }
                                                            }

                                                            Log.d("Changed ", "Status " + Status);

                                                            if (receivedQty < 0) {
                                                                receivedQty = 0;
                                                            }

                                                            multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, receivedQty, po, poqty, "", parts,
                                                                    branch, mfg, epoorderno, oeitunumber);
                                                            received_list.add(multiplePOWithStatus);

                                                            Listreceive_qty.add(k, receivedQty);
                                                            if (k != 0) {
                                                                if (received_Qty == 0) {
                                                                    received_Qty = 0;
                                                                } else {
                                                                    if (receivedQty < received_Qty) {

                                                                    }
                                                                    received_Qty = received_Qty - receivedQty;
                                                                }
                                                            } else {
                                                                received_Qty = received_Qty - receivedQty;
                                                            }
                                                        }
                                                    } else {
                                                        if (k == (po_QTY.size()) - 1) {
                                                            int po = sorted_partno.get(k).getPo();
                                                            String parts = sorted_partno.get(k).getPart();
                                                            String branch = sorted_partno.get(k).getBranch();
                                                            String mfg = sorted_partno.get(k).getMfg();
                                                            int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                            int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                            int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                            String price = sorted_partno.get(k).getUnit();

                                                            int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                            int received = prior + received_Qty;
                                                            String Status;
                                                            if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                Status = "R";
                                                            } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                                Status = "C";
                                                            } else {
                                                                if (received >= poqty) {
                                                                    Status = "C";
                                                                } else {
                                                                    Status = "I";
                                                                }
                                                            }

                                                            Log.d("Changed ", "Status " + Status);

                                                            if (received_Qty < 0) {
                                                                received_Qty = 0;
                                                            }

                                                            multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                    branch, mfg, epoorderno, oeitunumber);
                                                            received_list.add(multiplePOWithStatus);
                                                            Listreceive_qty.add(k, received_Qty);
                                                        } else {
                                                            int po = sorted_partno.get(k).getPo();
                                                            String parts = sorted_partno.get(k).getPart();
                                                            String branch = sorted_partno.get(k).getBranch();
                                                            String mfg = sorted_partno.get(k).getMfg();
                                                            int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                            int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                            int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                            String price = sorted_partno.get(k).getUnit();

                                                            int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                            int received = prior + receivedQty;
                                                            String Status;
                                                            if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                                Status = "R";
                                                            } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                                Status = "C";
                                                            } else {
                                                                if (received >= poqty) {
                                                                    Status = "C";
                                                                } else {
                                                                    Status = "I";
                                                                }
                                                            }

                                                            Log.d("Changed ", "Status " + Status);

                                                            if (received_Qty < 0) {
                                                                received_Qty = 0;
                                                            }

                                                            multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                    branch, mfg, epoorderno, oeitunumber);
                                                            received_list.add(multiplePOWithStatus);
                                                            Listreceive_qty.add(k, received_Qty);

                                                            if (k != 0) {
                                                                if (received_Qty == 0) {
                                                                    received_Qty = 0;
                                                                } else {
                                                                    received_Qty = received_Qty - receivedQty;
                                                                }
                                                            } else {
                                                                received_Qty = received_Qty - receivedQty;
                                                            }

                                                        }
                                                    }

                                                }

                                                for (int i = 0; i < received_list.size(); i++) {
                                                    int remove_receivedQty = received_list.get(i).getReceiveqty();
                                                    if (remove_receivedQty != 0) {
                                                        MultipleQtywithstatus.add(received_list.get(i));
                                                    }
                                                }
                                                Log.d("Changed ", "received_list " + received_list.size());

                                                adapter_po_new = new CustomAdapter_multiplepo_new(PartsReceivingDetailsActivity.this, MultipleQtywithstatus);
                                                ListMultiplepo.setAdapter(adapter_po_new);
                                                ListMultiplepo.setTextFilterEnabled(true);
                                                count = MultipleQtywithstatus.size();
                                                thumbnailsselection_new = new boolean[count];

                                                TextView mDialogFreeOKButton = (TextView) mMultipleDialog.findViewById(R.id.dialog_proceed);
                                                mDialogFreeOKButton.setTypeface(header_face);

                                                TextView mDialogFreeCancelButton = (TextView) mMultipleDialog.findViewById(R.id.dialog_social_cancel);
                                                mDialogFreeCancelButton.setTypeface(header_face);

                                                mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View view) {

                                                        mMultipleDialog.dismiss();

                                                        int status_dep = 0;

                                                        if (MultipleQtywithstatus != null) {
                                                            if (MultipleQtywithstatus.size() != 0) {
                                                                for (int i = 0; i < MultipleQtywithstatus.size(); i++) {
                                                                    String status = MultipleQtywithstatus.get(i).getStatus();
                                                                    int poqty = MultipleQtywithstatus.get(i).getPoqty();
                                                                    int receiptqty = Listreceive_qty.get(i);

                                                                    int prior = MultipleQtywithstatus.get(i).getPrior();
                                                                    int received = prior + receiptqty;
                                                                    if (received == poqty) {

                                                                    } else if (received < poqty) {

                                                                    } else if (received > poqty) {
                                                                        status_dep = 1;
                                                                    }

                                                                }

                                                                if (status_dep == 1) {
                                                                    MultiplePO_Incomplete();
                                                                } else {
                                                                    MultiplePO_Complete();
                                                                }
                                                            }
                                                        }

                                                    }
                                                });

                                                mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View view) {
                                                        receiptQty = "";
                                                        item_unitprice = "";
                                                        item_startbin = "";
                                                        item_endbin = "";
                                                        mMultipleDialog.dismiss();
                                                    }
                                                });

                                                mMultipleDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                                                mMultipleDialog.show();
                                            }
                                        }
                                    } else {
                                        if (sweetalrt) {
                                            sweetalrt = false;
                                            ponoenter = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                    .setTitleText("Alert!")
                                                    .setContentText("You are requried to enter the receipt quantity!")
                                                    .setCancelText("Ok")
                                                    .showCancelButton(true)

                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sweetalrt = true;
                                                            sDialog.dismiss();
                                                        }
                                                    });
                                            ponoenter.setCancelable(false);
                                            ponoenter.show();
                                        }
                                    }

                                }
                            });

                            mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    receiptQty = "";
                                    item_unitprice = "";
                                    item_startbin = "";
                                    item_endbin = "";
                                    mDialogs.dismiss();
                                }
                            });
                            mDialogs.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                            mDialogs.show();
                        }
                    }
                } else {
                    if ((mDialogs == null) || !mDialogs.isShowing()) {
                        mDialogs = new Dialog(PartsReceivingDetailsActivity.this);
                        mDialogs.setCanceledOnTouchOutside(false);
                        mDialogs.setCancelable(false);
                        mDialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialogs.setContentView(R.layout.dialog_add_part_details);

                        TextView part_nos = (TextView) mDialogs.findViewById(R.id.part_no);
                        final EditText po_no = (EditText) mDialogs.findViewById(R.id.po_no);
                        final EditText edt_sec_bin = (EditText) mDialogs.findViewById(R.id.ordered);
                        final EditText edt_prior = (EditText) mDialogs.findViewById(R.id.prior);
                        final EditText edt_description = (EditText) mDialogs.findViewById(R.id.current);
                        final EditText edt_unit_price = (EditText) mDialogs.findViewById(R.id.unit_price);
                        final EditText edt_document = (EditText) mDialogs.findViewById(R.id.document);
                        final EditText edt_bin_location = (EditText) mDialogs.findViewById(R.id.bin_location);
                        final EditText edt_po_qty = (EditText) mDialogs.findViewById(R.id.po_qty);
                        final EditText edt_receipt_qty = (EditText) mDialogs.findViewById(R.id.receipt_qty);
                        final EditText eporder = (EditText) mDialogs.findViewById(R.id.eporder);
                        final EditText unit_weight = (EditText) mDialogs.findViewById(R.id.unit_weight);


                        TextView mDialogFreeCancelButton = (TextView) mDialogs.findViewById(R.id.dialog_social_cancel);

                        TextView mDialogFreeOKButton = (TextView) mDialogs.findViewById(R.id.dialog_social_ok);

                        part_nos.setText(dummydata.get(click_pos).getPart());
                        part_nos.setTypeface(header_face);
                        po_no.setText(Integer.toString(dummydata.get(click_pos).getPo()));
                        po_no.setTypeface(header_face);
                        edt_prior.setText(dummydata.get(click_pos).getPrior());
                        edt_prior.setTypeface(header_face);
                        edt_unit_price.setTypeface(header_face);
                        edt_document.setText(dummydata.get(click_pos).getDocument());
                        edt_document.setTypeface(header_face);
                        edt_bin_location.setText(dummydata.get(click_pos).getBinlocation());
                        edt_bin_location.setTypeface(header_face);
                        edt_po_qty.setText(dummydata.get(click_pos).getPo_qty());
                        edt_po_qty.setTypeface(header_face);
                        eporder.setText(String.valueOf(dummydata.get(click_pos).getEpoorderno()));
                        eporder.setTypeface(header_face);
                        edt_receipt_qty.setTypeface(header_face);
                        edt_description.setText(dummydata.get(click_pos).getDecription());
                        edt_description.setTypeface(header_face);
                        edt_sec_bin.setText(dummydata.get(click_pos).getSec_bin());
                        edt_sec_bin.setTypeface(header_face);
                        unit_weight.setTypeface(header_face);
                        String weight_format = String.valueOf(dummydata.get(click_pos).getWeight());
                        weight_format = String.format("%.3f",Double.valueOf(weight_format));
                        unit_weight.setText(weight_format);

                        partsbranch = dummydata.get(click_pos).getBranch();
                        partsMfg = dummydata.get(click_pos).getMfg();
                        partnumber = dummydata.get(click_pos).getPart();
                        partsdescription = dummydata.get(click_pos).getDecription();
                        partsorderqty = Integer.parseInt(dummydata.get(click_pos).getPo_qty());
//                            partsreceivedqty = dummydata.get(click_pos).get
                        partsponumber = String.valueOf(dummydata.get(click_pos).getPo());
                        partsoeprodernum = String.valueOf(dummydata.get(click_pos).getEpoorderno());
                        partslocation = dummydata.get(click_pos).getBinlocation();


                        part = part_nos.getText().toString();

                        edt_receipt_qty.setEnabled(true);

                        edt_receipt_qty.requestFocus();

                        edt_receipt_qty.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                            keyboard.showSoftInput(edt_receipt_qty, 0);
                                                        }
                                                    }
                                , 200);

                        po_no.setEnabled(false);
                        edt_prior.setEnabled(false);
                        edt_description.setEnabled(false);
                        edt_unit_price.setEnabled(true);
                        edt_document.setEnabled(false);
                        edt_po_qty.setEnabled(false);
                        eporder.setEnabled(false);
                        part_nos.setEnabled(false);

                        TextView txt_header = (TextView) mDialogs.findViewById(R.id.txt_header);
                        TextView txtpart = (TextView) mDialogs.findViewById(R.id.txt_part);
                        TextView txtpo = (TextView) mDialogs.findViewById(R.id.txt_po);
                        TextView txt_ordered = (TextView) mDialogs.findViewById(R.id.txt_ordered);
                        TextView txt_prior = (TextView) mDialogs.findViewById(R.id.txt_prior);
                        TextView txt_current = (TextView) mDialogs.findViewById(R.id.txt_current);
                        TextView txt_unit = (TextView) mDialogs.findViewById(R.id.txt_unit);
                        TextView txt_doc = (TextView) mDialogs.findViewById(R.id.txt_doc);
                        TextView txt_poqty = (TextView) mDialogs.findViewById(R.id.txt_poqty);
                        TextView txt_bin = (TextView) mDialogs.findViewById(R.id.txt_bin);
                        TextView txt_receiptqty = (TextView) mDialogs.findViewById(R.id.txt_receiptqty);
                        TextView txt_weight = (TextView) mDialogs.findViewById(R.id.txt_weight);

                        txt_header.setTypeface(header_face);
                        txtpart.setTypeface(header_face);
                        txtpo.setTypeface(header_face);
                        txt_ordered.setTypeface(header_face);
                        txt_prior.setTypeface(header_face);
                        txt_current.setTypeface(header_face);
                        txt_unit.setTypeface(header_face);
                        txt_doc.setTypeface(header_face);
                        txt_poqty.setTypeface(header_face);
                        txt_bin.setTypeface(header_face);
                        txt_receiptqty.setTypeface(header_face);
                        txt_weight.setTypeface(header_face);


                        mDialogFreeCancelButton.setTypeface(header_face);
                        mDialogFreeOKButton.setTypeface(header_face);

                        if (single_receive_value == 1) {
                            edt_receipt_qty.setText(receiptQty);
                            edt_unit_price.setText(item_unitprice);
                            edt_bin_location.setText(item_startbin);
                            edt_sec_bin.setText(item_endbin);

                            Log.d("single_receive_value", "" + edt_receipt_qty.getText().toString());
                        } else {
                            edt_receipt_qty.setText("");
                            edt_unit_price.setText(dummydata.get(click_pos).getUnit());
                            edt_bin_location.setText(dummydata.get(click_pos).getBinlocation());
                            edt_sec_bin.setText(dummydata.get(click_pos).getSec_bin());
                        }


                        mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                item_unitprice = edt_unit_price.getText().toString();
                                item_startbin = edt_bin_location.getText().toString();
                                item_endbin = edt_sec_bin.getText().toString();


                                receiptQty = edt_receipt_qty.getText().toString();

                                if (receiptQty != null && !receiptQty.equals("")) {

                                    if (Sessiondata.getInstance().getPermission().contains("True")) {
                                        partsreceivedqty = Integer.parseInt(edt_receipt_qty.getText().toString());
                                        new AsyncPrintLabelsForPart().execute();
                                    }
                                }

                                po = Integer.parseInt(po_no.getText().toString());

                                po_Qty = edt_po_qty.getText().toString();
                                decription = edt_description.getText().toString();
                                prior = edt_prior.getText().toString();
                                sec_bin = edt_sec_bin.getText().toString();
                                unit = edt_unit_price.getText().toString();
                                document = edt_document.getText().toString();
                                bin = edt_bin_location.getText().toString();
                                epoorderno = Integer.parseInt(eporder.getText().toString());

                                oeitunumber = dummydata.get(click_pos).getOeitenum();

                                if (receiptQty != null && !receiptQty.equals("")) {

                                    mDialogs.dismiss();
                                    int chk_multi_pos = 0;
                                    for (int i = 0; i < dummydata.size(); i++) {
                                        String parts = dummydata.get(click_pos).getPart();
                                        String Qty = dummydata.get(i).getPo_qty();
                                        String ReceiveQty = dummydata.get(i).getPrior();

                                        int final_Qty;
                                        if (Qty.isEmpty()) {
                                            final_Qty = 0;
                                        } else {
                                            final_Qty = Integer.parseInt(Qty);
                                        }

                                        int final_receipt;
                                        if (ReceiveQty.isEmpty()) {
                                            final_receipt = 0;
                                        } else {
                                            final_receipt = Integer.parseInt(ReceiveQty);
                                        }


                                        if (dummydata.get(i).getPart().toString().contains(parts)) {
                                            if (final_receipt <= final_Qty
                                                    && final_receipt != final_Qty) {
                                                chk_multi_pos++;
                                            }
                                        }
                                    }

                                    Boolean chk_avail_qty = false;
                                    int chk_receivedQty;
                                    if (receiptQty.isEmpty()) {
                                        chk_receivedQty = 0;
                                    } else {
                                        chk_receivedQty = Integer.parseInt(receiptQty);
                                    }
                                    int chk_poqty;
                                    if (po_Qty.isEmpty()) {
                                        chk_poqty = 0;
                                    } else {
                                        chk_poqty = Integer.parseInt(po_Qty);
                                    }

                                    String Str_priors = dummydata.get(click_pos).getPrior();

                                    int chk_priors;
                                    if (Str_priors.isEmpty()) {
                                        chk_priors = 0;
                                    } else {
                                        chk_priors = Integer.parseInt(Str_priors);
                                    }

                                    int chk_received = chk_priors + chk_receivedQty;
                                    if (chk_received > chk_poqty) {
                                        chk_avail_qty = true;
                                    } else {
                                        chk_avail_qty = false;
                                    }
                                    Log.d("chk_avail_qty ", " " + chk_avail_qty);

                                    if (chk_multi_pos == 1) {

                                        if (Integer.parseInt(po_Qty) != chk_received) {

                                            if ((Integer.parseInt(po_Qty) < chk_received) && chk_avail_qty == true) {
                                                if (sweetalrt) {
                                                    sweetalrt = false;

                                                    mDialogponotequal = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.INFO_TYPE)
                                                            .setTitleText("Message!")
                                                            .setContentText("PO qty does not match with receive qty, Do you want to continue?")
                                                            .setCancelText("No")
                                                            .setConfirmText("Yes")
                                                            .showCancelButton(true)
                                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    if (checkInternetConenction()) {
                                                                        sweetalrt = true;
                                                                        single_receive_value = 1;
//                                                                            new AsyncGetPartDetls_LineItemClk().execute();
                                                                        sDialog.dismiss();

                                                                    } else {

                                                                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                                                                    }
                                                                }
                                                            })
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {

                                                                    int po = dummydata.get(click_pos).getPo();
                                                                    String part = dummydata.get(click_pos).getPart();
                                                                    String po_Qty = String.valueOf(dummydata.get(click_pos).getPo_qty());
                                                                    String sec_bin = dummydata.get(click_pos).getSec_bin();
                                                                    String prior = String.valueOf(dummydata.get(click_pos).getPrior());
                                                                    String decription = dummydata.get(click_pos).getDecription();
                                                                    String document = String.valueOf(dummydata.get(click_pos).getDocument());
                                                                    String bin = dummydata.get(click_pos).getBinlocation();
                                                                    String status;
                                                                    int receivedQty;
                                                                    if (receiptQty.isEmpty()) {
                                                                        receivedQty = 0;
                                                                    } else {
                                                                        receivedQty = Integer.parseInt(receiptQty);
                                                                    }
                                                                    int poqty;
                                                                    if (po_Qty.isEmpty()) {
                                                                        poqty = 0;
                                                                    } else {
                                                                        poqty = Integer.parseInt(po_Qty);
                                                                    }

                                                                    int priors = Integer.parseInt(dummydata.get(click_pos).getPrior());
                                                                    int received = priors + receivedQty;
                                                                    if (dummydata.get(click_pos).getStatus().toString().contains("R")) {
                                                                        status = "R";
                                                                    } else if (dummydata.get(click_pos).getStatus().toString().contains("C")) {
                                                                        status = "C";
                                                                    } else {
                                                                        if (received >= poqty) {
                                                                            status = "C";
                                                                        } else {
                                                                            status = "I";
                                                                        }
                                                                    }

                                                                    Log.d("Final ", "Status " + status);

                                                                    String branch = dummydata.get(click_pos).getBranch();
                                                                    String mfg = dummydata.get(click_pos).getMfg();
                                                                    int epoorderno = dummydata.get(click_pos).getEpoorderno();
                                                                    int oeitunumber = dummydata.get(click_pos).getOeitenum();

                                                                    String unit = item_unitprice;

                                                                    dummydata.get(click_pos).setPo(po);
                                                                    dummydata.get(click_pos).setPart(part);
                                                                    dummydata.get(click_pos).setPo_qty(po_Qty);
                                                                    dummydata.get(click_pos).setSec_bin(item_endbin);
                                                                    dummydata.get(click_pos).setPrior(prior);
                                                                    dummydata.get(click_pos).setDecription(decription);
                                                                    dummydata.get(click_pos).setUnitprice(item_unitprice);
                                                                    dummydata.get(click_pos).setDocument(document);
                                                                    dummydata.get(click_pos).setOld_sec_bin(sec_bin);
                                                                    dummydata.get(click_pos).setOld_binlocation(bin);
                                                                    dummydata.get(click_pos).setBinlocation(item_startbin);
                                                                    dummydata.get(click_pos).setStatus(status);
                                                                    dummydata.get(click_pos).setBranch(branch);
                                                                    dummydata.get(click_pos).setMfg(mfg);
                                                                    dummydata.get(click_pos).setEpoorderno(epoorderno);
                                                                    dummydata.get(click_pos).setOeitenum(oeitunumber);
                                                                    dummydata.get(click_pos).setReceipt_qty(receiptQty);

                                                                    dummydata.get(click_pos).setEnter_receiveQty(Integer.parseInt(receiptQty));
                                                                    dummydata.get(click_pos).setTransfer("");
                                                                    dummydata.get(click_pos).setOld_part("");

                                                                    receiptQty = "";
                                                                    item_unitprice = "";
                                                                    item_startbin = "";
                                                                    item_endbin = "";

                                                                    sweetalrt = true;
                                                                    sDialog.dismiss();

                                                                    Sessiondata.getInstance().setPartObjects(dummydata);
                                                                    Sessiondata.getInstance().setPartReceive(1);
                                                                    adapter.notifyDataSetChanged();


                                                                }
                                                            });
                                                    mDialogponotequal.setCancelable(false);
                                                    mDialogponotequal.show();
                                                }
                                            } else {
                                                if (dummydata.get(click_pos).getStatus().toString().contains("R")) {
                                                    status = "R";
                                                } else if (dummydata.get(click_pos).getStatus().toString().contains("C")) {
                                                    status = "C";
                                                } else {
                                                    status = "I";
                                                }

                                                sweetalrt = true;

                                                ReceivePartLineItem_Single();
                                            }
                                        } else {
                                            if (dummydata.get(click_pos).getStatus().toString().contains("R")) {
                                                status = "R";
                                            } else {
                                                status = "C";
                                            }

                                            sweetalrt = true;

                                            ReceivePartLineItem_Single();
                                        }

                                    } else if (chk_multi_pos >= 1) {
                                        if ((mMultipleDialog == null) || !mMultipleDialog.isShowing()) {
                                            mMultipleDialog = new Dialog(PartsReceivingDetailsActivity.this);
                                            mMultipleDialog.setCanceledOnTouchOutside(false);
                                            mMultipleDialog.setCancelable(false);
                                            mMultipleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            mMultipleDialog.setContentView(R.layout.dialog_get_multiple_po);

                                            TextView txt_header = (TextView) mMultipleDialog.findViewById(R.id.txt_header);
                                            txt_header.setTypeface(header_face);
                                            txt_header.setText("Multiple PO Number");
                                            ListMultiplepo = (ListView) mMultipleDialog.findViewById(R.id.list);

                                            int received_Qty = Integer.parseInt(receiptQty);
                                            Log.d("Changed ", "Enter_received_Qty " + received_Qty);

                                            String part = dummydata.get(click_pos).getPart();
                                            Log.d("Changed ", "MultiplePO_Part " + part);


                                            list_pos = new ArrayList<>();
                                            sorted_partno = new ArrayList<>();
                                            po_QTY = new ArrayList<>();
                                            received_QTY = new ArrayList<>();

                                            //Sort part no
                                            for (int i = 0; i < dummydata.size(); i++) {

                                                String Qty = dummydata.get(i).getPo_qty();
                                                String ReceiveQty = dummydata.get(i).getPrior();

                                                int final_Qty;
                                                if (Qty.isEmpty()) {
                                                    final_Qty = 0;
                                                } else {
                                                    final_Qty = Integer.parseInt(Qty);
                                                }

                                                int final_receipt;
                                                if (ReceiveQty.isEmpty()) {
                                                    final_receipt = 0;
                                                } else {
                                                    final_receipt = Integer.parseInt(ReceiveQty);
                                                }

                                                if (dummydata.get(i).getPart().toString().contains(part)) {
                                                    if (final_receipt <= final_Qty
                                                            && final_receipt != final_Qty) {
                                                        sorted_partno.add(dummydata.get(i));
                                                        list_pos.add(i);
                                                        po_QTY.add(Integer.parseInt(dummydata.get(i).getPo_qty()));
                                                        received_QTY.add(Integer.parseInt(dummydata.get(i).getPrior()));
                                                    }
                                                }
                                            }

                                            Log.d("Changed ", "ArraySize " + sorted_partno.size());

                                            //Split po_qty value
                                            received_list = new ArrayList<>();
                                            MultipleQtywithstatus = new ArrayList<>();
                                            Listreceive_qty = new ArrayList<>();

                                            for (int k = 0; k < po_QTY.size(); k++) {
                                                int receivedQty_list = received_QTY.get(k);
                                                int poQty = po_QTY.get(k);
                                                int receivedQty = poQty - receivedQty_list;
                                                Log.d("Changed ", "PO_QTY " + poQty);
                                                Log.d("Changed ", "received_List " + receivedQty_list);
                                                Log.d("Changed ", "receivedQty " + receivedQty);
                                                if (receivedQty < received_Qty) {
                                                    if (k == (po_QTY.size()) - 1) {
                                                        int po = sorted_partno.get(k).getPo();
                                                        String parts = sorted_partno.get(k).getPart();
                                                        String branch = sorted_partno.get(k).getBranch();
                                                        String mfg = sorted_partno.get(k).getMfg();
                                                        int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                        int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                        int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                        String price = sorted_partno.get(k).getUnit();

                                                        int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                        int received = prior + received_Qty;
                                                        String Status;
                                                        if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                            Status = "R";
                                                        } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                            Status = "C";
                                                        } else {
                                                            if (received >= received_Qty) {
                                                                Status = "C";
                                                            } else {
                                                                Status = "I";
                                                            }
                                                        }

                                                        Log.d("Changed ", "Status " + Status);

                                                        if (received_Qty < 0) {
                                                            received_Qty = 0;
                                                        }

                                                        multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                branch, mfg, epoorderno, oeitunumber);
                                                        received_list.add(multiplePOWithStatus);
                                                        Listreceive_qty.add(k, received_Qty);
                                                    } else {
                                                        int po = sorted_partno.get(k).getPo();
                                                        String parts = sorted_partno.get(k).getPart();
                                                        String branch = sorted_partno.get(k).getBranch();
                                                        String mfg = sorted_partno.get(k).getMfg();
                                                        int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                        int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                        int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                        String price = sorted_partno.get(k).getUnit();

                                                        int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                        int received = prior + receivedQty;
                                                        String Status;
                                                        if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                            Status = "R";
                                                        } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                            Status = "C";
                                                        } else {
                                                            if (received >= poqty) {
                                                                Status = "C";
                                                            } else {
                                                                Status = "I";
                                                            }
                                                        }

                                                        Log.d("Changed ", "Status " + Status);

                                                        if (receivedQty < 0) {
                                                            receivedQty = 0;
                                                        }

                                                        multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, receivedQty, po, poqty, "", parts,
                                                                branch, mfg, epoorderno, oeitunumber);
                                                        received_list.add(multiplePOWithStatus);

                                                        Listreceive_qty.add(k, receivedQty);
                                                        if (k != 0) {
                                                            if (received_Qty == 0) {
                                                                received_Qty = 0;
                                                            } else {
                                                                received_Qty = received_Qty - receivedQty;
                                                            }
                                                        } else {
                                                            received_Qty = received_Qty - receivedQty;
                                                        }
                                                    }
                                                } else {
                                                    if (k == (po_QTY.size()) - 1) {
                                                        int po = sorted_partno.get(k).getPo();
                                                        String parts = sorted_partno.get(k).getPart();
                                                        String branch = sorted_partno.get(k).getBranch();
                                                        String mfg = sorted_partno.get(k).getMfg();
                                                        int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                        int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                        int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                        String price = sorted_partno.get(k).getUnit();

                                                        int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                        int received = prior + received_Qty;
                                                        String Status;
                                                        if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                            Status = "R";
                                                        } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                            Status = "C";
                                                        } else {
                                                            if (received >= received_Qty) {
                                                                Status = "C";
                                                            } else {
                                                                Status = "I";
                                                            }
                                                        }

                                                        Log.d("Changed ", "Status " + Status);

                                                        if (received_Qty < 0) {
                                                            received_Qty = 0;
                                                        }

                                                        multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                branch, mfg, epoorderno, oeitunumber);
                                                        received_list.add(multiplePOWithStatus);
                                                        Listreceive_qty.add(k, received_Qty);

                                                    } else {
                                                        int po = sorted_partno.get(k).getPo();
                                                        String parts = sorted_partno.get(k).getPart();
                                                        String branch = sorted_partno.get(k).getBranch();
                                                        String mfg = sorted_partno.get(k).getMfg();
                                                        int epoorderno = sorted_partno.get(k).getEpoorderno();
                                                        int oeitunumber = sorted_partno.get(k).getOeitenum();
                                                        int poqty = Integer.parseInt(sorted_partno.get(k).getPo_qty());
                                                        String price = sorted_partno.get(k).getUnit();

                                                        int prior = Integer.parseInt(sorted_partno.get(k).getPrior());
                                                        int received = prior + receivedQty;
                                                        String Status;
                                                        if (sorted_partno.get(k).getStatus().toString().contains("R")) {
                                                            Status = "R";
                                                        } else if (sorted_partno.get(k).getStatus().toString().contains("C")) {
                                                            Status = "C";
                                                        } else {
                                                            if (received >= poqty) {
                                                                Status = "C";
                                                            } else {
                                                                Status = "I";
                                                            }
                                                        }

                                                        Log.d("Changed ", "Status " + Status);

                                                        if (received_Qty < 0) {
                                                            received_Qty = 0;
                                                        }

                                                        multiplePOWithStatus = new MultiplePOWithStatus(Status, prior, price, received_Qty, po, poqty, "", parts,
                                                                branch, mfg, epoorderno, oeitunumber);
                                                        received_list.add(multiplePOWithStatus);
                                                        Listreceive_qty.add(k, received_Qty);

                                                        if (k != 0) {
                                                            if (received_Qty == 0) {
                                                                received_Qty = 0;
                                                            } else {
                                                                received_Qty = received_Qty - receivedQty;
                                                            }
                                                        } else {
                                                            received_Qty = received_Qty - receivedQty;
                                                        }
                                                    }
                                                }

                                            }

                                            for (int i = 0; i < received_list.size(); i++) {
                                                int remove_receivedQty = received_list.get(i).getReceiveqty();
                                                if (remove_receivedQty != 0) {
                                                    MultipleQtywithstatus.add(received_list.get(i));
                                                }
                                            }
                                            Log.d("Changed ", "received_list " + received_list.size());

                                            adapter_po_new = new CustomAdapter_multiplepo_new(PartsReceivingDetailsActivity.this, MultipleQtywithstatus);
                                            ListMultiplepo.setAdapter(adapter_po_new);
                                            ListMultiplepo.setTextFilterEnabled(true);
                                            count = MultipleQtywithstatus.size();
                                            thumbnailsselection_new = new boolean[count];

                                            TextView mDialogFreeOKButton = (TextView) mMultipleDialog.findViewById(R.id.dialog_proceed);
                                            mDialogFreeOKButton.setTypeface(header_face);

                                            TextView mDialogFreeCancelButton = (TextView) mMultipleDialog.findViewById(R.id.dialog_social_cancel);
                                            mDialogFreeCancelButton.setTypeface(header_face);

                                            mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View view) {

                                                    mMultipleDialog.dismiss();

                                                    int status_dep = 0;

                                                    if (MultipleQtywithstatus != null) {
                                                        if (MultipleQtywithstatus.size() != 0) {
                                                            for (int i = 0; i < MultipleQtywithstatus.size(); i++) {
                                                                String status = MultipleQtywithstatus.get(i).getStatus();
                                                                int poqty = MultipleQtywithstatus.get(i).getPoqty();
                                                                int receiptqty = Listreceive_qty.get(i);

                                                                int prior = MultipleQtywithstatus.get(i).getPrior();
                                                                int received = prior + receiptqty;
                                                                if (received == poqty) {

                                                                } else if (received < poqty) {

                                                                } else if (received > poqty) {
                                                                    status_dep = 1;
                                                                }
                                                            }

                                                            if (status_dep == 1) {
                                                                MultiplePO_Incomplete();
                                                            } else {
                                                                MultiplePO_Complete();
                                                            }
                                                        }
                                                    }

                                                }
                                            });

                                            mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View view) {
                                                    receiptQty = "";
                                                    item_unitprice = "";
                                                    item_startbin = "";
                                                    item_endbin = "";
                                                    mMultipleDialog.dismiss();
                                                }
                                            });

                                            mMultipleDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                                            mMultipleDialog.show();
                                        }
                                    }
                                    if (sweetalrt) {
                                        sweetalrt = false;
                                        ponoenter = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                                .setTitleText("Alert")
                                                .setContentText("You are requried to enter the receipt quantity!")
                                                .setCancelText("Ok")
                                                .showCancelButton(true)

                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sweetalrt = true;
                                                        sDialog.dismiss();
                                                    }
                                                });
                                        ponoenter.setCancelable(false);
                                        ponoenter.show();
                                    }

                                }


                            }
                        });

                        mDialogFreeCancelButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                receiptQty = "";
                                item_unitprice = "";
                                item_startbin = "";
                                item_endbin = "";
                                mDialogs.dismiss();
                            }
                        });
                        mDialogs.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        mDialogs.show();
                    }
                }
            } else {
                ProgressBar.dismiss();

            }
        }
    }

    public class CustomAdapter extends BaseAdapter {
        ArrayList<PartObject> result;
        Context context;
        int[] imageId;
        private LayoutInflater inflater = null;

        public CustomAdapter(Context context, ArrayList<PartObject> list) {

            result = list;
            selectedList = new ArrayList<Integer>();
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

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.receive_childrow, parent, false);
                holder = new ViewHolder();

                holder.part = (TextView) convertView.findViewById(R.id.part_value);
                holder.po = (TextView) convertView.findViewById(R.id.po_value);
                holder.receive_qty = (TextView) convertView.findViewById(R.id.receive_qty);
                holder.po_qty = (TextView) convertView.findViewById(R.id.po_qty);
                holder.eporderno = (TextView) convertView.findViewById(R.id.eporderno);

                holder.txtpart = (TextView) convertView.findViewById(R.id.txt_part);
                holder.txtpo = (TextView) convertView.findViewById(R.id.txt_po);
                holder.txtreceive_qty = (TextView) convertView.findViewById(R.id.txt_receiptqty);
                holder.txtpo_qty = (TextView) convertView.findViewById(R.id.txt_poqty);
                holder.txt_eporder = (TextView) convertView.findViewById(R.id.txt_eporder);

                holder.txtreceived_qty = (TextView) convertView.findViewById(R.id.txt_receivedqty);
                holder.received_qty = (TextView) convertView.findViewById(R.id.received_qty);

                holder.status_icon = (ImageView) convertView.findViewById(R.id.status_icon);

                holder.txtpart.setTypeface(header_face);
                holder.txt_eporder.setTypeface(header_face);
                holder.txtpo.setTypeface(header_face);
                holder.txtreceive_qty.setTypeface(header_face);
                holder.txtreceived_qty.setTypeface(header_face);
                holder.txtpo_qty.setTypeface(header_face);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PartObject m = result.get(position);
            holder.part.setText(m.getPart());
            holder.po.setText(String.valueOf(m.getPo()));
            holder.receive_qty.setText(m.getReceipt_qty());
            holder.po_qty.setText(m.getPo_qty());

            if (m.getEpoorderno() == 0) {
                holder.eporderno.setText("");
            } else {
                holder.eporderno.setText(String.valueOf(m.getEpoorderno()));
            }

            if (m.getPrior().equals("0") || m.getPrior().equals("")) {
                holder.received_qty.setText("");
            } else {
                holder.received_qty.setText(m.getPrior());
            }

            if (m.getStatus() == "I") {
                holder.txtreceived_qty.setText("Received Qty");
                holder.status_icon.setImageDrawable(getResources().getDrawable(R.drawable.icircle));
            } else if (m.getStatus() == "C") {
                holder.txtreceived_qty.setText("Received Qty");
                holder.status_icon.setImageDrawable(getResources().getDrawable(R.drawable.ccircle));
            } else if (m.getStatus() == "R") {
                holder.txtreceived_qty.setText("");
                holder.received_qty.setText("");
                holder.status_icon.setImageDrawable(getResources().getDrawable(R.drawable.rcircle));
            }


            holder.po_qty.setTypeface(txt_face);
            holder.part.setTypeface(txt_face);
            holder.po.setTypeface(txt_face);
            holder.receive_qty.setTypeface(txt_face);
            holder.eporderno.setTypeface(txt_face);
            holder.received_qty.setTypeface(txt_face);

            return convertView;
        }
    }

    private class ViewHolder {
        private TextView part, po, receive_qty, po_qty, eporderno, received_qty;
        private TextView txtpart, txtpo, txtreceive_qty, txtpo_qty, txt_eporder, txtreceived_qty;
        private ImageView status_icon;

        public ViewHolder() {
        }
    }

    public class CustomAdapter_multiplepo_new extends BaseAdapter {
        public HashMap<Integer, String> myList = new HashMap<Integer, String>();
        ArrayList<MultiplePOWithStatus> result;
        Context mContext;
        int index;

        public CustomAdapter_multiplepo_new(Context context, ArrayList<MultiplePOWithStatus> list) {

            mContext = context;
            this.result = list;

            for (int i = 0; i < count; i++) {
                myList.put(i, "");
            }
        }

        @Override
        public int getCount() {
            return result.size();
        }

        @Override
        public Object getItem(int position) {

            return result.get(position);
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Holder holder;

            if (convertView == null) {

                holder = new Holder();

                convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_get_multiplepo_childrow, null);

                holder.po_no = (TextView) convertView.findViewById(R.id.po_no);
                holder.quantity = (TextView) convertView.findViewById(R.id.po_quantity);
                holder.eporder = (TextView) convertView.findViewById(R.id.eporder);

                holder.receive_qty = (EditText) convertView.findViewById(R.id.receive_quantity);
                holder.txt_po = (TextView) convertView.findViewById(R.id.txt_po);
                holder.txt_poqty = (TextView) convertView.findViewById(R.id.txt_poqty);
                holder.txt_receive = (TextView) convertView.findViewById(R.id.txt_receive);
                holder.txt_eporder = (TextView) convertView.findViewById(R.id.txt_eporder);

                holder.txt_po.setTypeface(header_face);
                holder.txt_poqty.setTypeface(header_face);
                holder.txt_receive.setTypeface(header_face);
                holder.txt_eporder.setTypeface(header_face);

                holder.po_no.setTypeface(txt_face);
                holder.eporder.setTypeface(txt_face);
                holder.receive_qty.setTypeface(txt_face);
                holder.quantity.setTypeface(txt_face);

                holder.po_no.setText(String.valueOf(result.get(position).getPonum()));
                holder.quantity.setText(String.valueOf(result.get(position).getPoqty()));
                if (result.get(position).getEporder() == 0) {
                    holder.eporder.setText("");
                } else {
                    holder.eporder.setText(String.valueOf(result.get(position).getEporder()));
                }
                holder.receive_qty.setText(String.valueOf(Listreceive_qty.get(position)));

                holder.po = holder.quantity.getText().toString();
                holder.receive = holder.receive_qty.getText().toString();

                if (position != (result.size() - 1)) {
                    holder.receive_qty.setEnabled(false);
                } else {
                    if (holder.po.toString().equalsIgnoreCase(holder.receive)) {
                        holder.receive_qty.setEnabled(false);
                    } else {
                        holder.receive_qty.setEnabled(true);
                    }
                }

                if (holder.po.toString().equalsIgnoreCase(holder.receive)) {
                    holder.receive_qty.setText(String.valueOf(Listreceive_qty.get(position)));
                } else {
                    holder.receive_qty.setText(String.valueOf(result.get(position).getReceiveqty()));
                }

                convertView.setTag(holder);
                convertView.setTag(R.id.receive_quantity, holder.receive_qty);

            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.pos = position;
            holder.receive_qty.setTag(position);
            holder.po_no.setTag(position);
            holder.eporder.setTag(position);
            holder.quantity.setTag(position);


            holder.receive_qty.addTextChangedListener(new TextWatcher() {// add text watcher to update your data after editing
                public void afterTextChanged(Editable s) {
                    if (holder.pos == (count - 1)) {
                        if (s.toString().length() == 0) {
                            Listreceive_qty.add(count - 1, 0);
                        } else {
                            Listreceive_qty.add(count - 1, Integer.valueOf(holder.receive_qty.getText().toString()));
                            Log.d("change_value", "" + s.toString());

                        }


                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int pos = (Integer) holder.receive_qty.getTag();
                }
            });
            result.get(position).setReceiveqty(Listreceive_qty.get(position));
            holder.po_no.setText(String.valueOf(result.get(position).getPonum()));
            holder.eporder.setText(String.valueOf(result.get(position).getEporder()));

            holder.quantity.setText(String.valueOf(result.get(position).getPoqty()));
            holder.receive_qty.setText(String.valueOf(Listreceive_qty.get(position)));

            holder.po = holder.quantity.getText().toString();
            holder.receive = holder.receive_qty.getText().toString();

            if (position != (result.size() - 1)) {
                holder.receive_qty.setEnabled(false);
            } else {
                if (holder.po.toString().equalsIgnoreCase(holder.receive)) {
                    holder.receive_qty.setEnabled(false);
                } else {
                    holder.receive_qty.setEnabled(true);
                }
            }


            if (holder.po.toString().contains(holder.receive)) {
                holder.receive_qty.setText(String.valueOf(Listreceive_qty.get(position)));
            } else {

            }


            return convertView;
        }

    }

    class Holder {
        TextView po_no, description, quantity, eporder, txt_po, txt_poqty, txt_receive, txt_eporder;
        EditText receive_qty;
        String po, receive;
        int pos;
    }

    public class AsynSetPartsHeadersubmit extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(PartsReceivingDetailsActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                user = Sessiondata.getInstance().getLoginObject().getUsername();
                Log.d("send_landingCost", "" + landed_submit);
                Log.d("send_Type", "" + Type);
                Log.d("branch_value_new", "" + branch_value);

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


                                            SetPartsHeader = WebServiceConsumer.getInstance().SetPartsHeader(usertoken, branch_new, order_new, landed_submit, user);

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

                        SetPartsHeader = WebServiceConsumer.getInstance().SetPartsHeader(usertoken, branch_new, order_new, landed_submit, user);

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

            complete_btn_value = 0;
            if (SetPartsHeader != null) {
                Log.d("SetPartsHeader", "" + SetPartsHeader.toString());

                if (SetPartsHeader.toString().contains("Session")) {
                    ProgressBar.dismiss();

                    String Result = SetPartsHeader;
                    String replace = Result.replace("Error - ", "");

                    Session = 4;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (checkInternetConenction()) {


                        new AsynReplacePartDetailsTasksubmit().execute();

                    } else {

                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }
            } else {
                if (checkInternetConenction()) {


                    new AsynReplacePartDetailsTasksubmit().execute();

                } else {

                    Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public class AsynReplacePartDetailsTasksubmit extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                user = Sessiondata.getInstance().getLoginObject().getUsername();

                Log.d("SetPartsHeader1", "" + Integer.parseInt(SetPartsHeader));

                if (dummydata != null) {

                    Log.d("CountReplacePartsize", "" + dummydata.size());
                    for (int i = 0; i < dummydata.size(); i++) {

                        procqty = 0;
                        status_rep = dummydata.get(i).getStatus();


                        if (dummydata.get(i).getReplacedPart() != null) {
                            if (dummydata.get(i).getReplacedPart()) {

                                if (status_rep.contains("R")) {
                                    status_rep = "I";
//                                    dummydata.get(i).setStatus("I");
                                } else if (status_rep.contains("I")) {
                                    status_rep = "R";
//                                    dummydata.get(i).setStatus("R");
                                } else if (status_rep.contains("C")) {
                                    status_rep = "C";
                                }

                            }
                        }

                        if ((!status_rep.toString().contains("I")) && dummydata.get(i).getReplacedPart()) {

                            if (status_rep.equals("R")) {
                                status_rep = "I";
                            }

                            order_rep = dummydata.get(i).getPo();
                            old_part_rep = dummydata.get(i).getOld_part();
                            part_no_rep = dummydata.get(i).getPart();
                            qty_no_rep = Integer.parseInt(dummydata.get(i).getPo_qty());
                            mfr_rep = dummydata.get(i).getMfg();
                            bin_loc_rep = dummydata.get(i).getBinlocation();
                            transfer_his_rep = dummydata.get(i).getTransfer();
                            oeitunum = dummydata.get(i).getOeitenum();
                            epoordernum = dummydata.get(i).getEpoorderno();

                            if (!dummydata.get(i).getReceipt_qty().equals(""))
                                procqty = Integer.parseInt(dummydata.get(i).getReceipt_qty());
                            Log.d("transfer_his", "" + transfer_his_rep);

//                            replace_part = WebServiceConsumer.getInstance().SetRepalcePartDetails(usertoken, order_rep, mfr_rep, old_part_rep, part_no_rep, qty_no_rep, bin_loc_rep, transfer_his_rep, user,oeitunum);
//                            replace_part = WebServiceConsumer.getInstance().SetRepalcePartDetailsV2(usertoken, order_rep, mfr_rep, old_part_rep, part_no_rep, qty_no_rep, bin_loc_rep, transfer_his_rep, user,oeitunum,procqty);
//                            replace_part = WebServiceConsumer.getInstance().SetRepalcePartDetailsV3(usertoken, order_rep, mfr_rep, old_part_rep, part_no_rep, qty_no_rep, bin_loc_rep, transfer_his_rep, user, oeitunum, procqty, epoordernum);
//                            replace_part = WebServiceConsumer.getInstance().SetRepalcePartDetailsV4(usertoken, order_rep, mfr_rep, old_part_rep, part_no_rep, qty_no_rep, bin_loc_rep, transfer_his_rep, user, oeitunum, procqty, epoordernum, "R");
                            replace_part = WebServiceConsumer.getInstance().SetRepalcePartDetailsV4(usertoken, order_rep, mfr_rep, old_part_rep, part_no_rep, qty_no_rep, bin_loc_rep, transfer_his_rep, user, oeitunum, procqty, epoordernum, status_rep, vendor_reference, print_lable);
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

                    Session = 5;

                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (checkInternetConenction()) {


                        new AsyncSetPartDetailsResultsubmit().execute();

                    } else {

                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }

            } else {
                if (checkInternetConenction()) {


                    new AsyncSetPartDetailsResultsubmit().execute();

                } else {

                    Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class AsyncSetPartDetailsResultsubmit extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                usertoken = Sessiondata.getInstance().getLoginObject().getUsertoken();

                user = Sessiondata.getInstance().getLoginObject().getUsername();

                Log.d("SetPartsHeader2", "" + Integer.parseInt(SetPartsHeader));

                if (dummydata != null) {
                    Log.d("CountSetPartDetailssize", "" + dummydata.size());
                    for (int i = 0; i < dummydata.size(); i++) {

                        int oepordnum = dummydata.get(i).getEpoorderno();
                        ordereds_gets = dummydata.get(i).getPo();
                        branch_gets = dummydata.get(i).getBranch();
                        mfg_gets = dummydata.get(i).getMfg();
                        partno_gets = dummydata.get(i).getPart();

                        Double unit_price;
                        if (dummydata.get(i).getUnit().isEmpty()) {
                            unit_price = 0.0;
                        } else {
                            unit_price = Double.parseDouble(dummydata.get(i).getUnit());
                        }

                        if (unit_price == 0 || unit_price == 0.0) {
                            unit_price = 0.0;
                        } else {
                            unit_price = Double.parseDouble(dummydata.get(i).getUnit());
                        }

                        Log.d("receiveQty_get", "" + dummydata.get(i).getReceipt_qty().toString());
                        if (dummydata.get(i).getReceipt_qty().toString().isEmpty()) {
                            receiveQty_gets = 0;
                        } else {
                            receiveQty_gets = Integer.parseInt(dummydata.get(i).getReceipt_qty());
                        }

                        status_gets = dummydata.get(i).getStatus();

                        if (dummydata.get(i).getReplacedPart() != null) {
                            if (dummydata.get(i).getReplacedPart()) {


                                if (status_gets.contains("R")) {
                                    status_gets = "I";
//                                    dummydata.get(i).setStatus("I");
                                } else if (status_gets.contains("I")) {
                                    status_gets = "R";
//                                    dummydata.get(i).setStatus("R");
                                }

                            }
                        }

                        oeitenum = dummydata.get(i).getOeitenum();
                        Log.d("oepordnum_get", "" + oepordnum);
                        Log.d("ordereds_get", "" + ordereds_gets);
                        Log.d("partno_get", "" + partno_gets);
                        Log.d("status", "" + status_gets);
                        Log.d("branch", "" + branch_gets);
                        Log.d("mfg_get", "" + mfg_gets);
                        Log.d("receiveQty_get", "" + receiveQty_gets);
                        Log.d("getOeitemnum", "" + oeitenum);
                        Log.d("unit_price", "" + unit_price);
                        String ordiclocmain = dummydata.get(i).getOld_binlocation();
                        String ordiclocsec = dummydata.get(i).getOld_sec_bin();
                        String iclocmain = dummydata.get(i).getBinlocation();
                        String iclocsec = dummydata.get(i).getSec_bin();

                        int receipt;
                        if (dummydata.get(i).getReceipt_qty().toString().isEmpty()) {
                            receipt = 0;
                        } else {
                            receipt = Integer.parseInt(dummydata.get(i).getReceipt_qty().toString());
                        }

                        Log.d("getReceipt_qty", " 1 " + dummydata.get(i).getReceipt_qty().toString());

                        Boolean flag = dummydata.get(i).getFlag();


                        if (receipt == 0 && status_gets.contains("C") && flag == true) {
                            SetPartDetailsResultone = WebServiceConsumer.getInstance().SetPartDetailsV4(usertoken, oepordnum, ordereds_gets, branch_gets, mfg_gets, partno_gets, receiveQty_gets, status_gets, user, oeitenum, unit_price, ordiclocmain, ordiclocsec, iclocmain, iclocsec, vendor_reference, print_lable);
                            Log.d("Success ", " 1 " + i);
                        } else if (receipt != 0) {
                            if ((!status_gets.contains("R")) && (!dummydata.get(i).getReplacedPart())) {
                                SetPartDetailsResultone = WebServiceConsumer.getInstance().SetPartDetailsV4(usertoken, oepordnum, ordereds_gets, branch_gets, mfg_gets, partno_gets, receiveQty_gets, status_gets, user, oeitenum, unit_price, ordiclocmain, ordiclocsec, iclocmain, iclocsec, vendor_reference, print_lable);
                                Log.d("Success ", " 1 " + i);
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

            ProgressBar.dismiss();

            if (SetPartDetailsResultone != null) {

                if (SetPartDetailsResultone.toString().contains("Session")) {

                    String Result = SetPartDetailsResultone;
                    String replace = Result.replace("Error - ", "");

                    Session = 6;
                    if (checkInternetConenction()) {

                        new AsyncSessionLoginTask().execute();

                    } else {
                        Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {

                    if (sweetalrtsuccess) {
                        sweetalrtsuccess = false;

                        sweetalt = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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

                                        Intent intent = new Intent(PartsReceivingDetailsActivity.this, PartReceiptActivity.class);
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

                    sweetalt = new SweetAlertDialog(PartsReceivingDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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

                                    Intent intent = new Intent(PartsReceivingDetailsActivity.this, PartReceiptActivity.class);
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
            ProgressBar.showCommonProgressDialog(PartsReceivingDetailsActivity.this);
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
                        mDialogmsg = new Dialog(PartsReceivingDetailsActivity.this);
                        mDialogmsg.setCanceledOnTouchOutside(false);
                        mDialogmsg.setCancelable(false);
                        mDialogmsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialogmsg.setContentView(R.layout.activity_message);

                        TextView mDialogFreeCancelButton = (TextView) mDialogmsg.findViewById(R.id.dialog_social_cancel);
                        TextView mDialogtxt_header = (TextView) mDialogmsg.findViewById(R.id.txt_header);
                        TextView mDialogFreeOKButton = (TextView) mDialogmsg.findViewById(R.id.dialog_social_ok);

                        TextView txt_dialog = (TextView) mDialogmsg.findViewById(R.id.txt_dialog);
                        String Result = loginObject.getMessage().toString();
                        txt_dialog.setText(Result);
                        txt_dialog.setTypeface(txt_face);
                        mDialogtxt_header.setTypeface(header_face);
                        mDialogFreeOKButton.setTypeface(header_face);
                        mDialogFreeCancelButton.setTypeface(header_face);

                        final Dialog finalMDialog = mDialogmsg;
                        mDialogFreeOKButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(PartsReceivingDetailsActivity.this, LoginActivity.class);
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
                        mDialogmsg.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        mDialogmsg.show();
                    } else if (!mDialogmsg.isShowing()) {
                        mDialogmsg.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        mDialogmsg.show();
                    }

                } else {
                    ProgressBar.dismiss();

                    if (Session == 0) {
                        if (checkInternetConenction()) {

                            new AsyncGetPartDetls().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    } else if (Session == 2) {
                        if (checkInternetConenction()) {

                            new AsyncGetFreightDetails().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 3) {

                        if (checkInternetConenction()) {

                            new AsynValidateOrders().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }

                    } else if (Session == 4) {

                        if (checkInternetConenction()) {

                            new AsynSetPartsHeadersubmit().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 5) {

                        if (checkInternetConenction()) {

                           new AsynReplacePartDetailsTasksubmit().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 6) {

                        if (checkInternetConenction()) {


                            new AsyncSetPartDetailsResultsubmit().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 9) {

                        if (checkInternetConenction()) {
                            single_receive_value = 1;
                            new AsyncGetPartDetls_LineItemClk().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 11) {

                        if (checkInternetConenction()) {

                            new AsynValidateOrders_LineItemClk().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (Session == 12) {

                        if (checkInternetConenction()) {

                            new AsyncGetPartDetls_list().execute();

                        } else {

                            Toast.makeText(PartsReceivingDetailsActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            } else {
                ProgressBar.dismiss();

                if (mDialognodata == null) {
                    mDialognodata = new Dialog(PartsReceivingDetailsActivity.this);
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
                    mDialognodata.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                    mDialognodata.show();
                }

            }
        }
    }

    public void vendor_refernce() {

        vendor_referenceDialog = new Dialog(PartsReceivingDetailsActivity.this);
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

                if (checkBox.isChecked())
                {
                    print_lable = true;
                }
                else
                {
                    print_lable = false;
                }

                new AsynSetPartsHeadersubmit().execute();
                vendor_referenceDialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Landing_Cost && !Epo_Status) {
                    submit_items = 1;
                }
                complete_btn_value = 0;
                vendor_referenceDialog.dismiss();

            }
        });



        vendor_referenceDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_slide;

        if (vendor_referenceDialog != null)
            vendor_referenceDialog.show();





    }


}
