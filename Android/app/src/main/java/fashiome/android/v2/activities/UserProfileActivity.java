package fashiome.android.v2.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;

import fashiome.android.R;
import fashiome.android.models.User;
import fashiome.android.v2.fragments.UserProfileFragment;

/**
 * Created by dsaha on 4/3/16.
 */
public class UserProfileActivity extends AppCompatActivity {

    private User profileForUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_v2);

        if (getIntent().getExtras().getParcelable(User.USER_KEY) != null) {
            profileForUser = (User)getIntent().getExtras().getParcelable(User.USER_KEY);
        }

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.flContainer, UserProfileFragment.newInstance(profileForUser));
        ft.commit();
    }
}
