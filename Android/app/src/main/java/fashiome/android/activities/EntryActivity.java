package fashiome.android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.parse.ParseUser;

import bolts.AppLinks;
import fashiome.android.v2.activities.IntroAndLoginActivity;
import fashiome.android.v2.activities.PanacheHomeActivity;

/**
 * Created by dsaha on 3/18/16.
 */
public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }

        if (ParseUser.getCurrentUser() == null) {
            Intent launchIntent = new Intent(EntryActivity.this, IntroAndLoginActivity.class);
            launchIntent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, false);
            startActivity(launchIntent);
        } else {
            Intent launchIntent = new Intent(EntryActivity.this, PanacheHomeActivity.class);
            startActivity(launchIntent);
            finish();
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
