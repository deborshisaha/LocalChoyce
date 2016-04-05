package fashiome.android.v2.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uber.sdk.android.rides.RequestButton;
import com.uber.sdk.android.rides.RideParameters;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import fashiome.android.R;
import fashiome.android.models.Product;

public class CheckoutActivity extends AppCompatActivity {
    private static final String DROPOFF_ADDR = "One Embarcadero Center, San Francisco";
    private static final float DROPOFF_LAT = 37.795079f;
    private static final float DROPOFF_LONG = -122.397805f;
    private static final String DROPOFF_NICK = "Embarcadero";
    private static final String PICKUP_ADDR = "1455 Market Street, San Francisco";
    private static final float PICKUP_LAT = 37.775304f;
    private static final float PICKUP_LONG = -122.417522f;
    private static final String PICKUP_NICK = "Uber HQ";
    private static final String UBERX_PRODUCT_ID = "a1111c8c-c720-46c3-8534-2fcdd730040d";

    @Bind(R.id.tvAmount)
    TextView mAmount;

    @Bind(R.id.tvDays)
    TextView mDays;

    @Bind(R.id.tvQty)
    TextView mQty;

    @Bind(R.id.tvProductTitle)
    TextView mTitle;

    @Bind(R.id.bDone)
    Button mDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Product p = getIntent().getParcelableExtra("product");
        int finalAmount = getIntent().getIntExtra("finalAmount", 0);
        int quantity = getIntent().getIntExtra("quantity", 0);
        int numberOfDays = getIntent().getIntExtra("numberOfDays", 0);


        Log.i("info",p.getProductName());

        mDays.setText(String.valueOf(numberOfDays));
        mQty.setText(String.valueOf(quantity));
        mAmount.setText("$"+String.valueOf(finalAmount));
        mTitle.setText(p.getProductName());

        RequestButton uberButtonBlack = (RequestButton) findViewById(R.id.uber_button_black);
        uberButtonBlack.setText("");

        RideParameters rideParameters = new RideParameters.Builder()
                .setProductId(UBERX_PRODUCT_ID)
                //.setPickupLocation(PICKUP_LAT, PICKUP_LONG, PICKUP_NICK, PICKUP_ADDR)
                .setPickupLocation((float)p.getAddress().getLatitude(), (float) p.getAddress().getLongitude(),null ,null)
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
                .build();

        uberButtonBlack.setRideParameters(rideParameters);

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    
}
