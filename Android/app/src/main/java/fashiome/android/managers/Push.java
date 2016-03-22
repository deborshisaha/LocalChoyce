package fashiome.android.managers;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fashiome.android.models.Conversation;
import fashiome.android.models.Message;
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
    public static String PUSH_TYPE_MESSAGE = "PUSH_TYPE_MESSAGE";

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

    public static void userSentMessage(String objectIdString, String conversationIdentifierString) throws JSONException {

        User currentUser = (User) ParseUser.getCurrentUser();
        String message = currentUser.getUsername() + " sent you a message";

        JSONObject dataObject = new JSONObject();
        dataObject.put(PUSH_TYPE, PUSH_TYPE_MESSAGE);
        dataObject.put(Conversation.CONVERSATION_IDENTIFIER, conversationIdentifierString);

        JSONObject parentJSONObject = new JSONObject();
        parentJSONObject.put("alert", message);
        parentJSONObject.put("data", dataObject);

        pushPushPush(objectIdString, parentJSONObject);
        Log.d(TAG, parentJSONObject.toString());
    }

    private static void pushPushPush (final Object object, final JSONObject parentJSONObject) {

        final ParseQuery pushQuery = ParseInstallation.getQuery();

        if (object instanceof User) {
            pushQuery.whereEqualTo("user", (User)object);
            ParsePush push = new ParsePush();
            push.setQuery(pushQuery);
            push.setData(parentJSONObject);
            push.sendInBackground();

        } else if (object instanceof String) {

            ParseQuery userQuery = User.getQuery();
            userQuery.whereEqualTo("objectId", (String)object);
            userQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
            userQuery.findInBackground(new FindCallback() {

                @Override
                public void done(List objects, ParseException e) {}

                @Override
                public void done(Object o, Throwable throwable) {

                    if (o instanceof ArrayList) {

                        ParseUser user = ((ArrayList<ParseUser> )o).get(0);
                        pushQuery.whereEqualTo("user", user);
                        ParsePush push = new ParsePush();
                        push.setQuery(pushQuery);
                        push.setData(parentJSONObject);
                        push.sendInBackground();
                    }
                }
            });
        }
    }


}
