package fashiome.android.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fashiome.android.R;
import fashiome.android.models.User;

public class LoginActivity extends AppCompatActivity {


    CircleImageView mProfileImage;
    Button mBtnFb;
    User parseUser;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnFb = (Button) findViewById(R.id.b_fb_login);
        mProfileImage = (CircleImageView) findViewById(R.id.ivProfileImage);


        //  Use this to output your Facebook Key Hash to Logs
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "fashiome.android",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }

        mBtnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, mPermissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {

                        if (user == null) {
                            Log.d("AppStarter", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("AppStarter", "User signed up and logged in through Facebook!");
                            associateInstallationWithUser();
                            getUserDetailsFromFB();
                        } else {
                            associateInstallationWithUser();
                            Log.d("AppStarter", "User logged in through Facebook!");
                            finish();
                            //getUserDetailsFromParse();
                        }
                    }
                });

            }
        });
    }


    private void associateInstallationWithUser () {
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
    }

    private void saveNewUser(final User user) {
        parseUser = (User) ParseUser.getCurrentUser();
        parseUser.setUsername(user.getUsername());
        parseUser.setEmail(user.getEmail());
        parseUser.put("profilePictureUrl", user.getProfilePictureURL());

        //Finally save all the user details
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(LoginActivity.this, "New user:" + user.getUsername() + " Signed up", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }


    private void getUserDetailsFromFB() {

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name,picture");
        parameters.putString("type", "large");

        final User user = new User();

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        try {

                            Log.d("Response", response.getRawResponse());

                            user.setEmail(response.getJSONObject().getString("email"));
                            //mEmailID.setText(email);

                            user.setUsername(response.getJSONObject().getString("name"));
                            //mUsername.setText(name);

                            JSONObject picture = response.getJSONObject().getJSONObject("picture");
                            JSONObject data = picture.getJSONObject("data");

                            Log.i("info", "https://graph.facebook.com/" + AccessToken.getCurrentAccessToken().getUserId() + "/picture?type=large");

                            String pictureUrl = "https://graph.facebook.com/"+AccessToken.getCurrentAccessToken().getUserId()+"/picture?type=large";

                            user.setProfilePictureURL(pictureUrl);
                            user.setFacebookId(AccessToken.getCurrentAccessToken().getUserId());

                            saveNewUser(user);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    public void goCreateAccount(View view){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

}
