package com.ebs.ecount.initial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ebs.ecount.R;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.uidesigns.OptAnimationLoader;
import com.ebs.ecount.uidesigns.ProgressBar;
import com.ebs.ecount.uidesigns.SimpleGestureFilter;
import com.ebs.ecount.uidesigns.fonts.RobotoTextView;
import com.ebs.ecount.utils.Sessiondata;
import com.ebs.ecount.webutils.WebServiceConsumer;
import com.ebs.ecount.webutils.eCountConstants;

import java.net.SocketTimeoutException;


/**
 * Created by techunity on 21/11/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener, SimpleGestureFilter.SimpleGestureListener {

    EditText Username,Password,serviceLink;
    Button Login;
    ImageView Clear_Username,Clear_Password;
    LoginObject loginObject = null;
    String Permission;
    String str_UserName,str_Password;
    String Result;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    public static SharedPreferences sharedPreferencess;
    public static SharedPreferences.Editor sharedPreferserver;
    Boolean saveLogin;
    ToggleButton remember_me;
    LinearLayout popup;
    Button btnSave,btnCancel;
    public Boolean server;
    Typeface header_face,txt_face;

    RotateAnimation anim;

    RobotoTextView remember_text,username_text,password_text;
    TextView above,version,header;

    private SimpleGestureFilter detector;
    AnimationSet versionAnim;
    TextView text_URL;

    public SharedPreferences appPreferences;
    public SharedPreferences.Editor appeditor;
    boolean isAppInstalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_social);

        text_URL = (TextView) findViewById(R.id.text_URL);

        Username = (EditText) findViewById(R.id.userName);
        Password = (EditText) findViewById(R.id.passWord);

        Clear_Username = (ImageView) findViewById(R.id.clear_username);
        Clear_Password = (ImageView) findViewById(R.id.clear_password);
        remember_me = (ToggleButton) findViewById(R.id.remember_me);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        detector = new SimpleGestureFilter(this,this);
        popup = (LinearLayout) findViewById(R.id.popup_window);

        btnSave = (Button) findViewById(R.id.buttonSave);
        btnCancel = (Button) findViewById(R.id.buttonCancel);
        popup.setVisibility(View.GONE);


        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        appPreferences = getSharedPreferences("App", MODE_PRIVATE);
        appeditor = appPreferences.edit();

        isAppInstalled = appPreferences.getBoolean("isAppInstalled",false);
        if(isAppInstalled==false){
            createShortcut();
        }


        sharedPreferencess = getSharedPreferences("server", MODE_PRIVATE);
        sharedPreferserver = sharedPreferencess.edit();

        serviceLink = (EditText) findViewById(R.id.enter_url);

        server = sharedPreferencess.getBoolean("server", false);
        if (server) {

            serviceLink.setText(sharedPreferencess.getString("serviceURL", ""));

        }
        else {

            serviceLink.setText(eCountConstants.SOAP_BASE_ADDRESS);
            eCountConstants.SOAP_BASE_ADDRESS = serviceLink.getText().toString();
        }

        if (saveLogin) {
            Username.setText(loginPreferences.getString("username",""));
            Password.setText(loginPreferences.getString("password",""));
            remember_me.setChecked(true);
        }

        Login = (Button) findViewById(R.id.login);
        remember_text=(RobotoTextView) findViewById(R.id.remember_text);
        username_text=(RobotoTextView) findViewById(R.id.username_text);
        password_text=(RobotoTextView) findViewById(R.id.password_text);


        anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        //Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.ABSOLUTE); //Repeat animation indefinitely
        anim.setDuration(500); //Put desired duration per anim cycle here, in milliseconds


        versionAnim = (AnimationSet) OptAnimationLoader.loadAnimation(LoginActivity.this, R.anim.popup_in_dialog);


        header_face= Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");
        txt_face = Typeface.createFromAsset(getAssets(), "fonts/helr45w.ttf");
        above=(TextView) findViewById(R.id.above);
        version=(TextView) findViewById(R.id.version);
        header=(TextView) findViewById(R.id.header);

        serviceLink.setTypeface(txt_face);
        text_URL.setTypeface(header_face);

        Username.setTypeface(txt_face);
        Password.setTypeface(txt_face);
        btnSave.setTypeface(header_face);
        btnCancel.setTypeface(header_face);
        Login.setTypeface(header_face);
        remember_text.setTypeface(header_face);
        above.setTypeface(header_face);
        version.setTypeface(header_face);
        header.setTypeface(header_face);
        username_text.setTypeface(txt_face);
        password_text.setTypeface(txt_face);


        above.startAnimation(versionAnim);
        version.startAnimation(versionAnim);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_UserName = Username.getText().toString();
                str_Password = Password.getText().toString();

                eCountConstants.SOAP_BASE_ADDRESS = serviceLink.getText()
                        .toString().trim();
                eCountConstants.resetSOAPAddress();

                sharedPreferserver.putBoolean("server", true);
                sharedPreferserver.putString("serviceURL", eCountConstants.SOAP_BASE_ADDRESS);
                sharedPreferserver.commit();
                if (remember_me.isChecked()){
                    loginPrefsEditor.putBoolean("saveLogin",true);
                    loginPrefsEditor.putString("username",str_UserName);
                    loginPrefsEditor.putString("password",str_Password);
                    loginPrefsEditor.commit();
                }else {
                    loginPrefsEditor.putBoolean("saveLogin",false);
                    loginPrefsEditor.putString("username","");
                    loginPrefsEditor.putString("password","");
                    loginPrefsEditor.commit();
                }

                if(checkInternetConenction()){

                    new AsyncLoginTask().execute();

                }else{

                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(findViewById(R.id.edit_area));
                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                }


            }
        });

        Clear_Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear_Username.startAnimation(anim);
                Username.setText("");
            }
        });

        Clear_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Clear_Password.startAnimation(anim);
                    Password.setText("");
            }
        });

    }

    public void createShortcut(){
        Intent shortcutIntent = new Intent(getApplicationContext(),LoginActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource
                .fromContext(getApplicationContext(), R.mipmap.icon));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(intent);

        //Make preference true
        appeditor.putBoolean("isAppInstalled", true);
        appeditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        above.startAnimation(versionAnim);
        version.startAnimation(versionAnim);
    }


    @Override
    public void onClick(View v) {
        if (v == btnSave) {

            eCountConstants.SOAP_BASE_ADDRESS = serviceLink.getText()
                    .toString().trim();
            eCountConstants.resetSOAPAddress();
            sharedPreferserver.putBoolean("server", true);
            sharedPreferserver.putString("serviceURL", eCountConstants.SOAP_BASE_ADDRESS);
            sharedPreferserver.commit();
            popup.setVisibility(View.GONE);

        }
        if (v == btnCancel) {
            serviceLink.setText(eCountConstants.SOAP_BASE_ADDRESS);
            popup.setVisibility(View.GONE);
        }
    }

    public class AsyncLoginTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(LoginActivity.this);
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                loginObject = WebServiceConsumer.getInstance().authenticateUser(
                        str_UserName,
                        str_Password);

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

                Log.d("Login_username", "" + Sessiondata.getInstance().getLoginObject().getUsername());
                Log.d("New_Usertoken", "" + Sessiondata.getInstance().getLoginObject().getUsertoken());
                Log.d("Login_userid", "" + Sessiondata.getInstance().getLoginObject().getUserid());
                Log.d("Login_branch", "" + Sessiondata.getInstance().getLoginObject().getBranch());

                if (loginObject.getUserid() == 0) {
                    ProgressBar.dismiss();
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(findViewById(R.id.edit_area));
                    Result = loginObject.getMessage().toString();
                    String replace = Result.replace("Error - ", "");

                    Log.d("Login Object", "" + Result);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    Toast.makeText(LoginActivity.this, replace, Toast.LENGTH_LONG).show();

                } else {


                    ProgressBar.dismiss();

                    ClearSession();

                    Sessiondata.getInstance().setTemp_username(str_UserName);
                    Sessiondata.getInstance().setTemp_password(str_Password);
                    new AsyncPermission().execute();
                }
            } else {
                ProgressBar.dismiss();
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(findViewById(R.id.edit_area));

                Toast.makeText(LoginActivity.this, "Check Url", Toast.LENGTH_LONG).show();
            }
        }
    }



    public class AsyncPermission extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(LoginActivity.this);
            if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Permission = WebServiceConsumer.getInstance().GetPrintLabelPermission(
                        loginObject.getUsertoken()
                       );

            } catch (SocketTimeoutException e) {
                Permission = null;
            } catch (Exception e) {
                Permission = null;
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            Log.d("Permission", Permission +  "");

            if (Permission != null) {
                ProgressBar.dismiss();
                Sessiondata.getInstance().setPermission(Permission);
                Intent login = new Intent(LoginActivity.this,Dashboard.class);
                startActivity(login);
                overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);



                } else {


                    ProgressBar.dismiss();
                    Sessiondata.getInstance().setPermission("false");
                    Intent login = new Intent(LoginActivity.this,Dashboard.class);
                    startActivity(login);
                    overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);


                }
        }
    }


    public void onSwipe(int direction) {

        switch (direction) {
            case SimpleGestureFilter.SWIPE_DOWN:

                popup.setVisibility(View.VISIBLE);
                break;

            case SimpleGestureFilter.SWIPE_UP:

                popup.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onDoubleTap() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        this.detector.onTouchEvent(event);
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    private boolean checkInternetConenction() {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
        Sessiondata.getInstance().setCounting_startbin("");
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

    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
        }else
        {
            finish();
            overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);
        }
    }
}
