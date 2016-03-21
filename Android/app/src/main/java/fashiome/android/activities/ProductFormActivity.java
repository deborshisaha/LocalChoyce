package fashiome.android.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;
import com.desmond.squarecamera.CameraActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
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
import fashiome.android.models.Product;
import fashiome.android.models.User;

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

    @Bind(R.id.spinnerSize)
    Spinner size;

    @Bind(R.id.spinnerGender)
    Spinner gender;


    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
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
        setContentView(R.layout.activity_add_new_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Post a product");

        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(ProductFormActivity.this,
                R.array.sizeEnum, android.R.layout.simple_spinner_dropdown_item);

        size.setAdapter(sizeAdapter);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(ProductFormActivity.this,
                R.array.genderEnum, android.R.layout.simple_spinner_dropdown_item);

        gender.setAdapter(genderAdapter);

        View.OnClickListener imageOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTapped = v.getId();
                selectImage(v);
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

            case SELECT_FILE: {

                Uri selectedImageUri = data.getData();
                String[] projection = { MediaStore.MediaColumns.DATA };
                CursorLoader cursorLoader = new CursorLoader(this,selectedImageUri, projection, null, null,
                        null);
                Cursor cursor =cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                addImage(bm);
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

        final ProgressDialog pd = new ProgressDialog(ProductFormActivity.this);
        pd.setMessage("Saving your product ...");
        pd.isIndeterminate();
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        final Cloudinary cloudinaryObject = new Cloudinary(Utils.cloudinaryUrlFromContext(this));

        product.setProductName(etProductName.getText().toString());
        product.setProductDescription(etProductDescription.getText().toString());
        product.setPrice(Double.parseDouble(etProductAskPrice.getText().toString()));
        product.setCurrency("USD");
        product.setProductPostedBy((User) User.getCurrentUser());
        product.setProductSize(size.getSelectedItem().toString());
        product.setGender(gender.getSelectedItem().toString());
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
                            pd.setMessage("Done!");
                            pd.dismiss();
                            Intent resultIntent = new Intent();
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

    private void selectImage(final View v) {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductFormActivity.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.drawable.ic_camera);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    launchCamera(v);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    /* this method is overridden to prevent the UP/BACK button_hollow from creating a new activity
    instead of showing the old activity */
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return null;
    }

}
