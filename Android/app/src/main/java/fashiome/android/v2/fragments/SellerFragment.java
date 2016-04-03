package fashiome.android.v2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import fashiome.android.R;
import fashiome.android.activities.IntroAndLoginActivity;
import fashiome.android.activities.MainActivity;
import fashiome.android.v2.activities.ProductFormActivity;

/**
 * Created by dsaha on 3/29/16.
 */
public class SellerFragment extends Fragment {

    FloatingActionButton fabAddProduct;

    final int FROM_FAB_TO_LOGIN = 300;

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
                Intent intent;
                if (ParseUser.getCurrentUser() == null) {
                    intent = new Intent(getActivity(), IntroAndLoginActivity.class);
                    intent.putExtra(IntroAndLoginActivity.LAUNCH_FOR_LOGIN, true);
                    startActivityForResult(intent, FROM_FAB_TO_LOGIN);
                } else {
                    startNewProductActivity();
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FROM_FAB_TO_LOGIN){
            if (ParseUser.getCurrentUser() != null) {
                startNewProductActivity();
            }
        }

    }

    public void startNewProductActivity(){
        Intent intent = new Intent( getActivity(), fashiome.android.v2.activities.ProductFormActivity.class );
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);

    }
}