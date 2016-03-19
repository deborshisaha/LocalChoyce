package fashiome.android.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

import fashiome.android.R;
import fashiome.android.fragments.IntroAndLoginFragment;

public class IntroAndLoginActivity extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        
        addSlide(IntroAndLoginFragment.newInstance(R.layout.intro, R.drawable.intro3, stringFromResourceId(R.string.introTitle1), stringFromResourceId(R.string.introDesc1), null));
        addSlide(IntroAndLoginFragment.newInstance(R.layout.intro, R.drawable.intro2, stringFromResourceId(R.string.introTitle2), stringFromResourceId(R.string.introDesc2), null));
        addSlide(IntroAndLoginFragment.newInstance(R.layout.intro, R.drawable.intro3, stringFromResourceId(R.string.introTitle3), stringFromResourceId(R.string.introDesc3), null));
        addSlide(IntroAndLoginFragment.newInstance(R.layout.intro, R.drawable.intro3, stringFromResourceId(R.string.introTitle4), stringFromResourceId(R.string.introDesc4), "Connect with Facebook"));

        setFadeAnimation();

        showSkipButton(true);
        setProgressButtonEnabled(false);

        setSeparatorColor(Color.parseColor("#00000000"));
    }

    private String stringFromResourceId (int resourceID) {
        return getResources().getString(resourceID);
    }

    @Override
    public void onSkipPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
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
}
