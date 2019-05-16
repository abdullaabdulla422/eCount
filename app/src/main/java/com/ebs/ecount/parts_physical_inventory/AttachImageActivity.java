package com.ebs.ecount.parts_physical_inventory;

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
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebs.ecount.R;
import com.ebs.ecount.initial.LoginActivity;
import com.ebs.ecount.objects.GetAttachments;
import com.ebs.ecount.objects.LoginObject;
import com.ebs.ecount.uidesigns.ProgressBar;
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
import java.util.Locale;

/**
 * Created by cbe-teclwsp-008 on 28/11/16.
 */
public class AttachImageActivity extends Activity{

    Gallery gallery;
    Button back;
    ArrayList<byte[]> attachedFilesData;
    EditText unit_id,model_value,make_value,serial_value;
    TextView txt_header;
    Bundle extras;
    ImageAdapter adapter;
    ArrayList<Integer> removableList = new ArrayList<Integer>();
    ArrayList<String> attachedImage = new ArrayList<>();

    Boolean sweetalrtsuccess=true;

    private boolean FAB_Status = false;

    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;
    Animation show_fab_3;
    Animation hide_fab_3;

    private Uri fileUri;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int PICK_FROM_FILE = 2;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    public static final String IMAGE_DIRECTORY_NAME = "eCount/Camera";


    public double latitude, longitude;

    ExifInterface exif;
    File mediaFile;

    View fab;
    SubActionButton button1,button2,button3;
    FloatingActionMenu rightLowerMenu;
    ImageView rlIcon1,rlIcon2,rlIcon3;
    Typeface header_face,txt_face;
    EditText notes;

    int Session = 0;
    LoginObject loginObject = null;
    Dialog mDialogSession;
    Dialog mDialognodata;
    ArrayList<GetAttachments> attachments = null;
    String user_token;
    String user;
    String branch;
    int processids;
    String equipId;
    String mfg;
    String serialNo;
    String model;
    String note_text;
    String add_equipment;
    String validateEquipnumber;
    String set_equipment_images;
    int detailId;
    Long offset= Long.valueOf(0);
    Boolean sweetalrt = true;
    SweetAlertDialog sweetalt;

    int inventory_btn_value=0;

    String temp_unitid,temp_make,temp_serial,temp_model;

    Boolean Sweetalrt_list=true;
    SweetAlertDialog Sweetalt_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        header_face= Typeface.createFromAsset(getAssets(), "fonts/helr65w.ttf");
        txt_face = Typeface.createFromAsset(getAssets(), "fonts/helr45w.ttf");

        txt_header = (TextView) findViewById(R.id.txt_header);

        unit_id = (EditText) findViewById(R.id.unit_id);
        model_value = (EditText) findViewById(R.id.model_value);
        make_value = (EditText) findViewById(R.id.make_value);
        serial_value = (EditText) findViewById(R.id.serial_value);

        TextView txt_unitid=(TextView)findViewById(R.id.txt_unitid);
        TextView txt_make=(TextView)findViewById(R.id.txt_make);
        TextView txt_model=(TextView)findViewById(R.id.txt_model);
        TextView txt_serial=(TextView)findViewById(R.id.txt_serial);
        TextView txt_notes=(TextView)findViewById(R.id.txt_notes);
        TextView txt_attachment=(TextView)findViewById(R.id.txt_attachment);
        Button submit = (Button)findViewById(R.id.btn_submit);
        notes = (EditText) findViewById(R.id.notes);
        txt_unitid.setTypeface(header_face);
        txt_make.setTypeface(header_face);
        txt_model.setTypeface(header_face);
        txt_serial.setTypeface(header_face);
        txt_notes.setTypeface(header_face);
        submit.setTypeface(header_face);
        txt_attachment.setTypeface(header_face);
        txt_header.setTypeface(header_face);
        unit_id.setTypeface(txt_face);
        model_value.setTypeface(txt_face);
        make_value.setTypeface(txt_face);
        serial_value.setTypeface(txt_face);
        notes.setTypeface(txt_face);

        unit_id.setText(Sessiondata.getInstance().getUnitId());
        serial_value.setText(Sessiondata.getInstance().getSerial());
        make_value.setText(Sessiondata.getInstance().getMake());
        model_value.setText(Sessiondata.getInstance().getModel());




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






                String unit_ids = unit_id.getText().toString();

                String notes_values = notes.getText().toString();

                if (unit_ids.isEmpty()){
                    if(Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list=  new SweetAlertDialog(AttachImageActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the unit id")
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
                } else if (notes_values.isEmpty()){
                    if(Sweetalrt_list) {
                        Sweetalrt_list = false;

                        Sweetalt_list=  new SweetAlertDialog(AttachImageActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Please enter the notes")
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
                else {
                    if(checkInternetConenction()){

                        if(inventory_btn_value==0) {


                            if(checkInternetConenction()){

                                new AsyncValidateHandwriteEquipnumber().execute();

                            }else{
                                Toast.makeText(AttachImageActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }

                    }else{

                        Toast.makeText(AttachImageActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }



            }
        });

        unit_id.addTextChangedListener(new TextWatcher() {
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
                    unit_id.setText(ss);
                }
                unit_id.setSelection(unit_id.getText().length());
            }
        });

        model_value.addTextChangedListener(new TextWatcher() {
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
                    model_value.setText(ss);
                }
                model_value.setSelection(model_value.getText().length());
            }
        });

        make_value.addTextChangedListener(new TextWatcher() {
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
                    make_value.setText(ss);
                }
                make_value.setSelection(make_value.getText().length());
            }
        });

        serial_value.addTextChangedListener(new TextWatcher() {
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
                    serial_value.setText(ss);
                }
                serial_value.setSelection(serial_value.getText().length());
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

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FAB_Status = false;
                rightLowerMenu.close(true);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
                rlIcon1.setAnimation(hide_fab_1);
                rlIcon2.setAnimation(hide_fab_2);

                Sessiondata.getInstance().setImageData(null);
                fileUri = null;

                Sessiondata.getInstance().setTemp_unitId(unit_id.getText().toString());
                Sessiondata.getInstance().setTemp_make(make_value.getText().toString());
                Sessiondata.getInstance().setTemp_model(model_value.getText().toString());
                Sessiondata.getInstance().setTemp_serial(serial_value.getText().toString());

                showFileChooser();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FAB_Status = false;
                rightLowerMenu.close(true);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
                rlIcon1.setAnimation(hide_fab_1);
                rlIcon2.setAnimation(hide_fab_2);

                Sessiondata.getInstance().setTemp_unitId(unit_id.getText().toString());
                Sessiondata.getInstance().setTemp_make(make_value.getText().toString());
                Sessiondata.getInstance().setTemp_model(model_value.getText().toString());
                Sessiondata.getInstance().setTemp_serial(serial_value.getText().toString());

                captureImage();
            }
        });


        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            Sessiondata.getInstance().setImageData(null);
        }

        back = (Button) findViewById(R.id.back);
        back.setTypeface(txt_face);

        gallery = (Gallery) findViewById(R.id.Image_gallery);

        attachedFilesData = new ArrayList<>();

        attachedFilesData = Sessiondata.getInstance().getWalkaroundgeneralimages();

        Sessiondata.getInstance().setImageData(null);
        attachedFilesData = new ArrayList<byte[]>();
        attachedFilesData.addAll(Sessiondata.getInstance().getAttachedFilesData());

        adapter = new ImageAdapter(AttachImageActivity.this);
        adapter.notifyDataSetChanged();
        gallery.setAdapter(adapter);

        extras = getIntent().getExtras();

        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FAB_Status) {
                    ClearSession();
                    Intent myintent= new Intent(AttachImageActivity.this,equipment_inventory_activity.class);
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
                    Intent myintent= new Intent(AttachImageActivity.this,equipment_inventory_activity.class);
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

        if(Sessiondata.getInstance().getEqup_image()==1){
            adapter = new ImageAdapter(AttachImageActivity.this);
            adapter.notifyDataSetChanged();
            gallery.setAdapter(adapter);
        }
            }

    public void ClearSession(){

        Sessiondata.getInstance().setInventory_equip_id(1);

        Sessiondata.getInstance().setInventory_dialoghandle(1);

        Sessiondata.getInstance().setTemp_unitId("");
        Sessiondata.getInstance().setTemp_make("");
        Sessiondata.getInstance().setTemp_model("");
        Sessiondata.getInstance().setTemp_serial("");

    }

    public void ClearSession_aftersubmit(){

        Sessiondata.getInstance().setInventory_equip_id(1);

        Sessiondata.getInstance().setInventory_dialoghandle(1);

        Sessiondata.getInstance().setTemp_unitId("");
        Sessiondata.getInstance().setTemp_make("");
        Sessiondata.getInstance().setTemp_model("");
        Sessiondata.getInstance().setTemp_serial("");

        Sessiondata.getInstance().setUnitId("");
        Sessiondata.getInstance().setMake("");
        Sessiondata.getInstance().setModel("");
        Sessiondata.getInstance().setSerial("");
        Sessiondata.getInstance().setMake_model("");

    }

    private void addImageToSessionData() {
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // already 8 was here
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


            Sessiondata.getInstance().setImageData(setExifMetaData(stream));

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
            Sessiondata.getInstance().setImageData(bytearrays);

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
                Orientation.LEFT_RIGHT);
        startActivityForResult(inten, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }


    @Override
    protected void onResume() {
        byte[] byteArray;
        if ((byteArray = Sessiondata.getInstance().getImageData()) != null) {
            attachedFilesData.add(byteArray);
            Sessiondata.getInstance().setImageData(null);
        }


        {
            adapter = new ImageAdapter(AttachImageActivity.this);
            adapter.notifyDataSetChanged();
            gallery.setAdapter(adapter);
        }
        super.onResume();
        sweetalrt=true;

        String temp_Ui,temp_Mk,temp_Mod,temp_Sn;

        temp_Ui = Sessiondata.getInstance().getTemp_unitId();
        temp_Mk = Sessiondata.getInstance().getTemp_make();
        temp_Mod = Sessiondata.getInstance().getTemp_model();
        temp_Sn = Sessiondata.getInstance().getTemp_serial();

        if (Sessiondata.getInstance().getUnitId().toString().equalsIgnoreCase(temp_Ui)){
            unit_id.setText(Sessiondata.getInstance().getUnitId());
        }else {
            unit_id.setText(temp_Ui);
        }

        if (Sessiondata.getInstance().getMake().toString().equalsIgnoreCase(temp_Mk)){
            make_value.setText(Sessiondata.getInstance().getMake());
        }else {
            make_value.setText(temp_Mk);
        }

        if (Sessiondata.getInstance().getSerial().toString().equalsIgnoreCase(temp_Sn)){
            serial_value.setText(Sessiondata.getInstance().getSerial());
        }else {
            serial_value.setText(temp_Sn);
        }

        if (Sessiondata.getInstance().getModel().toString().equalsIgnoreCase(temp_Mod)){
            model_value.setText(Sessiondata.getInstance().getModel());
        }else {
            model_value.setText(temp_Mod);
        }
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
                    Toast.makeText(AttachImageActivity.this,
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
                    ImageAdapter adapter = new ImageAdapter(AttachImageActivity.this);
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
            Intent myintent= new Intent(AttachImageActivity.this,equipment_inventory_activity.class);
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

    public class AsyncAddEquipment extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute(){
            ProgressBar.showCommonProgressDialog(AttachImageActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                user_token= Sessiondata.getInstance().getLoginObject().getUsertoken();
                user=Sessiondata.getInstance().getLoginObject().getUsername();
                branch=Sessiondata.getInstance().getLoginObject().getBranch();
                processids= Sessiondata.getInstance().getProcessId();

                temp_unitid = unit_id.getText().toString();
                temp_make = make_value.getText().toString();
                temp_model = model_value.getText().toString();
                temp_serial = serial_value.getText().toString();


                if (Sessiondata.getInstance().getUnitId().toString().equalsIgnoreCase(temp_unitid)){
                    equipId = Sessiondata.getInstance().getUnitId();
                }else {
                    equipId = temp_unitid;
                }

                if (Sessiondata.getInstance().getMake().toString().equalsIgnoreCase(temp_make)){
                    mfg = Sessiondata.getInstance().getMake();
                }else {
                    mfg = temp_make;
                }

                if (Sessiondata.getInstance().getSerial().toString().equalsIgnoreCase(temp_serial)){
                    serialNo = Sessiondata.getInstance().getSerial();
                }else {
                    serialNo = temp_serial;
                }

                if (Sessiondata.getInstance().getModel().toString().equalsIgnoreCase(temp_model)){
                    model = Sessiondata.getInstance().getModel();
                }else {
                    model = temp_model;
                }


                note_text=notes.getText().toString();


                Log.d("branch_usertoken",""+user_token);
                Log.d("AddEquipment_equipId",""+equipId);
                Log.d("AddEquipment_mfg",""+mfg);
                Log.d("AddEquipment_serialNo",""+serialNo);
                Log.d("AddEquipment_model",""+model);
                Log.d("AddEquipment_note",""+note_text);

                add_equipment = WebServiceConsumer.getInstance().AddEquipment(user_token,processids,equipId,mfg,model,serialNo,branch,note_text,user);

            }catch (SocketTimeoutException e){
                add_equipment = null;
            }
            catch (Exception e) {
                add_equipment = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            ProgressBar.dismiss();

            inventory_btn_value=0;

            Sessiondata.getInstance().setAddEquipmentId(add_equipment);

            if(add_equipment != null) {

                if (add_equipment.toString().contains("Session")) {

                    String Result = add_equipment;
                    String replace = Result.replace("Error - ", "");

                    Session = 0;

                    if(checkInternetConenction()){

                        new AsyncSessionLoginTask().execute();

                    }else{
                        Toast.makeText(AttachImageActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else if (add_equipment.toString().contains("Invalid length parameter passed to the LEFT or SUBSTRING function.")){
                    if(sweetalrtsuccess) {
                        sweetalrtsuccess = false;

                        sweetalt=  new SweetAlertDialog(AttachImageActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Equipment is not Added!")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        sweetalrtsuccess=true;

                                        Sessiondata.getInstance().setInventory_dialoghandle(1);

                                        Intent intent=new Intent(AttachImageActivity.this,equipment_inventory_activity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);


                                    }
                                });
                        sweetalt.setCancelable(false);
                        sweetalt.show();
                    }
                } else {
                    if(add_equipment.toString().contains("Violation")){
                        if(sweetalrt) {
                            sweetalrt = false;


                            sweetalt=new SweetAlertDialog(AttachImageActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Message!")
                                    .setContentText("This Equipment Already Belongs to Process id " + processids)
                                    .setCancelText("Ok")
                                    .showCancelButton(false)
                                    .setConfirmClickListener(null)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override

                                        public void onClick(SweetAlertDialog sDialog) {
                                            sweetalrt=true;
                                            sDialog.dismiss();

                                            Sessiondata.getInstance().setInventory_dialoghandle(1);

                                            ClearSession_aftersubmit();

                                            Intent intent = new Intent(AttachImageActivity.this, equipment_inventory_activity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_bottom_to_top_enter, R.anim.slide_bottom_to_top_leave);

                                        }
                                    });
                            sweetalt.setCancelable(false);
                            sweetalt.show();
                        }

                    }else {
                        if (checkInternetConenction()) {

                            new AsyncSetEquipmentImages().execute();

                        } else {

                            Toast.makeText(AttachImageActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } else{


            }
        }
    }

    public class AsyncSetEquipmentImages extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute(){
            ProgressBar.showCommonProgressDialog(AttachImageActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                user_token= Sessiondata.getInstance().getLoginObject().getUsertoken();
                detailId= Integer.parseInt(Sessiondata.getInstance().getAddEquipmentId());

                Sessiondata.getInstance().setAttachedFilesData((ArrayList<byte[]>) attachedFilesData.clone());
                Log.d("branch_usertoken",""+user_token);

                temp_unitid = unit_id.getText().toString();

                if (Sessiondata.getInstance().getUnitId().toString().equalsIgnoreCase(temp_unitid)){
                    equipId = Sessiondata.getInstance().getUnitId();
                }else {
                    equipId = temp_unitid;
                }

                Log.d("SetEquipImages_procids",""+processids);
                Log.d("SetEquipImages_equipId",""+equipId);

                for (int i = 0; i < Sessiondata.getInstance().getAttachedFilesData().size(); i ++){

                    attachedImage.add(Base64.encodeToString(Sessiondata.getInstance().getAttachedFilesData().get(i), Base64.DEFAULT));
                    Log.d("Base64",""+attachedImage.get(i).toString());
                    String content=attachedImage.get(i).toString();

                    set_equipment_images = WebServiceConsumer.getInstance().SetEquipmentImagesV2(user_token,processids,detailId,equipId,"",content,offset);


                }


            }catch (SocketTimeoutException e){
                set_equipment_images = null;
            }
            catch (Exception e) {
                set_equipment_images = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);

            ProgressBar.dismiss();

            if(set_equipment_images != null) {

                if (set_equipment_images.toString().contains("Session")) {

                    String Result = set_equipment_images;
                    String replace = Result.replace("Error - ", "");
                    Session = 1;

                    if(checkInternetConenction()){

                        new AsyncSessionLoginTask().execute();

                    }else{
                        Toast.makeText(AttachImageActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Log.d("set_equipment_images",""+set_equipment_images.toString());

                    if(sweetalrtsuccess) {
                        sweetalrtsuccess = false;

                        sweetalt=  new SweetAlertDialog(AttachImageActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success!")
                                .setContentText("Equipment Added Successfully!")
                                .setCancelText("Ok")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override

                                    public void onClick(SweetAlertDialog sDialog) {
                                        sweetalrtsuccess=true;

                                        Sessiondata.getInstance().setInventory_dialoghandle(1);

                                        ClearSession_aftersubmit();

                                        Intent intent=new Intent(AttachImageActivity.this,equipment_inventory_activity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_left_to_right_enter, R.anim.slide_left_to_right_leave);


                                    }
                                });
                        sweetalt.setCancelable(false);
                        sweetalt.show();
                    }


                }
            } else{

                if(sweetalrtsuccess) {
                    sweetalrtsuccess = false;

                    sweetalt=  new SweetAlertDialog(AttachImageActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success!")
                            .setContentText("Equipment Added Successfully!")
                            .setCancelText("Ok")
                            .showCancelButton(false)
                            .setCancelClickListener(null)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override

                                public void onClick(SweetAlertDialog sDialog) {
                                    sweetalrtsuccess=true;

                                    Sessiondata.getInstance().setInventory_dialoghandle(1);

                                    ClearSession_aftersubmit();

                                    Intent intent=new Intent(AttachImageActivity.this,equipment_inventory_activity.class);
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

    public class AsyncValidateHandwriteEquipnumber extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            ProgressBar.showCommonProgressDialog(AttachImageActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                user_token= Sessiondata.getInstance().getLoginObject().getUsertoken();

                temp_unitid = unit_id.getText().toString();
                temp_serial = serial_value.getText().toString();


                if (Sessiondata.getInstance().getUnitId().equalsIgnoreCase(temp_unitid)){
                    equipId = Sessiondata.getInstance().getUnitId();
                }else {
                    equipId = temp_unitid;
                }


                if (Sessiondata.getInstance().getSerial().equalsIgnoreCase(temp_serial)){
                    serialNo = Sessiondata.getInstance().getSerial();
                }else {
                    serialNo = temp_serial;
                }

                validateEquipnumber = WebServiceConsumer.getInstance().ValidateHandwriteEquipnumber(user_token, equipId, serialNo);

            } catch (SocketTimeoutException e) {
                validateEquipnumber = null;
            } catch (Exception e) {
                validateEquipnumber = null;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            ProgressBar.dismiss();

            if (validateEquipnumber != null)
            {

                if (validateEquipnumber.contains("Session")) {

                    Session = 2;

                    if(checkInternetConenction()){

                        new AsyncSessionLoginTask().execute();

                    }else{
                        Toast.makeText(AttachImageActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {

                    Log.d("validateEquipnumber", ""+ validateEquipnumber);



                    if (validateEquipnumber.equals("1"))
                    {

                        SweetAlertDialog sweetAlertDialog=  new SweetAlertDialog(AttachImageActivity.this, SweetAlertDialog.WARNINGMSG_TYPE)
                                .setTitleText("Alert!")
                                .setContentText("Equipment is already exist!")
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
                    else if (validateEquipnumber.equals("0"))
                    {

                        inventory_btn_value=1;

                        new AsyncAddEquipment().execute();

                    }
                    else
                    {
                        Log.d("validateEquipnumber", ""+ validateEquipnumber);

                    }

                }




            }


        }
    }



    public class AsyncSessionLoginTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
              ProgressBar.showCommonProgressDialog(AttachImageActivity.this);
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

                    if (mDialogSession == null){
                        mDialogSession = new Dialog(AttachImageActivity.this);
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
                                Intent intent = new Intent(AttachImageActivity.this,LoginActivity.class);
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
                    }
                    else if (!mDialogSession.isShowing()){
                        mDialogSession.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        mDialogSession.show();
                    }


                } else {
                    ProgressBar.dismiss();
                    if (Session == 0){
                        if(checkInternetConenction()){

                            new AsyncAddEquipment().execute();

                        }else{

                            Toast.makeText(AttachImageActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }else if (Session == 1){
                        if(checkInternetConenction()){

                            new AsyncSetEquipmentImages().execute();

                        }else{

                            Toast.makeText(AttachImageActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }
                    else if (Session == 2){
                        if(checkInternetConenction()){

                            new AsyncValidateHandwriteEquipnumber().execute();

                        }else{

                            Toast.makeText(AttachImageActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();

                        }
                    }

                }
            } else {
                ProgressBar.dismiss();

                if (mDialognodata == null){
                    mDialognodata = new Dialog(AttachImageActivity.this);
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
                else if (!mDialognodata.isShowing()){
                    mDialognodata.show();
                }

            }
        }
    }

}