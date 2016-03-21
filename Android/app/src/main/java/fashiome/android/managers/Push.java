package fashiome.android.managers;

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

    public static void userLikedProduct(Product product) throws JSONException {

        ParseQuery pushQuery = ParseInstallation.getQuery();

        pushQuery.whereEqualTo("user", product.getProductPostedBy());

        User currentUser = (User) ParseUser.getCurrentUser();

        String message = currentUser.getUsername() + " liked "+ product.getProductName();

        JSONObject data = new JSONObject("{\"alert\":\""+message+"\", \"data\":{\""+Product.PRODUCT_ID+"\":\""+ product.getObjectId() +"\"}}");

        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setData(data);
        push.sendInBackground();
    }
}
