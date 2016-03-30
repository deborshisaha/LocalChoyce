package fashiome.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fashiome.android.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class OnboardingCategorySelectionFragment extends Fragment {

    public OnboardingCategorySelectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding_category_selection, container, false);
    }
}
