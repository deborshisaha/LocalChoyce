package fashiome.android.v2.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.desmond.squarecamera.CameraActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.utils.Utils;
import fashiome.android.v2.adapters.ProductFormImageViewPagerAdapter;

/**
 * Created by dsaha on 3/29/16.
 */
public class ProductFormActivity extends AppCompatActivity {

    @Bind(R.id.fabAddProductImage)
    FloatingActionButton fabAddProductImage;

    @Bind(R.id.etProductName)
    EditText etProductName;

    @Bind(R.id.etProductDescription)
    EditText etProductDescription;

    @Bind(R.id.etProductPrice)
    EditText etProductPrice;

    @Bind(R.id.viewPagerProductImageHolder)
    ViewPager viewPagerProductImageHolder;

    private Handler delayHandler = null;
    private Runnable runnable = null;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CAMERA = 0;
//    private List<Bitmap> arrayOfBitmaps = new ArrayList<Bitmap>();
    private ProductFormImageViewPagerAdapter productFormImageViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_form_v2);

        ButterKnife.bind(this);

        fabAddProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });

        productFormImageViewPagerAdapter = new ProductFormImageViewPagerAdapter(this);

        viewPagerProductImageHolder.setAdapter(productFormImageViewPagerAdapter);

//        etProductName.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                if (s.length() != 0)
//                    Field2.setText("");
//            }
//        });
//
//        etProductDescription.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                if (s.length() != 0)
//                    Field1.setText("");
//            }
//        });
//
//        etProductPrice.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                if (s.length() != 0)
//                    Field1.setText("");
//            }
//        });
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

        Uri takenPhotoUri = null;

        switch (requestCode) {

            case REQUEST_CAMERA: {
                takenPhotoUri = data.getData();
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());

                if (productFormImageViewPagerAdapter != null) {
                    productFormImageViewPagerAdapter.add(takenImage);
                    productFormImageViewPagerAdapter.notifyDataSetChanged();
                }

                break;
            }
            case SELECT_FILE:{

                String url = data.getData().toString();
                Bitmap takenImage = null;
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

                if (productFormImageViewPagerAdapter != null) {
                    productFormImageViewPagerAdapter.add(takenImage);
                    productFormImageViewPagerAdapter.notifyDataSetChanged();
                }
                break;
            }
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
}
