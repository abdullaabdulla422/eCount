package com.ebs.ecount.initial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ebs.ecount.R;
import com.ebs.ecount.parts_physical_counting.Physical_counting_activity;
import com.ebs.ecount.parts_physical_inventory.GlobalVariables;
import com.ebs.ecount.parts_physical_inventory.equipment_inventory_activity;
import com.ebs.ecount.parts_receipt.PartReceiptActivity;
import com.ebs.ecount.uidesigns.OptAnimationLoader;
import com.ebs.ecount.uidesigns.ProgressBar;
import com.ebs.ecount.uidesigns.SweetAlertDialog;
import com.ebs.ecount.utils.Sessiondata;
import com.ebs.ecount.webutils.WebServiceConsumer;



/**
 * Created by techunity on 21/11/16.
 */
public class Dashboard extends Activity {

    Button part_receipt,part_phy_cyclecount,equip_phy_invent;
    ImageView img_part_receipt,img_part_phy_cyclecount,img_equip_phy_invent;
    Button logout;
    String Logout;
    private AnimationSet mReceiptInAnim;
    private AnimationSet EquipmentAnim;
    RotateAnimation anim;
    SweetAlertDialog mDialoglogout;
    Boolean sweetalrt = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            String[] perms = {"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_PHONE_STATE","android.permission.ACCESS_FINE_LOCATION"};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
        }

        img_part_phy_cyclecount = findViewById(R.id.img_counting);
        img_part_receipt = findViewById(R.id.img_receipt);
        img_equip_phy_invent = findViewById(R.id.img_equp);

        anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.ABSOLUTE);
        anim.setDuration(1000);

        //Start animation
        mReceiptInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(Dashboard.this, R.anim.blink);// 2.3.x system don't support alpha-animation on layer-list drawable
        EquipmentAnim = (AnimationSet) OptAnimationLoader.loadAnimation(Dashboard.this, R.anim.equp_animation);// 2.3.x system don't support alpha-animation on layer-list drawable

        img_part_receipt.startAnimation(mReceiptInAnim);
        img_part_phy_cyclecount.startAnimation(anim);
        img_equip_phy_invent.startAnimation(EquipmentAnim);


        part_receipt = findViewById(R.id.part_receipt);
        part_phy_cyclecount = findViewById(R.id.part_phy_cyclecount);
        equip_phy_invent = findViewById(R.id.equip_phy_invent);


        logout = findViewById(R.id.logout);

        final Typeface face= Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");

        part_receipt.setTypeface(face);
        part_phy_cyclecount.setTypeface(face);
        equip_phy_invent.setTypeface(face);

        logout.setTypeface(face);

        part_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Sessiondata.getInstance().getPartObjects() != null){
                    Sessiondata.getInstance().getPartObjects().clear();
                }

                if (Sessiondata.getInstance().getGetEPOParts() != null){
                    Sessiondata.getInstance().getPartObjects().clear();
                }

                Intent intent = new Intent(Dashboard.this,PartReceiptActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
            }
        });

        part_phy_cyclecount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Sessiondata.getInstance().setCounting_processid("");
                Sessiondata.getInstance().setCounting_endbin("");
                Sessiondata.getInstance().setCounting_startbin("");
                Sessiondata.getInstance().setLoad_value_onresume("");

                Sessiondata.getInstance().setCounting_partnew("");
                Sessiondata.getInstance().setCounting_branchname("");
                Sessiondata.getInstance().setEqu_branch_name("");
                Sessiondata.getInstance().setScanner_partno(0);

                Sessiondata.getInstance().setCounting_branchno("");
                Sessiondata.getInstance().setChk_preliminary("");

                Sessiondata.getInstance().setFlagphy_addpart(0);

                Intent intent = new Intent(Dashboard.this,Physical_counting_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
            }
        });

        equip_phy_invent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.showerrormsg = false;
                GlobalVariables.checkbtn_load = 0;

                Intent intent = new Intent(Dashboard.this,equipment_inventory_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
            }
        });


        img_part_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sessiondata.getInstance().getPartObjects() != null){
                    Sessiondata.getInstance().getPartObjects().clear();
                }

                if (Sessiondata.getInstance().getGetEPOParts() != null){
                    Sessiondata.getInstance().getPartObjects().clear();
                }

                Intent intent = new Intent(Dashboard.this,PartReceiptActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
            }
        });

        img_part_phy_cyclecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sessiondata.getInstance().setCounting_processid("");
                Sessiondata.getInstance().setCounting_endbin("");
                Sessiondata.getInstance().setCounting_startbin("");
                Sessiondata.getInstance().setLoad_value_onresume("");

                Sessiondata.getInstance().setCounting_partnew("");
                Sessiondata.getInstance().setCounting_branchname("");
                Sessiondata.getInstance().setEqu_branch_name("");
                Sessiondata.getInstance().setScanner_partno(0);

                Sessiondata.getInstance().setCounting_branchno("");
                Sessiondata.getInstance().setChk_preliminary("");

                Sessiondata.getInstance().setFlagphy_addpart(0);

                Intent intent = new Intent(Dashboard.this,Physical_counting_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
            }
        });

        img_equip_phy_invent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariables.showerrormsg = false;
                GlobalVariables.checkbtn_load = 0;

                Intent intent = new Intent(Dashboard.this,equipment_inventory_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sweetalrt) {
                    sweetalrt = false;
                    mDialoglogout = new SweetAlertDialog(Dashboard.this, SweetAlertDialog.INFO_TYPE)
                            .setTitleText("Logout?")
                            .setContentText("Are you sure you want to logout?")
                            .setCancelText("No")
                            .setConfirmText("Yes")
                            .showCancelButton(true)


                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    if(checkInternetConenction()){

                                        new AsyncLogoutTask().execute();
                                        sDialog.dismiss();
                                        sweetalrt = true;
                                    }else{
                                        Toast.makeText(Dashboard.this, "No Internet Connection", Toast.LENGTH_LONG).show();
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
                    mDialoglogout.setCancelable(false);
                    mDialoglogout.show();
                }
            }
        });


    }

    public void ClearSession(){

        Sessiondata.getInstance().setReplaceadapter(0);
        Sessiondata.getInstance().setPartReceive(0);
        Sessiondata.getInstance().setAddFreight(0);
        Sessiondata.getInstance().setUnitId("");
        Sessiondata.getInstance().setModel("");
        Sessiondata.getInstance().setMake("");
        Sessiondata.getInstance().setSerial("");
        Sessiondata.getInstance().setSub_process("");
        Sessiondata.getInstance().setTemp_username("");
        Sessiondata.getInstance().setTemp_password("");
        Sessiondata.getInstance().setProcessId(0);
        Sessiondata.getInstance().setInBound_data(0.0);
        Sessiondata.getInstance().setOutBound_data(0.0);
        Sessiondata.getInstance().setEPO_Status(false);
        Sessiondata.getInstance().setEqup_image(0);
        Sessiondata.getInstance().setAddEquipmentId("");
        Sessiondata.getInstance().setInventory_equip_id(0);
        Sessiondata.getInstance().setBranch_get("");
        Sessiondata.getInstance().setBranch_trim("");
        Sessiondata.getInstance().setCounting_processid("");
        Sessiondata.getInstance().setCounting_endbin("");
        Sessiondata.getInstance().setCount_value("");

        Sessiondata.getInstance().setHw_startbin("");
        Sessiondata.getInstance().setHw_endbin("");
        Sessiondata.getInstance().setHw_value("");
        Sessiondata.getInstance().setPart_value("");

        Sessiondata.getInstance().setHw_partnumber("");
        Sessiondata.getInstance().setHw_processid("");
        Sessiondata.getInstance().setHw_mfr("");
        Sessiondata.getInstance().setHw_notes("");
        Sessiondata.getInstance().setHw_qty("");
        Sessiondata.getInstance().setHw_value_notmatch("");

        Sessiondata.getInstance().setScanner_partreceipt(0);
        Sessiondata.getInstance().setScanner_partreceiving(0);
        Sessiondata.getInstance().setScanner_replace(0);
        Sessiondata.getInstance().setScanner_counting1(0);
        Sessiondata.getInstance().setScanner_counting2(0);
        Sessiondata.getInstance().setScanner_inventory(0);

        Sessiondata.getInstance().setScanner_partnumber(0);
        Sessiondata.getInstance().setScanner_hwstartbin(0);
        Sessiondata.getInstance().setScanner_hwendbin(0);

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


        Sessiondata.getInstance().setProcessid_match("");
        Sessiondata.getInstance().setPart_match("");
        Sessiondata.getInstance().setBranch_mfg("");

        Sessiondata.getInstance().setLoad_branch("");
        Sessiondata.getInstance().setLoad_processid(0);
        Sessiondata.getInstance().setLoad_startbin("");
        Sessiondata.getInstance().setLoad_endbin("");
        Sessiondata.getInstance().setLoad_partNum("");
        Sessiondata.getInstance().setLoaded_partsnotcounted(true);
        Sessiondata.getInstance().setCounting_partnum("");
        Sessiondata.getInstance().setLoad_value_onresume("");

        Sessiondata.getInstance().setCounting_partnew("");
        Sessiondata.getInstance().setCounting_branchname("");
        Sessiondata.getInstance().setEqu_branch_name("");
        Sessiondata.getInstance().setScanner_partno(0);

        Sessiondata.getInstance().setCounting_branchno("");

        Sessiondata.getInstance().setInventory_scanner("");
        Sessiondata.getInstance().setChk_preliminary("");

        Sessiondata.getInstance().setEqup_scanner(0);

        Sessiondata.getInstance().setInventory_dialoghandle(0);

        Sessiondata.getInstance().setFlagphy_addpart(0);

        Sessiondata.getInstance().setTemp_unitId("");
        Sessiondata.getInstance().setTemp_make("");
        Sessiondata.getInstance().setTemp_model("");
        Sessiondata.getInstance().setTemp_serial("");

        if (Sessiondata.getInstance().getDealerbranch() != null){
            Sessiondata.getInstance().getDealerbranch().clear();
        }

        if (Sessiondata.getInstance().getGetEPOParts() != null){
            Sessiondata.getInstance().getGetEPOParts().clear();
        }

        if (Sessiondata.getInstance().getHw_generalimages() != null){
            Sessiondata.getInstance().getHw_generalimages().clear();
        }
        if (Sessiondata.getInstance().getHw_attachedFilesData() != null){
            Sessiondata.getInstance().getHw_attachedFilesData().clear();
        }
        if (Sessiondata.getInstance().getHw_imageData() != null){
            Sessiondata.getInstance().getHw_imageData();
        }

        //arraylist
        if (Sessiondata.getInstance().getArray_EPONum() != null){
            Sessiondata.getInstance().getArray_EPONum().clear();
        }

        if (Sessiondata.getInstance().getPartObjects() != null){
            Sessiondata.getInstance().getPartObjects().clear();
        }

        if (Sessiondata.getInstance().getFreightlists() != null){
            Sessiondata.getInstance().getFreightlists().clear();
        }

        if (Sessiondata.getInstance().getPartsquantity() != null){
            Sessiondata.getInstance().getPartsquantity().clear();
        }

        if (Sessiondata.getInstance().getGetEquipmentLists() != null){
            Sessiondata.getInstance().getGetEquipmentLists().clear();
        }

        if (Sessiondata.getInstance().getDealerbranch() != null){
            Sessiondata.getInstance().getDealerbranch().clear();
        }

        if (Sessiondata.getInstance().getFreightlistsnew() != null){
            Sessiondata.getInstance().getFreightlistsnew().clear();
        }

        if (Sessiondata.getInstance().getMultiplePONumbers() != null){
            Sessiondata.getInstance().getMultiplePONumbers().clear();
        }

        if (Sessiondata.getInstance().getProcessid() != null){
            Sessiondata.getInstance().getProcessid().clear();
        }

        if (Sessiondata.getInstance().getAttachment() != null){
            Sessiondata.getInstance().getAttachment().clear();
        }

        if (Sessiondata.getInstance().getGetEquipmentProcessids() != null){
            Sessiondata.getInstance().getGetEquipmentProcessids().clear();
        }

        if(Sessiondata.getInstance().getGetManufacturerses() !=null){
            Sessiondata.getInstance().getGetManufacturerses().clear();
        }

        if(Sessiondata.getInstance().getBinlocation() !=null){
            Sessiondata.getInstance().getBinlocation().clear();
        }

        //object
        if (Sessiondata.getInstance().getBranch() != null){
            Sessiondata.getInstance().setBranch(null);
        }
        if (Sessiondata.getInstance().getLoginObject() != null){
            Sessiondata.getInstance().setLoginObject(null);
        }
        if (Sessiondata.getInstance().getBinlocation() != null){
            Sessiondata.getInstance().setBinlocation(null);
        }
        if (Sessiondata.getInstance().getImageData() != null){
            Sessiondata.getInstance().setImageData(null);
        }
        if (Sessiondata.getInstance().getEPONum() != null){
            Sessiondata.getInstance().setEPONum(null);
        }
        if (Sessiondata.getInstance().getPartsDetails() != null){
            Sessiondata.getInstance().setPartsDetails(null);
        }
        if (Sessiondata.getInstance().getValidateParts() != null){
            Sessiondata.getInstance().setValidateParts(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        img_part_receipt.startAnimation(mReceiptInAnim);
        img_part_phy_cyclecount.startAnimation(anim);
        img_equip_phy_invent.startAnimation(EquipmentAnim);

    }

    @Override
    public void onBackPressed() {

        if (sweetalrt) {
            sweetalrt = false;
            mDialoglogout = new SweetAlertDialog(Dashboard.this, SweetAlertDialog.INFO_TYPE)
                    .setTitleText("Logout?")
                    .setContentText("Are you sure you want to logout?")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)

                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            if(checkInternetConenction()){

                                new AsyncLogoutTask().execute();
                                sDialog.dismiss();
                                sweetalrt = true;
                            }else{
                                Toast.makeText(Dashboard.this, "No Internet Connection", Toast.LENGTH_LONG).show();
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
            mDialoglogout.setCancelable(false);
            mDialoglogout.show();
        }
    }

    private boolean checkInternetConenction() {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class AsyncLogoutTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(Dashboard.this);
        }


        @Override
        protected Void doInBackground(Void... params) {
            try {
                Logout = WebServiceConsumer.getInstance().removeActiveUser(Sessiondata.getInstance().getLoginObject().getUsertoken(),
                        Sessiondata.getInstance().getLoginObject().getUserid());

            } catch (java.net.SocketTimeoutException e) {
                Logout = null;
            } catch (Exception e) {
                Logout = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ProgressBar.dismiss();

            if(Logout != null) {
                ClearSession();
                Intent intent = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }else {
                ClearSession();
                Intent intent = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
            }

        }
    }
}
