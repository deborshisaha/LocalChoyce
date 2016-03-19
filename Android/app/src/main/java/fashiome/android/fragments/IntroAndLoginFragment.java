package fashiome.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import fashiome.android.*;

/**
 * Created by dsaha on 3/18/16.
 */
public class IntroAndLoginFragment extends Fragment {
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private static final String ARG_IMAGE_RES_ID = "imageResourceId";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "description";
    private static final String ARG_BTN_TITLE = "buttonTitle";


    public static IntroAndLoginFragment newInstance(int layoutResId, int imageResourceId, String title, String description, String btnTitle) {
        IntroAndLoginFragment sampleSlide = new IntroAndLoginFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        args.putInt(ARG_IMAGE_RES_ID, imageResourceId);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, description);
        args.putString(ARG_BTN_TITLE, btnTitle);


        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    private int layoutResId;
    private int imageResourceId;

    private String mTitle;
    private String mDescription;
    private String mButtonTitle;

    public IntroAndLoginFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID) && getArguments().containsKey(ARG_IMAGE_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
            imageResourceId = getArguments().getInt(ARG_IMAGE_RES_ID);

            mTitle = getArguments().getString(ARG_TITLE);
            mDescription = getArguments().getString(ARG_DESC);
            mButtonTitle = getArguments().getString(ARG_BTN_TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layoutResId, container, false);

        ImageView ivOlNewIntroImageHolder = (ImageView) v.findViewById(R.id.ivOlNewIntroImageHolder);
        ivOlNewIntroImageHolder.setImageResource(imageResourceId);

        TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);
        Button button = (Button) v.findViewById(R.id.btnAction);

        if (mTitle == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(mTitle);
        }

        if (mDescription == null) {
            tvDescription.setVisibility(View.GONE);
        } else {
            tvDescription.setText(mDescription);
        }

        if (mButtonTitle == null){
            button.setVisibility(View.GONE);
        } else {
            button.setText(mButtonTitle);
        }

        return v;
    }
}
