package fashiome.android.v2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uber.sdk.android.rides.RequestButton;
import com.uber.sdk.android.rides.RideParameters;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Product;

public class CheckoutActivity extends AppCompatActivity {
    private static final String DROPOFF_ADDR = "One Embarcadero Center, San Francisco";
    private static final float DROPOFF_LAT = 37.795079f;
    private static final float DROPOFF_LONG = -122.397805f;
    private static final String DROPOFF_NICK = "Embarcadero";
    private static final String UBERX_PRODUCT_ID = "a1111c8c-c720-46c3-8534-2fcdd730040d";

    @Bind(R.id.tvAmount)
    TextView tvAmount;

    @Bind(R.id.tvProductTitle)
    TextView tvProductTitle;

    @Bind(R.id.bDone)
    Button mDone;

    @Bind(R.id.tvWalkDirectionTimeEstimate)
    TextView tvWalkDirectionTimeEstimate;

    @Bind(R.id.tvCarDirectionTimeEstimate)
    TextView tvCarDirectionTimeEstimate;

    @Bind(R.id.tvBusDirectionTimeEstimate)
    TextView tvBusDirectionTimeEstimate;

    @Bind(R.id.tvUberTime)
    TextView tvUberTime;

    @Bind(R.id.tvUberPrice)
    TextView tvUberPrice;

    @Bind(R.id.tvDueDate)
    TextView tvDueDate;

    @Bind(R.id.tvOrderNumber)
    TextView tvOrderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkout_v2);

        ButterKnife.bind(this);

        Product p = getIntent().getParcelableExtra("product");

        int orderAmount = getIntent().getIntExtra("orderAmount", 0);
        int quantity = getIntent().getIntExtra("quantity", 0);
        int numberOfDays = getIntent().getIntExtra("numberOfDays", 0);
        String dueDate = getIntent().getStringExtra("dueDate");
        String orderNumber = getIntent().getStringExtra("orderNumber");


        tvWalkDirectionTimeEstimate.setText("4 mins");
        tvCarDirectionTimeEstimate.setText("5 mins");
        tvBusDirectionTimeEstimate.setText("6 mins");

        tvUberTime.setText("13 mins");
        tvUberPrice.setText("$9 - $16");

        tvDueDate.setText(dueDate);
        tvOrderNumber.setText(orderNumber);
        tvProductTitle.setText(p.getProductName());

        tvAmount.setText("USD "+orderAmount);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");


//        int finalAmount = getIntent().getIntExtra("finalAmount", 0);
//        int quantity = getIntent().getIntExtra("quantity", 0);
//        int numberOfDays = getIntent().getIntExtra("numberOfDays", 0);



//        mDays.setText(String.valueOf(numberOfDays));
//        mQty.setText(String.valueOf(quantity));
//        mAmount.setText("$"+String.valueOf(finalAmount));
        //mTitle.setText(p.getProductName());

        // mTitle.setText("Valentino's red dress");
//        RequestButton uberButtonBlack = (RequestButton) findViewById(R.id.uber_button_black);
//        RequestButton uberButtonBlack = (RequestButton) findViewById(R.id.uber_button_black);
//        uberButtonBlack.setText("");
//
//        RideParameters rideParameters = new RideParameters.Builder()
//                .setProductId(UBERX_PRODUCT_ID)
//                .setPickupLocation((float)p.getAddress().getLatitude(), (float) p.getAddress().getLongitude(),null ,null)
//                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
//                .build();
//
//        rideParameters.
//
//        uberButtonBlack.setRideParameters(rideParameters);
//
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    
}
