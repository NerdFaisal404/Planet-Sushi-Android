package fr.sushi.app.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import fr.sushi.app.R;


public class DialogUtils {

    private static Dialog dialog;

    public static void showDialog(Context context) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.dialog_custom_loading);


        //...finaly show it
        dialog.show();
    }

    public static void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }
}
