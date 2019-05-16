package com.ebs.ecount.uidesigns;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ebs.ecount.R;
import com.ebs.ecount.parts_physical_counting.Physical_counting_activity;
import com.ebs.ecount.parts_physical_counting.activity_handwrites_notmatch;
import com.ebs.ecount.parts_physical_inventory.GlobalVariables;
import com.ebs.ecount.parts_physical_inventory.equipment_inventory_activity;
import com.ebs.ecount.parts_receipt.PartReceiptActivity;
import com.ebs.ecount.parts_receipt.PartsReceivingDetailsActivity;
import com.ebs.ecount.parts_receipt.ReplacePartsActivity;
import com.ebs.ecount.utils.Sessiondata;

import java.util.Arrays;
import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScannerActivity extends BaseScannerActivity implements ZBarScannerView.ResultHandler,ZBarConstants {
    private ZBarScannerView mScannerView;
    ImageView cancel;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        setupToolbar();

        if(!isCameraAvailable()) {
            // Cancel request if there is no rear-facing camera.
             cancelRequest();
             return;
        }

        Log.d("GlobalVariables ", "0_UnCounted"+" 1_Counted");
        Log.d("GlobalVariables ",""+ GlobalVariables.showerrormsg+" checkbtn_load "+GlobalVariables.checkbtn_load);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.camera_preview);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);

        cancel = (ImageView)findViewById(R.id.cancel);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        cancel.startAnimation(animation2);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sessiondata.getInstance().getScanner_counting1() == 4){
                    Sessiondata.getInstance().setFlagphy_addpart(1);

                    Intent intent = new Intent(SimpleScannerActivity.this,Physical_counting_activity.class);
                    intent.putExtra("value1", Sessiondata.getInstance().getCounting_startbin());
                    intent.putExtra("LoadList","Onresume");
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_counting2() == 5){
                    Sessiondata.getInstance().setFlagphy_addpart(1);

                    Intent intent = new Intent(SimpleScannerActivity.this,Physical_counting_activity.class);
                    intent.putExtra("value2", Sessiondata.getInstance().getCounting_endbin());
                    intent.putExtra("LoadList","Onresume");
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_partnumber() == 7){

                    Intent intent = new Intent(SimpleScannerActivity.this,Physical_counting_activity.class);
                    intent.putExtra("value3", Sessiondata.getInstance().getCounting_partnum());
                    intent.putExtra("LoadList","Onresume");
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_partno() == 1){
                    Sessiondata.getInstance().setFlagphy_addpart(1);

                    Intent intent = new Intent(SimpleScannerActivity.this,Physical_counting_activity.class);
                    intent.putExtra("value4", Sessiondata.getInstance().getCounting_partnew());
                    intent.putExtra("LoadList","Onresume");
                    startActivity(intent);
                    finish();
                }
                else if (Sessiondata.getInstance().getScanner_inventory() == 6){
                    Sessiondata.getInstance().setInventory_dialoghandle(1);
                    Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                    startActivity(intent);
                }else if (Sessiondata.getInstance().getScanner_inventory() == 7){
                    Sessiondata.getInstance().setInventory_dialoghandle(1);
                    Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                    startActivity(intent);
                }else if (Sessiondata.getInstance().getScanner_inventory() == 8){
                    Sessiondata.getInstance().setInventory_dialoghandle(1);
                    Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                    startActivity(intent);
                }else if (Sessiondata.getInstance().getScanner_inventory() == 9){
                    Sessiondata.getInstance().setInventory_dialoghandle(1);
                    Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                    startActivity(intent);
                }
                else {
                    finish();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    public void cancelRequest() {
        Intent dataIntent = new Intent();dataIntent.putExtra(ERROR_INFO, "Camera unavailable");
        setResult(Activity.RESULT_CANCELED, dataIntent);
        finish();
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void handleResult(final Result rawResult) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Sessiondata.getInstance().getScanner_partreceipt() == 1){
                    Intent intent = new Intent(SimpleScannerActivity.this,PartReceiptActivity.class);
                    intent.putExtra("value", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_partreceiving() == 2){
                    Intent intent = new Intent(SimpleScannerActivity.this,PartsReceivingDetailsActivity.class);
                    intent.putExtra("value", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_replace() == 3){
                    Intent intent = new Intent(SimpleScannerActivity.this,ReplacePartsActivity.class);
                    intent.putExtra("value", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_counting1() == 4){
                    Intent intent = new Intent(SimpleScannerActivity.this,Physical_counting_activity.class);
                    intent.putExtra("value1", rawResult.getContents());
                    intent.putExtra("LoadList","Onresume");
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_counting2() == 5){
                    Intent intent = new Intent(SimpleScannerActivity.this,Physical_counting_activity.class);
                    intent.putExtra("value2", rawResult.getContents());
                    intent.putExtra("LoadList","Onresume");
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_inventory() == 6){
                    List<String> result = Arrays.asList(rawResult.getContents().split(","));
                    if (result.size()==1){
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        intent.putExtra("value1", rawResult.getContents());
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        Log.d("Value_Postion",""+Sessiondata.getInstance().getEqup_scanner());
                        intent.putExtra("value1", result.get(Sessiondata.getInstance().getEqup_scanner()));
                        startActivity(intent);
                        finish();
                    }

                }else if (Sessiondata.getInstance().getScanner_inventory() == 7){
                    List<String> result = Arrays.asList(rawResult.getContents().split(","));
                    if (result.size()==1){
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        intent.putExtra("value2", rawResult.getContents());
                        startActivity(intent);
                        finish();
                    }else if (result.size()>3){
                        Log.d("Value_Postion",""+Sessiondata.getInstance().getEqup_scanner());
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        intent.putExtra("value2", result.get(Sessiondata.getInstance().getEqup_scanner()));
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        startActivity(intent);
                    }
                }
                else if (Sessiondata.getInstance().getScanner_inventory() == 8){
                    List<String> result = Arrays.asList(rawResult.getContents().split(","));
                    if (result.size()==1){
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        intent.putExtra("value3", rawResult.getContents());
                        startActivity(intent);
                        finish();
                    }else if (result.size()>1){
                        Log.d("Value_Postion",""+Sessiondata.getInstance().getEqup_scanner());
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        intent.putExtra("value3", result.get(Sessiondata.getInstance().getEqup_scanner()));
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        startActivity(intent);
                    }
                }else if (Sessiondata.getInstance().getScanner_inventory() == 9){

                    List<String> result = Arrays.asList(rawResult.getContents().split(","));
                    if (result.size()==1){
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        intent.putExtra("value4", rawResult.getContents());
                        startActivity(intent);
                        finish();
                    }else if (result.size()>2){
                        Log.d("Value_Postion",""+Sessiondata.getInstance().getEqup_scanner());
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        String make_model = result.get(1) + "-" + result.get(2);
                        intent.putExtra("value4", make_model);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(SimpleScannerActivity.this,equipment_inventory_activity.class);
                        startActivity(intent);
                    }
                }

                else if (Sessiondata.getInstance().getScanner_partnumber() == 7){
                    Intent intent = new Intent(SimpleScannerActivity.this,Physical_counting_activity.class);
                    intent.putExtra("value3", rawResult.getContents());
                    intent.putExtra("LoadList","Onresume");
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_partno() == 1){
                    Intent intent = new Intent(SimpleScannerActivity.this,Physical_counting_activity.class);
                    intent.putExtra("value4", rawResult.getContents());
                    intent.putExtra("LoadList","Onresume");
                    startActivity(intent);
                    finish();
                }
                else if (Sessiondata.getInstance().getScanner_hwstartbin() == 8){
                    Intent intent = new Intent(SimpleScannerActivity.this,activity_handwrites_notmatch.class);
                    intent.putExtra("value4", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }else if (Sessiondata.getInstance().getScanner_hwendbin() == 9){
                    Intent intent = new Intent(SimpleScannerActivity.this,activity_handwrites_notmatch.class);
                    intent.putExtra("value5", rawResult.getContents());
                    startActivity(intent);
                    finish();
                }

            }
        }, 500);
    }
}
