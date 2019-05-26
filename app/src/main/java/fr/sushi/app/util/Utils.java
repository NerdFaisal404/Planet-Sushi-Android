package fr.sushi.app.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.ligl.android.widget.iosdialog.IOSDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


import fr.sushi.app.BuildConfig;
import fr.sushi.app.R;
import fr.sushi.app.data.local.SharedPref;
import fr.sushi.app.data.local.preference.PrefKey;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utils {

    public static void setStatusColor(Context context) {

    }


    public static String getFormattedTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));


        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public static File convertBitmapToFile(Bitmap imageBitmap, Context context) {
        File file = new File(context.getCacheDir(), String.valueOf(System.currentTimeMillis()));

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array
        Bitmap bitmap = imageBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static boolean isInternet(Context context) {
        boolean available = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] networkInfo = connectivity.getAllNetworkInfo();
            if (networkInfo != null) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        available = true;
                        break;
                    }
                }
            }


        }

        return available;
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            if (extension.contains("m4a")) {
                type = "audio/m4a";
            } else if (extension.contains("vcf")) {
                type = "vcard/vcf";
            } else {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
        }
        return type;
    }

    public static boolean isVideoFile(String url) {
        String filenameArray[] = url.split("\\.");
        String extension = filenameArray[filenameArray.length - 1];
        if (extension.equalsIgnoreCase("MOV") || extension.equalsIgnoreCase("mp4")) {
            return true;
        } else {
            return false;
        }
    }

    public static Bitmap retrieveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    //Date converter
    public static String dateConverter(String dateFromApi) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM h:mm a");
        Date date = null;
        try {
            date = inputFormat.parse(dateFromApi);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        String str = formattedDate.replace("AM", "am").replace("PM", "pm");
        return str; // 13 Oct 10:34 am
    }

    /**
     * @param activity the context of the activity
     * @brief methods for hiding the soft keyboard by forced
     */
    public static boolean hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            return inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);
        } else {
            Log.e("showSoftKeyboard", "Current Focus is null");
            return false;
        }
    }

    /**
     * Show a forward activity transition
     *
     * @param activity
     */
    public static void showForwardTransition(Activity activity) {
        try {
            activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        } catch (Exception e) {
            Log.e("ActivityForwardTransi", e.getMessage());
        }
    }

    /**
     * Show a backward activity transition
     *
     * @param activity
     */
    public static void showBackwardTransition(Activity activity) {
        try {
            activity.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        } catch (Exception e) {
            Log.e("ActivityBackwardTransi", e.getMessage());
        }
    }

    /**
     * @param activity the context of the activity
     * @brief methods for showing the soft keyboard by forced
     */
    public static void showSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(activity.getCurrentFocus(), 0);
        } else {
            Log.e("showSoftKeyboard", "Current Focus is null");
        }
    }

    public static boolean showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        assert imm != null;
        return imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String getCurrentTimeStamp() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String isoToTimestamp(String messageSentTime) {

        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df1.setTimeZone(TimeZone.getTimeZone("UTC+06:00"));

        Date result1 = null;
        try {
            result1 = df1.parse(messageSentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String msgTime = formatter.format(result1);

        if (msgTime != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(msgTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(date);
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            msgTime = day + "-" + month.substring(0, 3) + ", "
                    + timestampToShortTime(msgTime);
        }

        return msgTime;
    }

    public static String getMessageTime() {
        String msgTime = getCurrentTimeStamp();

        if (msgTime != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(msgTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(date);
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            msgTime = day + "-" + month.substring(0, 3) + ", "
                    + timestampToShortTime(msgTime);
        }

        return msgTime;
    }

    private static String timestampToShortTime(String timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String finaltime = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
        return finaltime;

    }

    public static void setShareApp(String subject, Context context) {
        //   Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/mipmap/" + "ic_launcher");

        Uri uri = Uri.parse("smsto:");
        Intent shareIntent = new Intent(Intent.ACTION_SENDTO, uri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.app_name) + " https://play.google.com/store/apps/details?id=" +
                BuildConfig.APPLICATION_ID);
        context.startActivity(shareIntent);


    }

    public static String getDecimalFormat(double value) {

        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(Locale.FRANCE);
        NumberFormat goodNumberFormat = new DecimalFormat("#,##0.00#", dfs);
        /*Locale locale = new Locale("fr", "FR");
        String pattern = "#.##";

        DecimalFormat decimalFormat = (DecimalFormat)
                NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(pattern);

        String format = decimalFormat.format(value);*/
        String format = goodNumberFormat.format(value);
        return format;

    }

    public static Spannable getColoredString(String mString, int colorId) {
        Spannable spannable = new SpannableString(mString + " ");
        spannable.setSpan(new ForegroundColorSpan(colorId), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static void showAlert(Context context, String title, String content) {

        new IOSDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton("D'accord", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static String getDistance(Location mLocation, double toLatitude, double toLongitude) {
        if (mLocation != null) {
            LatLng from = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            LatLng to = new LatLng(toLatitude, toLongitude);

            //Calculating the distance in meters
            Double distance = SphericalUtil.computeDistanceBetween(from, to);

            //Displaying the distance
            // Toast.makeText(this,String.valueOf(distance+" Meters"),Toast.LENGTH_SHORT).show();
            return formatDistance(distance);
        }

        return null;

    }

    public static String formatDistance(double distance) {
        String unit = "m";
        if (distance < 1) {
            distance *= 1000;
            unit = "mm";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = "km";
        }
        DecimalFormat df = new DecimalFormat("#");
        String formatted = df.format(distance);
        return formatted + unit;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void setOneTimeLaunched(boolean oneTimeLaunched) {
        SharedPref.write(PrefKey.ONE_TIME_LAUNCHED, oneTimeLaunched);
    }

    public static boolean isOneTimeLaunched() {
        return SharedPref.readBoolean(PrefKey.ONE_TIME_LAUNCHED, false);
    }

    public static String getFormatedPhoneNumber(String phoneNumber, Context context) {

        Phonenumber.PhoneNumber pn = null;
        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.createInstance(context);
            pn = pnu.parse(phoneNumber, "FR");
            String pnE164 = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.E164);

            return pnE164;
        } catch (NumberParseException e) {
            e.printStackTrace();
        }


        return "";


    }

    public static String getTodayDay(){
       /* SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
        return fmtOut.format(date);*/
        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

        System.out.println("DAY "+simpleDateFormat.format(date).toUpperCase());

        return simpleDateFormat.format(date).toLowerCase();
    }

    public static String getDay(){



        return "";
    }
}
