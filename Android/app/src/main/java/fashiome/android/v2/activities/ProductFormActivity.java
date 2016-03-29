package fashiome.android.v2.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.desmond.squarecamera.CameraActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
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

    private static final int REQUEST_CAMERA = 0;
    private List<Bitmap> arrayOfBitmaps = new ArrayList<Bitmap>();
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
                    //startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
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
        }
    }

}
