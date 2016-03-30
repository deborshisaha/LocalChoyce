package fashiome.android.v2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.User;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;
import fashiome.android.v2.adapters.UserProfileTabsAdapter;

import static fashiome.android.utils.Utils.getScreenWidthInDp;

public class UserProfileFragment extends Fragment {

    @Bind(R.id.rivImg)
    RoundedImageView rivProfilePicture;

    @Bind(R.id.tvUserFullname)
    TextView tvUserFullname;

    @Bind(R.id.tvUserCity)
    TextView tvUserCity;

    @Bind(R.id.pstsUserProfile)
    PagerSlidingTabStrip pstsUserProfile;

    @Bind(R.id.vpUserProfile)
    ViewPager vpUserProfile;

    private User user;

    public static UserProfileFragment newInstance(User user) {
        UserProfileFragment userProfileFragment = new UserProfileFragment();

        if (user != null) {
            Bundle args = new Bundle();
            args.putParcelable(User.USER_KEY, user);
            userProfileFragment.setArguments(args);
        }

        return userProfileFragment;
    }

    public UserProfileFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        user = (User)getArguments().getParcelable(User.USER_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user_profile_v2, container, false);

        ButterKnife.bind(this, view);

        setupHeader();

        if (user != null) {
            vpUserProfile.setAdapter(new UserProfileTabsAdapter(getActivity().getSupportFragmentManager(), user));
            pstsUserProfile.setViewPager(vpUserProfile);
        }

        return view;
    }

    public void setupHeader(){

        String URLString = null;

        if (user != null) {
            URLString = ImageURLGenerator.getInstance(getActivity()).URLForFBProfilePicture(user.getFacebookId(), getScreenWidthInDp(getActivity()));
            tvUserFullname.setText(user.getUsername());
            tvUserCity.setText("San Francisco, CA");
            //Utils.setRating(starLinearLayout, user.getRating(), this);
        }

        Log.d("DEBUG", URLString);

        if (URLString != null || URLString.length() > 0) {
            Glide.with(this).load(URLString).into(rivProfilePicture);
        }

    }
}
