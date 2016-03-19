package fashiome.android.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Product;

public class ProductRentDetailsFragment extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{


    Calendar myCalendar = Calendar.getInstance();

    public Context context;

    public Product product;

    private int finalAmount = 0;

    SimpleDateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);


    public interface ProductRentDetailsDialogListener {
        void onSavingRentDetails(int amount);
    }

    public ProductRentDetailsDialogListener listener;


    @Bind(R.id.currentDate) EditText mEditText;
    @Bind(R.id.saveButton) Button saveSettings;
    @Bind(R.id.spinnerSortOrder) Spinner sortOrder;
    @Bind(R.id.numDays) EditText mNumdays;
    @Bind(R.id.etQuantityNum) EditText mQuantity;
    @Bind(R.id.tvTotal) TextView mTotal;

    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.currentDate:

                new DatePickerDialog(context, this,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;

            case R.id.saveButton:
                saveRentDetails();
                break;

        }

    }

    private void saveRentDetails() {
        listener.onSavingRentDetails(finalAmount);
        dismiss();
    }

    public ProductRentDetailsFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ProductRentDetailsFragment newInstance(Product p) {
        ProductRentDetailsFragment frag = new ProductRentDetailsFragment();
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
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
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

        View view = inflater.inflate(R.layout.fragment_rent_details, container);

        product = getArguments().getParcelable("product");

        //title = getArguments().getString("title");

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.sizeEnum, android.R.layout.simple_spinner_dropdown_item);

        String title = product.getProductName() + " $"+ String.valueOf((int)product.getPrice()) + "/day";
        getDialog().setTitle(title);

        mTotal.setVisibility(View.INVISIBLE);

        mEditText.setText(displayFormat.format(myCalendar.getTime()));

        Log.i("info"," "+product.getNumberOfFavorites()+product.getNumberOfReviews()+product.getProductName()+product.getObjectId()+product.getPrice());

        mQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateFinalAmount();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sortOrder.setAdapter(adapter);
        mEditText.setOnClickListener(this);
        saveSettings.setOnClickListener(this);

    }

    public void calculateFinalAmount(){

        int quantity, numDays, productPrice;

        if(mQuantity.getText().length() > 0 && !mQuantity.getText().equals("")) {
            quantity = Integer.parseInt(mQuantity.getText().toString());
        } else quantity = 0;

        if(mNumdays.getText().length() > 0 && !mNumdays.getText().equals("")) {

            numDays = Integer.parseInt(mNumdays.getText().toString());
        } else numDays = 0;


        if(mQuantity.getText().length() > 0 && !mQuantity.getText().equals("")) {
            Log.i("info","Product price "+String.valueOf(product.getPrice()));
            productPrice = (int) product.getPrice();
            finalAmount = quantity * numDays * productPrice;
        } else finalAmount = 0;

        String displayAmount = "Total $"+String.valueOf(finalAmount);
        mTotal.setText(displayAmount);
        mTotal.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDate();

    }

    private void updateDate() {

        //String reqdDate = requiredFormat.format(myCalendar.getTime());
        String displayDate = displayFormat.format(myCalendar.getTime());
        mEditText.setText(displayDate);
    }

}