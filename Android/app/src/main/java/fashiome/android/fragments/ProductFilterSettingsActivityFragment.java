package fashiome.android.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fashiome.android.R;

/**
 * A placeholder fragment containing a simple view for filter settings
 */
public class ProductFilterSettingsActivityFragment extends Fragment {

    public ProductFilterSettingsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_filter_settings, container, false);
    }
}
