package fashiome.android.managers;

import android.util.Log;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import fashiome.android.models.Product;
import fashiome.android.models.User;

/**
 * Created by dsaha on 3/20/16.
 */
public class Push {

    private static String TAG = "Push";

    public static String PUSH_TYPE = "PUSH_TYPE";

    public static String PUSH_TYPE_LIKE = "PUSH_TYPE_LIKE";
    public static String PUSH_TYPE_RENT = "PUSH_TYPE_RENT";

    public static void userLikedProduct(Product product) throws JSONException {

        User currentUser = (User) ParseUser.getCurrentUser();
        String message = currentUser.getUsername() + " liked "+ product.getProductName();

        JSONObject dataObject = new JSONObject();
        dataObject.put(PUSH_TYPE, PUSH_TYPE_LIKE);
        dataObject.put(Product.PRODUCT_ID, product.getObjectId());

        JSONObject parentJSONObject = new JSONObject();
        parentJSONObject.put("alert", message);
        parentJSONObject.put("data", dataObject);

        pushPushPush(product.getProductPostedBy(), parentJSONObject);
        Log.d(TAG, parentJSONObject.toString());
    }

    public static void userRentedProduct(Product product) throws JSONException {

        User currentUser = (User) ParseUser.getCurrentUser();
        String message = currentUser.getUsername() + " rented "+ product.getProductName();
        JSONObject dataObject = new JSONObject();
        dataObject.put(PUSH_TYPE, PUSH_TYPE_RENT);
        dataObject.put(Product.PRODUCT_ID, product.getObjectId());

        JSONObject parentJSONObject = new JSONObject();
        parentJSONObject.put("alert", message);
        parentJSONObject.put("data", dataObject);

        pushPushPush(product.getProductPostedBy(), parentJSONObject);
        Log.d(TAG, parentJSONObject.toString());


    }

    private static void pushPushPush (User toUser, JSONObject parentJSONObject) {

        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("user", toUser);

        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setData(parentJSONObject);
        push.sendInBackground();
    }
}