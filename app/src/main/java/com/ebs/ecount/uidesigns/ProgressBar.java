package com.ebs.ecount.uidesigns;

/**
 * Created by techunity on 14/6/16.
 */


import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.ebs.ecount.R;


public class ProgressBar {
    public static Dialog commonProgressDialog;

    public static void showCommonProgressDialog(Activity activity) {
        commonProgressDialog = new Dialog(activity);
        commonProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        commonProgressDialog.setContentView(R.layout.progress_bar);
        commonProgressDialog.getWindow().setBackgroundDrawableResource (R.color.color_transparent);
        commonProgressDialog.setCanceledOnTouchOutside(false);
        commonProgressDialog.setCancelable(false);
        commonProgressDialog.show();
    }

    public static void showCommonProgressDialogfragment(FragmentActivity activity) {
        commonProgressDialog = new Dialog(activity);
        commonProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        commonProgressDialog.setContentView(R.layout.progress_bar);
        commonProgressDialog.getWindow().setBackgroundDrawableResource (R.color.color_transparent);
        commonProgressDialog.setCanceledOnTouchOutside(false);
        commonProgressDialog.setCancelable(false);
        commonProgressDialog.show();
    }

    public static void dismiss() {
        if(commonProgressDialog != null) {
            commonProgressDialog.dismiss();
        }
    }


}
