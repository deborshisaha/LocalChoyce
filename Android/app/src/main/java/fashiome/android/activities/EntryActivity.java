package fashiome.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dsaha on 3/18/16.
 */
public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent launchIntent = new Intent(EntryActivity.this, IntroAndLoginActivity.class);
        startActivity(launchIntent);

        finish();
    }
}
