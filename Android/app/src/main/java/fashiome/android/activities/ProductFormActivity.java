package fashiome.android.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;
import com.desmond.squarecamera.CameraActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.ParseException;
import com.parse.ParseUser;
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
import permissions.dispatcher.NeedsPermission;

/**
 * Created by dsaha on 3/8/16.
 */
public class ProductFormActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

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

    @Bind(R.id.btnSaveProduct)
    Button btnSaveProduct;

    private static final int REQUEST_CAMERA = 0;
    private int imageTapped = -1;
    private List<Bitmap> arrayOfBitmaps = new ArrayList<Bitmap>();
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 420;
    private static final int REQUEST_CODE_RESOLUTION = 3;

    final private Product product = new Product();

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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (resultCode != RESULT_OK) return;

        Uri takenPhotoUri = null;

        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                //mGoogleApiClient.connect();
                break;
            case REQUEST_CAMERA: {
                takenPhotoUri = data.getData();
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                addImage(takenImage);
                break;
            }
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void launchCamera(View v) {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    private void saveProduct() {

        final Cloudinary cloudinaryObject = new Cloudinary(Utils.cloudinaryUrlFromContext(this));

        product.setProductName(etProductName.getText().toString());
        product.setProductDescription(etProductDescription.getText().toString());
        product.setPrice(Double.parseDouble(etProductAskPrice.getText().toString()));
        product.setCurrency("USD");
        //product.setAddress(new Address(Item.getRandomLocation(37.48167, -122.15559, 5000)));

        // cannot use User.getCurrentUser() because while uploading we do a new Product
        // so User.getCurrentUser() returns null
        product.setProductPostedBy(ParseUser.getCurrentUser());
        Log.i("info","User is there: "+product.getProductPostedBy().getUsername());
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
                            Intent resultIntent  = new Intent();
                            resultIntent.putExtra("product", product);
                            setResult(RESULT_OK, resultIntent);
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
            for (int i = 0; i < arrayOfBitmaps.size(); i++) {
                Bitmap bmp = arrayOfBitmaps.get(i);
                if (i == 0) {
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

        if (arrayOfBitmaps.size() < 2) {
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

        for (int i = 0; i < arrayOfBitmaps.size(); i++) {

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

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Location location = null;
        try {
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch(SecurityException e){
            e.printStackTrace();
        }
        if (location != null) {
            Toast.makeText(this, "GPS location was found! Lat:"+ location.getLatitude() +" Long:"+location.getLongitude(), Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            product.setAddress(new Address(latLng));
        } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
        startLocationUpdates();
    }

    protected void startLocationUpdates() throws SecurityException {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                createLocationRequest(), this);
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        return mLocationRequest;
    }

    @Override
    public void onConnectionSuspended(int i) {Log.i(TAG, "onConnectionSuspended");}

    @Override
    public void onLocationChanged(Location location) {

        LatLng latLng = null;

        if (location == null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            product.setAddress(new Address(latLng));
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");
    }
}
