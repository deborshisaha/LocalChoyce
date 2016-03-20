package fashiome.android.utils;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import fashiome.android.models.Address;
import fashiome.android.models.Product;
import fashiome.android.models.User;

public class AppStarter extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        // https://parse.com/docs/android/guide#local-datastore
        // Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Product.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Address.class);

        Parse.initialize(this);

        /**
         * Facebook Utils
         */
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);

        /**
         * Parse installation
         */
        if (ParseUser.getCurrentUser() != null) {
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("user", ParseUser.getCurrentUser());
            installation.saveInBackground();
        } else {
            ParseInstallation.getCurrentInstallation().saveInBackground();
        }


        //auto user not good for our use cases disabling it
        //ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read and write access.
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
    public static Context getAppContext() {
        return sContext;
    }
}