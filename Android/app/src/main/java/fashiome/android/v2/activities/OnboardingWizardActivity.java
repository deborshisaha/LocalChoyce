package fashiome.android.v2.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.v2.classes.SearchCriteria;

public class OnboardingWizardActivity extends AppCompatActivity {

    @Bind(R.id.next)
    TextView mNext;

    @Bind(R.id.ivSelectedHer)
    ImageView ivSelectedHerIndicator;

    @Bind(R.id.ivSelectedHim)
    ImageView ivSelectedHimIndicator;

    @Bind(R.id.ivBannerImageOfHer)
    ImageView ivBannerImageOfHer;

    @Bind(R.id.ivBannerImageOfHim)
    ImageView ivBannerImageOfHim;

    @Bind(R.id.cvFemale)
    CardView cvFemale;

    @Bind(R.id.cvMale)
    CardView cvMale;

    @Bind(R.id.tvLookingFor)
    TextView tvLookingFor;

    @Bind(R.id.rlGenderPicker)
    RelativeLayout rlGenderPicker;

    int click = 0 ;

    final SearchCriteria searchCriteria = new SearchCriteria();

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding_wizard_v2);

        ButterKnife.bind(this);

        ivBannerImageOfHer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHim();
                selectHer();
                enableNext();
            }
        });

        ivBannerImageOfHim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHer();
                selectHim();
                enableNext();
            }
        });

        rlGenderPicker.animate().alpha(1).setDuration(600).setStartDelay(300);
    }

    private void enableNext() {
        mNext.setTextColor(getResources().getColor(R.color.blue));
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goto next screen
                AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent launchIntent = new Intent(OnboardingWizardActivity.this, ItemsInterestedInActivity.class);
                        launchIntent.putExtra(SearchCriteria.KEY, searchCriteria);
                        startActivity(launchIntent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }

                };

                if (searchCriteria.getGender() == SearchCriteria.Gender.GENDER_F) {
                    cvFemale.animate().alpha(0).setDuration(500).setStartDelay(200).setListener(animatorListenerAdapter);
                }  else {
                    cvMale.animate().alpha(0).setDuration(500).setStartDelay(200).setListener(animatorListenerAdapter);
                }
            }
        });
    }

    private void selectHer() {
        searchCriteria.setGender(SearchCriteria.Gender.GENDER_M);
        ivBannerImageOfHer.setImageDrawable(getDrawable(R.drawable.her));
        ivSelectedHerIndicator.setVisibility(View.VISIBLE);
    }

    private void clearHer() {
        ivBannerImageOfHer.setImageDrawable(getDrawable(R.drawable.her_blur));
        ivSelectedHerIndicator.setVisibility(View.GONE);
    }

    private void selectHim() {
        searchCriteria.setGender(SearchCriteria.Gender.GENDER_F);
        ivBannerImageOfHim.setImageDrawable(getDrawable(R.drawable.him));
        ivSelectedHimIndicator.setVisibility(View.VISIBLE);
    }

    private void clearHim() {
        ivBannerImageOfHim.setImageDrawable(getDrawable(R.drawable.him_blur));
        ivSelectedHimIndicator.setVisibility(View.GONE);
    }
}
