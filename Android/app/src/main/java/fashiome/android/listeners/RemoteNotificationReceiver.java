package fashiome.android.listeners;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.cloudinary.utils.StringUtils;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import fashiome.android.R;
import fashiome.android.activities.ProductDetailsActivity;
import fashiome.android.activities.UserDetailsActivity;
import fashiome.android.activities.UserProfileActivity;
import fashiome.android.managers.Push;
import fashiome.android.models.Product;

/**
 * Created by dsaha on 3/20/16.
 */
public class RemoteNotificationReceiver extends ParsePushBroadcastReceiver {

    private static final String TAG = "RemoteNotificationReceiver";
    private static final int EVENT_ON_PRODUCT_NOTIFICATION_ID = 1;

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return super.getActivity(context, intent);
    }

    @Override
    public void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {

        Log.d(TAG, "onPushOpen");

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            if (json.optJSONObject("data") != null) {
                JSONObject JSONDataObject = json.getJSONObject("data");
                handleOnPushOpenNotification(JSONDataObject, context, intent);
            }

        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }

    private void handleOnPushOpenNotification (JSONObject jsonObject, Context context, Intent intent) throws JSONException {

        String productID = null;
        String chatWindowID = null;

        if (jsonObject.optString(Product.PRODUCT_ID) != null) {
            productID = jsonObject.getString(Product.PRODUCT_ID);

            if (jsonObject.getString(Push.PUSH_TYPE).equals(Push.PUSH_TYPE_LIKE)) {
                Intent productDetailsLauncherIntent = new Intent(context, ProductDetailsActivity.class);
                productDetailsLauncherIntent.putExtra(Product.PRODUCT_ID, productID);
                productDetailsLauncherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(productDetailsLauncherIntent);
            } else if (jsonObject.getString(Push.PUSH_TYPE).equals(Push.PUSH_TYPE_RENT)) {
                Intent userDetailsLauncherIntent = new Intent(context, UserDetailsActivity.class);
                userDetailsLauncherIntent.putExtra(Product.PRODUCT_ID, productID);
                userDetailsLauncherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(userDetailsLauncherIntent);
            }
            
        } else if (jsonObject.optString("CHAT") != null) {
            chatWindowID = jsonObject.getString("CHAT");
        }

        if (!StringUtils.isEmpty(productID)) {
            // Look for something else
        }
    }
}
