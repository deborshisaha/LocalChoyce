package fashiome.android.v2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fashiome.android.R;
import fashiome.android.v2.activities.ProductFormActivity;

/**
 * Created by dsaha on 3/29/16.
 */
public class SellerFragment extends Fragment {

    //    @Bind(R.id.fabAddProduct)
    FloatingActionButton fabAddProduct;

    public SellerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_seller_v2, container, false);

        fabAddProduct = (FloatingActionButton) view.findViewById(R.id.fabAddProduct);

        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity(), ProductFormActivity.class );
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        return view;
    }
}