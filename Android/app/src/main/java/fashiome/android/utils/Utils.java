package fashiome.android.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fashiome.android.R;

/**
 * Created by dsaha on 3/5/16.
 */
public class Utils {
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static final String PARSE_DATE_FORMAT = "EEE MMM DDD HH:mm:ss z yyyy";

    public static int getScreenWidthInDp(Context c) {

        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int)dpWidth;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int screenWidthInPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return dpToPx((int)dpWidth);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        //SimpleDateFormat sf = new SimpleDateFormat(TWITTER_DATE_FORMAT, Locale.ENGLISH);
        //sf.setLenient(true);

        String relativeDate = "";

        long dateMillis = convertToDate(rawJsonDate).getTime();

        Log.i("info", "Millis createdAt :" + String.valueOf(dateMillis));

        Log.i("info", "Millis now :"+String.valueOf(System.currentTimeMillis()));

        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }

    public static Date convertToDate(String date)  {

        Date convertedDate = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(
                PARSE_DATE_FORMAT, Locale.US);
        sf.setLenient(true);
        try {
            convertedDate = sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static void setRating (LinearLayout starLinearLayout, int rating, Context context) {
        int i;
        for(i = 0;i<rating;i++){

            ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.ic_star_filled);
            android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(48,48);
            iv.setLayoutParams(layoutParams);
            starLinearLayout.addView(iv);
        }

        if(i < 5){
            for( ;i<5;i++){
                ImageView iv = new ImageView(context);
                iv.setImageResource(R.drawable.ic_star_empty);
                android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(46,46);
                iv.setLayoutParams(layoutParams);
                starLinearLayout.addView(iv);
            }

        }
    }
}
