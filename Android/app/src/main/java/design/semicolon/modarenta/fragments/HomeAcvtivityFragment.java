package design.semicolon.modarenta.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import design.semicolon.modarenta.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeAcvtivityFragment extends Fragment {

    public HomeAcvtivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_acvtivity, container, false);
    }
}
