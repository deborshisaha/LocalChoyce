package fashiome.android.v2.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import fashiome.android.R;
import fashiome.android.v2.activities.ProductFormActivity;

/**
 * Created by dsaha on 3/29/16.
 */
public class SellerFragment extends Fragment {

    FloatingActionButton fabAddProduct;

    private boolean fabInExplodedState = false;

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

                fabAddProduct.animate().scaleX(100).scaleY(100).setInterpolator(new AccelerateInterpolator()).setDuration(200).setStartDelay(300).setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation){
                        fabAddProduct.setImageDrawable(null);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent(getActivity(), ProductFormActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out_fast);
                        fabInExplodedState = true;
                    }

                }).start();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (fabInExplodedState) {

            fabAddProduct.animate().scaleX(1).scaleY(1).setInterpolator(new DecelerateInterpolator()).setDuration(400).setStartDelay(300).setListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    fabInExplodedState = false;
                    fabAddProduct.setImageResource(R.drawable.ic_add_tag);
                }

            }).start();
        }
    }
}