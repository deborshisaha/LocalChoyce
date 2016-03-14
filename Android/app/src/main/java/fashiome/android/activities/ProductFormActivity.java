package fashiome.android.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;
import com.desmond.squarecamera.CameraActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Address;
import fashiome.android.models.Item;
import fashiome.android.models.Product;

/**
 * Created by dsaha on 3/8/16.
 */
public class ProductFormActivity extends AppCompatActivity{

    private static final String TAG = "ProductFormActivity";
    @Bind(R.id.etProductName)
    EditText etProductName;

    @Bind(R.id.etProductDescription)
    EditText etProductDescription;

    @Bind(R.id.etProductAskPrice)
    EditText etProductAskPrice;

    @Bind(R.id.rivProductPrimaryImage)
    RoundedImageView rivProductPrimaryImage;

    @Bind(R.id.rivProductSecondaryImage)
    RoundedImageView rivProductSecondaryImage;

    private static final int REQUEST_CAMERA = 0;
    private int  imageTapped = -1;
    private List<Bitmap> arrayOfBitmaps = new ArrayList<Bitmap>();
    //private GoogleApiClient mGoogleApiClient;

    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 420;
    private static final int REQUEST_CODE_RESOLUTION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_form);

        ButterKnife.bind(this);

        View.OnClickListener imageOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTapped = v.getId();
                launchCamera(v);
//                Intent intent = new Intent(ProductFormActivity.this, GoogleDriveFilesListActivity.class);
//                startActivity(intent);
            }
        };

        rivProductPrimaryImage.setOnClickListener(imageOnClickListener);
        rivProductSecondaryImage.setOnClickListener(imageOnClickListener);

        rivProductSecondaryImage.setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (resultCode != RESULT_OK) return;

        Uri takenPhotoUri = null;

        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                //mGoogleApiClient.connect();
                break;
            case REQUEST_CAMERA:{
                takenPhotoUri = data.getData();
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                addImage(takenImage);
                break;
            }
        }
    }

    private void launchCamera(View v) {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    public void saveProduct(View view) {

        final Cloudinary cloudinaryObject = new Cloudinary(Utils.cloudinaryUrlFromContext(this));

        final Product product = new Product();
        product.setProductName(etProductName.getText().toString());
        product.setProductDescription(etProductDescription.getText().toString());
        product.setPrice(Double.parseDouble(etProductAskPrice.getText().toString()));
        product.setCurrency("USD");
        product.setAddress(new Address(Item.getRandomLocation(37.48167,-122.15559,5000)));
        //product.setProductPostedBy((User) User.getCurrentUser());
        product.setPhotos(getPhotoCloudinaryPublicIdList());
        product.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
                        @Override
                        protected String doInBackground(String... params) {
                            try {
                                int i = 0;
                                for (Bitmap bmp : arrayOfBitmaps) {
                                    Log.d("DEBUG", "image>>>>" + product.getObjectId() + getPhotoCloudinaryPublicIdList().get(i));
                                    cloudinaryObject.uploader().upload(getInputStream(bmp), ObjectUtils.asMap("public_id", product.getObjectId() + getPhotoCloudinaryPublicIdList().get(i)));
                                    i++;
                                }

                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            Toast.makeText(ProductFormActivity.this, "Wohoo!", Toast.LENGTH_LONG).show();
                            Intent resultIntent  = new Intent(ProductFormActivity.this,HomeActivity.class);
                            resultIntent.putExtra("product",product);
                            setResult(100,resultIntent);
                            finish();
                        }
                    };

                    task.execute();

                } else {
                    Log.d("DEBUG", "Cause: " + e.getCause());
                }
            }
        });
    }

    private void addImage(Bitmap takenImage) {

        if (addImageToArray(takenImage)) {
            for (int i=0; i < arrayOfBitmaps.size(); i++) {
                Bitmap bmp = arrayOfBitmaps.get(i);
                if (i==0) {
                    rivProductPrimaryImage.setImageBitmap(bmp);
                    rivProductSecondaryImage.setVisibility(View.VISIBLE);
                } else {
                    rivProductSecondaryImage.setImageBitmap(bmp);
                }
            }
        }
    }

    private boolean addImageToArray(Bitmap takenImage) {
        if (arrayOfBitmaps == null) {
            arrayOfBitmaps = new ArrayList<Bitmap>();
        }

        if (arrayOfBitmaps.size() < 2 ) {
            arrayOfBitmaps.add(takenImage);
            return true;
        } else if (imageTapped == rivProductPrimaryImage.getId()) {
            arrayOfBitmaps.add(0, takenImage);
            arrayOfBitmaps.remove(1);
            return true;
        } else if (imageTapped == rivProductSecondaryImage.getId()) {
            arrayOfBitmaps.add(1, takenImage);
            arrayOfBitmaps.remove(2);
            return true;
        }

        return false;
    }

    private ArrayList<String> getPhotoCloudinaryPublicIdList() {

        ArrayList<String> arrayList = new ArrayList<String>();

        for (int i=0; i < arrayOfBitmaps.size(); i++) {

            if (i==0){
                arrayList.add("_primary");
                continue;
            }

            arrayList.add("_"+i);
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

}
