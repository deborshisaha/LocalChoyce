package fashiome.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;

/**
 * Created by dsaha on 3/18/16.
 */
public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (ParseUser.getCurrentUser() == null) {
            Intent launchIntent = new Intent(EntryActivity.this, IntroAndLoginActivity.class);
            launchIntent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, false);
            startActivity(launchIntent);
        } else {
            Intent launchIntent = new Intent(EntryActivity.this, HomeActivity.class);
            startActivity(launchIntent);
        }

        finish();
    }
}
