package fashiome.android.activities;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.github.paolorotolo.appintro.AppIntro;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fashiome.android.R;
import fashiome.android.fragments.IntroAndLoginFragment;
import fashiome.android.models.User;

public class IntroAndLoginActivity extends AppIntro {

    private boolean activityForLogin;
    private Button mFacebookButton;
    private User parseUser;
    private static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};

    public static String LAUNCH_FOR_LOGIN = "LAUNCH_FOR_LOGIN";

    @Override
    public void init(Bundle savedInstanceState) {

        activityForLogin = getIntent().getExtras().getBoolean(LAUNCH_FOR_LOGIN);

        IntroAndLoginFragment introFifthFragment = IntroAndLoginFragment.newInstance(R.layout.intro, R.drawable.intro5, stringFromResourceId(R.string.introTitle5), stringFromResourceId(R.string.introDesc5), "Connect with Facebook");
        IntroAndLoginFragment introFirstFragment = IntroAndLoginFragment.newInstance(R.layout.intro, R.drawable.intro1, stringFromResourceId(R.string.introTitle1), stringFromResourceId(R.string.introDesc1), null);
        IntroAndLoginFragment introSecondFragment = IntroAndLoginFragment.newInstance(R.layout.intro, R.drawable.intro2, stringFromResourceId(R.string.introTitle2), stringFromResourceId(R.string.introDesc2), null);
        IntroAndLoginFragment introThirdFragment = IntroAndLoginFragment.newInstance(R.layout.intro, R.drawable.intro3, stringFromResourceId(R.string.introTitle3), stringFromResourceId(R.string.introDesc3), null);
        IntroAndLoginFragment introForthFragment = IntroAndLoginFragment.newInstance(R.layout.intro, R.drawable.intro4, stringFromResourceId(R.string.introTitle4), stringFromResourceId(R.string.introDesc4), null);

        if (activityForLogin != true) {
            addSlide(introFirstFragment);
            addSlide(introSecondFragment);
            addSlide(introThirdFragment);
            addSlide(introForthFragment);
        } else {
            showSkipButton(false);
        }

        addSlide(introFifthFragment);
        introFifthFragment.setOnClicklistener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookLoginClicked();
            }
        });

        setFadeAnimation();

        setProgressButtonEnabled(false);

        setSeparatorColor(Color.parseColor("#00000000"));
    }

    private String stringFromResourceId (int resourceID) {
        return getResources().getString(resourceID);
    }

    @Override
    public void onSkipPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //finish();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {

    }

    @Override
    public void onSlideChanged() {

    }

    private void onFacebookLoginClicked () {
        Log.d("DEBUG", "onFacebookLoginClicked");

        ParseFacebookUtils.logInWithReadPermissionsInBackground(IntroAndLoginActivity.this, mPermissions, new LogInCallback() {
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
                    ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

                    List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

                    Log.i("info",taskList.get(0).topActivity.getClassName()+" "+this.getClass().getEnclosingClass().getName());

                    if (taskList.get(0).numActivities == 1 &&
                            taskList.get(0).topActivity.getClassName().equals(this.getClass().getEnclosingClass().getName())) {
                        startActivity(new Intent(IntroAndLoginActivity.this, MainActivity.class));
                    } else {
                        finish();
                    }
                }
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
        parseUser.setRating(new Random().nextInt(4) + 1);
        parseUser.setPhoneNumber("4802088619");
        //parseUser.put("skypeId","");

        //Finally save all the user details
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
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
                AccessToken.getCurrentAccessToken(), "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {

                    public void onCompleted(GraphResponse response) {

                        try {

                            user.setEmail(response.getJSONObject().getString("email"));
                            user.setUsername(response.getJSONObject().getString("name"));

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
}
