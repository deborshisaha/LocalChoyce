package fashiome.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.models.Product;
import fashiome.android.models.ProductReview;
import fashiome.android.models.SellerReview;
import fashiome.android.models.User;

public class WriteReviewFragment extends DialogFragment {

    @Bind(R.id.etReviewText)
    EditText etReviewText;

    private Context mContext;
    private Product mProduct;
    private User mSeller;

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public static WriteReviewFragment newInstance(Product product) {
        WriteReviewFragment frag = new WriteReviewFragment();

        Bundle args = new Bundle();
        args.putParcelable(Product.PRODUCT_KEY, product);
        frag.setArguments(args);

        return frag;
    }

    public static WriteReviewFragment newInstance(User user) {
        WriteReviewFragment frag = new WriteReviewFragment();

        Bundle args = new Bundle();
        args.putParcelable(User.USER_KEY, user);
        frag.setArguments(args);

        return frag;
    }

    public WriteReviewFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_write_review, container);

        ButterKnife.bind(this, view);

        mProduct = getArguments().getParcelable(Product.PRODUCT_KEY);

        mSeller = getArguments().getParcelable(User.USER_KEY);

        if (mProduct != null) {

        } else if (mSeller != null) {

        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void saveSellerReview(User seller, String reviewText, int rating) {

        SellerReview sellerReview = new SellerReview();

        sellerReview.setUser((User) ParseUser.getCurrentUser());
        sellerReview.setReviewText(reviewText);
        sellerReview.setRating(rating);
        sellerReview.setSeller(seller);

        sellerReview.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    dismiss();
                }
            }
        });
    }

    private void saveProductReview(Product product, String reviewText, int rating) {

        ProductReview productReview = new ProductReview();

        productReview.setUser((User)ParseUser.getCurrentUser());
        productReview.setReviewText(reviewText);
        productReview.setRating(rating);
        productReview.setProduct(product);

        productReview.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    dismiss();
                }
            }
        });
    }

}
