package fashiome.android.v2.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.desmond.squarecamera.CameraActivity;
import com.google.android.gms.maps.model.LatLng;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.v2.activities.ProductDetailsActivity;
import fashiome.android.fragments.ProductRentDetailsFragment;
import fashiome.android.models.Address;
import fashiome.android.models.Product;
import fashiome.android.models.User;
import fashiome.android.utils.Utils;
import fashiome.android.v2.adapters.ProductFormImageViewPagerAdapter;
import fashiome.android.v2.fragments.ProductFacebookPostFragment;

/**
 * Created by dsaha on 3/29/16.
 */
public class ProductFormActivity extends AppCompatActivity implements ProductFacebookPostFragment.ProductFacebookPostDialogListener {

    @Bind(R.id.llProductUploadContent)
    LinearLayout llProductUploadContent;

    @Bind(R.id.fabUploadProduct)
    FloatingActionButton fabUploadProduct;

    @Bind(R.id.ivCloseUploadWindow)
    ImageView ivCloseUploadWindow;

    @Bind(R.id.tvNumberOfImagesUploaded)
    TextView tvNumberOfImagesUploaded;

    @Bind(R.id.etProductName)
    EditText etProductName;

    @Bind(R.id.etProductDescription)
    EditText etProductDescription;

    @Bind(R.id.etProductPrice)
    EditText etProductPrice;

    @Bind(R.id.viewPagerProductImageHolder)
    ViewPager viewPagerProductImageHolder;

    @Bind(R.id.llAddImage)
    LinearLayout llAddImage;

    @Bind(R.id.spinnerSize)
    Spinner size;

    @Bind(R.id.spinnerGender)
    Spinner gender;

    private Handler delayHandler = null;
    private Runnable runnable = null;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 0;
    private ProductFormImageViewPagerAdapter productFormImageViewPagerAdapter;
    private Product product = new Product();
    Bitmap facebookBitmap;
    private ObjectAnimator beats;
    LocationManager mLocationManager;
    Location currentLocation;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_form_v2);

        ButterKnife.bind(this);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000L, 300.0f, mLocationListener);

        populateProductDefaults();


        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(ProductFormActivity.this,
                R.array.sizeEnum, android.R.layout.simple_spinner_dropdown_item);

        size.setAdapter(sizeAdapter);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(ProductFormActivity.this,
                R.array.genderEnum, android.R.layout.simple_spinner_dropdown_item);

        gender.setAdapter(genderAdapter);

        llAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });

        productFormImageViewPagerAdapter = new ProductFormImageViewPagerAdapter(this);

        viewPagerProductImageHolder.setAdapter(productFormImageViewPagerAdapter);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (shouldEnableUploadButton()) {
                    enableUpload();
                } else {
                    disableUpload();
                }
            }
        };

        etProductName.addTextChangedListener(textWatcher);
        etProductPrice.addTextChangedListener(textWatcher);
        etProductDescription.addTextChangedListener(textWatcher);
        ivCloseUploadWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent resultIntent = new Intent();
                // overridePendingTransition(R.anim.fade_in_fast, R.anim.fade_out_fast);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        setUpAnimations();

    }

    private void setUpAnimations() {
        setUpUploadButtonAnimation();

        llProductUploadContent.setTranslationY(500);
        llProductUploadContent.animate().translationY(0).setDuration(300).setInterpolator(new DecelerateInterpolator()).setStartDelay(100).start();

        llAddImage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse));
    }

    private void setUpUploadButtonAnimation() {

        if (beats == null) {
            beats = ObjectAnimator.ofFloat(fabUploadProduct, "translationY", -10f, 0f);
        }

        beats.setDuration(500);
        beats.setInterpolator(new BounceInterpolator());
        beats.setRepeatCount(ValueAnimator.INFINITE);
    }

    private void populateProductDefaults() {
        product.setProductRating(new Random().nextInt(4) + 1);
        product.setNumberOfFavorites(new Random().nextInt(500) + 100);
        product.setNumberOfReviews(new Random().nextInt(20) + 1);
        product.setNumberOfViews(new Random().nextInt(1000) + 100);
        product.setNumberOfRentals(new Random().nextInt(500) + 100);
    }

    private void enableUpload () {
        fabUploadProduct.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        fabUploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProduct();
            }
        });

        if (beats != null) {beats.start();}
    }

    private void disableUpload () {
        fabUploadProduct.setBackgroundTintList(getResources().getColorStateList(R.color.material_design_gray_background));
        fabUploadProduct.setOnClickListener(null);

        if (beats != null) {beats.pause();}
    }

    private boolean shouldEnableUploadButton() {

        if (etProductName.getText().length() == 0 ||
                etProductPrice.getText().length() == 0 ||
                etProductDescription.getText().length() == 0 ||
                productFormImageViewPagerAdapter.getCount() == 0) {
            return false;
        }
        return true;
    }

    private void uploadProduct() {

        if (beats != null) {beats.pause();}

        final Cloudinary cloudinaryObject = new Cloudinary(com.cloudinary.android.Utils.cloudinaryUrlFromContext(this));

        product.setProductName(etProductName.getText().toString());
        product.setProductDescription(etProductDescription.getText().toString());
        product.setPrice(Double.parseDouble(etProductPrice.getText().toString()));
        product.setCurrency("USD");
        product.setProductPostedBy((User) User.getCurrentUser());
        product.setProductSize(size.getSelectedItem().toString());
        product.setGender(gender.getSelectedItem().toString());
        LatLng point = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        product.setAddress(new Address(point));
        product.setPhotos(getPhotoCloudinaryPublicIdList());

        final KProgressHUD hud = KProgressHUD.create(ProductFormActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setMaxProgress(101);
        hud.setLabel("Saving your product");
        hud.show();

        // Remove Call back
        delayHandler.removeCallbacks(runnable);
        runnable = null;

        product.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {

                if (e == null) {

                    hud.setLabel("Uploading photos (0/" + productFormImageViewPagerAdapter.getCount()+")").setMaxProgress(100).setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE);

                    AsyncTask<String, Integer, String> task = new AsyncTask<String, Integer, String>() {

                        @Override
                        protected void onProgressUpdate(Integer... values) {
                            hud.setProgress(values[0]);

                            if (values[1] != values[2]) {
                                hud.setLabel("Uploading photos ("+values[1]+"/"+ values[2]+ ")");
                            } else {
                                hud.setLabel("Upload complete!!");
                            }
                        }

                        @Override
                        protected String doInBackground(String... params) {
                            try {
                                int i = 1;
                                facebookBitmap = productFormImageViewPagerAdapter.getBitmaps().get(0);
                                for (Bitmap bmp : productFormImageViewPagerAdapter.getBitmaps()) {
                                    cloudinaryObject.uploader().upload(getInputStream(bmp), ObjectUtils.asMap("public_id", product.getObjectId() + getPhotoCloudinaryPublicIdList().get(i - 1)));
                                    int percentage = (int)(((float)i/productFormImageViewPagerAdapter.getCount()) * 100);

                                    Integer[] arr = {percentage, i, productFormImageViewPagerAdapter.getCount()};
                                    publishProgress(arr);
                                    i++;
                                }

                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String s) {

                            if (runnable == null) {
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        hud.dismiss();
                                        overridePendingTransition(R.anim.stay, R.anim.slide_down);
                                        showPostToFacebookDialog(product);
                                        //finish();
                                    }
                                };
                            }

                            delayHandler.postDelayed(runnable, 500);
                        }
                    };

                    task.execute();

                } else {
                    if (beats != null) {beats.resume();}
                    Log.d("DEBUG", "Cause: " + e.getCause());
                }
            }
        });
    }

    private void launchCamera() {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    private void selectImage(final View v) {
        final CharSequence[] items = { "Take Photo", "Choose from Library" };

        // Instantiate
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductFormActivity.this, R.style.PanacheAlertDialogStyle);
        builder.setTitle("Add Photo").setIcon(R.drawable.ic_add_image);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    launchCamera();
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (resultCode != RESULT_OK) return;

        Bitmap takenImage = null;

        switch (requestCode) {

            case REQUEST_CAMERA: {
                Uri takenPhotoUri = data.getData();
                Bitmap bmp = BitmapFactory.decodeFile(takenPhotoUri.getPath());

                takenImage = reduceBitmapSize(bmp, viewPagerProductImageHolder.getHeight());

                break;
            }
            case SELECT_FILE:{

                String url = data.getData().toString();
                InputStream instream = null;
                if (url.startsWith("content://com.google.android.apps.photos.content")){
                    try {
                        instream = getContentResolver().openInputStream(Uri.parse(url));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                if (instream != null) {
                    Bitmap bmp = BitmapFactory.decodeStream(instream);
                    takenImage = reduceBitmapSize(bmp, viewPagerProductImageHolder.getHeight());
                }

                break;
            }
        }

        if (productFormImageViewPagerAdapter != null && takenImage != null) {

            productFormImageViewPagerAdapter.add(takenImage);

            if (productFormImageViewPagerAdapter.getCount() == 5) {
                llAddImage.clearAnimation();
                llAddImage.setEnabled(false);
            }

            tvNumberOfImagesUploaded.setText(String.valueOf(productFormImageViewPagerAdapter.getCount())+"/5");
            productFormImageViewPagerAdapter.notifyDataSetChanged();
        }

        if (shouldEnableUploadButton()) {
            enableUpload();
        } else {
            disableUpload();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (delayHandler == null) {
            delayHandler = new Handler();
        }

        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    viewPagerProductImageHolder.setCurrentItem(productFormImageViewPagerAdapter.getCount(), true);
                }
            };
        }

        delayHandler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delayHandler.removeCallbacks(runnable);
        runnable = null;
        productFormImageViewPagerAdapter.removeAll();
    }

    private Bitmap reduceBitmapSize (Bitmap bitmap, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float aspectRatio = ((float)width / height);
        float newWidth = aspectRatio * newHeight;

        if (newWidth < Utils.getScreenWidth(this)) {
            newWidth = Utils.getScreenWidth(this);
            newHeight = (int)((float)newWidth/aspectRatio);
        }

        float scaleHeight = ((float) newHeight) / height;
        float scaleWidth = ((float) newWidth) / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    private ArrayList<String> getPhotoCloudinaryPublicIdList() {

        ArrayList<String> arrayList = new ArrayList<String>();

        for (int i = 0; i < productFormImageViewPagerAdapter.getCount(); i++) {

            if (i == 0) {
                arrayList.add("_primary");
                continue;
            }

            arrayList.add("_" + i);
        }

        return arrayList;
    }

    private ByteArrayInputStream getInputStream(Bitmap bitmap) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bitmapdata);

        return inputStream;
    }

    private void showPostToFacebookDialog(Product product) {
        FragmentManager fm = getSupportFragmentManager();
        Log.i("info", "Price before: " + product.getPrice());
        ProductFacebookPostFragment productFacebookPostFragment = ProductFacebookPostFragment.newInstance(product);
        productFacebookPostFragment.context = this;
        productFacebookPostFragment.facebookBitmap = facebookBitmap;
        productFacebookPostFragment.listener = this;
        productFacebookPostFragment.show(fm, "fragment_facebook_post_details");
    }

    public void showPostSuccessfulDialog() {

        String message = "Congratulations!";

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ProductFormActivity.this);

        alertDialogBuilder
                .setTitle(message)
                .setCancelable(true)
                .setIcon(R.drawable.ic_purchase_complete)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onPostFinished() {
        Log.i("info", "finished the post");
        finish();
    }
}
