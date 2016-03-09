package fashiome.android.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;
import com.desmond.squarecamera.CameraActivity;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Product;
import fashiome.android.models.User;

/**
 * Created by dsaha on 3/8/16.
 */
public class ProductFormActivity extends AppCompatActivity {

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
            }
        };

        rivProductPrimaryImage.setOnClickListener(imageOnClickListener);
        rivProductSecondaryImage.setOnClickListener(imageOnClickListener);

        rivProductSecondaryImage.setVisibility(View.GONE);

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
                                int i=0;
                                for (Bitmap bmp:arrayOfBitmaps) {
                                    Log.d("DEBUG", "image>>>>"+product.getObjectId()+getPhotoCloudinaryPublicIdList().get(i));
                                    cloudinaryObject.uploader().upload(getInputStream(bmp), ObjectUtils.asMap("public_id", product.getObjectId()+getPhotoCloudinaryPublicIdList().get(i)));
                                    i++;
                                }

                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            return null;
                        }
                    };

                    task.execute();

                } else {
                    Log.d("DEBUG", "Cause: " + e.getCause());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        Uri takenPhotoUri = null;

        if (requestCode == REQUEST_CAMERA) {
            takenPhotoUri = data.getData();
        }

        Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
        addImage(takenImage);
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
