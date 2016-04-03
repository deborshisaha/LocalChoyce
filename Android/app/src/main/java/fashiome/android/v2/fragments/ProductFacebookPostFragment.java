package fashiome.android.v2.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.activities.HomeActivity;
import fashiome.android.models.Product;
import fashiome.android.utils.ImageURLGenerator;
import fashiome.android.utils.Utils;

public class ProductFacebookPostFragment extends DialogFragment implements View.OnClickListener {

    public Context context;
    public Product product;
    public Bitmap facebookBitmap;
    KProgressHUD hud = null;

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

    public interface ProductFacebookPostDialogListener {
        void onPostFinished();
    }

    private PendingAction pendingAction = PendingAction.NONE;
    public ProductFacebookPostDialogListener listener;
    private static final String PERMISSION = "publish_actions";
    private static final String TAG = "ProductFacebookPostFrag";

    @Bind(R.id.etPostText)
    EditText mPostText;
    @Bind(R.id.btnPost)
    Button mPost;
    @Bind(R.id.btnCancel)
    Button mCancel;
    @Bind(R.id.ivPostImage)
    ImageView mPostImage;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnCancel:

                break;

            case R.id.btnPost:

                hud = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setMaxProgress(101);
                hud.setLabel("Sharing on Facebook");
                hud.show();
                postPhoto();
                break;

        }

    }

    public ProductFacebookPostFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ProductFacebookPostFragment newInstance(Product p) {
        ProductFacebookPostFragment frag = new ProductFacebookPostFragment();
        Bundle args = new Bundle();
        args.putParcelable("product", p);
        frag.setArguments(args);
        return frag;
    }

    // Assign the listener implementing events interface that will receive the events
    //public void setCustomObjectListener(NewTweetDialogListener listener) {
    //    this.listener = listener;
    //}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent

        //params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //params.height = WindowManager.LayoutParams.MATCH_PARENT;

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        //WindowManager.LayoutParams lp = myDialog.getWindow().getAttributes();
        //lp.dimAmount = 0.7f        // Call super onResume after sizing
        super.onResume();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        //dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.Dialog);

        View view = inflater.inflate(R.layout.fragment_product_facebook_post, container);

        product = getArguments().getParcelable("product");

        //title = getArguments().getString("title");

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = product.getProductName() + " $" + String.valueOf((int) product.getPrice()) + "/day";
        getDialog().setTitle(title);

        String URLString = ImageURLGenerator.getInstance(getActivity()).URLForImageWithCloudinaryPublicId(product.getImageCloudinaryPublicId(0), Utils.getScreenWidthInDp(getActivity()));

        Log.i(TAG,"Bitmap URL "+URLString);

        Glide.with(getActivity())
                .load(URLString)
                .into(mPostImage);

        Log.i("info", " " + product.getNumberOfFavorites() + product.getNumberOfReviews() + product.getProductName() + product.getObjectId() + product.getPrice());

        mPostText.setText(buildPostText());
        mPostText.setOnClickListener(this);
        mPost.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    public String buildPostText(){

        StringBuilder post = new StringBuilder();
        post.append("Style it with Panache! Check out my newly designed ");
        post.append(product.getProductName());
        post.append(" Now you can rent it for $");
        post.append((int)product.getPrice());
        post.append(" only!");
        return post.toString();
    }

    private void postPhoto() {
        //Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.id.ivPostImage);
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(facebookBitmap)
                .setCaption(buildPostText())
                .build();

        ArrayList<SharePhoto> photos = new ArrayList<>();
        photos.add(sharePhoto);
        SharePhotoContent sharePhotoContent =
                new SharePhotoContent.Builder()
                        .setPhotos(photos)
                        .build();

//        if (hasPublishPermission()) {
        ShareApi.share(sharePhotoContent, shareCallback);
/*        } else {
            pendingAction = PendingAction.POST_PHOTO;
            // We need to get new permissions, then complete the action when we get called back.
            LoginManager.getInstance().logInWithPublishPermissions(
                    this,
                    Arrays.asList(PERMISSION));
        }*/
    }

    public void informActivityAndFinish(){

        listener.onPostFinished();
        dismiss();
    }

    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
            informActivityAndFinish();
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            hud.dismiss();
            informActivityAndFinish();

            //showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                Log.i("info", alertMessage);
                //showResult(title, alertMessage);
            }
            informActivityAndFinish();
        }
    };

}