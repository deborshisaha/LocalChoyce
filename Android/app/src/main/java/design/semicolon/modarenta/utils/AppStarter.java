package design.semicolon.modarenta.utils;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class AppStarter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // https://parse.com/docs/android/guide#local-datastore
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);

        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }

}